package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.common.data.Record;
import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.DiffDetail;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderDetail;

public interface DiffDetailService extends IService<DiffDetail, Integer>{
	
	/** 获取单个订单的明细 */
	List<DiffDetail> getDiffDetails(String orderNo) throws ServiceException;

	List<Record> getK3OrderDetail() throws ServiceException;

	List<Order> getTodoOrder() throws ServiceException;

	/**
	 * 包裹明细也需要realqty 
	 * 订单明细的realQty 用于前端显示 数量由包裹明细汇总  订单实发与应发不一样时  不能用实发数量再拆包裹
	 * 
	 * @param order
	 * @param records
	 * @throws ServiceException
	 */
	void saveDiffResult(Order order, List<Record> records) throws ServiceException;
	
	void create(DiffDetail diffDetail) throws ServiceException;

}
