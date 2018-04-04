package com.zeusas.dp.ordm.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.common.service.IdGenService;
import com.zeusas.core.http.ActionException;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.AliPayment;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.entity.PackageDetail;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.security.auth.entity.AuthUser;

public class OrderTest {
	
	private OrderService orderService;
	private FileSystemXmlApplicationContext aContext;
	private IdGenService idGenService;
	private CartService cartService;
	private PackageService packageService;
	public final static String ID_ORDERNO="ORDERNO";

	{
		aContext=new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
		aContext.start();
		cartService=aContext.getBean(CartService.class);
		orderService=aContext.getBean(OrderService.class);
		idGenService=aContext.getBean(IdGenService.class);
		packageService=aContext.getBean(PackageService.class);
	}
	@Test
	public void tttt111(){
		CounterManager manager =AppContext.getBean(CounterManager.class);
		Order order=orderService.getsingleOrder("201702130004");
		
		System.err.println(order.getCounterId());
	Counter counter=	manager.get(order.getCounterId());
	System.out.println(counter.getType());
	}
	
	
	
	@Test
	public void test查询订单(){
		List<Integer>counterIds=new ArrayList<>();
		Conditions condition=new Conditions(counterIds,Order.status_WaitShip);
		//condition.setStart(11111l);
		//condition.setEnd(11111111111111l);
	List<Order>o1s=	orderService.findOrders(condition);
	for(Order o:o1s){
		//o.setAddress(o.getAddress()+"--");
		System.out.println(o);
		//orderService.update(o);
	}
	System.out.println(o1s.size());
	List<Integer>list=new ArrayList<>();
	list.add(4);
	list.add(3);
	list.add(2);
	list.add(1);
	list.add(0);
	list.remove(Integer.valueOf(3));
	System.out.println(list);
	}
	
	
	
	
	
	
	//@Test
	public void test01() throws ServiceException
	{
		String id=idGenService.generateDateId("ORDERID");
		String orderNo=idGenService.generateDateId(ID_ORDERNO);
		System.out.println(orderNo);
	}
	
@Test
	public void test02() throws ServiceException, ActionException{
		AuthUser user1=new AuthUser();
		user1.setLoginName("root");
		user1.setCommonName("root_1_1");
		
		AuthUser user2=new AuthUser();
		user2.setLoginName("root");
		user2.setCommonName("root_2");
		Counter counter=new Counter();
		counter.setCounterId(9214);
		Cart cart=cartService.getCounterCart(counter);
		counter.setAddress("北京西直门外大街");
		counter.setCounterId(1001);
		counter.setCounterName("门店名字1");
		counter.setCounterCode("code11111");
		orderService.buildOrder(cart, counter, user1,user1, 11392);
	}


@Test
public void test03() throws ServiceException{
	AuthUser user=new AuthUser();
	user.setLoginName("C03");
	//orderService.buildOrderDetails(cart, "s", 0.52);
}


//@Test
public void test通过订单号拿到单头() throws ServiceException{
	Order order=orderService.getsingleOrder("201612240002");
	System.out.println(order);
}

//@Test
public void test确认付款() throws ServiceException{
	AuthUser user=new AuthUser();
	user.setLoginName("财务");
	user.setCommonName("测试财务付款");
	orderService.paySuccess("201612240004", user, Arrays.asList(""));
}
//@Test
public  void test确认订单() throws ServiceException{
	orderService.completeOrder("201612240004");
}


//@Test
public void 通过OrderNo拿到明细() throws ServiceException
{
	AuthUser user=new AuthUser();
	user.setLoginName("C03");
	List<OrderDetail>orderDetails=orderService.getOrderDetails("201612200002");
	for(OrderDetail orderDetail:orderDetails){
		System.out.println(orderDetail);
	}
}

////@Test
//public void  测试门店主管拿到某个类型的订单() throws ServiceException
//{
//	List<Order>orders=orderService.getOrders("U0012", "1");
//	System.out.println(orders.size());
//	for(Order order:orders){
//		System.out.println(orderService.getOrderDetails(order.getOrderNo()));
//	}
//}


@Test
public void  测试某个类型的订单() throws ServiceException
{
	List<Order>orders=orderService.getOrders("1");
	//System.out.println(orders.size());
	System.out.println(orders);
	for(Order order:orders){
		System.out.println(orderService.getOrderDetails(order.getOrderNo()));
	}
}

//@Test
public void 拿到某个购物车里面的所有的赠品以及物料的费用() throws ServiceException{
	AuthUser user=new AuthUser();
	user.setLoginName("C04");
	//double fee=orderService.getPresentFee(cart);
	//System.out.println(fee);
}

@Test
public void 拿到所有提交的购物车() throws ServiceException{
	List<Integer> cartIds = new ArrayList<>();
	cartIds.add(11111);
	cartIds.add(1701030002);
	cartIds.add(1701030003);
	//System.out.println(cartService.findAllCommitCart(cartIds).size());
}

@Test
public void teseDate(){
	System.out.println(System.currentTimeMillis()+1000*60*60*24*30);
	System.out.println(System.currentTimeMillis()-1000*60*60*24*30);
}

@Test
public void 测试拿到订单的包裹() throws ServiceException{
	Order order=orderService.get(1702120002l);
	List<OrderDetail>orderDetails=orderService.getOrderDetails(order.getOrderNo());
	//orderService.bulidPackage(order, orderDetails);
}

@Test
public void 测试拿到订单的包裹2() throws ServiceException{
ProductManager pm=AppContext.getBean(ProductManager.class);
	Order order=orderService.get(1702120022l);
	System.out.println(order);
	List<OrderDetail>orderDetails=orderService.getOrderDetails(order.getOrderNo());
	List<PackageDetail>all=new ArrayList<>();
	List<OrderDetail>orderDetails_material=new ArrayList<>();
	List<OrderDetail>orderDetails_product=new ArrayList<>();
	for(OrderDetail orderDetail:orderDetails){
		if(!pm.isAuthenticProduct(orderDetail.getProductId())){
			System.out.println("ttttt");
			orderDetails_material.add(orderDetail);
		}else{
			orderDetails_product.add(orderDetail);
		}
	}
	List<PackageDetail>details=orderService.buildMaterialPack(order, orderDetails_material);
	
	List<PackageDetail>details2=orderService.buildProductPack(orderDetails_product,pm);
	
	all.addAll(details); all.addAll(details2);
	
	for(PackageDetail detail:all){
		System.out.println(detail);
	}
}


@Test
public void testPackage() throws ServiceException{
	System.err.println(orderService);
	Order order=orderService.get(1702120027l);
	System.err.println(order);
	List<OrderDetail>orderDetails=orderService.getOrderDetails(order.getOrderNo());
	System.out.println(orderDetails);
	orderService.bulidPackage(order, orderDetails);
}

//@Test
//public void 测试批量生成包裹() throws ServiceException{
//	List<Integer>orderIds=new ArrayList<>();
//	orderIds.add(1701170011);
//	orderService.buildPackages(orderIds);
//}

@Test
public void 测试财务审核并且生成包裹(){
	Order order;
	try {
		order = orderService.getsingleOrder("201704070001");
		orderService.financialAudit(order, true);
	} catch (ServiceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

@Test
public void alipay(){
	AliPaymentService ap = AppContext.getBean(AliPaymentService.class);
	AliPayment a = new AliPayment();
	a.setPaymentId("X1234");
	a.setBody("XXX");

	
	ap.save(a);
}

@Test
public void 测试配置项(){
	System.out.println(orderService.getPostage(8000));
}

@Test
public void 测试通过门店id拿到订单() throws ServiceException{
	List<Integer>counterIds=new ArrayList<>();
	counterIds.add(9213);
List<Order>o1s=	orderService.getOrders(counterIds,"2");
System.out.println(o1s.size());
}

@Test
public void 测试扫描购物车(){
	Counter counter=new Counter();
	counter.setCounterCode("16826");
	Cart cart=new Cart();
	cart.setCartId(1702080001L);
//	orderService.scan(cart);
}


@Test
public void tttttt11(){
	orderService=AppContext.getBean(OrderService.class);
	orderService.get(1703020001l);
//	orderService.get(1703020001);
//	orderService.get(1703020001);
//	orderService.get(1703020001);
	//orderService.getOrders("6");
//	ProductService ps=AppContext.getBean(ProductService.class);
//	ps.get(683);
	cartService.get(1701170011L);
}

@Test
public void 测试支付宝生成包裹(){
	Order order=orderService.get(1703220006l);
}


@Test
public void t111(){
	Cart cart=new Cart();
	cart.setCartId(1702080001L);
	ProductManager pm=AppContext.getBean(ProductManager.class);
	Product p1=pm.get(19581);
	Product p3=pm.get(19051);
	Product p4=pm.get(19603);
	Product p5=pm.get(19599);
	Product p6=pm.get(19607);
	cartService.add(cart, p1, 90);
	cartService.add(cart, p3, 65);
	cartService.add(cart, p4, 75);
	cartService.add(cart, p5, 20);
	cartService.add(cart, p6, 10);
}


//@Test
//public void test11(){
//	Cart cart=cartService.get(1702100006L);
//	System.out.println(cart);
//List<CartDetail>cartDetails=cartService.getCartDetailByCart(cart);
//	
//List<Integer> cartDetailIds = new ArrayList<Integer>();
//for (CartDetail cartDetail : cartDetails) {
//	cartDetailIds.add(cartDetail.getDetailId());
//}
//
//	Map<Integer, List<CartDetailDesc>> map_cartdesc;
//	map_cartdesc = cartService.getEveryCartDescByCartDetail(cartDetailIds);
//	
//	System.out.println(orderService.getMaterialFreeFee(cartService.getCartDetailByCart(cart), 0.04));
//	
//	 System.err.println(cartService.getMaterialFee(cartDetails, map_cartdesc,0.4).get("materialFee"));
//}


}

