package com.zeusas.dp.ordm.service.impl;

import static com.zeusas.dp.ordm.entity.Product.TYPEID_PRODUCT;
import static com.zeusas.dp.ordm.service.CartDetailService.ID_CARTDETAIL;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MAXAMOUNT;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MAXPOSTAGE;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MIDPOSTAGE;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MINIAMOUNT;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_NOPOSTAGE;
import static com.zeusas.dp.ordm.utils.OrdmConfig.ORDMCONFIG;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.service.IdGenService;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.active.bean.ActivityType;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.ActivityContext;
import com.zeusas.dp.ordm.active.model.ProductItem;
import com.zeusas.dp.ordm.active.model.ProductRule;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.dao.CartDao;
import com.zeusas.dp.ordm.dao.CartDetailDao;
import com.zeusas.dp.ordm.dao.OrderDao;
import com.zeusas.dp.ordm.dao.OrderDetailDao;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.CartDetailDesc;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.CustomerPricePolicy;
import com.zeusas.dp.ordm.entity.DeliveryWay;
import com.zeusas.dp.ordm.entity.FixedPrice;
import com.zeusas.dp.ordm.entity.Item;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ReserveProduct;
import com.zeusas.dp.ordm.rev.entity.ReservedActivity;
import com.zeusas.dp.ordm.rev.entity.ReservedActivityContext;
import com.zeusas.dp.ordm.rev.service.ReservedActivityManager;
import com.zeusas.dp.ordm.service.CartDetailService;
import com.zeusas.dp.ordm.service.CartService;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.FixedPriceManager;
import com.zeusas.dp.ordm.service.PRPolicyManager;
import com.zeusas.dp.ordm.service.PricePolicyManager;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.ReserveProductManager;
import com.zeusas.dp.ordm.task.StorehousesSyncTask;
import com.zeusas.dp.ordm.utils.OrdmConfig;
import com.zeusas.security.auth.entity.AuthUser;
@Transactional
@Service
public class CartServiceImpl extends BasicService<Cart, Long> implements CartService {

	final static Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

	final static String LOCK_CARTID = "CARTID";
	@Autowired
	private CartDao cartDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderDetailDao detailDao;
	@Autowired
	private CartDetailDao cartDetailDao;
	@Autowired
	CartDetailService cartDetailService;
	@Autowired
	IdGenService idGens;
	@Autowired
	PRPolicyManager prPolicyManager;
	@Autowired
	ProductManager pm ;
	@Autowired
	FixedPriceManager fixedPriceManager;
	@Autowired
	CounterManager counterManager;
	@Autowired
	CustomerManager customerManager;
	@Autowired
	ActivityManager activityManager;
	@Autowired
	DictManager dictManager ;
	@Autowired
	PricePolicyManager policyManager;

	protected Dao<Cart, Long> getDao() {
		return cartDao;
	}

	@Transactional
	public Cart createCounterCart(Counter counter) throws ServiceException {
		final Cart cart = new Cart(counter);
		idGens.lock(LOCK_CARTID);
		try {
			cart.setCartId(Long.parseLong(idGens.generateDateId(LOCK_CARTID)));
			cartDao.save(cart);
		} catch (Exception e) {
			throw new ServiceException("创建购物车异常{}", e);
		} finally {
			idGens.unlock(LOCK_CARTID);
		}
		return cart;
	}

	@Transactional
	public Cart getCounterCart(Counter counter) throws ServiceException {
		try {
			Cart cart = cartDao.getCounterCart(counter);
			if (cart == null ) {
				// 如果用户购物车不存在 就给用户创建一个购物车
				return createCounterCart(counter);
			}
			if (Cart.STATUS_COMMIT.equals(cart.getStatus())) {
				return null;
			}
			// 点击获取购物车 购物车就从失效状态变成有效状态
			if (Cart.STATUS_UNACTIVE.equals(cart.getStatus())) {
				cart.setStatus(Cart.STATUS_ACTIVE);
			}
			return cart;
		} catch (DaoException e) {
			throw new ServiceException("获取购物车异常", e);
		}
	}

	@Transactional(readOnly=true)
	public List<Cart> findAllCart(List<Integer> counterIds) throws ServiceException {
		// 如果购物车里面没有该counter的数据，说明该门店购物车不存在
		try {
			return cartDao.findAllCart(counterIds);
		} catch (DaoException e) {
			throw new ServiceException("获取门店购物车异常", e);
		}
	}

	@Transactional(readOnly = true)
	public List<Cart> getCheckCarts(List<Integer> counterIds) throws ServiceException {
		try {
			return cartDao.getCheckCarts(counterIds);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Transactional(readOnly=true)
	public List<CartDetail> getCartDetailByCart(Cart cart) throws ServiceException {
		try {
			return cartDetailDao.getCartDetailByCart(cart);
		} catch (DaoException e) {
			throw new ServiceException("获取购物车明细异常!", e);
		}
	}

	@Override
	public List<CartDetailDesc> getCartDescByCartDetail(List<CartDetail> cartDetails)
			throws ServiceException {
		List<CartDetailDesc>descs=new ArrayList<>();
		for(CartDetail cd:cartDetails){
			descs.addAll(cd.getCartDetailsDesc());
		}
		return descs;
	}

	@Transactional
	@Override
	public void add(Cart cart, List<Item> items) throws ServiceException {
		for (Item item : items) {
			int productid;
			try {
				productid = Integer.parseInt(item.getId());
			} catch (Exception e) {
				throw new ServiceException(e);
			}
			int num = item.getNum();
			Product p = pm.get(productid);
			if (p == null) {
				continue;
			}
			add(cart, p, num);
		}
	}

	@Transactional
	public void add(Cart cart, Product product, int num) throws ServiceException {
		ReserveProductManager reserveProductManager = AppContext
				.getBean(ReserveProductManager.class);
		ReservedActivityManager reservedActivityManager = AppContext
				.getBean(ReservedActivityManager.class);

		Counter counter = counterManager.get(cart.getCounterId());
		Assert.notNull(counter, "柜台为空");
		Assert.notNull(counter.getWarehouses(), "仓库为空");

		try {
			String stockId = counter.getWarehouses();
			CartDetail cartDetail = findCartDetail(product, cart);
			if (cartDetail != null) {
				cartDetail.setQuantity(num + cartDetail.getQuantity());
				cartDetailDao.update(cartDetail);
				return;
			}

			// 2.创建购物车明细
			cartDetail = new CartDetail(cart, product, num,//
					pm.isAuthenticProduct(product.getProductId()));

			List<ReservedActivity> reservedActivities = reservedActivityManager
					.getMyReservedActivity(counter);
			for (ReservedActivity reservedActivity : reservedActivities) {
				// 设计可以有多个预订会 实际只有一个(多个预订会要把id放到前端 前端再传回来)
				ReservedActivityContext context = reservedActivity.getContext();
				Assert.notNull(context, "购物车添加产品检验是否在预订会中时 正文为空");
				if (reservedActivity.getContext().containsProduct(product.getProductId())) {
					cartDetail.setRevId(reservedActivity.getRevId());
					break;
				}
			}

			Long detailsId = null;
			try {
				idGens.lock(ID_CARTDETAIL);
				detailsId = Long.parseLong(idGens.generateDateId(ID_CARTDETAIL));
			} finally {
				idGens.unlock(ID_CARTDETAIL);
			}
			cartDetail.setDetailId(detailsId);
			// FIX ME 如果产品是工服的话就为正品且不走价格策略
			Customer customer = customerManager.get(counter.getCustomerId());
			FixedPrice fixedPrice = fixedPriceManager.getFixedPrice(product.getProductId(),
					customer.getCustomerTypeID());
			// FIX ME：by Shihx 如果不算客户折扣则需要一个价格或新的折扣
			if (fixedPrice != null && !fixedPrice.getPricePolicy()) {
				// FIX ME:根据fix 来确实是固定价格还是打折
				cartDetail.setPricePolicy(fixedPrice.getPricePolicy());
				// 固定价格或者新折扣算出价格
				cartDetail.setPrice(fixedPrice.getFix() ? fixedPrice.getPrice() //
						: cartDetail.getPrice() * fixedPrice.getDiscount());
			}

			// 3.创建明细的描述
			CartDetailDesc desc = new CartDetailDesc(product);
			desc.setQuantity(1);
			desc.setCartDetailId(detailsId);
			// 判断是否为预订
			Integer pid = product.getProductId();
			boolean isReserve=reserveProductManager.isReserving(pid, stockId);
			if(isReserve){
				ReserveProduct reserveProduct= reserveProductManager.get(pid);
				desc.setBatchNo(reserveProduct.getBatchNo());
			}
			desc.setReserve(isReserve);
			// FIX ME 2.1 单个正品都是走销售 发货方式精确到每个购物车明细描述
			Dictionary deliveryWayItem = dictManager.lookUpByCode(OrdmConfig.DELIVERYWAY,
					OrdmConfig.DELIVERYWAY_SALES);
			DeliveryWay deliveryWay = new DeliveryWay(deliveryWayItem);
			desc.setDeliveryWayId(deliveryWay.getDeliveryWayId());
			desc.setDeliveryWayName(deliveryWay.getDeliveryName());
			desc.setPrice(cartDetail.getPrice());
			// XXX:添加到购物车 设置费比标志位
			// 默认正品支持配比 物料占用配比
			desc.setCostRatio(CartDetailDesc.GocostRatio);
			// 如果设置了价格策略 用策略里的标志
			if (fixedPrice != null) {
				desc.setCostRatio(fixedPrice.getCostRatio());
			}
			List<CartDetailDesc> descs = new ArrayList<>();
			descs.add(desc);
			cartDetail.setCartDetailsDesc(descs);
			if (descs == null || descs.size() <= 0) {
				throw new ServiceException("购物车明细的描述不能为空");
			}
			cartDetailService.save(cartDetail);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Transactional
	public CartDetail findCartDetail(Product product, Cart cart) throws ServiceException {
		List<CartDetail> cds = getCartDetailByCart(cart);
		List<CartDetail> query = new ArrayList<>();
		Map<Long, CartDetail> map_cartDetail = new HashMap<>();
		for (CartDetail cd : cds) {
			// FIX ME 2.1 判断都为单品是否重复添加
			if (Product.TYPEID_PRODUCT.equals(cd.getType()) //
					|| Product.TYPEID_PRESENT.equals(cd.getType()) //
					|| Product.TYPEID_METERIAL.equals(cd.getType())) {
				query.add(cd);
				map_cartDetail.put(cd.getDetailId(), cd);
			}
		}
		if (query.size() <= 0) {
			return null;
		}
		// 每次查询产品在不在购物车里面需要发送一条SQL语句
		List<CartDetailDesc> descs = getCartDescByCartDetail(query);
		if (descs.size()<=0) {
			return null;
		}
		// 判断单品是否重复取得明细描述的第一行即可
		Map<Long, CartDetailDesc> map_desc = new LinkedHashMap<>();
		for (CartDetailDesc desc : descs) {
			if (!map_desc.keySet().contains(desc.getCartDetailId())) {
				map_desc.put(desc.getCartDetailId(), desc);
			}
			for (CartDetailDesc d : map_desc.values()) {
				if (d.getProductId().equals(product.getProductId())) {
					return map_cartDetail.get(d.getCartDetailId());
				}
			}
		}
		return null;
	}

	/**
	 * 重载方法，添加活动
	 * 一个活动组在购物车的体现
	 */
	@Transactional
	public void add(Long cartId, String actId, List<Item> items, int num) throws ServiceException {
		/* 如果该活动组已经存在了就执行更新操作 */
		Activity activity = activityManager.get(actId);
		// 检查活动是否存在，或是否有效
		if (activity == null //
				|| !(activityManager.findAvaliable().contains(activity))) {
			logger.info("该活动id={}没有对应的活动, 或者过期。", actId);
			return;
		}
		String type = activity.getType();
		if (ActivityType.TYPE_BUYGIVE.equals(type)
				|| ActivityType.TYPE_PRENSENTOWNER.equals(type)
				/* 2017 -06-22 FIXIT BY wangs */
				|| ActivityType.TYPE_BUYGIVES.equals(type)) {
			addBuyGiveAndBigPgActive(cartId, actId, items, num, activity, pm);
		}if(ActivityType.TYPE_SUIT.equals(type) || ActivityType.TYPE_BIGPACAKGE.equals(type) ){
			/* 2017 -09-28 FIXIT BY wangs */
			addSuit(cartId, actId,items, num, activity, pm);
		}
	}

	/**
	 * 套装加入购物车结算
	 * @param cartId
	 * @param actId
	 * @param items
	 * @param num
	 * @param activity
	 * @param pm
	 * @param activityManager
	 * @author fengx  
	 * @fix wangs
	 */
	private void addSuit(Long cartId, String actId, List<Item> items , int num, Activity activity, 
			ProductManager pm ) {
		// 校验活动有效性
		ActivityManager activityManager = AppContext.getBean(ActivityManager.class);
		CounterManager counterManager=AppContext.getBean(CounterManager.class);
		ReservedActivityManager reservedActivityManager=AppContext.getBean(ReservedActivityManager.class);
		Cart cart=cartDao.get(cartId);
		Counter counter=counterManager.get(cart.getCounterId());
		
		List<ReservedActivity> reservedActivities=reservedActivityManager.getMyReservedActivity(counter);
		//活动属于预订会 则不为空
		Integer revId=null;
		for (ReservedActivity reservedActivity : reservedActivities) {
			if(reservedActivity.getContext().containsActivity(activity.getActId())){
				revId=reservedActivity.getRevId();
				break;
			}
		}
		
		Long detaiId=findSuitActive(actId, cartId);
		if (detaiId != null && detaiId != 0) {
			CartDetail cartDetail = cartDetailDao.get(detaiId);
			cartDetail.setQuantity(num + cartDetail.getQuantity());
			cartDetailDao.update(cartDetail);
			return;
		}

		Dictionary dictionary = dictManager.lookUpByCode(OrdmConfig.DELIVERYWAY, OrdmConfig.DELIVERYWAY_SALES);
		DeliveryWay deliveryWay_sale = new DeliveryWay(dictionary);
		// 套装,价格从活动正文里取得
		double singleSuitPrice = 0.0;// 单套价格
		// 活动结构
		Activity act = activityManager.get(actId);
		ActivityContext actContext = act.getContext();
		ProductRule buyRule = actContext.getActityGoods();
		Map<Integer, ProductItem> itemMap = new HashMap<>();
		if(ActivityType.TYPE_SUIT.equals(act.getType())) {
			itemMap = buyRule.getProductItem();
		}
		
	 	int actRevStatus= actContext.getRevWmsStatus(counter.getWarehouses()); 
	 	//活动打欠标志
	 	boolean isReserve =actRevStatus==ReserveProduct.STATUS_RESERVED.intValue()?true:false;
		
		// 添加大礼包
		if(ActivityType.TYPE_BIGPACAKGE.equals(act.getType())) {
			for (ProductRule rule : actContext.getActityExtras()) {
				for (ProductItem item :  rule.getProductItem().values()) {
					itemMap.put(item.getPid(), item);
				}
			}
		}

		Long detailId = Long.parseLong(idGens.generateDateId(CartDetailService.ID_CARTDETAIL));
		CartDetail cartDetail = new CartDetail(actId, detailId, num);
		if(revId!=null){
			cartDetail.setRevId(revId);
		}
		cartDetail.setCartId(cartId);
		cartDetail.setActivityName(activity.getName());
		cartDetail.setDetailId(detailId);
		
		double totalprice = 0.0;// 总价格
		List<CartDetailDesc>descs=new ArrayList<>();
		for (Item pItm : items) {
			Product p = pm.get(Integer.parseInt(pItm.getId()));
			CartDetailDesc d = new CartDetailDesc(p);
			ProductItem pitem = itemMap.get(p.getProductId());
			
			d.setReserve(isReserve);
			d.setQuantity(pitem.getQuantity());
			d.setCartDetailId(detailId);
			// 这里固定的组合 不能从数据库拿价格
			singleSuitPrice = pitem.getPrice()*pitem.getQuantity();
			d.setPrice(pitem.getPrice());
			d.setDeliveryWayId(deliveryWay_sale.getDeliveryWayId());
			d.setDeliveryWayName(deliveryWay_sale.getDeliveryName());
			
			// 设置该活动下的赠品以及物料走不走费比
			d.setCostRatio(false);
			descs.add(d);
			// 总价
			totalprice += singleSuitPrice;
		}
		cartDetail.setCartDetailsDesc(descs);
		cartDetail.setPrice(totalprice);
		// 价格策略
		cartDetail.setPricePolicy(true);
		if(cartDetail.getCartDetailsDesc()==null||cartDetail.getCartDetailsDesc().size()<=0){
			throw new ServiceException("购物车明细的描述不能为空");
		}
		cartDetailDao.save(cartDetail);
	}

	/***
	 * 看固定套装有没有
	 * 返回值是购物车明细的主键
	 * @param actId
	 * @param cartId
	 */
	private Long findSuitActive(String actId, Long cartId) {
		return cartDetailDao.findSuitActive(actId, cartId);
	}
	
	
	void addBuyGiveAndBigPgActive(Long cartId, String actId, List<Item> items, int num, Activity activity,
			ProductManager pm) {
		
		ReservedActivityManager reservedActivityManager=AppContext.getBean(ReservedActivityManager.class);
		Cart cart=cartDao.get(cartId);
		Counter counter=counterManager.get(cart.getCounterId());
		
		List<ReservedActivity> reservedActivities=reservedActivityManager.getMyReservedActivity(counter);
		//活动属于预订会 则不为空
		Integer revId=null;
		for (ReservedActivity reservedActivity : reservedActivities) {
			if(reservedActivity.getContext().containsActivity(activity.getActId())){
				revId=reservedActivity.getRevId();
				break;
			}
		}
		
		Long detaiId = findItems(actId, cartId, items);
		if (detaiId != null && detaiId != 0) {
			CartDetail cartDetail = cartDetailDao.get(detaiId);
			cartDetail.setQuantity(num + cartDetail.getQuantity());
			if(revId!=null){
				cartDetail.setRevId(revId);
			}
			cartDetailDao.update(cartDetail);
			return;
		}
		List<Item> actityGoods = new ArrayList<>();
		List<Item> actityExtra = new ArrayList<>();
		for (Item item : items) {
			if (Item.TYPE_FREE.equals(item.getType())) {
				actityExtra.add(item);
			} else {
				actityGoods.add(item);
			}
		}
		boolean flag = activityManager.validateCheck(actId, actityGoods, actityExtra);
		if (!flag) {
			return;
		}
		
		int actRevStatus=activity.getContext().getRevWmsStatus(counter.getWarehouses());
		//活动打欠标志
		boolean isReserve =actRevStatus==ReserveProduct.STATUS_RESERVED?true:false;
		
		
		// 需要校验
		/** 2.创建购物车明细:实例化一个活动 */
		Dictionary dictionary = dictManager.lookUpByCode(OrdmConfig.DELIVERYWAY,
				OrdmConfig.DELIVERYWAY_SALES);
		DeliveryWay deliveryWay_sale = new DeliveryWay(dictionary);
		Dictionary dictionary_market = dictManager.lookUpByCode(OrdmConfig.DELIVERYWAY,
				OrdmConfig.DELIVERYWAY_MARKETPRESENT);
		DeliveryWay deliveryWay_market = new DeliveryWay(dictionary_market);

		double totalprice = 0.0;
		Long detailId = Long.parseLong(idGens.generateDateId(CartDetailService.ID_CARTDETAIL));
		CartDetail cartDetail = new CartDetail(actId, detailId, num);
		cartDetail.setCartId(cartId);
		cartDetail.setActivityName(activity.getName());
		cartDetail.setPrice(totalprice);
		cartDetail.setDetailId(detailId);
		if(revId!=null){
			cartDetail.setRevId(revId);
		}
		List<CartDetailDesc>descs=new ArrayList<>();
		for (Item pItm : items) {
			Product p = pm.get(Integer.parseInt(pItm.getId()));
			CartDetailDesc d = new CartDetailDesc(p);
			d.setReserve(isReserve);
			d.setQuantity(pItm.getNum());
			d.setCartDetailId(detailId);
			if (!Item.TYPE_FREE.equals(pItm.getType())) {
				d.setPrice(p.getMemberPrice());
				// 买赠金额从产品中拿取
				totalprice += pm.get(Integer.parseInt(pItm.getId())).getMemberPrice()
						* pItm.getNum();
				d.setDeliveryWayId(deliveryWay_sale.getDeliveryWayId());
				d.setDeliveryWayName(deliveryWay_sale.getDeliveryName());
			} else {
				d.setDeliveryWayId(deliveryWay_market.getDeliveryWayId());
				d.setDeliveryWayName(deliveryWay_market.getDeliveryName());
				d.setPrice(0.0);
			}
			// 设置该活动下的赠品以及物料走不走费比
			d.setCostRatio(false);
			descs.add(d);
		}
		cartDetail.setCartDetailsDesc(descs);
		cartDetail.setPrice(totalprice);
		 //FIXME 活动没有判断走不走价格策略
		cartDetail.setPricePolicy(true);
		if(cartDetail.getCartDetailsDesc()==null||cartDetail.getCartDetailsDesc().size()<=0){
			throw new ServiceException("购物车明细的描述不能为空");
		}
		cartDetailDao.save(cartDetail);
	}
	
	/**
	 * 找到根据前台传过来的活动明细来判断数据库活动的明细是否有相同的，有就返回明细主键
	 * 
	 * @throws ServiceException
	 */
	@Transactional
	@Override
	public Long findItems(String actId, Long cartId, List<Item> insertItem)
			throws ServiceException {
		List<Item> items;
		List<CartDetail> cartDetails;
		if (Strings.isNullOrEmpty(actId)) {
			return null;
		}
		try {
			/** 判断购物车明细表有没有数据 */
			cartDetails = cartDetailDao.findItems(cartId);
			if (cartDetails.isEmpty()) {
				return null;
			}
			/** 遍历明细，判断明细表里面有没有活动id */
			List<CartDetail> cartDetails2 = new ArrayList<>();
			for (CartDetail d : cartDetails) {
				if (actId.equals(d.getActivityId())){
					cartDetails2.add(d);
				}
			}
			/** 判断组明细不存在活动id就返回null */
			if (cartDetails2.isEmpty()) {
				return null;
			}
			/** 遍历购物车里面的所有是该活动id的明细，并且判断是否与新添加的活动明细相等 */
			
			List<CartDetailDesc> descs = getCartDescByCartDetail(cartDetails2);
			Map<Long, List<CartDetailDesc>> map = new HashMap<>();
			for (CartDetailDesc desc : descs) {
				if (map.keySet().contains(desc.getCartDetailId())) {
					map.get(desc.getCartDetailId()).add(desc);
				} else {
					List<CartDetailDesc> lcdd = new LinkedList<CartDetailDesc>();
					lcdd.add(desc);
					map.put(desc.getCartDetailId(), lcdd);
				}
				for (Long key : map.keySet()) {
					List<CartDetailDesc> cartDetailDescs = map.get(key);
					items = new ArrayList<Item>();
					for (CartDetailDesc desc1 : cartDetailDescs) {
						Item item = new Item();
						item.setId(String.valueOf(desc1.getProductId()));
						item.setNum((int) desc1.getQuantity());
						item.setType(desc1.getPrice() == 0.0 ? Item.TYPE_FREE : Item.TYPE_PAY);
						items.add(item);
						if (items.equals(insertItem)) {
							return desc1.getCartDetailId();
						}
					}
				}
			}
		} catch (DaoException e) {
			throw new ServiceException("购物车细节获取异常", e);
		}
		return null;
	}

	@Transactional
	public boolean update(Long detailId, int num) throws ServiceException {
		CartDetail cartDetail = cartDetailDao.get(detailId);
		if (cartDetail == null) {
			return false;
		}
		if (num <= 0) {
			deleteDetailAndDesc(detailId);
			return true;
		}
		// FIX ME: 数量未修改，不做操作？
		int cartNum = cartDetail.getQuantity() == null ? 0 : cartDetail.getQuantity();
		if (cartNum != num) {
			cartDetail.setQuantity(num);
			cartDetailDao.update(cartDetail);
		}
		return true;
	}
	
	@Transactional
	/** 修改购物车的状态 */
	public void changeCartStatus(Long cartId, int status) throws ServiceException {
		Cart cart = get(cartId);
		if (cart == null) {
			throw new ServiceException("该购物车不存在");
		}
		cart.setStatus(status);
		cart.setLastUpdate(System.currentTimeMillis());
		update(cart);
	}
	
	@Transactional
	@Override
	public void commitCart(AuthUser user, Long cartId) throws ServiceException {
		Cart cart = get(cartId);
		if (cart == null) {
			throw new ServiceException("该购物车不存在");
		}
		cart.setStatus(Cart.STATUS_COMMIT);
		cart.setUserId(user.getLoginName());
		cart.setUserName(user.getCommonName());
		cart.setLastUpdate(System.currentTimeMillis());
		update(cart);
		// 提交购物车，申明锁库
		StorehousesSyncTask synctask = AppContext.getBean(StorehousesSyncTask.class);
		synctask.setUpdated(); 
	}

	@Transactional
	public void deleteDetailAndDesc(Long detailId) throws ServiceException {
		try {
			// 删除明细的所有的描述
			//cartDetailDescDao.deleteDetailAndDesc(detailId);
			// 删除明细
			cartDetailDao.delete(detailId);
		} catch (DaoException e) {
			throw new ServiceException("删除购物车明细以及描述异常");
		}
	}
	
	
	
	@Transactional
	public void deleteCart(Long cartId) {
		try {
			cartDetailDao.deleteCart(cartId);
			
			Cart cart = getDao().get(cartId);
			cart.setOrderId(null);
			getDao().update(cart);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	

	/***
	 * 拿到可以支持配送物料、赠品的价格
	 * 
	 * @param cartDetails
	 * @param discount
	 * @return
	 */
	@Override
	public double getSupportdistributionPrice(List<CartDetail> cartDetails, double discount) {
		double realPrice = 0.00;
		// 在购物车明细表里面遍历所有的产品以及优惠活动组
		// 这里是所有的正品以及优惠活动都走价格策略??? 目前都走价格策略
		// FIX ME:有些大货不参与费比计算 CartDetailDesc.isCostRatio
		for (CartDetail cartDetail : cartDetails) {
			//只有类型为正品才计算
			if (!TYPEID_PRODUCT.equals(cartDetail.getType())) {
				continue;
			}
			Integer qty=cartDetail.getQuantity();
			for (CartDetailDesc desc : cartDetail.getCartDetailsDesc()) {
				//不支持费比的正品
				if(!desc.isCostRatio()){
					continue;
				}
				double amt = desc.getQuantity() * desc.getPrice()*qty;
				if (cartDetail.isPricePolicy()) {
					realPrice += discount * amt;
				} else {
					realPrice += amt;
				}
			}
		}
		return realPrice;
	}

	public double getRealPrice(List<CartDetail> cartDetails, double discount) {
		double realPrice = 0.00;
		// 在购物车明细表里面遍历所有的产品以及优惠活动组
		// 这里是所有的正品以及优惠活动都走价格策略??? 目前都走价格策略
		for (CartDetail cartDetail : cartDetails) {
			//正品活动直接取Detail价格
			if (TYPEID_PRODUCT.equals(cartDetail.getType())
					||CartDetail.ActivityProduct.equals(cartDetail.getType())) {
				if (cartDetail.isPricePolicy()) {
					realPrice += discount * cartDetail.getQuantity() * cartDetail.getPrice();
				} else {
					realPrice += cartDetail.getPrice() * cartDetail.getQuantity();
				}
			//物料赠品 如果不走费比 则取desc价格
			}else {
				for (CartDetailDesc desc : cartDetail.getCartDetailsDesc()) {
					if(!desc.isCostRatio()){
						realPrice+=desc.getPrice()*desc.getQuantity()*cartDetail.getQuantity();
					}
				}
			}
		}
		return realPrice;
	}

	@Override
	public double getTotalFee(List<CartDetail> cartDetails) {
		double realPrice = 0.00;
		for (CartDetail cartDetail : cartDetails) {
			if (TYPEID_PRODUCT.equals(cartDetail.getType())
					|| CartDetail.ActivityProduct.equals(cartDetail.getType())) {
				realPrice += cartDetail.getPrice() * cartDetail.getQuantity();
			}else {
				for (CartDetailDesc desc : cartDetail.getCartDetailsDesc()) {
					if(!desc.isCostRatio()){
						realPrice+=desc.getPrice()*desc.getQuantity()*cartDetail.getQuantity();
					}
				}
			}
		}
		return realPrice;
	}

	@Override
	/**`
	 * <=0 说明没有超过费比 >0 超过费比 友情提示
	 */
	public double compareFeeAndPay(List<CartDetail> cartDetails,double discount)
			throws ServiceException {
		List<Dictionary> dictionaries = dictManager.get(OrdmConfig.ORDMCONFIG).getChildren();
		Dictionary dict_materialdiscount = null;

		for (Dictionary dictionary : dictionaries) {
			if (OrdmConfig.MATERIALDISCOUNT.equals(dictionary.getDid())) {
				dict_materialdiscount = dictionary;
			}
		}

		double peisongbi = Double.parseDouble(dict_materialdiscount.getValue());

		double realPrice = getSupportdistributionPrice(cartDetails, discount) * peisongbi;
		double presentFee = getPresentFee(cartDetails);
		return presentFee - realPrice;
	}

	/**
	 * 取得物料费用。
	 * @param cart 购物车
	 * @param customerTypeId 顾客ID
	 * @return 物料费用  大于'0'收费  小于'0' 免费
	 * @throws ServiceException
	 */
	@Transactional
	@Override
	public double getMaterialFee(Cart cart, Integer customerTypeId) throws ServiceException {
		List<CartDetail> cartDetails = getCartDetailByCart(cart);
		if (cartDetails == null || cartDetails.isEmpty()) {
			return 0.0;
		}
		CustomerPricePolicy policy = policyManager.getPolicy(customerTypeId);
		if (policy == null) {
			throw new ServiceException("PolicyID:" + customerTypeId + "价格策略不存在！");
		}
		// 控制一致性约束？policy可能为空
		// 通过类型找到折扣
		double discount = policy.getMaterialDiscount();
		
		// FIX ME直营折扣1.0导致配比比较高
		Map<String, Double> map = getMaterialFee(cartDetails, discount);
		// 浮点比较不能比较0，金钱以万分之一为比较单位
		if ((Double) map.get(Cart.MATERIALFEE) > 0.0001) {
			return TypeConverter.toDouble(map.get(Cart.MATERIALFEE),0);
		} else {
			return 0 - TypeConverter.toDouble(map.get(Cart.REMAINFEE),0);
		}
	}

	@Override
	public double getPresentFee(Cart cart) throws ServiceException {
		List<CartDetail> cartDetails = getCartDetailByCart(cart);
		return getPresentFee(cartDetails);
	}

	/**
	 * 获取订单里面的走费比的赠品以及物料 来源<p>
	 * <li>1:cartdetail里面类型为赠品以及物料的产品(并且都为走费比策略)
	 * <li>2:关联的赠品以及物料都是走费比策略。在cartDetailDES实体里面
	 * <li>3：判断改活动里面的赠品以及物料走不走费比
	 * <br>
	 * 这里运算出来的费比是要与正品的价格做比较的<p>
	 * 
	 * @param cartDetails
	 * @return
	 */
	@Override
	public double getPresentFee(List<CartDetail> cartDetails) throws ServiceException {
		double feePrice = 0.00;
		// 先找到购物车明细的所有的物料以及赠品
		for (CartDetail cartDetail:cartDetails) {
			List<CartDetailDesc> descs= cartDetail.getCartDetailsDesc();
			for (CartDetailDesc desc : descs) {
				// 判断描述表里面所有不为正品且占费比的的商品
				if (!pm.isAuthenticProduct(desc.getProductId())
						&& desc.isCostRatio()) {
					feePrice = feePrice + desc.getPrice() * desc.getQuantity()
							* cartDetail.getQuantity();
				}
			}
		}
		return feePrice;
	}

	@Override
	public Double[] getPAndMQty(Cart cart, ProductManager pm) throws ServiceException {
		List<CartDetail> cartDetails = getCartDetailByCart(cart);
		return getPAndMQty(cartDetails, pm);
	}

	/** 获取产品和数量的数量 */
	private Double[] getPAndMQty(List<CartDetail> cartDetails, ProductManager pm) {
		int totalNum = 0;
		int productQty = 0;
		int materialQty = 0;
		// 先找到购物车明细的所有的物料以及赠品
		Map<Long, CartDetail> cdm = new HashMap<>();
		for (CartDetail d:cartDetails) {
			cdm.put(d.getDetailId(), d);
		}
		
		for (CartDetail cartDetail : cartDetails) {
			for (CartDetailDesc desc : cartDetail.getCartDetailsDesc()) {
				totalNum += desc.getQuantity() * cartDetail.getQuantity();
				if (pm.isAuthenticProduct(desc.getProductId())) {
					productQty += desc.getQuantity() * cartDetail.getQuantity();
				} else {
					materialQty += desc.getQuantity() * cartDetail.getQuantity();
				}
			}
		}
		Double totalFee = getTotalFee(cartDetails);
		Double[] Qtys = new Double[] { (double) totalNum, (double) productQty,
				(double) materialQty, totalFee };
		return Qtys;
	}

	@Override
	public Map<Integer, Double> getPostage() throws ServiceException {
		Dictionary dict_maxAmount = dictManager.lookUpByCode(ORDMCONFIG,KEY_MAXAMOUNT);
		Dictionary dict_minAmount = dictManager.lookUpByCode(ORDMCONFIG,KEY_MINIAMOUNT);
		Dictionary dict_maxPostage = dictManager.lookUpByCode(ORDMCONFIG,KEY_MAXPOSTAGE);
		Dictionary dict_midPostage = dictManager.lookUpByCode(ORDMCONFIG,KEY_MIDPOSTAGE);
		Dictionary dict_noPostage = dictManager.lookUpByCode(ORDMCONFIG,KEY_NOPOSTAGE);
		
		final Map<Integer, Double> postage = new LinkedHashMap<>(0);

		Integer k1 = StringUtil.toInt(dict_maxAmount.getValue(), 10000);
		Double m1 = StringUtil.toDouble(dict_noPostage.getValue());
		postage.put(k1, m1);

		Integer k2 = StringUtil.toInt(dict_minAmount.getValue(), 5000);
		Double m2 = StringUtil.toDouble(dict_midPostage.getValue());
		postage.put(k2, m2);

		Double m3 = StringUtil.toDouble(dict_maxPostage.getValue());
		postage.put(0, m3);

		return postage;
	}

	@SuppressWarnings("unused")
	@Override
	public Map<String, Object> getCartData(Cart cart, Integer customerTypeId)
			throws ServiceException {
		Map<String, Object> map_cart = new HashMap<String, Object>();
		// 1.获取购物车的所有的明细
		List<CartDetail> cartDetails = getCartDetailByCart(cart);
		
		// 获取购物车里面的正品的数量和物料加赠品的数量
		Double[] arry_qty = getPAndMQty(cartDetails, pm);
		double qty_sum = arry_qty[0];
		// XXX
		map_cart.put("qty_sum", qty_sum);
		double qty_product = arry_qty[1];
		// XXX
		map_cart.put("qty_product", qty_product);
		double qty_present = arry_qty[2];
		// XXX
		map_cart.put("qty_present", qty_present);
		// 获取购物车里面会员价的总价(走价格策略前的价格)
		Double totalFee = getTotalFee(cartDetails);
		map_cart.put("totalFee", totalFee);
		// 获取购物车所有的物料的费用
		Double materialFee = getMaterialFee(cart, customerTypeId);
		map_cart.put(Cart.MATERIALFEE, materialFee);

		double discount = policyManager.getPolicy(customerTypeId).getDiscount();// 通过类型找到折扣
		double realPrice = getRealPrice(cartDetails, discount);
		return map_cart;
	}


	/**
	 * 拿最大的免费金额去遍历物料的map, map是按照产品的价格进行升序排序的 当某次不满足免费(超过费比) 下面的所有的物料都是收费的
	 * 以及该物料有部分收费 和部分免费
	 * discount：客户折扣（物料）
	 */
	@Override
	public Map<String, Double> getMaterialFee(final List<CartDetail> cartDetails,
			final double discount) throws ServiceException {
		// 物料配比(6%)
		Dictionary dict_mdiscount = dictManager.lookUpByCode(OrdmConfig.ORDMCONFIG, OrdmConfig.MATERIALDISCOUNT);
		
		Double materialdiscount = StringUtil.toDouble(dict_mdiscount.getValue());
		if (materialdiscount == null) {
			throw new ServiceException("物料配比字典定义错误！");
		}
		// 除去活动，可配送的金额（按折扣、考虑活动因素） 正品金额*物料折扣*物料配比
		double realPrice = getSupportdistributionPrice(cartDetails, discount) * materialdiscount;
		// 实际应配物料
		double totalMaterialFee = realPrice;
		// 实际已经选的物料
		double totalMaterialAmt = 0;
		
		Map<Long,CartDetail> detailsMap = new HashMap<>();
		for (CartDetail cd:cartDetails) {
			detailsMap.put(cd.getDetailId(), cd);
		}
		
		List<CartDetailDesc> allMaterials = new ArrayList<>();
		//Step 1, 取出物料，计算并排序
		for(CartDetail cartDetail:cartDetails){
			for (CartDetailDesc cd : cartDetail.getCartDetailsDesc()) {
				// 是否是正品，如果是正品或者不使用费比
				if (pm.isAuthenticProduct(cd.getProductId())) {
					continue;
				}
				// 收集赠品、物料(使用配比的)
				if(cd.isCostRatio()){
					allMaterials.add(cd);
				}
				//物料总金额(包括不使用配比的)
				totalMaterialAmt += cd.getPrice() * cd.getQuantity() * cartDetail.getQuantity();
			}
		}
		
		Collections.sort(allMaterials, (a, b) -> {
			double v = b.getPrice() - a.getPrice();
			if (v > 0) {
				return 1;
			}
			return v > -0.001 ? 0 : -1;
		});
		// 1st scan materials 扣除费比足以支付整条明细的物料
		Set<Integer> removed = new LinkedHashSet<>();
		List<CartDetailDesc> remains = new ArrayList<>();
		for (CartDetailDesc desc : allMaterials) {
			CartDetail c = detailsMap.get(desc.getCartDetailId());
			double v = desc.getPrice() * desc.getQuantity() * c.getQuantity();
			if (realPrice - v >= 0) {
				realPrice -= v;
				removed.add(desc.getProductId());
			} else {
				//XXX: Clone object: CartDetailDesc
				remains.add(desc);
			}
		}
		// 2nd scan: realPrice 已经扣减了物料，并记录下ID(removed) 扣除一条明细里剩余费比足以支付多个物料金额
		int v_realPrice = (int) (realPrice * 100);
		for (CartDetailDesc desc : remains) {
			int v_price = (int) (desc.getPrice() * 100);
			int num = v_realPrice / v_price;
			// 修正： 试算是否进行扣减
			if (num > 0 && (v_realPrice - v_price * num >= 0)) {
				// 修正：同步扣减
				v_realPrice -= v_price * num;
				realPrice -= num * desc.getPrice();
			}
		}
		
		final Map<String, Double> map_materialFee = new HashMap<>();
		// 不要钱的物料(使用的费比) 应配物料金额-剩余金额
		map_materialFee.put(Cart.MATERIALFREE, totalMaterialFee - realPrice);
		// 收费金额			物料总金额-(应配物料金额-剩余金额)
		double materialFee = totalMaterialAmt - totalMaterialFee + realPrice;
		map_materialFee.put(Cart.MATERIALFEE,materialFee);
		// 可配的物料金额 剩下的可配金额
		map_materialFee.put(Cart.REMAINFEE, materialFee > 0.001 ? 0.0 : realPrice);
		return map_materialFee;
	}

	private void addActivityToCart(Long cartId, List<Activity> activities, Double usefulActFee) throws ServiceException {
		// <活动id,套数>
		Map<String, Integer> suitMap = new HashMap<>();
		// <活动id,活动>
		Map<String, Activity> IdMap = new HashMap<>();
		for (Activity activity : activities) {
			String actId = activity.getActId();
			if (!suitMap.containsKey(actId)) {
				suitMap.put(actId, 0);
			}
			suitMap.put(actId, suitMap.get(actId) + 1);

			IdMap.put(actId, activity);
		}
		for (String actId : IdMap.keySet()) {

			Activity activity = IdMap.get(actId);

			Dictionary dictionary_market = dictManager.lookUpByCode(OrdmConfig.DELIVERYWAY,
					OrdmConfig.DELIVERYWAY_MARKETPRESENT);
			DeliveryWay deliveryWay_market = new DeliveryWay(dictionary_market);

			ActivityContext context = activity.getContext();
			List<ProductRule> productRules = context.getActityExtras();
			Long detailId = Long.parseLong(idGens.generateDateId(CartDetailService.ID_CARTDETAIL));
			Integer suitNum = suitMap.get(actId);

			CartDetail cartDetail = new CartDetail(actId, detailId, suitNum);
			cartDetail.setCartId(cartId);
			cartDetail.setActivityName(activity.getName());
			cartDetail.setDetailId(detailId);

			Double totalprice = 0.0;
			for (ProductRule productRule : productRules) {
				for (ProductItem pItm : productRule.getProductItem().values()) {
					Double price = pItm.getPrice();
					Product p = pm.get(pItm.getPid());
					CartDetailDesc d = new CartDetailDesc(p);
					d.setReserve(false);
					d.setQuantity(pItm.getQuantity());
					d.setCartDetailId(detailId);
					// FIX ME 2.1 这里固定的组合 不能从数据库拿价格
					d.setPrice(price);
					totalprice += price;
					d.setDeliveryWayId(deliveryWay_market.getDeliveryWayId());
					d.setDeliveryWayName(deliveryWay_market.getDeliveryName());
					// 设置该活动下的赠品以及物料走不走费比
					d.setCostRatio(false);
					cartDetail.addCartDetailsDesc(d);
				}
			}
			cartDetail.setPrice(totalprice * suitNum);
			if (cartDetail.getCartDetailsDesc() == null || cartDetail.getCartDetailsDesc().size() <= 0) {
				throw new ServiceException("购物车明细的描述不能为空");
			}
			cartDetailDao.save(cartDetail);
		}
		Cart cart = cartDao.get(cartId);
		for (Activity activity : activities) {
			cart.addActivRecord(activity.getActId());
		}
		cartDao.update(cart);
	}
	
	/** 获取购物车内指定产品的金额	  折扣后*/
	private Double getUsefulActFee(Counter counter, Collection<Integer> ProductId) throws ServiceException {
		Double pricae=0.0;
		Cart cart=cartDao.getCounterCart(counter);
		if(cart==null){
			return pricae;
		}
		List<CartDetail> cartDetails=cartDetailDao.getCartDetailByCart(cart);
		if(cartDetails==null){
			return pricae;
		}
		Customer customer=customerManager.get(counter.getCustomerId());
		CustomerPricePolicy policy=policyManager.getPolicy(customer.getCustomerTypeID());
		Double discount=policy.getDiscount();
		for (CartDetail cartDetail : cartDetails) {
			Integer suitNum=cartDetail.getQuantity();
			for (CartDetailDesc desc : cartDetail.getCartDetailsDesc()) {
				if (!ProductId.contains(desc.getProductId())) {
					continue;
				}
				pricae+=desc.getPrice()*discount*desc.getQuantity()*suitNum;
			}
		}
		return pricae;
	}

	/** 获取购物内已经使用和活动金额*/
	private Double getUsedActFee(Counter counter) throws ServiceException {
		Double price = 0.0;
		try {
			ActivityManager activityManager = AppContext.getBean(ActivityManager.class);
			Cart cart = cartDao.getCounterCart(counter);
			for (String actId : cart.getActivRecord()) {
				Activity activity = activityManager.get(actId);
				price += activity.getContext().getAmount().getAmount();
			}
		} catch (ServiceException e) {
			logger.error("获取购车内占用的活动金额错误");
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return price;
	}

	@Override
	@Transactional
	public void refuse(Long cartId) throws ServiceException {
		Cart cart = this.get(cartId);
		cart.setStatus(Cart.STATUS_ACTIVE);
		cart.setLastUpdate(System.currentTimeMillis());
		this.update(cart);
		StorehousesSyncTask synctask = AppContext.getBean(StorehousesSyncTask.class);
		synctask.setUpdated();
	}

	@Override
	@Transactional
	public void commitCart(AuthUser user, Long cartId, Double usefulActFee) throws ServiceException {
		Cart cart = get(cartId);
		if (cart == null) {
			throw new ServiceException("该购物车不存在");
		}
		Counter counter=counterManager.get(cart.getCounterId());
		cart.setStatus(Cart.STATUS_COMMIT);
		cart.setUserId(user.getLoginName());
		cart.setUserName(user.getCommonName());
		cart.setLastUpdate(System.currentTimeMillis());
		//往购物车里加赠品
		addActivityToCart(cartId, usefulActFee);
		//h活动记录
//		List<Activity> activities=getSuitableActId(counter, usefulActFee);
//		for (Activity activity : activities) {
//			cart.AddActivRecord(activity.getActId());
//		}
		update(cart);
		// 提交购物车，申明锁库
		StorehousesSyncTask synctask = AppContext.getBean(StorehousesSyncTask.class);
		synctask.setUpdated(); 
	}

	@Override
	public void addActivityToCart(Long cartId, Double usefulActFee) throws ServiceException {
		try {
			Cart cart=cartDao.get(cartId);
			Counter counter = counterManager.get(cart.getCounterId());
//			List<Activity> activities = getSuitableActId(counter, usefulActFee);
//			addActivityToCart(cartId, activities, usefulActFee);
		} catch (ServiceException e) {
			logger.error("活动赠品添加到购物错误");
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	
	@Override
	public Boolean checkActivityInCart(Long cartId,Double usefulActFee) throws ServiceException {
		try {
//			Cart cart=cartDao.get(cartId);
//			Counter counter= counterManager.get(cart.getCounterId());
//			//根据当前可用活动金额 响应活动方案
////			List<Activity>activits=getSuitableActId(counter, usefulActFee);
////			//活动方案里活动id集合
////			List<String> actIds=new ArrayList<>(activits.size());
////			for (Activity activity : activits) {
////				actIds.add(activity.getActId());
////			}
//			//购物车里活动记录
//			List<String> record=cart.getActivRecord();
//			//比较
//			if(actIds.size()!=record.size()){
//				return false;
//			}
//			Collections.sort(actIds);
//			Collections.sort(record);
//			for (int i = 0; i < record.size(); i++) {
//				if(actIds.get(i).equals(record.get(i))){
//					return false;
//				}
//			}
			return true;
		} catch (ServiceException e) {
			logger.error("校验购物车内活动是否与金额相符错误");
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Double getUsefulActFeeInCart(Counter counter) throws ServiceException {
		ActivityManager activityManager = AppContext.getBean(ActivityManager.class);
		// FIXME:目前通过柜台获取活动的只有一种
		List<Activity> myActivity = activityManager.findMyActivities(counter);
		if(myActivity.isEmpty()){
			return 0.0;
		}
		// 活动起始时间
		Long start=myActivity.get(0).getStart().getTime();
		// 活动大货id集合
		// 171014 error null exception bigpack no ActityGoods 
		if (myActivity.get(0).getContext().getActityGoods() == null) {
			return 0.0;
		}
		Set<Integer> ProductId = myActivity.get(0).getContext()//
				.getActityGoods().getProductItem().keySet();
		//以支付以及后续订单状态
		List<String> orderStatus=new ArrayList<>();
		orderStatus.add(Order.Status_DoPay);
		orderStatus.add(Order.status_LogisticsDelivery);
		orderStatus.add(Order.status_DoLogisticsDelivery);
		orderStatus.add(Order.status_WaitShip);
		
		Double orderPrice=0.0;
		Double orderUsedFee=0.0;
		Double cartPrice=0.0;
		Double cartUsedPrice=0.0;
	 	final ActivityManager actManager=AppContext.getBean(ActivityManager.class);
		try {
			List<Order> orders= orderDao.findPayOrders(counter.getCounterCode(),//
					orderStatus, start, System.currentTimeMillis());
			List<String> orderNos=new ArrayList<>(orders.size()); 
			
			for (Order order : orders) {
				//所有订单
				orderNos.add(order.getOrderNo());
				//订单占用的金额
				if(!order.getActivRecord().isEmpty()){
					for (String actId : order.getActivRecord()) {
						Activity activity= actManager.get(actId);
						Double amt=activity.getContext().getAmount().getAmount();
						orderUsedFee+=amt;
					}
				}
			}
			//订单内累计金额
			if(orderNos!=null&&!orderNos.isEmpty()){
				orderPrice=detailDao.getPriceByProductId(orderNos, ProductId);
			}
			//购物车内产品累计金额
			cartPrice=getUsefulActFee(counter, ProductId);
			//购物车内占用金额
			cartUsedPrice=getUsedActFee(counter);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return orderPrice+cartPrice-orderUsedFee-cartUsedPrice;
	}

	@Override
	public List<CartDetail> getCartDetailByCounter(List<Integer> counterIds) throws ServiceException {
		if(counterIds.isEmpty()){
			return null;
		}
		List<Cart> carts=cartDao.findAllCart(counterIds);
		List<Long> cartIds=new ArrayList<>();
		for (Cart cart : carts) {
			if(Cart.STATUS_COMMIT.equals(cart.getStatus())){
				cartIds.add(cart.getCartId());
			}
		}
		if(!cartIds.isEmpty()){
			return cartDetailDao.findByCarts(cartIds);
		}
		return null;
	}

	@Override
	public boolean checkReserveActivity(List<CartDetail> cartDetails) {
		Integer productNum=0;
		Map<Integer, Integer> activNum=new HashMap<>();
		for (CartDetail cartDetail : cartDetails) {
			Integer revId=cartDetail.getRevId();
			//物料跳过校验
			if(Product.TYPEID_METERIAL.equals(cartDetail.getType())//
					||Product.TYPEID_PRESENT.equals(cartDetail.getType())){
				continue;
			}
			if(revId!=null){
				if(!activNum.containsKey(revId)){
					activNum.put(revId, 0);
				}
				Integer num = activNum.get(revId)+1;
				activNum.put(revId, num);
			}
			if(revId==null){
				productNum++;
			}
		}
		//有正常订货又有预订会
		if(productNum!=0&&activNum.keySet().size()>0){
			return false;
		}
		//预订会数量大于1
		if(activNum.keySet().size()>1){
			return false;
		}
		return true;
	}

}
