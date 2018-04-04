package com.zeusas.dp.ordm.bi.service;

import java.util.List;

import com.zeusas.dp.ordm.entity.CounterSerial;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductSeller;

public interface ProductSellerManager {
	/** 接口1：根据门店选出系列 */
	CounterSerial  getSeriesSeller(String counterCode);
	
	/**
	 *  接口2：选出全国系列排序
	 *  extends OnStartApplication
	 * @return 全国数据，按系列排序
	 */
	CounterSerial getGlobalSeriesSeller();
	
	/**
	 * 接口3：选出全国单品销售表
	 * 
	 * @return
	 */
	List<ProductSeller>  getGlobalProductSeller();
	
	/**
	 * 单品店内排名
	 * 
	 * @param counterCode
	 * @return
	 */
	List<ProductSeller> getCounterProductSeller(String counterCode);

	/**
	 * 根据门店和系列拿到该门店的系列里面的产品的排名
	 * 
	 * @param counterCode
	 * @param serialId
	 * @return
	 */
	List<Product> getSerialProduct(String counterCode, String serialId);
}
