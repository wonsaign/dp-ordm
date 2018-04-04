package com.zeusas.dp.ordm.dao;

import java.util.List;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.Notification;

public interface NotificationDao extends Dao<Notification, Integer> {
	List<Notification> loadLast(int size) throws DaoException;
}
