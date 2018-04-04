package com.zeusas.dp.ordm.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zeusas.common.task.CronTask;
import com.zeusas.dp.ordm.stock.entity.Item;
import com.zeusas.dp.ordm.stock.service.StockSyncService;

/**
 * 仓库数据同步，同步已经设定好的仓库信息。
 * 
 * 目前仓库有：武汉仓、北京仓 （1天1次）
 *
 */
public class StorehousesSyncTask extends CronTask {
	static final Logger log = LoggerFactory.getLogger(StorehousesSyncTask.class);

	private final StockSyncService stockdeal;

	public StorehousesSyncTask() {
		stockdeal = new StockSyncService();
	}

	public Map<Integer, Map<Integer, Item>> getStockInfo() {
		return stockdeal.getStockInfo();
	}

	public Collection<Item> getAllItem() {
		Collection<Item> litems = new ArrayList<>();
		stockdeal.getStockInfo().values().stream().forEach(e -> {
			litems.addAll(e.values());
		});
		return litems;
	}

	public void setUpdated() {
		stockdeal.setUpdated();
	}

	/**
	 * 根据产品，取得定单库存信息
	 * 
	 * @param pid
	 * @return
	 */

	public List<Item> getStockInfo(Integer pid) {
		List<Item> Items = new ArrayList<>();
		getStockInfo().values().stream().forEach(e -> {
			Items.add(e.get(pid));
		});
		return Items;
	}

	@Override
	public void exec() throws Exception {
		stockdeal.dealStock();
	}

	@Override
	protected boolean ready() {
		return true;
	}
}
