package com.zeusas.dp.ordm.bi.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeusas.common.data.DDLDBMS;
import com.zeusas.common.data.Database;
import com.zeusas.common.data.DdlItem;
import com.zeusas.common.data.Record;
import com.zeusas.common.data.Table;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.QueryHelper;
import com.zeusas.dp.ordm.bi.model.CounterMapping;
import com.zeusas.dp.ordm.bi.model.CounterSellerData;
import com.zeusas.dp.ordm.bi.model.SellerData;
import com.zeusas.dp.ordm.bi.service.ProductSellerService;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.CounterProduct;
import com.zeusas.dp.ordm.entity.CounterSerial;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.service.CounterProductService;
import com.zeusas.dp.ordm.service.CounterSerialService;

/**
 * config.properties:
 * <p>
 * ====<br>
 * SYNCDB=dwt <br>
 * DDL=ordm_ddl.xml<br>
 * SYNCITEM=ORDM_SELLER<br>
 * jdbc.properties<br>
 * ====<br>
 * dwt.jdbc.driver=...<br>
 * dwt.jdbc.url=...<br>
 */
@Service(value = "productSellerService")
public class ProductSellerServiceImpl implements ProductSellerService {
	static Logger logger = LoggerFactory
			.getLogger(ProductSellerServiceImpl.class);

	@Autowired
	CounterProductService cpService;
	@Autowired
	CounterSerialService csService;
	long lastUpdate;
	// 销售数据中心
	SellerData sellerData;
	// 柜台数据
	Map<String, CounterSellerData> counterSeller = new LinkedHashMap<>();
	// 全局数据
	CounterSellerData globalSellerData;

	@Override
	public void prepare(Collection<Counter> counter, List<Product> products) {
		ArrayList<Product> pp = new ArrayList<>(600);
		for (Product p : products) {
			if (Product.TYPEID_PRODUCT.equals(p.getTypeId()) //
					&& p.isAvalible()) {
				pp.add(p);
			}
		}

		List<Counter> cc = new ArrayList<Counter>();
		for (Counter c : counter) {
			if (c.getStatus()) {
				cc.add(c);
			}
		}

		sellerData = new SellerData(cc, pp);
	}

	/**
	 * 从店务通系统中加栽数据到系统
	 * <p>
	 * 
	 * @param start
	 *            开始时间 yyyyMMdd
	 * @param end
	 *            结束四件 yyyyMMdd
	 */
	public void loadData(String start, String end) throws ServiceException {
		// FIXME: 取得销售数据
		DdlItem itm = DDLDBMS.getItem("ORDM_SELLER");
		if (itm == null) {
			throw new ServiceException("Data DDL {ORDM_SELLER} not exist!");
		}
		// 建立数据库管理工具
		Database db = new Database(itm);
		try {
			// Connect to data source
			Table tb;
			// GET the seller data from ${start} to ${end}
			tb = db.open("ORDM_SELLER", start, end);
			// 装数据加入销售数据对象
			for (Record r : tb.values()) {
				// {countCode, productID, seller}
				sellerData.addProductSell(
						(String) r.get(1), //
						TypeConverter.toInteger(r.get(2)), //
						TypeConverter.toInteger(r.get(3)),
						TypeConverter.toInteger(r.get(4)));
			}
			tb.clear();
			tb = null;
		} catch (Exception e) {
			throw new ServiceException("Load DDL error!", e);
		} finally {
			db.closeAll();
		}
	}

	public void doFinal() {
		// STEP 1:
		sellerData.doSum();
		// STEP 2
		int pnum = sellerData.sizeOfProduct();
		globalSellerData = new CounterSellerData(Counter.GLOBALCODE, pnum);
		globalSellerData.doFinal(sellerData.getProducts(), null, sellerData);

		// STEP 3
		CounterMapping cmapper = sellerData.getCounterMapping();
		for (int i = 0; i < sellerData.sizeOfCounter(); i++) {
			Counter counter = cmapper.get(i);
			CounterSellerData csd = new CounterSellerData(
					counter.getCounterCode(), pnum);
			csd.doFinal(sellerData.getData()[i], sellerData.getQty()[i],
					sellerData);
			counterSeller.put(counter.getCounterCode(), csd);
		}
	}

	private void doSaveOrUpdate(CounterSellerData data) {
		CounterProduct cp = cpService.get(data.getCid());
		if (cp == null) {
			cp = new CounterProduct(data.getCid(), data.getProductSeller());
			cpService.save(cp);
		} else {
			cp.setProducts(data.getProductSeller());
			cpService.update(cp);
		}
		CounterSerial cs = csService.get(data.getCid());
		if (cs == null) {
			cs = new CounterSerial(data.getCid(), data.getSeriesSeller());
			csService.save(cs);
		} else {
			cs.setSerials(data.getSeriesSeller());
			csService.update(cs);
		}
	}

	public void doExport() {
		for (CounterSellerData data : counterSeller.values()) {
			doSaveOrUpdate(data);
		}
		doSaveOrUpdate(globalSellerData);
	}

	public void close() {
		this.sellerData.clear();
		this.sellerData = null;
	}
}
