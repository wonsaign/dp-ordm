package com.zeusas.dp.ordm.service;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.CmbcPayRecord;

/**
 * 
 * @author fengx
 *@date 2017年1月17日 下午3:57:28
 */
public interface CmbcPayRecordService extends IService<CmbcPayRecord, String> {

	/**
	 * 民生付是校验支付成功与否
	 * @param cmbcOrderNo
	 * @param txtAmt
	 * @param billStatus
	 * @return
	 * @throws ServiceException
	 */
	boolean checkSuccess(String cmbcOrderNo, String txtAmt, String billStatus) throws ServiceException;

	/**
	 * 根据单号获取凭证
	 * @param orderNo
	 * @return
	 */
	CmbcPayRecord getByOrderNo(String orderNo);


}
