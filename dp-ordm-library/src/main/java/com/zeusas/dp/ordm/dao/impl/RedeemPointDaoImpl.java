package com.zeusas.dp.ordm.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.dao.RedeemPointDao;
import com.zeusas.dp.ordm.entity.RedeemPoint;
@Repository
public class RedeemPointDaoImpl extends CoreBasicDao<RedeemPoint, Long> implements RedeemPointDao {
	private static Logger logger = LoggerFactory.getLogger(RedeemPointDaoImpl.class);
	
	@Override
	public List<RedeemPoint> findByCounterCode(String counterCode) throws DaoException {
		String where = "WHERE  counterCode = (:counterCode)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("counterCode", counterCode);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("Condition: {}, 柜台号{}", where, counterCode);
			throw new DaoException(e);
		}
	}

	@Override
	public List<RedeemPoint> findByCounterCode(String counterCode, boolean avalible) throws DaoException {
		String where = "WHERE  counterCode = (:counterCode) and avalible =(:avalible)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("counterCode", counterCode);
		map.put("avalible", avalible);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("Condition: {}, 柜台号{}", where, counterCode);
			throw new DaoException(e);
		}
	}

	@Override
	public List<RedeemPoint> findByExcuteNo(String excuteNo) throws DaoException {
		String where = "WHERE  excuteNo = (:excuteNo)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("excuteNo", excuteNo);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("Condition: {}, 执行单号{}", where, excuteNo);
			throw new DaoException(e);
		}
	}

	@Override
	public List<RedeemPoint> findTodoByCounterCode(String counterCode) throws DaoException {
		String where = "WHERE  counterCode = (:counterCode) and avalible =1 and (excuteNo is null or excuteNo='')";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("counterCode", counterCode);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("Condition: {}, 柜台号{}", where, counterCode);
			throw new DaoException(e);
		}
	}

	@Override
	public List<RedeemPoint> findTodo() throws DaoException {
		String where = "WHERE (excuteNo is null or excuteNo='') and avalible =1";
		try {
			return find(where);
		} catch (Exception e) {
			logger.error("Condition: {}", where);
			throw new DaoException(e);
		}
	}

	@Override
	public List<RedeemPoint> findByCreatTime(long start, long end) throws DaoException {
		String where = "WHERE  creatTime >= (:start) and creatTime <(:end)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("end", end);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("Condition: {}, 筛选时间{}—{}", where, start, end);
			throw new DaoException(e);
		}
	}
	
	@Override
	public List<RedeemPoint> findAvalible() throws DaoException {
		String where = "WHERE Avalible = TRUE ORDER BY ID DESC";
		try {
			return find(where);
		} catch (Exception e) {
			logger.error("Condition: {}", where);
			throw new DaoException(e);
		}
	}


}
