package com.zeusas.dp.ordm.task;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.google.common.base.Objects;
import com.zeusas.common.data.DDLDBMS;
import com.zeusas.common.data.Database;
import com.zeusas.common.data.DdlItem;
import com.zeusas.common.data.Record;
import com.zeusas.common.data.Table;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.task.CronTask;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.DiffDetailService;
import com.zeusas.dp.ordm.service.OrderDetailService;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.ProductManager;

/**
 * 订单内，单品数据差分。
 * 
 * ORDER PUSH? ORDER (ORDM) -> P# M# original +/- diff (quantity) K3 -> P# M#
 * real
 * 
 * @author shihx
 * @date 2017年3月28日 上午8:29:53
 */
public class OrderDatailSyncTask extends CronTask {

	private OrderService orderService;
	private OrderDetailService orderDetailService;
	private ProductManager productManager;
	private DiffDetailService diffDetailService;
	static final Logger logger = LoggerFactory.getLogger(OrderDatailSyncTask.class);

	private Database k3DB;
	private Database ordmDB;

	final static String DDL_XML = "task/sync_ordersdetail_ddl.xml";
	/**
	 * Map<Orderno, Map<ProductId, List<OrderDeatil>>>
	 * 一个订单中一个产品id可能有多条明细
	 **/
	private final Map<String, Map<Integer, List<Record>>> orderdetailsold;
	/** Map <packageDetailId, OrderDetailId>   */
	private final Map<String, Long> findOrderDetailId;
	/** Map <OrderDetailId, qty>   每条订单明细实发数量计算结果*/
	private final Map<Long, Integer> orderDetailQty;
	/** Map <OrderNo, qty>   每条订单明细实发数量计算结果*/
	private final Map<String, Double> RealFees;
	/** Map <OrderNo, Order> 需要差分的订单  */
	private final Map<String, Order> todoDiffOrder;
	public OrderDatailSyncTask() {
		valid = DDLDBMS.load(DDL_XML);

		DdlItem k3Ditem = DDLDBMS.getItem("SYNC_ORDERDETAIL");

		k3DB = new Database(k3Ditem);

		DdlItem ordmDitem = DDLDBMS.getItem("ORDM_DIFFDETAIL");

		ordmDB = new Database(ordmDitem);

		orderService = AppContext.getBean(OrderService.class);
		orderDetailService = AppContext.getBean(OrderDetailService.class);
		productManager = AppContext.getBean(ProductManager.class);
		diffDetailService = AppContext.getBean(DiffDetailService.class);
		RealFees = new HashMap<>();

		orderdetailsold = new HashMap<>();
		todoDiffOrder = new HashMap<>();
		findOrderDetailId = new HashMap<>();
		orderDetailQty = new HashMap<>();
	}

	@Override
	public void exec() throws Exception {
		try {
			RealFees.clear();
			orderdetailsold.clear();
			todoDiffOrder.clear();
			findOrderDetailId.clear();
			orderDetailQty.clear();
			sync();
		} finally {
			close();
		}
	}

	@Override
	protected boolean ready() {
		return valid;
	}

	private void close() {
		k3DB.closeAll();
		ordmDB.closeAll();
	}

	/**
	 * 201702220015以及之前的订单没有关联上 201704060006以及之后的关联了packagedetail
	 * 装配K3订单详情数据（直发订单已经差分的订单不装配）
	 * 
	 * @throws SQLException
	 */
	private void sync() throws SQLException {
		Table k3DetailTB = k3DB.open("ORDERDETAIL_K3");
		Table k3RealFeeTB = k3DB.open("REALFEE_K3");
		Table ordmPackageDetailId = ordmDB.open("ORDM_PACKAGEDETAILID");

		Connection ordmConn = ordmDB.connect();

		// 直发系统订单头
		List<Order> ordmOrders = orderService.getOrders(Order.status_WaitShip);

		//装配需要差分的订单
		for (Order order : ordmOrders) {
			//订单差分标志是生成订单时根据门店类型初始化的
			if(!Order.diffStatus_todo.equals(order.getDiffStatus())){
				continue;
			}
			// 取出所有处理的订单 1702220015以及之前的订单没有关联上
			if (order.getId() > 1702220015L) {
				todoDiffOrder.put(order.getOrderNo(), order);
			}
		}
		
		//装配packageDetailId与OrderDetailId关系
		for (Record r : ordmPackageDetailId.values()) {
			findOrderDetailId.put(r.getString(1), r.getLong(2));
		}


		// 装配k3 orderdetail
		// 201702220015以及之前的订单没有关联上
		// 201704060006以及之后的关联了packagedetail
		for (Record r : k3DetailTB.values()) {
			String orderNo = r.getString(1);
			
			if(!todoDiffOrder.containsKey(orderNo)){
				continue;
			}
			//201702220015 到201704060006间的单子  单条明细未与金蝶关联  需要根据产品id把quantity加起来
			if (201702220015l <= r.getLong(1)//
					&& r.getLong(1) < 201704060006l) {

				Integer productId = r.getInteger(2);

				Map<Integer, List<Record>> productIdMap = orderdetailsold.get(orderNo);
				if (productIdMap == null) {
					productIdMap = new HashMap<>();
					orderdetailsold.put(orderNo, productIdMap);
				}
				if (!productIdMap.containsKey(productId)) {
					List<Record> detals = new ArrayList<>();
					orderdetailsold.get(orderNo).put(productId, detals);
				}
				productIdMap.get(productId).add(r);

			} else if (r.getLong(1) >= 201704060006l) {
				String packageDetailId=r.getString(6);
				Integer pid = r.getInteger(2);
				//packageDetailId为空:直发订单中没有 金蝶对订单追加的产品 
				if(productManager.get(pid) == null){
					logger.warn("订单差分，产品未同步到直发，产品id：" + pid);
					continue;
				}
				if(packageDetailId==null||packageDetailId.isEmpty()){
					insertDetail(r, ordmDB, ordmConn);
					continue;
				}
				//根据packageDetailId与orderDetailid关系 计算qty
				Long orderDetailId=findOrderDetailId.get(packageDetailId);
				Integer qty=r.getInteger(3);
				if(orderDetailId!=null){
					if(!orderDetailQty.containsKey(orderDetailId)){
						orderDetailQty.put(orderDetailId, qty);
					}else{
						orderDetailQty.put(orderDetailId, orderDetailQty.get(orderDetailId)+qty);
					}
				}
			}
		}

		// 装配k3 单头金额
		for (Record r : k3RealFeeTB.values()) {
			String orderNo = r.getString(1);
			if (todoDiffOrder.containsKey(orderNo)) {
				RealFees.put(orderNo, TypeConverter.toDouble(r.get(2)));
			}
		}
		
		for (Order order : todoDiffOrder.values()) {
			//1702220015到1704060006之间的订单详情 同一订单内同一产品相加
			if(1702220015l <= order.getId()//
					&& order.getId() < 1704060006l){

				final String orderNo = order.getOrderNo();
				List<OrderDetail> myDetails = orderService.getOrderDetails(orderNo);

				// 一个订单的详细 key:productId
				Map<Integer, List<Record>> k3Details = orderdetailsold.get(orderNo);
				// 未发的订单详单
				if (k3Details == null) {
					continue;
				}
				// 直发单子明细中产品ID 的set 用于反查
				Set<Integer> wmsProductIds = new HashSet<>();

				// TO DO:差分
				for (OrderDetail myOd : myDetails) {
					wmsProductIds.add(myOd.getProductId());

					// 一个订单中某一产品的多条详细
					List<Record> details = k3Details.get(myOd.getProductId());
					if (details == null) {
						continue;
					}
					for (Record k3Od : details) {
						// FIXME:差分判断 和处理
						// 现有问题:金蝶订单详情条目数量比直发多,其中某一单品发货数量比直发订单多
						if (checkDetails(myOd, k3Od, k3DetailTB)) {
							myOd.setRealQty(myOd.getRealQty() + k3Od.getInteger(3));
							orderDetailService.update(myOd);
						}
					}
				}
				// 单头总金额
				Double realFee=RealFees.get(orderNo);
				Double paymentFee=order.getPaymentFee();
				if(realFee==paymentFee*2){
					logger.warn("订单差分订单金额异常 ,单号{},直发订单金额{},金蝶金额{}",orderNo,paymentFee,realFee);
				}else if(realFee>paymentFee){
					logger.warn("订单差分订单金额可能异常,待排查确认, 单号{},直发订单金额{},金蝶金额{}",orderNo,paymentFee,realFee);
				}
				order.setRealFee(RealFees.get(orderNo));
				orderService.update(order);
				// 金蝶单子明细中产品ID 的set
				Set<Integer> K3ProductIds = k3Details.keySet();

				//FIXME:反查 找出金蝶发的 而直发订单里没有的(订货政策 ...)
				for (Integer productId : K3ProductIds) {
					if (wmsProductIds.contains(productId)) {
						continue;
					}
					
					List<Record> details = k3Details.get(productId);
					for (Record record : details) {
						Integer pid = record.getInteger(2);
						if (productManager.get(pid) != null) {
							insertDetail(record, ordmDB, ordmConn);
						} else {
							logger.warn("订单差分，产品未同步到直发，产品id：" + pid);
						}
					}
				}
			
			}else if(order.getId() >= 1704060006l){
			//1704060006之后订单使用packageDetailId关联 packageDetailId>=201704070024
				final String orderNo = order.getOrderNo();
				List<OrderDetail> myDetails = orderService.getOrderDetails(orderNo);

				for (OrderDetail myOd : myDetails) {
					Long orderDetailId=myOd.getId();
					
					if(orderDetailQty.containsKey(orderDetailId)){
						myOd.setRealQty(orderDetailQty.get(orderDetailId));
					}else {
						myOd.setRealQty(0);
					}
					orderDetailService.update(myOd);
				}
				// 单头总金额
				Double realFee=RealFees.get(orderNo);
				Double paymentFee=order.getPaymentFee();
				if(realFee==paymentFee*2){
					logger.warn("订单差分订单金额异常,单号{},直发订单金额{},金蝶金额{}",orderNo,paymentFee,realFee);
				}else if(realFee>paymentFee){
					logger.warn("订单差分订单金额可能异常,待排查确认, 单号{},直发订单金额{},金蝶金额{}",orderNo,paymentFee,realFee);
				}
				order.setRealFee(realFee);
				order.setDiffStatus(Order.diffStatus_doen);
				orderService.update(order);
			}
		}
	}

	/**
	 * 
	 * @param myDetail
	 * @param k3Detail
	 * @param detailTB
	 * @return
	 */
	// FIXME :活动上后 也许根据关联判断
	private boolean checkDetails(OrderDetail myDetail, Record k3Detail, Table detailTB) {

		String OrderNo = (String) detailTB.getFieldData(k3Detail, "OrderNo");
		if (!Objects.equal(OrderNo, myDetail.getOrderNo())) {
			return false;
		}

		Integer ProductId = (Integer) detailTB.getFieldData(k3Detail, "ProductId");
		if (!Objects.equal(ProductId, myDetail.getProductId())) {
			return false;
		}

		Integer RealQty = TypeConverter.toInteger(detailTB.getFieldData(k3Detail, "RealQty"));
		if (RealQty > myDetail.getProductId()) {
			return false;
		}

		return true;
	}

	/**
	 * K3单子里有 直发里没有的 比如:活动功能实现前的订货政策
	 * 
	 * @param detail
	 * @param DB
	 * @param conn
	 * @throws SQLException
	 */
	private void insertDetail(Record detail, Database DB, Connection conn) throws SQLException {
		try {
			DB.execUpdate("INSERT_DIFFDETAIL", //
					detail.get(1), //
					detail.get(2), //
					productManager.get(detail.getInteger(2)).getName(),//
					detail.get(3), //
					detail.get(4), //
					detail.get(5));
		} catch (Exception e) {
			logger.error("插入金蝶订单多出来的明细错误:{}", detail.toString());
			throw new SQLException(e);
		}
		conn.clearWarnings();
	}
	
	/**
	 * sql相对原来的方法增多  除去原来因为数据完整性产生的判断条件  一个订单一个事务处理
	 */
	private void orderDiff(){
		try {
			List<Record> k3OrderDetail = diffDetailService.getK3OrderDetail();
			Map<String, List<Record>>orderNo_Record=new HashMap<>();
			//金蝶回来的订单明细根据单号分组
			for (Record record : k3OrderDetail) {
				String orderNo=record.getString(1);
				if(!orderNo_Record.containsKey(orderNo)){
					orderNo_Record.put(orderNo, new ArrayList<>());
				}
				orderNo_Record.get(orderNo).add(record);
			}
			//直发状态为7 切需要差分的订单
			List<Order> todoOrder = diffDetailService.getTodoOrder();
			for (Order order : todoOrder) {
				String orderNo=order.getOrderNo();
				List<Record> records = orderNo_Record.get(orderNo);
				if(records!=null&&!records.isEmpty()){
					diffDetailService.saveDiffResult(order, records);
				}
			}
		} catch (Exception e) {
			logger.error("订单差分错误", e);
		}
	}
}
