package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.StockReserve;

public interface StockReserveService extends IService<StockReserve, String> {
	/**
	 * 创建
	 * @param stockReserve
	 * @return
	 * @throws ServiceException
	 */
	public StockReserve create(StockReserve stockReserve) throws ServiceException;

	/**
	 * 根据产品id获取所有设置
	 * @param pid
	 * @return
	 * @throws ServiceException
	 */
	public List<StockReserve> findReserveProduct(Integer pid) throws ServiceException;

	/**
	 * 根据描述模糊查询
	 * @param keyword
	 * @return
	 * @throws ServiceException
	 */
	public List<StockReserve> findByDesc(String keyword) throws ServiceException;

	/**
	 * 查询所有 状态可用 在时间范围内的
	 * @return
	 * @throws ServiceException
	 */
	public List<StockReserve> findAvalible() throws ServiceException;

	/**
	 * 根据产品id查询所有设置记录
	 * @param pid
	 * @return
	 * @throws ServiceException
	 */
	public List<StockReserve> findByProductId(Integer pid) throws ServiceException;

	/**
	 * 根据产品id查询有效设置记录
	 * @param pid
	 * @return
	 * @throws ServiceException
	 */
	public List<StockReserve> findAvalibleByProductId(Integer pid) throws ServiceException;

	/**
	 * 根据产品id查询作废设置记录
	 * @param pid
	 * @return
	 * @throws ServiceException
	 */
	public List<StockReserve> findCancelByProductId(Integer pid) throws ServiceException;

	/**
	 * 根据产品id查询过期设置记录
	 * @param pid
	 * @return
	 * @throws ServiceException
	 */
	public List<StockReserve> findExpireByProductId(Integer pid) throws ServiceException;
}
