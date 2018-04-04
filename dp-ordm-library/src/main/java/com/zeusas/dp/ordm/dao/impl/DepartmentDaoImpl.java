package com.zeusas.dp.ordm.dao.impl;

import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.dp.ordm.dao.DepartmentDao;
import com.zeusas.dp.ordm.entity.Department;

@Repository
public class DepartmentDaoImpl extends CoreBasicDao<Department, Integer> implements DepartmentDao{

}
