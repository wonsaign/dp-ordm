package com.zeusas.dp.ordm.entity;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSON;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.core.entity.IEntity;
import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.active.model.Activity;

@Entity
@Table(name = "bus_reserveRecord")
public class ReserveRecord implements IEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3588661844627756187L;
	
	//打欠：       2
	public static final Integer STATUS_RESERVED=2;
	//还欠中       3(扣库存)
	public static final Integer STATUS_WAIT=3;
	//已经还欠    4
	public static final Integer STATUS_DONE=4;
	//取消还欠    5
	public static final Integer STATUS_CANCLE=5;

	// 订单明细
	@Id
	@Column(name = "ORDERDETAILID", unique = true, nullable = false)
	protected Long orderDetailId;
	// 打欠单号
	@Column(name = "ORDERNO")
	protected String orderNo;
	// 状态
	@Column(name = "status")
	protected Integer status;
	//客户id
	@Column(name = "CUSTOMERID")
	private Integer customerId;
	// 柜台号
	@Column(name = "COUNTERCODE")
	private String counterCode;
	// 产品Id
	@Column(name = "PRODUCTID")
	protected Integer productId;
	// 数量
	@Column(name = "quantity")
	protected Integer quantity;
	// 这个产品的平均价格
	@Column(name = "UNITPRICE")
	private Double unitPrice;
	// 仓库
	@Column(name = "WAREHOUSE")
	protected Integer warehouse;
	// 还欠单号
	@Column(name = "EXCUTEORDERNO")
	protected String excuteOrderNo;
	// 打欠起始时间
	@Column(name = "RESERVESTART")
	private Date reserveStart;
	// 打欠结束时间
	@Column(name = "RESERVEEND")
	private Date reserveEnd;
	// 还欠起始时间
	@Column(name = "EXCUTESTART")
	protected Date excuteStart;
	// 还欠结束时间
	@Column(name = "EXCUTEEND")
	protected Date excuteEnd;
	// 创建时间
	@Column(name = "CREATETIME")
	protected Date createTime;
	// 最后更新
	@Column(name = "LASTUPDATE")
	protected Long lastUpdate;
	//免费数量
	@Column(name="FREEQTY")
	Integer freeQty;
	// 大货是否支持费比 赠品物料是否占用费比
	@Column(name = "COSTRATIO")
	private Boolean costRatio;
	// 关联到活动的id,订单的产品关联到某个活动
	@Column(name = "ACTIVITYID")
	private String activityId;
	/** 订单的父id */
	@Column(name = "PID")
	private Long pid;
	// 预订会标记
	@Column(name = "REVID")
	Integer revId;
	/**订单的发货类型id*/
	@Column(name="DELIVERYWAYID")
	private String deliveryWayId;
	/**订单的发货类型name*/
	@Column(name="DELIVERYWAYNAME")
	private String deliveryWayName;
	// 单品打欠批次
	@Column(name = "BATCHNO")
	private String batchNo;
	//赠送的物料正品为配送价 赠送的大货为会员价*折扣？？？
	@Column(name = "GIFTPRICE")
	private Double giftPrice;
	
	public ReserveRecord() {
		this.createTime = new Date();
		this.lastUpdate = System.currentTimeMillis();
		this.status=STATUS_RESERVED;
	}
	
	public ReserveRecord(OrderDetail orderDetail, Counter counter, ReserveProduct reserveProduct) {
		this();
		this.setOrderDetailInfo(orderDetail, counter);
		
		this.reserveStart =reserveProduct.getReserveStart();
		this.reserveEnd=reserveProduct.getReserveEnd();
		this.excuteStart = reserveProduct.getExcuteStart();
		this.excuteEnd = reserveProduct.getExcuteEnd();
	}
	
	public ReserveRecord(OrderDetail orderDetail, Counter counter, Activity activity) {
		this();
		this.setOrderDetailInfo(orderDetail, counter);
		
		this.reserveStart =activity.getContext().getRevStart();
		this.reserveEnd=activity.getContext().getRevEnd();
		this.excuteStart = activity.getContext().getExecStart();
		this.excuteEnd = activity.getContext().getExecEnd();
	}
	
	public void setOrderDetailInfo(OrderDetail orderDetail, Counter counter) {
		this.orderDetailId = orderDetail.getId();
		this.orderNo = orderDetail.getOrderNo();
		this.productId = orderDetail.getProductId();
		this.quantity = orderDetail.getQuantity();
		this.unitPrice = orderDetail.getUnitPrice();
		this.freeQty = orderDetail.getFreeQty();
		this.costRatio = orderDetail.getCostRatio();
		this.pid=orderDetail.getPid();
		this.revId=orderDetail.getRevId();
		this.deliveryWayId=orderDetail.getDeliveryWayId();
		this.deliveryWayName=orderDetail.getActivityName();
		this.batchNo=orderDetail.getBatchNo();
		this.giftPrice=orderDetail.getGiftPrice();
		
		this.counterCode = counter.getCounterCode();
		this.warehouse = TypeConverter.toInteger(counter.getWarehouses());
		this.customerId = counter.getCustomerId();
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

	public Long getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
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

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getExcuteOrderNo() {
		return excuteOrderNo;
	}

	public void setExcuteOrderNo(String excuteOrderNo) {
		this.excuteOrderNo = excuteOrderNo;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
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

	public Date getExcuteStart() {
		return excuteStart;
	}

	public void setExcuteStart(Date excuteStart) {
		this.excuteStart = excuteStart;
	}

	public Date getExcuteEnd() {
		return excuteEnd;
	}

	public void setExcuteEnd(Date excuteEnd) {
		this.excuteEnd = excuteEnd;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
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

	public Date getReserveStart() {
		return reserveStart;
	}

	public void setReserveStart(Date reserveStart) {
		this.reserveStart = reserveStart;
	}

	public Date getReserveEnd() {
		return reserveEnd;
	}

	public void setReserveEnd(Date reserveEnd) {
		this.reserveEnd = reserveEnd;
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

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Double getGiftPrice() {
		return giftPrice;
	}

	public void setGiftPrice(Double giftPrice) {
		this.giftPrice = giftPrice;
	}

	@Override
	public int hashCode() {
		return orderDetailId == null ? 0 : orderDetailId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().isInstance(this)) {
			return false;
		}
		ReserveRecord record = (ReserveRecord) obj;
		return Objects.equals(this.orderDetailId, record.orderDetailId)//
				&& Objects.equals(this.orderNo, record.orderNo)//
				&& Objects.equals(this.status, record.status)//
				&& Objects.equals(this.productId, record.productId)//
				&& Objects.equals(this.quantity, record.quantity)//
				&& Objects.equals(this.warehouse, record.warehouse)//
				&& Objects.equals(this.excuteOrderNo, record.excuteOrderNo);
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
