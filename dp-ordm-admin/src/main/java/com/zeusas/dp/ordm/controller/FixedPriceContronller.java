package com.zeusas.dp.ordm.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.common.utils.Status;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.bean.FixedPriceBean;
import com.zeusas.dp.ordm.bean.ProductSeriesBean;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.FixedPrice;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.service.FixedPriceManager;
import com.zeusas.dp.ordm.service.ProductManager;

@Controller
@RequestMapping("/fixedPrice")
public class FixedPriceContronller {

	static Logger logger = LoggerFactory.getLogger(FixedPriceContronller.class);
	@Autowired
	FixedPriceManager fixedPriceManager;
	@Autowired
	DictManager dictManager;
	@Autowired
	ProductManager productManager;

	/**
	 * 页面初始化
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/init")
	public String init(HttpServletRequest request) {
		DSResponse dsResponse = new DSResponse();
		List<FixedPrice> fixedPrices = new ArrayList<>(fixedPriceManager.findall());
		dsResponse.setData(getBeans(fixedPrices));
		request.setAttribute("DSResponse", dsResponse);
		return "/page/fixedprice";
	}

	/**
	 * 创建按钮 初始化创建页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/createBtn")
	public String initCreate(HttpServletRequest request) {
		DSResponse dsResponse = new DSResponse();
		// 从字典中获取客户类型
		Dictionary dict = dictManager.get(Customer.customerTypeDictId);
		List<Dictionary> dicts = dict.getChildren();
		// 所有产品
		List<ProductSeriesBean> beans = new ArrayList<>();
		List<Product> products = productManager.findAllAvaible();
		for (Product product : products) {
			if (product.isAvalible()) {
				String id = product.getProductId().toString();
				String name = product.getName() + "(" + product.getTypeName() + ")";
				ProductSeriesBean bean = new ProductSeriesBean(id, name);
				beans.add(bean);
			}
		}
		// 装配
		dsResponse.getExtra().put("customerType", dicts);
		dsResponse.getExtra().put("product", beans);
		dsResponse.getExtra().put("createflag", false);
		request.setAttribute("DSResponse", dsResponse);
		return "/page/fixedpriceadd";
	}

	/**
	 * 更新按钮 初始化更新页
	 * 
	 * @param request
	 * @param productId
	 * @return
	 */
	@RequestMapping("/updateBtn")
	public String initUpdate(HttpServletRequest request,
			@RequestParam(value = "productId", required = false) Integer productId) {
		DSResponse dsResponse = new DSResponse();
		// 从字典中获取客户类型
		Dictionary dict = dictManager.get(Customer.customerTypeDictId);
		List<Dictionary> dicts = dict.getChildren();
		FixedPrice fixedPrice = fixedPriceManager.get(productId);
		// 所有产品
		List<ProductSeriesBean> beans = new ArrayList<>();
		List<Product> products = productManager.findAllAvaible();
		for (Product product : products) {
			if (product.isAvalible()) {
				String id = product.getProductId().toString();
				String name = product.getName() + "(" + product.getTypeName() + ")";
				ProductSeriesBean bean = new ProductSeriesBean(id, name);
				beans.add(bean);
			}
		}
		// 装配
		dsResponse.setData(fixedPrice);
		dsResponse.getExtra().put("product", beans);
		dsResponse.getExtra().put("customerType", dicts);
		request.setAttribute("DSResponse", dsResponse);
		return "/page/fixedpriceupdate";
	}

	/**
	 * 在查找产品 系列时候ajax动态刷新 List<ProductSeriesBean> id:产品id name：产品名
	 * 
	 * @param pName
	 * @return
	 */
	@RequestMapping("/getProduct")
	@ResponseBody
	public List<ProductSeriesBean> getProduct(@RequestParam(value = "pName", required = false) String pName) {
		List<ProductSeriesBean> beans = new ArrayList<>();
		List<Product> products = productManager.findByName(pName);
		for (Product product : products) {
			if (product.isAvalible()) {
				String id = product.getProductId().toString();
				String name = product.getName() + "(" + product.getTypeName() + ")";
				ProductSeriesBean bean = new ProductSeriesBean(id, name);
				beans.add(bean);
			}
		}
		return beans;
	}

	/**
	 * 创建一口价
	 * 
	 * @param request
	 * @param fixedPrice
	 * @return
	 */
	@RequestMapping("/createFixedPrice")
	public String createFixedPrice(HttpServletRequest request, FixedPrice fixedPrice) {
		DSResponse dsResponse = new DSResponse();
		if (fixedPriceManager.get(fixedPrice.getProductId()) != null) {
			// 已经存在，创建失败
			dsResponse.addMessage("一口价策略已经存在，创建失败。");
		}
		if (fixedPrice.getCustomerTypeId().isEmpty()) {
			dsResponse.addMessage("客户类型不能为空，创建失败。");
		}
		if (fixedPrice.getFix() == null) {
			dsResponse.addMessage("一口价类型不能为空，创建失败。");
		}
		if(fixedPrice.getFix()&&fixedPrice.getPrice()==null){
			dsResponse.addMessage("类型为固定价格时价格不能为空。");
		}
		if (!fixedPrice.getFix() && fixedPrice.getDiscount() == null) {
			dsResponse.addMessage("类型为固定折扣时时折扣不能为空。");
		}
		if (fixedPrice.getCostRatio() == null) {
			dsResponse.addMessage("是否走不能为空，创建失败。");
		}
		try {
			if(fixedPrice.getFix()){
				fixedPrice.setDiscount(1.0);
			}else{
				fixedPrice.setPrice(0.0);
			}
			fixedPriceManager.creatFixedPrice(fixedPrice);
			dsResponse.setStatus(Status.SUCCESS);
			dsResponse.getExtra().put("createflag", true);
		} catch (Exception e) {
			logger.error("创建一口价策略错误", e);
		}
		// 所有客户类型
		Dictionary dict = dictManager.get(Customer.customerTypeDictId);
		Assert.notNull(dict,"从字典获取客户类型为空");
		List<Dictionary> dicts = dict.getChildren();

		dsResponse.getExtra().put("customerType", dicts);
		dsResponse.setData(fixedPrice);
		request.setAttribute("DSResponse", dsResponse);
		return "/page/fixedpriceadd";
	}

	/**
	 * 更新一口价
	 * 
	 * @param request
	 * @param fixedPrice
	 * @return
	 */
	@RequestMapping("/updateFixedPrice")
	public String updateFixedPrice(HttpServletRequest request, FixedPrice fixedPrice) {
		DSResponse dsResponse = new DSResponse();

		FixedPrice cacheFp = fixedPriceManager.get(fixedPrice.getProductId());
		FixedPrice tempFp = new FixedPrice();
		BeanDup.dup(cacheFp, tempFp);
		BeanDup.dupNotNull(fixedPrice, tempFp, "price", "discount", "pricePolicy", "fix", "suitAll", "customerTypeId");

		if (fixedPrice.getCustomerTypeId().isEmpty()) {
			dsResponse.setMessage("客户类型不能为空，更新失败");
		}
		if (fixedPrice.getPrice() == null) {
			dsResponse.setMessage("价格不能为空，更新失败");
		}
		if (cacheFp.equals(tempFp)) {
			dsResponse.setMessage("没有数据改变，更新失败");
		}
		try {
			fixedPriceManager.update(fixedPrice);
			dsResponse.setStatus(Status.SUCCESS);
		} catch (Exception e) {
			logger.error("更新一口价策略错误", e);
		}
		// 所有客户类型
		Dictionary dict = dictManager.get(Customer.customerTypeDictId);
		Assert.notNull(dict);
		List<Dictionary> dicts = dict.getChildren();

		dsResponse.getExtra().put("customerType", dicts);
		dsResponse.setData(fixedPrice);
		request.setAttribute("DSResponse", dsResponse);
		return "/page/fixedpriceupdate";
	}

	/**
	 * 禁用
	 * 
	 * @param request
	 * @param productId
	 * @return
	 */
	@RequestMapping("/diable")
	@ResponseBody
	public DSResponse diable(HttpServletRequest request,
			@RequestParam(value = "productId", required = false) Integer productId) {
		DSResponse dsResponse = new DSResponse();
		FixedPrice fixedPrice = fixedPriceManager.get(productId);
		if (fixedPrice == null) {
			dsResponse.setMessage("禁用失败,获取策略错误");
			return dsResponse;
		}
		fixedPrice.setStatus(false);
		try {
			fixedPriceManager.update(fixedPrice);
			dsResponse.setStatus(Status.SUCCESS);
			dsResponse.setMessage("禁用成功");
		} catch (Exception e) {
			logger.error("禁用失败", e);
		}
		return dsResponse;
	}

	/**
	 * 启用
	 * 
	 * @param request
	 * @param productId
	 * @return
	 */
	@RequestMapping("/enable")
	@ResponseBody
	public DSResponse enable(HttpServletRequest request,
			@RequestParam(value = "productId", required = false) Integer productId) {
		DSResponse dsResponse = new DSResponse();
		FixedPrice fixedPrice = fixedPriceManager.get(productId);
		if (fixedPrice == null) {
			dsResponse.setMessage("启用失败,获取策略错误");
			return dsResponse;
		}
		fixedPrice.setStatus(true);
		try {
			fixedPriceManager.update(fixedPrice);
			dsResponse.setStatus(Status.SUCCESS);
			dsResponse.setMessage("启用成功");
		} catch (Exception e) {
			logger.error("启用失败", e);
		}
		return dsResponse;
	}

	/**
	 * 搜索
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping("/search")
	public DSResponse search(HttpServletRequest request, @RequestParam(value = "name", required = false) String name) {
		DSResponse dsResponse = new DSResponse();
		List<FixedPriceBean> beans;
		if (StringUtil.isEmpty(name)) {
			beans = getBeans(fixedPriceManager.findall());
		} else {
			beans = getBeans(fixedPriceManager.findByProductName(name));
		}
		dsResponse.setData(beans);
		return dsResponse;
	}

	/**
	 * 封装bean
	 * 
	 * @param fixedPrices
	 * @return
	 */
	private List<FixedPriceBean> getBeans(Collection<FixedPrice> fixedPrices) {
		List<FixedPriceBean> beans = new ArrayList<>();
		for (FixedPrice fixedPrice : fixedPrices) {
			FixedPriceBean bean = new FixedPriceBean(fixedPrice);
			beans.add(bean);
		}
		return beans;
	}
}
