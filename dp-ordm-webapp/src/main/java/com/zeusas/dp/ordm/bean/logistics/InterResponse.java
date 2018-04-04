package com.zeusas.dp.ordm.bean.logistics;

import java.util.List;

import com.zeusas.core.entity.IEntity;

public class InterResponse implements IEntity {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6990424148930538062L;
	private String success;
	private String errCode;
	private boolean hasError;
	private String data;
	private List<String> message;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public boolean isHasError() {
		return hasError;
	}

	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public List<String> getMessage() {
		return message;
	}

	public void setMessage(List<String> message) {
		this.message = message;
	}

}
