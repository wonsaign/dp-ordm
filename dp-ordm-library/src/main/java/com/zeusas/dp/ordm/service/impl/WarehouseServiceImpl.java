package com.zeusas.dp.ordm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.dp.ordm.dao.WarehouseDao;
import com.zeusas.dp.ordm.entity.Warehouse;
import com.zeusas.dp.ordm.service.WarehouseService;
@Service
@Transactional
public class WarehouseServiceImpl extends BasicService<Warehouse, Integer> implements WarehouseService {
	@Autowired
	private WarehouseDao dao;
	
	@Override
	protected Dao<Warehouse, Integer> getDao() {
		return dao;
	}

}
