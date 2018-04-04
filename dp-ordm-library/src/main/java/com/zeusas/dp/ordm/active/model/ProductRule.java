package com.zeusas.dp.ordm.active.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.service.ProductManager;

public class ProductRule implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4279053963809975133L;
	// 活动责任部门
	protected String dutyCode;
	// 活动系列
	protected String serialsId;
	// 活动产品
	protected final Map<Integer, ProductItem> productItem = new HashMap<>();
	// 从产品中选择数量个数
	protected int quantity;
	// 是否必选
	protected boolean selected;

	public void init(String serialsId) {
		ProductManager pm = AppContext.getBean(ProductManager.class);
		List<Product> pp = pm.findByClass(serialsId);
		pp.stream().filter(p -> p.isAvalible()).forEach(e -> {
			ProductItem itm = new ProductItem(e.getProductId(), e.getMemberPrice(), //
					1);
			productItem.put(e.getProductId(), itm);
		});
	}

	public boolean check(Integer pid) {
		return productItem.containsKey(pid);
	}

	/** 判断所有产品的集合是否在这个产品规则里面 */
	public boolean check(List<Integer> pids) {
		return productItem.keySet().containsAll(pids);
	}

	/**
	 * 现在在item里封装了属性
	 * @return
	 */
	public List<Product> values() {
		ProductManager pm = AppContext.getBean(ProductManager.class);
		List<Product> pp = new ArrayList<>(productItem.size());
		for (Integer pid : productItem.keySet()) {
			Product p = pm.get(pid);
			if (p != null) {
				pp.add(p);
			}
		}
		return pp;
	}

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getDutyCode() {
		return dutyCode;
	}

	public void setDutyCode(String dutyCode) {
		this.dutyCode = dutyCode;
	}

	public Map<Integer, ProductItem> getProductItem() {
		return productItem;
	}

	public void setProductItem(Map<Integer, ProductItem> productItem) {
		if (productItem != null) {
			this.productItem.putAll(productItem);
		}
	}

	/**
	 * 取得产品的数量
	 * @return 产品的数量
	 */
	public int getQuantity() {
		// 必须产品，由ProductItem的数量决定
		if (selected) {
			int sum = 0;
			for (ProductItem item : productItem.values()) {
				sum += item.getQuantity();
			}
			return sum;
		}

		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getSerialsId() {
		return serialsId;
	}

	public void setSerialsId(String serialsId) {
		this.serialsId = serialsId;
		this.init(serialsId);
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public void addProductItem(ProductItem item) {
		this.productItem.put(item.getPid(), item);
	}
}
