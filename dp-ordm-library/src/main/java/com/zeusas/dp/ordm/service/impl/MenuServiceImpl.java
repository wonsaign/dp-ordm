package com.zeusas.dp.ordm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.core.service.BasicService;
import com.zeusas.dp.ordm.dao.MenuDao;
import com.zeusas.dp.ordm.entity.MenuNode;
import com.zeusas.dp.ordm.service.MenuService;

@Service
@Transactional
public class MenuServiceImpl extends BasicService<MenuNode, Integer> implements MenuService {

	@Autowired
	private MenuDao dao;

	@Override
	protected MenuDao getDao() {
		return dao;
	}

	public void setDao(MenuDao dao) {
		this.dao = dao;
	}
}
