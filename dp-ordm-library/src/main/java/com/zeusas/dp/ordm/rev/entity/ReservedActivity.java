package com.zeusas.dp.ordm.rev.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;
import com.zeusas.core.utils.DateTime;
import com.zeusas.dp.ordm.active.model.PeriodActivity;
import com.zeusas.dp.ordm.entity.Counter;
@Entity
@Table(name="bus_reservedactivity")
public class ReservedActivity implements IEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -688635152679979586L;
	/** 正常预订会*/
	public final static int S_OK = 0;
	/** 预订会无效 */
	public final static int S_INVALID = 1;
	/** 预订会过期 */
	public final static int S_EXPIRED = 2;
	
	/** 预定会标识ID */
	@Id
	@Column(name = "REVID", unique = true, nullable = false)
	Integer revId;
	@Column(name="SUBJECT")
	String subject;
	@Column(name="CONTENT")
	String content;
	@Column(name="TYPE")
	Integer type;
	@Column(name="status")
	int status;
	@Column(name="STARTTIME")
	Date startTime;
	@Column(name="ENDTIME")
	Date endTime;
	@Column(name = "context")
	@Type(type = "com.zeusas.dp.ordm.rev.entity.ReservedActivityContextType")
	ReservedActivityContext context;
	public Integer getRevId() {
		return revId;
	}
	public void setRevId(Integer revId) {
		this.revId = revId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public ReservedActivityContext getContext() {
		return context;
	}
	public void setContext(ReservedActivityContext context) {
		this.context = context;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * 有效性检查
	 * @return
	 */
	public boolean isAvaliable() {
		return ReservedActivity.S_OK==this.status&&dateCheck();
	}
	/**
	 * 检查时间是否有效
	 * 
	 * @return
	 */
	public boolean dateCheck() {
		Long now = new Date().getTime();
		return (startTime == null || now >= startTime.getTime())
				&& (endTime == null || now <= endTime.getTime());
	}
	/**
	 * 检查柜台仓库是否有效
	 * @param counter
	 * @return
	 */
	public boolean validCheck(Counter counter) {
		String stockId = counter.getWarehouses();
		String conterCode = counter.getCounterCode();

		List<PeriodActivity> stocks = context.getStocks();
		List<PeriodActivity> contersAct = context.getCounters();
		
		if(stocks==null&&contersAct==null){
			return true;
		}
		
		if (stocks != null && stocks.size() > 0) {
			for (PeriodActivity act : stocks) {
				if (act.validCheck(stockId)) {
					return true;
				}
			}
		}

		if (contersAct != null && contersAct.size() > 0) {
			for (PeriodActivity act : contersAct) {
				if (act.validCheck(conterCode)) {
					return true;
				}
			}
		}
		return false;
	}
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}
