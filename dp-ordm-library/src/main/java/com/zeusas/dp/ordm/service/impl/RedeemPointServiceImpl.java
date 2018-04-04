package com.zeusas.dp.ordm.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.zeusas.common.data.Record;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.service.IdGenService;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.dao.RedeemPointDao;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.Item;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.entity.RedeemPoint;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.RedeemPointService;
import com.zeusas.dp.ordm.service.StockService;
import com.zeusas.dp.ordm.service.SystemOrderService;
import com.zeusas.dp.ordm.utils.PoiUtil;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.service.AuthCenterManager;

@Service
@Transactional
public class RedeemPointServiceImpl extends BasicService<RedeemPoint, Long> implements RedeemPointService {
	private static Logger logger = LoggerFactory.getLogger(RedeemPointServiceImpl.class);

	public final static String ID_REDEEMPOINTID = "REDEEMPOINTID";
	@Autowired
	private RedeemPointDao dao;
	@Autowired
	private IdGenService idGenService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private SystemOrderService systemOrderService;

	@Override
	protected Dao<RedeemPoint, Long> getDao() {
		return dao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RedeemPoint> findByCounterCode(String CounterCode) throws ServiceException {
		List<RedeemPoint> redeemPoints = new ArrayList<>();
		try {
			redeemPoints = dao.findByCounterCode(CounterCode);
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (Exception e) {
			logger.error("查询积分兑换错误，柜台号:{}", CounterCode, e);
			throw new ServiceException(e);
		}
		return redeemPoints;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RedeemPoint> findByAvaiCounterCode(String CounterCode) throws ServiceException {
		List<RedeemPoint> redeemPoints = new ArrayList<>();
		try {
			redeemPoints = dao.findByCounterCode(CounterCode, true);
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (Exception e) {
			logger.error("查询积分兑换错误，柜台号:{}", CounterCode, e);
			throw new ServiceException(e);
		}
		return redeemPoints;
	}

	@Override
	public Order addToOrder(Order order) throws ServiceException {
		StockService stockService = AppContext.getBean(StockService.class);
		CounterManager counterManager = AppContext.getBean(CounterManager.class);
		String orderNo = order.getOrderNo();
		Assert.notNull(orderNo, "往订单添加积分兑换，订单号为空");
		try {
			String counterCode = order.getCounterCode();
			Counter counter = counterManager.getCounterByCode(counterCode);
			Assert.notNull(counter, "往订单添加积分兑换，柜台为空，柜台号：" + counterCode);
			String wid = counter.getWarehouses();

			List<RedeemPoint> redeemPoints = dao.findTodoByCounterCode(counterCode);
			if (redeemPoints.isEmpty()) {
				logger.debug("该订单没有可添加到订单的积分兑换产品");
				return order;
			}

			List<Item> items = new ArrayList<>(redeemPoints.size());
			for (RedeemPoint redeemPoint : redeemPoints) {
				//校验库存
				Integer productId = redeemPoint.getProductId();
				int num = redeemPoint.getNum();

				int qty = stockService.getStockProductQty(wid, productId.toString());
				//库存不足
				if (qty < num) {
					logger.info("积分兑换系统制单库存不足，柜台号{}，产品ID{}，数量{}，当前库存{}", //
							counterCode, productId, num, qty);
					continue;
				}
				Item item = new Item(redeemPoint.getProductId().toString(), //
						redeemPoint.getNum(), //
						0.0d, //
						redeemPoint.getDeliveryWay());
				item.setDetalType(OrderDetail.TYPE_REDEEMPOINTS);
				items.add(item);
				//更新成已经发货
				redeemPoint.setExcuteNo(orderNo);
				redeemPoint.setLastUpdate(System.currentTimeMillis());
				dao.update(redeemPoint);
			}
			//积分兑换产品加入订单明细
			order = orderService.addFreeMaterial(orderNo, items);
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.error("往订单添加积分兑换错误，订单号:{}", orderNo, e);
			throw new ServiceException(e);
		}
		return order;
	}

	@Override
	public void removeRedeem(String orderNo) throws ServiceException {
		List<RedeemPoint> redeemPoints = new ArrayList<>();
		try {
			redeemPoints = dao.findByExcuteNo(orderNo);
			if (redeemPoints == null) {
				logger.warn("恢复积分兑换数据错误 ，订单里没有积分兑换产品， 订单号:{}", orderNo);
			}
			for (RedeemPoint redeemPoint : redeemPoints) {
				//ExcuteNo为空表示该数据未加入订单
				redeemPoint.setExcuteNo(null);
				redeemPoint.setLastUpdate(System.currentTimeMillis());
				dao.save(redeemPoint);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (Exception e) {
			logger.error("恢复积分兑换数据错误，订单号:{}", orderNo, e);
			throw new ServiceException(e);
		}

	}

	@Override
	public List<Record> getExcel(File file, DSResponse response) throws IOException {
		response.getMessage().clear();
		response.addMessage("错误数据，请检查输入数据格式!");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat df = new DecimalFormat("0");
		Workbook workbook = PoiUtil.getWorkbook(new FileInputStream(file), file);
		Sheet sheet = workbook.getSheetAt(0);
		int rowNum = sheet.getPhysicalNumberOfRows();
		int colNum = sheet.getRow(0).getPhysicalNumberOfCells();
		// 验证表头
		if (colNum != 5) {
			response.getMessage().add("表头错误，必须为5列！");
			throw new IOException();
		}
		Row row0 = sheet.getRow(0);
		String rowName0 = getXCellVal(row0.getCell(0), sdf, df);
		String rowName1 = getXCellVal(row0.getCell(1), sdf, df);
		String rowName2 = getXCellVal(row0.getCell(2), sdf, df);
		String rowName3 = getXCellVal(row0.getCell(3), sdf, df);
		String rowName4 = getXCellVal(row0.getCell(4), sdf, df);
		if (!("柜台号".equals(rowName0) || "counterCode".equalsIgnoreCase(rowName0))) {
			response.getMessage().add("表头错误，第一列名称必须为柜台号");
			throw new IOException();
		}
		if (!("产品编码".equals(rowName1) || "productCode".equalsIgnoreCase(rowName1))) {
			response.getMessage().add("表头错误，第二列名称必须为产品编码");
			throw new IOException();
		}
		if (!"数量".equals(rowName2)) {
			response.getMessage().add("表头错误，第三列名称必须为数量");
			throw new IOException();
		}
		if (!("发货方式".equals(rowName3) || "deliveryWay".equalsIgnoreCase(rowName1))) {
			response.getMessage().add("表头错误，第四列名称必须发货方式");
			throw new IOException();
		}
		if (!("备注".equals(rowName4))) {
			response.getMessage().add("表头错误，第五列名称必须备注");
			throw new IOException();
		}
		List<Record> list = new ArrayList<>(rowNum - 1);
		String xCellVal = null;

		// i=0时，为表头，从i=1开始获取Excel数据
		for (int i = 1; i < rowNum; i++) {
			Row row = sheet.getRow(i);
			// 得到当前行中存在数据的列数
			Record r = new Record(colNum - 1);
			for (int j = 0; j < colNum; j++) {
				Cell cell = row.getCell(j);
				if (cell == null) {
					xCellVal = "";
				} else {
					xCellVal = getXCellVal(cell, sdf, df);
				}
				r.set(j, xCellVal);
			}
			list.add(r);
		}
		int size = list.size();
		String isCounterCode = "^[0-9]{1,20}$";
		String isNum = "^[0-9]{1,11}$";
		String delivery = "^[0-9]{2}$";
		for (int i = 0; i < size; i++) {
			Object cell;
			StringBuilder sb = null;
			if ((cell = list.get(i).getValue()[0]) == null || !(cell instanceof String)
					|| !(cell.toString().matches(isCounterCode)))
				sb = new StringBuilder(", 柜台号格式不正确");
			if ((cell = list.get(i).getValue()[1]) == null || !(cell instanceof String))
				sb = (sb == null ? new StringBuilder() : sb).append(", 产品CODE格式不正确");
			if ((cell = list.get(i).getValue()[2]) == null || !(cell.toString().matches(isNum)))
				sb = (sb == null ? new StringBuilder() : sb).append(", 数量格式不正确;");
			if ((cell = list.get(i).getValue()[3]) == null || !(cell.toString().matches(delivery)))
				sb = (sb == null ? new StringBuilder() : sb).append(", 发货方式不正确;");
			if (sb != null)
				response.getMessage().add(new StringBuilder("第").append(i + 1).append("行").append(sb).toString());
		}
		if (response.getMessage().size() > 1)
			throw new IOException();
		return list;
	}

	@SuppressWarnings("deprecation")
	private static String getXCellVal(Cell cell, SimpleDateFormat sdf, DecimalFormat df) {
		String val = null;
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				val = sdf.format(cell.getDateCellValue()); // 日期型
			} else {
				val = df.format(cell.getNumericCellValue()); // 数字型
			}
			break;
		case XSSFCell.CELL_TYPE_STRING: // 文本类型
			val = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN: // 布尔型
			val = String.valueOf(cell.getBooleanCellValue());
			break;
		case XSSFCell.CELL_TYPE_BLANK: // 空白
			val = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_ERROR: // 错误
			val = "错误";
			break;
		case XSSFCell.CELL_TYPE_FORMULA: // 公式
			try {
				val = String.valueOf(cell.getStringCellValue());
			} catch (IllegalStateException e) {
				val = String.valueOf(cell.getNumericCellValue());
			}
			break;
		default:
			val = cell.getRichStringCellValue() == null ? null : cell.getRichStringCellValue().toString();
		}
		return val;
	}

	@Override
	public RedeemPoint createRedeemPoint(RedeemPoint redeemPoint) throws ServiceException {
		idGenService.lock(ID_REDEEMPOINTID);
		try {
			long id = Long.parseLong(idGenService.generateDateId(ID_REDEEMPOINTID));
			redeemPoint.setId(id);
			redeemPoint.setLastUpdate(System.currentTimeMillis());
			dao.save(redeemPoint);
			return redeemPoint;
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.error("创建积分兑换记录错误", e);
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_REDEEMPOINTID);
		}
	}

	@Override
	public void buildSystemOrder() throws ServiceException {
		CounterManager counterManager = AppContext.getBean(CounterManager.class);
		CustomerManager customerManager = AppContext.getBean(CustomerManager.class);
		StockService stockService = AppContext.getBean(StockService.class);
		AuthCenterManager authCenterManager = AppContext.getBean(AuthCenterManager.class);

		List<RedeemPoint> redeemPoints = new ArrayList<>();
		Map<Integer, Map<Integer, com.zeusas.dp.ordm.stock.entity.Item>> StockItems = stockService.getallStock();
		try {
			redeemPoints = dao.findTodo();
			//把积分兑换记录按柜台号并归
			Map<String, List<RedeemPoint>> groupByCounterCode = new HashMap<>();
			//单头 计算折扣用到客户
			Map<String, Customer> customers = new HashMap<>();
			for (RedeemPoint redeemPoint : redeemPoints) {
				String counterCode = redeemPoint.getCounterCode();
				Counter counter = counterManager.getCounterByCode(counterCode);
				//柜台被禁用时通过Code取不到Counter
				if (counter == null) {
					logger.debug("积分兑换系统制单柜台不存在或者被禁用，柜台号{}", counterCode);
					continue;
				}
				Integer customerId = counter.getCustomerId();
				Customer customer = customerManager.get(customerId);
				if (customer == null) {
					logger.debug("积分兑换系统制单客户不存在或者，客户ID{}", customerId);
					continue;
				}
				customers.put(counterCode, customer);
				//校验库存
				Integer wid = TypeConverter.toInteger(counter.getWarehouses());
				Integer productId = redeemPoint.getProductId();
				int num = redeemPoint.getNum();
				//XXX: 批量制单 直接操作库存？
//				int qty = stockService.getStockProductQty(wid, productId.toString());
				int qty = 0;
				Map<Integer, com.zeusas.dp.ordm.stock.entity.Item> warehouseStock = StockItems.get(wid);
				if (warehouseStock == null) {
					logger.info("系统制单，仓库库存不存在，仓库id{}", wid);
					continue;
				}
				com.zeusas.dp.ordm.stock.entity.Item stockitem = warehouseStock.get(productId);
				if (stockitem == null) {
					logger.info("系统制单，库存不存在，仓库id{}，产品id{}", wid, productId);
					continue;
				}
				qty = stockitem.getV();
				//库存不足
				if (qty >= num) {
					stockitem.addValue(-num);
				} else {
					logger.info("积分兑换系统制单库存不足，柜台号{}，产品ID{}，数量{}，当前库存{}", //
							counterCode, productId, num, qty);
					continue;
				}
				//根据柜台号分类
				if (!groupByCounterCode.containsKey(counterCode)) {
					groupByCounterCode.put(counterCode, new ArrayList<>());
				}
				groupByCounterCode.get(counterCode).add(redeemPoint);

			}
			//系统制单
			for (Map.Entry<String, List<RedeemPoint>> e : groupByCounterCode.entrySet()) {
				String counterCode = e.getKey();
				AuthUser root = authCenterManager.getAuthUser("root");
				Customer customer = customers.get(counterCode);
				Counter counter = counterManager.getCounterByCode(counterCode);
				List<Item> todoItem = new ArrayList<>();
				for (RedeemPoint redeemPoint : e.getValue()) {
					Item item = new Item(redeemPoint.getProductId().toString(), //
							redeemPoint.getNum(), //
							0.0d, //
							redeemPoint.getDeliveryWay());
					item.setDetalType(OrderDetail.TYPE_REDEEMPOINTS);
					todoItem.add(item);
				}
				Order order = systemOrderService.buildOrder(counter, root, customer.getCustomerTypeID(), todoItem);
				for (RedeemPoint redeemPoint : e.getValue()) {
					//更新成已经发货
					redeemPoint.setExcuteNo(order.getOrderNo());
					redeemPoint.setLastUpdate(System.currentTimeMillis());
					dao.update(redeemPoint);
				}
				List<OrderDetail> orderDetails = orderService.getOrderDetails(order.getOrderNo());
				orderService.bulidPackagePlus(order, orderDetails);
			}
			//重新计算库存
			stockService.setUpdated();
			stockService.rebuildSrock();
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.error("积分兑换系统制单错误", e);
			throw new ServiceException(e);
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<RedeemPoint> getAvalible() throws ServiceException {
		List<RedeemPoint> redeemPoints = new ArrayList<>();
		try {
			redeemPoints = dao.findAvalible();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return redeemPoints;
	}

	@Override
	public void cancel(Long id) throws ServiceException {
		RedeemPoint redeem = dao.get(id);
		if (redeem == null)
			throw new ServiceException("记录不存在!");
		if (!redeem.getAvalible())
			throw new ServiceException("记录已经失效，请勿重复操作！");
		else if (redeem.getExcuteNo() != null)
			throw new ServiceException("记录已经生效，不能删除。请联系管理员删除！");
		redeem.setAvalible(false);
		redeem.setLastUpdate(System.currentTimeMillis());
		dao.update(redeem);
	}
}
