package com.zeusas.dp.ordm.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

@Entity
@Table(name = "base_warehouse")
public class Warehouse implements IEntity {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String dict_type="205";
	
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	//
	@Column(name = "PID")
	private Integer pid;
	//
	@Column(name = "CODE")
	private Integer code;
	//
	@Column(name = "NAME")
	private String name;
	//
	@Column(name = "LASTUPDATE")
	private Long lastUpdate;
	//
	@Column(name = "STATUS")
	private Integer status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof Warehouse)) {
			return false;
		}
		Warehouse w = (Warehouse) obj;
		return Objects.equals(this.id, w.id) //
				&& Objects.equals(this.pid, w.pid) //
				&& Objects.equals(this.status, w.status) //
				&& Objects.equals(this.code, w.code) //
				&& Objects.equals(this.name, w.name) ;
	}
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}
