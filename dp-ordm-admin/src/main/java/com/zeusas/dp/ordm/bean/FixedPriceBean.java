package com.zeusas.dp.ordm.bean;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.entity.FixedPrice;

public class FixedPriceBean {
	
	private Integer id;

	private Integer productId;

	private String productName;

	private String price;

	private String discount;
	
	private Set<String> customerTypeId;
	
	private String pricePolicy;

	private String fix;

	private String status;
	
	private String costRatio;
	
	private String lastUpdate;
	
	public FixedPriceBean() {
	}
	

	public FixedPriceBean(FixedPrice fixedPrice) {
		super();
		DecimalFormat df = new DecimalFormat("#0.00");   
		this.id = fixedPrice.getId();
		this.productId = fixedPrice.getProductId();
		this.productName = fixedPrice.getProductName();
		this.price = df.format(fixedPrice.getPrice());
		this.discount =df.format(fixedPrice.getDiscount());
		this.customerTypeId = fixedPrice.getCustomerTypeId();
		this.pricePolicy = fixedPrice.getPricePolicy()?"是":"否";
		this.costRatio = fixedPrice.getCostRatio()?"是":"否";
		this.fix = fixedPrice.getFix()?"固定价格":"固定折扣";
		this.status = fixedPrice.getStatus()?"启用":"禁用";
		this.lastUpdate = fixedPrice.getLastUpdate()==null?"":DateTime.formatDate(DateTime.YYYY_MM_DD_HMS, new Date(fixedPrice.getLastUpdate()));;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Set<String> getCustomerTypeId() {
		return customerTypeId;
	}

	public void setCustomerTypeId(Set<String> customerTypeId) {
		this.customerTypeId = customerTypeId;
	}

	public String getPricePolicy() {
		return pricePolicy;
	}

	public void setPricePolicy(String pricePolicy) {
		this.pricePolicy = pricePolicy;
	}

	public String getFix() {
		return fix;
	}

	public void setFix(String fix) {
		this.fix = fix;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getCostRatio() {
		return costRatio;
	}

	public void setCostRatio(String costRatio) {
		this.costRatio = costRatio;
	}


	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	
}
