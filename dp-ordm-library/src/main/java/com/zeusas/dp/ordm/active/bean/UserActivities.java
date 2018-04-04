package com.zeusas.dp.ordm.active.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.ProductRule;

public final class UserActivities {
	
	final Map<Integer, List<Activity>> productSet = new HashMap<>();
	final Map<String, Activity> serialsSet = new HashMap<>();

	public boolean isContainsItself(Integer pid) {
		List<Activity> acts = productSet.get(pid);
		if (acts == null || acts.isEmpty()) {
			return false;
		}
		return ActivityType.TYPE_PRENSENTOWNER.equals(acts.get(0).getType());
	}
	
	public boolean isJoinProduct(Integer pid) {
		return productSet.containsKey(pid);
	}

	public boolean isJoinSerials(String sid) {
		return serialsSet.containsKey(sid);
	}

	public void clear() {
		serialsSet.clear();
		productSet.clear();
	}

	public List<Activity> getProductActivities(Integer pid) {
		if(productSet==null||productSet.size() <=0){
			return new ArrayList<Activity>();
		}
		return productSet.get(pid);
	}

	public Activity getProductActivities(String sid) {
		return serialsSet.get(sid);
	}

	public void addToSerials(Activity act) {
		ProductRule productRule=act.getContext().getActityGoods();
		if(productRule!=null){
			String serialID = act.getContext()//
					.getActityGoods().getSerialsId();
			if (!Strings.isNullOrEmpty(serialID)) {
				serialsSet.put(serialID, act);
			}
		}
	}

	public void setProducts(Map<Integer, List<Activity>> p) {
		productSet.putAll(p);
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
