package com.zeusas.dp.ordm.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.common.data.DDLDBMS;
import com.zeusas.common.data.Database;
import com.zeusas.common.data.DdlItem;
import com.zeusas.common.data.Record;
import com.zeusas.common.data.Table;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.entity.UserDetail;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.entity.OrgUnit;
import com.zeusas.security.auth.service.AuthCenterManager;
import com.zeusas.security.auth.service.AuthUserService;
import com.zeusas.security.auth.utils.DigestEncoder;

public class CreateAutherForCustomer {

	static Logger logger;
	FileSystemXmlApplicationContext ctx;
	private CustomerManager customerManager;
	private UserCustomerManager userCustomerManager;
	private CounterManager counterManager;
	private AuthCenterManager authCenterManager;
	private AuthUserService authUserService;
	private UserCustomerService userCustomerService;
	// 创建用户起始id U10000
	private int userId = 11216;
	
	private String province="江苏省";

	private Hanyu hanyu;

	private Map<String, String> shortNameMap = new HashMap<>();

	private final Set<String> direct_sale_roles = new HashSet<>();
	private final Set<String> franchise_roles = new HashSet<>();

	Set<String> loginNames;

	@Before
	public void setUp() throws Exception {
		logger = LoggerFactory.getLogger(CreateAutherForCustomer.class);
		ctx = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
		customerManager = ctx.getBean(CustomerManager.class);
		counterManager = ctx.getBean(CounterManager.class);
		authCenterManager = ctx.getBean(AuthCenterManager.class);
		authUserService = ctx.getBean(AuthUserService.class);
		userCustomerManager = ctx.getBean(UserCustomerManager.class);
		userCustomerService = ctx.getBean(UserCustomerService.class);
		ctx.start();
		
		hanyu = new Hanyu();
		direct_sale_roles.add("10");

		franchise_roles.add("10");
		franchise_roles.add("11");
		franchise_roles.add("12");
		franchise_roles.add("13");
		franchise_roles.add("14");

		loginNames = allLoginName();
	}

	@Test
	public void creatAuthUserFroCustomer() throws SQLException, IOException {

		setRegion(shortNameMap);
		// 1. 获取User_Customer关联里没有的客户
		List<Customer> customers = getCustomser();
		
		// 2. 获取现有用户LoginName 判重
		Set<String> LoginNames = allLoginName();

		
		FileWriter fw = new FileWriter(new File("passwd.txt"));
		StringBuilder line = new StringBuilder();
		for (Customer customer : customers) {
			if("运营商".equals(customer.getCustomerType())){
				System.err.println("运营商需要手动创建");
				continue;
			}
			Integer orgId = getOrgId(customer);
			AuthUser authUser = new AuthUser();
			if (orgId == null) {
				// 根据门店找组织结构 没有则不建帐号
				System.err.println("客户的柜台在店务通组织机构中没有,客户："+customer.getCustomerID()+" "+customer.getCustomerName());
				continue;
			}
			
			// authuser
			userId++;
			// 密码明文
			String password = getFixLenthString(6);

			authUser = initAuthUser(customer, password, orgId);
			// userdeatil
			UserDetail detail = initUserDetail(authUser, customer);
			// UserCustomer
			UserCustomer userCustomer = initUserCustomer(customer, authUser);

			try {
				userCustomerService.creatAuthuserForCustomer(authUser, detail, userCustomer);
				loginNames.add(authUser.getLoginName());
				line.setLength(0);
				
				line.append(customer.getCustomerID()).append(',')//
				.append(customer.getCustomerName())//
				.append(authUser.getLoginName()).append(',')//
				.append(password).append("\r\n");
				System.out.println(customer.getCustomerID() + "," + customer.getCustomerName() + ","
						+ authUser.getLoginName() + "," + password);
				fw.write(line.toString());
			} catch (Exception e) {
				logger.error("为客户创建用户错误{}", customer, e);
			}
		}
		fw.close();
	}

	private List<Customer> getCustomser() {
		Set<Integer> customerid = new HashSet<>();
		userCustomerManager.findall().forEach(e -> customerid.add(e.getCustomerId()));
		return customerManager.findAll().stream().filter(e -> e.getStatus()//
				&&province.equals(e.getProvince())//
				&& !customerid.contains(e.getCustomerID()))
				.collect(Collectors.toList());
	}

	private Set<String> allLoginName() {
		Set<String> loginNames = new HashSet<>();
		authUserService.findAll().stream().forEach(e -> loginNames.add(e.getLoginName()));
		return loginNames;
	}

	/**
	 * 获取客户的组织机构id
	 * 
	 * @param customer
	 * @return
	 */
	private Integer getOrgId(Customer customer) {
		Integer orgid = null;
		Set<String> counterIds = new HashSet<>();
		counterIds = customer.getCounters();
		if (!counterIds.isEmpty()) {
			for (String id : counterIds) {
				Counter counter = counterManager.get(Integer.parseInt(id));
				if (counter == null) {
					continue;
				}
				OrgUnit org = authCenterManager.getOrgUnitByCode(counter.getCounterCode());
				if (org != null) {
					orgid = org.getOrgId();
					break;
				}

			}
		}
		return orgid;
	}

	/**
	 * 装载地区国标
	 * 
	 * @param regionMap
	 * @throws SQLException
	 */
	private void setRegion(Map<String, String> regionMap) throws SQLException {
		DDLDBMS.load("task/fix_ddl.xml");
		DdlItem localDitem = DDLDBMS.getItem("ORDM");
		Database localDB = new Database(localDitem);

		Table regionTb = localDB.open("REGION_PROVINCE");
		regionTb.close();
		Table regionTb2 = localDB.open("REGION_CITY");
		regionTb2.close();
		List<Record> Regions = (List<Record>) regionTb.values();
		Regions.addAll((List<Record>) regionTb2.values());
		for (Record region : Regions) {
			String regionName = (String) region.get(3);
			if (regionName.contains("市")) {
				regionName = regionName.replace("市", "");
			} else if (regionName.contains("族自治区")) {
				regionName = regionName.substring(0, regionName.indexOf("族自治区") - 1);
			} else if (regionName.contains("省")) {
				regionName = regionName.replace("省", "");
			}
			regionMap.put(regionName, ((String) region.get(8)).toLowerCase());
		}

		localDB.closeAll();
	}

	/**
	 * 客户名称拼音化
	 * 
	 * @param regionMap
	 *            key:地区名 value:国标缩写
	 * @param customerName
	 * @return
	 * @throws SQLException
	 */
	private String customerNamePinyin(Map<String, String> regionMap, Customer customer) throws SQLException {
//		String loginName = null;
//
//		// 加盟为拼音 直营为柜台号
//		if ("直营".equals(customer.getCustomerType())) {
//			List<Counter> cl = counterManager.getCounterByCustomerId(customer.getCustomerID());
//			//直营上级客户
//			if(customer.getChildren()!=null&&!customer.getChildren().isEmpty()){
//				loginName=hanyu.getStringPinYin(customer.getCustomerName());
//			//直营柜台客户
//			}else if (!cl.isEmpty()) {
//				loginName = cl.get(0).getCounterCode();
//			}
//		} else if ("加盟商".equals(customer.getCustomerType())//
//				||"代理商".equals(customer.getCustomerType())//
//				||"分销商".equals(customer.getCustomerType())) {
//			String customerName = customer.getCustomerName();
//			if(customerName.indexOf('/')>=0){
//				customerName=customerName.substring(0, customerName.indexOf('/'));
//			}
//
//			// 湖南长沙XXXX
//			if (regionMap.containsKey(customerName.substring(0, 2))
//					&& regionMap.containsKey(customerName.substring(2, 4))) {
//				loginName = regionMap.get(customerName.substring(0, 2)) + ""
//						+ regionMap.get(customerName.substring(2, 4))
//						+ hanyu.getFirstSpell(customerName.substring(4), 0);
//			} else if (regionMap.containsKey(customerName.substring(0, 2))) {
//				if (customerName.length() == 4) {
//					// 长度为4 例:湖南李某
//					loginName = regionMap.get(customerName.substring(0, 2))
//							+ hanyu.getStringPinYin(customerName.substring(2));
//				} else if (customerName.length() == 5) {
//					// 长度为5 例:湖南李某某
//					loginName = regionMap.get(customerName.substring(0, 2))
//							+ hanyu.getStringPinYin(customerName.substring(2, 3))
//							+ hanyu.getFirstSpell(customerName.substring(3), 0);
//				} else {
//					// 长度不定 例:武汉青山红钢城沃尔玛 目前格式为前两个转为国标缩写 后面全缩写
//					loginName = regionMap.get(customerName.substring(0, 2))
//							+ hanyu.getFirstSpell(customerName.substring(2), 0);
//				}
//			} else {
//				// 国标表里没有 着全拼音缩写
//				loginName = hanyu.getFirstSpell(customerName, 0);
//			}
//		}
		
		String loginName = null;

		// 加盟为拼音 直营为柜台号
		if (Customer.customerType_Direct.equals(customer.getCustomerTypeID().toString())//
				&& customer.getChildren().isEmpty()) {

			List<Counter> cl = counterManager.getCounterByCustomerId(customer.getCustomerID());
			loginName = cl.get(0).getCounterCode();
		}
		// FIXME:分销没有数据
		if (Customer.customerType_Franchisee.equals(customer.getCustomerTypeID().toString())//
				|| Customer.customerType_Agent.equals(customer.getCustomerTypeID().toString())) {

			String customerName = customer.getCustomerName();
			// 两人合开的用户如:湖南某某某/某某某 取前面名字
			if (customerName.indexOf('/') >= 0) {
				customerName = customerName.substring(0, customerName.indexOf('/'));
				
			}
			if (customerName.indexOf('（') >= 0) {
				customerName = customerName.substring(0, customerName.indexOf('（'));
				
			}

			// 省的长度 北京=2 黑龙江=3 (内蒙=2 装备 map<省名,国标缩写>时特殊处理)
			Integer indexOfProvince = 2;
			while (indexOfProvince < 4) {
				// 湖南长沙XXXX
				if (regionMap.containsKey(customerName.substring(0, indexOfProvince))) {
					// 长度为省名长度+2 例:湖南李某 黑龙江李某 国标+全拼
					if (customerName.length() == indexOfProvince + 2) {
						loginName = regionMap.get(customerName.substring(0, indexOfProvince))
								+ hanyu.getStringPinYin(customerName.substring(indexOfProvince));
					}
					// 长度为省名长度+3 例:湖南李某某 黑龙江李某某 国标+姓氏全拼+名字缩写
					if (customerName.length() == indexOfProvince + 3) {
						loginName = regionMap.get(customerName.substring(0, indexOfProvince))
								+ hanyu.getStringPinYin(customerName.substring(indexOfProvince, indexOfProvince + 1))
								+ hanyu.getFirstSpell(customerName.substring(indexOfProvince+1), 0);
					}
					// 长度大于省名长度+2（3） 例:江苏欧阳云飞 jsoyyf
					if (customerName.length() > indexOfProvince + 3) {
						loginName = regionMap.get(customerName.substring(0, indexOfProvince))
								+ hanyu.getFirstSpell(customerName.substring(indexOfProvince), 0);
					}
				}
				if (loginName != null) {
					break;
				}
				indexOfProvince++;
			}
			// 国标表里没有 着全拼音缩写
			if (loginName == null) {
				loginName = hanyu.getFirstSpell(customerName, 0);
			}
		}
		if("12398".equals(customer.getCustomerTypeID().toString())){
			loginName = hanyu.getFirstSpell(customer.getCustomerName(), 0);
		}

		return loginName;
	}

	/**
	 * 返回长度为【strLength】的随机数，在前面补0
	 */
	private static String getFixLenthString(int len) {
		char []cc = {'0','1','2','3','5','6','8','9'};
		StringBuilder b = new StringBuilder();
		Random rm = new Random();
		for (int i=0; i<len; i++){
			int idx = rm.nextInt(cc.length);
			b.append(cc[idx]);
		}
		return b.toString();
	}

	private UserDetail initUserDetail(AuthUser authUser, Customer customer) {
		// TODO 判空
		UserDetail detail = new UserDetail();
		detail.setUserId(authUser.getUid());
		detail.setName(customer.getCustomerName());
		detail.setMobile(customer.getMobile());
		detail.setPhone(customer.getPhone());
		detail.setCustomerType(customer.getCustomerType());
		detail.setProvince(customer.getProvince());
		detail.setCity(customer.getCity());
		detail.setAreaCounty(customer.getAreaCounty());
		detail.setPhone(customer.getPostCode());
		detail.setStatus(true);
		detail.setLastUpdate(System.currentTimeMillis());
		return detail;
	}

	private AuthUser initAuthUser(Customer customer, String password, Integer orgId) throws SQLException {
		// TODO 判空
		AuthUser authUser = new AuthUser();
		String loginName = customerNamePinyin(shortNameMap, customer);
		if(loginName==null){
			System.out.println(customer);
		}

		int i = 2;

		String temp = new String(loginName);
		while (loginNames.contains(temp)) {
			temp = loginName + i;
		}
		loginName = temp;
		

		authUser.setUid("U" + userId);
		authUser.setLoginName(loginName);
		authUser.setCommonName(customer.getCustomerName());
		authUser.setPassword(password);
		authUser.setOrgUnit(orgId);
		if ("直营".equals(customer.getCustomerType())
				&&customer.getChildren()!=null
				&&customer.getChildren().isEmpty()) {
			authUser.setRoles(direct_sale_roles);
		} else {
//		} else if ("加盟商".equals(customer.getCustomerType())) {
			authUser.setRoles(franchise_roles);
		}

		authUser.setType(AuthUser.type_customer);
		authUser.setStatus(AuthUser.user_enable);
		authUser.setCreateTime(System.currentTimeMillis());
		authUser.setLastUpdate(System.currentTimeMillis());

		return authUser;
	}

	private UserCustomer initUserCustomer(Customer customer, AuthUser authUser) throws SQLException {
		UserCustomer userCustomer = new UserCustomer();

		userCustomer.setUserId(authUser.getUid());
		userCustomer.setLoginName(authUser.getLoginName());
		userCustomer.setCustomerId(customer.getCustomerID());
		userCustomer.setCustomerUserId(authUser.getUid());
		userCustomer.setCustomerLoginName(authUser.getLoginName());
		userCustomer.setCounters(customer.getCounters());
		userCustomer.setStatus(1);
		userCustomer.setLastUpdate(System.currentTimeMillis());

		return userCustomer;
	}
	@Test
	public void testName() throws Exception {
		String loginName=null;
		setRegion(shortNameMap);
		Customer customer=customerManager.get(23177);
		loginName=customerNamePinyin(shortNameMap, customer);
		System.out.println(loginName);
	}
}
