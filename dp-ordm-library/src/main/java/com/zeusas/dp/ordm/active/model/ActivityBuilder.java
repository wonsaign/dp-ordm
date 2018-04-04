package com.zeusas.dp.ordm.active.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zeusas.dp.ordm.active.bean.UserActivities;

public final class ActivityBuilder {
	long lastUpdate;

	/**
	 * 基于产品为线索，创建活动集
	 * 
	 * @param acts
	 * @return
	 */
	public static UserActivities build(List<Activity> acts) {
		UserActivities userAct = new UserActivities();

		final Map<Integer, List<Activity>> pbuilder = new HashMap<>();
		for (Activity act : acts) {
			// 追加系列到系列的活动
			userAct.addToSerials(act);
			ProductRule rule = act.getContext().getActityGoods();
			if (rule == null) {
				continue;
			}
			Set<Integer> item = rule.getProductItem().keySet();
			for (Integer pid : item) {
				List<Activity> al = pbuilder.get(pid);
				if (al == null) {
					al = new ArrayList<>();
					pbuilder.put(pid, al);
				}
				al.add(act);
			}
		}
		
		// FIXME: test the order?
		for (List<Activity> v : pbuilder.values()) {
			if (v.size() > 1) {
				Collections.sort(v, //
						(a, b) -> b.getPriority() - a.getPriority());
			}
			((ArrayList<?>) v).trimToSize();
		}
		userAct.setProducts(pbuilder);
		return userAct;
	}
}
