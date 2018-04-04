package com.zeusas.dp.ordm.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.service.SmsService;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.common.utils.Status;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.DateTime;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.bean.AreaOrderBean;
import com.zeusas.dp.ordm.bean.AreaOrderItem;
import com.zeusas.dp.ordm.bean.MergeOrderBean;
import com.zeusas.dp.ordm.bean.OrderBean;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.MaterialTemplate;
import com.zeusas.dp.ordm.entity.MonthPresent;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderCredentials;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.entity.PresentContext;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.service.ActivityLimitManager;
import com.zeusas.dp.ordm.service.BalanceService;
import com.zeusas.dp.ordm.service.Conditions;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.MaterialTemplateManager;
import com.zeusas.dp.ordm.service.MonthPresentManager;
import com.zeusas.dp.ordm.service.OrderCredentialsService;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.PackageService;
import com.zeusas.dp.ordm.service.RedeemPointService;
import com.zeusas.dp.ordm.service.UserCustomerManager;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.http.BasicController;

/**
 * 订单的Controller
 * 
 * @author fengx
 * @date 2016年12月6日 下午3:54:33
 */
@Controller
@RequestMapping("/orderadm")
public final class OrderController extends BasicController {

	static Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderCredentialsService orderCredentialsService;
	@Autowired
	private DictManager dictManager;
	@Autowired
	private CounterManager counterManager;
	@Autowired
	private CustomerManager customerManager;
	@Autowired
	private UserCustomerManager userCustomerManager;
	@Autowired
	private MonthPresentManager monthPresentManager;
	@Autowired
	private MaterialTemplateManager materialTemplateManager;
	@Autowired
	private BalanceService balanceService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private PackageService packageService;
	@Autowired
	private ActivityLimitManager activityLimitManager;
	@Autowired 
	private RedeemPointService redeemPointService;

	/**
	 * 未审核订单初始化
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/init")
	public String init(HttpServletRequest request) {
		List<OrderBean> obl = null;
		try {
			List<Order> ol = orderService.getOrders(Order.Status_DoPay)//
					.stream().filter(o -> !o.isMerger()).collect(Collectors.toList());
			// 时间倒序
			OrderSort(ol);
			obl = getBeans(ol);
			// 可用余额
			for (OrderBean ob : obl) {
				if ("直营".equals(ob.getCustomerType())) {
					ob.setUsefulBalance(0.0);
					continue;
				}
				Double usefulBalance = balanceService//
						.getUsefulBalance(TypeConverter.toInteger(ob.getOrder().getCustomerId()));
				ob.setUsefulBalance(usefulBalance);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		request.setAttribute("OrderBeanList", obl);
		return "/page/ordercheck";
	}

	/**
	 * 未审核订单分页
	 * 
	 * @param request
	 * @param page
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "/orderpage")
	@ResponseBody
	public List<OrderBean> orderPage(HttpServletRequest request,
			@RequestParam(value = "page", required = false) int page,
			@RequestParam(value = "num", required = false) int num) {

		List<OrderBean> obl = null;
		try {
			List<Order> ol = orderService.getOrders(Order.Status_DoPay);
			OrderSort(ol);

			int size = ol.size();
			int startNo = (page - 1) * num;
			int endNo = page * num < size ? page * num : size;
			ol = ol.subList(startNo, endNo);

			obl = getBeans(ol);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		request.setAttribute("page", page);
		return obl;
	}

	/**
	 * 已经审核订单初始化
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getchecked")
	public String getchecked(HttpServletRequest request) {
		AuthUser authUser = BasicController.getAuthUser(request);
		List<Order> ol;
		if (AuthUser.type_org.equals(authUser.getType())) {
			ol = getCheckedOrder();
		} else {
			ol = getCheckedOrder(authUser.getLoginName());
		}
		OrderSort(ol);
		int size = ol.size();
		int max = (size % 10 == 0) ? (size / 10) : (size / 10 + 1);
		if (size > 10) {
			ol = ol.subList(0, 10);
		}
		List<OrderBean> obl = getBeans(ol);
		request.setAttribute("max", max);
		request.setAttribute("page", 1);
		request.setAttribute("OrderBeanList", obl);
		return "/page/orderchecked";
	}

	/**
	 * 无效订单初始化
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getinvalid")
	public String getinvalid(HttpServletRequest request) {
		AuthUser authUser = BasicController.getAuthUser(request);
		List<Order> ol;
		String name = null;
		if (AuthUser.type_org.equals(authUser.getType())) {
			ol = findInvalidOrder(name);
		} else {
			ol = null;
		}
		OrderSort(ol);
		int size = ol.size();
		int max = (size - 1) / 10 + 1;
		if (size > 10) {
			ol = ol.subList(0, 10);
		}
		List<OrderBean> obl = getBeans(ol);
		request.setAttribute("max", max);
		request.setAttribute("page", 1);
		request.setAttribute("OrderBeanList", obl);
		return "/page/orderinvalid";
	}

	/**
	 * 无效订单分页
	 * 
	 * @param request
	 * @param page
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "/invalidOrderPage")
	@ResponseBody
	public List<OrderBean> invalidOrderPage(HttpServletRequest request,
			@RequestParam(value = "page", required = false) int page,
			@RequestParam(value = "num", required = false) int num,
			@RequestParam(value = "key", required = false) String name) {

		List<OrderBean> obl = null;
		try {
			List<Order> ol = findInvalidOrder(name);
			OrderSort(ol);
			int size = ol.size();
			int startNo = (page - 1) * num;
			int endNo = page * num < size ? page * num : size;
			ol = ol.subList(startNo, endNo);
			obl = getBeans(ol);
			request.setAttribute("page", page);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return obl;

	}

	/**
	 * 作废订单删除 点击删除按钮，更新状态，将状态置为status_NotDisplay
	 * 
	 * @param request
	 * @param orderNo
	 * @param page
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "/orderDelete", method = RequestMethod.POST)
	public String orderDelete(HttpServletRequest request, @RequestParam(value = "orderNo") String orderNo,
			@RequestParam(value = "page") Integer page, @RequestParam(value = "key", required = false) String name) {
		List<OrderBean> obl = null;
		try {
			Order o = orderService.getsingleOrder(orderNo);
			orderService.changeOrderStatus(o.getId(), Order.status_NotDisplay);// 将订单状态置为无效
			List<Order> ol = findInvalidOrder(name);
			OrderSort(ol);
			int size = ol.size();
			int max = (size - 1) / 10 + 1;
			if (size > 10) {
				ol = ol.subList(10 * (page - 1), page < max ? 10 * page : size);
			}
			obl = getBeans(ol);
			request.setAttribute("max", max);
			request.setAttribute("page", page);
			request.setAttribute("key", name);
			request.setAttribute("name", name);
			request.setAttribute("OrderBeanList", obl);

		} catch (ServiceException e) {
			logger.error("订单状态修改失败", e);
		}
		return "/page/orderinvalid";
	}

	// 查找无效订单
	@RequestMapping("/searchInvalidOrder")
	public String searchInvalidOrder(HttpServletRequest request,
			@RequestParam(value = "name", required = false) String name) {
		AuthUser authUser = BasicController.getAuthUser(request);
		List<Order> orders = new ArrayList<>();
		if (AuthUser.type_org.equals(authUser.getType())) {
			orders = findInvalidOrder(name);
		}
		OrderSort(orders);
		int size = orders.size();
		if (size > 10) {
			orders = orders.subList(0, 10);
		}
		List<OrderBean> orderBeans = getBeans(orders);
		request.setAttribute("OrderBeanList", orderBeans);
		int max = (size - 1) / 10 + 1;
		request.setAttribute("max", max);
		request.setAttribute("key", name);
		request.setAttribute("page", 1);
		request.setAttribute("name", name); //
		return "/page/orderinvalid";
	}

	private List<Order> findInvalidOrder(String name) {
		if (StringUtil.isEmpty(name)) //
			return new ArrayList<>();
		// FIX ME :orderService.findAll() ->orderService.getOrders(status)
		List<Order> invalid = orderService.getOrders(Order.Status_Invalid)//
				.stream()
				.filter(o -> o.getCustomerName().indexOf(name) > -1 //
						|| o.getOrderNo().indexOf(name) > -1)//
				.collect(Collectors.toList());
		return invalid;
	}

	private List<Order> getInvalidOrder() {
		// FIX ME :orderService.findAll() ->orderService.getOrders(status)
		List<Order> invalid = orderService.getOrders(Order.Status_Invalid);
		return invalid;
	}

	/**
	 * 无效审核初始化
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getreview")
	public String getreview(HttpServletRequest request) {
		AuthUser authUser = BasicController.getAuthUser(request);
		List<Order> ol = new ArrayList<>();
		if (AuthUser.type_org.equals(authUser.getType())) {
			ol = findReviewOrder("");
		}
		OrderSort(ol);
		int size = ol.size();
		int max = (size - 1) / 10 + 1;
		if (size > 10) {
			ol = ol.subList(0, 10);
		}
		List<OrderBean> obl = getBeans(ol);
		request.setAttribute("max", max);
		request.setAttribute("page", 1);
		request.setAttribute("OrderBeanList", obl);
		return "/page/orderreview";
	}

	/**
	 * 无效审核分页
	 * 
	 * @param request
	 * @param page
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "/reviewOrderPage")
	@ResponseBody
	public List<OrderBean> reviewOrderPage(HttpServletRequest request,
			@RequestParam(value = "page", required = false) int page,
			@RequestParam(value = "num", required = false) int num,
			@RequestParam(value = "key", required = false) String name) {

		List<Order> ol = findReviewOrder(name);
		OrderSort(ol);

		int size = ol.size();
		int startNo = (page - 1) * num;
		int endNo = page * num < size ? page * num : size;
		ol = ol.subList(startNo, endNo);
		List<OrderBean> obl = getBeans(ol);
		request.setAttribute("page", page);
		return obl;
	}

	// 查找无效审核
	@RequestMapping("/searchReviewOrder")
	public String searchReviewOrder(HttpServletRequest request,
			@RequestParam(value = "name", required = false) String name) {
		AuthUser authUser = BasicController.getAuthUser(request);
		List<Order> orders = new ArrayList<>();
		if (AuthUser.type_org.equals(authUser.getType())) {
			orders = findReviewOrder(name);
		}
		OrderSort(orders);
		int size = orders.size();
		if (size > 10) {
			orders = orders.subList(0, 10);
		}
		List<OrderBean> orderBeans = getBeans(orders);
		request.setAttribute("OrderBeanList", orderBeans);
		int max = (size - 1) / 10 + 1;
		request.setAttribute("max", max);
		request.setAttribute("key", name);
		request.setAttribute("name", name);
		request.setAttribute("page", 1);
		return "/page/orderreview";
	}

	/**
	 * 作废订单 点击无效按钮，更新状态，将状态置为Status_Invalid 将包裹状态置为10
	 * 
	 * @param request
	 * @param orderNo
	 * @param page
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "/orderInvalid", method = RequestMethod.POST)
	public String orderInvalid(HttpServletRequest request, @RequestParam(value = "orderNo") String orderNo,
			@RequestParam(value = "page") Integer page, @RequestParam(value = "key", required = false) String key) {
		try {
			List<OrderBean> obl;
			Order o = orderService.getsingleOrder(orderNo);
			orderService.changeOrderStatus(o.getId(), Order.Status_Invalid);// 订单置为无效
			// 退回活动限定套数
			List<OrderDetail> details = orderService.getOrderDetails(o.getOrderNo());
			activityLimitManager.releaseOrderToCart(o, details);
			
			packageService.changePackageStatus(orderNo, 10); // 将包裹Status置为10
			List<Order> ol = findReviewOrder(key);
			OrderSort(ol);
			int size = ol.size();
			int max = (size - 1) / 10 + 1;
			if (size > 10) {
				ol = ol.subList(10 * (page - 1), page < max ? 10 * page : size);
			}
			obl = getBeans(ol);
			request.setAttribute("max", max);
			request.setAttribute("page", page);
			request.setAttribute("key", key);
			request.setAttribute("name", key);
			request.setAttribute("OrderBeanList", obl);

		} catch (ServiceException e) {
			e.printStackTrace();
			logger.error("订单状态修改失败", e);
		}
		return "/page/orderreview";
	}

	private List<Order> findReviewOrder(String name) {
		if (StringUtil.isEmpty(name)) //
			return new ArrayList<>();
		// FIX ME :orderService.findAll() ->orderService.getOrders(status)
		List<String> orderStatus = new ArrayList<>();
		orderStatus.add(Order.status_DoLogisticsDelivery);
		orderStatus.add(Order.status_WaitShip);
		List<Order> review = orderService.getOrders(orderStatus)//
				.stream()
				.filter(o -> o.getCustomerName().indexOf(name) > -1 //
						|| o.getOrderNo().indexOf(name) > -1)//
				.collect(Collectors.toList());
		return review;
	}

	private List<Order> getReviewOrder() {
		// FIX ME :orderService.findAll() ->orderService.getOrders(status)\
		List<String> orderStatus = new ArrayList<>();
		orderStatus.add(Order.status_DoLogisticsDelivery);
		orderStatus.add(Order.status_WaitShip);
		List<Order> review = orderService.getOrders(orderStatus)//
				.stream()//
				.collect(Collectors.toList());
		return review;
	}

	/**
	 * 初始化当天订单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/areaorder")
	public String areaorder(HttpServletRequest request) {
		// 当天零点
		String date = DateTime.formatDate(DateTime.YYYY_MM_DD, new Date(System.currentTimeMillis()));
		AreaOrderBean areaOrderBean = getareaorder(request, date, date);
		request.setAttribute("areaOrderBean", areaOrderBean);
		return "/page/areaorder";
	}

	/**
	 * 运营商 代理商查看其加盟订单汇总
	 * 
	 * @param request
	 * @return
	 */
	// FIXME:优化
	@RequestMapping("/getareaorder")
	@ResponseBody
	public AreaOrderBean getareaorder(HttpServletRequest request,
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime) {

		Long start = DateTime.toDate(DateTime.YYYY_MM_DD, starttime).getTime();
		Long end = DateTime.toDate(DateTime.YYYY_MM_DD, endtime).getTime();

		end += 24 * 60 * 60 * 1000;
		// 直营的订单汇总
		AuthUser authUser = BasicController.getAuthUser(request);
		UserCustomer uc = userCustomerManager.getByLoginName(authUser.getLoginName());
		Customer myCustomer = customerManager.get(uc.getCustomerId());

		String customerCode = myCustomer.getCustomerCode();
		String firstCode = customerCode.substring(0, customerCode.indexOf("."));
		String myCustomerTypeId = myCustomer.getCustomerTypeID().toString();
		if (Customer.customerType_Operator.equals(myCustomerTypeId)) {
			firstCode = counterManager.getGroupCodeByCustomer(myCustomer);
		}

		Collection<Counter> counters = counterManager.findAll()//
				.stream().filter(e -> e.getStatus() && e.getCounterType() != null)//
				.collect(Collectors.toList());
		// 该运营商和他的加盟代理分销的所有柜台id
		Set<Integer> counterIds = new HashSet<>();

		for (Counter counter : counters) {
			if (counter.getCounterType().startsWith(firstCode)) {
				counterIds.add(counter.getCounterId());
			}
		}

		//FIXME:添加状态为9的
		Conditions conditions = new Conditions(counterIds, Order.status_WaitShip, start, end);
		List<Order> orders = orderService.findOrders(conditions).stream().filter(o -> o.getRealFee() != 0)
				.collect(Collectors.toList());

		AreaOrderItem myItem = new AreaOrderItem(myCustomer);
		Map<String, AreaOrderItem> agent = new LinkedHashMap<>();
		Map<String, AreaOrderItem> distributor = new LinkedHashMap<>();
		Map<String, AreaOrderItem> frOfOperator = new LinkedHashMap<>();
		Map<String, AreaOrderItem> frOfAgent = new LinkedHashMap<>();

		AreaOrderBean areaOrderBean = new AreaOrderBean();
		areaOrderBean.setMyItem(myItem);

		for (Order order : orders) {
			String customerId = order.getCustomerId();
			Customer customer = customerManager.get(Integer.parseInt(customerId));
			Counter counter = counterManager.get(order.getCounterId());
			String customerTypeId = customer.getCustomerTypeID().toString();
			String counterType = counter.getCounterType();

			if (customer.equals(myCustomer)) {
				myItem.addRealFee(order.getRealFee());
				myItem.addPaymentFee(order.getPaymentFee());
				myItem.incQty();
			}
			// 代理商
			if (Customer.customerType_Operator.equals(myCustomerTypeId)//
					&& Customer.customerType_Agent.equals(customerTypeId)) {
				if (!agent.containsKey(customerId)) {
					AreaOrderItem item = new AreaOrderItem(customer);
					agent.put(customerId, item);
				}
				agent.get(customerId).addRealFee(order.getRealFee());
				agent.get(customerId).addPaymentFee(order.getPaymentFee());
				agent.get(customerId).incQty();
				areaOrderBean.getSummary().addRealFee(order.getRealFee());
				areaOrderBean.getSummary().addPaymentFee(order.getPaymentFee());
				areaOrderBean.getSummary().incQty();
			}
			// 分销商
			if (Customer.customerType_Operator.equals(myCustomerTypeId)//
					&& Customer.customerType_Distributor.equals(customerTypeId)) {
				if (!distributor.containsKey(customerId)) {
					AreaOrderItem item = new AreaOrderItem(customer);
					distributor.put(customerId, item);
				}
				distributor.get(customerId).addRealFee(order.getRealFee());
				distributor.get(customerId).addPaymentFee(order.getPaymentFee());
				distributor.get(customerId).incQty();
				areaOrderBean.getSummary().addRealFee(order.getRealFee());
				areaOrderBean.getSummary().addPaymentFee(order.getPaymentFee());
				areaOrderBean.getSummary().incQty();
			}
			// 运营的加盟
			if (Customer.customerType_Operator.equals(myCustomerTypeId)//
					&& Customer.customerType_Franchisee.equals(customerTypeId)//
					&& counterType.startsWith(firstCode + ".J")) {
				if (!frOfOperator.containsKey(customerId)) {
					AreaOrderItem item = new AreaOrderItem(customer);
					frOfOperator.put(customerId, item);
				}
				frOfOperator.get(customerId).addRealFee(order.getRealFee());
				frOfOperator.get(customerId).addPaymentFee(order.getPaymentFee());
				frOfOperator.get(customerId).incQty();
				areaOrderBean.getSummary().addRealFee(order.getRealFee());
				areaOrderBean.getSummary().addPaymentFee(order.getPaymentFee());
				areaOrderBean.getSummary().incQty();
			}

			// 我是运营商 代理的加盟
			if (Customer.customerType_Operator.equals(myCustomerTypeId)//
					&& Customer.customerType_Franchisee.equals(customerTypeId)//
					&& counterType.startsWith(firstCode + ".D")//
					&& counterType.indexOf(".J") > 0) {
				if (!frOfAgent.containsKey(customerId)) {
					AreaOrderItem item = new AreaOrderItem(customer);
					frOfAgent.put(customerId, item);
				}
				frOfAgent.get(customerId).addRealFee(order.getRealFee());
				frOfAgent.get(customerId).addPaymentFee(order.getPaymentFee());
				frOfAgent.get(customerId).incQty();
				areaOrderBean.getSummary().addRealFee(order.getRealFee());
				areaOrderBean.getSummary().addPaymentFee(order.getPaymentFee());
				areaOrderBean.getSummary().incQty();
			}
			
			// 我是代理商 代理的加盟
			if (Customer.customerType_Agent.equals(myCustomerTypeId)//
					&& Customer.customerType_Franchisee.equals(customerTypeId)//
					&& counterType.startsWith(counterManager.getGroupCodeByCustomer(myCustomer))//
					&& counterType.indexOf(".J") > 0) {
				if (!frOfAgent.containsKey(customerId)) {
					AreaOrderItem item = new AreaOrderItem(customer);
					frOfAgent.put(customerId, item);
				}
				frOfAgent.get(customerId).addRealFee(order.getRealFee());
				frOfAgent.get(customerId).addPaymentFee(order.getPaymentFee());
				frOfAgent.get(customerId).incQty();
				areaOrderBean.getSummary().addRealFee(order.getRealFee());
				areaOrderBean.getSummary().addPaymentFee(order.getPaymentFee());
				areaOrderBean.getSummary().incQty();
			}
		}

		if (Customer.customerType_Operator.equals(myCustomerTypeId)//
				|| Customer.customerType_Agent.equals(myCustomerTypeId)) {
			areaOrderBean.setFrOfAgent(new ArrayList<>(frOfAgent.values()));
		}

		if (Customer.customerType_Operator.equals(myCustomerTypeId)) {
			areaOrderBean.setFrOfOperator(new ArrayList<>(frOfOperator.values()));
			areaOrderBean.setAgent(new ArrayList<>(agent.values()));
			areaOrderBean.setDistributor(new ArrayList<>(distributor.values()));
		}
		return areaOrderBean;
	}

	/**
	 * 运营商 代理商查看其加盟订单汇总页：单个客户汇总详细
	 * 
	 * @param request
	 * @param starttime
	 * @param endtime
	 * @param customerId
	 * @return
	 */
	@RequestMapping("/areaorderDetail")
	@ResponseBody
	public List<OrderBean> areaorderDetail(HttpServletRequest request,
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			@RequestParam(value = "customerId", required = false) Integer customerId) {

		Long start = DateTime.toDate(DateTime.YYYY_MM_DD, starttime).getTime();
		Long end = DateTime.toDate(DateTime.YYYY_MM_DD, endtime).getTime();
		// FIX ME:获取订单方式:跟根据客户的柜台获取 改为根据客户id获取 因为客户和柜台关系是变化的 订单中客户id是不变的
		List<String> orderStatus = new ArrayList<>();
		orderStatus.add(Order.status_WaitShip);
		List<Order> myOrders = orderService.findOrders(customerId.toString(), orderStatus, start, end);
		List<OrderBean> orderBeans = getBeans(myOrders);
		return orderBeans;
	}

	/**
	 * 已经审核订单分页
	 * 
	 * @param request
	 * @param page
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "/checkedOrderPage")
	@ResponseBody
	public List<OrderBean> checkedOrderPage(HttpServletRequest request,
			@RequestParam(value = "page", required = false) int page,
			@RequestParam(value = "num", required = false) int num,
			@RequestParam(value = "key", required = false) String key) {
		AuthUser authUser = BasicController.getAuthUser(request);
		List<Order> ol;
		if (!"".equals(key)) {
			if (AuthUser.type_org.equals(authUser.getType())) {
				ol = findCheckedOrder(key);
			} else {
				ol = findCheckedOrder(key, authUser.getLoginName());
			}
		} else {
			if (AuthUser.type_org.equals(authUser.getType())) {
				ol = getCheckedOrder();
			} else {
				ol = getCheckedOrder(authUser.getLoginName());
			}
		}
		OrderSort(ol);
		int size = ol.size();
		int startNo = (page - 1) * num;
		int endNo = page * num < size ? page * num : size;
		ol = ol.subList(startNo, endNo);
		List<OrderBean> obl = getBeans(ol);
		request.setAttribute("page", page);
		return obl;
	}

	/**
	 * 通过审核 返回当前分页
	 * 
	 * @param orderNo
	 * @param page
	 * @param num
	 * @param request
	 * @return
	 */
	@RequestMapping("/orderPass")
	@ResponseBody
	public List<OrderBean> orderPass(@RequestParam(value = "orderNo") String orderNo) {
		List<OrderBean> obl = null;
		// 10秒内，禁止重复提交
		HttpSession sess = request.getSession();
		Long t0 = (Long) sess.getAttribute(orderNo);
		Long t1 = System.currentTimeMillis();
		if (t0 != null && (t1 - t0) < 120000) {
			return null;
		}
		sess.setAttribute(orderNo, t1);
		try {

			Order order = orderService.getsingleOrder(orderNo);
			Counter counter = counterManager.get(order.getCounterId());
			
			//打欠产品生成记录
			orderService.saveReserveRecord(orderNo);
			// 添加月度物料
			addMaterial(counter, orderNo);
			// 添加还欠产品 预订会单子不随单还欠
			if (!Order.TYPE_RESERVEACTIVITY.equals(order.getReserveFlag())) {
				orderService.addReserveRecord(orderNo);
			}
			//积分兑换
			order=redeemPointService.addToOrder(order);
			// FIXME
			orderService.financialAudit(order, true);

			List<Order> ol = orderService.getOrders(Order.Status_DoPay);
			OrderSort(ol);
			obl = getBeans(ol);
		} catch (Exception e) {
			logger.error("获取订单错误", e);
		}
		return obl;
	}

	/**
	 * 审核不通过 更新状态 更新回退备注 返回当前分页
	 * 
	 * @param request
	 * @param orderNo
	 * @param refuseCommon
	 * @param page
	 * @param num
	 * @return
	 */
	@RequestMapping("/refuseOrder")
	@ResponseBody
	public List<OrderBean> refuseOrder(HttpServletRequest request, @RequestParam(value = "orderNo") String orderNo,
			@RequestParam(value = "refuseCommon") String refuseCommon) {
		List<OrderBean> obl = null;
		try {
			refuseCommon = refuseCommon.trim();
			Order order = orderService.getsingleOrder(orderNo);
			order.setCheckDesc(refuseCommon);
			orderService.financialAudit(order, false);

			// send sms to remake customer
			Customer customer = customerManager.get(Integer.parseInt(order.getCustomerId()));
			String template = "SMS_89020002";
			HashMap<String, String> ctx = new HashMap<String, String>();
			ctx.put("orderNo", order.getOrderNo());
			smsService.send(template, customer.getMobile(), ctx);

			List<Order> ol = orderService.getOrders(Order.Status_DoPay);
			OrderSort(ol);
			obl = getBeans(ol);
		} catch (ServiceException e) {
			logger.error("获取订单出错", e);
		}
		return obl;
	}

	/**
	 * 初始化合并订单页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/initmerge")
	public String initMergeOrder(HttpServletRequest request) {
		DSResponse dsResponse = new DSResponse();
		try {
			List<OrderCredentials> credentials = getMergeCredentials();
			// XXX:??倒序没成功
			credentials.stream().sorted(Comparator.comparing(OrderCredentials::getOcid).reversed());

			List<MergeOrderBean> beans = new ArrayList<>(credentials.size());
			for (OrderCredentials orderCredentials : credentials) {
				MergeOrderBean bean = new MergeOrderBean(orderCredentials);
				Double usefulBalance = balanceService
						.getUsefulBalance(TypeConverter.toInteger(orderCredentials.getCustomerId()));
				bean.setUsefulBalance(usefulBalance);
				beans.add(bean);
				Customer customer = customerManager.get(Integer.parseInt(orderCredentials.getCustomerId()));
				Assert.notNull(customer, "装配bean时获取customer为空,合并单号" + orderCredentials.getOcid());
				bean.setCustomerName(customer.getCustomerName());
			}
			dsResponse.setStatus(Status.SUCCESS);
			dsResponse.setData(beans);
			request.setAttribute("dsResponse", dsResponse);
		} catch (ServiceException e) {
			logger.error("初始化后台合并订单页错误", e);
		}
		return "/page/ordermerge";
	}

	/**
	 * 合并订单审核失败
	 * 
	 * @return
	 */
	@RequestMapping("/refuseCombineOrder")
	@ResponseBody
	public DSResponse refuseCombineOrder(HttpServletRequest request, @RequestParam(value = "orderNo") String ocid,
			@RequestParam(value = "refuseCommon") String refuseCommon) {
		List<Order> os = new ArrayList<>();
		DSResponse dsResponse = new DSResponse();
		try {

			OrderCredentials ocb = orderCredentialsService.get(ocid);
			if (ocb == null) {
				dsResponse.setMessage("获取凭证错误");
				return dsResponse;
			}
			refuseCommon = refuseCommon.trim();
			for (String s : ocb.getOrderNos()) {
				Order order = orderService.getsingleOrder(s);
				order.setCheckDesc(refuseCommon);
				os.add(order);
			}
			orderService.financialAuditCombine(os, false);

			// send sms to remake customer
			Customer customer = customerManager.get(Integer.parseInt(os.get(0).getCustomerId()));
			String template = "SMS_89020002";
			HashMap<String, String> ctx = new HashMap<String, String>();
			ctx.put("orderNo", "合并支付订单");
			smsService.send(template, customer.getMobile(), ctx);

			List<OrderCredentials> credentials = getMergeCredentials();
			dsResponse.setData(credentials);
			dsResponse.setStatus(Status.SUCCESS);
		} catch (ServiceException e) {
			dsResponse.setStatus(Status.FAILURE);
			logger.error("获取订单出错", e);
		}
		return dsResponse;
	}

	@RequestMapping("/passCombineOrder")
	@ResponseBody
	public DSResponse passCombineOrder(HttpServletRequest request, @RequestParam(value = "orderNo") String ocid) {
		final List<Order> orders = new ArrayList<>();
		DSResponse dsResponse = new DSResponse();
		// 10秒内，禁止重复提交
		HttpSession sess = request.getSession();
		Long t0 = (Long) sess.getAttribute(ocid);
		Long t1 = System.currentTimeMillis();
		if (t0 != null && (t1 - t0) < 10000) {
			return null;
		}
		sess.setAttribute(ocid, t1);
		try {

			OrderCredentials ocb = orderCredentialsService.get(ocid);
			if (ocb == null) {
				dsResponse.setResponseInfo(-1, "获取凭证错误");
				return dsResponse;
			}
			for (String orderNo : ocb.getOrderNos()) {
				
				Order order = orderService.getsingleOrder(orderNo);
				Counter counter = counterManager.get(order.getCounterId());
				
				//打欠产品生成记录
				orderService.saveReserveRecord(orderNo);
				// 添加物料()
				addMaterial(counter, order.getOrderNo());
				// 添加还欠产品 预订会单子不随单还欠
				if (!Order.TYPE_RESERVEACTIVITY.equals(order.getReserveFlag())) {
					orderService.addReserveRecord(orderNo);
				}
				//积分兑换
				redeemPointService.addToOrder(order);
				orders.add(order);
			}
			orderService.financialAuditCombine(orders, true);
			List<OrderCredentials> credentials = getMergeCredentials();
			dsResponse.setData(credentials);
			dsResponse.setStatus(Status.SUCCESS);
		} catch (ServiceException e) {
			dsResponse.setStatus(Status.FAILURE);
			logger.error("获取订单出错", e);
		}
		return dsResponse;
	}

	@RequestMapping("/searchCheckedOrder")
	public String searchCheckedOrder(HttpServletRequest request,
			@RequestParam(value = "name", required = false) String name) {
		AuthUser authUser = BasicController.getAuthUser(request);
		List<Order> oders;
		if (AuthUser.type_org.equals(authUser.getType())) {
			oders = findCheckedOrder(name);
		} else {
			oders = findCheckedOrder(name, authUser.getLoginName());
		}
		OrderSort(oders);
		int size = oders.size();
		if (size > 10) {
			oders = oders.subList(0, 10);
		}
		List<OrderBean> orderBeans = getBeans(oders);
		request.setAttribute("OrderBeanList", orderBeans);
		int max = (size % 10 == 0) ? (size / 10) : (size / 10 + 1);
		request.setAttribute("max", max);
		request.setAttribute("key", name);
		request.setAttribute("page", 1);
		return "/page/orderchecked";
	}

	/**
	 * 根据合并订单的父凭证获取订单
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/getMergeOrders")
	@ResponseBody
	public DSResponse getMergeOrders(@RequestParam(value = "id", required = false) String ocid) {
		DSResponse dsResponse = new DSResponse();
		OrderCredentials credentials = orderCredentialsService.get(ocid);
		if (credentials == null) {
			dsResponse.addMessage("获取合并订单父凭证错误");
			return dsResponse;
		}
		List<Order> orders = (List<Order>) orderService.findByCredentials(credentials);
		if (orders == null || orders.isEmpty()) {
			dsResponse.addMessage("获取所有订单错误");
			return dsResponse;
		}
		List<OrderBean> orderBeans = getBeans(orders);
		dsResponse.setData(orderBeans);
		return dsResponse;
	}

	/**
	 * 获取订单明细
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/orderDetail")
	@ResponseBody
	public OrderBean orderDetail(@RequestParam(value = "id", required = false) String id) {
		Order order = orderService.get(Long.parseLong(id));
		if (order == null) {
			return new OrderBean();
		}
		List<OrderDetail> orderDetails = orderService.getOrderDetails(order.getOrderNo());
		OrderBean orderBean = null;
		if (orderDetails != null && !orderDetails.isEmpty()) {
			orderBean = new OrderBean(order, orderDetails);
		}
		return orderBean == null ? new OrderBean() : orderBean;
	}

	/**
	 * 封装OrderBean 用于前台显示
	 * 
	 * @param ol
	 * @return
	 */
	private List<OrderBean> getBeans(List<Order> ol) {
		Dictionary status = dictManager.get(Order.Status_HardCode);
		Dictionary paytype = dictManager.get(Order.PayType_root);
		List<Dictionary> sl = status.getChildren();
		List<Dictionary> pl = paytype.getChildren();
		Map<String, String> statusMap = new HashMap<>(sl.size());
		Map<String, String> payMap = new HashMap<>(pl.size());
		for (Dictionary d : sl) {
			statusMap.put(d.getHardCode(), d.getName());
		}
		for (Dictionary p : pl) {
			payMap.put(p.getHardCode(), p.getName());
		}
		List<OrderBean> obl = new ArrayList<>();
		if (ol != null && ol.size() > 0) {
			for (Order o : ol) {
				OrderBean ob = new OrderBean(o);
				Counter counter = counterManager.getCounterByCode(o.getCounterCode());
				Customer customer = customerManager.get(counter.getCustomerId());
				ob.setCustomerName(customer.getCustomerName());
				ob.setCustomerType(customer.getCustomerType());
				ob.setOrderStatus(statusMap.get(o.getOrderStatus()));
				ob.setPayType(payMap.get(o.getPayTypeId()));
				obl.add(ob);
			}
		}

		return obl;
	}

	/**
	 * 获取经过财务审核后的订单
	 * 
	 * 待执行物流推送 status_LogisticsDelivery="5"; 物流已推送
	 * status_DoLogisticsDelivery="6"; 待物流发货 public final static String
	 * status_WaitShip="7"; 待门店接收货物 status_Shipping="8"; 确认完成订单
	 * status_CompleteShipping="9"; 财务审核不通过 status_ForFinancialRefuse="10";
	 * 
	 * @return
	 */
	private List<Order> getCheckedOrder() {
		Set<String> orderStatus = new HashSet<>();
		orderStatus.add(Order.status_LogisticsDelivery);
		orderStatus.add(Order.status_DoLogisticsDelivery);
		orderStatus.add(Order.status_WaitShip);
		orderStatus.add(Order.status_ForFinancialRefuse);

		return orderService.getOrders(orderStatus);
	}

	private List<Order> getCheckedOrder(String loginName) {
		List<Order> ol = new ArrayList<>();

		UserCustomer userCustomer = userCustomerManager.getByLoginName(loginName);
		Customer customer = customerManager.get(userCustomer.getCustomerId());
		Set<String> counterIdSet = userCustomer.getCounters();
		if (counterIdSet.isEmpty() && customer.getLevel() < 4) {
			counterIdSet = customerManager.findAllChildrenCounters(customer);
		}
		List<Integer> counterIds = counterIdSet.stream().map(e -> Integer.parseInt(e)).collect(Collectors.toList());
		List<Order> allOrder = orderService.getOrdersByCounterId(counterIds);
		for (Order order : allOrder) {
			if (checkValidStatus(order.getOrderStatus())) {
				ol.add(order);
			}
		}
		return ol;
	}

	private List<Order> findCheckedOrder(String name) {
		return getCheckedOrder().stream()//
				.filter(o -> o.getCounterName().indexOf(name) > -1 //
						|| o.getCustomerName().indexOf(name) > -1 //
						|| o.getOrderNo().indexOf(name) > -1)//
				.collect(Collectors.toList());
	}

	static boolean checkValidStatus(String status) {
		return (status.equals(Order.status_DoLogisticsDelivery)//
				|| status.equals(Order.status_CompleteShipping) //
				|| status.equals(Order.status_WaitShip)//
				|| status.equals(Order.status_LogisticsDelivery) //
				|| status.equals(Order.status_ForFinancialRefuse));
	}

	private List<Order> findCheckedOrder(String name, String loginName) {
		UserCustomer userCustomer = userCustomerManager.getByLoginName(loginName);
		List<Integer> counterIds = userCustomer.getCounters()//
				.stream().map(e -> Integer.parseInt(e))//
				.collect(Collectors.toList());

		return orderService.getOrdersByCounterId(counterIds)//
				.stream()//
				.filter(o -> o.getCounterName().indexOf(name) > -1 //
						|| o.getCustomerName().indexOf(name) > -1//
						|| o.getOrderNo().indexOf(name) > -1)//
				.collect(Collectors.toList());
	}

	/**
	 * 物料分配时，向定单中，追加中物料，XXX: 物料不收取费用
	 * 
	 * @param counter
	 * @param orderNo
	 */
	private void addMaterial(Counter counter, String orderNo) {
		String counterCode = counter.getCounterCode();
		int yearmonth=monthPresentManager.getyearmonth();
		MonthPresent monthPresent= monthPresentManager.findByMonthAndCode(yearmonth, counterCode);
		if(monthPresent!=null){
			monthPresentManager.excute(orderNo, monthPresent);
		}

		// 新店物料
		//XXX:未使用
//		Double area = counter.getArea();
//		Boolean newcounter = counter.getNewCounter();
//		// FIXME: 确认一下？
//		if (area != null && area != 0 && newcounter != null && newcounter) {
//			MaterialTemplate template = materialTemplateManager.getByArea(area);
//			if (template != null) {
//				counterManager.excute(counter.getCounterId(), orderNo);
//			}
//		}
	}

	private List<OrderCredentials> getMergeCredentials() {
		// 合并订单的凭证id
		Set<String> ocids = new HashSet<>();
		// 父凭证id
		Set<String> pids = new HashSet<>();
		List<OrderCredentials> credentials = new ArrayList<>();
		// 合并的订单
		orderService.getOrders(Order.Status_DoPay)//
				.stream().filter(o -> o.isMerger())//
				.forEach(o -> ocids.add(o.getCredentialsNo()));

		List<OrderCredentials> allCredentials = orderCredentialsService.getCredentials();
		for (OrderCredentials credential : allCredentials) {
			// 装配父凭证id
			if (ocids.contains(credential.getOcid())) {
				pids.add(credential.getCombineId());
			}
		}

		// 父凭证
		for (OrderCredentials credential : allCredentials) {
			if (pids.contains(credential.getOcid())) {
				credentials.add(credential);
			}
		}
		return credentials;
	}

	// 订单排序
	private void OrderSort(List<Order> orders) {
		Comparator<Order> cmp = (a, b) -> (int) (b.getId() - a.getId());
		Collections.sort(orders, cmp);
	}

}
