package com.zeusas.dp.ordm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.NestedServletException;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.dp.ordm.active.bean.UserActivities;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.ActivityBuilder;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.bean.ProductBean;
import com.zeusas.dp.ordm.bi.service.ProductSellerManager;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.CounterSerial;
import com.zeusas.dp.ordm.entity.Notification;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ReserveProduct;
import com.zeusas.dp.ordm.entity.SerialSeller;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.rev.entity.ReservedActivity;
import com.zeusas.dp.ordm.rev.service.ReservedActivityManager;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.NotificationService;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.ReserveProductManager;
import com.zeusas.dp.ordm.service.UserCustomerManager;
import com.zeusas.security.auth.entity.AuthUser;

/**
 * 
 * @author wonsign
 * @date 2016年12月16日 下午7:47:47
 */
@Controller
@RequestMapping("/ordm")
public class IndexController extends OrdmBasicController {
	static Logger logger = LoggerFactory.getLogger(IndexController.class);
	@Autowired
	private ProductManager productManager;
	@Autowired
	private ActivityManager activityManager;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private ProductSellerManager productSellerManager;
	@Autowired
	private DictManager dictManager;
	@Autowired
	private UserCustomerManager userCustomerManager;
	@Autowired
	private CounterManager counterManager;
	@Autowired
	private ReservedActivityManager reservedActivityManager;
	@Autowired
	private ReserveProductManager reserveProductManager;
	
	/**
	 * 
	 * 显示首页，并且将所有的商品以及产品活动显示出来 新品 本地热销 全国热销 套装礼品
	 * 
	 * @param mp
	 * @param request
	 * @return
	 * @throws NestedServletException
	 */
	@RequestMapping(value = "/index")
	public String index(ModelMap mp, HttpServletRequest request)
			throws IOException {
		
		// 判断前端cookie是否开启，不开启系统无法运行
		if(request.getCookies()==null||request.getCookies().length==0){
			logger.warn("浏览器cookie未开启");
			return "login";
		}

		// 用方法，显示所有的产品，但是不包括套装礼品 只有套装活动的标题页
		final List<ProductBean> productBeans = new ArrayList<ProductBean>();
		// 门店选择
		AuthUser authUser = super.getAuthUser();
		if (authUser == null) {
			logger.warn("登录用户未空");
			return "login";
		}

		// 设置所有的柜台Session
		super.setAllCounters(request);
		List<Counter> counters = new ArrayList<Counter>();
		counters.addAll((List<Counter>)super.getAllCounters());
		mp.addAttribute("shops",counters);
		// 如果没有店面，重新登录
		if(super.getAllCounters().isEmpty()){
			logger.warn("用户没有绑定柜台");
			return "login";
		}
		
		// 设置一次性默认的CounterCode
		if (counters.size() == 1) {
			super.setDefaultCounter(counters.get(0).getCounterCode());
		}
		// 初次登录orderCounter未设定的场合
		if (super.getOrderCounter() == null) {
			super.setOrderCounter(counters.get(0));
		}
		
		try {
			// ②Message读取 设置消息到Session 默认取10条
			List<Notification> notification = notificationService.getLast(10);
			mp.addAttribute("notify", notification);
			super.setNotification(notification);
			
			// ③根据数据库未处理订单的大小处理 
			// nUse
			// UserCustomer userCustomer = userCustomerManager
			//		.getByLoginName(authUser.getLoginName());
			// 设置消息size
//			Integer orderMsgSize = null;
//			List<Order> orders = orderService.getOrders(
//					userCustomer.getCustomerLoginName(), Order.Status_UnPay);
			
			// 获取 待审核订单、待付款订单、待收货订单、财务退回 的数量
			// FIX ME:
			super.setAllOrderSize();
			mp.addAttribute("all_order_size",super.getAllOrderSize());
			// 全国畅销
			CounterSerial globalCounterSerial = productSellerManager
					.getGlobalSeriesSeller();
			List<SerialSeller> globalSerialSellers = globalCounterSerial
					.getSerials();

			// 将畅销系列的名字取出来 没有图片设置默认图片
			// 全国
			List<Dictionary> globalSerials = new ArrayList<Dictionary>();
			for (SerialSeller serialSeller : globalSerialSellers) {
				Dictionary dictionary = dictManager.lookUpByCode(Product.PRODUCT_POSITIVE_SERIES,
						serialSeller.getSid());
				// 默认图片
				// FIXME:字典里竟然有空字段
				if (dictionary != null && dictionary.isActive()) {
					if (dictionary.getUrl() == null) {
						dictionary.setUrl(ProductBean.DEFSERI);
					}
				}
				globalSerials.add(dictionary);
			}

			// 本地
			String counterCD = super.getCounterCode();
			if (counterCD == null) {
				counterCD = counters.get(0).getCounterCode();
				super.setCounterCode(counterCD);
			}
			// 设置默认仓库,设法弄成全局的
			setAutoMyStock();

			CounterSerial localCounterSerial = getCounterSerial(counterCD);
			List<SerialSeller> localSerialSellers = localCounterSerial.getSerials();
			List<Dictionary> localSerials = new ArrayList<Dictionary>();
			for (SerialSeller serialSeller : localSerialSellers) {
				Dictionary dictionary = dictManager.lookUpByCode(Product.PRODUCT_POSITIVE_SERIES,
						serialSeller.getSid());
				// 默认图片
				// FIXME:字典里竟然有空字段
				if(dictionary!=null){
					if(dictionary.getUrl()==null){
						dictionary.setUrl(ProductBean.DEFSERI);
					}
					localSerials.add(dictionary);
				}
			}

			// 放入前端 Attribute 里
			List<Dictionary> lds = productManager.findByHardCode(Product.PRODUCT_TYPE);
			ProductBean pb;
			
			// 获取门店
			Counter counter = counterManager.getCounterByCode(super.getCounterCode());
			for (Dictionary type : lds) {
				if (!type.isActive()) {
					continue;
				}
				
				switch (type.getHardCode()) {
				// 优惠活动
				case "4":
					List<Activity> activities = new ArrayList<>();
					// 全国活动
					List<Activity> globalAct = activityManager.findGlobal();
					activities.addAll(globalAct);
					// 私有活动
					List<Activity> myAct = activityManager.findMyActivities(counterManager.getCounterByCode(super.getCounterCode()));
					activities.addAll(myAct);
					
					// 去除预定会的活动
					activities = activities.stream()//
							.filter(e->reservedActivityManager.isAvailableActivity(counter, e.getActId()))//
							.collect(Collectors.toList());
					
					UserActivities userActivitiesGoble = ActivityBuilder.build(activities);
					super.setUserActivityList(userActivitiesGoble);
					
					if(activities.size()<1){
						pb = new ProductBean(null, null, null);
					}else{
						pb = new ProductBean(type, null, activities);
					}
					productBeans.add(pb);
					break;
				// 新品推出
				case "1":
					List<Product> products = productManager.findByType(type
							.getHardCode());
					if(products==null){
						pb = new ProductBean(null, null, null);
					}else{
						// 去除预定会的产品
						products = products.stream()//
								.filter(e->reservedActivityManager.isAvailableProduct(counter, e.getProductId()))//
								.collect(Collectors.toList());
						// products = ProductBean.cloneProducts(products);
						pb = new ProductBean(type, products, null);
					}
					productBeans.add(pb);
					break;
				// 全国畅销
				case "2":
					pb = new ProductBean();
					pb.setProductType(type);
					pb.setSeries(globalSerials);
					productBeans.add(pb);
					break;
				// 集客商品	
				case "5":
					// 这里设置集客产品
					break;
				// 预定会
				case "6":
					// 根据门店获取预定会列表
					List<ReservedActivity> reservedActivities = reservedActivityManager.getMyReservedActivity(counter);
					if(reservedActivities.size() < 1){
						pb = new ProductBean(null, null, null);
					}else{
						Set<Integer> proIds = new HashSet<>();
						Set<String> actIds = new HashSet<>();
						for (ReservedActivity reservedAct : reservedActivities) {
							proIds.addAll(reservedAct.getContext().getProducts());
							actIds.addAll(reservedAct.getContext().getActivities());
						}
						// 获取预定会的产品
						List<Product> revProducts = new ArrayList<>();
						if ((proIds != null) && (proIds.size() > 0)) {
							revProducts = productManager.findAll().stream()//
									.filter(e->proIds.contains(e.getProductId()))//
									.collect(Collectors.toList());
						}
						// 获取预定会的活动
						List<Activity> revActivities = new ArrayList<>();
						if ((actIds != null) && (actIds.size() > 0)) {
							revActivities = activityManager.findAvaliable().stream()//
								.filter(e->actIds.contains(e.getActId()))//
								.collect(Collectors.toList());
						}
						pb = new ProductBean(type, revProducts, revActivities);
					}
					pb.setReservedActivities(reservedActivities);
					productBeans.add(pb);
					break;
				// 打欠商品
				case "7":
					// 获取打欠列表
					List<ReserveProduct> revProducts = reserveProductManager.findMyReserveProduct(counter);
					if(revProducts==null||revProducts.isEmpty()){
						pb = new ProductBean();
					}else{
						// 获取产品列表
						List<Product> productLst = new ArrayList<>();
						for (ReserveProduct resPro : revProducts) {
							if (reservedActivityManager.isAvailableProduct(counter, resPro.getProductId())) {
								productLst.add(productManager.get(resPro.getProductId()));
							}
						}
						pb = new ProductBean(type, productLst, null);
					}
					productBeans.add(pb);
					break;
				// 本地畅销	
				default:
					pb = new ProductBean();
					pb.setProductType(type);
					pb.setSeries(localSerials);
					productBeans.add(pb);
				}
			}
			Collections.sort(productBeans);
			mp.addAttribute("productBean", productBeans);
			// 设置前段StockId
			setResAttr(mp);
		} catch (UnauthenticatedException e) {
			return "login";
		} catch (Exception e) {
			logger.error("显示产品错误", e.getMessage());
		}
		return "index";
	}

	public CounterSerial getCounterSerial(String counterCode) {
		CounterSerial cs = super.getCounterSerial(counterCode);
		if (cs == null) {
			cs = productSellerManager.getSeriesSeller(counterCode);
			super.setCounterSerial(cs);
		}
		return cs;
	}
}	
