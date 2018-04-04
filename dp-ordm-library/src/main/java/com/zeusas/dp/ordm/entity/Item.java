package com.zeusas.dp.ordm.entity;

import java.io.Serializable;
import java.util.Objects;

import com.alibaba.fastjson.JSON;

public class Item implements Serializable, Comparable<Item> {
	public final static String TYPE_PAY = "PAY";
	public final static String TYPE_FREE = "FREE";

	/**  serialVersionUID */
	private static final long serialVersionUID = 4463250541707025292L;
	
	private String id;
	private String type;
	private Integer num;
	private Double amt;
	//订单的发货类型id
	private String deliveryWayId;
	private String actId;
	
	private Integer detalType;
	
	private Integer revId;
	
	private String batchNo;

	public Item() {
	}

	public Item(String id, int num) {
		this.id = id;
		this.num = num;
	}
	
	public Item(String type, String id, int num) {
		this(id,num);
		this.type = type;
	}
	
	public Item(String type, String id, int num ,double amt) {
		this(type,id,num);
		this.amt=amt;
	}
	
	public Item(String id, Integer num, Double amt, String deliveryWayId) {
		this(id, num,deliveryWayId);
		this.amt = amt;
	}
	
	
	public Item(String id, Integer num, String deliveryWayId) {
		this(id, num);
		this.deliveryWayId = deliveryWayId;
	}

	public Item(String id, Integer num, Double amt, String deliveryWayId, String actId) {
		this(id, num, amt, deliveryWayId);
		this.actId = actId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Double getAmt() {
		return amt;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
	}

	public String getDeliveryWayId() {
		return deliveryWayId;
	}

	public void setDeliveryWayId(String deliveryWayId) {
		this.deliveryWayId = deliveryWayId;
	}

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public Integer getDetalType() {
		return detalType;
	}

	public Integer getRevId() {
		return revId;
	}

	public void setRevId(Integer revId) {
		this.revId = revId;
	}

	public void setDetalType(Integer detalType) {
		this.detalType = detalType;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof Item)) {
			return false;
		}
		Item itm = (Item) obj;
		return Objects.equals(id, itm.id)//
				&& Objects.equals(num, itm.num)//
				&& Objects.equals(type, itm.type)//
				&& Objects.equals(amt, itm.amt)//
				&& Objects.equals(deliveryWayId, itm.deliveryWayId)//
				&& Objects.equals(actId, itm.actId);
	}

	public String toString() {
		return JSON.toJSONString(this);
	}

	@Override
	public int compareTo(Item o) {
		return id.compareTo(o.id);
	}
}
