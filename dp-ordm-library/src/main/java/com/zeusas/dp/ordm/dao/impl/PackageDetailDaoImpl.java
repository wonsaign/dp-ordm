package com.zeusas.dp.ordm.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.dao.PackageDetailDao;
import com.zeusas.dp.ordm.entity.PackageDetail;

@Repository
public class PackageDetailDaoImpl extends CoreBasicDao<PackageDetail, String>implements PackageDetailDao {
	private static Logger logger = LoggerFactory.getLogger(PackageDetailDaoImpl.class);
	@Override
	public List<PackageDetail> findByPackageId(String packageId) throws DaoException {
		String where = "WHERE  packageId =(:packageId)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("packageId", packageId);
		try {
			return find(where, map);
		} catch (Exception e) {
			logger.error("hql: {}, packageId{}", where, packageId);
			throw new DaoException(e);
		}
	}

}
