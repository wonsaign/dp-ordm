package com.zeusas.dp.ordm.service;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.common.service.SmsService;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.dao.CmbcPayRecordDao;
import com.zeusas.dp.ordm.entity.CmbcPayRecord;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderCredentials;
import com.zeusas.dp.ordm.utils.UriUtil;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.http.BasicController;

public class TestEnvierment extends BasicController{

	private FileSystemXmlApplicationContext aContext;

	OrderDetailService orderDetailService;
	OrderService orderService;
	OrderCredentialsService credentialsService;
	CmbcPayRecordService crs;
	CmbcPayRecordDao crsdao;
	CounterManager counterManager;
	ActivityManager activityManager;
	{
		aContext = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
		aContext.start();
		orderDetailService = aContext.getBean(OrderDetailService.class);
		orderService = aContext.getBean(OrderService.class);
		credentialsService = aContext.getBean(OrderCredentialsService.class);
		crs = aContext.getBean(CmbcPayRecordService.class);
		crsdao = aContext.getBean(CmbcPayRecordDao.class);
		counterManager = aContext.getBean(CounterManager.class);
		activityManager = aContext.getBean(ActivityManager.class);
	}
	
	
	
	//@Test
	public void Test(){
		Order order = new Order();
		order.setAddress("t");
		order.setPhone("t");
		order.setContact("t");
		order.setCounterId(123456789);
		order.setCounterName("t");
		order.setCounterCode("t");
		order.setUserId("t");
		order.setUserName("t");
		order.setCustomerId("t");
		order.setCustomerName("t");
		order.setOrderStatus(Order.Status_UnPay);
		order.setOrderCreatTime(System.currentTimeMillis());
		order.setExpressFee(0.0);
		order.setMaterialFee(0.0);
		order.setRealFee(0.0);
		
		order.setOrderNo("ttt");
		order.setId(Long.valueOf(66666666));
		order.setCredentialsNo("t");
		
		Counter counter = new Counter();
		counter.setAddress("tt");
		counter.setCounterId(987654);
		counter.setCounterName("tt");
	
//		List<String> t = new ArrayList<>();
//		t.add(order.getOrderNo())
		
		OrderCredentials orderCredentials = new OrderCredentials(order, counter);
		orderCredentials.setOcid("111111222222");
		credentialsService.save(orderCredentials);
	}
//	@Test
	public void 转换数据类型(){
		List<OrderCredentials> orderCredentials = credentialsService.findAll();
		for (OrderCredentials oc : orderCredentials) {
			StringBuffer sb = new StringBuffer();
//			sb.append("[\"").append(oc.getOrderNo()).append("\"]");
//			oc.setOrderNo(sb.toString());
			credentialsService.update(oc);
		}
	}
//	@Test
	public void 获取属性测试(){
		OrderCredentials o = credentialsService.getOrderCredentials("201701190001");
		System.out.println(o==null);
	//	OrderCredentials o = credentialsService.get(Long.valueOf(1707010487));
	//	System.out.println(o.getOrderNos().get(1));
	}
	
//	@Test
	public void 生成合并订单(){
		// 201706300006
		AuthUser payMan = new AuthUser();
//		orderCredentials.setCreatorId(payMan.getLoginName());
//		orderCredentials.setCreatorName(payMan.getCommonName());
//		orderCredentials.setPayManId(payMan.getLoginName());
//		orderCredentials.setPayManName(payMan.getCommonName());
		payMan.setLoginName("hnyaoshan");
		payMan.setCommonName("湖南姚珊");
		double actualPay = 49548.0;
		String description = "测试姚珊合并订单";
		List<String> combineImages = new ArrayList<>();
		combineImages.add("o/p/1707/1/201707010014_0.jpg");
		
		List<Order> os = new ArrayList<Order>(4);
		Order o1 = orderService.getsingleOrder("201707250007");
		os.add(o1);
		Order o2 = orderService.getsingleOrder("201707260008");
		os.add(o2);
	//	Order o3 = orderService.getsingleOrder("201706300008");
	//	os.add(o3);
	//	List<Order> orders = orderService.getCombineOrders(os);
		try {
//		orderService.createCombinePayment(os);//Step1 生成
//		
//		orderService.userBalance(os, 50.0, true);//Step2 使用余额
//		
//		orderService.payCombineOrder(os, payMan, actualPay, description, combineImages);//Step3 合并支付
//		
//		orderService.balancePayCombineOrdersDirect(os,payMan,description);	//Step3.5余额全额支付
//		
//		orderService.confirmCombineOrderC(os);//Step4 确认合并支付
//		
//		orderService.cancelCombineOrders(os);//Step5 取消支付
//		
//		orderService.dismantleCombineOrders(os);//Step6 拆解合并订单
		
		orderService.financialAuditCombine(os, true);//财务审核
	//	List<OrderCredentials> ocs = credentialsService.getAllCombineOrders();
		} catch (Exception e){
			e.printStackTrace();
		}  
		
	}
//	@Test
	public void 获取合并订单的集合(){
		OrderCredentials ocs = credentialsService.getCombineOrderCredentials("201707010010");
	//	System.out.println(ocs.getOrderNos().get(2));
		
		OrderCredentials oc =credentialsService.getOrderCredentials("201707010010");
		//System.out.println(oc.getOrderNo());
	}
	
//	@Test
	public void 获取单个CMCC凭证根据单号(){
		try{
			CmbcPayRecord cpr =  crs.getByOrderNo("201707260003");
			System.out.println(cpr==null);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} 
	}
	
	// @Test
	public void test1(){
		try{
			Counter c = counterManager.getCounterByCode("000003");
			System.out.println(c);
			List<Activity> myActies = activityManager.findMyActivities(c);
			System.out.println(myActies);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Integer a = 1;
		Integer b = 2;
		Integer c = 1;
		System.out.println(a.intValue()==c.intValue());
		System.out.println(a.equals(c));
		
		int nowTime = LocalTime.now().getHour();
		System.out.println(nowTime);
	}
	
}
 