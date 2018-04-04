package com.zeusas.dp.ordm.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeusas.common.service.OnStartApplication;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.dp.ordm.entity.MaterialTemplate;
import com.zeusas.dp.ordm.service.CounterService;
import com.zeusas.dp.ordm.service.MaterialTemplateManager;
import com.zeusas.dp.ordm.service.MaterialTemplateService;
@Service
public class MaterialTemplateManagerImpl extends OnStartApplication implements MaterialTemplateManager{

	static Logger logger = LoggerFactory.getLogger(MaterialTemplateManagerImpl.class);
	
	@Autowired
	MaterialTemplateService materialTemplateService;
	
	final Map<Integer, MaterialTemplate> all_template;
	
	
	public MaterialTemplateManagerImpl() {
		all_template=new HashMap<>();
	}

	@Override
	public void load() {
		all_template.clear();
		
		List<MaterialTemplate> all=materialTemplateService.findAll();
		
		for (MaterialTemplate materialTemplate : all) {
			all_template.put(materialTemplate.getId(), materialTemplate);
		}
		
	}

	@Override
	public Collection<MaterialTemplate> findAll() {
		return all_template.values();
	}

	@Override
	public MaterialTemplate getByArea(Double area) {
		for (MaterialTemplate template : findAll()) {
			//MinArea<area<MaxArea
			//FIXME: MinArea<area<=MaxArea ? MinArea<=area<=MaxArea ?
			if(template.getMaxArea().compareTo(area)//
					+area.compareTo(template.getMinArea())==2){
				return template;
			}
		}
		return null;
	}

	@Override
	public MaterialTemplate get(Integer id) {
		return all_template.get(id);
	}

	@Override
	public void onStartLoad() {
		if(all_template.isEmpty()){
			load();
		}
	}

	@Override
	public void update(MaterialTemplate materialTemplate) throws ServiceException {
		MaterialTemplate dbTemplate=materialTemplateService.get(materialTemplate.getId());
		
		BeanDup.dupNotNull(materialTemplate, dbTemplate, "name","minArea","maxArea","countext","status","lastUpdate");
		
		dbTemplate.setLastUpdate(new Date());
		try {
			materialTemplateService.update(dbTemplate);
		} catch (Exception e) {
			logger.error("更新新店物料失败");
			throw new ServiceException(e);
		}
	}


}
