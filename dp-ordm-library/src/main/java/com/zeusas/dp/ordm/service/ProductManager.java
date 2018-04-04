package com.zeusas.dp.ordm.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.Product;

public interface ProductManager {
	/**找到所有可用的产品*/
	public List<Product> findAllAvaible();
	/**加载所有的产品*/
	public void reload();
	/**添加一个产品 */
	public void add(Product product)throws ServiceException;
	/**根据ID获取一个产品*/
	public Product get(int productId);
	/**更新一个产品的信息 */
	Product updateInfo(Product product) throws ServiceException;
	/**根据系列获取所有的产品*/
	List<Product> findByClass(String clazz);
	/**根据自定义类型获取所有的产品*/
	List<Product> findByType(String type);
	/**根据身体部位获取所有产品*/
	/**根据自定义类型的硬码来获取所有的自定义类型*/
	List<Dictionary> findByHardCode(String type);
	/**通过名字、系列、条形码进行搜索找出所有的产品*/
	List<Product> findByName(String name) throws ServiceException;
	/**根据大类找到下面的系列*/
	List<String>findSerials(String name)throws ServiceException;
	/**根据大类和产品名字模糊查询*/
	List<Product>fingBySerialAndName(String SerialId,String name)throws ServiceException;
	/**根据身体部位类型找到某个系列的所有产品*/
	List<Product>findByBodyType(String bodyTypeId);
	/** 根据产品的id来判断是不是正品*/
	boolean isAuthenticProduct(int productId);
	/** 每个系列对应的产品*/
	public Map<String, Set<Product>> findAllSeriesClass();
	/**按照价格排序获取产品集合 true默认为降序排序 false为升序排列*/
	List<Product>findByPrice(String serialId,boolean flag);
	/**按照身体类型进行的按价格排序*/
	List<Product>findByPriceWithBody(String bodyTypeId,boolean flag);
	/** 找到所有的无效的产品*/
	List<Product> findAllDisabledProduct();
	/** 根据名字查询无效的产品*/
	List<Product> findDisabledProduct(String name) throws ServiceException;
	/**根据产品编码获取产品*/
	Product findByCode(String code);
	List<Product> findAll();
	}
