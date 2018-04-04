package com.zeusas.dp.ordm.entity;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.ImmutableListType;

public class ProductSellerListType extends ImmutableListType<ProductSeller>  {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5162182059952452969L;

	public ProductSellerListType() {
		super(ProductSeller.class);
	}

	public String toString() {
		return JSON.toJSONString(this);
	}
}