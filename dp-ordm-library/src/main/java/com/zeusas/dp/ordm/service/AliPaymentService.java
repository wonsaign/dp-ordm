package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.AliPayment;
import com.zeusas.security.auth.entity.AuthUser;

/**
 * 
 * @author fengx
 *@date 2017年1月17日 下午3:57:28
 */
public interface AliPaymentService extends IService<AliPayment, String> {

	public boolean checkSuccess(String subject, String total_fee,
			String out_trade_no) throws ServiceException;
	
	public boolean combineCheckSuccess(String ali_total_fee,
			String ali_trade_no) throws ServiceException;
	
	public void updateAlipayStatus(String payStatus,String orderNo,double paymentPrice) throws ServiceException;
	
	public boolean updateOUN(String orderID,AuthUser user)throws ServiceException;
	
	public boolean updateCombineOUN(List<String> orderNos, AuthUser user)throws ServiceException;
}
