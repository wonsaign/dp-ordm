package com.zeusas.dp.ordm.dao;

import java.util.List;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.dp.ordm.entity.OrderCredentials;

public interface OrderCredentialsDao extends Dao<OrderCredentials, String> {

	/**
	 * 因为hibernate 持久态的问题，所以这里需要手动调用getCurrentSession.clear()
	 * @param ocid
	 * @throws DaoException
	 */
	void deleteOC(String ocid) throws DaoException;
	
	List<OrderCredentials> getParentCredentials() throws DaoException;
	
	List<OrderCredentials> getChildCredentials() throws DaoException;
	
	List<OrderCredentials> getCredentials() throws DaoException;
}
