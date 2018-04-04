package com.zeusas.dp.ordm.stock.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.zeusas.common.data.DDLDBMS;
import com.zeusas.common.data.Database;
import com.zeusas.common.data.DdlItem;
import com.zeusas.common.data.Meta;
import com.zeusas.common.data.Record;
import com.zeusas.common.data.Table;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.QueryHelper;
import com.zeusas.dp.ordm.entity.CartDetailDesc;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ReserveRecord;
import com.zeusas.dp.ordm.entity.StockReserve;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.ReserveRecordService;
import com.zeusas.dp.ordm.service.StockReserveService;
import com.zeusas.dp.ordm.service.WarehouseManager;
import com.zeusas.dp.ordm.stock.entity.Item;
import com.zeusas.dp.ordm.utils.OrdmConfig;

public final class StockSyncService {
	static final Logger log = LoggerFactory.getLogger(StockSyncService.class);
	static final String DDL = "task/sync_storehouses_ddl.xml";

	private boolean valid;

	private long stockStatusTime = -1;

	/**
	 * 现在默认是武汉仓，如果多他，这里应把仓库信息传入 <大仓ID -> <产品ID, 库存信息-Item>>
	 * 
	 * @since v1.2
	 */
	final Map<Integer, Map<Integer, Item>> stockItems = new LinkedHashMap<>();

	private Database erpDB;
	private Database wmsDB;
	
	private boolean updated;

	public StockSyncService() {
		
		valid = DDLDBMS.load(DDL);

		DdlItem item = DDLDBMS.getItem("ERP_SYNC");
		erpDB = new Database(item);
		DdlItem itemtest = DDLDBMS.getItem("WMS_SYNC");
		wmsDB = new Database(itemtest);
		updated = true;
	}

	public boolean isValid() {
		return valid;
	}

	/**
	 * 根据大仓ID，取得库存
	 * 
	 * @param warehouseID 大仓ID
	 * @return 库存
	 */
	public Map<Integer, Item> getStockInfo(Integer warehouseID) {
		if (!updated) {
			return stockItems.get(warehouseID);
		}
		try {
			setStockData(erpDB, wmsDB);
		} catch (SQLException e) {
			log.error("库存信息同步错误", e);
		}
		updated = false;
		return stockItems.get(warehouseID);
	}

	/**
	 * 取得所有库存信息
	 * 
	 * @return 库存信息 <仓ID-><产品ID, 库存>>
	 */
	public Map<Integer, Map<Integer, Item>> getStockInfo() {
		if (!updated) {
			return stockItems;
		}
		try {
			setStockData(erpDB, wmsDB);
		} catch (SQLException e) {
			log.error("库存信息同步错误", e);
		}
		updated = false;
		return stockItems;
	}

	public void setUpdated() {
		updated = true;
	}

	public void dealStock() {
		try {
			// 校验库存信息是否变化，变化直接刷新库存信息
			long tst = getStockStatusTime();
			if (updated //
					|| tst > stockStatusTime //
					|| getWmsStatusTime(wmsDB) < tst) {
				setStockData(erpDB, wmsDB);
				stockStatusTime = tst;
				updated = false;
			}
		} catch (Exception e) {
			log.error("库存同步错误。", e);
		} finally {
			erpDB.closeAll();
			wmsDB.closeAll();
		}
	}

	private long getStockStatusTime() throws SQLException {
		long stockStatusTime = -1L;
		Meta meta = erpDB.getDDL().getMeta("BIN_STATUS");

		Connection conn = erpDB.connect();
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(meta.getStmt());
			if (rs.next()) {
				stockStatusTime = rs.getDate(1).getTime();
			}
		} finally {
			QueryHelper.close(rs);
			QueryHelper.close(stmt);
		}
		return stockStatusTime;
	}
	/**
	 * 根据产品ID和仓库ID获取改当前产品库存
	 * @param stockId
	 * @param productId
	 * @return
	 */
	public int getStockProductQty(String stockId,String productId) {
		int qutity = 0;
		// 获取当前库存信息
		Map<Integer, Map<Integer, Item>> stocks = this.getStockInfo();
		// 选择对应的仓库
		Map<Integer, Item> selectStock = stocks.get(Integer.parseInt(stockId));
		Collection<Item> stockItems = selectStock.values();
		for (Item item : stockItems) {
			if(item.getPid().equals(Integer.parseInt(productId))) {
				qutity = item.getV();
				break;
			}
		}
		return qutity;
	}

	/**
	 * 同步的核心处理工作
	 * 
	 * @param erpdb
	 * @param wmsdb
	 * @throws SQLException
	 */
	// FIXME 加入扣减提交购物车的库存
	public void setStockData(Database erpdb, Database wmsdb) throws SQLException {
		final DictManager dictManager = AppContext.getBean(DictManager.class);
		WarehouseManager warehouseManager = AppContext.getBean(WarehouseManager.class);
		ReserveRecordService reserveRecordService = AppContext.getBean(ReserveRecordService.class);
		StockReserveService stockReserveService =AppContext.getBean(StockReserveService.class);
		
		// 装配仓库
		final Map<Integer, Map<Integer, Item>> warehouse = new LinkedHashMap<>();
		// 字典仓库的父ID:205
		for (Dictionary dict:dictManager.get(OrdmConfig.WAREHOUSE).getChildren()) {
			warehouse.put(TypeConverter.toInteger(dict.getHardCode()), new LinkedHashMap<>());
		}
		
		// FIXME: 以后版本使用总线微服务
		Table tb_inventory = erpdb.open("BIN_INVENTORY");
		// 获取所有商品库存信息 productID, stockId, qty
		for (Record r : tb_inventory.values()) {
			// 产品id
			Integer pid = r.getInteger(1);
			// k3中子仓库的id
			Integer childStockId = r.getInteger(2);
			// 大仓id
			Integer parentStockId = warehouseManager.get(childStockId).getPid();
			Item itm = warehouse.get(parentStockId).get(pid);

			if (itm == null) {
				// r.getInteger(3): 当前库存
				// 设置默认第四个参数  - 打欠标志为0 
				itm = new Item(pid, parentStockId, r.getInteger(3));
				warehouse.get(parentStockId).put(pid, itm);
			} else {
				itm.addValue(r.getInteger(3));
			}
		}

		// 获取并更新实际库存信息 金蝶库存-对应金蝶未审核单据数量-直发系统库存数量
		Table tb_erporder = erpdb.open("BIN_ERPORDER");
		for (Record r : tb_erporder.values()) {
			Item itm = getERPItem(warehouseManager, warehouse, r);
			if (itm == null || r.get(3) == null) {
				continue;
			}
			int rslt = itm.getV() - r.getInteger(3);
			itm.setV(rslt);
		}

		final ProductManager pm = AppContext.getBean(ProductManager.class);

		Table tb_wmsorder = wmsdb.open("BIN_WMSORDER");
		// 扣除订单内库存,并且去掉了预购产品
		tb_wmsorder.values()//
				.stream()//
				.filter(e -> e != null && getWMSItem(warehouse, e) != null)//
				.forEach(e -> {
					Item itm = getWMSItem(warehouse, e);
					itm.addValue(-1 * e.getInteger(3));
				});
		
		// 获取等待发货的预订产品
		List<ReserveRecord> reserveRecords = reserveRecordService.findRecordByStatus(ReserveRecord.STATUS_WAIT);
		for (ReserveRecord reserveRecord : reserveRecords) {
			Item itm = warehouse.get(reserveRecord.getWarehouse()).get(reserveRecord.getProductId());
			if(itm != null) {
				itm.addValue(-1 * reserveRecord.getQuantity());
			}
		}
		//购物车占的库存
		Table tb_wmscart = wmsdb.open("BIN_WMSCART", //
				System.currentTimeMillis(), //
				60 * 60 * 60 * 1000);
		for (Record record : tb_wmscart.values()) {
			Integer stockId = record.getInteger(1);
			Integer quantity = record.getInteger(2);
			List<CartDetailDesc> descs = JSON.parseArray(record.getString(3)).toJavaList(CartDetailDesc.class);
			for (CartDetailDesc desc : descs) {
				Item itm = warehouse.get(stockId).get(desc.getProductId());
				if(itm == null) {
					continue;
				}
				itm.addValue(-1 * quantity * desc.getQuantity());
			}
		}
		
		//预留库存
		List<StockReserve> stockReserves = stockReserveService.findAvalible();
		//状态为可用 在起始时间结束时间内的预留库存
		for (StockReserve stockReserve : stockReserves) {
			Integer pid=stockReserve.getProductId();
			for (Item reserveItem : stockReserve.getDetail()) {
				int qty= reserveItem.getV();
				//预留库存为0则不做操作
				if(qty==0){
					continue;
				}
				Integer wid=TypeConverter.toInteger(reserveItem.getW());
				//拿到前面库存计算结果
				Item itm = warehouse.get(wid).get(pid);
				if(itm!=null){
					itm.addValue(-qty);
				}else{
					reserveItem.setPid(pid);
					reserveItem.setV(-qty);
					warehouse.get(wid).put(pid, reserveItem);
				}
			}
		}
		
		
		// 把库存计算结果装配到内存: e 大仓ID
		stockItems.putAll(warehouse);
		for (Map<Integer, Item> items : warehouse.values()) {
			for (Item itm : items.values()) {
				Product p = pm.get(itm.getPid());
				if(p != null){
					p.addInv(itm.getW(), itm.getV());
				}
			}
		}
		warehouse.clear();
	}

	long getWmsStatusTime(Database wmsdb) throws SQLException {
		// FIXME FX 取到购物车的库存
		Meta meta = wmsdb.getDDL().getMeta("BIN_ORDERSTATUS");
		Connection conn = wmsdb.connect();
		Statement stmt = null;
		ResultSet rs = null;
		long wmsStatusTime = -1L;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(meta.getStmt());
			if (rs.next()) {
				wmsStatusTime = rs.getDate(1).getTime();
			}
		} finally {
			QueryHelper.close(rs);
			QueryHelper.close(stmt);
		}
		return wmsStatusTime;
	}

	private Item getERPItem(WarehouseManager whManager, Map<Integer, Map<Integer, Item>> warehouse, Record r) {
		Integer pid = r.getInteger(1);
		Integer childStockId = r.getInteger(2);
		Integer parentStockId = null;
		// 大仓id
		if (whManager.get(childStockId) != null) {
			parentStockId = whManager.get(childStockId).getPid();
		} 
		
		return warehouse.get(parentStockId).get(pid);
	}

	private Item getWMSItem(Map<Integer, Map<Integer, Item>> warehouse, Record r) {
		Integer pid = r.getInteger(1);
		Integer parentStockId = r.getInteger(2);
		return warehouse.get(parentStockId).get(pid);
	}
}
