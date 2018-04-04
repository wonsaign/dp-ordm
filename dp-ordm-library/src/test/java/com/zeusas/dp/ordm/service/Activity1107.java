package com.zeusas.dp.ordm.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.ActivityContext;
import com.zeusas.dp.ordm.active.model.AmountRule;
import com.zeusas.dp.ordm.active.model.PeriodActivity;
import com.zeusas.dp.ordm.active.model.ProductItem;
import com.zeusas.dp.ordm.active.model.ProductRule;
import com.zeusas.dp.ordm.active.service.ActivityManager;


public class Activity1107 {
	private FileSystemXmlApplicationContext aContext;

	private ActivityManager activityManager;
	CounterManager counterManager;
	ProductManager pm;

	void init() {
		aContext = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
		aContext.start();
		counterManager = AppContext.getBean(CounterManager.class);
		activityManager = aContext.getBean(ActivityManager.class);
		pm = aContext.getBean(ProductManager.class);
		//pm.reload();
	}


	public void 双11打欠活动1(){
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("打欠活动1107");
		activity.setDescription("镇店之宝高保湿 巨补水");
		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		
		activity.setStatus(0);
		activity.setType("08");
		activity.setStart(DateTime.toDate(DateTime.YYYY_MM_DD, "2017-11-01"));
		activity.setTo(DateTime.toDate(DateTime.YYYY_MM_DD, "2017-11-25"));

		// 活动内容具体描述
		ActivityContext context = new ActivityContext();
		//产品购买规则
		ProductRule good1 = new ProductRule();
		// need price
		for (Integer pid :Arrays.asList(3563)){
			ProductItem item = new ProductItem(pid, 0, 2);
			good1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(14418)){
			ProductItem item = new ProductItem(pid, 260.0, 1);
			good1.addProductItem(item);
		}
		// dont need price
		for (Integer pid :Arrays.asList(3547,18854)){
			ProductItem item = new ProductItem(pid, 0, 1);
			good1.addProductItem(item);
		}
		
		// 购物袋
		for (Integer pid :Arrays.asList(24736)){
			ProductItem item = new ProductItem(pid, 0, 1);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(1);
		context.setActityGoods(good1);
		
		
		// 制作区域
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20171125);
		pa1.setFrom(20171101);
		Set<String> s1 = new HashSet<>();
		s1.add("10118");
		s1.add("10615");
		s1.add("23227");
		s1.add("23228");
		pa1.setVal(s1);
		stocks.add(pa1);
		
//		PeriodActivity pa2 = new PeriodActivity();
//		pa2.setTo(20171125);
//		pa2.setFrom(20171101);
//		Set<String> s2 = new HashSet<>();
//		s2.add("10118");
//		pa2.setVal(s2);
//		stocks.add(pa2);
		
		context.setStocks(stocks);
		context.setZones(null);
		context.setCounters(null);
		
		AmountRule ar = new AmountRule();
		ar.setAmount(450.0);
		ar.setType(04);
		context.setAmount(ar);
		
		activity.setStatus(0);
		
		context.setRevStart(DateTime.toDate(DateTime.YYYY_MM_DD, "2017-11-01"));
		context.setRevEnd(DateTime.toDate(DateTime.YYYY_MM_DD, "2017-11-10"));
		context.setExecStart(DateTime.toDate(DateTime.YYYY_MM_DD, "2017-11-11"));
		context.setExecEnd(DateTime.toDate(DateTime.YYYY_MM_DD, "2017-11-25"));
		
		Set<Integer> revproduct=new HashSet<>();
		revproduct.add(3563);
		revproduct.add(14418);
		
		revproduct.add(18854);
		
		context.setRevProducts(revproduct);
		
		context.setOrAddStatus("10118", 1);
		context.setOrAddStatus("10615", 1);
		context.setOrAddStatus("23227", 1);
		context.setOrAddStatus("23228", 1);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
		
	}
	
	public static void main(String[] args) {
		Activity1107 a = new Activity1107();
		a.init();
		a.双11打欠活动1();
//		a.双11活动2();
//		a.双11活动3();
//		a.双11活动4_1();
//		a.双11活动4_2();
		
//		a.双11大礼包1();
	}


}
