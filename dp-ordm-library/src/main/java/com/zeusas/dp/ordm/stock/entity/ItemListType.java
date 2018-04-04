package com.zeusas.dp.ordm.stock.entity;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.ImmutableListType;

public class ItemListType extends ImmutableListType<Item>  {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6714778218017704687L;

	public ItemListType() {
		super(Item.class);
	}

	public String toString() {
		return JSON.toJSONString(this);
	}
}