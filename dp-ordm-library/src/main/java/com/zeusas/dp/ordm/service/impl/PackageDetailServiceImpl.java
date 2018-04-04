package com.zeusas.dp.ordm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.dp.ordm.dao.PackageDetailDao;
import com.zeusas.dp.ordm.entity.PackageDetail;
import com.zeusas.dp.ordm.service.PackageDetailService;

@Service
public class PackageDetailServiceImpl extends BasicService<PackageDetail, String> implements PackageDetailService {

	@Autowired
	private PackageDetailDao dao;

	@Override
	protected Dao<PackageDetail, String> getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

}
