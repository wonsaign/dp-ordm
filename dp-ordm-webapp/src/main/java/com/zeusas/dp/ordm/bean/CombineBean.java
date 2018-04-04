package com.zeusas.dp.ordm.bean;

import java.io.Serializable;

public class CombineBean implements Serializable{
	
	/**
	 * 
	 */
	//XXX: 必须继承可序列化
	private static final long serialVersionUID = 5870379085993688123L;

	
	// 合并订单每单的ID或者合并id
	private String id;
	// img
	private String img;
	// 实际付款
	private String ay;
	// 描述
	private String des;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}


	public String getAy() {
		return ay;
	}

	public void setAy(String ay) {
		this.ay = ay;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}
	
	
}
