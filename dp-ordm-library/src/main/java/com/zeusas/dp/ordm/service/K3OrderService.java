package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.dp.ordm.entity.K3Order;

/**
 * 
 * @author wangs
 *
 */
public interface K3OrderService extends IService<K3Order, Integer>{

	List<K3Order> getByCustomerId(Integer cusId, String startTime, String endTime);

	/**通过订单日期*/
	List<K3Order> getByCustomerIdAndTime(Integer cusId, String startTime, String endTime);
}
