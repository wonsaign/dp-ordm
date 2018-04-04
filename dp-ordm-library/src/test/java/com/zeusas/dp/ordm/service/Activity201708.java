package com.zeusas.dp.ordm.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.ActivityContext;
import com.zeusas.dp.ordm.active.model.AmountRule;
import com.zeusas.dp.ordm.active.model.PeriodActivity;
import com.zeusas.dp.ordm.active.model.ProductItem;
import com.zeusas.dp.ordm.active.model.ProductRule;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.service.ProductManager;


public class Activity201708 {
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

	public void 石斛兰买11增1() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("新品石斛兰买11赠1");
		activity.setDescription("石斛兰买11赠1");
		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		
		activity.setStatus(0);
		activity.setType("02");

		// 活动内容具体描述
		ActivityContext context = new ActivityContext();
		//产品购买规则
		ProductRule good1 = new ProductRule();
		
		for (Integer pid :Arrays.asList(23097,23096,23098,21380,23326)){
			ProductItem item = new ProductItem(pid, 0, 11);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(11);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(23097,23096,23098,21380,23326)){
			ProductItem item = new ProductItem(pid, 0, 1);
			prule2.addProductItem(item);
		}
		// 赠送数量1个
		prule2.setQuantity(1);
		actityExtras.add(prule2);
		
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	public void 石斛兰大礼包等级1(){
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("石斛兰订货会订货礼包");
		activity.setDescription("石斛兰订货会订货礼包，满8000");
		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		
		activity.setStatus(0);
		activity.setType("41");

		// 活动内容具体描述
		ActivityContext context = new ActivityContext();
		//产品购买规则
		ProductRule good1 = new ProductRule();
		
		for (Integer pid :Arrays.asList(23097,23096,23098,21380,23326)){
			ProductItem item = new ProductItem(pid, 0, 1);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(1);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		
		Map<Integer, ProductItem> productItem = new HashMap<>();
		Product p1 = pm.get(22576);
		ProductItem pm1 = new ProductItem(p1.getProductId(), 0, 25);
		productItem.put(p1.getProductId(), pm1);
		Product p2 = pm.get(22627);
		ProductItem pm2 = new ProductItem(p2.getProductId(), 0, 10);
		productItem.put(p2.getProductId(), pm2);

		Product p3 = pm.get(22629);
		ProductItem pm3 = new ProductItem(p3.getProductId(), 0, 8);
		productItem.put(p3.getProductId(), pm3);
		
		Product p4 = pm.get(22630);
		ProductItem pm4 = new ProductItem(p4.getProductId(), 0, 21);
		productItem.put(p4.getProductId(), pm4);
		
		// 赠送数量1个
		prule2.setProductItem(productItem);
		prule2.setQuantity(1);
		actityExtras.add(prule2);
		
		// 制作区域
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20170821);
		pa1.setFrom(20170809);
		Set<String> s1 = new HashSet<>();
		s1.add("10118");
		s1.add("23228");
		s1.add("23227");
		s1.add("10615");
		pa1.setVal(s1);
		stocks.add(pa1);
		
//		PeriodActivity pa2 = new PeriodActivity();
//		pa2.setTo(20170820);
//		pa2.setFrom(20170809);
//		Set<String> s2 = new HashSet<>();
//		s2.add("10615");
//		pa2.setVal(s2);
//		stocks.add(pa2);
		
		context.setStocks(stocks);
		context.setZones(null);
		context.setCounters(null);
		
		AmountRule ar = new AmountRule();
		ar.setAmount(8000.0);
		ar.setType(04);
		context.setAmount(ar);
		
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
		
	}
	public void 石斛兰大礼包等级2(){
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("石斛兰订货会订货礼包");
		activity.setDescription("石斛兰订货会订货礼包，满12000");
		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		
		activity.setStatus(0);
		activity.setType("41");

		// 活动内容具体描述
		ActivityContext context = new ActivityContext();
		//产品购买规则
		ProductRule good1 = new ProductRule();
		
		for (Integer pid :Arrays.asList(23097,23096,23098,21380,23326)){
			ProductItem item = new ProductItem(pid, 0, 1);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(1);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		
		Map<Integer, ProductItem> productItem = new HashMap<>();
		Product p1 = pm.get(22576);
		ProductItem pm1 = new ProductItem(p1.getProductId(), 0, 47);
		productItem.put(p1.getProductId(), pm1);
		Product p2 = pm.get(22627);
		ProductItem pm2 = new ProductItem(p2.getProductId(), 0, 19);
		productItem.put(p2.getProductId(), pm2);

		Product p3 = pm.get(22629);
		ProductItem pm3 = new ProductItem(p3.getProductId(), 0, 16);
		productItem.put(p3.getProductId(), pm3);
		
		Product p4 = pm.get(22630);
		ProductItem pm4 = new ProductItem(p4.getProductId(), 0, 42);
		productItem.put(p4.getProductId(), pm4);
		
		// 赠送数量1个
		prule2.setProductItem(productItem);
		prule2.setQuantity(1);
		actityExtras.add(prule2);
		
		// 制作区域
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20170821);
		pa1.setFrom(20170809);
		Set<String> s1 = new HashSet<>();
		s1.add("10118");
		s1.add("23228");
		s1.add("23227");
		s1.add("10615");
		pa1.setVal(s1);
		stocks.add(pa1);
		
//		PeriodActivity pa2 = new PeriodActivity();
//		pa2.setTo(20170820);
//		pa2.setFrom(20170809);
//		Set<String> s2 = new HashSet<>();
//		s2.add("10118");
//		s2.add("23228");
//		s2.add("23227");
//		s2.add("10615");
//		pa2.setVal(s2);
//		stocks.add(pa2);
		
		context.setStocks(stocks);
		context.setZones(null);
		context.setCounters(null);
		
		AmountRule ar = new AmountRule();
		ar.setAmount(12000.0);
		ar.setType(04);
		context.setAmount(ar);
		
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	public void 石斛兰大礼包等级3(){
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("石斛兰订货会订货礼包");
		activity.setDescription("石斛兰订货会订货礼包，满50000");
		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		
		activity.setStatus(0);
		activity.setType("41");

		// 活动内容具体描述
		ActivityContext context = new ActivityContext();
		//产品购买规则
		ProductRule good1 = new ProductRule();
		
		for (Integer pid :Arrays.asList(23097,23096,23098,21380,23326)){
			ProductItem item = new ProductItem(pid, 0, 1);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(1);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		
		Map<Integer, ProductItem> productItem = new HashMap<>();
		Product p1 = pm.get(22576);
		ProductItem pm1 = new ProductItem(p1.getProductId(), 0, 197);
		productItem.put(p1.getProductId(), pm1);
		Product p2 = pm.get(22627);
		ProductItem pm2 = new ProductItem(p2.getProductId(), 0, 78);
		productItem.put(p2.getProductId(), pm2);

		Product p3 = pm.get(22629);
		ProductItem pm3 = new ProductItem(p3.getProductId(), 0, 66);
		productItem.put(p3.getProductId(), pm3);
		
		Product p4 = pm.get(22630);
		ProductItem pm4 = new ProductItem(p4.getProductId(), 0, 147);
		productItem.put(p4.getProductId(), pm4);
		
		// 赠送数量1个
		prule2.setProductItem(productItem);
		prule2.setQuantity(1);
		actityExtras.add(prule2);
		
		// 制作区域
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20170821);
		pa1.setFrom(20170809);
		Set<String> s1 = new HashSet<>();
		s1.add("10118");
		s1.add("23228");
		s1.add("23227");
		s1.add("10615");
		pa1.setVal(s1);
		stocks.add(pa1);
		
//		PeriodActivity pa2 = new PeriodActivity();
//		pa2.setTo(20170820);
//		pa2.setFrom(20170809);
//		Set<String> s2 = new HashSet<>();
//		s2.add("10615");
//		pa2.setVal(s2);
//		stocks.add(pa2);
		
		context.setStocks(stocks);
		context.setZones(null);
		context.setCounters(null);
		
		AmountRule ar = new AmountRule();
		ar.setAmount(50000.0);
		ar.setType(04);
		context.setAmount(ar);
		
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	

	
	
	public static void main(String[] args) {
		Activity201708 a = new Activity201708();
		a.init();
	//	a.石斛兰买11增1();
		a.石斛兰大礼包等级1();
		a.石斛兰大礼包等级2();
		a.石斛兰大礼包等级3();
	}


}
