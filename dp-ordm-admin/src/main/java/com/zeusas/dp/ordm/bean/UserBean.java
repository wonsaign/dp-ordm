package com.zeusas.dp.ordm.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.zeusas.dp.ordm.entity.UserDetail;
import com.zeusas.security.auth.entity.AuthUser;

public class UserBean {

	private AuthUser authUser;
	private UserDetail userDetail;

	private String uid;
	private String loginName;
	private String password;
	private String commonName;
	private String orgUnit;
	private String roles;
	private String mobile;
	private String customerType;
	private String status;
	private String lastUpdate;

	public UserBean() {
	}

	public UserBean(AuthUser authUser) {
		this.authUser = authUser;
		this.uid = authUser.getUid() == null ? "" : authUser.getUid();
		this.loginName = authUser.getLoginName() == null ? "" : authUser.getLoginName();
		this.password = authUser.getPassword() == null ? "" : authUser.getPassword();
		this.commonName = authUser.getCommonName() == null ? "" : authUser.getCommonName();
		this.orgUnit = authUser.getOrgUnit() == null ? "" : authUser.getOrgUnit().toString();
		this.mobile = "";
		this.customerType = "";
		String role = JSON.toJSONString(authUser.getRoles());
		if (role.startsWith("[")) {
			role=role.substring(1, role.length());
		}
		if (role.endsWith("]")) {
			role=role.substring(0, role.length() - 1);
		}
		if (role.contains("\"")) {
			role=role.replace("\"", "");
		}
		this.roles = role;
		this.status = authUser.user_enable.equals(authUser.getStatus()) ? "启用" : "禁用";
		if (authUser.getLastUpdate() != null) {
			Date dt = new Date(authUser.getLastUpdate());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			this.lastUpdate = dateFormat.format(dt);
		} else {
			this.lastUpdate = "";
		}
	}

	
	public UserBean(AuthUser authUser, UserDetail userDetail) {
		this.authUser = authUser;
		this.userDetail = userDetail;
		this.uid = authUser.getUid() == null ? "" : authUser.getUid();
		this.loginName = authUser.getLoginName() == null ? "" : authUser.getLoginName();
		this.password = authUser.getPassword() == null ? "" : authUser.getPassword();
		this.commonName = authUser.getCommonName() == null ? "" : authUser.getCommonName();
		this.orgUnit = authUser.getOrgUnit() == null ? "" : authUser.getOrgUnit().toString();
		this.mobile = userDetail.getMobile() == null ? "" : userDetail.getMobile();
		this.customerType = userDetail.getCustomerType() == null ? "" : userDetail.getCustomerType();
		String role = JSON.toJSONString(authUser.getRoles());
		if (role.startsWith("[")) {
			role=role.substring(1, role.length());
		}
		if (role.endsWith("]")) {
			role=role.substring(0, role.length() - 1);
		}
		if (role.contains("\"")) {
			role=role.replace("\"", "");
		}
		this.roles = role;
		this.status = authUser.user_enable.equals(authUser.getStatus()) ? "启用" : "禁用";
		if (authUser.getLastUpdate() != null) {
			Date dt = new Date(authUser.getLastUpdate());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			this.lastUpdate = dateFormat.format(dt);
		} else {
			this.lastUpdate = "";
		}
	}

	public AuthUser getAuthUser() {
		return authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public String getUid() {
		return uid == null ? "" : uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLoginName() {
		return loginName == null ? "" : loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getCommonName() {
		return commonName == null ? "" : commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getOrgUnit() {
		return orgUnit == null ? "" : orgUnit;
	}

	public void setOrgUnit(String orgUnit) {
		this.orgUnit = orgUnit;
	}

	public String getRoles() {
		return roles == null ? "" : roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getMobile() {
		return mobile == null ? "" : mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCustomerType() {
		return customerType == null ? "" : userDetail.getCustomerType();
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getStatus() {
		return status == null ? "" : status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastUpdate() {
		return lastUpdate == null ? "" : lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
