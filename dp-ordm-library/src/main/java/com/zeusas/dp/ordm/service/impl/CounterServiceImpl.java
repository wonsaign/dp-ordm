package com.zeusas.dp.ordm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.dp.ordm.dao.CounterDao;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.service.CounterService;

@Service
public class CounterServiceImpl extends BasicService<Counter, Integer> implements CounterService {
	@Autowired
	private CounterDao counterDao;
	
	@Override
	protected Dao<Counter, Integer> getDao() {
		return counterDao;
	}

}
