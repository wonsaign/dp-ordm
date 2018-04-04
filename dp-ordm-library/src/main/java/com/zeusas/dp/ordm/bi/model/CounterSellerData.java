package com.zeusas.dp.ordm.bi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductSeller;
import com.zeusas.dp.ordm.entity.SerialSeller;

public class CounterSellerData implements Serializable {
	/** serialVersionUID */
	private static final long serialVersionUID = -8683832432884028167L;

	/** 系列ID */
	final String cid;
	/** 产品销售数据 */
	final List<ProductSeller> productSeller;
	/** 按系列分排序的销售数据 */
	final List<SerialSeller> seriesSeller;

	public CounterSellerData(String cid, int size) {
		productSeller = new ArrayList<>(size);
		seriesSeller = new ArrayList<>(size / 6);
		this.cid = cid;
	}

	public String getCid() {
		return cid;
	}

	public List<ProductSeller> getProductSeller() {
		return productSeller;
	}

	public List<SerialSeller> getSeriesSeller() {
		return seriesSeller;
	}

	/**
	 * 数据最终分析处理 根据数据中心的数据关系表，分析单品、序列的数据
	 * 
	 * @param data
	 *            按映射关系的銷售数据作数据源
	 * @param sdata
	 *            整体数据
	 */
	public void doFinal(int data[], int qty[], SellerData sdata) {
		ProductMapping pmapper = sdata.getProductMapping();
		Map<String, SerialSeller> seriesMap = new HashMap<>();
		for (int i = 0; i < pmapper.size(); i++) {
			Product p = pmapper.get(i);
			ProductSeller ps = new ProductSeller(p.getProductId(),
					qty==null?0:qty[i], data[i]);
			productSeller.add(ps);

			String sid = p.getFitemClassId();
			SerialSeller seriesSeller = seriesMap.get(sid);
			if (seriesSeller == null) {
				seriesSeller = new SerialSeller(sid);
				seriesMap.put(sid, seriesSeller);
			}
			seriesSeller.addSeller(ps);
		}

		seriesSeller.addAll(seriesMap.values());
		productSeller.sort(Comparator.comparingInt(ProductSeller::getVal)
				.reversed());
		for (SerialSeller s : seriesSeller) {
			((ArrayList<?>) s.getSell()).trimToSize();
			// 对数据汇总
			s.doSum();
			s.getSell().sort(
					Comparator.comparingInt(ProductSeller::getVal).reversed());
		}
		seriesSeller.sort(Comparator.comparingInt(SerialSeller::getVal)
				.reversed());
	}

	public void clear() {
		productSeller.clear();
		seriesSeller.clear();
	}
}
