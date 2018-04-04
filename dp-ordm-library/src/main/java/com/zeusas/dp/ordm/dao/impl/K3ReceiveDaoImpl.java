package com.zeusas.dp.ordm.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.dp.ordm.dao.K3ReceiveDao;
import com.zeusas.dp.ordm.entity.K3Receive;
/**
 * 
 * @author wangs
 */
@Repository
public class K3ReceiveDaoImpl extends CoreBasicDao<K3Receive, Integer> implements K3ReceiveDao {
	static final Logger log = LoggerFactory.getLogger(K3ReceiveDao.class);
}
