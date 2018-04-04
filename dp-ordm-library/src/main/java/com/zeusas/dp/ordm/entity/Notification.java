package com.zeusas.dp.ordm.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Objects;
import com.zeusas.core.entity.IEntity;

/**
 * 
 * @author fengx
 * @date 2016年12月16日 上午10:52:12
 */
@Entity
@Table(name = "base_notify")
public class Notification implements IEntity {
	private static final long serialVersionUID = 8147497542343642947L;
	//消息id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MID", unique = true, nullable = false)
	private Integer mid;
	//登录名
	@Column(name = "LOGINNAME")
	private String loginName;
	//发布选择的名字 如物流部:XXX
	@Column(name = "USERNAME")
	private String userName;
	//角色集合
	@Column(name = "ROLES")
	@Type(type = "com.zeusas.core.entity.StringSetType")
	private Set<String> roles;
	//消息标题
	@Column(name = "TITLE")
	private String title;
	//消息内容
	@Column(name = "CONTENT")
	private String content;
	@Column(name = "BEGIN")
	private Long begin;
	@Column(name = "END")
	private Long end;
	@Column(name = "ACTIVE")
	private Boolean active;
	@Column(name = "PRIORITY")
	private Integer priority;
	@Column(name = "LASTUPDATE")
	private Long lastupdate;
	@Column(name = "UPDATOR")
	private String updator;

	public Integer getMid() {
		return this.mid;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Set<String> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getBegin() {
		return this.begin;
	}

	public void setBegin(Long begin) {
		this.begin = begin;
	}

	public Long getEnd() {
		return this.end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Long getLastupdate() {
		return this.lastupdate;
	}

	public void setLastupdate(Long lastupdate) {
		this.lastupdate = lastupdate;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	@Override
	public int hashCode() {
		return	 mid == null? 0 : mid.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null || getClass() != obj.getClass()){
			return false;
		}

		Notification a = (Notification) obj;
		if (!(Objects.equal(begin, a.begin) //
				&& Objects.equal(content, a.content) //
				&& Objects.equal(loginName, a.loginName) //
				&& Objects.equal(mid, a.mid) //
				&& Objects.equal(roles, a.roles) //
				&& Objects.equal(title, a.title) //
		&& Objects.equal(userName, a.userName))) {
			return false;
		}

		return true;
	}
	
	public String toString(){
		return JSON.toJSONString(this);
	}
}