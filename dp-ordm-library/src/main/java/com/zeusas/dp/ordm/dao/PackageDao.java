package com.zeusas.dp.ordm.dao;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.IPackage;

public interface PackageDao extends Dao<IPackage, String> {
	
	public IPackage findByOrderNo(String orderNo)throws DaoException;

}
