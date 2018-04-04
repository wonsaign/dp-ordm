package com.zeusas.dp.ordm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.common.service.IdGenService;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.dao.FixedPriceDao;
import com.zeusas.dp.ordm.entity.FixedPrice;
import com.zeusas.dp.ordm.service.FixedPriceService;
import com.zeusas.security.auth.service.impl.AuthUserServiceImpl;
import com.zeusas.security.auth.utils.DigestEncoder;

@Service
@Transactional
public class FixedPriceServiceImpl extends BasicService<FixedPrice, Integer> implements FixedPriceService{
	static Logger logger = LoggerFactory.getLogger(FixedPriceServiceImpl.class);

	final static String IDGEN_FIXEDPRICE = "FIXEDPRICEID";

	
	@Autowired
	private FixedPriceDao dao;
	@Autowired
	private IdGenService idGenService;
	
	@Override
	protected Dao<FixedPrice, Integer> getDao() {
		return dao;
	}

	@Override
	public void createFixedPrice(FixedPrice fixedPrice) throws ServiceException {
		try {
			String id = idGenService.generateStringId(IDGEN_FIXEDPRICE);
			fixedPrice.setId(Integer.parseInt(id));
			fixedPrice.setLastUpdate(System.currentTimeMillis());
			save(fixedPrice);
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(IDGEN_FIXEDPRICE);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public FixedPrice getByProductId(Integer productId) throws ServiceException {
		return dao.getByProductId(productId);
	}

}
