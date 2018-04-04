package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.UserDetail;
import com.zeusas.security.auth.entity.AuthUser;

public interface UserDetailService extends IService<UserDetail, String> {
	void createAuthUserDetails(AuthUser authUser,String passwd, UserDetail userDetails) throws ServiceException;
	public List<UserDetail> findAllUserDetail(List<String> userIds) throws ServiceException ;
}
