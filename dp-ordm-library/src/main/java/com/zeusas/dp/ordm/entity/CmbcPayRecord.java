package com.zeusas.dp.ordm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.zeusas.core.entity.IEntity;
/**
 * 民生付返回密文实体
 */
@Entity
@Table(name = "bus_cmbc")
public class CmbcPayRecord implements IEntity{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CMBCID",unique = true ,nullable = false)
	private String cmbcId;
	
	@Column(name= "ORDERNO")
	private String orderNo;
	
	@Column(name= "BANKFLAG")
	private String bankFlag;
	
	@Column(name= "TXTAMT")
	private String txtAmt;
	
	@Column(name= "DATETIME")
	private String dateTime;
	
	@Column(name= "BILLSTATUS")
	private String billStatus;
	
	@Column(name= "REMARK")
	private String remark;
	
	@Column(name = "NOTIFYTXT")
	private String notifyTxt;
	
	public String getCmbcId() {
		return cmbcId;
	}

	public void setCmbcId(String cmbcId) {
		this.cmbcId = cmbcId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getBankFlag() {
		return bankFlag;
	}

	public void setBankFlag(String bankFlag) {
		this.bankFlag = bankFlag;
	}

	public String getTxtAmt() {
		return txtAmt;
	}

	public void setTxtAmt(String txtAmt) {
		this.txtAmt = txtAmt;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}


	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNotifyTxt() {
		return notifyTxt;
	}

	public void setNotifyTxt(String notifyTxt) {
		this.notifyTxt = notifyTxt;
	}

}
