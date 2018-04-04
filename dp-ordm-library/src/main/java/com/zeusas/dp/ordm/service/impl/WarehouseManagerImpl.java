package com.zeusas.dp.ordm.service.impl;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeusas.common.service.OnStartApplication;
import com.zeusas.dp.ordm.entity.Warehouse;
import com.zeusas.dp.ordm.service.WarehouseManager;
import com.zeusas.dp.ordm.service.WarehouseService;
@Service
public class WarehouseManagerImpl extends OnStartApplication implements WarehouseManager {

	static Logger logger = LoggerFactory.getLogger(WarehouseManagerImpl.class);
	
	@Autowired
	private WarehouseService warehouseService;
	
	final Map<Integer,Warehouse> allWarehouse;
	
	public WarehouseManagerImpl() {
		allWarehouse= new LinkedHashMap<>();
	}
	
	@Override
	public void load() {
		allWarehouse.clear();
		
		List<Warehouse> all =warehouseService.findAll();
		
		all.stream().forEach(e->allWarehouse.put(e.getId(), e));
		
	}
	
	@Override
	public Warehouse get(Integer id) {
		return allWarehouse.get(id);
	}

	@Override
	public Collection<Warehouse> findAll() {
		return allWarehouse.values();
	}

	@Override
	public void onStartLoad() {
		if(allWarehouse.size()==0){
			load();
		}
	}
	
	@Override
	public List<Warehouse> findByName(String name) {
		return allWarehouse.values().stream()//
				.filter(e -> e.getName().indexOf(name) >= 0)//
				.collect(Collectors.toList());
	}
}
