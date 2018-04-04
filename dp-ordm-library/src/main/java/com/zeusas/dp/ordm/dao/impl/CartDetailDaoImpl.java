package com.zeusas.dp.ordm.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.dao.CartDetailDao;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.CartDetail;

/**
 * 
 * @author shihx
 * @date 2016年12月6日 下午3:04:52
 */

@Repository
public class CartDetailDaoImpl extends CoreBasicDao<CartDetail, Long> implements CartDetailDao {
	
	private static Logger logger = LoggerFactory.getLogger(CartDetailDaoImpl.class);
	@Override
	public List<CartDetail> getCartDetailByCart(Cart cart) throws DaoException {
		String where = "WHERE cartId = ?";
		List<CartDetail> cartDetails;
		Long  cartId = cart.getCartId();
		try {
			cartDetails = find(where, cartId);
		}catch (Exception e) {
			logger.error("获取所有购物车明细异常 ,hql:{},cartID:{}",where,cartId);
			throw new DaoException(e);
		}
		getCurrentSession().clear();
		return cartDetails;
	}

	@Override
	public void deleteCart(Long cartId) throws DaoException {
		String sql = "DELETE FROM " + entityClass.getName() + " WHERE CartId = ?";
		// 删除明细的所有的描述
		try{
			super.execute(sql, cartId);
		}catch (Exception e) {
			logger.error("删除购物车异常 ,hql:{},cartID:{}",sql,cartId);
			throw new DaoException(e);
		}
		getCurrentSession().clear();
	}

	@Override
	public void save(CartDetail t) throws DaoException{
		try{
			super.getCurrentSession().save(t);
		}catch (Exception e) {
			logger.error("保存购物车明细异常");
			throw new DaoException(e);
		}
	}

	@Override
	public List<CartDetail> findItems(Long cartId) throws DaoException {
		List<CartDetail> cds ;
		String hql = "WHERE cartId =?";
		try{
			cds = super.find(hql, cartId);
		}catch (Exception e) {
			logger.error("获取明细Item异常 ,hql:{},cartID:{}",hql,cartId);
			throw new DaoException(e);
		}
		getCurrentSession().clear();
		return cds ;
	}

	@Override
	public Long findSuitActive(String actId, Long cartId) throws DaoException {
		List<CartDetail> cartDetails;
		String hql = "WHERE cartId =? and activityId =? ";
		try {
			/** 判断购物车明细表有没有数据 */
			cartDetails = super.find(hql, cartId, actId);
		}catch (Exception e) {
			logger.error("获取正确的活动 ,hql:{},cartID:{}",hql,cartId);
			throw new DaoException(e);
		}
		return cartDetails.isEmpty() ? null :cartDetails.get(0).getDetailId();
		
	}

	@Override
	public void deleteByActId(Long cartId,Collection<String> activityId) throws DaoException {
		String where = "WHERE  cartId = (:cartId) AND activityId IN (:activityId)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cartId", cartId);
		map.put("activityId", activityId);
		try {
			List<CartDetail> details= find(where, map);
			if(details!=null){
				Long[] ids=new Long[details.size()];
				List<Long> Ids=new ArrayList<>(details.size());
				for (CartDetail cartDetail : details) {
					Ids.add(cartDetail.getCartId());
				}
				Ids.toArray(ids);
				delete(ids);
			}
		} catch (Exception e) {
			logger.error("根据活动ID删除异常 ,cartID:{}",cartId);
			throw new DaoException(e);
		}
	}

	@Override
	public List<CartDetail> findByCarts(List<Long> cartId) throws DaoException {
		String where = "WHERE  cartId IN (:cartId)";
		List<CartDetail> details;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cartId", cartId);
		try {
			details= find(where, map);
		} catch (Exception e) {
			logger.error("根据活动ID删除异常 ,cartID:{}",cartId);
			throw new DaoException(e);
		}
		return details;
	}

}
