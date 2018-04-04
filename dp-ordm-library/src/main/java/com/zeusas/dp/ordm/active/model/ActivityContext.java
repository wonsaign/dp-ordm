package com.zeusas.dp.ordm.active.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.zeusas.dp.ordm.entity.ReserveProduct;
import com.zeusas.dp.ordm.entity.ReserveRecord;

@SuppressWarnings("serial")
public class ActivityContext implements Serializable {
	/** 参加活动产品 */
	private ProductRule actityGoods;
	/** 活动赠品 */
	private List<ProductRule> actityExtras = new ArrayList<>();
	/** XXX: 未使用，阻止序列化 ，不能删除！ */
	transient ProductRule actityExtra;
	// + - sum fix:
	private AmountRule amount;

	private List<PeriodActivity> stocks;
	private List<PeriodActivity> zones;
	private List<PeriodActivity> counters;

	// 打欠起始时间(默认)
	private Date revStart;
	// 打欠结束时间(默认)
	private Date revEnd;
	// 还欠起始时间(默认)
	private Date execStart;
	// 还欠结束时间(默认)
	private Date execEnd;

	private Set<Integer> revProducts;

	/**
	 * 仓库是否打欠标识
	 * @see com.zeusas.dp.ordm.entity.ReserveProduct.ReserveProduct
	 */
	private Map<String, Integer> revWmsStatus;

	public Date getRevStart() {
		return revStart;
	}

	public void setRevStart(Date revStart) {
		this.revStart = revStart;
	}

	public int getRevWmsStatus(String wid) {
		if (revWmsStatus == null || revProducts == null) {
			return 0;
		}
		Integer s = revWmsStatus.get(wid);
		return s == null ? 0 : s.intValue();
	}
	public void setOrAddStatus(String wid, int status) {
		if(revWmsStatus==null){
			revWmsStatus=new HashMap<>();
			
		}
		this.revWmsStatus.put(wid, status);
	}
	
	public void setStatus(Map<String, Integer> status) {
		if (revWmsStatus != null) {
			this.revWmsStatus.putAll(status);
		}
	}

	public Date getRevEnd() {
		return revEnd;
	}

	public void setRevEnd(Date revEnd) {
		this.revEnd = revEnd;
	}

	public Date getExecStart() {
		return execStart;
	}

	public void setExecStart(Date execStart) {
		this.execStart = execStart;
	}

	public Date getExecEnd() {
		return execEnd;
	}

	public void setExecEnd(Date execEnd) {
		this.execEnd = execEnd;
	}

	public Set<Integer> getRevProducts() {
		return revProducts;
	}
	
	public void setRevProducts(Set<Integer> revProducts) {
		this.revProducts = revProducts;
	}

	public Map<String, Integer> getRevWmsStatus() {
		return revWmsStatus;
	}

	public void setRevWmsStatus(Map<String, Integer> revWmsStatus) {
		this.revWmsStatus = revWmsStatus;
	}

	public ProductRule getActityGoods() {
		return actityGoods;
	}

	public void setActityGoods(ProductRule actityGoods) {
		this.actityGoods = actityGoods;
	}

	public List<ProductRule> getActityExtras() {
		return actityExtras == null ? new ArrayList<>(0) : actityExtras;
	}

	public boolean checkIsGlobal() {
		return (this.zones == null || this.zones.isEmpty()) //
				&& (this.stocks == null || this.stocks.isEmpty()) //
				&& (this.counters == null || this.counters.isEmpty());
	}

	public boolean checkActityExtras(List<Integer> pids) {
		if (actityExtras.size() == 1) {
			return actityExtras.get(0).check(pids);
		}

		Set<Integer> all = new HashSet<>();
		for (ProductRule rule : actityExtras) {
			all.addAll(rule.productItem.keySet());
		}
		return all.containsAll(pids);
	}

	public void setActityExtras(List<ProductRule> actityExtras) {
		this.actityExtras = actityExtras;
	}

	public ProductRule getActityExtra() {
		return this.actityExtras.isEmpty() ? null : actityExtras.get(0);
	}

	public void setActityExtra(ProductRule actityExtra) {
		this.actityExtras.add(actityExtra);
	}

	public AmountRule getAmount() {
		return amount;
	}

	public void setAmount(AmountRule amount) {
		this.amount = amount;
	}

	public List<PeriodActivity> getZones() {
		return zones;
	}

	public void setZones(List<PeriodActivity> zones) {
		if (zones != null && zones.size() > 0) {
			this.zones = zones;
		}
	}

	public List<PeriodActivity> getCounters() {
		return counters;
	}

	public void setCounters(List<PeriodActivity> counters) {
		if (counters != null && counters.size() > 0) {
			this.counters = counters;
		}
	}

	public List<PeriodActivity> getStocks() {
		return stocks;
	}

	public void setStocks(List<PeriodActivity> stocks) {
		if (stocks != null && stocks.size() > 0)
			this.stocks = stocks;
	}
	
	public boolean isReserving(String wid,Long time){
		Date start =revStart;
		Date end=revEnd;
		if(start==null||end==null){
			return false;
		}
		Integer status= getRevWmsStatus(wid);
		return !ReserveProduct.STATUS_BUY.equals(status)//
				&&start.getTime()<time//
				&&time<=end.getTime();
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
