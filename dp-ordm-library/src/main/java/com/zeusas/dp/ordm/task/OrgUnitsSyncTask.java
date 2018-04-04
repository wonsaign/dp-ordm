package com.zeusas.dp.ordm.task;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.zeusas.common.data.DDLDBMS;
import com.zeusas.common.data.Database;
import com.zeusas.common.data.DdlItem;
import com.zeusas.common.data.Record;
import com.zeusas.common.data.Table;
import com.zeusas.common.task.CronTask;
import com.zeusas.core.utils.AppContext;
import com.zeusas.security.auth.entity.OrgUnit;
import com.zeusas.security.auth.service.AuthCenterManager;

public class OrgUnitsSyncTask extends CronTask {
	static final Logger logger = LoggerFactory.getLogger(OrgUnitsSyncTask.class);

	private final Database dwtDB;
	private final Database ordmDB;
	
	private final Map<Object, Integer> orgLevelMap = new HashMap<>();

	final static int F_PATH = 5;

	public OrgUnitsSyncTask() {
		valid = DDLDBMS.load("task/sync_orgunits_ddl.xml");
		if (valid) {
			DdlItem dwtDitem = DDLDBMS.getItem("DWT_ORGUNIT");
			DdlItem developDitem = DDLDBMS.getItem("ORDM_ORGUNIT");

			dwtDB = new Database(dwtDitem);
			ordmDB = new Database(developDitem);
		} else {
			ordmDB = null;
			dwtDB = null;
		}
		orgLevelMap.put("0", 10301);
		orgLevelMap.put("1", 10302);
		orgLevelMap.put("7", 10303);
		orgLevelMap.put("5", 10304);
		orgLevelMap.put("2", 10305);
		orgLevelMap.put("3", 10306);
		orgLevelMap.put("6", 10307);
		orgLevelMap.put("4", 10308);
		orgLevelMap.put("Z", 10309);
	}

	@Override
	public void exec() throws Exception {
		try {
			sync();
		} finally {
			close();
		}
	}

	@Override
	protected boolean ready() {
		return valid;
	}


	public void close() throws Exception {
		dwtDB.closeAll();
		ordmDB.closeAll();
	}

	void sync() throws SQLException {
		Table orgTB = dwtDB.open("ORGANIZATION");
		Connection ordmDBconn = ordmDB.connect();
		
		AuthCenterManager acm = AppContext.getBean(AuthCenterManager.class);

		for (Record rec : orgTB.values()) {
			Integer orgId = (Integer) rec.get(1);
			OrgUnit o = acm.getOrgUnitById(orgId);

			Record parentRec = orgTB.get(getParentPath((String) rec.get(F_PATH)));

			if (o == null) {
				// 根节点
				if (rec.get(1).equals(1)) {
					insert(rec, 0, ordmDB, ordmDBconn);
				} else if (parentRec != null) {
					// 有父节点节点
					insert(rec, (Integer) parentRec.get(1), ordmDB, ordmDBconn);
				}
			} else {
				// update
				// 根节点
				if (rec.get(1).equals(1) && !equal(rec, o, 0,orgTB)) {
					update(rec, 0, ordmDB, ordmDBconn);
				} else if (parentRec != null && !equal(rec, o, (Integer) parentRec.get(1),orgTB)) {
					// 有父节点节点
					update(rec, (Integer) parentRec.get(1), ordmDB, ordmDBconn);
				} else {
					// 没有父节点节点
					// NOP
				}
			}
		}
		
		acm.loadOrgUnit();
	}

	void insert(Record org, Integer pid, Database DB, Connection conn) throws SQLException {
		try {
			// 根节点
			DB.execUpdate("INSERT_CORE_ORGUNIT",  //
					org.get(1), //
					pid, org.get(2), //
					org.get(3), //
					orgLevelMap.get((String) org.get(6)), //
					isValid(org), //
					System.currentTimeMillis());
		} catch (Exception e) {
			logger.error("插入组织错误{}", org.toString(), e);
		}
		conn.clearWarnings();
	}

	void update(Record org, Integer pid, Database DB, Connection conn) throws SQLException {
		try {
			DB.execUpdate("UPDATE_CORE_ORGUNIT", pid, //
					org.get(2), //
					org.get(3), //
					orgLevelMap.get(org.getString(6)), //
					isValid(org), //
					System.currentTimeMillis(), //
					org.get(1));
		} catch (Exception e) {
			logger.error("插入组织错误{}",org.toString(), e);
		}
		conn.clearWarnings();
	}

	/**
	 * rec.get(7): ValidFlag, rec.get(8)TestType
	 * 
	 * @param rec
	 * @return
	 */
	static int isValid(Record rec) {
		Integer status = rec.getInteger(8);

		return ((rec.getString(3)).indexOf("撤") < 0)//
				&& "1".equals(rec.get(7)) //
				&& (status != null && status.intValue() == 0) ? 1 : 0;
	}

	/**
	 * 通过子路径算出父路径 不可修改
	 * 
	 * @param path
	 * @return
	 */
	static String getParentPath(String path) {
		int len = path.length() - 1;
		while ((path.charAt(--len)) != '/')
			;// NOP
		return path.substring(0, len + 1);
	}

	/**
	 * ORGID,PID,ORGCODE,COMMONNAME,LEVEL,STATUS Record 中没有pid 需要通过路径算出父路径 获取父id
	 * 
	 * @param rec
	 * @param org
	 * @return
	 */
	boolean equal(Record rec, OrgUnit org, Integer PID,Table orgTB) {
		Integer orgId = (Integer) orgTB.getFieldData(rec, "orgId");
		if (!Objects.equal(orgId, org.getOrgId())) {
			return false;
		}

		if (!Objects.equal(PID, org.getPid())) {
			return false;
		}

		String orgCode = (String) orgTB.getFieldData(rec, "orgCode");
		if (!Objects.equal(orgCode, org.getOrgCode())) {
			return false;
		}

		String commonName = (String) orgTB.getFieldData(rec, "commonName");
		if (!Objects.equal(commonName, org.getCommonName())) {
			return false;
		}

		Integer Level = orgLevelMap.get((String) rec.get(6));
		if (!Objects.equal(Level, org.getLevel())) {
			return false;
		}

		Integer Status = isValid(rec);
		if (!Objects.equal(Status, org.getStatus())) {
			return false;
		}
		return true;
	}
}
