package com.zeusas.dp.ordm.bean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.zeusas.dp.ordm.entity.StockReserve;

public class StockReserveBean {
	// 产品ID
	private Integer productId;
	// 产品名称
	private String productName;
	// 总库存
	private Integer totalAmt;
	// 预留
	private Integer reserveAmt;
	// 直发可用
	private Integer availableAmt;
	// 库存详情
	private Set<StockDetailBean> details;

	private Map<Integer, StockDetailBean> index;

	public StockReserveBean(StockReserve sr) {
		this.productId = sr.getProductId();
		this.productName = sr.getProductName();
		this.totalAmt = this.reserveAmt = this.availableAmt = 0;
		details = new HashSet<>();
		index = new HashMap<>();
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Set<StockDetailBean> getDetails() {
		return details;
	}

	public void setDetails(Set<StockDetailBean> details) {
		this.details = details;
	}

	public Integer getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Integer totalAmt) {
		this.totalAmt = totalAmt;
	}

	public Integer getReserveAmt() {
		return reserveAmt;
	}

	public void setReserveAmt(Integer reserveAmt) {
		this.reserveAmt = reserveAmt;
	}

	public Integer getAvailableAmt() {
		return availableAmt;
	}

	public void setAvailableAmt(Integer availableAmt) {
		this.availableAmt = availableAmt;
	}

	@Override
	public int hashCode() {
		return productId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StockReserveBean)
			return productId.equals(((StockReserveBean) obj).productId);
		return false;
	}

	public StockDetailBean get(Integer stockId) {
		return index.get(stockId);
	}

	public void put(Integer stockId, StockDetailBean bean) {
		index.put(stockId, bean);
		details.add(bean);
	}

	public void putIfAbsent(Integer stockId, StockDetailBean bean) {
		if (index.putIfAbsent(stockId, bean) == null)
			details.add(bean);
	}

	@Override
	public String toString() {
		return "StockReserveBean [productId=" + productId + ", productName=" + productName + ", totalAmt=" + totalAmt
				+ ", reserveAmt=" + reserveAmt + ", availableAmt=" + availableAmt + ", details=" + details + "]";
	}

}
