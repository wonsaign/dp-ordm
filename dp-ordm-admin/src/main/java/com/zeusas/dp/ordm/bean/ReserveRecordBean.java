package com.zeusas.dp.ordm.bean;

import java.util.Date;

import com.zeusas.dp.ordm.entity.ReserveRecord;

public class ReserveRecordBean {
	// 打欠单号
	private String orderNo;
	// 状态
	private Integer status;
	//客户id
	private Integer customerId;
	// 客户名称
	private String customerName;
	// 柜台号
	private String counterCode;
	// 柜台名称
	private String counterName;
	// 产品Id
	private Integer productId;
	// 产品名称
	private String productName;
	// 产品编码
	private String productCode;
	// 数量
	private Integer quantity;
	// 仓库
	private Integer warehouse;
	// 仓库名称
	private String warehouseName;
	// 还欠单号
	private String excuteOrderNo;
	// 创建时间
	private Date createTime;
	// 最后更新
	private Long lastUpdate;
	// 免费数量
	private Integer freeQty;
	// 大货是否支持费比 赠品物料是否占用费比
	private Boolean costRatio;
	// 关联到活动的id,订单的产品关联到某个活动
	private String activityId;
	/** 订单的父id */
	private Long pid;
	// 预订会标记
	private Integer revId;
	/**订单的发货类型id*/
	private String deliveryWayId;
	/**订单的发货类型name*/
	private String deliveryWayName;
	
	public ReserveRecordBean() {}
	
	public ReserveRecordBean(ReserveRecord record) {
		this.orderNo = record.getOrderNo();
		this.status = record.getStatus();
		this.customerId = record.getCustomerId();
		this.counterCode = record.getCounterCode();
		this.productId = record.getProductId();
		this.quantity = record.getQuantity();
		this.warehouse = record.getWarehouse();
		this.excuteOrderNo = record.getExcuteOrderNo();
		this.createTime = record.getCreateTime();
		this.lastUpdate = record.getLastUpdate();
		this.freeQty = record.getFreeQty();
		this.costRatio = record.getCostRatio();
		this.activityId = record.getActivityId();
		this.pid = record.getPid();
		this.revId = record.getRevId();
		this.deliveryWayId= record.getDeliveryWayId();
		this.deliveryWayName = record.getDeliveryWayName();
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getCounterCode() {
		return counterCode;
	}
	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}
	public String getCounterName() {
		return counterName;
	}
	public void setCounterName(String counterName) {
		this.counterName = counterName;
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
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Integer warehouse) {
		this.warehouse = warehouse;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public String getExcuteOrderNo() {
		return excuteOrderNo;
	}
	public void setExcuteOrderNo(String excuteOrderNo) {
		this.excuteOrderNo = excuteOrderNo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public Integer getFreeQty() {
		return freeQty;
	}
	public void setFreeQty(Integer freeQty) {
		this.freeQty = freeQty;
	}
	public Boolean getCostRatio() {
		return costRatio;
	}
	public void setCostRatio(Boolean costRatio) {
		this.costRatio = costRatio;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public Integer getRevId() {
		return revId;
	}
	public void setRevId(Integer revId) {
		this.revId = revId;
	}
	public String getDeliveryWayId() {
		return deliveryWayId;
	}
	public void setDeliveryWayId(String deliveryWayId) {
		this.deliveryWayId = deliveryWayId;
	}
	public String getDeliveryWayName() {
		return deliveryWayName;
	}
	public void setDeliveryWayName(String deliveryWayName) {
		this.deliveryWayName = deliveryWayName;
	}
}
