package com.zeusas.dp.ordm.active.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.zeusas.common.service.OnStartApplication;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.DateTime;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.active.bean.ActivityType;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.ActivityContext;
import com.zeusas.dp.ordm.active.model.ProductRule;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.active.service.ActivityService;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Item;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ReserveProduct;
import com.zeusas.dp.ordm.service.ProductManager;

/**
 * 活动启动时装入，在每次取有效活动时，检查是否在
 *
 */
@Service
public class ActivityManagerImpl extends OnStartApplication implements ActivityManager {

	static Logger logger = LoggerFactory.getLogger(ActivityManagerImpl.class);

	@Autowired
	private ActivityService activityService;

	// 所有活动的集合
	private final Map<String, Activity> activities;

	private long lastUpdate = 0;

	public ActivityManagerImpl() {
		activities = new ConcurrentHashMap<>();
	}

	public void reload() {
		ProductManager pman = AppContext.getBean(ProductManager.class);

		List<Activity> actives = activityService.findAll(new Date());

		// 清除 缓存
		activityService.clear();
		actives.stream().forEach(e -> {
			if (ActivityType.TYPE_PRENSENTOWNER.equals(e.getType())) {
				e.setPriority(100);
			}
			ActivityContext ctx = e.getContext();
			ProductRule actGood = ctx.getActityGoods();

			if (actGood != null) {
				buildProductItem(pman, ctx.getActityGoods());
			}

			ctx.getActityExtras().forEach(v -> {
				buildProductItem(pman, v);
			});
			activities.put(e.getActId(), e);
		});
	}

	private void buildProductItem(ProductManager pman, ProductRule pruler) {
		pruler.getProductItem().values().forEach(e -> {
			Product p;
			Integer pid = e.getPid();
			if (pid != null && (p = pman.get(pid)) != null) {
				e.setProduct(p);
			}
		});
	}

	@Override
	public boolean modifyActiveStatus(Activity activity, boolean flag) {
		Activity update = activities.get(activity.getActId());
		Assert.notNull(update, "更新对象不能为空");
		update.setStatus(flag ? Activity.S_OK : Activity.S_INVALID);
		update.setLastUpdate(System.currentTimeMillis());
		activityService.update(update);
		return true;
	}

	@Override
	public Activity get(String actId) {
		return Strings.isNullOrEmpty(actId) ? null : activities.get(actId);
	}

	@Override
	public void add(Activity activity) {
		activityService.add(activity);
	}

	@Override
	public Collection<Activity> values() {
		if (!DateTime.checkPeriod(Calendar.DATE, lastUpdate)) {
			reload();
			this.lastUpdate = System.currentTimeMillis();
		}
		return activities.values();
	}

	@Override
	public List<Activity> findAvaliable() {
		// 加载活动
		if (!DateTime.checkPeriod(Calendar.DATE, lastUpdate)) {
			reload();
			this.lastUpdate = System.currentTimeMillis();
		}

		return activities.values().parallelStream() //
				.filter(e -> (e.getStatus() == Activity.S_OK)) // 状态为有效的
				.filter(e -> e.dateCheck()) // 日期检查
				.collect(Collectors.toList());
	}

	@Override
	public boolean validateCart(List<CartDetail> details) {
		for (CartDetail cd : details) {
			if (!validateCart(cd)) {
				return false;
			}
		}
		return true;
	}

	private boolean validateCart(CartDetail detail) {
		String actId = detail.getActivityId();
		if (actId == null)
			return true;
		Activity activ = this.get(actId);
		if (activ == null) {
			return false;
		}
		ActivityContext ctx = activ.getContext();
		int goodNum = ctx.getActityGoods() != null ? ctx.getActityGoods().getProductItem().size()
				: 0;
		int presentNum = 0;
		if (!(ctx.getActityExtras().size() == 0)) {
			for (ProductRule rule : ctx.getActityExtras()) {
				presentNum += rule.getProductItem().size();
			}
		}
		switch (activ.getType()) {
		case ActivityType.TYPE_SUIT:
		case ActivityType.TYPE_BIGPACAKGE:
			return detail.getCartDetailsDesc().size() == (goodNum + presentNum);
		case ActivityType.TYPE_BUYGIVE:
			// TODO: 除以上两种活动类型外其他待校验
		case ActivityType.TYPE_BUYGIVES:
		case ActivityType.TYPE_PRENSENTOWNER:
		case ActivityType.TYPE_BIGPACAKGES:
			break;
		default:
			;
		}
		return true;
	}

	@Override
	public boolean validateCheck(String actId, //
			List<Item> actityGoods, //
			List<Item> actityExtra) {
		Activity activity = get(actId);
		// Step 1: 活动基本情况检查
		if (activity == null // 不存在
				|| Activity.S_OK != activity.getStatus()// 无效
				|| !activity.dateCheck()) {// 不在活动时间内
			return false;
		}
		// STEP 2: 这里是否需要校验赠品库存 送完为止？？
		List<Integer> pids = new ArrayList<>(actityExtra.size());
		actityExtra.forEach(e -> pids.add(Integer.parseInt(e.getId())));
		ActivityContext context = activity.getContext();
		// 判断加入购物车的产品集合是否都在该活动里面
		/* 大礼包特殊待遇 2017 -05 - 08 FIXIT BY wangs */
		if (!ActivityType.TYPE_BIGPACAKGE.equals(activity.getType())
				&& !context.checkActityExtras(pids)) {
			return false;
		}
		pids.clear();
		// STEP 3: 这里是否需要校验商品库存 送完为止？？
		actityGoods.forEach(e -> pids.add(Integer.parseInt(e.getId())));
		/* 大礼包特殊待遇 2017 -05 - 08 FIXIT BY wangs */
		if (!ActivityType.TYPE_BIGPACAKGE.equals(activity.getType())
				&& !context.getActityGoods().check(pids)) {
			return false;
		}
		// STEP 4: TODO 量的检查
		switch (activity.getType()) {
		case ActivityType.TYPE_BUYGIVE:
			// 4.1 活动类型为4的复杂形势
		case ActivityType.TYPE_BUYGIVES:
			boolean b = validate(activity, actityGoods, actityExtra);
			return b;
		default:
			break;
		}
		return true;
	}

	/**
	 * 校验买赠产品实例化是否满足活动
	 * 
	 * @param activity
	 * @param actityGoods
	 * @param actityExtra
	 * @return
	 */
	private boolean validate(Activity activity, List<Item> actityGoods, //
			List<Item> actityExtra) {
		// 所有购买产品总数
		int allGoodsNumber = 0;
		for (Item item : actityGoods) {
			allGoodsNumber += item.getNum();
		}
		// 取得活动
		List<ProductRule> rules = activity.getContext().getActityExtras();
		// 活动与产品数量的对应关系
		int[] extranNumber = new int[rules.size()];
		// 定义：产品对应活动位置的映射关系
		Map<Integer, Integer> pidToActive = new HashMap<>();
		for (int i = 0; i < rules.size(); i++) {
			// 活动对产品量清0
			extranNumber[i] = 0;
			// 取得对应规则
			ProductRule rule = rules.get(i);
			// 建立：产品对应活动位置的映射关系
			for (Integer pid : rule.getProductItem().keySet()) {
				pidToActive.put(pid, i);
			}
		}
		// 扫描赠品，放入对应的活动
		for (Item item : actityExtra) {
			int idx = pidToActive.get(Integer.parseInt(item.getId()));
			extranNumber[idx] += item.getNum();
		}
		int stdGoodNum = activity.getContext().getActityGoods().getQuantity();
		for (int i = 0; i < rules.size(); i++) {
			ProductRule rule = rules.get(i);
			int stdExtra = rule.getQuantity();
			// 如果 购4赠3，买6赠3 ＯＫ， 买8购5 不对，如果买8赠7 OＫ
			if (allGoodsNumber / stdGoodNum != extranNumber[i] / stdExtra) {
				return false;
			}
			// 如果 购4赠3，买6赠3 ＯＫ， 如果买8赠7 不通过！
			if (((double) allGoodsNumber) / stdGoodNum < ((double) extranNumber[i]) / stdExtra) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onStartLoad() {
		reload();
	}

	@Override
	public List<Activity> findByType(String type) {
		if (StringUtil.isEmpty(type)) {
			return new ArrayList<Activity>(0);
		}
		return findAvaliable().stream()//
				.filter(e -> e.getType().equals(type)) //
				.collect(Collectors.toList());
	}

	@Override
	public List<Activity> findMyActivities(Counter counter) {
		return findAvaliable().stream()//
				.filter(e -> e.validCheck(counter))//
				.collect(Collectors.toList());
	}

	@Override
	public List<Activity> findGlobal() {
		return findAvaliable().stream() //
				.filter(e -> e.getContext().checkIsGlobal())//
				.collect(Collectors.toList());
	}

	@Override
	public void changeReserveStatus(String actId, String wid, Integer status) {
		Activity activity = this.get(actId);
		activity.getContext().setOrAddStatus(wid, status);
		activityService.update(activity);
	}

	@Override
	public List<Activity> findMyRevActivity(Counter counter) {
		String wid = counter.getWarehouses();
		Assert.notNull(wid, "仓库不能为空");
		List<Activity> myRevActivitys = new ArrayList<>();
		for (Activity actv : findMyActivities(counter)) {
			ActivityContext context = actv.getContext();
			Integer status = context.getRevWmsStatus(wid);
			if (ReserveProduct.STATUS_RESERVABLE.intValue() == status.intValue()//
					|| ReserveProduct.STATUS_RESERVED.intValue() == status.intValue()) {
				myRevActivitys.add(actv);
			}
		}
		return myRevActivitys;
	}

	@Override
	public boolean isReservable(Counter counter, Integer productId) {
		for (Activity activity : this.findMyRevActivity(counter)) {

			ActivityContext context = activity.getContext();
			if (context.getRevStart() == null || context.getRevEnd() == null) {
				return false;
			}
			Long start = context.getRevStart().getTime();
			Long end = context.getRevEnd().getTime();
			Long now = System.currentTimeMillis();
			int status = context.getRevWmsStatus(counter.getWarehouses());
			Set<Integer> revProducts = context.getRevProducts();
			if (revProducts == null || revProducts.isEmpty()) {
				return false;
			}
			if (now > start && now <= end //
					&& ReserveProduct.STATUS_RESERVABLE.intValue() == status//
					&& revProducts.contains(productId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void changeToReserved(Integer productId, Counter counter) {
		String wid = counter.getWarehouses();
		Assert.notNull(wid, "更新打欠活动打欠状态，仓库不能为空");
		for (Activity activity : this.findMyRevActivity(counter)) {
			ActivityContext context = activity.getContext();
			Set<Integer> revProducts = context.getRevProducts();
			// 该活动无打欠设置跳过
			if (revProducts == null || revProducts.isEmpty()) {
				continue;
			}
			if (!revProducts.contains(productId)) {
				continue;
			}
			int mystatus = context.getRevWmsStatus(wid);
			if (mystatus == ReserveProduct.STATUS_RESERVABLE) {
				activity.getContext().setOrAddStatus(wid, ReserveProduct.STATUS_RESERVED);
				activityService.update(activity);
			}
		}
	}
}
