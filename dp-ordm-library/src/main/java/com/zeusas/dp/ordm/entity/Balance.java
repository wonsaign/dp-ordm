package com.zeusas.dp.ordm.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

@Entity
@Table(name = "base_balance")
public class Balance implements IEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7599878321476756444L;

	// 客户id
	@Id
	@Column(name = "CUSTOMERID", unique = true, nullable = false)
	private Integer customerID;

	// 余额
	@Column(name = "BALANCE")
	private double balance;

	// 预定锁定金额
	@Column(name = "RESERVEAMOUNT")
	private double reserveAmount;
	
	// 锁定 已付款 待推送金蝶中的订单中使用的余额 
	@Column(name = "LOCKBALANCE")
	private double lockBalance;

	// 待付款订单  待推送金蝶 存已经使用的余额
	@Column(name = "UNFINISHORDER")
	@Type(type = "com.zeusas.core.entity.StringListType")
	private List<String> unfinishOrder;
	
	// 状态
	@Column(name = "STATUS")
	private Boolean status;
	// 最后更新
	@Column(name = "LASTUPDATE")
	private Long lastUpdate;

	public Balance() {
		setLastUpdate(System.currentTimeMillis());
	}

	public Integer getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Integer customerID) {
		this.customerID = customerID;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
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

	public double getReserveAmount() {
		return reserveAmount;
	}

	public void setReserveAmount(double reserveAmount) {
		this.reserveAmount = reserveAmount;
	}

	public List<String> getUnfinishOrder() {
		return unfinishOrder;
	}

	public void setUnfinishOrder(List<String> unfinishOrder) {
		this.unfinishOrder = unfinishOrder;
	}

	public double getLockBalance() {
		return lockBalance;
	}

	public void setLockBalance(double lockBalance) {
		this.lockBalance = lockBalance;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
