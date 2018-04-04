package com.zeusas.dp.ordm.bean;

import java.util.ArrayList;
import java.util.List;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.core.utils.AppConfig;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.rev.entity.ReservedActivity;

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

	private List<Activity> activitys;
	
	private Activity activity;
	
	private boolean canclePrePro;// 是否可以取消还欠
	
	// 打欠专用：仓库，产品名称，数量，所属订单，归属于哪个店铺，归属于哪个活动
	private String warehouse;
	private String pname;
	private String qty;
	private String belongOrder;
	private String belongCounter;
	private long orderDetailId;
	private String belongActivity;
	// 预售开始和结束时间
	private String start;
	private String end;
	
	// 占位符
	private String placeholder;
	// 预定会
	private List<ReservedActivity> reservedActivities;
	/*
	 * 打欠归还产品
	 */
	public ProductBean(String warehouse, 
			String pname,
			String qty, 
			String belongOrder, 
			String belongCounter,
			boolean  canclePrePro,
			long orderDetailId,
			String belongActivity) {
		super();
		this.warehouse = warehouse;
		this.pname = pname;
		this.qty = qty;
		this.belongOrder = belongOrder;
		this.belongCounter = belongCounter;
		this.canclePrePro = canclePrePro;
		this.orderDetailId = orderDetailId;
		this.belongActivity = belongActivity;
	}

	public ProductBean(){}

	//类型 产品组 产品
	public ProductBean( Dictionary productType,List<Product> products,List<Activity> activitys) {
		this.activitys = activitys;
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
	
	public ProductBean(Product product){
		this.product = product;
	}
	
	// 
	public ProductBean(String start ,String end ,Product product,Activity act){
		this.start = start;
		this.end = end;
		this.product = product;
		this.activity = act;
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


	public List<Dictionary> getSeries() {
		return series;
	}



	public void setSeries(List<Dictionary> series) {
		this.series = series;
	}

	
	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getBelongOrder() {
		return belongOrder;
	}

	public void setBelongOrder(String belongOrder) {
		this.belongOrder = belongOrder;
	}

	public String getBelongCounter() {
		return belongCounter;
	}

	public void setBelongCounter(String belongCounter) {
		this.belongCounter = belongCounter;
	}
	
	
	
	public String getBelongActivity() {
		return belongActivity;
	}

	public void setBelongActivity(String belongActivity) {
		this.belongActivity = belongActivity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	public boolean getCanclePrePro() {
		return this.isCanclePrePro();
	}


	public boolean isCanclePrePro() {
		return canclePrePro;
	}

	public void setCanclePrePro(boolean canclePrePro) {
		this.canclePrePro = canclePrePro;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
	
	

	public List<Activity> getActivitys() {
		return activitys;
	}

	public void setActivitys(List<Activity> activitys) {
		this.activitys = activitys;
	}

	public Activity getActivity() {
		return activity;
	}

	public List<ReservedActivity> getReservedActivities() {
		return reservedActivities;
	}

	public void setReservedActivities(List<ReservedActivity> reservedActivities) {
		this.reservedActivities = reservedActivities;
	}

	public long getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(long orderDetailId) {
		this.orderDetailId = orderDetailId;
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

		if (seq0 == null) {
			return -1;
		}
		if (seq1 == null) {
			return 1;
		}
		if (seq0.intValue() == seq1.intValue()) {
			return 0;
		}

		return seq1 < seq0 ? -1 : 1;
	}
	
	
 }
