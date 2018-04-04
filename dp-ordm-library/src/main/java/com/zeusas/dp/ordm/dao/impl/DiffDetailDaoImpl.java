package com.zeusas.dp.ordm.dao.impl;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.dao.DiffDetailDao;
import com.zeusas.dp.ordm.entity.DiffDetail;

@Repository
public class DiffDetailDaoImpl extends CoreBasicDao<DiffDetail, Integer> implements DiffDetailDao {
	
	private static Logger logger = LoggerFactory.getLogger(DiffDetailDaoImpl.class);

	@Override
	public List<DiffDetail> getDiffDetails(String orderNo) throws DaoException {
		String where = "orderNo =?";
		try {
			return find(where, orderNo);
		} catch (DaoException e) {
			logger.error("获取订单明细失败, orderNo={}",orderNo);
			throw new ServiceException(e);
		}
	}


}
