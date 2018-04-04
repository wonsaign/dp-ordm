package com.zeusas.dp.ordm.dao.impl;

import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.dp.ordm.dao.UserCustomerDao;
import com.zeusas.dp.ordm.entity.UserCustomer;

@Repository
public class UserCustomerDaoImpl extends CoreBasicDao<UserCustomer, String> implements UserCustomerDao {

}
