package com.zeusas.dp.ordm.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import com.zeusas.dp.ordm.entity.AssociatedProduct;
import com.zeusas.dp.ordm.entity.ProductRelationPolicy;

public class PRPBean {
	
	private ProductRelationPolicy productRelationPolicy;
	
	private String policyId;
	private String name;
	private String productName;
	private String type;
	private String level;
	private String minOrderUnit;
	private String associatedProducts;
	private String status;
	private String lastUpdate;
	private Set<MaterielBean> materielBean;
	
	public PRPBean() {
	}

	public PRPBean(ProductRelationPolicy productRelationPolicy) {
		this.productRelationPolicy = productRelationPolicy;
		this.policyId=productRelationPolicy.getPolicyId()==null?"":productRelationPolicy.getPolicyId();
		this.name = productRelationPolicy.getName()==null?"":productRelationPolicy.getName();
		if(productRelationPolicy.getType()!=null){
			switch (productRelationPolicy.getType()) {
			case "serial":
				this.type = "系列";
				break;
			case "product":
				this.type = "产品";
				break;
			default:
				this.type = "全局";
				break;
			}
		}else {
			this.type="";
		}
		this.level=productRelationPolicy.getLevel()
				==null?"":productRelationPolicy.getLevel().toString();
		this.minOrderUnit = productRelationPolicy.getMinOrderUnit()
				==null?"":productRelationPolicy.getMinOrderUnit().toString();
		Set<AssociatedProduct> apSet = productRelationPolicy.getAssociatedProducts();
		associatedProducts="";
		if(apSet!=null&&!apSet.isEmpty()){
			for (AssociatedProduct ap : apSet) {
				associatedProducts+=ap.getPid().toString()+":"+ap.getCoeff()+",";
			}
			if(associatedProducts.endsWith(",")){
				associatedProducts=associatedProducts.substring(0, associatedProducts.length()-1);
			}
		}
		this.status = productRelationPolicy.getStatus()==ProductRelationPolicy.enable?"启用":"禁用";
		if(productRelationPolicy.getLastUpdate()!=null){
			Date dt=new Date(productRelationPolicy.getLastUpdate());
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			this.lastUpdate=dateFormat.format(dt);
		}else{
			this.lastUpdate="";
		}
	}

	public ProductRelationPolicy getProductRelationPolicy() {
		return productRelationPolicy;
	}

	public void setProductRelationPolicy(ProductRelationPolicy productRelationPolicy) {
		this.productRelationPolicy = productRelationPolicy;
	}

	public String getName() {
		return name==null?"":name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type==null?"":type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMinOrderUnit() {
		return minOrderUnit==null?"":minOrderUnit;
	}

	public void setMinOrderUnit(String minOrderUnit) {
		this.minOrderUnit = minOrderUnit;
	}

	public String getAssociatedProducts() {
		return associatedProducts==null?"":associatedProducts;
	}

	public void setAssociatedProducts(String associatedProducts) {
		this.associatedProducts = associatedProducts;
	}

	public String getStatus() {
		return status==null?"":status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastUpdate() {
		return lastUpdate==null?"":lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getProductName() {
		return productName==null?"":productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getLevel() {
		return level==null?"":level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPolicyId() {
		return policyId==null?"":policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public Set<MaterielBean> getMaterielBean() {
		return materielBean;
	}

	public void setMaterielBean(Set<MaterielBean> materielBean) {
		this.materielBean = materielBean;
	}
	
	
}
