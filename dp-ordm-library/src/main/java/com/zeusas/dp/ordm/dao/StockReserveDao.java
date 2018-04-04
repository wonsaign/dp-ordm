package com.zeusas.dp.ordm.dao;

import java.util.List;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.StockReserve;

public interface StockReserveDao extends Dao<StockReserve, String> {

	public List<StockReserve> findReserveProduct(Integer pid) throws DaoException;

	public List<StockReserve> findByDesc(String keyword) throws DaoException;

	public List<StockReserve> findAvalible() throws DaoException;

	public List<StockReserve> findAvalibleByProductId(Integer pid) throws DaoException;

	public List<StockReserve> findByProductId(Integer pid) throws DaoException;

	public List<StockReserve> findCancelByProductId(Integer pid) throws DaoException;

	public List<StockReserve> findExpireByProductId(Integer pid) throws DaoException;
}
