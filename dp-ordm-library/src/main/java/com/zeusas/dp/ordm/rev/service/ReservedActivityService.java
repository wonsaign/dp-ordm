package com.zeusas.dp.ordm.rev.service;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.rev.entity.ReservedActivity;

public interface ReservedActivityService extends IService<ReservedActivity, Integer> {
	void add(ReservedActivity reservedActivity)throws ServiceException;
}
