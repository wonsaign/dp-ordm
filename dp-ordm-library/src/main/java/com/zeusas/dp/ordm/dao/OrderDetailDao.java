package com.zeusas.dp.ordm.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.OrderDetail;

/**
 * 
 * @author shihx
 * @date 2016年12月6日 下午3:03:39
 */
public interface OrderDetailDao extends Dao<OrderDetail, Integer> {

	/** 根据订单号删除订单的明细 */
	void deleteOrderDetails(String orderNo) throws DaoException;

	List<OrderDetail> getOrderDetails(String orderNo) throws DaoException ;
	
	List<OrderDetail> getOrderDetails(Collection<String> OrderNo) throws DaoException ;
	/** 根据订单号删除订单的明细 */
	Double getPriceByProductId(Collection<String> OrderNo,Collection<Integer> ProductId) throws DaoException ;
	/**
	 * 根据单号集合和产品集合获取，指定产品的明细集合
	 * @param ordersNos
	 * @param proIds
	 * @return
	 * @throws DaoException
	 */
	List<OrderDetail> getAssignProsList(List<String> ordersNos, List<Integer> proIds) throws DaoException;
	
	List<OrderDetail> findByCreateTime(Date start,Date end) throws DaoException;
}
