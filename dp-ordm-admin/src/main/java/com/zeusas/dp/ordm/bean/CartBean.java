package com.zeusas.dp.ordm.bean;

import java.io.Serializable;

import com.zeusas.dp.ordm.entity.CustomerPricePolicy;
import com.zeusas.dp.ordm.entity.Product;

/**
 * 封装一个购物车的bean.包含了正常产品和活动组产品以及价格策略
 * 
 * @author fengx
 * @date 2016年12月6日 下午3:42:49
 */
public class CartBean implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 3025389493213749686L;

	private Product product;

	private CustomerPricePolicy pricepolicy;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public CustomerPricePolicy getPricepolicy() {
		return pricepolicy;
	}

	public void setPricepolicy(CustomerPricePolicy pricepolicy) {
		this.pricepolicy = pricepolicy;
	}
}
