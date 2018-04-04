package com.zeusas.dp.ordm.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;
import com.zeusas.security.auth.entity.AuthUser;

/**
 * 购物车实体
 * 
 * @author fengx
 * @date 2016年12月12日 上午8:48:52
 */
@Entity
@Table(name = "bus_cart")
public class Cart implements IEntity {
	
	private static final long serialVersionUID = 4751640753676019771L;

	//购物车状态为自己可用
	public static final Integer STATUS_ACTIVE=1;
	//购物车为提交状态
	public static final Integer STATUS_COMMIT=2;
	//购物车状态为休眠状态
	public static final Integer STATUS_UNACTIVE=3;
	
	//购物车处于未锁定状态
	public static final Integer STATUS_UNLOCK=666;
	//购物车处于自己锁定状态
	public static final Integer STATUS_OWNERLOCK=777;
	//购物车处于他人锁定状态
	public static final Integer STATUS_OTHERLOCK=444;
	
	/** 物料费用常量*/
	public static final String MATERIALFEE="materialFee";
	/** 物料还有多久免费的常量*/
	public static final String REMAINFEE="remainFee";
	
	public static final String MATERIALFREE="materialFree";
	
	// 购物车编码:YYMMDD0001
	@Id
	@Column(name = "CARTID", unique = true, nullable = false)
	protected Long cartId;
	// 门店的编码
	@Column(name = "COUNTERID", unique = true)
	protected Integer counterId;
	/**门店的名称*/
	@Column(name="COUNTERNAME")
	private String counterName;
	// 用户 id
	@Column(name = "USERID")
	protected String userId;
	
	@Column(name = "USERNAME")
	protected String userName;
	// 创建时间
	@Column(name = "CREATEDATE")
	protected Long createDate;
	// 最后更新时间
	@Column(name = "LASTUPDATE")
	protected Long lastUpdate;
	// 判断购物车的状态，如根据当前时间和最后修改时间来确定购物车是否失效 
	// 这里应该放在数据字典里面，因为这个还有提交到店长那里的状态
	@Column(name = "STATUS")
	protected Integer status;
	// 购物车存放的订单id
	@Column(name="ORDERID")
	protected Long orderId;
	// 活动记录
	@Column(name = "ACTIVRECORD")
	@Type(type = "com.zeusas.core.entity.StringListType")
	private List<String> activRecord;
	
	public Cart() {
		this.createDate=System.currentTimeMillis();
		this.lastUpdate=System.currentTimeMillis();
		this.status=Cart.STATUS_ACTIVE;
	}
	
	/**当某个用户初始化购物车时的构造方法*/
	public Cart(AuthUser user) {
		this();
		this.userName=user.getCommonName();
		this.userId=user.getLoginName();
	}
	
	/**当某个用户初始化购物车时的构造方法*/
	public Cart(Counter counter) {
		this();
		this.counterId=counter.getCounterId();
		this.counterName=counter.getCounterName();
	}
	
	public Cart(AuthUser user,Counter counter) {
		this();
		this.userName=user.getCommonName();
		this.userId=user.getLoginName();
		this.counterId=counter.getCounterId();
		this.counterName=counter.getCounterName();
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

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Integer getCounterId() {
		return this.counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public Long getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	public List<String> getActivRecord() {
		return activRecord == null ? new ArrayList<>(0) : activRecord;
	}

	public void setActivRecord(List<String> activRecord) {
		this.activRecord = activRecord;
	}
	
	public void addActivRecord(String ActId) {
		if(this.activRecord==null){
			this.activRecord=new ArrayList<>();
		}
		this.activRecord.add(ActId);
	}

	public int hashCode() {
		return cartId == null ? 0 : cartId.hashCode();
	}

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
		Cart other = (Cart) obj;
		return Objects.equals(this.cartId, other.cartId)//
				&& Objects.equals(this.counterId, other.counterId)//
				&& Objects.equals(this.userId, other.userId)//
				&& Objects.equals(this.status, other.status);
	}
	
	public Cart(Long cartId, Integer counterId, String counterName, String userId, String userName, Long createDate,
			Long lastUpdate, Integer status) {
		this.cartId = cartId;
		this.counterId = counterId;
		this.counterName = counterName;
		this.userId = userId;
		this.userName = userName;
		this.createDate = createDate;
		this.lastUpdate = lastUpdate;
		this.status = status;
	}

	public String toString(){
		return JSON.toJSONString(this);
	}

}