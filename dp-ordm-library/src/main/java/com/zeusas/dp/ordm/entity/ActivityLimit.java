package com.zeusas.dp.ordm.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

@Entity
@Table(name = "bus_activitylimit")
public class ActivityLimit implements IEntity ,Cloneable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6298623408360000105L;
	// 客户ID
	@Id
	@Column(name = "CUSTOMERID", unique = true, nullable = false)
	private Integer customerID;
	// 客户名
	@Column(name = "CUSTOMERNAME")
	private String customerName;
	// 正文
	@Column(name = "CONTEXT")
	@Type(type = "com.zeusas.dp.ordm.entity.LimitContextListType")
	private List<LimitContext> context;

	public Integer getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Integer customerID) {
		this.customerID = customerID;
	}

	public List<LimitContext> getContext() {
		return context;
	}

	public void setContext(List<LimitContext> context) {
		this.context = context;
	}
	
	public void addContext(LimitContext context){
		if(this.context==null){
			this.context=new ArrayList<>();
		}
		this.context.add(context);
	}
	

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public int hashCode() {
		return customerID == null ? 0 : customerID.hashCode();
	}

	public Map<String, LimitContext> toContextMap() {
		Map<String, LimitContext> map = new HashMap<>();
		for (LimitContext ctx : this.context) {
			map.put(ctx.getActId(), ctx);
		}
		return map;
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
		ActivityLimit other = (ActivityLimit) obj;
		return Objects.equals(this.customerID, other.customerID)//
				&& Objects.equals(this.customerName, other.customerName)//
				&& Objects.equals(this.context, other.context);
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
