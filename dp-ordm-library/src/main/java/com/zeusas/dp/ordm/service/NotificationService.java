package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.dp.ordm.entity.Notification;

public interface NotificationService extends IService<Notification, Integer> {
	List<Notification> getLast(int size);
	
	public void save(Notification notif);
	
	public Notification update(Notification notif);
	
}
