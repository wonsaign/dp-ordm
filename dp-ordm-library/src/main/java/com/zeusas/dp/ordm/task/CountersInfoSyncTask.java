package com.zeusas.dp.ordm.task;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.zeusas.common.data.DDLDBMS;
import com.zeusas.common.data.Database;
import com.zeusas.common.data.DdlItem;
import com.zeusas.common.data.Meta;
import com.zeusas.common.data.Record;
import com.zeusas.common.data.Table;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.task.CronTask;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.utils.StockArea;

/**
 * 柜台信息同步
 *
 */
public class CountersInfoSyncTask extends CronTask {
	static final Logger logger = LoggerFactory.getLogger(CountersInfoSyncTask.class);

	final static String TB_K3_COUNTER =  "K3_COUNTER";
	// 排除测试禁用柜台号
	final static String testCounterIds = "21940,21941,21942,21943,22040,999000002,999000003,999000004,999000005";

	private Database k3DB;
	private Database ordmDB;

	public CountersInfoSyncTask() {
		valid = DDLDBMS.load("task/sync_counters_ddl.xml");

		DdlItem k3Ditem = DDLDBMS.getItem("K3_COUNTERS");
		DdlItem developDitem = DDLDBMS.getItem("ORDM_COUNTERS");

		k3DB = new Database(k3Ditem);
		ordmDB = new Database(developDitem);
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

	private void close() throws Exception {
		k3DB.closeAll();
		ordmDB.closeAll();
	}

	private void sync() throws SQLException {
		Connection ordmConn = ordmDB.connect();

		Table coutererTB = k3DB.open(TB_K3_COUNTER);
		Meta meta = k3DB.getDDL().getMeta(TB_K3_COUNTER);

		CounterManager cm = AppContext.getBean(CounterManager.class);
		
		for (Record rec : coutererTB.values()) {
			Integer CounterId = (Integer) rec.getPK().getKey();

			Counter c = cm.get(CounterId);
			Counter recordCounter = rec.toBean(meta, Counter.class);
			
			if (c != null) {
				// 不比较的字段
				recordCounter.setOwner(c.getOwner());
				recordCounter.setShippingAddress(c.getShippingAddress());
				recordCounter.setLastUpdate(c.getLastUpdate());
				recordCounter.setWarehouses(c.getWarehouses());
			}

			if (c == null) {
				// insert
				insertCounter(rec, ordmDB, ordmConn);
			} else if (!equal(coutererTB, rec, c)) {
				// update
				updateCounter(rec, ordmDB, ordmConn);
			} else {
				// NOP
			}
		}
		
		Collection<Counter> counters = cm.findAll();

		for (Counter counter : counters) {
			if (coutererTB.get(counter.getCounterId()) == null 
					&& counter.getStatus()
					&& testCounterIds.indexOf((counter.getCounterId().toString())) < 0) {
				// TODO: 金蝶数据没了 ordm库删掉 禁用？
				disableCounter(counter.getCounterId(), ordmDB, ordmConn);
			}
		}
		// reload Counter information to cache
		cm.load();
	}

	private void insertCounter(Record counter, Database DB, Connection conn) throws SQLException {
		//stockId允许为空 "[\"\"]
		String stockId=StockArea.getStockId(counter.getString(9), counter.getString(10));
		String mobile = CustomersInfoSyncTask.getMobile(counter.getString(6));
		String phone = CustomersInfoSyncTask.getMobile(counter.getString(7));
		try {
			DB.execUpdate("INSERT_COUNTER", //
					counter.get(1), //
					counter.get(2), //
					counter.get(3), //
					counter.get(4), //
					"1", //
					counter.get(5), //
					mobile, //
					phone, //
					counter.get(8), //
					counter.get(9), //
					counter.get(10), //
					counter.get(11), //
					TypeConverter.toInteger(counter.get(12)), //
					counter.get(13), //
					counter.get(14), //
					counter.get(15), //
					counter.get(16), //
					counter.get(17), //
					getStatus(counter.get(18)), //
					System.currentTimeMillis(), //
					stockId,//
					counter.get(20),//
					counter.get(21)==null?0:TypeConverter.toBoolean(counter.get(21)),//
					counter.get(22));
		} catch (Exception e) {
			logger.error("插入柜台错误:{}{}{}", counter.get(1), counter.get(2), counter.get(3), e);
		}
		conn.clearWarnings();
	}

	private void updateCounter(Record counter, Database DB, Connection conn) throws SQLException {
		String mobile = CustomersInfoSyncTask.getMobile(counter.getString(6));
		String phone = CustomersInfoSyncTask.getMobile(counter.getString(7));
		try {
			DB.execUpdate("UPDATE_COUNTER", //
					counter.get(2), //
					counter.get(3), //
					counter.get(4), //
					counter.get(5), //
					mobile, //
					phone, //
					counter.get(8), //
					counter.get(9), //
					counter.get(10), //
					counter.get(11), //
					TypeConverter.toInteger(counter.get(12)), //
					counter.get(13), //
					counter.get(15), //
					counter.get(16), //
					counter.get(17), //
					getStatus(counter.get(18)), //
					System.currentTimeMillis(), //
					counter.get(20), //
					counter.get(21)==null?0:TypeConverter.toBoolean(counter.get(21)),//
					counter.get(22),//
					counter.get(1));
		} catch (Exception e) {
			logger.error("更新柜台错误:{}", counter.toString(), e);
		}
		conn.clearWarnings();
	}

	private void disableCounter(Integer counterId, Database DB, Connection conn) throws SQLException {
		try {
			DB.execUpdate("DISABLE_COUNTER", 0, System.currentTimeMillis(), counterId);
		} catch (Exception e) {
			logger.error("禁用门店错误{}", counterId, e);
		}
		conn.clearWarnings();
	}

	private Integer getStatus(Object o) {
		return Objects.equal(TypeConverter.toInteger(o), 0) ? 1 : 0;
	}

	/**
	 * 
	 * CounterID CounterCode CounterName customerId Contact Mobile Phone Country
	 * Province City AreaCounty AreaPrice Address PostCode Channel Type Status
	 * CounterType
	 * 
	 * @param rec
	 * @param counter
	 * @return
	 */
	boolean equal(Table coutererTB, Record rec, Counter counter) {
		String CounterCode = (String) coutererTB.getFieldData(rec, "CounterCode");
		if (!Objects.equal(CounterCode, counter.getCounterCode())) {
			return false;
		}

		String CounterName = (String) coutererTB.getFieldData(rec, "CounterName");
		if (!Objects.equal(CounterName, counter.getCounterName())) {
			return false;
		}

		Integer customerId = TypeConverter.toInteger(coutererTB.getFieldData(rec, "customerId"));
		if (!Objects.equal(customerId, counter.getCustomerId())) {
			return false;
		}

		String Contact = (String) coutererTB.getFieldData(rec, "Contact");
		if (!Objects.equal(Contact, counter.getContact())) {
			return false;
		}

		String Mobile = (String) coutererTB.getFieldData(rec, "Mobile");
		if (!Objects.equal(Mobile, counter.getMobile())) {
			return false;
		}

		String Phone = (String) coutererTB.getFieldData(rec, "Phone");
		if (!Objects.equal(Phone, counter.getPhone())) {
			return false;
		}

		String Country = (String) coutererTB.getFieldData(rec, "Country");
		if (!Objects.equal(Country, counter.getCountry())) {
			return false;
		}

		String Province = (String) coutererTB.getFieldData(rec, "Province");
		if (!Objects.equal(Province, counter.getProvince())) {
			return false;
		}

		String City = (String) coutererTB.getFieldData(rec, "City");
		if (!Objects.equal(City, counter.getCity())) {
			return false;
		}

		String AreaCounty = (String) coutererTB.getFieldData(rec, "AreaCounty");
		if (!Objects.equal(AreaCounty, counter.getAreaCounty())) {
			return false;
		}

		Integer AreaPrice = TypeConverter.toInteger(coutererTB.getFieldData(rec, "AreaPrice"));
		if (!Objects.equal(AreaPrice, counter.getAreaPrice())) {
			return false;
		}

		String Address = (String) coutererTB.getFieldData(rec, "Address");
		if (!Objects.equal(Address, counter.getAddress())) {
			return false;
		}

		String PostCode = (String) coutererTB.getFieldData(rec, "PostCode");
		if (!Objects.equal(PostCode, counter.getPostCode())) {
			return false;
		}

		String Channel = (String) coutererTB.getFieldData(rec, "Channel");
		if (!Objects.equal(Channel, counter.getChannel())) {
			return false;
		}

		String Type = (String) coutererTB.getFieldData(rec, "Type");
		if (!Objects.equal(Type, counter.getType())) {
			return false;
		}

		String CounterType = (String) coutererTB.getFieldData(rec, "CounterType");
		if (!Objects.equal(CounterType, counter.getCounterType())) {
			return false;
		}
		
		Boolean newCounter = TypeConverter.toBoolean(coutererTB.getFieldData(rec, "CounterType"));
		if (!Objects.equal(newCounter, counter.getNewCounter())) {
			return false;
		}
		
		Double area = TypeConverter.toDouble((coutererTB.getFieldData(rec, "area")));
		if (!Objects.equal(area, counter.getArea())) {
			return false;
		}

		// k3 0 ordm 1
		Integer status = getStatus(coutererTB.getFieldData(rec, "Status"));
		if (!Objects.equal(TypeConverter.toBoolean(status), counter.getStatus())) {
			return false;
		}
		return true;
	}
}
