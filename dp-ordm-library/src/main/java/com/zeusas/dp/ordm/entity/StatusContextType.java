package com.zeusas.dp.ordm.entity;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.ImmutableType;

public class StatusContextType extends ImmutableType<StatusContext> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1413224844702369461L;

	public StatusContextType() {
		super(StatusContext.class);
	}

	public String toString() {
		return JSON.toJSONString(this);
	}
}
