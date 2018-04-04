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
import com.zeusas.dp.ordm.dao.K3ReceiveDao;
import com.zeusas.dp.ordm.entity.K3Receive;
import com.zeusas.dp.ordm.service.K3ReceiveService;

@Service
@Transactional
public class K3ReceiveServiceImpl extends BasicService<K3Receive,Integer> 
		implements K3ReceiveService {
	private static Logger logger = LoggerFactory.getLogger(K3ReceiveServiceImpl.class);
	
	@Autowired
	private K3ReceiveDao k3ReceiveDao;
	
	@Override
	protected Dao<K3Receive, Integer> getDao() {
		return k3ReceiveDao;
	}
	
	@Override
	public List<K3Receive> getByCustomerId(Integer cusId,String startTime,String endTime){
		List<K3Receive> receives;
		
		String hql = "fCustomerID = ? and fYear*100+fPeriod >= ? and fYear*100+fPeriod <= ?";
		
		try{
			//Acount 表添加数据
			receives = k3ReceiveDao.find(hql, cusId , Integer.valueOf(startTime) , Integer.valueOf(endTime));
			
			return receives==null?new ArrayList<K3Receive>(0):receives;
		}catch(DaoException e){
			logger.warn("获取数据异常");
			throw new ServiceException(e);
		}
		
	}

	@Override
	public List<K3Receive> getByCustomerIdAndTime(Integer cusId, String startTime, String endTime) {
		List<K3Receive> receives;
		
		String hql = "fCustomerID = ? and FDate >= ? and Fdate <= ?";
		
		try{
			//Acount 表添加数据
			receives = k3ReceiveDao.find(hql, cusId , startTime , endTime);
			
			return receives==null?new ArrayList<K3Receive>(0):receives;
		}catch(DaoException e){
			logger.warn("获取数据异常");
			throw new ServiceException(e);
		}
		
	}
	

}
