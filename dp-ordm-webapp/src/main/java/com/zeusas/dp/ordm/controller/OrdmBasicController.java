package com.zeusas.dp.ordm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import com.zeusas.core.http.ActionException;
import com.zeusas.dp.ordm.active.bean.ActivityType;
import com.zeusas.dp.ordm.active.bean.UserActivities;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.AmountRule;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.bean.CartLocker;
import com.zeusas.dp.ordm.bi.service.ProductSellerManager;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.CartDetailDesc;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.CounterSerial;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.CustomerPricePolicy;
import com.zeusas.dp.ordm.entity.LimitContext;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductSeller;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.service.ActivityLimitManager;
import com.zeusas.dp.ordm.service.CartService;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.PricePolicyManager;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.ReserveProductManager;
import com.zeusas.dp.ordm.service.UserCustomerManager;
import com.zeusas.dp.ordm.utils.StockArea;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.http.BasicController;

public abstract class OrdmBasicController extends BasicController implements StockArea {
	public final static String MY_SERIERS = "TIsnFmCl";
	public final static String MY_PRODUCTS = "datVZUev";
	public final static String USERDETAILS = "fissSoFF";
	public final static String CURRCOUNTERCODE = "QtNImh4d";
	public final static String ORDERCOUNTER = "ElyX7rAc";
	public final static String ALL_COUNTERS = "VfImmHQb";
	public final static String CURRIMG = "qZrW4SN6";
	public final static String SERIERS_PRODUCT = "nUBzD2BU";
	public final static String NOTIFICATION = "oeUwFngk";
	public final static String DEFAULTCOUNTER = "oe8QFngk";
	public final static String ALL_ORDER_SIZE = "lNmwzm1B";
	protected static String CARTLOCK = "hwcR3F";
	public final static String USER_ACTICITY_DATA = "nAw8BdI9";
	public final static String MY_STOCK = "v3Wz58Bi";

	static Logger logger = LoggerFactory.getLogger(OrdmBasicController.class);
	// 向前台发送 product数据的属性
	final static Set<String> filterProductField = new HashSet<>(Arrays.asList(
			"actFlag", "actItself",//
			"avalible", "barCode", "description", "imageURL", "inv", //
			"materialPrice", "memberPrice", "name", "productCode", //
			"productId", "qty", "retailPrice", "specifications", "status", //
			"type", "typeId", "typeName","minUnit"));
	final static int MAX_PNUM = 48;

	@Autowired
	private UserCustomerManager userCustomerManager;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CartService cartService;
	@Autowired
	private CounterManager counterManager;
	@Autowired
	private CustomerManager customerManager;
	@Autowired
	private ActivityManager activityManager;
	@Autowired
	private ProductManager productManager;
	@Autowired
	private ProductSellerManager productSellerManager;
	@Autowired
	private ReserveProductManager reserveProductManager;
	@Autowired
	private PricePolicyManager pricePolicyManager;
	@Autowired
	private ActivityLimitManager activityLimitManager;

	protected static final Map<Integer, CartLocker> cartLockers = new HashMap<Integer, CartLocker>();
	protected static final Map<String, Integer> all_order_size = new HashMap<String, Integer>();

	// 设置系统消息
	protected void setDefaultCounter (String counterCode) {
		if(counterCode == null || "".equals(counterCode)) {
			return;
		}
		HttpSession session = request.getSession(false);
		session.setAttribute(DEFAULTCOUNTER, counterCode);
	}

	protected String getDefaultCounter() {
		HttpSession session = request.getSession(false);
		return session == null ? null : (String) session.getAttribute(DEFAULTCOUNTER);
	}

	// 设置系统消息
	protected void setNotification(List<?> notification) {
		if(notification == null || notification.size() == 0) {
			return;
		}
		HttpSession session = request.getSession(false);
		session.setAttribute(NOTIFICATION, notification);
	}

	protected List<?> getNotification() {
		HttpSession session = request.getSession(false);
		return session == null ? null :  (List<?>) session.getAttribute(NOTIFICATION);
	}

	// 设置活动usersession
	protected void setUserActivityList(UserActivities userActivity) {
		if(userActivity == null) {
			return;
		}
		HttpSession session = request.getSession(false);
		session.setAttribute(USER_ACTICITY_DATA, userActivity);
	}

	protected UserActivities getUserActivityList() {
		HttpSession session = request.getSession(false);
		return session == null ? null :  (UserActivities) session.getAttribute(USER_ACTICITY_DATA);
	}

	// 设置产品Session
	protected void setProductList(List<?> products) {
		if(products == null || products.size() == 0) {
			return;
		}
		HttpSession session = request.getSession(false);
		session.setAttribute(SERIERS_PRODUCT, products);
	}

	protected List<?> getProductList() {
		HttpSession session = request.getSession(false);
		return session == null ? null :  (List<?>) session.getAttribute(SERIERS_PRODUCT);
	}

	// 设置所有柜台
	protected void setAllCounters(List<Counter> counters) {
		if(counters == null || counters.size() == 0) {
			return;
		}
		HttpSession session = request.getSession(false);
		session.setAttribute(ALL_COUNTERS, counters);
	}

	// 设置所有柜台
	protected void setAllCounters(HttpServletRequest request) {
		List<Counter> counters = new ArrayList<>();
		AuthUser authUser = super.getAuthUser();
		UserCustomer userCustomer = userCustomerManager.getByLoginName(authUser.getLoginName());
		Customer customer = customerManager.get(userCustomer.getCustomerId());
		Set<String> counterSet = userCustomer.getCounters();
		if (counterSet.isEmpty() && customer.getLevel() < 4) {
			counterSet = customerManager.findAllChildrenCounters(customer);
		}
		counters.addAll(counterManager.findByCounterId(counterSet));
		setAllCounters(counters);
	}

	// 取得所有的柜台
	@SuppressWarnings("unchecked")
	protected List<Counter> getAllCounters() {
		HttpSession sess = request.getSession(false);
		return sess == null ? null : (List<Counter>) sess.getAttribute(ALL_COUNTERS);
	}

	protected List<Integer> getAllCounterIds() {
		List<Counter> counters = (List<Counter>) getAllCounters();
		if(counters == null) {
			return null;
		}
		List<Integer> counterIds = new ArrayList<>();
		for (Counter counter : counters) {
			counterIds.add(counter.getCounterId());
		}
		return counterIds;
	}

	// 设置所有 待审核订单、待付款订单、待收货订单、财务退回 的数量
	protected void setAllOrderSize() throws Exception {
		HttpSession session = request.getSession(false);
		AuthUser authUser = super.getAuthUser();
		if(authUser == null) {
			throw new Exception("登录超时,重新登录");
		}
		UserCustomer userCustomer = userCustomerManager.getByLoginName(authUser.getLoginName());
		if(userCustomer == null || userCustomer.getCustomerId() == null) {
			throw new Exception("没有此用户,用户没有id");
		}
		Customer customer = customerManager.get(userCustomer.getCustomerId());

		Set<String> counterIdSet = userCustomer.getCounters();
		if (counterIdSet.isEmpty() && customer.getLevel() < 4) {
			counterIdSet = customerManager.findAllChildrenCounters(customer);
		}
		List<Integer> counterIds = new ArrayList<>(counterIdSet.size());
		for (String counterId : counterIdSet) {
			counterIds.add(Integer.parseInt(counterId));
		}
		List<Cart> owner = cartService.getCheckCarts(counterIds);
		List<Order> unPay = orderService.getOrders(counterIds, Order.Status_UnPay);
		List<Order> waitShip = orderService.getOrders(counterIds, Order.status_DoLogisticsDelivery);
		List<Order> refuse = orderService.getOrders(counterIds, Order.status_ForFinancialRefuse);
		List<Order> invalid = orderService.getOrders(counterIds, Order.Status_Invalid);
		List<Order> shipping = orderService.getOrders(counterIds, Order.status_WaitShip);
		List<Order> doPay = orderService.getOrders(counterIds, Order.Status_DoPay);
		List<Order> completed = orderService.getOrders(counterIds, Order.status_CompleteShipping);

		doPay.addAll(orderService.getOrders(counterIds, Order.status_LogisticsDelivery));
		all_order_size.put("invalid", invalid.size());
		all_order_size.put("owner", owner.size());
		all_order_size.put("unPay", unPay.size());
		all_order_size.put("doPay", doPay.size());
		all_order_size.put("waitShip", waitShip.size());
		all_order_size.put("shipping", shipping.size());
		all_order_size.put("refuse", refuse.size());
		all_order_size.put("completed", completed.size());
		session.setAttribute(ALL_ORDER_SIZE, all_order_size);
	}

	// 取得所有 待审核订单、待付款订单、待收货订单、财务退回 的数量
	public Map<String, Integer> getAllOrderSize() {
		HttpSession sess = request.getSession(false);
		@SuppressWarnings("unchecked")
		Map<String, Integer> map = (Map<String, Integer>) sess.getAttribute(ALL_ORDER_SIZE);
		return map;
	}

	// 拿到门店购物车之后将其转化为CartLocker并放入session
	protected void setCartLocker(CartLocker lock) {
		String userId = getAuthUser().getLoginName();
		HttpSession sess = request.getSession();
		CartLocker sessLock = (CartLocker) sess.getAttribute(CARTLOCK);
		if (sessLock != null) {
			if (sessLock.equals(lock)) {
				return;
			}
			// 拿到新锁后释放旧锁
			sessLock.unlock(userId);
			sess.removeAttribute(CARTLOCK);
		}
		// 从map里面拿CartLock
		CartLocker holdLock = cartLockers.get(lock.getCounterId());
		if (holdLock == null) {
			lock.lock(userId);
			cartLockers.put(lock.getCounterId(), lock);
			holdLock = lock;
		} else {
			holdLock.lock(userId);
		}
		sess.setAttribute(CARTLOCK, holdLock);
	}

	public static CartLocker getCartLocker(Integer countId) {
		return cartLockers.get(countId);
	}

	protected void removeCartLocker(Integer countId) {
		String myLoginID = super.getAuthUser().getLoginName();
		CartLocker lock = getCartLocker(countId);
		if (lock != null //
				&& !lock.isLocked(myLoginID)) {
			cartLockers.remove(countId);
		}
		request.getSession().removeAttribute(CARTLOCK);
	}

	protected boolean isLockByOther(CartLocker locker) {
		if (cartLockers == null || cartLockers.isEmpty()) {
			return false;
		}
		for (Integer i : cartLockers.keySet()) {
			CartLocker getLock = cartLockers.get(i);
			// 门店id相等并且cartlock是被其他人锁定就返回true
			if (getLock.getCounterId().equals(locker.getCounterId()) && getLock.isLocked(locker.getUserId())) {
				return true;
			}
		}
		return false;
	}

	// 设置头部和当前的柜台
	public void setOrderCounter(Counter counter) {
		if(counter == null || "".equals(counter)) {
			return;
		}
		HttpSession s = request.getSession(false);
		s.setAttribute(ORDERCOUNTER, counter);
	}

	// 清除ordercounter session
	public void removeOrderCounter() {
		request.getSession().removeAttribute(ORDERCOUNTER);
	}

	// 获取头部和当前的柜台
	public Counter getOrderCounter() {
		HttpSession s = request.getSession(false);
		return s == null ? null : (Counter) s.getAttribute(ORDERCOUNTER);
	}

	/**
	 * 如果店面的销售数据没有，是否考虑使用全国数据？
	 */
	@SuppressWarnings("unchecked")
	protected void setCounterSerial(CounterSerial c) {
		HttpSession sess = request.getSession(false);
		Map<String, CounterSerial> cs = (Map<String, CounterSerial>) sess.getAttribute(c.getCounterCode());
		if (cs == null) {
			cs = new HashMap<>();
			sess.setAttribute(MY_SERIERS, cs);
		}
		cs.put(c.getCounterCode(), c);
	}

	@SuppressWarnings("unchecked")
	protected CounterSerial getCounterSerial(String counterCode) {
		Map<String, CounterSerial> cs = (Map<String, CounterSerial>) request.getSession(false).getAttribute(MY_SERIERS);
		return cs == null ? null : cs.get(counterCode);
	}

	// 设置柜台号
	protected final void setCounterCode(String conterCode) {
		if(conterCode == null) {
			return;
		}
		HttpSession sess = request.getSession(false);
		sess.setAttribute(CURRCOUNTERCODE, conterCode);
	}

	// 取得柜台号
	protected final String getCounterCode() {
		HttpSession sess = request.getSession(false);
		return sess == null ? null : (String) sess.getAttribute(CURRCOUNTERCODE);
	}

	/**
	 * 更换店铺所在的库存位置 设置默认的仓库id
	 * 
	 */
	protected void setAutoMyStock() {
		HttpSession sess = request.getSession(false);
		Integer myStock = this.lockStock(this.getCounterCode());
		sess.setAttribute(MY_STOCK, myStock);
	}

	/**
	 * 取得默认仓库id
	 * 
	 * @return 默认仓库id
	 */
	protected Integer getMyStock() {
		HttpSession sess = request.getSession(false);
		return sess == null ? null : (Integer) sess.getAttribute(MY_STOCK);
	}

	//
	public Integer lockStock(String counterId) {
		// 获取当前柜台
		Counter counter = counterManager.getCounterByCode(counterId);
		return Integer.parseInt(counter.getWarehouses());
	}

	/**
	 * 设置一个全局都要使用的stockId
	 * 
	 * @return
	 */
	public void setResAttr(ModelMap mp) {
		mp.addAttribute("stockId", getMyStock());
	}
	
	
	/**
	 * 无货产品排序放在后面
	 * @author wangs
	 * 
	 */
	// TODO:按照以前的排序，无库存的产品放在后面，还要按照一个销量来排序。 前段控制？
//	private void sortZeroInvBehind(List<Product> products){
//		products.stream().sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());
//	}
	
	/**
	 * 设置默认的全国销量
	 * 
	 * @author wangs
	 * @param List
	 *            <Product>
	 * @return List<Product>
	 */
	// 设置销量
	protected void addDefaultQty(List<Product> products) {
		String counterCode;
		if (this.getAllCounters().size() > 1) {
			if (this.getCounterCode() == null) {
				counterCode = this.getDefaultCounter();
			} else {
				counterCode = this.getCounterCode();
			}
		} else {
			counterCode = this.getCounterCode();
		}
		List<ProductSeller> sellers;
		sellers = productSellerManager.getCounterProductSeller(counterCode);
		
		Map<Integer,ProductSeller> sellerMap = new HashMap<>();
		for (ProductSeller ps:sellers){
			sellerMap.put(ps.getPid(), ps);
		}
		
		for (Product p : products) {
			ProductSeller ps = sellerMap.get(p.getProductId());
			if (ps != null) {
				p.setQty(ps.getQty());
			}
		}
	}

	/**
	 * 讲商品进行活动设置处理
	 * 
	 * @author wangs
	 * @param List
	 *            <Product>
	 * @return List<Product>
	 */

	protected void changeActFlag(List<Product> pp) {
		UserActivities ua = this.getUserActivityList();
		if(ua == null) {
			return;
		}
		pp.forEach(p -> {
			Integer pid = p.getProductId();
			if (ua.isJoinProduct(pid)) {
				p.setActFlag(true);
			}
			if (ua.isContainsItself(pid)) {
				p.setActItself(true);
			}
		});
	}
	
	/**
	 * 讲商品进行预售设置处理
	 * 
	 * @author wangs
	 * @param List
	 *            <Product>
	 * @return List<Product>
	 */
	private void changePreFlag(List<Product> pp) {
		String stockid=this.getOrderCounter().getWarehouses();
		
		for (Product product : pp) {
			Integer pid = product.getProductId();
			if(reserveProductManager.isSelling(pid,stockid)) {
				// 正常0
				product.setPrePro(0);
			} else if(reserveProductManager.isReservable(pid, stockid)){
				// 可打欠1
				product.setPrePro(1);
			} else if(reserveProductManager.isReserving(pid,stockid)) {
				// 打欠中2
				product.setPrePro(2);
			} else {
				// 打欠结束 开始还欠
				product.setPrePro(3);
			}
		}
	}
	
	/**
	 * 包装产品
	 * 
	 * @author wangs
	 * @param products
	 * @return
	 */
	protected List<Product> wrapProduct(List<Product> products,ModelMap mp) {
		// 设置“活动商品”上图标
		changeActFlag(products);
		// 添加默认的销量
		addDefaultQty(products);
		// 添加stockId
		setResAttr(mp);
		// 是否是预售产品
		changePreFlag(products);
		Predicate<Product> pred = (p) -> p.getAvalible() == null ? false : p.getAvalible();
		return products.stream() //
				.filter(pred) //
				.limit(MAX_PNUM)//
				.collect(Collectors.toList());
	}
	
	/**
	 * 包装产品
	 * 
	 * @author wangs
	 * @param products
	 * @return
	 */
	protected List<Product> wrapProduct(List<Product> products) {
		// 设置“活动商品”上图标
		changeActFlag(products);
		// 添加默认的销量
		addDefaultQty(products);
		// 是否是预售产品
		changePreFlag(products);
		Predicate<Product> pred = (p) -> p.getAvalible() == null ? false : p.getAvalible();
		return products.stream() //
				.filter(pred) //
				.limit(MAX_PNUM)//
				.collect(Collectors.toList());
	}

	/**
	 * 根据用户去取base_customer表中取得【客户类型】所执行的折扣政策
	 * 
	 * @author wangs
	 * @throws ActionException 
	 */
	protected double getProductDiscount() throws ActionException {
		// UserCustomer
		if(super.getAuthUser() == null) {
			throw new ActionException("帐号异常 没有柜台，无权登录。");
		}
		UserCustomer userCustomer = userCustomerManager.getByLoginName(super.getAuthUser().getLoginName());
		int customerId = userCustomer.getCustomerId();
		// 客户
		Customer customer = customerManager.get(customerId);
		int userTypeId = customer.getCustomerTypeID();
		// 11387直营 11392运营商 11391加盟商
		CustomerPricePolicy customerPricePolicy = pricePolicyManager.getPolicy(userTypeId);
		return customerPricePolicy.getMaterialDiscount();
	}

	/**
	 * 活动类型为04的大礼包 金额判断，如果有礼包，并且满足金额条件，返回为真，
	 * 
	 * @author wangs
	 * 
	 * @param cart
	 *            购物车
	 * @return 是否通过检查
	 */
	protected boolean checkBigPkAmt(Cart cart) throws ActionException {
		// 加入大包裹校驗
		// 獲取購物車的全部
		List<CartDetail> cartDetails = cartService.getCartDetailByCart(cart);

		double bigPackAmt = 0;

		// 计算大礼包的总金额
		for (CartDetail cd : cartDetails) {
			String actId = cd.getActivityId();
			Activity activity;
			if (actId == null //
					|| (activity = activityManager.get(actId)) == null) {
				continue;
			}
			// 选择的大礼包的个数 以及 总额
			if (ActivityType.TYPE_BIGPACAKGE.equals(activity.getType())) {
				AmountRule rule = activity.getContext().getAmount();
				if (rule != null) {
					bigPackAmt += rule.getAmount() * cd.getQuantity();
				}
			}
		}

		// 如果小于0.01元，返回检查通过。
		if (bigPackAmt < 0.01) {
			return true;
		}

		// 购物车内总总金额
		double totalFee = 0.0;
		// FIXME: 10000001 :表示在活动里进行的活动,作用类似于配置文件（暂时用常量处理）
		Activity removeActiv = activityManager.get("10000001");
		// 取出活动中的减额产品
		Set<Integer> ignoreProducts = removeActiv.getContext().getActityGoods().getProductItem()
				.keySet();
		for (CartDetail cd : cartDetails) {
			// 累计总金额
			totalFee += cd.getPrice() * cd.getQuantity();

			// FIXME:计算去掉套装的计算.暂时使用10.10-10.25
			// 08活动套装暂时不计累计
			String actId;
			if ((actId = cd.getActivityId()) != null) {
				Activity activity = activityManager.get(actId);
				if (activity != null && ActivityType.TYPE_SUIT.equals(activity.getType())) {
					AmountRule rule = activity.getContext().getAmount();
					if (rule != null) {
						totalFee -= cd.getPrice() * cd.getQuantity();
					}
				}
			}

			// 过滤产品也不累计,产品ids ,不包含活动
			if (ignoreProducts != null && actId == null) {
				List<CartDetailDesc> CartDetailDescs = cd.getCartDetailsDesc();
				// 明细中循环取 ,循环她嵌套循环,但是只有一个,没有问题
				for (CartDetailDesc cartDetailDesc : CartDetailDescs) {
					Integer productId = cartDetailDesc.getProductId();
					if (ignoreProducts.contains(productId)) {
						// 减额计算 = 明细里的数量*产品会员价*外面产品购买的套数
						totalFee -= cartDetailDesc.getQuantity() //
								* productManager.get(productId).getMemberPrice()//
								* cd.getQuantity();
					}
				}
			}
		}

		// 取出客户的折扣
		double discount = getProductDiscount();
		// 计算物料的费用
		Map<String, Double>materialFees = cartService.getMaterialFee(cartDetails, discount);
		// 取出免费的物料
		Double mfree = materialFees.get(Cart.MATERIALFREE);
		
		double buyAmt = (totalFee - mfree) * discount;
		// 如果礼包中的金额<定货总价，通过检查
		logger.debug("礼包金额{},选购的金额{}", bigPackAmt, buyAmt);
		return bigPackAmt < buyAmt;
	}
	
	/**
	 * 系统赠送的大礼包
	 * @param cart
	 * @return
	 */
//	protected Map<Activity,Integer> suitOfSystemSendBigPk(Cart cart) {
//		List<CartDetail> cartDetails = cartService.getCartDetailByCart(cart);
//		// XXX : Map<Double,List<String>>  同一金额规则 可能有多个活动 目前只有
//		Map<Double,String> mapLevel = new HashMap<>();
//		// 活动 以及 套数
//		Map<Activity,Integer> actIdAndSuit = new HashMap<>();
//		// 必须是固定
//		List<Activity> acts = activityManager.findByType(ActivityType.TYPE_BIGPACAKGE);
//		if(acts.size() == 0) {
//			return 0;
//		}
//		for (Activity activity : acts) {
//			if(activity.isAutoAlloca()) {
//				// 金额规则
//			   double amtRule =	activity.getContext().getAmount().getAmount();
//			   // 金额规则 level 目前只是一一对应
//			   mapLevel.put(amtRule, activity.getActId());
//			};
//		}
//		// 购物车内总总金额
//		double totalFee = 0.0;
//		for (CartDetail cd : cartDetails) {
//			// 累计总金额
//			totalFee += cd.getPrice() * cd.getQuantity();
//			// 
//			List<CartDetailDesc> CartDetailDescs = cd.getCartDetailsDesc();
//			for (CartDetailDesc Desc : CartDetailDescs) {
//				productManager
//				Desc.getProductId();
//			}
//		}
//		// 取出客户的折扣
//		double discount = getProductDiscount();
//		// 计算物料的费用
//		Map<String, Double>materialFees = cartService.getMaterialFee(cartDetails, discount);
//		// 取出免费的物料
//		Double mfree = materialFees.get(Cart.MATERIALFREE);
//		double buyAmt = (totalFee - mfree) * discount;
//		return 0;
//	}
	/**
	 * 限制用户活动选购量
	 * 
	 * @author wangs
	 * 
	 * @return Map<String,Integer> 
	 * size = 0 表示 无活动  size> 0 标识有超出
	 */
	protected Map<String,Integer> beyondRecord(Integer customerId , List<CartDetail> cartDetails)throws ActionException {
		// 记录超出的记录 actID -> beyondQty
		Map<String,Integer> beyondRecord = new HashMap<>();
		// 用户自己选购的活动的统计套数 活动id -> 活动套数 
		Map<String , Integer> actId_suitQty = new HashMap<>();
		for (CartDetail cartDetail : cartDetails) {
			String actId = cartDetail.getActivityId();
			if(CartDetail.ActivityProduct.equals(cartDetail.getType())) {
				if(actId_suitQty.get(actId) == null ) {
					actId_suitQty.put(actId, cartDetail.getQuantity());
				} else {
					actId_suitQty.put(actId, //
							// XXX:累加运算 can use?
							actId_suitQty.get(actId) + cartDetail.getQuantity());
				}
			}
		}
		// 活动ID -> 正文
		Map<String, LimitContext> avalibleAct =	activityLimitManager.getLimitContextAvaliable(customerId);
		// 若其中任一为空 表示不限 或者 无活动商品不校验
		if(avalibleAct == null || actId_suitQty == null || actId_suitQty.size() == 0) {
			return beyondRecord ;
		}

		for (String actId : actId_suitQty.keySet()) {
			LimitContext limitContext = avalibleAct.get(actId);
			// 超出数量
			if(limitContext == null) {
				continue;
			}
			Integer beyondAmt = actId_suitQty.get(actId) - limitContext.getQty();
			if(beyondAmt > 0) {
				if(beyondRecord.get(actId) == null){
					beyondRecord.put(actId, beyondAmt);
				} else {
					beyondRecord.put(actId, //
							// 超出累额计算  can use?
							beyondRecord.get(actId) + beyondAmt);
				}
			}
		}
		return beyondRecord;
	}
	
}
