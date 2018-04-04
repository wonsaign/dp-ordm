package com.zeusas.dp.ordm.active.model;

import java.io.Serializable;

import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;
import com.zeusas.dp.ordm.entity.Product;

public class ProductItem implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -3407662284851552088L;
	
	private Integer pid;
	private double price;
	private int quantity;
	
	@Transient
	private transient Product product;
	
	public ProductItem(){
		quantity = 1;
	}
	
	public ProductItem(Integer pid, double price) {
		this.pid = pid;
		this.price = price;
		this.quantity = 1;
	}
	
	
	public ProductItem(Integer pid, double price, int quantity) {
		this.pid = pid;
		this.price = price;
		this.quantity = quantity;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}	
	
	/** 获取产品*/
	@java.beans.Transient
	public Product getProduct() {
		return this.product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
