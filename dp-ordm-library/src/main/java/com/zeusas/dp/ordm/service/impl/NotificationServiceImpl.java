package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.dao.NotificationDao;
import com.zeusas.dp.ordm.entity.Notification;
import com.zeusas.dp.ordm.service.NotificationService;

@Service
@Transactional
public class NotificationServiceImpl //
		extends BasicService<Notification, Integer> //
		implements NotificationService {
	
	final static int MAX_SIZE = 32;

	@Autowired
	private NotificationDao dao;

	private long lastUpdate;

	final transient List<Notification> last = new ArrayList<Notification>();

	public NotificationServiceImpl() {
		lastUpdate = 0;
	}

	@Override
	protected Dao<Notification, Integer> getDao() {
		return dao;
	}

	public List<Notification> getLast(int size) {
		long now = System.currentTimeMillis();
		if (last.size() == 0 || now - lastUpdate > 30 * 60 * 1000L) {
			loadLast(MAX_SIZE);
		}
		List<Notification> result = new ArrayList<Notification>(size);
		for (int i = 0; i < size && i < last.size(); i++) {
			result.add(last.get(i));
		}
		return result;
	}

	private List<Notification> loadLast(int size) {
		this.lastUpdate = System.currentTimeMillis();
		try {
			List<Notification> rr = dao.loadLast(size);
			last.clear();
			last.addAll(rr);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return last;
	}

	@Transactional(rollbackFor = Exception.class, readOnly = false)
	public int update(Integer pk, Map<String, Object> values) {
		int r = 0;
		try {
			values.put("lastUpadate", System.currentTimeMillis());
			r = dao.update(pk, values);
			loadLast(MAX_SIZE);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return r;
	}

	@Transactional
	@Override
	public void save(Notification notif) {
		notif.setLastupdate(System.currentTimeMillis());
		try {
			dao.save(notif);
			loadLast(MAX_SIZE);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Transactional
	public int delete(Integer i) {
		int r;
		try {
			r = dao.delete(i);
			loadLast(MAX_SIZE);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return r;
	}

	@Transactional
	@Override
	public Notification update(Notification notif) {
		try {
			notif.setLastupdate(System.currentTimeMillis());
			dao.update(notif);
			loadLast(MAX_SIZE);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return notif;
	}
}
