package com.zeusas.dp.ordm.data.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
/**
 * bean of DWT user
 * @author shihx
 * @date 2016年12月11日 下午5:38:21
 */
@Entity
public class DwtUser {
	@Column(name = "BIN_UserID")
	private Integer userId;
	@Column(name = "BIN_EmployeeID")
	private Integer employeeId;
	@Column(name = "LonginName")
	private String longinName;
	@Column(name = "PassWord")
	private String pwd;
	@Column(name = "BIN_OrganizationID")
	private Integer orgid;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}
	
	public String getLonginName() {
		return longinName;
	}
	public void setLonginName(String longinName) {
		this.longinName = longinName;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public Integer getOrgid() {
		return orgid;
	}
	public void setOrgid(Integer orgid) {
		this.orgid = orgid;
	}
	
	public int hashCode() {
		return userId==null?0:userId.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this==obj){
			return true;
		}
		if (obj==null){
			return false;
		}
		if (!obj.getClass().isInstance(this)){
			return false;
		}
		DwtUser a = (DwtUser)obj;
		if (!Objects.equals(this.userId, a.userId)){
			return false;
		}
		return a.userId.equals(a.userId);
	}
	@Override
	public String toString() {
		return "dwtUser [userId=" + userId + ", employeeId=" + employeeId + ", name=" + longinName + ", pwd=" + pwd
				+ ", orgid=" + orgid + "]";
	}
	
}
