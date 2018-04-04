package com.zeusas.dp.ordm.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

/**
 * 
 * @author fengx
 *@date 2016年12月22日 下午1:56:46
 */
@Entity
@Table(name="base_counterprod")
public class CounterProduct implements IEntity {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5902127880444617864L;
	//门店id
	@Id
	@Column(name="COUNTERCODE")
	private String counterCode;
	//关联的产品
	@Column(name="PRODUCTS")
	@Type(type = "com.zeusas.dp.ordm.entity.ProductSellerListType")
	private List<ProductSeller> products;

	@Column(name="LASTUPDATE")
	private Long lastUpdate;

	public CounterProduct() {
		this.lastUpdate = System.currentTimeMillis();
	}
	
	public CounterProduct(String counterCode, List<ProductSeller> products) {
		this();
		this.counterCode = counterCode;
		this.products = products;
	}
	
	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode == null ? "" : counterCode.intern();
	}

	public List<ProductSeller> getProducts() {
		return products;
	}

	public void setProducts(List<ProductSeller> products) {
		this.products = products;
		this.lastUpdate = System.currentTimeMillis();
	}

	public Long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
