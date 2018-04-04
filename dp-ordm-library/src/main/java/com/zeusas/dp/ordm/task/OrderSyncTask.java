package com.zeusas.dp.ordm.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.zeusas.common.data.DDLDBMS;
import com.zeusas.common.data.Database;
import com.zeusas.common.data.DdlItem;
import com.zeusas.common.data.Record;
import com.zeusas.common.data.Table;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.task.CronTask;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.service.OrderService;


/**
 * SELECT FHeadSelfB0170
 FStatus,
 FHeadSelfB0165 --(审核长时间 反审核后值会变化 只取一次)
FROM  [AIS20130314203706].[dbo].[ICStockBill]  
WHERE  FHeadSelfB0170 is not null 
AND FHeadSelfB0170 <>''             --取直发系统单号不为空的订单
AND frob=1  --红蓝字 (该条件不需要)
AND FCancellation=0   --是否作废
AND fdate is not null                  
AND fdate>DateAdd(day,-15,GETDATE())  --取最近15天的订单 
AND fheadselfb0164='11052' --订单类型(推物流后 反审核 该状态会变化 该条件也去掉)
AND FStatus=1  --(是否审核)
 * @author fengx
 *@date 2017年3月13日 下午3:11:48
 */
public class OrderSyncTask  extends CronTask{

	static final Logger logger = LoggerFactory.getLogger(OrderSyncTask.class);
	
	public OrderSyncTask() {
		valid = DDLDBMS.load("task/sync_order_ddl.xml");
	}
	
	@Override
	public void exec() throws Exception {
		OrderService orderService=AppContext.getBean(OrderService.class);
		// 先查询直发系统有没有待金蝶审核的订单
		List<Order> orders = getToSendOrders(orderService);
		if (orders == null || orders.isEmpty()) {
			return;
		}
		
		DdlItem itm = DDLDBMS.getItem("SYNC_ORDER");
		Assert.notNull(itm,"");
		
		Database dbK3 = new Database(itm);
		try {
			Table tb = dbK3.open("SYNC_ORDER",-15);
			// key --orderNo 订单号
			for (Order o : orders) {
				// 通过订单号orderno去判断订单是否在同步过来的records中
				Record r = tb.get(o.getOrderNo());
				if (r == null) {
					logger.debug("{} 订单不在金蝶同步的记录中", o);
					continue;
				}
				// 获取订单在金蝶系统里面的状态

				o.setOrderStatus(Order.status_WaitShip);
				o.setAuditTime(TypeConverter.toDate(r.get(3)));
				orderService.update(o);
				
				// 执行物流推送已经发货 2017-05-02替换新模板，老模板是：【SMS_60385280】
				// 暂停使用短信模板
//				String template = "SMS_63875449";
//				HashMap<String,String> ctx = new HashMap<String,String>();
//				ctx.put("orderid","№:"+o.getOrderNo());
//				smsService.send(template, o.getPhone(), ctx);
			}
			StorehousesSyncTask synctask = AppContext.getBean(StorehousesSyncTask.class);
			synctask.setUpdated();
		} finally {
			dbK3.closeAll();
		}
	
	}
	
	/**
	 * 获取本系统待推送到心怡状态的订单  
	 */
	private List<Order> getToSendOrders(OrderService orderService) {
		List<Order> orders = null;
		try {
			orders = orderService.getOrders(Order.status_DoLogisticsDelivery);
		} catch (Exception e) {
			logger.error("订单获取信息异常");
		}
		return orders;
	}

	@Override
	protected boolean ready() {
		return valid;
	}

}
