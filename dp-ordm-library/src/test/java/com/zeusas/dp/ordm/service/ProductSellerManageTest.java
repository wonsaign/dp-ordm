package com.zeusas.dp.ordm.service;
import java.util.List;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.bi.service.ProductSellerManager;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.CounterSerial;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductSeller;
import com.zeusas.dp.ordm.service.ProductManager;


public class ProductSellerManageTest {
	
	private ClassPathXmlApplicationContext aContext;
	private ProductSellerManager manager;

	{
		aContext=new ClassPathXmlApplicationContext("core-test.xml");
		manager=aContext.getBean(ProductSellerManager.class);
		aContext.start();
	}
	
	@Test
	public void test01(){
		String counterCode="999005";
		CounterSerial serial=manager.getSeriesSeller(counterCode);
		System.out.println(serial.getSerials().size());
		
		CounterSerial serial2=manager.getGlobalSeriesSeller();
		System.out.println(serial2.getSerials().size());
		
		System.out.println(manager.getGlobalProductSeller().size());
	}
	
	
	
	@Test
	public void test02(){
		CounterSerial serial=manager.getGlobalSeriesSeller();
		System.out.println(serial.getSerials().get(0).getSell().size());
		System.out.println(serial.getSerials().size());
		System.out.println(manager.getGlobalProductSeller().size());
	}
	
	@Test
	public void  测试全国的单品的销量(){
		List<ProductSeller>sellers=manager.getGlobalProductSeller();
		for(ProductSeller seller:sellers){
			System.out.println(seller);
		}
	}
	
	@Test
	public void  测试某个门店的单品的销量(){
ProductManager pm=AppContext.getBean(ProductManager.class);
		List<ProductSeller>sellers=manager.getCounterProductSeller("010020250");
		for(ProductSeller seller:sellers){
			System.err.println(pm.get(seller.getPid()).getMemberPrice()+"---"+seller.getQty()+"---"+seller.getVal());
		}
	}
	
	
	@Test
	public void test拿到门店的某个系列的销量排名(){
		List<Product>ps=manager.getSerialProduct("global", "19377");
		System.out.println(ps);
		for(Product p:ps){
			System.out.println(ps);
		}
	}

}
