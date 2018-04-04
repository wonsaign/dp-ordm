package com.zeusas.dp.ordm.service;

import java.util.List;
import java.util.Map;

import com.zeusas.core.http.ActionException;
import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.CartDetailDesc;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Item;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.security.auth.entity.AuthUser;

public interface CartService extends IService<Cart, Long> {

	// FIXME:加入预定会活动时,根据预定会ID校验,修改所有ADDCART方法 以及 CartToOrder
	// 可能产品生成的情况 一,URL路径加入 ; 二,时间超时; 将预定会校验拿过来后使用.
	
	/** 基于门店创建一个购物车 */
	Cart createCounterCart(Counter counter) throws ServiceException;

	/**
	 * 获取门店的购物车
	 * 
	 * @throws ActionException
	 */
	Cart getCounterCart(Counter counter) throws ServiceException, ActionException;
 
	/** 拿到用户门店的所有的购物车 */
	List<Cart> findAllCart(List<Integer> counterIds) throws ServiceException;

	/** 获取所有待审核的基于门店的购物车 */
	List<Cart> getCheckCarts(List<Integer> counterIds) throws ServiceException;

	/*** 根据购物车拿到购物车的明细 */
	List<CartDetail> getCartDetailByCart(Cart cart) throws ServiceException;

	/** 根据购物车明细的集合拿到所有的明细的描述  */
	List<CartDetailDesc> getCartDescByCartDetail(List<CartDetail> cartDetails) throws ServiceException;

	/** 添加产品到购物车  */
	void add(Cart cart, Product product, int num) throws ServiceException;

	/** 批量加入单品到购物车 */
	void add(Cart cart, List<Item> items) throws ServiceException;

	/** * * 判断购物车的产品有没有要添加的Product 有就Update 没有就create（只针对单品） */
	CartDetail findCartDetail(Product product, Cart cart) throws ServiceException;

	/** * 添加一个活动组的所有的明细 */
	void add(Long cartId, String actId, List<Item> items, int num) throws ServiceException;

	/** 添加活动组(优惠活动)时判断Items是否存在购物车明细表里面了 */
	Long findItems(String actId, Long cartId, List<Item> insertItem) throws ServiceException;

	/** 删除购物车明细以及描述 fix525 */
	void deleteDetailAndDesc(Long detailId) throws ServiceException;
	
	/**删除购物车所有明细*/
	void deleteCart(Long cartId);

	/** 购物车的产品的数量进行更新操作 */
	boolean update(Long detailId, int num) throws ServiceException;

	/** 改变购物车的状态 */
	void changeCartStatus(Long cartId, int status) throws ServiceException;

	/** 提交购物车 */
	void commitCart(AuthUser user, Long cartId) throws ServiceException;
	/** 提交购物车 新活动 */
	@Deprecated
	void commitCart(AuthUser user, Long cartId,Double usefulActFee) throws ServiceException;

	/** 获取某个购物车里面的费比 */
	double getPresentFee(List<CartDetail> cartDetails)
			throws ServiceException;

	/** 获取该购物车的物料费用 */
	double getPresentFee(Cart cart) throws ServiceException;

	//XXX:物料费用1
	/** 获取购物车里面实际物料费用与可配送物料费用的差值 */
	double getMaterialFee(Cart cart, Integer customerTypeId) throws ServiceException;

	/** 比较购物车总金额的x%与所有物料赠品做比较 */
	double compareFeeAndPay(List<CartDetail> cartDetails, double discount) throws ServiceException;

	/**
	 * 获取购物车订单的实际金额 现在就要找到客户的组织机构拿到价格折扣 只是此时实际金额订货员是看不到的 当result<=0 说明没有超过费比
	 * 当result>0 超过的result需要计算到总价里面
	 */
	double getRealPrice(List<CartDetail> cartDetails, double discount);

	/**
	 * 获取购物车的总金额
	 */
	double getTotalFee(List<CartDetail> cartDetails);

	/**
	 * 获取所有产品、产品、物料和赠品的数量 int[0]--->总数 int[1]--->产品数量 int[2]--->物料和赠品的数量
	 * 
	 * @param cart
	 * @param pm
	 * @return
	 * @throws ServiceException
	 */
	Double[] getPAndMQty(Cart cart, ProductManager pm) throws ServiceException;

	/**
	 * 获取每个阶段对应的邮费
	 */
	Map<Integer, Double> getPostage() throws ServiceException;

	/** 获取购物车相应的数据   
	 * TODO 计划做成一个前端通过这个方法拿到所有的数据，包括正品、物料的金额以及数量等，减少数据库的交互*/
	public Map<String, Object> getCartData(Cart cartId, Integer customerTypeId)
			throws ServiceException;
	//XXX:物料费用2
	/** 获取免费的物料以及收费的物料 **/
	public Map<String, Double> getMaterialFee(List<CartDetail> cartDetails,
			 double discount);

	//XXX:物料费用3
	/** 获取购物车里面产品可支持的物料配比的总金额  如总金额=1W 
	 *  1W*discount 
	 *  */
	double getSupportdistributionPrice(List<CartDetail> cartDetails, double discount);
	
	/** 购物车审核不通过 */
	void refuse(Long cartId)throws ServiceException;
	
	/** 往购物车里添加活动正品*/
	@Deprecated
	public void addActivityToCart(Long cartId, Double usefulActFee) throws ServiceException;
	
	
	/** 审核购物时校验 里添加的活动赠品是否符合活动可用金额*/
	@Deprecated
	Boolean checkActivityInCart(Long cartId,Double usefulActFee)throws ServiceException;
	
	/**
	 * 获取可用于活动的累计消费金额
	 * 购物车提示用 计算包含购物内不的金额(未付款)
	 * 目前只有一个活动
	 */
	Double getUsefulActFeeInCart(Counter counter)throws ServiceException;
	/**
	 * 根据柜台集合获取已经提交购物车明细(用于统计一个客户所有购物里产品)
	 * @param counterIds
	 * @return
	 * @throws ServiceException
	 */
	List<CartDetail> getCartDetailByCounter(List<Integer> counterIds) throws ServiceException;
	/**
	 * 检查购物车明细预订会标记(排他性) 物料不检查
	 * @param cartDetails
	 * @return
	 */
	boolean  checkReserveActivity(List<CartDetail> cartDetails);
}
