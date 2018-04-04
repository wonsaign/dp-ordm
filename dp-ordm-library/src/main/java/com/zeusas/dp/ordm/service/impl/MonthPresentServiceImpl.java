package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.common.service.IdGenService;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.dao.MonthPresentDao;
import com.zeusas.dp.ordm.entity.MonthPresent;
import com.zeusas.dp.ordm.service.MonthPresentService;

/**
 * 
 * @author fengx
 * @date 2017年1月22日 上午11:17:16
 */
@Service
@Transactional
public class MonthPresentServiceImpl extends BasicService<MonthPresent, Long> implements MonthPresentService {
	public final static String ID_MONTHPRESENT = "MONTHPRESENTID";

	@Autowired
	private MonthPresentDao dao;
	@Autowired
	private IdGenService idGenService;

	@Override
	protected Dao<MonthPresent, Long> getDao() {
		return dao;
	}

	@Override
	public void createMonthPresent(MonthPresent monthPresent) throws ServiceException {
		if (monthPresent.getContext().isEmpty()) {
			throw new ServiceException("正文明细不能为空");
		}
		monthPresent.setYearMonth(monthPresent.getYearMonth());
		monthPresent.setCreateTime(new Date());
		monthPresent.setLastUpdate(new Date());
		monthPresent.setStatus(true);
		try {
			idGenService.lock(ID_MONTHPRESENT);
			monthPresent.setId(Long.parseLong(idGenService.generateDateId((ID_MONTHPRESENT))));
			dao.save(monthPresent);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_MONTHPRESENT);
		}
	}

	@Override
	public List<MonthPresent> findByYearMonth(Integer yearMonth) throws ServiceException {
		List<MonthPresent> monthPresents=new ArrayList<>();
		try {
			monthPresents=dao.findByYearMonth(yearMonth);
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (Exception e) {
			throw new ServiceException(e);
		} 
		return monthPresents;
	}
}
