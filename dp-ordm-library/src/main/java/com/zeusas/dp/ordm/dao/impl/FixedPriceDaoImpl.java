package com.zeusas.dp.ordm.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.dao.FixedPriceDao;
import com.zeusas.dp.ordm.entity.FixedPrice;

@Repository
public class FixedPriceDaoImpl extends CoreBasicDao<FixedPrice, Integer> implements FixedPriceDao {

	private static Logger logger = LoggerFactory.getLogger(FixedPriceDaoImpl.class);

	@Override
	public FixedPrice getByProductId(Integer productId) throws DaoException {
		String where = "ProductId = ?";
		List<FixedPrice> fps;
		try {
			fps = find(where, productId);
		} catch (Exception e) {
			logger.error("一口价错误 条件:{},PID:{}", where, productId);
			throw new DaoException(e);
		}
		return fps.isEmpty()? new FixedPrice():fps.get(0);
	}

}
