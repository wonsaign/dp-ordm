package com.zeusas.dp.ordm.entity;

import java.util.ArrayList;
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
import com.zeusas.security.auth.entity.AuthUser;

/**
 * 月度礼包
 * @author fengx
 * @date 2017年2月6日 上午8:03:45
 */
@Entity
@Table(name = "bus_monthpresent")
public class MonthPresent implements IEntity {
	/** serialVersionUID */
	private static final long serialVersionUID = 7367164286595164132L;
	
	public final static String UNEXECUTE="0";
	
	public final static String EXECUTED="1";
	/** 生日礼盒 */
	public final static String TYPE_DICT="208";
	/** 生日礼盒 */
	public final static String TYPE_GIFT_BOX="01";
	/** 月度物料分配 */
	public final static String TYPE_MONTHLY_MATERIAL="02";
	/** 市场部补货 */
	public final static String TYPE_MARKETING_REPLENISHMENT="03";
	/** 设计部补货 */
	public final static String TYPE_DESIGN_REPLENISHMENT="04";
	/** 大促 */
	public final static String TYPE_PROMOTION="05";
	/** 美肤课堂 */
	public final static String TYPE_SKIN_CLASS="06";
	/** 沙龙 */
	public final static String TYPE_SALON="07";
	/** 公关部补货 */
	public final static String TYPE_PR_REPLENISHMENT="08";
	/** 新店物料 */
	public final static String TYPE_NEW_COUNTER="09";
	/** 重点店面 */
	public final static String TYPE_KEY_COUNTER="10";

	/** 主键 */
	@Id
	@Column(name = "ID")
	private Long id;

	/** 门店编码 */
	@Column(name = "counterCode")
	private String counterCode;

	/** 门店名称 */
	@Column(name = "COUNTERNAME")
	private String counterName;
	
	/** 月度物料模板 模板 */
	@Column(name = "CONTEXT")
	@Type(type = "com.zeusas.dp.ordm.entity.PresentContextType")
	private List<PresentContext> context ;
	
	/** 状态 */
	@Column(name = "STATUS")
	private Boolean status;

	/** 执行年月 */
	@Column(name = "YEARMONTH")
	private Integer yearMonth;
	
	/** 创建人 */
	@Column(name = "CREATOR")
	private String creator;
	
	/** 创建时间 */
	@Column(name = "CREATETIME")
	private Date createTime;

	/** 最后更新时间 */
	@Column(name = "LASTUPDATE")
	private Date lastUpdate;

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public List<PresentContext> getContext() {
		if(context==null){
			this.context=new ArrayList<>();
		}
		return context;
	}

	public void setContext(List<PresentContext> context) {
		this.context = context;
	}
	
	public void addContext(PresentContext context) {
		if(this.context==null){
			this.context = new ArrayList<>();
		}
		int index=this.context.size();
		context.setIndex(index+1);
		this.context.add(context);
	}
	
	public void addContext(List<PresentContext> contexts) {
		for (PresentContext context : contexts) {
			int index=this.context.size();
			context.setIndex(index+1);
			this.context.add(context);
		}
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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

	public Integer getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(Integer yearMonth) {
		this.yearMonth = yearMonth;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public MonthPresent() {
	}
	
	public MonthPresent(Counter counter) {
		super();
		this.counterCode = counter.getCounterCode();
		this.counterName = counter.getCounterName();
		this.status = true;
		this.createTime = new Date();
		this.lastUpdate = new Date();
	}
	
	public MonthPresent(Counter counter,AuthUser user) {
		super();
		this.counterCode = counter.getCounterCode();
		this.counterName = counter.getCounterName();
		this.status = true;
		this.creator = user.getLoginName();
		this.createTime = new Date();
		this.lastUpdate = new Date();
	}
	
	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || (!(obj instanceof MonthPresent))) {
			return false;
		}
		MonthPresent b = (MonthPresent)obj;
		return Objects.equal(this.id, b.id)//
				||Objects.equal(this.counterCode, b.counterCode)//
				||Objects.equal(this.counterName, b.counterName)//
				||Objects.equal(this.context, b.context)//
				||Objects.equal(this.status, b.status)//
				||Objects.equal(this.yearMonth, b.yearMonth);
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
