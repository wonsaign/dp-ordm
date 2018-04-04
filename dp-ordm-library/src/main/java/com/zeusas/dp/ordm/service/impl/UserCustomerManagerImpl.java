package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.zeusas.common.service.OnStartApplication;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.service.UserCustomerManager;
import com.zeusas.dp.ordm.service.UserCustomerService;

@Service
public class UserCustomerManagerImpl extends OnStartApplication //
		implements UserCustomerManager {

	static Logger logger = LoggerFactory.getLogger(UserCustomerManagerImpl.class);

	final Map<String, UserCustomer> userIdMap;
	final Map<String, UserCustomer> loginNameMap;
	final Map<Integer, UserCustomer> customerId;
	final Map<Integer, List<UserCustomer>> customerId_all;

	@Autowired
	private UserCustomerService userCustomerService;

	public UserCustomerManagerImpl() {
		userIdMap = new ConcurrentHashMap<>();
		loginNameMap = new ConcurrentHashMap<>();
		customerId = new ConcurrentHashMap<>();
		customerId_all = new ConcurrentHashMap<>();
	}

	private void clearAll() {
		userIdMap.clear();
		loginNameMap.clear();
		customerId.clear();
		customerId_all.clear();
	}

	@Override
	public void load() {
		clearAll();

		List<UserCustomer> userCustomers = userCustomerService.findAll();

		for (UserCustomer uc : userCustomers) {
			userIdMap.put(uc.getUserId(), uc);
			loginNameMap.put(uc.getLoginName(), uc);
			if (uc.getUserId().equals(uc.getCustomerUserId())) {
				customerId.put(uc.getCustomerId(), uc);
			}
		}

		for (UserCustomer uc : userCustomers) {
			if (!customerId_all.containsKey(uc.getCustomerId())) {
				List<UserCustomer> ucs = new ArrayList<>(userCustomers.size());
				customerId_all.put(uc.getCustomerId(), ucs);
			}
			customerId_all.get(uc.getCustomerId()).add(uc);
		}

		for (List<?> c : customerId_all.values()) {
			((ArrayList<?>) c).trimToSize();
		}
	}

	// @Override
	public UserCustomer getByUserId(String userId) {
		UserCustomer uc = userIdMap.get(userId);
		return uc;
	}
	@Override
	public boolean isBoss(String name){
		UserCustomer uc = loginNameMap.get(name);
		return uc.getUserId().equals(uc.getCustomerUserId());
	}

	@Override
	public UserCustomer getByLoginName(String name) {
		UserCustomer uc = loginNameMap.get(name);
		return uc;
	}

	@Override
	public UserCustomer getCustomerById(Integer customerid) {
		UserCustomer uc = customerId.get(customerid);
		return uc;
	}

	@Override
	public List<UserCustomer> getByCustomerId(Integer customerId) {
		List<UserCustomer> ucs = customerId_all.get(customerId);
		return ucs;
	}

	@Override
	public boolean create(UserCustomer userCustomer) throws Exception {
		Assert.notNull(userCustomer);
		boolean flag = false;
		if (userCustomer.getUserId() != null) {
			userCustomer.setStatus(1);
			userCustomer.setLastUpdate(System.currentTimeMillis());
			userCustomerService.save(userCustomer);
			load();
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean update(UserCustomer userCustomer) throws Exception {
		Assert.notNull(userCustomer);
		boolean flag = false;
		if (userCustomer.getUserId() != null) {
			UserCustomer chache = userIdMap.get(userCustomer.getUserId());
			UserCustomer dbUserCustomer = userCustomerService.get(userCustomer.getUserId());

			BeanDup.dupNotNull(userCustomer, dbUserCustomer, "counters", "status");
			if(dbUserCustomer.getCounters().isEmpty()){
				dbUserCustomer.setStatus(0);
			}else{
				dbUserCustomer.setStatus(1);
			}
			userCustomerService.update(dbUserCustomer);
			BeanDup.dupNotNull(userCustomer, chache, "counters", "status");
			if(chache.getCounters().isEmpty()){
				chache.setStatus(0);
			}else{
				chache.setStatus(1);
			}
//			load();
			flag = true;
		}
		return flag;
	}

	@Override
	public void onStartLoad() {
		if (userIdMap.size() == 0) {
			load();
		}
	}

	@Override
	public Collection<UserCustomer> findall() {
		return userIdMap.values();
	}
}
