package com.zeusas.dp.ordm.entity;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.ImmutableListType;

public class CartDetailDescListType extends ImmutableListType<CartDetailDesc> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public CartDetailDescListType() {
		super(CartDetailDesc.class);
	}

	public String toString() {
		return JSON.toJSONString(this);
	}
}