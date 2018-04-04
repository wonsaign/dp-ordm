package com.zeusas.dp.ordm.entity;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.ImmutableListType;

public class PresentContextType  extends ImmutableListType<PresentContext>{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public PresentContextType() {
		super(PresentContext.class);
	}
	
	public String toString(){
		return JSON.toJSONString(this);
	}
}
