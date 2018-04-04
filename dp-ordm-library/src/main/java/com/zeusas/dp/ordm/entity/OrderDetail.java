package com.zeusas.dp.ordm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

/**
 * Orderdetail entity. @author fx 订单的明细，包含了订单的所有产品，包含产品、赠品、物料以及活动产品
 */
@Entity
@Table(name = "bus_orderdetail")
public class OrderDetail implements IEntity {

	private static final long serialVersionUID = 7842614153553403716L;
	// 购买
	public static final int TYPE_BUY = 0;
	// 打欠
	public static final int TYPE_RESERVE = 1;
	// 还欠	
	public static final int TYPE_DONE = 2;
	// 补货	
	public static final int TYPE_REPLENISHMENT = 3;
	// 有库存打欠（同一单子打欠 同一单还）	
	public static final int TYPE_RESERVE_THEN_DONE = 4;
	//积分兑换
	public static final int TYPE_REDEEMPOINTS = 5;

	// 主键自增的id
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	// 关联的订单id
	@Column(name = "ORDERNO")
	private String orderNo;
	
	// 该行产品在这个订单的位置
	@Column(name = "LINENUMBER")
	private Integer lineNumber;
	
	// 关联的产品的id
	@Column(name = "PRODUCTID")
	private Integer productId;
	
	// 产品的编码
	@Column(name = "UNITCODE")
	private String unitCode;
	
	// 产品名称
	@Column(name = "PRODUCTNAME")
	private String productName;
	
	// 产品的数量
	@Column(name = "Quantity")
	private Integer quantity;
	
	// 这个产品的平均价格
	@Column(name = "UNITPRICE")
	private Double unitPrice;
	
	//产品的会员价
	@Column(name="MEMBERPRICE")
	private Double memberPrice;

	// 这个产品的物流编码
	@Column(name = "LOGITICSCODE")
	private String logiticsCode;
	
	// 判断订单里面的产品是正常产品(正常产品里面包含了赠品、物料以及产品)还有活动产品
	@Column(name = "TYPEID")
	private String typeId;
	
	// 关联到活动的id,订单的产品关联到某个活动
	@Column(name = "ACTIVITYID")
	private String activityId;

	@Column(name = "ACTIVITYNAME")
	private String activityName;
	
	/**订单的父id*/
	@Column(name="PID")
	private Long pid;
	
	/**真实发货数量*/
	@Column(name="REALQTY")
	Integer realQty;
	
	/**订单的发货类型id*/
	@Column(name="DELIVERYWAYID")
	private String deliveryWayId;
	/**订单的发货类型name*/
	@Column(name="DELIVERYWAYNAME")
	private String deliveryWayName;
	
	/** 订单套装的数量*/
	@Column(name="SuitNumber")
	private Integer suitNumber;
	//明细类型：购买 打欠 还欠 补货
	@Column(name="detailType")
	private Integer detailType;
	
	/**免费数量*/
	@Column(name="FREEQTY")
	Integer freeQty;
	
	// 大货是否支持费比 赠品物料是否占用费比
	@Column(name = "COSTRATIO")
	private Boolean costRatio;
	
	// 预订会标记
	@Column(name = "revId")
	Integer revId;
	//单品打欠批次号
	@Column(name = "BATCHNO")
	private String batchNo;
	// 赠送的物料正品为配送价 赠送的大货为会员价*折扣？？？
	@Column(name = "GIFTPRICE")
	private Double giftPrice;

	public OrderDetail() {
		this.realQty=0;
		this.freeQty=0;
	}

	public OrderDetail(Product product,CartDetail cartDetail,CartDetailDesc desc,String orderNo) {
		this();
		this.activityId=cartDetail.getActivityId();
		this.activityName=cartDetail.getActivityName();
		// 订单每个产品的数量  都是明细的数量*明细描述的数量
		this.quantity=((int) (desc.getQuantity() * cartDetail.getQuantity()));
		this.orderNo=orderNo;
		this.productId=desc.getProductId();
		this.typeId=product.getTypeId();
		this.unitCode=product.getProductCode();
		this.productName=product.getName();
		this.suitNumber=cartDetail.getQuantity();
		// 每条订单的明细的发货方式又购物车里面的发货方式决定
		this.batchNo=desc.getBatchNo();
	}
	
	public OrderDetail(Product product,String orderNo,Integer qty) {
		this();
		this.quantity=qty;
		this.orderNo=orderNo;
		this.productId=product.getProductId();
		this.typeId=product.getTypeId();
		this.unitCode=product.getProductCode();
		this.productName=product.getName();
		this.logiticsCode=product.getLogisticsCode();
	}

	/**
	 * 取得：订单的ID（父ＩＤ）
	 * 
	 * @return 订单的ID
	 */
	public Long getPid() {
		return pid;
	}

	/**
	 * 设定：订单的ID（父ＩＤ）
	 * @param pid 订单ID
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
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

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getLogiticsCode() {
		return logiticsCode;
	}

	public void setLogiticsCode(String logiticsCode) {
		this.logiticsCode = logiticsCode;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId==null?null:typeId.intern();
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Double getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(Double memberPrice) {
		this.memberPrice = memberPrice;
	}
	
	public void setRealQty(Integer realQty) {
		this.realQty = realQty;
	}

	public Integer getRealQty() {
		return this.realQty;
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

	public Integer getSuitNumber() {
		return suitNumber;
	}

	public void setSuitNumber(Integer suitNumber) {
		this.suitNumber = suitNumber;
	}

	public Integer getDetailType() {
		return detailType == null ? TYPE_BUY : detailType;
	}

	public void setdetailType(Integer reserve) {
		this.detailType = reserve;
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

	public String toString(){
		return JSON.toJSONString(this);
	}
}