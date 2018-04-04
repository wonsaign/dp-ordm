package com.zeusas.dp.ordm.bean;

import java.io.Serializable;
import java.util.Objects;

import com.alibaba.fastjson.JSON;

public class Item implements Serializable, Comparable<Item> {
	public final static String TYPE_FREE = "FREE";
	public final static String TYPE_PAY = "PAY";

	/**  serialVersionUID */
	private static final long serialVersionUID = 4463250541707025292L;
	
	private String id;
	private String type;
	private int num;

	public Item() {
	}

	public Item(String id, int num) {
		this.id = id;
		this.num = num;
	}
	
	public Item(String type, String id, int num) {
		this.type = type;
		this.id = id;
		this.num = num;
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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
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
				&& Objects.equals(type, itm.type);
	}

	public String toString() {
		return JSON.toJSONString(this);
	}

	@Override
	public int compareTo(Item o) {
		return id.compareTo(o.id);
	}
}
