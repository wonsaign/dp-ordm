package com.zeusas.dp.ordm.service;

import java.util.List;
import java.util.Map;

import com.zeusas.core.http.ActionException;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.ActivityLimit;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.LimitContext;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderDetail;

public interface ActivityLimitManager {

	void load();

	ActivityLimit get(Integer customerId);

	Map<String, LimitContext> getLimitContextAvaliable(Integer customerId) throws ActionException;

	Boolean commitCartToOrder(Integer customerid, List<CartDetail> details) throws ServiceException;
	
	/**
	 * 释放订单调用处理，退回到可用活动数量。
	 */
	Boolean releaseOrderToCart(Order order, List<OrderDetail> details) throws ServiceException;
}
