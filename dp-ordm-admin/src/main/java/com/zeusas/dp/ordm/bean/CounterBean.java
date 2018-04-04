package com.zeusas.dp.ordm.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;

public class CounterBean {
	
	public CounterBean() {
	}
	
	public CounterBean(Counter counter, Customer customer) {
		this.counter = counter;
		this.customer = customer;
		this.counterCode=counter.getCounterCode()==null?"":counter.getCounterCode();
		this.counterName=counter.getCounterName()==null?"":counter.getCounterName();
		this.customerName=customer.getCustomerName()==null?"":customer.getCustomerName();
		this.contact=counter.getContact()==null?"":counter.getContact();
		this.mobile=counter.getMobile()==null?"":counter.getMobile();
		this.address=counter.getAddress()==null?"":counter.getAddress();
		this.type=counter.getType()==null?"":counter.getType();
		this.status=counter.getStatus()?"营业":"撤店";
		if(customer.getLastUpdate()!=null){
			Date dt=new Date(counter.getLastUpdate());
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			this.lastUpdate=dateFormat.format(dt);
		}else{
			this.lastUpdate="";
		}
	}
	private Counter counter;
	private Customer customer;
	
	private String counterCode;
	private String counterName;
	private String customerName;
	private String contact;
	private String mobile;
	private String address;
	private String type;
	private String status;
	private String lastUpdate;
	
	public Counter getCounter() {
		return counter;
	}

	public void setCounter(Counter counter) {
		this.counter = counter;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getCounterCode() {
		return counterCode==null?"":counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getCounterName() {
		return counterName==null?"":counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
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

	public String getAddress() {
		return address==null?"":address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return type==null?"":type;
	}

	public void setType(String type) {
		this.type = type;
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

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	
}
