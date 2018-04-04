package com.zeusas.dp.ordm.entity;

import java.io.Serializable;
import java.util.Objects;

import com.alibaba.fastjson.JSON;

public class LimitContext implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	// 活动id
	private String actId;
	// 数量
	private Integer max;
	// 剩余数量
	private Integer qty;
	// 浮动系数
	private Double coefficient;

	public LimitContext(){
		coefficient = 1.0;
	}
	
	public LimitContext(String actId, int max, double coefficient) {
		this.actId = actId;
		this.max = max;
		this.coefficient = coefficient;
		this.qty = (int) (max * coefficient);
	}
	
	
	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Double getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(Double coefficient) {
		this.coefficient = coefficient;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	@Override
	public int hashCode() {
		return  actId == null ? 0 : actId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().isInstance(this)) {
			return false;
		}
		LimitContext other=(LimitContext)obj;
		return Objects.equals(this.actId, other.actId)//
				&& Objects.equals(this.max, other.max)//
				&& Objects.equals(this.qty, other.qty)//
				&& Objects.equals(this.coefficient, other.coefficient);
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
