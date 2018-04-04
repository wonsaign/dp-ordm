package com.zeusas.dp.ordm.service;

import java.util.Collection;
import java.util.List;

import com.zeusas.dp.ordm.entity.Warehouse;

public interface WarehouseManager {
	public Warehouse get(Integer id);
	public Collection<Warehouse> findAll();
	void load();
	/**按名称模糊查询*/
	public List<Warehouse> findByName(String name);
}
