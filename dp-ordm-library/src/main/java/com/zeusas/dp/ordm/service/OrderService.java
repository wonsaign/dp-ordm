package com.zeusas.dp.ordm.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.zeusas.core.dao.DaoException;
import com.zeusas.core.http.ActionException;
import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Item;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderCredentials;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.entity.PackageDetail;
import com.zeusas.security.auth.entity.AuthUser;

public interface OrderService extends IService<Order, Long> {
	
	// FIXME: 打欠发货处理  生成包裹时候->校验库存->无库存打欠
	//								  ->有库存直发
	
	/** 生成订单头 */
	Order buildOrder(Cart cart, Counter counter, AuthUser checkUser, AuthUser makeUser, Integer customerTypeId)
			throws ServiceException;

	/** 获取单个订单的明细 */
	List<OrderDetail> getOrderDetails(String orderNo) throws ServiceException;

	/** 用户根据订单状态获取获取自己所有门店id的订单 */
	List<Order> getOrders(List<Integer> counterIds, String orderStatus) throws ServiceException;

	/** 用户获取门店所有的订单 */
	List<Order> getOrdersByCounterId(Collection<Integer> counterIds) throws ServiceException;

	/** 查询订单的 */
	List<Order> findOrders(Conditions condition) throws ServiceException;

	List<Order> findOrders(String customerId, Collection<String> orderStatus, Long start, Long end) throws DaoException;

	/** 财务部、物流部审核获取全部的一状态类型订单 */
	List<Order> getOrders(String status) throws ServiceException;

	/** 根据状态查询 */
	List<Order> getOrders(Collection<String> orderStatus) throws ServiceException;

	/**
	 * 财务审核
	 * 
	 * @throws ServiceException
	 */
	void financialAudit(Order order, boolean flag) throws ServiceException;

	/** 改变订单的状态 */
	boolean changeOrderStatus(Long orderId, String orderStatus) throws ServiceException;

	/** 清空购物车 */
	void clearCart(Long cartID) throws ServiceException;

	/** 购物车审核不通过 */
	void refuse(Long cart) throws ServiceException;

	/** 根据单号获取单个订单 */
	Order getsingleOrder(String orderNo) throws ServiceException;

	// 确认完成订单*/
	void completeOrder(String orderNo) throws ServiceException;

	/** 根据订单最终金额来确定是否包邮 */
	int getPostage(double totalPrice);

	/** 更新一个订单的一些备注字段如实际支付金额、备注、审核备注等 */
	Order updateOrder(Order order) throws ServiceException;

	/** 根据订单和订单明细生成一个订单包裹和订单包裹的明细 (单个操作) */
	@Deprecated
	void bulidPackage(Order order, List<OrderDetail> orderDetails) throws ServiceException;

	void bulidPackagePlus(Order order, List<OrderDetail> orderDetails) throws ServiceException;
	
	/** 上传图片支付订单 */
	Order payOrder(Order order, AuthUser payMan) throws ServiceException;

	/** 确认支付 */
	Order confirmPay(Order order) throws ServiceException;

	/** 取消支付 */
	Order cancelPay(Order order) throws ServiceException;

	/** 支付宝支付 */
	Order payByAlipay(Order order, double totleFee) throws ServiceException;

	/** 直营店订货 */
	Order payByDirect(Order order, AuthUser user) throws ServiceException;

	List<PackageDetail> buildMaterialPack(Order order, List<OrderDetail> orderDetails) throws ServiceException;

	List<PackageDetail> buildProductPack(List<OrderDetail> orderDetails, ProductManager pm) throws ServiceException;

	void paySuccess(String orderNo, AuthUser payMan, List<String> imageUrl) throws ServiceException;

	/** 取消订单 */
	void cancelOrder(Order order) throws ServiceException;

	void userBalance(Long orderId, double usefulBalance, boolean flag) throws ServiceException;

	/** 全款余额订货 */
	Order payByBlance(Order order, AuthUser user, String counterType) throws ServiceException;

	/**
	 * 将一个订单还原成一个完成的购物车
	 * 
	 * @param order
	 *            一个完整的订单
	 * @throws ServiceException
	 * @throws ActionException
	 */
	void buildOrderToCart(Order order) throws ServiceException, ActionException;

	/** 合并订单 */
	OrderCredentials createCombinePayment(List<Order> orders) throws ServiceException;

	/**
	 * 合并订单支付，创建合并支付的凭证 只有确认的时候才会提交，其余剩下任何操作都不会产生凭证 保证凭证的唯一一次生成。
	 * 
	 */
	OrderCredentials payCombineOrder(List<Order> os, AuthUser payMan, Double actualPay, String description,
			List<String> combineImages) throws ServiceException;

	/** 合并订单使用余额 */
	void userBalance(List<Order> os, double usefulBalance, boolean flag) throws ServiceException;

	/** 确认合并支付 */
	void confirmCombineOrders(List<Order> os) throws ServiceException;

	/**
	 * 取消合并订单 Step1 设置每单Merger状态为false Step2 删除每个单独凭证里的合并ID Step3 删除合并订单
	 */
	void cancelCombineOrders(List<Order> os) throws ServiceException;

	/**
	 * 获取合并订单里的订单集合
	 * 
	 * @param os
	 * @return
	 */
	List<Order> getCombineOrders(List<Order> os) throws ServiceException;

	/**
	 * 拆解合并订单 删除凭证，去掉凭证里子凭证的父ID，还原每单的状态都为待付款 就是重置这种属性为以前的状态
	 */
	void dismantleCombineOrders(List<Order> os) throws ServiceException;

	/**
	 * 余额全额支付 controller变化，不需要上传截图了
	 */
	List<Order> balancePayCombineOrdersDirect(List<Order> os, AuthUser payMan, String description)
			throws ServiceException;

	/** 合并订单财务审核 */
	void financialAuditCombine(List<Order> os, boolean flag) throws ServiceException;

	/**
	 * 订单追加物料
	 * 
	 * @param orderNo
	 * @param materials
	 * @throws ServiceException
	 */
	public Order addFreeMaterial(String orderNo, Collection<Item> materials) throws ServiceException;

	public void addReserveRecord(String orderNo) throws ServiceException;

	/**
	 * 民生付
	 * 
	 * @param order
	 * @param totleFee
	 * @return
	 * @throws ServiceException
	 */
	Order payByCmbcpay(Order order, double totleFee) throws ServiceException;

	/**
	 * 根据单号集合获取订单
	 * 
	 * @param OrderNos
	 * @return
	 * @throws ServiceException
	 */
	Collection<Order> findByOrderNo(Collection<String> orderNos) throws ServiceException;

	/**
	 * 
	 * @param OrderNos
	 * @return
	 * @throws ServiceException
	 */
	Collection<Order> findByCredentials(OrderCredentials credentials) throws ServiceException;

	/**
	 * 获取指定时间内（start<支付时间<end） 指定柜台 指定状态（3 5 6 7）的订单
	 */
	List<Order> findPayOrders(String counterCode, Collection<String> orderStatus, Long start, Long end)
			throws ServiceException;

	/**
	 * 获取指定时间内（start<支付时间<end） 指定柜台 指定状态（3 5 6 7）的订单
	 */
	List<Order> findPayOrders(Collection<String> orderStatus, Long start, Long end) throws ServiceException;

	/**
	 * 获取指定时间内（start<支付时间<end） 指定柜台 指定状态（3 5 6 7）的订单内指定产品的金额
	 */
	Double getProductAmt(String counterCode, Collection<String> orderStatus, Collection<Integer> ProductId, Long start,
			Long end) throws ServiceException;

	/**
	 * 获取可用于活动的累计消费金额 购物车提示用 计算只有 3 5 6 7 的订单 目前只有一个活动
	 */
	Double getUsefulActFeeInOrder(Counter counter) throws ServiceException;

	/**
	 * 获取满足的活动
	 * 
	 * @param counter
	 * @param usefulActFee
	 * @return
	 * @throws ServiceException
	 */
	List<Activity> getSuitableActId(Counter counter, Double usefulActFee) throws ServiceException;

	/**
	 * 预定活动 物料
	 * 
	 * @param cartId
	 * @param usefulActFee
	 * @throws ServiceException
	 */
	public void addActivityToOrder(String orderNo, Counter counter) throws ServiceException;

	List<Order> findActivityOrder() throws ServiceException;
	
	public void saveReserveRecord(String orderNo)throws ServiceException;
	
	/**
	 * 作废订单
	 * @param order
	 * @throws ServiceException
	 */
	public void refuseOrder(Order order) throws ServiceException;
	
	/** 
	 * 获取满足的自动大礼包活动
	 * 
	 * @param cart 购物车
	 * @return 多个礼包及个数购成的MAP
	 * @throws ServiceException
	 */
	public Map<Activity, Integer> getAutoBigPackage(Cart cart) throws ServiceException;
	
	/**
	 *  自动把大礼包加到订单
	 * 
	 * @param order
	 * @throws ServiceException
	 * 
	 * 
	 */
	@Deprecated
	public void AddAutoBigPackage(Order order) throws ServiceException;

	Map<Double, Order> canAddToOrder(List<Integer> counterIds) throws ServiceException;

	double getMaterialFreeFee(List<CartDetail> cartDetails, double discount) throws ServiceException;
	
	/**
	 * 获取多个订单累积满足的自动大礼包活动的个数
	 * @param orders
	 * @return
	 * @throws ServiceException
	 * 
	 * @since 2.0.X 2018.03 订货会版本
	 */
	public Map<Activity, Integer> getAutoBigPackage(List<Order> orders) throws ServiceException;
	
	/**
	 * 添加指定数量的特定自动大礼包到指定订单
	 * @param order
	 * @param activityId
	 * @param num
	 * @throws ServiceException
	 * 
	 *  @since 2.0.X 2018.03 订货会版本
	 */
	public void addBigPackageToOrder(Order order, String activityId, int num) throws ServiceException;

	/**
	 * 获得合并订单之后产生的额外的大礼包
	 * 
	 * @param orders
	 * 
	 * @since 2.0.X 2018.03 订货会版本
	 */
	public Map<Activity, Integer> getMergeOtherBigPackage(List<Order> orders);
}

