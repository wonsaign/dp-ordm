package com.zeusas.dp.ordm.dao;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.FixedPrice;

public interface FixedPriceDao extends Dao<FixedPrice, Integer>  {
	
	public FixedPrice getByProductId(Integer productId)throws DaoException;

}
