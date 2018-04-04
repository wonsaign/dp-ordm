package com.zeusas.dp.ordm.rev.entity;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.ImmutableType;

public class ReservedActivityContextType  extends ImmutableType<ReservedActivityContext> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -344884796173328922L;

	public ReservedActivityContextType() {
		super(ReservedActivityContext.class);
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
