package com.zeusas.dp.ordm.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.service.DictionaryService;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.bi.service.ProductSellerManager;
import com.zeusas.dp.ordm.entity.CounterSerial;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductSeller;
import com.zeusas.dp.ordm.entity.SerialSeller;
import com.zeusas.dp.ordm.service.ProductManager;

public final class SerialSort {

	public static void sort() {
		ProductManager productManager = AppContext.getBean(ProductManager.class);
		DictManager dictManager = AppContext.getBean(DictManager.class);
		DictionaryService dictionaryService = AppContext.getBean(DictionaryService.class);
		// 拿到产品系列下面的字典
		List<Dictionary> productClass = productManager
				.findByHardCode(Product.PRODUCT_POSITIVE_SERIES);
		ProductSellerManager sellerManager = AppContext.getBean(ProductSellerManager.class);
		CounterSerial counterSerial = sellerManager.getGlobalSeriesSeller();
		List<SerialSeller> serialSellers = counterSerial.getSerials();

		for (SerialSeller seller : serialSellers) {
			for (Dictionary d : productClass) {
				if (d.getHardCode().equals(seller.getSid())) {
					d.setSeqid(seller.getVal());
					dictionaryService.update(d);
				}
			}
		}
		dictManager.reload();
		SortBodyType();
	}

	/**
	 * 对产品的品类进行全国总销量的排名
	 * */
	private static void SortBodyType() {
		ProductManager productManager = AppContext.getBean(ProductManager.class);
		DictManager dictManager = AppContext.getBean(DictManager.class);
		List<Dictionary> productClass = productManager.findByHardCode(Product.PRODUCT_BODY_TYPE);
		ProductSellerManager sellerManager = AppContext.getBean(ProductSellerManager.class);
		DictionaryService dictionaryService = AppContext.getBean(DictionaryService.class);
		Map<Integer, Integer> map_pSeller = new HashMap<>();

		List<ProductSeller> productSellers = sellerManager.getGlobalProductSeller();
		for (ProductSeller productSeller : productSellers) {
			map_pSeller.put(productSeller.getPid(), productSeller.getVal());
		}
		for (Dictionary dictionary : productClass) {
			List<Product> ps = productManager.findByBodyType(dictionary.getHardCode());
			if (ps != null) {
				for (Product p : ps) {
					int seqId = dictionary.getSeqid();
					if (map_pSeller.get(p.getProductId()) != null) {
						dictionary.setSeqid(seqId + map_pSeller.get(p.getProductId()));
					}
				}
			}
			dictionaryService.update(dictionary);
		}
		dictManager.reload();
	}

}
