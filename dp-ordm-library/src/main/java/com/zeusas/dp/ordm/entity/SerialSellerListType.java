package com.zeusas.dp.ordm.entity;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.ImmutableListType;

public class SerialSellerListType extends ImmutableListType<SerialSeller> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public SerialSellerListType() {
		super(SerialSeller.class);
	}

	public String toString() {
		return JSON.toJSONString(this);
	}
}