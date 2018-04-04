package com.zeusas.dp.ordm.bean.logistics;

import com.zeusas.core.entity.IEntity;

public class LogisticDetail implements IEntity{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5571928044423506980L;
	private String OrderNo;
	private String SourceNo;
	private String rec_id;
	private String rec_name;
	private String SupplyID;
	private String SupplyIDName;
	private String tel;
	private String fadelTel;
	private String fadelName;
	private String departStation;
	private String deliverStatus;
	private String departStationName;
	private String endStation;
	private String endStationName;
	private String scheduleDate;
	private String nodeDesc;
	private String deliveryTime;
	private String tNum;

	public String getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}

	public String getSourceNo() {
		return SourceNo;
	}

	public void setSourceNo(String sourceNo) {
		SourceNo = sourceNo;
	}

	public String getRec_id() {
		return rec_id;
	}

	public void setRec_id(String rec_id) {
		this.rec_id = rec_id;
	}

	public String getRec_name() {
		return rec_name;
	}

	public void setRec_name(String rec_name) {
		this.rec_name = rec_name;
	}

	public String getSupplyID() {
		return SupplyID;
	}

	public void setSupplyID(String supplyID) {
		SupplyID = supplyID;
	}

	public String getSupplyIDName() {
		return SupplyIDName;
	}

	public void setSupplyIDName(String supplyIDName) {
		SupplyIDName = supplyIDName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFadelTel() {
		return fadelTel;
	}

	public void setFadelTel(String fadelTel) {
		this.fadelTel = fadelTel;
	}

	public String getFadelName() {
		return fadelName;
	}

	public void setFadelName(String fadelName) {
		this.fadelName = fadelName;
	}

	public String getDepartStation() {
		return departStation;
	}

	public void setDepartStation(String departStation) {
		this.departStation = departStation;
	}

	public String getDeliverStatus() {
		return deliverStatus;
	}

	public void setDeliverStatus(String deliverStatus) {
		this.deliverStatus = deliverStatus;
	}

	public String getDepartStationName() {
		return departStationName;
	}

	public void setDepartStationName(String departStationName) {
		this.departStationName = departStationName;
	}

	public String getEndStation() {
		return endStation;
	}

	public void setEndStation(String endStation) {
		this.endStation = endStation;
	}

	public String getEndStationName() {
		return endStationName;
	}

	public void setEndStationName(String endStationName) {
		this.endStationName = endStationName;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public String getNodeDesc() {
		return nodeDesc;
	}

	public void setNodeDesc(String nodeDesc) {
		this.nodeDesc = nodeDesc;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String gettNum() {
		return tNum;
	}

	public void settNum(String tNum) {
		this.tNum = tNum;
	}
}
