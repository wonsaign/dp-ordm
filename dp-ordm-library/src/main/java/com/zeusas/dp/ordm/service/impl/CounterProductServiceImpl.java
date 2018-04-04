package com.zeusas.dp.ordm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.dp.ordm.dao.CounterProductDao;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.CounterProduct;
import com.zeusas.dp.ordm.entity.ProductSeller;
import com.zeusas.dp.ordm.service.CounterProductService;

@Service
@Transactional
public class CounterProductServiceImpl extends
		BasicService<CounterProduct, String> implements CounterProductService {

	@Autowired
	private CounterProductDao dao;

	@Override
	protected Dao<CounterProduct, String> getDao() {
		return dao;
	}

	@Override
	public List<ProductSeller> getGlobalProduct() {
		CounterProduct cp = get(Counter.GLOBALCODE);
		return cp == null ? null : cp.getProducts();
	}
}
