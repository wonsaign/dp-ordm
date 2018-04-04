package com.zeusas.dp.ordm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

@Entity
@Table(name = "BASE_BALANCERECORD")
public class BalanceRecord implements IEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 4455761309777060063L;

	public final static int S_INITIAL = 0;
	public final static int S_PAYMENT = 1;

	@Id
	@Column(name = "RECORDID", unique = true, nullable = false)
	private Long recordId;
	// 订单号
	@Column(name = "ORDERNO")
	private String orderNo;
	// 客户id
	@Column(name = "CUSTOMERID")
	private Integer customerID;
	// 客户名
	@Column(name = "CUSTOMERNAME")
	private String customerName;
	// 流水描述
	@Column(name = "DESCRIPTION")
	private String description;
	// 该条记录金额: order amount
	@Column(name = "AMOUNT")
	private double amount;
	// 实际支付
	@Column(name = "REALPAYMENTFEE")
	private double realpaymentfee;
	// 扣除该条金额后余额
	@Column(name = "BALANCE")
	private double balance;
	/** 该订单使用的余额 */
	@Column(name = "USEBALANCE")
	private double useBalance;
	// 操作用户Id
	@Column(name = "OPERATORID")
	private String operatorId;
	// 操作用户名
	@Column(name = "OPERATORNAME")
	private String operatorName;

	@Column(name = "LASTUPDATE")
	private long lastUpdate;

	@Column(name = "STATUS")
	private int status;

	public BalanceRecord() {
		this.lastUpdate = System.currentTimeMillis();
		this.useBalance = 0;
	}
	
	

	public BalanceRecord(String orderNo, double amount, double realpaymentfee, double balance,
			double useBalance, int status) {
		super();
		this.orderNo = orderNo;
		this.amount = amount;
		this.realpaymentfee = realpaymentfee;
		this.balance = balance;
		this.useBalance = useBalance;
		this.lastUpdate = System.currentTimeMillis();
		this.status = status;
		
		StringBuffer sb=new StringBuffer();
		sb.append("订单:");
		sb.append(orderNo);
		sb.append(" ,订单总金额:");
		sb.append(amount);
		if(useBalance!=0){
			sb.append(" ,使用余额:");
			sb.append(useBalance);
		}
		sb.append(" ,实际支付:");
		sb.append(realpaymentfee);
		
		sb.append(" ,余额:");
		sb.append(balance);
		if(status==0){
			sb.append(" ,支付状态:支付前");
		}else if(status==1){
			sb.append(" ,支付状态:支付后");
		}
		this.description = sb.toString();
	}



	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public double getRealpaymentfee() {
		return realpaymentfee;
	}

	public void setRealpaymentfee(double realpaymentfee) {
		this.realpaymentfee = realpaymentfee;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Integer customerID) {
		this.customerID = customerID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getUseBalance() {
		return useBalance;
	}

	public void setUseBalance(double useBalance) {
		this.useBalance = useBalance;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String toString() {
		return JSON.toJSONString(this);
	}
}
