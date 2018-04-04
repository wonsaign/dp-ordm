package com.zeusas.dp.ordm.dao;

import java.util.Collection;
import java.util.List;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.CartDetail;
/**
 * 
 * @author shihx
 * @date 2016年12月6日 下午3:03:16
 */
public interface CartDetailDao extends Dao<CartDetail, Long> {
	
	 List<CartDetail> getCartDetailByCart(Cart cart) throws DaoException;
	 
	 void deleteCart(Long cartId)throws DaoException;
	 
	 
	 List<CartDetail> findItems(Long cartId) throws DaoException;
	 
	 
	 void save(CartDetail cartDetail) throws DaoException;
	 
	 Long findSuitActive(String actId, Long cartId)throws DaoException;
	 /**删除指定活动明细 */
	 void deleteByActId(Long cartId,Collection<String> actIds)throws DaoException;
	 
	 List<CartDetail> findByCarts(List<Long> cartIds) throws DaoException;

}
