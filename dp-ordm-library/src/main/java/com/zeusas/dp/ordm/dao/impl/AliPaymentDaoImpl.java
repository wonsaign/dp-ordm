package com.zeusas.dp.ordm.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.common.entity.OpLogger;
import com.zeusas.common.service.OpLoggerService;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.QueryHelper;
import com.zeusas.dp.ordm.dao.AliPaymentDao;
import com.zeusas.dp.ordm.entity.AliPayment;



/**
 * 
 * @author fengx
 * @date 2017年1月17日 下午3:55:50
 */
@Repository
public class AliPaymentDaoImpl extends CoreBasicDao<AliPayment, String> implements AliPaymentDao {

	static Logger logger = LoggerFactory.getLogger(AliPaymentDaoImpl.class);
	@Autowired
	private OpLoggerService bizLogger;
	
	public void save(AliPayment alip) {
		Session sess = null;
		Transaction trans = null;
		try {
			sess = super.openSession();
			trans = sess.beginTransaction();
			sess.save(alip);
			trans.commit();
		} catch (Exception e) {
			if (trans != null) {
				trans.rollback();
			}
			logger.error("{}", alip,e);
		} finally {
			if (sess != null) {
				sess.close();
			}
		}
	}
	@Override
	public void buildK3Receive(String orderNo, double paymenprice, Integer fCustomer,Integer i)
			throws DaoException {
		DataSource ds = (DataSource) AppContext.getBean("k3");
		Connection conn = null;
		String sql = "{call dbo.f_sp_ZFReceive (?,?,?,?)}";
		CallableStatement callableStatement = null;
		Assert.notNull(ds,"dataSource is null");
		try {
			conn = ds.getConnection();
			callableStatement = conn.prepareCall(sql);
			
			callableStatement.setString(1, orderNo);
			callableStatement.setDouble(2, paymenprice);
			callableStatement.setInt(3, fCustomer);
			callableStatement.setInt(4, i);
			
			callableStatement.execute();
		} catch (Exception e) {
			logger.error("Exec proceduer call: paymenprice {},{},{},{}", 
					orderNo,  paymenprice, fCustomer,e);
			StringBuilder summary = new StringBuilder();
			summary.append("OrderNo:").append(orderNo)//
					.append(", Payment Price:").append(paymenprice)//
					.append(",CustomerID:").append(fCustomer);
			OpLogger bizLog = new OpLogger().addSummary(summary.toString());
			bizLogger.save(bizLog);
			throw new DaoException(e);
		} finally {
			QueryHelper.close(callableStatement);
			QueryHelper.close(conn);
		}
	}

	@Override
	public void buildK3OtherReceive(String orderNo, double paymenprice, Integer fCustomer)
			throws DaoException {
		DataSource ds = (DataSource) AppContext.getBean("k3");
		Connection conn = null;
		String sql = "{call dbo.f_sp_ZFOtherReceive (?,?,?)}";
		CallableStatement callableStatement = null;
		Assert.notNull(ds,"dataSource is null");
		try {
			conn = ds.getConnection();
			callableStatement = conn.prepareCall(sql);
			
			callableStatement.setString(1, orderNo);
			callableStatement.setDouble(2, paymenprice);
			callableStatement.setInt(3, fCustomer);
			
			callableStatement.execute();
		} catch (Exception e) {
			logger.error("Exec proceduer call: paymenprice {},{},{}", 
					orderNo,  paymenprice, fCustomer,e);
			StringBuilder summary = new StringBuilder();
			summary.append("OrderNo:").append(orderNo)//
					.append(", Payment Price:").append(paymenprice)//
					.append(",CustomerID:").append(fCustomer);
			OpLogger bizLog = new OpLogger().addSummary(summary.toString());
			bizLogger.save(bizLog);
			throw new DaoException(e);
		} finally {
			QueryHelper.close(callableStatement);
			QueryHelper.close(conn);
		}
	}
}
