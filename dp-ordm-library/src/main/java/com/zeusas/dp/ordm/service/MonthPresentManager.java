package com.zeusas.dp.ordm.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.MonthPresent;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.PresentContext;

public interface MonthPresentManager{
	public void load();
	
	public MonthPresent get(Long id);
	/**
	 * 根据年月 和柜台号获取MonthPresent
	 * @param yearmonth
	 * @param counterCode
	 * @return
	 */
	public MonthPresent findByMonthAndCode(Integer yearmonth,String counterCode);
	/**
	 * 获取当月未推送物料
	 * @param counterCode
	 * @return
	 */
	public List<PresentContext> getUnexecutedByCounter(String counterCode);
	/**
	 * 根据年月获取
	 * @param yearmonth
	 * @return
	 */
	public Map<String,MonthPresent> findByYearMonth(Integer yearmonth);
	
	public Collection<MonthPresent> findAll();
	
	/**
	 * 使用MonthPresentManager.excute(String counterCode, MonthPresent monthPresent)
	 */
	@Deprecated 
	public void excute(String counterCode, String type) throws ServiceException;
	/**
	 * 使用MonthPresentManager.excute(String counterCode, MonthPresent monthPresent)
	 */
	@Deprecated 
	public void excute(String orderNo,String counterCode, PresentContext presentContext) throws ServiceException;
	/**
	 * 由原来的根据类型更新状态改为根据index更新  sql减少
	 */
	public void excute(String orderNo, MonthPresent monthPresent) throws ServiceException;
	/**
	 * 超过结束时间 系统制单
	 * @param monthPresent
	 * @throws ServiceException
	 */
	public Order excute(MonthPresent monthPresent) throws ServiceException;
	
	@Deprecated
	public Order excute(MonthPresent monthPresent,List<String> activRecord) throws ServiceException;
	
	public void update(MonthPresent monthPresent) throws ServiceException;
	
	public void saveOrUpdate(MonthPresent monthPresent,String creator) throws ServiceException;
	/**
	 * 获取当前核算月
	 * 20170726->201708
	 * @return
	 */
	public Integer getyearmonth();
	
	public Integer checkUploadFileHeard(Workbook workbook)throws ServiceException;
	
	public List<MonthPresent> readFile(Workbook workbook)throws ServiceException;
}
