package com.zeusas.dp.ordm.service;

import com.zeusas.core.service.IService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.ProductRelationPolicy;

/**
 * 
 * @author fengx
 * @date 2016年12月13日 下午1:35:42
 */
public interface ProductRelationPolicyService extends IService<ProductRelationPolicy, String> {
	
	/**找到一个产品关联策略*/
	public ProductRelationPolicy findByProductPolicy(ProductRelationPolicy relationPolicy) throws ServiceException;
	/**创建或者更新一个产品策略*/
	public void createOrUpateProductPolicy(ProductRelationPolicy relationPolicy) throws ServiceException;
	

}
