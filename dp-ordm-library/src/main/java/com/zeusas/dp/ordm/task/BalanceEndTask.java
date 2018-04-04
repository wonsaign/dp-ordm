package com.zeusas.dp.ordm.task;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zeusas.common.data.DDLDBMS;
import com.zeusas.common.data.Database;
import com.zeusas.common.data.DdlItem;
import com.zeusas.common.data.Proc;
import com.zeusas.common.data.Record;
import com.zeusas.common.data.Table;
import com.zeusas.common.task.CronTask;
import com.zeusas.core.utils.QueryHelper;

public class BalanceEndTask extends CronTask {
	
	static final Logger logger = LoggerFactory.getLogger(BalanceEndTask.class);
	
	final static String DDL = "task/balance_end_ddl.xml";
	
	final static String DDL_NAME_WMS = "WMS_BALANCE_SYNC"; 
	
	final static String TABLE_YEAR="GET_YEAR_ACCOUNT";
	
	public BalanceEndTask() {
		valid = DDLDBMS.load(DDL);
	}

	@Override
	public void exec() throws Exception {
		// TODO: 同步代码处理 
		DdlItem wms_item = DDLDBMS.getItem(DDL_NAME_WMS);
 
		Database db_wms = new Database(wms_item);
		try {
			// 删除直发表中的收款单、销售出库单数据
			snycEndMoney(db_wms); 
		} finally { 
			db_wms.closeAll();
		}
	}

	void snycEndMoney(Database db_wms) throws SQLException {
		// 删除直发系统的收款信息
		Table tb1 = db_wms.open(TABLE_YEAR);
		int maxYear = 0;
		int maxPeriod = 0;

		Connection conn = db_wms.connect();

		if (tb1.size() > 0) {
			for (Record rec : tb1.values()) {
				maxYear = rec.getInteger(1);
				maxPeriod = rec.getInteger(2);
			}
		}
		DdlItem ddlItem = db_wms.getDDL();
		Proc proc1 = ddlItem.getProc("CUS_BALANCE_END_1");
		Proc proc2 = ddlItem.getProc("CUS_BALANCE_END_2");
		Proc proc3 = ddlItem.getProc("CUS_BALANCE_END_3");
		Proc proc4 = ddlItem.getProc("CUS_BALANCE_END_4");
		Proc proc5 = ddlItem.getProc("CUS_BALANCE_END_5");
		Proc proc6 = ddlItem.getProc("CUS_BALANCE_END_6");
		
		if (maxYear == 0 && maxPeriod == 0) {
			Calendar cal = Calendar.getInstance();
			// FIXME
			cal.add(Calendar.YEAR, Calendar.MONTH == 0 ? -1 : 0);
			cal.add(Calendar.MONTH, Calendar.MONTH == 0 ? 12 : 0);
			maxYear = cal.get(Calendar.YEAR);
			maxPeriod = cal.get(Calendar.MONTH);
		}
		
		QueryHelper.execUpdate(conn, proc1.getStatement(), maxYear, maxPeriod);
		QueryHelper.execUpdate(conn, proc2.getStatement(), maxYear, maxPeriod);
		QueryHelper.execUpdate(conn, proc3.getStatement(), maxYear, maxPeriod);
		QueryHelper.execUpdate(conn, proc4.getStatement(), maxYear, maxPeriod,maxYear, maxPeriod);
		QueryHelper.execUpdate(conn, proc5.getStatement(),maxPeriod!=12?maxYear:maxYear+1,maxPeriod==12?1:maxPeriod+1, maxYear, maxPeriod);
		QueryHelper.execUpdate(conn, proc6.getStatement());
	}
 
	@Override
	protected boolean ready() {
		return valid;
	}

}
