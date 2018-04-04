package com.zeusas.dp.ordm.entity;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

/**
 * 产品最小订单单位和商品关联策略
 * 
 * @author fengx
 * @date 2016年12月12日 下午6:12:16
 */
@Entity
@Table(name="bus_productrelationpolicy")
public class ProductRelationPolicy implements IEntity {
	private static final long serialVersionUID = -7832757392505901478L;
	
	public final static int GLOBAL_LEVLE = 1;//全局策略等级为1
	public final static int SERIAL_LEVLE = 2;//系列策略等级为2
	public final static int PRODUCT_LEVLE = 3;//产品策略等级为3
	public final static String TYPE_HARDCODE = "106";//全局策略等级为1
	public final static String GLOBAL_TYPE = "global";//全局策略等级为1
	public final static String SERIAL_TYPE = "serial";//系列策略等级为2
	public final static String PRODUCT_TYPE = "product";//产品策略等级为3
	public final static int disable = 0;//status 禁用
	public final static int enable = 1;//status  启用
	
	
	// 策略id
	@Id
	@Column(name = "POLICYID")
	private String policyId;
	// 策略名称 
	@Column(name = "NAME")
	private String name;
	// 策略等级 关联产品的策略会屏蔽系列策略 系列策略会屏蔽全局的策略
	@Column(name = "LEVEL")
	private Integer level;
	//产品id或者系列id或者全局id
	@Column(name = "PID")
	private String pId;
	// 最小的订货单位
	@Column(name = "MINORDERUNIT")
	private Integer minOrderUnit;
	// 策略的状态
	@Column(name = "STATUS")
	private Integer status;
	//是关联产品类型还是关联系列类型还是关联全局类型
	@Column(name="TYPE")
	private  String type;
	@Column(name = "ASSOCIATEDPRODUCTS")
	@Type(type = "com.zeusas.dp.ordm.entity.AssociateProductSetType")
	private Set<AssociatedProduct> associatedProducts;
	// 策略的最后更新时间
	@Column(name = "LASTUPDATE")
	private Long lastUpdate;
	
	// 创建人 
	@Column(name = "CREATOR")
	private String creator;
	
	// 创建时间 
	@Column(name = "CREATETIME")
	private Date createTime;
	
	@Column(name = "UPDATOR")
	private String updator;

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public Integer getMinOrderUnit() {
		return minOrderUnit;
	}

	public void setMinOrderUnit(Integer minOrderUnit) {
		this.minOrderUnit = minOrderUnit;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Set<AssociatedProduct> getAssociatedProducts() {
		return associatedProducts;
	}

	public void setAssociatedProducts(Set<AssociatedProduct> associatedProducts) {
		this.associatedProducts = associatedProducts;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	@Override
	public int hashCode() {
		return policyId == null ? 0 : policyId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!obj.getClass().isInstance(this)) {
			return false;
		}
		ProductRelationPolicy other = (ProductRelationPolicy) obj;
		if (!Objects.equals(this.policyId, other.policyId)) {
			return false;
		}
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.level, other.level)) {
			return false;
		}
		if (!Objects.equals(this.pId, other.pId)) {
			return false;
		}
		if (!Objects.equals(this.associatedProducts, other.associatedProducts)) {
			return false;
		}
		if (!Objects.equals(this.minOrderUnit, other.minOrderUnit)) {
			return false;
		}
		return true;
	}
	public String toString(){
		return JSON.toJSONString(this);
	}
}
