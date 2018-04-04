package com.zeusas.dp.ordm.dao.impl;

import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.dp.ordm.dao.CounterDao;
import com.zeusas.dp.ordm.entity.Counter;
/**
 * 
 * @author shihx
 * @date 2016年12月6日 下午3:05:01
 */
@Repository
public class CounterDaoImpl extends CoreBasicDao<Counter, Integer> implements CounterDao{

}
