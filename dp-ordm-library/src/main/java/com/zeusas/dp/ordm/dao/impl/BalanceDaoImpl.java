package com.zeusas.dp.ordm.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zeusas.common.dao.CoreBasicDao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.QueryHelper;
import com.zeusas.dp.ordm.dao.BalanceDao;
import com.zeusas.dp.ordm.entity.Balance;

@Repository
public class BalanceDaoImpl extends CoreBasicDao<Balance, Integer> implements
		BalanceDao {

	final static Logger logger = LoggerFactory.getLogger(BalanceDaoImpl.class);

	protected Connection getK3Connection() {
		DataSource ds = (DataSource) AppContext.getBean("k3");
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	public String getBalance(int customerId) throws DaoException {
		Connection conn = getK3Connection();

		String sql = "{?=call dbo.f_fn_getcusmoney (?)}";
		CallableStatement callableStatement = null;
		try {
			callableStatement = conn.prepareCall(sql);
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.setInt(2, customerId);
			callableStatement.execute();
			return callableStatement.getNString(1);
		} catch (Exception e) {
			logger.error(
					"Exec proceduer call: {?=call dbo.f_fn_getcusmoney (?)} {},{}",
					Types.VARCHAR, customerId);
			throw new DaoException(e);
		} finally {
			QueryHelper.close(callableStatement);
			QueryHelper.close(conn);
		}
	}
}
