package com.zeusas.dp.ordm.rev.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.zeusas.dp.ordm.active.model.PeriodActivity;

public class ReservedActivityContext implements Serializable {

	/** serialVersionUID*/
	private static final long serialVersionUID = -4809277189393979188L;
	
	/** 预定会主推产品(exclusive) */
	private Set<Integer> products;
	/** 预定会主推产品 */
	private Set<String> activities;

	private List<PeriodActivity> stocks;
	private List<PeriodActivity> counters;
	private List<PeriodActivity> zones;

	public Set<Integer> getProducts() {
		return products;
	}

	public void setProducts(Set<Integer> products) {
		this.products = products;
	}

	public Set<String> getActivities() {
		return activities;
	}

	public boolean containsProduct(Integer pid) {
		return products == null ? false : products.contains(pid);
	}

	public boolean containsActivity(String activityId) {
		return activities == null ? false : activities.contains(activityId);
	}

	public void setActivities(Set<String> activities) {
		this.activities = activities;
	}

	public List<PeriodActivity> getStocks() {
		return stocks;
	}

	public void setStocks(List<PeriodActivity> stocks) {
		this.stocks = stocks;
	}

	public List<PeriodActivity> getZones() {
		return zones;
	}

	public void setZones(List<PeriodActivity> zones) {
		this.zones = zones;
	}

	public List<PeriodActivity> getCounters() {
		return counters;
	}

	public void setCounters(List<PeriodActivity> counters) {
		this.counters = counters;
	}
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
