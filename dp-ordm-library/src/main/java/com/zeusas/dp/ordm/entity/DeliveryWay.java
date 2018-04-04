package com.zeusas.dp.ordm.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.zeusas.common.entity.Dictionary;

/**
 * 产品的发货方式
 * @author fengx
 *@date 2017年3月30日 下午6:03:51
 */
public class DeliveryWay implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1813917962070049975L;

	/** 发货方式id */
	private String deliveryWayId;
	/** 发货方式Name*/
	private String deliveryName;
	public String getDeliveryWayId() {
		return deliveryWayId;
	}
	public void setDeliveryWayId(String deliveryWayId) {
		this.deliveryWayId = deliveryWayId;
	}
	public String getDeliveryName() {
		return deliveryName;
	}
	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}
	
	public DeliveryWay() {
		// TODO Auto-generated constructor stub
	}
	
	
	/** 通过字典项实例化一个发货方式*/
	public DeliveryWay(Dictionary dictionary) {
		this.deliveryWayId=dictionary.getHardCode();
		this.deliveryName=dictionary.getName();
	}
	
	@Override
	public int hashCode() {
		return (deliveryWayId == null) ? 0 : deliveryWayId.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeliveryWay other = (DeliveryWay) obj;
		if (deliveryName == null) {
			if (other.deliveryName != null)
				return false;
		} else if (!deliveryName.equals(other.deliveryName))
			return false;
		if (deliveryWayId == null) {
			if (other.deliveryWayId != null)
				return false;
		} else if (!deliveryWayId.equals(other.deliveryWayId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return JSON.toJSONString(this);
	}
	
	
	
}
