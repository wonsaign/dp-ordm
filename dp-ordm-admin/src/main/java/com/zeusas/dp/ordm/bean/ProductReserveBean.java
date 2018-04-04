package com.zeusas.dp.ordm.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.entity.StockReserve;

public class ProductReserveBean {

	private String id;
	private Integer productId;
	private String productName;
	private String creator;
	private String modifier;
	private String createTime;
	private String startTime;
	private String endTime;
	private List<String> detail;
	private String description;
	private Boolean valid;
	private Long lastUpdate;
	private String cancellation;

	final static ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat(DateTime.YYYY_MM_DD_HMS);
		}
	};

	public ProductReserveBean(StockReserve reserve, List<String> detail) {
		DateFormat format = dateFormat.get();
		this.id = reserve.getId();
		this.productId = reserve.getProductId();
		this.productName = reserve.getProductName();
		this.creator = reserve.getCreator();
		this.modifier = reserve.getModifier();
		this.createTime = format.format(new Date(reserve.getCreateTime()));
		this.startTime = format.format(new Date(reserve.getStartTime()));
		this.endTime = format.format(new Date(reserve.getEndTime()));
		this.valid = reserve.getValid();
		this.detail = detail;
		this.description = reserve.getDescription();
		this.lastUpdate = reserve.getLastUpdate();
	}

	public ProductReserveBean(StockReserve reserve, List<String> detail, String cancellation) {
		this(reserve, detail);
		this.cancellation = cancellation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getDetail() {
		return detail;
	}

	public void setDetail(List<String> detail) {
		this.detail = detail;
	}

	public Long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public String getCancellation() {
		return cancellation;
	}

	public void setCancellation(String cancellation) {
		this.cancellation = cancellation;
	}

	@Override
	public String toString() {
		return "ProductReserveBean [id=" + id + ", productId=" + productId + ", productName=" + productName
				+ ", creator=" + creator + ", modifier=" + modifier + ", createTime=" + createTime + ", startTime="
				+ startTime + ", endTime=" + endTime + ", valid=" + valid + ", detail=" + detail + ", description="
				+ description + ", lastUpdate=" + lastUpdate + "]";
	}

}
