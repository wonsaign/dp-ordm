package com.zeusas.dp.ordm.service;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.entity.UserDetail;
import com.zeusas.security.auth.entity.AuthUser;

public interface UserCustomerService extends IService<UserCustomer, String> {

	void creatAuthuserForCustomer(AuthUser authUser, UserDetail detail, UserCustomer userCustomer)
			throws ServiceException;

	void creatAuthuserForCustomer(AuthUser authUser, UserDetail userDetail);

	void updateAuthuserForCustomer(AuthUser authUser, UserDetail userDetail);
}
