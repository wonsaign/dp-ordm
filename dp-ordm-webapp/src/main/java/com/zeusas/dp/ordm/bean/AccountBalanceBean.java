package com.zeusas.dp.ordm.bean;

import java.time.LocalDate;

public class AccountBalanceBean {
	
	/**
	 * 2017/04/27
	 * 目前只有5个
	 */
	protected String fNumber;
	protected String fDate;
	protected String fExplanation;
	protected Double amount;
	protected String fType;
	
	/**
	 * 下列后续是使用
	 */
	protected Integer fCustomerID;
	protected String fCustomerName;
	LocalDate date;
	protected Integer id;
	protected Integer fYear;
	protected Integer fPeriod;
	
	
	
	public AccountBalanceBean(String fNumber, String fDate, String fExplanation, Double amount, String fType) {
		super();
		this.fNumber = fNumber;
		this.fDate = fDate;
		this.fExplanation = fExplanation;
		this.amount = amount;
		this.fType = fType;
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
	public String getfExplanation() {
		return fExplanation;
	}
	public void setfExplanation(String fExplanation) {
		this.fExplanation = fExplanation;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getfType() {
		return fType;
	}
	public void setfType(String fType) {
		this.fType = fType;
	}
	
	
}
