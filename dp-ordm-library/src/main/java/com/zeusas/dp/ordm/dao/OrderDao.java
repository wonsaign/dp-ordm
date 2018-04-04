package com.zeusas.dp.ordm.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.service.Conditions;
/**
 * 
 * @author shihx
 * @date 2016年12月6日 下午3:03:32
 */
public interface OrderDao extends Dao<Order, Long> {

	List<Order> findByCounterIds(Collection<Integer> counterIds, String typeId) throws DaoException;
	
	List<Order> findByCounterIds(Collection<Integer> counterIds) throws DaoException;
	
	List<Order>findByType(String status) throws DaoException;
	
	List<Order>findByType(Collection<String> orderStatus) throws DaoException;
	
	Order findByOrderNo(String orderNo) throws DaoException;
	
	List<Order>findOrders(String customerId,Collection<String> orderStatus,Long start,Long end)throws DaoException;

	/** 支付所有合并的订单*/
	List<Order> getMergeOrders(Collection<Integer> counterIds,String orderStatus)throws DaoException;

	List<Order> findOrders(Conditions condition) throws DaoException;
	/** 根据订单号获取订单 */
	List<Order> findByOrderNo(Collection<String> OrderNo)throws DaoException;
	/** 根据柜台号 订单状态 时间段  获取支付后的订单*/
	List<Order>findPayOrders(String counterCode,Collection<String> orderStatus,Long start,Long end)throws DaoException;
	/**订单状态 时间段  获取支付后的订单*/
	List<Order>findPayOrders(Collection<String> orderStatus,Long start,Long end)throws DaoException;
	/** 获取有活动记录的订单 */
	List<Order>findActivityOrder() throws DaoException;
	
	List<Order>findByDiffStatus(Integer diffStatus) throws DaoException;
}
