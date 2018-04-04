package com.zeusas.dp.ordm.bean;

import java.util.ArrayList;
import java.util.List;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.core.utils.AppConfig;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.entity.Product;

public class ProductBean  implements Comparable<ProductBean>{
	
	public static final String PRODUCT = "u/d/zhengpin.png";
	public static final String DONATION = "u/d/wuliao.jpg";
	public static final String MATERIAL = "u/d/zengpin.jpg";
	public static final String DEFSERI = "u/d/serialDefault.jpg";
	
	private Integer count;
	
	private Product product;
	
	private final List<Product> products = new ArrayList<>();
	
	private Dictionary productType;
	
	private List<Dictionary> series;

	private List<Activity> activity;
	
	public ProductBean(){}

	//类型 产品组 产品
	public ProductBean( Dictionary productType,List<Product> products,List<Activity> activity) {
		this.activity = activity;
		//this.products = products;
		setProducts(products);
		this.productType = productType;
	}
	
	

	//类型 系列
	public ProductBean( Dictionary productType,List<Dictionary> series) {
		this.series = series;
		this.productType = productType;
	}

	public ProductBean(Integer count,Product product){
		this.count = count;
		this.product = product;
	}
	
	public List<Product> getProducts() {
		return products;
	}



	public void setProducts(List<Product> products) {
		if (products==null || products.size()==0){
			return;
		}
		for (Product p:products){
			Product p1 = p.clone();
			// SET default image to P1;
			this.products.add(p1);
		}
	}


	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Dictionary getProductType() {
		return productType;
	}



	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setProductType(Dictionary productType) {
		this.productType = productType;
	}




	public List<Activity> getActivity() {
		return activity;
	}



	public void setActivity(List<Activity> activity) {
		this.activity = activity;
	}



	public List<Dictionary> getSeries() {
		return series;
	}



	public void setSeries(List<Dictionary> series) {
		this.series = series;
	}

	public static Product cloneProduct(Product pp0) {
		Product p = pp0.clone();
		if (StringUtil.isEmpty(p.getImageURL())
				&& !StringUtil.isEmpty(p.getTypeId())) {
			if (p.getTypeId().equals(Product.TYPEID_PRODUCT)) {
				p.setImageURL(AppConfig.getVfsPrefix()+PRODUCT);
			} else if (p.getTypeId().equals(Product.TYPEID_METERIAL)) {
				p.setImageURL(AppConfig.getVfsPrefix()+MATERIAL);
			} else {
				p.setImageURL(AppConfig.getVfsPrefix()+DONATION);
			}
		}else{
			p.setImageURL(AppConfig.getVfsPrefix()+p.getImageURL());
		}
		return p;
	}
	
	public int compareTo(ProductBean o) {
		if(o==null){
			return 1;
		}
		if(this.getProductType()==null){
			return 0;
		}
		if(o.getProductType()==null){
			return 1;
		}
		
		Integer seq0 = this.productType.getSeqid();
		Integer seq1 = o.getProductType().getSeqid();

		if (seq0 == seq1) {
			return 0;
		}
		if (seq0 == null) {
			return -1;
		}
		if (seq1 == null) {
			return 1;
		}

		return seq1 < seq0 ? -1 : 1;
	}
	
	
 }
