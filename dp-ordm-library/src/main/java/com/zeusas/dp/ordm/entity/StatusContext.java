package com.zeusas.dp.ordm.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class StatusContext implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2558420979414522946L;

	// 状态 true 未完成 false已完成
	private final Map<String, Integer> status = new HashMap<>();

	public StatusContext() {}

	public Map<String, Integer> getStatus() {
		return status;
	}

	public int getStatus(String wid) {
		Integer s;
		if ((s = status.get(wid)) == null) {
			return 0;
		}
		return s.intValue();
	}

	public void setOrAddStatus(String wid, int status) {
		this.status.put(wid, status);
	}
	
	public void setStatus(Map<String, Integer> status) {
		if (status != null) {
			this.status.putAll(status);
		}
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public int size() {
		return this.status.size();
	}
}
