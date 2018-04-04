package com.zeusas.dp.ordm.bi.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeusas.common.service.OnStartApplication;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.bi.service.ProductSellerManager;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.CounterProduct;
import com.zeusas.dp.ordm.entity.CounterSerial;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductSeller;
import com.zeusas.dp.ordm.entity.SerialSeller;
import com.zeusas.dp.ordm.service.CounterProductService;
import com.zeusas.dp.ordm.service.CounterSerialService;
import com.zeusas.dp.ordm.service.ProductManager;

@Service("productSellerManager")
public class ProductSellerManagerImpl extends OnStartApplication //
		implements ProductSellerManager {

	@Autowired
	private CounterSerialService serialService;
	@Autowired
	private CounterProductService productService;

	/** 全国的单品的销量 */
	final List<ProductSeller> globalProductSeller;
	/** 全国的系列的销量 */
	private CounterSerial globalSerialSeller;

	private long lastUpdate;

	public ProductSellerManagerImpl() {
		globalProductSeller = new ArrayList<>();
		lastUpdate = 0;
	}

	@Override
	public CounterSerial getSeriesSeller(String counterCode) {
		//新店没有销售数据返回全国的
		return serialService.get(counterCode)==null//
				?serialService.get(Counter.GLOBALCODE)//
						:serialService.get(counterCode);
	}

	@Override
	public CounterSerial getGlobalSeriesSeller() {
		if (!DateTime.checkPeriod(Calendar.DATE, lastUpdate)) {
			onStartLoad();
		}
		return this.globalSerialSeller;
	}

	@Override
	public List<ProductSeller> getGlobalProductSeller() {
		if (!DateTime.checkPeriod(Calendar.DATE, lastUpdate)) {
			onStartLoad();
		}
		return globalProductSeller;
	}

	@Override
	public List<ProductSeller> getCounterProductSeller(String counterCode) {
		CounterProduct cp = productService.get(counterCode);
		if (cp != null //
				&& cp.getProducts() != null) {
			return cp.getProducts();
		}
		return new ArrayList<>(0);
	}

	@Override
	public void onStartLoad() {
		globalProductSeller.addAll(productService.getGlobalProduct());
		globalSerialSeller = serialService.getGlobalCounterSerial();
		lastUpdate = System.currentTimeMillis();
	}

	@Override
	public List<Product> getSerialProduct(String counterCode, String serialId) {
		ProductManager pm = AppContext.getBean(ProductManager.class);
		final List<Product> products = new ArrayList<>();
		CounterSerial seller = getSeriesSeller(counterCode);
		List<SerialSeller> serialSellers = seller.getSerials();
		for (SerialSeller serialSeller : serialSellers) {
			if (serialSeller.getSid().equals(serialId)) {
				List<ProductSeller> productSellers = serialSeller.getSell();
				for (ProductSeller productSeller : productSellers) {
					Product p = pm.get(productSeller.getPid());
					products.add(p);
				}
			}
		}
		return products;
	}

}
