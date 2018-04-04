package com.zeusas.dp.ordm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.utils.VfsDocClient;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppConfig;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.bean.OrderBean;
import com.zeusas.dp.ordm.bean.ProductBean;
import com.zeusas.dp.ordm.bean.SeriesBean;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.utils.UriUtil;

/**
 * 
 * @author fx 定义产品的的Controller 包含了产品的增删改查
 */
@Controller
@RequestMapping("/product")
public class ProductController {
	static Logger logger = LoggerFactory.getLogger(OrgUnitController.class);
	@Autowired
	private ProductManager pManagerService;
	//调用字典管理类 对产品类型进行维护
	@Autowired
	private DictManager dictManager;
	
	
	@RequestMapping("/init")
	public String initProduct(HttpServletRequest request,Model model) {
		model.addAttribute("ldicts", dictManager.get("107").getChildren());
		List<ProductBean> productBeans=new ArrayList<>(); 
		List<Product> pros = pManagerService.findAllAvaible();
		for (Product p : pros) {
			if (Product.TYPEID_METERIAL.equals(p.getTypeId())) {
				ProductBean productBean=new ProductBean(p);
				productBeans.add(productBean);
			}
		}
		model.addAttribute("productBeans", productBeans);
		return "/page/product";
	}
	
	/**
	 * Magic Number!
	 * 
	 * @param name
	 * @param model
	 * @param typeid
	 * @return
	 */
	// 根据产品的名称获取某些产品(模糊查询)
	@RequestMapping(value = "/product")
	@ResponseBody
	public List<ProductBean> findByName(@RequestParam(value = "name", required = false) String name, Model model,
			@RequestParam(value = "typeid", required = false) String typeid,
			@RequestParam(value = "avalible", required = false) String avalible) {
		// What is "107"?
		List<Product> pro = new ArrayList<Product>();
		List<ProductBean> productBeans=new ArrayList<>(); 
		if (Strings.isNullOrEmpty(typeid)){
			return productBeans;
		}
		try {
			if ("1".equals(avalible)) {
				pro=pManagerService.fingBySerialAndName(typeid, name);
			} else {
				if (StringUtil.isEmpty(name)) {
					List<Product> pros = pManagerService
							.findAllDisabledProduct();
					for (Product p : pros) {
						if (typeid.equals(p.getTypeId())) {
							pro.add(p);
						}
					}
				} else {
					List<Product> pros = pManagerService
							.findDisabledProduct(name);
					for (Product p : pros) {
						if (typeid.equals(p.getTypeId())) {
							pro.add(p);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("根据产品的名称获取某些产品(模糊查询)错误。", e);
		}
		for (Product product : pro) {
			ProductBean pb=new ProductBean(product);
			productBeans.add(pb);
		}
		return productBeans;
	}
	
	/**ajax更新商品状态
	 * */
	@RequestMapping("/changeavalibel")
	@ResponseBody
	public String changeProductAvalible(
			@RequestParam(value = "pid") String pid,
			@RequestParam(value = "avalible") boolean avalible) {
		if (Strings.isNullOrEmpty(pid)) {
			return "falser";
		}
		
		Integer id = StringUtil.toInt(pid, 0);
		
		Product p = pManagerService.get(id);
		if (p == null) {
			return "falser";
		}

		p.setAvalible(avalible);
		try {
			pManagerService.updateInfo(p);
			pManagerService.reload();
		} catch (ServiceException e) {
			logger.error("更新产品状态错误", e);
		}

		return "success";
	}
	
	// 根据产品的系列获取一个系列的产品(产品表自带的两个类型)
	@RequestMapping("/findbycategory")
	public String findByCategory(String categoryCode) {
		throw new UnsupportedOperationException();
	}

	// 根据我们自定义的产品类型找到产品(自定义的产品类型)
	@RequestMapping("/findbyptype")
	public String findByOwnerType(String typeCode) {
		throw new UnsupportedOperationException();
	}

	// 給List<Proudct>添加自定义的类型
	@RequestMapping("addproducttype")
	public String addptype(List<Product> products, String typeCode) {
		throw new UnsupportedOperationException();
	}

	// 跳转到 折扣策略
	@RequestMapping("customerpricepolicy")
	public String customerPricepolicy() {
		return "/page/customerpricepolicy";
	}
	
	//跳转到 折扣策略 创建
	@RequestMapping("customerpricepolicyadd")
	public String customerPricePolicyAdd() {
		return "/page/customerpricepolicyadd";
	}

	@RequestMapping("productupdate")
	public String productupdate(@RequestParam(value="id") int productId,Model model) {
		model.addAttribute("product", pManagerService.get(productId));
		return "/page/productadd";
	}
	
	@RequestMapping(value = "/productupdatesave", method = RequestMethod.POST)
	public String productupdate(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request, Product product) {
		// 文件存放路劲，上传的文件放在项目的upload文件夹下
		Integer pid = product.getProductId();
		try {
			String serId = pManagerService.get(pid).getFitemClassId();

			String uri = UriUtil.getURI(UriUtil.FMT_PRODUCT, serId) + pid;
			String fname = file.getOriginalFilename();
			uri = uri + fname.substring(fname.lastIndexOf('.'));

			VfsDocClient.put(AppConfig.getPutVfsPrefix() + uri, file.getBytes());

			product.setImageURL(uri);
			pManagerService.updateInfo(product);
		} catch (Exception e) {
			logger.error("上传的文件失败。", e);
		}
		return "/page/index";
	}
	
	@RequestMapping(value = "/productupdatesave2", method = RequestMethod.POST)
	public String productupdate2(Model model, @ModelAttribute("product") Product product,
			@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
		// 文件存放路劲，上传的文件放在项目的upload文件夹下
		Integer pid = product.getProductId();//前段不可信
		Product productM = pManagerService.get(pid);
		Assert.notNull(productM);
		
		if (file != null && file.getBytes().length > 0) {
			try {
				String serId = pManagerService.get(pid).getFitemClassId();
				String uri = UriUtil.getURI(UriUtil.FMT_PRODUCT, serId) + pid;
				String fname = file.getOriginalFilename();
				uri = uri + fname.substring(fname.lastIndexOf('.'));
				VfsDocClient.put(AppConfig.getPutVfsPrefix() + uri, file.getBytes());
				productM.setImageURL(uri);
			} catch (Exception e) {
				logger.error("上传的文件失败。", e);
				model.addAttribute("message", "更新失败");
				model.addAttribute("product", productM);
				return "page/productadd";
			}
		}

		try {
			productM.setDescription(StringUtil.isEmpty(product.getDescription()) ? "" : product.getDescription());
			
			pManagerService.updateInfo(productM);
			model.addAttribute("message", "更新成功");
		} catch (Exception e) {
			logger.error("产品更新失败",e);
		}

		model.addAttribute("product", productM);
		return "page/productadd";
	}
	
	/**
	 * 系列初始化
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/productseries")  
	public String seriesInit(HttpServletRequest request) {
		try {
			Dictionary d = dictManager.get(Product.PRODUCT_POSITIVE_SERIES);
			List<Dictionary> dl = d.getChildren();
			List<Dictionary> dltemp = new ArrayList<Dictionary>(dl.size());
			dltemp.addAll(dl);
//			setPageAttribute(request, dltemp, 1);
			List<SeriesBean> sl=getSeriesBean(dltemp);
			request.setAttribute("SeriesBeanList", sl);
		} catch (Exception e) {
			logger.error("", e);
		}
		return "/page/productseries";
	}
	/**
	 * 返回长度为分页长度的List<Dictionary> 关键字name
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping("/searchseries")
	public String seriesSearch(HttpServletRequest request,
			@RequestParam(value = "name", required = false) String name){
		Dictionary d= dictManager.get(Product.PRODUCT_POSITIVE_SERIES);
		List<Dictionary> dl=d.getChildren();
		List<Dictionary> dltemp = new ArrayList<Dictionary>();
		List<Dictionary> fbns=dictManager.lookupByName(name);
		for (Dictionary fbn : fbns) {
			if(dl.contains(fbn)){
				dltemp.add(fbn);
			}
		}
		setPageAttribute(request, dltemp, 1);
		return "/page/productseries";
	}
	
	/**
	 * 系列分页
	 * @param request
	 * @param page
	 * @param num
	 * @param key
	 * @return
	 */
	@RequestMapping(value = "/seriespage")
	@ResponseBody
	public List<SeriesBean> seriesPage(HttpServletRequest request,
			@RequestParam(value="page",required=false)int page,
			@RequestParam(value="num",required=false)int num,
			@RequestParam(value="key",required=false)String key){
		Dictionary d= dictManager.get(Product.PRODUCT_POSITIVE_SERIES);
		List<Dictionary> dl=d.getChildren();
		List<Dictionary> dltemp = new ArrayList<Dictionary>();
		request.setAttribute("page", page);
		int startNo = (page-1)*num; 
		if(!StringUtil.isEmpty(key)){
			List<Dictionary> fbns=dictManager.lookupByName(key);
			for (Dictionary fbn : fbns) {
				if(dl.contains(fbn)){
					dltemp.add(fbn);
				}
			}
			int size = dltemp.size();
			int endNo = page*num<size?page*num:size;
			dltemp=dltemp.subList(startNo, endNo);
		}else {
			int size = dl.size();
			int endNo = page*num<size?page*num:size;
			dltemp=dl.subList(startNo, endNo);
		}
		List<SeriesBean> sl=getSeriesBean(dltemp);
		return sl;
	}
	
	
	@RequestMapping("/getSeriesUrl")
	public String getSeriesUrl(@RequestParam(value="did") String did,Model model) {
		Dictionary dictionary=dictManager.get(did);
		model.addAttribute("dictionary", dictionary);
		return "/page/seriesadd";
	}
	/**
	 * 更新图片url
	 * 
	 * 更新完ajax把更新后的url画到前端
	 * 
	 * @param file
	 * @param request
	 * @param dictionary
	 * @return
	 */
	@RequestMapping(value = "/updateSeriesUrl", method = RequestMethod.POST)
	public String updateSeriesUrl(HttpServletRequest request,Model model,
			@ModelAttribute("dictionary") Dictionary dictionary,
			@RequestParam(value = "file", required = false) MultipartFile file)throws IOException {
		
			Dictionary changeD=new Dictionary();
			Dictionary cacheD=dictManager.get(dictionary.getDid());
			BeanDup.dup(cacheD, changeD);
			BeanDup.dupNotNull(dictionary,changeD,"summary","url");
		if (file != null && file.getBytes().length > 0) {
			//跟新图片和属性
			try {
				// image or document URI
				String uri = UriUtil.getURI(UriUtil.FMT_SERIAL) + cacheD.getHardCode();
				String originName = file.getOriginalFilename();
				// file extended name
				String ext = originName.substring(originName.lastIndexOf('.'));
				uri = uri + ext;
				// get inputStream from MultipartFile
				VfsDocClient.put(AppConfig.getPutVfsPrefix() + uri, file.getBytes());
				changeD.setUrl(uri);
				dictManager.update(changeD);
				model.addAttribute("message", "更新成功");
			} catch (Exception e) {
				logger.error("文件上传失败！", e);
				model.addAttribute("message", "更新失败");
				model.addAttribute("dictionary", dictionary);
				return "page/productadd";
			}
		}else{
			//只更新属性
			try {
				dictManager.update(changeD);
				model.addAttribute("message", "更新成功");
			} catch (Exception e) {
				model.addAttribute("message", "更新失败");
				model.addAttribute("dictionary", changeD);
				return "page/productadd";
			}
		}
			Dictionary d = dictManager.get(dictionary.getDid());
			model.addAttribute("dictionary", d);
		return "/page/seriesadd";
	}

	/**
	 * 封装产品系列Bean
	 * @param dl
	 * @return
	 */
	private List<SeriesBean> getSeriesBean(List<Dictionary> dl){
		final List<SeriesBean> sl=new ArrayList<SeriesBean>(dl.size());
		for (Dictionary d : dl) {
			SeriesBean s=getSeriesBean(d.getDid());
			sl.add(s);
		}
		return sl;
	}
	
	/**
	 * 封装产品系列Bean
	 * @param Did
	 * @return
	 */
	private SeriesBean getSeriesBean(String Did){
		Dictionary d=new Dictionary();
		try {
			BeanDup.dup(dictManager.get(Did), d);
		} catch (Exception e) {
			logger.error("获取系列错误",e);
		}
		SeriesBean sb=new SeriesBean(d);
		return sb;
	}

	/**
	 * 返回分页参数
	 * @param request
	 * @param sl
	 * @param max
	 * @param page
	 */
	private void setPageAttribute(HttpServletRequest request,List<Dictionary> dltemp,int page){
		int size=dltemp.size();
		int max=(size%10==0)?(size/10):(size/10+1);
		if(dltemp.size()>10){
			dltemp=dltemp.subList(0, 10);
		}
		List<SeriesBean> sl=getSeriesBean(dltemp);
		request.setAttribute("max", max);
		request.setAttribute("page", 1);
		request.setAttribute("SeriesBeanList", sl);
	}
}
