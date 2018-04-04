package com.zeusas.dp.ordm.dao;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.Balance;

public interface BalanceDao extends Dao<Balance, Integer> {
	public String getBalance(int customerId) throws DaoException;
}
