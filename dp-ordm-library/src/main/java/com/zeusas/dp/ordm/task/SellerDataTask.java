package com.zeusas.dp.ordm.task;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zeusas.common.data.DDLDBMS;
import com.zeusas.common.task.CronTask;
import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.bi.service.ProductSellerService;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.ProductManager;

public class SellerDataTask extends CronTask {
	static final Logger logger = LoggerFactory.getLogger(SellerDataTask.class);
	
	@Autowired
	ProductSellerService ps;
	@Autowired
	CounterManager counterManager;
	@Autowired
	ProductManager productManager;
	
	public SellerDataTask(){
		valid = DDLDBMS.load("task/sync_seller_ddl.xml");
	}

	@Override
	public void exec() throws Exception {
		Calendar cal = Calendar.getInstance();
		String to = DateTime.formatDate(DateTime.YYYYMMDD, cal.getTime());
		cal.add(Calendar.DATE, -36);
		String from = DateTime.formatDate(DateTime.YYYYMMDD, cal.getTime());
		logger.info("同步销售数据，从{}到{}.", from, to);
		try {
			ps.prepare(counterManager.findAll(),
					productManager.findAllAvaible());
			ps.loadData(from, to);
			ps.doFinal();
			ps.doExport();
		} catch (Exception e) {
			logger.error("同步销售数据，从{}到{}.",from, to, e);
		}
		SerialSort.sort();
	}

	@Override
	protected boolean ready() {
		return valid;
	}
}
