 package com.zeusas.dp.ordm.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

/**
 * 活动产品细节表 关联该活动产品所有的产品、物料、赠品等
 * 
 * @author fengx
 * @date 2016年12月10日 上午11:09:11
 */
@Entity
@Table(name = "bus_productgroupdetail")
@Deprecated
public class ProductGroupDetail implements IEntity {

	private static final long serialVersionUID = -5045598270564932697L;

	@Id
	@Column(name = "DETAILID", unique = true) 
	private String detailId;
	//关联到活动id
	@Column(name = "ACTID")
	private String actId;
	//系列名称或者产品名称
	@Column(name = "NAME")
	private String name;
	//产品id
	@Column(name = "PRODUCTID")
	private Integer productId;
	//系列id
	@Column(name = "SERIALID")
	private String serialId;
	//购买或者赠送
	@Column(name = "BUYORGIVE")
	private Boolean buyOrGive;
	//系列还是产品
	@Column(name = "SERIALORPRODUCT")
	private Boolean serialOrProduct;

	public String getDetailId() {
		return this.detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getActId() {
		return this.actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getSerialId() {
		return this.serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}


	public Boolean isBuyOrGive() {
		return this.buyOrGive;
	}

	public void setBuyOrGive(Boolean buyOrGive) {
		this.buyOrGive = buyOrGive;
	}

	public Boolean isSerialOrProduct() {
		return this.serialOrProduct;
	}

	public void setSerialOrProduct(Boolean serialOrProduct) {
		this.serialOrProduct = serialOrProduct;
	}

	
		public int hashCode() {
		return detailId == null ? 0 : detailId.hashCode();
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
		ProductGroupDetail other = (ProductGroupDetail) obj;
		if (!Objects.equals(this.detailId, other.detailId)) {
			return false;
		}
		if (!Objects.equals(this.actId, other.actId)) {
			return false;
		}
		if (!Objects.equals(this.productId, other.productId)) {
			return false;
		}
		if (!Objects.equals(this.serialId, other.serialId)) {
			return false;
		}
		if (!Objects.equals(this.buyOrGive, other.buyOrGive)) {
			return false;
		}
		return true;

	}

	public String toString() {
		return JSON.toJSONString(this);
	}

}