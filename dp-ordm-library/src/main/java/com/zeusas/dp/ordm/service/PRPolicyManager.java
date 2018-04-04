package com.zeusas.dp.ordm.service;


import java.util.List;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductRelationPolicy;

/**
 * 产品关联策略的字典
 * 
 * @author fengx
 * @date 2016年12月13日 下午2:48:38
 */
public interface PRPolicyManager {
	/** 重置 */
	public void reload();

	/** 添加一个新的关联策略 */
	public void add(ProductRelationPolicy policy) throws ServiceException;

	/** 更新一个产品关联策略 */
	public void update(ProductRelationPolicy policy);

	/**
	 * 判断该产品是否有关联策略 1.如果该产品存在关联策略，就找出其对应的最小订货单位以及关联产品，如果没有就去找系列关联策略
	 * 2.如果该系列存在关联策略，就找出其对应的最小订货单位以及关联产品，如果没有就去找全局关联策略 3.找到全局关联策略
	 */
	public ProductRelationPolicy get(Product product);

	/** 找到产品策略的所有关联类型 1.产品2.系列3.全局 */
	List<Dictionary> findByHardCode(String type);

	/** 找到所有的关联策略 */
	public List<ProductRelationPolicy> findall();

	/** 根据名字找到关联策略 */
	public List<ProductRelationPolicy> findByName(String Name);

	/** 分页关联策略 */
	public List<ProductRelationPolicy> pagination(int page, int num);

	/** 根据一个策略id获取整个关联策略 */
	public ProductRelationPolicy get(String policyId);
	
	/** 校验策略名是否存在 */
	public boolean checkName(String Name);
}
