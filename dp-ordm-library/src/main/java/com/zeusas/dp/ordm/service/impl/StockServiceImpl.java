package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.active.model.ProductItem;
import com.zeusas.dp.ordm.active.model.ProductRule;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.ReserveProductManager;
import com.zeusas.dp.ordm.service.StockService;
import com.zeusas.dp.ordm.stock.entity.Item;
import com.zeusas.dp.ordm.stock.entity.RequestParameters;
import com.zeusas.dp.ordm.stock.entity.ResultMessage;
import com.zeusas.dp.ordm.task.StorehousesSyncTask;

@Service
public class StockServiceImpl implements StockService {

	@Override
	public ResultMessage get() {
		StorehousesSyncTask task = AppContext.getBean(StorehousesSyncTask.class);
		Collection<Item> litems = task.getAllItem();
		return new ResultMessage(1, null, null, litems);
	}

	@Override
	public ResultMessage getDataByID(List<RequestParameters> requestParameters) {
		List<Item> values = new ArrayList<Item>(1);
		StorehousesSyncTask task = AppContext.getBean(StorehousesSyncTask.class);
		Map<Integer, Map<Integer, Item>> stocks = task.getStockInfo();
		// 现在仓库变成多个了，需要选择
		for (RequestParameters rp : requestParameters) {
			Integer pid = rp.getId();
			Integer subId = rp.getSubID();//这里是仓库id
			Map<Integer, Item> items = stocks.get(subId);
			Item itm = null;
			if (items != null) {
				itm = items.get(pid);
			}
			// 
			if (itm == null) {
				itm = new Item(pid, subId, 0);
			}
			values.add(itm);
		}
		return new ResultMessage(1, null, null, values);
	}

	@Override
	public ResultMessage getDataByID(List<RequestParameters> requestParameters, Counter counter) {
		List<Item> values = new ArrayList<Item>(1);
		StorehousesSyncTask task = AppContext.getBean(StorehousesSyncTask.class);
		ReserveProductManager reserveProductManager = AppContext.getBean(ReserveProductManager.class);
		ActivityManager activityManager = AppContext.getBean(ActivityManager.class);
		if (counter == null) {
			return new ResultMessage(1, null, null, values);
		}
		Map<Integer, Map<Integer, Item>> stocks = task.getStockInfo();
		// 现在仓库变成多个了，需要选择
		for (RequestParameters rp : requestParameters) {
			Integer pid = rp.getId();
			Integer subId = rp.getSubID();//这里是仓库id
			Map<Integer, Item> items = stocks.get(subId);
			Item itm = null;
			// 可打欠标志
			// 单品
			boolean isSelling = reserveProductManager.isSelling(pid, counter.getWarehouses());
			boolean isRePro = reserveProductManager.isReservable(pid, counter.getWarehouses());
			boolean isActPro = activityManager.isReservable(counter, pid);
			if (isSelling || isRePro || isActPro) {
				if (items != null) {
					itm = items.get(pid);
					if (isRePro) {
						// 单品打欠标志
						itm.setT(1);
					}
					if (isActPro) {
						// 活动打欠标志
						itm.setA(1);
					}
				}
			}
			// 活动
			if (itm == null) {
				itm = new Item(pid, subId, 0);
			}
			values.add(itm);
		}
		return new ResultMessage(1, null, null, values);
	}

	@Override
	public int getStockProductQty(String stockId, String productId) {
		StorehousesSyncTask task = AppContext.getBean(StorehousesSyncTask.class);
		int qutity = 0;
		// 获取当前库存信息
		Map<Integer, Map<Integer, Item>> stocks = task.getStockInfo();
		// 选择对应的仓库
		Map<Integer, Item> selectStock = stocks.get(Integer.parseInt(stockId));

		//更改
		// @author tianyr
		// @date 2018-1-18
		Item item;
		if ((item = selectStock.get(Integer.parseInt(productId))) != null)
			qutity = item.getV();
		return qutity;
	}

	@Override
	public Map<Integer, Map<Integer, Item>> getallStock() {
		StorehousesSyncTask task = AppContext.getBean(StorehousesSyncTask.class);
		return task.getStockInfo();
	}

	@Override
	public List<Item> getStrock(Integer productId) {
		List<Item> items = new ArrayList<>();
		StorehousesSyncTask task = AppContext.getBean(StorehousesSyncTask.class);
		Map<Integer, Map<Integer, Item>> stocks = task.getStockInfo();
		//8次
		for (Map<Integer, Item> map : stocks.values()) {
			Item itm = map.get(productId);
			if (itm != null) {
				items.add(itm);
			}
		}
		return items;
	}

	@Override
	public void setUpdated() {
		StorehousesSyncTask task = AppContext.getBean(StorehousesSyncTask.class);
		task.setUpdated();
	}

	@Override
	public void rebuildSrock() throws Exception {
		StorehousesSyncTask task = AppContext.getBean(StorehousesSyncTask.class);
		task.exec();
	}
	
	@Override
	public int getSuitNum(ProductRule productRule,int suitNum,Order order){
		final StockService stockService = AppContext.getBean(StockService.class);
		final CounterManager counterManager = AppContext.getBean(CounterManager.class);
		final ReserveProductManager  reserveProductManager = AppContext.getBean(ReserveProductManager.class);
		// 有效校验
		Counter counter = counterManager.get(order.getCounterId());
		String wid = counter.getWarehouses();
		Assert.notNull(wid, "自动加入大礼包校验库存仓库为空");
		for (ProductItem item : productRule.getProductItem().values()) {
			Integer productId = item.getPid();
			Integer num = item.getQuantity();
			// 大礼包里产品打欠不校验库存
			if (reserveProductManager.isReserving(productId, wid)) {
				continue;
			}
			Integer qty = stockService.getStockProductQty(wid, productId.toString());
			int suit = qty / num;
			if (suit >= 0 && suit < suitNum) {
				suitNum = suit;
			}
		}
		return suitNum;
	}


}
