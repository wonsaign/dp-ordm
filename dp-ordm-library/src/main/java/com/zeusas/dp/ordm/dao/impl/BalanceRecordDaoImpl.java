package com.zeusas.dp.ordm.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.dao.BalanceRecordDao;
import com.zeusas.dp.ordm.entity.BalanceRecord;

@Repository
public class BalanceRecordDaoImpl extends CoreBasicDao<BalanceRecord, Long>
		implements BalanceRecordDao {
	
	@Override
	public void save(BalanceRecord t) throws DaoException {
		Session sess = super.openSession();
		Transaction trans = null;

		try {
			trans = sess.beginTransaction();
			super.save(t);
			trans.commit();
		} catch (Exception e) {
			if (trans != null) {
				trans.rollback();
			}
		} finally {
			if (sess != null) {
				sess.close();
			}
		}

	}

	@Override
	public List<BalanceRecord> findByCustomerId(Integer customerId)
			throws DaoException {
		List<BalanceRecord> records;
		String where = "customerID =?";
		try {
			records = this.find(where, customerId);
		} catch (DaoException e) {
			throw new DaoException(e);
		}
		return records;
	}
}
