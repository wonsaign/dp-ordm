package com.zeusas.dp.ordm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.dp.ordm.bean.MaterielBean;
import com.zeusas.dp.ordm.bean.PRPBean;
import com.zeusas.dp.ordm.bean.ProductSeriesBean;
import com.zeusas.dp.ordm.entity.AssociatedProduct;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductRelationPolicy;
import com.zeusas.dp.ordm.service.PRPolicyManager;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.security.auth.http.BasicController;

/**
 * 定义商品关联策略的Controller
 * 
 * @author fengx
 * @date 2016年12月13日 下午1:38:23
 */

@Controller
@RequestMapping("/productpolicy")
public class ProductRelationPolicyController extends BasicController {
	
	static Logger logger = LoggerFactory.getLogger(ProductRelationPolicyController.class);

	@Autowired
	private PRPolicyManager prpolicyManager;
	
	@Autowired
	private ProductManager productManager;
	
	@Autowired
	private DictManager dictManager;
	
	/**
	 * 初始化 显示分页数量的list
	 * @param request
	 * @return
	 */
	@RequestMapping("/init")
	public String init(HttpServletRequest request){
		List<ProductRelationPolicy> prpl=prpolicyManager.findall();
		List<PRPBean> prpbl=getPRPBean(prpl);
		request.setAttribute("PRPBeanList", prpbl);
		return "/page/productrelationpolicy";
	}
	
	/**
	 * 产品关联搜索 
	 * 返回长度为分页长度的List<Customer> 关键字name 
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping("/searchPRP")
	public String searchPRP(HttpServletRequest request,@RequestParam(value = "name", required = false) String name){
		List<ProductRelationPolicy> pl=prpolicyManager.findByName(name);
		int size=pl.size();
		if(pl.size()>10){
			pl=pl.subList(0, 10);
		}
		List<PRPBean> prpbl=getPRPBean(pl);
		request.setAttribute("PRPBeanList", prpbl);
		int max=(size%10==0)?(size/10):(size/10+1);
		request.setAttribute("max", max);
		request.setAttribute("key", name);
		request.setAttribute("page", 1);
		return "/page/productrelationpolicy";
	}
	
	@RequestMapping("/enable")
	public String enable(@RequestParam(value="parameter",required=false)String policyId){
		ProductRelationPolicy policy=new ProductRelationPolicy();
		try {
			BeanDup.dup(prpolicyManager.get(policyId), policy);
			policy.setStatus(ProductRelationPolicy.enable);
			prpolicyManager.update(policy);
		} catch (Exception e) {
			logger.error("获取用户错误", e);
		}
		return "/page/index";
	}
	
	@RequestMapping("/disable")
	public String disable(@RequestParam(value="parameter",required=false)String policyId){
		ProductRelationPolicy policy=new ProductRelationPolicy();
		try {
			BeanDup.dup(prpolicyManager.get(policyId), policy);
			policy.setStatus(ProductRelationPolicy.disable);
			prpolicyManager.update(policy);
		} catch (Exception e) {
			logger.error("获取用户错误", e);
		}
		return "/page/index";
	}
	
	/**
	 * 产品关联策略 分页ajax
	 * @param request
	 * @param page
	 * @param num
	 * @param key
	 * @return
	 */
	@RequestMapping(value = "/PRPpage")
	@ResponseBody
	public List<PRPBean> prpPage(HttpServletRequest request,
			@RequestParam(value="page",required=false)int page,
			@RequestParam(value="num",required=false)int num,
			@RequestParam(value="key",required=false)String key){
		List<ProductRelationPolicy> prpl=new ArrayList<ProductRelationPolicy>(num);
		request.setAttribute("page", page);
		if(!"".equals(key)){
			prpl=prpolicyManager.findByName(key);
			int size = prpl.size();
			int startNo = (page-1)*num; 
			int endNo = page*num<size?page*num:size;
			prpl=prpl.subList(startNo, endNo);
		}else {
			prpl=prpolicyManager.pagination(page, num);
		}
		List<PRPBean> prpbl=getPRPBean(prpl);
		return prpbl;
	}
	
	/**
	 * 创建按钮 跳到创建页
	 * @param request
	 * @return
	 */
	@RequestMapping("/addbtn")
	public String addbtn(HttpServletRequest request) {
		DSResponse dsResponse = new DSResponse();
		// 产品关联策略类型
		Dictionary d = dictManager.get(ProductRelationPolicy.TYPE_HARDCODE);
		List<Dictionary> dl = d.getChildren();
		request.setAttribute("PRPtype", dl);

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
		// 产品系列
		Dictionary productsSeriesRoot = dictManager.get(Product.PRODUCT_POSITIVE_SERIES);
		List<Dictionary> productsSeries = productsSeriesRoot.getChildren();
		String psName = productsSeriesRoot.getName();
		for (Dictionary dictionary : productsSeries) {
			if (dictionary.isActive()) {
				String id = dictionary.getHardCode();
				String name = dictionary.getName() + "(" + psName + ")";
				ProductSeriesBean bean = new ProductSeriesBean(id, name);
				beans.add(bean);
			}
		}

		// 产品系列
		Dictionary productsPresentRoot = dictManager.get(Product.PRODUCT_PRESENT_TYPE);
		List<Dictionary> productsPresents = productsPresentRoot.getChildren();
		String ppName = productsPresentRoot.getName();
		for (Dictionary dictionary : productsPresents) {
			if (dictionary.isActive()) {
				String id = dictionary.getHardCode();
				String name = dictionary.getName() + "(" + ppName + ")";
				ProductSeriesBean bean = new ProductSeriesBean(id, name);
				beans.add(bean);
			}
		}

		// 产品系列
		Dictionary productsMaterielRoot = dictManager.get(Product.PRODUCT_MATERIEL_TYPE);
		List<Dictionary> productsMateriels = productsMaterielRoot.getChildren();
		String pmName = productsMaterielRoot.getName();
		for (Dictionary dictionary : productsMateriels) {
			if (dictionary.isActive()) {
				String id = dictionary.getHardCode();
				String name = dictionary.getName() + "(" + pmName + ")";
				ProductSeriesBean bean = new ProductSeriesBean(id, name);
				beans.add(bean);
			}
		}
		
		dsResponse.getExtra().put("product", beans);
		dsResponse.getExtra().put("PRPtype", dl);
		dsResponse.getExtra().put("createflag", false);
		request.setAttribute("DSResponse", dsResponse);
		return "/page/productrelationpolicyadd";
	}
	
	
	/**
	 * 创建按钮 跳到创建页
	 * @param request
	 * @return
	 */
	@RequestMapping("/creatPRP")
	public String creatPRP(HttpServletRequest request,Model model,
			@ModelAttribute("prp")ProductRelationPolicy prp,
			@RequestParam(value="prpset",required=false)String prpset){
		Set<AssociatedProduct> products=new LinkedHashSet<AssociatedProduct>();
		products.addAll(JSON.parseArray(prpset, AssociatedProduct.class));
		prp.setAssociatedProducts(products);
		prp.setStatus(1);
		try {
//			if(prp.equals(prpolicyManager.get(prp.getpId()))){
//				model.addAttribute("message", "更新失败 已经创建");
//			}else{
				prpolicyManager.add(prp);
				model.addAttribute("message", "更新成功");
				model.addAttribute("createflag",true);
//			}
		} catch (ServiceException e) {
			logger.error("创建产品关联策略错误",e);
			PRPBean bean= getPRPBean(prp);
			model.addAttribute("message", "更新失败");
			model.addAttribute("createflag", false);
			model.addAttribute("policyBean", bean);
			return "/page/prpolicyupupdate";
		}finally {
			Dictionary d= dictManager.get(ProductRelationPolicy.TYPE_HARDCODE);
			List<Dictionary> dl= d.getChildren();
			request.setAttribute("PRPtype", dl);
		}
		PRPBean bean= getPRPBean(prp);
		model.addAttribute("policyBean", bean);
		return "/page/productrelationpolicyadd";
	}

	/**
	 * 创建按钮 跳到创建页
	 * @param request
	 * @return
	 */
	@RequestMapping("/updatebtn")
	public String updatebtn(HttpServletRequest request,@RequestParam(value = "policyId") String policyId){
		PRPBean bean=getPRPBean(policyId);
		Dictionary d= dictManager.get(ProductRelationPolicy.TYPE_HARDCODE);
		List<Dictionary> dl= d.getChildren();
		request.setAttribute("PRPtype", dl);
		request.setAttribute("policyBean", bean);
		return "/page/prpolicyupupdate";
	}
	
	/**
	 * 根据传过来的商品关联策略进行更新或者新建一个新的商品策略
	 * 
	 * @param policy
	 * @param request
	 * @return
	 */
	@RequestMapping("/update")
	public String updateOrSavePolicys(HttpServletRequest request,Model model,
			@ModelAttribute("prp")ProductRelationPolicy prp,
			@RequestParam(value="prpset",required=false)String prpset) {
		Set<AssociatedProduct> products=new LinkedHashSet<AssociatedProduct>();
		products.addAll(JSON.parseArray(prpset, AssociatedProduct.class));
		prp.setAssociatedProducts(products);
		try {
			if(prp.equals(prpolicyManager.get(prp.getPolicyId()))){
				model.addAttribute("message", "更新失败 未修改");
			}else{
				prpolicyManager.add(prp);
				model.addAttribute("message", "更新成功");
			}
		} catch (ServiceException e) {
			logger.error("更新产品关联策略错误",e);
			PRPBean bean= getPRPBean(prp.getPolicyId());
			model.addAttribute("message", "更新失败");
			model.addAttribute("policyBean", bean);
			return "/page/prpolicyupupdate";
		}finally {
			Dictionary d= dictManager.get(ProductRelationPolicy.TYPE_HARDCODE);
			List<Dictionary> dl= d.getChildren();
			request.setAttribute("PRPtype", dl);
		}
		PRPBean bean= getPRPBean(prp.getPolicyId());
		model.addAttribute("policyBean", bean);
		return "/page/prpolicyupupdate";
	}
	
	/**
	 * 在查找赠品物料时候ajax动态刷新
	 * @param pName
	 * @return
	 */
	@RequestMapping("/getmateriel")
	@ResponseBody
	public List<Product> getMaterielGift(@RequestParam(value = "pName",required = false) String pName){
		List<Product> pl = new ArrayList<Product>();
		try {
			List<Product> pltemp = productManager.findByName(pName);
			for (Product p : pltemp) {
				if (!productManager.isAuthenticProduct(p.getProductId())) {
					pl.add(p);
				}
			}
		} catch (ServiceException e) {
			logger.error("获取商品失败！", e);
		}
		return pl;
	}
	
	/**
	 * 在查找产品 系列时候ajax动态刷新
	 * List<ProductSeriesBean> 
	 *  id name
	 * @param pName
	 * @return
	 */
	@RequestMapping("/getproductseries")
	@ResponseBody
	public List<ProductSeriesBean> getProductSeries(@RequestParam(value = "pName",required = false) String pName){
		List<ProductSeriesBean> psbl=new ArrayList<ProductSeriesBean>();
		try {
			List<Product> pltemp = productManager.findByName(pName);
			for (Product p : pltemp) {
				ProductSeriesBean bean=new ProductSeriesBean();
				bean.setId(p.getProductId().toString());
				if (productManager.isAuthenticProduct(p.getProductId())) {
					bean.setName(p.getName());
					psbl.add(bean);
				}else{
					bean.setName(p.getName()+"(物料)");
					psbl.add(bean);
				}
			}
			List<Dictionary> dictemps= dictManager.lookupByName(pName);
			List<Dictionary> ld = dictemps.stream().filter(e -> e.isActive() == true)
					.collect(Collectors.toList());
			dictemps = ld;
			for (Dictionary d : dictemps) {
				ProductSeriesBean bean=new ProductSeriesBean();
				bean.setId(d.getHardCode());
				if(Product.PRODUCT_POSITIVE_SERIES.equals(d.getType())){
					bean.setName(d.getName());
					psbl.add(bean);
				}else if(Product.PRODUCT_PRESENT_TYPE.equals(d.getType())){
					bean.setName(d.getName()+"(赠品)");
					psbl.add(bean);
				}else if(Product.PRODUCT_MATERIEL_TYPE.equals(d.getType())){
					bean.setName(d.getName()+"(物料)");
					psbl.add(bean);
				}
			}
		} catch (ServiceException e) {
			logger.error("获取商品失败！", e);
		}
		return psbl;
	}
	
	/**
	 * 创建产品关联策略时校验产品策略是否存在
	 * @param pName
	 * @return
	 */
	@RequestMapping("/checkName")
	@ResponseBody
	public boolean checkPRPName(@RequestParam(value = "name",required = false) String name){
		return prpolicyManager.checkName(name);
	}
	
	
	private List<PRPBean> getPRPBean(List<ProductRelationPolicy> prpl){
		List<PRPBean> prplbl=new ArrayList<PRPBean>(prpl.size());
		for (ProductRelationPolicy prp : prpl) {
			PRPBean bean=getPRPBean(prp.getPolicyId());
			prplbl.add(bean);
		}
		return prplbl;
	}
	
	private PRPBean getPRPBean(ProductRelationPolicy PRP){
		ProductRelationPolicy prp=new ProductRelationPolicy();
		PRPBean prpBean=null;
		try {
			BeanDup.dupNotNull(PRP,prp);
			prpBean=new PRPBean(prp);
			//关联产品 系列的名字 从产品表 字典表取
			if(prp.PRODUCT_TYPE.equals(prp.getType())){
				Product p=productManager.get(Integer.parseInt(prp.getpId()));
				prpBean.setProductName(p.getName());
			}else if(prp.SERIAL_TYPE.equals(prp.getType())){
				Dictionary d= dictManager.lookUpByCode(Product.PRODUCT_MATERIEL_TYPE, prp.getpId());
				if(d==null){
					d= dictManager.lookUpByCode(Product.PRODUCT_PRESENT_TYPE, prp.getpId());
				}
				if(d==null){
					d= dictManager.lookUpByCode(Product.PRODUCT_POSITIVE_SERIES, prp.getpId());
				}
				prpBean.setProductName(d.getName());
			}else{
				prpBean.setProductName("全局");
			}
		} catch (Exception e) {
			logger.error("获取产品关联策略出错",e);
			e.printStackTrace();
		}finally {
			if(prpBean!=null){
				prpBean=setAPS(prpBean);
			}
		}
		return prpBean;
	
	}
	
	private PRPBean getPRPBean(String PolicyId){
		ProductRelationPolicy prp=new ProductRelationPolicy();
		PRPBean prpBean=null;
		try {
			BeanDup.dup(prpolicyManager.get(PolicyId),prp);
			prpBean=new PRPBean(prp);
			//关联产品 系列的名字 从产品表 字典表取
			if(ProductRelationPolicy.PRODUCT_TYPE.equals(prp.getType())){
				Product p=productManager.get(Integer.parseInt(prp.getpId()));
				prpBean.setProductName(p.getName());
			}else if(ProductRelationPolicy.SERIAL_TYPE.equals(prp.getType())){
				Dictionary d= dictManager.lookUpByCode(Product.PRODUCT_MATERIEL_TYPE, prp.getpId());
				if(d==null){
					d= dictManager.lookUpByCode(Product.PRODUCT_PRESENT_TYPE, prp.getpId());
				}
				if(d==null){
					d= dictManager.lookUpByCode(Product.PRODUCT_POSITIVE_SERIES, prp.getpId());
				}
				prpBean.setProductName(d.getName());
			}else{
				prpBean.setProductName("全局");
			}
		} catch (Exception e) {
			logger.error("获取产品关联策略出错 {}",prp,e);
		}finally {
			if(prpBean!=null){
				prpBean=setAPS(prpBean);
			}
		}
		return prpBean;
	}
	/**
	 * 封装bean 把产品pid:Coeff 封装成产品名:值
	 * AssociatedProducts String  显示
	 * Set<MaterielBean> id name Coeff  用于update时页面初始化
	 * @param prpBean
	 * @return
	 */
	private PRPBean setAPS(PRPBean prpBean){
		Set<AssociatedProduct> associatedProducts=prpBean.getProductRelationPolicy().getAssociatedProducts();
		Set<MaterielBean> materieSet=new HashSet<MaterielBean>();
		if(associatedProducts!=null&&!associatedProducts.isEmpty()){
			StringBuffer strb=new StringBuffer();
			for (AssociatedProduct ap : associatedProducts) {
				try {
					Product p=productManager.get(ap.getPid());
					strb.append(p.getName()+":"+ap.getCoeff()+", ");
					MaterielBean mb=new MaterielBean();
					mb.setPid(ap.getPid());
					mb.setName(p.getName());
					mb.setCoeff(ap.getCoeff());
					
					materieSet.add(mb);
				} catch (Exception e) {
					logger.error("封装产品关联策略 国庆产品错误",e);
					e.printStackTrace();
				}
			}
			String str=strb.toString();
			if(str.endsWith(",")){
				str=str.substring(0, str.length()-1);
			}
			prpBean.setAssociatedProducts(str);
			prpBean.setMaterielBean(materieSet);
		}else {
			prpBean.setAssociatedProducts("");
			prpBean.setMaterielBean(materieSet);
		}
		return prpBean;
	}

}
