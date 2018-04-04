package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.SerialInfo;
import com.zeusas.dp.ordm.service.SerialInfoService;

@Service
public class SerialInfoServiceImpl  implements SerialInfoService {
	private long lastUpdate = 0;
	
	final Map<String,SerialInfo>serialInfos=new HashMap<String,SerialInfo>();
	
	private void checkLoad() {
		DictManager dictManager = AppContext.getBean(DictManager.class);
		if (dictManager.lastUpdate() == lastUpdate) {
			return;
		}
		lastUpdate = dictManager.lastUpdate();
		
		serialInfos.clear();
		List<Dictionary> dictionaries=dictManager.get(SerialInfo.SERIALINFO_TYPE).getChildren();
		for(Dictionary dictionary:dictionaries){
			SerialInfo serialInfo=new SerialInfo(dictionary);
			serialInfos.put(serialInfo.getSid(),serialInfo);
		}
	}
	
	public int size(){
		return serialInfos.size();
	}
	
	public List<SerialInfo> getSerialInfo(){
		checkLoad();
		final List<SerialInfo> val = new ArrayList<SerialInfo>(size());
		val.addAll(serialInfos.values());
		return val;
	}

	@Override
	public List<SerialInfo> getActiveSerialInfo() {
		checkLoad();
		List<SerialInfo> val = new ArrayList<SerialInfo>(serialInfos.size());
		for (SerialInfo si : serialInfos.values()) {
			if (si.isActive()) {
				val.add(si);
			}
		}
		return val;
	}

	@Override
	public List<SerialInfo> getUnActiveSerialInfo() {
		checkLoad();
		List<SerialInfo> val = new ArrayList<SerialInfo>(serialInfos.size());
		for (SerialInfo si : serialInfos.values()) {
			if (!si.isActive()) {
				val.add(si);
			}
		}
		return val;
	}

	@Override
	public SerialInfo getSingleSerialInfo(String serialId) {
		return serialInfos.get(serialId);
	}

	@Override
	public SerialInfo get(String key) {
		return serialInfos.get(key);
	}
	

	
	
	@Override
	public void save(SerialInfo t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public SerialInfo update(SerialInfo t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int delete(String ids) {
		throw new UnsupportedOperationException("Not suppport Delete!");
	}

	@Override
	public int delete(String[] ids) {
		throw new UnsupportedOperationException("Not suppport Delete!");
	}

	@Override
	public int update(String pk, Map<String, Object> values)
			throws ServiceException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<SerialInfo> findAll() {
		return getSerialInfo();
	}

	@Override
	public List<SerialInfo> find(String where, Object... args)
			throws ServiceException {
		throw new UnsupportedOperationException("Not suppport Delete!");
	}

	@Override
	public int execute(String hql, Object... params) throws ServiceException {
		throw new UnsupportedOperationException("Not suppport Delete!");
	}

	@Override
	public int executeNative(String sql, Object... params)
			throws ServiceException {
		throw new UnsupportedOperationException("Not suppport Delete!");
	}

	@Override
	public List<SerialInfo> getScrollData(int firstindex, int maxresult,
			String wheresql, String orderby, Object... params)
			throws ServiceException {
		throw new UnsupportedOperationException("Not suppport Delete!");
	}

	@Override
	public int update(Map<String, Object> values, String cond, Object... args) throws ServiceException {
		throw new UnsupportedOperationException("Not support Update!");
	}

	@Override
	public int count(String cond, Object... args) throws ServiceException {
		return 0;
	}

	@Override
	public List<SerialInfo> find(String[] ids) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();		
	}

}
