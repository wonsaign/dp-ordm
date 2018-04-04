package com.zeusas.dp.ordm.service;


import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.OrderCredentials;

public interface OrderCredentialsService  extends IService<OrderCredentials, String>{
	
	/**根据单号找到凭据*/
	public OrderCredentials getOrderCredentials(String orderNo) throws ServiceException;
	/** 根据单号找到合并支付的凭证*/
	OrderCredentials getCombineOrderCredentials(String orderNo) throws ServiceException;
	/**
	 * 获取所有合并订单的集合(父凭证)
	 * @return
	 * @throws ServiceException
	 */
	List<OrderCredentials> getParentCredentials() throws ServiceException;
	/**
	 * 获取所有合并订单的集合(子凭证)
	 * @return
	 * @throws ServiceException
	 */
	List<OrderCredentials> getChildCredentials() throws ServiceException;
	/**
	 * 获取所有合并订单的集合(子凭证和父凭证)
	 * @return
	 * @throws ServiceException
	 */
	List<OrderCredentials> getCredentials() throws ServiceException;
	
}
