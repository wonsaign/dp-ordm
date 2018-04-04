package com.zeusas.dp.ordm.stock.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResultMessage implements Serializable {
	
	/** serialVersionUID*/
	private static final long serialVersionUID = 1L;
	/** 响应状态 0,1 */
	private Integer resultStatus;
	/**
	 * 错误响应状态 
	 * <li>111:参数为空 
	 * <li>222:请求数据不存在；
	 * <li>333：请求数据库异常；
	 * <li>444：请求超时；
	 * <li>555：其他请求类问题
	 */
	private Integer errorCode;
	/** 请求错误信息 */
	private String errorMessage;
	/** 请求正常时的返回数据 */
	private List<Item> data = new ArrayList<>();

	public ResultMessage(Integer resultStatus, Integer errorCode,
			String errorMessage, Collection<Item> listData) {
		this.resultStatus = resultStatus;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		if (listData != null) {
			this.data.addAll(listData);
		}
	}

	public Integer getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(Integer resultStatus) {
		this.resultStatus = resultStatus;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<Item> getData() {
		return data;
	}

	public void setData(List<Item> data) {
		this.data = data;
	}

}
