package com.zeusas.dp.ordm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;
import com.zeusas.core.utils.StringUtil;

/**
 * 
 * @author fengx
 * @date 2017年1月10日 下午5:54:32
 */
@Entity
@Table(name = "bus_package")
public class IPackage implements IEntity {

	final public static Integer STATUS_PUSHWAIT = 0;
	final	public static Integer STATUS_PUSHSUCCESS = 1;
	
	// 单号编码字典
	public static final String BILL_CODE = "210";
	// 销售单
	public static final String BILL_BXX = "BXX";
	// 调拨单
	public static final String BILL_BDD = "BDD";
	// 打欠单
	public static final String BILL_DQ = "DQ";
	// 还欠单
	public static final String BILL_HQ = "HQ";
	// 补货
	public static final String BILL_BH = "BH";
	// 预订会
	public static final String BILL_YDH = "YDH";
	// 打欠取消
	public static final String BILL_QX = "QX";
	// 积分兑换
	public static final String BILL_JF = "JF";
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4031677730791787326L;
	/** 主键 */
	@Id
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	/** 订单号 */
	@Column(name = "ORDERNO")
	private String orderNo;
	//金蝶 订单号 
	@Column(name = "BILLNO")
	private String billNo;
	//柜台ID
	@Column(name="COUNTERID")
	private Integer counterId;
	/** 仓库编码 */
	@Column(name = "WAREHOUSECODE")
	private String warehouseCode;
	/** 仓库名称 */
	@Column(name = "WAREHOUSENAME")
	private String warehouseName;
	/** 产品总金额 */
	@Column(name = "TOTALAMOUNT")
	private Double totalAmount;
	/** 总数量 */
	@Column(name = "TOTALQTY")
	private Integer totalQty;
	/**包裹的状态*/
	@Column(name="STATUS")
	private Integer status;
	/**订单的消息*/
	@Column(name="MESSAGE")
	private  String message ;
	/**创建时间*/
	@Column(name="CREATETIME")
	private Long createTime;
	/**更新时间*/
	@Column(name="UPDATETIME")
	private Long updateTime;
	@Column(name="DESCRIPTION")
	private String description;
	

	public IPackage() {
	}

	public IPackage(Order order) {
		//this.id = id;
		this.orderNo = order.getOrderNo();
		//this.warehouseCode = warehouseCode;
		//this.warehouseName = warehouseName;
		this.totalAmount = order.getPaymentFee();
		this.totalQty = order.getTotalNum();
		this.createTime=System.currentTimeMillis();
		this.status=STATUS_PUSHWAIT;
		this.counterId=order.getCounterId();
		if(StringUtil.isEmpty(order.getDescription())){
			// 确保金蝶不出错
			this.description = "无";
		}else{
			this.description=order.getDescription();
		}
	}
	

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getWarehouseCode() {
		return this.warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getWarehouseName() {
		return this.warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public Double getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getTotalQty() {
		return this.totalQty;
	}

	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}
	

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return id == null? 0 : id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IPackage other = (IPackage) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (orderNo == null) {
			if (other.orderNo != null)
				return false;
		} else if (!orderNo.equals(other.orderNo))
			return false;
		if (totalAmount == null) {
			if (other.totalAmount != null)
				return false;
		} else if (!totalAmount.equals(other.totalAmount))
			return false;
		if (totalQty == null) {
			if (other.totalQty != null)
				return false;
		} else if (!totalQty.equals(other.totalQty))
			return false;
		if (warehouseCode == null) {
			if (other.warehouseCode != null)
				return false;
		} else if (!warehouseCode.equals(other.warehouseCode))
			return false;
		if (warehouseName == null) {
			if (other.warehouseName != null)
				return false;
		} else if (!warehouseName.equals(other.warehouseName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}