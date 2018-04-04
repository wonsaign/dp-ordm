package com.zeusas.dp.ordm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.dp.ordm.dao.ActivityLimitDao;
import com.zeusas.dp.ordm.entity.ActivityLimit;
import com.zeusas.dp.ordm.service.ActivityLimitService;
@Service
@Transactional
public class ActivityLimitServiceImpl extends BasicService<ActivityLimit, Integer> implements ActivityLimitService {
	@Autowired
	private ActivityLimitDao dao;
	
	@Override
	protected Dao<ActivityLimit, Integer> getDao() {
		return dao;
	}

}
