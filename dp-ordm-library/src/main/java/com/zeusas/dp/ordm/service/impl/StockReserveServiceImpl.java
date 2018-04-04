package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.common.service.IdGenService;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.dao.StockReserveDao;
import com.zeusas.dp.ordm.entity.StockReserve;
import com.zeusas.dp.ordm.service.StockReserveService;

@Service
@Transactional
public class StockReserveServiceImpl extends BasicService<StockReserve, String> implements StockReserveService {

	public final static String ID_STOCKRESERVE = "STOCKRESERVEID";

	private static Logger logger = LoggerFactory.getLogger(StockReserveService.class);

	@Autowired
	private IdGenService idGenService;
	@Autowired
	StockReserveDao dao;

	@Override
	protected Dao<StockReserve, String> getDao() {
		return dao;
	}

	@Override
	public StockReserve create(StockReserve stockReserve) throws ServiceException {
		try {
			idGenService.lock(ID_STOCKRESERVE);
			String id = idGenService.generateStringId(ID_STOCKRESERVE);
			stockReserve.setId(id);
			dao.save(stockReserve);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.error("创建预留库存错误,{}", stockReserve, e);
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_STOCKRESERVE);
		}
		return stockReserve;
	}

	@Override
	@Transactional(readOnly = true)
	public List<StockReserve> findReserveProduct(Integer pid) throws ServiceException {
		List<StockReserve> reserves = new ArrayList<>();
		try {
			reserves = dao.findReserveProduct(pid);
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (Exception e) {
			logger.error("根据产品id查询预留库存错误,{}", pid, e);
			throw new ServiceException(e);
		}
		return reserves;
	}

	@Override
	@Transactional(readOnly = true)
	public List<StockReserve> findByDesc(String keyword) throws ServiceException {
		List<StockReserve> reserves = new ArrayList<>();
		try {
			reserves = dao.findByDesc(keyword);
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (Exception e) {
			logger.error("根据功能描述查询预留库存错误,{}", keyword, e);
			throw new ServiceException(e);
		}
		return reserves;
	}

	@Override
	@Transactional(readOnly = true)
	public List<StockReserve> findAvalible() throws ServiceException {
		List<StockReserve> reserves = new ArrayList<>();
		try {
			reserves = dao.findAvalible();
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (Exception e) {
			logger.error("查询有效预留库存错误", e);
			throw new ServiceException(e);
		}
		return reserves;
	}

	@Override
	public List<StockReserve> findAvalibleByProductId(Integer pid) throws ServiceException {
		List<StockReserve> reserves = new ArrayList<>();
		try {
			reserves = dao.findAvalibleByProductId(pid);
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (Exception e) {
			logger.error("查询有效预留库存错误", e);
			throw new ServiceException(e);
		}
		return reserves;
	}

	@Override
	public List<StockReserve> findByProductId(Integer pid) throws ServiceException {
		List<StockReserve> reserves = new ArrayList<>();
		try {
			reserves = dao.findByProductId(pid);
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (Exception e) {
			logger.error("查询有效预留库存错误", e);
			throw new ServiceException(e);
		}
		return reserves;
	}

	@Override
	public List<StockReserve> findCancelByProductId(Integer pid) throws ServiceException {
		List<StockReserve> reserves = new ArrayList<>();
		try {
			reserves = dao.findCancelByProductId(pid);
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (Exception e) {
			logger.error("查询作废预留库存错误", e);
			throw new ServiceException(e);
		}
		return reserves;
	}

	@Override
	public List<StockReserve> findExpireByProductId(Integer pid) throws ServiceException {
		List<StockReserve> reserves = new ArrayList<>();
		try {
			reserves = dao.findExpireByProductId(pid);
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (Exception e) {
			logger.error("查询过期预留库存错误", e);
			throw new ServiceException(e);
		}
		return reserves;
	}

}
