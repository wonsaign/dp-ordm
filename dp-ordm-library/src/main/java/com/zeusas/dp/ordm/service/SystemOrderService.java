package com.zeusas.dp.ordm.service;

import java.util.Collection;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Item;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.ReserveRecord;
import com.zeusas.security.auth.entity.AuthUser;

public interface SystemOrderService extends IService<Order, Long> {
	/**
	 * 系统制单
	 * @param counter
	 * @param makeUser
	 * @param customerTypeId
	 * @param detail 月度物料等
	 * @return
	 * @throws ServiceException
	 */
	Order buildOrder(Counter counter, AuthUser makeUser, Integer customerTypeId,Collection<Item> detail)
			throws ServiceException;
	/**
	 * 还欠：系统制单
	 * @param counter
	 * @param makeUser
	 * @param customerTypeId
	 * @param detail 打欠记录
	 * @return
	 * @throws ServiceException
	 */
	Order buildOrderForReserve(Counter counter, AuthUser makeUser, Integer customerTypeId,Collection<ReserveRecord> record)
			throws ServiceException;
	
	/**
	 * 礼盒导入生成代付款订单，执行操作读取preOrder状态为0(未处理)的预订单-执行系统下单，计算物料，运费，总价等等生成订单明细，凭证，单头，之前的preOrder状态置为已处理
	 * 
	 */
	public void transPreOrderToOrder();
}
