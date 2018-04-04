package com.zeusas.dp.ordm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.dao.UserCustomerDao;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.entity.UserDetail;
import com.zeusas.dp.ordm.service.UserCustomerService;
import com.zeusas.dp.ordm.service.UserDetailService;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.service.AuthUserService;

@Service
public class UserCustomerServiceImpl extends BasicService<UserCustomer, String> implements UserCustomerService{

	@Autowired
	private UserCustomerDao dao;
	@Autowired
	private AuthUserService authUserService;
	@Autowired
	private UserDetailService userDetailService;
	
	@Override
	protected Dao<UserCustomer, String> getDao() {
		return dao;
	}

	@Transactional
	public void creatAuthuserForCustomer(AuthUser authUser, UserDetail detail, UserCustomer userCustomer)
			throws ServiceException {
		try {
			authUserService.createAuthUser(authUser,authUser.getPassword());
			AuthUser dbUser=authUserService.findByLoginName(authUser.getLoginName());
			Assert.notNull(dbUser);
			String uid=dbUser.getUid();
			detail.setUserId(uid);
			userDetailService.save(detail);
			userCustomer.setUserId(uid);
			userCustomer.setCustomerUserId(uid);
			this.save(userCustomer);
		} catch (Exception e) {
			throw new ServiceException("为客户创建帐号错误");
		}
		
	}

	@Override
	@Transactional
	public void creatAuthuserForCustomer(AuthUser authUser, UserDetail detail) throws ServiceException{

		try {
			authUserService.createAuthUser(authUser,authUser.getPassword());
			AuthUser dbUser=authUserService.findByLoginName(authUser.getLoginName());
			Assert.notNull(dbUser);
			String uid=dbUser.getUid();
			detail.setUserId(uid);
			userDetailService.save(detail);
		} catch (Exception e) {
			throw new ServiceException("为客户创建帐号错误");
		}
	}

	@Override
	@Transactional
	public void updateAuthuserForCustomer(AuthUser authUser, UserDetail userDetail) {

		try {
			authUserService.update(authUser);
			userDetailService.update(userDetail);
		} catch (Exception e) {
			throw new ServiceException("更新账号错误");
		}
	}
}
