package com.zeusas.dp.ordm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zeusas.common.service.DictManager;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.entity.ActivityLimit;
import com.zeusas.dp.ordm.service.ActivityLimitManager;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.FixedPriceManager;
import com.zeusas.dp.ordm.service.MaterialTemplateManager;
import com.zeusas.dp.ordm.service.MonthPresentManager;
import com.zeusas.dp.ordm.service.PRPolicyManager;
import com.zeusas.dp.ordm.service.PricePolicyManager;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.ReserveProductManager;
import com.zeusas.dp.ordm.service.UserCustomerManager;
import com.zeusas.dp.ordm.service.WarehouseManager;
import com.zeusas.security.auth.service.AuthCenterManager;

public class CacheLoader {
	final static Logger logger = LoggerFactory.getLogger(CacheLoader.class);
	
	@RequestMapping("/dict")
	@ResponseBody
	public DSResponse loadDict() {
		DictManager dm = AppContext.getBean(DictManager.class);
		dm.reload();
		return DSResponse.OK;
	}
	
	@RequestMapping("/auth_{id}")
	@ResponseBody
	public DSResponse loadGroup(@PathVariable(value = "id") String id) {
		AuthCenterManager dm = AppContext.getBean(AuthCenterManager.class);
		switch (id) {
		case "group":
			dm.loadGroup();
			break;
		case "org":
			dm.loadOrgUnit();
			break;
		case "role":
			dm.loadRole();
			break;
		case "perm":
			dm.permSettings();
			break;
		case "user":
			dm.loadAuthUser();
			break;
		case "all":
		default:
			dm.loadAll();
		}
		return DSResponse.OK;
	}
	
	@RequestMapping("/counter")
	@ResponseBody
	public DSResponse loadCounter(){
		CounterManager cm = AppContext.getBean(CounterManager.class);
		cm.load();
		return DSResponse.OK;
	}
	
	@RequestMapping("/customer")
	@ResponseBody
	public DSResponse loadCustomer(){
		CustomerManager cm  = AppContext.getBean(CustomerManager.class);
		cm.load();
		return DSResponse.OK;
	}
	
	@RequestMapping("/policy")
	@ResponseBody
	public DSResponse loadPolicy() {
		PricePolicyManager pm = AppContext.getBean(PricePolicyManager.class);
		pm.reload();
		return DSResponse.OK;
	}

	@RequestMapping("/product")
	@ResponseBody
	public DSResponse loadProduct(){
		ProductManager pm = AppContext.getBean(ProductManager.class);
		pm.reload();
		return DSResponse.OK;
	}
	
	@RequestMapping("/fixedPrice")
	@ResponseBody
	public DSResponse loadfixedPrice(){
		FixedPriceManager fpm = AppContext.getBean(FixedPriceManager.class);
		fpm.reload();
		return DSResponse.OK;
	}
	
	
	@RequestMapping("/prpolicy")
	@ResponseBody
	public DSResponse loadPRPolicy(){
		PRPolicyManager prpm = AppContext.getBean(PRPolicyManager.class);
		prpm.reload();
		return DSResponse.OK;
	}
	
	@RequestMapping("/usercust")
	@ResponseBody
	public DSResponse loadUserCustomer(){
		UserCustomerManager ucm = AppContext.getBean(UserCustomerManager.class);
		ucm.load();
		return DSResponse.OK;
	}
	
	@RequestMapping("/materialTemplate")
	@ResponseBody
	public DSResponse loadMaterialTemplate(){
		MaterialTemplateManager fpm = AppContext.getBean(MaterialTemplateManager.class);
		fpm.load();
		return DSResponse.OK;
	}
	
	@RequestMapping("/monthPresent")
	@ResponseBody
	public DSResponse loadMonthPresent(){
		MonthPresentManager fpm = AppContext.getBean(MonthPresentManager.class);
		fpm.load();
		return DSResponse.OK;
	}
	
	@RequestMapping("/warehouse")
	@ResponseBody
	public DSResponse loadWarehouse(){
		WarehouseManager fpm = AppContext.getBean(WarehouseManager.class);
		fpm.load();
		return DSResponse.OK;
	}
	
	@RequestMapping("/reserveProduct")
	@ResponseBody
	public DSResponse loadReserveProduct(){
		ReserveProductManager fpm = AppContext.getBean(ReserveProductManager.class);
		fpm.load();
		return DSResponse.OK;
	}
	
	@RequestMapping("/limit")
	@ResponseBody
	public DSResponse loadActivityLimit(){
		ActivityLimitManager fpm = AppContext.getBean(ActivityLimitManager.class);
		fpm.load();
		return DSResponse.OK;
	}
}
