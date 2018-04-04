package com.zeusas.dp.ordm.active.service;

import java.util.Date;
import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.active.model.Activity;

public interface ActivityService  extends IService<Activity, String> {

	/** 添加一个活动*/
	void add(Activity activity) throws ServiceException;
	/**
	 * 取指定时间开始的所有活动
	 * @param date
	 * @return
	 */
	List<Activity> findAll(Date date);
}
