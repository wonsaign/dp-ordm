package com.zeusas.dp.ordm.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.dao.OrderCredentialsDao;
import com.zeusas.dp.ordm.entity.OrderCredentials;

/**
 * 
 * @author fengx
 * @date 2017年1月9日 上午9:03:01
 */
@Repository
public class OrderCredentialsDaoImpl extends CoreBasicDao<OrderCredentials, String> implements OrderCredentialsDao {
	final static Logger logger = LoggerFactory.getLogger(OrderCredentialsDaoImpl.class);
	
	@Override
	public void deleteOC(String ocid) throws DaoException{
		String sql = "DELETE FROM " + entityClass.getName() + " WHERE ocid = ?";
		// 删除明细的所有的描述
		try{
			super.execute(sql, ocid);
			getCurrentSession().clear();
		}catch(Exception e){
			logger.error("sql: {}, ocid:{}, 订单状态：{}", sql, ocid);
			throw new DaoException(e);
		}
	}
	
	@Override
	public List<OrderCredentials> getParentCredentials() throws DaoException {
		List<OrderCredentials> OrderCredentials;
		String where = " OrderNo like '%,%'";
		try {
			OrderCredentials = super.find(where);
		} catch (DaoException e) {
			logger.error("condition:{}",where);
			throw new ServiceException(e);
		}
		return OrderCredentials;
	}

	@Override
	public List<OrderCredentials> getChildCredentials() throws DaoException {
		List<OrderCredentials> OrderCredentials;
		String where = " combineId is not null";
		try {
			OrderCredentials = super.find(where);
		} catch (DaoException e) {
			logger.error("condition:{}",where);
			throw new ServiceException(e);
		}
		return OrderCredentials;
	}
	
	@Override
	public List<OrderCredentials> getCredentials() throws DaoException {
		List<OrderCredentials> OrderCredentials;
		String where = " combineId is not null or OrderNo like '%,%'";
		try {
			OrderCredentials = super.find(where);
		} catch (DaoException e) {
			logger.error("condition:{}",where);
			throw new ServiceException(e);
		}
		return OrderCredentials;
	}
	
}
