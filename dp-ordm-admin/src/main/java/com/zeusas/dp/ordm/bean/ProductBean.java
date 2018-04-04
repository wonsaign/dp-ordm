package com.zeusas.dp.ordm.bean;

import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.entity.Product;

public class ProductBean {

	private Product product;
	
	private String lastUpdate;

	public ProductBean() {
	}

	public ProductBean(Product product) {
		this.product = product;
		this.lastUpdate = product.getLastUpdate()//
				==null?"":DateTime.formatDate(//
						DateTime.YYYY_MM_DD_HMS,  product.getLastUpdate());
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
}
