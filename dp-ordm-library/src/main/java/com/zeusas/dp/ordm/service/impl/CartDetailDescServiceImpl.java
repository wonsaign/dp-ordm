package com.zeusas.dp.ordm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.dp.ordm.dao.CartDetailDescDao;
import com.zeusas.dp.ordm.entity.CartDetailDesc;
import com.zeusas.dp.ordm.service.CartDetailDescService;

@Service
public class CartDetailDescServiceImpl extends BasicService<CartDetailDesc, Long> implements CartDetailDescService{

	@Autowired
	private CartDetailDescDao dao;
	
	@Override
	protected Dao<CartDetailDesc, Long> getDao() {
		return dao;
	}

}
