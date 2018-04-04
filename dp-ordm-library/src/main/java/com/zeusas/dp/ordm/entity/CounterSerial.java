package com.zeusas.dp.ordm.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;

/**
 * 
 * @author fengx
 *@date 2016年12月22日 下午1:59:43
 */
@Entity
@Table(name="base_counterserial")
public class CounterSerial implements IEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 2332320466226076340L;
	
	@Id
	@Column(name = "COUNTERCODE")
	private String counterCode;
	@Column(name = "SERIALS")
	@Type(type = "com.zeusas.dp.ordm.entity.SerialSellerListType")
	private List<SerialSeller> serials;
	@Column(name="LASTUPDATE")
	private Long lastUpdate;

	public CounterSerial(String counterCode, List<SerialSeller> serials) {
		this.counterCode = counterCode;
		this.serials = serials;
		this.lastUpdate = System.currentTimeMillis();
	}

	public CounterSerial() {
		this.lastUpdate = System.currentTimeMillis();
	}
	
	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public List<SerialSeller> getSerials() {
		return serials;
	}

	public void setSerials(List<SerialSeller> serials) {
		this.serials = serials;
		this.lastUpdate = System.currentTimeMillis();
	}

	public Long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
