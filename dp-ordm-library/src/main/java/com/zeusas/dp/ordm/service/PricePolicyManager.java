package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.CustomerPricePolicy;

public interface PricePolicyManager {
	
	public void reload();
	public boolean update(CustomerPricePolicy pricePolicy)throws ServiceException;
	public CustomerPricePolicy getPolicy(Integer typeId)  ;
	public List<CustomerPricePolicy> findAll();
	public List<Integer>findAllIds();
	public List<CustomerPricePolicy> findByCustomerType(Integer typeId);
}
