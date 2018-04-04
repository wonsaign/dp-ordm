package com.zeusas.dp.ordm.dao.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.dao.OrderDetailDao;
import com.zeusas.dp.ordm.entity.OrderDetail;

/**
 * 
 * @author shihx
 * @date 2016年12月6日 下午3:05:16
 */
@Repository
public class OrderDetailDaoImpl extends CoreBasicDao<OrderDetail, Integer> implements OrderDetailDao {

	private static Logger logger = LoggerFactory.getLogger(OrderDetailDaoImpl.class);
	
	@Override
	public void deleteOrderDetails(String orderNo) throws DaoException {
		String sql = "DELETE FROM " + entityClass.getName() + " WHERE OrderNo = ?";
		// 删除明细的所有的描述
		try{
			super.execute(sql, orderNo);
		} catch (Exception e) {
			logger.error("删除明细异常 条件:{}", sql);
			throw new DaoException(e);
		}
		getCurrentSession().clear();

	}

	@Override
	public List<OrderDetail> getOrderDetails(String orderNo) throws DaoException {
		List<OrderDetail> orderDetails;
		String where = "orderNo =?";
		try {
			orderDetails = super.find(where, orderNo);
		} catch (Exception e) {
			logger.error("根据单后获取明细异常 条件:{}", where);
			throw new DaoException(e);
		}
		return orderDetails;
	}

	@Override
	public List<OrderDetail> getOrderDetails(Collection<String> OrderNo) throws DaoException {
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
	public Double getPriceByProductId(Collection<String> OrderNo, Collection<Integer> ProductId) throws DaoException {
		Double price=0.0;
		String where = "WHERE  OrderNo IN (:OrderNo) AND ProductId IN(:ProductId)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("OrderNo", OrderNo);
		map.put("ProductId", ProductId);
		try {
			List<OrderDetail> details= find(where, map);
			if(details!=null){
				for (OrderDetail orderDetail : details) {
					price+=orderDetail.getUnitPrice()*orderDetail.getQuantity();
				}
			}
		} catch (Exception e) {
			logger.error("hql: {}, 订单号{},产品id{}", where, OrderNo,ProductId);
			throw new DaoException(e);
		}
		return price;
	}
	
	@Override
	public List<OrderDetail> getAssignProsList(List<String> ordersNos ,List<Integer> proIds) throws DaoException{
		String sql = "WHERE  orderNo IN (:orderNo)  AND productId IN (:productId) ";
		Map<String, Object> condition =  new HashMap<String, Object>();
		// 变换主键
		condition.put("orderNo", ordersNos);
		condition.put("productId", proIds);
		try{
			return this.find(sql, condition);
		}catch (Exception e) {
			logger.error("hql: {}, 订单号{},产品id{}", sql, ordersNos,proIds);
			throw new DaoException(e);
		}
	}
	
	@Override
	@Transactional
	public List<OrderDetail> findByCreateTime(Date start, Date end) throws DaoException {
		String formatStart = DateTime.formatDate(DateTime.YYYYMMDD, start);
		String formatEnd = DateTime.formatDate(DateTime.YYYYMMDD, end);

		Long startTime = TypeConverter.toLong(formatStart.substring(1)) * 10000;
		Long endTime = TypeConverter.toLong(formatEnd.substring(1)) * 10000;

		String where = "Id >= (:startTime) and Id <= (:endTime)";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			return find(where, map);
		} catch (Exception e) {
			logger.error("condition:{}", where);
			throw new DaoException(e);
		}
	}
}
