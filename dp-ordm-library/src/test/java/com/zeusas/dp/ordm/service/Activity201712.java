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


public class Activity201712 {
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

	public void 第12月礼包1_2w() {
		Activity activity = new Activity();
		
		// 自动扫描为true
		activity.setAutoAlloca(true);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("12月超值订货礼包一");
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
		for (Integer pid :Arrays.asList(20377,
										20766,
										21646,
										21836,
										22462,
										23109,
										23356,
										23362,
										23995,
										24087,
										24102,
										24125,
										24423,
										24427,
										24757,
										24768
										)){
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
		for (Integer pid :Arrays.asList(24933,25454)){
			ProductItem item = new ProductItem(pid, 0, 10);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(24821)){
			ProductItem item = new ProductItem(pid, 0, 100);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(25372)){
			ProductItem item = new ProductItem(pid, 0, 20);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		// dont need price
		for (Integer pid :Arrays.asList(25452,25009,25011,25013)){
			ProductItem item = new ProductItem(pid, 0, 18);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(23336,23358,23334)){
			ProductItem item = new ProductItem(pid, 0, 8);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}

		prule1.setQuantity(qty);
		actityExtras.add(prule1);
		
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20171225);
		pa1.setFrom(20171206);
		Set<String> s1 = new HashSet<>();
		s1.add("23228");
		s1.add("23229");
		// 16号 2点后放开
		s1.add("23227");
		s1.add("23231");
		s1.add("10615");
		s1.add("23232");
		// 17号2点后放开
		s1.add("10118");
		s1.add("23230");
		pa1.setVal(s1);
		stocks.add(pa1);
		
		context.setStocks(stocks);
		context.setZones(null);
		context.setCounters(null);
		
		activity.setContext(context);
		
		System.out.println(activity);
		activityManager.add(activity);
	
		
	}
	
	public void 第12月礼包2w() {
		Activity activity = new Activity();
		
		// 自动扫描为true
		activity.setAutoAlloca(true);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("12月超值订货礼包二");
		activity.setDescription("零售价每满5万赠超值大礼包一套.礼盒不做累额计算。");
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
		for (Integer pid :Arrays.asList(20377,
										20766,
										21646,
										21836,
										22462,
										23109,
										23356,
										23362,
										23995,
										24087,
										24102,
										24125,
										24423,
										24427,
										24757,
										24768
										)){
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
		for (Integer pid :Arrays.asList(25637)){
			ProductItem item = new ProductItem(pid, 0, 1);
			qty += item.getQuantity();
			prule1.addProductItem(item);

		prule1.setQuantity(qty);
		actityExtras.add(prule1);
		
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20171225);
		pa1.setFrom(20171206);
		Set<String> s1 = new HashSet<>();
		s1.add("23228");
		s1.add("23229");
		// 16号 2点后放开
		s1.add("23227");
		s1.add("23231");
		s1.add("10615");
		s1.add("23232");
		// 17号2点后放开
		s1.add("10118");
		s1.add("23230");
		pa1.setVal(s1);
		stocks.add(pa1);
		
		context.setStocks(stocks);
		context.setZones(null);
		context.setCounters(null);
		
		activity.setContext(context);
		
		System.out.println(activity);
		activityManager.add(activity);
	
		}	
	}
	
	public void test12月礼包1_2w() {
		Activity activity = new Activity();
		
		// 自动扫描为true
		activity.setAutoAlloca(true);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("12月超值订货礼包一");
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
		for (Integer pid :Arrays.asList(20377,
										20766,
										21646,
										21836,
										22462,
										23109,
										23356,
										23362,
										23995,
										24087,
										24102,
										24125,
										24423,
										24427,
										24757,
										24768
										)){
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
		for (Integer pid :Arrays.asList(24933)){
			ProductItem item = new ProductItem(pid, 0, 10);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(24821)){
			ProductItem item = new ProductItem(pid, 0, 100);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(25372)){
			ProductItem item = new ProductItem(pid, 0, 20);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		// dont need price
		for (Integer pid :Arrays.asList(25009,25011,25013)){
			ProductItem item = new ProductItem(pid, 0, 18);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(23336,23358,23334)){
			ProductItem item = new ProductItem(pid, 0, 8);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}

		prule1.setQuantity(qty);
		actityExtras.add(prule1);
		
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20171225);
		pa1.setFrom(20171206);
		Set<String> s1 = new HashSet<>();
		s1.add("23228");
		s1.add("23229");
		// 16号 2点后放开
		s1.add("23227");
		s1.add("23231");
		s1.add("10615");
		s1.add("23232");
		// 17号2点后放开
		s1.add("10118");
		s1.add("23230");
		pa1.setVal(s1);
		stocks.add(pa1);
		
		context.setStocks(stocks);
		context.setZones(null);
		context.setCounters(null);
		
		activity.setContext(context);
		
		System.out.println(activity);
		activityManager.add(activity);
	
		
	}
	
	public void 第18年01月礼包1_3w() {
		Activity activity = new Activity();
		
		// 自动扫描为true
		activity.setAutoAlloca(true);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("超值大礼包赠送");
		activity.setDescription("单笔订单每满1.3万，赠超值大礼包1套，礼盒不做累额计算。");
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		activity.setStatus(0);
		activity.setType("04");
		
		// 活动内容具体描述
		ActivityContext context = new ActivityContext();

		AmountRule amtRuler = new AmountRule();
		amtRuler.setAmount(13000);
		Set<Integer> filtered = new HashSet<>();
		// 排除的产品
		for (Integer pid :Arrays.asList(20377,
										20766,
										21646,
										21836,
										22462,
										23109,
										23356,
										23362,
										23995,
										24087,
										24102,
										24125,
										24423,
										24427,
										24757,
										24768,
										25129,
										25317
										)){
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

		for (Integer pid :Arrays.asList(25246,25248,25250,25252)){
			ProductItem item = new ProductItem(pid, 0, 10);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(24821)){
			ProductItem item = new ProductItem(pid, 0, 100);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(25695,25697)){
			ProductItem item = new ProductItem(pid, 0, 20);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		// dont need price
		for (Integer pid :Arrays.asList(23334,23336,23358)){
			ProductItem item = new ProductItem(pid, 0, 8);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(24933,25136)){
			ProductItem item = new ProductItem(pid, 0, 11);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		
		for (Integer pid :Arrays.asList(25453,24950,24956)){
			ProductItem item = new ProductItem(pid, 0, 5);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		
		for (Integer pid :Arrays.asList(25454)){
			ProductItem item = new ProductItem(pid, 0, 6);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		
		for (Integer pid :Arrays.asList(24524,25469)){
			ProductItem item = new ProductItem(pid, 0, 7);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		
		for (Integer pid :Arrays.asList(24960)){
			ProductItem item = new ProductItem(pid, 0, 25);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		
		for (Integer pid :Arrays.asList(19147)){
			ProductItem item = new ProductItem(pid, 0, 2);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		
		for (Integer pid :Arrays.asList(17561)){
			ProductItem item = new ProductItem(pid, 0, 3);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}

		prule1.setQuantity(qty);
		actityExtras.add(prule1);
		
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20180205);
		pa1.setFrom(20180115);
		Set<String> s1 = new HashSet<>();
		s1.add("23228");
		s1.add("23229");
		// 16号 2点后放开
		s1.add("23227");
		s1.add("23231");
		s1.add("10615");
		s1.add("23232");
		// 17号2点后放开
		s1.add("10118");
		s1.add("23230");
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
		Activity201712 a = new Activity201712();
		a.init();
	//	a.第12月礼包1_2w();
//		a.test12月礼包1_2w();
//		a.第12月礼包2w();
		a.第18年01月礼包1_3w();
	}
}
