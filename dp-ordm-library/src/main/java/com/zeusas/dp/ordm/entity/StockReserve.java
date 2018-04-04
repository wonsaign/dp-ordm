package com.zeusas.dp.ordm.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;
import com.zeusas.dp.ordm.stock.entity.Item;

@Entity
@Table(name = "bus_stockreserve")
public class StockReserve implements IEntity {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7369072175769844362L;
	//主键
	@Id
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	//产品id
	@Column(name = "PRODUCTID")
	private Integer productId;
	//产品名
	@Column(name = "PRODUCTNAME")
	private String productName;
	//创建者
	@Column(name = "CREATOR")
	private String creator;
	//修改者
	@Column(name = "MODIFIER")
	private String modifier;
	//创建时间
	@Column(name = "CREATETIME")
	private Long createTime;
	//最后更新时间
	@Column(name = "LASTUPDATE")
	private Long lastUpdate;
	//其实时间
	@Column(name = "STARTTIME")
	private Long startTime;
	//结束时间
	@Column(name = "ENDTIME")
	private Long endTime;
	//是否可用 true=可用  false=禁用
	@Column(name = "VALID")
	private Boolean valid;
	//描述
	@Column(name = "DESCRIPTION")
	private String description;
	//仓库预留库存
	@Column(name = "DETAIL")
	@Type(type = "com.zeusas.dp.ordm.stock.entity.ItemListType")
	private List<Item> detail;
	//描述
	@Column(name = "CANCELLATION")
	private String cancellation;

	public StockReserve() {
		createTime = System.currentTimeMillis();
		lastUpdate = createTime;
	}

	public StockReserve(Integer productId, String productName, Long startTime, Long endTime, String description,
			List<Item> detail) {
		this();
		this.productId = productId;
		this.productName = productName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		this.detail = detail;
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

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Item> getDetail() {
		return detail;
	}

	public void setDetail(List<Item> detail) {
		this.detail = detail;
	}

	public String getCancellation() {
		return cancellation;
	}

	public void setCancellation(String cancellation) {
		this.cancellation = cancellation;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
