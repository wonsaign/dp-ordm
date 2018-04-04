package com.zeusas.dp.ordm.service;

import java.util.Collection;

import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.MaterialTemplate;

public interface MaterialTemplateManager {
	
 public void load();
 
 public MaterialTemplate get(Integer id);
 
 public Collection<MaterialTemplate> findAll();
 /**
  * 根据门店面积 获取模板
  * @param area
  * @return
  */
 public MaterialTemplate getByArea(Double area);
 
 public void update(MaterialTemplate materialTemplate) throws ServiceException;
}
