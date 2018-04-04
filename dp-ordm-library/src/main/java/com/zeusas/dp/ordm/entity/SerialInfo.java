package com.zeusas.dp.ordm.entity;


import java.util.Objects;

import com.alibaba.fastjson.JSON;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.core.entity.IEntity;

/**
 * 产品序列信息
 * 从业务字典表中，取出关于产品序列条目，独立维护。 
 */
public class SerialInfo implements IEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** */
	public final static String SERIALINFO_TYPE= "104";
	// 系列名字 -name
	String sid;
	// 系列图片路径--url
	String name;
	// 系列id -HardCode
	String imageURL;
	// 系列的状态
	Integer status;
	// 系列的优先级
	Integer seqId;
	//最后更新时间
	Long lastUpdate;
	// 
	String summary;
	
	boolean active;
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSeqId() {
		return seqId;
	}
	public void setSeqId(Integer seqId) {
		this.seqId = seqId;
	}
	public Long getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public SerialInfo(Dictionary dict) {
		this.sid = dict.getHardCode();
		this.name = dict.getName();
		this.imageURL = dict.getUrl();
		this.status = dict.getStatus();
		this.seqId = dict.getSeqid();
		this.lastUpdate = dict.getLastUpdate();
		this.summary = dict.getSummary();
		this.active = dict.isActive();
	}
	
	public SerialInfo() {
		super();
	}
	@Override
	public int hashCode() {
		return	sid == null ? 0 : sid.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerialInfo a = (SerialInfo) obj;
		return Objects.equals(this.sid, a.sid)
				&& Objects.equals(this.imageURL, a.imageURL)
				&& Objects.equals(this.name, a.name)
				&& Objects.equals(this.seqId, a.seqId)
				&& Objects.equals(this.status, a.status)
				&& Objects.equals(this.summary, a.summary)
				;
	}
	

	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
