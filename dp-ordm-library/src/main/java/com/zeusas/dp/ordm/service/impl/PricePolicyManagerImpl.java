package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeusas.common.service.OnStartApplication;
import com.zeusas.core.service.ServiceException;
import com.zeusas.dp.ordm.entity.CustomerPricePolicy;
import com.zeusas.dp.ordm.service.CustomerPricePolicyService;
import com.zeusas.dp.ordm.service.PricePolicyManager;

/**
 * 实现客户类型价格策略的管理
 * 
 * @author fengx
 * @date 2016年12月20日 下午1:34:36
 */
@Service
public class PricePolicyManagerImpl extends OnStartApplication implements PricePolicyManager {

	final Map<Integer, CustomerPricePolicy> Map_PricePolicy;

	@Autowired
	private CustomerPricePolicyService pricePolicyService;

	public PricePolicyManagerImpl(){
		 Map_PricePolicy = new HashMap<>();
	}
	
	@Override
	public void reload() {
		Map_PricePolicy.clear();
		List<CustomerPricePolicy> policies = pricePolicyService.findAll();
		if (policies == null || policies.isEmpty()) {
			return;
		}
		for (CustomerPricePolicy policy : policies) {
			Map_PricePolicy.put(policy.getCustomerTypeId(), policy);
		}
	}

	@Override
	public boolean update(CustomerPricePolicy pricePolicy) throws ServiceException {
		int typeId = pricePolicy.getCustomerTypeId();
		if (typeId == 0 || Map_PricePolicy.get(typeId) == null) {
			return false;
		}
		CustomerPricePolicy updatePolicy = Map_PricePolicy.get(typeId);
		if (pricePolicy.getDiscount() != null && pricePolicy.getDiscount() > 0.00) {
			updatePolicy.setDiscount(pricePolicy.getDiscount());
		}
		if (pricePolicy.getMaterialDiscount() != null && pricePolicy.getMaterialDiscount() > 0.00) {
			updatePolicy.setMaterialDiscount(pricePolicy.getMaterialDiscount());
		}
		if (pricePolicy.getDescription() != null) {
			updatePolicy.setDescription(pricePolicy.getDescription());
		}
		updatePolicy.setLastUpdate(System.currentTimeMillis());
		pricePolicyService.update(updatePolicy);
		return true;
	}

	@Override
	public CustomerPricePolicy getPolicy(Integer typeId) {
		return typeId == null ? null : Map_PricePolicy.get(typeId);
	}

	@Override
	public List<CustomerPricePolicy> findAll() {
		return pricePolicyService.findAll();
	}
	
	@Override
	public List<Integer> findAllIds() {
		List<CustomerPricePolicy> policies=findAll();
		List<Integer>ids=new ArrayList<>();
		for(CustomerPricePolicy policy:policies){
			ids.add(policy.getCustomerTypeId());
		}
		return ids;
	}

	@Override
	public void onStartLoad() {
		if (Map_PricePolicy.size() == 0) {
			reload();
		}
	}

	@Override
	public List<CustomerPricePolicy> findByCustomerType(Integer typeId) {
		return Map_PricePolicy.values().stream()//
				.filter(e -> Objects.isNull(typeId) || e.getCustomerTypeId().equals(typeId))//
				.collect(Collectors.toList());
	}
}
