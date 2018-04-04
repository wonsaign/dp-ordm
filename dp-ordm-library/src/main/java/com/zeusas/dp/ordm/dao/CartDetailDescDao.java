package com.zeusas.dp.ordm.dao;

import java.util.List;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.CartDetailDesc;

public interface CartDetailDescDao extends Dao<CartDetailDesc, Long> {
	
	 List<CartDetailDesc> getCartDescByCartDetail(CartDetail cartDetail) throws DaoException;

	 public List<CartDetailDesc> getCartDescByCartDetail(List<Long> detailIds) throws DaoException;
	 
	void  deleteDetailAndDesc(Long detailId) throws DaoException;

}
