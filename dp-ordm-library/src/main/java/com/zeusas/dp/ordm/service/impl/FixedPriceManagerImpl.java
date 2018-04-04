package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeusas.common.service.OnStartApplication;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.dp.ordm.entity.CustomerPricePolicy;
import com.zeusas.dp.ordm.entity.FixedPrice;
import com.zeusas.dp.ordm.service.CustomerPricePolicyService;
import com.zeusas.dp.ordm.service.FixedPriceManager;
import com.zeusas.dp.ordm.service.FixedPriceService;

/***
 * 将一口价策略加载到内存
 * 
 * @author fengx
 * @date 2017年5月27日 上午9:59:13
 */
@Service
public class FixedPriceManagerImpl extends OnStartApplication implements FixedPriceManager {

	@Autowired
	private FixedPriceService service;
	@Autowired
	private CustomerPricePolicyService pricePolicyService;
	/** key=产品id value=客户类型集合 */
	final Map<Integer, Set<String>> map_productid;
	/** key=客户类型id value=固定价格策略集合 */
	/** <cutomerTypeId<productId,FixedPrice>> */
	final Map<Integer, Map<Integer, FixedPrice>> map_customer;

	final Map<Integer, FixedPrice> allFixedPrice;

	public FixedPriceManagerImpl() {
		allFixedPrice = new HashMap<>();
		map_productid = new HashMap<>();
		map_customer = new HashMap<>();
	}

	@Override
	public void onStartLoad() {
		reload();
	}
	
	@Override
	public void reload() {
		List<Integer> allTypeIds = new ArrayList<>();
		List<CustomerPricePolicy> policies = pricePolicyService.findAll();
		for (CustomerPricePolicy policy : policies) {
			Integer customerTypeId = policy.getCustomerTypeId();
			allTypeIds.add(customerTypeId);

			if (!map_customer.containsKey(customerTypeId)) {
				map_customer.put(customerTypeId, new HashMap<>());
			}

		}

		List<FixedPrice> fixedPrices = service.findAll();

		for (FixedPrice fixedPrice : fixedPrices) {
			Integer productId = fixedPrice.getProductId();
			
			allFixedPrice.put(productId, fixedPrice);
			
			Set<String> customerTypeId = fixedPrice.getCustomerTypeId();
			map_productid.put(fixedPrice.getProductId(), customerTypeId);

			for (Integer typeId : allTypeIds) {
				if (customerTypeId.contains(typeId.toString())) {
					map_customer.get(typeId).put(productId, fixedPrice);
				}
			}
		}
	}

	@Override
	public Map<Integer, FixedPrice> getFixedPrices(Integer cutomterTypeId) {
		Map<Integer, FixedPrice> fixedPrices = map_customer.get(cutomterTypeId);
		return fixedPrices == null ? new HashMap<>() : fixedPrices;
	}

	@Override
	public List<Integer> getCutomtersByProduct(Integer productId) {
		List<Integer> ids = new ArrayList<>();
		Set<String> customerTypes=map_productid.get(productId)==null?new HashSet<>():map_productid.get(productId);
		for (String customerTypeId : customerTypes) {
			ids.add(Integer.parseInt(customerTypeId));
		}
		return ids;
	}

	@Override
	public FixedPrice getFixedPrice(Integer productid, Integer cutomterTypeId) {
		Map<Integer, FixedPrice> prices = getFixedPrices(cutomterTypeId);
		return prices.get(productid);
	}

	@Override
	public boolean creatFixedPrice(FixedPrice fixedPrice) throws ServiceException {
		boolean createFlag = false;
		if (!getCutomtersByProduct(fixedPrice.getProductId()).isEmpty()) {
			throw new ServiceException("创建一口价错误:一口价策略已存在");
		}
		if (fixedPrice.getProductId() == null || //
				fixedPrice.getProductName() == null || //
				fixedPrice.getFix() == null || //
				fixedPrice.getCustomerTypeId() == null) {
			throw new ServiceException("创建一口价错误:ProductId ProductName Fix  CustomerTypeId不能为空");
		}
		if(fixedPrice.getFix()&&fixedPrice.getPrice()==null){
			throw new ServiceException("创建一口价错误: Fix为true时价格不能为空");
		}
		if (!fixedPrice.getFix() && fixedPrice.getDiscount() == null) {
			throw new ServiceException("创建一口价错误: Fix为false时折扣不能为空");
		}
		try {
			fixedPrice.setStatus(true);
			service.createFixedPrice(fixedPrice);
			createFlag = true;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		reload();
		return createFlag;
	}

	@Override
	public boolean update(FixedPrice fixedPrice) throws ServiceException {
		boolean flag = false;
		Integer productId=fixedPrice.getProductId();
		FixedPrice dbfp =service.getByProductId(productId);
		
		BeanDup.dupNotNull(fixedPrice, dbfp, "price","discount","customerTypeId","pricePolicy","fix","status","suitAll","costRatio","updator");
		
		if(fixedPrice.getCustomerTypeId().isEmpty()){
			Set<String> customerTypeIds= new LinkedHashSet<>();
			dbfp.setCustomerTypeId(customerTypeIds);
			dbfp.setStatus(false);
		}
		dbfp.setLastUpdate(System.currentTimeMillis());
		try {
			service.update(dbfp);
			flag=true;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		reload();
		return flag;
	}

	@Override
	public FixedPrice get(Integer productid) {
		return allFixedPrice.get(productid);
	}

	@Override
	public Collection<FixedPrice> findall() {
		return allFixedPrice.values();
	}

	@Override
	public List<FixedPrice> findByProductName(String name) {
		List<FixedPrice> fixedPrices= new ArrayList<>();
		for (FixedPrice fixedPrice : allFixedPrice.values()) {
			if(fixedPrice.getProductName().indexOf(name)>-1){
				fixedPrices.add(fixedPrice);
			}
		}
		return fixedPrices;
	}

}
