package com.zeusas.dp.ordm.bean.logistics;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.zeusas.core.entity.IJSON;

public class OrderCodeBean implements IJSON {
	@JSONField(name = "orderCodes")
	private List<String> orderCodes;

	public List<String> getOrderCodes() {
		return orderCodes;
	}

	public void setOrderCodes(List<String> orderCodes) {
		this.orderCodes = orderCodes;
	}
}