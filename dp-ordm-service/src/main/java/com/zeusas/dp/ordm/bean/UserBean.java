package com.zeusas.dp.ordm.bean;

import com.zeusas.dp.ordm.entity.UserDetail;
import com.zeusas.security.auth.entity.AuthUser;

public class UserBean {

	private AuthUser authUser;
	private UserDetail userDetail;
	private String roles;
	
	public AuthUser getAuthUser() {
		return authUser;
	}
	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
		this.roles = authUser.getRoles().toString();
	}
	public UserDetail getUserDetail() {
		return userDetail;
	}
	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}
	public String getRoles() {
		return roles==null?"":roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	
 }
