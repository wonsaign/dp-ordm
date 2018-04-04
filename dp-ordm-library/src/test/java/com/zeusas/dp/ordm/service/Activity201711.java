package com.zeusas.dp.ordm.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.ActivityContext;
import com.zeusas.dp.ordm.active.model.AmountRule;
import com.zeusas.dp.ordm.active.model.PeriodActivity;
import com.zeusas.dp.ordm.active.model.ProductItem;
import com.zeusas.dp.ordm.active.model.ProductRule;
import com.zeusas.dp.ordm.active.service.ActivityManager;


public class Activity201711 {
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

	public void 双12礼包() {
		Activity activity = new Activity();
		
		// 自动扫描为true
		activity.setAutoAlloca(true);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("双12系统赠送礼包");
		activity.setDescription("零售价每满3万赠超值大礼包一套.礼盒不做累额计算。");
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		activity.setStatus(0);
		activity.setType("04");
		
		// 活动内容具体描述
		ActivityContext context = new ActivityContext();

		AmountRule amtRuler = new AmountRule();
		amtRuler.setAmount(12000);
		Set<Integer> filtered = new HashSet<>();
		// 排除的产品
		for (Integer pid :Arrays.asList(20766,21646,22462,20377,21836,23109,23356,23362,23995,24087,24102,24125)){
			filtered.add(pid);
		}
		amtRuler.setFilterPro(filtered);
		amtRuler.setType(AmountRule.TYPE_TOTAL);
		context.setAmount(amtRuler);
		
		List<ProductRule> actityExtras = context.getActityExtras();
		
		ProductRule prule1 = new ProductRule();
		prule1.setSelected(true);
		int qty = 0;
		// need price
		for (Integer pid :Arrays.asList(24708,24537)){
			ProductItem item = new ProductItem(pid, 0, 13);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(24344)){
			ProductItem item = new ProductItem(pid, 0, 6);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		// dont need price
		for (Integer pid :Arrays.asList(23336,23358,23334)){
			ProductItem item = new ProductItem(pid, 0, 7);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(18661,18794)){
			ProductItem item = new ProductItem(pid, 0, 50);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		
		for (Integer pid :Arrays.asList(25172,25173)){
			ProductItem item = new ProductItem(pid, 0, 7);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		
		for (Integer pid :Arrays.asList(25174)){
			ProductItem item = new ProductItem(pid, 0, 10);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(24386)){
			ProductItem item = new ProductItem(pid, 0, 2);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		prule1.setQuantity(qty);
		actityExtras.add(prule1);
		// Test
//		List<PeriodActivity> stocks = new ArrayList<>();
//		PeriodActivity pa1 = new PeriodActivity();
//		pa1.setTo(20171125);
//		pa1.setFrom(20171101);
//		Set<String> s1 = new HashSet<>();
//		s1.add("10118");
//		s1.add("10615");
//		pa1.setVal(s1);
//		stocks.add(pa1);
		
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20171125);
		pa1.setFrom(20171116);
		Set<String> s1 = new HashSet<>();
		s1.add("23228");
		s1.add("23229");
		// 16号 2点后放开
		s1.add("23227");
		s1.add("23231");
		pa1.setVal(s1);
		stocks.add(pa1);
		
		
		PeriodActivity pa2 = new PeriodActivity();
		pa2.setTo(20171125);
		pa2.setFrom(20171117);
		Set<String> s2 = new HashSet<>();
		s2.add("10615");
		s2.add("23232");
		// 17号2点后放开
//		s2.add("10118");
//		s2.add("23230");
		pa2.setVal(s2);
		stocks.add(pa2);
		
		
		context.setStocks(stocks);
		context.setZones(null);
		context.setCounters(null);
		
		activity.setContext(context);
		
		System.out.println(activity);
		activityManager.add(activity);
	
		
	}
	
	public void 双12礼包1() {
		Activity activity = new Activity();
		
		// 自动扫描为true
		activity.setAutoAlloca(true);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("双12系统赠送礼包1");
		activity.setDescription("零售价每满3万赠超值大礼包一套.礼盒不做累额计算。");
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		activity.setStatus(0);
		activity.setType("04");
		
		// 活动内容具体描述
		ActivityContext context = new ActivityContext();

		AmountRule amtRuler = new AmountRule();
		amtRuler.setAmount(12000);
		Set<Integer> filtered = new HashSet<>();
		// 排除的产品
		for (Integer pid :Arrays.asList(20766,21646,22462,20377,21836,23109,23356,23362,23995,24087,24102,24125)){
			filtered.add(pid);
		}
		amtRuler.setFilterPro(filtered);
		amtRuler.setType(AmountRule.TYPE_TOTAL);
		context.setAmount(amtRuler);
		
		List<ProductRule> actityExtras = context.getActityExtras();
		
		ProductRule prule1 = new ProductRule();
		prule1.setSelected(true);
		int qty = 0;
		// need price
		for (Integer pid :Arrays.asList(25174)){
			ProductItem item = new ProductItem(pid, 0, 10);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		prule1.setQuantity(qty);
		actityExtras.add(prule1);
		// Test
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20181125);
		pa1.setFrom(20171101);
		Set<String> s1 = new HashSet<>();
		s1.add("10118");
		s1.add("10615");
		pa1.setVal(s1);
		stocks.add(pa1);
		
		context.setStocks(stocks);
		context.setZones(null);
		context.setCounters(null);
		
		activity.setContext(context);
		
		System.out.println(activity);
		activityManager.add(activity);
	
		
	}
	
	public void 双12礼包2() {
		Activity activity = new Activity();
		
		// 自动扫描为true
		activity.setAutoAlloca(true);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("双12系统赠送礼包2");
		activity.setDescription("零售价每满3万赠超值大礼包一套.礼盒不做累额计算。");
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		activity.setStatus(0);
		activity.setType("04");
		
		// 活动内容具体描述
		ActivityContext context = new ActivityContext();

		AmountRule amtRuler = new AmountRule();
		amtRuler.setAmount(20000);
		Set<Integer> filtered = new HashSet<>();
		// 排除的产品
		for (Integer pid :Arrays.asList(20766,21646,22462,20377,21836,23109,23356,23362,23995,24087,24102,24125)){
			filtered.add(pid);
		}
		amtRuler.setFilterPro(filtered);
		amtRuler.setType(AmountRule.TYPE_TOTAL);
		context.setAmount(amtRuler);
		
		List<ProductRule> actityExtras = context.getActityExtras();
		
		ProductRule prule1 = new ProductRule();
		prule1.setSelected(true);
		int qty = 0;
		// need price
		for (Integer pid :Arrays.asList(24708,24537)){
			ProductItem item = new ProductItem(pid, 0, 13);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		prule1.setQuantity(qty);
		actityExtras.add(prule1);
		// Test
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20181125);
		pa1.setFrom(20171101);
		Set<String> s1 = new HashSet<>();
		s1.add("10118");
		s1.add("10615");
		pa1.setVal(s1);
		stocks.add(pa1);
		
		context.setStocks(stocks);
		context.setZones(null);
		context.setCounters(null);
		
		activity.setContext(context);
		
		System.out.println(activity);
		activityManager.add(activity);
	
		
	}
	
	public static void main(String[] args) {
		Activity201711 a = new Activity201711();
		a.init();
		a.双12礼包1();
		a.双12礼包2();
	}


}
