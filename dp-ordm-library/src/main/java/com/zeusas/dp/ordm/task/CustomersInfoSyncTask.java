package com.zeusas.dp.ordm.task;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.zeusas.common.data.DDLDBMS;
import com.zeusas.common.data.Database;
import com.zeusas.common.data.DdlItem;
import com.zeusas.common.data.Record;
import com.zeusas.common.data.Table;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.task.CronTask;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.UserCustomerManager;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.service.AuthCenterManager;

public class CustomersInfoSyncTask extends CronTask {
	static final Logger logger = LoggerFactory.getLogger(CustomersInfoSyncTask.class);
	final static String customerIds = "1";

	private final Database k3DB;
	private final Database ordmDB;

	public CustomersInfoSyncTask() {
		valid = DDLDBMS.load("task/sync_customers_ddl.xml");

		DdlItem k3Ditem = DDLDBMS.getItem("K3_CUSTOMERS");
		DdlItem developDitem = DDLDBMS.getItem("ORDM_CUSTOMERS");

		k3DB = new Database(k3Ditem);
		ordmDB = new Database(developDitem);
	}

	@Override
	public void exec() throws Exception {
		try {
			sync();
			fixCounters();
			fixUserCustomer();
			disableUser();
		} finally {
			close();
		}
	}

	@Override
	protected boolean ready() {
		return valid;
	}

	protected void close() {
		k3DB.closeAll();
		ordmDB.closeAll();
	}
	
	/**
	 * 同步客户表
	 * @throws SQLException
	 */
	protected void sync() throws SQLException {
		Connection ordmConn = ordmDB.connect();
		Table customerTB = k3DB.open("K3_CUSTOMER");
		Table orgCustomerTB = k3DB.open("K3_ORGCUSTOMER");

		CustomerManager cm = AppContext.getBean(CustomerManager.class);

		// 核算客户
		for (Record rec : customerTB.values()) {
			Integer customersId = (Integer) rec.getPK().getKey();
			Customer c = cm.get(customersId);
			if (c == null) {
				// call Insert
				insertCustomer(rec, ordmDB, ordmConn);
			} else if (!equal(rec, c, customerTB)) {
				// call Update
				updateCustomer(rec, ordmDB, ordmConn);
			}
		}

		// 机构客户
		for (Record rec : orgCustomerTB.values()) {
			Integer customersId = (Integer) rec.getPK().getKey();
			Customer c = cm.get(customersId);
			if (c == null) {
				// call Insert
				insertOrgCustomer(rec, ordmDB, ordmConn);
			} else if (!equalOrgCustomer(rec, c, orgCustomerTB)) {
				// call Update
				updateOrgCustomer(rec, ordmDB, ordmConn);
			}
		}

		// 禁用客户
		List<Customer> customers = cm.findAll();

		for (Customer customer : customers) {
			if (customerTB.get(customer.getCustomerID()) == null //
					&& customer.getStatus() //
					&& customerIds.indexOf((customer.getCustomerID().toString())) < 0) {
				disableCustomer(customer.getCustomerID(), ordmDB, ordmConn);
			}
		}
		cm.load();
	}

	/**
	 * 修复客户表中的柜台id集合
	 * 
	 * @throws SQLException
	 */
	protected void fixCounters() throws SQLException {
		CustomerManager customerManager = AppContext.getBean(CustomerManager.class);
		CounterManager counterManager = AppContext.getBean(CounterManager.class);
 
		List<Customer> customers = customerManager.findAll();
		for (Customer customer : customers) {
			List<Counter> counters = counterManager.getCounterByCustomerId(customer.getCustomerID());
			Set<String> counterIds = new HashSet<>();
			// 普通客户获取柜台
			if (counters != null) {
				counters = counters.stream().filter(e -> e.getStatus()).collect(Collectors.toList());
				for (Counter counter : counters) {
					counterIds.add(counter.getCounterId().toString());
				}
			}
			customer.setCounters(counterIds);
			try {
				customerManager.updateCounterSet(customer);
			} catch (Exception e) {
				logger.error("修复客户柜台集合错误,客户id{}", customer.getCustomerID(), e);
			}
		}
	}

	/**
	 * 修复用户客户关联表中 柜台(id)集合
	 * 
	 * @throws SQLException
	 */
	protected void fixUserCustomer() throws SQLException {
		CustomerManager customerManager = AppContext.getBean(CustomerManager.class);
		UserCustomerManager userCustomerManager = AppContext.getBean(UserCustomerManager.class);

		Collection<UserCustomer> userCustomers = userCustomerManager.findall();
		for (UserCustomer userCustomer : userCustomers) {
			Customer customer = customerManager.get(userCustomer.getCustomerId());
			if (customer == null) {
				continue;
			}
			Set<String> bossCounters = customer.getCounters();
			// 客户
			if (userCustomer.getUserId().equals(userCustomer.getCustomerUserId())) {
				userCustomer.setCounters(bossCounters);
				// 子帐号
			} else if (userCustomer.getStatus().equals(1)) {
				Set<String> myCounters = new HashSet<>();
				for (String counterId : userCustomer.getCounters()) {
					if (bossCounters.contains(counterId)) {
						myCounters.add(counterId);
					}
				}
				userCustomer.setCounters(myCounters);
			}
			try {
				userCustomerManager.update(userCustomer);
			} catch (Exception e) {
				logger.error("修复用户客户关联错误,客户id{}", customer.getCustomerID(), e);
			}
		}

	}

	/**
	 * 禁用帐号
	 * 
	 * @throws SQLException
	 */
	protected void disableUser() throws SQLException {
		CustomerManager customerManager = AppContext.getBean(CustomerManager.class);
		UserCustomerManager userCustomerManager = AppContext.getBean(UserCustomerManager.class);
		AuthCenterManager authCenterManager = AppContext.getBean(AuthCenterManager.class);

		Collection<UserCustomer> userCustomers = userCustomerManager.findall();
		for (UserCustomer userCustomer : userCustomers) {
			AuthUser authUser = authCenterManager.getAuthUser(userCustomer.getLoginName());
			if (authUser == null) {
				continue;
			}
			// 客户
			if (userCustomer.getUserId().equals(userCustomer.getCustomerUserId())) {
				Customer customer = customerManager.get(userCustomer.getCustomerId());
				if (customer == null) {
					continue;
				}
				if (customer.getLevel() == null) {
					continue;
				}
				if(!customer.getStatus()){
					authUser.setStatus(0);
				}else if(customer.getLevel() > 3){
					authUser.setStatus(userCustomer.getStatus());
				}
				// 子帐号
			} else if (userCustomer.getStatus().equals(0) && authUser.getStatus().equals(1)) {
				authUser.setStatus(0);
			}
			try {
				authCenterManager.updateAuthUser(authUser);
			} catch (Exception e) {
				logger.error("修复用户客户关联 禁用用错误,客户id: {}", userCustomer.getCustomerId(), e);
			}
		}
	}

	/** 手机号处理 */
	public static String getMobile(String mobile) {
		mobile = Strings.nullToEmpty(mobile);
		String v[] = StringUtil.split(mobile, "\\;|\\,|-|\\/|\\s+|\\(|\\)");
		if (v == null || v.length == 0) {
			return "";
		}
		mobile = v[0];
		for (String s : v) {
			if (s.length() == 11 && s.charAt(0) == '1') {
				return s;
			}
			if ((s.length() > mobile.length()) && s.length() < 12) {
				mobile = s;
			}
		}
		if (mobile.length()>11) {
			mobile = mobile.substring(0,11);
		}
		return mobile;
	}
	
	/**
	 * customer.get(4), // 1330120123
	 * customer.get(5), //
	 * @param customer
	 * @param DB
	 * @param conn
	 * @throws SQLException
	 */
	private void insertCustomer(Record customer, Database DB, Connection conn) throws SQLException {
		try {
			String mobile = getMobile(customer.getString(4));
			customer.set(4, mobile);
			DB.execUpdate("INSERT_CUSTOMER", customer.get(1), //
					customer.get(2), //
					customer.get(3), //
					customer.get(4), //
					customer.get(5), //
					customer.get(6), //
					customer.get(7), //
					customer.get(8), //
					customer.get(9), //
					customer.get(10), //
					customer.get(11), //
					customer.get(12), //
					customer.get(13), //
					getStatus(customer.get(14)), //
					System.currentTimeMillis(), //
					customer.get(15), //
					customer.get(16));
		} catch (Exception e) {
			logger.error("插入客户错误{}", customer.toString(), e);
		}
		conn.clearWarnings();
	}

	private void updateCustomer(Record customer, Database DB, Connection conn) throws SQLException {
		try {
			// customer.get(3)
			String mobile = getMobile(customer.getString(3));
			customer.set(3, mobile);
			DB.execUpdate("UPDATE_CUSTOMER", customer.get(2), //
					customer.get(3), //
					customer.get(4), //
					customer.get(5), //
					customer.get(6), //
					customer.get(7), //
					customer.get(8), //
					customer.get(9), //
					customer.get(10), //
					customer.get(11), //
					customer.get(12), //
					customer.get(13), //
					getStatus(customer.get(14)), //
					System.currentTimeMillis(), //
					customer.get(15), //
					customer.get(16), //
					customer.get(1));
		} catch (Exception e) {
			logger.error("更新客户错误{}", customer.toString(), e);
		}
		conn.clearWarnings();
	}

	private void insertOrgCustomer(Record customer, Database DB, Connection conn) throws SQLException {
		try {
			DB.execUpdate("INSERT_ORGCUSTOMER", //
					customer.get(1), //
					customer.get(2), //
					customer.get(3), //
					getCustomerTypeId(customer.getString(5)), //
					getCustomerType(customer.getString(5)), //
					getStatus(customer.get(4)), //
					System.currentTimeMillis(), //
					customer.get(5), //
					customer.get(6));
		} catch (Exception e) {
			logger.error("插入机构客户错误{}", customer.toString(), e);
		}
		conn.clearWarnings();
	}

	private void updateOrgCustomer(Record customer, Database DB, Connection conn) throws SQLException {
		try {
			DB.execUpdate("UPDATE_ORGCUSTOMER", customer.get(2), //
					customer.get(3), //
					getCustomerTypeId(customer.getString(5)), //
					getCustomerType(customer.getString(5)), //
					getStatus(customer.get(4)), //
					System.currentTimeMillis(), //
					customer.get(5), //
					customer.get(6), //
					customer.get(1));
		} catch (Exception e) {
			logger.error("更新机构客户错误{}", customer.toString(), e);
		}
		conn.clearWarnings();
	}

	private void disableCustomer(Integer customerId, Database DB, Connection conn) throws SQLException {
		try {
			DB.execUpdate("DISABLE_CUSTOMER", 0, System.currentTimeMillis(), customerId);
		} catch (Exception e) {
			logger.error("禁用客户错误, 客户:{}", customerId, e);
		}
		conn.clearWarnings();
	}

	private Integer getStatus(Object o) {
		return Objects.equal(TypeConverter.toInteger(o), 0) ? 1 : 0;
	}

	/**
	 * customerid,parentid,customername,Mobile,Phone,Contact,
	 * CustomerTypeID,CustomerType,Province,City,AreaCounty,
	 * Address,PostCode,Status,lastUpdate
	 * 
	 * @param rec
	 * @param customer
	 * @return
	 */
	boolean equal(Record rec, Customer customer, Table customerTB) {
		Integer parentid = (Integer) customerTB.getFieldData(rec, "parentid");
		if (!Objects.equal(parentid, customer.getParentId())) {
			return false;
		}

		String customername = (String) customerTB.getFieldData(rec, "customername");
		if (!Objects.equal(customername, customer.getCustomerName())) {
			return false;
		}

		String Mobile = (String) customerTB.getFieldData(rec, "Mobile");
		if (!Objects.equal(Mobile, customer.getMobile())) {
			return false;
		}

		String Phone = (String) customerTB.getFieldData(rec, "Phone");
		if (!Objects.equal(Phone, customer.getPhone())) {
			return false;
		}

		String Contact = (String) customerTB.getFieldData(rec, "Contact");
		if (!Objects.equal(Contact, customer.getContact())) {
			return false;
		}

		Integer CustomerTypeID = (Integer) customerTB.getFieldData(rec, "CustomerTypeID");
		if (!Objects.equal(CustomerTypeID, customer.getCustomerTypeID())) {
			return false;
		}

		String CustomerType = (String) customerTB.getFieldData(rec, "CustomerType");
		if (!Objects.equal(CustomerType, customer.getCustomerType())) {
			return false;
		}

		String Province = (String) customerTB.getFieldData(rec, "Province");
		if (!Objects.equal(Province, customer.getProvince())) {
			return false;
		}

		String City = (String) customerTB.getFieldData(rec, "City");
		if (!Objects.equal(City, customer.getCity())) {
			return false;
		}

		String AreaCounty = (String) customerTB.getFieldData(rec, "AreaCounty");
		if (!Objects.equal(AreaCounty, customer.getAreaCounty())) {
			return false;
		}

		String Address = (String) customerTB.getFieldData(rec, "Address");
		if (!Objects.equal(Address, customer.getAddress())) {
			return false;
		}

		String PostCode = (String) customerTB.getFieldData(rec, "PostCode");
		if (!Objects.equal(PostCode, customer.getPostCode())) {
			return false;
		}
		// 金蝶0 =ordm 1
		Integer status = getStatus(customerTB.getFieldData(rec, "Status"));
		if (!Objects.equal(StringUtil.toBoolean("" + status), customer.getStatus())) {
			return false;
		}

		String CustomerCode = (String) customerTB.getFieldData(rec, "CustomerCode");
		if (!Objects.equal(CustomerCode, customer.getCustomerCode())) {
			return false;
		}

		Integer level = TypeConverter.toInteger(customerTB.getFieldData(rec, "level"));
		if (!Objects.equal(level, customer.getLevel())) {
			return false;
		}
		return true;
	}

	// 机构客户
	boolean equalOrgCustomer(Record rec, Customer customer, Table customerTB) {
		Assert.notNull(customerTB);
		Integer parentid = (Integer) customerTB.getFieldData(rec, "parentid");
		if (!Objects.equal(parentid, customer.getParentId())) {
			return false;
		}

		String customername = (String) customerTB.getFieldData(rec, "customername");
		if (!Objects.equal(customername, customer.getCustomerName())) {
			return false;
		}

		Integer CustomerTypeID = getCustomerTypeId(rec.getString(5));
		if (!Objects.equal(CustomerTypeID, customer.getCustomerTypeID())) {
			return false;
		}

		String CustomerType = getCustomerType(rec.getString(5));
		if (!Objects.equal(CustomerType, customer.getCustomerType())) {
			return false;
		}
		// 金蝶0 =ordm 1
		Integer status = getStatus(customerTB.getFieldData(rec, "Status"));
		if (!Objects.equal(StringUtil.toBoolean("" + status), customer.getStatus())) {
			return false;
		}

		String CustomerCode = (String) customerTB.getFieldData(rec, "CustomerCode");
		if (!Objects.equal(CustomerCode, customer.getCustomerCode())) {
			return false;
		}
		Integer level = TypeConverter.toInteger(customerTB.getFieldData(rec, "level"));
		if (!Objects.equal(level, customer.getLevel())) {
			return false;
		}
		return true;
	}

	// FIXME
	private String getCustomerType(String code) {
		if (code.startsWith("B")&&!code.startsWith("B.Y.")) {
			return "直营";
		} else {
			return "运营商";
		}
	}

	// FIXME
	private Integer getCustomerTypeId(String code) {
		if (code.startsWith("B")&&!code.startsWith("B.Y.")) {
			return 11387;
		}
		return 11392;
	}
}