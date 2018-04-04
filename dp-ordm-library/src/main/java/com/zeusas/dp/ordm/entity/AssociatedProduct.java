package com.zeusas.dp.ordm.entity;

import java.io.Serializable;
import java.util.Objects;

import com.alibaba.fastjson.JSON;

/**
 * 封装一个用来产品关联物料以及赠品的实体
 * 
 * @author fengx
 * @date 2016年12月13日 上午8:15:16
 */
public class AssociatedProduct implements Serializable {

	private static final long serialVersionUID = -6818181170656524710L;
	// 产品id
	private Integer pid;
	// 产品的数量【3/1=0.33】【4-1:0.25】【5-1:0.2】
	private double coeff;

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public double getCoeff() {
		return coeff;
	}

	public void setCoeff(double coeff) {
		this.coeff = coeff;
	}

	public int hashCode() {
		return pid == null ? 0 : pid.hashCode();
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !(obj instanceof AssociatedProduct)) {
			return false;
		}
		AssociatedProduct ap = (AssociatedProduct) obj;
		return Objects.equals(this.pid, ap.pid) //
				&& Objects.equals(this.coeff, ap.coeff);

	}

	public String toString() {
		return JSON.toJSONString(this);
	}
}
