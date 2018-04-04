package com.zeusas.dp.ordm.bean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.http.HttpUtil;
import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderDetail;

public class OrderBean {
	
	private Order order;
	
	private String id;
	// 订单编码
	private String orderNo;
	//门店名称
	private String counterName;
	// 订单的总价
	/***实际要支付金额*/
	private String paymentFee;
	// 商品总价
	private String productPrice;
	//物流的费用
	private String expressFee;
	//额外的物料费用
	private String materialFee;
	// 订单关联到数据字典的订单支付类型
	private String payType;
	// 订单关联到数据字典的订单状态类型
	private String orderStatus;
	// 订单的备注
	private String description;
	//订单图片路径
	private List<String> imageURL = new ArrayList<>();
	//有无图片
	private String img;
	/***实际支付总金额*/
	private String paymentPrice;
	/**财务审核订单的备注*/
	private String checkDesc;
	//客户名
	private String customerName;
	//客类型
	private String customerType;
	//创建时间
	private String orderCreatTime;
	//实际发货款
	private String realFee;
	//可用余额
	private String usefulBalance;
	//支付时间
	private String orderPayTime;
	//最后更新
	private String lastUpdate;
	
	private List<OrderDetail> orderDetails;
	
	
	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public OrderBean() {
	}

	public OrderBean(Order order) {
		init(order);
	}
	
	public OrderBean(Order order,List<OrderDetail> orderDetails) {
		init(order);
		this.orderDetails=orderDetails==null?new ArrayList<>(0):orderDetails;
	}
	
	void init(Order order){
		this.order = order;
		this.id = order.getId()==null?"":order.getId().toString();
		this.orderNo = order.getOrderNo()==null?"":order.getOrderNo();
		this.paymentFee=order.getPaymentFee()==null?"":order.getPaymentFee().toString();
		this.counterName = order.getCounterName()==null?"":order.getCounterName();
		this.expressFee = order.getExpressFee()==null?"":order.getExpressFee().toString();
		this.materialFee = order.getMaterialFee()==null?"":order.getMaterialFee().toString();
		this.description = order.getDescription()==null?"":order.getDescription();
		this.payType = order.getPayTypeId()==null?"":order.getPayTypeId();
		this.orderStatus = order.getOrderStatus()==null?"":order.getOrderStatus();
		this.counterName =order.getCounterName()==null?"":order.getCounterName();
		this.imageURL = order.getImageURL();
		this.img=order.getImageURL().isEmpty()?"无":"有";
		Double price=order.getProductFee();
		DecimalFormat df = new DecimalFormat("#0.00");   
		this.productPrice = df.format(price);
		Double fee=order.getRealFee();
		this.realFee=fee==null?"":df.format(fee);
		this.paymentPrice=order.getPaymentPrice()==null?"":order.getPaymentPrice().toString();
		this.checkDesc=order.getCheckDesc()==null?"":order.getCheckDesc();
		this.orderCreatTime =order.getOrderCreatTime()==null//
				?"":DateTime.formatDate(DateTime.YYYY_MM_DD_HMS, new Date(order.getOrderCreatTime()));
		this.orderPayTime=order.getOrderPayTime()==null//
				?"":DateTime.formatDate(DateTime.YYYY_MM_DD_HMS, new Date(order.getOrderPayTime()));
		this.lastUpdate=order.getLastUpdate()==null//
				?"":DateTime.formatDate(DateTime.YYYY_MM_DD_HMS, new Date(order.getLastUpdate()));
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getId() {
		return id==null?"":id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo==null?"":orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCounterName() {
		return counterName==null?"":counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public String getPaymentFee() {
		return paymentFee==null?"":paymentFee;
	}

	public void setPaymentFee(String paymentFee) {
		this.paymentFee = paymentFee;
	}

	public String getProductPrice() {
		return productPrice==null?"":productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getExpressFee() {
		return expressFee==null?"":expressFee;
	}

	public void setExpressFee(String expressFee) {
		this.expressFee = expressFee;
	}

	public String getMaterialFee() {
		return materialFee==null?"":materialFee;
	}

	public void setMaterialFee(String materialFee) {
		this.materialFee = materialFee;
	}

	public String getDescription() {
		return description==null?"":description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPayType() {
		return payType==null?"":payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getOrderStatus() {
		return orderStatus==null?"":orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


	public List<String> getImageURL() {
		return imageURL == null ? new ArrayList<>(0) : imageURL;
	}

	public void setImageURL(List<String> imageURL) {
		this.imageURL = imageURL;
	}

	public String getImg() {
		return img==null?"":img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getPaymentPrice() {
		return paymentPrice==null?"":paymentPrice;
	}

	public void setPaymentPrice(String paymentPrice) {
		this.paymentPrice = paymentPrice;
	}

	public String getCheckDesc() {
		return checkDesc==null?"":checkDesc;
	}

	public void setCheckDesc(String checkDesc) {
		this.checkDesc = checkDesc;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getOrderCreatTime() {
		return orderCreatTime;
	}

	public void setOrderCreatTime(String orderCreatTime) {
		this.orderCreatTime = orderCreatTime;
	}

	public String getRealFee() {
		return realFee;
	}

	public void setRealFee(String realFee) {
		this.realFee = realFee;
	}

	public String getUsefulBalance() {
		return usefulBalance;
	}

	public void setUsefulBalance(Double usefulBalance) {
		DecimalFormat df = new DecimalFormat("#0.00");   
		this.usefulBalance = df.format(usefulBalance);
	}

	public String getOrderPayTime() {
		return orderPayTime;
	}

	public void setOrderPayTime(String orderPayTime) {
		this.orderPayTime = orderPayTime;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
}
