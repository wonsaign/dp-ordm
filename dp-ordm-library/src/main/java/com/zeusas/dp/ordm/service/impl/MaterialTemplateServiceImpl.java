package com.zeusas.dp.ordm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.dao.MaterialTemplateDao;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.MaterialTemplate;
import com.zeusas.dp.ordm.service.CounterService;
import com.zeusas.dp.ordm.service.MaterialTemplateService;
import com.zeusas.dp.ordm.service.OrderService;

@Service
public class MaterialTemplateServiceImpl extends BasicService<MaterialTemplate, Integer>
		implements MaterialTemplateService {

	final static Logger logger = LoggerFactory.getLogger(OrderCredentialsServiceImpl.class);

	@Autowired
	private MaterialTemplateDao dao;

	@Override
	protected Dao<MaterialTemplate, Integer> getDao() {
		return dao;
	}

	@Override
	@Transactional
	public void excute(Integer counterId, String orderNo) throws ServiceException {
		OrderService orderService = AppContext.getBean(OrderService.class);
		CounterService counterService = AppContext.getBean(CounterService.class);
		Counter counter = counterService.get(counterId);
		if (counter == null) {
			throw new ServiceException("执行新店物料错误,柜台为空");
		}
		Double area = counter.getArea();
		if (area == null) {
			throw new ServiceException("执行新店物料错误,面积为空");
		}
		Boolean newcounter=counter.getNewCounter();
		if (newcounter == null) {
			throw new ServiceException("执行新店物料错误,新店标志为空");
		}
		if (!newcounter) {
			throw new ServiceException("执行新店物料错误,不是新店");
		}
		for (MaterialTemplate template : this.findAll()) {
			if (template.getMaxArea().compareTo(area)//
					+ area.compareTo(template.getMinArea()) == 2) {
				try {
					orderService.addFreeMaterial(orderNo, template.getCountext());
					counter.setNewCounter(false);
					counterService.update(counter);
					break;
				} catch (Exception e) {
					throw new ServiceException(e);
				}
			}
		}
	}

}
