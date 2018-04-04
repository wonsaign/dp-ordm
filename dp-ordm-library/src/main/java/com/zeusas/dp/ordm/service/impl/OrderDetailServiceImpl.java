package com.zeusas.dp.ordm.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.dao.OrderDetailDao;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.service.OrderDetailService;

@Service
public class OrderDetailServiceImpl extends BasicService<OrderDetail, Integer> implements OrderDetailService {
	@Autowired
	private OrderDetailDao dao;
	
	@Override
	protected Dao<OrderDetail, Integer> getDao() {
		return dao;
	}

	@Override
	@Transactional(readOnly=true)
	public List<OrderDetail> getAssignProsList(List<String> ordersNos, List<Integer> proIds) throws ServiceException {
		return dao.getAssignProsList(ordersNos, proIds);
	}

	@Override
	@Transactional(readOnly=true)
	public List<OrderDetail> findByCreateTime(Date start, Date end) throws ServiceException {
		return dao.findByCreateTime(start, end);
	}
}
