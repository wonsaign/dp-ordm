package com.zeusas.dp.ordm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Objects;

public class PresentContext implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1582748389780281002L;
	
	private int index;
	// 模板类型
	private String type;
	// 是否已执行 true:已执行  false:未执行
	private Boolean status;
	// 模板明细（id: PRODUCTID, TYPE:发货方式，QTY:数量：配送数量）
	private Set<Item> items = new HashSet<>();
	// 配送开始时间
	private Date startTime;
	// 每月配送结束时间
	private Date endTime;
	
	private String excuteNo;
	
	private long createTime;
	
	public PresentContext() {
		this.createTime=System.currentTimeMillis();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Set<Item> getItems() {
		return items==null?new HashSet<>():items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}
	
	public void addItems(Item item) {
		if(this.items==null){
			this.items=new HashSet<>();
		}
		if(item!=null){
			this.items.add(item);
		}
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getExcuteNo() {
		return excuteNo;
	}

	public void setExcuteNo(String excuteNo) {
		this.excuteNo = excuteNo;
	}

	@Override
	public int hashCode() {
		return items == null ? 0 : items.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || (!(obj instanceof PresentContext))) {
			return false;
		}
		PresentContext b = (PresentContext)obj;
		return Objects.equal(this.type, b.type)//
				&&Objects.equal(this.status, b.status)//
				&&Objects.equal(this.items, b.items)//
				&&Objects.equal(this.startTime, b.startTime)//
				&&Objects.equal(this.endTime, b.endTime);
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
