package com.zeusas.dp.ordm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.common.service.IdGenService;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.dao.CartDetailDao;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.CartDetailDesc;
import com.zeusas.dp.ordm.service.CartDetailService;

@Service
@Transactional
public class CartDetailServiceImpl extends BasicService<CartDetail, Long> implements
		CartDetailService {
	@Autowired
	private CartDetailDao cartDetailDao;
	@Autowired
	IdGenService idGenService;

	@Override
	protected Dao<CartDetail, Long> getDao() {
		return cartDetailDao;
	}

	@Transactional
	public void save(CartDetail entity) {
		cartDetailDao.save(entity);
	}

	@Transactional
	@Override
	public void save(CartDetail cartdetails, List<CartDetailDesc> desc) throws ServiceException {
		try {
			idGenService.lock(ID_CARTDETAIL);
			Long id = cartdetails.getDetailId();
			if (id == null) {
				id = Long.parseLong(idGenService.generateDateId(ID_CARTDETAIL));
				cartdetails.setDetailId(id);
			}
			super.save(cartdetails);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_CARTDETAIL);
		}
	}

	@Transactional
	@Override
	public void save(CartDetail cartdetails, CartDetailDesc desc) throws ServiceException {
		try {
			idGenService.lock(ID_CARTDETAIL);
			Long id = Long.parseLong(idGenService.generateDateId(ID_CARTDETAIL));
			cartdetails.setDetailId(id);
			cartDetailDao.save(cartdetails);
			desc.setCartDetailId(id);
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_CARTDETAIL);
		}
	}

	@Transactional
	public int delete(Long[] ids) {
		return getDao().delete(ids);
	}

	@Transactional
	public int delete(Long id) {
		return getDao().delete(id);
	}
}
