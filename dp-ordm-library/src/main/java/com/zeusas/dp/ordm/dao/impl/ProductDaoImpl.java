package com.zeusas.dp.ordm.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.dp.ordm.dao.ProductDao;
import com.zeusas.dp.ordm.entity.Product;
/**
 * 
 * @author shihx
 * @date 2016年12月6日 下午3:05:53
 */
@Repository
public class ProductDaoImpl extends CoreBasicDao<Product, Integer> implements ProductDao {
	static final Logger log = LoggerFactory.getLogger(ProductDao.class);
}
