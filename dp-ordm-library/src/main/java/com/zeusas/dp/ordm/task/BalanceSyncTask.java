package com.zeusas.dp.ordm.task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.zeusas.common.data.DDLDBMS;
import com.zeusas.common.data.Database;
import com.zeusas.common.data.DdlItem;
import com.zeusas.common.data.Proc;
import com.zeusas.common.data.Record;
import com.zeusas.common.data.Table;
import com.zeusas.common.task.CronTask;
import com.zeusas.core.utils.QueryHelper;

public class BalanceSyncTask extends CronTask {
	static final Logger logger = LoggerFactory.getLogger(BalanceSyncTask.class);
	final static String DDL = "task/balance_sync_ddl.xml";
	final static String DDL_NAME_K3 = "K3_BALANCE_SYNC";
	final static String DDL_NAME_WMS = "WMS_BALANCE_SYNC";

	final static String TB_BASE_RECEIVE_ORDER = "BASE_RECEIVE_ORDER";
	final static String TB_BASE_K3_ORDER = "BASE_K3_ORDER";
	
	String fIDs = "";
	String fInterIDs="";
	

	public BalanceSyncTask() {
		valid = DDLDBMS.load(DDL);
	}

	@Override
	public void exec() throws Exception {
		// TODO: 同步代码处理
		DdlItem k3_item = DDLDBMS.getItem(DDL_NAME_K3);
		DdlItem wms_item = DDLDBMS.getItem(DDL_NAME_WMS);

		Database db_k3 = new Database(k3_item);
		Database db_wms = new Database(wms_item);
		try {
			// 同步
			sync_step1(db_k3, db_wms);
		} finally {
			db_k3.closeAll();
			db_wms.closeAll();
		}
	}

	/**
	 * 先取K3数据
	 * @param db_k3
	 * @param db_wms
	 * @throws SQLException
	 */
	private void sync_step1(Database db_k3, Database db_wms)  {
		try{
			Connection conn = db_k3.connect();
	
			//先把此节点的数据打上标记，是我接下来要取的数据
			QueryHelper.execUpdate(conn, "UPDATE dbo.icstockbill SET syncFlag = 1 WHERE isnull(syncFlag,0) = 0");
			QueryHelper.execUpdate(conn, "UPDATE dbo.t_rp_contact SET syncFlag = 1 WHERE isnull(syncFlag,0) = 0");
			QueryHelper.execUpdate(conn, "UPDATE dbo.T_K3ZFCONTACT set syncFlag = 1 WHERE isnull(syncFlag,0) = 0");
			QueryHelper.execUpdate(conn, "UPDATE dbo.T_K3ZFORDER set syncFlag = 1 WHERE isnull(syncFlag,0) = 0");
			
			Table tb1 = db_k3.open("T_K3ZFCONTACT");
			Table tb2 = db_k3.open("T_K3ZFORDER");
			Table tb3 = db_k3.open("K3_ORDER");
			Table tb4 = db_k3.open("K3_RECEIVE_ORDER");
			
			QueryHelper.execUpdate(conn, "UPDATE dbo.icstockbill SET syncFlag = 2 WHERE isnull(syncFlag,0) = 1");
			QueryHelper.execUpdate(conn, "UPDATE dbo.t_rp_contact SET syncFlag =2 WHERE isnull(syncFlag,0) = 1");
			QueryHelper.execUpdate(conn, "DELETE from dbo.T_K3ZFCONTACT WHERE isnull(syncFlag,0) = 1"); 
			QueryHelper.execUpdate(conn, "DELETE from dbo.T_K3ZFORDER WHERE isnull(syncFlag,0) = 1");
	
			StringBuilder bb = new StringBuilder(512); 
			 
			if (tb1.size() != 0) {
				 
				for (Record rec : tb1.values()) {
					bb.append(rec.getPK().getKey()).append(',');
				}
				bb.setLength(bb.length() - 1);			 
				fIDs = bb.toString(); 
			}
			 
			
			if (tb2.size() != 0) {
				bb.setLength(0);
				for (Record rec : tb2.values()) {
					bb.append(rec.getPK().getKey()).append(',');
				}
				bb.setLength(bb.length() - 1);
				fInterIDs = bb.toString();			
			}
			/*
			 * 开始处理mysql
			 */
			
			if(!Strings.isNullOrEmpty(fInterIDs)){
				db_wms.execUpdate("DEL_BK3_ORDER", fInterIDs);
			}
			if(!Strings.isNullOrEmpty(fIDs)){
				db_wms.execUpdate("DEL_RECEIVE_ORDER", fIDs);
			}
			
			sync_nodate(db_wms,tb3,"BASE_K3_ORDER","UPDATE_BK3_ORDER", "INSERT_BK3_ORDER");
			sync_nodate(db_wms,tb4,"BASE_K3_RECEIVE","UPDATE_BK3_RECEIVER", "INSERT_BK3_RECEIVER");
		 }catch(SQLException e) {
			 logger.error("syncstep1sql",e);
		 }
		 catch(Exception e) {
			 logger.error("syncstep1",e);
		 }
	}

	private void sync_nodate(Database db_wms,Table table,String wms_table,String update_tb_id,String ins_tb_id)  {
		if (table.size() == 0) {
			return;
		}
		
		StringBuilder bb = new StringBuilder(512);
		bb.append("SELECT fid FROM ").append(wms_table).append(" WHERE fid in (");
		table.values().forEach(e -> {
			bb.append(e.getPK().getKey()).append(',');
		});
		bb.setLength(bb.length() - 1);
		bb.append(")");

		Set<Integer> updateSet = new HashSet<>();
		ResultSet rs = null;
		Statement stmt = null;
		try {
			Connection conn = db_wms.connect();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(bb.toString());
			while (rs.next()) {
				updateSet.add(rs.getInt(1));
			}
			for (Record rec : table.values()) {
				if (updateSet.contains(rec.getInteger(1))) {
					Proc proc = db_wms.getDDL().getProc(update_tb_id);
					QueryHelper.execUpdate(conn, proc.getStatement(), rec.get(1), rec.get(2), rec.get(3),
							rec.get(4), rec.get(5), rec.get(6), rec.get(7), rec.get(8), rec.get(9),
							rec.get(10), rec.get(11), rec.get(12), rec.get(1));
				} else {
					Proc proc = db_wms.getDDL().getProc(ins_tb_id);
					QueryHelper.execUpdate(conn, proc.getStatement(), rec.get(1), rec.get(2), rec.get(3),
							rec.get(4), rec.get(5), rec.get(6), rec.get(7), rec.get(8), rec.get(9),
							rec.get(10), rec.get(11), rec.get(12));
				}
			} 
		} 
		catch (SQLException e) {
		logger.error("syncstep2sql",e);	
		}
		catch(Exception e) {
			logger.error("syncstep2",e);
			
		}
		finally {
			QueryHelper.close(rs);
			QueryHelper.close(stmt);
		}
	}
	
	
	@Override
	protected boolean ready() {
		return valid;
	}
}
