package com.zeusas.dp.ordm.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class ProductSeller implements Serializable, Comparable<ProductSeller> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5670520855278655949L;
	// 产品ID
	private int pid;
	// 库存
	private Integer inv;
	// 销售金额
	private int val;
	// 销售量
	private int qty;

	public ProductSeller() {
	}

	public ProductSeller(int pid, int qty, int val) {
		this.pid = pid;
		this.qty = qty;
		this.val = val;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}

	public Integer getInv() {
		return inv == null ? 0 : inv;
	}

	public void setInv(Integer inv) {
		this.inv = inv;
	}

	@Override
	public int compareTo(ProductSeller o) {
		if (this == o || val == o.val) {
			return 0;
		}
		return val > o.val ? 1 : -1;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inv == null) ? 0 : inv.hashCode());
		result = prime * result + pid;
		result = prime * result + val;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductSeller other = (ProductSeller) obj;
		if (inv == null) {
			if (other.inv != null)
				return false;
		} else if (!inv.equals(other.inv))
			return false;
		if (pid != other.pid)
			return false;
		if (val != other.val)
			return false;
		return true;
	}

}
