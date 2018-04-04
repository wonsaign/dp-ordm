package com.zeusas.dp.ordm.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.dp.ordm.dao.K3OrderDao;
import com.zeusas.dp.ordm.entity.K3Order;
/**
 * 
 * @author wangs
 */
@Repository
public class K3OrderDaoImpl extends CoreBasicDao<K3Order, Integer> implements K3OrderDao {
	static final Logger log = LoggerFactory.getLogger(K3OrderDao.class);
}
