package com.zeusas.dp.ordm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

@Entity
@Table(name = "bus_redeempoint")
public class RedeemPoint implements IEntity {

	private static final long serialVersionUID = -8136286953394141866L;
	@Id
	@Column(name = "ID")
	private Long Id;
	/** 柜台号 */
	@Column(name = "COUNTERCODE")
	private String counterCode;
	/** 产品ID */
	@Column(name = "PRODUCTID")
	private Integer productId;
	/** 数量*/
	@Column(name = "NUM")
	private Integer num;
	/** 系统制单日期*/
	@Column(name = "DEADLINE")
	private Long deadline;
	/** 是否禁用*/
	@Column(name = "AVALIBLE")
	private Boolean avalible;
	/** 导入时间*/
	@Column(name = "CREATTIME")
	private Long creatTime;
	/** 制单人*/
	@Column(name = "CREATOR")
	private String creator;
	/** 执行单号*/
	@Column(name = "EXCUTENO")
	private String excuteNo;
	/** 发货方式*/
	@Column(name = "DELIVERYWAY")
	private String deliveryWay;

	@Column(name = "LASTUPDATE")
	private Long lastUpdate;
	/** 备注*/
	@Column(name = "REMARK")
	private String remark;

	public RedeemPoint() {
		this.lastUpdate = this.creatTime = System.currentTimeMillis();
	}

	public RedeemPoint(String counterCode, Integer productId, Integer num, String creator, String deliveryWay,
			String remark) {
		this();
		this.counterCode = counterCode;
		this.productId = productId;
		this.num = num;
		this.creator = creator;
		this.avalible = true;
		this.deliveryWay = deliveryWay;
		this.remark = remark;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		this.Id = id;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getExcuteNo() {
		return excuteNo;
	}

	public void setExcuteNo(String excuteNo) {
		this.excuteNo = excuteNo;
	}

	public Long getDeadline() {
		return deadline;
	}

	public void setDeadline(Long deadline) {
		this.deadline = deadline;
	}

	public Boolean getAvalible() {
		return avalible;
	}

	public void setAvalible(Boolean avalible) {
		this.avalible = avalible;
	}

	public Long getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Long creatTime) {
		this.creatTime = creatTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Long getLastUpdate() {
		return lastUpdate;
	}

	public String getDeliveryWay() {
		return deliveryWay;
	}

	public void setDeliveryWay(String deliveryWay) {
		this.deliveryWay = deliveryWay;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
