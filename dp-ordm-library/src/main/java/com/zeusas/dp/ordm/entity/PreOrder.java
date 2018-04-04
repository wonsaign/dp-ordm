package com.zeusas.dp.ordm.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.zeusas.core.entity.IEntity;

@Entity
@Table(name = "bus_preorder")
/**
 * 系统导入预订单结构
 * @author pengbo
 *
 */
public class PreOrder implements IEntity {

	public static final Integer undeal = 0;
	public static final Integer dealed = 1;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8173240778986216788L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
//	// 客户id
//	@Column(name = "customid")
//	private String customId;
//	// 客户名称
//	@Column(name = "customname")
//	private String customName;
	// 门店名称
	@Column(name = "countername")
	private String counterName;
	// 门店的编码
	@Column(name = "countercode", nullable = false)
	private String counterCode;
	@Column(name = "productid", nullable = false)
	private String productId;
	@Column(name = "productname")
	private String productName;
	@Column(name = "qty")
	private Integer qty;
	@Column(name = "price")
	private Double price;
	@Column(name = "uploaddate")
	private Date uploadDate;
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "deliverywayid")
	private String deliveryWayId;


	public PreOrder() {
	}

	public PreOrder(String counterName, String counterCode, String productId, String productName,
			Integer qty,Double price, Date uploadDate) {
//		this.customId = customId;
//		this.customName = customName;
		this.counterName = counterName;
		this.counterCode = counterCode;
		this.productId = productId;
		this.productName = productName;
		this.qty = qty;
		this.price=price;
		this.uploadDate = uploadDate;
		this.status=PreOrder.undeal;
		//默认为01配送
		this.deliveryWayId = "01";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public Integer getCustomId() {
//		return customId;
//	}
//
//	public void setCustomId(Integer customId) {
//		this.customId = customId;
//	}
//
//	public String getCustomName() {
//		return customName;
//	}
//
//	public void setCustomName(String customName) {
//		this.customName = customName;
//	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
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

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDeliveryWayId() {
		return deliveryWayId;
	}

	public void setDeliveryWayId(String deliveryWayId) {
		this.deliveryWayId = deliveryWayId;
	}
	
	

}
