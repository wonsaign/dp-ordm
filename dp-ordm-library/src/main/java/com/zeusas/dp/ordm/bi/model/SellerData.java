package com.zeusas.dp.ordm.bi.model;

import java.io.Serializable;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Product;


/**
 * 銷售数据基礎数据<p>
 * 从店务通的数据，同步到系统，便于快速计算，直接存储到內存，內存需求量：
 * 10000 x 800 x 64 = 需要500M,是台行的。
 *
 */
public class SellerData implements Serializable {
	/** serialVersionUID */
	private static final long serialVersionUID = -6740696543803114097L;
	static Logger logger = LoggerFactory.getLogger(SellerData.class);
	// Counter mapping table
	final CounterMapping counterMapping;
	// Product mapping table
	final ProductMapping productMapping;

	int[][] data;
	int[][] quantity;

	/** 全国店汇总 */
	private int[] counters;
	/** 未定义的数据，拆店后的数据 */
	private int[] products;

	int total;

	public int[] getProducts() {
		return products;
	}

	public int[] getCounters() {
		return counters;
	}

	public SellerData(Collection<Counter> cc, Collection<Product> pp) {
		counterMapping = new CounterMapping(cc);
		productMapping = new ProductMapping(pp);

		build(cc, pp);
	}

	public void build(Collection<Counter> cc, Collection<Product> pp) {
		data = new int[cc.size()][pp.size()];
		quantity = new int[cc.size()][pp.size()];
		
		counters = new int[counterMapping.size()];
		products = new int[productMapping.size()];

		total = 0;
	}

	public int sizeOfCounter() {
		return counterMapping.size();
	}

	public int sizeOfProduct() {
		return productMapping.size();
	}

	public CounterMapping getCounterMapping() {
		return counterMapping;
	}

	public ProductMapping getProductMapping() {
		return productMapping;
	}

	public void doSum() {
		for (int c = 0; c < data.length; c++) {
			for (int p = 0; p < data[0].length; p++) {
				counters[c] += data[c][p];
				products[p] += data[c][p];
				total += data[c][p];
			}
		}
	}

	public int[][] getData() {
		return data;
	}

	public int[][] getQty(){
		return quantity;
	}

	/**
	 * 增加销售数据
	 * 
	 * @param c 店面编码
	 * @param p 产品ID
	 * @param value 销售数据
	 */
	public void addProductSell(String c, Integer p, Integer value,Integer qty) {
		Integer idxc = counterMapping.index(c);
		Integer idxp = productMapping.index(p);
		if (value == null || idxp == null) {
			logger.debug("店ID{}的销售数据为{}无法统计！",c,value);
			return;
		}
		if (idxc == null) {
			logger.debug("门店:{}数据统计到全国数据中，门店不在统计表中。",c);
			products[idxp.intValue()] += value;
		} else {
			data[idxc][idxp] += value;
			quantity[idxc][idxp] += qty;
		}
	}

	public void clear() {
		for (int c = 0; c < data.length; c++) {
			counters[c] = 0;
			for (int p = 0; p < data[0].length; p++) {
				data[c][p] = 0;
			}
		}
		for (int p = 0; p < data[0].length; p++) {
			products[p] = 0;
		}
	}

	public void release() {
		data = null;
		counters = null;
		products = null;
	}
}
