package com.zeusas.dp.ordm.service;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.MaterialTemplate;

public interface MaterialTemplateService extends IService<MaterialTemplate, Integer> {
	/**
	 * 把新店物料加到订单 更新柜台新店标志
	 * @param counterId
	 * @throws ServiceException
	 */
	public void excute(Integer counterId ,String orderNo)throws ServiceException;
}
