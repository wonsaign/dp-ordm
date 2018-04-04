package com.zeusas.dp.ordm.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.dp.ordm.dao.CustomerDao;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.service.CustomerService;
@Service
public class CustomerServiceImpl extends BasicService<Customer, Integer> implements CustomerService {
	@Autowired
  	private CustomerDao dao;
	
	@Override
	protected Dao<Customer, Integer> getDao() {
		return dao;
	}
}
