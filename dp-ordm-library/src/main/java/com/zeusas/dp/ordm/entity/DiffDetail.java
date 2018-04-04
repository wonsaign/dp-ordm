package com.zeusas.dp.ordm.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

@Entity
@Table(name = "bus_diffdetail")
public class DiffDetail implements IEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 9046230108927976318L;

	// id
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;

	// 订单号
	@Column(name = "ORDERNO")
	private String orderNo;

	// 产品id
	@Column(name = "PRODUCTID")
	private Integer productId;

	// 产品名
	@Column(name = "PRODUCTNAME")
	private String productName;

	// 实发数量
	@Column(name = "REALQTY")
	private Integer realQty;

	// 单价
	@Column(name = "REALUNITPRICE")
	private double realUnitPrice;

	// 总计
	@Column(name = "REALTOTALPRICE")
	private double realTotalPrice;
	
	public DiffDetail() {
		super();
	}

	
	public DiffDetail(String orderNo, Integer productId, String productName, Integer realQty, double realUnitPrice,
			double realTotalPrice) {
		super();
		this.orderNo = orderNo;
		this.productId = productId;
		this.productName = productName;
		this.realQty = realQty;
		this.realUnitPrice = realUnitPrice;
		this.realTotalPrice = realTotalPrice;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public Integer getRealQty() {
		return realQty;
	}

	public void setRealQty(Integer realQty) {
		this.realQty = realQty;
	}

	public double getRealUnitPrice() {
		return realUnitPrice;
	}

	public void setRealUnitPrice(double realUnitPrice) {
		this.realUnitPrice = realUnitPrice;
	}

	public double getRealTotalPrice() {
		return realTotalPrice;
	}

	public void setRealTotalPrice(double realTotalPrice) {
		this.realTotalPrice = realTotalPrice;
	}
	
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof DiffDetail)) {
			return false;
		}
		DiffDetail diffDetail = (DiffDetail) obj;
		return Objects.equals(orderNo, diffDetail.orderNo)//
				&& Objects.equals(productId, diffDetail.productId)//
				&& Objects.equals(realQty, diffDetail.realQty);
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
