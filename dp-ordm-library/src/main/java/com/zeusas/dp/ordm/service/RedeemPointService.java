package com.zeusas.dp.ordm.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.zeusas.common.data.Record;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.RedeemPoint;

public interface RedeemPointService extends IService<RedeemPoint, Long> {
	/** 根据柜台查找记录*/
	public List<RedeemPoint> findByCounterCode(String CounterCode) throws ServiceException;

	public List<RedeemPoint> findByAvaiCounterCode(String CounterCode) throws ServiceException;

	public Order addToOrder(Order order) throws ServiceException;

	// 订单作废，将bus_redeempoint执行单号置为空
	public void removeRedeem(String orderNo) throws ServiceException;

	public List<Record> getExcel(File upload, DSResponse response) throws IOException;
	
	public RedeemPoint createRedeemPoint(RedeemPoint redeemPoint) throws ServiceException;
	
	public void buildSystemOrder() throws ServiceException;
	
	public List<RedeemPoint> getAvalible() throws ServiceException;
	
	public void cancel(Long id) throws ServiceException;


}
