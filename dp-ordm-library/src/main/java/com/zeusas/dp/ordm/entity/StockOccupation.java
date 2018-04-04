package com.zeusas.dp.ordm.entity;

import com.zeusas.core.entity.IEntity;

public class StockOccupation implements IEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 8781940465238887727L;
	private Integer stockID;
	private String stockCode;
	private Integer productID;
	private String productName;
	private Integer qty;

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
