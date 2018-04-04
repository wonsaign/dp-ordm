package com.zeusas.dp.ordm.active.model;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.ImmutableType;

public class ActivityContextType extends ImmutableType<ActivityContext> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public ActivityContextType() {
		super(ActivityContext.class);
	}

	public String toString(){
		return JSON.toJSONString(this);
	}
}
