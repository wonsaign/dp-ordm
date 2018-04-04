package com.zeusas.dp.ordm.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.dao.K3OrderDao;
import com.zeusas.dp.ordm.entity.K3Order;
import com.zeusas.dp.ordm.service.K3OrderService;
@Service
@Transactional
public class K3OrderServiceImpl extends BasicService<K3Order,Integer> 
		implements K3OrderService {
	
	private static Logger logger = LoggerFactory.getLogger(K3OrderServiceImpl.class);
	
	@Autowired
	private K3OrderDao k3OrderDao;
	
	@Override
	protected Dao<K3Order, Integer> getDao() {
		return k3OrderDao;
	}

	@Override
	public List<K3Order> getByCustomerId(Integer cusId,String startTime,String endTime){
		List<K3Order> orders;
		String hql = "fCustomerID = ? and fYear*100+fPeriod >= ? and fYear*100+fPeriod <= ?";
		try{
			//Acount 表添加数据
			orders = k3OrderDao.find(hql, cusId , Integer.valueOf(startTime) , Integer.valueOf(endTime));
			
			return orders==null?new ArrayList<K3Order>(0):orders;
		}catch(DaoException e){
			logger.warn("获取数据异常");
			throw new ServiceException(e);
		}
		
	}

	@Override
	public List<K3Order> getByCustomerIdAndTime(Integer cusId, String startTime, String endTime) {

		List<K3Order> orders;
		String hql = "fCustomerID = ? and FDate >= ? and FDate <= ?";
		try{
			//Acount 表添加数据
			orders = k3OrderDao.find(hql, cusId , startTime , endTime);
			
			return orders==null?new ArrayList<K3Order>(0):orders;
		}catch(DaoException e){
			logger.warn("获取数据异常");
			throw new ServiceException(e);
		}
		
	
	}
	
}
