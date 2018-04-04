package com.zeusas.dp.ordm.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.FixedPrice;

public interface FixedPriceManager {

	/** 根据一个产品id拿到下面的客户类型集合*/
	List<Integer> getCutomtersByProduct(Integer productId);
	
	/** 根据一个客户类型id 拿到下面所有的一口价产品*/
	Map<Integer,FixedPrice> getFixedPrices(Integer cutomterTypeId);
	
	/**
	 * 
	 * @param productid  产品id
	 * @param cutomterTypeId  客户类型id
	 * @return
	 */
	FixedPrice getFixedPrice(Integer productid,Integer cutomterTypeId);
	
	public boolean creatFixedPrice(FixedPrice fixedPrice) throws ServiceException;
	
	public boolean update(FixedPrice fixedPrice) throws ServiceException;
	
	public FixedPrice get(Integer productid);
	
	public Collection<FixedPrice> findall();
	
	public List<FixedPrice> findByProductName(String name);
	
	public void reload();

}
