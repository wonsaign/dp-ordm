package com.zeusas.dp.ordm.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.active.bean.ActivityType;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.ActivityContext;
import com.zeusas.dp.ordm.active.model.AmountRule;
import com.zeusas.dp.ordm.active.model.PeriodActivity;
import com.zeusas.dp.ordm.active.model.ProductItem;
import com.zeusas.dp.ordm.active.model.ProductRule;
import com.zeusas.dp.ordm.active.service.ActivityManager;


public class ActivityTestCase {
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

	public void 礼包01_买赠() {
		Activity activity = new Activity();
		
		// 自动扫描为true
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("买赠01活动测试");
		activity.setDescription("买赠礼包单一");
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		activity.setStatus(0);
		activity.setType(ActivityType.TYPE_BUYGIVE);
		// 活动内容具体描述
		ActivityContext context = new ActivityContext();

		ProductRule buyRule =  new ProductRule();
		buyRule.setSelected(false);
		int qty0 = 2;
		// need price
		for (Integer pid :Arrays.asList(25095,24580,24578)){
			ProductItem item = new ProductItem(pid, 0, 6);
			buyRule.addProductItem(item);
		}
		buyRule.setQuantity(qty0);
		context.setActityGoods(buyRule);
		
		
		List<ProductRule> actityExtras = context.getActityExtras();
		
		ProductRule prule1 = new ProductRule();
		prule1.setSelected(false);
		int qty = 1;
		// need price
		for (Integer pid :Arrays.asList(19963,22263)){
			ProductItem item = new ProductItem(pid, 0, 3);
			prule1.addProductItem(item);
		}

		prule1.setQuantity(qty);
		actityExtras.add(prule1);
		
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20180205);
		pa1.setFrom(20180110);
		Set<String> s1 = new HashSet<>();
		s1.add("23228");
		s1.add("23229");
		s1.add("23227");
		s1.add("23231");
		s1.add("10615");
		s1.add("23232");
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
	public void 礼包11_买赠() {
		Activity activity = new Activity();
		
		// 自动扫描为true
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("买赠11活动测试");
		activity.setDescription("买赠礼包多个");
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		activity.setStatus(0);
		activity.setType(ActivityType.TYPE_BUYGIVES);
		// 活动内容具体描述
		ActivityContext context = new ActivityContext();

		ProductRule buyRule =  new ProductRule();
		buyRule.setSelected(false);
		int qty0 = 12;
		// need price
		for (Integer pid :Arrays.asList(25893,21317,21311)){
			ProductItem item = new ProductItem(pid, 0, 6);
			buyRule.addProductItem(item);
		}
		buyRule.setQuantity(qty0);
		context.setActityGoods(buyRule);
		
		List<ProductRule> actityExtras = context.getActityExtras();
		
		ProductRule prule1 = new ProductRule();
		prule1.setSelected(true);
		int qty = 2;
		// need price
		for (Integer pid :Arrays.asList(23885)){
			ProductItem item = new ProductItem(pid, 0, 1);
			prule1.addProductItem(item);
		}

		prule1.setQuantity(qty);
		actityExtras.add(prule1);
		
		ProductRule prule2 = new ProductRule();
		prule2.setSelected(false);
		int qty2 = 10;
		// need price
		for (Integer pid :Arrays.asList(21394,19963)){
			ProductItem item = new ProductItem(pid, 0, 10);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(qty2);
		actityExtras.add(prule2);
		
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20180205);
		pa1.setFrom(20180110);
		Set<String> s1 = new HashSet<>();
		s1.add("23228");
		s1.add("23229");
		s1.add("23227");
		s1.add("23231");
		s1.add("10615");
		s1.add("23232");
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
	public void 礼包02_买赠自身() {
		Activity activity = new Activity();
		
		// 自动扫描为true
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("买赠自身活动测试");
		activity.setDescription("买赠自身");
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		activity.setStatus(0);
		activity.setType(ActivityType.TYPE_PRENSENTOWNER);
		// 活动内容具体描述
		ActivityContext context = new ActivityContext();

		ProductRule buyRule =  new ProductRule();
		buyRule.setSelected(false);
		int qty0 = 11;
		// need price
		for (Integer pid :Arrays.asList(18768,18769,23874)){
			ProductItem item = new ProductItem(pid, 0, 11);
			buyRule.addProductItem(item);
		}
		buyRule.setQuantity(qty0);
		context.setActityGoods(buyRule);
		
		List<ProductRule> actityExtras = context.getActityExtras();
		
		ProductRule prule1 = new ProductRule();
		prule1.setSelected(false);
		int qty = 1;
		// need price
		for (Integer pid :Arrays.asList(18768,18769,23874)){
			ProductItem item = new ProductItem(pid, 0, 1);
			prule1.addProductItem(item);
		}

		prule1.setQuantity(qty);
		actityExtras.add(prule1);
		
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20180205);
		pa1.setFrom(20180110);
		Set<String> s1 = new HashSet<>();
		s1.add("23228");
		s1.add("23229");
		s1.add("23227");
		s1.add("23231");
		s1.add("10615");
		s1.add("23232");
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
	
	public void 礼包04_自动送() {

		Activity activity = new Activity();
		
		// 自动扫描为true
		activity.setAutoAlloca(true);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("超值大礼包赠送");
		activity.setDescription("单笔订单每满1.2万，赠超值大礼包1套，礼盒不做累额计算。");
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		activity.setStatus(0);
		activity.setType(ActivityType.TYPE_BIGPACAKGE);
		
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
										24768,
										25129,
										25317,
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

		for (Integer pid :Arrays.asList(22032,22845)){
			ProductItem item = new ProductItem(pid, 0, 1);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(22241)){
			ProductItem item = new ProductItem(pid, 0, 100);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(22573,22574)){
			ProductItem item = new ProductItem(pid, 0, 6);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
	
		prule1.setQuantity(qty);
		actityExtras.add(prule1);
		
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20180205);
		pa1.setFrom(20180110);
		Set<String> s1 = new HashSet<>();
		s1.add("23228");
		s1.add("23229");
		s1.add("23227");
		s1.add("23231");
		s1.add("10615");
		s1.add("23232");
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
	
	public void 礼包08_套装() {
		Activity activity = new Activity();
		
		// 自动扫描为true
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("套装活动测试");
		activity.setDescription("活动套装");
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		activity.setStatus(0);
		activity.setType(ActivityType.TYPE_SUIT);
		// 活动内容具体描述
		ActivityContext context = new ActivityContext();

		ProductRule buyRule =  new ProductRule();
		buyRule.setSelected(true);
		int qty0 = 1;
		// need price
		for (Integer pid :Arrays.asList(23098)){
			ProductItem item = new ProductItem(pid, 100, 1);
			buyRule.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(18771)){
			ProductItem item = new ProductItem(pid, 110, 1);
			buyRule.addProductItem(item);
		}
		buyRule.setQuantity(qty0);
		context.setActityGoods(buyRule);		
		
		List<PeriodActivity> stocks = new ArrayList<>();
		PeriodActivity pa1 = new PeriodActivity();
		pa1.setTo(20180205);
		pa1.setFrom(20180110);
		Set<String> s1 = new HashSet<>();
		s1.add("23228");
		s1.add("23229");
		s1.add("23227");
		s1.add("23231");
		s1.add("10615");
		s1.add("23232");
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
		ActivityTestCase a = new ActivityTestCase();
		a.init();
		a.礼包01_买赠();
		a.礼包11_买赠();
		a.礼包02_买赠自身();
		//a.礼包04_自动送();
		//a.礼包08_套装();
		
	}
}
