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
import com.zeusas.common.task.CronTask;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.service.CartService;

public class CartCheckTask  extends CronTask{

	static final Logger logger = LoggerFactory.getLogger(CartCheckTask.class);

	public CartCheckTask() {
		valid = DDLDBMS.load("task/check_cart_ddl.xml");
	}
	
	@Override
	public void exec() throws Exception {
		DdlItem itm = DDLDBMS.getItem("WMS_SYNC");
		CartService cartService=AppContext.getBean(CartService.class);
		Assert.notNull(itm);
		Database dbK3 = new Database(itm);
		try {
			Table tb = dbK3.open("CHECK_CART",System.currentTimeMillis(),30*60*1000);
			List<Record> records=tb.values();
			for(Record r:records){
				Cart cart =cartService.get((Long)r.get(1));
				if(cart!=null){
					cart.setStatus(Cart.STATUS_ACTIVE);
					cart.setLastUpdate(System.currentTimeMillis());
					cartService.update(cart);
				}
			}
		} finally {
			dbK3.closeAll();
		}
		StorehousesSyncTask synctask=AppContext.getBean(StorehousesSyncTask.class);
		synctask.setUpdated();
	}
	

	@Override
	protected boolean ready() {
		return valid;
	}

}
