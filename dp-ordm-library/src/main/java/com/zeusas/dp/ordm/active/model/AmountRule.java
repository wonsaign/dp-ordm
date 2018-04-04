package com.zeusas.dp.ordm.active.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSON;

public class AmountRule implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8063779662496334997L;

	public final static int TYPE_PLUS = 1;
	public final static int TYPE_MINUS = 2;
	public final static int TYPE_FIXED = 3;

	// 商品总金额
	public final static int TYPE_TOTAL = 4;
	// 仅送一套
	public final static int TYPE_ONLYONE = 5;

	// 如果是 TYPE_TOTAL, total/amt = activity suit number
	private double amount;
	private int type;
	// 不参与金额计算的产品
	Set<Integer> filterPro;
	// 与金额计算的产品
	Set<Integer> containPro;
	// 不参与金额计算的活动
	Set<String> filterAct;
	// 与金额计算的活动
	Set<String> containAct;

	public AmountRule() {
	}

	public AmountRule(int type, double amt) {
		this.type = type;
		this.amount = amt;
	}

	public boolean check(double t) {
		return t > amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Set<Integer> getFilterPro() {
		return filterPro == null ? new HashSet<>(0) : filterPro;
	}

	public void setFilterPro(Set<Integer> filtered) {
		if (filtered != null && filtered.size() > 0) {
			this.filterPro = filtered;
		}
	}

	public void addFilterPro(Integer product) {
		if (filterPro == null) {
			filterPro = new HashSet<>();
		}
		filterPro.add(product);
	}

	public void addFilterPro(Collection<Integer> productIds) {
		if (filterPro == null) {
			filterPro = new HashSet<>();
		}
		if (productIds != null && productIds.size() > 0) {
			filterPro.addAll(productIds);
		}
	}

	public Set<Integer> getContainPro() {
		return containPro == null ? new HashSet<>(0) : containPro;
	}

	public void setContainPro(Set<Integer> contain) {
		if (contain != null && contain.size() > 0) {
			this.containPro = contain;
		}
	}

	public void addContainPro(Integer product) {
		if (containPro == null) {
			containPro = new HashSet<>();
		}
		containPro.add(product);
	}

	public void addContainPro(Collection<Integer> productIds) {
		if (containPro == null) {
			containPro = new HashSet<>();
		}
		if (productIds != null && productIds.size() > 0) {
			containPro.addAll(productIds);
		}
	}

	public Set<String> getFilterAct() {
		return filterAct == null ? new HashSet<>(0) : filterAct;
	}

	public void setFilterAct(Set<String> filterAct) {
		if (filterAct != null && filterAct.size() > 0) {
			this.filterAct = filterAct;
		}
	}

	public void addFilterAct(String activityId) {
		if (filterAct == null) {
			filterAct = new HashSet<>();
		}
		filterAct.add(activityId);
	}

	public void addFilterAct(Collection<String> activityIds) {
		if (filterAct == null) {
			filterAct = new HashSet<>();
		}
		if (activityIds != null && activityIds.size() > 0) {
			filterAct.addAll(activityIds);
		}
	}

	public Set<String> getContainAct() {
		return containAct == null ? new HashSet<>(0) : containAct;
	}

	public void setContainAct(Set<String> containAct) {
		if (containAct != null && containAct.size() > 0) {
			this.containAct = containAct;
		}
	}

	public void addContainAct(String activityId) {
		if (containAct == null) {
			containAct = new HashSet<>();
		}
		containAct.add(activityId);
	}

	public void addContainAct(Collection<String> activityIds) {
		if (containAct == null) {
			containAct = new HashSet<>();
		}
		if (activityIds != null && activityIds.size() > 0) {
			containAct.addAll(activityIds);
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
