package com.zeusas.dp.ordm.task;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.stylesheets.LinkStyle;

import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.task.CronTask;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.entity.ReserveProduct;
import com.zeusas.dp.ordm.entity.ReserveRecord;
import com.zeusas.dp.ordm.entity.Warehouse;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.ReserveProductManager;
import com.zeusas.dp.ordm.service.ReserveRecordService;
import com.zeusas.dp.ordm.service.StockService;
import com.zeusas.dp.ordm.service.SystemOrderService;
import com.zeusas.dp.ordm.stock.service.StockSyncService;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.service.AuthCenterManager;

public class ReserveOrderTask extends CronTask {
	
	static final Logger logger = LoggerFactory.getLogger(ReserveOrderTask.class);

	final Map<Integer, Map<String, Integer>> stocks = new LinkedHashMap<>();

	@Override
	public void exec() throws Exception {
		changeRecordSatatus();
		updateStock();
		// 不使用系统制单还欠
//		if (LocalTime.now().getHour() == 12) {
//			systemOrder();
//		}
		changeProductStatus();
	}

	/**
	 * 获取待发货的预订记录
	 * 
	 * @return <counterCode, List<ReserveRecord>>
	 */
	private Map<String, List<ReserveRecord>> getWaitShipRecord() {
		ReserveRecordService recordService = AppContext.getBean(ReserveRecordService.class);
		Map<String, List<ReserveRecord>> map = recordService.findRecordByStatus(ReserveRecord.STATUS_WAIT)//
				.stream()//
				.sorted(Comparator.comparing(ReserveRecord::getLastUpdate).reversed())
				.collect(Collectors.groupingBy(ReserveRecord::getCounterCode));
		return map;
	}

	/**
	 * 获取待还的预订记录
	 * 
	 * @return <productId, List<ReserveRecord>>
	 */
	private List<ReserveRecord> getReserveRecord() {
		ReserveRecordService recordService = AppContext.getBean(ReserveRecordService.class);
		List<ReserveRecord> records = recordService.findRecordByStatus(ReserveRecord.STATUS_RESERVED)//
				.stream()//
				.sorted(Comparator.comparing(ReserveRecord::getCreateTime).reversed())//
				.collect(Collectors.toList());
		return records;
	}
	
	/**
	 * 根据打欠记录判断是否还欠完成
	 * 当前时间>打欠结束时间的产品 (完成+取消==所有) &&(所有！=0)   
	 * 当前时间>打欠结束时间的产品 (未完成>=0) &&(所有！=0)
	 */
	//FIXME:
	private void changeProductStatus() {
		ReserveRecordService recordService = AppContext.getBean(ReserveRecordService.class);
		ReserveProductManager reserveProductManager = AppContext.getBean(ReserveProductManager.class);
		// 需要检查的产品及仓库<产品id,<未完成的仓库>>
		Map<Integer, List<Integer>> toCheckProducts = new HashMap<>();

		for (ReserveProduct reserveProduct : reserveProductManager.findAll()) {
			// 超过预订期 切状态为未还完的需要 去record看是否完成
			Map<String, Integer> statusMap = reserveProduct.getContext().getStatus();
			Long excuteStart = reserveProduct.getExcuteStart().getTime();
			Integer productId = reserveProduct.getProductId();

			for (String wid : statusMap.keySet()) {
				// 状态为未完成的 且打欠结束的
				//FiXME
				if (ReserveProduct.STATUS_DONE.intValue() != reserveProduct.getStatus(wid)//
						&& ReserveProduct.STATUS_BUY.intValue() != reserveProduct.getStatus(wid)//
						&&System.currentTimeMillis()>excuteStart) {
					// 装配
					if (!toCheckProducts.containsKey(productId)) {
						toCheckProducts.put(productId, new ArrayList<>());
					}
					toCheckProducts.get(productId).add(Integer.parseInt(wid));
				}
			}
		}
		if(toCheckProducts.size()<1){
			return;
		}
		// 需要检查的产品的记录
		List<ReserveRecord> records = recordService.findByProductId(toCheckProducts.keySet());
		Map<Integer, Map<String, Integer>> allStatus = new HashMap<>();
		Map<Integer, Map<String, Integer>> finishStatus = new HashMap<>();
		for (ReserveRecord reserveRecord : records) {

			Integer productId = reserveRecord.getProductId();
			String wid = reserveRecord.getWarehouse().toString();
			// 初始化是以所有状态的为主 所有状态map存在时 完成的没有则默认为0
			if (!allStatus.containsKey(productId)) {
				allStatus.put(productId, new HashMap<>());
				finishStatus.put(productId, new HashMap<>());
			}
			if (!allStatus.get(productId).containsKey(wid)) {
				allStatus.get(productId).put(wid, 0);
				finishStatus.get(productId).put(wid, 0);
			}
			// 所有状态根据仓库分组
			int all = allStatus.get(productId).get(wid) + 1;
			allStatus.get(productId).put(wid, all);

			Integer status = reserveRecord.getStatus();
			if (ReserveRecord.STATUS_CANCLE.equals(status)//
					|| ReserveRecord.STATUS_DONE.equals(status)) {
				int finish = finishStatus.get(productId).get(wid) + 1;
				finishStatus.get(productId).put(wid, finish);
			}
		}
		// 更新状态
		for (Integer productId : allStatus.keySet()) {
			ReserveProduct reserveProduct = reserveProductManager.get(productId);
			for (String wid : finishStatus.get(productId).keySet()) {
				Integer all = allStatus.get(productId).get(wid);
				Integer finish = finishStatus.get(productId).get(wid);
				// 所有记录数==完成+取消 && 所有！=0时为还欠完成
				if (all != 0 && all.intValue() == finish.intValue()) {
					reserveProduct.setOrAddStatus(wid, ReserveProduct.STATUS_DONE);
				}
			}
			reserveProductManager.update(reserveProduct);
		}
	}

	/**
	 * 占库存
	 * 根据需要占库存的记录 构建临时库存
	 */
	private void changeRecordSatatus() {
		StockService stockService = AppContext.getBean(StockService.class);
		//清理临时库存
		stocks.clear();
		
		ReserveRecordService recordService = AppContext.getBean(ReserveRecordService.class);
		List<ReserveRecord> reserveRecords = getReserveRecord();
		//活动打欠
		Map<Long, List<ReserveRecord>>  activityRecord=new HashMap<>();
		for (ReserveRecord record : reserveRecords) {
			if(!ReserveRecord.STATUS_RESERVED.equals(record.getStatus())){
				continue;
			}
			
			Integer productId = record.getProductId();
			String wid = record.getWarehouse().toString();
			//装配库存
			if(!stocks.containsKey(productId)){
				stocks.put(productId, new HashMap<>());
			}
			if(!stocks.get(productId).containsKey(wid)){
				Integer qty = stockService.getStockProductQty(wid, productId.toString());
				stocks.get(productId).put(wid, qty);
			}
			//不管活动单品都装入map 保证有序
			Long pid=record.getPid();
			Long detailId=record.getOrderDetailId();
			String activityId= record.getActivityId();
			if(activityId!=null){
				if(!activityRecord.containsKey(pid)){
					activityRecord.put(pid, new ArrayList<>());
				}
				activityRecord.get(pid).add(record);
			}else{
				if(!activityRecord.containsKey(detailId)){
					activityRecord.put(detailId, new ArrayList<>());
				}
				activityRecord.get(detailId).add(record);
			}
		}
		//占用库存
		for (Long id : activityRecord.keySet()) {
			List<ReserveRecord> records=activityRecord.get(id);
			//单品
			if(records.size()==1){
				ReserveRecord record=records.get(0);
				Integer productId = record.getProductId();
				String wid = record.getWarehouse().toString();
				Integer num = record.getQuantity();
				
				Integer qty = stocks.get(productId).get(wid);
				if (qty >= num) {
					stocks.get(productId).put(wid, qty - num);
					record.setStatus(ReserveRecord.STATUS_WAIT);
					recordService.update(record);
				}
			}
			if(records.size()>1){
				//活动明细库存全部充足
				boolean flag=true;
				for (ReserveRecord record : records) {
					Integer productId = record.getProductId();
					String wid = record.getWarehouse().toString();
					Integer num = record.getQuantity();
					
					Integer qty = stocks.get(productId).get(wid);
					if (qty < num) {
						//一个库存不不足则活动不满足
						flag=false;
					}
				}
				//活动不满足跳过该活动
				if(!flag){
					continue;
				}
				for (ReserveRecord record : records) {
					Integer productId = record.getProductId();
					String wid = record.getWarehouse().toString();
					Integer num = record.getQuantity();

					Integer qty = stocks.get(productId).get(wid);
					stocks.get(productId).put(wid, qty - num);
					record.setStatus(ReserveRecord.STATUS_WAIT);
					recordService.update(record);
				}
			}
		}
	}
	
	private void updateStock(){
		StorehousesSyncTask task = AppContext.getBean(StorehousesSyncTask.class);
		try {
			task.setUpdated();
			task.exec();
		} catch (Exception e) {
			logger.error("打欠记录占库存 通知库存定时任务重新计算时出错",e);
		}
	}
	

	/**
	 * 系统制单还欠
	 */
	private void systemOrder() {
		CounterManager counterManager = AppContext.getBean(CounterManager.class);
		SystemOrderService systemOrderService = AppContext.getBean(SystemOrderService.class);
		OrderService orderService = AppContext.getBean(OrderService.class);
		CustomerManager customerManager = AppContext.getBean(CustomerManager.class);
		AuthCenterManager authCenterManager = AppContext.getBean(AuthCenterManager.class);

		Map<String, List<ReserveRecord>> recordsMap = getWaitShipRecord();
		for (String counterCode : recordsMap.keySet()) {
			Counter counter = counterManager.getCounterByCode(counterCode);
			Customer customer = customerManager.get(counter.getCustomerId());
			List<ReserveRecord> records = recordsMap.get(counterCode);
			AuthUser makeUser = authCenterManager.getAuthUser("root");

			Order order = systemOrderService.buildOrderForReserve(counter, makeUser, customer.getCustomerTypeID(),
					records);
			List<OrderDetail> orderDetails = orderService.getOrderDetails(order.getOrderNo());
//			orderService.bulidPackage(order, orderDetails);
			orderService.bulidPackagePlus(order, orderDetails);
		}
	}
}
