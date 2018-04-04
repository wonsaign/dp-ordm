package com.zeusas.dp.ordm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeusas.common.service.IdGenService;
import com.zeusas.core.dao.Dao;
import com.zeusas.core.service.BasicService;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.dao.ProductRelationPolicyDao;
import com.zeusas.dp.ordm.entity.ProductRelationPolicy;
import com.zeusas.dp.ordm.service.ProductRelationPolicyService;

/**
 * 
 * @author fengx
 * @date 2016年12月13日 下午1:36:12
 */
@Service
public class ProductRelationPolicyServiceImpl extends BasicService<ProductRelationPolicy, String>
		implements ProductRelationPolicyService {

	private final static String productRelation = "PRODUCTRELATION";
	
	@Autowired
	private ProductRelationPolicyDao dao;
	@Autowired
	private IdGenService idGen;

	@Override
	protected Dao<ProductRelationPolicy, String> getDao() {
		return dao;
	}

	@Override
	@Transactional(readOnly=true)
	public ProductRelationPolicy findByProductPolicy(ProductRelationPolicy relationPolicy) throws ServiceException {
		if (StringUtil.isEmpty(relationPolicy.getpId())) {
			return null;
		}
		List<ProductRelationPolicy> policies = super.find("WHERE pId=?", relationPolicy.getpId());
		return policies.isEmpty()?null:policies.get(0);
	}

	@Transactional
	public void createOrUpateProductPolicy(ProductRelationPolicy relationPolicy) throws ServiceException {
		ProductRelationPolicy relationPolicyUpdate = findByProductPolicy(relationPolicy);
		// "该策略已经存在！请新建一个策略或者点击更新策略"，更新策略
		if (relationPolicyUpdate != null) {
			BeanDup.dupNotNull(relationPolicy, relationPolicyUpdate);
			relationPolicyUpdate.setLastUpdate(System.currentTimeMillis());
			this.update(relationPolicyUpdate);
			return;
		}
		idGen.lock(productRelation);
		try {
			String policyId = idGen.generateStringId(productRelation);
			relationPolicy.setPolicyId(policyId);
			relationPolicy.setLastUpdate(System.currentTimeMillis());
			save(relationPolicy);
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			idGen.unlock(productRelation);
		}
	}

}
