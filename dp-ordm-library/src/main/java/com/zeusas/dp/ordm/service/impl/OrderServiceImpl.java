package com.zeusas.dp.ordm.service.impl;

import static com.zeusas.dp.ordm.service.CartDetailService.ID_CARTDETAIL;
import static com.zeusas.dp.ordm.utils.OrdmConfig.DELIVERYWAY;
import static com.zeusas.dp.ordm.utils.OrdmConfig.DELIVERYWAY_DRPRESENT;
import static com.zeusas.dp.ordm.utils.OrdmConfig.DELIVERYWAY_SALES;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_INDIVIDUAL_DISCOUNT;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MAXAMOUNT;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MAXPOSTAGE;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MIDPOSTAGE;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_MINIAMOUNT;
import static com.zeusas.dp.ordm.utils.OrdmConfig.KEY_NOPOSTAGE;
import static com.zeusas.dp.ordm.utils.OrdmConfig.ORDMCONFIG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.service.IdGenService;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.http.ActionException;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.active.bean.ActivityType;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.AmountRule;
import com.zeusas.dp.ordm.active.model.ProductItem;
import com.zeusas.dp.ordm.active.model.ProductRule;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.dao.AliPaymentDao;
import com.zeusas.dp.ordm.dao.CartDetailDao;
import com.zeusas.dp.ordm.dao.CmbcPayRecordDao;
import com.zeusas.dp.ordm.dao.OrderCredentialsDao;
import com.zeusas.dp.ordm.dao.OrderDao;
import com.zeusas.dp.ordm.dao.OrderDetailDao;
import com.zeusas.dp.ordm.dao.ReserveRecordDao;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.CartDetailDesc;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.CustomerPricePolicy;
import com.zeusas.dp.ordm.entity.DeliveryWay;
import com.zeusas.dp.ordm.entity.FixedPrice;
import com.zeusas.dp.ordm.entity.IPackage;
import com.zeusas.dp.ordm.entity.Item;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderCredentials;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.entity.PackageDetail;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ReserveProduct;
import com.zeusas.dp.ordm.entity.ReserveRecord;
import com.zeusas.dp.ordm.service.CartService;
import com.zeusas.dp.ordm.service.Conditions;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.FixedPriceManager;
import com.zeusas.dp.ordm.service.OrderCredentialsService;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.PackageService;
import com.zeusas.dp.ordm.service.PricePolicyManager;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.RedeemPointService;
import com.zeusas.dp.ordm.service.ReserveProductManager;
import com.zeusas.dp.ordm.service.StockService;
import com.zeusas.dp.ordm.task.StorehousesSyncTask;
import com.zeusas.dp.ordm.utils.OrdmConfig;
import com.zeusas.security.auth.entity.AuthUser;

/**
 * 
 * @author fengx
 * @date 2016年12月19日 下午6:33:23
 */
@Service
@Transactional
public class OrderServiceImpl extends BasicService<Order, Long> implements OrderService {
	public final static String ID_ORDERID = "ORDERID";
	public final static String ID_ORDERNO = "ORDERNO";
	public final static String ID_ORDERCREDENTIALID = "ORDERCREDENTIAL";
	public final static String ID_ORDERDETAILID = "ORDERDETAILID";
	public final static String ID_PACKAGE = "PACKAGEID";
	public final static String ID_PACKAGEDETAIL = "PACKAGEDETAIL";
	public final static String BILLCODE_YDH = "21006";
	public final static String BILLCODE_BXX = "21001";

	final static double DEFAULT_MATERIAL_DISCOUNT = 0.06;

	private static Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderDetailDao detailDao;
	@Autowired
	private OrderCredentialsDao orderCredentialsDao;
	@Autowired
	private CartService cartService;
	@Autowired
	private IdGenService idGenService;
	@Autowired
	private OrderCredentialsService credentialsService;
	@Autowired
	private PackageService packageService;
	@Autowired
	private AliPaymentDao aliPaymentDao;
	@Autowired
	private CounterManager counterManager;
	@Autowired
	private DictManager dictManager;
	@Autowired
	private CustomerManager customerManager;
	@Autowired
	private CmbcPayRecordDao cmbcPayRecordDao;
	@Autowired
	ReserveRecordDao reserveRecordDao;
	@Autowired
	private CartDetailDao cartDetailDao;

	@Autowired
	private RedeemPointService redeemPointService;

	@Override
	protected Dao<Order, Long> getDao() {
		return orderDao;
	}

	private void buildOrderDetails(String orderNo, double discount, //
			List<CartDetail> cartDetails, boolean isNewOrder) throws ServiceException {

		// 如果购物车里面有产品id 说明是更新订单明细，所以就要删除原来的订单明细
		if (!isNewOrder) {
			detailDao.deleteOrderDetails(orderNo);
		}

		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		int lineNo = 1;
		// 拿到购物车的所有的明细
		for (CartDetail cd : cartDetails) {
			Integer revId = cd.getRevId();
			for (CartDetailDesc desc : cd.getCartDetailsDesc()) {
				Product product = productManager.get(desc.getProductId());
				OrderDetail orderDetail = new OrderDetail(product, cd, desc, orderNo);
				orderDetail.setdetailType(desc.getReserve() ? OrderDetail.TYPE_RESERVE
						: OrderDetail.TYPE_BUY);
				try {
					idGenService.lock(ID_ORDERDETAILID);
					orderDetail.setId(Long.parseLong(idGenService
							.generateDateId((ID_ORDERDETAILID))));
					orderDetail.setLineNumber(lineNo++);
					// 设置物流编码
					orderDetail.setLogiticsCode(product.getLogisticsCode());
					double price = desc.getPrice();
					if (productManager.isAuthenticProduct(desc.getProductId())) {
						orderDetail.setMemberPrice(product.getMemberPrice());
						// 订单明细的产品价格=购物车描述的里面的价格 (单价)
						if (cd.isPricePolicy()) {
							orderDetail.setUnitPrice(desc.getPrice() * discount);
						} else {
							orderDetail.setUnitPrice(desc.getPrice());
						}
						// 免费的大货
						if (price < 0.0001) {
							orderDetail.setGiftPrice(orderDetail.getMemberPrice() * discount);
						} else {
							orderDetail.setGiftPrice(price);
						}
					} else {
						orderDetail.setMemberPrice(product.getMaterialPrice());
						orderDetail.setGiftPrice(product.getMaterialPrice());
						// 取购物车明细描述的价格
						orderDetail.setUnitPrice(desc.getPrice());
					}
					// 这个pid只是为了前台的展示 表示 几件产品是一个组合包
					orderDetail.setPid(desc.getCartDetailId());
					orderDetail.setDeliveryWayId(desc.getDeliveryWayId());
					orderDetail.setDeliveryWayName(desc.getDeliveryWayName());
					orderDetail.setCostRatio(desc.isCostRatio());
					if (revId != null) {
						orderDetail.setRevId(revId);
					}
					detailDao.save(orderDetail);
				} catch (ServiceException e) {
					throw e;
				} catch (Exception e) {
					throw new ServiceException(e);
				} finally {
					idGenService.unlock(ID_ORDERDETAILID);
				}
			}
		}
	}

	/** 通過用戶和購物車生成訂單頭 */
	@Transactional
	public Order buildOrder(Cart cart, Counter counter, AuthUser checkUser, AuthUser makeUser,
			Integer customerTypeId) throws ServiceException {
		// FIX ME 订单表里面物料的配送比还没有计算--- 订单里面的联系方式还是存在问题的
		// 订单支付类型还没有插入 需要修正
		// 生成订单时关联到客户策略
		final PricePolicyManager pricePolicyManager = AppContext.getBean(PricePolicyManager.class);
		final ProductManager pm = AppContext.getBean(ProductManager.class);
		// 用户类型价格运算的折扣率
		CustomerPricePolicy pricePolicy = pricePolicyManager.getPolicy(customerTypeId);

		double discount = pricePolicy.getDiscount();
		// 用户物料配比的折扣率
		double materialDiscount = pricePolicy.getMaterialDiscount();
		// 制单人、审核人、门店实例化一个订单
		Order order = new Order(checkUser, makeUser, counter);
		String orderId;
		String orderNo;
		String orderCredentialsId;
		// isNewOrder: 判断这是一个退回的订单还是全新生成的一个订单
		boolean isNewOrder = cart.getOrderId() == null;
		try {
			idGenService.lock(ID_ORDERID);
			idGenService.lock(ID_ORDERNO);
			idGenService.lock(ID_ORDERCREDENTIALID);
			// 生成订单id、订单No的主键
			if (isNewOrder) {
				orderId = idGenService.generateDateId(ID_ORDERID);
				orderNo = idGenService.generateDateId(ID_ORDERNO);
				orderCredentialsId = idGenService.generateDateId(ID_ORDERCREDENTIALID);
				order.setId(Long.parseLong(orderId));
				order.setCredentialsNo(orderCredentialsId);
				order.setOrderNo(orderNo);
			} else {
				order = get(cart.getOrderId());
				orderCredentialsId = order.getCredentialsNo();
				orderNo = order.getOrderNo();
			}
			// 所有正品的总价(折扣后)
			double totalPrice = 0.00;
			List<CartDetail> cartDetails = cartService.getCartDetailByCart(cart);
			totalPrice = getRealPrice(cartDetails, discount);

			// 根据所有购物车明细的id获取所有购物车的描述(真正的购物车产品表)
			// 拿到明细id（key）---明细的扫描的集合(value)的map
			// 获取产品、正品、物料的数量
			Integer nums_array[] = getPAndMQty(cartDetails, pm);
			// 获取总数量、正品数量、物料以及正品数量
			int totalNum = nums_array[0];
			int productQty = nums_array[1];
			int materialQty = nums_array[2];
			order.setTotalNum(totalNum);
			// 物料赠品的总价(未乘物料折扣)
			double totalPresentFee = getPresentFee(cartDetails);
			// 需要收费的物料的价格
			double materialFee = (double) cartService.getMaterialFee(cartDetails, materialDiscount)
					.get(Cart.MATERIALFEE);
			// materialFee<0说明没有超出物料配比 大于0的绝对值就是超出的物料的金额
			order.setMaterialFee(materialFee < 0.001 ? 0 : materialFee);
			// 获取订单的邮费
			double expressFee = getPostage(totalPrice);
			if (OrdmConfig.COUNTER_TYPE_DIRECT.equals(counter.getType())) {
				expressFee = 0.0;
			}
			// 订单免费配送的物料的金额(正品 *物料折扣*配比)
			order.setMaterialFreeAmt(getMaterialFreeFee(cartDetails, materialDiscount));
			// 邮费
			order.setExpressFee(expressFee);
			// 正品费用
			order.setProductFee(totalPrice);
			totalPrice += materialFee < 0.001 ? 0 : materialFee;
			totalPrice += expressFee;
			order.setPaymentFee(totalPrice);
			// 应付
			order.setPayable(totalPrice);
			// 订单折扣前的实际价格(正品原价？)
			order.setOrderOriginalFee(getOrderOriginalFee(cartDetails));
			// 生成订单凭据
			OrderCredentials orderCredentials = credentialsService.getOrderCredentials(order
					.getOrderNo());
			if (orderCredentials == null) {
				orderCredentials = new OrderCredentials(order, counter);
				orderCredentials.setOcid(orderCredentialsId);
			}
			orderCredentials.setProductQty(productQty);
			orderCredentials.setMaterialQty(materialQty);
			orderCredentials.setMaterialDiscount(getGitfPrice(cartDetails, materialDiscount));
			orderCredentials.setProductAmt(getRealPrice(cartDetails, discount));
			orderCredentials.setMaterialPay(totalPresentFee);
			// FIXME 这里可以考虑覆盖orderDao里面的save方法
			// 生成订单明细 订单生成后清除购物车
			clearCart(cart.getCartId());
			StorehousesSyncTask synctask = AppContext.getBean(StorehousesSyncTask.class);
			synctask.setUpdated();
			// 设置预订会标记
			for (CartDetail detail : cartDetails) {
				Integer revId = detail.getRevId();
				if (revId != null) {
					order.setReserveFlag(Order.TYPE_RESERVEACTIVITY);
					break;
				}
			}
			buildOrderDetails(orderNo, discount, cartDetails, isNewOrder);
			// 为什么更新操作放在上一个flag里面就不行 可能是上面buildOrderDetails（）参数有cartDetails cart
			// 但是也不影响
			// if(!isNewOrder){
			// cart.setOrderId(null);
			// cartService.update(cart);
			// }
			if (isNewOrder) {
				orderDao.save(order);
				credentialsService.save(orderCredentials);
			} else {
				cart.setStatus(Cart.STATUS_ACTIVE);
				cart.setOrderId(null);
				cartService.update(cart);
				order.setOrderStatus(Order.Status_UnPay);
				orderDao.update(order);
				credentialsService.update(orderCredentials);
			}
			return order;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_ORDERCREDENTIALID);
			idGenService.unlock(ID_ORDERNO);
			idGenService.unlock(ID_ORDERID);
		}
	}

	/**
	 * 当前免费物料-实际购买配送的物料
	 * 
	 * @param cartDetails
	 * @param discount
	 * @return
	 * @throws ServiceException
	 */
	public double compareFeeAndPay(List<CartDetail> cartDetails, double discount)
			throws ServiceException {
		DictManager dictManager = AppContext.getBean(DictManager.class);
		Dictionary dict_materialdiscount = dictManager.lookUpByCode(OrdmConfig.ORDMCONFIG,
				OrdmConfig.MATERIALDISCOUNT);

		double peisongbi = Double.parseDouble(dict_materialdiscount.getValue());
		double realPrice = cartService.getSupportdistributionPrice(cartDetails, discount)
				* peisongbi;
		double presentFee = getPresentFee(cartDetails);
		return presentFee - realPrice;
	}

	/**
	 * 获取免费物料的费用
	 * 
	 * @param cartDetails
	 * @param discount
	 * @return
	 */
	@Override
	public double getMaterialFreeFee(List<CartDetail> cartDetails, double discount) {
		DictManager dictManager = AppContext.getBean(DictManager.class);
		Dictionary dict_materialdiscount = dictManager.lookUpByCode(OrdmConfig.ORDMCONFIG,
				OrdmConfig.MATERIALDISCOUNT);
		double peisongbi = Double.parseDouble(dict_materialdiscount.getValue());
		return cartService.getSupportdistributionPrice(cartDetails, discount) * peisongbi;
	}

	/***
	 * 获取购物车里面实际的正品支付金额
	 * 
	 * @param cartDetails
	 * @param discount
	 * @return
	 */
	private double getRealPrice(List<CartDetail> cartDetails, double discount) {
		double realPrice = 0.00;
		// 在购物车明细表里面遍历所有的产品以及优惠活动组
		// 这里是所有的正品以及优惠活动都走价格策略??? 目前都走价格策略
		for (CartDetail cartDetail : cartDetails) {
			// 活动 正品 取Detail价格
			if (cartDetail.getType().equals(Product.TYPEID_PRODUCT)
					|| cartDetail.getType().equals(CartDetail.ActivityProduct)) {
				if (cartDetail.isPricePolicy()) {
					realPrice += discount * cartDetail.getQuantity() * cartDetail.getPrice();
				} else {
					realPrice += cartDetail.getPrice() * cartDetail.getQuantity();
				}
			} else {
				// 物料 赠品缺desc价格
				for (CartDetailDesc desc : cartDetail.getCartDetailsDesc()) {
					if (!desc.isCostRatio()) {
						realPrice += desc.getPrice() * desc.getQuantity()
								* cartDetail.getQuantity();
					}
				}
			}
		}
		return realPrice;
	}

	/***
	 * 获取购物车正品物料配比的金额
	 * 
	 * @param cartDetails
	 * @param discount
	 * @return
	 */
	@Deprecated
	private double getMaterialPrictAmount(List<CartDetail> cartDetails, double materialDiscount) {
		double realPrice = 0.00;
		// 在购物车明细表里面遍历所有的产品以及优惠活动组
		// 这里是所有的正品以及优惠活动都走价格策略??? 目前都走价格策略
		for (CartDetail cartDetail : cartDetails) {
			// 非正品不支持6%
			if (!cartDetail.getType().equals(Product.TYPEID_PRODUCT)) {
				continue;
			}
			int qty = cartDetail.getQuantity();
			for (CartDetailDesc desc : cartDetail.getCartDetailsDesc()) {
				// 不支持6%的正品
				if (!desc.isCostRatio()) {
					continue;
				}
				double amt = desc.getQuantity() * desc.getPrice() * qty;
				if (cartDetail.isPricePolicy()) {
					realPrice += materialDiscount * amt;
				} else {
					realPrice += amt;
				}
			}
		}
		return realPrice;
	}

	private double getOrderOriginalFee(List<CartDetail> cartDetails) {
		ProductManager pm = AppContext.getBean(ProductManager.class);
		double realPrice = 0;
		for (CartDetail cartDetail : cartDetails) {
			List<CartDetailDesc> descs = cartDetail.getCartDetailsDesc();
			for (CartDetailDesc desc : descs) {
				Integer productId = desc.getProductId();
				if (pm.isAuthenticProduct(productId)) {
					Product p = pm.get(productId);
					realPrice += p.getMemberPrice() * cartDetail.getQuantity() * desc.getQuantity();
				}
			}
		}
		return realPrice;
	}

	/** 获取产品、正品、物料的数量 */
	private Integer[] getPAndMQty(List<CartDetail> cartDetails, ProductManager pm) {
		int totalNum = 0;
		int productQty = 0;
		int materialQty = 0;

		for (CartDetail cartDetail : cartDetails) {
			List<CartDetailDesc> descs = cartDetail.getCartDetailsDesc();
			for (CartDetailDesc desc : descs) {
				totalNum += desc.getQuantity() * cartDetail.getQuantity();
				if (pm.isAuthenticProduct(desc.getProductId())) {
					productQty += desc.getQuantity() * cartDetail.getQuantity();
				} else {
					materialQty += desc.getQuantity() * cartDetail.getQuantity();
				}
			}
		}

		Integer[] Qtys = new Integer[] { totalNum, productQty, materialQty };
		return Qtys;
	}

	/**
	 * 计算费比走购物车描述表即可
	 * 
	 * @param cartDetails
	 * @return
	 * @throws ServiceException
	 */
	public double getPresentFee(List<CartDetail> cartDetails) throws ServiceException {
		double feePrice = 0;
		// 先找到购物车明细的所有的物料以及赠品
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		for (CartDetail cartDetail : cartDetails) {
			List<CartDetailDesc> descs = cartDetail.getCartDetailsDesc();
			for (CartDetailDesc desc : descs) {
				// 判断描述表里面所有不为正品且走费比的的商品
				if (!productManager.isAuthenticProduct(desc.getProductId()) && desc.isCostRatio()) {
					feePrice += desc.getPrice() * desc.getQuantity() * cartDetail.getQuantity();
				}
			}
		}

		return feePrice;
	}

	/**
	 * 获取最大的可赠送费比 discount 用户类型价格策略 discount1 用户物料配比策略
	 * 
	 * @return
	 */
	private double getGitfPrice(List<CartDetail> cartDetails, double discount) {
		DictManager dictManager = AppContext.getBean(DictManager.class);

		Dictionary dict_materialdiscount = dictManager.lookUpByCode(OrdmConfig.ORDMCONFIG,
				OrdmConfig.MATERIALDISCOUNT);
		Assert.notNull(dict_materialdiscount, "");

		double discount1 = TypeConverter.toDouble(dict_materialdiscount.getValue(),
				DEFAULT_MATERIAL_DISCOUNT);
		return cartService.getSupportdistributionPrice(cartDetails, discount) * discount1;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderDetail> getOrderDetails(String orderNo) throws ServiceException {
		List<OrderDetail> orderDetails;
		try {
			orderDetails = detailDao.getOrderDetails(orderNo);
		} catch (DaoException e) {
			logger.warn("获取订单明细失败");
			throw new ServiceException(e);
		}
		return orderDetails;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> getOrders(String status) throws ServiceException {
		List<Order> orders;
		try {
			orders = orderDao.findByType(status);
		} catch (DaoException e) {
			logger.warn("获取订单失败, typeID={}", status, e);
			throw new ServiceException(e);
		}
		return orders;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> getOrders(List<Integer> counterIds, String orderStatus)
			throws ServiceException {
		try {
			return orderDao.findByCounterIds(counterIds, orderStatus);
		} catch (DaoException e) {
			logger.warn("获取订单失败");
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> getOrdersByCounterId(Collection<Integer> counterIds) throws ServiceException {
		try {
			return orderDao.findByCounterIds(counterIds);
		} catch (DaoException e) {
			logger.warn("获取订单失败");
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean changeOrderStatus(Long orderId, String orderStatus) throws ServiceException {
		Order updateOrder = get(orderId);
		if (updateOrder == null) {
			throw new ServiceException("订单不存在, 订单状态：" + orderStatus);
		}
		updateOrder.setOrderStatus(orderStatus);
		updateOrder.setLastUpdate(System.currentTimeMillis());
		update(updateOrder);
		return true;
	}

	@Override
	@Transactional
	public void clearCart(Long cartID) throws ServiceException {
		try {
			cartService.changeCartStatus(cartID, Cart.STATUS_ACTIVE);
			cartService.deleteCart(cartID);
		} catch (DaoException e) {
			throw new ServiceException("清除购物车失败", e);
		}
		// 清空购物车之后将购物车重置为店员可用的状态
		refuse(cartID);
	}

	@Override
	public void refuse(Long cartId) {
		Cart cart = cartService.get(cartId);
		cart.setStatus(Cart.STATUS_ACTIVE);
		cart.setLastUpdate(System.currentTimeMillis());
		cartService.update(cart);
		StorehousesSyncTask synctask = AppContext.getBean(StorehousesSyncTask.class);
		synctask.setUpdated();
	}

	/**
	 * 订单支付成功调用方法
	 * 
	 * @throws ServiceException
	 */
	@Override
	public void paySuccess(String orderNo, AuthUser payMan, List<String> imageUrl)
			throws ServiceException {
		Order order = getsingleOrder(orderNo);
		if (order == null) {
			throw new ServiceException("该订单不存在，请重新确认!");
		}
		// 付款人的信息
		order.setPayManId(payMan.getLoginName());
		order.setPayManName(payMan.getCommonName());
		order.setImageURL(imageUrl);

		changeOrderStatus(order.getId(), Order.Status_DoPay);
	}

	/**
	 * 线下转账，余额不足全部支付
	 */
	@Override
	public Order payOrder(Order order, AuthUser payMan) throws ServiceException {
		try {
			final Order updateOrder = get(order.getId());
			if (updateOrder == null) {
				logger.warn("支付订单失败, 订单{}不存在。", order.getId());
				throw new ServiceException("订单不存在");
			}
			if (order.getPaymentPrice() != null && order.getPaymentPrice() > 0) {
				updateOrder.setPaymentPrice(order.getPaymentPrice());
			}

			// FIXME
			if (order.getImageURL().size() > 0) {
				updateOrder.setImageURL(order.getImageURL());
			}

			if (!StringUtil.isEmpty(order.getDescription())) {
				updateOrder.setDescription(order.getDescription());
			}
			updateOrder.setPayManId(payMan.getLoginName());
			updateOrder.setPayManName(payMan.getCommonName());
			updateOrder.setPayTypeId(Order.PayType_offlinePay);
			updateOrder.setLastUpdate(System.currentTimeMillis());
			updateOrder.setOrderPayTime(System.currentTimeMillis());

			OrderCredentials updateCredential = credentialsService.getOrderCredentials(updateOrder
					.getOrderNo());
			updateCredential.setPayManId(payMan.getLoginName());
			updateCredential.setPayManName(payMan.getCommonName());
			updateCredential.setPayType(Order.PayType_offlinePay);
			updateCredential.setPayTypeName("线下支付,上传凭据");
			updateCredential.setCustomerDesc(updateOrder.getDescription());
			updateCredential.setImgUrl(updateOrder.getImageURL());

			credentialsService.update(updateCredential);
			update(updateOrder);

			return updateOrder;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Order confirmPay(Order order) throws ServiceException {
		Order updateOrder = get(order.getId());
		// 订单描述若为Null 会影响金蝶推单 ，必须默认一个属性
		if (Strings.isNullOrEmpty(updateOrder.getDescription())) {
			updateOrder.setDescription("无");
		}
		updateOrder.setOrderStatus(Order.Status_DoPay);
		updateOrder.setOrderPayTime(System.currentTimeMillis());
		update(updateOrder);
		return updateOrder;
	}

	@Override
	public Order cancelPay(Order order) throws ServiceException {
		Order updateOrder = get(order.getId());
		if (!updateOrder.getOrderStatus().equals(Order.Status_UnPay)) {
			logger.warn("该类型订单不能取消支付{}", updateOrder);
			throw new ServiceException("该类型订单不能取消支付");
		}
		updateOrder.setImageURL(null);
		updateOrder.setPaymentPrice(null);
		updateOrder.setPayTypeId(null);
		updateOrder.setDescription(null);
		updateOrder.setPayManId(null);
		updateOrder.setPayManName(null);
		update(updateOrder);
		return null;
	}

	/**
	 * 合并订单支付，创建合并支付的凭证 只有确认的时候才会提交，其余剩下任何操作都不会产生凭证 保证凭证的唯一一次生成。
	 * 
	 */
	@Override
	public OrderCredentials payCombineOrder(List<Order> os, AuthUser payMan, Double actualPay,
			String description, List<String> combineImages) throws ServiceException {
		// 来自系统内的订单
		List<Order> orders = this.getCombineOrders(os);
		try {

			OrderCredentials orderCredentials = credentialsService
					.getCombineOrderCredentials(orders.get(0).getOrderNo());
			Assert.notNull(orderCredentials, "凭证为空");
			// 用户实际支付
			if (actualPay < 0) {
				throw new ServiceException("实际支付小于0元");
			}
			orderCredentials.setActualPay(actualPay);
			// 订单描述
			orderCredentials.setCustomerDesc(description);
			orderCredentials.setCreatorId(payMan.getLoginName());
			orderCredentials.setCreatorName(payMan.getCommonName());
			orderCredentials.setPayManId(payMan.getLoginName());
			orderCredentials.setPayManName(payMan.getCommonName());
			orderCredentials.setAddress("合并地址，详情以每单为准");
			orderCredentials.setPayTypeName("合并支付");
			orderCredentials.setImgUrl(combineImages);
			// 凭证更新
			credentialsService.update(orderCredentials);
			return orderCredentials;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 财务退回重新支付合并订单
	 * 
	 * @param os
	 * @param payMan
	 * @param actualPay
	 * @param description
	 * @param combineImages
	 * @return
	 */
	@Transactional
	public OrderCredentials repayCombineOrders(List<Order> os, AuthUser payMan, Double actualPay,
			String description, List<String> combineImages) {

		// 来自系统内的订单
		List<Order> orders = this.getCombineOrders(os);
		try {
			// 如果没有订单凭证就生成 并且状态不是财务退回并且是合并支付的
			OrderCredentials orderCredentials = credentialsService
					.getCombineOrderCredentials(orders.get(0).getOrderNo());
			Assert.notNull(orderCredentials, "");
			// 无凭证
			for (Order order : orders) {
				// 每一单必须满足这两个条件才可以继续下面的操作
				if (Order.status_ForFinancialRefuse.equals(order.getOrderStatus())
						|| !order.isMerger()) {
					throw new ServiceException("不满足合并订单再支付");
				}
			}
			// 用户实际支付
			orderCredentials.setActualPay(actualPay);
			// 订单描述
			orderCredentials.setCustomerDesc(description);
			orderCredentials.setCreatorId(payMan.getLoginName());
			orderCredentials.setCreatorName(payMan.getCommonName());
			orderCredentials.setPayManId(payMan.getLoginName());
			orderCredentials.setPayManName(payMan.getCommonName());
			orderCredentials.setAddress("合并地址，详情以每单为准");
			orderCredentials.setPayTypeName("合并支付");
			orderCredentials.setUseBlance(0.00);
			orderCredentials.setImgUrl(combineImages);
			// 凭证更新
			credentialsService.update(orderCredentials);
			return orderCredentials;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * 确认金额后将订单发放到每一单，修改每一单的状态，并设置每一单状态为线下支付 确认合并订单
	 */
	@Override
	@Transactional
	public void confirmCombineOrders(List<Order> orders) throws ServiceException {
		if (orders.size() < 2) {
			throw new ServiceException("订单数量少于2!");
		}

		OrderCredentials orderCred = credentialsService.getCombineOrderCredentials(orders.get(0)
				.getOrderNo());
		if (orderCred == null) {
			throw new ServiceException("订单凭证为空");
		}
		// Step1: 将金额全部分发到每一单， 修改每一单状态为已付款，并设置支付类型为线下支付，设置付款人，图片路径
		double actualPay = orderCred.getActualPay();
		// 是否使用余额 保留2位小数
		double useBlance = Math.round(orderCred.getUseBlance() * 100) / 100.0;
		double balanceRemains = useBlance;
		double actualPayRemains = actualPay;
		// 实际在本订单支付金额
		double payAmt;
		for (Order order : orders) {
			double paymentFee = order.getPaymentFee();
			// 使用余额并且全额支付
			if (balanceRemains - paymentFee >= 0) {
				balanceRemains -= paymentFee;
				order.doPayment(paymentFee, 0, Order.PayType_offlinePay, orderCred);
			} else if (balanceRemains > 0.001) {
				// 扣除余额后，需支付金额
				double payable = paymentFee - balanceRemains;
				if (actualPayRemains >= payable) {
					payAmt = payable;
					actualPayRemains -= payAmt;
				} else {
					payAmt = actualPayRemains;
					actualPayRemains = 0;
				}
				order.doPayment(balanceRemains, payAmt, Order.PayType_offlinePay, orderCred);
				balanceRemains = 0;
			} else if (actualPayRemains > 0) {
				// 扣除余额后，需支付金额
				double payable = paymentFee;
				// 实际在本订单支付金额
				if (actualPayRemains >= payable) {
					payAmt = payable;
					actualPayRemains -= payAmt;
				} else {
					payAmt = actualPayRemains;
					actualPayRemains = 0;
				}
				order.doPayment(0, payAmt, Order.PayType_offlinePay, orderCred);
			} else {
				order.doPayment(0, 0, Order.PayType_offlinePay, orderCred);
			}

		}

		try {
			for (Order order : orders) {
				// FIXME:添加活动物料到订单，事务回滚?
				this.update(order);

				// this.addActivityToOrder(order.getOrderNo(),
				// counterManager.getCounterByCode(order.getCounterCode()));
				// Step2: 凭证路径设置
				OrderCredentials ocs = credentialsService.getOrderCredentials(order.getOrderNo());
				ocs.setImgUrl(orderCred.getImgUrl());
				credentialsService.update(ocs);
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 合并订单确认支付
	 * 
	 * @param orders
	 * @param flag
	 *            是否审核通过
	 * @throws ServiceException
	 */
	@Override
	@Transactional
	public void financialAuditCombine(List<Order> orders, boolean flag) throws ServiceException {
		List<Order> Cmborders = this.getCombineOrders(orders);

		try {
			String orderNo0 = Cmborders.get(0).getOrderNo();
			OrderCredentials ocb = credentialsService.getCombineOrderCredentials(orderNo0);
			if (ocb == null) {
				throw new ServiceException("订单不存在");
			}
			Map<String, List<OrderDetail>> orderDetailsMap = new HashMap<>();

			for (Order order : Cmborders) {
				if (flag) {
					String orderNo = order.getOrderNo();
					List<OrderDetail> orderDetails = detailDao.getOrderDetails(orderNo);
					orderDetailsMap.put(orderNo, orderDetails);
				}
			}
			detailDao.clear();
			for (Order order : Cmborders) {
				if (!Order.Status_DoPay.equals(order.getOrderStatus())) {
					throw new ServiceException("订单不是待审核状态");
				}
				if (flag) {
					order.setOrderStatus(Order.status_LogisticsDelivery);
					List<OrderDetail> orderDetails = orderDetailsMap.get(order.getOrderNo());
					// bulidPackage(order, orderDetails);
					bulidPackagePlus(order, orderDetails);
				} else {
					order.setOrderStatus(Order.status_ForFinancialRefuse);
				}

				if (!StringUtil.isEmpty(order.getCheckDesc())) {
					order.setCheckDesc(order.getCheckDesc());
				}

				order.setLastUpdate(System.currentTimeMillis());
				this.update(order);
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * 取消合并订单 Step1 设置每单Merger状态为false,支付类型为Null,图片为Null,每单为待付款 Step2
	 * 删除每个单独凭证里的合并ID Step3 删除合并订单
	 */
	@Override
	@Transactional
	public void cancelCombineOrders(List<Order> os) throws ServiceException {
		List<Order> orders = this.getCombineOrders(os);
		try {
			for (Order order : orders) {
				// 若有一单为已付款 则不能删除
				if (Order.Status_DoPay.equals(order.getOrderStatus())) {
					throw new ServiceException("合并订单已经付款不能删除！");
				}
				// Step1
				order.setMerger(false);
				order.setPayTypeId(null);
				order.setImageURL(null);
				order.setOrderStatus(Order.Status_UnPay);
				this.update(order);
				// Step2
				OrderCredentials oc = credentialsService.getOrderCredentials(order.getOrderNo());
				oc.setCombineId(null);
				credentialsService.update(oc);

			}
			OrderCredentials ocs = credentialsService.getCombineOrderCredentials(orders.get(0)
					.getOrderNo());
			// Step3
			orderCredentialsDao.deleteOC(ocs.getOcid());

		} catch (ServiceException e) {
			logger.error("取消订单异常", e);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * 拆解合并订单 删除凭证，去掉凭证里子凭证的父ID，还原每单的状态都为待付款 就是重置这种属性为以前的状态
	 */
	@Override
	@Transactional
	public void dismantleCombineOrders(List<Order> os) throws ServiceException {
		List<Order> orders = this.getCombineOrders(os);
		try {
			for (Order order : orders) {
				// Step1 去掉子凭证
				// 只有待付款和财务退回的可以拆分
				if (Order.Status_UnPay.equals(order.getOrderStatus())
						|| Order.status_ForFinancialRefuse.equals(order.getOrderStatus())) {

					OrderCredentials oc = credentialsService
							.getOrderCredentials(order.getOrderNo());
					oc.setCombineId(null);
					credentialsService.update(oc);
					// Step2 还原 释放余额
					// payManID payManName payMentPrice description orderPayTime
					// imageUrl payTypeID isMerge lastUpdate
					// orderStatus useBalance payAble
					order.setPayManId(null);
					order.setPayManName(null);
					order.setPaymentPrice(null);
					order.setDescription(null);
					order.setOrderPayTime(null);
					order.setImageURL(null);
					order.setPayTypeId(null);
					order.setMerger(false);

					order.setOrderStatus(Order.Status_UnPay);
					order.setUseBalance(0.0);
					order.setPayable(order.getPaymentFee());

					this.update(order);
				} else {
					throw new ServiceException("拆解订单异常");
				}
			}
			// Step3 del OrderCredentials
			OrderCredentials ocb = credentialsService.getCombineOrderCredentials(orders.get(0)
					.getOrderNo());
			orderCredentialsDao.deleteOC(ocb.getOcid());
		} catch (ServiceException e) {
			logger.error("拆分异常", e);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 合并订单余额全额支付 controller变化，不需要上传截图了
	 */
	@Override
	public List<Order> balancePayCombineOrdersDirect(List<Order> os, AuthUser payMan,
			String description) throws ServiceException {
		List<Order> orders = null;
		try {
			orders = this.getCombineOrders(os);
			OrderCredentials orderCredentials = credentialsService
					.getCombineOrderCredentials(orders.get(0).getOrderNo());
			// 用户实际支付
			orderCredentials.setActualPay((double) 0);

			Assert.notNull(orderCredentials, "");
			// 订单描述
			orderCredentials.setCustomerDesc(description);
			orderCredentials.setCreatorId(payMan.getLoginName());
			orderCredentials.setCreatorName(payMan.getCommonName());
			orderCredentials.setPayManId(payMan.getLoginName());
			orderCredentials.setPayManName(payMan.getCommonName());
			orderCredentials.setAddress("合并地址，详情以每单为准");
			orderCredentials.setPayTypeName("合并支付");
			// 全额支付
			orderCredentials.setActualPay(orderCredentials.getTotalAmt());
			// 凭证更新
			credentialsService.update(orderCredentials);
			logger.info(orderCredentials.getOcid(), "合并支付确认付款凭证");
			for (Order order : orders) {

				OrderCredentials credentials = credentialsService.getOrderCredentials(order
						.getOrderNo());
				order.doPayment(order.getPaymentFee(), 0, Order.PayType_offlinePay,
						orderCredentials);
				orderDao.update(order);

				if (credentials != null) {
					credentials.setPayManId(payMan.getLoginName());
					credentials.setPayManName(payMan.getCommonName());
					credentials.setPayType(Order.PayType_balancePay);
					credentials.setPayTypeName("余额支付");
				}
				credentialsService.update(credentials);
			}
			return orders;
		} catch (ServiceException e) {
			logger.error("余额全额支付异常", e);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return orders;
	}

	@Transactional(readOnly = true)
	public Order getsingleOrder(String orderNo) throws ServiceException {
		return orderDao.findByOrderNo(orderNo);
	}

	public void completeOrder(String orderNo) throws ServiceException {
		Order order = getsingleOrder(orderNo);
		if (order == null) {
			throw new ServiceException("该订单不存在，请核算后重新确认!");
		}
		changeOrderStatus(order.getId(), Order.status_CompleteShipping);// 完成支付
	}

	@Override
	public int getPostage(double totalPrice) {
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
		// FIXME:临界值
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
	@Transactional
	public Order updateOrder(Order order) throws ServiceException {
		Order updateOrder = get(order.getId());
		if (updateOrder == null) {
			logger.error("更新订单失败, 订单ID={}", order.getId());
			throw new ServiceException("订单不存在");
		}
		
		//FIXME: 应付价格会<0? 传入的Order 支付费用可能为空？
		if (order.getPaymentPrice() != null && order.getPaymentPrice() >= 0) {
			updateOrder.setPaymentPrice(order.getPaymentPrice());
		}
		try {
			OrderCredentials updateCredential = credentialsService.getOrderCredentials(updateOrder
					.getOrderNo());
			if (!Strings.isNullOrEmpty(order.getDescription())) {
				updateOrder.setDescription(order.getDescription());
				if (updateCredential != null) {
					updateCredential.setCheckerDesc(order.getDescription());
					credentialsService.update(updateCredential);
				}
			}
			if (!Strings.isNullOrEmpty(order.getCheckDesc())) {
				updateOrder.setCheckDesc(order.getCheckDesc());
			}
			if (!Strings.isNullOrEmpty(order.getOrderStatus())) {
				updateOrder.setOrderStatus(order.getOrderStatus());
			}
			if (order.getTotalNum() != null) {
				updateOrder.setTotalNum(order.getTotalNum());
			}
			updateOrder.setLastUpdate(System.currentTimeMillis());
			return update(updateOrder);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional
	public void bulidPackage(Order order, List<OrderDetail> orderDetails) throws ServiceException {
		assert order != null;
		if (!order.getOrderStatus().equals(Order.status_LogisticsDelivery)) {
			logger.error("该订单状态不应该生成包裹{}", order);
			throw new ServiceException("订单不处于待物流推送状态!");
		}

		IPackage iPackage = new IPackage(order);
		// FIXME
		Counter counter = counterManager.get(order.getCounterId());
		Dictionary dic = dictManager.lookUpByCode(OrdmConfig.WAREHOUSE, counter.getWarehouses());

		iPackage.setWarehouseCode(dic.getValue());
		iPackage.setWarehouseName(dic.getName());

		String packageId;
		try {
			idGenService.lock(ID_PACKAGE);
			packageId = idGenService.generateDateId(ID_PACKAGE);
			iPackage.setId(packageId);
			buildPackageAndDetail(order, orderDetails, iPackage);
		} catch (ServiceException e) {
			logger.error("生成包裹失败, 订单ID#{}", order.getOrderNo());
			throw e;
		} catch (Exception e) {
			logger.error("生成包裹失败, 订单ID#{}", order.getOrderNo());
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_PACKAGE);
		}
	}

	@Transactional
	public void bulidPackagePlus(Order order, List<OrderDetail> orderDetails)
			throws ServiceException {
		assert order != null;
		if (!order.getOrderStatus().equals(Order.status_LogisticsDelivery)) {
			logger.error("该订单状态不应该生成包裹{}", order);
			throw new ServiceException("订单不处于待物流推送状态!");
		}
		CounterManager cm = AppContext.getBean(CounterManager.class);
		Counter counter = cm.getCounterByCode(order.getCounterCode());
		// 计算费比使用
		Map<String, List<OrderDetail>> codeDetailMap = calculationFreeQty(order, orderDetails);

		// 正常产品金额
		double normalAmt = 0.0;
		// 打欠产品金额
		double reserveAmt = 0.0;
		// 正常产品使用的余额
		double normalBalance = 0.0;
		// 打欠产品使用的余额
		double reserveBalance = 0.0;
		// 订单使用的总余额
		double useBlance = order.getUseBalance();

		for (List<OrderDetail> details : codeDetailMap.values()) {
			for (OrderDetail detail : details) {
				// 计算正常产品 打欠产品金额
				Double amt = detail.getUnitPrice() * (detail.getQuantity() - detail.getFreeQty());
				if (OrderDetail.TYPE_BUY == (detail.getDetailType()) || //
						OrderDetail.TYPE_RESERVE_THEN_DONE == (detail.getDetailType())) {
					normalAmt += amt;
				}
				if (OrderDetail.TYPE_RESERVE == (detail.getDetailType())) {
					reserveAmt += amt;
				}
			}
		}
		// 邮费算正常产品
		normalAmt += order.getExpressFee();

		normalBalance = useBlance > normalAmt ? normalAmt : useBlance;
		reserveBalance = useBlance > normalAmt ? useBlance - normalAmt : new Double(0);

		order.setNormalAmt(normalAmt);
		order.setReserveAmt(reserveAmt);
		order.setNormalBalance(normalBalance);
		order.setReserveBalance(reserveBalance);

		orderDao.update(order);
		// 打欠不生成包裹
		codeDetailMap.put(IPackage.BILL_DQ, new ArrayList<>());

		// FIXME:by shi all直接用orderDetails替换？引用传递
		List<OrderDetail> all = new ArrayList<>();
		for (List<OrderDetail> details : codeDetailMap.values()) {
			if (!details.isEmpty()) {
				all.addAll(details);
			}
		}

		// 1.直营
		if (Counter.Type_Direct.equals(counter.getType())) {
			List<OrderDetail> productDetails = new ArrayList<>();
			List<OrderDetail> materialDetails = new ArrayList<>();
			for (OrderDetail detail : all) {
				// 正品 生日礼盒(物料)走调拨单
				// 物料赠品 工服(正品)走销售单
				if (detail.getUnitCode().startsWith("C.SR")) {
					productDetails.add(detail);
					continue;
				}
				if (detail.getUnitCode().startsWith("W.G")) {
					materialDetails.add(detail);
					continue;
				}
				if (Product.TYPEID_PRODUCT.equals(detail.getTypeId())) {
					productDetails.add(detail);
				} else {
					materialDetails.add(detail);
				}
			}
			if (!productDetails.isEmpty()) {
				IPackage iPackage = bulidPackage(order, IPackage.BILL_BDD);
				buildPackageDetal(order, productDetails, iPackage);
			}
			if (!materialDetails.isEmpty()) {
				IPackage iPackage = bulidPackage(order, IPackage.BILL_BXX);
				buildPackageDetal(order, materialDetails, iPackage);
			}
			return;
		}

		// 2.加盟
		// 纯打欠 把物料拆分数量更新到reserveRecord中 若收邮费则生成DQ包裹
		if (!codeDetailMap.get(IPackage.BILL_DQ).isEmpty()//
				&& codeDetailMap.get(IPackage.BILL_BXX).isEmpty()//
				&& codeDetailMap.get(IPackage.BILL_YDH).isEmpty()) {
			IPackage iPackage = bulidPackage(order, IPackage.BILL_DQ);
			buildPackageDetal(order, new ArrayList<>(), iPackage);
			return;
		}

		// 纯补货 生成BH包裹 不收邮费
		if (!codeDetailMap.get(IPackage.BILL_BH).isEmpty()//
				&& codeDetailMap.get(IPackage.BILL_BXX).isEmpty()//
				&& codeDetailMap.get(IPackage.BILL_YDH).isEmpty()) {
			IPackage iPackage = bulidPackage(order, IPackage.BILL_BH);
			buildPackageDetal(order, codeDetailMap.get(IPackage.BILL_BH), iPackage);
			return;
		}

		// 纯还欠生成HQ包裹 不受收费
		if (!codeDetailMap.get(IPackage.BILL_HQ).isEmpty()//
				&& codeDetailMap.get(IPackage.BILL_BXX).isEmpty()//
				&& codeDetailMap.get(IPackage.BILL_YDH).isEmpty()) {
			IPackage iPackage = bulidPackage(order, IPackage.BILL_HQ);
			buildPackageDetal(order, codeDetailMap.get(IPackage.BILL_HQ), iPackage);
			return;
		}

		// 预订会 生成一个订单
		if (!codeDetailMap.get(IPackage.BILL_YDH).isEmpty()) {
			// 根据门店类型来生成包裹 加盟:BXX 直营:BXX 和 BDD
			IPackage iPackage = bulidPackage(order, IPackage.BILL_YDH);
			buildPackageDetal(order, all, iPackage);
			return;
		}

		// 积分兑换 生成JF包裹 不收邮费
		if (!codeDetailMap.get(IPackage.BILL_JF).isEmpty()//
				&& codeDetailMap.get(IPackage.BILL_BXX).isEmpty()//
				&& codeDetailMap.get(IPackage.BILL_YDH).isEmpty()) {
			IPackage iPackage = bulidPackage(order, IPackage.BILL_JF);
			buildPackageDetal(order, codeDetailMap.get(IPackage.BILL_JF), iPackage);
			return;
		}

		// 正常订货 随单补货 随单还欠
		if (!codeDetailMap.get(IPackage.BILL_BXX).isEmpty()//
				&& codeDetailMap.get(IPackage.BILL_YDH).isEmpty()) {
			// 根据门店类型来生成包裹 加盟:BXX 直营:BXX 和 BDD
			IPackage iPackage = bulidPackage(order, IPackage.BILL_BXX);
			buildPackageDetal(order, all, iPackage);
			return;
		}
	}

	/**
	 * @param order
	 * @param billCode
	 *            订单号编码
	 * @throws ServiceException
	 */
	private IPackage bulidPackage(Order order, String billCode) throws ServiceException {
		assert order != null;
		if (!order.getOrderStatus().equals(Order.status_LogisticsDelivery)) {
			logger.error("该订单状态不应该生成包裹{}", order);
			throw new ServiceException("订单不处于待物流推送状态!");
		}
		IPackage iPackage = new IPackage(order);

		Counter counter = counterManager.get(order.getCounterId());
		Dictionary dic = dictManager.lookUpByCode(OrdmConfig.WAREHOUSE, counter.getWarehouses());

		iPackage.setWarehouseCode(dic.getValue());
		iPackage.setWarehouseName(dic.getName());
		iPackage.setBillNo(billCode + order.getOrderNo());

		String packageId;
		try {
			idGenService.lock(ID_PACKAGE);
			packageId = idGenService.generateDateId(ID_PACKAGE);
			iPackage.setId(packageId);
		} catch (ServiceException e) {
			logger.error("生成包裹失败, 订单ID#{}", order.getOrderNo());
			throw e;
		} catch (Exception e) {
			logger.error("生成包裹失败, 订单ID#{}", order.getOrderNo());
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_PACKAGE);
		}
		return iPackage;
	}

	/**
	 * 计算配比使用 根据订单类型分类
	 */
	private Map<String, List<OrderDetail>> calculationFreeQty(Order order,
			List<OrderDetail> orderDetails) throws ServiceException {
		// 最大免物料费用金额
		double realPrice = order.getMaterialFreeAmt();
		// 正品
		List<OrderDetail> productDetails = new ArrayList<>(orderDetails.size());
		// 物料
		List<OrderDetail> materialDetails = new ArrayList<>(orderDetails.size());

		Map<Long, OrderDetail> materialMap = new HashMap<>();

		for (OrderDetail detail : orderDetails) {
			if (Product.TYPEID_PRODUCT.equals(detail.getTypeId())) {
				productDetails.add(detail);
			} else {
				materialDetails.add(detail);
				materialMap.put(detail.getId(), detail);
			}
		}

		Comparator<OrderDetail> cmp = (a, b) -> {
			double v = b.getUnitPrice() - a.getUnitPrice();
			return v > 0 ? 1 : (v > -0.001) ? 0 : -1;
		};

		Collections.sort(materialDetails, cmp);

		Set<Long> removed = new HashSet<>();
		// step 1: scan 配比内植物医生赠送
		for (OrderDetail meterial : materialDetails) {
			int detailType = meterial.getDetailType();
			if (!DELIVERYWAY_SALES.equals(meterial.getDeliveryWayId())//
					|| OrderDetail.TYPE_DONE == (detailType)//
					|| OrderDetail.TYPE_REPLENISHMENT == (detailType)//
					|| !meterial.getCostRatio()) {
				continue;
			}
			double v = meterial.getUnitPrice() * meterial.getQuantity();
			// 整条明细可以使用费比支付
			if (realPrice - v >= 0) {
				realPrice -= v;
				removed.add(meterial.getId());
				meterial.setFreeQty(meterial.getQuantity());
			}
			// 打欠产品需要更record
			if (OrderDetail.TYPE_RESERVE == (detailType)) {
				ReserveRecord record = reserveRecordDao.get(meterial.getId());
				record.setFreeQty(meterial.getFreeQty());
				reserveRecordDao.update(record);
			}
			detailDao.update(meterial);
		}
		// step 2: rescan
		for (OrderDetail meterial : materialDetails) {
			int detailType = meterial.getDetailType();
			if (removed.contains(meterial.getId())//
					|| !DELIVERYWAY_SALES.equals(meterial.getDeliveryWayId())//
					|| OrderDetail.TYPE_DONE == detailType//
					|| OrderDetail.TYPE_REPLENISHMENT == detailType//
					|| OrderDetail.TYPE_REDEEMPOINTS == detailType//
					|| !meterial.getCostRatio()) {
				continue;
			}
			// 剩余费比不足以支付整条明细 num为该条明细可用费比支付个数
			int num = (int) (realPrice / meterial.getUnitPrice());
			if (num > 0) {
				// 已经拆包的，也要移除
				removed.add(meterial.getId());
				meterial.setFreeQty(num);
				realPrice -= num * meterial.getUnitPrice();
				// 打欠产品需要更record
				if (OrderDetail.TYPE_RESERVE == (detailType)) {
					ReserveRecord record = reserveRecordDao.get(meterial.getId());
					record.setFreeQty(num);
					reserveRecordDao.update(record);
				}
				meterial.setFreeQty(num);
				detailDao.update(meterial);
			}
		}

		// 装配Map
		DictManager dictManager = AppContext.getBean(DictManager.class);
		Dictionary billCode = dictManager.get(IPackage.BILL_CODE);
		List<Dictionary> billCodes = billCode.getChildren();
		Dictionary ydhDict = dictManager.get(BILLCODE_YDH);
		Dictionary bxxDict = dictManager.get(BILLCODE_BXX);
		// 预订会订单编码
		String revActCode = ydhDict.getHardCode();
		String bxxCode = bxxDict.getHardCode();

		// 订单明细中DetailType与单号编码的映射 物料默认先放到BXX中 最后判断是否是直营再拆出来放到BDD中
		Map<Integer, String> codeMap = new HashMap<>();

		Map<String, List<OrderDetail>> detailMap = new HashMap<>();

		for (Dictionary dict : billCodes) {
			Integer value = TypeConverter.toInteger(dict.getValue());
			String code = dict.getHardCode();
			codeMap.put(value, code);
			// 初始化
			detailMap.put(code, new ArrayList<>());
		}

		// 此处用orderDetails是因为orderDetails 和 materialDetails里都是他的引用
		for (OrderDetail detail : orderDetails) {

			String code = codeMap.get(detail.getDetailType());
			// 预订会编码
			if (detail.getRevId() != null//
					&& OrderDetail.TYPE_RESERVE == detail.getDetailType()) {
				code = revActCode;
				// 不是预订会的有库存打欠 放到BXX中
			} else if (OrderDetail.TYPE_RESERVE_THEN_DONE == detail.getDetailType()) {
				code = bxxCode;
			}
			detailMap.get(code).add(detail);
		}
		return detailMap;
	}

	/**
	 * 改版后 只分明细 不计算
	 * 
	 * @param orderDetails
	 * @param iPackage
	 */
	private void buildPackageDetal(Order order, List<OrderDetail> orderDetails, IPackage iPackage) {
		DictManager dictManager = AppContext.getBean(DictManager.class);// 2.1
		ProductManager pm = AppContext.getBean(ProductManager.class);
		FixedPriceManager fixedPriceManager = AppContext.getBean(FixedPriceManager.class);

		// 植物医生赠送
		Dictionary dictionary_present = dictManager
				.lookUpByCode(DELIVERYWAY, DELIVERYWAY_DRPRESENT);// 2.1
		DeliveryWay dw_present = new DeliveryWay(dictionary_present);// 2.1
		// 邮费
		Dictionary postage_100 = dictManager.lookUpByCode(ORDMCONFIG, KEY_MAXPOSTAGE);
		Dictionary postage_150 = dictManager.lookUpByCode(ORDMCONFIG, KEY_MIDPOSTAGE);

		Dictionary dictionary_discount = dictManager.lookUpByCode(ORDMCONFIG,
				KEY_INDIVIDUAL_DISCOUNT);
		double individual_discount = TypeConverter.toDouble(dictionary_discount.getValue());

		// 包裹名细
		final List<PackageDetail> packageDetails = new ArrayList<PackageDetail>();
		Integer lineNo = 1;
		String packageId = iPackage.getId();

		int totalQty = 0;
		double totalAmt = 0.0;
		try {
			idGenService.lock(ID_PACKAGEDETAIL);
			for (OrderDetail detail : orderDetails) {
				// 产品数量
				int quantity = detail.getQuantity();
				// 免费数量
				int freeQty = detail.getFreeQty();
				// 购买数量
				int buyQty = freeQty == 0 ? quantity : quantity - freeQty;
				// 计算总价 数量
				totalQty += quantity;
				totalAmt += buyQty * detail.getUnitPrice();

				double merberPrice = detail.getMemberPrice();
				// 默认为单价
				double indivPrice = detail.getUnitPrice();
				Integer pid = detail.getProductId();
				FixedPrice fixedprice = fixedPriceManager.get(pid);
				// 有价格策略 且打折的 用会员价算出生个体户价格
				if (indivPrice > 0.0001//
						&& fixedprice != null //
						&& fixedprice.getPricePolicy()) {
					indivPrice = merberPrice * individual_discount;
				}

				if (freeQty != 0) {
					PackageDetail packageDetail = new PackageDetail(detail);
					packageDetail.setUnitPrice(0d);
					packageDetail.setIndivPrice(0d);
					packageDetail.setProductQty(freeQty);
					// 销售的物料拆为赠送
					if (!Product.TYPEID_PRODUCT.equals(detail.getTypeId())//
							&& DELIVERYWAY_SALES.equals(detail.getDeliveryWayId())) {
						packageDetail.setDeliveryWayId(dw_present.getDeliveryWayId());
						packageDetail.setDeliveryWayName(dw_present.getDeliveryName());
					} else {
						packageDetail.setDeliveryWayId(detail.getDeliveryWayId());
						packageDetail.setDeliveryWayName(detail.getDeliveryWayName());
					}
					packageDetail.setLineNumber(lineNo++);
					packageDetail.setOrderDetailId(detail.getId());
					packageDetail.setPackageId(packageId);
					packageDetail.setId(idGenService.generateDateId(ID_PACKAGEDETAIL));
					packageDetails.add(packageDetail);
				}
				if (buyQty != 0) {
					PackageDetail packageDetail = new PackageDetail(detail);
					packageDetail.setProductQty(buyQty);
					packageDetail.setDeliveryWayId(detail.getDeliveryWayId());
					packageDetail.setDeliveryWayName(detail.getDeliveryWayName());
					packageDetail.setLineNumber(lineNo++);
					packageDetail.setOrderDetailId(detail.getId());
					packageDetail.setPackageId(packageId);
					packageDetail.setId(idGenService.generateDateId(ID_PACKAGEDETAIL));
					packageDetail.setIndivPrice(indivPrice);
					packageDetails.add(packageDetail);
				}
			}
			if (!packageDetails.isEmpty()//
					&& order.getExpressFee().intValue() > 0) {
				Dictionary dictionary = dictManager.lookUpByCode(DELIVERYWAY, DELIVERYWAY_SALES);
				DeliveryWay deliveryWay = new DeliveryWay(dictionary);
				Product postage = pm.get(22542);
				double expressFee = order.getExpressFee();

				PackageDetail packageDetail_postage = new PackageDetail(deliveryWay);
				packageDetail_postage.setProductId(22542);
				packageDetail_postage.setProductName("直发系统邮费");
				packageDetail_postage.setUnitPrice(expressFee);
				packageDetail_postage.setGiftPrice(expressFee);
				packageDetail_postage.setProductQty(1);
				packageDetail_postage.setOrderDetailId(0L);
				packageDetail_postage.setProductCode(postage.getProductCode());
				packageDetail_postage.setLogiticsCode(postage.getLogisticsCode());
				packageDetail_postage.setProductName(postage.getName());
				packageDetail_postage.setLineNumber(lineNo++);
				packageDetail_postage.setPackageId(packageId);
				packageDetail_postage.setId(idGenService.generateDateId(ID_PACKAGEDETAIL));
				packageDetail_postage.setIndivPrice(expressFee);
				packageDetails.add(packageDetail_postage);
			}

			iPackage.setTotalAmount(totalAmt);
			iPackage.setTotalQty(totalQty);
			packageService.save(iPackage, packageDetails);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.error("生成包裹明细失败 orderid={}", orderDetails.get(0).getOrderNo());
			throw new ServiceException("生成包裹明细失败", e);
		} finally {
			idGenService.unlock(ID_PACKAGEDETAIL);
		}
	}

	@Override
	public List<PackageDetail> buildProductPack(List<OrderDetail> orderDetails, ProductManager pm)
			throws ServiceException {
		List<PackageDetail> packageDetails = new ArrayList<PackageDetail>();
		for (OrderDetail detail : orderDetails) {
			PackageDetail packageDetail = new PackageDetail();
			// 产品id
			packageDetail.setProductId(detail.getProductId());
			// 包裹里面得到的价格是订单里面每件产品的实际单价
			packageDetail.setUnitPrice(detail.getUnitPrice());
			// 包裹里面得到订单里面的每件产品的数量
			packageDetail.setProductQty(detail.getQuantity());
			packageDetail.setDeliveryWayId(detail.getDeliveryWayId());
			packageDetail.setDeliveryWayName(detail.getDeliveryWayName());
			packageDetail.setOrderDetailId(detail.getId());
			packageDetails.add(packageDetail);
		}
		return packageDetails;
	}

	/**
	 * 根据购物车、订单数据，生成物料包裹
	 */
	@Override
	public List<PackageDetail> buildMaterialPack(Order order, List<OrderDetail> orderMaterials)
			throws ServiceException {
		DictManager dictManager = AppContext.getBean(DictManager.class);// 2.1
		// 植物医生赠送
		Dictionary dictionary_present = dictManager
				.lookUpByCode(DELIVERYWAY, DELIVERYWAY_DRPRESENT);// 2.1
		DeliveryWay dw_present = new DeliveryWay(dictionary_present);// 2.1
		// 销售
		Dictionary dictionary_sale = dictManager.lookUpByCode(DELIVERYWAY, DELIVERYWAY_SALES);// 2.1
		DeliveryWay dw_sale = new DeliveryWay(dictionary_sale);// 2.1

		// 包裹名细
		final List<PackageDetail> packageDetails = new ArrayList<PackageDetail>();
		// 最大免物料费用金额
		double realPrice = order.getMaterialFreeAmt();

		Map<Long, OrderDetail> materialMap = new HashMap<>();
		for (OrderDetail d : orderMaterials) {
			materialMap.put(d.getId(), d);
		}

		Comparator<OrderDetail> cmp = (a, b) -> {
			double v = b.getUnitPrice() * b.getQuantity() - a.getUnitPrice() * b.getQuantity();
			return v > 0 ? 1 : (v > -0.001) ? 0 : -1;
		};

		Collections.sort(orderMaterials, cmp);

		Set<Long> removed = new HashSet<>();
		// step 1: scan 配比内植物医生赠送
		for (OrderDetail b : orderMaterials) {
			int detailType = b.getDetailType();
			// 发货方式为销售的物料要拆分为销售和植物医生赠送
			// 不占配比的物料不参与计算
			if (!DELIVERYWAY_SALES.equals(b.getDeliveryWayId())//
					|| OrderDetail.TYPE_DONE == (detailType)//
					|| !b.getCostRatio()) {
				continue;
			}
			double v = b.getUnitPrice() * b.getQuantity();
			// 整条明细可以使用费比支付
			if (realPrice - v >= 0) {
				realPrice -= v;
				removed.add(b.getId());
				// 本单预订产品不生成包裹
				if (OrderDetail.TYPE_RESERVE == (detailType)) {
					ReserveRecord record = reserveRecordDao.get(b.getId());
					record.setFreeQty(b.getQuantity());
					reserveRecordDao.update(record);
					continue;
				}
				PackageDetail pkg = makePackage(dw_present, b, b.getQuantity(), 0);
				if (b.getUnitPrice() <= 0) {
					pkg.setDeliveryWayId(b.getDeliveryWayId());
					pkg.setDeliveryWayName(b.getDeliveryWayName());
				}
				packageDetails.add(pkg);
			}
		}
		// step 2: rescan
		for (OrderDetail b : orderMaterials) {
			int detailType = b.getDetailType();
			// FIX ME:已经使用费比的部分和发货方式不是销售的 还欠产品不占用配比
			// 不占配比的物料不参与计算
			if (removed.contains(b.getId())//
					|| !DELIVERYWAY_SALES.equals(b.getDeliveryWayId())//
					|| OrderDetail.TYPE_DONE == detailType //
					|| !b.getCostRatio()) {
				continue;
			}
			// 剩余费比不足以支付整条明细 num为该条明细可用费比支付个数
			int num = (int) (realPrice / b.getUnitPrice());
			if (num > 0) {
				// 已经拆包的，也要移除
				removed.add(b.getId());
				// 本单预订产品不生成包裹
				if (OrderDetail.TYPE_RESERVE == detailType) {
					ReserveRecord record = reserveRecordDao.get(b.getId());
					record.setFreeQty(num);
					reserveRecordDao.update(record);
					continue;
				}
				// 拆封：
				PackageDetail pkg1 = makePackage(dw_present, b, num, 0);
				packageDetails.add(pkg1);
				PackageDetail pkg2;
				pkg2 = makePackage(dw_sale, b, b.getQuantity() - num, b.getUnitPrice());
				packageDetails.add(pkg2);
				// XXX:拆分数量
				b.setFreeQty(num);
				detailDao.update(b);
				break;
			}
		}
		// 剩下为销售或者其他 还欠产品不占费比但是要生成包裹
		for (OrderDetail b : orderMaterials) {
			if (removed.contains(b.getId())) {
				continue;
			}
			PackageDetail pkg1;
			// 还欠明细拆分
			if (OrderDetail.TYPE_DONE == (b.getDetailType())) {
				Integer freeQty = b.getFreeQty();
				// 全部收费
				if (freeQty.intValue() == 0) {
					pkg1 = makePackage(dw_sale, b, b.getQuantity(), b.getUnitPrice());
					// 部分
				} else {
					pkg1 = makePackage(dw_present, b, b.getFreeQty(), 0);
					PackageDetail pkg2 = makePackage(dw_sale, b, b.getQuantity() - b.getFreeQty(),
							b.getUnitPrice());
					packageDetails.add(pkg2);
				}
				// 本单物料发货方式为销售的
			} else if (DELIVERYWAY_SALES.equals(b.getDeliveryWayId())//
					|| !b.getCostRatio()) {
				pkg1 = makePackage(dw_sale, b, b.getQuantity(), b.getUnitPrice());
			} else {
				// FIXME:物料发货方式除了销售都不收钱？ 改： 字典里设标志表示该发货方式不收钱
				Dictionary dictionary_Material = dictManager.lookUpByCode(DELIVERYWAY,
						b.getDeliveryWayId());
				DeliveryWay dw_Material = new DeliveryWay(dictionary_Material);
				pkg1 = makePackage(dw_Material, b, b.getQuantity(), 0);
			}
			packageDetails.add(pkg1);
		}
		return packageDetails;
	}

	private PackageDetail makePackage(DeliveryWay dw, OrderDetail detail, int num, double price) {
		PackageDetail packageDetail = new PackageDetail(dw);
		packageDetail.setOrderDetailId(detail.getId());
		packageDetail.setProductId(detail.getProductId());
		packageDetail.setUnitPrice(price);
		packageDetail.setProductQty(num);
		return packageDetail;
	}

	private void buildPackageAndDetail(Order order, List<OrderDetail> orderDetails,
			IPackage iPackage) throws ServiceException {
		DictManager dictManager = AppContext.getBean(DictManager.class);
		ProductManager pm = AppContext.getBean(ProductManager.class);

		int lineNo = 1;
		String packageId = iPackage.getId();

		final List<OrderDetail> orderDetails_material = new ArrayList<>();
		final List<OrderDetail> orderDetails_product = new ArrayList<>();
		for (OrderDetail orderDetail : orderDetails) {
			// 如果不是正品
			if (!pm.isAuthenticProduct(orderDetail.getProductId())) {
				// 预订物料不生成包裹明细后续处理 但是要参与计算
				orderDetails_material.add(orderDetail);
			} else {
				// 如果是预订正品不生成包裹明细
				if (OrderDetail.TYPE_RESERVE == orderDetail.getDetailType()) {
					continue;
				}
				orderDetails_product.add(orderDetail);
			}
		}
		List<PackageDetail> all = new ArrayList<PackageDetail>();
		// 物料明细 根据配件计算物料金额 拆分
		List<PackageDetail> details_material = buildMaterialPack(order, orderDetails_material);
		// 产品明细
		List<PackageDetail> details_product = buildProductPack(orderDetails_product, pm);
		Dictionary postage_100 = dictManager.lookUpByCode(ORDMCONFIG, KEY_MAXPOSTAGE);
		Dictionary postage_150 = dictManager.lookUpByCode(ORDMCONFIG, KEY_MIDPOSTAGE);
		all.addAll(details_product);
		all.addAll(details_material);
		if (order.getExpressFee().intValue() == Integer.parseInt(postage_100.getValue())
				|| order.getExpressFee().intValue() == Integer.parseInt(postage_150.getValue())) {
			Dictionary dictionary = dictManager.lookUpByCode(DELIVERYWAY, DELIVERYWAY_SALES);
			DeliveryWay deliveryWay = new DeliveryWay(dictionary);
			PackageDetail packageDetail_postage = new PackageDetail(deliveryWay);
			packageDetail_postage.setProductId(22542);
			packageDetail_postage.setProductName("直发系统邮费");
			packageDetail_postage.setUnitPrice(order.getExpressFee());
			packageDetail_postage.setProductQty(1);
			packageDetail_postage.setOrderDetailId(0L);
			all.add(packageDetail_postage);
		}
		try {
			idGenService.lock(ID_PACKAGEDETAIL);
			for (PackageDetail packageDetail : all) {
				Product p = pm.get(packageDetail.getProductId());
				packageDetail.setProductCode(p.getProductCode());
				packageDetail.setLogiticsCode(p.getLogisticsCode());
				packageDetail.setProductName(p.getName());
				packageDetail.setLineNumber(lineNo++);
				packageDetail.setPackageId(packageId);
				packageDetail.setId(idGenService.generateDateId(ID_PACKAGEDETAIL));
			}
			packageService.save(iPackage, all);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.error("生成包裹明细失败 orderid={}", order.getOrderNo());
			throw new ServiceException("生成包裹明细失败");
		} finally {
			idGenService.unlock(ID_PACKAGEDETAIL);
		}
	}

	@Override
	public void financialAudit(Order order, boolean flag) throws ServiceException {
		Order updateOrder = get(order.getId());
		if (updateOrder == null) {
			throw new ServiceException("订单不存在");
		}
		if (!updateOrder.getOrderStatus().equals(Order.Status_DoPay)) {
			throw new ServiceException("不属于财务审核订单");
		}
		try {
			if (flag) {
				updateOrder.setOrderStatus(Order.status_LogisticsDelivery);
				List<OrderDetail> orderDetails = getOrderDetails(updateOrder.getOrderNo());
				// bulidPackage(updateOrder, orderDetails);
				bulidPackagePlus(updateOrder, orderDetails);
			} else {
				updateOrder.setOrderStatus(Order.status_ForFinancialRefuse);
			}
			if (!StringUtil.isEmpty(order.getCheckDesc())) {
				updateOrder.setCheckDesc(order.getCheckDesc());
			}

			updateOrder.setLastUpdate(System.currentTimeMillis());
			update(updateOrder);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 更新数据库字段
	 */
	@Override
	public Order payByAlipay(Order order, double totleFee) throws ServiceException {
		try {
			String orderStatus = order.getOrderStatus();
			if (!Order.Status_UnPay.equals(orderStatus) && //
					!Order.status_ForFinancialRefuse.equals(orderStatus)) {
				logger.warn("订单不是处于待付款状态{}", order);
				throw new ServiceException("订单不是处于代付款状态");
			}

			order.setOrderStatus(Order.status_LogisticsDelivery);
			order.setPayTypeId(Order.PayType_aliPay);
			order.setPaymentPrice(totleFee);

			if (StringUtil.isEmpty(order.getDescription())) {
				order.setDescription("支付宝支付");
			}

			// 支付宝支付直接生成包裹
			List<OrderDetail> orderDetails = detailDao.getOrderDetails(order.getOrderNo());
			// bulidPackage(order, orderDetails);
			bulidPackagePlus(order, orderDetails);

			order.setLastUpdate(System.currentTimeMillis());
			order.setOrderPayTime(System.currentTimeMillis());
			update(order);

			OrderCredentials credentials = credentialsService.getOrderCredentials(order
					.getOrderNo());
			if (credentials != null) {
				credentials.setPayType(Order.status_LogisticsDelivery);
				credentials.setPayTypeName("支付宝支付");
				credentialsService.update(credentials);
			}
			List<IPackage> packages = packageService.getPackageByNo(order.getOrderNo());
			if (order.getReserveFlag() != null //
					&& Order.TYPE_RESERVEACTIVITY.equals(order.getReserveFlag())//
					&& packages.isEmpty()) {
				Counter coutner = counterManager.get(order.getCounterId());
				// 预定会专款
				aliPaymentDao.buildK3Receive(order.getOrderNo(), totleFee, coutner.getCustomerId(),
						Integer.valueOf(0));
				// 预定会的其他应收单 专用收款
				aliPaymentDao.buildK3OtherReceive(order.getOrderNo(), totleFee,
						coutner.getCustomerId());
			}
			return order;
		} catch (Exception e) {
			logger.error("alipay", e);
		}
		return order;
	}

	/**
	 * 民生付
	 */
	@Override
	@Transactional
	public Order payByCmbcpay(Order order, double totleFee) throws ServiceException {
		try {
			String orderStatus = order.getOrderStatus();
			if (!Order.Status_UnPay.equals(orderStatus) && //
					!Order.status_ForFinancialRefuse.equals(orderStatus)) {
				logger.warn("订单不是处于待付款状态{}", order);
				throw new ServiceException("订单不是处于代付款状态");
			}

			order.setOrderStatus(Order.status_LogisticsDelivery);
			order.setPayTypeId(Order.PayType_cmbcPay);
			order.setPaymentPrice(totleFee);

			if (StringUtil.isEmpty(order.getDescription())) {
				order.setDescription("民生付");
			}

			// 支付宝支付直接生成包裹
			List<OrderDetail> orderDetails = getOrderDetails(order.getOrderNo());
			// bulidPackage(order, orderDetails);
			bulidPackagePlus(order, orderDetails);

			order.setLastUpdate(System.currentTimeMillis());
			order.setOrderPayTime(System.currentTimeMillis());
			update(order);

			OrderCredentials credentials = credentialsService.getOrderCredentials(order
					.getOrderNo());
			if (credentials != null) {
				credentials.setPayType(Order.status_LogisticsDelivery);
				credentials.setPayTypeName("民生付");
				credentialsService.update(credentials);
			}

			// XXX:民生付存储过程
			// Counter coutner = counterManager.get(order.getCounterId());
			// cmbcPayRecordDao.buildK3Receive(order.getOrderNo(), totleFee,
			// coutner.getCustomerId());
			return order;
		} catch (Exception e) {
			logger.error("cmbcPay", e);
		}
		return order;
	}

	@Override
	public Order payByDirect(Order order, AuthUser user) throws ServiceException {
		Order updateOrder = getsingleOrder(order.getOrderNo());
		if (updateOrder == null) {
			logger.warn("该订单不存在");
			throw new ServiceException("该订单不存在");
		}
		String orderStatus = updateOrder.getOrderStatus();
		if (!Order.Status_UnPay.equals(orderStatus)//
				&& !Order.status_ForFinancialRefuse.equals(orderStatus)) {
			logger.warn("订单不是处于代付款状态{}", updateOrder);
			throw new ServiceException("订单不是处于代付款状态");
		}
		updateOrder.setDescription(order.getDescription());
		updateOrder.setPayTypeId(Order.PayType_directOrder);
		updateOrder.setPayManId(user.getLoginName());
		updateOrder.setPayManName(user.getCommonName());
		updateOrder.setLastUpdate(System.currentTimeMillis());
		updateOrder.setPaymentPrice(0.0);
		updateOrder.setOrderStatus(Order.Status_DoPay);
		updateOrder.setOrderPayTime(System.currentTimeMillis());
		OrderCredentials credentials = credentialsService.getOrderCredentials(order.getOrderNo());
		if (credentials != null) {
			credentials.setPayManId(user.getLoginName());
			credentials.setPayManName(user.getCommonName());
			credentials.setPayType(Order.PayType_directOrder);
			credentials.setPayTypeName("直营支付");
			credentialsService.update(credentials);
		}

		return updateOrder;
	}

	@Override
	public Order payByBlance(Order order, AuthUser user, String counterType)
			throws ServiceException {
		Order updateOrder = getsingleOrder(order.getOrderNo());
		if (updateOrder == null) {
			logger.warn("该订单不存在");
			throw new ServiceException("该订单不存在");
		}
		String orderStatus = updateOrder.getOrderStatus();
		if (!Order.Status_UnPay.equals(orderStatus)//
				&& !Order.status_ForFinancialRefuse.equals(orderStatus)) {
			logger.warn("订单不是处于代付款状态{}", updateOrder);
			throw new ServiceException("订单不是处于代付款状态");
		}

		updateOrder.setDescription(order.getDescription());
		if ("加盟".equals(counterType)) {
			updateOrder.setPayTypeId(Order.PayType_balancePay);
		} else if ("直营".equals(counterType)) {
			updateOrder.setPayTypeId(Order.PayType_directOrder);
		}
		updateOrder.setPayManId(user.getLoginName());
		updateOrder.setPayManName(user.getCommonName());
		updateOrder.setLastUpdate(System.currentTimeMillis());
		updateOrder.setPaymentPrice(0.0);
		updateOrder.setOrderStatus(Order.Status_DoPay);
		updateOrder.setOrderPayTime(System.currentTimeMillis());
		OrderCredentials credentials = credentialsService.getOrderCredentials(order.getOrderNo());
		if (credentials != null) {
			credentials.setPayManId(user.getLoginName());
			credentials.setPayManName(user.getCommonName());
			if ("加盟".equals(counterType)) {
				credentials.setPayType(Order.PayType_balancePay);
				credentials.setPayTypeName("余额支付");
			} else if ("直营".equals(counterType)) {
				credentials.setPayType(Order.PayType_directOrder);
				credentials.setPayTypeName("直营支付");
			}
			credentialsService.update(credentials);
		}

		return updateOrder;
	}

	@Override
	public void cancelOrder(Order order) throws ServiceException {
		Order updateOrder = get(order.getId());
		if (updateOrder == null) {
			throw new ServiceException("订单不存在, 订单状态：");
		}
		updateOrder.setUseBalance((double) 0);
		updateOrder.setPayable((double) 0);
		updateOrder.setOrderStatus(Order.Status_Invalid);
		updateOrder.setLastUpdate(System.currentTimeMillis());
		updateOrder.setOrderCancelTime(System.currentTimeMillis());
		updateOrder.setCancelDesc(order.getCancelDesc());
		update(updateOrder);
		redeemPointService.removeRedeem(updateOrder.getOrderNo());
		StorehousesSyncTask synctask = AppContext.getBean(StorehousesSyncTask.class);
		synctask.setUpdated();
	}

	/**
	 * 单个订单使用余额
	 */
	@Override
	public void userBalance(Long orderId, double usefulBalance, boolean flag)
			throws ServiceException {
		Order order = this.get(orderId);
		Assert.notNull(order, "订单不存在！");
		double paymentFee = order.getPaymentFee();
		// true :使用余额 false:释放余额
		if (flag) {
			// 余额充足
			if (Math.abs(usefulBalance - paymentFee) > 0.0001 && (usefulBalance - paymentFee) > 0) {
				order.setUseBalance(paymentFee);
				order.setPayable((double) 0);
			} else {
				// 余额不足付整单
				double usedBalance = usefulBalance > 0 ? usefulBalance : 0;
				order.setUseBalance(usedBalance);
				order.setPayable(paymentFee - usedBalance);
			}
		} else {
			order.setUseBalance((double) 0);
			order.setPayable(paymentFee);
		}
		try {
			orderDao.update(order);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * 获取合并订单里的订单集合
	 * 
	 * @param orders
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public List<Order> getCombineOrders(List<Order> orders) throws ServiceException {
		if (orders.size() < 2) {
			throw new ServiceException("合并订单为空,并且最少两单");
		}
		List<String> orderNos = new ArrayList<>();
		for (Order o : orders) {
			orderNos.add(o.getOrderNo());
		}
		Map<String, Object> params = new HashMap<>();
		params.put("orderNos", orderNos);
		try {
			return orderDao.find("WHERE orderNo IN ( :orderNos)", params);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 合并订单使用余额
	 */
	@Override
	public void userBalance(List<Order> os, double usefulBalance, boolean flag)
			throws ServiceException {
		List<Order> orders = this.getCombineOrders(os);
		OrderCredentials orderCredentials = credentialsService.getCombineOrderCredentials(orders
				.get(0).getOrderNo());
		Assert.notNull(orderCredentials, "");
		// 应付金额
		double payable = orderCredentials.getTotalAmt();
		try {
			// true :使用余额 false:释放余额
			if (flag) {
				// 余额充足
				if (Math.abs(usefulBalance - payable) > 0.0001 && (usefulBalance - payable) > 0) {
					orderCredentials.setUseBlance(payable);
					orderCredentials.setActualPay((double) 0);
				} else {
					orderCredentials.setUseBlance((usefulBalance > 0) ? usefulBalance : 0.0);
				}
			} else {
				orderCredentials.setUseBlance((double) 0);
			}
			credentialsService.update(orderCredentials);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	@Transactional
	public void buildOrderToCart(Order order) throws ServiceException, ActionException {
		CounterManager counterManager = AppContext.getBean(CounterManager.class);
		ActivityManager activityManager = AppContext.getBean(ActivityManager.class);
		ProductManager pm = AppContext.getBean(ProductManager.class);
		try {
			// 通过订单号 拿到订单下面的明细
			List<OrderDetail> orderDetails = detailDao.getOrderDetails(order.getOrderNo());
			// 给订单明细做索引 key=pid 是通过购物车明细和其描述生成的 pid相等说明是同一个明细下面的产品
			if (orderDetails.size() <= 0) {
				return;
			}
			// 通过订单号获取到门店
			Counter counter = counterManager.get(order.getCounterId());
			// 预定 送物料活动 要除去
			List<Activity> activities = activityManager.findByType(ActivityType.TYPE_BIGPACAKGES);
			Set<String> actIds = new HashSet<>(activities.size());
			// 大礼包是免费的 所以要清掉
			activities.addAll(activityManager.findByType(ActivityType.TYPE_BIGPACAKGE));
			for (Activity activity : activities) {
				actIds.add(activity.getActId());
			}

			Map<Long, List<OrderDetail>> map_orderdetail = orderDetails.stream() //
					.filter(e -> !actIds.contains(e.getActivityId()))//
					.collect(Collectors.groupingBy(OrderDetail::getPid));

			// 通过门店获取购物车
			Cart cart = cartService.getCounterCart(counter);
			cart.setOrderId(order.getId());

			for (Long key : map_orderdetail.keySet()) {
				final List<OrderDetail> ods = map_orderdetail.get(key);
				OrderDetail orderDetail0 = ods.get(0);
				// size=1说明该订单明细是一个单品
				if (ods.size() == 1 && Strings.isNullOrEmpty(orderDetail0.getActivityId())) {
					// 这里可以做一个批量添加的方法，避免每次添加产品的时候都要从数据库去查询，因为订单每条明细都是单独的独立的不同的记录
					cartService.add(cart, pm.get(orderDetail0.getProductId()),
							orderDetail0.getQuantity());
					continue;
				}
				// size>1 说明该订单明细是一个包组合 是一个活动
				// 多个明细不一定是活动 如果活动id为空说明不是一个活动
				if (StringUtil.isEmpty(orderDetail0.getActivityId())) {
					continue;
				}
				int qty = orderDetail0.getSuitNumber();
				// 将明细构建成items 调用增加活动产品到购物车的方法
				List<Item> items = new ArrayList<>(ods.size());
				for (OrderDetail od : ods) {
					Item item = new Item(od.getUnitPrice() <= 0 ? Item.TYPE_FREE : Item.TYPE_PAY, //
							od.getProductId().toString(), //
							od.getQuantity() / od.getSuitNumber());
					items.add(item);
				}
				// FIX ME 需要确定加入活动的时候有没有校验活动的有效期
				cartService.add(cart.getCartId(), orderDetail0.getActivityId(), items, qty);
			}
			cartService.update(cart);
			// 订单退回到购物车时候，订单状态应该是无效的，防止退回到购物车的时候其他付款之后的订单发生错误
			order.setOrderStatus(Order.Status_Invalid);
			// 退回购车时 清除41活动明细 清除单头活动记录
			order.setActivRecord(null);
			update(order);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> findOrders(Conditions condition) throws ServiceException {
		try {
			return orderDao.findOrders(condition);
		} catch (DaoException e) {
			logger.warn("获取订单失败");
			throw new ServiceException(e);
		}
	}

	/**
	 * 创建合并订单的凭证
	 * 
	 * @param Controllor
	 *            给出所有的订单集合。
	 * @author wangs
	 */
	@Override
	@Transactional
	public OrderCredentials createCombinePayment(List<Order> orders) throws ServiceException {
		// 凭证主键 ，凭证一定是要新生成的
		String orderCredentialsId;
		try {
			idGenService.lock(ID_ORDERCREDENTIALID);
			orderCredentialsId = idGenService.generateDateId(ID_ORDERCREDENTIALID);
			// 一个单号有且只有一个合并单号
			// 设置订单支付方式为：合并支付，在字典表里
			// 尺寸小于2不生成
			for (Order o : orders) {
				// XXX:在上一步传递过来的orders
				Order order = this.getsingleOrder(o.getOrderNo());
				// 直营订单不能加入
				Customer customer = customerManager.get(Integer.valueOf(order.getCustomerId()));
				// 11391 加盟 11387 直营
				String customerTypeId = customer.getCustomerTypeID().toString();
				if ("11387".equals(customerTypeId)) {
					throw new ServiceException("包含直营订单");
				}
				// 不是未支付的订单不合并，并且抛出异常回滚合并
				if (!Order.Status_UnPay.equals(order.getOrderStatus())) {
					throw new ServiceException("订单状态不合格！");
				}
				// 进行判重，只能有唯一一个合并订单，若为合并支付的订单不能再次参加其他订单的合并
				if (order.isMerger()) {
					throw new ServiceException(order.getOrderNo() + "已经在其他合并订单中存在！");
				}
				order.setMerger(true);
				// 更新状态
				this.update(order);
				// 获取单个凭证
				OrderCredentials orderCredentials = credentialsService.getOrderCredentials(o
						.getOrderNo());
				// 设置合并父ID
				if (orderCredentials != null) {
					orderCredentials.setCombineId(orderCredentialsId);
					credentialsService.update(orderCredentials);
				}
			}
			// 创建新的合并订单
			OrderCredentials combineOrders = new OrderCredentials(orders);
			// 设置主键
			combineOrders.setOcid(orderCredentialsId);
			combineOrders.setUseBlance((double) 0);
			// 生成合并订单凭证
			credentialsService.save(combineOrders);
			// 返回单个订单凭证
			return credentialsService.get(orderCredentialsId);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_ORDERCREDENTIALID);
		}
	}

	@Override
	@Transactional
	public Order addFreeMaterial(String orderNo, Collection<Item> materials)
			throws ServiceException {
		Order order = null;
		try {
			order = getsingleOrder(orderNo);
			Integer totalNum = order.getTotalNum();
			for (Item item : materials) {
				if (item.getDetalType() == null) {
					item.setDetalType(OrderDetail.TYPE_REPLENISHMENT);
				}
			}
			Integer materialNum = addMaterial(order, materials, 1);
			order.setTotalNum(totalNum + materialNum);
			order = updateOrder(order);
		} catch (Exception e) {
			logger.error("Method:addFreeMaterial orderNo:{}", orderNo);
		}
		return order;
	}

	private Integer addMaterial(Order order, Collection<Item> materials, Integer suitNum)
			throws ServiceException {
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		final DictManager dictManager = AppContext.getBean(DictManager.class);
		final ActivityManager activityManager = AppContext.getBean(ActivityManager.class);
		final PricePolicyManager pricePolicyManager = AppContext.getBean(PricePolicyManager.class);

		String customerId = order.getCustomerId();

		Customer customer = customerManager.get(TypeConverter.toInteger(customerId));
		Assert.notNull(customer, "添加物料 客户为空");

		// 用户类型价格运算的折扣率
		CustomerPricePolicy pricePolicy = pricePolicyManager
				.getPolicy(customer.getCustomerTypeID());

		double discount = pricePolicy.getDiscount();
		// 追加产品的总数
		Integer totalNum = 0;

		String orderNo = order.getOrderNo();
		List<OrderDetail> details = getOrderDetails(orderNo);
		// 初始化明细行号 (如果没有订单明细则为1)
		int lineNo = 1;

		if (details != null && !details.isEmpty()) {
			// 最后一条明细
			OrderDetail lastDetail = details.stream()
					.max(Comparator.comparing(d -> d.getLineNumber())).get();
			lineNo = lastDetail.getLineNumber() + 1;
		}
		Map<String, Long> pidMap = new HashMap<>();
		for (Item item : materials) {
			if (item.getNum().intValue() == 0) {
				continue;
			}
			Product product = productManager.get(TypeConverter.toInteger(item.getId()));
			OrderDetail orderDetail = new OrderDetail(product, orderNo, item.getNum() * suitNum);
			Dictionary deliveryWay = dictManager.lookUpByCode(DELIVERYWAY, item.getDeliveryWayId());

			if (deliveryWay == null) {
				logger.error("Method:addMaterial orderNo{}, 发货方式不存在 发货方式id :{}", orderNo,
						item.getDeliveryWayId());
				continue;
			}

			Activity activity = null;
			if (!StringUtil.isEmpty(item.getActId())) {
				activity = activityManager.get(item.getActId());
			}

			try {
				idGenService.lock(ID_ORDERDETAILID);
				idGenService.lock(ID_CARTDETAIL);
				orderDetail.setId(Long.parseLong(idGenService.generateDateId((ID_ORDERDETAILID))));
				orderDetail.setLineNumber(lineNo);
				// 会员价
				if (productManager.isAuthenticProduct(product.getProductId())) {
					orderDetail.setMemberPrice(product.getMemberPrice());
					orderDetail.setGiftPrice(product.getMemberPrice() * discount);
				} else {
					orderDetail.setMemberPrice(product.getMaterialPrice());
					orderDetail.setGiftPrice(product.getMaterialPrice());
				}
				// 单价
				orderDetail.setUnitPrice(item.getAmt());
				// 默认走配比
				orderDetail.setCostRatio(true);
				// 追加的物料是正常产品
				Integer detailType = item.getDetalType();
				if (detailType != null) {
					orderDetail.setdetailType(detailType);
				} else {
					orderDetail.setdetailType(OrderDetail.TYPE_BUY);
				}
				// 这个pid只是为了前台的展示 表示 几件产品是一个组合包
				String activityId = orderDetail.getActivityId();
				Long detailPId = new Long(0);
				if (activityId == null) {
					detailPId = Long.parseLong(idGenService.generateDateId(ID_CARTDETAIL));
				}
				if (activityId != null && !pidMap.containsKey(activityId)) {
					detailPId = Long.parseLong(idGenService.generateDateId(ID_CARTDETAIL));
					pidMap.put(activityId, detailPId);
				}
				if (activityId != null && pidMap.containsKey(activityId)) {
					detailPId = pidMap.get(activityId);
				}
				orderDetail.setPid(detailPId);
				// 行号
				orderDetail.setLineNumber(lineNo);
				// 发货方式
				orderDetail.setDeliveryWayId(deliveryWay.getHardCode());
				orderDetail.setDeliveryWayName(deliveryWay.getName());
				// 套数
				orderDetail.setSuitNumber(suitNum);
				// 活动
				if (activity != null) {
					orderDetail.setActivityId(activity.getActId());
					orderDetail.setActivityName(activity.getName());
				}
				// 打欠批次
				String batchNo = item.getBatchNo();
				if (batchNo != null) {
					orderDetail.setBatchNo(batchNo);
				}
				totalNum += item.getNum();
				detailDao.save(orderDetail);
				lineNo++;
			} catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				logger.error("Method:addMaterial orderNo:{}", orderNo, e);
				throw new ServiceException(e);
			} finally {
				idGenService.unlock(ID_ORDERDETAILID);
				idGenService.unlock(ID_CARTDETAIL);
			}
		}
		return totalNum * suitNum;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Order> findByOrderNo(Collection<String> orderNos) throws ServiceException {
		return orderDao.findByOrderNo(orderNos);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Order> findByCredentials(OrderCredentials credentials)
			throws ServiceException {
		List<String> orderNos = credentials.getOrderNos();
		try {
			Assert.notEmpty(orderNos, "凭证里的单号集合为空");
			return findByOrderNo(orderNos);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> findPayOrders(String counterCode, Collection<String> orderStatus,
			Long start, Long end) throws ServiceException {
		if (orderStatus.contains(Order.Status_UnPay)//
				|| orderStatus.contains(Order.status_CompleteShipping)//
				|| orderStatus.contains(Order.status_ForFinancialRefuse)) {
			throw new ServiceException("订单状态不符");
		}
		return orderDao.findPayOrders(counterCode, orderStatus, start, end);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> findPayOrders(Collection<String> orderStatus, Long start, Long end)
			throws ServiceException {
		if (orderStatus.contains(Order.Status_UnPay)//
				|| orderStatus.contains(Order.status_CompleteShipping)//
				|| orderStatus.contains(Order.status_ForFinancialRefuse)) {
			throw new ServiceException("订单状态不符");
		}
		return orderDao.findPayOrders(orderStatus, start, end);
	}

	@Override
	@Transactional(readOnly = true)
	public Double getProductAmt(String counterCode, Collection<String> orderStatus,
			Collection<Integer> ProductId, Long start, Long end) throws ServiceException {
		if (orderStatus.contains(Order.Status_UnPay)//
				|| orderStatus.contains(Order.status_CompleteShipping)//
				|| orderStatus.contains(Order.status_ForFinancialRefuse)) {
			throw new ServiceException("订单状态不符");
		}
		Double price = 0.0;
		try {
			List<Order> orders = orderDao.findPayOrders(counterCode, orderStatus, start, end);
			List<String> orderNos = new ArrayList<>(orders.size());
			for (Order order : orders) {
				orderNos.add(order.getOrderNo());
			}
			price = detailDao.getPriceByProductId(orderNos, ProductId);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return price;
	}

	@Override
	@Transactional(readOnly = true)
	public Double getUsefulActFeeInOrder(Counter counter) throws ServiceException {
		ActivityManager activityManager = AppContext.getBean(ActivityManager.class);
		// FIXME:目前通过柜台获取活动的只有一种
		List<Activity> myActivity = activityManager.findMyActivities(counter);
		if (myActivity.isEmpty()) {
			return 0.0;
		}
		// 活动起始时间
		Long start = myActivity.get(0).getStart().getTime();
		// 活动大货id集合
		Set<Integer> ProductId = myActivity.get(0).getContext()//
				.getActityGoods().getProductItem().keySet();
		// 以支付以及后续订单状态
		List<String> orderStatus = new ArrayList<>();
		orderStatus.add(Order.Status_DoPay);
		orderStatus.add(Order.status_LogisticsDelivery);
		orderStatus.add(Order.status_DoLogisticsDelivery);
		orderStatus.add(Order.status_WaitShip);

		Double orderPrice = 0.0;
		Double orderUsedFee = 0.0;
		final ActivityManager actManager = AppContext.getBean(ActivityManager.class);
		try {
			List<Order> orders = orderDao.findPayOrders(counter.getCounterCode(), //
					orderStatus, start, System.currentTimeMillis());
			List<String> orderNos = new ArrayList<>(orders.size());

			for (Order order : orders) {
				// 所有订单
				orderNos.add(order.getOrderNo());
				// 订单占用的金额
				if (!order.getActivRecord().isEmpty()) {
					for (String actId : order.getActivRecord()) {
						Activity activity = actManager.get(actId);
						Double amt = activity.getContext().getAmount().getAmount();
						orderUsedFee += amt;
					}
				}
			}
			// 订单内累计金额
			if (orderNos != null && !orderNos.isEmpty()) {
				orderPrice = detailDao.getPriceByProductId(orderNos, ProductId);
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return orderPrice - orderUsedFee;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Activity> getSuitableActId(Counter counter, Double usefulActFee)
			throws ServiceException {
		// 获取活动
		ActivityManager activityManager = AppContext.getBean(ActivityManager.class);
		List<Activity> myActivity = activityManager.findMyActivities(counter);
		// <amt,actid>
		// 根据金额排降序
		Map<Double, String> levelMap = new TreeMap<Double, String>(new Comparator<Double>() {
			public int compare(Double obj1, Double obj2) {
				return obj2.compareTo(obj1);
			}
		});
		for (Activity activity : myActivity) {
			Double amt = activity.getContext().getAmount().getAmount();
			levelMap.put(amt, activity.getActId());
		}
		// 满足的活动id
		List<Activity> actIds = new ArrayList<>();
		for (Double amt : levelMap.keySet()) {
			int num = (int) (usefulActFee / amt);
			// 不满足，下一档次
			if (num < 1) {
				continue;
			}
			for (int i = 0; i < num; i++) {
				String actId = levelMap.get(amt);
				actIds.add(activityManager.get(actId));
			}
			usefulActFee -= amt * num;
		}
		return actIds;
	}

	@Override
	@Transactional
	public void addActivityToOrder(String orderNo, Counter counter) throws ServiceException {
		Double usefulActFee = getUsefulActFeeInOrder(counter);
		List<Activity> activities = getSuitableActId(counter, usefulActFee);
		// <活动id,套数>
		Map<String, Integer> suitMap = new HashMap<>();
		// <活动id,活动>
		Map<String, List<Item>> IdMap = new HashMap<>();
		// 活动id记录
		List<String> activRecord = new ArrayList<>();
		// 装配Map
		for (Activity activity : activities) {
			String actId = activity.getActId();
			// 订单中记录活动
			activRecord.add(actId);
			// 装配活动 对应套数
			if (!suitMap.containsKey(actId)) {
				suitMap.put(actId, 0);
			}
			suitMap.put(actId, suitMap.get(actId) + 1);
			// 装配活动Item ProductItem->Itme
			// ActityExtras数量一般为1 循环次数为n*1*m

			if (IdMap.containsKey(actId)) {
				continue;
			} else {
				IdMap.put(actId, new ArrayList<>());
			}
			for (ProductRule productRule : activity.getContext().getActityExtras()) {
				for (ProductItem productItem : productRule.getProductItem().values()) {
					// if (!IdMap.containsKey(actId)) {
					// IdMap.put(actId, new ArrayList<>());
					// }
					Item item = new Item(productItem.getPid().toString(), //
							productItem.getQuantity(), //
							productItem.getPrice(), //
							OrdmConfig.DELIVERYWAY_MARKETPRESENT, //
							actId);

					IdMap.get(actId).add(item);
				}
			}
		}
		// 添加
		try {
			Order order = orderDao.findByOrderNo(orderNo);
			Integer totalNum = order.getTotalNum();
			Integer materialNum = 0;
			for (String actId : suitMap.keySet()) {
				List<Item> materials = IdMap.get(actId);
				materialNum += addMaterial(order, materials, suitMap.get(actId));
			}
			order.setTotalNum(totalNum + materialNum);
			order.setActivRecord(activRecord);
			updateOrder(order);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> findActivityOrder() throws ServiceException {
		return orderDao.findActivityOrder();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> getOrders(Collection<String> orderStatus) throws ServiceException {
		return orderDao.findByType(orderStatus);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> findOrders(String customerId, Collection<String> orderStatus, Long start,
			Long end) throws DaoException {
		return orderDao.findOrders(customerId, orderStatus, start, end);
	}

	@Override
	@Transactional
	public void addReserveRecord(String orderNo) throws ServiceException {
		try {
			Order order = getsingleOrder(orderNo);
			String counterCode = order.getCounterCode();
			// 获取所有可以随单发的
			List<ReserveRecord> records = reserveRecordDao.findByCounterCodeAndStatus(counterCode,
					ReserveRecord.STATUS_WAIT);
			if (records == null || records.isEmpty()) {
				return;
			}
			order = addReserveRecordToOrder(order, records, 1);

			updateOrder(order);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Method:addReserveRecord orderNo:{}", orderNo, e);
			throw new ServiceException(e);
		}

	}

	private Order addReserveRecordToOrder(Order order, Collection<ReserveRecord> records,
			Integer suitNum) throws ServiceException {
		final ProductManager productManager = AppContext.getBean(ProductManager.class);
		final DictManager dictManager = AppContext.getBean(DictManager.class);

		// 追加产品的总数
		Integer totalNum = 0;
		String orderNo = order.getOrderNo();
		double rOweAmt = order.getrOweAmt();
		double yOweAmt = order.getyOweAmt();
		List<OrderDetail> details = getOrderDetails(orderNo);
		// 初始化明细行号 (如果没有订单明细则为1)
		int lineNo = 1;

		if (details != null && !details.isEmpty()) {
			// 最后一条明细
			OrderDetail lastDetail = details.stream()
					.max(Comparator.comparing(d -> d.getLineNumber())).get();
			lineNo = lastDetail.getLineNumber() + 1;
		}

		for (ReserveRecord record : records) {
			Product product = productManager.get(TypeConverter.toInteger(record.getProductId()));
			OrderDetail orderDetail = new OrderDetail(product, orderNo, record.getQuantity()
					* suitNum);
			Dictionary deliveryWay = dictManager.lookUpByCode(DELIVERYWAY,
					OrdmConfig.DELIVERYWAY_SALES);

			try {
				idGenService.lock(ID_ORDERDETAILID);
				idGenService.lock(ID_CARTDETAIL);
				orderDetail.setId(Long.parseLong(idGenService.generateDateId((ID_ORDERDETAILID))));
				orderDetail.setLineNumber(lineNo);
				// 会员价
				if (productManager.isAuthenticProduct(product.getProductId())) {
					orderDetail.setMemberPrice(product.getMemberPrice());
				} else {
					orderDetail.setMemberPrice(product.getMaterialPrice());
				}
				// 单价
				// FIX ME:检查还欠产品金额 会员价X折扣
				orderDetail.setUnitPrice(record.getUnitPrice());
				orderDetail.setGiftPrice(record.getGiftPrice());
				// 这个pid只是为了前台的展示 表示 几件产品是一个组合包
				Long cartsId = Long.parseLong(idGenService.generateDateId(ID_CARTDETAIL));
				orderDetail.setPid(cartsId);
				// 行号
				orderDetail.setLineNumber(lineNo);
				// 发货方式
				String deliveryWayId = record.getDeliveryWayId();
				if (deliveryWayId != null) {
					orderDetail.setDeliveryWayId(deliveryWayId);
					orderDetail.setDeliveryWayName(record.getDeliveryWayName());
				} else {
					orderDetail.setDeliveryWayId(deliveryWay.getHardCode());
					orderDetail.setDeliveryWayName(deliveryWay.getName());
				}
				// 套数
				orderDetail.setSuitNumber(suitNum);
				orderDetail.setdetailType(OrderDetail.TYPE_DONE);
				// 免费数量
				orderDetail.setFreeQty(record.getFreeQty());
				orderDetail.setCostRatio(record.getCostRatio());
				orderDetail.setRevId(record.getRevId());
				String batchNo = record.getBatchNo();
				if (batchNo != null) {
					orderDetail.setBatchNo(batchNo);
				}
				totalNum += record.getQuantity();
				detailDao.save(orderDetail);
				record.setStatus(ReserveRecord.STATUS_DONE);
				record.setExcuteOrderNo(orderNo);
				reserveRecordDao.update(record);
				double amt = record.getUnitPrice() * (record.getQuantity() - record.getFreeQty());
				// 预订会
				if (record.getRevId() != null) {
					rOweAmt += amt;
				} else {
					yOweAmt += amt;
				}
				lineNo++;
			} catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				logger.error("Method:addReserveRecordToOrder order:{},");
				throw new ServiceException(e);
			} finally {
				idGenService.unlock(ID_ORDERDETAILID);
				idGenService.unlock(ID_CARTDETAIL);
			}
		}
		int num = order.getTotalNum() + totalNum * suitNum;
		order.setTotalNum(num);
		order.setrOweAmt(rOweAmt);
		order.setyOweAmt(yOweAmt);
		return order;
	}

	@Override
	@Transactional
	public void saveReserveRecord(String orderNo) throws ServiceException {
		final ReserveProductManager reserveProductManager = AppContext
				.getBean(ReserveProductManager.class);
		final StockService stockService = AppContext.getBean(StockService.class);
		final ActivityManager activityManager = AppContext.getBean(ActivityManager.class);

		try {
			Order order = getsingleOrder(orderNo);
			if (order.getOrderPayTime() == null) {
				order.setOrderPayTime(System.currentTimeMillis());
			}
			Counter counter = counterManager.getCounterByCode(order.getCounterCode());
			String wid = counter.getWarehouses();
			Long payTime = order.getOrderPayTime();
			Assert.notNull(wid, "生成打欠记录仓库id不能为空");
			// 检查本单明细生成预订记录
			List<OrderDetail> orderDetails = detailDao.getOrderDetails(orderNo);
			// 1. 装配分组：活动放一起
			Map<Long, List<OrderDetail>> datailMap = new HashMap<>();
			for (OrderDetail orderDetail : orderDetails) {
				// 单品
				if (orderDetail.getActivityId() == null) {
					Long detailId = orderDetail.getId();
					if (!datailMap.containsKey(detailId)) {
						datailMap.put(detailId, new ArrayList<>());
					}
					datailMap.get(detailId).add(orderDetail);
					// 活动
				} else {
					Long pid = orderDetail.getPid();
					if (!datailMap.containsKey(pid)) {
						datailMap.put(pid, new ArrayList<>());
					}
					datailMap.get(pid).add(orderDetail);
				}
			}

			// 2. 生成打欠记录
			for (Long id : datailMap.keySet()) {
				List<OrderDetail> details = datailMap.get(id);
				String activityId = details.get(0).getActivityId();
				// 单品
				if (activityId == null) {
					OrderDetail detail = details.get(0);
					Integer productId = detail.getProductId();
					// 订单明细是打欠且 产品是打欠中
					if (OrderDetail.TYPE_RESERVE == detail.getDetailType()//
							&& reserveProductManager.isReserving(productId, payTime, wid)) {
						int qty = stockService.getStockProductQty(wid, productId.toString());
						int num = detail.getQuantity();
						// 加入购物车的时候是打欠产品 但是实际有库存 生成包裹时换欠
						if (qty >= num) {
							detail.setdetailType(OrderDetail.TYPE_RESERVE_THEN_DONE);
							detailDao.update(detail);
						} else {
							// 加入购物车的时候是打欠产品 生成包裹时没库存 需要生成打欠记录
							ReserveProduct reserveProduct = reserveProductManager.get(productId);
							ReserveRecord record = new ReserveRecord(detail, counter,
									reserveProduct);
							reserveRecordDao.save(record);
						}
					}
				}
				// 活动
				if (activityId != null) {
					// 活动库存不足且可以打欠
					Activity activity = activityManager.get(activityId);
					// 打欠有效性检查
					boolean revFlag = activity.getContext().isReserving(wid, payTime);
					// 全打欠活动
					if (revFlag) {
						// 活动明细库存全部充足
						boolean StockFlag = true;
						for (OrderDetail detail : details) {
							Integer productId = detail.getProductId();
							Integer num = detail.getQuantity();

							Integer qty = stockService
									.getStockProductQty(wid, productId.toString());
							if (qty < num) {
								// 一个库存不不足则活动不满足
								StockFlag = false;
							}
						}
						OrderDetail firstDetail = details.get(0);
						// 库存不足生成打欠记录
						if (!StockFlag//
								&& OrderDetail.TYPE_RESERVE == (firstDetail.getDetailType())) {
							for (OrderDetail detail : details) {
								ReserveRecord record = new ReserveRecord(detail, counter, activity);
								reserveRecordDao.save(record);
							}
						}
						// 库存充足修改打欠标志 正常发货
						if (StockFlag//
								&& OrderDetail.TYPE_RESERVE == (firstDetail.getDetailType())) {
							for (OrderDetail detail : details) {
								detail.setdetailType(OrderDetail.TYPE_BUY);
								detailDao.update(detail);
							}
						}
						// 活动部分商品打欠(大礼包等部分打欠 因为免费可取消的 )
					} else {
						for (OrderDetail detail : details) {
							// 库存不足生成打欠记录
							if (OrderDetail.TYPE_RESERVE != (detail.getDetailType())) {
								continue;
							}
							Integer productId = detail.getProductId();
							Integer num = detail.getQuantity();
							Integer qty = stockService
									.getStockProductQty(wid, productId.toString());

							if (qty >= num) {
								detail.setdetailType(OrderDetail.TYPE_RESERVE_THEN_DONE);
								detailDao.update(detail);
							} else {
								// 加入购物车的时候是打欠产品 生成包裹时没库存 需要生成打欠记录
								ReserveProduct reserveProduct = reserveProductManager
										.get(productId);
								ReserveRecord record = new ReserveRecord(detail, counter,
										reserveProduct);
								reserveRecordDao.save(record);
							}
						}
					}
				}
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Method:saveReserveRecord , orderNo:{}", orderNo, e);
			throw new ServiceException(e);
		}
	}

	/**
	 * 作废订单，更新订单状态，记录作废原因
	 */
	@Override
	public void refuseOrder(Order order) throws ServiceException {
		try {
			update(order);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void AddAutoBigPackage(Order order) throws ServiceException {
		final ActivityManager activityManager = AppContext.getBean(ActivityManager.class);
		final StockService stockService = AppContext.getBean(StockService.class);
		final CounterManager counterManager = AppContext.getBean(CounterManager.class);
		final ReserveProductManager reserveProductManager = AppContext
				.getBean(ReserveProductManager.class);
		final PricePolicyManager pricePolicyManager = AppContext.getBean(PricePolicyManager.class);
		Counter counter = counterManager.getCounterByCode(order.getCounterCode());
		Customer customer = customerManager.get(counter.getCustomerId());
		String wid = counter.getWarehouses();
		Assert.notNull(wid, "自动加入大礼包校验库存仓库为空");

		CustomerPricePolicy pricePolicy = pricePolicyManager
				.getPolicy(customer.getCustomerTypeID());
		double materielDiscount = pricePolicy.getMaterialDiscount();
		// 直营4折
		Double pruoductFee = order.getProductFee();
		if (Counter.Type_Direct.equals(counter.getType())) {
			pruoductFee = pruoductFee * materielDiscount;
		}
		List<OrderDetail> details = detailDao.getOrderDetails(order.getOrderNo());

		List<Activity> bigpackages = activityManager.findByType(ActivityType.TYPE_BIGPACAKGE);

		Comparator<Activity> cmp = (a, b) -> {
			double v = b.getContext().getAmount().getAmount()
					- a.getContext().getAmount().getAmount();
			return v > 0 ? 1 : (v > -0.001) ? 0 : -1;
		};

		Collections.sort(bigpackages, cmp);
		// FIXME:目前只有一个大礼包 金额规则参与计算的产品一样 如果多个礼包 产于计算的产品不一样则无法 进行累计计算
		double userfulAmt = -1;
		for (Activity activity : bigpackages) {
			// 2018/01/27 Wutq ADD 直营礼包计算错误修正 Start
			userfulAmt = -1;
			// 2018/01/27 Wutq ADD 直营礼包计算错误修正 End
			if (!activity.validCheck(counter)) {
				continue;
			}
			String actid = activity.getActId();

			// 自选大礼包跳过
			if (!activity.isAutoAlloca()) {
				continue;
			}
			ProductRule productRule = activity.getContext().getActityExtra();
			// 赠品为空
			if (productRule == null) {
				continue;
			}
			Double packageAmt = activity.getContext().getAmount().getAmount();
			// 产品总价小于礼包金额肯定不满足
			if (pruoductFee < packageAmt) {
				continue;
			}
			// 初始化实际参加活动累计可用金额
			if (userfulAmt < 0) {
				double amt = 0.0;
				AmountRule amountRule = activity.getContext().getAmount();
				Assert.notNull(amountRule, "大礼包活动金额规则不能为空");

				// 不参与金额计算的产品
				Set<Integer> filterPro = amountRule.getFilterPro();
				// 与金额计算的产品
				Set<Integer> containPro = amountRule.getContainPro();
				// 不参与金额计算的活动
				Set<String> filterAct = amountRule.getFilterAct();
				// 与金额计算的活动
				Set<String> containAct = amountRule.getContainAct();
				// 减法计算 初始化金额为付费产品金额
				if (!filterAct.isEmpty() || !filterPro.isEmpty()) {
					amt = pruoductFee;
				}
				if (filterAct.isEmpty() //
						&& filterPro.isEmpty() //
						&& containPro.isEmpty() //
						&& containAct.isEmpty()) {
					amt = pruoductFee;
				}
				// 已经参加的活动
				for (OrderDetail detail : details) {
					String activityId = detail.getActivityId();
					Integer productId = detail.getProductId();
					double detailAmt = detail.getUnitPrice() * detail.getQuantity();

					if (Counter.Type_Direct.equals(counter.getType())) {
						detailAmt = detailAmt * materielDiscount;
					}

					// 单品
					if (activityId == null) {
						// 参与计算的产品不为空 加法
						if (!containPro.isEmpty()//
								&& containPro.contains(productId)) {
							amt += detailAmt;
						}
						// 参与计算的产品不为空 减法
						if (!filterPro.isEmpty()//
								&& filterPro.contains(productId)) {
							amt -= detailAmt;
						}
					} else {
						// 活动
						if (!containAct.isEmpty()//
								&& containAct.contains(activityId)) {
							amt += detailAmt;
						}
						// 参与计算的产品不为空 减法
						if (!filterAct.isEmpty()//
								&& filterAct.contains(activityId)) {
							amt -= detailAmt;
						}
					}
				}
				userfulAmt = amt;
			}

			int suitNum = (int) (userfulAmt / packageAmt);
			// 2018/01/27 Wutq ADD 直营礼包计算错误修正 Start
			if (suitNum <= 0) {
				continue;
			}
			// 2018/01/27 Wutq ADD 直营礼包计算错误修正 End
			// 库存校验 决定是否加入订单 生成包裹前校验库存决定是否生成打欠记录
			boolean StockFlag = true;
			for (ProductItem item : productRule.getProductItem().values()) {
				Integer productId = item.getPid();
				Integer num = item.getQuantity();
				// 大礼包里产品打欠不校验库存
				if (reserveProductManager.isReserving(productId, wid)) {
					continue;
				}
				Integer qty = stockService.getStockProductQty(wid, productId.toString());
				int suit = qty / num;
				if (suit > 0 && suit < suitNum) {
					suitNum = suit;
				}
				if (qty < num) {
					// 一个库存不不足则活动不满足
					StockFlag = false;
				}
			}

			if (!StockFlag) {
				continue;
			}
			// 装配大礼包赠品
			List<Item> items = new ArrayList<>();
			for (ProductItem productItem : productRule.getProductItem().values()) {
				Item item = new Item(productItem.getPid().toString(), //
						productItem.getQuantity(), //
						productItem.getPrice(), //
						OrdmConfig.DELIVERYWAY_MARKETPRESENT, //
						actid);
				if (reserveProductManager.isReserving(productItem.getPid(), wid)) {
					String batchNo = reserveProductManager.get(productItem.getPid()).getBatchNo();
					item.setBatchNo(batchNo);
					item.setDetalType(OrderDetail.TYPE_RESERVE);
				}
				items.add(item);
			}
			// 大礼包加到订单中
			Integer totalNum = order.getTotalNum();
			Integer materialNum = addMaterial(order, items, suitNum);
			order.setTotalNum(totalNum + materialNum);
			updateOrder(order);
		}
	}

	@Override
	public Map<Activity, Integer> getAutoBigPackage(Cart cart) throws ServiceException {
		final ActivityManager activityManager = AppContext.getBean(ActivityManager.class);
		final StockService stockService = AppContext.getBean(StockService.class);
		final CounterManager counterManager = AppContext.getBean(CounterManager.class);
		final CustomerManager customerManager = AppContext.getBean(CustomerManager.class);
		final ReserveProductManager reserveProductManager = AppContext
				.getBean(ReserveProductManager.class);
		final PricePolicyManager pricePolicyManager = AppContext.getBean(PricePolicyManager.class);

		Counter counter = counterManager.get(cart.getCounterId());
		Customer customer = customerManager.get(counter.getCustomerId());
		// 用户类型价格运算的折扣率
		CustomerPricePolicy pricePolicy = pricePolicyManager
				.getPolicy(customer.getCustomerTypeID());
		double discount = pricePolicy.getDiscount();
		String wid = counter.getWarehouses();
		Assert.notNull(wid, "自动加入大礼包校验库存仓库为空");

		List<CartDetail> details = cartDetailDao.getCartDetailByCart(cart);

		if (Counter.Type_Direct.equals(counter.getType())) {
			discount = pricePolicy.getMaterialDiscount();
		}

		Double pruoductFee = getRealPrice(details, discount);

		List<Activity> bigpackages = activityManager.findByType(ActivityType.TYPE_BIGPACAKGE);

		Comparator<Activity> cmp = (a, b) -> {
			double v = b.getContext().getAmount().getAmount()
					- a.getContext().getAmount().getAmount();
			return v > 0 ? 1 : (v > -0.001) ? 0 : -1;
		};

		Collections.sort(bigpackages, cmp);
		// FIXME:目前只有一个大礼包 金额规则参与计算的产品一样 如果多个礼包 产于计算的产品不一样则无法 进行累计计算
		double userfulAmt = -1;
		Map<Activity, Integer> activityMap = new HashMap<>();
		for (Activity activity : bigpackages) {
			// 2018/01/27 Wutq ADD 直营礼包计算错误修正 Start
			userfulAmt = -1;
			// 2018/01/27 Wutq ADD 直营礼包计算错误修正 End
			if (!activity.validCheck(counter)) {
				continue;
			}
			// 自选大礼包跳过
			if (!activity.isAutoAlloca()) {
				continue;
			}
			ProductRule productRule = activity.getContext().getActityExtra();
			// 赠品为空
			if (productRule == null) {
				continue;
			}
			Double packageAmt = activity.getContext().getAmount().getAmount();
			// 产品总价小于礼包金额肯定不满足
			if (pruoductFee < packageAmt) {
				continue;
			}
			// 初始化实际参加活动累计可用金额
			if (userfulAmt < 0) {
				double amt = 0.0;
				AmountRule amountRule = activity.getContext().getAmount();
				Assert.notNull(amountRule, "大礼包活动金额规则不能为空");

				// 不参与金额计算的产品
				Set<Integer> filterPro = amountRule.getFilterPro();
				// 与金额计算的产品
				Set<Integer> containPro = amountRule.getContainPro();
				// 不参与金额计算的活动
				Set<String> filterAct = amountRule.getFilterAct();
				// 与金额计算的活动
				Set<String> containAct = amountRule.getContainAct();
				// 减法计算 初始化金额为付费产品金额
				if (!filterAct.isEmpty() || !filterPro.isEmpty()) {
					amt = pruoductFee;
				}
				if (filterAct.isEmpty() //
						&& filterPro.isEmpty() //
						&& containPro.isEmpty() //
						&& containAct.isEmpty()) {
					amt = pruoductFee;
				}
				// 已经参加的活动
				for (CartDetail detail : details) {
					String activityId = detail.getActivityId();
					Integer productId = detail.getCartDetailsDesc().get(0).getProductId();
					double detailAmt = detail.getPrice() * detail.getQuantity() * discount;

					// 单品
					if (activityId == null) {
						// 参与计算的产品不为空 加法
						if (!containPro.isEmpty()//
								&& containPro.contains(productId)) {
							amt += detailAmt;
						}
						// 参与计算的产品不为空 减法
						if (!filterPro.isEmpty()//
								&& filterPro.contains(productId)) {
							amt -= detailAmt;
						}
					} else {
						// 活动
						if (!containAct.isEmpty()//
								&& containAct.contains(activityId)) {
							amt += detailAmt;
						}
						// 参与计算的产品不为空 减法
						if (!filterAct.isEmpty()//
								&& filterAct.contains(activityId)) {
							amt -= detailAmt;
						}
					}
				}
				userfulAmt = amt;
			}

			int suitNum = (int) (userfulAmt / packageAmt);
			// 2018/01/27 Wutq ADD 直营礼包计算错误修正 Start
			if (suitNum <= 0) {
				continue;
			}
			// 2018/01/27 Wutq ADD 直营礼包计算错误修正 End
			// 累积金额赠送套数不能超过门店数的场合
			if (activity.getContext().getAmount().getType() == AmountRule.TYPE_ONLYONE) {
				suitNum = Math.min(suitNum, customer.getCounters().size());
			}
			// 库存校验 决定是否加入订单 生成包裹前校验库存决定是否生成打欠记录
			boolean StockFlag = true;
			for (ProductItem item : productRule.getProductItem().values()) {
				Integer productId = item.getPid();
				Integer num = item.getQuantity();
				// 大礼包里产品打欠不校验库存
				if (reserveProductManager.isReserving(productId, wid)) {
					continue;
				}
				Integer qty = stockService.getStockProductQty(wid, productId.toString());
				int suit = qty / num;
				if (suit > 0 && suit < suitNum) {
					suitNum = suit;
				}
				if (qty < num) {
					// 一个库存不不足则活动不满足
					StockFlag = false;
				}
			}

			if (!StockFlag) {
				continue;
			}
			activityMap.put(activity, suitNum);
		}
		return activityMap;
	}

	/**
	 * 可以追加合并礼盒的订单,同一个订单追加
	 * 
	 * @return
	 */
	@SuppressWarnings("null")
	@Override
	public Map<Double, Order> canAddToOrder(List<Integer> counterIds) throws ServiceException {
		Map<Double, Order> return_map = null;
		try {
			// 过滤合并订单
			List<Order> orders = this.getOrders(counterIds, Order.Status_UnPay);
			// orderNo -> order
			Map<String, Order> allOrdersMap = new HashMap<>();
			for (Order order : orders) {
				allOrdersMap.put(order.getOrderNo(), order);
			}

			if (orders.size() < 0) {
				//
				return null;
			}
			// 大于两单的Ids 默认只有第一个非礼盒订单
			List<Integer> counter_ids = new ArrayList<>();
			// <CounterId , Orders>
			Map<Integer, Set<String>> temp_map = new HashedMap<>();
			for (Order order : orders) {
				//
				Integer counterId = order.getCounterId();
				if (temp_map.get(counterId).isEmpty()) {
					Set<String> m = new HashSet<>();
					m.add(order.getOrderNo());
					temp_map.put(counterId, m);
					continue;
				}
				temp_map.get(counterId).add(order.getOrderNo());
				// 默认只有一个礼盒
				if (!counter_ids.contains(counterId)) {
					counter_ids.add(counterId);
				}
			}
			// orderNo 对应
			double[] amt = new double[counter_ids.size()];
			Map<String, Integer> order_amt = new HashMap<>();
			for (int i = 0; i < counter_ids.size(); i++) {
				Set<String> order_tmp = temp_map.get(counter_ids.get(i));
				for (String orderNo : order_tmp) {
					//
					amt[i] = 0d;
					if (allOrdersMap.get(orderNo).getIssysin() == Order.PRESENT_TYPE_NORMAL) {
						order_amt.put(orderNo, i);
						continue;
					}
					// 礼盒应付金额
					amt[i] = allOrdersMap.get(orderNo).getPaymentFee();
				}
			}
			for (String no : order_amt.keySet()) {
				Integer index = order_amt.get(no);
				return_map.put(amt[index], allOrdersMap.get(no));
			}

		} catch (Exception e) {
			logger.error("显示合并订单错误,counterIds:{}", counterIds);
			throw new ServiceException(e);
		}
		return return_map;
	}

	@Override
	public Map<Activity, Integer> getAutoBigPackage(List<Order> orders) throws ServiceException {
		final ActivityManager activityManager = AppContext.getBean(ActivityManager.class);
		final CounterManager counterManager = AppContext.getBean(CounterManager.class);
		final PricePolicyManager pricePolicyManager = AppContext.getBean(PricePolicyManager.class);

		// 获取有效自动大礼包
		List<Activity> bigpackages = activityManager.findByType(ActivityType.TYPE_BIGPACAKGE);

		Comparator<Activity> cmp = (a, b) -> {
			double v = b.getContext().getAmount().getAmount()
					- a.getContext().getAmount().getAmount();
			return v > 0 ? 1 : (Math.abs(v) < 0.0001) ? 0 : -1;
		};

		bigpackages = bigpackages.stream().filter(e -> e.isAutoAlloca() //
				&& e.getContext().getActityExtra() != null //
				&& e.getContext().getAmount().getAmount() > 0.0) //
				.sorted(cmp) //
				.collect(Collectors.toList());
		// FIX: 对流排序处理收集
		// Collections.sort(bigpackages, cmp);

		Map<Activity, Integer> activityMap = new HashMap<>();
		for (Activity activity : bigpackages) {
			// 记录礼包有效累积金额
			double totalEffectAmount = 0.0;
			Double packageAmt = activity.getContext().getAmount().getAmount();
			AmountRule amountRule = activity.getContext().getAmount();
			Assert.notNull(amountRule, "大礼包活动金额规则不能为空");
			for (Order order : orders) {
				// 记录单个礼包有效累积金额
				double orderEffectAmount = 0.0;
				// 有效校验
				Counter counter = counterManager.get(order.getCounterId());
				Customer customer = customerManager.get(counter.getCustomerId());
				// 取WMS rdcID#
				String wid = counter.getWarehouses();
				Assert.notNull(wid, "自动加入大礼包校验库存仓库为空");
				// 用户类型价格运算的折扣率
				CustomerPricePolicy pricePolicy = pricePolicyManager.getPolicy(customer
						.getCustomerTypeID());
				double discount = pricePolicy.getDiscount();

				if (!activity.validCheck(counter)) {
					continue;
				}

				// 计算累积金额
				List<OrderDetail> details = detailDao.getOrderDetails(order.getOrderNo());
				for (OrderDetail detail : details) {
					orderEffectAmount += getActivityEffectiveAmt(detail, amountRule);
				}
				if (Counter.Type_Direct.equals(counter.getType())) {
					orderEffectAmount = orderEffectAmount * discount;
				}
				totalEffectAmount += orderEffectAmount;
			}

			int suitNum = (int) (totalEffectAmount / packageAmt);
			if (suitNum <= 0) {
				continue;
			}
			// 累积金额赠送套数不能超过门店数的场合
			if (amountRule.getType() == AmountRule.TYPE_ONLYONE) {
				// 串？
				Integer custId = StringUtil.toInt(orders.get(0).getCustomerId());
				if (custId == null) {
					logger.error("XXX: 数据不合法，顾客ID不是整型串，ID＝{}", orders.get(0).getCustomerId());
					continue;
				}

				Customer customer = customerManager.get(custId);
				suitNum = Math.min(suitNum, customer.getCounters().size());
			}
			if (suitNum > 0) {
				activityMap.put(activity, suitNum);
			}
		}
		return activityMap;
	}

	@Override
	public void addBigPackageToOrder(Order order, String activityId, int num)
			throws ServiceException {
		final ActivityManager activityManager = AppContext.getBean(ActivityManager.class);
		final CounterManager counterManager = AppContext.getBean(CounterManager.class);
		final ReserveProductManager reserveProductManager = AppContext
				.getBean(ReserveProductManager.class);

		Counter counter = counterManager.getCounterByCode(order.getCounterCode());
		Assert.notNull(counter, "订单中的门店不能为空");
		String wid = counter.getWarehouses();
		Assert.notNull(wid, "自动加入大礼包校验库存仓库为空");

		Activity activity = activityManager.get(activityId);
		ProductRule productRule = activity.getContext().getActityExtra();
		// 装配大礼包赠品
		List<Item> items = new ArrayList<>();
		for (ProductItem productItem : productRule.getProductItem().values()) {
			Item item = new Item(productItem.getPid().toString(), //
					productItem.getQuantity(), //
					productItem.getPrice(), //
					OrdmConfig.DELIVERYWAY_MARKETPRESENT, //
					activityId);
			if (reserveProductManager.isReserving(productItem.getPid(), wid)) {
				String batchNo = reserveProductManager.get(productItem.getPid()).getBatchNo();
				item.setBatchNo(batchNo);
				item.setDetalType(OrderDetail.TYPE_RESERVE);
			}
			items.add(item);
		}
		// 大礼包加到订单中
		Integer totalNum = order.getTotalNum();
		Integer materialNum = addMaterial(order, items, num);
		order.setTotalNum(totalNum + materialNum);
		updateOrder(order);
	}

	/**
	 * 获取订单明细的有效金额
	 * 
	 * @param detail
	 * @param amountRule
	 * @return
	 */
	private double getActivityEffectiveAmt(OrderDetail detail, AmountRule amountRule) {

		Assert.notNull(amountRule, "大礼包活动金额规则不能为空");

		// 不参与金额计算的产品
		Set<Integer> filterPro = amountRule.getFilterPro();
		if (filterPro.contains(detail.getProductId())) {
			return 0.0;
		}

		// 与金额计算的产品
		Set<Integer> containPro = amountRule.getContainPro();

		// 不参与金额计算的活动
		Set<String> filterAct = amountRule.getFilterAct();
		// 与金额计算的活动
		Set<String> containAct = amountRule.getContainAct();

		// 已经参加的活动
		String activityId = detail.getActivityId();
		Integer productId = detail.getProductId();
		double detailAmt = detail.getUnitPrice() * detail.getQuantity();

		double amt = 0.0;
		// 单品
		if (activityId == null) {
			// 参与计算的活动为空
			if (containAct.isEmpty()) {
				// 参与计算的产品不为空,只累积参与计算的产品
				if (containPro.isEmpty() //
						|| containPro.contains(productId)) {
					amt = detailAmt;
				}
			}
		} else {
			// 参与计算的活动不为空，只累积参与计算的产品
			if (containAct.isEmpty() && filterAct.isEmpty() //
					|| containAct.contains(activityId) //
					|| !filterAct.contains(activityId)) {
				amt = detailAmt;
			}
		}

		return amt;
	}

	/**
	 * 获得合并订单之后产生的额外的大礼包
	 * 
	 * @param orders
	 */
	public Map<Activity, Integer> getMergeOtherBigPackage(List<Order> orders) {
		Map<Activity, Integer> allPackage = getAutoBigPackage(orders);
		for (Order order : orders) {
			Map<Activity, Integer> orderPackage = getAutoBigPackage(Arrays.asList(order));
			for (Activity activity : orderPackage.keySet()) {
				int allNum = allPackage.get(activity);
				int oneNum = orderPackage.get(activity);
				if (allNum == 0) {
					continue;
				}
				int otherNum = allNum - oneNum;
				if (otherNum <= 0) {
					allPackage.remove(activity);
				} else if (otherNum > 0) {
					allPackage.put(activity, allNum - oneNum);
				} else {
					logger.error("合并订单额外大礼包计算错误，{}活动出错", activity.getName());
				}

			}
		}
		return allPackage;
	}
}
