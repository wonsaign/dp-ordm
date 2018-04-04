package com.zeusas.dp.ordm.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import com.alibaba.fastjson.JSON;
import com.zeusas.dp.ordm.entity.CustomerPricePolicy;

public class PricePolicyBean {
	
	private CustomerPricePolicy customerPricePolicy;
	private String id;
	private Integer typeId;
	private String customerType;
	private String discount;
	private String status;
	private String description;
	private String lastUpdate;
	
	public PricePolicyBean() {
	}

	public PricePolicyBean(CustomerPricePolicy customerPricePolicy) {
		this.customerPricePolicy = customerPricePolicy;
		this.id = customerPricePolicy.getId()==null?"":customerPricePolicy.getId().toString();
		this.typeId=customerPricePolicy.getCustomerTypeId()==null?0:customerPricePolicy.getCustomerTypeId();
		this.customerType = customerPricePolicy.getCustomerTypeId()==null?"":customerPricePolicy.getCustomerTypeId().toString();
		this.discount = customerPricePolicy.getDiscount()==null?"":customerPricePolicy.getDiscount().toString();
		this.description = customerPricePolicy.getDescription()==null?"":customerPricePolicy.getDescription();
		if(customerPricePolicy.getStatus()==null){
			this.status="";
		}else{
			this.status = customerPricePolicy.Policy_disable.equals(customerPricePolicy.getStatus())?"禁用":"启用";
		}
		if(customerPricePolicy.getLastUpdate()!=null){
			Date dt=new Date(customerPricePolicy.getLastUpdate());
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			this.lastUpdate=dateFormat.format(dt);
		}else{
			this.lastUpdate="";
		}
	}

	public CustomerPricePolicy getCustomerPricePolicy() {
		return customerPricePolicy;
	}

	public void setCustomerPricePolicy(CustomerPricePolicy customerPricePolicy) {
		this.customerPricePolicy = customerPricePolicy;
	}

	public String getId() {
		return id==null?"":id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerType() {
		return customerType==null?"":customerType;
	}

	public void setCustomerType(String customerTypeId) {
		this.customerType = customerTypeId;
	}

	public String getDiscount() {
		return discount==null?"":discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getStatus() {
		return status==null?"":status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description==null?"":description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLastUpdate() {
		return lastUpdate==null?"":lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
}
