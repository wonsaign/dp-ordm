package com.zeusas.dp.ordm.bean;

import java.util.ArrayList;
import java.util.List;

public class AreaOrderBean {
	//直营
	private AreaOrderItem myItem;
	//代理
	private List<AreaOrderItem> agent;
	//分销
	private List<AreaOrderItem> distributor;
	//运营的加盟
	private List<AreaOrderItem> frOfOperator;
	//代理的加盟
	private List<AreaOrderItem> frOfAgent;
	//代理汇总
//	private AreaOrderItem agentSummary;
	//分销汇总
//	private AreaOrderItem distributorSummary;
	// 加盟汇总
	private AreaOrderItem summary;
	
	public AreaOrderBean() {
		this.summary=new AreaOrderItem();
//		this.agentSummary=new AreaOrderItem();
//		this.distributorSummary=new AreaOrderItem();
		this.agent=new ArrayList<>();
		this.distributor=new ArrayList<>();
		this.frOfOperator=new ArrayList<>();
		this.frOfAgent=new ArrayList<>();
	}
	public AreaOrderItem getMyItem() {
		return myItem;
	}
	public void setMyItem(AreaOrderItem myItem) {
		this.myItem = myItem;
	}
	public List<AreaOrderItem> getAgent() {
		return agent;
	}
	public void setAgent(List<AreaOrderItem> agent) {
		this.agent = agent;
	}
	public List<AreaOrderItem> getDistributor() {
		return distributor;
	}
	public void setDistributor(List<AreaOrderItem> distributor) {
		this.distributor = distributor;
	}
	public List<AreaOrderItem> getFrOfOperator() {
		return frOfOperator;
	}
	public void setFrOfOperator(List<AreaOrderItem> frOfOperator) {
		this.frOfOperator = frOfOperator;
	}
	public List<AreaOrderItem> getFrOfAgent() {
		return frOfAgent;
	}
	public void setFrOfAgent(List<AreaOrderItem> frOfAgent) {
		this.frOfAgent = frOfAgent;
	}
	public AreaOrderItem getSummary() {
		return summary;
	}
	public void setSummary(AreaOrderItem summary) {
		this.summary = summary;
	}
//	public AreaOrderItem getAgentSummary() {
//		return agentSummary;
//	}
//	public void setAgentSummary(AreaOrderItem agentSummary) {
//		this.agentSummary = agentSummary;
//	}
//	public AreaOrderItem getDistributorSummary() {
//		return distributorSummary;
//	}
//	public void setDistributorSummary(AreaOrderItem distributorSummary) {
//		this.distributorSummary = distributorSummary;
//	}
}
