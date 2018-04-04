package com.zeusas.dp.ordm.service;

import java.util.List;

import com.zeusas.core.service.IService;
import com.zeusas.dp.ordm.entity.SerialInfo;

public interface SerialInfoService  extends IService<SerialInfo, String>{
	/**获取所有的系列*/
	List<SerialInfo> getSerialInfo();
	/**获取所有有效的系列*/
	List<SerialInfo> getActiveSerialInfo();
	/**获取所有无效的系列*/
	List<SerialInfo>getUnActiveSerialInfo();
	/**获取单个系列*/
	SerialInfo getSingleSerialInfo(String serialId);
	List<SerialInfo> find(String[] ids);
	void clear();

}
