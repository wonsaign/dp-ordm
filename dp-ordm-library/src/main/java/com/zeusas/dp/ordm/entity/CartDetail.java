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
import com.zeusas.core.service.ServiceException;

/**
 * Cartdetail entity. 
 * @author fx 
 * 购物车细节表，存放的是每个购物车关联的产品、赠品、物料以及组合产品
 * 所有的产品、以及活动产品的所有细节都在第二张细节表里面
 */
@Entity
@Table(name = "bus_cartdetail")
public class CartDetail implements IEntity {

	private static final long serialVersionUID = -5941451826246470629L;

	public static final String  ActivityProduct="666";//代表为活动产品
	
	public static final boolean Do_Price=true;//不走价格策略
	public static final boolean NoDo_Price=false;//走价格策略
	// 购物车明细主键
	@Id
	@Column(name = "DETAILID", unique = true, nullable = false)
	private Long detailId;
	// 关联到购物车的id
	@Column(name = "CARTID")
	private Long cartId;
	
	// 产品的在订单中的显示价格
	@Column(name = "PRICE")
	private double price;

	@Column(name = "QUANTITY")
	private Integer quantity;

	// 产品的类型id
	@Column(name = "TYPE")
	private String type;

	// 判断是否执行价格策略
	//FIXME 是否打折
	@Column(name = "PRICEPOLICY")
	private boolean pricePolicy;

	// 关联到活动的id ProductGroup#actId { 单品 0 | 活动 XXX } 
	@Column(name = "ACTIVITYID")
	private String activityId;

	@Column(name = "ACTIVITYNAME")
	private String activityName;
	
	//预订会标记
	@Column(name = "revId")
	Integer revId;
	
	
	@Column(name="CARTDETAILDESCS")
	@Type(type = "com.zeusas.dp.ordm.entity.CartDetailDescListType")
	private   List<CartDetailDesc> cartDetailsDesc ;
	
	public CartDetail() {
	}
	
	/**购物车明细的构造方法
	 * @throws ServiceException */
	public CartDetail(Cart cart,Product product,int num,boolean flag)  {
		this.cartId=cart.getCartId();
		this.quantity=num;
		if (flag) {
			this.pricePolicy=Do_Price;
			this.type=Product.TYPEID_PRODUCT;
			this.price=product.getMemberPrice();
		} else {
			this.pricePolicy=NoDo_Price;
			this.type=product.getTypeId();// 获取产品的类型id
			this.price=product.getMaterialPrice();
		}
	}
	
	/**购物车明细关于活动组的构造方法*/
	public CartDetail(String actId,Long detailId,int num) {
		this.detailId=detailId;
		this.activityId=actId;
		this.quantity=num;
		this.type=ActivityProduct;
		this.pricePolicy=true;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public List<CartDetailDesc> getCartDetailsDesc() {
		return cartDetailsDesc==null?new ArrayList<>():cartDetailsDesc;
	}

	public void setCartDetailsDesc(List<CartDetailDesc> cartDetailsDesc) {
			this.cartDetailsDesc = cartDetailsDesc;
	}
	public void addCartDetailsDesc(CartDetailDesc cartDetailsDesc) {
		if(this.cartDetailsDesc ==null){
			this.cartDetailsDesc=new ArrayList<>();
		}
		this.cartDetailsDesc.add(cartDetailsDesc);
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public boolean isPricePolicy() {
		return pricePolicy;
	}

	public void setPricePolicy(boolean pricePolicy) {
		this.pricePolicy = pricePolicy;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public Integer getRevId() {
		return revId;
	}

	public void setRevId(Integer revId) {
		this.revId = revId;
	}

	public String toString(){
		return JSON.toJSONString(this);
	}
}