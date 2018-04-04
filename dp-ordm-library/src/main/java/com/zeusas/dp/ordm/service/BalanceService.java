package com.zeusas.dp.ordm.service;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.Balance;

public interface BalanceService extends IService<Balance, Integer> {

	/** 金蝶余额 */
	public double getBalance(int customerId) throws ServiceException;

	/** 可用余额 */
	public double getUsefulBalance(int customerId) throws ServiceException;

}
