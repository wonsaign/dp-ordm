package com.zeusas.dp.ordm.dao.impl;

import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.dp.ordm.dao.CustomerDao;
import com.zeusas.dp.ordm.entity.Customer;
@Repository
public class CustomerDaoImpl extends CoreBasicDao<Customer, Integer> implements CustomerDao {

}
