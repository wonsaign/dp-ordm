package com.zeusas.dp.ordm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.NestedServletException;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppConfig;
import com.zeusas.core.utils.BeanDup;
import com.zeusas.core.utils.DateTime;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.active.bean.UserActivities;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.ActivityContext;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.bean.ProductBean;
import com.zeusas.dp.ordm.bi.service.ProductSellerManager;
import com.zeusas.dp.ordm.entity.AssociatedProduct;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.CounterSerial;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductRelationPolicy;
import com.zeusas.dp.ordm.entity.ProductSeller;
import com.zeusas.dp.ordm.entity.SerialSeller;
import com.zeusas.dp.ordm.rev.service.ReservedActivityManager;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.PRPolicyManager;
import com.zeusas.dp.ordm.service.ProductManager;

/**
 * 
 * @author fx 定义产品的的Controller 包含了产品的增删改查
 */

@Controller
@RequestMapping("/product")
public class ProductController extends OrdmBasicController {
	static Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	final static byte[] JSONEmptyObj = "{}".getBytes();
	final static byte[] JSONEmptyArray = "[]".getBytes();
	final static List<Product> EMPTYLIST = new ArrayList<>(0);
	final static Map<Integer,Product> EMPTYMAP = new HashMap<>(0);
	final static String JSON_CONTENTTYPE = "application/json; charset=utf-8";

	@Autowired
	private ProductManager productManager;
	@Autowired
	private ActivityManager activityManager;
	@Autowired
	private ProductSellerManager productSellerManager;
	@Autowired
	private PRPolicyManager prPolicyManager;// 关联策略manage
	@Autowired
	private ReservedActivityManager reservedActivityManager;
	@Autowired
	private CounterManager counterManager;
	
	/** 每页显示商品数量 */
	private int PAGE_SIZE = 10;

	/**
	 * 根据产品的id获取产品
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/activity_{actId}")
	public String getById(ModelMap mp,
			@PathVariable(value = "actId", required = false) String actId)//
			throws IOException {
		// 设置仓库
		setAutoMyStock();
		try {
			Activity activity = activityManager.get(actId);
			String stTime = DateTime.formatDate("yyyy/MM/dd", activity.getStart());
			String enTime = DateTime.formatDate("yyyy/MM/dd", activity.getTo());
			ActivityContext context = activity.getContext();
			
			// 校验是否可用(若在预定会中仅所属门店可用)
			String counterCode = super.getCounterCode();
			Counter counter = counterManager.getCounterByCode(counterCode);
			boolean isAvailableActivity = reservedActivityManager.isAvailableActivity(counter, actId);
			// 正常活动返回null->0     可打欠->1   打欠中->2 打欠结束->4
			int status = activity.getContext().getRevWmsStatus(counter.getWarehouses());
			// 封装一个包含打钱活动的Map
			Map<Integer,Integer> actMap = new HashMap<>();
			Set<Integer> revProducts = activity.getContext().getRevProducts();
			if(revProducts!=null) {
				for (Integer integer : revProducts) {
					actMap.put(integer, integer);
				}
			}
			// 打欠活动标志显示
			mp.addAttribute("RevMap", actMap);
			mp.addAttribute("ActStatus", status);
			mp.addAttribute("RevAct", isAvailableActivity);
			mp.addAttribute("productgroup", activity);
			mp.addAttribute("imgUrl", activity.getImage());
			mp.addAttribute("stTime", stTime);
			mp.addAttribute("enTime", enTime);
			//2017-05-05 新添加
			mp.addAttribute("activityContext", context);//递给前段一个活动规则
			// 设置前段StockId
			setResAttr(mp);
		} catch (Exception e) {
			logger.error("product error", e);
		}
		// 错误消息无法返回
		return "productgroup_detail";
	}

	/**
	 * 显示某一个系列中的所有产品
	 * 
	 * @author wonsign
	 * @return
	 */
	@RequestMapping(value = "/series_{seriesId}")
	public String seriesDetail(ModelMap mp,
			@PathVariable(value = "seriesId", required = true) String seriesId) //
			throws IOException {
		// 设置顶部菜单栏
		List<Dictionary> productClass = productManager
				.findByHardCode(Product.PRODUCT_POSITIVE_SERIES)
				.stream()
				.sorted(Comparator.comparingInt(Dictionary::getSeqid)
						.reversed()) //
				.collect(Collectors.toList());
		
		List<Dictionary> bigType = productManager
				.findByHardCode(Product.PRODUCT_BIG_TYPE);
		List<Dictionary> productType = productManager
				.findByHardCode(Product.PRODUCT_TYPE);
		List<Dictionary> bodyType = productManager
				.findByHardCode(Product.PRODUCT_BODY_TYPE);
		List<Dictionary> presents = productManager
				.findByHardCode(Product.PRODUCT_PRESENT_TYPE);
		List<Dictionary> materiels = productManager
				.findByHardCode(Product.PRODUCT_MATERIEL_TYPE);

		mp.addAttribute("bodyType", bodyType);
		mp.addAttribute("productClass", productClass);
		mp.addAttribute("bigType", bigType);
		mp.addAttribute("productType", productType);
		mp.addAttribute("presents", presents);
		mp.addAttribute("materiels", materiels);
		
		try {
			// 系列本地单品排序
			List<Product> products;
			products = productSellerManager.getSerialProduct(getCounterCode(), seriesId);
			// 过滤掉不属于门店的预定会产品
			products = filterRevProduct(super.getCounterCode(), products);
			setProductList(products);
			// 如果这个系列里没产品就会产生异常，所以必须判空异常
			products = wrapProduct(products,mp);
			// 排序后的系列单品
			mp.addAttribute("products", products);// XXX: 可能为空，但是不会影响前段
		} catch (Exception e) {
			logger.error("系列显示错误", e);
		}
		return "product";
	}

	/**
	 * 获取类型 getTypeDetail
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping("/{HardCode}_more")
	public String getTypeDetail(
			ModelMap mp,
			@PathVariable(value = "HardCode", required = false) String HardCode) throws IOException {
		try {
			List<Product> products = productManager.findByType(HardCode);
			// 过滤掉不属于门店的预定会产品
			products = filterRevProduct(super.getCounterCode(), products);
			// 包装产品
			products = wrapProduct(products,mp);
			mp.addAttribute("products", products);
		} catch (Exception e) {
			logger.error("显示产品错误", e);
		}
		return "product_detail";
	}

	/**
	 * 提供主页跳转
	 * 
	 * @author jcm
	 * @param mp
	 * @param request
	 * @return String
	 * @throws NestedServletException
	 */
	@RequestMapping("/product_{hardCode}")
	public String findProductClass(
			ModelMap mp,
			@PathVariable(value = "hardCode", required = false) String hardCode,
			HttpServletRequest request) throws IOException {
		List<Dictionary> productClass = productManager
				.findByHardCode(Product.PRODUCT_POSITIVE_SERIES);
		List<Dictionary> bigType = productManager
				.findByHardCode(Product.PRODUCT_BIG_TYPE);
		List<Dictionary> productType = productManager
				.findByHardCode(Product.PRODUCT_TYPE);
		List<Dictionary> bodyType = productManager
				.findByHardCode(Product.PRODUCT_BODY_TYPE);
		List<Dictionary> presents = productManager
				.findByHardCode(Product.PRODUCT_PRESENT_TYPE);
		List<Dictionary> materiels = productManager
				.findByHardCode(Product.PRODUCT_MATERIEL_TYPE);
		List<Product> products = new ArrayList<Product>();
		// FIXME:设置默认仓库,设法弄成全局的
		setAutoMyStock();
		try {
			switch (hardCode) {
			case Product.PRODUCT_NEWPRODUCT:
				products = productManager
						.findByType(Product.PRODUCT_NEWPRODUCT);
				// 过滤掉不属于门店的预定会产品
				products = filterRevProduct(super.getCounterCode(), products);
				// 包装产品
				products = wrapProduct(products,mp);
				mp.addAttribute("products", products.stream().limit(MAX_PNUM)
						.collect(Collectors.toList()));
				mp.addAttribute("ptype", Product.PRODUCT_NEWPRODUCT);
				break;
			case Product.PRODUCT_GLOBALSELLER:
				List<ProductSeller> sellers = productSellerManager
						.getGlobalProductSeller();
				products = new ArrayList<Product>();
				for (ProductSeller seller : sellers) {
					products.add(productManager.get(seller.getPid()));
				}
				// 过滤掉不属于门店的预定会产品
				products = filterRevProduct(super.getCounterCode(), products);
				// 包装产品
				products = wrapProduct(products,mp);
				mp.addAttribute("products", products.stream().limit(MAX_PNUM)
						.collect(Collectors.toList()));
				mp.addAttribute("ptype", Product.PRODUCT_GLOBALSELLER);
				break;
			case Product.PRODUCT_COUNTERSELLER:
				List<ProductSeller> sellers1 = productSellerManager
						.getCounterProductSeller(super.getCounterCode());
				products = new ArrayList<Product>();
				for (ProductSeller seller : sellers1) {
					products.add(productManager.get(seller.getPid()));
				}
				// 过滤掉不属于门店的预定会产品
				products = filterRevProduct(super.getCounterCode(), products);
				// 包装产品
				products = wrapProduct(products,mp);
				mp.addAttribute("products", products.stream().limit(MAX_PNUM)
						.collect(Collectors.toList()));
				mp.addAttribute("ptype", Product.PRODUCT_COUNTERSELLER);
				break;
			default:
				;
			}
			mp.addAttribute("productClass", productClass);
			mp.addAttribute("bigType", bigType);
			mp.addAttribute("productType", productType);
			mp.addAttribute("bodyType", bodyType);
			mp.addAttribute("presents", presents);
			mp.addAttribute("materiels", materiels);
		} catch (Exception e) {
			logger.error("获取产品系列错误", e);
		}
		return "product";
	}

	@RequestMapping("/activity")
	public String listMoreActi(ModelMap mp,
			@PathVariable(value = "pid", required = false) String pid,
			HttpServletRequest request) throws IOException {
		try {
			// Ajax 加载内容
			List<Dictionary> productClass = productManager
					.findByHardCode(Product.PRODUCT_POSITIVE_SERIES);
			List<Dictionary> bigType = productManager
					.findByHardCode(Product.PRODUCT_BIG_TYPE);
			List<Dictionary> productType = productManager
					.findByHardCode(Product.PRODUCT_TYPE);
			List<Dictionary> bodyType = productManager
					.findByHardCode(Product.PRODUCT_BODY_TYPE);
			List<Dictionary> presents = productManager
					.findByHardCode(Product.PRODUCT_PRESENT_TYPE);
			List<Dictionary> materiels = productManager
					.findByHardCode(Product.PRODUCT_MATERIEL_TYPE);
			mp.addAttribute("productClass", productClass);
			mp.addAttribute("bigType", bigType);
			mp.addAttribute("bodyType", bodyType);
			mp.addAttribute("productType", productType);
			mp.addAttribute("presents", presents);
			mp.addAttribute("materiels", materiels);

		} catch (Exception e) {
			logger.error("获取产品系列错误", e);
		}
		return "product";
	}

	/**
	 * 在order页面显示商品系列、商品大类、爆品畅销品等
	 * 
	 * @author jcm
	 * @param mp
	 * @param request
	 * @return String
	 * @throws NestedServletException
	 */
	@RequestMapping("/product")
	public String findProduct(ModelMap mp, HttpServletRequest request)
			throws IOException {
		// FIXME:设置默认仓库,设法弄成全局的
		setAutoMyStock();
		try {
			List<Dictionary> productClass = productManager
					.findByHardCode(Product.PRODUCT_POSITIVE_SERIES);
			// List<Dictionary> bigType =
			// productManager.findByHardCode(Product.PRODUCT_BIG_TYPE);
			// 108表示产品品类
			productClass = productClass
					.stream()
					.sorted(Comparator.comparingInt(Dictionary::getSeqid)
							.reversed()).collect(Collectors.toList());
			List<Dictionary> bodyType = productManager
					.findByHardCode(Product.PRODUCT_BODY_TYPE)//
					.stream()//
					.sorted(Comparator.comparingInt(Dictionary::getSeqid)
							.reversed()).collect(Collectors.toList());
			List<Dictionary> productType = productManager
					.findByHardCode(Product.PRODUCT_TYPE);

			List<Product> products = findByOwnerType(Product.PRODUCT_GLOBALSELLER,mp);
			List<Dictionary> presents = productManager
					.findByHardCode(Product.PRODUCT_PRESENT_TYPE);
			List<Dictionary> materiels = productManager
					.findByHardCode(Product.PRODUCT_MATERIEL_TYPE);
			if (products != null && products.size() > 0) {
				// 过滤掉不属于门店的预定会产品
				products = filterRevProduct(super.getCounterCode(), products);
				products.stream().filter(e -> false == e.getAvalible())
						.limit(MAX_PNUM).collect(Collectors.toList());
				// 设置“活动商品”上图标
				changeActFlag(products);
				addDefaultQty(products);

			}
			// 获取 待审核订单、待付款订单、待收货订单、财务退回 的数量
			super.setAllOrderSize();
			mp.addAttribute("all_order_size", super.getAllOrderSize());
			mp.addAttribute("productClass", productClass);
			mp.addAttribute("bodyType", bodyType);
			mp.addAttribute("productType", productType);
			mp.addAttribute("products", products);
			mp.addAttribute("presents", presents);
			mp.addAttribute("materiels", materiels);
			mp.addAttribute("stockId", getMyStock());
		} catch (Exception e) {
			logger.error("获取产品系列错误", e);
		}
		return "product";
	}

	/**
	 * 根据产品系列进行产品查询
	 * 
	 * @author jcm
	 * @param request
	 * @return List<Product>
	 * @throws NestedServletException
	 */
	@RequestMapping(value = "/findbyseries", produces = JSON_CONTENTTYPE)
	@ResponseBody
//	public byte[] findSerice(
	public List<Product> findSerice(			
			@RequestParam(value = "seachname", required = false) String searchName,
			@RequestParam(value = "sortPrice", required = false) String sort,
			@RequestParam(value = "typeId", required = false) String typeId,
			@RequestParam(value = "tag", required = false) String tag)
			throws IOException {
		
		int start = StringUtil.toInt(tag, 0);
		if(Strings.isNullOrEmpty(searchName)){
			//return JSONEmptyArray;
			return new ArrayList<>(0);
		}
		
		List<Product> products;
		if (sort != null) {
			boolean needSort = sort.equals("1");
			products = productManager.findByPrice(searchName, needSort);
		} else {
			products = productManager.findByClass(searchName).stream() //
					.filter(e -> (typeId != null //
							&& typeId.equals(e.getTypeId()) //
							|| typeId == null))
					.collect(Collectors.toList());
		}

		// 过滤掉不属于门店的预定会产品
		products = filterRevProduct(super.getCounterCode(), products);
		int size = products.size();
		if (start >= size) {
			//return JSONEmptyArray;
			return new ArrayList<>(0);
		}
		int end = Math.min(size, start + PAGE_SIZE);
		Product[] pp = Arrays.copyOfRange(products.toArray(new Product[0]),
				start, end);

		List<Product> result = wrapProduct(Arrays.asList(pp));
		return result;
	//	return DSResponse.toJsonStream(filterProductField, result);
	}

	/**
	 * 获取指定商品的最小购货单位 application/json;charset=UTF-8
	 * 
	 * @param productId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getminorderunit", method = RequestMethod.POST )
	@ResponseBody
	public Map<?,?> getproductminorderunit(Integer productId) throws IOException {
		Map<String,Object> result = new HashMap<>();
		ProductRelationPolicy po = null;
		if (productId != null) {
			Product p = productManager.get(productId);
			po = (p == null) ? null : prPolicyManager.get(p);
		}
		if (po != null) {
			result.put("pId", po.getpId());
			result.put("minOrderUnit", po.getMinOrderUnit());
		}
		return result;
	}

	/**
	 * 通过 产品品类 查找产品
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/findbybodytype", produces = JSON_CONTENTTYPE)
	@ResponseBody
//	public byte[] findByBodyType(
	public List<Product> findByBodyType(
			@RequestParam(value = "sortPrice", required = false) String sort,
			@RequestParam(value = "seachname", required = false) String bodyTypeId,
			@RequestParam(value = "tag", required = false) String tag,
			ModelMap mp)
			throws IOException {
		List<Product> products;
		int start = StringUtil.toInt(tag, 0);
		if (sort == null) {
			products = productManager.findByBodyType(bodyTypeId).stream()//
					.filter(e -> {
						return Product.TYPEID_PRODUCT.equals(e.getTypeId());
					}).collect(Collectors.toList());
		} else {
			boolean flag = "1".equals(sort);
			products = productManager.findByPriceWithBody(bodyTypeId, flag).stream()//
					.filter(e -> {
						return Product.TYPEID_PRODUCT.equals(e.getTypeId());
					}).collect(Collectors.toList());
		}

		// 过滤掉不属于门店的预定会产品
		products = filterRevProduct(super.getCounterCode(), products);
		int size = products.size();
		if (start >= size) {
			//return JSONEmptyArray;
			return new ArrayList<>(0);
		}

		int end = Math.min(size, start + PAGE_SIZE);
		Product[] pp = Arrays.copyOfRange(products.toArray(new Product[0]),
				start, end);

		// 包装产品
		products = wrapProduct(Arrays.asList(pp));
		return products;
		//return DSResponse.toJsonStream(filterProductField, products);
	}

	/**
	 * 根据产品的系列获取一个正价商品下系列的产品
	 * 
	 * @author jcm
	 * @param categoryCode
	 * @return List<Product> plist
	 */
	@RequestMapping(value = "/findbybigtype", produces = JSON_CONTENTTYPE)
	@ResponseBody
//	public byte[] findByBigType(
	public List<Product> findByBigType(		
			@RequestParam(value = "seachname", required = false) String seachname,
			@RequestParam(value = "tag", required = false) String tag)
			throws IOException {
		List<Product> products = new ArrayList<Product>();
		List<Dictionary> productType = productManager
				.findByHardCode(Product.PRODUCT_BIG_TYPE);
		int tag1 = StringUtil.toInt(tag, 0);
		try {
			for (Dictionary d : productType) {
				if (seachname != null && d.isActive()
						&& seachname.equals(d.getName())) {
					products = productManager.fingBySerialAndName(
							d.getHardCode(), "");
				}
			}
		} catch (Exception e) {
			logger.error("获取产品系列错误", e);
		}

		// 过滤掉不属于门店的预定会产品
		products = filterRevProduct(super.getCounterCode(), products);
		int size = products.size();
		if (tag1 >= size) {
		//	return JSONEmptyArray;
			return new ArrayList<>(0);
		}

		int start = tag1;
		int end = Math.min(size, start + PAGE_SIZE);
		Product[] pp = Arrays.copyOfRange(products.toArray(new Product[0]),
				start, end);

		// 包装产品
		products = wrapProduct(Arrays.asList(pp));
		return products;
		//return DSResponse.toJsonStream(filterProductField, products);
	}

	/**
	 * 根据我们自定义的产品类型找到产品(自定义的产品类型)爆品什么的
	 * 
	 * @author jcm
	 * @param typeCode
	 * @return List<Product> plist
	 */
	@RequestMapping(value = "/findbyptype")
	@ResponseBody
	public List<Product> findByOwnerType(
			@RequestParam(value = "seachname", required = false) String seachname,ModelMap mp)
			throws IOException {
		List<Product> products;
		switch (seachname) {
		case Product.PRODUCT_NEWPRODUCT:
			products = productManager.findByType(Product.PRODUCT_NEWPRODUCT);
			break;
		case Product.PRODUCT_GLOBALSELLER:
			List<ProductSeller> sellers = productSellerManager
					.getGlobalProductSeller();
			products = new ArrayList<Product>();
			for (ProductSeller seller : sellers) {
				products.add(productManager.get(seller.getPid()));
			}
			break;
		case Product.PRODUCT_COUNTERSELLER:
			List<ProductSeller> sellers1;
			sellers1 = productSellerManager.getCounterProductSeller(getCounterCode());
			products = new ArrayList<Product>();
			for (ProductSeller seller : sellers1) {
				Product p = productManager.get(seller.getPid());
				if (p != null) {
					products.add(p);
				}
			}
			break;
		case Product.PRODUCT_GATHER:
			// 集客商品
			products = new ArrayList<>(0); //NOP
			break;
		default:
			products = new ArrayList<>(0); //NOP
		}

		// 过滤掉不属于门店的预定会产品
		products = filterRevProduct(super.getCounterCode(), products);
		// 包装产品
		 products = wrapProduct(products,mp);
		return products;
	}

	/**
	 * ajax加载活动商品
	 * 
	 * @author jcm
	 * @param seachname
	 * @return
	 */
	@RequestMapping("/findgroup")
	@ResponseBody
	public List<Activity> findGroup(
			@RequestParam(value = "seachname", required = false) String seachname) {
		List<Activity> lpg = new ArrayList<Activity>();
		String path = AppConfig.getVfsPrefix();
		if (seachname == null) {
			return lpg;
		}
		try {
			List<Activity> pgs =  activityManager.findGlobal();
			// 过滤掉不属于门店的预定会活动
			pgs = filterRevActivity(super.getCounterCode(), pgs);
			Assert.notNull(pgs,"");
			for (Activity p : pgs) {
				Activity pGoup = new Activity();
				BeanDup.dup(p, pGoup);
				String img = path + pGoup.getImage();
				pGoup.setImage(img);
				lpg.add(pGoup);
			}
		} catch (ServiceException e) {
			logger.error("获取活动商品出错！", e);
		}
		return lpg;
	}

	/**
	 * 根据产品的系列获取一个正价商品下系列的产品
	 * 
	 * @author jcm
	 * @param categoryCode
	 * @return List<Product> plist
	 */
	@RequestMapping(value = "/findbycategory", produces = JSON_CONTENTTYPE)
	@ResponseBody
	//public byte[] findByCategory(String categoryCode) throws IOException {
	public List<Product> findByCategory(String categoryCode) throws IOException {	
		List<Product> products = new ArrayList<Product>(0);
		List<Dictionary> dlist = productManager
				.findByHardCode(Product.PRODUCT_POSITIVE_SERIES);
		try {
			for (Dictionary d : dlist) {
				if (categoryCode.equals(d.getName())) {
					products = productManager.findByClass(d.getHardCode());
				}
			}
		} catch (Exception e) {
			logger.error("获取产品系列错误", e);
		}
		// 过滤掉不属于门店的预定会产品
		products = filterRevProduct(super.getCounterCode(), products);
		// 包装产品
		products = wrapProduct(products);
		return products;
	//	return DSResponse.toJsonStream(filterProductField, products);
	}

	/**
	 * 根据产品的名称获取某些产品(模糊查询)
	 * 
	 * @author jcm
	 * @param seachname
	 * @return List<Product>
	 */
	@RequestMapping(value = "/getbyname", produces = JSON_CONTENTTYPE )
	@ResponseBody
	//public byte[] findByName(
	public List<Product> findByName(			
			@RequestParam(value = "seachname", required = false) String seachname, //
			@RequestParam(value = "hardCode", required = false) String hardCode, //
			@RequestParam(value = "sid", required = false) String sid)
			throws IOException {
		// seachname: 输入
		// hardCode 系列编码
		// sid: 标签
		List<Product> products = new ArrayList<>(0);
		// 去除空格
		seachname = seachname == null ? "" : seachname.trim();
		try {
			products = productManager.findByName(seachname);
			if (products.size() != 0 && sid == null) {
				products = getListProductByClass(products);
			} else {
				if (Strings.isNullOrEmpty(sid)) {
					sid = "p11386";
				}
				List<Product> pl = new ArrayList<Product>();
				switch (sid) {
				case "p11386":
					for (Product p : products) {
						if (Product.TYPEID_PRODUCT.equals(p.getTypeId())) {
							pl.add(p);
						}
					}
					break;
				case "p11388":
					for (Product p : products) {
						if (Product.TYPEID_PRESENT.equals(p.getTypeId())) {
							pl.add(p);
						}
					}
					break;
				case "m11389":
					for (Product p : products) {
						if (Product.TYPEID_METERIAL.equals(p.getTypeId())) {
							pl.add(p);
						}
					}
					break;
				default:
					;
				}
			}
		} catch (Exception e) {
			logger.error("模糊查询出错！", e);
		}
		// 过滤掉不属于门店的预定会产品
		products = filterRevProduct(super.getCounterCode(), products);
		// 包装产品
		products = wrapProduct(products);
		return products;
	//	return DSResponse.toJsonStream(filterProductField, products);
	}

	/**
	 * findByName商品赛选里面避免代码重复
	 * 
	 * @param products
	 * @return
	 */
	protected List<Product> getListProductByClass(List<Product> products) {
		List<Product> pl = new ArrayList<Product>();
		for (Product p : products) {
			if (Product.TYPEID_PRODUCT.equals(p.getTypeId())) {
				pl.add(p);
			}
		}
		if (pl.isEmpty()) {
			for (Product p : products) {
				if (Product.TYPEID_PRESENT.equals(p.getTypeId())) {
					pl.add(p);
				}
			}
		}
		if (pl.isEmpty()) {
			for (Product p : products) {
				if (Product.TYPEID_METERIAL.equals(p.getTypeId())) {
					pl.add(p);
				}
			}
		}
		return pl;
	}

	/**
	 * 显示产品详细
	 * 
	 * @author wonsign
	 * @param mp
	 * @param productId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/detail_{productId}")
	public String productDetail(
			ModelMap mp,
			@PathVariable(value = "productId", required = true) Integer productId)
			throws IOException {
		// FIXME:设置默认仓库,设法弄成全局的
		setAutoMyStock();
		// 添加赠品物料集合
		List<ProductBean> additionalProducts = new ArrayList<ProductBean>();
		try {
			Product product = productManager.get(productId);
			ProductRelationPolicy relationPolicy = prPolicyManager.get(product);
			if (relationPolicy != null
					&& relationPolicy.getAssociatedProducts() != null) {
				Set<AssociatedProduct> associatedProducts = relationPolicy
						.getAssociatedProducts();
				for (AssociatedProduct ap : associatedProducts) {
					// 物料和赠品
					Product additionalProduct = productManager.get(ap.getPid());
					// 每个product的数量
					Integer qty = (int) ap.getCoeff();
					Assert.notNull(qty,"");
					ProductBean pb = new ProductBean(qty, additionalProduct);
					additionalProducts.add(pb);
				}
			}
			boolean actItself = false;
			actItself = product.getActItself();
			// 单品包装
			List<Product> p0 = new ArrayList<Product>(1); 
			p0.add(product);
			wrapProduct(p0);
			product = p0.get(0);
			
			UserActivities ua = super.getUserActivityList();
			List<Activity> actPros = ua.getProductActivities(productId);
			// 校验是否可用(若在预定会中仅所属门店可用)
			String counterCode = super.getCounterCode();
			Counter counter = counterManager.getCounterByCode(counterCode);
			boolean isAvailableProduct = reservedActivityManager.isAvailableProduct(counter, productId);
			
			mp.addAttribute("isAvailableProduct", isAvailableProduct);
			mp.addAttribute("additionalProduct", additionalProducts);
			mp.addAttribute("product", product);
			mp.addAttribute("activities", actPros);
			mp.addAttribute("actItself", actItself);
			// 设置一个全局都要使用的stockId
			setResAttr(mp);
		} catch (Exception e) {
			logger.error("产品详细错误", e);
		}
		return "product_detail";
	}


	/**
	 * 根据产品系列排序 按照全国和本地排序
	 * 
	 * @param serialGlobal
	 * @param serialLocal
	 * @return
	 */
	@RequestMapping(value = "/sortProduct")
	@ResponseBody
	public List<Product> sortProduct(
			@RequestParam(value = "serialGlobal", required = false) String serialGlobal,
			@RequestParam(value = "serialLocal", required = false) String serialLocal) {
		// 全国 按照遍历每个系列，然后equals。。。。
		try {
			List<Product> productsG = new ArrayList<Product>();
			if (serialGlobal != null) {
				List<ProductSeller> productSellers = sortProductSeller(
						productSellerManager.getGlobalSeriesSeller(),
						serialGlobal);
				for (ProductSeller p : productSellers) {
					Product product = productManager.get(p.getPid());
					productsG.add(product);
				}
				// 过滤掉不属于门店的预定会产品
				productsG = filterRevProduct(super.getCounterCode(), productsG);
				// 包装产品
				productsG = wrapProduct(productsG);
			}
			// 本地：前端不可信，从缓存中取得相应的柜台号。
			List<Product> productsL = new ArrayList<Product>();
			if (serialLocal != null) {
				String counterCode = super.getCounterCode();
				CounterSerial CounterSerialL = getCounterSerial(counterCode);
				List<ProductSeller> productSellersL = sortProductSeller(
						CounterSerialL, serialLocal);
				for (ProductSeller productSeller : productSellersL) {
					Product product = productManager
							.get(productSeller.getPid());
					productsL.add(product);
				}
				// 过滤掉不属于门店的预定会产品
				productsL = filterRevProduct(super.getCounterCode(), productsL);
				// 包装产品
				productsL = wrapProduct(productsL);
			}
			return serialGlobal == null ? productsL : productsG;
		} catch (Exception e) {
			logger.error("全国本地销量排序异常", e);
		}
		return new ArrayList<Product>(0);
	}

	// 获取本地的系列Session
	protected CounterSerial getCounterSerial(String counterCode) {
		CounterSerial cs = super.getCounterSerial(counterCode);
		if (cs == null) {
			cs = productSellerManager.getSeriesSeller(counterCode);
			super.setCounterSerial(cs);
		}
		return cs;
	}

	// 获取降续排序顺序后产品的集合
	protected List<ProductSeller> sortProductSeller(
			CounterSerial counterSerial, String sortId) {
		List<SerialSeller> serialSellers = counterSerial.getSerials();
		List<ProductSeller> productSeller = new ArrayList<ProductSeller>();
		for (SerialSeller serialSeller : serialSellers) {
			if (serialSeller.getSid().equals(sortId)) {
				productSeller.addAll(serialSeller.getSell());
			}
		}
		return productSeller;
	}
	
	/**
	 * 除去预定会中的产品
	 * @param counter
	 * @param src
	 * @return
	 */
	private List<Product> filterRevProduct(String counterCode, List<Product> src) {
		Counter counter = counterManager.getCounterByCode(counterCode);
		return src.stream().filter(e-> reservedActivityManager.isAvailableProduct(counter, e.getProductId()))
					.collect(Collectors.toList());
	}
	
	/**
	 * 除去预定会中的活动
	 * @param counter
	 * @param src
	 * @return
	 */
	private List<Activity> filterRevActivity(String counterCode, List<Activity> src) {
		Counter counter = counterManager.getCounterByCode(counterCode);
		return src.stream().filter(e-> reservedActivityManager.isAvailableActivity(counter, e.getActId()))
				.collect(Collectors.toList());
	}

}