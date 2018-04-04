package com.zeusas.dp.ordm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.dao.DaoException;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.dao.UserDetailDao;
import com.zeusas.dp.ordm.entity.UserDetail;
import com.zeusas.dp.ordm.service.UserDetailService;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.service.AuthCenterManager;

@Service
@Transactional
public class UserDetailServiceImpl extends BasicService<UserDetail, String> implements UserDetailService {
	
	final static Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);
	
	@Autowired
	private UserDetailDao userDetailDao;
	
	@Override
	protected Dao<UserDetail, String> getDao() {
		return userDetailDao;
	}

	@Transactional
	public void createAuthUserDetails(AuthUser authUser, String passwd, UserDetail userDetails) throws ServiceException {
		// 使用认证管理中心类，注意使用
		AuthCenterManager authCenter = AppContext.getBean(AuthCenterManager.class);
		try {
			authCenter.createAuthUser(authUser, passwd);
			userDetails.setLastUpdate(System.currentTimeMillis());
			userDetails.setStatus(true);
			userDetails.setUserId(authUser.getUid());
			super.save(userDetails);
		} catch (ServiceException e){
			throw e;
		} catch (Exception e){
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<UserDetail> findAllUserDetail(List<String> userIds) throws ServiceException {
		List<UserDetail>  details= null;
		String where = "WHERE  userId IN (:userId) ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userIds);
		try {
			details = userDetailDao.find(where, map);
		} catch (DaoException e) {
			logger.warn("购物车获取异常{}", details, e);
			throw new ServiceException("获取门店购物车异常",e);
		}
		return details;
	}

}
