package com.zeusas.dp.ordm.dao;

import java.util.Collection;
import java.util.List;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.Counter;
/**
 * 
 * @author shihx
 * @date 2016年12月6日 下午3:00:17
 */
public interface CartDao extends Dao<Cart, Long> {
	
	/** 获取门店购物车*/
	Cart getCounterCart(Counter counter) throws DaoException;

	/**获取所有的购物车*/
	List<Cart> findAllCart(Collection<Integer> counterIds) throws DaoException;
	/**获取所有待审核的购物车*/
	List<Cart> getCheckCarts(List<Integer> counterIds)throws DaoException;
}
