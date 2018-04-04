package com.zeusas.dp.ordm.dao.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.dao.OrderDao;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.service.Conditions;
/**
 * 
 * @author shihx
 * @date 2016年12月6日 下午3:05:08
 */
@Repository
public class OrderDaoImpl extends CoreBasicDao<Order, Long> implements OrderDao {
	private static Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);
	
	@Override
	public List<Order> findByCounterIds(Collection<Integer> counterIds, String orderStatus) throws DaoException {
		String where = "WHERE  counterId IN (:counterId) AND orderStatus = (:orderStatus)  ORDER BY lastUpdate ASC";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("counterId", counterIds);
		map.put("orderStatus", orderStatus);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("Condition: {}, 柜台ids{}, 订单状态：{}", where, counterIds, orderStatus);
			throw new DaoException(e);
		}
	}
	
	@Override
	public List<Order> findByCounterIds(Collection<Integer> counterIds) throws DaoException {
		String where = "WHERE  counterId IN (:counterId)  ORDER BY lastUpdate ASC";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("counterId", counterIds);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("condition:{},柜台ids{}",where,counterIds);
			throw new DaoException(e);
		}
	}

	@Override
	public List<Order> findByType(String status) throws DaoException{
		List<Order> orders;
		String where = " orderStatus = ?";
		try {
			orders = super.find(where, status);
		} catch (DaoException e) {
			logger.error("condition:{},类型状态:{}",where,status);
			throw new DaoException(e);
		}
		return orders;
	}
	
	@Override
	public List<Order> findByType(Collection<String> orderStatus) throws DaoException {
		String where = "WHERE  orderStatus  in (:orderStatus) ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderStatus", orderStatus);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("Condition: {}, 订单状态：{}", where,orderStatus);
			throw new DaoException(e);
		}
	}

	@Override
	public Order findByOrderNo(String orderNo) throws DaoException{
		String where = "orderNo = ?";
		try{
			List<Order> orders = find(where, orderNo);
			return orders.isEmpty() ? null : orders.get(0);
		}catch (Exception e){
			logger.error("condition:{},orderNo:{}",where,orderNo);
			throw new DaoException(e);
		}
	}
	
	@Override
	public List<Order> getMergeOrders(Collection<Integer> counterIds, String orderStatus) throws DaoException {
		String where = "WHERE  counterId IN (:counterId) AND isMerger =1 AND orderStatus =(:orderStatus) ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("counterId", counterIds);
		map.put("orderStatus", orderStatus);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("Condition: {}, 柜台ids{}, 订单状态：{}", where, counterIds, orderStatus);
			throw new DaoException(e);
		}
	}

	@Override
	public List<Order> findOrders(Conditions cond) throws DaoException{
		StringBuilder sb = new StringBuilder("WHERE 1=1 "); 
		Map<String, Object> map = new HashMap<String, Object>();
		if(cond.getCounterId()!=null&&cond.getCounterId().size()>=1){
			sb.append(" AND counterId IN (:counterId)");
			map.put("counterId", cond.getCounterId());
		}
		if(!Strings.isNullOrEmpty(cond.getStatus())){
			sb.append(" AND orderStatus =(:orderStatus)");
			map.put("orderStatus", cond.getStatus());
		}
		if(cond.getStart()!=null&&cond.getEnd()!=null){
			sb.append(" AND OrderCreatTime > (:start) AND   OrderCreatTime < (:end)");
			map.put("start", cond.getStart());
			map.put("end", cond.getEnd());
		}
		try {
			return find(sb.toString(), map);
		} catch (Exception e) {
			logger.error("Condition: {}, 条件:{}", sb.toString(), cond);
			throw new DaoException(e);
		}
	}

	@Override
	public List<Order> findByOrderNo(Collection<String> OrderNo)throws DaoException {
		String where = "WHERE  OrderNo IN (:OrderNo) ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("OrderNo", OrderNo);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {}, 订单号{}", where, OrderNo);
			throw new DaoException(e);
		}
	}

	@Override
	public List<Order> findPayOrders(String counterCode, Collection<String> orderStatus, Long start, Long end)
			throws DaoException {
		String where = "WHERE  counterCode =(:counterCode)  AND orderStatus IN(:orderStatus) AND OrderPayTime >(:start) AND OrderPayTime <(:end)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("counterCode", counterCode);
		map.put("orderStatus", orderStatus);
		map.put("start", start);
		map.put("end", end);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {}, 柜台号{},订单状态{},起始时间{},结束时间{}", where, counterCode,orderStatus,start,end);
			throw new DaoException(e);
		}
	}
	
	@Override
	public List<Order> findPayOrders(Collection<String> orderStatus, Long start, Long end)
			throws DaoException {
		String where = "WHERE  orderStatus IN(:orderStatus) AND OrderPayTime >(:start) AND OrderPayTime <(:end)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderStatus", orderStatus);
		map.put("start", start);
		map.put("end", end);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {},订单状态{},起始时间{},结束时间{}", where,orderStatus,start,end);
			throw new DaoException(e);
		}
	}

	@Override
	public List<Order> findActivityOrder() throws DaoException {
		String where = "WHERE  ActivRecord is  not NULL and  ActivRecord!='null'";
		try {
			return find(where);
		} catch (Exception e) {
			logger.error("hql: {}", where);
			throw new DaoException(e);
		}
	}

	@Override
	public List<Order> findOrders(String customerId, Collection<String> orderStatus, Long start, Long end)
			throws DaoException {
		String where = "WHERE  customerId =(:customerId)  AND orderStatus IN(:orderStatus) AND OrderPayTime >(:start) AND OrderPayTime <(:end)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerId", customerId);
		map.put("orderStatus", orderStatus);
		map.put("start", start);
		map.put("end", end);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {}, 客户id{},订单状态{},起始时间{},结束时间{}", where, customerId,orderStatus,start,end);
			throw new DaoException(e);
		}
	}

	@Override
	public List<Order> findByDiffStatus(Integer diffStatus) throws DaoException {
		String where = "WHERE  diffStatus IN(:diffStatus)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("diffStatus", diffStatus);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {},订单差分状态{}", where,diffStatus);
			throw new DaoException(e);
		}
	}
}
