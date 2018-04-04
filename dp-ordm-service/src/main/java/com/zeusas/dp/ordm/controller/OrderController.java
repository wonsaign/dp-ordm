package com.zeusas.dp.ordm.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.common.utils.ImageUtils;
import com.zeusas.common.utils.VfsDocClient;
import com.zeusas.core.http.ActionException;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppConfig;
import com.zeusas.dp.ordm.bean.OrderBean;
import com.zeusas.dp.ordm.entity.BalanceRecord;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.DiffDetail;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.service.AliPaymentService;
import com.zeusas.dp.ordm.service.BalanceRecordService;
import com.zeusas.dp.ordm.service.BalanceService;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.DiffDetailService;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.UserCustomerManager;
import com.zeusas.dp.ordm.utils.UriUtil;
import com.zeusas.security.auth.entity.AuthUser;

/**
 * 订单的Controller
 * 
 * @author fengx
 * @date 2016年12月6日 下午3:54:33
 */

@Controller
@RequestMapping("/order")
public class OrderController extends OrdmBasicController {
	static Logger logger = LoggerFactory.getLogger(OrderController.class);

	static final int MAX_IMAGE_SIZE = 600 * 1024;

	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductManager productManager;
	@Autowired
	private DictManager dictManager;
	@Autowired
	private UserCustomerManager userCustomerManager;
	@Autowired
	private CounterManager counterManager;
	@Autowired
	private AliPaymentService aliPaymentService;
	@Autowired
	private DiffDetailService diffDetailService;
	@Autowired
	private BalanceService balanceService;
	@Autowired
	private BalanceRecordService balanceRecordService;
	@Autowired
	private CustomerManager customerManager;

	/**
	 * 显示所有的订单，按照订单状态将所有的订单放入下拉框中
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */

	@RequestMapping("/index")
	public String orderIndex(ModelMap mp) throws IOException {
		// 设置默认仓库,设法弄成全局的
		setAutoMyStock();
		// 设置前段StockId
		setResAttr(mp);
		try {
			// 获取所有状态的订单
			Dictionary dict = dictManager.get(Order.Status_HardCode);

			// 1 1 (1+1)
			// 前端 后端 前后端
			List<Dictionary> orders = dict.getChildren() //
					.stream()//
					.filter(s -> s.getStatus().intValue() == 3 || s.getStatus().intValue() == 1) // 1：前端；2：后端；3
					.sorted(Comparator.comparing(Dictionary::getSeqid))//
					.collect(Collectors.toList());
			// 判断角色设置不同的Index页 adm：待付款 其他待定。

			mp.addAttribute("orders", orders);
			// 获取 待审核订单、待付款订单、待收货订单、财务退回 的数量
			// FIX ME:
			super.setAllOrderSize();
			mp.addAttribute("all_order_size", super.getAllOrderSize());

		} catch (Exception e) {
			logger.error("订单显示错误", e);
		}
		return "order";
	}

	/**
	 * 订单查询
	 * 
	 * @param typeId
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/search")
	public @ResponseBody DSResponse orderSearch(@RequestParam(value = "typeId") String typeId) throws IOException {
		// 获取所有状态的订单
		AuthUser authUser = super.getAuthUser();
		try {
			// FIXME: 传客户LoginName
			UserCustomer userCustomer = userCustomerManager.getByLoginName(authUser.getLoginName());
			List<Order> orders;

			Set<String> counterIdSet = userCustomer.getCounters();
			List<Integer> counterIds = new ArrayList<>(counterIdSet.size());
			for (String counterId : counterIdSet) {
				counterIds.add(Integer.parseInt(counterId));
			}

			orders = orderService.getOrders(counterIds, typeId);

			// 状态"3"，"5"都显示到已付款(已付款状态为'3')
			if (typeId.equals(Order.Status_DoPay)) {
				List<Order> orderFive = orderService.getOrders(counterIds, Order.status_LogisticsDelivery);
				orders.addAll(orderFive);
			}
			// 状态"6": 显示到待收货(待收货的状态为'8')
			if (typeId.equals("8")) {
				List<Order> ordersEight = orderService.getOrders(counterIds, Order.status_DoLogisticsDelivery);
				orders.addAll(ordersEight);
			}
			// 订单根据id倒叙
			Assert.notNull(orders);
			// 比较算子
			Comparator<Order> cmp = (a, b) -> (int) (b.getId() - a.getId());
			Collections.sort(orders, cmp);

			return new DSResponse(orders);
		} catch (Exception e) {
			logger.error("订单显示错误,typeId{}", typeId, e);
		}

		return new DSResponse(-1, "购物车删除商品信息异常！");
	}

	/**
	 * 订单查询
	 * 
	 * @param typeId
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/mobilesearch")
	public String mobileSearch(@RequestParam(value = "typeId") String typeId, Model mp) throws IOException {
		// 获取所有状态的订单
		try {
			List<Order> orders;

			List<Integer> counterIds = super.getAllCounterIds();

			orders = orderService.getOrders(counterIds, typeId);

			// 状态"3"，"5"都显示到已付款(已付款状态为'3')
			if (typeId.equals(Order.Status_DoPay)) {
				List<Order> orderFive = orderService.getOrders(counterIds, Order.status_LogisticsDelivery);
				orders.addAll(orderFive);
			}
			// 状态"6": 显示到待收货(待收货的状态为'8')
			if (typeId.equals("8")) {
				List<Order> ordersEight = orderService.getOrders(counterIds, Order.status_DoLogisticsDelivery);
				orders.addAll(ordersEight);
			}
			// 订单根据id倒叙
			Assert.notNull(orders);
			// 比较算子
			Comparator<Order> cmp = (a, b) -> (int) (b.getId() - a.getId());
			Collections.sort(orders, cmp);
			mp.addAttribute("orders", orders);
			return "order_list";
		} catch (Exception e) {
			logger.error("订单显示错误,typeId{}", typeId, e);
		}

		return null;
	}

	/**
	 * 订单详细。
	 * 
	 * @param mp
	 * @param orderId
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/detail")
	public String orderDetail(ModelMap mp, @RequestParam(value = "orderId", required = false) Long orderId,
			HttpServletRequest request) throws IOException {
		// 设置默认仓库,设法弄成全局的
		setAutoMyStock();
		// 设置前段StockId
		setResAttr(mp);
		Order order = orderService.get(orderId);

		Assert.notNull(order);
		String orderNo = order.getOrderNo();
		Map<Long, List<OrderBean>> orderBeanMap = new HashMap<>();
		try {
			// detail表进行Map处理
			List<OrderBean> orderBeans = new ArrayList<OrderBean>();
			// 根据单号获取订单详情
			List<OrderDetail> orderDetails = orderService.getOrderDetails(orderNo);

			for (OrderDetail orderDetail : orderDetails) {
				// product 和 order_detail 表关联
				OrderBean orderBean = new OrderBean(productManager.get(orderDetail.getProductId()), orderDetail);
				orderBeans.add(orderBean);
			}

			for (OrderBean ob : orderBeans) {
				Long typeId = Long.valueOf(ob.getOrderDetail().getTypeId());
				// 根据类型ID判断，一组Bean放在一起，typeId相同为同一订单
				List<OrderBean> pGroup = orderBeanMap.get(typeId);
				if (pGroup == null) {
					pGroup = new ArrayList<OrderBean>();
					orderBeanMap.put(typeId, pGroup);
				}
				pGroup.add(ob);
			}
			// 未付款则使用余额
			if (order.getOrderStatus().equals(Order.Status_UnPay)) {
				double usefulBalance = getUseflBalance(request);
				mp.addAttribute("usefulBalance", usefulBalance);
			}
			// 金蝶差分
			List<DiffDetail> diffDetails = diffDetailService.getDiffDetails(orderNo);
			mp.addAttribute("diffDetails", diffDetails);

			mp.addAttribute("OrderDetails", orderBeanMap);
			// 订单总金额的信息 以及关联字典表的类型名字 订单头和字典表关联
			// FIXME
			OrderBean ob = new OrderBean(order,
					dictManager.lookUpByCode(Order.Status_HardCode,
							// FIXME
							order.getOrderStatus()));
			mp.addAttribute("MyOrder", ob);
			mp.addAttribute("order", order);
			mp.addAttribute("counter", counterManager.getCounterById(order.getCounterId()));
		} catch (Exception e) {
			logger.error("订单显示错误", e);
		}
		return "order_detail";
	}

	/**
	 * 上传图片 付款成功后上传截图，交付财务待审核
	 * 
	 * @param file
	 * @param request
	 * @param product
	 * @return
	 */
	@RequestMapping(value = "/uploadimage", method = RequestMethod.POST)
	public String upload(Model model, @ModelAttribute("order") Order order,
			@RequestParam(value = "files[]", required = false) MultipartFile[] files) throws IOException {
		Order updateOrder = null;
		HttpSession sess = request.getSession();
		String sessId = "ORDER:" + order.getId();
		ImageUploadInfo uploadInfo = (ImageUploadInfo) sess.getAttribute(sessId);
		try {
			if (uploadInfo == null //
					|| (System.currentTimeMillis() - uploadInfo.lastUpdate > 60 * 1000)) {
				Order dbOrder = orderService.get(order.getId());
				logger.debug("Create session id={}", sessId);
				uploadInfo = new ImageUploadInfo(dbOrder.getImageURL());
				sess.setAttribute(sessId, uploadInfo);
			}

			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				if (file == null || file.getBytes().length == 0) {
					continue;
				}

				String fname = file.getOriginalFilename();
				final String ext = fname.substring(fname.lastIndexOf('.')).toLowerCase();
				// png jpg jpeg tif gif
				if (".png.jpg.jpeg.tif.gif".indexOf(ext) < 0) {
					throw new ActionException("图片格式不正确：" + fname);
				}
				final String uri = UriUtil.getURI(UriUtil.FMT_Order) + order.getOrderNo() + "_" + uploadInfo.size();
				logger.debug("XXX: Upload file: {}, size:{}", uri, file.getBytes().length);

				byte imgBytes[] = file.getBytes();
				String url;
				if (imgBytes.length < MAX_IMAGE_SIZE) {
					url = AppConfig.getPutVfsPrefix() + uri + ext;
					uploadInfo.images.add(uri + ext);
					VfsDocClient.put(url, imgBytes);
				} else {
					url = AppConfig.getPutVfsPrefix() + uri + ".jpg";
					uploadInfo.images.add(uri + ".jpg");
					byte cbb[] = compressImage(imgBytes, 1024, 1024);
					VfsDocClient.put(url, cbb);
				}
			}
			order.setImageURL(uploadInfo.images);
			updateOrder = orderService.payOrder(order, super.getAuthUser());
			model.addAttribute(DSResponse.DS_MESSAGE, "success");
		} catch (ActionException e) {
			model.addAttribute(DSResponse.DS_MESSAGE, "error");
			model.addAttribute("order", order);
			logger.error("上传截图失败，OrderID={}", order.getOrderNo(), e);
		} catch (Exception e) {
			model.addAttribute(DSResponse.DS_MESSAGE, "error");
			model.addAttribute("order", order);
			logger.error("上传截图失败，OrderID={}", order.getOrderNo(), e);
		}
		Assert.notNull(updateOrder);
		OrderBean ob = new OrderBean(updateOrder,
				dictManager.lookUpByCode(Order.Status_HardCode, updateOrder.getOrderStatus()));
		model.addAttribute("MyOrder", ob);

		model.addAttribute("order", updateOrder);
		return "order_detail";
	}

	private byte[] compressImage(byte b[], int wigth, int height) throws ActionException {
		ByteArrayInputStream bb = new ByteArrayInputStream(b);
		try {
			BufferedImage bimg = ImageIO.read(bb);
			BufferedImage cmpressed = ImageUtils.resizeImage(bimg, ImageUtils.IMAGE_JPEG, 1024, 1024);
			ByteArrayOutputStream output = new ByteArrayOutputStream(32 * 1024);
			ImageIO.write(cmpressed, "jpg", output);
			bb = null;
			output.flush();
			return output.toByteArray();
		} catch (IOException e) {
			throw new ActionException("压缩图片失败.");
		}
	}

	@RequestMapping(value = "/uploaddata", method = RequestMethod.POST)
	public String upload(Model model, @ModelAttribute("order") Order order) throws IOException {
		Order updateOrder = null;
		try {
			updateOrder = orderService.payOrder(order, super.getAuthUser());
			model.addAttribute("message", "success");
		} catch (Exception e) {
			model.addAttribute("message", "error");
			model.addAttribute("order", order);
			logger.error("存储实付金额失败", e);
		}
		Assert.notNull(updateOrder);
		Assert.notNull(order.getPaymentPrice());
		OrderBean ob = new OrderBean(updateOrder,
				dictManager.lookUpByCode(Order.Status_HardCode, updateOrder.getOrderStatus()));
		model.addAttribute("MyOrder", ob);

		model.addAttribute("order", updateOrder);
		return "order_detail";
	}

	/**
	 * 订单确认页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public String confirmOrder(@ModelAttribute("order") Order order) {
		Long orderId = order.getId();
		try {
			// FIXME:对数据库操作过多
			recalculateBalance(orderId);
			// 记录流水
			Order bdOrder2 = orderService.get(orderId);
			saveBalanceRecord(bdOrder2, BalanceRecord.S_INITIAL, request);
			orderService.confirmPay(order);
			saveBalanceRecord(bdOrder2, BalanceRecord.S_PAYMENT, request);
		} catch (Exception e) {
			logger.error("订单确定失败, OrderId={}", orderId, e);
		}
		return "forward:/order/index.do";
	}

	/**
	 * 
	 * @param order
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/confirmdirect", method = RequestMethod.POST)
	public String confirmDirect(Order order, Model model) {
		String orderNo = order.getOrderNo();
		try {
			// FIXME:对数据库操作过多
			recalculateBalance(order.getId());

			Order bdOrder = orderService.getsingleOrder(orderNo);
			Counter counter = counterManager.get(bdOrder.getCounterId());
			Assert.notNull(counter);

			saveBalanceRecord(bdOrder, BalanceRecord.S_INITIAL, request);
			// orderService.payByDirect(order, super.getAuthUser());
			orderService.payByBlance(order, getAuthUser(), counter.getType());
			bdOrder = orderService.getsingleOrder(orderNo);
			saveBalanceRecord(bdOrder, BalanceRecord.S_PAYMENT, request);
		} catch (Exception e) {
			logger.error("直营订单提交失败, OrderNo={}", orderNo, e);
		}
		return "forward:/order/index.do";
	}

	/**
	 * 订单确认页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public String cancelOrder(@ModelAttribute("order") Order order, Model model) {
		try {
			Order order2 = orderService.cancelPay(order);
			model.addAttribute("order", order2);
		} catch (Exception e) {
			logger.error("订单取消支付失败, orderNo={}", order.getOrderNo(), e);
		}
		return "forward:/order/index.do";
	}

	/**
	 * 确认收货
	 * 
	 * @return
	 */
	@RequestMapping(value = "/finishorder", method = RequestMethod.POST)
	public String comfirmGoods(@ModelAttribute("order") Order order, Model model) {
		try {
			orderService.changeOrderStatus(order.getId(), Order.status_CompleteShipping);
		} catch (Exception e) {
			logger.error("订单取消支付失败, orderId={}", order.getId(), e);
		}
		return "forward:/order/index.do";
	}

	// FIXME: fail
	@RequestMapping(value = "/cancelfull", method = RequestMethod.POST)
	public String cancelOrderFull(@ModelAttribute("order") Order order, Model model) {
		try {
			orderService.cancelOrder(order);
		} catch (Exception e) {
			logger.error("订单取消失败, orderId={}", order.getId(), e);
		}
		return "forward:/order/index.do";
	}

	class ImageUploadInfo {
		final List<String> images;
		final long lastUpdate;

		ImageUploadInfo(List<String> imgs) {
			images = imgs == null ? new ArrayList<>() : imgs;
			lastUpdate = System.currentTimeMillis();
		}

		int size() {
			return images.size();
		}
	}

	/**
	 * ajax 更新订单用户名字
	 */
	@RequestMapping(value = "/UOUN", method = RequestMethod.GET)
	public @ResponseBody String updateOrderUserName(String orderID) {
		// FIXME:实现更新订单用户
		boolean upSuccess = aliPaymentService.updateOUN(orderID, super.getAuthUser());
		if (upSuccess) {
			return "success";
		}
		return "fail";
	}

	/**
	 * 使用余额 释放余额 勾选动作
	 * 
	 * @param mp
	 * @param orderId
	 * @param flag
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/useBalance")
	public String useBalance(ModelMap mp, @RequestParam(value = "orderId", required = false) Long orderId,
			@RequestParam(value = "flag", required = false) Boolean flag, HttpServletRequest request) {
		try {
			double usefulBalance = getUseflBalance(request);
			orderService.userBalance(orderId, usefulBalance, flag);
			return orderDetail(mp, orderId, request);
		} catch (Exception e) {
			logger.error("订单取消失败, orderId={}", orderId, e);
		}
		return "order_detail";
	}

	/**
	 * 订单回退到购物车
	 * 
	 * @author wangs
	 * 
	 */
	@RequestMapping(value = "/rollBack", method = RequestMethod.POST)
	public String backToCart(@RequestParam(value = "orderId", required = false) Long orderId) {

		try {
			Order order = orderService.get(orderId);
			String status = order.getOrderStatus();
			// 只有订单状态为2：待付款的订单才可以退回。
			if (!Order.Status_UnPay.equals(status) && !Order.status_ForFinancialRefuse.equals(status)) {
				return "redirect:/ordm/index.do";
			}
			// 退回到购物车
			orderService.buildOrderToCart(order);
		} catch (ServiceException | ActionException e) {
			logger.error("订单退回异常", e);
		} catch (Exception e) {
			logger.error("订单退回异常", e);
		}
		return "redirect:/order/index.do";
	}

	/**
	 * 获取可用余额 K3余额-（未支付 已使用余额+已支付 已使用余额）-未推送单子总金额
	 * 
	 * @param request
	 * @return
	 */
	private double getUseflBalance(HttpServletRequest request) {
		AuthUser authUser = this.getAuthUser();
		UserCustomer uc = userCustomerManager.getByLoginName(authUser.getLoginName());
		return balanceService.getUsefulBalance(uc.getCustomerId());
	}

	/**
	 * 记录流水
	 * 
	 * @param order
	 * @param status
	 *            :BalanceRecord.S_INITIAL S_PAYMENT
	 * @param request
	 * @return
	 */
	private double saveBalanceRecord(Order order, int status, HttpServletRequest request) {
		AuthUser authUser = this.getAuthUser();
		UserCustomer uc = userCustomerManager.getByLoginName(authUser.getLoginName());
		Customer customer = customerManager.get(uc.getCustomerId());

		double balance = balanceService.getUsefulBalance(customer.getCustomerID());

		// 实际支付
		double realpaymentfee = 0;
		if (Order.PayType_aliPay.equals(order.getPayTypeId())) {
			realpaymentfee = order.getPayable();
		} else if (Order.PayType_directOrder.equals(order.getPayTypeId()) //
				|| Order.PayType_offlinePay.equals(order.getPayTypeId())) {
			realpaymentfee = order.getPaymentPrice();
		}

		balance = (status == BalanceRecord.S_INITIAL) ? balance + order.getUseBalance() : balance;

		BalanceRecord record = new BalanceRecord(order.getOrderNo(), //
				order.getPaymentFee(), //
				realpaymentfee, balance, order.getUseBalance(), //
				status);

		balanceRecordService.save(record, customer, authUser);

		return balanceService.getUsefulBalance(uc.getCustomerId());
	}

	/**
	 * 支付前重新计算余额 并修改单头
	 * 
	 * @param orderId
	 */
	private void recalculateBalance(Long orderId) {
		Order bdOrder = orderService.get(orderId);
		// 此订单余额
		double myBalance = bdOrder.getUseBalance();
		// 如果使用余额 确认支付时再计算一次可使用余额
		if (myBalance != 0) {
			// 可用余额中把本订单已经使用余额除去
			double usefulBalance = getUseflBalance(request);
			usefulBalance += myBalance;
			// 重新使用余额
			orderService.userBalance(bdOrder.getId(), usefulBalance, true);
		}
	}

}
