package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.ReserveProduct;

public interface ReserveProductManager {

	void load();
	
	ReserveProduct get(Integer productId);
	
	List<ReserveProduct> findAll();
	
	void add(ReserveProduct reserveProduct) throws ServiceException;
	
	void update(ReserveProduct reserveProduct) throws ServiceException;
	/**
	 * 产品是否在预订中
	 * @param productId
	 * @return
	 */
	Boolean isReserving(Integer productId,String stockId);
	/**
	 * 产品是否可预订(状态是可以预订 还没变化为预订中 )
	 * @param productId
	 * @return
	 */
	Boolean isReservable(Integer productId,String stockId);
	/**
	 * 校验订单中产品（支付时间）是否可预订
	 * @param productId
	 * @return
	 */
	Boolean isReserving(Integer productId,Long payTime,String stockId);
	/**
	 * 是否是正常产品
	 * @param productId
	 * @return
	 */
	Boolean isSelling(Integer productId,String stockId);
	
	List<ReserveProduct> findByName(String name);
	
	void changeAvalible(Integer id,boolean avalible);

	/**
	 * 还欠完成后,重置该记录
	 * @param reserveProduct
	 */
	void reset(ReserveProduct reserveProduct);
	/**
	 * 更改打欠产品该仓库打欠状态
	 * @param status {@link ReserveProduct#STATUS_RESERVED}
	 * @param wid
	 */
	void changeStatus(Integer productId,Integer status,String wid)throws ServiceException;
	/**
	 * 获取我的打欠产品
	 * @param counter
	 * @return
	 */
	List<ReserveProduct> findMyReserveProduct(Counter counter);
	/**
	 * 所有打欠批次
	 * 打欠批次格式为日期
	 * @return 
	 */
	List<String> ListBatchNo(); 
	

}
