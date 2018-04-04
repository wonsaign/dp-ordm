package com.zeusas.dp.ordm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.active.bean.ActivityType;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.ActivityContext;
import com.zeusas.dp.ordm.active.model.ProductItem;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.CartDetailDesc;
import com.zeusas.dp.ordm.entity.DeliveryWay;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.service.CartDetailService;
import com.zeusas.dp.ordm.service.CartService;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.utils.OrdmConfig;

/***
 * 大礼包
 * 
 * @author fengx
 * @date 2017年3月24日 上午10:48:27
 */
public final class GiftPackageStrategy {

	public  void execScan(Cart cart, //
			CartDetailService cartDetailService,//
			CartService cartService, double discount) {

		ProductManager pm = AppContext.getBean(ProductManager.class);
		ActivityManager am = AppContext.getBean(ActivityManager.class);
		DictManager dm = AppContext.getBean(DictManager.class);

		List<CartDetail> cartDetails = cartService.getCartDetailByCart(cart);

		double realFee = cartService.getRealPrice(cartDetails, discount);
		List<Activity> activities = am.findByType(ActivityType.TYPE_BIGPACAKGE);
		if (activities == null || activities.isEmpty()) {
			return;
		}
		for (Activity activity : activities) {
			ActivityContext context = activity.getContext();
			if (realFee <= context.getAmount().getAmount()) {
				return;
			}
			int multiple = (int) (realFee / context.getAmount().getAmount());
			int quantity = context.getActityExtra().getQuantity();
			Map<Integer, ProductItem> productItems = context.getActityExtra()
					.getProductItem();
			CartDetail cartDetail = new CartDetail();
			cartDetail.setCartId(cart.getCartId());
			cartDetail.setActivityId(activity.getActId());
			cartDetail.setActivityName(activity.getName());
			cartDetail.setQuantity(multiple * quantity);
			cartDetail.setType(CartDetail.ActivityProduct);
			cartDetail.setPricePolicy(true);
			cartDetail.setPrice(0.0);
			List<CartDetailDesc> descs = new ArrayList<>();

			productItems
					.values()
					.stream()
					.forEach(
							e -> {
								Product p = pm.get(e.getPid());
								CartDetailDesc desc = new CartDetailDesc(p);
								desc.setPrice(0.0);
								desc.setQuantity(e.getQuantity());
								descs.add(desc);
								desc.setProductName(p.getName());
								Dictionary dictionary = dm.lookUpByCode(
										OrdmConfig.DELIVERYWAY,
										OrdmConfig.DELIVERYWAY_MARKETPRESENT);
								DeliveryWay deliveryWay = new DeliveryWay(
										dictionary);
								desc.setDeliveryWayId(deliveryWay
										.getDeliveryWayId());
								desc.setDeliveryWayName(deliveryWay
										.getDeliveryName());
							});
			cartDetailService.save(cartDetail, descs);
		}
	}
}
