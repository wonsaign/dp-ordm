package com.zeusas.dp.ordm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.zeusas.core.entity.IEntity;

@Entity
@Table(name = "base_department")
public class Department implements IEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2920125028611567386L;
	@Id
	@Column(name = "ID")
	private Integer id;
	@Column(name = "CODE")
	private String code;
	@Column(name = "NAME")
	private String name;
	@Column(name = "CONTACT")
	private String contact;
	@Column(name = "PHONE")
	private String phone;
	@Column(name = "PARENTID")
	private Integer parentid;
	@Column(name = "LEVEL")
	private Integer level;
	@Column(name = "LASTUPDATE")
	private Long lastUpdate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
