package com.zeusas.dp.ordm.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

/**
 * Counter entity. @author fx
 */
@Entity
@Table(name = "base_counter")
public class Counter implements IEntity {

	private static final long serialVersionUID = -5117910280883697490L;
	public final static Integer GLOBALID = 999999999;
	public final static String GLOBALCODE = GLOBALID.toString();
	/**直营*/
	public final static String Type_Direct = "直营";
	/**加盟商*/
	public final static String Type_Franchisee = "加盟";
	// 柜台ID 来源金蝶 与组织关联只能用counterCode
	@Id
	@Column(name = "COUNTERID", unique = true, nullable = false)
	private Integer counterId;
	@Column(name = "COUNTERCODE")
	private String counterCode;
	@Column(name = "COUNTERNAME")
	private String counterName;
	// 金蝶核算使用
	@Column(name = "CUSTOMERID")
	private Integer customerId;
	// 联系人
	@Column(name = "CONTACT")
	private String contact;
	// 所有人
	@Column(name = "OWNER")
	private String owner;
	// 手机
	@Column(name = "MOBILE")
	private String mobile;
	// 电话
	@Column(name = "PHONE")
	private String phone;
	// 国家
	@Column(name = "COUNTRY")
	private String country;
	// 省
	@Column(name = "PROVINCE")
	private String province;
	// 市
	@Column(name = "CITY")
	private String city;
	// 区(县)
	@Column(name = "AREACOUNTY")
	private String areaCounty;
	// 店铺地址
	@Column(name = "ADDRESS")
	private String address;
	// 收货地址
	@Column(name = "SHIPPING_ADDRESS")
	private String shippingAddress;
	// 邮编
	@Column(name = "POSTCODE")
	private String postCode;
	// 渠道
	@Column(name = "CHANNEL")
	private String channel;
	// 店铺类型
	@Column(name = "TYPE")
	private String type;
	/** 店铺关联的仓库 */
	@Column(name = "warehouse")
	private String warehouses;
	@Column(name = "STATUS")
	private Boolean status;
	@Column(name = "LASTUPDATE")
	private Long lastUpdate;

	@Column(name = "COUNTERTYPE")
	private String counterType;

	@Column(name = "AREAPRICE")
	private Integer areaPrice;
	
	@Column(name = "NEWCOUNTER")
	private Boolean newCounter;
	
	@Column(name = "AREA")
	private Double area;

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		if (channel != null) {
			this.channel = channel;
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if (type != null) {
			this.type = type.intern();
		}
	}

	public Boolean getStatus() {
		return status;
	}

	public String getWarehouses() {
		return warehouses;
	}

	public void setWarehouses(String warehouses) {
		this.warehouses = warehouses;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Long getLastUpdate() {
		return lastUpdate == null ? 0 : lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getCounterType() {
		return counterType;
	}

	public void setCounterType(String counterType) {
		this.counterType = counterType;
	}

	public Integer getAreaPrice() {
		return areaPrice;
	}

	public void setAreaPrice(Integer areaPrice) {
		this.areaPrice = areaPrice;
	}

	public Boolean getNewCounter() {
		return newCounter;
	}

	public void setNewCounter(Boolean newCounter) {
		this.newCounter = newCounter;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public int hashCode() {
		return counterId == null ? 0 : counterId.hashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().isInstance(this)) {
			return false;
		}
		Counter other = (Counter) obj;
		if (!Objects.equals(this.counterId, other.counterId)) {
			return false;
		}
		if (!Objects.equals(this.contact, other.contact)) {
			return false;
		}
		if (!Objects.equals(this.mobile, other.mobile)) {
			return false;
		}
		if (!Objects.equals(this.province, other.province)) {
			return false;
		}
		if (!Objects.equals(this.city, other.city)) {
			return false;
		}
		if (!Objects.equals(this.areaCounty, other.areaCounty)) {
			return false;
		}
		if (!Objects.equals(this.address, other.address)) {
			return false;
		}
		if (!Objects.equals(this.shippingAddress, other.shippingAddress)) {
			return false;
		}
		if (!Objects.equals(this.channel, other.channel)) {
			return false;
		}
		if (!Objects.equals(this.type, other.type)) {
			return false;
		}
		if (!Objects.equals(this.postCode, other.postCode)) {
			return false;
		}

		return true;
	}

	public String toString() {
		return JSON.toJSONString(this);
	}
}