package com.zeusas.dp.ordm.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.dao.PackageDao;
import com.zeusas.dp.ordm.entity.IPackage;

@Repository
public class PackageDaoImpl extends CoreBasicDao<IPackage, String>implements PackageDao {
	
	private static Logger logger = LoggerFactory.getLogger(PackageDaoImpl.class);
	
	@Override
	public IPackage findByOrderNo(String orderNo) throws DaoException {
		String where = "WHERE  orderNo =(:orderNo)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderNo", orderNo);
		try {
			List<IPackage> packages=find(where, map);
			return packages.isEmpty()?null:packages.get(0);
		} catch (Exception e) {
			logger.error("hql: {}, OrderNo{}", where, orderNo);
			throw new DaoException(e);
		}
	}

}
