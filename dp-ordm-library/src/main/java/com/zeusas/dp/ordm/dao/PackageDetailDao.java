package com.zeusas.dp.ordm.dao;

import java.util.List;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.PackageDetail;

public interface PackageDetailDao extends Dao<PackageDetail, String> {
	
	public List<PackageDetail> findByPackageId(String packageId) throws DaoException;

}
