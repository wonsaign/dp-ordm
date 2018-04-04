package com.zeusas.dp.ordm.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

/**
 * 客户表 与
 * 
 * @author shihx
 * @date 2016年12月13日 上午9:42:23
 */
@Entity
@Table(name = "base_customer")
public class Customer implements IEntity , Comparable<Customer>{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7663645924058844374L;
	/**字典里客户类型*/
	public final static String customerTypeDictId = "202";
	/**直营*/
	public final static String customerType_Direct = "11387";
	/**加盟商*/
	public final static String customerType_Franchisee = "11391";
	/**运营商*/
	public final static String customerType_Operator = "11392";
	/**代理商*/
	public final static String customerType_Agent = "11393";
	/**分销商*/
	public final static String customerType_Distributor = "12414";
	
	// 客户ID
	@Id
	@Column(name = "CUSTOMERID", unique = true, nullable = false)
	private Integer customerID;
	// 客户父ID
	@Column(name = "PARENTID")
	private Integer parentId;
	// 姓名
	@Column(name = "CUSTOMERNAME")
	private String customerName;
	//联系人
	@Column(name = "CONTACT")
	private String contact;
	// 手机
	@Column(name = "MOBILE")
	private String mobile;
	// 电话
	@Column(name = "PHONE")
	private String phone;
	// 客户类型
	@Column(name = "CUSTOMERTYPE")
	private String customerType;
	
	@Column(name="CUSTOMERTYPEID")
	private Integer customerTypeID;
	// 省
	@Column(name = "PROVINCE")
	private String province;
	// 市
	@Column(name = "CITY")
	private String city;
	// 区(县)
	@Column(name = "AREACOUNTY")
	private String areaCounty;
	// 地址
	@Column(name = "ADDRESS")
	private String address;
	// 邮编
	@Column(name = "POSTCODE")
	private String postCode;
	//状态
	@Column(name = "STATUS")
	private Boolean status;
	//最后更新
	@Column(name = "LASTUPDATE")
	private Long lastUpdate;
	//客户编码
	@Column(name = "CUSTOMERCODE")
	private String CustomerCode;
	@Column(name = "LEVEL")
	private Integer level;
	/**
	 * 客户的柜台Id集合
	 */
	@Column(name = "COUNTERS")
	@Type(type = "com.zeusas.core.entity.StringSetType")
	private Set<String> counters;
	/**
	 * 客户的子客户
	 */
	@Transient
	transient List<Customer> children;
	
	@java.beans.Transient
	public List<Customer> getChildren() {
		return children==null?new ArrayList<>():children;
	}

	@java.beans.Transient
	public void setChildren(List<Customer> children) {
		this.children = children;
	}

	public void addChildren(Customer child) {
		Assert.notNull(child);
		if (children == null) {
			children = new ArrayList<>();
		}
		children.add(child);
	}
	
	public Integer getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Integer customerID) {
		this.customerID = customerID;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public Integer getCustomerTypeID() {
		return customerTypeID;
	}

	public void setCustomerTypeID(Integer customerTypeID) {
		this.customerTypeID = customerTypeID;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAreaCounty() {
		return areaCounty;
	}

	public void setAreaCounty(String areaCounty) {
		this.areaCounty = areaCounty;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public int hashCode() {
		return customerID == null ? 0 : customerID.hashCode();
	}
	
	public Set<String> getCounters() {
		return counters==null?new HashSet<>():counters;
	}

	public void setCounters(Set<String> counters) {
		this.counters = counters;
	}

	public String getCustomerCode() {
		return CustomerCode;
	}

	public void setCustomerCode(String customerCode) {
		CustomerCode = customerCode;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null //
				|| (getClass().isAssignableFrom(obj.getClass())))
			return false;
		Customer other = (Customer) obj;
		if (!Objects.equals(this.customerID, other.customerID)){
			return false;
		}
		if (!Objects.equals(this.parentId, other.parentId)){
			return false;
		}
		if (!Objects.equals(this.customerName, other.customerName)){
			return false;
		}
		if (!Objects.equals(this.customerType, other.customerType)){
			return false;
		}
		if (!Objects.equals(this.mobile, other.mobile)){
			return false;
		}
		if (!Objects.equals(this.status, other.status)){
			return false;
		}
		if (!Objects.equals(this.phone, other.phone)){
			return false;
		}
		if (!Objects.equals(this.postCode, other.postCode)){
			return false;
		}
		if (!Objects.equals(this.province, other.province)){
			return false;
		}
		if (!Objects.equals(this.city, other.city)){
			return false;
		}
		return true;
	}
	public String toString(){
		return JSON.toJSONString(this);
	}

	@Override
	public int compareTo(Customer o) {
		String id = (parentId == null) ? "0" : parentId + ":" + getCustomerID();
		String o_id = (o.parentId == null) ? "0" : o.parentId + ":" + o.getCustomerID();
		return id.compareTo(o_id);
	}
}