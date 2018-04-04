package com.zeusas.dp.ordm.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;
import com.zeusas.security.auth.entity.AuthUser;

/**
 * Order entity. @author fx 订单的单头，主要包含了制单人、关联的门店、关联的门店店主、付款人、以及订单的简单信息
 */
@Entity
@Table(name = "bus_order")
public class Order implements IEntity {

	private static final long serialVersionUID = -8376670647221392289L;

	/* 订单硬码 */
	public final static String Status_HardCode = "100";
	/* 订单无效 */
	public final static String Status_Invalid = "0";
	/* 主管待审核 */
	public final static String Status_ForOwnerCheck = "1";
	/* 订单未付款 */
	public final static String Status_UnPay = "2";
	/* 订单付款 */
	public final static String Status_DoPay = "3";
	/* 待财务审核 */
	// public final static String status_ForFinancialCheck="4";
	/* 待推送ERP */
	public final static String status_LogisticsDelivery = "5";
	/* 已推送ERP(待发货) */
	public final static String status_DoLogisticsDelivery = "6";
	/* ERP已审核 */
	public final static String status_WaitShip = "7";
	/* 待门店接收货物 */
	// public final static String status_Shipping="8";
	/* 确认完成订单 */
	public final static String status_CompleteShipping = "9";
	/* 财务审核不通过 */
	public final static String status_ForFinancialRefuse = "10";
	/* 不显示订单 */
	public final static String status_NotDisplay = "11";
	/* 订单最大邮费默认值 */
	public final static int MaxPostage = 150;
	/* 订单区间邮费默认值 */
	public final static int MidPostage = 100;
	/* 订单免邮 */
	public final static int NoPostage = 0;
	/* 0-XXX邮费为XXX */
	public final static double Miniprice = 5000;
	/* xxx-XXX邮费为XXX */
	public final static double Maxprice = 10000;

	/* 支付类型id */
	public final static String PayType_root = "203";
	/* 支付宝 */
	public final static String PayType_aliPay = "1";
	/* 银联支付 */
	public final static String PayType_unionPay = "2";
	/* 线下支付 */
	public final static String PayType_offlinePay = "3";
	/* 直营付款 */
	public final static String PayType_directOrder = "4";
	/* 余额付款 */
	public final static String PayType_balancePay = "5";
	/* 合并支付 */
	public final static String PayType_combinedPay = "6";
	/* 民生付 */
	public final static String PayType_cmbcPay = "7";
	/* 系统支付 */
	public final static String PayType_system = "8";
	/* 订单差分未做 */
	public final static Integer diffStatus_todo = 0;
	/* 订单差分已做 */
	public final static Integer diffStatus_doen = 1;
	/* 订单差分不做 */
	public final static Integer diffStatus_undo = 2;
	/* 预订会标记 */
	public final static Integer TYPE_RESERVEACTIVITY = 1;

	public final static int PRESENT_TYPE_IMPORT = 1;
	public final static int PRESENT_TYPE_COMBINE = 2;
	public final static int PRESENT_TYPE_NORMAL = 0;

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	// 订单编码
	@Column(name = "ORDERNO")
	private String orderNo;
	// 关联的门店id
	@Column(name = "COUNTERID")
	private Integer counterId;
	// 门店名称
	@Column(name = "COUNTERNAME")
	private String counterName;
	// 门店的编码
	@Column(name = "COUNTERCODE")
	private String counterCode;
	// 关联的门店店长id
	@Column(name = "CUSTOMERID")
	private String customerId;
	// 主管(审核人的名字)
	@Column(name = "CUSTOMERNAME")
	private String customerName;
	// 关联的下单人的id
	@Column(name = "USERID")
	private String userId;
	// 下单人的名字
	@Column(name = "USERNAME")
	private String userName;
	// 关联的支付人的id
	@Column(name = "PAYMANID")
	private String payManId;
	// 付款人的名字
	@Column(name = "PAYMANNAME")
	private String payManName;
	// 订单产品原价金额
	@Column(name = "ORDERORIGINALFEE")
	private Double orderOriginalFee;
	// 客户实际支付金额
	@Column(name = "PAYMENTPRICE")
	private Double paymentPrice;
	// 订单生成后的实际金额(不可修改)
	@Column(name = "PAYMENTFEE")
	private Double paymentFee;
	// 订单生成后的产品的金额
	@Column(name = "PRODUCTFEE")
	private Double productFee;
	// 物流的费用
	@Column(name = "EXPRESSFEE")
	private Double expressFee;
	// 额外的物料费用
	@Column(name = "MATERIALFEE")
	private Double materialFee;
	// 免费赠送的的物料金额
	@Column(name = "MATERIALFREEAMT")
	private Double materialFreeAmt;
	// 订单商品的总数量
	@Column(name = "TOTALNUM")
	private Integer totalNum;
	// 定义订单是直营还是加盟店下的订单
	@Column(name = "C_CATAGORY")
	private String ccatagory;
	// 订单的地址
	@Column(name = "ADDRESS")
	private String address;
	// 订单联系方式
	@Column(name = "CONTACT")
	private String contact;
	// 订单电话
	@Column(name = "PHONE")
	private String phone;
	// 订单的仓库
	@Column(name = "STORAGE")
	private String storage;
	// 订单的备注
	@Column(name = "DESCRIPTION")
	private String description;
	// 财务审核订单的备注
	@Column(name = "CHECKDESC")
	private String checkDesc;
	// 订单的创建时间
	@Column(name = "ORDERCREATTIME")
	private Long orderCreatTime;
	// 订单支付时间
	@Column(name = "ORDERPAYTIME")
	private Long orderPayTime;
	// 订单取消时间
	@Column(name = "ORDERCANCELTIME")
	private Long orderCancelTime;
	// 订单的最后更新时间
	@Column(name = "LASTUPDATE")
	private Long lastUpdate;
	// 订单关联到数据字典的订单支付类型
	@Column(name = "PAYTYPEID")
	private String payTypeId;
	// 订单关联到数据字典的订单状态类型
	@Column(name = "ORDERSTATUS")
	private String orderStatus;
	// 订单图片路径
	@Column(name = "IMAGEURL")
	@Type(type = "com.zeusas.core.entity.StringListType")
	private List<String> imageURL;
	// 订单是否合并
	@Column(name = "ISMERGER")
	private boolean isMerger;
	// 订单关联的凭据
	@Column(name = "CREDENTIALSNO")
	private String credentialsNo;// 关联bus_ordercredentials表中主键ocid
	// 活动记录
	@Column(name = "ACTIVRECORD")
	@Type(type = "com.zeusas.core.entity.StringListType")
	private List<String> activRecord;
	// k3订单金额
	@Column(name = "REALFEE")
	private Double realFee;

	// 该订单使用的余额
	@Column(name = "USEBALANCE")
	private Double useBalance;

	// 实际应付 应付总价-使用余额
	@Column(name = "PAYABLE")
	private Double payable;
	// 金蝶审核时间
	@Column(name = "AUDITTIME")
	private Date auditTime;
	// 订单差分状态
	@Column(name = "DIFFSTATUS")
	private Integer diffStatus;
	// 订单作废原因
	@Column(name = "CANCELDESC")
	private String cancelDesc;
	// 是否为系统导入订单 0,null不是 ，1是
	@Column(name = "ISSYSIN")
	private Integer issysin;
	// 正常产品金额
	@Column(name = "NORMALAMT")
	private Double normalAmt;
	// 打欠产品金额
	@Column(name = "RESERVEAMT")
	private Double reserveAmt;
	// 正常产品使用的余额
	@Column(name = "NORMALBALANCE")
	private Double normalBalance;
	// 打欠产品使用的余额
	@Column(name = "RESERVEBALANCE")
	private Double reserveBalance;
	// 预订会还欠金额
	@Column(name = "ROWEAMT")
	private Double rOweAmt;
	// 普通打欠单还欠金额
	@Column(name = "YOWEAMT")
	private Double yOweAmt;
	// 预订会标记
	@Column(name = "reserveFlag")
	private Integer reserveFlag;

	public List<String> getImageURL() {
		return imageURL == null ? new ArrayList<>(0) : imageURL;
	}

	public void setImageURL(List<String> imageURL) {
		this.imageURL = imageURL;
	}

	public void addImageURL(String url) {
		if (this.imageURL == null) {
			this.imageURL = new ArrayList<>();
		}
		this.imageURL.add(url);
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

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPayManId() {
		return payManId;
	}

	public void setPayManId(String payManId) {
		this.payManId = payManId;
	}

	public Double getOrderOriginalFee() {
		return orderOriginalFee;
	}

	public void setOrderOriginalFee(Double orderOriginalFee) {
		this.orderOriginalFee = orderOriginalFee;
	}

	public Double getPaymentFee() {
		return paymentFee;
	}

	public void setPaymentFee(Double paymentFee) {
		this.paymentFee = paymentFee;
	}

	public Double getProductFee() {
		return productFee;
	}

	public void setProductFee(Double productFee) {
		this.productFee = productFee;
	}

	public Double getMaterialFee() {
		return materialFee;
	}

	public void setMaterialFee(Double materialFee) {
		this.materialFee = materialFee;
	}

	public Double getMaterialFreeAmt() {
		return materialFreeAmt;
	}

	public void setMaterialFreeAmt(Double materialFreeAmt) {
		this.materialFreeAmt = materialFreeAmt;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public String getCcatagory() {
		return ccatagory;
	}

	public void setCcatagory(String ccatagory) {
		this.ccatagory = ccatagory;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getOrderCreatTime() {
		return orderCreatTime;
	}

	public void setOrderCreatTime(Long orderCreatTime) {
		this.orderCreatTime = orderCreatTime;
	}

	public Long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(String payTypeId) {
		this.payTypeId = payTypeId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPayManName() {
		return payManName;
	}

	public void setPayManName(String payManName) {
		this.payManName = payManName;
	}

	public Double getExpressFee() {
		return expressFee;
	}

	public void setExpressFee(Double expressFee) {
		this.expressFee = expressFee;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPhone() {
		return phone;
	}

	public Double getrOweAmt() {
		return rOweAmt;
	}

	public void setrOweAmt(Double rOweAmt) {
		this.rOweAmt = rOweAmt;
	}

	public Double getyOweAmt() {
		return yOweAmt;
	}

	public void setyOweAmt(Double yOweAmt) {
		this.yOweAmt = yOweAmt;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getCheckDesc() {
		return checkDesc;
	}

	public void setCheckDesc(String checkDesc) {
		this.checkDesc = checkDesc;
	}

	public Double getPaymentPrice() {
		return paymentPrice;
	}

	public void setPaymentPrice(Double paymentPrice) {
		this.paymentPrice = paymentPrice;
	}

	public boolean isMerger() {
		return isMerger;
	}

	// 兼容前台
	public boolean getMerger() {
		return this.isMerger();
	}

	public void setMerger(boolean isMerger) {
		this.isMerger = isMerger;
	}

	public String getCredentialsNo() {
		return credentialsNo;
	}

	public void setCredentialsNo(String credentialsNo) {
		this.credentialsNo = credentialsNo;
	}

	public Long getOrderPayTime() {
		return orderPayTime;
	}

	public void setOrderPayTime(Long orderPayTime) {
		this.orderPayTime = orderPayTime;
	}

	public Long getOrderCancelTime() {
		return orderCancelTime;
	}

	public void setOrderCancelTime(Long orderCancelTime) {
		this.orderCancelTime = orderCancelTime;
	}

	public Double getRealFee() {
		return realFee;
	}

	public void setRealFee(Double realFee) {
		this.realFee = realFee;
	}

	public Double getUseBalance() {
		return useBalance == null ? 0 : useBalance;
	}

	public void setUseBalance(Double useBalance) {
		this.useBalance = useBalance;
	}

	public Double getPayable() {
		return payable;
	}

	public void setPayable(Double payable) {
		this.payable = payable;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getDiffStatus() {
		return diffStatus;
	}

	public void setDiffStatus(Integer diffStatus) {
		this.diffStatus = diffStatus;
	}

	public int getIssysin() {
		return issysin == null ? PRESENT_TYPE_NORMAL : issysin.intValue();
	}

	public void setIssysin(Integer issysin) {
		if (issysin != null) {
			this.issysin = issysin;
		}
	}

	public Double getNormalAmt() {
		return normalAmt;
	}

	public void setNormalAmt(Double normalAmt) {
		this.normalAmt = normalAmt;
	}

	public Double getReserveAmt() {
		return reserveAmt;
	}

	public void setReserveAmt(Double reserveAmt) {
		this.reserveAmt = reserveAmt;
	}

	public Double getNormalBalance() {
		return normalBalance;
	}

	public void setNormalBalance(Double normalBalance) {
		this.normalBalance = normalBalance;
	}

	public Double getReserveBalance() {
		return reserveBalance;
	}

	public void setReserveBalance(Double reserveBalance) {
		this.reserveBalance = reserveBalance;
	}

	public Integer getReserveFlag() {
		return reserveFlag;
	}

	public void setReserveFlag(Integer reserveFlag) {
		this.reserveFlag = reserveFlag;
	}

	/**
	 * 使用余额
	 * 
	 * @param balane
	 *            已经使用的余额
	 * @param payment
	 *            已经实际支付的费用
	 * @param cred
	 *            凭证信息
	 */
	public void doPayment(double balane, double payment, String payTypeId, OrderCredentials cred) {
		// 未使用的情况
		this.payable = payment;
		this.paymentPrice = payment;
		this.useBalance = balane;
		this.lastUpdate = System.currentTimeMillis();

		this.orderStatus = Order.Status_DoPay;
		this.payTypeId = payTypeId;
		this.imageURL = cred.getImgUrl();

		this.payManId = cred.getPayManId();
		this.payManName = cred.getPayManName();
		this.orderPayTime = this.lastUpdate;
	}

	public Order() {
		// 订单初始化状态
		this.orderStatus = Status_UnPay;
		this.orderCreatTime = System.currentTimeMillis();
		this.lastUpdate = System.currentTimeMillis();
		this.expressFee = 0.0;// 初始的邮费为0
		this.materialFee = 0.0;// 初始化的物料费用为0
		// 实际发货金额
		this.realFee = 0.0;
		// 正常产品金额
		this.normalAmt = 0.0;
		// 打欠产品金额
		this.reserveAmt = 0.0;
		// 正常产品使用的余额
		this.normalBalance = 0.0;
		// 打欠产品使用的余额
		this.reserveBalance = 0.0;
		// 预订会还欠金额
		this.rOweAmt = 0.0;
		// 普通打欠单还欠金额
		this.yOweAmt = 0.0;

	}

	public void setCounterInfo(Counter counter) {
		// 初始化一些门店的信息
		this.address = counter.getAddress();
		this.phone = counter.getPhone();
		this.contact = counter.getContact();
		this.counterId = counter.getCounterId();
		this.counterName = counter.getCounterName();
		this.counterCode = counter.getCounterCode();
		
		// 客户信息
		this.customerId = String.valueOf(counter.getCustomerId());

		this.diffStatus = "加盟".equals(counter.getType()) ? 0 : 2;
	}
	
	public Order(AuthUser checkUser, AuthUser makeUser, Counter counter) {
		this();

		// 审核人
		this.userId = makeUser.getLoginName();
		this.userName = makeUser.getCommonName();
		this.customerName = checkUser.getCommonName();
		this.setCounterInfo(counter);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activRecord == null) ? 0 : activRecord.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((auditTime == null) ? 0 : auditTime.hashCode());
		result = prime * result + ((cancelDesc == null) ? 0 : cancelDesc.hashCode());
		result = prime * result + ((ccatagory == null) ? 0 : ccatagory.hashCode());
		result = prime * result + ((checkDesc == null) ? 0 : checkDesc.hashCode());
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
		result = prime * result + ((counterCode == null) ? 0 : counterCode.hashCode());
		result = prime * result + ((counterId == null) ? 0 : counterId.hashCode());
		result = prime * result + ((counterName == null) ? 0 : counterName.hashCode());
		result = prime * result + ((credentialsNo == null) ? 0 : credentialsNo.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((customerName == null) ? 0 : customerName.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((diffStatus == null) ? 0 : diffStatus.hashCode());
		result = prime * result + ((expressFee == null) ? 0 : expressFee.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imageURL == null) ? 0 : imageURL.hashCode());
		result = prime * result + (isMerger ? 1231 : 1237);
		result = prime * result + issysin;
		result = prime * result + ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
		result = prime * result + ((materialFee == null) ? 0 : materialFee.hashCode());
		result = prime * result + ((materialFreeAmt == null) ? 0 : materialFreeAmt.hashCode());
		result = prime * result + ((orderCancelTime == null) ? 0 : orderCancelTime.hashCode());
		result = prime * result + ((orderCreatTime == null) ? 0 : orderCreatTime.hashCode());
		result = prime * result + ((orderNo == null) ? 0 : orderNo.hashCode());
		result = prime * result + ((orderOriginalFee == null) ? 0 : orderOriginalFee.hashCode());
		result = prime * result + ((orderPayTime == null) ? 0 : orderPayTime.hashCode());
		result = prime * result + ((orderStatus == null) ? 0 : orderStatus.hashCode());
		result = prime * result + ((payManId == null) ? 0 : payManId.hashCode());
		result = prime * result + ((payManName == null) ? 0 : payManName.hashCode());
		result = prime * result + ((payTypeId == null) ? 0 : payTypeId.hashCode());
		result = prime * result + ((payable == null) ? 0 : payable.hashCode());
		result = prime * result + ((paymentFee == null) ? 0 : paymentFee.hashCode());
		result = prime * result + ((paymentPrice == null) ? 0 : paymentPrice.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((productFee == null) ? 0 : productFee.hashCode());
		result = prime * result + ((realFee == null) ? 0 : realFee.hashCode());
		result = prime * result + ((storage == null) ? 0 : storage.hashCode());
		result = prime * result + ((totalNum == null) ? 0 : totalNum.hashCode());
		result = prime * result + ((useBalance == null) ? 0 : useBalance.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (activRecord == null) {
			if (other.activRecord != null)
				return false;
		} else if (!activRecord.equals(other.activRecord))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (auditTime == null) {
			if (other.auditTime != null)
				return false;
		} else if (!auditTime.equals(other.auditTime))
			return false;
		if (cancelDesc == null) {
			if (other.cancelDesc != null)
				return false;
		} else if (!cancelDesc.equals(other.cancelDesc))
			return false;
		if (ccatagory == null) {
			if (other.ccatagory != null)
				return false;
		} else if (!ccatagory.equals(other.ccatagory))
			return false;
		if (checkDesc == null) {
			if (other.checkDesc != null)
				return false;
		} else if (!checkDesc.equals(other.checkDesc))
			return false;
		if (contact == null) {
			if (other.contact != null)
				return false;
		} else if (!contact.equals(other.contact))
			return false;
		if (counterCode == null) {
			if (other.counterCode != null)
				return false;
		} else if (!counterCode.equals(other.counterCode))
			return false;
		if (counterId == null) {
			if (other.counterId != null)
				return false;
		} else if (!counterId.equals(other.counterId))
			return false;
		if (counterName == null) {
			if (other.counterName != null)
				return false;
		} else if (!counterName.equals(other.counterName))
			return false;
		if (credentialsNo == null) {
			if (other.credentialsNo != null)
				return false;
		} else if (!credentialsNo.equals(other.credentialsNo))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (diffStatus == null) {
			if (other.diffStatus != null)
				return false;
		} else if (!diffStatus.equals(other.diffStatus))
			return false;
		if (expressFee == null) {
			if (other.expressFee != null)
				return false;
		} else if (!expressFee.equals(other.expressFee))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageURL == null) {
			if (other.imageURL != null)
				return false;
		} else if (!imageURL.equals(other.imageURL))
			return false;
		if (isMerger != other.isMerger)
			return false;
		if (issysin != other.issysin)
			return false;
		if (lastUpdate == null) {
			if (other.lastUpdate != null)
				return false;
		} else if (!lastUpdate.equals(other.lastUpdate))
			return false;
		if (materialFee == null) {
			if (other.materialFee != null)
				return false;
		} else if (!materialFee.equals(other.materialFee))
			return false;
		if (materialFreeAmt == null) {
			if (other.materialFreeAmt != null)
				return false;
		} else if (!materialFreeAmt.equals(other.materialFreeAmt))
			return false;
		if (orderCancelTime == null) {
			if (other.orderCancelTime != null)
				return false;
		} else if (!orderCancelTime.equals(other.orderCancelTime))
			return false;
		if (orderCreatTime == null) {
			if (other.orderCreatTime != null)
				return false;
		} else if (!orderCreatTime.equals(other.orderCreatTime))
			return false;
		if (orderNo == null) {
			if (other.orderNo != null)
				return false;
		} else if (!orderNo.equals(other.orderNo))
			return false;
		if (orderOriginalFee == null) {
			if (other.orderOriginalFee != null)
				return false;
		} else if (!orderOriginalFee.equals(other.orderOriginalFee))
			return false;
		if (orderPayTime == null) {
			if (other.orderPayTime != null)
				return false;
		} else if (!orderPayTime.equals(other.orderPayTime))
			return false;
		if (orderStatus == null) {
			if (other.orderStatus != null)
				return false;
		} else if (!orderStatus.equals(other.orderStatus))
			return false;
		if (payManId == null) {
			if (other.payManId != null)
				return false;
		} else if (!payManId.equals(other.payManId))
			return false;
		if (payManName == null) {
			if (other.payManName != null)
				return false;
		} else if (!payManName.equals(other.payManName))
			return false;
		if (payTypeId == null) {
			if (other.payTypeId != null)
				return false;
		} else if (!payTypeId.equals(other.payTypeId))
			return false;
		if (payable == null) {
			if (other.payable != null)
				return false;
		} else if (!payable.equals(other.payable))
			return false;
		if (paymentFee == null) {
			if (other.paymentFee != null)
				return false;
		} else if (!paymentFee.equals(other.paymentFee))
			return false;
		if (paymentPrice == null) {
			if (other.paymentPrice != null)
				return false;
		} else if (!paymentPrice.equals(other.paymentPrice))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (productFee == null) {
			if (other.productFee != null)
				return false;
		} else if (!productFee.equals(other.productFee))
			return false;
		if (realFee == null) {
			if (other.realFee != null)
				return false;
		} else if (!realFee.equals(other.realFee))
			return false;
		if (storage == null) {
			if (other.storage != null)
				return false;
		} else if (!storage.equals(other.storage))
			return false;
		if (totalNum == null) {
			if (other.totalNum != null)
				return false;
		} else if (!totalNum.equals(other.totalNum))
			return false;
		if (useBalance == null) {
			if (other.useBalance != null)
				return false;
		} else if (!useBalance.equals(other.useBalance))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	public List<String> getActivRecord() {
		return activRecord == null ? new ArrayList<>(0) : activRecord;
	}

	public void setActivRecord(List<String> activRecord) {
		this.activRecord = activRecord;
	}

	public String getCancelDesc() {
		return cancelDesc;
	}

	public void setCancelDesc(String cancelDesc) {
		this.cancelDesc = cancelDesc;
	}

	public void addActivRecord(String ActId) {
		if (this.activRecord == null) {
			this.activRecord = new ArrayList<>();
		}
		this.activRecord.add(ActId);
	}

	public void addActivRecord(List<String> ActIds) {
		if (this.activRecord == null) {
			this.activRecord = new ArrayList<>();
		}
		this.activRecord.addAll(ActIds);
	}

	public String toString() {
		return JSON.toJSONString(this);
	}

}