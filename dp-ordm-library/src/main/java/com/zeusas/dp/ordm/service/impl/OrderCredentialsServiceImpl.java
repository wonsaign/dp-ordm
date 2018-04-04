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
import com.zeusas.dp.ordm.dao.OrderCredentialsDao;
import com.zeusas.dp.ordm.entity.OrderCredentials;
import com.zeusas.dp.ordm.service.OrderCredentialsService;

@Service
@Transactional
public class OrderCredentialsServiceImpl extends BasicService<OrderCredentials, String>
		implements OrderCredentialsService {

	final static Logger logger = LoggerFactory.getLogger(OrderCredentialsServiceImpl.class);

	@Autowired
	private OrderCredentialsDao dao;

	@Override
	protected Dao<OrderCredentials, String> getDao() {
		return dao;
	}

	/**
	 * 非HQL，区分大小写
	 * OrderNo列改为了集合，所以这里修改查找条件为like，由于每个订单对应1个凭证或者2个凭证
	 * 所以返回结果只有一个或者为null
	 * @author wangs
	 */
	@Transactional(readOnly=true)
	@Override
	public OrderCredentials getOrderCredentials(String orderNo) throws ServiceException {
		String where = "where OrderNo like ?";
			List<OrderCredentials> ocs;
			try {
				ocs = dao.find(where, '%' + orderNo + '%');			
			// 可能会搜索到两个
			for (OrderCredentials orderCredentials : ocs) {
				if (orderCredentials.getOrderNos().size() == 1) {
					return orderCredentials;
				}
			}
			return null;
		} catch (DaoException e) {
			logger.error("获取凭据异常,orderNo-{}", orderNo);
			throw new ServiceException("获取凭据异常", e);
		}
	}
	
	/**
	 * 非HQL，区分大小写
	 * OrderNo列改为了集合，所以这里修改查找条件为like，由于每个订单对应1个凭证或者2个凭证
	 * 所以返回结果是一个凭证
	 * @author wangs
	 */
	@Transactional(readOnly=true)
	@Override
	public OrderCredentials getCombineOrderCredentials(String orderNo) throws ServiceException {
		String where = "WHERE OrderNo like ?";
		List<OrderCredentials> ocs;
		try {
			
			ocs = dao.find(where, '%' + orderNo + '%');	
			// ocs.size() > 1 为合并的订单，并且只有1,2的原因是一单是单独的，另外一单是合并的
			for (OrderCredentials oc : ocs) {
				if (oc.getOrderNos().size() > 1) {
					return oc;
				}
			}
			return null;
		} catch (DaoException e) {
			logger.error("获取合并凭据异常,orderNo-{}", orderNo);
			throw new ServiceException("获取合并获取凭据异常", e);
		}
	}
	/**
	 * 获取所有合并订单的集合
	 * @return
	 * @throws ServiceException
	 */
	@Override
	@Transactional(readOnly=true)
	public List<OrderCredentials> getParentCredentials() throws ServiceException {
		return dao.getParentCredentials();
	}

	@Override
	@Transactional(readOnly=true)
	public List<OrderCredentials> getChildCredentials() throws ServiceException {
		return dao.getChildCredentials();
	}

	@Override
	@Transactional(readOnly=true)
	public List<OrderCredentials> getCredentials() throws ServiceException {
		return dao.getCredentials();
	}
	
	
	
}
