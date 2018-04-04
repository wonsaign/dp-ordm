package com.zeusas.dp.ordm.task;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.task.CronTask;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.service.ActivityLimitManager;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.RedeemPointService;
import com.zeusas.dp.ordm.utils.OrdmConfig;

/**
 * 撤销订单
 * <p>
 * 当订单超过一定时间后，系统将锁定订单，4H或8H后，自动撤销 订单。
 *
 */
public class CancelOrdersTask extends CronTask {
	static final Logger logger = LoggerFactory
			.getLogger(CancelOrdersTask.class);

	@Autowired
	OrderService orderService;
	@Autowired
	RedeemPointService redeemPointService;

	@Override
	public void exec() throws Exception {
		// 未付款和财务退回
		List<Order> orders;
		Set<String> status = new HashSet<>();
		status.add(Order.Status_UnPay);
		status.add(Order.status_ForFinancialRefuse);
		
		orders = orderService.getOrders(status);
		// 遍历订单 判断订单是否超过
		Long nowTime = System.currentTimeMillis();
		int tiemoutH = getOrderTimeout();
		if (!orders.isEmpty()) {
			for (Order order : orders) {
				long overtime = nowTime - order.getLastUpdate();
				if (overtime > 1000 * 60 * 60 * tiemoutH) {
					// FIXME 万一在这一瞬间 人家支付了订单如何避免这样的情况的发生
					Order newOrder = orderService.get(order.getId());
					if (newOrder.getOrderStatus().equals(Order.Status_UnPay) ||
							newOrder.getOrderStatus().equals(Order.status_ForFinancialRefuse)) {
						// 自动退回活动限制数量
						ActivityLimitManager alm =	AppContext.getBean(ActivityLimitManager.class);
						List<OrderDetail> details = orderService.getOrderDetails(order.getOrderNo());
						alm.releaseOrderToCart(order, details);
						
						cancelOrder(newOrder);
					}
				}
			}
			
			StorehousesSyncTask synctask = AppContext.getBean(StorehousesSyncTask.class);
			synctask.setUpdated();
		}
	}
	
	private void cancelOrder(Order order) {
		try {
			order.setOrderStatus(Order.Status_Invalid);
			order.setLastUpdate(System.currentTimeMillis());
			order.setOrderCancelTime(System.currentTimeMillis());
			order.setCancelDesc("支付超时，订单作废");
			orderService.update(order);
			redeemPointService.removeRedeem(order.getOrderNo());
			logger.debug("Cancel Order:{} OK!", order);
		} catch (Exception e) {
			logger.warn("获取所有待付款的订单:{}",order,e);
		}
	}
	
	@Override
	protected boolean ready() {
		return true;
	}

	/**
	 * 获取订单超时时间
	 * 
	 * @return
	 */
	protected int getOrderTimeout() {
		DictManager dictManager = AppContext.getBean(DictManager.class);
		Dictionary dict_timeout = dictManager.lookUpByCode(OrdmConfig.ORDMCONFIG, OrdmConfig.ORDERTIMEOUT);
		Assert.notNull(dict_timeout,"");
		int timeout = StringUtil.toInt(dict_timeout.getValue(), 48);
		return timeout;
	}

}
