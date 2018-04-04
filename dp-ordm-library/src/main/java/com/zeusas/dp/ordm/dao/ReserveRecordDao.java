package com.zeusas.dp.ordm.dao;

import java.util.Collection;
import java.util.List;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.ReserveRecord;

public interface ReserveRecordDao extends Dao<ReserveRecord, Long> {
	
	public List<ReserveRecord> findByStatus(Integer status) throws DaoException;
	
	public List<ReserveRecord> findByStatus(Collection<Integer> status) throws DaoException;
	/**
	 * 根据订单号查询记录 用于前端显示 还欠详情
	 * @param orderNo
	 * @return
	 * @throws DaoException
	 */
	public List<ReserveRecord> findByOrderNo(String  orderNo) throws DaoException;
	/**
	 * 根据柜台号和状态查询预订记录
	 * 用于还欠加入订单时查询
	 * @param counterCode
	 * @param status
	 * @return
	 * @throws DaoException
	 */
	public List<ReserveRecord> findByCounterCodeAndStatus(String  counterCode,Integer status) throws DaoException;
	/**
	 * 根据客户id获取所有预订记录
	 * @param ccustomerId
	 * @param status
	 * @return
	 * @throws DaoException
	 */
	public List<ReserveRecord> findByCustomerId(String  customerId) throws DaoException;
	/**
	 * 根据客户id 预订记录  获取所有预订记录
	 * @param ccustomerId
	 * @param status
	 * @return
	 * @throws DaoException
	 */
	public List<ReserveRecord> findByCustomerIdAndStatus(String  customerId,Collection<Integer> status) throws DaoException;
	/**
	 * @param productIds
	 * @return
	 */
	List<ReserveRecord> findByProductId(Collection<Integer> productIds);
	
	/**
	 * 根据订单明细id 更改状态
	 * @param status
	 * @param orderDetailId
	 */
	void changeSingleStatus(Integer status,Long orderDetailId) throws DaoException;
	/**
	 * 根据pid(活动在订单里同一个活动的体现) 更改状态
	 * @param status
	 * @param orderDetailId
	 */
	void changeActivityStatus(Integer status,Long pid) throws DaoException;
	/**
	 * 根据pid获取同属于一组的产品
	 * @param pid
	 * @return
	 */
	List<ReserveRecord> findByPid(Long pid);
	
	List<ReserveRecord> search(Collection<String> customerId , Collection<String> counterCode, String orderNo);
	
}
