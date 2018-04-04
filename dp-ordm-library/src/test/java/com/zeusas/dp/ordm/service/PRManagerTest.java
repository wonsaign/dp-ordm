package com.zeusas.dp.ordm.service;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.AssociatedProduct;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductRelationPolicy;
import com.zeusas.dp.ordm.service.PRPolicyManager;
import com.zeusas.dp.ordm.service.ProductRelationPolicyService;

public class PRManagerTest {
	PRPolicyManager manager;
	DictManager dictManager;
	ProductManager productManager;
	FileSystemXmlApplicationContext ctx;
	@Before
	public void setUp() throws Exception {
		try {
			ctx = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
			manager=AppContext.getBean(PRPolicyManager.class);
			 dictManager=AppContext.getBean(DictManager.class);
			 productManager=AppContext.getBean(ProductManager.class);
			ctx.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 测试一个产品关联策略  Product.getProductId   Product.getFitlemClassId
	 * 这里做的是优先找到单个产品的关联策略  没有的话就去产品所在的系列里面找系列策略  还没有的话
	 * 就去寻找去全局策略
	 */
	//@Test
	public void test01()
	{
		Product product=new Product();
		product.setProductId(895);
		ProductRelationPolicy policy=manager.get(product);
		//System.out.println(policy==null);
		System.out.println(policy.getMinOrderUnit());
		System.out.println(policy.getAssociatedProducts());
		System.out.println(policy.getName());
		for(AssociatedProduct product1:policy.getAssociatedProducts()){
			System.out.println(product1.getPid()+"\t\t"+product1.getCoeff());
		}
	}
	
	/**
	 * 测试全局关联的插入 都是CreateOrUpdate
	 * @throws ServiceException
	 */
//	@Test
	public void 测试插入一个全局的策略() throws ServiceException{
		ProductRelationPolicy policy=new ProductRelationPolicy();
		policy.setpId("global");
		policy.setMinOrderUnit(10);//全局订货最小单位为10
		policy.setName("这是一个全局关联策略呀4");
		policy.setLevel(2);
		policy.setType("global");
		policy.setStatus(0);
		
		Set<AssociatedProduct>products=new HashSet<AssociatedProduct>(); 
		AssociatedProduct product1=new AssociatedProduct();
		product1.setPid(684);
		product1.setCoeff(5);;
		AssociatedProduct product2=new AssociatedProduct();
		product2.setPid(685);
		product1.setCoeff(6);;
		products.add(product1);
		products.add(product2);
		policy.setAssociatedProducts(products);
		manager.add(policy);
	}
	
	//@Test
	public  void 测试一个系列的关联() throws ServiceException{
		ProductRelationPolicy policy=new ProductRelationPolicy();
		policy.setMinOrderUnit(8);//系列订货最小单位为8
		policy.setLevel(2);
		policy.setType("serial");
		policy.setpId("14134");
		policy.setName("这是系列的策略呀1");
		Set<AssociatedProduct>products=new HashSet<AssociatedProduct>(); 
		AssociatedProduct product1=new AssociatedProduct();
		product1.setCoeff(0.25);;
		product1.setPid(683);
		AssociatedProduct product2=new AssociatedProduct();
		product1.setCoeff(0.25);;
		product2.setPid(684);
		AssociatedProduct product3=new AssociatedProduct();
		product1.setCoeff(0.25);;
		product3.setPid(685);
		products.add(product1);
		products.add(product2);
		products.add(product3);
		policy.setAssociatedProducts(products);
		manager.add(policy);
	}
	
	@Test
	public void 测试插入某个产品的关联策略() throws ServiceException
	{
		
//		985	芦荟原液高补水凝露（小袋）
//		993	芦荟原液高补水乳液（小袋）
//		4178	芦荟原液高补水BB霜（小袋）
//		18640	雪莲净澈无瑕凝露
//		18661	雪莲净澈无瑕修护乳
//		18794	雪莲净澈无瑕雪花精华液
//		19399	山茶花悦泽水润精华液
//		21770	2ml净透焕采防晒隔离霜SPF20 PA+
		
		ProductRelationPolicy policy=new ProductRelationPolicy();
		policy.setpId("985");
		policy.setMinOrderUnit(50);//系列订货最小单位为8
		policy.setType("product");
		policy.setName("芦荟原液高补水凝露（小袋）");

		Set<AssociatedProduct>products=new HashSet<AssociatedProduct>(); 
		policy.setAssociatedProducts(products);
		manager.add(policy);
	}
	
	@Test
	public void getname() throws Exception {
		ProductRelationPolicy prp= manager.get("195");
		if(ProductRelationPolicy.PRODUCT_TYPE.equals(prp.getType())){
			Product p=productManager.get(Integer.parseInt(prp.getpId()));
		}else if(ProductRelationPolicy.SERIAL_TYPE.equals(prp.getType())){
			Dictionary d= dictManager.lookUpByCode(Product.PRODUCT_MATERIEL_TYPE, prp.getpId());
			if(d==null){
				d= dictManager.lookUpByCode(Product.PRODUCT_PRESENT_TYPE, prp.getpId());
			}
			if(d==null){
				d= dictManager.lookUpByCode(Product.PRODUCT_POSITIVE_SERIES, prp.getpId());
			}
			System.out.println(d.getName());
		}else{
			System.out.println("全局");
		}
	}
	
}
