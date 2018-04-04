package com.zeusas.dp.ordm.bean;

import java.util.List;
import java.util.Map;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.entity.Product;

public class OrderBean {

	private String formatTime;
	
	private List<Order> order;
	
	private Dictionary orderType;
	
	private OrderDetail orderDetail;

	private Product product;
	
	private Order singleOrder;
	
	private String excuteOrderNo;
	
	private double amt ;

	private Map<Integer, List<OrderDetail>> detailGroup;

	public OrderBean(){}
	
	// 订单  总额
	public OrderBean(Order o , double d) {
		this.singleOrder = o;
		this.amt = d;
	}
	public OrderBean(List<Order> order,
			Dictionary orderType) {
		this.order = order;
		this.orderType = orderType;
	}
	//订单与时间
	public OrderBean(Order singleOrder,String formatTime) {
		this.formatTime = formatTime;
		this.singleOrder = singleOrder;
	}
	
	//产品 与 细节
	public OrderBean(Product product,OrderDetail orderDetail) {
		this.orderDetail = orderDetail;
		this.product = product;
	}
	//产品 ,明细,预购产品状态
	public OrderBean(Product product,OrderDetail orderDetail,String excuteOrderNo) {
		this.orderDetail = orderDetail;
		this.product = product;
		this.excuteOrderNo = excuteOrderNo;
	}
	//产品 与 细节组
	public OrderBean(Product product,Map<Integer,List<OrderDetail>> detailGroup) {
		this.detailGroup = detailGroup;
		this.product = product;
	}
	//订单 与 类型
	public OrderBean(Order singleOrder,
			Dictionary orderType) {
		this.singleOrder = singleOrder;
		this.orderType = orderType;
	}
	
	
	public OrderDetail getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(OrderDetail orderDetail) {
		this.orderDetail = orderDetail;
	}

	public List<Order> getOrder() {
		return order;
	}

	public void setOrder(List<Order> order) {
		this.order = order;
	}

	public Dictionary getOrderType() {
		return orderType;
	}

	public void setOrderType(Dictionary orderType) {
		this.orderType = orderType;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Order getSingleOrder() {
		return singleOrder;
	}

	public void setSingleOrder(Order singleOrder) {
		this.singleOrder = singleOrder;
	}

	public Map<Integer, List<OrderDetail>> getDetailGroup() {
		return detailGroup;
	}

	public void setDetailGroup(Map<Integer, List<OrderDetail>> detailGroup) {
		this.detailGroup = detailGroup;
	}

	public String getFormatTime() {
		return formatTime;
	}

	public void setFormatTime(String formatTime) {
		this.formatTime = formatTime;
	}

	public String getReserveRecordStatus() {
		return excuteOrderNo;
	}

	public void setReserveRecordStatus(String excuteOrderNo) {
		this.excuteOrderNo = excuteOrderNo;
	}

	public String getExcuteOrderNo() {
		return excuteOrderNo;
	}

	public void setExcuteOrderNo(String excuteOrderNo) {
		this.excuteOrderNo = excuteOrderNo;
	}

	public double getAmt() {
		return amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	
 }
