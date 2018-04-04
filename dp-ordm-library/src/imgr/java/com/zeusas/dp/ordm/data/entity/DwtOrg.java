package com.zeusas.dp.ordm.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * bean of DWT Org
 * 
 * @author shihx
 * @date 2016年12月9日 下午4:13:31
 */
@Entity
public class DwtOrg {

	@Column(name = "BIN_OrganizationID")
	private Integer orgID;
	@Column(name = "DepartCode")
	private String code;
	@Column(name = "DepartName")
	private String name;
	@Column(name = "Path")
	private String path;
	@Column(name = "BIN_ProvinceID")
	private Integer provinceId;
	@Column(name = "BIN_CityID")
	private Integer cityId;
	@Column(name = "BIN_CountyID")
	private Integer countyId;
	@Column(name = "Address")
	private String address;
	@Column(name = "Type")
	private String type;
	@Column(name = "ValidFlag")
	private String validflag;
	@Column(name = "UpdateTime")
	private Date updatetime;

	public Integer getOrgID() {
		return orgID;
	}

	public void setOrgID(Integer orgID) {
		this.orgID = orgID;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getCountyId() {
		return countyId;
	}

	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValidflag() {
		return validflag;
	}

	public void setValidflag(String validflag) {
		this.validflag = validflag;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	@Override
	public String toString() {
		return "wdtOrg [orgID=" + orgID + ", code=" + code + ", name=" + name + ", path=" + path + ", provinceId="
				+ provinceId + ", cityId=" + cityId + ", countyId=" + countyId + ", address=" + address + ", type="
				+ type + ", validflag=" + validflag + ", updatetime=" + updatetime + "]";
	}
}
