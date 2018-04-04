package com.zeusas.dp.ordm.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zeusas.core.dao.DaoException;
import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.ReserveRecord;

public interface ReserveRecordService extends IService<ReserveRecord, Long> {
	/**
	 * 根据状态货打欠记录
	 * @return
	 */
	public List<ReserveRecord> findRecordByStatus(Integer status);
	
	public List<ReserveRecord> findRecordByStatus(Collection<Integer> status);
	/**
	 * 根据单号获取打欠记录
	 * @return
	 */
	public List<ReserveRecord> getRecordByOrderNo(String orderNo);
	/**
	 * OrderService.saveReserveRecord(String orderNo)
	 * @param orderNo
	 * @throws ServiceException
	 */
	@Deprecated
	void add(String orderNo) throws ServiceException;
	/**
	 * 根据柜台号获取待执行的预订记录
	 * @param counterCode
	 * @return
	 */
	List<ReserveRecord> findWeitShipByCounterCode(String counterCode);
	/**
	 * 取消预订(单条)
	 * @throws ServiceException
	 */
	void cancleReserve(Long orderDetailId) throws ServiceException;
	
	/**
	 * 取消预订(活动)
	 * @throws ServiceException
	 */
	void cancleReserveActivity(Long orderDetailId) throws ServiceException;
	/**
	 * 根据客户id获取所有预订记录
	 * @param customerId
	 * @return
	 * @throws ServiceException
	 */
	public List<ReserveRecord> findByCustomerId(String  customerId) throws ServiceException;
	/**
	 * 根据客户id 预订记录  获取所有预订记录
	 * @param customerId
	 * @param status
	 * @return
	 * @throws ServiceException
	 */
	public List<ReserveRecord> findByCustomerIdAndStatus(String  customerId,Collection<Integer> status) throws ServiceException;
	
	List<ReserveRecord> findByProductId(Collection<Integer> productIds);
	/**
	 * 获取产品在还欠起始和截止日期内的总和
	 * @param reserveStart
	 * @param reserveEnd
	 */
	public int findByTime(Integer productId, Date reserveStart, Date reserveEnd);
	
	/**
	 * 根据订单明细id 更改状态
	 * @param status
	 * @param orderDetailId
	 */
	void changeSingleStatus(Integer status,Long orderDetailId)throws ServiceException;
	/**
	 * 根据pid(活动在订单里同一个活动的体现) 更改状态
	 * @param status
	 * @param orderDetailId
	 */
	void changeActivityStatus(Integer status,Long orderDetailId)throws ServiceException;
	/**
	 * @param status 状态
	 * @return Map<Integer, Map<Integer, Integer>> 产品id,仓库id,数量
	 */
	Map<Integer, Map<Integer, Integer>> groupByProductWarehouse(Collection<Integer> status);
	
} 


