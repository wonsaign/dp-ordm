package com.zeusas.dp.ordm.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.ActivityContext;
import com.zeusas.dp.ordm.active.model.AmountRule;
import com.zeusas.dp.ordm.active.model.ProductItem;
import com.zeusas.dp.ordm.active.model.ProductRule;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.service.ProductManager;

public class Activity201706 {
	private FileSystemXmlApplicationContext aContext;

	private ActivityManager activityManager;

	ProductManager pm;

	void init() {
		aContext = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
		aContext.start();
		activityManager = aContext.getBean(ActivityManager.class);
		pm = AppContext.getBean(ProductManager.class);
		pm.reload();
	}

	public void act01() {

		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("人气爆品订就送");
		activity.setDescription("测试三生三世的面膜单片买30送10个 ");
		
		// 创建人id
		
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		activity.setStatus(0);
		activity.setType("01");

		// 活动内容具体描述
		ActivityContext context = new ActivityContext();
		ProductRule actityGoods = new ProductRule();
		// 产品规则里面的产品条目
		// 需要购买的
		// 16714 16714 18772 16732 晚安膜
		

		
		Map<Integer, ProductItem> productItem = new HashMap<>();
		Product p1 = pm.get(21394);
		ProductItem pm1 = new ProductItem(p1.getProductId(), p1.getMemberPrice(), 10);
		productItem.put(p1.getProductId(), pm1);
		Product p2 = pm.get(21396);
		ProductItem pm2 = new ProductItem(p2.getProductId(), p2.getMemberPrice(), 10);
		productItem.put(p2.getProductId(), pm2);

		Product p3 = pm.get(21129);
		ProductItem pm3 = new ProductItem(p3.getProductId(), p3.getMemberPrice(), 10);
		productItem.put(p3.getProductId(), pm3);
		
		Product p4 = pm.get(21123);
		ProductItem pm4 = new ProductItem(p4.getProductId(), p4.getMemberPrice(), 10);
		productItem.put(p4.getProductId(), pm4);
		
		Product p5 = pm.get(21125);
		ProductItem pm5 = new ProductItem(p5.getProductId(), p5.getMemberPrice(), 10);
		productItem.put(p5.getProductId(), pm5);
		
		Product p6 = pm.get(21127);
		ProductItem pm6 = new ProductItem(p6.getProductId(), p6.getMemberPrice(), 10);
		productItem.put(p6.getProductId(), pm6);

		actityGoods.setProductItem(productItem);
		// 购买产品需要订到2个
		actityGoods.setQuantity(30);
		context.setActityGoods(actityGoods);

		// 产品赠送规则
		// 18850 21173 18852 18854 喷雾
		ProductRule actityExtra = new ProductRule();
		Map<Integer, ProductItem> map_item = new HashMap<>();

		/**

		 */
		Product pp1 = pm.get(21394);
		ProductItem pitem1 = new ProductItem(pp1.getProductId(), 0, 10);
		map_item.put(pp1.getProductId(), pitem1);

		Product pp2 = pm.get(21396);
		ProductItem pitem2 = new ProductItem(pp2.getProductId(), 0, 10);
		map_item.put(pp2.getProductId(), pitem2);
		
		Product pp3 = pm.get(21129);
		ProductItem pitem3 = new ProductItem(pp3.getProductId(), 0, 10);
		map_item.put(pp3.getProductId(), pitem3);
		
		Product pp4 = pm.get(21123);
		ProductItem pitem4 = new ProductItem(pp4.getProductId(), 0, 10);
		map_item.put(pp4.getProductId(), pitem4);
		
		Product pp5 = pm.get(21125);
		ProductItem pitem5 = new ProductItem(pp5.getProductId(), 0, 10);
		map_item.put(pp5.getProductId(), pitem5);
		
		Product pp6 = pm.get(21127);
		ProductItem pitem6 = new ProductItem(pp2.getProductId(), 0, 10);
		map_item.put(pp6.getProductId(), pitem6);

		actityExtra.setProductItem(map_item);
		// 赠送数量1个
		actityExtra.setQuantity(10);
		context.setZones(null);
		context.setCounters(null);
		context.setActityExtra(actityExtra);
		activity.setStatus(1);
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}

	
	public void act02() {
		// 16776 16714 18772 16732 晚安膜

		// s 18850 18852 18854 喷雾
		// 18152 气垫粉凝霜

		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("夏季毛孔清理大作战");
		activity.setDescription("购任意系列，即可赠送活动商品1件。 订货政策： 订滇青瓜，野玫瑰，白茶系列任意3件，送活动商品1件");
		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		activity.setStatus(0);
		activity.setType("01");

		// 活动内容具体描述
		ActivityContext context = new ActivityContext();
		ProductRule actityGoods = new ProductRule();
		// 产品规则里面的产品条目
		// 需要购买的
		// 16776 16714 18772 16732 晚安膜
		Map<Integer, ProductItem> productItem = new HashMap<>();
		
		Product p1 = pm.get(19051);
		ProductItem pm1 = new ProductItem(p1.getProductId(), p1.getMemberPrice(), 1);
		productItem.put(p1.getProductId(), pm1);
		
		Product p2 = pm.get(19597);
		ProductItem pm2 = new ProductItem(p2.getProductId(), p2.getMemberPrice(), 1);
		productItem.put(p2.getProductId(), pm2);

		Product p4 = pm.get(19599);
		ProductItem pm4 = new ProductItem(p4.getProductId(), p4.getMemberPrice(), 1);
		productItem.put(p4.getProductId(), pm4);
		
		Product p3 = pm.get(19601);
		ProductItem pm3 = new ProductItem(p3.getProductId(), p3.getMemberPrice(), 1);
		productItem.put(p3.getProductId(), pm3);
		
		Product p5 = pm.get(19603);
		ProductItem pm5 = new ProductItem(p5.getProductId(), p5.getMemberPrice(), 1);
		productItem.put(p5.getProductId(), pm5);
		
		Product p6 = pm.get(19605);
		ProductItem pm6 = new ProductItem(p6.getProductId(), p6.getMemberPrice(),1);
		productItem.put(p6.getProductId(), pm6);
		
		
		Product p7 = pm.get(19607);
		ProductItem pm7 = new ProductItem(p7.getProductId(), p7.getMemberPrice(),1);
		productItem.put(p7.getProductId(), pm7);
		
		Product p8 = pm.get(20413);
		ProductItem pm8 = new ProductItem(p8.getProductId(), p8.getMemberPrice(),1);
		productItem.put(p8.getProductId(), pm8);
		
		Product p9 = pm.get(20414);
		ProductItem pm9 = new ProductItem(p9.getProductId(), p9.getMemberPrice(),1);
		productItem.put(p9.getProductId(), pm9);
		
		Product p10 = pm.get(20415);
		ProductItem pm10 = new ProductItem(p10.getProductId(), p10.getMemberPrice(),1);
		productItem.put(p10.getProductId(), pm10);
		
		Product p11 = pm.get(20416);
		ProductItem pm11 = new ProductItem(p11.getProductId(), p11.getMemberPrice(), 1);
		productItem.put(p11.getProductId(), pm11);
		
		Product p12 = pm.get(20417);
		ProductItem pm12 = new ProductItem(p12.getProductId(), p12.getMemberPrice(), 1);
		productItem.put(p12.getProductId(), pm12);

		Product p14 = pm.get(21308);
		ProductItem pm14 = new ProductItem(p14.getProductId(), p14.getMemberPrice(), 1);
		productItem.put(p14.getProductId(), pm14);
		
		Product p13 = pm.get(21311);
		ProductItem pm13 = new ProductItem(p13.getProductId(), p13.getMemberPrice(), 1);
		productItem.put(p13.getProductId(), pm13);
		
		Product p15 = pm.get(21312);
		ProductItem pm15 = new ProductItem(p15.getProductId(), p15.getMemberPrice(), 1);
		productItem.put(p15.getProductId(), pm15);
		
		Product p16 = pm.get(21315);
		ProductItem pm16 = new ProductItem(p16.getProductId(), p16.getMemberPrice(), 1);
		productItem.put(p16.getProductId(), pm16);
		
		
		Product p17 = pm.get(21316);
		ProductItem pm17 = new ProductItem(p17.getProductId(), p17.getMemberPrice(),1);
		productItem.put(p17.getProductId(), pm17);
		
		Product p18 = pm.get(22434);
		ProductItem pm18 = new ProductItem(p18.getProductId(), p18.getMemberPrice(),1);
		productItem.put(p18.getProductId(), pm18);
		
		Product p19 = pm.get(22582);
		ProductItem pm19 = new ProductItem(p19.getProductId(), p19.getMemberPrice(),1);
		productItem.put(p19.getProductId(), pm19);
		
		
		

		actityGoods.setProductItem(productItem);
		// 购买产品需要订到2个
		actityGoods.setQuantity(3);
		context.setActityGoods(actityGoods);

		// 产品赠送规则
		// 18850 21173 18852 18854 喷雾
		ProductRule actityExtra = new ProductRule();
		Map<Integer, ProductItem> map_item = new HashMap<>();

		Product pp1 = pm.get(19531);
		ProductItem pitem1 = new ProductItem(pp1.getProductId(), 0, 1);
		map_item.put(pp1.getProductId(), pitem1);

		Product pp3 = pm.get(20409);
		ProductItem pitem3 = new ProductItem(pp3.getProductId(), 0, 1);
		map_item.put(pp3.getProductId(), pitem3);
		
		Product pp2 = pm.get(20412);
		ProductItem pitem2 = new ProductItem(pp2.getProductId(), 0, 1);
		map_item.put(pp2.getProductId(), pitem2);

		actityExtra.setProductItem(map_item);
		// 赠送数量1个
		actityExtra.setQuantity(1);
		context.setZones(null);
		context.setCounters(null);
		context.setActityExtra(actityExtra);
		activity.setStatus(1);
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	public void act03() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("超值大礼包赠送");
		activity.setDescription("订货满1万，赠以下大礼包5套：【年中庆定制化妆包、年中庆定制便携包包、中样2个、野玫瑰旅行3件套】 ");
		

		
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
		
		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(22875,21760,21402,21403,22934,22935,19513,19509)){
			ProductItem item = new ProductItem(pid, 0, 1);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(10);
		
		
		ProductRule prule1 = new ProductRule();
		prule1.setSelected(true);
		int qty = 0;
		for (Integer pid :Arrays.asList(22952,22953,22031)){
			ProductItem item = new ProductItem(pid, 0, 5);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		prule1.setQuantity(qty);
		
		actityExtras.add(prule2);
		actityExtras.add(prule1);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	public void act04() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("测试三生三世的面膜单片买30送10个 ");
		activity.setDescription("测试三生三世的面膜单片买30送10个 ");
		
		
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
		
		for (Integer pid :Arrays.asList(21394,21396,21129,21123,21125,
				21127)){
			ProductItem item = new ProductItem(pid, 0, 30);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(30);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(21394,21396,21129,21123,21125,
				21127)){
			ProductItem item = new ProductItem(pid, 0, 10);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(10);
		actityExtras.add(prule2);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	
	public void act05() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("年中庆超值订货1赠1");
		activity.setDescription("喷雾订1送1 ");
		

		
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
		
		for (Integer pid :Arrays.asList(18850,18852,18854)){
			ProductItem item = new ProductItem(pid, 0, 3);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(1);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(18850,18852,18854)){
			ProductItem item = new ProductItem(pid, 0, 3);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(1);
		actityExtras.add(prule2);

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	public void act06() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("【年中庆】超值订货三生三世面膜订2赠1（片）");
		activity.setDescription("三生三世面膜订2赠1 ");
		

		
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
		//,21053,21056,21075,21084,21086,21088
		for (Integer pid :Arrays.asList(21123,21125,21127,21129,21394,21396)){
			ProductItem item = new ProductItem(pid, 0, 40);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(2);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		//,21053,21056,21075,21084,21086,21088
		for (Integer pid :Arrays.asList(21123,21125,21127,21129,21394,21396)){
			ProductItem item = new ProductItem(pid, 0, 20);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(1);
		actityExtras.add(prule2);

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	public void act07() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("年中庆超值订货三生三世面膜订2赠1");
		activity.setDescription("三生三世面膜订2赠1 ");
		

		
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
		//, 21123,21125,21127,21129,21394,21396
		for (Integer pid :Arrays.asList(21053,21056,21075,21084,21086,21088)){
			ProductItem item = new ProductItem(pid, 0, 4);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(2);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		//,21053,21056,21075,21084,21086,21088
		for (Integer pid :Arrays.asList(21053,21056,21075,21084,21086,21088)){
			ProductItem item = new ProductItem(pid, 0, 2);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(1);
		actityExtras.add(prule2);

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	public void act08() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("年中庆超值订货2赠1");
		activity.setDescription("山茶花系列订2赠1 ");
		

		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		
		activity.setStatus(0);
		activity.setType("01");

		// 活动内容具体描述
		ActivityContext context = new ActivityContext();

		
		//产品购买规则
		ProductRule good1 = new ProductRule();
		
		for (Integer pid :Arrays.asList(18556,19071,19161,19162,19256,19880)){
			ProductItem item = new ProductItem(pid, 0, 6);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(2);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(18556,19071,19161,19162,19256,19880)){
			ProductItem item = new ProductItem(pid, 0, 1);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(1);
		actityExtras.add(prule2);

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	public void act09() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("年中庆超值订货2赠1");
		activity.setDescription("敷眼水，眼霜订2赠1（除十全眼霜，肌底液外） ");
		

		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		
		activity.setStatus(0);
		activity.setType("01");

		// 活动内容具体描述
		ActivityContext context = new ActivityContext();

		
		//产品购买规则
		ProductRule good1 = new ProductRule();
		
		for (Integer pid :Arrays.asList(3581,4423,17759,17761,17763,18211,18213,18336,19607)){
			ProductItem item = new ProductItem(pid, 0, 6);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(2);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(3581,4423,17759,17761,17763,18211,18213,18336,19607)){
			ProductItem item = new ProductItem(pid, 0, 1);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(1);
		actityExtras.add(prule2);

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	public void act10() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("年中庆超值订货2赠1");
		activity.setDescription("山茶花系列订2赠1 ");
		

		
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
		
		for (Integer pid :Arrays.asList(18556,19071,19161,19162,19256,19880)){
			ProductItem item = new ProductItem(pid, 0, 4);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(2);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(18556,19071,19161,19162,19256,19880)){
			ProductItem item = new ProductItem(pid, 0, 2);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(1);
		actityExtras.add(prule2);

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	//-------------------------------------彩妆-------------------------------------------------
	public void act11() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("超值大礼包赠送(白茶)");
		activity.setDescription("订货满1万，赠以下大礼包5套：【年中庆定制化妆包、年中庆定制便携包包、中样2个、野玫瑰旅行3件套】 ");
		

		
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
		for (Integer pid :Arrays.asList(22952,22953,22031,22934,22935)){
			ProductItem item = new ProductItem(pid, 0, 5);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		prule1.setQuantity(qty);
		
		actityExtras.add(prule1);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	public void act12() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("超值大礼包赠送(滇青瓜)");
		activity.setDescription("订货满1万，赠以下大礼包5套：【年中庆定制化妆包、年中庆定制便携包包、中样2个、野玫瑰旅行3件套】 ");
		

		
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
		for (Integer pid :Arrays.asList(22952,22953,22031,19509,19513)){
			ProductItem item = new ProductItem(pid, 0, 5);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		prule1.setQuantity(qty);
		
		actityExtras.add(prule1);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	public void act13() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("超值大礼包赠送(野玫瑰)");
		activity.setDescription("订货满1万，赠以下大礼包5套：【年中庆定制化妆包、年中庆定制便携包包、中样2个、野玫瑰旅行3件套】 ");
		

		
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
		for (Integer pid :Arrays.asList(22952,22953,22031,21760,22875)){
			ProductItem item = new ProductItem(pid, 0, 5);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		prule1.setQuantity(qty);
		
		actityExtras.add(prule1);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	
	//-------------------------------------彩妆-----------------------------------------

	
	
	public void act04_1() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("测试三生三世的面盒装买3送1 ");
		activity.setDescription("测试三生三世的面膜单片买3送1 ");

		
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
		
		for (Integer pid :Arrays.asList(21053,21056,21075,21084,21086,21088)){
			ProductItem item = new ProductItem(pid, 0, 3);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(3);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(21053,21056,21075,21084,21086,21088)){
			ProductItem item = new ProductItem(pid, 0, 1);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(1);
		actityExtras.add(prule2);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	
	
	
	public void act04_2() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("测试晚安面膜买9送3 ");
		activity.setDescription("测试晚安面膜买9送3");

		
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

		for (Integer pid :Arrays.asList(16714,16776,16732)){
			ProductItem item = new ProductItem(pid, 0, 9);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(3);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(16714,16776,16732)){
			ProductItem item = new ProductItem(pid, 0, 3);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(1);
		actityExtras.add(prule2);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	
	public void act04_3() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("测试原液的面膜单片买30送10个 ");
		activity.setDescription("测试原液的面膜单片买30送10个 ");
		
		
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

		for (Integer pid :Arrays.asList(19965,19963,19967)){
			ProductItem item = new ProductItem(pid, 0, 30);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(30);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(19965,19963,19967)){
			ProductItem item = new ProductItem(pid, 0, 10);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(10);
		actityExtras.add(prule2);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	
	public void act04_4() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("测试原液面膜盒装买3送1 ");
		activity.setDescription("测试原液面膜盒装买3送1");

		
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
		
		for (Integer pid :Arrays.asList(19536,19502,19658)){
			ProductItem item = new ProductItem(pid, 0, 3);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(3);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(19536,19502,19658)){
			ProductItem item = new ProductItem(pid, 0, 1);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(1);
		actityExtras.add(prule2);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	public void act04_5() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("测试天丝单片买30送10个 ");
		activity.setDescription("测试天丝面膜单片买30送10个 ");
		
		
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
		for (Integer pid :Arrays.asList(20771,20865)){
			ProductItem item = new ProductItem(pid, 0, 30);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(30);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(20771,20865)){
			ProductItem item = new ProductItem(pid, 0, 10);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(10);
		actityExtras.add(prule2);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	
	public void act04_6() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("测试天丝面膜盒装买3送1 ");
		activity.setDescription("测试天丝面膜盒装买3送1");

		
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

		
		for (Integer pid :Arrays.asList(19581,17561,17571,17569,19145,19147)){
			ProductItem item = new ProductItem(pid, 0, 3);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(3);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(19581,17561,17571,17569,19145,19147)){
			ProductItem item = new ProductItem(pid, 0, 1);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(1);
		actityExtras.add(prule2);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	
	
	public void act04_7() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("测试无纺布盒装3送1 ");
		activity.setDescription("测试无纺布盒装3送1");

		
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
		
		for (Integer pid :Arrays.asList(20939,20989,14416,14418,19581)){
			ProductItem item = new ProductItem(pid, 0, 3);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(3);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(20939,20989,14416,14418,19581)){
			ProductItem item = new ProductItem(pid, 0, 1);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(1);
		actityExtras.add(prule2);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	
	public void act04_8() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("测试眼膜、颈膜盒装3送1 ");
		activity.setDescription("测试眼膜、颈膜盒装3送1");

		
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

		for (Integer pid :Arrays.asList(19793,19803,19878)){
			ProductItem item = new ProductItem(pid, 0, 3);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(3);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(19793,19803,19878)){
			ProductItem item = new ProductItem(pid, 0, 1);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(1);
		actityExtras.add(prule2);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	
	public void act04_9() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("测试眼膜、颈膜买30送10个 ");
		activity.setDescription("测试眼膜、颈膜单片买30送10个 ");
		
		
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
		for (Integer pid :Arrays.asList(19897,19899,19901)){
			ProductItem item = new ProductItem(pid, 0, 30);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(30);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(19897,19899,19901)){
			ProductItem item = new ProductItem(pid, 0, 10);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(10);
		actityExtras.add(prule2);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	
	
	public void act04_10() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("眼部敷眼水、眼霜订4赠2 ");
		activity.setDescription("眼部敷眼水、眼霜订4赠2 ");

		
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

		for (Integer pid :Arrays.asList(17761,17759,17763,18213,18211,18336,19607,4423)){
			ProductItem item = new ProductItem(pid, 0, 4);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(4);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(17761,17759,17763,18213,18211,18336,19607,4423)){
			ProductItem item = new ProductItem(pid, 0, 2);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(2);
		actityExtras.add(prule2);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	
	public void act04_11() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("喷雾系列订3赠3 ");
		activity.setDescription("喷雾系列订3赠3 ");

		
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

		for (Integer pid :Arrays.asList(18850,18854,18852)){
			ProductItem item = new ProductItem(pid, 0, 3);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(3);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(18850,18854,18852)){
			ProductItem item = new ProductItem(pid, 0, 3);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(3);
		actityExtras.add(prule2);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	
	
	public void bigact() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("大类活动 ");
		activity.setDescription("大类活动");

		
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


		for (Integer pid :Arrays.asList(
				21053,	21056,	21075,		21084,	21086,	21088,	19965,	19963,	19967,	19581,
				17561,		17571,		17569,		19145,	19147,	20939,
				20989,		14416,14418)){
			ProductItem item = new ProductItem(pid, 0, 3);
			good1.addProductItem(item);
		}
		
		for (Integer pid :Arrays.asList(16714,16776,16732)){
			ProductItem item = new ProductItem(pid, 0, 9);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到1个
		good1.setQuantity(3);
		context.setActityGoods(good1);
		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();

		ProductRule prule2 = new ProductRule();
		for (Integer pid :Arrays.asList(
				21053,	21056,	21075,		21084,	21086,	21088,	19965,	19963,	19967,	19581,
				17561,		17571,		17569,		19145,	19147,	20939,
				20989,		14416,14418)){
			ProductItem item = new ProductItem(pid, 0, 1);
			prule2.addProductItem(item);
		}
		for (Integer pid :Arrays.asList(16714,16776,16732)){
			ProductItem item = new ProductItem(pid, 0, 3);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(1);
		actityExtras.add(prule2);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	
	public void 大礼包() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("超值大礼包赠送");
		activity.setDescription("订货满1万，赠多功能洗漱包6个【可叠加】 ");
		

		
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
		for (Integer pid :Arrays.asList(23315)){
			ProductItem item = new ProductItem(pid, 0, 6);
			qty += item.getQuantity();
			prule1.addProductItem(item);
		}
		prule1.setQuantity(qty);
		
		actityExtras.add(prule1);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	public void zengHenduo() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("狂欢套装订货");
		activity.setDescription("【套装订货】订白茶系列任意12件，赠喷雾3支+控油祛痘/白茶细致毛孔/三生三世面膜30片");
		
		
		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		
		activity.setStatus(0);
		activity.setType("11");

		
		// 活动内容具体描述
		ActivityContext context = new ActivityContext();

		
		//产品购买规则
		ProductRule good1 = new ProductRule();

		for (Integer pid :Arrays.asList(22582,21308,21311,21312,21315,21316,22434)){
			ProductItem item = new ProductItem(pid, 0, 6);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到4个
		good1.setQuantity(4);
		context.setActityGoods(good1);

		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();
		
		
		ProductRule prule1 = new ProductRule();
		prule1.setSelected(false);
		for (Integer pid :Arrays.asList(18850,18854,18852)){
			ProductItem item = new ProductItem(pid, 0, 1);
			prule1.addProductItem(item);
		}
		prule1.setQuantity(1);
		
		ProductRule prule2 = new ProductRule();
		prule2.setSelected(false);
		for (Integer pid :Arrays.asList(20771,20865,21129,21396,21394)){
			ProductItem item = new ProductItem(pid, 0, 10);
			prule2.addProductItem(item);
		}
		prule2.setQuantity(10);
		
		actityExtras.add(prule1);
		actityExtras.add(prule2);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}
	
	public void zengHenduo1() {
		Activity activity = new Activity();
		
		// 自动扫描为false
		activity.setAutoAlloca(false);
		// 绑定为false
		activity.setBunding(false);
		// 发货方式
		activity.setDutyCode("24");
		activity.setName("Test活动");
		activity.setDescription("测试 ");
		
		
		
		// 创建人id
		activity.setOwnerId("root");
		// 创建人名称
		activity.setOwnerName("root");
		
		activity.setStatus(0);
		activity.setType("01");

		
		// 活动内容具体描述
		ActivityContext context = new ActivityContext();

		
		//产品购买规则
		ProductRule good1 = new ProductRule();

		for (Integer pid :Arrays.asList(22582,21308,21311,21312,21315,21316,22434)){
			ProductItem item = new ProductItem(pid, 0, 6);
			good1.addProductItem(item);
		}
		
		// 购买产品需要订到4个
		good1.setQuantity(4);
		context.setActityGoods(good1);

		
		// 产品赠送规则
		List<ProductRule> actityExtras = context.getActityExtras();
		
		
		ProductRule prule1 = new ProductRule();
		prule1.setSelected(true);
		ProductItem item = new ProductItem(18850, 0, 1);
		prule1.addProductItem(item);
		ProductItem item2 = new ProductItem(20771, 0, 10);
		prule1.addProductItem(item2);
		
/*		for (Integer pid :Arrays.asList(18850,20771)){
			ProductItem item = new ProductItem(pid, 0, 1);
			prule1.addProductItem(item);
		}*/
		prule1.setQuantity(1);		
		
		actityExtras.add(prule1);

		

		// 赠送数量1个
		context.setZones(null);
		context.setCounters(null);
		activity.setStatus(0);
		
		activity.setContext(context);
		
		System.out.println(activity);
		
		activityManager.add(activity);
	}

	
	
	public static void main(String[] args) {
		Activity201706 a = new Activity201706();
		a.init();
		a.zengHenduo();
//		a.zengHenduo1();
	//	a.act01();
//		a.act02();
	//	a.大礼包();
		//String 汉子="hanzi";
		//System.out.println(汉子);
//		a.act04_1();
//		a.act04_2();
//		a.act04_3();
//		a.act04_4();
//		a.act04_5();
//		a.act04_6();
//	a.act04_10();
//		a.act04_8();
//		a.act04_9();
//		a.act04_10();
//		a.act04_11();
//		a.act05();
//		a.act06();
//		a.act07();
//		a.act08();
//      a.act09();
//		a.act10();
//		a.act11();
//		a.act12();
	//	a.act13();
//		a.act14();
//		a.act15();
//		a.act16();
	}
	
	
}
