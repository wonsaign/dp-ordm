package com.zeusas.dp.ordm.entity;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

/**
 * 所有购物车产品在这个类里面的体现，有产品、物料以及活动产品等
 * 
 * @author fengx
 * @date 2016年12月10日 上午10:41:08
 */
public class CartDetailDesc implements IEntity {

	/** * serialVersionUID */
	private static final long serialVersionUID = 1L;
	public final static boolean GocostRatio = true;// 走费用比
	public final static boolean NotGocostRatio = false;// 不走费用比
	public final static boolean GoPricePolicy = true;// 走价格策略
	public final static boolean NotGoPricePolicy = false;// 不走价格策略

	private Long id;
	// 关联到购物车明细表的主键
	private Long cartDetailId;
	// 产品id
	private Integer productId;
	// 每组合单位数量
	private double quantity;
	// 产品单价
	private Double price;
	// 产品名称
	private String productName;
	/** 订单的发货类型id */
	private String deliveryWayId;
	/** 订单的发货类型name */
	private String deliveryWayName;
	// 是否走费用比
	// XXX:定义修正：大货是否参与可用费比计算 赠品物料是否占用费比
	private boolean costRatio;
	// 打欠标志
	private boolean reserve;
	// 单品打欠批次号
	private String batchNo;

	public CartDetailDesc() {
	}

	public CartDetailDesc(Product p, AssociatedProduct ap, Long cartDetaiId) {
		this(p);
		this.quantity = ap.getCoeff();
		this.cartDetailId = cartDetaiId;
		this.costRatio = GocostRatio;
	}

	public CartDetailDesc(Product p) {
		this.productId = p.getProductId();
		this.productName = p.getName();
		this.price = Product.TYPEID_PRODUCT.equals(p.getTypeId()) ? p.getMemberPrice() : p.getMaterialPrice();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCartDetailId() {
		return cartDetailId;
	}

	public void setCartDetailId(Long cartDetailId) {
		this.cartDetailId = cartDetailId;
	}

	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * 取得是否使用费比计算
	 * 
	 * @return 是否使用费比计算
	 */
	public boolean isCostRatio() {
		return costRatio;
	}

	public void setCostRatio(boolean costRatio) {
		this.costRatio = costRatio;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public int getQuantity() {
		return (int) quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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

	public boolean getReserve() {
		return reserve;
	}

	public void setReserve(boolean reserve) {
		this.reserve = reserve;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String toString() {
		return JSON.toJSONString(this);
	}
}