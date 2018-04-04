package com.zeusas.dp.ordm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

/**
 * 
 * @author fengx
 * @date 2017年1月10日 下午5:59:15
 */
@Entity
@Table(name = "bus_packagedetail")
public class PackageDetail implements IEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 5946531365897036562L;

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private String id;
	@Column(name = "PACKAGEID")
	private String packageId;
	@Column(name = "LINENUMBER")
	private Integer lineNumber;
	@Column(name = "PRODUCTID")
	private Integer productId;
	@Column(name = "PRODUCTCODE")
	private String productCode;
	@Column(name = "PRODUCTNAME")
	private String productName;
	@Column(name = "UNITPRICE")
	private Double unitPrice;
	@Column(name = "PRODUCTQTY")
	private Integer productQty;
	/** 这个产品的物料编码 */
	@Column(name = "LOGITICSCODE")
	private String logiticsCode;
	/**订单的发货类型id*/
	@Column(name="DELIVERYWAYID")
	private String deliveryWayId;
	/**订单的发货类型name*/
	@Column(name="DELIVERYWAYNAME")
	private String deliveryWayName;
	/** 包裹关联的订单明细id */
	@Column(name="ORDERDETAILID")
	private Long orderDetailId;
	/** 个体户单价 */
	@Column(name="INDIVPRICE")
	private Double IndivPrice;
	//赠送的物料正品为配送价 赠送的大货为会员价*折扣？？？
	@Column(name = "GIFTPRICE")
	private Double giftPrice;
	/**真实发货数量*/
	@Column(name="REALQTY")
	Integer realQty;
	
	public PackageDetail() {
	}
	
	public PackageDetail(DeliveryWay deliveryWay) {
		this.deliveryWayId=deliveryWay.getDeliveryWayId();
		this.deliveryWayName=deliveryWay.getDeliveryName();
	}
	
	public PackageDetail(OrderDetail orderDetail) {
		this.productId = orderDetail.getProductId();
		this.productCode = orderDetail.getUnitCode();
		this.productName = orderDetail.getProductName();
		this.unitPrice = orderDetail.getUnitPrice();
		this.giftPrice = orderDetail.getGiftPrice();
		this.productQty = orderDetail.getQuantity();
		this.logiticsCode = orderDetail.getLogiticsCode();
	}


	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPackageId() {
		return this.packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public Integer getLineNumber() {
		return this.lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getProductQty() {
		return this.productQty;
	}

	public void setProductQty(Integer productQty) {
		this.productQty = productQty;
	}

	public String getLogiticsCode() {
		return logiticsCode;
	}

	public void setLogiticsCode(String logiticsCode) {
		this.logiticsCode = logiticsCode;
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

	public Double getIndivPrice() {
		return IndivPrice;
	}

	public void setIndivPrice(Double indivPrice) {
		IndivPrice = indivPrice;
	}

	public Double getGiftPrice() {
		return giftPrice;
	}

	public void setGiftPrice(Double giftPrice) {
		this.giftPrice = giftPrice;
	}

	public Integer getRealQty() {
		return realQty;
	}

	public void setRealQty(Integer realQty) {
		this.realQty = realQty;
	}

	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PackageDetail other = (PackageDetail) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lineNumber == null) {
			if (other.lineNumber != null)
				return false;
		} else if (!lineNumber.equals(other.lineNumber))
			return false;
		if (logiticsCode == null) {
			if (other.logiticsCode != null)
				return false;
		} else if (!logiticsCode.equals(other.logiticsCode))
			return false;
		if (packageId == null) {
			if (other.packageId != null)
				return false;
		} else if (!packageId.equals(other.packageId))
			return false;
		if (productCode == null) {
			if (other.productCode != null)
				return false;
		} else if (!productCode.equals(other.productCode))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (productQty == null) {
			if (other.productQty != null)
				return false;
		} else if (!productQty.equals(other.productQty))
			return false;
		if (unitPrice == null) {
			if (other.unitPrice != null)
				return false;
		} else if (!unitPrice.equals(other.unitPrice))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}