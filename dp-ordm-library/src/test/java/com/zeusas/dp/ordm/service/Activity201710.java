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


public class Activity201710 {
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

	public void 排除正品礼包() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("礼包排除正品");
		activity.setDescription("礼包排除正品");
		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		
		activity.setStatus(0);
		activity.setType("00");

		// 活动内容具体描述
		ActivityContext context = new ActivityContext();
		//产品购买规则
		ProductRule good1 = new ProductRule();
		
		for (Integer pid :Arrays.asList(20766,21646,22462,20377,21836,23109,23356,23362,23995,24087,24102,24125)){
			ProductItem item = new ProductItem(pid, 0, 1);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(1);
		context.setActityGoods(good1);
		
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	public void 双11活动1(){
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("镇店之宝高保湿 巨补水");
		activity.setDescription("镇店之宝高保湿 巨补水");
		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		
		activity.setStatus(0);
		activity.setType("08");

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
		pa1.setTo(20171025);
		pa1.setFrom(20171009);
		Set<String> s1 = new HashSet<>();
		s1.add("23228");
		s1.add("23227");
		s1.add("10615");
		pa1.setVal(s1);
		stocks.add(pa1);
		
		PeriodActivity pa2 = new PeriodActivity();
		pa2.setTo(20171012);
		pa2.setFrom(20171011);
		Set<String> s2 = new HashSet<>();
		s2.add("10118");
		pa2.setVal(s2);
		stocks.add(pa2);
		
		context.setStocks(stocks);
		context.setZones(null);
		context.setCounters(null);
		
		AmountRule ar = new AmountRule();
		ar.setAmount(450.0);
		ar.setType(04);
		context.setAmount(ar);
		
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
		
	}

	public void 双11活动2() {

		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("明星热卖 皙白雪肌 沁润透亮");
		activity.setDescription("皙白雪肌 沁润透亮");
		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		
		activity.setStatus(0);
		activity.setType("08");

		// 活动内容具体描述
		ActivityContext context = new ActivityContext();
		//产品购买规则
		ProductRule good1 = new ProductRule();
		// need price
		for (Integer pid :Arrays.asList(20414)){
			ProductItem item = new ProductItem(pid, 150.0, 1);
			good1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(20416)){
			ProductItem item = new ProductItem(pid, 160.0, 1);
			good1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(20415)){
			ProductItem item = new ProductItem(pid, 176.0, 1);
			good1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(20417)){
			ProductItem item = new ProductItem(pid, 150.0, 1);
			good1.addProductItem(item);
		}
		// dont need price
		for (Integer pid :Arrays.asList(20418,20413,20409,20412)){
			ProductItem item = new ProductItem(pid, 0, 1);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(1);
		context.setActityGoods(good1);
		
		context.setZones(null);
		context.setCounters(null);
		
		AmountRule ar = new AmountRule();
		ar.setAmount(636.0);
		ar.setType(04);
		context.setAmount(ar);
		
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
		
	
	}

	public void 双11活动3() {

		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("秋冬之选 360度全效护肤");
		activity.setDescription("");
		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		
		activity.setStatus(0);
		activity.setType("08");

		// 活动内容具体描述
		ActivityContext context = new ActivityContext();
		//产品购买规则
		ProductRule good1 = new ProductRule();
		// need price
		for (Integer pid :Arrays.asList(16714)){
			ProductItem item = new ProductItem(pid, 188.0, 1);
			good1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(20414)){
			ProductItem item = new ProductItem(pid, 150.0, 1);
			good1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(20416)){
			ProductItem item = new ProductItem(pid, 160.0, 1);
			good1.addProductItem(item);
		}
		// dont need price
		for (Integer pid :Arrays.asList(18152,20413,17761)){
			ProductItem item = new ProductItem(pid, 0, 1);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(1);
		context.setActityGoods(good1);
		
		context.setZones(null);
		context.setCounters(null);
		
		AmountRule ar = new AmountRule();
		ar.setAmount(498.0);
		ar.setType(04);
		context.setAmount(ar);
		
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
		
	
	}
	
	public void 双11活动4_1() {

		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("补水神器 纯净山茶 润泽瀑水");
		activity.setDescription("山茶花活动一");
		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		
		activity.setStatus(0);
		activity.setType("08");

		// 活动内容具体描述
		ActivityContext context = new ActivityContext();
		//产品购买规则
		ProductRule good1 = new ProductRule();
		// need price
		for (Integer pid :Arrays.asList(19071)){
			ProductItem item = new ProductItem(pid, 58.0, 1);
			good1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(18556)){
			ProductItem item = new ProductItem(pid, 78.0, 1);
			good1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(19161)){
			ProductItem item = new ProductItem(pid, 108.0, 1);
			good1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(19256)){
			ProductItem item = new ProductItem(pid, 98.0, 1);
			good1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(19162)){
			ProductItem item = new ProductItem(pid, 98.0, 1);
			good1.addProductItem(item);
		}
		// dont need price
		for (Integer pid :Arrays.asList(13124,17759,21325)){
			ProductItem item = new ProductItem(pid, 0, 1);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(1);
		context.setActityGoods(good1);
		
		context.setZones(null);
		context.setCounters(null);
		
		AmountRule ar = new AmountRule();
		ar.setAmount(440.0);
		ar.setType(04);
		context.setAmount(ar);
		
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}

	public void 双11活动4_2() {

		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("补水神器 纯净山茶 润泽瀑水");
		activity.setDescription("山茶花活动二");
		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		
		activity.setStatus(0);
		activity.setType("08");

		// 活动内容具体描述
		ActivityContext context = new ActivityContext();
		//产品购买规则
		ProductRule good1 = new ProductRule();
		// need price
		for (Integer pid :Arrays.asList(19071)){
			ProductItem item = new ProductItem(pid, 58.0, 1);
			good1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(18556)){
			ProductItem item = new ProductItem(pid, 78.0, 1);
			good1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(19161)){
			ProductItem item = new ProductItem(pid, 108.0, 1);
			good1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(19256)){
			ProductItem item = new ProductItem(pid, 98.0, 1);
			good1.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(19162)){
			ProductItem item = new ProductItem(pid, 98.0, 1);
			good1.addProductItem(item);
		}
		// dont need price
		for (Integer pid :Arrays.asList(18852,17759,21325)){
			ProductItem item = new ProductItem(pid, 0, 1);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(1);
		context.setActityGoods(good1);
		
		context.setZones(null);
		context.setCounters(null);
		
		AmountRule ar = new AmountRule();
		ar.setAmount(440.0);
		ar.setType(04);
		context.setAmount(ar);
		
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	public void 双11大礼包1(){
		
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("双11超值大礼包赠送");
		activity.setDescription("订货满2 万 ");
		

		
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
		amtRuler.setType(AmountRule.TYPE_TOTAL);
		context.setAmount(amtRuler);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();
		
		
		ProductRule prule1 = new ProductRule();
		prule1.setSelected(true);
		int qty = 0;
		// 仙人掌4件套 石斛兰洁面15g 凝露15ml 乳液15ml
		for (Integer pid :Arrays.asList(24344,23336,23358,23334)){
			ProductItem item = new ProductItem(pid, 0, 13);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		// 晚宴包
		for (Integer pid :Arrays.asList(24386)){
			ProductItem item = new ProductItem(pid, 0, 3);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		
		// 陈列物料
		for (Integer pid :Arrays.asList(24726)){
			ProductItem item = new ProductItem(pid, 0, 1);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		// 面膜
		for (Integer pid :Arrays.asList(21127)){
			ProductItem item = new ProductItem(pid, 0, 10);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		
		
		prule1.setQuantity(qty);
		actityExtras.add(prule1);

		

		// 赠送数量1个
		context.setStocks(null);
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	public void 系统自动赠送礼包() {
		Activity activity = new Activity();
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("双12满额1.2万赠送礼包");
		activity.setDescription("订货满1.2 万 ");
		activity.setAutoAlloca(true);
		
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
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();
		
		
		ProductRule prule1 = new ProductRule();
		prule1.setSelected(true);
		int qty = 0;
		// 仙人掌4件套 石斛兰洁面15g 凝露15ml 乳液15ml
		for (Integer pid :Arrays.asList(23336,23358,23334)){
			ProductItem item = new ProductItem(pid, 0, 16);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		prule1.setQuantity(qty);
		actityExtras.add(prule1);

		// 赠送数量1个
		context.setStocks(null);
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	
	}
	
	
	public static void main(String[] args) {
		Activity201710 a = new Activity201710();
		a.init();
//		a.双11活动1();
//		a.双11活动2();
//		a.双11活动3();
//		a.双11活动4_1();
//		a.双11活动4_2();
		
		a.系统自动赠送礼包();
	}


}
