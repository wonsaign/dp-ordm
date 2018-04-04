package com.zeusas.dp.ordm.service.impl;

import static com.zeusas.dp.ordm.service.CartDetailService.ID_CARTDETAIL;
import static com.zeusas.dp.ordm.utils.OrdmConfig.DELIVERYWAY;
import static com.zeusas.dp.ordm.utils.OrdmConfig.DELIVERYWAY_MARKETPRESENT;
import static com.zeusas.dp.ordm.utils.OrdmConfig.DELIVERYWAY_SALES;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MAXAMOUNT;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MAXPOSTAGE;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MIDPOSTAGE;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MINIAMOUNT;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_NOPOSTAGE;
import static com.zeusas.dp.ordm.utils.OrdmConfig.ORDMCONFIG;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.service.IdGenService;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.dao.OrderDao;
import com.zeusas.dp.ordm.dao.OrderDetailDao;
import com.zeusas.dp.ordm.dao.PreOrderDao;
import com.zeusas.dp.ordm.dao.ReserveRecordDao;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.CustomerPricePolicy;
import com.zeusas.dp.ordm.entity.DeliveryWay;
import com.zeusas.dp.ordm.entity.FixedPrice;
import com.zeusas.dp.ordm.entity.Item;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderCredentials;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.entity.PreOrder;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ReserveRecord;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.FixedPriceManager;
import com.zeusas.dp.ordm.service.OrderCredentialsService;
import com.zeusas.dp.ordm.service.PricePolicyManager;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.SystemOrderService;
import com.zeusas.dp.ordm.task.StorehousesSyncTask;
import com.zeusas.dp.ordm.utils.OrdmConfig;
import com.zeusas.security.auth.entity.AuthUser;

@Service
@Transactional
public class SystemOrderServiceImpl extends BasicService<Order, Long> implements SystemOrderService {

	private static Logger logger = LoggerFactory.getLogger(SystemOrderServiceImpl.class);

	public final static String ID_ORDERID = "ORDERID";
	public final static String ID_ORDERNO = "ORDERNO";
	public final static String ID_ORDERCREDENTIALID = "ORDERCREDENTIAL";
	public final static String ID_ORDERDETAILID = "ORDERDETAILID";
	public final static String ID_PACKAGE = "PACKAGEID";
	public final static String ID_PACKAGEDETAIL = "PACKAGEDETAIL";
	public final static String TYPEID_PRODUCT = Product.TYPEID_PRODUCT;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private PreOrderDao preOrderDao;
	@Autowired
	private OrderDetailDao orderDetailDao;
	@Autowired
	private IdGenService idGenService;
	@Autowired
	private OrderCredentialsService credentialsService;
	@Autowired
	private ReserveRecordDao reserveRecordDao;

	@Override
	@Transactional
	public Order buildOrder(Counter counter, AuthUser makeUser, Integer customerTypeId, Collection<Item> detail)
			throws ServiceException {
		// 生成订单时关联到客户策略
		final PricePolicyManager pricePolicyManager = AppContext.getBean(PricePolicyManager.class);
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		// 用户类型价格运算的折扣率
		CustomerPricePolicy pricePolicy = pricePolicyManager.getPolicy(customerTypeId);

		double discount = pricePolicy.getDiscount();
		// 物料折扣
		double materialDiscount = pricePolicy.getMaterialDiscount();

		Order order = new Order(makeUser, makeUser, counter);
		String orderId;
		String orderNo;
		String orderCredentialsId;
		// isNewOrder: 判断这是一个退回的订单还是全新生成的一个订单
		try {
			idGenService.lock(ID_ORDERID);
			idGenService.lock(ID_ORDERNO);
			idGenService.lock(ID_ORDERCREDENTIALID);
			orderId = idGenService.generateDateId(ID_ORDERID);
			orderNo = idGenService.generateDateId(ID_ORDERNO);
			orderCredentialsId = idGenService.generateDateId(ID_ORDERCREDENTIALID);
			order.setId(Long.parseLong(orderId));
			order.setCredentialsNo(orderCredentialsId);
			order.setOrderNo(orderNo);

			double totalPrice = 0.00;
			totalPrice = getRealPrice(detail, discount);

			// 获取总数量、正品数量、物料以及正品数量
			int totalNum = 0;
			int productQty = 0;
			int materialQty = 0;
			for (Item item : detail) {
				Integer productId = TypeConverter.toInteger(item.getId());
				if (productManager.isAuthenticProduct(productId)) {
					productQty += item.getNum();
				} else {
					materialQty += item.getNum();
				}
			}
			totalNum = productQty + materialQty;
			order.setTotalNum(totalNum);

			// 物料赠品的总价
			double totalPresentFee = getPresentFee(detail);
			// 需要收费的物料的价格
			double materialFee = getMaterialFee(detail, materialDiscount);
			// materialFee<0说明没有超出物料配比 大于0的绝对值就是超出的物料的金额
			order.setMaterialFee(materialFee < 0.001 ? 0 : materialFee);
			// 订单免费配送的物料的金额
			double materialFreeAmt = getMaterialFreeFee(detail, materialDiscount);
			order.setMaterialFreeAmt(materialFreeAmt);
			// 正品费用
			order.setProductFee(totalPrice);
			totalPrice += materialFee < 0.001 ? 0 : materialFee;
			// 获取订单的邮费
			// double expressFee = getPostage(totalPrice);
			// if (OrdmConfig.COUNTER_TYPE_DIRECT.equals(counter.getType())) {
			// expressFee = 0.0;
			// }
			// totalPrice += expressFee;
			order.setExpressFee(0.0);
			order.setPaymentFee(totalPrice);
			// 应付
			order.setPayable(totalPrice);
			// 订单折扣前的实际价格 正品会员价？？？
			order.setOrderOriginalFee(getOrderOriginalFee(detail));
			// 生成订单凭据
			OrderCredentials orderCredentials = new OrderCredentials(order, counter);
			orderCredentials.setOcid(orderCredentialsId);
			orderCredentials.setProductQty(productQty);
			orderCredentials.setMaterialQty(materialQty);
			orderCredentials.setMaterialDiscount(materialFreeAmt);
			orderCredentials.setProductAmt(getRealPrice(detail, discount));
			orderCredentials.setMaterialPay(totalPresentFee);
			// 生成订单明细 订单生成后清除购物车
			StorehousesSyncTask synctask = AppContext.getBean(StorehousesSyncTask.class);
			synctask.setUpdated();
			List<OrderDetail> orderDetails = buildOrderDetails(orderNo, discount, detail);
			for (OrderDetail orderDetail : orderDetails) {
				if(orderDetail.getDetailType()==null){
					orderDetail.setdetailType(OrderDetail.TYPE_BUY);
				}
				orderDetailDao.save(orderDetail);
			}

			// order.setOrderStatus(Order.Status_DoPay);
			order.setOrderStatus(Order.status_LogisticsDelivery);
			order.setPayManId(makeUser.getLoginName());
			order.setPayManName(makeUser.getCommonName());
			order.setPayTypeId(Order.PayType_system);
			order.setOrderPayTime(System.currentTimeMillis());
			order.setDescription("物料分发系统制单");
			orderDao.save(order);
			credentialsService.save(orderCredentials);
			return order;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_ORDERID);
			idGenService.unlock(ID_ORDERNO);
			idGenService.unlock(ID_ORDERCREDENTIALID);
		}
	}

	@Override
	@Transactional
	public Order buildOrderForReserve(Counter counter, AuthUser makeUser, Integer customerTypeId,
			Collection<ReserveRecord> records) throws ServiceException {

		// 生成订单时关联到客户策略
		final PricePolicyManager pricePolicyManager = AppContext.getBean(PricePolicyManager.class);
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		// 用户类型价格运算的折扣率
		CustomerPricePolicy pricePolicy = pricePolicyManager.getPolicy(customerTypeId);

		double discount = pricePolicy.getDiscount();
		// 物料折扣
		double materialDiscount = pricePolicy.getMaterialDiscount();

		Order order = new Order(makeUser, makeUser, counter);
		String orderId;
		String orderNo;
		String orderCredentialsId;

		List<Item> detail = new ArrayList<>(records.size());
		for (ReserveRecord reserveRecord : records) {
			// 还欠单子收费 (打欠单子 欠货产品没传到金蝶 还欠单子该多少钱就是多少钱)
			Item item = new Item(reserveRecord.getProductId().toString(), //
					reserveRecord.getQuantity(), //
					reserveRecord.getUnitPrice(),
					OrdmConfig.DELIVERYWAY_SALES);
			item.setRevId(reserveRecord.getRevId());
			item.setDetalType(OrderDetail.TYPE_DONE);
			detail.add(item);
		}

		// isNewOrder: 判断这是一个退回的订单还是全新生成的一个订单
		try {
			idGenService.lock(ID_ORDERID);
			idGenService.lock(ID_ORDERNO);
			idGenService.lock(ID_ORDERCREDENTIALID);
			orderId = idGenService.generateDateId(ID_ORDERID);
			orderNo = idGenService.generateDateId(ID_ORDERNO);
			orderCredentialsId = idGenService.generateDateId(ID_ORDERCREDENTIALID);
			order.setId(Long.parseLong(orderId));
			order.setCredentialsNo(orderCredentialsId);
			order.setOrderNo(orderNo);

			double totalPrice = 0.00;
			totalPrice = getRealPrice(detail, discount);

			// 获取总数量、正品数量、物料以及正品数量
			int totalNum = 0;
			int productQty = 0;
			int materialQty = 0;
			for (Item item : detail) {
				Integer productId = TypeConverter.toInteger(item.getId());
				if (productManager.isAuthenticProduct(productId)) {
					productQty += item.getNum();
				} else {
					materialQty += item.getNum();
				}
			}
			totalNum = productQty + materialQty;
			order.setTotalNum(totalNum);

			// 物料赠品的总价
			double totalPresentFee = getPresentFee(detail);
			// 需要收费的物料的价格
			double materialFee = getMaterialFee(detail, materialDiscount);
			// materialFee<0说明没有超出物料配比 大于0的绝对值就是超出的物料的金额
			order.setMaterialFee(materialFee < 0.001 ? 0 : materialFee);
			// 订单免费配送的物料的金额
			double materialFreeAmt = getMaterialFreeFee(detail, materialDiscount);
			order.setMaterialFreeAmt(materialFreeAmt);
			// 正品费用
			order.setProductFee(totalPrice);
			totalPrice += materialFee < 0.001 ? 0 : materialFee;
			// 订单的邮费
			order.setExpressFee(0.0);
			order.setPaymentFee(totalPrice);
			// 应付
			order.setPayable(totalPrice);
			// 订单折扣前的实际价格 正品会员价？？？
			order.setOrderOriginalFee(getOrderOriginalFee(detail));
			// 生成订单凭据
			OrderCredentials orderCredentials = new OrderCredentials(order, counter);
			orderCredentials.setOcid(orderCredentialsId);
			orderCredentials.setProductQty(productQty);
			orderCredentials.setMaterialQty(materialQty);
			orderCredentials.setMaterialDiscount(materialFreeAmt);
			orderCredentials.setProductAmt(getRealPrice(detail, discount));
			orderCredentials.setMaterialPay(totalPresentFee);
			// 生成订单明细 订单生成后清除购物车
			StorehousesSyncTask synctask = AppContext.getBean(StorehousesSyncTask.class);
			synctask.setUpdated();

			List<OrderDetail> orderDetails = buildOrderDetails(orderNo, discount, detail);
			double rOweAmt = order.getrOweAmt();
			double yOweAmt = order.getrOweAmt();
			for (OrderDetail orderDetail : orderDetails) {
				// 预订会
				double amt = orderDetail.getUnitPrice() * (orderDetail.getQuantity() - orderDetail.getFreeQty());
				if (orderDetail.getRevId() != null) {
					rOweAmt += amt;
				} else {
					yOweAmt += amt;
				}
				orderDetailDao.save(orderDetail);
			}

			// order.setOrderStatus(Order.Status_DoPay);
			order.setOrderStatus(Order.status_LogisticsDelivery);
			order.setPayManId(makeUser.getLoginName());
			order.setPayManName(makeUser.getCommonName());
			order.setPayTypeId(Order.PayType_system);
			order.setOrderPayTime(System.currentTimeMillis());
			order.setDescription("预订还欠系统制单");
			order.setrOweAmt(rOweAmt);
			order.setyOweAmt(yOweAmt);
			orderDao.save(order);
			credentialsService.save(orderCredentials);
			for (ReserveRecord reserveRecord : records) {
				reserveRecord.setStatus(ReserveRecord.STATUS_DONE);
				reserveRecord.setExcuteOrderNo(orderNo);
				reserveRecordDao.update(reserveRecord);
			}
			return order;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_ORDERID);
			idGenService.unlock(ID_ORDERNO);
			idGenService.unlock(ID_ORDERCREDENTIALID);
		}
	}

	/**
	 * 系统制单 获取明细里面实际的正品支付金额
	 * 
	 * @param detail
	 * @param discount
	 * @return
	 */
	private double getRealPrice(Collection<Item> detail, double discount) {
		FixedPriceManager fixedPriceManager = AppContext.getBean(FixedPriceManager.class);
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		double realPrice = 0.00;
		for (Item item : detail) {
			if (!productManager.isAuthenticProduct(TypeConverter.toInteger(item.getId()))) {
				continue;
			}
			Double price = getPrice(item, productManager);
			Integer productid = TypeConverter.toInteger(item.getId());
			FixedPrice fixedPrice = fixedPriceManager.get(productid);

			if (fixedPrice != null && !fixedPrice.getPricePolicy()) {
				realPrice += item.getNum() * price;
			} else {
				realPrice += discount * item.getNum() * price;
			}
		}
		return realPrice;
	}

	/**
	 * 明细里要占费比的物料赠品总价(折扣前)
	 * 
	 * @param cartDetails
	 * @return
	 * @throws ServiceException
	 */
	public double getPresentFee(Collection<Item> detail) throws ServiceException {
		double feePrice = 0;
		FixedPriceManager fixedPriceManager = AppContext.getBean(FixedPriceManager.class);
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		// 先找到购物车明细的所有的物料以及赠品
		for (Item item : detail) {
			Integer productid = TypeConverter.toInteger(item.getId());
			FixedPrice fixedPrice = fixedPriceManager.get(productid);
			// 正品和不占费比的物料
			if (productManager.isAuthenticProduct(TypeConverter.toInteger(item.getId())) //
					|| (fixedPrice != null && !fixedPrice.getCostRatio())) {
				continue;
			}
			Double price = getPrice(item, productManager);
			// 描述表里面所有物料且走费比的
			feePrice += item.getNum() * price;
		}
		return feePrice;
	}

	public Double getMaterialFee(Collection<Item> detail, double discount) throws ServiceException {
		final DictManager dictManager = AppContext.getBean(DictManager.class);
		final FixedPriceManager fixedPriceManager = AppContext.getBean(FixedPriceManager.class);
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		// 物料配比
		Dictionary dict_mdiscount = dictManager.lookUpByCode(OrdmConfig.ORDMCONFIG, OrdmConfig.MATERIALDISCOUNT);

		Double materialdiscount = StringUtil.toDouble(dict_mdiscount.getValue());
		if (materialdiscount == null) {
			throw new ServiceException("物料配比字典定义错误！");
		}

		double realPrice = getSupportMaterialFree(detail, discount) * materialdiscount;
		// 实际应配物料
		double totalMaterialFee = realPrice;
		// 实际已经选的物料总金额(包括不占费比的物料)
		double totalMaterialAmt = 0;

		Map<String, Item> detailsMap = new HashMap<>();
		for (Item item : detail) {
			detailsMap.put(item.getId(), item);
		}

		// 要占用费比的赠品、物料
		List<Item> costRatioMaterials = new ArrayList<>();

		// Step 1, 取出物料，计算并排序
		for (Item item : detail) {
			Integer productid = TypeConverter.toInteger(item.getId());
			FixedPrice fixedPrice = fixedPriceManager.get(productid);
			// 正品或者不占配比的物料
			if (productManager.isAuthenticProduct(TypeConverter.toInteger(item.getId()))) {
				continue;
			}
			Double price = getPrice(item, productManager);
			totalMaterialAmt += item.getNum() * price;
			// 不占配比物料
			if (fixedPrice != null && !fixedPrice.getCostRatio()) {
				continue;
			}
			costRatioMaterials.add(item);
		}

		Collections.sort(costRatioMaterials, (a, b) -> {
			double v = b.getNum() * getPrice(b, productManager) - a.getNum() * getPrice(a, productManager);
			if (v > 0) {
				return 1;
			}
			return v > -0.001 ? 0 : -1;
		});

		// 1st scan materials
		Set<String> removed = new LinkedHashSet<>();
		List<Item> remains = new ArrayList<>();
		for (Item item : costRatioMaterials) {
			Double price = getPrice(item, productManager);
			double fee = item.getNum() * price;
			if (realPrice - fee >= 0) {
				realPrice -= fee;
				removed.add(item.getId());
			} else {
				remains.add(item);
			}
		}

		// 2nd scan: realPrice 已经扣减了物料，并记录下ID(removed)
		int v_realPrice = (int) (realPrice * 100);
		for (Item item : remains) {
			Double price = getPrice(item, productManager);
			int v_price = (int) (price * 100);
			int num = v_realPrice / v_price;
			// 修正： 试算是否进行扣减
			if (num > 0 && (v_realPrice - v_price * num >= 0)) {
				// 修正：同步扣减
				v_realPrice -= v_price * num;
				realPrice -= num * price;
			}
		}
		// Map<String, Object> map_materialFee = new HashMap<>();
		// 收费金额 =已选物料总价-(可配物料金额-剩余金额)
		double materialFee = totalMaterialAmt - totalMaterialFee + realPrice;
		return materialFee;
	}

	/***
	 * 获取配比金额
	 * 
	 * @param cartDetails
	 * @param discount
	 * @return
	 */
	public double getSupportMaterialFree(Collection<Item> detail, double discount) {
		double realPrice = 0.00;
		FixedPriceManager fixedPriceManager = AppContext.getBean(FixedPriceManager.class);
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		// 支持配比的正品
		for (Item item : detail) {
			Integer productid = TypeConverter.toInteger(item.getId());
			FixedPrice fixedPrice = fixedPriceManager.get(productid);
			// 物料或者不走配比的大货
			if (!productManager.isAuthenticProduct(TypeConverter.toInteger(item.getId()))//
					|| (fixedPrice != null && !fixedPrice.getCostRatio())) {
				continue;
			}
			Double price = getPrice(item, productManager);
			double amt = item.getNum() * price;
			// 不打折
			if (fixedPrice != null && !fixedPrice.getPricePolicy()) {
				realPrice += amt;
			} else {
				realPrice += discount * amt;
			}
		}
		return realPrice;
	}

	/**
	 * 获取免费物料的费用
	 * 
	 * @param cartDetails
	 * @param discount
	 * @return
	 */
	public double getMaterialFreeFee(Collection<Item> detail, double discount) {
		DictManager dictManager = AppContext.getBean(DictManager.class);
		Dictionary dict_materialdiscount = dictManager.lookUpByCode(OrdmConfig.ORDMCONFIG, OrdmConfig.MATERIALDISCOUNT);
		double materialdiscount = Double.parseDouble(dict_materialdiscount.getValue());
		return getSupportMaterialFree(detail, discount) * materialdiscount;
	}

	/**
	 * 正品原价
	 * 
	 * @param detail
	 * @return
	 */
	private double getOrderOriginalFee(Collection<Item> detail) {
		ProductManager productManager = AppContext.getBean(ProductManager.class);
		double realPrice = 0;
		for (Item item : detail) {
			Integer productId = TypeConverter.toInteger(item.getId());
			if (productManager.isAuthenticProduct(productId)) {
				Product p = productManager.get(productId);
				realPrice += p.getMemberPrice() * item.getNum();
			}
		}
		return realPrice;
	}

	private List<OrderDetail> buildOrderDetails(String orderNo, double discount, //
			Collection<Item> details) throws ServiceException {
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		final FixedPriceManager fixedPriceManager = AppContext.getBean(FixedPriceManager.class);
		final DictManager dictManager = AppContext.getBean(DictManager.class);
		int lineNo = 1;
		// FIXNE:默认发货方式是哪一种 是否会有多种
		Dictionary dictionary = dictManager.lookUpByCode(DELIVERYWAY, DELIVERYWAY_MARKETPRESENT);
		Long detailsId;
		List<OrderDetail> orderDetails = new ArrayList<>(details.size());
		try {
			idGenService.lock(ID_CARTDETAIL);
			idGenService.lock(ID_ORDERDETAILID);
			detailsId = Long.parseLong(idGenService.generateDateId(ID_CARTDETAIL));
			for (Item item : details) {
				Double price = getPrice(item, productManager);
				Integer productId = TypeConverter.toInteger(item.getId());
				Product product = productManager.get(productId);
				OrderDetail orderDetail = new OrderDetail(product, orderNo, item.getNum());
				FixedPrice fixedPrice = fixedPriceManager.get(productId);
				// FIXNE:发货方式是哪一种 是否会有多种
				String deliveryWayId = item.getDeliveryWayId();
				if (!Strings.isNullOrEmpty(item.getDeliveryWayId())) {
					dictionary = dictManager.lookUpByCode(DELIVERYWAY, deliveryWayId);
				}
				DeliveryWay deliveryWay = new DeliveryWay(dictionary);
				orderDetail.setId(Long.parseLong(idGenService.generateDateId((ID_ORDERDETAILID))));
				orderDetail.setLineNumber(lineNo++);
				orderDetail.setMemberPrice(price);
				// 有价格策略且不走客户折扣
				if (fixedPrice != null && !fixedPrice.getPricePolicy()) {
					orderDetail.setUnitPrice(fixedPrice.getFix() ? fixedPrice.getPrice() //
							: price * fixedPrice.getDiscount());
				} else if (productManager.isAuthenticProduct(productId)) {
					// 订单明细的产品价格=购物车描述的里面的价格 (单价)
					orderDetail.setUnitPrice(price * discount);
				} else {
					// 取购物车明细描述的价格
					orderDetail.setUnitPrice(price);
				}
				if (productManager.isAuthenticProduct(productId)) {
					//免费的大货
					if(price<0.0001){
						orderDetail.setGiftPrice(price * discount);
					}else{
						orderDetail.setGiftPrice(price);
					}
				} else {
					orderDetail.setGiftPrice(product.getMaterialPrice());
				}

				if (fixedPrice != null) {
					orderDetail.setCostRatio(fixedPrice.getCostRatio());
				} else {
					orderDetail.setCostRatio(true);
				}
				orderDetail.setPid(detailsId);
				orderDetail.setSuitNumber(1);
				orderDetail.setDeliveryWayId(deliveryWay.getDeliveryWayId());
				orderDetail.setDeliveryWayName(deliveryWay.getDeliveryName());
				orderDetail.setRevId(item.getRevId());
				if(item.getDetalType()==null){
					orderDetail.setdetailType(OrderDetail.TYPE_BUY);
				}
				orderDetails.add(orderDetail);
			}
		} catch (ServiceException e) {
			logger.error("业务异常", e);
		} catch (Exception e) {
			logger.error("数据异常", e);
		} finally {
			idGenService.unlock(ID_ORDERDETAILID);
			idGenService.unlock(ID_CARTDETAIL);
		}

		return orderDetails;
	}

	private Double getPrice(Item item, ProductManager productManager) {
		Double price = item.getAmt();
		Integer productId = TypeConverter.toInteger(item.getId());
		Product product = productManager.get(productId);
		if (price == null) {
			price = productManager.isAuthenticProduct(productId) ? product.getMemberPrice()
					: product.getMaterialPrice();
		}
		return price;
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

	@Override
	public void transPreOrderToOrder() {
		List<PreOrder> remain = preOrderDao.find("where status = ?", PreOrder.undeal);
		Map<String, List<PreOrder>> undealMap = remain.stream().filter(po -> po.getQty() != 0)
				.collect(Collectors.groupingBy(PreOrder::getCounterCode));
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		final CounterManager counterManager = AppContext.getBean(CounterManager.class);
		final CustomerManager customerManager = AppContext.getBean(CustomerManager.class);
		final PricePolicyManager pricePolicyManager = AppContext.getBean(PricePolicyManager.class);
		AuthUser sysuser = new AuthUser();
		sysuser.setLoginName("root");
		sysuser.setCommonName("直发超级管理员");
		try {
			idGenService.lock(ID_ORDERID);
			idGenService.lock(ID_ORDERNO);
			idGenService.lock(ID_ORDERCREDENTIALID);
			for (Entry<String, List<PreOrder>> entry : undealMap.entrySet()) {
				List<PreOrder> valuelist = entry.getValue();
				if (StringUtil.isEmpty(entry.getKey())) {
					continue;
				}
				Counter counter = counterManager.getCounterByCode(entry.getKey());
				if (counter == null) {
					logger.error("柜台号上传错误", entry.getKey());
					continue;
				}
				Predicate<PreOrder> predicate = (po) -> {
					return isInteger(po.getProductId());
				};
				// 去掉不合格产品
				valuelist = valuelist.stream().filter(predicate).collect(Collectors.toList());
				Integer customerId = counter.getCustomerId();
				Customer customer = customerManager.get(customerId);
				// 用户类型价格运算的折扣率
				CustomerPricePolicy pricePolicy = pricePolicyManager.getPolicy(customer.getCustomerTypeID());
				double discount = pricePolicy.getDiscount();
				// 物料折扣
				double materialDiscount = pricePolicy.getMaterialDiscount();
				Order order = new Order(sysuser, sysuser, counter);
				String orderId;
				String orderNo;
				String orderCredentialsId;
				// isNewOrder: 判断这是一个退回的订单还是全新生成的一个订单

				orderId = idGenService.generateDateId(ID_ORDERID);
				orderNo = idGenService.generateDateId(ID_ORDERNO);
				orderCredentialsId = idGenService.generateDateId(ID_ORDERCREDENTIALID);
				order.setId(Long.parseLong(orderId));
				order.setCredentialsNo(orderCredentialsId);
				order.setOrderNo(orderNo);
				double totalPrice = 0.00;
				totalPrice = getRealPrice(valuelist, discount);
				// 获取总数量、正品数量、物料以及正品数量
				int totalNum = 0;
				int productQty = 0;
				int materialQty = 0;
				for (PreOrder po : valuelist) {
					String pId = po.getProductId();
					Integer productId = TypeConverter.toInteger(pId);
					if (productManager.isAuthenticProduct(productId)) {
						productQty += po.getQty();
					} else {
						// 非产品操作，暂不考虑
						// materialQty += item.getNum();
					}
				}
				totalNum = productQty + materialQty;
				order.setTotalNum(totalNum);

				// 物料赠品的总价
				double totalPresentFee = getPresentFee(valuelist);
				// 需要收费的物料的价格
				double materialFee = getMaterialFee(valuelist, materialDiscount);
				// materialFee<0说明没有超出物料配比 大于0的绝对值就是超出的物料的金额
				order.setMaterialFee(materialFee < 0.001 ? 0 : materialFee);
				// 订单免费配送的物料的金额
				double materialFreeAmt = getMaterialFreeFee(valuelist, materialDiscount);
				order.setMaterialFreeAmt(materialFreeAmt);
				// 正品费用
				order.setProductFee(totalPrice);
				totalPrice += materialFee < 0.001 ? 0 : materialFee;
				// 订单的邮费
				double postage = getPostage(totalPrice);
				order.setExpressFee(postage);
				// 要使用明细里的价格进行计算
				order.setPaymentFee(postage + totalPrice);
				// 应付
				order.setPayable(postage + totalPrice);
				// 订单折扣前的实际价格 正品会员价？？？
				order.setOrderOriginalFee(getOrderOriginalFee(valuelist));
				// 生成订单凭据
				OrderCredentials orderCredentials = new OrderCredentials(order, counter);
				orderCredentials.setOcid(orderCredentialsId);
				orderCredentials.setProductQty(productQty);
				orderCredentials.setMaterialQty(materialQty);
				orderCredentials.setMaterialDiscount(materialFreeAmt);
				orderCredentials.setProductAmt(getRealPrice(valuelist, discount));
				orderCredentials.setMaterialPay(totalPresentFee);

				// 生成订单明细 订单生成后清除购物车
				StorehousesSyncTask synctask = AppContext.getBean(StorehousesSyncTask.class);
				synctask.setUpdated();
				// 订单明细
				List<OrderDetail> orderDetails = buildOrderDetails(orderNo, discount, valuelist);
				
				for (OrderDetail orderDetail : orderDetails) {
					orderDetail.setdetailType(OrderDetail.TYPE_BUY);
					orderDetailDao.save(orderDetail);
				}

				order.setOrderStatus(Order.Status_UnPay);
				order.setPayManId(sysuser.getLoginName());
				order.setPayManName(sysuser.getCommonName());
				order.setOrderPayTime(System.currentTimeMillis());
				order.setDescription("礼盒导入功能待付款订单");
				// 通过礼盒导入功能,生成订单增加标识
				order.setIssysin(1);
				orderDao.save(order);
				credentialsService.save(orderCredentials);
				changedeal(valuelist);

			}
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			idGenService.unlock(ID_ORDERID);
			idGenService.unlock(ID_ORDERNO);
			idGenService.unlock(ID_ORDERCREDENTIALID);
		}

	}

	private boolean isInteger(String productId) {
		try {
			Integer.parseInt(productId);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	private void changedeal(List<PreOrder> valuelist) {
		for (PreOrder po : valuelist) {
			po.setStatus(PreOrder.dealed);
			preOrderDao.update(po);
		}

	}

	private double getPresentFee(List<PreOrder> valuelist) {
		double feePrice = 0;
		FixedPriceManager fixedPriceManager = AppContext.getBean(FixedPriceManager.class);
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		// 先找到购物车明细的所有的物料以及赠品
		try {
			for (PreOrder po : valuelist) {
				Integer productid = TypeConverter.toInteger(po.getProductId());
				FixedPrice fixedPrice = fixedPriceManager.get(productid);
				// 正品和不占费比的物料
				if (productManager.isAuthenticProduct(TypeConverter.toInteger(productid)) //
						|| (fixedPrice != null && !fixedPrice.getCostRatio())) {
					continue;
				}
				Double price = getPrice(po, productManager);
				// 描述表里面所有物料且走费比的
				feePrice += po.getQty() * price;
			}
		} catch (Exception e) {
			logger.error("產品號錯誤");
		}
		return feePrice;
	}

	private Double getPrice(PreOrder po, ProductManager productManager) {
		// Double price = po.getPrice();
		Integer productId = TypeConverter.toInteger(po.getProductId());
		Product product = productManager.get(productId);
		// 是否用导入价格当原价
		// if (price == null) {
		Double price = productManager.isAuthenticProduct(productId) ? product.getMemberPrice()
				: product.getMaterialPrice();
		// }
		return price;
	}

	private double getMaterialFee(List<PreOrder> valuelist, double materialDiscount) {
		final DictManager dictManager = AppContext.getBean(DictManager.class);
		final FixedPriceManager fixedPriceManager = AppContext.getBean(FixedPriceManager.class);
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		// 物料配比
		Dictionary dict_mdiscount = dictManager.lookUpByCode(OrdmConfig.ORDMCONFIG, OrdmConfig.MATERIALDISCOUNT);

		Double materialdiscount = StringUtil.toDouble(dict_mdiscount.getValue());
		if (materialdiscount == null) {
			throw new ServiceException("物料配比字典定义错误！");
		}

		double realPrice = getSupportMaterialFree(valuelist, materialDiscount) * materialdiscount;
		// 实际应配物料
		double totalMaterialFee = realPrice;
		// 实际已经选的物料总金额(包括不占费比的物料)
		double totalMaterialAmt = 0;

		Map<Long, PreOrder> detailsMap = new ConcurrentHashMap<>();
		for (PreOrder po : valuelist) {
			detailsMap.put(po.getId(), po);
		}

		// 要占用费比的赠品、物料
		List<PreOrder> costRatioMaterials = new ArrayList<>();

		// Step 1, 取出物料，计算并排序
		for (PreOrder item : valuelist) {
			Integer productid = TypeConverter.toInteger(item.getProductId());
			FixedPrice fixedPrice = fixedPriceManager.get(productid);
			// 正品或者不占配比的物料
			if (productManager.isAuthenticProduct(TypeConverter.toInteger(productid))) {
				continue;
			}
			Double price = getPrice(item, productManager);
			totalMaterialAmt += item.getQty() * price;
			// 不占配比物料
			if (fixedPrice != null && !fixedPrice.getCostRatio()) {
				continue;
			}
			costRatioMaterials.add(item);
		}

		Collections.sort(costRatioMaterials, (a, b) -> {
			double v = b.getQty() * getPrice(b, productManager) - a.getQty() * getPrice(a, productManager);
			if (v > 0) {
				return 1;
			}
			return v > -0.001 ? 0 : -1;
		});

		// 1st scan materials
		Set<Long> removed = new LinkedHashSet<>();
		List<PreOrder> remains = new ArrayList<>();
		for (PreOrder item : costRatioMaterials) {
			Double price = getPrice(item, productManager);
			double fee = item.getQty() * price;
			if (realPrice - fee >= 0) {
				realPrice -= fee;
				removed.add(item.getId());
			} else {
				remains.add(item);
			}
		}

		// 2nd scan: realPrice 已经扣减了物料，并记录下ID(removed)
		int v_realPrice = (int) (realPrice * 100);
		for (PreOrder item : remains) {
			Double price = getPrice(item, productManager);
			int v_price = (int) (price * 100);
			int num = v_realPrice / v_price;
			// 修正： 试算是否进行扣减
			if (num > 0 && (v_realPrice - v_price * num > 0)) {
				// 修正：同步扣减
				v_realPrice -= v_price * num;
				realPrice -= num * price;
			}
		}
		// Map<String, Object> map_materialFee = new HashMap<>();
		// 收费金额 =已选物料总价-(可配物料金额-剩余金额)
		double materialFee = totalMaterialAmt - totalMaterialFee + realPrice;
		return materialFee;
	}

	private Double getSupportMaterialFree(List<PreOrder> valuelist, double materialDiscount) {
		double realPrice = 0.00;
		FixedPriceManager fixedPriceManager = AppContext.getBean(FixedPriceManager.class);
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		// 支持配比的正品
		for (PreOrder item : valuelist) {
			Integer productid = TypeConverter.toInteger(item.getProductId());
			FixedPrice fixedPrice = fixedPriceManager.get(productid);
			// 物料或者不走配比的大货
			if (!productManager.isAuthenticProduct(TypeConverter.toInteger(item.getProductId()))//
					|| (fixedPrice != null && !fixedPrice.getCostRatio())) {
				continue;
			}
			Double price = getPrice(item, productManager);
			double amt = item.getQty() * price;
			// 不打折
			if (fixedPrice != null && !fixedPrice.getPricePolicy()) {
				realPrice += amt;
			} else {
				realPrice += materialDiscount * amt;
			}
		}
		return realPrice;
	}

	private double getMaterialFreeFee(List<PreOrder> valuelist, double materialDiscount) {
		DictManager dictManager = AppContext.getBean(DictManager.class);
		Dictionary dict_materialdiscount = dictManager.lookUpByCode(OrdmConfig.ORDMCONFIG, OrdmConfig.MATERIALDISCOUNT);
		double materialdiscount = Double.parseDouble(dict_materialdiscount.getValue());
		return getSupportMaterialFree(valuelist, materialDiscount) * materialdiscount;
	}

	private Double getOrderOriginalFee(List<PreOrder> valuelist) {
		ProductManager productManager = AppContext.getBean(ProductManager.class);
		double realPrice = 0;
		for (PreOrder item : valuelist) {
			Integer productId = TypeConverter.toInteger(item.getProductId());
			if (productManager.isAuthenticProduct(productId)) {
				Product p = productManager.get(productId);
				realPrice += p.getMemberPrice() * item.getQty();
			}
		}
		return realPrice;
	}

	private List<OrderDetail> buildOrderDetails(String orderNo, double discount, List<PreOrder> valuelist) {
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		final FixedPriceManager fixedPriceManager = AppContext.getBean(FixedPriceManager.class);
		final DictManager dictManager = AppContext.getBean(DictManager.class);
		int lineNo = 1;
		// FIXNE:默认发货方式是哪一种 是否会有多种
		Dictionary dictionary = dictManager.lookUpByCode(DELIVERYWAY, DELIVERYWAY_SALES);

		List<OrderDetail> orderDetails = new ArrayList<>(valuelist.size());
		try {
			idGenService.lock(ID_ORDERDETAILID);
			idGenService.lock(ID_CARTDETAIL);
			for (PreOrder item : valuelist) {
				if (item.getQty() == 0) {
					continue;
				}
				Double price = getPrice(item, productManager);
				Integer productId = TypeConverter.toInteger(item.getProductId());
				Product product = productManager.get(productId);
				OrderDetail orderDetail = new OrderDetail(product, orderNo, item.getQty());
				FixedPrice fixedPrice = fixedPriceManager.get(productId);
				// FIXNE:发货方式是哪一种 是否会有多种
				String deliveryWayId = item.getDeliveryWayId();
				if (!Strings.isNullOrEmpty(item.getDeliveryWayId())) {
					dictionary = dictManager.lookUpByCode(DELIVERYWAY, deliveryWayId);
				}
				DeliveryWay deliveryWay = new DeliveryWay(dictionary);

				orderDetail.setId(Long.parseLong(idGenService.generateDateId((ID_ORDERDETAILID))));
				orderDetail.setLineNumber(lineNo++);
				Long detailsId = Long.parseLong(idGenService.generateDateId(ID_CARTDETAIL));
				orderDetail.setMemberPrice(price);
				// 有价格策略且不走客户折扣
				if (fixedPrice != null && !fixedPrice.getPricePolicy()) {
					orderDetail.setUnitPrice(fixedPrice.getFix() ? fixedPrice.getPrice() //
							: price * fixedPrice.getDiscount());
				} else if (productManager.isAuthenticProduct(productId)) {
					// 订单明细的产品价格=购物车描述的里面的价格 (单价)
					orderDetail.setUnitPrice(price * discount);
				} else {
					// 取购物车明细描述的价格
					orderDetail.setUnitPrice(price);
				}

				if (productManager.isAuthenticProduct(productId)) {
					//免费的大货
					if(price<0.0001){
						orderDetail.setGiftPrice(price * discount);
					}else{
						orderDetail.setGiftPrice(price);
					}
				} else {
					orderDetail.setGiftPrice(product.getMaterialPrice());
				}
				
				if (fixedPrice != null) {
					orderDetail.setCostRatio(fixedPrice.getCostRatio());
				} else {
					orderDetail.setCostRatio(true);
				}
				orderDetail.setPid(detailsId);
				orderDetail.setSuitNumber(1);
				orderDetail.setDeliveryWayId(deliveryWay.getDeliveryWayId());
				orderDetail.setDeliveryWayName(deliveryWay.getDeliveryName());
				orderDetails.add(orderDetail);
			}
		} catch (ServiceException e) {
			logger.error("", e);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			idGenService.unlock(ID_ORDERDETAILID);
			idGenService.unlock(ID_CARTDETAIL);
		}
		return orderDetails;
	}

	private double getRealPrice(List<PreOrder> valuelist, double discount) {
		FixedPriceManager fixedPriceManager = AppContext.getBean(FixedPriceManager.class);
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		double realPrice = 0.00;
		for (PreOrder item : valuelist) {
			if (!productManager.isAuthenticProduct(TypeConverter.toInteger(item.getProductId()))) {
				continue;
			}
			Double price = getPrice(item, productManager);
			Integer productid = TypeConverter.toInteger(item.getProductId());
			FixedPrice fixedPrice = fixedPriceManager.get(productid);

			if (fixedPrice != null && !fixedPrice.getPricePolicy()) {
				// Fix by : wangs 2017-11-30
				realPrice += item.getQty() * fixedPrice.getPrice();
			} else {
				realPrice += discount * item.getQty() * price;
			}
		}
		return realPrice;
	}

	@Override
	protected Dao<Order, Long> getDao() {
		return orderDao;
	}

}
