package com.zeusas.dp.ordm.entity;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.ImmutableListType;
import com.zeusas.core.entity.ImmutableSetType;

public class ItemSetType extends ImmutableSetType<Item>  {

	private static final long serialVersionUID = -4233754588415498157L;

	public ItemSetType() {
		super(Item.class);
	}

	public String toString() {
		return JSON.toJSONString(this);
	}
}