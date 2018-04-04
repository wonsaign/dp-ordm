package com.zeusas.dp.ordm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zeusas.core.utils.BeanDup;
import com.zeusas.dp.ordm.bean.CounterBean;
import com.zeusas.dp.ordm.bean.CustomerBean;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.UserCustomerManager;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.http.BasicController;
import com.zeusas.security.auth.service.AuthCenterManager;

/**
 * 客户 柜台
 * 
 * @author shihx
 * @date 2016年12月22日 下午3:01:01
 */
@Controller
@RequestMapping("/counter")
public class CounterContronller {

	static Logger logger = LoggerFactory.getLogger(CounterContronller.class);

	@Autowired
	private CounterManager counterManager;
	@Autowired
	private CustomerManager customerManager;
	@Autowired
	private AuthCenterManager authCenterManager;
	@Autowired
	private UserCustomerManager userCustomerManager;

	/**
	 * 柜台页初始化
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/init")
	public String showCounter(HttpServletRequest request) {
		List<Counter> counters = getCounters(request);
		// int size = counters.size();
		// int max = (size % 10 == 0) ? (size / 10) : (size / 10 + 1);
		// List<Counter> cl = counters.size()>10?counters.subList(0,
		// 10):counters;
		// List<CounterBean> cbl = getCounterBean(cl);
		// request.setAttribute("max", max);
		// request.setAttribute("page", 1);
		List<CounterBean> cbl = getCounterBean(counters);
		request.setAttribute("CounterBeanlist", cbl);
		return "/page/counter";
	}

	/**
	 * 返回长度为分页长度的List<Counter> 关键字name
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping("/searchcounter")
	public String searchCounter(HttpServletRequest request,
			@RequestParam(value = "name", required = false) String name) {
		List<Counter> serchCounter = counterManager.findByName(name);
		List<Counter> chlidCounters = getCounters(request);
		List<Counter> returnCounter = new ArrayList<>(serchCounter.size());
		for (Counter counter : serchCounter) {
			if (chlidCounters.contains(counter)) {
				returnCounter.add(counter);
			}
		}
		int size = returnCounter.size();
		if (returnCounter.size() > 10) {
			returnCounter = returnCounter.subList(0, 10);
		}
		List<CounterBean> cbl = getCounterBean(returnCounter);
		request.setAttribute("CounterBeanlist", cbl);
		int max = (size % 10 == 0) ? (size / 10) : (size / 10 + 1);
		request.setAttribute("max", max);
		request.setAttribute("key", name);
		request.setAttribute("page", 1);
		return "/page/counter";
	}

	/**
	 * 柜台分页
	 * 
	 * @param request
	 * @param page
	 * @param num
	 * @param key
	 * @return
	 */
	@RequestMapping(value = "/counterpage")
	@ResponseBody
	public List<CounterBean> counterPage(HttpServletRequest request,
			@RequestParam(value = "page", required = false) int page,
			@RequestParam(value = "num", required = false) int num,
			@RequestParam(value = "key", required = false) String key) {
		int size;
		int startNo = (page - 1) * num;
		int endNo;
		List<Counter> chlidCounters = getCounters(request);
		List<Counter> returnCounter = new ArrayList<>();
		request.setAttribute("page", page);
		if (!"".equals(key)) {
			List<Counter> serchCounter = counterManager.findByName(key);
			for (Counter counter : serchCounter) {
				if (chlidCounters.contains(counter)) {
					returnCounter.add(counter);
				}
			}
			size = returnCounter.size();
			endNo = page * num < size ? page * num : size;
			returnCounter = returnCounter.subList(startNo, endNo);
		} else {
			size = chlidCounters.size();
			endNo = page * num < size ? page * num : size;
			returnCounter = chlidCounters.subList(startNo, endNo);
		}
		List<CounterBean> cbl = getCounterBean(returnCounter);
		return cbl;
	}

	private List<CounterBean> getCounterBean(List<Counter> cl) {
		List<CounterBean> cbl = new ArrayList<CounterBean>(cl.size());
		for (Counter c : cl) {
			CounterBean cb = getCounterBean(c.getCounterId());
			cbl.add(cb);
		}
		return cbl;
	}

	private CounterBean getCounterBean(Integer counterId) {
		CounterBean cb = null;
		Counter co = new Counter();
		Customer cu = new Customer();
		try {
			BeanDup.dup(counterManager.getCounterById(counterId), co);
			if (customerManager.get(co.getCustomerId()) != null) {
				BeanDup.dup(customerManager.get(co.getCustomerId()), cu);
			}
			cb = new CounterBean(co, cu);
		} catch (Exception e) {
			logger.error("封装CounterBean获取 counter 或者 Customer出错", e);
		}
		return cb;
	}

	/**
	 * 获取柜台 通过AuthUser的Type来决定从组织树或者客户树获取 柜台
	 * 
	 * @param request
	 * @return
	 */
	public List<Counter> getCounters(HttpServletRequest request) {
		String loginName = request.getRemoteUser();
		AuthUser user = BasicController.getAuthUser(request);
		Assert.notNull(user);
		List<Counter> counters = new ArrayList<>();
		if (AuthUser.type_org.equals(user.getType())) {
			// 看到所有柜台
			counters.addAll(counterManager.findAll());
		} else if (AuthUser.type_customer.equals(user.getType())) {
			// 从customer树往下查
			List<Customer> childCustomer = new ArrayList<>();
			// 通过用户获取客户
			UserCustomer userCustomer = userCustomerManager.getByLoginName(loginName);
			Customer customer = customerManager.get(userCustomer.getCustomerId());
			// 自己的门店
			counters.addAll(counterManager.getCounterByCustomerId(customer.getCustomerID()));
			// 获取子客户
			if (customerManager.findAllChildren(customer) != null
					&& !customerManager.findAllChildren(customer).isEmpty()) {
				childCustomer.addAll(customerManager.findAllChildren(customer));
				for (Customer child : childCustomer) {
					counters.addAll(counterManager.getCounterByCustomerId(child.getCustomerID()));
				}
			}
		}
		return counters;
	}
}
