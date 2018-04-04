package com.zeusas.dp.ordm.bean.logistics;

import java.util.List;

import com.zeusas.core.entity.IEntity;

public class LogisticData implements IEntity {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5443161285073786273L;
	private List<LogisticDetail> detail;
	private String orderCode;
	private String time;

	public List<LogisticDetail> getDetail() {
		return detail;
	}

	public void setDetail(List<LogisticDetail> detail) {
		this.detail = detail;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
