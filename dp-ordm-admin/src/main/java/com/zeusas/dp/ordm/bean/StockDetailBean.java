package com.zeusas.dp.ordm.bean;

import com.zeusas.core.entity.IJSON;
import com.zeusas.dp.ordm.stock.entity.Item;

public class StockDetailBean implements IJSON {
	private Integer stockId;
	// 仓库名称
	private String stockName;
	// 库存
	private Integer totalAmt;
	// 预留
	private Integer reserveAmt;
	// 直发可用
	private Integer availableAmt;

	public StockDetailBean(Integer stockId, String stockName, Integer totalAmt, Integer reserveAmt,
			Integer availableAmt) {
		this.stockId = stockId;
		this.stockName = stockName;
		this.totalAmt = totalAmt;
		this.reserveAmt = reserveAmt;
		this.availableAmt = availableAmt;
	}

	public StockDetailBean(String name, Item item) {
		this.stockId = item.getW();
		this.stockName = name;
		this.reserveAmt = 0;
		this.totalAmt = this.availableAmt = item.getV();
	}

	public Integer getStockId() {
		return stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
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
		return stockId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StockDetailBean)
			return this.stockId.equals(((StockDetailBean) obj).stockId);
		return false;
	}

	@Override
	public String toString() {
		return this.toJSON();
	}

}
