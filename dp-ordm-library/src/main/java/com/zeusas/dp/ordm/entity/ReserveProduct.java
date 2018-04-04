package com.zeusas.dp.ordm.entity;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.zeusas.core.entity.IEntity;
import com.zeusas.core.utils.DateTime;

@Entity
@Table(name = "bus_reserveproduct")
public class ReserveProduct implements IEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7412785931625910134L;

	// 正常： 0
	// 可打欠： 1
	// 打欠： 2
	// 还欠结束 4
	
	// 正常： 0
	public static final Integer STATUS_BUY = 0;
	// 可打欠： 1
	public static final Integer STATUS_RESERVABLE = 1;
	// 打欠： 2
	public static final Integer STATUS_RESERVED = 2;
	// 还欠结束 4
	public static final Integer STATUS_DONE = 4;

	// 产品id
	@Id
	@Column(name = "productId", unique = true, nullable = false)
	private Integer productId;
	// 产品的名字
	@Column(name = "productName")
	private String productName;
	// 正文
	@Column(name = "context")
	@Type(type = "com.zeusas.dp.ordm.entity.StatusContextType")
	private StatusContext context;
	
	@Column(name = "MAX")
	private Integer max;
	// 打欠起始时间(默认)
	@Column(name = "RESERVESTART")
	@DateTimeFormat(pattern = DateTime.YYYY_MM_DD_HMS)
	private Date reserveStart;
	// 打欠结束时间(默认)
	@Column(name = "RESERVEEND")
	@DateTimeFormat(pattern = DateTime.YYYY_MM_DD_HMS)
	private Date reserveEnd;
	// 还欠起始时间(默认)
	@Column(name = "EXCUTESTART")
	@DateTimeFormat(pattern = DateTime.YYYY_MM_DD_HMS)
	private Date excuteStart;
	// 还欠结束时间(默认)
	@Column(name = "EXCUTEEND")
	@DateTimeFormat(pattern = DateTime.YYYY_MM_DD_HMS)
	private Date excuteEnd;
	// 状态 true 未完成 false已完成
	@Column(name = "STATUS")
	private Integer status;
	// 可用
	@Column(name = "AVALIBLE")
	private Boolean avalible;
	// 创建时间
	@Column(name = "CREATETIME")
	@DateTimeFormat(pattern = DateTime.YYYY_MM_DD_HMS)
	private Date createTime;
	// 最后更新时间
	@Column(name = "LASTUPDATE")
	private long lastUpdate;
	// 创建人
	@Column(name = "CREATOR")
	private String creator;

	@Column(name = "UPDATOR")
	private String updator;

	@Column(name = "PRODUCTTYPEID")
	private String productTypeId;

	@Column(name = "PRODUCTTYPENAME")
	private String productTypeName;

	@Column(name = "PRODUCTCODE")
	private String productCode;
	//排序
	@Column(name = "SEQID")
	private Integer seqId;
	//是否在首页显示
	@Column(name = "VISIBLE")
	private Boolean visible;
	//批次号
	@Column(name = "BATCHNO")
	private String batchNo;
	// 批次集合
	@Column(name = "BATCHGROUP")
	@Type(type = "com.zeusas.core.entity.StringSetType")
	private Set<String> batchGroup = new LinkedHashSet<>();

	public ReserveProduct() {
		this.createTime = new Date();
		this.lastUpdate = System.currentTimeMillis();
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
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

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
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

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public String getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(String productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public StatusContext getContext() {
		return context;
	}

	public void setContext(StatusContext context) {
		if (context.size() != 0) {
			this.context = context;
		}
	}

	public int getStatus(String wid) {
		if (this.context == null) {
			return 0;
		}
		return this.context.getStatus(wid);
	}
	
	public void setOrAddStatus(String wid, int status) {
		if (this.context == null) {
			context=new StatusContext();
		}
		context.getStatus().put(wid, status);
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		if (status != null) {
			this.status = status;
		}
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

	public Integer getSeqId() {
		return seqId;
	}

	public void setSeqId(Integer seqId) {
		this.seqId = seqId;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	@Override
	public int hashCode() {
		return productId == null ? 0 : productId.hashCode();
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Set<String> getBatchGroup() {
		return batchGroup;
	}

	public void setBatchGroup(Set<String> batchGroup) {
		this.batchGroup = batchGroup;
	}
	
	public void addBatch(String batch) {
		if(!Strings.isNullOrEmpty(batch)){
			batchGroup.add(batch);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().isInstance(this)) {
			return false;
		}
		ReserveProduct product = (ReserveProduct) obj;
		return Objects.equals(this.productId, product.productId)//
				&& Objects.equals(this.productName, product.productName)//
				&& Objects.equals(this.avalible, product.avalible)//
				&& Objects.equals(this.context, product.context)//
				&& Objects.equals(this.avalible, product.avalible);
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
