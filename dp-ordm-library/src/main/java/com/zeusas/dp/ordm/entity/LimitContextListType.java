package com.zeusas.dp.ordm.entity;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.ImmutableListType;

public class LimitContextListType extends ImmutableListType<LimitContext> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8040267161206708938L;

	public LimitContextListType() {
		super(LimitContext.class);
	}

	public String toString() {
		return JSON.toJSONString(this);
	}
}
