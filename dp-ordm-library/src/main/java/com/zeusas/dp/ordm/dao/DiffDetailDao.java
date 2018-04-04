package com.zeusas.dp.ordm.dao;

import java.util.List;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.DiffDetail;

public interface DiffDetailDao extends Dao<DiffDetail, Integer>{
	
	List<DiffDetail> getDiffDetails(String orderNo) throws DaoException;
}
