package com.zeusas.dp.ordm.dao;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.CmbcPayRecord;

/**
 * 
 * @author fengx
 *@date 2017年1月17日 下午3:55:36
 */
public interface CmbcPayRecordDao  extends Dao<CmbcPayRecord, String>{

	/**
	 * 民生付存贮过程
	 * @param orderNo
	 * @param paymenprice
	 * @param fCustomer
	 * @throws DaoException
	 */
	void buildK3Receive(String orderNo, double paymenprice, Integer fCustomer) throws DaoException;

}
