package com.zeusas.dp.ordm.bean;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.zeusas.core.utils.DateTime;

/**
 * 返回到前段的打欠设置bean
 * 
 * @author zhensx
 *
 */
public class ReserveProductBean {

	protected Integer productId;
	// 产品的名字
	private String productName;
	// 打欠起始时间
	@DateTimeFormat(pattern = DateTime.YYYY_MM_DD_HMS)
	protected Date reserveStart;
	// 打欠结束时间
	@DateTimeFormat(pattern = DateTime.YYYY_MM_DD_HMS)
	protected Date reserveEnd;
	// 还欠起始时间
	@DateTimeFormat(pattern = DateTime.YYYY_MM_DD_HMS)
	protected Date excuteStart;
	// 还欠结束时间
	@DateTimeFormat(pattern = DateTime.YYYY_MM_DD_HMS)
	protected Date excuteEnd;
	// 状态 true 未完成 false已完成
	protected Boolean status;
	// 可用
	protected Boolean avalible;
	// 创建时间
	@DateTimeFormat(pattern = DateTime.YYYY_MM_DD_HMS)
	protected Date createTime;
	// 最后更新时间
	@DateTimeFormat(pattern = DateTime.YYYY_MM_DD_HMS)
	protected Date lastUpdate;
	// 创建人
	protected String creator;
	// 该产品的打欠总量
	protected Integer totalReserve;

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

	public Date getReserveStart() {
		return reserveStart;
	}

	public void setReserveStart(Date reserveStart) {
		this.reserveStart = reserveStart;
	}

	public Date getReserveEnd() {
		return reserveEnd;
	}

	public void setReserveEnd(Date reserveEnd) {
		this.reserveEnd = reserveEnd;
	}

	public Date getExcuteStart() {
		return excuteStart;
	}

	public void setExcuteStart(Date excuteStart) {
		this.excuteStart = excuteStart;
	}

	public Date getExcuteEnd() {
		return excuteEnd;
	}

	public void setExcuteEnd(Date excuteEnd) {
		this.excuteEnd = excuteEnd;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getAvalible() {
		return avalible;
	}

	public void setAvalible(Boolean avalible) {
		this.avalible = avalible;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Integer getTotalReserve() {
		return totalReserve;
	}

	public void setTotalReserve(Integer totalReserve) {
		this.totalReserve = totalReserve;
	}

}
