package com.zeusas.dp.ordm.bean;

import java.text.DecimalFormat;

import com.zeusas.dp.ordm.entity.Customer;

public class AreaOrderItem {
	//客户id
	private Integer customerId;
	//客户名
	private String customerName;
	//客户类型
	private String customerType;
	//订单数量
	private Integer quantity;
	//直发订单金额
	private Double paymentFee;
	//直发订单金额
	private String paymentFeeFormat;
	//金蝶发货金额
	private Double realFee;
	//金额保留两位小数
	private String realFeeFormat;

	public AreaOrderItem() {
		this.paymentFee=0.0;
		this.realFee = 0.0;
		this.quantity = 0;
	}

	public AreaOrderItem(Customer customer) {
		this();
		this.customerId = customer.getCustomerID();
		this.customerName = customer.getCustomerName();
		this.customerType = customer.getCustomerType();
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getRealFee() {
		return realFee;
	}

	public void setRealFee(Double realFee) {
		this.realFee = realFee;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public void addRealFee(Double fee) {
		if (fee != null) {
			this.realFee += fee;
		}
	}

	public void incQty() {
		this.quantity++;
	}
	
	public void addQty(Integer qty) {
		if(qty!=null){
			this.quantity+=qty;
		}
	}

	public String getRealFeeFormat() {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(realFee);
	}

	public void setRealFeeFormat(String realFeeFormat) {
		this.realFeeFormat = realFeeFormat;
	}

	public Double getPaymentFee() {
		return paymentFee;
	}

	public void setPaymentFee(Double paymentFee) {
		this.paymentFee = paymentFee;
	}

	public String getPaymentFeeFormat() {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(paymentFee);
	}

	public void setPaymentFeeFormat(String paymentFeeFormat) {
		this.paymentFeeFormat = paymentFeeFormat;
	}
	public void addPaymentFee(Double paymentFee) {
		if (paymentFee != null) {
			this.paymentFee += paymentFee;
		}
	}
}
