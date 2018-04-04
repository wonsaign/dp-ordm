package com.zeusas.dp.ordm.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zeusas.common.utils.DSResponse;
import com.zeusas.common.utils.Status;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.dp.ordm.bean.CustomerBean;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.entity.UserDetail;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.UserCustomerManager;
import com.zeusas.dp.ordm.service.UserCustomerService;
import com.zeusas.dp.ordm.utils.Hanyu;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.entity.OrgUnit;
import com.zeusas.security.auth.http.BasicController;
import com.zeusas.security.auth.service.AuthCenterManager;

/**
 * 客户
 * 
 * @author shihx
 * @date 2016年12月22日 下午3:01:01
 */
@Controller
@RequestMapping("/customer")
public class CustomerContronller {

	static Logger logger = LoggerFactory.getLogger(CustomerContronller.class);

	@Autowired
	private CounterManager counterManager;
	@Autowired
	private CustomerManager customerManager;
	@Autowired
	private AuthCenterManager authCenterManager;
	@Autowired
	private UserCustomerManager userCustomerManager;
	@Autowired
	private UserCustomerService userCustomerService;

	/**
	 * 客户页初始化
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/init")
	public String showCustomer(HttpServletRequest request) {
		List<Customer> customers = getCustomers(request);
		List<CustomerBean> cbl = getcustomerBean(customers);
		request.setAttribute("CustomerBeanList", cbl);
		return "/page/customer";
	}

	/**
	 * 客户搜索 返回长度为分页长度的List<Customer> 关键字name
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping("/searchcustomer")
	public String searchCustomer(HttpServletRequest request,
			@RequestParam(value = "name", required = false) String name) {
		List<Customer> customers = null;
		Integer userTtype = BasicController.getAuthUser(request).getType();
		if (AuthUser.type_org.equals(userTtype)) {
			customers = customerManager.findByName(name);
		} else {
			List<Customer> myCustomers = getCustomers(request);
			List<Customer> customersByName = customerManager.findByName(name);
			for (Customer customer : customersByName) {
				if (myCustomers.contains(customer)) {
					customers.add(customer);
				} else {
					customers = new ArrayList<>(0);
				}
			}
		}
		int size = customers.size();
		if (customers.size() > 10) {
			customers = customers.subList(0, 10);
		}
		List<CustomerBean> cbl = getcustomerBean(customers);
		request.setAttribute("CustomerBeanList", cbl);
		int max = (size % 10 == 0) ? (size / 10) : (size / 10 + 1);
		request.setAttribute("max", max);
		request.setAttribute("key", name);
		request.setAttribute("page", 1);
		return "/page/customer";
	}

	/**
	 * 客户页面 分页ajax
	 * 
	 * @param request
	 * @param page
	 * @param num
	 * @param key
	 * @return
	 */
	@RequestMapping(value = "/customerpage")
	@ResponseBody
	public List<CustomerBean> customerPage(HttpServletRequest request,
			@RequestParam(value = "page", required = false) int page,
			@RequestParam(value = "num", required = false) int num,
			@RequestParam(value = "key", required = false) String key) {
		List<Customer> cl = new ArrayList<Customer>(num);
		request.setAttribute("page", page);
		if (!"".equals(key)) {
			cl = customerManager.findByName(key);
			int size = cl.size();
			int startNo = (page - 1) * num;
			int endNo = page * num < size ? page * num : size;
			cl = cl.subList(startNo, endNo);
		} else {
			cl = customerManager.pagination(page, num);
		}
		List<CustomerBean> cbl = getcustomerBean(cl);
		return cbl;
	}

	@RequestMapping(value = "/createBtn")
	@ResponseBody
	public DSResponse createUserBtn(HttpServletRequest request,
			@RequestParam(value = "customerId", required = false) int customerId) {
		DSResponse dsResponse = new DSResponse();
		dsResponse.setStatus(Status.FAILURE);
		// 判断客户是否已经创建客户
		UserCustomer userCustomer = userCustomerManager.getCustomerById(customerId);
		if (userCustomer != null) {
			dsResponse.setMessage("该客户已经创建用户，用户名:" + userCustomer.getLoginName());
			return dsResponse;
		}
		// 客户是否有柜台
		Customer customer = customerManager.get(customerId);
		Assert.notNull(customer);
		if (!customer.getStatus() || customer.getCounters().isEmpty()) {
			dsResponse.setMessage("客户被禁用或客户没有柜台");
			return dsResponse;
		}
		// 柜台是否在店务通组织机构中
		Integer orgid = getOrgId(customer);
		if (orgid == null) {
			dsResponse.setMessage("客户柜台在店务通组织机构没数据");
			return dsResponse;
		}
		// FIXME: shortNameMap 数据来源表需要更换 并放到内存中 目前先为空写后续代码；
		Map<String, String> shortNameMap = new HashMap<>();
		String loginName = customerNamePinyin(shortNameMap, customer);
		dsResponse.setStatus(Status.SUCCESS);
		dsResponse.setMessage("预计客户登录名:");
		dsResponse.setData(loginName);
		return dsResponse;
	}

	@RequestMapping(value = "/create")
	@ResponseBody
	public DSResponse createUser(HttpServletRequest request,
			@RequestParam(value = "customerId", required = false) int customerId) {
		DSResponse dsResponse = new DSResponse();
		dsResponse.setStatus(Status.FAILURE);
		// FIXME: shortNameMap 数据来源表需要更换 并放到内存中 目前先为空写后续代码；
		Map<String, String> shortNameMap = new HashMap<>();

		Customer customer = customerManager.get(customerId);
		if (Customer.customerType_Operator.equals(customer.getCustomerTypeID().toString())) {
			dsResponse.setMessage("运营商需要手动创建");
			return dsResponse;
		}

		AuthUser authUser = new AuthUser();
		// 组织机构id
		Integer orgId = getOrgId(customer);
		// 登录名
		String loginName = customerNamePinyin(shortNameMap, customer);
		int i = 2;
		while (authCenterManager.getAuthUser(loginName + i++) != null) {
		}
		loginName = loginName + (i-1);
		// 密码明文
		String password = getFixLenthString(6);

		authUser.setLoginName(loginName);
		authUser.setPassword(password);
		authUser = initAuthUser(customer, authUser, orgId);

		UserDetail userDetail=initUserDetail(authUser, customer);
		UserCustomer userCustomer =initUserCustomer(customer, authUser);
		
		try {
			userCustomerService.creatAuthuserForCustomer(authUser, userDetail, userCustomer);
			authCenterManager.loadAuthUser();
			userCustomerManager.load();
			
			dsResponse.setStatus(Status.SUCCESS);
			dsResponse.setMessage("创建帐号成功，帐号："+loginName+"密码："+password);
		} catch (Exception e) {
			logger.error("为客户创建帐号错误，客户："+customer.getCustomerName(),e);
		}
		return dsResponse;
	}
	/**
	 * 获取子客户
	 * @param request
	 * @return
	 */
	private List<Customer> getCustomers(HttpServletRequest request) {
		String loginName = request.getRemoteUser();
		AuthUser user = BasicController.getAuthUser(request);
		Assert.notNull(user);
		List<Customer> customers = new ArrayList<>();
		if (AuthUser.type_org.equals(user.getType())) {
			// org类型 看到所有客户
			customers.addAll(customerManager.findAll());
		} else if (AuthUser.type_customer.equals(user.getType())) {
			// 从customer树往下查
			UserCustomer userCustomer = userCustomerManager.getByLoginName(loginName);
			Customer customer = customerManager.get(userCustomer.getCustomerId());
			Assert.notNull(customer);
			customers.add(customer);
			if (customerManager.findAllChildren(customer) != null) {
				customers.addAll(customerManager.findAllChildren(customer));
			}
		}
		return customers;
	}
	/**
	 * 封装bean
	 * @param cl
	 * @return
	 */
	private List<CustomerBean> getcustomerBean(List<Customer> cl) {
		List<CustomerBean> cbl = new ArrayList<>(cl.size());
		CustomerBean cb = null;
		Customer cu = new Customer();
		for (Customer c : cl) {
			BeanDup.dup(customerManager.get(c.getCustomerID()), cu);
			cb = new CustomerBean(cu);
			if (userCustomerManager.getCustomerById(cu.getCustomerID()) != null) {
				cb.setCreateUser(true);
			}
			cbl.add(cb);
		}
		return cbl;
	}

	/**
	 * 获取客户的组织机构id
	 * 
	 * @param customer
	 * @return
	 */
	private Integer getOrgId(Customer customer) {
		Integer orgid = null;
		Set<String> counterIds = customer.getCounters();
		if (!counterIds.isEmpty()) {
			for (String id : counterIds) {
				Counter counter = counterManager.get(Integer.parseInt(id));
				if (counter == null) {
					continue;
				}
				OrgUnit org = authCenterManager.getOrgUnitByCode(counter.getCounterCode());
				if (org != null) {
					orgid = org.getOrgId();
					break;
				}
			}
		}
		return orgid;
	}

	/**
	 * 客户名称拼音化
	 * 
	 * @param regionMap
	 *            key:地区名 value:国标缩写
	 * @param customerName
	 * @return
	 * @throws SQLException
	 */
	private String customerNamePinyin(Map<String, String> regionMap, Customer customer) {

		Hanyu hanyu = new Hanyu();

		String loginName = null;

		// 加盟为拼音 直营为柜台号
		if (Customer.customerType_Direct.equals(customer.getCustomerTypeID().toString())//
				&& customer.getChildren().isEmpty()) {

			List<Counter> cl = counterManager.getCounterByCustomerId(customer.getCustomerID());
			loginName = cl.get(0).getCounterCode();
		}
		// FIXME:分销没有数据
		if (Customer.customerType_Franchisee.equals(customer.getCustomerTypeID().toString())//
				|| Customer.customerType_Agent.equals(customer.getCustomerTypeID().toString())) {

			String customerName = customer.getCustomerName();
			// 两人合开的用户如:湖南某某某/某某某 取前面名字
			if (customerName.indexOf('/') >= 0) {
				customerName = customerName.substring(0, customerName.indexOf('/'));
			}

			// 省的长度 北京=2 黑龙江=3 (内蒙=2 装备 map<省名,国标缩写>时特殊处理)
			Integer indexOfProvince = 2;
			while (indexOfProvince < 4) {
				// 湖南长沙XXXX
				if (regionMap.containsKey(customerName.substring(0, indexOfProvince))) {
					// 长度为省名长度+2 例:湖南李某 黑龙江李某 国标+全拼
					if (customerName.length() == indexOfProvince + 2) {
						loginName = regionMap.get(customerName.substring(0, indexOfProvince))
								+ hanyu.getStringPinYin(customerName.substring(indexOfProvince));
					}
					// 长度为省名长度+3 例:湖南李某某 黑龙江李某某 国标+姓氏全拼+名字缩写
					if (customerName.length() == indexOfProvince + 3) {
						loginName = regionMap.get(customerName.substring(0, indexOfProvince))
								+ hanyu.getStringPinYin(customerName.substring(indexOfProvince, indexOfProvince + 1))
								+ hanyu.getFirstSpell(customerName.substring(indexOfProvince + 1), 0);
					}
					// 长度大于省名长度+2（3） 例:江苏欧阳云飞 jsoyyf
					if (customerName.length() > indexOfProvince + 3) {
						loginName = regionMap.get(customerName.substring(0, indexOfProvince))
								+ hanyu.getFirstSpell(customerName.substring(indexOfProvince), 0);
					}
				}
				if (loginName != null) {
					break;
				}
				indexOfProvince++;
			}
			// 国标表里没有 着全拼音缩写
			if (loginName == null) {
				loginName = hanyu.getFirstSpell(customerName, 0);
			}
		}

		return loginName;
	}

	/**
	 * 返回长度为【strLength】的随机数，在前面补0 用于生成密码
	 */
	private static String getFixLenthString(int len) {
		char[] cc = { '0', '1', '2', '3', '5', '6', '8', '9' };
		StringBuilder b = new StringBuilder();
		Random rm = new Random();
		for (int i = 0; i < len; i++) {
			int idx = rm.nextInt(cc.length);
			b.append(cc[idx]);
		}
		return b.toString();
	}
	/**
	 * 初始化authUser
	 * @param customer
	 * @param authUser
	 * @param orgId
	 * @return
	 */
	private AuthUser initAuthUser(Customer customer, AuthUser authUser, Integer orgId) {
		authUser.setCommonName(customer.getCustomerName());
		authUser.setOrgUnit(orgId);
		Set<String> roles = new LinkedHashSet<>();
		if ("直营".equals(customer.getCustomerType()) && customer.getChildren().isEmpty()) {
			roles.add("10");
			authUser.setRoles(roles);
		} else {
			roles.add("10");
			roles.add("11");
			roles.add("12");
			roles.add("13");
			roles.add("14");
			authUser.setRoles(roles);
		}

		authUser.setType(AuthUser.type_customer);
		authUser.setStatus(AuthUser.user_enable);
		authUser.setCreateTime(System.currentTimeMillis());
		authUser.setLastUpdate(System.currentTimeMillis());

		return authUser;
	}
	/**
	 * 初始化customer
	 * @param authUser
	 * @param customer
	 * @return
	 */
	private UserDetail initUserDetail(AuthUser authUser, Customer customer) {
		UserDetail detail = new UserDetail();
		detail.setName(customer.getCustomerName());
		detail.setCustomerType(customer.getCustomerType());
		detail.setProvince(customer.getProvince());
		if (customer.getCity() != null) {
			detail.setCity(customer.getCity());
		}
		if (customer.getAreaCounty() != null) {
			detail.setAreaCounty(customer.getAreaCounty());
		}
		if (customer.getMobile() != null) {
			detail.setMobile(customer.getMobile());
		}
		if (customer.getPhone() != null) {
			detail.setPhone(customer.getPhone());
		}
		if (customer.getPostCode() != null) {
			detail.setPhone(customer.getPostCode());
		}
		detail.setStatus(true);
		detail.setLastUpdate(System.currentTimeMillis());
		return detail;
	}
	/**
	 * 初始化UserCustomer
	 * @param customer
	 * @param authUser
	 * @return
	 */
	private UserCustomer initUserCustomer(Customer customer, AuthUser authUser){
		UserCustomer userCustomer = new UserCustomer();

		userCustomer.setLoginName(authUser.getLoginName());
		userCustomer.setCustomerId(customer.getCustomerID());
		userCustomer.setCustomerLoginName(authUser.getLoginName());
		userCustomer.setCounters(customer.getCounters());
		userCustomer.setStatus(1);
		userCustomer.setLastUpdate(System.currentTimeMillis());

		return userCustomer;
	}
}
