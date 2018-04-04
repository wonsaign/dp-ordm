package com.zeusas.dp.ordm.service;

import java.util.Collection;
import java.util.List;

import com.zeusas.dp.ordm.entity.UserCustomer;

public interface UserCustomerManager {
	
	public Collection <UserCustomer> findall();
	
	public UserCustomer getByLoginName(String name);
	
	public boolean create(UserCustomer userCustomer) throws Exception;
	
	/** get UserCustomer by customerid*/
	public List<UserCustomer> getByCustomerId(Integer customerId);
	
	public boolean update(UserCustomer userCustomer) throws Exception;

	/** get Customer customerid*/
	public UserCustomer getCustomerById(Integer customerid);

	void load();
	/** check boss */
	boolean isBoss(String name);
}
