package com.zeusas.dp.ordm.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.dao.PackageDao;
import com.zeusas.dp.ordm.dao.PackageDetailDao;
import com.zeusas.dp.ordm.entity.IPackage;
import com.zeusas.dp.ordm.entity.PackageDetail;
import com.zeusas.dp.ordm.service.PackageService;

@Service
public class PackageServiceImpl extends BasicService<IPackage, String> implements PackageService {

	
	final static Logger logger=LoggerFactory.getLogger(PackageServiceImpl.class);
	
	public final static String ID_PACKAGE="PACKAGEID";
	public final static String ID_PACKAGEDETAIL="PACKAGEDETAIL";
	
	@Autowired
	private PackageDao dao;

	
	@Autowired
	private PackageDetailDao detailDao;
	
	@Override
	protected Dao<IPackage, String> getDao() {
		return dao;
	}

	@Override
	@Transactional
	public void save(IPackage iPackage, List<PackageDetail> details) throws ServiceException {
		try {
			super.save(iPackage);
			for (PackageDetail packageDetail : details) {
				detailDao.save(packageDetail);
			}
		} catch (Exception e) {
			logger.error("生成包裹明细失败", e);
			throw new ServiceException("生成包裹明细失败");
		}
	}

	@Override
	@Transactional
	public boolean changePackageStatus(String OrderNo, Integer status) throws ServiceException {
		String where = "where OrderNo = ?";
		try{
			List<IPackage> iPackages = find(where,OrderNo);
			if(iPackages.isEmpty()){
				throw new ServiceException("订单不存在, 订单状态：" + status);
			}
			IPackage updateiPackage = iPackages.get(0);
			updateiPackage.setStatus(status);
			update(updateiPackage);
			return true;
		}catch (Exception e) {
			logger.error("修改状态异常"+e);
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<IPackage> getPackageByNo(String orderNo) throws ServiceException {
		String where = "where OrderNo = ?";
		List<IPackage> iPackages;
		try{
			iPackages = find(where,orderNo);
		}catch (Exception e) {
			logger.error("包裹查询异常"+e);
			throw new ServiceException(e);
		}
		return iPackages;
	}

}
