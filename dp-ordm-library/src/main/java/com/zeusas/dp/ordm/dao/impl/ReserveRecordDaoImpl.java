package com.zeusas.dp.ordm.dao.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.dao.ReserveRecordDao;
import com.zeusas.dp.ordm.entity.ReserveRecord;
@Repository
public class ReserveRecordDaoImpl extends CoreBasicDao<ReserveRecord, Long> implements ReserveRecordDao {

	private static Logger logger = LoggerFactory.getLogger(ReserveRecordDao.class);
	
	@Override
	public List<ReserveRecord> findByStatus(Integer status) throws DaoException{
		List<ReserveRecord> records;
		String where = " Status = ?";
		try {
			records = super.find(where, status);
		} catch (DaoException e) {
			logger.error("condition:{},类型状态:{}",where,status);
			throw new DaoException(e);
		}
		return records;
	}

	@Override
	public List<ReserveRecord> findByOrderNo(String orderNo) throws DaoException{
		List<ReserveRecord> records;
		String where = " OrderNo = ?";
		try {
			records = super.find(where, orderNo);
		} catch (DaoException e) {
			logger.error("condition:{},类型状态:{}",where,orderNo);
			throw new DaoException(e);
		}
		return records;
	}

	@Override
	public List<ReserveRecord> findByCounterCodeAndStatus(String CounterCode, Integer Status) throws DaoException {
		String where = "WHERE  CounterCode =(:CounterCode)  AND Status =(:Status) ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CounterCode", CounterCode);
		map.put("Status", Status);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {}, 柜台号{},状态{}", where, CounterCode,Status);
			throw new DaoException(e);
		}
	}

	@Override
	public List<ReserveRecord> findByStatus(Collection<Integer> status) throws DaoException {
		String where = "WHERE Status IN (:Status) ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Status", status);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {}, 柜台号{},状态{}", where,status);
			throw new DaoException(e);
		}
	}

	@Override
	public List<ReserveRecord> findByCustomerId(String CustomerId) throws DaoException {
		String where = "WHERE CustomerId  =(:CustomerId) ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CustomerId", CustomerId);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {}, 柜台号{},客户id{}", where,CustomerId);
			throw new DaoException(e);
		}
	}

	@Override
	public List<ReserveRecord> findByCustomerIdAndStatus(String CustomerId, Collection<Integer> Status)
			throws DaoException {
		String where = "WHERE CustomerId =(:CustomerId) AND  Status IN (:Status)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CustomerId", CustomerId);
		map.put("Status", Status);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {}, 柜台号{},客户id{},状态{}", where,CustomerId,Status);
			throw new DaoException(e);
		}
	}

	@Override
	public List<ReserveRecord> findByProductId(Collection<Integer> ProductId) {
		String where = "WHERE ProductId in (:ProductId)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ProductId", ProductId);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {}, 产品id{}", where,ProductId);
			throw new DaoException(e);
		}
	}

	@Override
	public void changeSingleStatus(Integer status, Long orderDetailId)  throws DaoException{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		try {
			update(orderDetailId, map);
		} catch (Exception e) {
			logger.error("更新单条打欠记录失败，主键{}，状态{}",orderDetailId ,status);
			throw new DaoException(e);
		}
	}

	@Override
	public void changeActivityStatus(Integer status, Long pid)  throws DaoException{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		try {
			update(map, "pid=?", pid);
		} catch (Exception e) {
			logger.error("更新单条打欠记录失败，活动标志{}，状态{}",pid ,status);
			throw new DaoException(e);
		}
	}

	@Override
	public List<ReserveRecord> findByPid(Long pid) {
		String where = "WHERE pid  =(:pid) ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {}, pid{},", where,pid);
			throw new DaoException(e);
		}
	}
	
	@Override
	public List<ReserveRecord> search(Collection<String> CustomerId, Collection<String> CounterCode, String OrderNo) {
		String where = "WHERE CounterCode  IN (:CounterCode) AND CustomerId IN (:CustomerId) AND  OrderNo like :OrderNo";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CounterCode", CounterCode);
		map.put("CustomerId", CustomerId);
		map.put("OrderNo", '%' + OrderNo + '%');
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {},CounterCode{},CustomerId{},OrderNo{}", where,CounterCode,CustomerId,OrderNo);
			throw new DaoException(e);
		}
	}
}
