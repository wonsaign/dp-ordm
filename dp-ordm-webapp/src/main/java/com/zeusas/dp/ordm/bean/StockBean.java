package com.zeusas.dp.ordm.bean;

import java.beans.Transient;

public class StockBean {
	private String wmsId;
	private String name;
	private String spec;
	private Integer qty;
	private int pid;
	private transient String classId;
	private boolean joinAct;
	
	public StockBean(int pid,String wmsId, String name, String spec, Integer qty, String classId, boolean joinAct) {
		this.pid = pid ;
		this.wmsId = wmsId;
		this.name = name;
		this.spec = (spec == null) ? "" : spec;
		this.qty = qty;
		this.classId = classId;
		this.joinAct = joinAct;
	}

	public StockBean() {
	}

	
	public boolean getJoinAct() {
		return joinAct;
	}

	public void setJoinAct(boolean joinAct) {
		this.joinAct = joinAct;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	@Transient
	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getWmsId() {
		return wmsId;
	}

	public void setWmsId(String wmsId) {
		this.wmsId = wmsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	
}
