package com.zeusas.dp.ordm.rev.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.service.IdGenService;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderCredentials;
import com.zeusas.dp.ordm.rev.dao.ReservedActivityDao;
import com.zeusas.dp.ordm.rev.entity.ReservedActivity;
import com.zeusas.dp.ordm.rev.service.ReservedActivityService;
import com.zeusas.dp.ordm.task.StorehousesSyncTask;
import com.zeusas.dp.ordm.utils.OrdmConfig;

@Transactional
@Service
public class ReservedActivityServiceImpl extends BasicService<ReservedActivity, Integer> implements
		ReservedActivityService {
	public final static String ID_RESERVEDACTIVITY = "RESERVEDACTIVITYID";
	
	@Autowired
	private ReservedActivityDao dao;
	@Autowired
	private IdGenService idGenService;

	@Override
	protected Dao<ReservedActivity, Integer> getDao() {
		return dao;
	}

	@Override
	public void add(ReservedActivity reservedActivity) throws ServiceException {
		try {
			idGenService.lock(ID_RESERVEDACTIVITY);
			Integer revId =TypeConverter.toInteger(idGenService.generateDateId(ID_RESERVEDACTIVITY));
			reservedActivity.setRevId(revId);
			dao.save(reservedActivity);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_RESERVEDACTIVITY);
		}
		
	}

}
