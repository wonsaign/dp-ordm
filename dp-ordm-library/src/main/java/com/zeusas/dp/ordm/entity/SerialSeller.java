package com.zeusas.dp.ordm.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class SerialSeller implements Serializable, Comparable<SerialSeller> {

	/** serialVersionUID */
	private static final long serialVersionUID = -1502663454548623837L;
	/** 系列*/
	private String sid;
	/** 库存*/
	private Integer inv;
	/** 销售数据*/
	private int val;
	
	/**所有产品的销售数据*/
	final List<ProductSeller> sell = new ArrayList<ProductSeller>();
	
	public SerialSeller() {
	}

	public SerialSeller(String sid) {
		this.sid = sid;
	}

	public SerialSeller(String sid, int val) {
		this.sid = sid;
		this.val = val;
	}

	public SerialSeller(String sid, Integer inv, int val) {
		this.sid = sid;
		this.inv = inv;
		this.val = val;
	}

	public List<ProductSeller> getSell() {
		return sell;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Integer getInv() {
		return inv;
	}

	public void setInv(Integer inv) {
		this.inv = inv;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}

	public void setSell(List<ProductSeller> ps) {
		if (ps != null && ps.size() > 0) {
			this.sell.addAll(ps);
		}
	}

	@Override
	public int compareTo(SerialSeller o) {
		if (val == o.val) {
			return 0;
		}

		return val > o.val ? 1 : -1;
	}

	public void addSeller(ProductSeller ps) {
		if (ps != null) {
			sell.add(ps);
		}
	}
	
	public void doSum() {
		this.inv = 0;
		this.val = 0;
		for (ProductSeller p : sell) {
			inv += p.getInv();
			val += p.getVal();
		}
	}

	@Override
	public int hashCode() {
		return (sell == null) ? 0 : sell.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerialSeller other = (SerialSeller) obj;
		if (inv == null) {
			if (other.inv != null)
				return false;
		} else if (!inv.equals(other.inv))
			return false;
		if (sell == null) {
			if (other.sell != null)
				return false;
		} else if (!sell.equals(other.sell))
			return false;
		if (sid == null) {
			if (other.sid != null)
				return false;
		} else if (!sid.equals(other.sid))
			return false;
		if (val != other.val)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}
