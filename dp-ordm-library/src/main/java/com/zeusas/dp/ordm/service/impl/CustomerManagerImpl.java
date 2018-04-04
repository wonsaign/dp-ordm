package com.zeusas.dp.ordm.service.impl;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.zeusas.common.service.OnStartApplication;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.CustomerService;

@Service
public class CustomerManagerImpl extends OnStartApplication implements CustomerManager {

	static Logger logger = LoggerFactory.getLogger(CustomerManagerImpl.class);

	final Map<Integer, Customer> customers;
	final Map<String,List<Customer>> customerGroupCode;

	@Autowired
	CustomerService customerService;

	public CustomerManagerImpl() {
		customers = new LinkedHashMap<>();
		customerGroupCode=new LinkedHashMap<>();
	}

	@Override
	public void load() {
		customers.clear();
		List<Customer> all = customerService.findAll();

		for (Customer c : all) {
			customers.put(c.getCustomerID(), c);
			
			String code= getGroupCode(c);
			if (!Strings.isNullOrEmpty(code)) {
				if (!customerGroupCode.containsKey(code)) {
					customerGroupCode.put(code, new ArrayList<>());
				}
				customerGroupCode.get(code).add(c);
			}
		}

		for (Customer c : all) {
			if (!c.getStatus()) {
				continue;
			}
			Integer pid = c.getParentId();
			Customer pCustomer = customers.get(pid);
			if (pCustomer == null) {
				continue;
			}
			pCustomer.addChildren(c);
		}
	}

	@Override
	public List<Customer> findAll() {
		List<Customer> all = new ArrayList<Customer>(customers.size());
		all.addAll(customers.values());
		return all;
	}

	@Override
	public void onStartLoad() {
		if (customers.isEmpty()) {
			load();
		}
	}

	@Override
	public Customer get(Integer id) {
		return id == null ? null : customers.get(id);
	}

	public List<Customer> pagination(int page, int num) {
		int size = customers.size();
		int startNo = (page - 1) * num;
		int endNo = page * num < size ? page * num : size;
		if (startNo >= size) {
			return new ArrayList<Customer>(0);
		}

		final List<Customer> cl = new ArrayList<Customer>(num);
		int i = 0;
		for (Customer c : customers.values()) {
			if (i >= endNo) {
				break;
			} else if (i >= startNo) {
				cl.add(c);
			}
			i++;
		}
		return cl;
	}

	public List<Customer> findByName(String name) {
		if (Strings.isNullOrEmpty(name)) {
			return findAll();
		}
		return customers.values().stream()//
				.filter(e -> e.getCustomerName().indexOf(name) >= 0)//
				.collect(Collectors.toList());
	}

	@Override
	public void updateCounterSet(Customer c) throws ServerException {
		Assert.notNull(c);
		CustomerService cs = AppContext.getBean(CustomerService.class);
		 Set<String> counters =c.getCounters();
		if (c.getCustomerID() != null) {
			Customer customer = customers.get(c.getCustomerID());
			Customer dbCustomer = cs.get(c.getCustomerID());
			customer.setCounters(counters);
			dbCustomer.setCounters(counters);
			if(customer.getLevel()==null||(customer.getLevel()>3&& counters.isEmpty())){
				customer.setStatus(false);
				dbCustomer.setStatus(false);
			}
			if(customer.getLevel()!=null&&customer.getLevel()<=3){
				customer.setStatus(true);
				dbCustomer.setStatus(true);
			}
			cs.update(dbCustomer);
		}
	}

	private void findAll(Customer customer, List<Customer> customers) {
		List<Customer> child = customer.getChildren();
		if (child == null || child.isEmpty()) {
			return;
		}
		for (Customer c : child) {
			customers.add(c);
			findAll(c, customers);
		}
	}

	public List<Customer> findAllChildren(Customer customer) {
		List<Customer> customers = new ArrayList<Customer>();
		findAll(customer, customers);
		Collections.sort(customers);
		return customers;
	}

	public List<Integer> findAllChildrenCounterId(Customer customer) {
		Set<String> counterIds = findAllChildrenCounters(customer);
		List<Integer> counterIds2 = new ArrayList<>(counterIds.size());
		counterIds.stream().forEach(e->counterIds2.add(Integer.parseInt(e)));
		return counterIds2;
	}

	@Override
	public Set<String> findAllChildrenCounters(Customer customer) {
		Set<String> counterIds = new HashSet<>();
		List<Customer> childs = findAllChildren(customer);
		childs.stream().filter(e->e.getCounters()!=null)//
			.forEach(e->counterIds.addAll(e.getCounters()));
		return counterIds;
	}

	@Override
	public String getGroupCode(Customer customer) {
		String code = null;
		String customerCode = customer.getCustomerCode();
		if (!Strings.isNullOrEmpty(customerCode)) {
			//长度为一
			if(customerCode.indexOf(".")<0){
				return customerCode;
			}
			String[] str = customerCode.split("\\.");
			String secondCode = str[1];
			//长度为二
			if(str.length==2){
				return customerCode;
			}
			//长度大于二
			// Z J
			if ("Z".equals(secondCode) || "J".equals(secondCode)) {
				code = customerCode.substring(0, customerCode.indexOf("."));
			} else if ("D".equals(secondCode) || "F".equals(secondCode)) {
			// D F
				code = customerCode.substring(0, customerCode.indexOf(str[2]) - 1);
			}else if (customerCode.startsWith("B.Y")) {
				code = "B.Y";
			}
		}
		return code;
	}

	@Override
	public List<Customer> getCustomerByGroupCode(String code) {
		return customerGroupCode.get(code)==null?new ArrayList<>():customerGroupCode.get(code);
	}

}
