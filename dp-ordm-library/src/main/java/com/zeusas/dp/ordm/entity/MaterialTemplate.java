package com.zeusas.dp.ordm.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Objects;
import com.zeusas.core.entity.IEntity;
/**
 * 新店物料模板
 * @author shihx
 * @date 2017年7月14日 上午9:28:54
 */
@Entity
@Table(name = "bus_materialtemplate")
public class MaterialTemplate implements IEntity{

	private static final long serialVersionUID = -3158228462193663376L;

	@Id
	@Column(name = "ID")
	private Integer id;
	//名称
	@Column(name = "NAME")
	private String name;
	//适用盖模板的最小面积
	@Column(name = "minArea")
	private Double minArea;
	//最大面积
	@Column(name = "maxArea")
	private Double maxArea;
	//模板正文
	@Column(name = "Countext")
	@Type(type = "com.zeusas.dp.ordm.entity.ItemSetType")
	private  Set<Item> countext;
	//状态 true:启用 false:禁用
	@Column(name = "Status")
	private Boolean status;
	//创建者
	@Column(name = "Creator")
	private String creator;
	//创建时间
	@Column(name = "CreateTime")
	private Date createTime;
	//最后更新
	@Column(name = "LastUpdate")
	private Date lastUpdate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getMinArea() {
		return minArea;
	}
	public void setMinArea(Double minArea) {
		this.minArea = minArea;
	}
	public Double getMaxArea() {
		return maxArea;
	}
	public void setMaxArea(Double maxArea) {
		this.maxArea = maxArea;
	}
	public Set<Item> getCountext() {
		return countext;
	}
	public void setCountext(Set<Item> countext) {
		this.countext = countext;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	@Override
	public int hashCode() {
		return id==null?0:id.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || (!(obj instanceof MaterialTemplate))) {
			return false;
		}
		MaterialTemplate b = (MaterialTemplate)obj;
		return Objects.equal(this.id, b.id)//
				||Objects.equal(this.name, b.name)//
				||Objects.equal(this.minArea, b.minArea)//
				||Objects.equal(this.maxArea, b.maxArea)//
				||Objects.equal(this.countext, b.countext)//
				||Objects.equal(this.status, b.status);
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
