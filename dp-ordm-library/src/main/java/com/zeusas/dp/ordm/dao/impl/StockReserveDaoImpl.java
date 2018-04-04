package com.zeusas.dp.ordm.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.dao.StockReserveDao;
import com.zeusas.dp.ordm.entity.StockReserve;

@Repository
public class StockReserveDaoImpl extends CoreBasicDao<StockReserve, String> implements StockReserveDao {
	private static Logger logger = LoggerFactory.getLogger(StockReserveDaoImpl.class);

	@Override
	public List<StockReserve> findReserveProduct(Integer productId) throws DaoException {
		String where = "WHERE  productId IN (:productId)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productId", productId);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("Condition: {}, 产品id{}", where, productId);
			throw new DaoException(e);
		}
	}

	@Override
	public List<StockReserve> findByDesc(String description) throws DaoException {
		String where = "WHERE description like ?";
		try {
			return find(where, '%' + description + '%');
		} catch (Exception e) {
			logger.error("hql: {},description{}", where, description);
			throw new DaoException(e);
		}
	}

	@Override
	public List<StockReserve> findAvalible() throws DaoException {
		long now = System.currentTimeMillis();
		String where = "WHERE startTime <= (:startTime) and endTime >(:endTime) and valid =1";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startTime", now);
		map.put("endTime", now);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {},description{}", where);
			throw new DaoException(e);
		}
	}

	@Override
	public List<StockReserve> findAvalibleByProductId(Integer pid) throws DaoException {
		long now = System.currentTimeMillis();
		String where = "WHERE startTime <= (:startTime) AND endTime >(:endTime) AND valid =1 AND productId =(:productId) ORDER BY endTime DESC";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startTime", now);
		map.put("endTime", now);
		map.put("productId", pid);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {},description{}", where);
			throw new DaoException(e);
		}
	}

	@Override
	public List<StockReserve> findByProductId(Integer pid) throws DaoException {
		String where = "WHERE productId=?";
		try {
			return find(where, pid);
		} catch (Exception e) {
			logger.error("hql: {},description{}", where);
			throw new DaoException(e);
		}
	}

	@Override
	public List<StockReserve> findCancelByProductId(Integer pid) throws DaoException {
		String where = "WHERE productId=? AND valid = 0 ORDER BY lastUpdate DESC";
		try {
			return find(where, pid);
		} catch (Exception e) {
			logger.error("hql: {},description{}", where);
			throw new DaoException(e);
		}
	}

	@Override
	public List<StockReserve> findExpireByProductId(Integer pid) throws DaoException {
		long now = System.currentTimeMillis();
		String where = "WHERE productId =(:productId) AND endTime <(:endTime) AND valid = 1 ORDER BY endTime DESC";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productId", pid);
		map.put("endTime", now);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {},description{}", where);
			throw new DaoException(e);
		}
	}

}
