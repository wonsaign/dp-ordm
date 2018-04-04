package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.MonthPresent;

public interface MonthPresentService extends IService<MonthPresent, Long> {
	
	public void createMonthPresent(MonthPresent monthPresent) throws ServiceException;
	
	public List<MonthPresent> findByYearMonth(Integer yearMonth) throws ServiceException;
}
