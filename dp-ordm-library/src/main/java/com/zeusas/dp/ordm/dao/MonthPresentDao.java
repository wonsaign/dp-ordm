package com.zeusas.dp.ordm.dao;

import java.util.List;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.MonthPresent;

public interface MonthPresentDao extends Dao<MonthPresent, Long>  {

	public List<MonthPresent> findByYearMonth(Integer yearMonth) throws DaoException;
}
