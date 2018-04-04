package com.zeusas.dp.ordm.task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zeusas.common.data.DDLDBMS;
import com.zeusas.common.data.Database;
import com.zeusas.common.data.DdlItem;
import com.zeusas.common.data.Record;
import com.zeusas.common.data.Table;
import com.zeusas.common.task.CronTask;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.service.OrderService;

public class LogisticSyncTask extends CronTask {
	static final Logger logger = LoggerFactory.getLogger(LogisticSyncTask.class);
	final static String TB_K3_ORDER = "K3_ORDER";

	private Database k3DB;

	public LogisticSyncTask() {
		valid = DDLDBMS.load("task/sync_logistics_ddl.xml");
		DdlItem k3Ditem = DDLDBMS.getItem("K3_ORDERS");
		k3DB = new Database(k3Ditem);
	}

	@Override
	public void exec() throws Exception {
		try {
			snycLogistic();
		} finally {
			close();
		}
	}

	@Override
	protected boolean ready() {
		return valid;
	}

	private void close() throws Exception {
		k3DB.closeAll();
	}

	void snycLogistic() throws SQLException {
		Table orderTB = k3DB.open(TB_K3_ORDER);

		OrderService orderService = AppContext.getBean(OrderService.class);
		List<String> shipped = new ArrayList<>(orderTB.values().size());//
		for (Record rec : orderTB.values()) {
			String OrderNo = String.valueOf(rec.getPK());
			shipped.add(OrderNo);
		}
		List<Order> list = orderService.getOrders(Order.status_WaitShip);
		for (Order o : list) {
			if (shipped.contains("BXX20" + o.getId())) {
				orderService.changeOrderStatus(o.getId(), Order.status_CompleteShipping);
			}
		}
	}

}
