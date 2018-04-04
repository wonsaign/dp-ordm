package com.zeusas.dp.ordm.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

/**
 * 
 * 
 * 11387	0x0000000000524250	01	直营	1
 * 11391	0x0000000000524255	02	加盟商	0.4
 * 11392	0x000000000052425A	03	运营商	0.3
 * 11393	0x000000000052425F	04	代理商	0.4
 * 11414	0x0000000000524267	05	总部	1
 * 11415	0x000000000052426C	06	其他	1
 * 12398	0x00000000006D8F73	07	LS品牌商	0
 * 12414	0x00000000006E126E	08	植物医生品牌商	0
 * 
 * @author fengx
 *@date 2016年12月20日 上午11:51:13
 */
@Entity
@Table(name = "bus_pricepolicy")
public class CustomerPricePolicy implements IEntity {
	
	public final static String customerTypeId_root="202";
	public final static Integer Policy_enable=1;
	public final static Integer Policy_disable=0;

	private static final long serialVersionUID = 4939440524025418142L;

	// 价格策略id
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	// 关联的用户类型的id
	@Column(name = "CUSTOMERTYPEID")
	private Integer customerTypeId;
	// 折扣
	@Column(name = "DISCOUNT")
	private Double discount;
	/** 物料配比的折扣*/
	@Column(name="MATERIALDISCOUNT")
	private Double materialDiscount;
	// 状态
	@Column(name = "STATUS")
	private Integer status;
	// 最后更新时间
	@Column(name = "LASTUPDATE")
	private Long lastUpdate;
	// 描述
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "UPDATOR")
	private String updator;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCustomerTypeId() {
		return this.customerTypeId;
	}

	public void setCustomerTypeId(Integer customerTypeId) {
		this.customerTypeId = customerTypeId;
	}

	public Double getDiscount() {
		return this.discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getMaterialDiscount() {
		return materialDiscount;
	}

	public void setMaterialDiscount(Double materialDiscount) {
		this.materialDiscount = materialDiscount;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

	@Override
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
		CustomerPricePolicy other = (CustomerPricePolicy) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		if (!Objects.equals(this.customerTypeId, other.customerTypeId)) {
			return false;
		}
		if (!Objects.equals(this.discount, other.discount)) {
			return false;
		}
		if (!Objects.equals(this.status, other.status)) {
			return false;
		}
		return true;
	}
	public String toString(){
		return JSON.toJSONString(this);
	}
}