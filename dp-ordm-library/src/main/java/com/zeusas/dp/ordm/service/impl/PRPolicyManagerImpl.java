package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.service.OnStartApplication;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductRelationPolicy;
import com.zeusas.dp.ordm.service.PRPolicyManager;
import com.zeusas.dp.ordm.service.ProductRelationPolicyService;

@Service
public class PRPolicyManagerImpl extends OnStartApplication implements
		PRPolicyManager {

	final static String global = "global";

	final static String productRelation = "106";

	@Autowired
	private ProductRelationPolicyService policyService;

	/** 字典基于DID 索引 */
	private final Map<String, ProductRelationPolicy> global_dicts;
	private final Map<String, ProductRelationPolicy> serial_dicts;
	private final Map<Integer, ProductRelationPolicy> product_dicts;
	private final Map<String, ProductRelationPolicy> name_dicts;
	private final Map<String, ProductRelationPolicy> id_dicts;

	public PRPolicyManagerImpl() {
		global_dicts = new ConcurrentHashMap<>();
		serial_dicts = new ConcurrentHashMap<>();
		product_dicts = new ConcurrentHashMap<>();
		name_dicts = new ConcurrentHashMap<>();
		id_dicts = new ConcurrentHashMap<>();
	}

	@Override
	public void reload() {
		global_dicts.clear();
		serial_dicts.clear();
		product_dicts.clear();
		name_dicts.clear();
		id_dicts.clear();
		
		List<ProductRelationPolicy> policies = policyService.findAll();
		for (ProductRelationPolicy policy : policies) {
			// 先判断是不是产品等级
			if ("product".equals(policy.getType())) {
				product_dicts.put(Integer.parseInt(policy.getpId()), policy);
			}
			if (policy.getType().equals("serial")) {
				serial_dicts.put(policy.getpId(), policy);
			}
			if (policy.getType().equals("global")) {
				global_dicts.put(global, policy);
			}
			if (policy.getName() != null) {
				name_dicts.put(policy.getName(), policy);
			}
			if (policy.getPolicyId() != null) {
				id_dicts.put(policy.getPolicyId(), policy);
			}
		}
	}

	@Override
	public void add(ProductRelationPolicy policy) throws ServiceException {
		policy.setLastUpdate(System.currentTimeMillis());
		policyService.createOrUpateProductPolicy(policy);
		reload();
	}

	@Override
	public void update(ProductRelationPolicy policy) {
		policy.setLastUpdate(System.currentTimeMillis());
		policyService.update(policy);
		reload();
	}

	@Override
	public ProductRelationPolicy get(Product product) {
		Integer pid = product.getProductId();
		if (product_dicts.containsKey(pid)) {
			return product_dicts.get(pid);
		}
		String classId = product.getFitemClassId();
		if (product.getFitemClassId() != null
				&& serial_dicts.containsKey(classId)) {
			return serial_dicts.get(classId);
		}
		if (global_dicts.containsKey(global)) {
			return global_dicts.get(global);
		}
		return null;
	}

	public ProductRelationPolicy get(String policyId) {
		if (id_dicts.containsKey(policyId)) {
			return id_dicts.get(policyId);
		}
		return null;
	}

	@Override
	public List<Dictionary> findByHardCode(String type) {
		DictManager dictManager = AppContext.getBean(DictManager.class);
		return dictManager.get(type).getChildren();
	}

	public List<ProductRelationPolicy> findByName(String Name) {
		List<ProductRelationPolicy> pl = new ArrayList<ProductRelationPolicy>(
				name_dicts.size());
		Set<String> keys = name_dicts.keySet();
		for (String key : keys) {
			if (key.contains(Name)) {
				pl.add(name_dicts.get(key));
			}
		}
		return pl;
	}

	public List<ProductRelationPolicy> pagination(int page, int num) {
		int size = name_dicts.size();
		int startNo = (page - 1) * num;
		int endNo = page * num < size ? page * num : size;
		if (startNo >= size) {
			return new ArrayList<ProductRelationPolicy>(0);
		}
		final List<ProductRelationPolicy> pl = new ArrayList<ProductRelationPolicy>(
				num);
		int i = 0;
		for (ProductRelationPolicy p : name_dicts.values()) {
			if (i >= endNo) {
				break;
			} else if (i >= startNo) {
				pl.add(p);
			}
			i++;
		}
		return pl;
	}

	public boolean checkName(String Name) {
		return name_dicts.containsKey(Name);
	}

	public List<ProductRelationPolicy> findall() {
		List<ProductRelationPolicy> pl = new ArrayList<ProductRelationPolicy>(
				name_dicts.size());
		pl.addAll(name_dicts.values());
		return pl;
	}

	@Override
	public void onStartLoad() {
		if (product_dicts.size() == 0) {
			reload();
		}
	}
}
