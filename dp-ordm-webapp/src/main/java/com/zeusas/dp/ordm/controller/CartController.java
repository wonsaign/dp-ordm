package com.zeusas.dp.ordm.controller;

import static com.zeusas.dp.ordm.entity.Cart.STATUS_COMMIT;
import static com.zeusas.dp.ordm.entity.Cart.STATUS_OTHERLOCK;
import static com.zeusas.dp.ordm.entity.Cart.STATUS_UNLOCK;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.service.SmsService;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.common.utils.Status;
import com.zeusas.core.http.ActionException;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.bean.CartBean;
import com.zeusas.dp.ordm.bean.CartLocker;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.CartDetailDesc;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.Item;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductRelationPolicy;
import com.zeusas.dp.ordm.entity.ReserveProduct;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.service.ActivityLimitManager;
import com.zeusas.dp.ordm.service.BalanceService;
import com.zeusas.dp.ordm.service.CartService;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.PRPolicyManager;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.ReserveProductManager;
import com.zeusas.dp.ordm.service.StockService;
import com.zeusas.dp.ordm.service.UserCustomerManager;
import com.zeusas.dp.ordm.utils.OrdmConfig;
import com.zeusas.security.auth.entity.AuthUser;
import com.zeusas.security.auth.service.AuthCenterManager;

/**
 * 
 * @author fengx
 * @date 2016年12月6日 下午3:00:46 购物车的初始化是在用户创建的时候就给其初始化一个购物
 */
@Controller
@RequestMapping("/cart")
public class CartController extends OrdmBasicController {

	static Logger logger = LoggerFactory.getLogger(CartController.class);
	
	@Autowired
	private CartService cartService;
	@Autowired
	private AuthCenterManager authCenterManager;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CounterManager counterManager;
	@Autowired
	private CustomerManager customerManager;
	@Autowired
	private ProductManager productManager;
	@Autowired
	private UserCustomerManager userCustomerManager;
	@Autowired
	private DictManager dictManager;
	@Autowired
	private SmsService smsService;
	@Autowired
	private ActivityManager activityManager;
	@Autowired
	private PRPolicyManager prPolicyManager;
	@Autowired
	private ReserveProductManager reserveProductManager;
	@Autowired
	private ActivityLimitManager activityLimitManager;
	@Autowired
	private BalanceService balanceService;
	@Autowired
	private StockService stockService;

	/**
	 * 基于门店拿到一个购物车，然后判断该购物车是否被提交或者被其他人锁了
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getislockbyother", method = RequestMethod.POST)
	public @ResponseBody CartBean isLockByOther() throws IOException {
		Counter counter = getOrderCounter();
		if (counter == null) {
			return null;
		}
		Integer counterId = counter.getCounterId();
		if (super.getAuthUser()==null) {
			return null;
		}
		String loginName = super.getAuthUser().getLoginName();
		try {
			// 1.根据门店拿到一个购物车
			Cart cart = cartService.getCounterCart(counter);
			// 判断购物车是否为提交状态
			if (cart == null) {
				return null;
			}
			// 2.构建一个购物车锁
			CartLocker cartLocker = new CartLocker(cart.getCartId(), //
					counterId, //
					loginName);
			CartBean cb = new CartBean();
			// 查看这个购物车是否被其他人给锁了
			if (!super.isLockByOther(cartLocker)) {
				super.setCartLocker(cartLocker);
			}
			cb.setCounterId(counterId);
			if (STATUS_COMMIT.equals(cart.getStatus())) {
				cb.setStatus(STATUS_COMMIT);
				cb.setDesc("购物车等待审核中...");
				return cb;
			}
			if (super.isLockByOther(cartLocker)) {
				cb.setStatus(STATUS_OTHERLOCK);
				cb.setDesc("正在被其他用户(" + getCartLocker(cart.getCounterId()).getUserId() + ")编辑");
				return cb;
			}
			cb.setStatus(STATUS_UNLOCK);
			return cb;

		} catch (Exception e) {
			logger.error("检查购物车是否锁定时发生错误。", e);
		}
		return null;
	}

	@RequestMapping(value = "/getcartstatus", method = RequestMethod.POST)
	public @ResponseBody List<CartBean> getCartStatus(Model model) throws IOException {
		// List<Counter> counters = new ArrayList<Counter>();
		try {
			List<Integer> counterIds = super.getAllCounterIds();
			if (counterIds.isEmpty()) {
				throw new UnauthenticatedException("帐号异常 没有柜台，无权登录。");
			}

			List<Cart> carts = cartService.findAllCart(counterIds);
			List<CartBean> lcbs = new ArrayList<CartBean>();

			// 判断门店是否有购物车
			List<Integer> lcart_counterids = new ArrayList<>();
			for (Cart cart : carts) {
				lcart_counterids.add(cart.getCounterId());
			}
			for (Integer counterId : counterIds) {
				if (!lcart_counterids.contains(counterId)) {
					CartBean cartBean = new CartBean(counterId, STATUS_UNLOCK, "正常");
					lcbs.add(cartBean);
				}
			}

			// 判断门店购物车是否被锁定、提交
			for (Cart cart : carts) {
				Integer counterId = cart.getCounterId();
				// 去缓存里面去拿到一个购物车
				CartLocker cartLocker = getCartLocker(counterId);
				// 查看购物车是否被用户提交
				if (cart.getStatus() == STATUS_COMMIT) {
					CartBean cartBean = new CartBean(counterId, STATUS_COMMIT, "购物车等待审核中...");
					lcbs.add(cartBean);
					continue;
				}
				// 如果锁为空 购物车也没有被锁
				if (cartLocker == null) {
					CartBean cartBean = new CartBean(counterId, STATUS_UNLOCK, "正常");
					lcbs.add(cartBean);
					continue;
				}
				// 判断购物车是否被自己锁
				if (!cartLocker.isLocked(super.getAuthUser().getLoginName())) {
					CartBean cartBean = new CartBean(counterId, STATUS_UNLOCK, "正常");
					lcbs.add(cartBean);
					continue;
				}
				// 如果购物车是被其他人锁了 就显示购物车状态是被其他人编辑的
				if (cartLocker.isLocked(super.getAuthUser().getLoginName())) {
					CartBean cartBean = new CartBean(counterId, STATUS_OTHERLOCK,
							"正在被其他用户(" + cartLocker.getUserId() + ")编辑");
					lcbs.add(cartBean);
					continue;
				}
			}
			return lcbs;
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public String cart(ModelMap model) throws IOException {
		// 设置默认仓库,设法弄成全局的
		setAutoMyStock();
		// 设置前段StockId
		setResAttr(model);
		try {
			Cart cart = cartService.getCounterCart(super.getOrderCounter());
			if (cart == null || super.getOrderCounter() == null) {
				return "cart";
			}
			model.addAttribute("cart", cart);
			model.addAttribute("counter", super.getOrderCounter());
			// 购物车明细
			List<CartDetail> cartDetails = cartService.getCartDetailByCart(cart);
			model.addAttribute("lcds", cartDetails);

			// 获取折扣
			double materialDiscout = getProductDiscount();
			model.addAttribute("discout", materialDiscout);
			// 封装cartBean
			List<CartBean> cartBeans = new ArrayList<>();
			// 判空
			if (cartDetails.isEmpty()) {
				model.addAttribute("lcdds", cartBeans);
				return "cart";
			} 
			
			List<CartDetailDesc> cartDetailDescs = new ArrayList<>();
			List<Product> products = new ArrayList<Product>();
			// 多次循环遍历 封装Bean 可以解决统一产品 在A活动是打欠产品 在B活动是非打欠产品
			for (CartDetail cartDetail : cartDetails) {
				String actID = cartDetail.getActivityId();
				Set<Integer> revProducts = new HashSet<>();
				// 默认状态未0
				int status = Integer.valueOf(0);
				if(!Strings.isNullOrEmpty(actID)) {
					Activity act = activityManager.get(actID);
					status = act.getContext().getRevWmsStatus(super.getOrderCounter().getWarehouses());
					revProducts = act.getContext().getRevProducts();
				}
				// 为NUll 默认为0
				List<CartDetailDesc> cartDetailDs = cartDetail.getCartDetailsDesc();
				
				for (CartDetailDesc desc : cartDetailDs) {
					cartDetailDescs.add(desc);
					Product p = productManager.get(desc.getProductId());
					if (!products.contains(p)) {
						products.add(p);
					}
					CartBean cartBean;
					// 打欠中  和 可打欠 都要反映给反映给前端
					if(ReserveProduct.STATUS_RESERVABLE.intValue() == status //
							|| ReserveProduct.STATUS_RESERVED.intValue() == status //
							&& revProducts.contains(desc.getProductId())) {
						cartBean = new CartBean(desc,status);
					}else {
						// 0 代表正常产品
						cartBean = new CartBean(desc,Integer.valueOf(0));
					}
					cartBeans.add(cartBean);
				}
			}
			// 包装商品
			wrapProduct(products);
			model.addAttribute("lcdds", cartBeans);
			model.addAttribute("lps", products);
		} catch (Exception e) {
			logger.error("CartCotrollor异常,method {}","cart", e);
		}
		return "cart";
	}

	// 首页或订货添加商品到购物车
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody DSResponse addCart(Product productVO, int num) throws IOException {
		try {
			Assert.notNull(num, "");
			Assert.notNull(productVO, "");
			// 预售产品是否在正确的时间
			Integer pid = productVO.getProductId();
			
			Counter counter=super.getOrderCounter();
			if(counter == null) {
				logger.warn("柜台号为空,登录超时.");
				return new DSResponse(Status.FAILURE, "柜台号为空,登录超时.");
			}
			Cart cart = cartService.getCounterCart(counter);
			if(cart == null) {
				return new DSResponse(Status.FAILURE, "购物车为空");
			}
			ReserveProduct reserveProduct = reserveProductManager.get(pid);
			// 产品ID and 仓库ID
			String stockId = counter.getWarehouses();
			if (reserveProduct != null) {
				boolean isSelling = reserveProductManager.isSelling(pid, stockId);
				boolean isReserving = reserveProductManager.isReserving(pid, stockId);
				boolean isReservable = reserveProductManager.isReservable(pid, stockId);
				// 不是正常销售 不是可打欠 不是是打欠中 => 还欠中.... 不可加入购物车
				if (!isSelling && !isReservable && !isReserving) {
					return new DSResponse(Status.FAILURE, "该产品不在预售时间之内！");
				}
			}
			Product product = productManager.get(pid);
			// 获取最小订货单位并判断是否满足
			ProductRelationPolicy relationPolicy = prPolicyManager.get(product);
			int required = relationPolicy.getMinOrderUnit();
			if (num % required != 0) {
				return new DSResponse(Status.FAILURE, "购物车删除商品信息异常！");
			}
			cartService.add(cart, product, num);
			return new DSResponse(Status.SUCCESS, "OK");
		} catch (Exception e) {
			logger.error("CartCotrollor异常,method {}","addCart", e);
		}
		return new DSResponse(Status.FAILURE, "购物车删除商品信息异常！");
	}

	// 活动商品添加
	@RequestMapping(value = "/addactivity", method = RequestMethod.POST)
	public @ResponseBody DSResponse addProductGroup(HttpServletRequest req, String actId,
			@RequestParam(value = "json") String json, int num) throws IOException {
		try {
			Cart cart = cartService.getCounterCart(super.getOrderCounter());
			if (super.getOrderCounter() == null) {
				return new DSResponse(Status.FAILURE, "柜台号不存在.");
			}
			List<Item> items = JSON.parseArray(json, Item.class);
			if (items == null || items.size() <= 0) {
				return new DSResponse(Status.FAILURE, "您未选择要购买的产品，请在活动界面选择具体想购买的产品以及数量！");
			}
			cartService.add(cart.getCartId(), actId, items, num);
			return new DSResponse(Status.SUCCESS, "OK");
		} catch (Exception e) {
			logger.error("CartCotrollor异常.", e);
		}
		return new DSResponse(Status.FAILURE, "添加活动商品信息异常！");
	}

	/**
	 * 购物车内修改商品数量
	 * 
	 * @param req
	 * @param detailId
	 * @param num
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody DSResponse updateCart(HttpServletRequest req, Long detailId, Integer num) throws IOException {
		try {
			Assert.notNull(detailId, "");
			Assert.notNull(num, "");
			cartService.update(detailId, num);
			return new DSResponse(Status.SUCCESS, "OK");
		} catch (Exception e) {
			logger.error("CartCotrollor异常，商品详细ID:{}", detailId, e);
		}
		return new DSResponse(Status.FAILURE, "购物车删除商品信息异常！");
	}

	// 购物车删除商品信息:成套删除
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody DSResponse deleteCartProduct(Long detailId) throws IOException {
		try {
			Assert.notNull(detailId, "");
			cartService.deleteDetailAndDesc(detailId);
			return new DSResponse(Status.SUCCESS, "OK");
		} catch (Exception e) {
			logger.error("CartCotrollor异常,method{},detailId{}","deleteCartProduct",detailId, e);
		}
		return new DSResponse(Status.FAILURE, "购物车删除商品信息异常！");
	}

	// 清空购物车
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public @ResponseBody DSResponse clear(Long cartID) {
		try {
			Assert.notNull(cartID, "清除购物车,购物车为空");
			cartService.deleteCart(cartID);
		} catch (Exception e) {
			logger.error("CartCotrollor异常,method{},cartId{}","clear",cartID, e);
		}
		return new DSResponse(Status.FAILURE, "清空购物车异常！");
	}

	// 计算购物车数量 未处理
	// 计算物料配比
	@RequestMapping(value = "/getpresentprice", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getPresentPrice(Cart cart) throws IOException {
		try {
			// FIXME 因为这里 购物车可能不存在产品，导致获取物料费用异常
			if(cart == null) {
				throw new ActionException("Session超时 参数为空");
			}
			if(cart.getCounterId() == null) {
				throw new ActionException("购物车内ID为空");
			}
			Counter co = counterManager.getCounterById(cart.getCounterId());
			Map<String, Object> map = new LinkedHashMap<>();

			if (co != null) {
				Customer cu = customerManager.get(co.getCustomerId());

				// XXX:石斛兰预定会的活动 (8000,12000,50000礼包)
//				Counter counter = counterManager.getCounterByCode(super.getCounterCode());
//				List<Activity> myActis = activityManager.findMyActivities(counter);
//				if (!StringUtil.isEmpty(myActis)) {
//					// 折前要购物的价格
//					Double amount = cartService.getUsefulActFeeInCart(counter);
//					List<Activity> activitys = orderService.getSuitableActId(counter, amount);
//
//					if (activitys.size() > 0) {
//						List<String> actNums = new ArrayList<>();
//						for (Activity act : activitys) {
//							actNums.add(Double.toString(act.getContext().getAmount().getAmount()));
//						}
//						Map<String, List<String>> doActes = new HashMap<String, List<String>>();
//						doActes.put(activitys.get(0).getName(), actNums);
//						// 参加了什么活动
//						map.put("doAct", doActes);
//					}
//					// 显示金额是多少
//					map.put("myAmount", amount / getProductDiscount());
//				}
				
				Double[] mqty =	cartService.getPAndMQty(cart, productManager);
				double free = cartService.getMaterialFee(cart, cu.getCustomerTypeID());
				// OrdmConfig.KEY_MAXAMOUNT = 204001:物流免费最小金额
				String freeemoney = dictManager.get(OrdmConfig.KEY_MAXAMOUNT).getName();
				// 获取折扣0
				double materialDiscout = getProductDiscount();
				map.put("discout", materialDiscout);
				map.put("materialfee", free);
				// lqtys:
				map.put("lqtys", mqty);
				map.put("freeemoney", freeemoney);
			}
			return map;
		} catch (ActionException e) {
			logger.warn("CartCotrollor警告,method:{}","getPresentPrice", e.getMessage());
		} catch (Exception e) {
			logger.error("CartCotrollor异常,method{}","getPresentPrice", e);
		}
		return null;
	}

	// 购物车审核
	@RequestMapping(value = "/cartcommit", method = RequestMethod.GET)
	public @ResponseBody DSResponse cartCommit(HttpServletRequest req, ModelMap model, Cart cartVO) throws IOException {
		// 设置默认仓库,设法弄成全局的
		setAutoMyStock();
		// 设置前段StockId
		setResAttr(model);
		try {
			Cart cart = cartService.get(cartVO.getCartId());
			Counter counter = counterManager.getCounterById(cart.getCounterId());
			Customer customer = customerManager.get(counter.getCustomerId());
			List<CartDetail> cartDetails = cartService.getCartDetailByCart(cart);
			if(cartDetails == null || cartDetails.isEmpty()) {
				return new DSResponse(Status.FAILURE, "订单明细为空,不能提交.");
			}
			// 活动校验条目
			if(!activityManager.validateCart(cartDetails)){
				return new DSResponse(Status.FAILURE, "活动条目不对,请检查活动条目数.");
			}
			// 检查总金额是否满足大礼包要求
			if (!checkBigPkAmt(cart)) {
				// 不满足大礼包
				return new DSResponse(Status.FAILURE, "订单金额不满足大礼包金额要求。");
			}
			
			// 检查活动是否限制订货数量
			Map<String, Integer> beyondRecordMap = beyondRecord(customer.getCustomerID(),cartDetails);
			if(beyondRecordMap.size() > 0) {
				// 返回错误信息
				StringBuffer errorMessage = new StringBuffer();
				for (String acti : beyondRecordMap.keySet()) {
					Integer outQty = beyondRecordMap.get(acti);
					String actName = activityManager.get(acti).getName();
					errorMessage.append("超出数量的活动:" + actName).append("超出了数量"+ outQty + "套.");
				}
				return new DSResponse(Status.FAILURE,"超过限定活动购买数量:"+errorMessage);
			}

			// 直营限额
			Double materialFee = cartService.getMaterialFee(cart, customer.getCustomerTypeID());
			if (materialFee > 0.01 && customer.getCustomerTypeID() == 11387) {
				return new DSResponse(Status.FAILURE, "直营限额，不能超过物料费比");
			}
			
			if (!cartService.checkReserveActivity(cartDetails)) {
				return new DSResponse(Status.FAILURE, "购物车内不能同时包含预订会和正常产品");
			}

			// 这里做赠送礼包提示 BIGPACK isAutoAlloca = true 
			StringBuffer message = new StringBuffer("");
			Map<Activity, Integer> sysSendSuit = orderService.getAutoBigPackage(cart);
			if(sysSendSuit.size() > 0) {
				for (Activity activity : sysSendSuit.keySet()) {
					// 提示添加了活动类型 
					message.append("您已经满足活动:")//
						.append(activity.getName())//
						.append("。共")//
						.append(sysSendSuit.get(activity))//
						.append("套.");//
				}
			}
			
			// 消除掉 ordercounter session 及锁定的session
			super.removeCartLocker(cart.getCounterId());
			super.removeOrderCounter();

			//
			cartService.commitCart(super.getAuthUser(), cart.getCartId());
			// 发送通知短信
			UserCustomer u = userCustomerManager.getByLoginName(super.getAuthUser().getLoginName());
			if (u.getLoginName().equals(u.getCustomerLoginName())) {
				Customer boss = customerManager.get(u.getCustomerId());
				String template = "SMS_60390488";
				HashMap<String, String> ctx = new HashMap<String, String>();
//				smsService.send(template, boss.getMobile(), ctx);
			}
			return new DSResponse(Status.SUCCESS, message.toString());
		} catch (Exception e) {
			logger.error("CartCotrollor异常 ,method{}","cartCommit", e);
		}
		return new DSResponse(Status.FAILURE, "购物车提交错误");
	}

	/**
	 * 购物车审核管理清单
	 * 
	 * @param req
	 *            HttpServletRequest对象
	 * @param model
	 * @return 处理完后，返回到當前页面
	 */
	@RequestMapping(value = "/cart_manage", method = RequestMethod.POST)
	public @ResponseBody List<Cart> cartManager(HttpServletRequest req, Model model) throws IOException {
		List<Integer> lis = super.getAllCounterIds();
		if (lis.isEmpty()) {
			logger.error("帐号异常，没有柜台，无权登录。");
			return null;
		}
		try {
			return cartService.getCheckCarts(lis);
		} catch (Exception e) {
			logger.error("CartCotrollor异常 , method{}" ,"cartManager", e);
		}
		return null;
	}

	// 购物车审核管理明细
	@RequestMapping(value = "/cart_managedetail", method = RequestMethod.GET)
	public String cartManagerDetail(ModelMap model, Long cartId) throws IOException {
		// 设置默认仓库,设法弄成全局的
		setAutoMyStock();
		// 设置前段StockId
		setResAttr(model);
		try {
			Cart cart = cartService.get(cartId);
			if(cart == null) {
				logger.warn("购物车为空,ID{}", cartId);
				return null;
			}
			model.addAttribute("cart", cart);

			List<CartDetail> cartDetail = cartService.getCartDetailByCart(cart);
			model.addAttribute("lcds", cartDetail);
			if (cartDetail.isEmpty()) {
				model.addAttribute("lcdds", "");
			} else {
				List<CartDetailDesc> cartDetailDescs = cartService.getCartDescByCartDetail(cartDetail);
				Set<Product> lpsSet = new LinkedHashSet<Product>();
				for (CartDetailDesc cdd : cartDetailDescs) {
					Product p = productManager.get(cdd.getProductId());
					lpsSet.add(p);
				}

				final List<Product> products = new ArrayList<>(lpsSet);
				// 添加各种标识
				wrapProduct(products);

				model.addAttribute("lcdds", cartDetailDescs);
				model.addAttribute("lps", products);
			}

			// 获取当前门店信息
			Counter counter = counterManager.getCounterById(cart.getCounterId());
			model.addAttribute("counter", counter);
		} catch (Exception e) {
			logger.error("CartCotrollor异常 method{},id{}","cartManagerDetail",cartId, e);
		}
		return "cart_managedetail";
	}

	/**
	 * 购物车审核拒绝
	 * @param model
	 * @param cart
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/cartcheckrefuse", method = RequestMethod.POST)
	public @ResponseBody DSResponse cartCheckRefuse(Model model, Cart cart) throws IOException {
		if (cart == null) {
			return new DSResponse(Status.FAILURE, "审核处理错误");
		}
		try {
			orderService.refuse(cart.getCartId());
			return new DSResponse(Status.SUCCESS, "完成");
		} catch (Exception e) {
			logger.error("CartCotrollor异常, CartID={}", cart.getCartId(), e);
		}
		return new DSResponse(Status.FAILURE, "审核处理错误");
	}

	/**
	 * 检查待审核到审核状态是否有41 类型的活动
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/checkcondition", method = RequestMethod.POST)
	@Deprecated
	public @ResponseBody Map checkcondition(Cart cartVO) throws IOException {
		// TODO:去购物车明细表里去拿取里面的是否参加该类型的活动，参加了，检查金额，从车中的所有商品外加
		Map<String, Map<String, List<String>>> data = new HashMap<>();
//		try {
//			Cart realCart = cartService.get(cartVO.getCartId());
//			// 折前要购物的价格
//			Counter counter = counterManager.get(realCart.getCounterId());
//			List<Activity> myActis = activityManager.findMyActivities(counter);
//			Double amount = cartService.getUsefulActFeeInCart(counter);
//			if (!StringUtil.isEmpty(myActis)) {
//				// 折前要购物的价格
//				List<Activity> activitys = orderService.getSuitableActId(counter, amount);
//
//				if (activitys.size() > 0) {
//					List<String> actNums = new ArrayList<>();
//					for (Activity act : activitys) {
//						// TODO: Format double String...
//						actNums.add(Double.toString(act.getContext().getAmount().getAmount()));
//					}
//					Map<String, List<String>> doActes = new HashMap<String, List<String>>();
//					doActes.put(activitys.get(0).getName(), actNums);
//					// 参加了什么活动
//					data.put("0", doActes);
//					return data;
//				}
//			}
//		} catch (Exception e) {
//			logger.error("", e);
//		}
		data.put("-1", new HashMap<String, List<String>>(0));
		return data;
	}

	// 购物车审核通过
	@RequestMapping(value = "/cartcheckpass", method = RequestMethod.POST)
	public @ResponseBody DSResponse cartCheckPass(Model model, Cart cartVO) throws IOException {
		try {
			Cart cart = cartService.get(cartVO.getCartId());
			// 1，表示为：活跃状态;2，表示为：可以提交状态;
			if (cart == null || !Cart.STATUS_COMMIT.equals(cart.getStatus())) {
				logger.warn("购物车：{}为空。", cartVO.getCartId());
				return new DSResponse(Status.FAILURE, "购物车为空");
			}
			Counter counter = counterManager.getCounterById(cart.getCounterId());
			Customer customer = customerManager.get(counter.getCustomerId());
			List<CartDetail> cartDetails = cartService.getCartDetailByCart(cart);
			if(cartDetails == null || cartDetails.isEmpty()) {
				return new DSResponse(Status.FAILURE, "订单明细为空,不能提交.");
			}
			// 直营限额
			Double materialFee = cartService.getMaterialFee(cart, customer.getCustomerTypeID());
			// 11387 代表 直营类型
			if (materialFee > 0.01 && customer.getCustomerTypeID() == 11387) {
				return new DSResponse(Status.FAILURE, "直营限额，不能超过物料费比");
			}
			// 检查总金额是否满足大礼包要求
			if (!checkBigPkAmt(cart)) {
				// 不满足大礼包
				return new DSResponse(Status.FAILURE, "订单金额不满足大礼包金额要求。");
			}
			
			// 活动校验条目
			if(!activityManager.validateCart(cartDetails)){
				return new DSResponse(Status.FAILURE, "活动条目不对,请检查活动条目数.");
			}
			// TODO: 减少记录
			activityLimitManager.commitCartToOrder(counter.getCustomerId() , cartDetails);
			
			// 因为提交一单以后购物车就被清空了，所以后续连点就会产生空订单
			Order successOrddr = orderService.buildOrder(cart, counter, authCenterManager.getAuthUser(request.getRemoteUser()), // 审核人
					authCenterManager.getAuthUser(cart.getUserId()), // 制单人
					customer.getCustomerTypeID());
			// 自动加大礼包
			Map<Activity, Integer> act = orderService.getAutoBigPackage(Arrays.asList(orderService.get(successOrddr.getId())));
			for(Activity activity : act.keySet()){
				int suitNum = stockService.getSuitNum(activity.getContext().getActityExtra(),act.get(activity),orderService.get(successOrddr.getId()));
				if(suitNum>0){
				orderService.addBigPackageToOrder(orderService.get(successOrddr.getId()),activity.getActId(), suitNum);
				}
			}
//			orderService.AddAutoBigPackage(successOrddr);
			
			// 默认使用余额支付
			double usefulBalance = getUseflBalance(request);
			orderService.userBalance(successOrddr.getId(), usefulBalance, true);

			return new DSResponse(Status.SUCCESS, "完成");
		} catch (Exception e) {
			logger.error("CartCotrollor异常, cartId={}", cartVO.getCartId(), e);
		}
		return new DSResponse(Status.FAILURE, "审核处理错误");
	}
	
	private double getUseflBalance(HttpServletRequest request) {
		AuthUser authUser = this.getAuthUser();
		UserCustomer uc = userCustomerManager.getByLoginName(authUser.getLoginName());
		return balanceService.getUsefulBalance(uc.getCustomerId());
	}

	@RequestMapping("/initcartnum")
	@ResponseBody
	public List<Cart> initNum() throws IOException {
		List<Cart> carts = new ArrayList<Cart>(1);
		// 柜台未确定，返回空车！
		Counter counter = super.getOrderCounter();
		if (counter == null) {
			return carts;
		}
		try {
			Cart cart = cartService.getCounterCart(counter);
			if (cart != null) {
				carts.add(cart);
			}
		} catch (Exception e) {
			logger.error("获取购物车失败,柜台为空", e);
		}
		return carts;
	}
}
