package com.zeusas.dp.ordm.entity;

import com.zeusas.core.entity.IEntity;

public class Stock implements IEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -8917575072866216877L;

	public Integer stockID;
	public String stockCode;
	public Integer productID;
	public String productName;
	public Integer qty;

	public Integer getStockID() {
		return stockID;
	}

	public void setStockID(Integer stockID) {
		this.stockID = stockID;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public Integer getProductID() {
		return productID;
	}

	public void setProductID(Integer productID) {
		this.productID = productID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}
}
