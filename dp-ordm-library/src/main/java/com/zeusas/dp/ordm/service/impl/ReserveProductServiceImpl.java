package com.zeusas.dp.ordm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.dp.ordm.dao.ReserveProductDao;
import com.zeusas.dp.ordm.entity.ReserveProduct;
import com.zeusas.dp.ordm.service.ReserveProductService;
@Service
public class ReserveProductServiceImpl extends BasicService<ReserveProduct, Integer> implements ReserveProductService {
	@Autowired
	private  ReserveProductDao dao;
	
	@Override
	protected Dao<ReserveProduct, Integer> getDao() {
		return dao;
	}

}
