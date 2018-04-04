package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.dp.ordm.entity.K3Receive;

/**
 * 
 * @author wangs
 *
 */
public interface K3ReceiveService extends IService<K3Receive, Integer>{

	List<K3Receive> getByCustomerId(Integer cusId, String startTime, String endTime);
	/**通过客户id和订单日期*/
	List<K3Receive> getByCustomerIdAndTime(Integer cusId, String startTime, String endTime);
}
