package com.zeusas.dp.ordm.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.dao.NotificationDao;
import com.zeusas.dp.ordm.entity.Notification;

@Repository
public class NotificationDaoImpl extends CoreBasicDao<Notification, Integer> implements NotificationDao {
	private static Logger logger = LoggerFactory.getLogger(NotificationDaoImpl.class);

	@Override
	public List<Notification> loadLast(int size) throws DaoException {
		String hql = "WHERE active = ?";
		String orderBy = "ORDER BY priority DESC, lastUpdate DESC";
		try {
			return getScrollData(0, size, hql, orderBy, Boolean.TRUE);
		} catch (Exception e) {
			logger.error("HQL:{}, Order By:{}", hql, orderBy);
			throw new DaoException(e);
		}
	}

}
