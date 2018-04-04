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
import com.zeusas.dp.ordm.dao.K3AcountDao;
import com.zeusas.dp.ordm.entity.K3Acount;
import com.zeusas.dp.ordm.service.K3AcountService;

@Service
@Transactional
public class K3AcountServiceImpl extends BasicService<K3Acount,Integer> 
		implements K3AcountService {
	
	private static Logger logger = LoggerFactory.getLogger(K3AcountServiceImpl.class);
	
	@Autowired
	private K3AcountDao k3AcountDao;
	
	@Override
	protected Dao<K3Acount, Integer> getDao() {
		return k3AcountDao;
	}

	@Override
	public List<K3Acount> getByCustomerId(Integer cusId,String startTime,String endTime){
		List<K3Acount> acounts;
		
		String hql = "fCustomerID = ? and fYear*100+fPeriod >= ? and fYear*100+fPeriod <= ?";
		
		try{
			//Acount 表添加数据
			acounts = k3AcountDao.find(hql, cusId , Integer.valueOf(startTime) , Integer.valueOf(endTime));
			
			return acounts==null?new ArrayList<K3Acount>(0):acounts;
		}catch(DaoException e){
			logger.warn("获取数据异常");
			throw new ServiceException(e);
		}
		
	}

	@Override
	public List<K3Acount> getByYearAndPeriod(Integer cusId, Integer year, Integer preiod) {

		List<K3Acount> acounts;
		String hql = "fCustomerID = ? and fYear = ? and  fPeriod = ?";
		try{
			//Acount 表添加数据
			acounts = k3AcountDao.find(hql, cusId,year,preiod);
			return acounts==null?new ArrayList<K3Acount>(0):acounts;
		}catch(DaoException e){
			logger.warn("获取数据异常");
			throw new ServiceException(e);
		}
	
	}
	

}
