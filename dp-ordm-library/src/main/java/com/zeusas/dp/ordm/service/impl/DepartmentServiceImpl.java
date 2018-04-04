package com.zeusas.dp.ordm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.dp.ordm.dao.DepartmentDao;
import com.zeusas.dp.ordm.entity.Department;
import com.zeusas.dp.ordm.service.DepartmentService;

@Service
@Transactional
public class DepartmentServiceImpl extends BasicService<Department, Integer> implements DepartmentService {

	@Autowired
	DepartmentDao dao;

	@Override
	protected Dao<Department, Integer> getDao() {
		return dao;
	}

}
