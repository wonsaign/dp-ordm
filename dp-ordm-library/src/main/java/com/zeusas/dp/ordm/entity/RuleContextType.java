package com.zeusas.dp.ordm.entity;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.ImmutableType;

public class RuleContextType  extends ImmutableType<RuleContext> {

	/*** serialVersionUID	 */
	private static final long serialVersionUID = 1L;

	public RuleContextType() {
		super(RuleContext.class);
	}
	
	public String toString() {
		return JSON.toJSONString(this);
	}

}
