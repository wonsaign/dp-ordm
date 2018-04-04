package com.zeusas.dp.ordm.service.impl;

import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MAXAMOUNT;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MAXPOSTAGE;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MIDPOSTAGE;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MINIAMOUNT;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_NOPOSTAGE;
import static com.zeusas.dp.ordm.utils.OrdmConfig.ORDMCONFIG;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

import com.zeusas.beans.BeanClone;
import com.zeusas.common.data.Record;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.service.IdGenService;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.dao.OrderCredentialsDao;
import com.zeusas.dp.ordm.dao.OrderDao;
import com.zeusas.dp.ordm.dao.OrderDetailDao;
import com.zeusas.dp.ordm.dao.PreOrderDao;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.CustomerPricePolicy;
import com.zeusas.dp.ordm.entity.FixedPrice;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderCredentials;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.entity.PreOrder;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.FixedPriceManager;
import com.zeusas.dp.ordm.service.OrderCredentialsService;
import com.zeusas.dp.ordm.service.OrderDetailService;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.PreOrderService;
import com.zeusas.dp.ordm.service.PricePolicyManager;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.utils.OrdmConfig;
import com.zeusas.dp.ordm.utils.PoiUtil;
import com.zeusas.security.auth.entity.AuthUser;

/**
 * 系统导入礼盒功能及包含礼盒订单合并
 * 
 * @author pengbo
 *
 */
@Service
@Transactional
public class PreOrderServiceImpl extends BasicService<PreOrder, Long> implements PreOrderService {

	final static Logger logger = LoggerFactory.getLogger(PreOrderServiceImpl.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public final static String ID_ORDERID = "ORDERID";
	public final static String ID_ORDERNO = "ORDERNO";
	public final static String ID_ORDERCREDENTIALID = "ORDERCREDENTIAL";
	public final static String ID_ORDERDETAILID = "ORDERDETAILID";
	public final static String ID_CARTDETAILID = "CARTDETAILID";

	// 数字格式，防止长数字成为科学计数法形式，或者int变为double形式
	private static DecimalFormat df = new DecimalFormat("0");

	private static String val = null;
	@Autowired
	private PreOrderDao dao;
	@Autowired
	private OrderDao orderdao;
	@Autowired
	private OrderDetailDao orderdetaildao;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private OrderCredentialsService orderCredentialsService;

	@Autowired
	private OrderCredentialsDao orderCredentialdao;

	@Autowired
	private IdGenService idGenService;

	@Override
	protected Dao<PreOrder, Long> getDao() {
		return dao;
	}

	@Override
	public boolean insertExcel(File file) throws IOException {

		List<Record> list = new ArrayList<>();
		try {
			Workbook workbook = PoiUtil.getWorkbook(new FileInputStream(file), file);
			// 获取第一张表
			Sheet sheet = workbook.getSheet("Sheet1");
			// 得到数据的行数/列数
			int rowNum = sheet.getPhysicalNumberOfRows();
			int colNum = sheet.getRow(0).getPhysicalNumberOfCells();
			String xCellVal = null;
			list.clear();
			// i=0时，为表头，从i=1开始获取Excel数据
			for (int i = 1; i < rowNum; i++) {
				Row row = sheet.getRow(i);
				// 得到当前行中存在数据的列数
				Record r = new Record(colNum);
				for (int j = 0; j < colNum; j++) {
					Cell cell = row.getCell(j);
					if (cell == null) {
						xCellVal = "";
					} else {
						xCellVal = getXCellVal(cell);
					}
					r.set(j, xCellVal);
				}
				list.add(r);
			}
			for (Record r : list) {
				PreOrder po = new PreOrder(r.getString(0).trim(), r.getString(1).trim(), r.getString(2).trim(),
						r.getString(3).trim(), //
						r.getInteger(4), r.getDouble(5), sdf.parse(r.getString(6)));
				po.setStatus(PreOrder.undeal);
				dao.save(po);
			}
			return true;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("deprecation")
	private static String getXCellVal(Cell cell) {
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

	/**
	 * 
	 * 将多个订单合并到一个柜台，主要功能是去掉运费？
	 * 
	 * @param orderNos
	 *            Order numbers
	 * @param counterCode
	 *            Counter code
	 */
	// @Override
	public boolean combineOrder2(List<String> orderNos, String counterCode, AuthUser opuser) {
		final CounterManager counterManager = AppContext.getBean(CounterManager.class);
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		final PricePolicyManager pricePolicyManager = AppContext.getBean(PricePolicyManager.class);
		final CustomerManager customerManager = AppContext.getBean(CustomerManager.class);
		final FixedPriceManager fixedPriceManager = AppContext.getBean(FixedPriceManager.class);

		// 用户类型价格运算的折扣率
		Counter counter = counterManager.getCounterByCode(counterCode);
		Customer customer = customerManager.get(counter.getCustomerId());
		CustomerPricePolicy pricePolicy = pricePolicyManager.getPolicy(customer.getCustomerTypeID());

		double discount = 0.0;
		double materialDiscount = 0.0;

		// 物料折扣
		if (pricePolicy != null) {
			discount = pricePolicy.getDiscount();
			materialDiscount = pricePolicy.getMaterialDiscount();
		}
		Order newOrder = new Order(opuser, opuser, counter);
		Map<Integer, Integer> qtyMap = new LinkedHashMap<>();
		Map<Integer, Double> unitPriceMap = new ConcurrentHashMap<>();

		double priceOrder = 0.0;
		double lastMaterialFree = 0.0;
		double lastMaterialFee = 0.0;
		Collection<Order> orders = orderdao.findByOrderNo(orderNos);
		Map<Integer, Boolean> bmap = new HashMap<Integer, Boolean>();
		Map<Integer, Integer> dtmap = new HashMap<Integer, Integer>();
		List<OrderDetail> ods = null;
		double oldpri = 0.0;
		// 遍历两层Order-detail->生成产品-总量Map
		for (Order order : orders) {
			priceOrder += order.getProductFee();
			if (order.getMaterialFreeAmt() != 0) {
				lastMaterialFree += order.getMaterialFreeAmt();
			}
			if (order.getMaterialFee() != 0) {
				lastMaterialFee += order.getMaterialFee();
			}

			// fix 礼盒订单
			if (order.getIssysin() == 0) {
				ods = orderdetaildao.find("orderNo = ? ", order.getOrderNo());
				oldpri = order.getProductFee();
				order.setOrderStatus(Order.status_NotDisplay);
				orderdao.update(order);
				continue;
			}
			List<OrderDetail> odetails = orderdetaildao.find("orderNo = ? ", order.getOrderNo());

			for (OrderDetail od : odetails) {
				Integer productId = od.getProductId();
				int qq = 0;
				if (!qtyMap.containsKey(productId)) {
					qq += od.getQuantity();
				} else {
					qq = qtyMap.get(productId);
					qq += od.getQuantity();
				}
				// FIXME同一次导入的商品价格不同如何处理
				unitPriceMap.put(productId, od.getMemberPrice());
				dtmap.put(productId, od.getDetailType());
				bmap.put(productId, od.getCostRatio());
				qtyMap.put(productId, qq);
			}
			order.setOrderStatus(Order.status_NotDisplay);
			orderdao.update(order);
		}

		String orderId;
		String orderNo;
		String orderCredentialsId;
		try {
			idGenService.lock(ID_ORDERID);
			idGenService.lock(ID_ORDERNO);
			idGenService.lock(ID_ORDERCREDENTIALID);
			idGenService.lock(ID_ORDERDETAILID);
			orderId = idGenService.generateDateId(ID_ORDERID);
			orderNo = idGenService.generateDateId(ID_ORDERNO);
			orderCredentialsId = idGenService.generateDateId(ID_ORDERCREDENTIALID);
			int lineNo = 1;
			// 遍历总量map生成新明细
			List<OrderDetail> listod = new ArrayList<>();
			Set<Integer> set = new HashSet<>();
			for (Map.Entry<Integer, Integer> prodId : qtyMap.entrySet()) {
				// 明细数量为0,退出此次循环
				if (prodId.getValue() == 0) {
					continue;
				}
				Product product = productManager.get(prodId.getKey());
				FixedPrice fixedPrice = fixedPriceManager.get(prodId.getKey());
				OrderDetail od = new OrderDetail(product, orderNo, prodId.getValue());
				od.setId(Long.parseLong(idGenService.generateDateId((ID_ORDERDETAILID))));
				od.setLineNumber(lineNo++);
				// Fixby wangs 2017-11-30 一口价处理
				// 有价格策略且不走客户折扣
				// add new Func(新增物料默认合并之后单价为0，通过系统导上层继承物料价格)

				String type = product.getTypeId();
				if (type.equals(Product.TYPEID_PRESENT) || type.equals(Product.TYPEID_METERIAL)) {
					// 置零通过差值不进行物料价格计算
					od.setUnitPrice(0.0);
					od.setMemberPrice(0.0);
				} else {
					od.setUnitPrice(fixedPrice != null && !fixedPrice.getPricePolicy() //
							? fixedPrice.getPrice() : discount * unitPriceMap.get(prodId.getKey()));
					od.setMemberPrice(unitPriceMap.get(prodId.getKey()));
				}
				// 套数订单详情
				od.setSuitNumber(qtyMap.get(product.getProductId()));
				od.setdetailType(dtmap.get(product.getProductId()));
				boolean costr = (fixedPrice != null ? fixedPrice.getCostRatio() : bmap.get(product.getProductId()));
				// 是否走价格策略
				od.setCostRatio(costr);

				od.setDeliveryWayId("01");
				od.setDeliveryWayName("销售");
				orderdetaildao.save(od);
				listod.add(od);
				set.add(od.getProductId());
			}
			if (ods != null) {
				for (OrderDetail od : ods) {
					od.setOrderNo(orderNo);
					od.setLineNumber(lineNo++);
					orderdetaildao.update(od);
				}
				listod.addAll(ods);
			}

			double totalPrice = 0.00;
			totalPrice = getRealPrice(listod, set, oldpri);
			// 获取总数量、正品数量、物料以及正品数量
			int totalNum = 0;
			int productQty = 0;
			int materialQty = 0;
			for (OrderDetail item : listod) {
				Integer productId = TypeConverter.toInteger(item.getProductId());
				if (productManager.isAuthenticProduct(productId)) {
					productQty += item.getQuantity();
				} else {
					materialQty += item.getQuantity();
				}
			}
			totalNum = productQty + materialQty;
			newOrder.setTotalNum(totalNum);
			// 物料赠品的总价
			double totalPresentFee = getPresentFee(listod);
			newOrder.setMaterialFreeAmt(lastMaterialFree);
			// materialFee<0说明没有超出物料配比 大于0的绝对值就是超出的物料的金额
			newOrder.setMaterialFee(lastMaterialFee < 0.001 ? 0 : lastMaterialFee);
			// 生成订单凭据
			// 订单的邮费
			double postage = getPostage(totalPrice);
			if (priceOrder != totalPrice) {
				logger.info("总价计算两种方式结果不一样");
			}

			// 生成新订单
			newOrder.setOrderNo(orderNo);
			newOrder.setExpressFee(postage);
			newOrder.setProductFee(totalPrice);
			newOrder.setId(Long.parseLong(orderId));
			newOrder.setOrderOriginalFee(getOrderOriginalFee(listod));
			newOrder.setPaymentFee(totalPrice + postage + lastMaterialFee);
			newOrder.setPayable(totalPrice + postage + lastMaterialFee);
			newOrder.setCredentialsNo(orderCredentialsId);
			newOrder.setOrderStatus(Order.Status_UnPay);
			newOrder.setDescription("系统礼盒导入合并订单：" + orderNos);
			// 通过礼盒导入功能,生成订单增加标识
			newOrder.setIssysin(2);
			orderdao.save(newOrder);

			OrderCredentials orderCredentials = new OrderCredentials(newOrder, counter);
			orderCredentials.setOcid(orderCredentialsId);
			orderCredentials.setProductQty(productQty);
			orderCredentials.setMaterialQty(materialQty);
			orderCredentials.setMaterialDiscount(materialDiscount);
			orderCredentials.setProductAmt(totalPrice);
			orderCredentials.setMaterialPay(totalPresentFee);
			orderCredentialdao.save(orderCredentials);
			return true;
		} catch (ServiceException e) {
			logger.error("", e);
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_ORDERID);
			idGenService.unlock(ID_ORDERNO);
			idGenService.unlock(ID_ORDERCREDENTIALID);
			idGenService.unlock(ID_ORDERDETAILID);
		}
		return false;
	}

	private double getRealPrice(List<OrderDetail> listod, Set<Integer> set, double oldpri) {
		double total = oldpri;
		for (OrderDetail od : listod) {
			if (set.contains(od.getProductId()))
				total += od.getUnitPrice() * od.getQuantity();
		}
		return total;
	}

//	private double getRealPrice(List<OrderDetail> listod) {
//		double total = 0.0;
//		for (OrderDetail od : listod) {
//			total += od.getUnitPrice() * od.getQuantity();
//		}
//		return total;
//	}

	private double getPresentFee(List<OrderDetail> listod) {
		double feePrice = 0;
		FixedPriceManager fixedPriceManager = AppContext.getBean(FixedPriceManager.class);
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		// 先找到购物车明细的所有的物料以及赠品
		for (OrderDetail item : listod) {
			Integer productid = TypeConverter.toInteger(item.getProductId());
			FixedPrice fixedPrice = fixedPriceManager.get(productid);
			// 正品和不占费比的物料
			if (productManager.isAuthenticProduct(TypeConverter.toInteger(productid)) //
					|| (fixedPrice != null && !fixedPrice.getCostRatio())) {
				continue;
			}
			Double price = getPrice(item, productManager);
			// 描述表里面所有物料且走费比的
			feePrice += item.getQuantity() * price;
		}
		return feePrice;
	}

	private int getPostage(double totalPrice) {
		// 总金额大于最大限额，包邮
		DictManager dictManager = AppContext.getBean(DictManager.class);

		Dictionary dict_maxAmount = dictManager.lookUpByCode(ORDMCONFIG, KEY_MAXAMOUNT);
		Dictionary dict_minAmount = dictManager.lookUpByCode(ORDMCONFIG, KEY_MINIAMOUNT);
		Dictionary dict_maxPostage = dictManager.lookUpByCode(ORDMCONFIG, KEY_MAXPOSTAGE);
		Dictionary dict_midPostage = dictManager.lookUpByCode(ORDMCONFIG, KEY_MIDPOSTAGE);
		Dictionary dict_noPostage = dictManager.lookUpByCode(ORDMCONFIG, KEY_NOPOSTAGE);

		double maxAmt = StringUtil.toDouble(dict_maxAmount.getValue());
		double minAmt = StringUtil.toDouble(dict_minAmount.getValue());
		int noPostage = StringUtil.toInt(dict_noPostage.getValue(), 0);
		int maxPostage = StringUtil.toInt(dict_maxPostage.getValue(), Order.MaxPostage);
		int midPostage = StringUtil.toInt(dict_midPostage.getValue(), Order.MidPostage);
		if (totalPrice >= maxAmt) {
			return noPostage;
		}
		// 订单低于最小金额 走最大邮费
		else if (totalPrice < minAmt) {
			return maxPostage;
		} else
			return midPostage;
	}

	/**
	 * 正常定单与礼盒定单合并定单操作
	 * 
	 * @param orderNos
	 *            待合并的订单
	 * @param counterCode
	 * @param opuser
	 *            操作员
	 * @return
	 */
	@Override
	public void combineOrder(List<String> orderNos, String counterCode, AuthUser opuser) {
		CounterManager counterManager = AppContext.getBean(CounterManager.class);
		final Counter counter = counterManager.getCounterByCode(counterCode);
		// 礼盒
		final List<Order> orderBox = new LinkedList<>();
		// 正常訂單
		final List<Order> normalBox = new LinkedList<>();
		// 正常订单
		Order normal;
		double originalFee = 0.0;
		double materialFee = 0.0;
		double productFee = 0.0;
		double paymentFee;
		double materialFreeAmt = 0.0;
		double useBalance = 0.0;
		int totalNum = 0;

		for (String orderNo : orderNos) {
			Order order = orderService.getsingleOrder(orderNo);

			useBalance += order.getUseBalance();
			originalFee += order.getOrderOriginalFee();
			materialFee += order.getMaterialFee();
			productFee += order.getProductFee();
			materialFreeAmt += order.getMaterialFreeAmt();
			totalNum += order.getTotalNum();

			// FIXME: 1 导入 2 合并后订单
			if (order.getIssysin() == Order.PRESENT_TYPE_NORMAL //
					|| order.getIssysin() == Order.PRESENT_TYPE_COMBINE) {
				normalBox.add(order);
			} else {
				orderBox.add(order);
			}
		}

		switch (normalBox.size()) {
		case 1:
			normal = normalBox.get(0);
			break;
		case 0:
			normal = orderBox.get(0);
			orderBox.remove(0);
			break;
		default:
			throw new ServiceException("正常訂單必須小於2！");
		}

		List<OrderDetail> orderDetails = orderService.getOrderDetails(normal.getOrderNo());
		int lineNumber = orderDetails.size();
		// 主键生成器 锁
		try {
			idGenService.lock(ID_ORDERDETAILID);
			idGenService.lock(ID_CARTDETAILID);
			for (Order order : orderBox) {
				for (OrderDetail orderDetail : orderService.getOrderDetails(order.getOrderNo())) {
					OrderDetail newDail = BeanClone.dup(orderDetail, new OrderDetail());

					String id = idGenService.generateDateId(ID_ORDERDETAILID);
					String pid = idGenService.generateDateId(ID_CARTDETAILID);
					newDail.setId(Long.parseLong(id));
					newDail.setOrderNo(normal.getOrderNo());
					newDail.setLineNumber(lineNumber++);
					newDail.setPid(Long.parseLong(pid));

					orderDetailService.save(newDail);
				}
			}
			// 订单
			normal.setUseBalance(useBalance);
			normal.setOrderOriginalFee(originalFee);
			normal.setProductFee(productFee);
			normal.setMaterialFee(materialFee);
			normal.setMaterialFreeAmt(materialFreeAmt);
			normal.setTotalNum(totalNum);
			// 计算邮费
			double expressFee = getPostage(productFee);
			if (OrdmConfig.COUNTER_TYPE_DIRECT.equals(counter.getType())) {
				expressFee = 0.0;
			}
			paymentFee = productFee + materialFee + expressFee;
			normal.setPaymentFee(paymentFee);

			double payable = paymentFee - useBalance;
			normal.setPayable(payable);
			if (!normal.getCounterCode().equals(counterCode) //
					&& normal.getIssysin() != Order.PRESENT_TYPE_NORMAL) {
				normal.setCounterInfo(counter);
			}

			normal.setExpressFee(expressFee);
			normal.setIssysin(Order.PRESENT_TYPE_COMBINE);

			orderService.update(normal);
			// 憑證更新
			OrderCredentials orderCredentials = orderCredentialsService.getOrderCredentials(normal.getOrderNo());
			orderCredentials.setOrderInfo(normal, counter);
			orderCredentialdao.update(orderCredentials);

			for (Order o : orderBox) {
				o.setOrderStatus(Order.status_NotDisplay);
				orderService.update(o);
			}

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_ORDERDETAILID);
			idGenService.unlock(ID_CARTDETAILID);
		}
	}

	private Double getPrice(OrderDetail item, ProductManager productManager) {
		Double price = item.getUnitPrice();
		Integer productId = TypeConverter.toInteger(item.getProductId());
		Product product = productManager.get(productId);
		if (price == null) {
			price = productManager.isAuthenticProduct(productId) ? product.getMemberPrice()
					: product.getMaterialPrice();
		}
		return price;
	}

	/**
	 * 正品原价
	 * 
	 * @param detail
	 * @return
	 */
	private double getOrderOriginalFee(Collection<OrderDetail> detail) {
		ProductManager productManager = AppContext.getBean(ProductManager.class);
		double realPrice = 0;
		for (OrderDetail item : detail) {
			Integer productId = TypeConverter.toInteger(item.getProductId());
			if (productManager.isAuthenticProduct(productId)) {
				Product p = productManager.get(productId);
				realPrice += p.getMemberPrice() * item.getQuantity();
			}
		}
		return realPrice;
	}
}