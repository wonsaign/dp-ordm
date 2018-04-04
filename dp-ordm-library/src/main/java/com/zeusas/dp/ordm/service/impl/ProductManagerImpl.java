package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.base.Strings;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.service.OnStartApplication;
import com.zeusas.core.http.HttpUtil;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppConfig;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductRelationPolicy;
import com.zeusas.dp.ordm.service.PRPolicyManager;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.ProductService;

/**
 * 
 * @author fengx
 * 
 * @date 2016年12月14日 下午3:45:36
 */
@Service
public class ProductManagerImpl extends OnStartApplication implements ProductManager {
	
	final static String IMG_PRODUCT = "u/d/zhengpin.png";
	final static String IMG_DONATION = "u/d/wuliao.jpg";
	final static String IMG_MATERIAL = "u/d/zengpin.jpg";
	final static String IMG_SERAILS = "u/d/serialDefault.jpg";
	
	@Autowired
	private ProductService productService;

	// 所有的产品的一个集合
	final Map<Integer, Product> products;
	// 根据我们自定义的类型对应产品的一个map key=type
	final Map<String, List<Product>> indexByType;
	// key=class系列
	final Map<String, Set<Product>> indexByClass;
	// 大类对应的所有的系列编码
	final Map<String, List<String>> BigType_SerialCode;
	// 身体部位对应的产品
	final Map<String, List<Product>> indexByBodyType;
	//<productCode, Product> <产品编码,产品>
	final Map<String, Product> product_code;

	public ProductManagerImpl() {
		// 所有的产品的一个集合
		products = new ConcurrentHashMap<>();
		indexByType = new ConcurrentHashMap<>();
		indexByClass = new ConcurrentHashMap<>();
		BigType_SerialCode = new ConcurrentHashMap<>();
		indexByBodyType = new ConcurrentHashMap<>();
		product_code = new HashMap<>();
	}

	public void onStartLoad() {
		reload();
	}
	
	// 加一个所有被禁用掉的产品的方法
	@Override
	public List<Product> findAllDisabledProduct() {
		return products.values() //
				.parallelStream() //
				.filter(e -> !e.isAvalible()) //
				.collect(Collectors.toList());
	}

	private void setImageUrlPrefix(Product p) {
		String uri = p.getImageURL();
		if (Strings.isNullOrEmpty(uri)) {
			switch (p.getTypeId()) {
			case Product.TYPEID_PRODUCT:
				p.setImageURL(AppConfig.getVfsPrefix()+IMG_PRODUCT);
				break;
			case Product.TYPEID_METERIAL:
				p.setImageURL(AppConfig.getVfsPrefix()+IMG_MATERIAL);
				break;
			default:
				p.setImageURL(AppConfig.getVfsPrefix()+IMG_DONATION);
			}
		} else {
			p.setImageURL(AppConfig.getVfsPrefix()+uri);
		}
	}

	private void clear() {
		products.clear();
		indexByType.clear();
		indexByClass.clear();
		BigType_SerialCode.clear();
		indexByBodyType.clear();
		product_code.clear();
	}
	
	@Override
	public void reload() {
		clear();
		
		PRPolicyManager policyManager = AppContext.getBean(PRPolicyManager.class);
		if(policyManager.findall().isEmpty()){
			policyManager.reload();
		}
		
		List<Product> pp2 = productService.findAll();
		ArrayList<Product> pp = new ArrayList<Product>(pp2.size());
		for (Product p : pp2) {
			// XXX: add url schema to imgageURL
			setImageUrlPrefix(p);
			products.put(p.getProductId(), p);
			// add minUnit
			ProductRelationPolicy prp = policyManager.get(p);
			if (prp != null) {
				Integer minUnit = prp.getMinOrderUnit();
				p.setMinUnit(minUnit);
			}
			if (p.isAvalible()) {
				pp.add(p);
			}
			
			product_code.put(p.getProductCode(), p);
		}
		
		for (Product product : pp) {
			Set<String> types = product.getType();
			if (types == null) {
				continue;
			}
			for (String type : types) {
				List<Product> ps = indexByType.get(type);
				if (ps == null) {
					ps = new ArrayList<Product>();
					indexByType.put(type, ps);
				}
				ps.add(product);
			}
		}
		// 给序列建立索引
		for (Product product : pp) {
			if (product.getFitemClassId() == null) {
				continue;
			}
			String clazz = product.getFitemClassId();
			Set<Product> pts = indexByClass.get(clazz);
			if (pts == null) {
				pts = new LinkedHashSet<Product>();
				indexByClass.put(clazz, pts);
			}
			pts.add(product);
		}
		for (Product product : pp) {
			if (product.getTypeId() == null) {
				continue;
			}
			String bigType = product.getTypeId();
			List<String> ss = BigType_SerialCode.get(bigType);
			if (ss == null) {
				ss = new ArrayList<String>();
				BigType_SerialCode.put(bigType, ss);
			}
			if (!ss.contains(product.getFitemClassId()))
				ss.add(product.getFitemClassId());
		}

		for (Product product : pp) {
			if (product.getBodyTypeId() == null) {
				continue;
			}
			String bodyTypeId = product.getBodyTypeId();
			List<Product> bps = indexByBodyType.get(bodyTypeId);
			if (bps == null) {
				bps = new ArrayList<Product>();
				indexByBodyType.put(bodyTypeId, bps);
			}
			bps.add(product);
		}

		for (List<Product> pl : indexByType.values()) {
			((ArrayList<?>) pl).trimToSize();
		}
	}

	/**
	 * 增加产品，刷新产品cache
	 */
	public void add(Product product) throws ServiceException {
		productService.save(product);
		reload();
	}

	/**
	 * 更新从数据库取出更新
	 */
	@Override
	public Product updateInfo(Product product) throws ServiceException {
		try {
			Product p = productService.get(product.getProductId());
			if (product.getImageURL() != null) {
				p.setImageURL(product.getImageURL());
			}
			if (!HttpUtil.isEmpty(product.getDescription())) {
				p.setDescription(product.getDescription());
			}
			if (product.isAvalible() != null) {
				p.setAvalible(product.isAvalible());
			}
			p.setLastUpdate(System.currentTimeMillis());
			productService.update(p);
			reload();// FiXME
			return p;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public List<Product> findByType(String type) {
		List<Product> result = this.indexByType.get(type);
		return result == null ? new ArrayList<>(0) : result;
	}

	public List<Product> findByClass(String clazz) {
		Set<Product> seriesClass = indexByClass.get(clazz);
		if (seriesClass == null) {
			return new ArrayList<Product>(0);
		}

		final List<Product> pp = new ArrayList<>(seriesClass.size());
		pp.addAll(seriesClass);
		return pp;
	}

	public Map<String, Set<Product>> findAllSeriesClass() {
		return this.indexByClass;
	}

	public Product get(int productId) {
		return products.get(productId);
	}

	public List<Dictionary> findByHardCode(String type) {
		DictManager dictManager = AppContext.getBean(DictManager.class);
		Dictionary dict = dictManager.get(type);
		return dict == null ? null : dict.getChildren();
	}

	public List<Product> findByName(String name) throws ServiceException {
		if (StringUtil.isEmpty(name)) {
			return new ArrayList<>(0);
		}

		String ww[] = StringUtil.split(name, "\\s+");
		return products.values().parallelStream()//
				.filter(e -> e.isAvalible() && checkName(e, ww))//
				.collect(Collectors.toList());
	}

	@Override
	public List<Product> findDisabledProduct(String name) throws ServiceException {
		if (StringUtil.isEmpty(name)) {
			return new ArrayList<>(0);
		}

		String ww[] = StringUtil.split(name, "\\s+");
		
		return products.values().stream()//
				.filter(e -> !e.isAvalible() && checkName(e, ww))//
				.collect(Collectors.toList());
	}

	static boolean checkName(Product p, String[] ww) {
		StringBuilder txt = new StringBuilder();
		txt.setLength(0);
		txt.append(p.getBarCode()).append(':');
		txt.append(p.getMemberPrice()).append(':');
		txt.append(p.getName());
		return (StringUtil.matchAll(txt.toString(), ww));
	}

	public List<String> findSerials(String name) throws ServiceException {
		if (name != null) {
			return BigType_SerialCode.get(name);
		}
		return null;
	}

	public List<Product> fingBySerialAndName(String SerialId, String name) throws ServiceException {
		List<String> series = findSerials(SerialId);
		List<Product> product_final = new ArrayList<Product>();
		List<Product> products = new ArrayList<Product>();
		
		if (series == null || series.size() == 0) {
			return product_final;
		}
		for (String s : series) {
			List<Product> ps = findByClass(s);
			products.addAll(ps);
		}
		if (products.size() == 0) {
			return product_final;
		}
		if (name == null) {
			for (Product p : products) {
				product_final.add(p);
			}
			return product_final;
		}
		for (Product p : products) {
			if (p.getName().contains(name)) {
				product_final.add(p);
			}
		}
		return product_final;
	}

	public boolean isAuthenticProduct(int productId) {
		Product product = products.get(productId);
		return Product.TYPEID_PRODUCT.equals(product.getTypeId());
	}

	public List<Product> findAllAvaible() {
		return products.values().parallelStream()//
				.filter(e -> e.isAvalible())//
				.collect(Collectors.toList());
	}

	@Override
	public List<Product> findByBodyType(String bodyTypeId) {
		List<Product> result = indexByBodyType.get(bodyTypeId);
		return result == null ? new ArrayList<>(0) : result;
	}

	@Override
	public List<Product> findByPrice(String serialId, boolean flag) {
		List<Product> products = findByClass(serialId);
		List<Product> sortProducts = new ArrayList<Product>(products.size());
		if (flag) {
			products.stream() //
					.sorted(Comparator.comparingDouble(Product::getMemberPrice).reversed())//
					.forEach(e -> sortProducts.add(e));
		} else {
			products.stream()//
					.sorted(Comparator.comparingDouble(Product::getMemberPrice))//
					.forEach(e -> sortProducts.add(e));
		}
		return sortProducts;
	}

	@Override
	public List<Product> findByPriceWithBody(String bodyTypeId, boolean flag) {
		List<Product> products = findByBodyType(bodyTypeId);
		if (flag) {
			return products.stream()//
					.sorted(Comparator.comparingDouble(Product::getMemberPrice).reversed())//
					.collect(Collectors.toList());
		} 
		
		return	products.stream()//
					.sorted(Comparator.comparingDouble(Product::getMemberPrice))//
					.collect(Collectors.toList());
	}

	@Override
	public List<Product> findAll() {
		return products.values().stream().collect(Collectors.toList());
	}

	@Override
	public Product findByCode(String code) {
		return product_code.get(code);
	}
}
