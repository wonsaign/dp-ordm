package com.zeusas.dp.ordm.service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.dp.ordm.bi.service.ProductSellerManager;
import com.zeusas.dp.ordm.entity.CounterSerial;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductSeller;
import com.zeusas.dp.ordm.entity.SerialSeller;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.task.CancelOrdersTask;
import com.zeusas.dp.ordm.task.ProductSyncTask;
import com.zeusas.dp.ordm.task.SellerDataTask;
import com.zeusas.dp.ordm.utils.OrdmConfig;

public class ProductTest {

	private FileSystemXmlApplicationContext aContext;

	ProductManager manager;
	{
		aContext = new FileSystemXmlApplicationContext("config/ordm-unit-test.xml");
		aContext.start();
		manager = aContext.getBean(ProductManager.class);
	}
	/*
	 * @Test public void test1() {
	 * System.out.println(aContext.getBean("idGenerator")); }
	 */

	 @Test
	public void test111() {
		List<Product> products = manager.findAllAvaible();
		System.out.println(products.size());
		System.out.println(manager.findByClass("10252").size());
	}

	@Test
	public void 通过物料系列拿到物料() {
		// 20869
		List<Product> products = manager.findByClass("20869");
		System.out.println(products.size());
	}

	// @Test
	public void 更新() throws ServiceException {
		Product product = new Product();
		product.setProductId(676);
		product.setAvalible(false);
		manager.updateInfo(product);
	}

	@Test
	public void testGet() throws ServiceException {
		ProductManager manager = aContext.getBean(ProductManager.class);
		List<Product> products = manager.findByName("工服");
		System.err.println(products);
	}

	// @Test
	public void vv1() {
		ProductManager manager = aContext.getBean(ProductManager.class);
		Product product = manager.get(1);
		Set<String> types = product.getType();
		System.out.println(types);
		for (String s : types) {
			System.out.println(s);
		}
		// 判断产品是否有这个类型
		System.out.println(types.contains("新品"));
	}

	/**
	 * 测试根据一个类型拿到所有的产品
	 * 
	 * @throws ServiceException
	 */
	// @Test
	public void testProducts() throws ServiceException {
		// 传入一个类型
		String clazz = "";
		ProductManager manager = aContext.getBean(ProductManager.class);
		System.out.println(aContext.getBean(ProductManager.class));
		List<Dictionary> dictionaries = manager.findByHardCode("104");
		System.out.println(dictionaries.size());
		// 拿到字典里面所有的类型，并且可以判断是否是可用的状态
		for (Dictionary dictionary : dictionaries) {
			System.out.println(dictionary.getName());
		}
		// 根据类型找到所有的产品
		clazz = dictionaries.get(1).getHardCode();
		System.out.println(dictionaries.get(1).getHardCode());
		List<Product> products = manager.findByClass(clazz);
		System.out.println("该系列下的所有的产品");
		int i = 0;
		Set<String> types = new HashSet<String>();
		types.add("1");
		types.add("2");
		for (Product product : products) {
			System.out.println("产品名字:" + product.getName() + "\t" + "产品编码:" + product.getProductId() + "产品价格:"
					+ product.getMemberPrice());
			// 遍历产品为每一个产品加上图片路径等。
			i++;
			product.setImageURL("img" + i);
			product.setType(types);
			manager.updateInfo(product);
		}
	}

	// @Test
	public void tttt() throws ServiceException {
		ProductManager manager = aContext.getBean(ProductManager.class);
		System.out.println(manager.findByClass("680").size());
		System.out.println(manager.findByType("2").size());
		System.out.println(manager.findByName("玫瑰").size());
		for (Product p : manager.findByName("玫瑰")) {
			System.out.println(p.getName() + "\t" + p.getFitemClassName());
		}
		System.out.println(manager.findSerials("893").size());
		// for(String s:manager.findSerials("1723")){
		// System.out.println(s);
		// }
	}

	// @Test
	public void t111ttt() throws ServiceException {
		ProductManager manager = aContext.getBean(ProductManager.class);
		System.out.println(manager.findByClass("680").size());
		System.out.println(manager.findByType("2").size());
		System.out.println(manager.findByName("玫瑰").size());
		for (Product p : manager.findByName("玫瑰")) {
			System.out.println(p.getName() + "\t" + p.getFitemClassName());
		}
		System.out.println("赠品玫瑰数量1：" + manager.fingBySerialAndName("893", "玫瑰").size());
		System.out.println("所哟玫瑰数量2：" + manager.findSerials("893").size());
		System.out.println(manager.findByName("玫瑰").size());
		System.out.println("赠品玫瑰数量3：" + manager.fingBySerialAndName("893", null).size());
		// for(String s:manager.findSerials("1723")){
		// System.out.println(s);
		// }
	}

	/**
	 * 测试一个产品是不是正品
	 */
	@Test
	public void yyyy() {
		ProductManager manager = aContext.getBean(ProductManager.class);
		boolean flag = manager.isAuthenticProduct(20894);
		System.out.println(flag);
	}

	// @Test
	public void test拿到任意的产品() {
		ProductManager manager = aContext.getBean(ProductManager.class);
		System.out.println(manager.get(11655));

	}

	@Test
	public void test拿到自定义类型的产品() {

		ProductManager manager = aContext.getBean(ProductManager.class);
		List<Product> products = manager.findByType("1");
		System.out.println(products == null);

	}

	@Test
	public void 拿到大类下面的所有产品() throws ServiceException {
		ProductManager manager = aContext.getBean(ProductManager.class);
		List<Product> products = manager.fingBySerialAndName("670", null);
		System.out.println(products.size());

	}

	@Test
	public void 测试价格排序() {
		String serialId = "1929";
		ProductManager manager = aContext.getBean(ProductManager.class);
		List<Product> products = manager.findByPrice(serialId, true);
		System.out.println("size" + products.size());
		for (Product product : products) {
			System.out.println(product.getMemberPrice() + "---" + product.getName());
		}
	}

	@Test
	public void testList排序() {
		List<Integer> ls = new ArrayList<Integer>();
		ls.add(4);
		ls.add(3);
		ls.add(5);
		ls.add(1);
		ls.add(0, 6);
		System.out.println(ls);
		System.out.println(ls.get(0));
	}

	@Test
	public void 测试按部位分类() {
		// 20040
		ProductManager manager = aContext.getBean(ProductManager.class);
		System.out.println(manager.findByBodyType("20052").size());
	}

	@Test
	public void 测试按部位分类价格排序() {
		// 20040
		ProductManager manager = aContext.getBean(ProductManager.class);
		List<Product> products = manager.findByPriceWithBody("20052", false);
		for (Product p : products) {
			System.out.println(p.getMemberPrice() + "-----------" + p.getName());
		}
	}

	@Test
	public void testdup1() {
		ProductManager manager = aContext.getBean(ProductManager.class);
		Product p1 = manager.get(694);
		Product p2 = new Product();
		BeanDup.dup(p1, p2);
		System.out.println(p2);
	}

	@Test
	public void 测试系列排名() {
		ProductManager manager = aContext.getBean(ProductManager.class);
		List<Dictionary> productClass = manager.findByHardCode(Product.PRODUCT_POSITIVE_SERIES);

		List<Dictionary> updateProductClass = new ArrayList<Dictionary>();

		productClass.stream().sorted(Comparator.comparingInt(Dictionary::getSeqid).reversed())
				.forEach(e -> updateProductClass.add(e));

		List<Dictionary> sorted = productClass.stream().sorted(Comparator.comparingInt(Dictionary::getSeqid).reversed())
				.collect(Collectors.toList());

		// System.out.println(productClass.size());
		// System.out.println(updateProductClass.size());
		for (Dictionary d : sorted) {
			System.out.println(d.getSeqid() + "---" + d.getSummary());
		}
	}

	/**
	 * 19551081---无纺布 16562797---芦荟系列 16557686---精油系列 14599760---滇青瓜系列
	 * 13347770---雪莲系列 10360219---美眼医生系列 6892308---隐形盒装 5718128---接骨木系列
	 * 5525196---气垫粉系列 5379103---睡眠面膜 5121130---薰衣草系列 4629844---仙人掌系列
	 * 4385078---玫瑰系列 4203740---野玫瑰系列 4167832---红石榴系列 4012672---山茶花系列
	 * 3975903---隐形面贴 3692154---彩妆系列 3149454---绿茶系列 2533236---手部系列
	 * 2061536---青瓜系列 1209196---积雪草 965354---红参系列 764595---套装系列 726484---水洗面膜
	 * 464521---身体系列 332098---喷雾系列 299340---石斛兰系列 198578---美妆工具 132075---茶树系列
	 * 38456---洗护系列 258---防晒、隔离系列 0---黄岑系列 0---抹茶系列 0---樱花系列
	 * 
	 */
	@Test
	public void 测试销售数据和字段系列结合起来() {
		ProductManager productManager = aContext.getBean(ProductManager.class);
		DictManager dictManager = aContext.getBean(DictManager.class);
		List<Dictionary> productClass = productManager.findByHardCode(Product.PRODUCT_POSITIVE_SERIES);

		ProductSellerManager sellerManager = AppContext.getBean(ProductSellerManager.class);
		CounterSerial counterSerial = sellerManager.getGlobalSeriesSeller();
		List<SerialSeller> serialSellers = counterSerial.getSerials();
		int i = 1000;
		for (SerialSeller seller : serialSellers) {
			for (Dictionary d : productClass) {
				if (d.getHardCode().equals(seller.getSid())) {
					d.setSeqid(seller.getVal());
					dictManager.update(d);
				}
			}
		}
	}

	@Test
	public void 如果该系列里面没有活跃产品() {// 就将其系列更新为不可用的状态
		ProductManager productManager = aContext.getBean(ProductManager.class);
		DictManager dictManager = aContext.getBean(DictManager.class);
		List<Dictionary> productClass = productManager.findByHardCode(Product.PRODUCT_POSITIVE_SERIES);
		ProductSellerManager sellerManager = AppContext.getBean(ProductSellerManager.class);
		CounterSerial counterSerial = sellerManager.getGlobalSeriesSeller();
		List<SerialSeller> serialSellers = counterSerial.getSerials();
		int i = 1000;
		for (Dictionary d : productClass) {
			if (productManager.findByClass(d.getHardCode()) == null) {
				d.setActive(false);
				dictManager.update(d);
			}
		}
	}

	@Test
	public void 如果该系列里面没有活跃产品2() {// 就将其品类更新为不可用的状态
		ProductManager productManager = aContext.getBean(ProductManager.class);
		DictManager dictManager = aContext.getBean(DictManager.class);
		List<Dictionary> productClass = productManager.findByHardCode(Product.PRODUCT_BODY_TYPE);
		ProductSellerManager sellerManager = AppContext.getBean(ProductSellerManager.class);
		CounterSerial counterSerial = sellerManager.getGlobalSeriesSeller();
		List<SerialSeller> serialSellers = counterSerial.getSerials();
		int i = 1000;
		for (Dictionary d : productClass) {
			List<Product> ps = productManager.findByBodyType(d.getHardCode());
			if (ps == null) {
				d.setActive(false);
				dictManager.update(d);
			}
		}
	}

	@Test
	public void 测试销售数据和品类结合起来() {
		ProductManager productManager = aContext.getBean(ProductManager.class);
		DictManager dictManager = aContext.getBean(DictManager.class);
		List<Dictionary> productClass = productManager.findByHardCode(Product.PRODUCT_BODY_TYPE);
		ProductSellerManager sellerManager = AppContext.getBean(ProductSellerManager.class);

		Map<Integer, Integer> map_pSeller = new HashMap<>();

		List<ProductSeller> productSellers = sellerManager.getGlobalProductSeller();
		for (ProductSeller productSeller : productSellers) {
			map_pSeller.put(productSeller.getPid(), productSeller.getVal());
		}

		for (Dictionary dictionary : productClass) {
			List<Product> ps = productManager.findByBodyType(dictionary.getHardCode());
			System.err.println(ps == null);
			if (ps != null) {
				for (Product p : ps) {
					int seqId = dictionary.getSeqid();
					if (map_pSeller.get(p.getProductId()) != null) {
						dictionary.setSeqid(seqId + map_pSeller.get(p.getProductId()));
					}
				}
			}
			dictManager.update(dictionary);
		}

	}

	public static void main(String args[]) {
		System.out.println(new Date(1486741614951L));
	}

	@Test
	public void 测试搜索() throws ServiceException {
		List<Product> pts = manager.findByName("山茶花");
		System.out.println(pts);
	}

	@Test
	public void 测试所有的无效的产品() {
		ProductManager productManager = aContext.getBean(ProductManager.class);
		List<Product> products = productManager.findAllDisabledProduct();
		for (Product p : products) {
			System.out.println(p.isAvalible() + "----" + p);
		}
	}

	@Test
	public void 查询无效的产品() throws ServiceException {
		ProductManager productManager = aContext.getBean(ProductManager.class);
		List<Product> products = productManager.findDisabledProduct("玫瑰");
		for (Product p : products) {
			System.out.println(p.isAvalible() + "----" + p);
		}
	}

	@Test
	public void findByHardCode() throws ServiceException {
		ProductManager productManager = aContext.getBean(ProductManager.class);
		List<Dictionary> dictionaries = productManager.findByHardCode("104");
		for (Dictionary d : dictionaries) {
			System.out.println(d.isActive());
		}
	}

	@Test
	public void findByHardCode222() throws ServiceException {
		ProductManager productManager = aContext.getBean(ProductManager.class);
		Product product = productManager.get(21402);
		System.out.println(product.getType() == null);

	}

	@Test
	public void tttt22() {
		SellerDataTask task = AppContext.getBean(SellerDataTask.class);
		try {
			task.exec();
			// System.out.println(new Date(1488349437685l));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void 超时订单取消() {
		CancelOrdersTask task = AppContext.getBean(CancelOrdersTask.class);
		try {
			task.exec();
			// System.out.println(new Date(1488349437685l));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test11() {
		DictManager dictManager = AppContext.getBean(DictManager.class);
		List<Dictionary> dictionaries = dictManager.get(OrdmConfig.ORDMCONFIG).getChildren();

		// Dictionary dict_materialdiscount =
		// dictManager.lookUpByCode(OrdmConfig.ORDMCONFIG,
		// UserResource.MATERIALDISCOUNT);
		// System.out.println(dict_materialdiscount);
		// System.out.println(new Date(1488918630496L));
	}

	@Test
	public void getDate() {
		System.out.println(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 20);
		System.out.println(new Date(1490069031762L));
	}

	@Test
	public void 测试产品同步() {
		ProductSyncTask task = new ProductSyncTask();
		try {
			task.exec();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void t1() {
		System.out.println(manager.findAll());
	}
}
