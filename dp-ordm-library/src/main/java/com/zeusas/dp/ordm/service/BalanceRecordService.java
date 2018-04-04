package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.BalanceRecord;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.security.auth.entity.AuthUser;

public interface BalanceRecordService extends IService<BalanceRecord, Long> {
	public void save(BalanceRecord t, Customer customer, AuthUser authUser)
			throws ServiceException;

	public List<BalanceRecord> findByCustomerId(Integer customerId)
			throws ServiceException;
}
