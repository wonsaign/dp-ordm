package com.zeusas.dp.ordm.bean;

public class RedeemPointBean {
	private Long Id;
	private String counterName;
	private String productName;
	private Integer num;
	private Boolean avalible;
	private String createTime;
	private String creator;
	private String excutorNo;
	private String deliveryWay;
	private String remark;

	public RedeemPointBean(Long id, String counterName, String productName, Integer num, Boolean avalible,
			String createTime, String creator, String excutorNo, String deliveryWay, String remark) {
		// 序号
		this.Id = id;
		// 柜台名
		this.counterName = counterName;
		// 产品名
		this.productName = productName;
		// 数量
		this.num = num;
		// 是否有效
		this.avalible = avalible;
		// 创建时间
		this.createTime = createTime;
		// 创建人
		this.creator = creator;
		// 执行单号
		this.excutorNo = excutorNo;
		// 发货方式
		this.deliveryWay = deliveryWay;
		// 备注
		this.remark = remark;
		/*
		 * 前端显示删除按钮
		 */
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Boolean getAvalible() {
		return avalible;
	}

	public void setAvalible(Boolean avalible) {
		this.avalible = avalible;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getExcutorNo() {
		return excutorNo;
	}

	public void setExcutorNo(String excutorNo) {
		this.excutorNo = excutorNo;
	}

	public String getDeliveryWay() {
		return deliveryWay;
	}

	public void setDeliveryWay(String deliveryWay) {
		this.deliveryWay = deliveryWay;
	}

}
