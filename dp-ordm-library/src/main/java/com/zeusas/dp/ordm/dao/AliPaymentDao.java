package com.zeusas.dp.ordm.dao;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.AliPayment;

/**
 * 
 * @author fengx
 *@date 2017年1月17日 下午3:55:36
 */
public interface AliPaymentDao  extends Dao<AliPayment, String>{

	void buildK3Receive(String orderNo, double paymenprice, Integer fCustomer, Integer i) throws DaoException;

	void buildK3OtherReceive(String orderNo, double paymenprice, Integer fCustomer) throws DaoException;
}
