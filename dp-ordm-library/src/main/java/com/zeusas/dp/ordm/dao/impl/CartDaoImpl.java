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
import com.zeusas.dp.ordm.dao.CartDao;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.Counter;
/**
 * 
 * @author shihx
 * @date 2016年12月6日 下午3:04:41
 */

@Repository
public class CartDaoImpl extends CoreBasicDao<Cart, Long> implements CartDao {
	private static Logger logger = LoggerFactory.getLogger(CartDaoImpl.class);

	public Cart getCounterCart(Counter counter) throws DaoException {
		String cond = "WHERE counterId=?  ORDER BY lastUpdate DESC";
		List<Cart> carts = null;
		Integer cid = counter.getCounterId();
		try {
			carts = find(cond, cid );
		} catch (Exception e) {
			logger.error("根据柜台获取购物车异常 ,hql:{},counterId:{}",cond,cid);
			throw new DaoException(e);
		}
		getCurrentSession().clear();
		return carts.isEmpty()? null:carts.get(0);
	}

	@Override
	public List<Cart> findAllCart(Collection<Integer> counterIds) throws DaoException {
		String where = "WHERE  counterId IN (:counterId) ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("counterId", counterIds);
		List<Cart> carts = null;
		try {
			carts = find(where, map);
		} catch (Exception e) {
			logger.error("获取所有购物车异常 ,hql:{}",where);
			throw new DaoException(e);
		}
		getCurrentSession().clear();
		return carts;
	}

	@Override
	public List<Cart> getCheckCarts(List<Integer> counterIds) throws DaoException {
		String where = "WHERE  counterId IN (:counterId) AND status IN (:status)  ORDER BY lastUpdate ASC";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("counterId", counterIds);
		map.put("status", Cart.STATUS_COMMIT);
		List<Cart> carts;
		try {
			carts = find(where, map);
		} catch (Exception e) {
			logger.error("获取已经提交的购物车 ,hql:{}",where);
			throw new DaoException(e);
		}
		getCurrentSession().clear();
		return carts;
	}

}
