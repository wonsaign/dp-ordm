package com.zeusas.dp.ordm.dao;

import java.util.List;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.RedeemPoint;

public interface RedeemPointDao extends Dao<RedeemPoint, Long> {
	public List<RedeemPoint> findByCounterCode(String CounterCode) throws DaoException;
	
	public List<RedeemPoint> findByCounterCode(String CounterCode, boolean avalible) throws DaoException;
	
	public List<RedeemPoint> findByExcuteNo(String excuteNo) throws DaoException;
	
	public List<RedeemPoint> findTodoByCounterCode(String CounterCode) throws DaoException;
	
	public List<RedeemPoint> findTodo() throws DaoException;
	
	public List<RedeemPoint> findByCreatTime(long start, long end) throws DaoException;
	
	public List<RedeemPoint> findAvalible() throws DaoException;

}
