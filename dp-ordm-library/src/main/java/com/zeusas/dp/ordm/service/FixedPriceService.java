package com.zeusas.dp.ordm.service;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.FixedPrice;

public interface FixedPriceService extends IService<FixedPrice, Integer> {
	
	public void createFixedPrice(FixedPrice fixedPrice) throws ServiceException;
	
	public FixedPrice getByProductId(Integer productId)throws ServiceException;
}
