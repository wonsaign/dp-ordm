package com.zeusas.dp.ordm.task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.task.CronTask;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.active.bean.ActivityType;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.PeriodActivity;
import com.zeusas.dp.ordm.active.model.ProductItem;
import com.zeusas.dp.ordm.active.model.ProductRule;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Item;
import com.zeusas.dp.ordm.entity.MonthPresent;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.entity.PresentContext;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.MonthPresentManager;
import com.zeusas.dp.ordm.service.OrderDetailService;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.utils.OrdmConfig;
import com.zeusas.security.auth.service.AuthCenterManager;

public class MonthPresentTask extends CronTask {
	static final Logger logger = LoggerFactory.getLogger(MonthPresentTask.class);
	//<活动id，活动>
	private final Map<String,Activity> activities= new HashMap<>();
	//<单号，订单>
	private final Map<String,Order> ordersMap= new HashMap<>();
	//<柜台，已经使用的金额>
	private final Map<String,Double> usedAmt= new HashMap<>();
	//<柜台，已经使用的金额>
	private final Map<String,List<String>> counterActs= new HashMap<>();
	//<柜台，明细>
	private final Map<String,List<OrderDetail>> counterDetail= new HashMap<>();
	
	public MonthPresentTask() {
		getActivity();
	}

	@Override
	public void exec() throws Exception {
		try {
			//装配活动数据
			getActivity();
			//预处理订单 明细数据
			orderAndDetail();
			//把活动数据装配到月度物料中
			activityDate();
			//执行月度物料
			excute();
		} finally {
			close();
		}
	}

	@Override
	protected boolean ready() {
		return valid;
	}

	private void close() throws Exception {
	}
	
	/**
	 * 装配活动类型为41的活动数据
	 */
	protected void getActivity() {
		activities.clear();
		ActivityManager activityManager=AppContext.getBean(ActivityManager.class);
		List<Activity> acties =activityManager.findByType(ActivityType.TYPE_BIGPACAKGES);
		for (Activity activity : acties) {
			activities.put(activity.getActId(), activity);
		}
	}

	/**
	 * 订单与明细数据预处理
	 * @throws SQLException
	 */
	protected void orderAndDetail() throws SQLException{
		ordersMap.clear();
		counterDetail.clear();
		OrderService orderService=AppContext.getBean(OrderService.class);
		OrderDetailService orderDetailService=AppContext.getBean(OrderDetailService.class);
		//初步根据订单状态 支付时间过滤订单
		List<String> orderStatus =new ArrayList<>();
		orderStatus.add(Order.Status_DoPay);
		orderStatus.add(Order.status_LogisticsDelivery);
		orderStatus.add(Order.status_DoLogisticsDelivery);
		orderStatus.add(Order.status_WaitShip);
		
		//起始时间一样 结束时间不一样 先用当前时间 进一步筛选再用具体活动时间
	
		List<Order> orders= orderService.findPayOrders(orderStatus, getStartTime().getTime(), System.currentTimeMillis());
		Set<String> orderNoSet=new HashSet<>(orders.size()*4/3);
		List<Order> activityOrder=orderService.findActivityOrder();
		// 订单中已经使用的金额
		for (Order order : activityOrder) {
			String counterCode=order.getCounterCode();
			if(!usedAmt.containsKey(counterCode)){
				usedAmt.put(counterCode, new Double(0));
			}
			Double tmpAmt=new Double(0);
			for (String actid : order.getActivRecord()) {
				tmpAmt+= activities.get(actid).getContext().getAmount().getAmount();
			}
			tmpAmt+=usedAmt.get(counterCode);
			usedAmt.put(counterCode, tmpAmt);
		}
		//<订单号,柜台号> 用于装配<柜台,明细>或者<柜台,金额>
		Map<String, String> counterOrderMap=new HashMap<>();
		for (Order order : orders) {
			String orderNo=order.getOrderNo();
			String counterCode=order.getCounterCode();
			orderNoSet.add(orderNo);
			counterOrderMap.put(orderNo, counterCode);
			ordersMap.put(orderNo, order);
		}
		
		//活动条件产品集合
		Set<Integer> productId=getProductId();
		List<OrderDetail> details=orderDetailService.findByCreateTime(getStartTime(), new Date());
		//<单号,List<订单明细>>
		Map<String, List<OrderDetail>> detailMap= details.stream()//
			.filter(d->orderNoSet.contains(d.getOrderNo())&&productId.contains(d.getProductId()))//
			.collect(Collectors.groupingBy(OrderDetail::getOrderNo));
		//<柜台号,List<订单明细>>
		for (String orderNo : detailMap.keySet()) {
			String counterCode=counterOrderMap.get(orderNo);
			if(!counterDetail.containsKey(counterCode)){
				counterDetail.put(counterCode, new ArrayList<>());
			}
			counterDetail.get(counterCode).addAll(detailMap.get(orderNo));
		}
	}

	protected void activityDate() {
		CounterManager counterManager = AppContext.getBean(CounterManager.class);
		MonthPresentManager monthPresentManager = AppContext.getBean(MonthPresentManager.class);
		for (String counterCode : counterDetail.keySet()) {
			Counter counter = counterManager.getCounterByCode(counterCode);
			Double amt = getUsefulAmt(counter);
			if (amt == 0) {
				continue;
			}
			Map<String, Integer> suitActivity = getSuitActivity(amt,counter);
			if (suitActivity.isEmpty()) {
				continue;
			}
			//<柜台号,activRecord>
			if(!counterActs.containsKey(counterCode)){
				counterActs.put(counterCode, new ArrayList<>());
			}
			for (String actid : suitActivity.keySet()) {
				for (int i=0;i<suitActivity.get(actid) ; i++) {
					counterActs.get(counterCode).add(actid);
				}
			}
			MonthPresent monthPresent=initMonthPresent(suitActivity, counter);
			monthPresentManager.saveOrUpdate(monthPresent, "root");
		}

	}
	
	
	/**
	 * 月度物料定时推送
	 * @throws SQLException
	 */
	protected void excute() throws SQLException {
		MonthPresentManager monthPresentManager = AppContext.getBean(MonthPresentManager.class);
		OrderService orderService = AppContext.getBean(OrderService.class);
	
		Integer yearmonth = monthPresentManager.getyearmonth();
		Map<String, MonthPresent> presentMap = monthPresentManager.findByYearMonth(yearmonth);
	
		if(presentMap==null||presentMap.isEmpty()){
			return;
		}
		Collection<MonthPresent> presents = presentMap.values();
	
		for (MonthPresent monthPresent : presents) {
			String counterCode=monthPresent.getCounterCode();
			//系统制单
			Order order;
			if(counterActs.containsKey(counterCode)){
				order= monthPresentManager.excute(monthPresent,counterActs.get(counterCode));
			}else{
				order= monthPresentManager.excute(monthPresent);
			}
			//生成包裹
			if(order==null){
				continue;
			}
			List<OrderDetail> orderDetails=orderService.getOrderDetails(order.getOrderNo());
//			orderService.bulidPackage(order, orderDetails);
			orderService.bulidPackagePlus(order, orderDetails);
		}
	}

	/**
	 * 柜台获取可用金额
	 * @param counter
	 */
	private Double getUsefulAmt(Counter counter){
		Double amt=0.0;
		String counterCode=counter.getCounterCode();
		//获取这个柜台的活动
		List<Activity> acts=activities.values().stream()//
		.filter(e -> e.validCheck(counter))//
		.collect(Collectors.toList());
		
		if(acts.isEmpty()){
			return amt;
		}
		//定时任务执行时满足的活动的结束时间  0<当前时间-结束时间<=十分钟
		Long endTime=getEndTime(acts.get(0), counter).getTime();
		//根据定时支付时间来筛选订单明细 累计计算金额
		List<OrderDetail> details=counterDetail.get(counter.getCounterCode());
		Long startTime=getStartTime().getTime();
		for (OrderDetail orderDetail : details) {
			Order order=ordersMap.get(orderDetail.getOrderNo());
			Long rderPayTime=order.getOrderPayTime();
			if(rderPayTime>startTime&&rderPayTime<=endTime){
				amt+=orderDetail.getUnitPrice()*orderDetail.getQuantity();
			}
		}
		//累计金额-=已经使用的金额
		if(usedAmt.containsKey(counterCode)){
			amt-=usedAmt.get(counterCode);
		}
		return amt;
	}
	/**
	 * 根据金额获取活动已经对应数量
	 * @param usefulAmt
	 * @return
	 */
	//FIX ME:加上时效限制
	private Map<String, Integer> getSuitActivity(Double usefulAmt, Counter counter) {
		// <活动id,数量>
		Map<String, Integer> presentProduct = new HashMap<>();
		// <amt,actid>
		// 根据金额排降序
		Map<Double, String> levelMap = new TreeMap<Double, String>(new Comparator<Double>() {
			public int compare(Double obj1, Double obj2) {
				return obj2.compareTo(obj1);
			}
		});
		String warehouses = counter.getWarehouses();
		for (Activity activity : activities.values()) {
			if (!activity.validCheck(counter)) {
				continue;
			}
			
			//PeriodActivity 的结束时间 
			Integer endTime = 0;
			Integer now = TypeConverter.toInteger(DateTime.formatDate(DateTime.YYYYMMDD, new Date()));
			List<PeriodActivity> stocks = activity.getContext().getStocks();
			if (stocks != null && stocks.size() > 0) {
				for (PeriodActivity periodActivity : stocks) {
					Integer end = periodActivity.getTo();
					//大于结束时间小于一天
					if (periodActivity.validCheck(warehouses) && //
							now - end >= 0 && now - end < 1) {
						endTime=end;
						break;
					}
				}
			}

			if (endTime != 0) {
				Double amt = activity.getContext().getAmount().getAmount();
				levelMap.put(amt, activity.getActId());
			}

		}
		for (Double amt : levelMap.keySet()) {
			int num = (int) (usefulAmt / amt);
			// 不满足，下一档次
			if (num < 1) {
				continue;
			}
			for (int i = 0; i < num; i++) {
				String actId = levelMap.get(amt);
				if (!presentProduct.containsKey(actId)) {
					presentProduct.put(actId, 0);
				}
				presentProduct.put(actId, presentProduct.get(actId) + 1);
			}
			usefulAmt -= amt * num;
		}
		return presentProduct;
	}
	/**
	 * 构造月度物料实体
	 * @param suitActivity
	 * @param counter
	 * @return
	 */
	private MonthPresent initMonthPresent(Map<String, Integer> suitActivity, Counter counter) {
		AuthCenterManager authCenterManager = AppContext.getBean(AuthCenterManager.class);
		MonthPresent monthPresent = new MonthPresent(counter, authCenterManager.getAuthUser("root"));
		for (String actId : suitActivity.keySet()) {
			Activity activity = activities.get(actId);
			Integer suitNum = suitActivity.get(actId);

			PresentContext context = new PresentContext();
			context.setStartTime(activity.getStart());
			Date endTime = getEndTime(activity, counter);
			context.setEndTime(endTime);
			context.setStatus(false);
			context.setType(MonthPresent.TYPE_MARKETING_REPLENISHMENT);
			// 循环一次
			for (ProductRule productRule : activity.getContext().getActityExtras()) {
				for (ProductItem pItem : productRule.getProductItem().values()) {
					// FIXME:发货方式待确定
					Item item = new Item(pItem.getPid().toString()//
							, pItem.getQuantity() * suitNum//
							, pItem.getPrice()//
							, OrdmConfig.DELIVERYWAY_MARKETPRESENT);
					context.addItems(item);
				}
			}
			monthPresent.addContext(context);
		}
		return monthPresent;
	}

	/**
	 * 活动起始时间(多个活动最早的起始时间 用于数据初步赛选 不用于金额计算)
	 * @return
	 */
	private Date getStartTime(){
		Date startDate=new Date();
		for (Activity activity : activities.values()) {
			Date time=activity.getStart();
			if(time.getTime()<startDate.getTime()){
				startDate=time;
			}
		}
		return startDate;
	}
	/**
	 * 活动里仓库规则里的结束时间(匹配的第一个仓库规则的时间 理论只有一个)
	 * @param activity
	 * @param counter
	 * @return
	 */
	private Date getEndTime(Activity activity,Counter counter){
		Long now=new Date().getTime();
		Long endTime=new Long(0);
		List<PeriodActivity>  stocks=activity.getContext().getStocks();
		for (PeriodActivity periodActivity : stocks) {
			Long to=DateTime.toDate(DateTime.YYYYMMDD,periodActivity.getTo().toString()).getTime();
			Long dev=now-to;
			//FIXME:时间
//			if(dev>0&&dev<=10*60*1000//
			if(2*60*60*1000<dev&&dev<=2*60*60*1000+10*60*1000//
					&&periodActivity.getVal().contains(counter.getWarehouses())){
				endTime=to;
				break;
			}
		}
		return new Date(endTime);
	}
	/**
	 * 活动产品规则里产品集合
	 * @return
	 */
	private Set<Integer> getProductId(){
		Set<Integer> productId=new HashSet<>();
		for (Activity activity : activities.values()) {
			productId.addAll(activity.getContext().getActityGoods().getProductItem().keySet());
			break;
		}
		//XXX:如果正文不一样怎么处理 目前一个活动3个档次 产品集合是一样的
		return productId;
	}
}
