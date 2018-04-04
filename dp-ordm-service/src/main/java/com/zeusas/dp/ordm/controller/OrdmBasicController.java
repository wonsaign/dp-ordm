package com.zeusas.dp.ordm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import com.zeusas.dp.ordm.active.bean.UserActivities;
import com.zeusas.dp.ordm.bean.CartLocker;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.CounterSerial;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.service.CartService;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.UserCustomerManager;
import com.zeusas.dp.ordm.utils.StockArea;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.http.BasicController;

public abstract class OrdmBasicController extends BasicController implements StockArea{
	public static String MY_SERIERS = "TIsnFmCl";
	public static String MY_PRODUCTS = "datVZUev";
	public static String USERDETAILS = "fissSoFF";
	public static String CURRCOUNTERCODE = "QtNImh4d";
	public static String ORDERCOUNTER = "ElyX7rAc";
	public static String ALL_COUNTERS = "VfImmHQb";
	public static String CURRIMG = "qZrW4SN6";
	public static String SERIERS_PRODUCT = "nUBzD2BU";
	public static String NOTIFICATION = "oeUwFngk";
	public static String DEFAULTCOUNTER = "oe8QFngk";
	public static String ALL_ORDER_SIZE = "lNmwzm1B";
	protected static String CARTLOCK = "hwcR3F";
	public static String USER_ACTICITY_DATA = "nAw8BdI9";
	public static String MY_STOCK = "v3Wz58Bi";
	
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
	
	protected static final Map<Integer, CartLocker> cartLockers = new HashMap<Integer, CartLocker>();
	protected static final Map<String,Integer> all_order_size = new HashMap<String,Integer>();
	
	//设置系统消息
	protected void setDefaultCounter(String counterCode){
		HttpSession session = request.getSession(false);
		session.setAttribute(DEFAULTCOUNTER,counterCode);
	}
	protected String getDefaultCounter() {
		HttpSession session = request.getSession(false);
		return (String) session.getAttribute(DEFAULTCOUNTER);
	}
	
	
	//设置系统消息
	protected void setNotification(List<?> notification){
		HttpSession session = request.getSession(false);
		session.setAttribute(NOTIFICATION,notification);
	}
	
	protected List<?> getNotification() {
		HttpSession session = request.getSession(false);
		return (List<?>) session.getAttribute(NOTIFICATION);
	}
	
	//设置活动usersession
	protected void setUserActivityList(UserActivities userActivity) {
		HttpSession session = request.getSession(false);
		session.setAttribute(USER_ACTICITY_DATA,userActivity);
	}
	
	protected UserActivities getUserActivityList() {
		HttpSession session = request.getSession(false);
		return (UserActivities) session.getAttribute(USER_ACTICITY_DATA);
	}
	
	//设置产品Session
	protected void setProductList(List<?> products) {
		HttpSession session = request.getSession(false);
		session.setAttribute(SERIERS_PRODUCT,products);
	}
	
	protected List<?> getProductList() {
		HttpSession session = request.getSession(false);
		return (List<?>) session.getAttribute(SERIERS_PRODUCT);
	}
	
	// 设置所有柜台
	protected void setAllCounters(List<?> counters) {
		HttpSession session = request.getSession(false);
		session.setAttribute(ALL_COUNTERS, counters);
	}

	// 设置所有柜台
	protected void setAllCounters(HttpServletRequest request) {
		List<Counter> counters = new ArrayList<>();
		AuthUser authUser = super.getAuthUser();
		UserCustomer userCustomer=userCustomerManager.getByLoginName(authUser.getLoginName());
		Customer customer= customerManager.get(userCustomer.getCustomerId());
		Set<String> counterSet=userCustomer.getCounters();
		if(counterSet.isEmpty()&&customer.getLevel()<4){
			counterSet=customerManager.findAllChildrenCounters(customer);
		}
		counters.addAll(counterManager.findByCounterId(counterSet));
		setAllCounters(counters);
	}
	
	// 取得所有的柜台
	protected List<?> getAllCounters() {
		HttpSession sess = request.getSession(false);
		return (List<?>) sess.getAttribute(ALL_COUNTERS);
	}
	
	protected List<Integer> getAllCounterIds() {
		List<Counter> counters=(List<Counter>) getAllCounters();
		List<Integer> counterIds=new ArrayList<>(counters.size());
		for (Counter counter : counters) {
			counterIds.add(counter.getCounterId());
		}
		return counterIds;
	}
	
	// 设置所有 待审核订单、待付款订单、待收货订单、财务退回 的数量
	protected void setAllOrderSize() {
		HttpSession session = request.getSession(false);
		AuthUser authUser = super.getAuthUser();
		UserCustomer userCustomer = userCustomerManager
				.getByLoginName(authUser.getLoginName());
		Customer customer= customerManager.get(userCustomer.getCustomerId());
		//FIXME
		Set<String> counterIdSet= userCustomer.getCounters();
		if(counterIdSet.isEmpty()&&customer.getLevel()<4){
			counterIdSet=customerManager.findAllChildrenCounters(customer);
		}
		List<Integer> counterIds=new ArrayList<>(counterIdSet.size());
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
		
		doPay.addAll(orderService.getOrders(counterIds, Order.status_LogisticsDelivery));
		all_order_size.put("invalid",invalid.size());
		all_order_size.put("owner",owner.size());
		all_order_size.put("unPay",unPay.size());
		all_order_size.put("doPay",doPay.size());
		all_order_size.put("waitShip",waitShip.size());
		all_order_size.put("shipping",shipping.size());
		all_order_size.put("refuse",refuse.size());
		session.setAttribute(ALL_ORDER_SIZE, all_order_size);
	}

	// 取得所有 待审核订单、待付款订单、待收货订单、财务退回 的数量
	public Map<String,Integer> getAllOrderSize() {
		HttpSession sess = request.getSession(false);
		@SuppressWarnings("unchecked")
		Map<String,Integer> map = (Map<String,Integer>) sess.getAttribute(ALL_ORDER_SIZE);
		return  map;
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
	
	protected boolean isLockByOther(CartLocker locker){
		if(cartLockers==null||cartLockers.isEmpty()){
			return false;
		}
		for(Integer i:cartLockers.keySet()){
			CartLocker getLock=cartLockers.get(i);
			//门店id相等并且cartlock是被其他人锁定就返回true
			if(getLock.getCounterId().equals(locker.getCounterId())
					&&getLock.isLocked(locker.getUserId())){
				return true;
			}
		}
		return false;
	}
	
	// 设置头部和当前的柜台
	public void setOrderCounter(Counter counter) {
		HttpSession s = request.getSession(false);
		s.setAttribute(ORDERCOUNTER, counter);
	}
	
	// 清除ordercounter session
	public void removeOrderCounter(){
		request.getSession().removeAttribute(ORDERCOUNTER);
	}

	// 获取头部和当前的柜台
	public Counter getOrderCounter() {
		HttpSession s = request.getSession(false);
		return (Counter) s.getAttribute(ORDERCOUNTER);
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
		HttpSession sess = request.getSession(false);
		sess.setAttribute(CURRCOUNTERCODE, conterCode);
	}

	// 取得柜台号
	protected final String getCounterCode() {
		HttpSession sess = request.getSession(false);
		return (String) sess.getAttribute(CURRCOUNTERCODE);
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
		return (Integer) sess.getAttribute(MY_STOCK);
	}
	
	//
	public Integer lockStock(String counterId){
		// 获取当前柜台
		Counter counter = counterManager.getCounterByCode(counterId);
		return Integer.parseInt(counter.getWarehouses());
	}
	
	/**
	 * 设置一个全局都要使用的stockId
	 * @return
	 */
	public void setResAttr(ModelMap mp){
		mp.addAttribute("stockId",getMyStock());
	}
	
}
