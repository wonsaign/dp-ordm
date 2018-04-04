package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.CartDetailDesc;

public interface CartDetailService extends IService<CartDetail, Long> {
	public final static String ID_CARTDETAIL = "CARTDETAILID";
	
	void save(CartDetail cartdetails, List<CartDetailDesc> desc) throws ServiceException;

	void save(CartDetail cartdetails, CartDetailDesc desc) throws ServiceException;
}
