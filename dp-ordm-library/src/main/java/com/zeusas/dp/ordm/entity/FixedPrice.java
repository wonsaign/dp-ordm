package com.zeusas.dp.ordm.entity;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

/**
 * 产品一口价策略
 * 
 * @author fengx
 * @date 2017年5月27日 上午9:09:47
 */
@Entity
@Table(name = "bus_fixedprice")
public class FixedPrice implements IEntity{

	/**	 * serialVersionUID	 */
	private static final long serialVersionUID = -2881019854696731596L;

	/** 主键 */
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	/** 产品id */
	@Column(name = "PRODUCTID")
	private Integer productId;

	/** 产品名称 */
	@Column(name = "PRODUCTNAME")
	private String productName;

	/** 产品的价格 */
	@Column(name = "PRICE")
	private Double price;

	/** 产品的固定折扣 */
	@Column(name = "DISCOUNT")
	private Double discount;
	//FIX ME :根据FixedPriceManager 这里应该是客户类型Id集合
	@Column(name="CUSTOMERTYPEID")
	@Type(type = "com.zeusas.core.entity.StringSetType")
	private Set<String> customerTypeId;
	/** 是否走折扣 添加到购物车时生效 */
	@Column(name="PRICEPOLICY")
	private Boolean pricePolicy;

	/** 产品是否为固定价格 固定价格1|固定折扣0 */
	@Column(name = "Fix")
	private Boolean fix;

	/** 一口价策略是否可用 */
	@Column(name = "Status")
	private Boolean status;
	/** 是否走费用比 */
	@Column(name="COSTRATIO")
	private Boolean costRatio;
	
	@Column(name = "LASTUPDATE")
	private Long lastUpdate;
	
	@Column(name="UPDATOR")
	private String updator;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Set<String> getCustomerTypeId() {
		return customerTypeId==null?new LinkedHashSet<>():customerTypeId;
	}

	public void setCustomerTypeId(Set<String> customerTypeId) {
		this.customerTypeId = customerTypeId;
	}

	public Boolean getPricePolicy() {
		return pricePolicy;
	}

	public void setPricePolicy(Boolean pricePolicy) {
		this.pricePolicy = pricePolicy;
	}

	public Boolean getFix() {
		return fix;
	}

	public void setFix(Boolean fix) {
		this.fix = fix;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getCostRatio() {
		return costRatio;
	}

	public void setCostRatio(Boolean costRatio) {
		this.costRatio = costRatio;
	}

	public Long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	@Override
	public int hashCode() {
		return (id == null) ? 0 : id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FixedPrice other = (FixedPrice) obj;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (Double.doubleToLongBits(discount) != Double.doubleToLongBits(other.discount))
			return false;
		if (fix != other.fix)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
