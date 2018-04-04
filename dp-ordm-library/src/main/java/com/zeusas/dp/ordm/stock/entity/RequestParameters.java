package com.zeusas.dp.ordm.stock.entity;

import java.io.Serializable;

public class RequestParameters implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4839824429601032228L;
	/** 请求ID */
	private Integer id;
	/** 请求编码 */
	private String code;
	/** 请求名称 */
	private String name;
	/** 辅助ID Subsidiary 仓库 */
	private Integer subID;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSubID() {
		return subID;
	}

	public void setSubID(Integer subID) {
		this.subID = subID;
	}
	
}
