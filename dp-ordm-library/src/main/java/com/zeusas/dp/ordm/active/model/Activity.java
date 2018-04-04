package com.zeusas.dp.ordm.active.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Objects;
import com.zeusas.core.entity.IEntity;
import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.entity.Counter;

@Entity
@Table(name = "BUS_ACTIVITY")
public class Activity implements IEntity {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 正常活动 */
	public final static int S_OK = 0;
	/** 活动无效 */
	public final static int S_INVALID = 1;
	/** 活动过期 */
	public final static int S_EXPIRED = 2;
	@Id
	@Column(name = "ACTID", unique = true, nullable = false)
	private String actId;
	@Column(name = "name")
	private String name;
	@Column(name = "Description")
	private String description;
	@Column(name = "image")
	private String image;
	// 活动类型: 01|02|04|08
	@Column(name = "type")
	private String type;
	// 开始时间
	@Column(name = "start")
	private Date start;
	// 结束时间
	@Column(name = "TOEND")
	private Date to;
	@Column(name = "priority")
	private Integer priority;
	// 承担费用的部门
	@Column(name = "DUTYCODE")
	private String dutyCode;
	// 是否是捆绑销售
	@Column(name = "bunding")
	private boolean bunding;
	// 是否自动扫描/分配
	@Column(name = "AUTOALLOCA")
	private boolean autoAlloca;
	@Column(name = "CREATEDATE")
	private Date createDate;
	@Column(name = "LASTUPDATE")
	private long lastUpdate;
	// 创建人
	@Column(name = "OWNERID")
	private String ownerId;
	// 创建人名称
	@Column(name = "OWNERNAME")
	private String ownerName;
	// 活动状态
	@Column(name = "status")
	private int status;

	@Column(name = "context")
	@Type(type = "com.zeusas.dp.ordm.active.model.ActivityContextType")
	private ActivityContext context;

	public Activity() {
		createDate = new Date();
		lastUpdate = System.currentTimeMillis();
		bunding = false;
		autoAlloca = false;
		priority = 0;
	}

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	/**
	 * 检查时间是否有效
	 * 
	 * @return
	 */
	public boolean dateCheck() {
		int now = DateTime.toYMD(new Date());
		return (start == null || now >= DateTime.toYMD(start))
				&& (to == null || now <= DateTime.toYMD(to));
	}

	public boolean validCheck(Counter counter) {
		String stockId = counter.getWarehouses();
		String conterCode = counter.getCounterCode();

		List<PeriodActivity> stocks = context.getStocks();
		if (stocks != null && stocks.size() > 0) {
			for (PeriodActivity act : stocks) {
				if (act.validCheck(stockId)) {
					return true;
				}
			}
		}

		List<PeriodActivity> contersAct = context.getCounters();
		if (contersAct != null && contersAct.size() > 0) {
			for (PeriodActivity act : contersAct) {
				if (act.validCheck(conterCode)) {
					return true;
				}
			}
		}
		return false;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public int getPriority() {
		return priority == null ? 0 : priority;
	}

	public void setPriority(Integer priority) {
		if (priority != null) {
			this.priority = priority;
		}
	}

	public String getDutyCode() {
		return dutyCode;
	}

	public void setDutyCode(String dutyCode) {
		this.dutyCode = dutyCode;
	}

	public boolean isBunding() {
		return bunding;
	}

	public void setBunding(boolean bunding) {
		this.bunding = bunding;
	}

	public boolean isAutoAlloca() {
		return autoAlloca;
	}

	public void setAutoAlloca(boolean autoAlloca) {
		this.autoAlloca = autoAlloca;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public ActivityContext getContext() {
		return context;
	}

	public void setContext(ActivityContext context) {
		this.context = context;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Activity(String actId, String name, String description, String image, String type,
			Date start, Date to, int priority, String dutyCode, boolean bunding,
			boolean autoAlloca, Date createDate, long lastUpdate, String ownerId, String ownerName,
			int status) {
		super();
		this.actId = actId;
		this.name = name;
		this.description = description;
		this.image = image;
		this.type = type;
		this.start = start;
		this.to = to;
		this.priority = priority;
		this.dutyCode = dutyCode;
		this.bunding = bunding;
		this.autoAlloca = autoAlloca;
		this.createDate = createDate;
		this.lastUpdate = lastUpdate;
		this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.status = status;
	}

	@Override
	public int hashCode() {
		return this.actId == null ? 0 : actId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !(obj instanceof Activity)) {
			return false;
		}
		Activity act = (Activity) obj;
		return Objects.equal(this.actId, act.actId) //
				&& Objects.equal(this.type, act.type)//
				&& Objects.equal(this.start, act.start)//
				&& Objects.equal(this.to, act.to)//
				&& Objects.equal(this.status, act.status);
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
