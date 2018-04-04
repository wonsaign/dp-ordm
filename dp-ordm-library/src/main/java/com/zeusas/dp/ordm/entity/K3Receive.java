package com.zeusas.dp.ordm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

/**
 * 金蝶余额期初
 * 金蝶出库单
 * 金蝶收款单
 * @author wangs
 */
@Entity
@Table(name = "base_k3_receive")
public class K3Receive implements IEntity{

	private static final long serialVersionUID = -3929318540913788579L;

	/*CREATE TABLE `base_k3_receive` (
			  `ID` int(11) NOT NULL AUTO_INCREMENT,
			  `FID` int(11) NOT NULL,
			  `FYear` int(11) NOT NULL,
			  `FPeriod` int(11) NOT NULL,
			  `FDate` datetime NOT NULL,
			  `FType` int(11) NOT NULL,
			  `FTypeName` varchar(50) NOT NULL,
			  `FNumber` varchar(50) NOT NULL,
			  `FCustomerID` int(11) NOT NULL,
			  `FCustomerName` varchar(50) NOT NULL,
			  `FAmount` decimal(20,2) NOT NULL,
			  `FCheckType` int(11) NOT NULL,
			  `FExplanation` varchar(200) DEFAULT NULL,
			  PRIMARY KEY (`ID`)
			) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;*/
	
	@Id
	@Column(name = "ID", unique = true, nullable = true)
	protected Integer id;	//主键ID
	@Column(name = "FID")	
	protected Integer fId;	//单据ID
	@Column(name = "FYEAR",nullable = true)
	protected Integer fYear;	//会计年
	@Column(name = "FPERIOD",nullable = true)
	protected Integer fPeriod;	//会计月
	@Column(name = "FNUMBER")
	protected String fNumber;	//单据号
	@Column(name = "FDATE")
	protected String fDate;	//订单日期
	@Column(name = "FTYPE")
	protected Integer fType;	//单据类型
	@Column(name = "FTYPENAME")
	protected String fTypeName;	//类型名
	@Column(name = "FCUSTOMERID")
	protected Integer fCustomerID;	//客户ID
	@Column(name = "FCUSTOMERNAME")
	protected String fCustomerName;	//客户姓名
	@Column(name = "FEXPLANATION")
	protected String fExplanation;	//描述
	@Column(name = "FAMOUNT")
	protected double fAmount;	//金额
	@Column(name = "FCHECKTYPE")
	protected Integer fCheckType;	//审核状态
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getfId() {
		return fId;
	}



	public void setfId(Integer fId) {
		this.fId = fId;
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



	public String getfNumber() {
		return fNumber;
	}



	public void setfNumber(String fNumber) {
		this.fNumber = fNumber;
	}



	public String getfDate() {
		return fDate;
	}



	public void setfDate(String fDate) {
		this.fDate = fDate;
	}



	public Integer getfType() {
		return fType;
	}



	public void setfType(Integer fType) {
		this.fType = fType;
	}



	public String getfTypeName() {
		return fTypeName;
	}



	public void setfTypeName(String fTypeName) {
		this.fTypeName = fTypeName;
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



	public String getfExplanation() {
		return fExplanation;
	}



	public void setfExplanation(String fExplanation) {
		this.fExplanation = fExplanation;
	}



	public double getfAmount() {
		return fAmount;
	}

	public void setfAmount(double fAmount) {
		this.fAmount = fAmount;
	}

	public Integer getfCheckType() {
		return fCheckType;
	}



	public void setfCheckType(Integer fCheckType) {
		this.fCheckType = fCheckType;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public String toString(){
		return JSON.toJSONString(this);
	}
}
