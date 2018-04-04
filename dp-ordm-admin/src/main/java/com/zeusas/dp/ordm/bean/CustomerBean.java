package com.zeusas.dp.ordm.bean;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.entity.Customer;

public class CustomerBean {
	
	private Customer customer;
	
	private String customerID;
	private String customerName;
	private String contact;
	private String mobile;
	private String customerType;
	private String postCode;
	private String status;
	private String lastUpdate;
	
	private boolean createUser =false;

	public CustomerBean() {
	}

	public CustomerBean(Customer customer) {
		this.customer = customer;
		this.customerID = customer.getCustomerID()==null?"":customer.getCustomerID().toString();
		this.customerName = customer.getCustomerName()==null?"":customer.getCustomerName();
		this.contact = customer.getContact()==null?"":customer.getContact();
		this.mobile = customer.getMobile()==null?"":customer.getMobile();
		this.customerType = customer.getCustomerType()==null?"":customer.getCustomerType();
		this.postCode = customer.getPostCode()==null?"":customer.getPostCode();
		if(customer.getStatus()==null){
			this.status="";
		}else{
			this.status = customer.getStatus()?"启用":"禁用";
		}
		
		Long t;
		if((t=customer.getLastUpdate())!=null){
			this.lastUpdate=DateTime.formatDate(DateTime.YYYY_MM_DD_HMS, t);
		}else{
			this.lastUpdate="";
		}
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getCustomerID() {
		return customerID==null?"":customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getCustomerName() {
		return customerName==null?"":customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getContact() {
		return contact==null?"":contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getMobile() {
		return mobile==null?"":mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCustomerType() {
		return customerType==null?"":customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getPostCode() {
		return postCode==null?"":postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getStatus() {
		return status==null?"":status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastUpdate() {
		return lastUpdate==null?"":lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public boolean isCreateUser() {
		return createUser;
	}

	public void setCreateUser(boolean createUser) {
		this.createUser = createUser;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
