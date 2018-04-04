package com.zeusas.dp.ordm.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.dp.ordm.dao.K3AcountDao;
import com.zeusas.dp.ordm.entity.K3Acount;
/**
 * 
 * @author wangs
 */
@Repository
public class K3AcountDaoImpl extends CoreBasicDao<K3Acount, Integer> implements K3AcountDao {
	static final Logger log = LoggerFactory.getLogger(K3AcountDao.class);
}
