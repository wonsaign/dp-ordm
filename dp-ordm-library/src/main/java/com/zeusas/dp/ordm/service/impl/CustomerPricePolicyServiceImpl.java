package com.zeusas.dp.ordm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.dp.ordm.dao.CustomerPricePolicyDao;
import com.zeusas.dp.ordm.entity.CustomerPricePolicy;
import com.zeusas.dp.ordm.service.CustomerPricePolicyService;

@Service
public class CustomerPricePolicyServiceImpl extends BasicService<CustomerPricePolicy, Integer> implements CustomerPricePolicyService {
	@Autowired
	private CustomerPricePolicyDao dao;
	
	@Override
	protected Dao<CustomerPricePolicy, Integer> getDao() {
		return dao;
	}

}
