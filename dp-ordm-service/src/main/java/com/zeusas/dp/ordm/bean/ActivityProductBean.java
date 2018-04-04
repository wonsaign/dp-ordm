package com.zeusas.dp.ordm.bean;

import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.CartDetailDesc;

/**
 * 封装一个用户自己挑选的活动组合的bean
 * 
 * @author fengx
 * @date 2016年12月11日 上午9:20:25
 */
public class ActivityProductBean {

	// 购物车明细(每一行是产品的明细，此时一个活动组在这里体现的也是一个产品)
	private CartDetail cartDetail;
	// 活动组具体的体现，买了多少，送了多少，多少走费用比等
	private CartDetailDesc activityCartDetail;

	public CartDetail getCartDetail() {
		return cartDetail;
	}

	public void setCartDetail(CartDetail cartDetail) {
		this.cartDetail = cartDetail;
	}

	public CartDetailDesc getActivityCartDetail() {
		return activityCartDetail;
	}

	public void setActivityCartDetail(CartDetailDesc activityCartDetail) {
		this.activityCartDetail = activityCartDetail;
	}

}
