package com.zeusas.dp.ordm.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

/**
 * 金蝶余额期初
 * @author wangs
 */
@Entity
@Table(name = "base_k3_account")
public class K3Acount implements IEntity{

	private static final long serialVersionUID = -3929318540913788579L;
	
	/*CREATE TABLE `base_k3_account` (
			  `ID` int(11) NOT NULL,
			  `FYear` int(11) DEFAULT NULL,
			  `FPeriod` int(11) DEFAULT NULL,
			  `FCustomerID` int(11) DEFAULT NULL,
			  `FCustomerName` varchar(255) DEFAULT NULL,
			  `BeginMoney` decimal(20,2) DEFAULT NULL,
			  `ReceiveMoney` decimal(20,2) DEFAULT NULL,
			  `OutMoney` decimal(20,2) DEFAULT NULL,
			  `EndMoney` decimal(20,2) DEFAULT NULL,
			  PRIMARY KEY (`ID`)
			) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	*/
	@Id
	@Column(name = "ID", unique = true, nullable = true)
	protected Integer id;	//主键
	@Column(name = "FYEAR")
	protected Integer fYear;	//会计年
	@Column(name = "FPERIOD")
	protected Integer fPeriod;
	@Column(name = "FCUSTOMERID")
	protected Integer fCustomerID;
	@Column(name = "FCUSTOMERNAME")
	protected String fCustomerName;
	@Column(name = "BEGINMONEY")
	protected double beginMoney;
	@Column(name = "RECEIVEMONEY")
	protected double receiveMoney;
	@Column(name = "OUTMONEY")
	protected double outMoney;
	@Column(name = "ENDMONEY")
	protected double endMoney;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getfYear() {
		return fYear;
	}

	public void setfYear(Integer fYear) {
		this.fYear = fYear;
	}

	public Integer getfPeriod() {
		return fPeriod;
	}

	public void setfPeriod(Integer fPeriod) {
		this.fPeriod = fPeriod;
	}

	public Integer getfCustomerID() {
		return fCustomerID;
	}

	public void setfCustomerID(Integer fCustomerID) {
		this.fCustomerID = fCustomerID;
	}

	public String getfCustomerName() {
		return fCustomerName;
	}

	public void setfCustomerName(String fCustomerName) {
		this.fCustomerName = fCustomerName;
	}

	public double getBeginMoney() {
		return beginMoney;
	}

	public void setBeginMoney(double beginMoney) {
		this.beginMoney = beginMoney;
	}

	public double getReceiveMoney() {
		return receiveMoney;
	}

	public void setReceiveMoney(double receiveMoney) {
		this.receiveMoney = receiveMoney;
	}

	public double getOutMoney() {
		return outMoney;
	}

	public void setOutMoney(double outMoney) {
		this.outMoney = outMoney;
	}

	public double getEndMoney() {
		return endMoney;
	}

	public void setEndMoney(double endMoney) {
		this.endMoney = endMoney;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String toString(){
		return JSON.toJSONString(this);
	}
}
