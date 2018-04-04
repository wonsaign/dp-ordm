package com.zeusas.dp.ordm.stock.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

@SuppressWarnings("serial")
public class Item implements Serializable {
	/** 产品ID */
	private Integer pid;
	/** 子仓ID */
	private Integer w;
	/** 库存结果数据 */
	private Integer v;
	/** 获取设置单品打欠标识 -> 类型  */
	private Integer t;
	/** 获取设置活动打欠标识 -> 类型  */
	private Integer a;

	public Item() {
	}

	public Item(Integer productId, Integer wId, Integer value) {
		this.pid = productId;
		this.w = wId;
		this.v = value;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer itemID) {
		this.pid = itemID;
	}

	public Integer getW() {
		return w;
	}

	public void setW(Integer subID) {
		this.w = subID;
	}

	public Integer getV() {
		return v;
	}

	public Integer getT() {
		return t;
	}

	public void setT(Integer t) {
		this.t = t;
	}

	public Integer getA() {
		return a;
	}

	public void setA(Integer a) {
		this.a = a;
	}

	public void setV(Integer transResult) {
		this.v = transResult;
	}

	public void addValue(Integer r) {
		if (r != null) {
			this.v += r.intValue();
		}
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
