package com.zeusas.dp.ordm.controller;

import static com.zeusas.dp.ordm.entity.Cart.STATUS_COMMIT;
import static com.zeusas.dp.ordm.entity.Cart.STATUS_OTHERLOCK;
import static com.zeusas.dp.ordm.entity.Cart.STATUS_UNLOCK;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.zeusas.common.service.DictManager;
import com.zeusas.common.service.SmsService;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.common.utils.Status;
import com.zeusas.dp.ordm.active.bean.ActivityType;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.AmountRule;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.bean.CartBean;
import com.zeusas.dp.ordm.bean.CartLocker;
import com.zeusas.dp.ordm.entity.Cart;
import com.zeusas.dp.ordm.entity.CartDetail;
import com.zeusas.dp.ordm.entity.CartDetailDesc;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.CustomerPricePolicy;
import com.zeusas.dp.ordm.entity.Item;
import com.zeusas.dp.ordm.entity.Product;
import com.zeusas.dp.ordm.entity.ProductRelationPolicy;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.service.CartService;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.PRPolicyManager;
import com.zeusas.dp.ordm.service.PricePolicyManager;
import com.zeusas.dp.ordm.service.ProductManager;
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
	private double materialDiscout;
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
	private PricePolicyManager pricePolicyManager;
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

	/**
	 * 基于门店拿到一个购物车，然后判断该购物车是否被提交或者被其他人锁了
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
		String loginName = super.getAuthUser().getLoginName();
		try {
			// 1.根据门店拿到一个购物车
			Cart cart = cartService.getCounterCart(counter);
			// 判断购物车是否为提交状态
			if (STATUS_COMMIT.equals(cart.getStatus())) {
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
			// FIXME fengx  getCartLocker(cart.getCounterId()).getUserId()
			if (super.isLockByOther(cartLocker)) {
				cb.setStatus(STATUS_OTHERLOCK);
				cb.setDesc("正在被其他用户(" +getCartLocker(cart.getCounterId()).getUserId() + ")编辑");
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
	public @ResponseBody List<CartBean> getCartStatus(Model model)
			throws IOException {
		AuthUser authUser = getAuthUser();
		List<Counter> lcounters = new ArrayList<Counter>();
		try {
			Set<String> counterSet = userCustomerManager.getByLoginName(authUser.getLoginName())
					.getCounters();
			for (String code : counterSet) {
				Counter c = counterManager.getCounterById(Integer.parseInt(code));
				lcounters.add(c);
			}
			if (lcounters.isEmpty()) {
				throw new UnauthenticatedException("帐号异常，无权登录。");
			}

			List<Integer> lcounterids = new ArrayList<>();
			for (Counter counter : lcounters) {
				lcounterids.add(counter.getCounterId());
			}

			List<Cart> lcarts = cartService.findAllCart(lcounterids);
			List<CartBean> lcbs = new ArrayList<CartBean>();

			// 判断门店是否有购物车
			List<Integer> lcart_counterids = new ArrayList<>();
			for (Cart cart : lcarts) {
				lcart_counterids.add(cart.getCounterId());
			}
			for (Integer counterId : lcounterids) {
				if (!lcart_counterids.contains(counterId)) {
					CartBean cartBean = new CartBean(counterId, STATUS_UNLOCK, "正常");
					lcbs.add(cartBean);
				}
			}

			// 判断门店购物车是否被锁定、提交
			for (Cart cart : lcarts) {
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
				// FIXME  fengx  这里逻辑好像是有点问题的是  
				// 如果购物车是被其他人锁了  就显示购物车状态是被其他人编辑的
				if (cartLocker.isLocked(super.getAuthUser().getLoginName())) {
					CartBean cartBean = new CartBean(counterId, STATUS_OTHERLOCK, "正在被其他用户("
							+ cartLocker.getUserId() + ")编辑");
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
			model.addAttribute("cart", cart);

			model.addAttribute("counter", super.getOrderCounter());

			List<CartDetail> lcds = cartService.getCartDetailByCart(cart);
			model.addAttribute("lcds", lcds);

			// 获取折扣
			materialDiscout = getMaterialDiscout();
			model.addAttribute("discout", materialDiscout);

			if (lcds.isEmpty()) {
				model.addAttribute("lcdds", "");
			} else {
				List<CartDetailDesc> lcdds = cartService.getCartDescByCartDetail(lcds);
				List<Product> lps = new ArrayList<Product>();
				for (CartDetailDesc cdd : lcdds) {
					Product p = productManager.get(cdd.getProductId());
					if (!lps.contains(p)) {
						lps.add(productManager.get(cdd.getProductId()));
					}
				}
				model.addAttribute("lcdds", lcdds);
				model.addAttribute("lps", lps);
			}

		} catch (Exception e) {
			logger.error("CartCotrollor异常", e);
		}
		return "cart";

	}

	// 首页或订货添加商品到购物车
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody DSResponse addCart(Product product, int num) throws IOException {
		try {
			Assert.notNull(num);
			Assert.notNull(product);
			product = productManager.get(product.getProductId());
			Cart cart = cartService.getCounterCart(super.getOrderCounter());
			// 获取最小订货单位并判断是否满足
			ProductRelationPolicy relationPolicy = prPolicyManager.get(product);
			int required = relationPolicy.getMinOrderUnit();
			if (num % required != 0) {
				return new DSResponse(Status.FAILURE, "购物车删除商品信息异常！");
			}
			
			cartService.add(cart, product, num);
			return new DSResponse(Status.SUCCESS, "OK");
		} catch (Exception e) {
			logger.error("CartCotrollor异常", e);
		}
		return new DSResponse(Status.FAILURE, "购物车删除商品信息异常！");
	}

	// 活动商品添加
	@RequestMapping(value = "/addactivity", method = RequestMethod.POST)
	public @ResponseBody DSResponse addProductGroup(HttpServletRequest req, String actId,
			@RequestParam(value = "json") String json, int num) throws IOException {
		try {
			Cart cart = cartService.getCounterCart(super.getOrderCounter());
			List<Item> items = JSON.parseArray(json, Item.class);
			if(items==null||items.size()<=0){
				return new DSResponse(Status.FAILURE, "您未选择要购买的产品，请在活动界面选择具体想购买的产品以及数量！");
			}
			cartService.add(cart.getCartId(), actId, items, num);
			return new DSResponse(Status.SUCCESS, "OK");
		} catch (Exception e) {
			logger.error("CartCotrollor异常", e);
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
	public @ResponseBody DSResponse updateCart(HttpServletRequest req, Long detailId, Integer num)
			throws IOException {
		try {
			Assert.notNull(detailId);
			Assert.notNull(num);
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
			Assert.notNull(detailId);
			cartService.deleteDetailAndDesc(detailId);
			return new DSResponse(Status.SUCCESS, "OK");
		} catch (Exception e) {
			logger.error("CartCotrollor异常", e);
		}
		return new DSResponse(Status.FAILURE, "购物车删除商品信息异常！");
	}

	// 计算购物车数量 未处理
	@RequestMapping(value = "/getpresentprice", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getPresentPrice(Cart cart) throws IOException {
		try {
			// FIXME 因为这里 购物车可能不存在产品，导致获取物料费用异常
			Counter co = counterManager.getCounterById(cart.getCounterId());
			Map<String, Object> map = new LinkedHashMap<>();

			if (co != null) {
				Customer cu = customerManager.get(co.getCustomerId());
				// OrdmConfig.KEY_MAXAMOUNT = 204001:物流免费最小金额
				String freeemoney = dictManager.get(OrdmConfig.KEY_MAXAMOUNT).getName();
				// 获取折扣
				materialDiscout = getMaterialDiscout();
				map.put("discout", materialDiscout);
				map.put("materialfee", cartService.getMaterialFee(cart, cu.getCustomerTypeID()));
				// lqtys:
				map.put("lqtys", cartService.getPAndMQty(cart, productManager));
				map.put("freeemoney", freeemoney);
			}
			return map;
		} catch (Exception e) {
			logger.error("CartCotrollor异常", e);
		}
		return null;
	}

	// 购物车审核
	@RequestMapping(value = "/cartcommit", method = RequestMethod.GET)
	public @ResponseBody DSResponse cartCommit(HttpServletRequest req, ModelMap model, Cart cart)
			throws IOException {
		// 设置默认仓库,设法弄成全局的
		setAutoMyStock();
		// 设置前段StockId
		setResAttr(model);
		try {
			// 检查总金额是否满足大礼包要求
			if (!checkBigPkAmt(cart)) {
				// 不满足大礼包
				return new DSResponse(Status.FAILURE, "订单金额不满足大礼包金额要求。");
			}

			// 消除掉 ordercounter session 及锁定的session
			CartLocker cartLocker = getCartLocker(cart.getCounterId());
			super.removeCartLocker(cartLocker.getCounterId());
			super.removeOrderCounter();

			cartService.commitCart(super.getAuthUser(), cart.getCartId());

			// 发送通知短信
			UserCustomer u = userCustomerManager.getByLoginName(super.getAuthUser().getLoginName());
			if (u.getLoginName().equals(u.getCustomerLoginName())) {
				Customer boss = customerManager.get(u.getCustomerId());
				String template = "SMS_60390488";
				HashMap<String, String> ctx = new HashMap<String, String>();
				smsService.send(template, boss.getMobile(), ctx);
			}
			return new DSResponse(Status.SUCCESS, "完成");
		} catch (Exception e) {
			logger.error("CartCotrollor异常", e);
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
	public @ResponseBody List<Cart> cartManager(HttpServletRequest req, Model model)
			throws IOException {
		AuthUser authUser = super.getAuthUser();
		try {
			List<Counter> lcounters = new ArrayList<Counter>();
			Set<String> counterSet = userCustomerManager.getByLoginName(authUser.getLoginName())
					.getCounters();
			for (String code : counterSet) {
				Counter c = counterManager.getCounterById(Integer.parseInt(code));
				lcounters.add(c);
			}
			if (lcounters.isEmpty()) {
				throw new UnauthenticatedException("帐号异常，无权登录。");
			}

			List<Integer> lis = new ArrayList<>();
			for (Counter counter : lcounters) {
				lis.add(counter.getCounterId());
			}
			return cartService.getCheckCarts(lis);
		} catch (Exception e) {
			logger.error("CartCotrollor异常", e);
		}
		return null;
	}
	
	/**
	 * 购物车审核管理清单
	 * 
	 * @param req
	 *            HttpServletRequest对象
	 * @param model
	 * @return 处理完后，返回到當前页面
	 */
	@RequestMapping(value = "/mobilecart_manage", method = RequestMethod.GET)
	public String cartMobileManager(HttpServletRequest req, Model model)
			throws IOException {
		AuthUser authUser = super.getAuthUser();
		try {
			List<Counter> lcounters = new ArrayList<Counter>();
			Set<String> counterSet = userCustomerManager.getByLoginName(authUser.getLoginName())
					.getCounters();
			for (String code : counterSet) {
				Counter c = counterManager.getCounterById(Integer.parseInt(code));
				lcounters.add(c);
			}
			if (lcounters.isEmpty()) {
				throw new UnauthenticatedException("帐号异常，无权登录。");
			}

			List<Integer> lis = new ArrayList<>();
			for (Counter counter : lcounters) {
				lis.add(counter.getCounterId());
			}
			model.addAttribute("carts",cartService.getCheckCarts(lis));
		} catch (Exception e) {
			logger.error("CartCotrollor异常", e);
		}
		return "order_list";
	}
 
	// 购物车审核管理明细
	@RequestMapping(value = "/cart_managedetail", method = RequestMethod.GET)
	public String cartManagerDetail(ModelMap model, Long cartId)
			throws IOException {
		// 设置默认仓库,设法弄成全局的
		setAutoMyStock();
		// 设置前段StockId
		setResAttr(model);
		try {
			Cart cart = cartService.get(cartId);
			model.addAttribute("cart", cart);

			List<CartDetail> lcds = cartService.getCartDetailByCart(cart);
			model.addAttribute("lcds", lcds);
			if (lcds.isEmpty()) {
				model.addAttribute("lcdds", "");
			} else {
				List<CartDetailDesc> lcdds = cartService.getCartDescByCartDetail(lcds);
				Set<Product> lpsSet = new LinkedHashSet<Product>();
				for (CartDetailDesc cdd : lcdds) {
					Product p = productManager.get(cdd.getProductId());
					lpsSet.add(p);
				}
				
				final List<Product> lps = new ArrayList<>(lpsSet);
				
				model.addAttribute("lcdds", lcdds);
				model.addAttribute("lps", lps);
			}

			// 获取当前门店信息
			Counter counter = counterManager.getCounterById(cart.getCounterId());
			model.addAttribute("counter", counter);
		} catch (Exception e) {
			logger.error("CartCotrollor异常", e);
		}
		return "cart_managedetail";
	}

	/**
	 *  购物车审核拒绝
	 *  
	 * @param model
	 * @param cart
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/cartcheckrefuse", method = RequestMethod.POST)
	public @ResponseBody DSResponse cartCheckRefuse(Model model, Cart cart)
			throws IOException {
		if (cart == null) {
			return new DSResponse(Status.FAILURE, "审核处理错误");
		}
		try {
			orderService.refuse(cart.getCartId());
			return new DSResponse(Status.SUCCESS, "完成");
		} catch (Exception e) {
			logger.error("CartCotrollor异常, CartID={}",cart.getCartId(), e);
		}
		return new DSResponse(Status.FAILURE, "审核处理错误");
	}

	// 购物车审核通过
	@RequestMapping(value = "/cartcheckpass", method = RequestMethod.POST)
	public @ResponseBody DSResponse cartCheckPass(Model model, Cart cart)
			throws IOException {
		try {
			Cart carttemp = cartService.get(cart.getCartId());
			Counter counter = counterManager.getCounterById(carttemp.getCounterId());
			Customer customer = customerManager.get(counter.getCustomerId());

			// 检查总金额是否满足大礼包要求
			if (!checkBigPkAmt(cart)) {
				// 不满足大礼包
				return new DSResponse(Status.FAILURE, "订单金额不满足大礼包金额要求。");
			}
			
			orderService.buildOrder(carttemp, counter,
					authCenterManager.getAuthUser(request.getRemoteUser()), // 审核人
					authCenterManager.getAuthUser(carttemp.getUserId()),// 制单人
					customer.getCustomerTypeID());
			return new DSResponse(Status.SUCCESS, "完成");
		} catch (Exception e) {
			logger.error("CartCotrollor异常, cartId={}",cart.getCartId(), e);
		}
		return new DSResponse(Status.FAILURE, "审核处理错误");
	}

	@RequestMapping("/initcartnum")
	@ResponseBody
	public List<Cart> initNum() {
		List<Cart> carts = new ArrayList<Cart>(1);
		Counter counter = super.getOrderCounter();

		try {
			Cart cart = cartService.getCounterCart(counter);
			if (cart != null) {
				carts.add(cart);
			}
		} catch (Exception e) {
			logger.error("获取购物车失败, counter={},{}", //
					counter.getCounterId(), //
					counter.getCounterName(), e);
		}
		return carts;
	}

	/**
	 * 根据用户去取base_customer表中取得【客户类型】所执行的折扣政策
	 * 
	 * @author wangs
	 */
	private double getMaterialDiscout() {
		UserCustomer userCustomer = userCustomerManager.getByLoginName(super.getAuthUser()
				.getLoginName());
		int customerId = userCustomer.getCustomerId();
		Customer customer = customerManager.get(customerId);
		int userTypeId = customer.getCustomerTypeID();
		// 11387直营 11392运营商 11391加盟商
		CustomerPricePolicy customerPricePolicy = pricePolicyManager.getPolicy(userTypeId);
		return customerPricePolicy.getMaterialDiscount();
	}

	/**
	 * 活动类型为04的大礼包 金额判断，如果有礼包，并且满足金额条件，返回为真，
	 * @author wangs
	 * 
	 * @param cart 购物车
	 * @return 是否通过检查
	 */
	private boolean checkBigPkAmt(Cart cart) {
		// 加入大包裹校驗
		// 獲取購物車的全部
		List<CartDetail> cartDetails = cartService.getCartDetailByCart(cart);

		double bigPackAmt = 0;
		// 计算大礼包的总金额
		for (CartDetail cd : cartDetails) {
			String actId = cd.getActivityId();
			Activity activity = activityManager.get(actId);
			if (activity == null)
				continue;
			if (ActivityType.TYPE_BIGPACAKGE.equals(activity.getType())) {
				AmountRule rule = activity.getContext().getAmount();
				if (rule != null) {
					bigPackAmt += rule.getAmount() * cd.getQuantity();
				}
			}
		}
		// 如果小于1元，返回检查通过。
		if (bigPackAmt<0.01) {
			return true;
		}
		
		// 购物车内总总金额
		double totalFee = 0;
		for (CartDetail cd : cartDetails) {
			totalFee += cd.getPrice() * cd.getQuantity();
		}
		// 如果礼包中的金额<定货总价，通过检查
		return bigPackAmt < totalFee * getMaterialDiscout();
	}
	
}
