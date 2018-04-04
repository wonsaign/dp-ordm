package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.dp.ordm.entity.K3Acount;

/**
 * 
 * @author wangs
 *
 */
public interface K3AcountService extends IService<K3Acount, Integer>{
	/** 根据客户ID 获取三张表里全部的信息 三张表可以为空*/
	List<K3Acount> getByCustomerId(Integer cusId, String startTime, String endTime);
	/**根据客户id,year,preiod,获取框k3acount*/
	List<K3Acount> getByYearAndPeriod(Integer cusId, Integer year, Integer preiod);

}
