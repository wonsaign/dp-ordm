package com.zeusas.dp.ordm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.service.IdGenService;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.dao.BalanceRecordDao;
import com.zeusas.dp.ordm.entity.BalanceRecord;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.service.BalanceRecordService;
import com.zeusas.security.auth.entity.AuthUser;

@Service
@Transactional
public class BalanceRecordServiceImpl extends BasicService<BalanceRecord, Long> implements BalanceRecordService {

	public final static String ID_RECORDID = "RECORDID";

	@Autowired
	private BalanceRecordDao dao;

	@Autowired
	private IdGenService idGenService;

	@Override
	protected Dao<BalanceRecord, Long> getDao() {
		return dao;
	}

	public void save(BalanceRecord t ,Customer customer,AuthUser authUser) throws ServiceException {
		try {
			idGenService.lock(ID_RECORDID);
			String recordId=idGenService.generateDateId(ID_RECORDID);
			t.setRecordId(TypeConverter.toLong(recordId));
			t.setCustomerID(customer.getCustomerID());
			t.setCustomerName(customer.getCustomerName());
			t.setOperatorId(authUser.getUid());
			t.setOperatorName(authUser.getLoginName());
			dao.save(t);
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			idGenService.unlock(ID_RECORDID);
		}
	}

	@Override
	public List<BalanceRecord> findByCustomerId(Integer customerId) throws ServiceException {
		return dao.findByCustomerId(customerId);
	}
}
