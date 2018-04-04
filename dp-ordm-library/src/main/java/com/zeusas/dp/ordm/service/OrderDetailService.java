package com.zeusas.dp.ordm.service;

import java.util.Date;
import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.OrderDetail;

public interface OrderDetailService extends IService<OrderDetail, Integer> {
	List<OrderDetail> getAssignProsList(List<String> ordersNos, List<Integer> proIds) throws ServiceException;
	
	List<OrderDetail> findByCreateTime(Date start,Date end) throws ServiceException;
}
