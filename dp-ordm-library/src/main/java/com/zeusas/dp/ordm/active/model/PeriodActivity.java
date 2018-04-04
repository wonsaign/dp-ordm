package com.zeusas.dp.ordm.active.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class PeriodActivity implements Serializable {
	//针对仓库（区域，柜台）的活动开始时间
	private Integer from;
	//针对仓库（区域，柜台）的活动结束时间
	private Integer to;

	private Set<String> val;

	public PeriodActivity() {
		val = new LinkedHashSet<>();
	}

	public Integer getFrom() {
		return from;
	}

	public void setFrom(Integer from) {
		this.from = from;
	}

	public Integer getTo() {
		return to;
	}

	public void setTo(Integer to) {
		this.to = to;
	}

	public Set<String> getVal() {
		return val;
	}

	public void setVal(Set<String> val) {
		if (val != null) {
			this.val.addAll(val);
		}
	}

	public boolean validCheck(String v) {
		if (!val.contains(v)) {
			return false;
		}
		LocalDate now = LocalDate.now();
		int tm = now.getYear() * 10000 //
				+ now.getMonthValue() * 100 //
				+ now.getDayOfMonth();

		return (from == null || tm >= from.intValue()) //
				&& (to == null || tm <= to.intValue());
	}
}
