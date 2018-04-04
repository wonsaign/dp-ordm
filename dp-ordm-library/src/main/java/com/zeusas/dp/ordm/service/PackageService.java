package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.IPackage;
import com.zeusas.dp.ordm.entity.PackageDetail;

public interface PackageService extends IService<IPackage, String> {
	void save(IPackage iPackage,List<PackageDetail>details) throws ServiceException;
	boolean changePackageStatus(String OrderNo,Integer status) throws ServiceException;
	List<IPackage> getPackageByNo(String orderNo) throws ServiceException;
}
