package com.zeusas.dp.ordm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.dp.ordm.bean.PricePolicyBean;
import com.zeusas.dp.ordm.entity.CustomerPricePolicy;
import com.zeusas.dp.ordm.service.PricePolicyManager;

@Controller
@RequestMapping("/pricePolicy")
public class PricePolicyContronller {

	static Logger logger = LoggerFactory.getLogger(PricePolicyContronller.class);
	@Autowired
	PricePolicyManager ppm;
	@Autowired
	DictManager dictm;
	
	@RequestMapping("/init")
	public String init(HttpServletRequest request){
		List<CustomerPricePolicy> ppl=ppm.findAll();
		List<PricePolicyBean> ppbl=getBeans(ppl);
		request.setAttribute("PricePolicyBeanList", ppbl);
		return "/page/customerpricepolicy";
	}
	
	@RequestMapping("/updatebtn")
	public String updateBtn(@RequestParam(value = "typeId") Integer typeId,HttpServletRequest request){
		PricePolicyBean ppb=getBean(typeId);
		request.setAttribute("PricePolicyBean", ppb);
		return "/page/customerpricepolicyadd";
	}
	
	@RequestMapping("/update")
	public String update(HttpServletRequest request,Model model,
			@ModelAttribute("customerPricePolicy")CustomerPricePolicy customerPricePolicy){
		CustomerPricePolicy cp= ppm.getPolicy(customerPricePolicy.getCustomerTypeId());
		try {
			if(cp.getDiscount().equals(customerPricePolicy.getDiscount())
					&&cp.getDescription().equals(customerPricePolicy.getDescription())){
				model.addAttribute("message", "更新失败 未修改");
			}else{
				ppm.update(customerPricePolicy);
				model.addAttribute("message", "更新成功");
			}
		} catch (Exception e) {
			logger.error("获取价格策略错误",e);
			model.addAttribute("message", "更新失败");
			PricePolicyBean ppb=getBean(customerPricePolicy);
			request.setAttribute("PricePolicyBean", ppb);
			return "/page/customerpricepolicyadd";

		}
		PricePolicyBean ppb=getBean(customerPricePolicy.getCustomerTypeId());
		request.setAttribute("PricePolicyBean", ppb);
		return "/page/customerpricepolicyadd";
	}
	private List<PricePolicyBean> getBeans(List<CustomerPricePolicy> ppl){
		List<PricePolicyBean> ppbl=new ArrayList<>(ppl.size());
		for (CustomerPricePolicy cpp : ppl) {
			PricePolicyBean ppb=getBean(cpp.getCustomerTypeId());
			ppbl.add(ppb);
		}
		return ppbl;
	} 
	
	private PricePolicyBean getBean (Integer typeId){
		CustomerPricePolicy cpp=new CustomerPricePolicy();
		Dictionary d=null;
		try {
			BeanDup.dup(ppm.getPolicy(typeId), cpp);
			d= dictm.lookUpByCode(CustomerPricePolicy.customerTypeId_root, cpp.getCustomerTypeId().toString());
		} catch (Exception e) {
			logger.error("获取价格策略错误",e);
		}
		PricePolicyBean ppb=new PricePolicyBean(cpp);
		if(d!=null){
			ppb.setCustomerType(d.getName());
		}
		return ppb;
	}
	
	private PricePolicyBean getBean (CustomerPricePolicy customerPricePolicy){
		CustomerPricePolicy cpp=new CustomerPricePolicy();
		Dictionary d=null;
		try {
			BeanDup.dupNotNull(customerPricePolicy, cpp);
			d= dictm.lookUpByCode(CustomerPricePolicy.customerTypeId_root, customerPricePolicy.getCustomerTypeId().toString());
		} catch (Exception e) {
			logger.error("获取价格策略错误",e);
		}
		PricePolicyBean ppb=new PricePolicyBean(cpp);
		if(d!=null){
			ppb.setCustomerType(d.getName());
		}
		return ppb;
	}
}
