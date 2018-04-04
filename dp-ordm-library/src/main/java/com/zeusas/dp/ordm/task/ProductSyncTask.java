package com.zeusas.dp.ordm.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.zeusas.common.data.DDLDBMS;
import com.zeusas.common.data.Database;
import com.zeusas.common.data.DdlItem;
import com.zeusas.common.data.Meta;
import com.zeusas.common.data.Record;
import com.zeusas.common.data.Table;
import com.zeusas.common.task.CronTask;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.ProductService;

/**
 * 产品信息同步
 * <p>
 * 
 * 从金蝶中取得产品信息，生成直发系统的产品信息，同时更新产品库。
 * <p>
 * 
 */
public class ProductSyncTask extends CronTask {
	static final Logger logger = LoggerFactory.getLogger(ProductSyncTask.class);

	final static String SYNC_PRODUCTS = "SYNC_PRODUCTS";
	final static String DDL = "task/sync_products_ddl.xml";

	public ProductSyncTask() {
		valid = DDLDBMS.load(DDL);
	}

	@Override
	public void exec() throws Exception {
		List<Product> new_products = new ArrayList<>();
		DdlItem itm = DDLDBMS.getItem(SYNC_PRODUCTS);

		Assert.notNull(itm,"DDL定义不能为空。");

		Meta meta = itm.getMeta(SYNC_PRODUCTS);
		Database db = new Database(itm);
		try {
			Table tb = db.open(SYNC_PRODUCTS);
			for (Record rec : tb.values()) {
				Product product = rec.toBean(meta, Product.class);
				new_products.add(product);
			}
			tb.clear();

			ProductService productService =  AppContext.getBean(ProductService.class);
			List<Product> products = productService.findAll();
			/*
			 * FIXME 系统同步过来的产品默认都是可用的 avalible=true 现在有一种情况就是从数据库同步过来的产品不一定是可用的，
			 * 有些产品是人为将其 Disable掉的就算是同步 也应该是将其置为不可用，初步想法是：利用产品里面的status字段标记
			 * 是人为禁用掉的，就算是系统同步，也是将其avalible置为false.
			 */
			ProductManager pm = AppContext.getBean(ProductManager.class);
			updateProduct(products, new_products, pm);
			pm.reload();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			db.closeAll();
		}
	}

	@Override
	protected boolean ready() {
		return valid;
	}

	/**
	 * 
	 * @param products
	 *            系统原来的所有的产品
	 * 
	 * @param new_products
	 *            最新的产品(数据库同步过来的最新的产品)
	 * 
	 */
	public static void updateProduct(List<Product> products, List<Product> new_products, ProductManager pm) {
		/** 新增的产品集合 */
		List<Product> insertProducts = new ArrayList<>();
		List<Product> updateProducts = new ArrayList<>();
		/** 内存里面有的产品但是同步过来的集合没有的产品的集合 */
		List<Product> forbidProducts = new ArrayList<>();

		Map<Integer, Product> map_newProducts = new HashMap<>();
		for (Product p : new_products) {
			map_newProducts.put(p.getProductId(), p);
		}
		products.stream()//
				.filter(p -> map_newProducts.get(p.getProductId()) == null)//
				.forEach(p -> {
					// 内存产品在最新的产品集合里面不存在
					p.setAvalible(false);
					p.setStatus(1);// 禁用
					p.setLastUpdate(System.currentTimeMillis());
					forbidProducts.add(p);
				});
		
		// 遍历最新的产品list 在拿到的产品有没有在原来的list里面
		for (Product product : new_products) {
			if (product.getProductId() == null) {
				logger.error("产品id不存在，数据错误：{}", product);
			}
			Product p = pm.get(product.getProductId());
			if (p == null) {
				// 如果新的产品list的某个产品没有在原来的list里面就说明需要insert
				product.setAvalible(true);
				product.setLastUpdate(System.currentTimeMillis());
				insertProducts.add(product);
			} else if (p.equals(product)) {
				// 产品完全相等，不需要进行任何操作
				continue;
			} else {
				// 如果新的产品list的某个产品有在原来的list里面就说明可能需要更新
				product.setType(p.getType());
				product.setImageURL(p.getImageURL());
				product.setDescription(p.getDescription());
				product.setLastUpdate(System.currentTimeMillis());
				product.setSeqId(p.getSeqId());
				product.setAvalible(true);
				// 将最新的产品加入到更新集合中
				updateProducts.add(product);
			}
		}
		ProductService ps = AppContext.getBean(ProductService.class);
		for (Product p : insertProducts) {
			if("否".equals(p.getIssued())){
				p.setAvalible(false);
			}
			ps.save(p);
		}
		for (Product p : updateProducts) {
			//禁用生日礼盒
			if (p.getName().indexOf("生日礼盒") > 0) {
				p.setAvalible(false);
				p.setStatus(1);
			}
			//禁用不下发到直发的产品
			if("否".equals(p.getIssued())){
				p.setAvalible(false);
			}
			//禁用产品错误导致价格为零
			if(Product.TYPEID_PRODUCT.equals(p.getTypeId())//
					// fixby wangs: 170907 MaterialPrice 改为 MemberPrice
					&& Math.abs(p.getMemberPrice()) < 0.001){
				p.setAvalible(false);
			}
			ps.update(p);
		}
		for (Product p : forbidProducts) {
			ps.update(p);
		}
	}
	
}
