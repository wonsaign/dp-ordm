package com.zeusas.dp.ordm.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.http.ActionException;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.CartDetailDesc;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Item;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductRelationPolicy;
import com.zeusas.security.auth.entity.AuthUser;


public class CartTest {


	private FileSystemXmlApplicationContext aContext;
	
	OrderService orderService;
	CartService cartService;
	CounterManager counterManager;
	CartDetailService cartDetailService;
	ProductManager pm;
	
	
	
	{
		aContext=new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
		aContext.start();
		orderService=aContext.getBean(OrderService.class);
		cartService=aContext.getBean(CartService.class);
		counterManager=AppContext.getBean(CounterManager.class);
		pm=AppContext.getBean(ProductManager.class);
		cartDetailService=AppContext.getBean(CartDetailService.class);
	}
	
	@Test
	public void test000(){
		List<String>list=new ArrayList<>();
		System.out.println(list.size());
	}
	
	@Test
	public void 测试产品的最小单位的限制(){
		PRPolicyManager prPolicyManager =AppContext.getBean(PRPolicyManager.class);
	  Product 	product = pm.get(19260);
		// FIXME 这里校验数量更加偏向于放在Controller，因为Controller可以让客户知道错误信息
		ProductRelationPolicy relationPolicy= prPolicyManager.get(product);
		int required = relationPolicy.getMinOrderUnit();
		 System.out.println(required);
		 System.err.println(30%3);
	}
	
	
	public void t1() {
		List<Product> list = new ArrayList<Product>();

		Map<String, List<Product>> v = list.parallelStream()//将集合变成一个块状流
				.filter(e -> e.getBarCode() != null || e.getpBrand()==null)//过滤器 if(){}
				.collect(Collectors.groupingBy(Product::getFitemClassId));//将流收集，按getFitemClassId做key写成一个map
	
	}
	
	@Test
	public void ttt1(){
		System.out.println(pm.fingBySerialAndName("11386", "刷"));
	}
	
//	@Test
//	public void 基于门店拿到购物车() throws ServiceException{
//		Counter counter=new Counter();
//		counter.setCounterId(11311);
//		counter.setCounterName("门店测试");
//		Cart cart=cartService.getCounterCart(counter);
//		System.out.println(cart);
//	}
	
	@Test
	public void 基于门店和用户拿到购物车2() throws ServiceException{
		Counter counter=new Counter();
		counter.setCounterId(11311);
		counter.setCounterName("门店测试");
		AuthUser user=new AuthUser();
		user.setLoginName("test");
		user.setCommonName("commonName");
		//Cart cart=cartService.findCounterCart(counter, user);
		//System.out.println(cart);
	}
	
	
	
	@Test
	public void test111() throws ServiceException{
		AuthUser user=new AuthUser();
		user.setLoginName("C03");
		user.setCommonName("测试C03用户购物车");
		Cart cart=cartService.get(1706020002l);
		Product p=pm.get(23263);
		cartService.add(cart, p, 6);
		System.out.println(cart);
	}
	
	
	
	
	
	
	
	// 测试一个产品的添加
	@Test
	public void testAdd() throws ServiceException, ActionException {
		Counter counter=new Counter();
		counter.setCounterId(9206);
		Cart cart = cartService.get(1704250067L);
		Product product = pm.get(21121);
		//47.589
		cartService.add(cart, product, 6);
		
		AuthUser user1=new AuthUser();
		user1.setLoginName("root");
		user1.setCommonName("root_1_1");
		
		AuthUser user2=new AuthUser();
		user2.setLoginName("root");
		user2.setCommonName("root_2");
		//orderService.buildOrderToCart(order);
		//orderService.buildOrderToCart(order);
		//orderService.buildOrderToCart(order);
		
		orderService.buildOrder(cart, counter, user1,user1, 11392);
	}
	
	@Test
	public void 测试订单返回购物车() throws ServiceException, ActionException{
		Order order=	orderService.getsingleOrder("201706200002");
		orderService.buildOrderToCart(order);
		// 通过门店获取购物车
//		Cart cart = cartService.getCounterCart(counter);
//		cart.setOrderId(111111111111l);
//		cartService.update(cart);
	}
	
	@Test
	public void test购物车保存执行的sql次数(){
		CartDetail cartDetail=new CartDetail();
		cartDetail.setDetailId(11111L);
		List<CartDetailDesc>desc=new ArrayList<>();
		 CartDetailDesc desc1=new CartDetailDesc();
		 desc1.setCartDetailId(11111L);
		desc.add(desc1);
		cartDetail.setCartDetailsDesc(desc);
		cartDetailService.save(cartDetail);
	}
	
	@Test
	public void buildOrder() throws ServiceException, ActionException{
		AuthUser user1=new AuthUser();
		user1.setLoginName("root");
		user1.setCommonName("root_1_1");
		
		AuthUser user2=new AuthUser();
		user2.setLoginName("root");
		user2.setCommonName("root_2");
		Order order=	orderService.getsingleOrder("201705100001");
		orderService.buildOrderToCart(order);
		//orderService.buildOrderToCart(order);
		//orderService.buildOrderToCart(order);
		Counter counter = counterManager.get(order.getCounterId());
		Cart cart = cartService.getCounterCart(counter);
		
		orderService.buildOrder(cart, counter, user1,user1, 11392);
	}
	
	@Test
	public void test01() throws ServiceException, ActionException{
		
   Order order=	orderService.getsingleOrder("201705100001");
   
   
   List<OrderDetail>orderDetails=orderService.getOrderDetails(order.getOrderNo());
   System.err.println(orderDetails.size());
   
   Map<Long, List<OrderDetail>>map_orderdetail=new HashMap<>();
   
   
     for(OrderDetail orderDetail:orderDetails){
    	 List<OrderDetail> ods= map_orderdetail.get(orderDetail.getPid());
    	 if(ods==null){
    		 ods=new ArrayList<>();
    		 map_orderdetail.put(orderDetail.getPid(), ods);
    	 }
    	 ods.add(orderDetail);
     }
System.out.println(map_orderdetail.keySet().size());
Counter counter=counterManager.get(order.getCounterId());
Cart cart=cartService.getCounterCart(counter);
  for(Long key:map_orderdetail.keySet()){
	  List<OrderDetail> ods= map_orderdetail.get(key);
	  if(ods.size()==1){
		  cartService.add(cart, pm.get(ods.get(0).getProductId()), ods.get(0).getQuantity());
	  }
	  if(ods.size()>1){
		  
		  
		  
		//  cartService.add(cartId, actId, items, num);
	  }
	  AuthUser user1=new AuthUser();
		user1.setLoginName("root");
		user1.setCommonName("root_1_1");
		
		AuthUser user2=new AuthUser();
		user2.setLoginName("root");
		user2.setCommonName("root_2");
	  //orderService.buildOrder(cart, counter, user1, user1, 11387);
  }
        

	}
	
	
	@Test
	public void test批量Add() throws ServiceException {
		Cart cart = new Cart();
		cart.setCartId(1701190001L);
		List<Item>items=new ArrayList<>();
//		683
//		689
//		694
//		697
//		700
//		707
		Item item=new Item();
		item.setId("683");
		item.setNum(5);
		items.add(item);
		Item item1=new Item();
		item1.setId("689");
		item1.setNum(5);
		items.add(item1);
		cartService.add(cart, items);
	}
	
	@Test
	public void tt1111() throws ServiceException {
		Product product = pm.get(1735);
		System.out.println(product.getMemberPrice()*6);
		//47.589
	}
	
	
//	*//**
//	 * 测试一个活动组在购物车的具体实现
//	 * @throws ServiceException
//	 *//*
@Test
	public void  addGroupProduct() throws ServiceException{
		//购买产品id为1的10件
		Item item1=new Item(Item.TYPE_PAY, String.valueOf(1731),12);
		Item item3=new Item(Item.TYPE_PAY, String.valueOf(1739), 6);
		Item item4=new Item(Item.TYPE_PAY, String.valueOf(1751), 6);
		Item item5=new Item(Item.TYPE_FREE, String.valueOf(1755), 5);
		List<Item>items=new ArrayList<>();
		items.add(item1);
		items.add(item3);
		items.add(item4);
	  items.add(item5);
		
		
		AuthUser user=new AuthUser();
		user.setLoginName("C03");
	//	Cart cart=cartService.getMyCart(user);
		cartService.add(1702080001L, "17040701", null,8);
		
	}
	
//	*//**
//	 * 测试拿到一个活动在一个购物车的多次体现
//	 * @throws ServiceException
//	 *//*
//@Test
//	public void teetst() throws ServiceException
//	{
//		AuthUser user=new AuthUser();
//		user.setLoginName("C03");
//		Cart cart=null;
//		
//		
//	   	//购买产品id为1的10件
//		Item item1=new Item(Item.TYPE_PAY, String.valueOf(1713), 18);
//		Item item3=new Item(Item.TYPE_FREE, String.valueOf(2901), 2);
//		Item item4=new Item(Item.TYPE_FREE, String.valueOf(2915), 2);
//		Item item5=new Item(Item.TYPE_FREE, String.valueOf(2921), 2);
//				List<Item>items=new ArrayList<>();
//				items.add(item1);
//				items.add(item3);
//				items.add(item4);
//				items.add(item5);
//				
//		
//		Integer detailed=cartService.findItems("1001", 1701180001L, null);
//		System.out.println(detailed);
//		
//	}
//
//	@Test
//		public void teetst2() throws ServiceException
//		{
//			AuthUser user=new AuthUser();
//			user.setLoginName("root");
//		   	//购买产品id为1的10件
//			Item item1=new Item(Item.TYPE_PAY, String.valueOf(1755), 18);
//			Item item3=new Item(Item.TYPE_FREE, String.valueOf(1763), 2);
//			Item item4=new Item(Item.TYPE_FREE, String.valueOf(2011), 2);
//					List<Item>items=new ArrayList<>();
//					items.add(item1);
//					items.add(item3);
//					items.add(item4);
//			Integer detailed=cartService.findItems("12290025", 1612280001l, items);
//			System.out.println(detailed);
//		}
//	
//	
//	@Test
//	public void teetst2_1() throws ServiceException
//	{
//		AuthUser user=new AuthUser();
//		user.setLoginName("C03");
//	   	//购买产品id为1的10件
//		Item item1=new Item(Item.TYPE_PAY, String.valueOf(1755), 18);
//		Item item3=new Item(Item.TYPE_FREE, String.valueOf(1763), 2);
//		Item item4=new Item(Item.TYPE_FREE, String.valueOf(2011), 2);
//				List<Item>items=new ArrayList<>();
//				items.add(item1);
//				items.add(item3);
//				items.add(item4);
//		cartService.add(1612280001l, "12290025", items, 10);
//	}
//	
//	
//	
//	@Test
//	public void 测试购物车的真实价格以及费用比() throws ServiceException{
//		AuthUser user=new AuthUser();
//		user.setLoginName("C03");
//		user.setCommonName("测试C03用户购物车");
//		Cart cart=null;//cartService.getMyCart(user);
//	//double p=	cartService.getRealPrice(cart, 0.5);//461380.0
//	//System.out.println(p);
//	//System.out.println("费比:"+cartService.getPresentFee(cart));
////	System.out.println("价格与费用比"+cartService.compareFeeAndPay(cart, 0.5));
//	}
//	
//	
//	//@Test
//	public void ttt() throws ServiceException
//	{
//		AuthUser user=new AuthUser();
//		user.setLoginName("C01");
//		String cond = "WHERE userId=? AND status=0 ORDER BY lastUpdate DESC";
//		cartService.find(cond, user.getLoginName());
//		System.out.println(cartService.find(cond, user.getLoginName()).size());
//	}
//	
//	//@Test
//	public void 测试集合对象是否相同()
//	{
//		Item item1=new Item(Item.TYPE_PAY, String.valueOf(1), 10);
//		//购买产品id为11的10件
//		Item item2=new Item(Item.TYPE_FREE, String.valueOf(683), 10);
//		Item item3=new Item(Item.TYPE_FREE, String.valueOf(683), 10);
//		Item item4=new Item(Item.TYPE_FREE, String.valueOf(684), 10);
//		Item item5=new Item(Item.TYPE_FREE, String.valueOf(685), 10);
//		List<Item>items1=new ArrayList<>();
//		items1.add(item1);
//		items1.add(item2);
//		items1.add(item3);
//		items1.add(item4);
//		items1.add(item5);
//		
//		//购买产品id为11的10件
//		Item item3_1=new Item(Item.TYPE_FREE, String.valueOf(683), 10);
//		Item item4_1=new Item(Item.TYPE_FREE, String.valueOf(684), 10);
//		List<Item>items2=new ArrayList<>();
//		items2.add(item1);
//		items2.add(item3_1);
//		items2.add(item2);
//		items2.add(item4_1);
//		items2.add(item5);
//		
//		System.out.println(items1.equals(items2));
//		
//	}
//	
//	@Test
//	public void 测试in() {
//		List<Integer> detailIds = new ArrayList<>();
//		detailIds.add(1701030012);
//		detailIds.add(1701040020);
//		detailIds.add(1701030003);
//		Map<Integer, CartDetailDesc> map_desc = new LinkedHashMap<Integer, CartDetailDesc>();
//		try {
//			List<CartDetailDesc> descs = cartService.getCartDescByCartDetail(detailIds);
//			for (CartDetailDesc desc : descs) {
//				if (!map_desc.keySet().contains(desc.getCartDetailId())) {
//					map_desc.put(desc.getCartDetailId(), desc);
//				}
//			}
//		System.out.println(descs);
//		System.out.println(map_desc);
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void 测试多个门店购物车获取() throws ServiceException{
//		List<Integer> counterIds = new ArrayList<>();
//		counterIds.add(20866);
//		counterIds.add(20865);
//		counterIds.add(20868);
//		List<Cart>carts=cartService.getCheckCarts(counterIds);
//		for(Cart cart :carts){
//			System.out.println(cart);
//		}
//	}
//	
//	@Test
//	public void 测试多个门店购物车获取2() throws ServiceException{
//		List<Integer> counterIds = new ArrayList<>();
//		counterIds.add(20866);
//		counterIds.add(16160);
//		counterIds.add(20868);
//		AuthUser user=new AuthUser();
//		user.setLoginName("C01");
//		List<Cart>carts=cartService.findAllCart(counterIds);
//		System.out.println(carts);
//	}
//	
//	@Test
//	public void 门店购物车(){
//		Counter counter1=new Counter();
//		counter1.setCounterId(16160);
//		counter1.setCounterName("111");
//		System.out.println("1"+counter1);
//		
//		Counter counter2=new Counter();
//		counter2.setCounterId(222);
//		counter2.setCounterName("222");
//		
//		Counter counter3=new Counter();
//		counter3.setCounterId(333);
//		counter3.setCounterName("333");
//		List<Counter>counters=new ArrayList<Counter>();
//		counters.add(counter1);
//		counters.add(counter2);
//		counters.add(counter3);
//		
////		CartBean cartBean=new CartBean();
////		List<Cart> carts=new ArrayList<>();
////		Cart cart=new Cart();
////		cart.setStatus(Cart.Status_OtherLock);
////		cart.setCounterId(16160);
////		carts.add(cart);
////		List<CartBean>cartBeans=(cartBean.getCartBeans(carts, counters));
////		for(CartBean cBean:cartBeans){
////			System.out.println(cBean);
////		}
//	}
//	
//	
//	@Test
//	public void testDouble(){
//		double d1=56.30;
//		double d2=2.21;
//		System.out.println(d1);
//		DecimalFormat format=new DecimalFormat("#0.00");
//		System.out.println(format.format(d1-d2));
//	}
//	
//	@Test
//	public void 动态拿到购物车里面的物料比() throws ServiceException{
//		Counter counter=new Counter();
//		counter.setCounterId(9213);
//		Cart cart = cartService.getCounterCart(counter);
//		int customerTypeId=11391;
//	  double d=	cartService.getMaterialFee(cart, customerTypeId);
//	 System.out.println(d);
//	}
//	
//	
//	@Test
//	public void 测试邮费map() throws ServiceException{
//	Map<Integer,Double>map=	cartService.getPostage();
//	for(Integer i:map.keySet()){
//		System.out.println("key:"+i+"---"+"value:"+map.get(i));
//	}
//	}
//	
////	@Test
////	public  void 测试购物车所有的物料() throws ServiceException{
////		Counter counter=new Counter();
////		counter.setCounterId(15292);
////		Cart cart = cartService.getCounterCart(counter);
////		List<CartDetail>cartDetails=cartService.getCartDetailByCart(cart);
////		List<Integer>cartdetailIds=new ArrayList<>();
////		for(CartDetail cartDetail:cartDetails){
////			cartdetailIds.add(cartDetail.getDetailId());
////		}
////	
//////	Map<Product,Integer>map_sort_product=cartService.
//////			getSortMaterial( cartDetails,cartService.getEveryCartDescByCartDetail(cartdetailIds));
////// 	
//////   double  now=0.0;
//////   for(Product p:map_sort_product.keySet()){
//////	   now+=p.getMaterialPrice()*map_sort_product.get(p);
//////   }
//////   
////   
//////	double peisongbi = Double.parseDouble(UserResource.get(UserResource.MATERIALDISCOUNT));
////   double peisongbi = Double.parseDouble("0.07");
////	double realPrice = cartService.getRealPrice(cartDetails, 0.4) * peisongbi;
////	
////	
////	System.out.println("可配送的物料"+realPrice);
////   
////	System.err.println("现在的物料价格"+now+"----------");
////	
////	System.err.println("???????"+(now-realPrice));
////   
//// double materialFee=  cartService.getMaterialFee(cart, 11391);
//// System.out.println("物料费用差值"+materialFee);
////
//// 
////           for(Product p:map_sort_product.keySet()){
////        	   double  materialPrice=p.getMaterialPrice()*map_sort_product.get(p);;
////        	   realPrice=realPrice-materialPrice;
////        	   System.out.println(realPrice+"!!!"+map_sort_product.get(p));
////        	   if(realPrice<0){
////        		   //如果金额小于0 的话就说明超出了物料的配比 、就取最大的
////        		   realPrice=realPrice+materialPrice;
////        		   int give_num=(int) (realPrice/p.getMaterialPrice());
////        		   System.err.println("最大赠送数量"+give_num);
////        		   System.out.println("需要购买的数量是"+(map_sort_product.get(p)-give_num));
////        		   break;
////        	   }
////           }
////           
////           
////           
////           
////           double   p1_free=0.0;
////           double   p1_fee=0.0;
////           
////           for(Product p:map_sort_product.keySet()){
////        	   double  materialPrice=p.getMaterialPrice()*map_sort_product.get(p);;
////        	   realPrice=realPrice-materialPrice;
////        	   System.out.println(realPrice+"!!!"+map_sort_product.get(p));
////        	   if(realPrice<0){
////        		   //如果金额小于0 的话就说明超出了物料的配比 、就取最大的
////        		   realPrice=realPrice+materialPrice;
////        		   int give_num=(int) (realPrice/p.getMaterialPrice());
////        		   System.err.println("最大赠送数量"+give_num);
////        		   p1_free+=give_num*p.getMaterialPrice();
////        		   System.out.println("需要购买的数量是"+(map_sort_product.get(p)-give_num));
////        		   map_sort_product.put(p, map_sort_product.get(p)-give_num);
////        		   break;
////        	   }
////        	   map_sort_product.put(p, 0);
////        	   p1_free+=materialPrice;
////           }
////           
////           for(Product p:map_sort_product.keySet()){
////        	   p1_fee+=p.getMaterialPrice()*map_sort_product.get(p);;
////           }
////           
////           System.out.println("课配送比"+p1_free);
////           System.out.println("收费送比"+p1_fee);
//// 
//// 
////   
////	}
//
////	@Test
////	public  void 有的物料2() throws ServiceException{
////		Counter counter=new Counter();
////		counter.setCounterId(9213);
////		Cart cart = cartService.getCounterCart(counter);
////		System.out.println(cart);
////		List<CartDetail>cartDetails=cartService.getCartDetailByCart(cart);
////		List<Integer>cartdetailIds=new ArrayList<>();
////		for(CartDetail cartDetail:cartDetails){
////			cartdetailIds.add(cartDetail.getDetailId());
////		}
////		System.out.println(cartdetailIds==null);
////		System.err.println(cartdetailIds.size());
////		System.out.println(cartDetails);
////		cartService.getEveryCartDescByCartDetail(cartdetailIds);
//////           Map<String,Object>map_material=cartService.
//////        		   getMaterialFee(cartDetails, cartService.getEveryCartDescByCartDetail(cartdetailIds), 1.0);
////	}
//	
//	
//	
//	@Test
//	public  void 测试购物车所有的物料2() throws ServiceException{
//		Counter counter=new Counter();
//		counter.setCounterId(9213);
//		Cart cart = cartService.getCounterCart(counter);
//		List<CartDetail>cartDetails=cartService.getCartDetailByCart(cart);
//		System.err.println(cartDetails);
//		System.err.println(cart);
//		List<Integer>cartdetailIds=new ArrayList<>();
//		for(CartDetail cartDetail:cartDetails){
//			cartdetailIds.add(cartDetail.getDetailId());
//		}
//		System.err.println(cartdetailIds);
//           Map<String,Object>map_material=cartService.
//        		   getMaterialFee(cartDetails, cartService.getEveryCartDescByCartDetail(cartdetailIds), 0.4);
//           
//           System.out.println("可配送比"+map_material.get("materialFree"));
//           System.out.println("收费费用"+map_material.get("materialFee"));
//           
//           List<PackageDetail>packageDetails=(List<PackageDetail>) map_material.get("packageDetails");
//           System.out.println("----------------------------------");
//           for(PackageDetail pd:packageDetails){
//        	//   System.out.println(pd.getProductId()+"---"+pd.getUnitPrice()+"---"+pd.getProductQty());
//        	   System.out.println(pd);
//           }
//           System.out.println("----------------------------------");
//	}
//
//	
//	@Test
//	public  void 测试购物车所有的物料2新方法() throws ServiceException{
//		Counter counter=new Counter();
//		counter.setCounterId(9213);
//		Cart cart = cartService.getCounterCart(counter);
//		List<CartDetail>cartDetails=cartService.getCartDetailByCart(cart);
//		System.err.println(cartDetails);
//		System.err.println(cart);
//		List<Integer>cartdetailIds=new ArrayList<>();
//		for(CartDetail cartDetail:cartDetails){
//			cartdetailIds.add(cartDetail.getDetailId());
//		}
//		System.err.println(cartdetailIds);
//           Map<String,Object>map_material=cartService.
//        		   getMaterialFee(cartDetails, cartService.getEveryCartDescByCartDetail(cartdetailIds), 0.4);
//           
//	}
//	
//	
//	
//	
//	@Test
//	public void testAdd2() throws ServiceException {
//		AuthUser user = new AuthUser();
//		user.setLoginName("C03");
//		Counter counter=new Counter();
//		counter.setCounterId(15292);
//		Cart cart = cartService.getCounterCart(counter);
//		Product product = productManager.get(14418);
//		System.out.println(product.getMemberPrice()*60);
//		//47.589
//		cartService.add(cart, product, 6);
//	}
//	
//	@Test
//	public void test11(){
//		Map<Product, Integer>map_product=new HashMap<>();
//		map_product.put(productManager.get(800), 5);
//		map_product.put(productManager.get(800), 10);
//		System.out.println(map_product.get(productManager.get(4438)));
//	}
//	
//	@Test
//	public void 测试字典() throws ServiceException{
//		Map<Integer, Double> postage=cartService.getPostage();
//		System.out.println(postage);
//	}
//	
	@Test
	public void testgetMaterialFee() throws ServiceException, ActionException{
		Cart cart=cartService.get(1704250034L);
		System.out.println(cartService.getMaterialFee(cart, 11387));
	}
	
	@Test
	public void 正品费比() throws ServiceException, ActionException{
		Cart cart=cartService.get(1702120002L);
		List<CartDetail> cartDetails=cartService.getCartDetailByCart(cart);
		cartService.getMaterialFee(cartDetails, 0.4);
//		System.out.println(cartService.getSupportdistributionPrice(cartDetails, 0.4)*0.06);
	}
//	
//	
//	
//	@Test
//	public void testMap(){
//		List<String>l=new LinkedList();
//		l.add("s");
//		System.out.println(l);
//		l.remove("s");
//		System.out.println(l);
//		
//	}
//	
//	@Test
//	public void yyyy(){
//		DictManager dictManager = AppContext.getBean(DictManager.class);//2.1
//		Dictionary dictionary =dictManager.lookUpByCode("206", "01");//2.1
//		System.out.println(dictionary);
////		Dictionary dict_materialdiscount = dictManager.lookUpByCode(OrdmConfig.ORDMCONFIG, UserResource.MATERIALDISCOUNT);
////		System.err.println(dict_materialdiscount);
//	}
	
}
