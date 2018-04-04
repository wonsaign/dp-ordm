package com.zeusas.dp.ordm.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

@Entity
@Table(name = "bus_ordercredentials")
public class OrderCredentials implements IEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 2981159318812569286L;

	// 订单凭证的id
	@Id
	@Column(name = "OCID")
	private String ocid;
	// 订单的编号
	@Column(name = "ORDERNO")
	@Type(type = "com.zeusas.core.entity.StringListType")
	private final List<String> orderNo;
//	private  String orderNo;
	// 门店的id
	@Column(name = "COUNTERID")
	private Integer counterId;
	// 门店的名称
	@Column(name = "COUNTERNAME")
	private String counterName;
	// 关联的门店店长id
	@Column(name = "CUSTOMERID")
	private String customerId;
	// 主管(审核人的名字)
	@Column(name = "CUSTOMERNAME")
	private String customerName;
	// 关联的下单人的id
	@Column(name = "CREATORID")
	private String creatorId;
	// 下单人的名字
	@Column(name = "CREATORNAME")
	private String creatorName;
	// 关联的支付人的id
	@Column(name = "PAYMANID")
	private String payManId;
	// 付款人的名字
	@Column(name = "PAYMANNAME")
	private String payManName;
	// 订单地址
	@Column(name = "ADDRESS")
	private String address;
	// 产品总数
	@Column(name = "TOTALQTY")
	private Integer totalQty;
	// 正品的总数
	@Column(name = "PRODUCTQTY")
	private Integer productQty;
	// 物料的总数
	@Column(name = "MATERIALQTY")
	private Integer materialQty;
	// 订单的总价
	@Column(name = "TOTALAMT")
	private Double totalAmt;
	// 正品的总金额
	@Column(name = "PRODUCTAMT")
	private Double productAmt;
	// 物料以及赠品的实际支付金额
	@Column(name = "MaterialPay")
	private Double materialPay;
	// 订单所有物料的费用
	@Column(name = "MATERIALAMT")
	private Double materialAmt;
	// 订单配送可赠送最大限额 ，免费物料
	@Column(name = "MATERIALDISCOUNT")
	private Double materialDiscount;
	// 订单的运费
	@Column(name = "POSTAGE")
	private Double postage;
	// 订单支付类型id
	@Column(name = "PAYTYPE")
	private String payType;
	// 订单支付类型名称
	@Column(name = "PAYTYPENAME")
	private String payTypeName;
	// 下单人备注
	@Column(name = "CUSTOMERDESC")
	private String customerDesc;
	// 审核人备注
	@Column(name = "CHECKERDESC")
	private String checkerDesc;
	// 单据的上传图片
	@Column(name = "IMGURL")
	@Type(type = "com.zeusas.core.entity.StringListType")
	private List<String> imgUrl;
	// 订单生成时间
	@Column(name = "STARTTIME")
	private Long startTime;
	// 订单完成时间
	@Column(name = "ENDTIME")
	private Long endTime;
	// 使用余额
	@Column(name = "USEBLANCE")
	private Double useBlance;
	// 实际支付
	@Column(name = "ACTUALPAY")
	private Double actualPay;
	// 关联的合并订单的父ID
	@Column(name = "COMBINEID")
	private String combineId;

	public OrderCredentials() {
		// 总数量
		this.totalQty = 0;
		// 总金额
		this.totalAmt = 0.0;
		// 物料总费用
		this.materialAmt = 0.0;
		// 额外费用
		this.postage = 0.0;
		// 描述
		this.customerDesc = null;
		// 免费物料
		this.materialDiscount = 0.0;
		// 应付物料费用
		this.materialAmt = 0.0;

		// xxx
		this.orderNo = new ArrayList<>();
	}

	public void setOrderInfo(Order order, Counter counter) {
		if(!this.orderNo.contains(order.getOrderNo())){
			this.orderNo.add(order.getOrderNo());
		}
		this.counterId = counter.getCounterId();
		this.counterName = counter.getCounterName();
		this.address = counter.getShippingAddress();
		this.totalQty = order.getTotalNum();
		this.totalAmt = order.getPaymentFee();
		this.materialAmt = order.getMaterialFee();
		this.postage = order.getExpressFee();
		this.payType = order.getPayTypeId();
		this.customerDesc = order.getDescription();
		this.checkerDesc = order.getCheckDesc();
		this.imgUrl = order.getImageURL();
		this.startTime = order.getOrderCreatTime();
		this.customerId = order.getCustomerId();
		this.customerName = order.getCustomerName();
		this.creatorId = order.getUserId();
		this.creatorName = order.getUserName();
	}
	/**
	 * 保持原有的方法
	 * 
	 * @param order
	 * @param counter
	 */
	public OrderCredentials(Order order, Counter counter) {
		this();
		// xxx
		this.setOrderInfo(order , counter);
	}

	/**
	 * 若是一个订单集合，去掉一些属性，每一单都有合并，可任意删除合并 需要单独设置图片路径。
	 * 
	 * @param orders
	 * @param counter
	 */
	public OrderCredentials(List<Order> orders) {
		this();
		for (Order order : orders) {
			// 加入柜台号的集合集合
			// xxx
			this.orderNo.add(order.getOrderNo());
			this.totalQty += order.getTotalNum();
			this.totalAmt += order.getPaymentFee();
			this.materialDiscount += order.getMaterialFreeAmt();
			this.materialAmt += order.getMaterialFee();
			this.postage += order.getExpressFee();

		}
		this.customerName = orders.get(0).getCustomerName();
		this.customerId = orders.get(0).getCustomerId();// 所有合并支付的支付类型都是一样的，默认取第一个.
		this.payType = Order.PayType_combinedPay;// 设置为合并支付
		this.startTime = System.currentTimeMillis();
	}

	public String getOcid() {
		return ocid;
	}

	public void setOcid(String ocid) {
		this.ocid = ocid;
	}

	public List<String> getOrderNos() {
		return orderNo;
	}

	public void setOrderNos(List<String> orderNo) {
		if (orderNo != null) {
			this.orderNo.addAll(orderNo);
		}
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getPayManId() {
		return payManId;
	}

	public void setPayManId(String payManId) {
		this.payManId = payManId;
	}

	public String getPayManName() {
		return payManName;
	}

	public void setPayManName(String payManName) {
		this.payManName = payManName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}

	public Integer getProductQty() {
		return productQty;
	}

	public void setProductQty(Integer productQty) {
		this.productQty = productQty;
	}

	public Integer getMaterialQty() {
		return materialQty;
	}

	public void setMaterialQty(Integer materialQty) {
		this.materialQty = materialQty;
	}

	public Double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}

	public Double getProductAmt() {
		return productAmt;
	}

	public void setProductAmt(Double productAmt) {
		this.productAmt = productAmt;
	}

	public Double getMaterialAmt() {
		return materialAmt;
	}

	public void setMaterialAmt(Double materialAmt) {
		this.materialAmt = materialAmt;
	}

	public Double getMaterialDiscount() {
		return materialDiscount;
	}

	public void setMaterialDiscount(Double materialDiscount) {
		this.materialDiscount = materialDiscount;
	}

	public Double getPostage() {
		return postage;
	}

	public void setPostage(Double postage) {
		this.postage = postage;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public String getCustomerDesc() {
		return customerDesc;
	}

	public void setCustomerDesc(String customerDesc) {
		this.customerDesc = customerDesc;
	}

	public String getCheckerDesc() {
		return checkerDesc;
	}

	public void setCheckerDesc(String checkerDesc) {
		this.checkerDesc = checkerDesc;
	}

	public List<String> getImgUrl() {
		return imgUrl == null ? new ArrayList<>(0) : imgUrl;
	}

	public void setImgUrl(List<String> imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Double getMaterialPay() {
		return materialPay;
	}

	public void setMaterialPay(Double materialPay) {
		this.materialPay = materialPay;
	}

	public Double getUseBlance() {
		return useBlance;
	}

	public void setUseBlance(Double useBlance) {
		this.useBlance = useBlance;
	}

	public Double getActualPay() {
		return actualPay;
	}

	public void setActualPay(Double actualPay) {
		this.actualPay = actualPay;
	}

	public String getCombineId() {
		return combineId;
	}

	public void setCombineId(String combineId) {
		this.combineId = combineId;
	}
	
//	public void setOrderNo(String no){
//		this.orderNo = no;
//	}
//	
//	public String getOrderNo() {
//		return orderNo;
//	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
