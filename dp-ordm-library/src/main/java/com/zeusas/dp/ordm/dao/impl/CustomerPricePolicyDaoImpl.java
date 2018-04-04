package com.zeusas.dp.ordm.dao.impl;

import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.dp.ordm.dao.CustomerPricePolicyDao;
import com.zeusas.dp.ordm.entity.CustomerPricePolicy;

/**
 * 
 * @author shihx
 * @date 2016年12月6日 下午3:05:46
 */
@Repository
public class CustomerPricePolicyDaoImpl extends
		CoreBasicDao<CustomerPricePolicy, Integer> implements
		CustomerPricePolicyDao {
}
