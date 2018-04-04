package com.zeusas.dp.ordm.active.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.zeusas.common.service.IdGenService;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.active.dao.ActivityDao;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.service.ActivityService;

@Transactional
@Service
public class ActivityServiceImpl extends BasicService<Activity, String>
		implements ActivityService {

	static Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);

	String ACTID = "ACTIVITYID";

	@Autowired
	private ActivityDao dao;
	@Autowired
	private IdGenService idGenService;
	
	@Override
	protected Dao<Activity, String> getDao() {
		return dao;
	}

	@Override
	public void add(Activity activity) throws ServiceException {
		Assert.notNull(activity);
		if (activity.getContext().getActityGoods() == null&&   //
				activity.getContext().getActityExtra()==null) {
			logger.warn("活动创建失败:{}", activity);
			throw new ServiceException("活动创建失败:" + activity);
		}
		String actId;
		try {
			idGenService.lock(ACTID);
			// ID Schema: YYYYMMDD0000
			actId = idGenService.generateDateId(ACTID);
			activity.setActId(actId);
			activity.setLastUpdate(System.currentTimeMillis());
			dao.save(activity);
		} catch (Exception e) {
			logger.warn("活动主键创建失败:", e);
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ACTID);
		}
	}

	/**
	 * 禁止活动service调用删除活动的方法
	 */
	@Override
	public int delete(String ids) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int delete(String[] ids) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Activity> findAll(Date date) {
		// FIXME: 使用Date,取Date后到今天的所有活动
		return super.findAll();
	}
}
