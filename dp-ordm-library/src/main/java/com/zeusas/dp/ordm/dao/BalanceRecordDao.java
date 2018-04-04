package com.zeusas.dp.ordm.dao;

import java.util.List;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.BalanceRecord;

public interface BalanceRecordDao extends Dao<BalanceRecord, Long> {
	public void save(BalanceRecord t)throws DaoException;
	public List<BalanceRecord> findByCustomerId(Integer customerId)throws DaoException;
}
