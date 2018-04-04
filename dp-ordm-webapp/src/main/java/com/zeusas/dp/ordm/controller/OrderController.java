package com.zeusas.dp.ordm.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.client.utils.URIBuilder;
import org.apache.shiro.authz.UnauthenticatedException;
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

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonParseException;
import com.google.common.base.Strings;
import com.zeusas.common.data.TypeConverter;
import com.zeusas.common.entity.Dictionary;
import com.zeusas.common.service.DictManager;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.common.utils.ImageUtils;
import com.zeusas.common.utils.Status;
import com.zeusas.common.utils.VfsDocClient;
import com.zeusas.core.http.ActionException;
import com.zeusas.core.http.QHttpClients;
import com.zeusas.core.service.ServiceException;
import com.zeusas.core.utils.AppConfig;
import com.zeusas.core.utils.AppContext;
import com.zeusas.core.utils.DateTime;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.active.bean.ActivityType;
import com.zeusas.dp.ordm.active.model.Activity;
import com.zeusas.dp.ordm.active.model.ProductItem;
import com.zeusas.dp.ordm.active.model.ProductRule;
import com.zeusas.dp.ordm.active.service.ActivityManager;
import com.zeusas.dp.ordm.bean.CombineBean;
import com.zeusas.dp.ordm.bean.OrderBean;
import com.zeusas.dp.ordm.bean.ProductBean;
import com.zeusas.dp.ordm.bean.logistics.InterResponse;
import com.zeusas.dp.ordm.bean.logistics.LogisticData;
import com.zeusas.dp.ordm.bean.logistics.LogisticDetail;
import com.zeusas.dp.ordm.bean.logistics.OrderCodeBean;
import com.zeusas.dp.ordm.entity.BalanceRecord;
import com.zeusas.dp.ordm.entity.Counter;
import com.zeusas.dp.ordm.entity.Customer;
import com.zeusas.dp.ordm.entity.DiffDetail;
import com.zeusas.dp.ordm.entity.IPackage;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderCredentials;
import com.zeusas.dp.ordm.entity.OrderDetail;
import com.zeusas.dp.ordm.entity.ReserveProduct;
import com.zeusas.dp.ordm.entity.ReserveRecord;
import com.zeusas.dp.ordm.entity.UserCustomer;
import com.zeusas.dp.ordm.entity.Warehouse;
import com.zeusas.dp.ordm.rev.entity.ReservedActivity;
import com.zeusas.dp.ordm.rev.service.ReservedActivityManager;
import com.zeusas.dp.ordm.service.ActivityLimitManager;
import com.zeusas.dp.ordm.service.AliPaymentService;
import com.zeusas.dp.ordm.service.BalanceRecordService;
import com.zeusas.dp.ordm.service.BalanceService;
import com.zeusas.dp.ordm.service.CounterManager;
import com.zeusas.dp.ordm.service.CustomerManager;
import com.zeusas.dp.ordm.service.DiffDetailService;
import com.zeusas.dp.ordm.service.OrderCredentialsService;
import com.zeusas.dp.ordm.service.OrderDetailService;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.PackageService;
import com.zeusas.dp.ordm.service.ProductManager;
import com.zeusas.dp.ordm.service.ReserveProductManager;
import com.zeusas.dp.ordm.service.ReserveRecordService;
import com.zeusas.dp.ordm.service.StockService;
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

	static final String ROLE_ORDER_MGR = "21";// 21代表了拥有可以查看订单的权限
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
	@Autowired
	private OrderCredentialsService credentialsService;
	@Autowired
	private ActivityManager activityManager;
	@Autowired
	private ReserveRecordService reserveRecordService;
	@Autowired
	private ReserveProductManager reserveProductManager;
	@Autowired
	private ActivityLimitManager activityLimitManager;
	@Autowired
	private ReservedActivityManager reservedActivityManager;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private PackageService packageService;
	
	// 合并订单image缓存
	private Map<String, List<String>> combineImageCache = new HashMap<String, List<String>>();

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
	 * @throws Exception 
	 */
	@RequestMapping("/mobilesearch")
	public String mobileSearch(@RequestParam(value = "typeId") String typeId, Model mp) throws IOException {
		// 获取所有状态的订单
		try {
			List<Order> orders;

			List<Integer> counterIds = super.getAllCounterIds();
			if (StringUtil.isEmpty(counterIds)) {
				throw new UnauthenticatedException("帐号异常 没有柜台，无权登录。");
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
			Assert.notNull(orders, "订单集合为空");
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
	 * FIXME: 以下代码需要大修改！！
	 * 
	 * @param typeId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/search")
	public @ResponseBody DSResponse orderSearch(@RequestParam(value = "typeId") String typeId) throws IOException {
		// 获取所有状态的订单
		List<Order> orders;
		try {
			List<Integer> counterIds = super.getAllCounterIds();
			if (StringUtil.isEmpty(counterIds)) {
				throw new UnauthenticatedException("帐号异常 没有柜台，无权登录。");
			}

			orders = orderService.getOrders(counterIds, typeId);

			// 状态"3"，"5"都显示到已付款(已付款状态为'3')
			if (typeId.equals(Order.Status_DoPay)) {
				List<Order> orderFive = orderService.getOrders(counterIds,
						Order.status_LogisticsDelivery);
				orders.addAll(orderFive);
			}
			// 状态"6": 显示到待收货(待收货的状态为'8')
			if ("8".equals(typeId)) {
				List<Order> ordersEight = orderService.getOrders(counterIds,
						Order.status_DoLogisticsDelivery);
				orders.addAll(ordersEight);
			}
			// 订单根据id倒叙
			Assert.notNull(orders, "订单集合为空");
			// 比较算子
			Comparator<Order> cmp = (a, b) -> (int) (b.getId() - a.getId());
			Collections.sort(orders, cmp);
			return new DSResponse(orders);
		} catch (UnauthenticatedException e) {
			logger.warn("登录超时,重新登录.");
		} catch (Exception e) {
			logger.error("订单显示错误,typeId{}", typeId, e);
		}
		return new DSResponse(Status.FAILURE,"订单显示失败.");
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
		Map<Long, List<OrderBean>> orderBeanMap = new HashMap<>();
		try {
			Order order = orderService.get(orderId);

			Assert.notNull(order, "订单为空");
			List<Counter> counters = super.getAllCounters();
			Counter orderCounter = counterManager.get(order.getCounterId());
			if (!counters.contains(orderCounter) //
					&& !super.getUserRoles().contains(ROLE_ORDER_MGR)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			String orderNo = order.getOrderNo();
			// detail表进行Map处理
			List<OrderBean> orderBeans = new ArrayList<OrderBean>();
			// 根据单号获取订单详情
			List<OrderDetail> orderDetails = orderService.getOrderDetails(orderNo);
			
			// 获取已经完成的收货记录
			List<ReserveRecord> reserveRecords = reserveRecordService.getRecordByOrderNo(orderNo);
			// 如果为空改为null
			if (reserveRecords.isEmpty()) {
				for (OrderDetail orderDetail : orderDetails) {
					// product 和 order_detail 表关联
					OrderBean orderBean = new OrderBean(productManager.get(orderDetail.getProductId()), //
							orderDetail, //
							"null"//
					);
					orderBeans.add(orderBean);
				}
				// 只要不为NUll,则都是预售产品
			} else {
				// 添加 订单明细 执行单号
				Map<Long, String> reserveRecordStatus = new HashMap<Long, String>();
				for (ReserveRecord reserveRecord : reserveRecords) {
					reserveRecordStatus.put(reserveRecord.getOrderDetailId(), //
							reserveRecord.getExcuteOrderNo());
				}
				for (OrderDetail orderDetail : orderDetails) {
					// product 和 order_detail 表关联
					OrderBean orderBean = new OrderBean(productManager.get(orderDetail.getProductId()), //
							orderDetail, //
							reserveRecordStatus.get(orderDetail.getId()));
					orderBeans.add(orderBean);
				}
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
				boolean canUseBalance = true ;
				// 包含打欠产品
//				for (OrderDetail orderDetail : orderDetails) {
//					if(!OrderDetail.type_buy.equals(orderDetail.getDetailType())){
//						canUseBalance = false;
//						break;
//					}
//				}
				double usefulBalance = getUseflBalance(request);
				if(canUseBalance) {
					mp.addAttribute("usefulBalance", usefulBalance);
				}
			}
			
			// 查询物流信息
			if (order.getOrderStatus().equals(Order.status_WaitShip)) {
				try {
					List<String> logistics = new ArrayList<String>();
					List<IPackage> packages = packageService.getPackageByNo(orderNo);
					String req = null;
					int size = packages.size();
					if (size == 1) {
						req = packages.get(0).getBillNo() == null ? "BXX20" + orderId : packages.get(0).getBillNo();
						String result = post(req);
						List<String> logs = analyzeLogistic(result);
						if (!logs.isEmpty()) {
							logistics.add(req + ":\n");
							logistics.addAll(logs);
						}
					} else if (packages.size() > 1) {
						for (int i = 0; i < size; i++) {
							req = packages.get(i).getBillNo() != null//
									? packages.get(i).getBillNo()//
									: (i & 1) == 1//
											? "BXX20" + orderId//
											: "BDD20" + orderId;
							String result = post(req);
							List<String> logs = analyzeLogistic(result);
							if (!logs.isEmpty()) {
								logistics.add(req + ":\n");
								logistics.addAll(logs);
							}
						}
					}
					mp.addAttribute("logistics", logistics);
				} catch (JsonParseException e) {
					logger.error("物流显示错误", e);
				}
			}

			// 金蝶差分（XXX:如果是3.0如何处理？）
			List<DiffDetail> diffDetails = diffDetailService.getDiffDetails(orderNo);
			mp.addAttribute("diffDetails", diffDetails);
			mp.addAttribute("OrderDetails", orderBeanMap);

			// 订单总金额的信息 以及关联字典表的类型名字 订单头和字典表关联
			OrderBean ob = new OrderBean(order,
					dictManager.lookUpByCode(Order.Status_HardCode, order.getOrderStatus()));
			mp.addAttribute("MyOrder", ob);
			mp.addAttribute("order", order);
			mp.addAttribute("counter", counterManager.getCounterById(order.getCounterId()));

		} catch (Exception e) {
			logger.error("订单显示错误", e);
		}
		return "order_detail";
	}
	
	/**
	 * FIXME: 如果定制解析，或容错处理，需要待处理的串、流的样本贴上来！！
	 * 
	 * @param result
	 * @return
	 * @throws JsonParseException
	 */
	private List<String> analyzeLogistic(String result) throws JsonParseException {
		List<String> logistics = new ArrayList<String>();
		InterResponse ir = JSON.parseObject(result, InterResponse.class);
		String data = ir.getData();
		if (Strings.isNullOrEmpty(data)) {
			return logistics;
		}
		// Json数据格式有问题，手动解析: "[]"
		data = data.substring(1, data.length() - 1);
		LogisticData da = JSON.parseObject(data, LogisticData.class);
		Collections.sort(da.getDetail(), new Comparator<LogisticDetail>() {
			@Override
			public int compare(LogisticDetail arg0, LogisticDetail arg1) {
				return arg0.getScheduleDate().compareTo(arg1.getScheduleDate());
			}
		});
		// FIXME: 需要给出输出样本！！
		for (LogisticDetail de : da.getDetail())
			logistics.add("[" + de.getScheduleDate() + "] " + de.getNodeDesc());
		return logistics;
	}

	/**
	 * 请求物流接口获取信息
	 * 
	 * @param orderCodes
	 * @return
	 * @throws ActionException
	 */
	private String post(String orderCodes) throws ActionException {
		final ResourceBundle config = ResourceBundle.getBundle("config");
		QHttpClients client = new QHttpClients();
		final String URL = config.getString("LOGISTIC_URL");
		String contentType = config.getString("LOGISTIC_CONTENTTYPE");
		OrderCodeBean ocb = new OrderCodeBean();
		try {
			URI accessURI = new URIBuilder(URL).build();
			ocb.setOrderCodes(Arrays.asList(orderCodes));
			byte[] bytes = client.post(accessURI, contentType, ocb.toJSON().getBytes());
			return new String(bytes, "UTF-8");
		} catch (Exception e) {
			logger.error("URL:{},  orderNo:{}", URL, orderCodes);
			throw new ActionException(e);
		}
	}

	@RequestMapping(value = "/orderDelete", method = RequestMethod.POST)
	public @ResponseBody DSResponse orderDelete(@RequestParam(value = "orderNo") Long orderNo) {
		try {
			Order o = orderService.get(orderNo);
			// 将订单状态置为无效
			if (o.getOrderStatus().equals(Order.Status_Invalid)) {
				orderService.changeOrderStatus(o.getId(), Order.status_NotDisplay);
				return DSResponse.SUCCESS;
			}
		} catch (ServiceException e) {
			logger.error("订单状态修改失败", e);
		}
		return new DSResponse(-1, "无效订单删除异常！");
	}

	/**
	 * 上传图片 付款成功后上传截图，交付财务待审核 分两步，第一步 ：设置图片
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
			// 这里并没有实际支付金额 这个order只有id和orderNo属性
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
		Assert.notNull(updateOrder, "更新订单未空");
		OrderBean ob = new OrderBean(updateOrder,
				dictManager.lookUpByCode(Order.Status_HardCode, updateOrder.getOrderStatus()));
		model.addAttribute("MyOrder", ob);

		model.addAttribute("order", updateOrder);
		return "order_detail";
	}

	// 继续上面的方法，第二步，设置数据
	@RequestMapping(value = "/uploaddata", method = RequestMethod.POST)
	public String upload(Model model, @ModelAttribute("order") Order order) throws IOException {
		Order updateOrder = null;
		try {
			// 这个order里有属性id paymentPrice description
			updateOrder = orderService.payOrder(order, super.getAuthUser());
			model.addAttribute("message", "success");
		} catch (Exception e) {
			model.addAttribute("message", "error");
			model.addAttribute("order", order);
			logger.error("存储实付金额失败", e);
		}
		Assert.notNull(updateOrder, "更新订单未空");
		Assert.notNull(order.getPaymentPrice(), "未付款");
		OrderBean ob = new OrderBean(updateOrder,
				dictManager.lookUpByCode(Order.Status_HardCode, updateOrder.getOrderStatus()));
		model.addAttribute("MyOrder", ob);

		model.addAttribute("order", updateOrder);
		return "order_detail";
	}

	/**
	 * 合并订单显示
	 * 
	 * @param orderNo
	 * @param mp
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/combinePage", method = RequestMethod.GET)
	public String combineOrderPage(ModelMap mp) throws IOException {
		List<Order> orders = null;
		try {
			List<Integer> counterIds = super.getAllCounterIds();
			if (StringUtil.isEmpty(counterIds)) {
				throw new UnauthenticatedException("帐号异常 没有柜台，无权登录。");
			}
			// 过滤合并订单
			orders = orderService.getOrders(counterIds, Order.Status_UnPay).stream().filter(o -> o.isMerger() == false)
					.collect(Collectors.toList());
			mp.addAttribute("orders", orders);
		} catch (UnauthenticatedException e) {
			logger.warn("登录超时,重新登录");
		} catch (Exception e) {
			logger.error("获取合并订单异常", e);
		}
		return "pay_detail";
	}

	/**
	 * 根据任意合并订单单号 获取 合并订单里所有的订单
	 * 
	 * @param OrderId
	 * @param mp
	 * @author wangs
	 * @return
	 */
	@RequestMapping(value = "/getCombineOrder", method = RequestMethod.GET)
	public String getCombineOrderById(@RequestParam(value = "orderId") String OrderId, ModelMap mp) {
		List<Order> orders;
		double productAmt = 0.0;
		double discountAmt = 0.0;
		// 额外物料费用
		double extraMAmt = 0.0;
		double extraFree = 0.0;
		double usefulBalance;
		double creToUseBalance;
		Double[] data = new Double[8];
		try {
			Order order = orderService.get(Long.parseLong(OrderId));
			Assert.notNull(order, "空订单");
			// 获取合并订单
			OrderCredentials ocb = credentialsService.getCombineOrderCredentials(order.getOrderNo());
			List<String> orderNos = ocb.getOrderNos();
			creToUseBalance = ocb.getUseBlance() == null ? 0.0 : ocb.getUseBlance();
			orders = new ArrayList<>();
			for (String orderNo : orderNos) {
				Order combineOrder=orderService.getsingleOrder(orderNo);
				if(Order.Status_UnPay.equals(combineOrder.getOrderStatus())//
						||Order.status_ForFinancialRefuse.equals(combineOrder.getOrderStatus())){
					orders.add(combineOrder);
				}
			}
			for (Order o : orders) {
				productAmt += o.getOrderOriginalFee();
				discountAmt += o.getPaymentFee();
				extraMAmt += o.getMaterialFee();
				extraFree += o.getExpressFee();
			}
			usefulBalance = getUseflBalance(request);
			data[0] = productAmt;
			data[1] = discountAmt - creToUseBalance;
			data[2] = extraMAmt;
			data[3] = extraFree;
			data[4] = usefulBalance;
			data[5] = creToUseBalance;
			// 凭证主键
			mp.addAttribute("ocid", ocb.getOcid());
			mp.addAttribute("data", data);
			mp.addAttribute("orders", orders);
		} catch (Exception e) {
			logger.error("获取合并订单异常", e);
		}
		return "pay_detail";
	}
	
	/**
	 * 获得合并订单总的礼盒信息
	 * @author wangbin
	 * @param os
	 * @return
	 */
	@RequestMapping(value = "/getMessage", method = RequestMethod.POST)
	public @ResponseBody DSResponse combineOrdersMessage(@RequestParam(value = "os") String os, ModelMap mp) {
		List<CombineBean> cb = JSON.parseArray(os, CombineBean.class);
		try {
			List<Long> oids = new ArrayList<>();

			for (CombineBean combineBean : cb) {
				Long id = StringUtil.toLong(combineBean.getId());
				if (id != null) {
					oids.add(id);
				}
			}
			StringBuffer message = new StringBuffer("");
			List<Order> orders = orderService.find(oids.toArray(new Long[0]));
			Map<Activity, Integer> otherPackage = orderService.getAutoBigPackage(orders);
			if(otherPackage.size() > 0) {
				for (Activity activity : otherPackage.keySet()) {
					int suitNum = getSuitNum(activity.getContext().getActityExtra(),otherPackage.get(activity),orders.get(0));
					if(suitNum>0){
					// 提示添加了活动类型 
					message.append("您已经满足活动:")//
						.append(activity.getName())//
						.append(" : ")//
						.append(otherPackage.get(activity))//
						.append("套.");//
				}
				}
				if(message.length()==0){
					message.append("礼包无库存");
				}
			}
			return new DSResponse(Status.SUCCESS,message.toString());
		} catch (Exception e) {
			logger.error("确认合并订单错误。", e);
		}
		return new DSResponse(Status.FAILURE, "合并订单失败。");
	}
	
	/**
	 * 获得合并订单产生的额外礼盒信息
	 * @author wangbin
	 * @param os
	 * @return
	 */
	@RequestMapping(value = "/getOtherMessage", method = RequestMethod.POST)
	public @ResponseBody DSResponse combineOtherMessage(@RequestParam(value = "os") String os, ModelMap mp) {
		List<CombineBean> cb = JSON.parseArray(os, CombineBean.class);
		try {
			List<Long> oids = new ArrayList<>();

			for (CombineBean combineBean : cb) {
				Long id = StringUtil.toLong(combineBean.getId());
				if (id != null) {
					oids.add(id);
				}
			}
			StringBuffer message = new StringBuffer("");
			List<Order> orders = orderService.find(oids.toArray(new Long[0]));
			Map<Activity, Integer> otherPackage = orderService.getMergeOtherBigPackage(orders);
			if(otherPackage.size() > 0) {
				for (Activity activity : otherPackage.keySet()) {
					int suitNum = getSuitNum(activity.getContext().getActityExtra(),otherPackage.get(activity),orders.get(0));
					if(suitNum>0){
					// 提示添加了活动类型 
					message.append("您已经满足活动:")//
						.append(activity.getName())//
						.append(" : ")//
						.append(otherPackage.get(activity))//
						.append("套.");//
				}
				}
				if(message.length()==0){
					message.append("额外礼包无库存");
				}
			}
			return new DSResponse(Status.SUCCESS,message.toString());
		} catch (Exception e) {
			logger.error("确认合并订单错误。", e);
		}
		return new DSResponse(Status.FAILURE, "合并订单失败。");
	}
	

	/**
	 * 合并的方法计算总计
	 * 
	 * @param os
	 * @return
	 */
	@RequestMapping(value = "/combineCount", method = RequestMethod.POST)
	public @ResponseBody DSResponse comfirmOrdersToCombine(@RequestParam(value = "os") String os, ModelMap mp) {
		List<CombineBean> cb = JSON.parseArray(os, CombineBean.class);
		double productAmt = 0.0;
		double discountAmt = 0.0;
		// 额外物料费用
		double extraMAmt = 0.0;
		double extraFree = 0.0;
		double usefulBalance = 0.0;
		Double[] data = new Double[8];
		try {
			List<Long> oids = new ArrayList<>();

			for (CombineBean combineBean : cb) {
				Long id = StringUtil.toLong(combineBean.getId());
				if (id != null) {
					oids.add(id);
				}
			}

			List<Order> orders = orderService.find(oids.toArray(new Long[0]));
			Map<Activity, Integer> otherPackage = orderService.getMergeOtherBigPackage(orders);
			if (orders == null || orders.size() < 2) {
				throw new ActionException("订单为空或者合并的订单数小于2！");
			}

			UserCustomer uc = userCustomerManager.getByLoginName(getAuthUser().getLoginName());
			Integer myCustomerId = uc.getCustomerId();

			for (Order order : orders) {
				Integer counterId = order.getCounterId();
				Integer orderCustomerId = counterManager.get(counterId).getCustomerId();
				if (!myCustomerId.equals(orderCustomerId)) {
					throw new ActionException("合并订单客户账户不一致。");
				}
			}

			for (Order order : orders) {
				productAmt += order.getOrderOriginalFee();
				discountAmt += order.getPaymentFee();
				extraMAmt += order.getMaterialFee();
				extraFree += order.getExpressFee();
			}
			usefulBalance = getUseflBalance(request);
			data[0] = productAmt;
			data[1] = discountAmt;
			data[2] = extraMAmt;
			data[3] = extraFree;
			data[4] = usefulBalance;
			// 生成合并订单
			OrderCredentials combineOrders = orderService.createCombinePayment(orders);
			data[5] = Double.valueOf(combineOrders.getOcid());
			
			return new DSResponse(data);
		} catch (ActionException e) {
			return new DSResponse(Status.FAILURE, e.getMessage());
		} catch (Exception e) {
			logger.error("确认合并订单错误。", e);
		}
		return new DSResponse(Status.FAILURE, "合并订单失败。");
	}

	/**
	 * 拆解合并订单
	 * 
	 * @param os
	 * @param mp
	 * @return
	 */
	@RequestMapping(value = "/dismantleCombine", method = RequestMethod.POST)
	public @ResponseBody DSResponse dismantleCombineOrders(@RequestParam(value = "os") String os, ModelMap mp) {
		List<CombineBean> cb = JSON.parseArray(os, CombineBean.class);
		List<Order> orders = new ArrayList<>();
		try {
			for (CombineBean combineBean : cb) {
				Order o = orderService.get(Long.parseLong(combineBean.getId()));
				orders.add(o);
			}
			// 拆解合并订单
			orderService.dismantleCombineOrders(orders);

			return new DSResponse("redirect:/order/combinePage.do");
		} catch (ServiceException e) {
		} catch (Exception e) {
			logger.error("拆解合并订单失败", e);
		}
		return new DSResponse("redirect:/order/combinePage.do");

	}

	/**
	 * Step1 上传图片 付款成功后上传截图，交付财务待审核 Step1.1 分两步，第一步 ：设置图片
	 * 
	 * @param file
	 * @param request
	 * @param product
	 * @return
	 */
	@RequestMapping(value = "/uploadCombineImage", method = RequestMethod.POST)
	public String uploadCombine(Model model, @ModelAttribute("os") String os,
			@RequestParam(value = "files[]", required = false) MultipartFile[] files) throws IOException {
		HttpSession session = request.getSession();
		List<CombineBean> cb = JSON.parseArray(os, CombineBean.class);
		List<Order> orders = new ArrayList<>();
		for (CombineBean combineBean : cb) {
			Order o = orderService.get(Long.parseLong(combineBean.getId()));
			orders.add(o);
		}
		// 以单号最后3位拼接
		StringBuffer sbs = new StringBuffer();
		for (Order o : orders) {
			// str = str.substring(str.length()-3,str.length());
			sbs.append(o.getOrderNo().substring(o.getOrderNo().length() - 3, o.getOrderNo().length()));
		}
		String image_url = sbs.toString();
		String sessId = "ORDERC:" + image_url;
		ImageUploadInfo uploadInfo = (ImageUploadInfo) session.getAttribute(sessId);

		try {
			if (uploadInfo == null //
					|| (System.currentTimeMillis() - uploadInfo.lastUpdate > 60 * 1000)) {
				logger.debug("Create session id={}", sessId);
				// XXX: can not use Arrays.asList
				List<String> tmps = new ArrayList<>();
				uploadInfo = new ImageUploadInfo(tmps);
				session.setAttribute(sessId, uploadInfo);
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
				final String uri = UriUtil.getURI(UriUtil.FMT_COMB) + image_url + "_" + uploadInfo.size();
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
			// 这里并没有实际支付金额 这个order只有id和orderNo属性
			combineImageCache.put(super.getAuthUser().getLoginName(), uploadInfo.images);
			model.addAttribute(DSResponse.DS_MESSAGE, "success");
		} catch (ActionException e) {
			model.addAttribute(DSResponse.DS_MESSAGE, "error");
			logger.error("上传截图失败，OrderID={}", image_url, e);
		} catch (Exception e) {
			model.addAttribute(DSResponse.DS_MESSAGE, "error");
			logger.error("上传截图失败，OrderID={}", image_url, e);
		}
		return "pay_detail";
	}

	/**
	 * Step1.2 第二步：上传信息到后台 订单合并支付,等待确认，确认页面是
	 * 
	 * @param model
	 * @param orders
	 * @param actualPay
	 * @param description
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/uploadCombineOrders", method = RequestMethod.POST)
	public String uploadCombineOrders(Model model, @ModelAttribute("os") String os,
			@ModelAttribute("actualPay") String actualPay, @ModelAttribute("description") String description)
			throws IOException {
		try {
			List<CombineBean> cb = JSON.parseArray(os, CombineBean.class);
			List<Order> orders = new ArrayList<>();
			for (CombineBean combineBean : cb) {
				Order o = orderService.get(Long.parseLong(combineBean.getId()));
				orders.add(o);
			}
			// 生成凭证，并修改里面的一些属性
			String loginName = super.getAuthUser().getLoginName();
			// 存储生成的图片路径
			List<String> combineImages = combineImageCache.get(loginName);
			// 生成合并订单
			orderService.payCombineOrder(orders, super.getAuthUser(), TypeConverter.toDouble(actualPay, -1),
					description, combineImages);
			// 删掉保存的cache图片
			combineImageCache.remove(loginName);
			model.addAttribute("message", "success");
		} catch (Exception e) {
			model.addAttribute("message", "error");
			logger.error("存储实付金额失败", e);
		}
		return "pay_detail";
	}

	/**
	 * Step2 使用余额 释放余额 勾选动作
	 * 
	 * @param mp
	 * @param orderId
	 * @param flag
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/useCombineBalance")
	@ResponseBody
	public DSResponse useCombineBalance(ModelMap mp, @RequestParam(value = "ocid", required = false) String ocid,
			@RequestParam(value = "flag", required = false) Boolean flag, HttpServletRequest request) {
		DSResponse dsResponse = new DSResponse();
		try {
			OrderCredentials occ = credentialsService.get(ocid);
			List<String> ss = occ.getOrderNos();
			List<Order> os = (List<Order>) orderService.findByOrderNo(ss);
			double usefulBalance = getUseflBalance(request);
			orderService.userBalance(os, usefulBalance, flag);
			OrderCredentials occNow = credentialsService.get(ocid);
			double usefulBalanceNow = getUseflBalance(request);
			// 剩余余额
			dsResponse.getExtra().put("usefulBalance", usefulBalanceNow);
			// 使用余额
			dsResponse.getExtra().put("useBalance", occNow.getUseBlance());
			// 应付
			dsResponse.getExtra().put("payable", occNow.getTotalAmt() - occNow.getUseBlance());
			// 返回当前页刷新数据
		} catch (Exception e) {
			logger.error("合并订单取消失败", e);
		}
		return dsResponse;
	}

	/**
	 * Step3 合并订单确认，金额才会转入每单 因为合并订单的金额比较大，所以需要确认一下，不存在直接通过
	 * 
	 * @param orders
	 * @return
	 */
	@RequestMapping(value = "/confirmCombine", method = RequestMethod.POST)
	public @ResponseBody DSResponse confirmCombineOrder(@ModelAttribute("os") String os,Long ds) {
		List<CombineBean> combineBeans = JSON.parseArray(os, CombineBean.class);
		List<Long> orderIds = new ArrayList<>();
		try {

			for (CombineBean cb : combineBeans) {
				Long id = StringUtil.toLong(cb.getId());
				if (id != null) {
					orderIds.add(id);
				}
			}
			if (orderIds.size() < 2) {
				throw new ActionException("订单数量小于2!");
			}
			List<Order> orders = orderService.find(orderIds.toArray(new Long[0]));
			orderService.confirmCombineOrders(orders);
//			orders = orderService.find(orderIds.toArray(new Long[0]));
//			for(Order order:orders){
//				Map<Activity, Integer> act = orderService.getAutoBigPackage(Arrays.asList(order));
//				for(Activity activity : act.keySet()){
//					int suitNum = getSuitNum(activity.getContext().getActityExtra(),act.get(activity),order);
//					if(suitNum>0){
//					orderService.AddBigPackageToOrder(order,activity.getActId(), suitNum);
//					}
//				}
//			}
			orders = orderService.find(orderIds.toArray(new Long[0]));
			Map<Activity, Integer> otherAct = orderService.getMergeOtherBigPackage(orders);
			Order otherOrder = orderService.get(ds);
			for(Activity activity : otherAct.keySet()){
				int suitNum = getSuitNum(activity.getContext().getActityExtra(),otherAct.get(activity),otherOrder);
				if(suitNum>0){
				orderService.addBigPackageToOrder(otherOrder,activity.getActId(), suitNum);
				}
			}
			
			// FIXME:是否存在活动，只有区域活动才会添加
//			for (Order order : orders) {
//				if (isExistMyActis(counterManager.get(order.getCounterId()))) {
//					orderService.addActivityToOrder(order.getOrderNo(),
//							counterManager.getCounterByCode(order.getCounterCode()));
//				}
//			}
			
		} catch (Exception e) {
			logger.error("合并订单确认失败", e);
			return new DSResponse(-1, "合并订单确认失败");
		}
		return DSResponse.OK;
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
			
			// FIXME:
//			if (isExistMyActis(counterManager.get(order.getCounterId()))) {
//				orderService.addActivityToOrder(order.getOrderNo(),
//						counterManager.getCounterByCode(order.getCounterCode()));
//			}
			saveBalanceRecord(bdOrder2, BalanceRecord.S_PAYMENT, request);
//			Map<Activity, Integer> act = orderService.getAutoBigPackage(Arrays.asList(orderService.get(orderId)));
//			for(Activity activity : act.keySet()){
//				int suitNum = getSuitNum(activity.getContext().getActityExtra(),act.get(activity),orderService.get(orderId));
//				if(suitNum>0){
//				orderService.AddBigPackageToOrder(orderService.get(orderId),activity.getActId(), suitNum);
//				}
//			}
//			orderService.AddAutoBigPackage(orderService.get(orderId));
		} catch (Exception e) {
			logger.error("订单确定失败, OrderId={}", orderId, e);
		}
		return "forward:/order/index.do";
	}

	/**
	 * 直营直接提交 或者全额使用余额支付
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
			Assert.notNull(counter, "柜台为空");

			saveBalanceRecord(bdOrder, BalanceRecord.S_INITIAL, request);
			// orderService.payByDirect(order, super.getAuthUser());
			orderService.payByBlance(order, getAuthUser(), counter.getType());
			bdOrder = orderService.getsingleOrder(orderNo);

			// FIXME:
//			if (isExistMyActis(counterManager.get(order.getCounterId()))) {
//				orderService.addActivityToOrder(order.getOrderNo(),
//						counterManager.getCounterByCode(order.getCounterCode()));
//			}

			saveBalanceRecord(bdOrder, BalanceRecord.S_PAYMENT, request);
//			Map<Activity, Integer> act = orderService.getAutoBigPackage(Arrays.asList(orderService.get(order.getId())));
//			for(Activity activity : act.keySet()){
//				int suitNum = getSuitNum(activity.getContext().getActityExtra(),act.get(activity),orderService.get(order.getId()));
//				if(suitNum>0){
//				orderService.AddBigPackageToOrder(orderService.get(order.getId()),activity.getActId(), suitNum);
//				}
//			}
		} catch (Exception e) {
			logger.error("直营订单提交失败, OrderNo={}", orderNo, e);
		}
		return "forward:/order/index.do";
	}

	/**
	 * 全额使用余额支付合并订单
	 * 
	 * @param order
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/confirmCombineDirect", method = RequestMethod.POST)
	public @ResponseBody DSResponse confirmCombineDirect(@ModelAttribute("os") String os, Model model,
			@RequestParam(value = "description") String decription,Long ds) {
		List<CombineBean> combineBeans = JSON.parseArray(os, CombineBean.class);
		List<Long> orderIds = new ArrayList<>();
		try {
			for (CombineBean cb : combineBeans) {
				Long id = StringUtil.toLong(cb.getId());
				if (id != null) {
					orderIds.add(id);
				}
			}
			if (orderIds.size() < 2) {
				throw new ActionException("订单数量小于2!");
			}
			List<Order> orders = orderService.find(orderIds.toArray(new Long[0]));
			// FIXME:对数据库操作过多
			recalculateBalance(orders);
			OrderCredentials ocb = credentialsService.getCombineOrderCredentials(orders.get(0).getOrderNo());
			// 全额支付
			if (Math.abs(ocb.getTotalAmt() - ocb.getUseBlance()) < 0.001) {
				orderService.balancePayCombineOrdersDirect(orders, getAuthUser(), decription);

//				for (Order order : orders) {
//					// FIXME:添加活动类型为41的活动物料
//					if (isExistMyActis(counterManager.get(order.getCounterId()))) {
//						orderService.addActivityToOrder(order.getOrderNo(),
//								counterManager.getCounterByCode(order.getCounterCode()));
//					}
//				}
			}
//			orders = orderService.find(orderIds.toArray(new Long[0]));
//			for(Order order:orders){
//				Map<Activity, Integer> act = orderService.getAutoBigPackage(Arrays.asList(order));
//				for(Activity activity : act.keySet()){
//					int suitNum = getSuitNum(activity.getContext().getActityExtra(),act.get(activity),order);
//					if(suitNum>0){
//					orderService.AddBigPackageToOrder(order,activity.getActId(), suitNum);
//					}
//				}
//			}
			orders = orderService.find(orderIds.toArray(new Long[0]));
			Map<Activity, Integer> otherAct = orderService.getMergeOtherBigPackage(orders);
			Order otherOrder = orderService.get(ds);
			for(Activity activity : otherAct.keySet()){
				int suitNum = getSuitNum(activity.getContext().getActityExtra(),otherAct.get(activity),otherOrder);
				if(suitNum>0){
					orderService.addBigPackageToOrder(otherOrder,activity.getActId(), suitNum);
				}
			}
		} catch (ActionException e) {
			logger.warn(e.getMessage());
		}catch (Exception e) {
			logger.error("合并订单提交失败", e);
			return new DSResponse(DSResponse.FAILURE);
		}
		return new DSResponse(DSResponse.SUCCESS);
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
			// 退回活动限定套数
			List<OrderDetail> details = orderService.getOrderDetails(order.getOrderNo());
			activityLimitManager.releaseOrderToCart(order, details);
			//取消订单备注
			order.setCancelDesc("客户取消订单，取消人："+this.getAuthUser().getLoginName());
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
		// FIXME:实现更新订单 影响支付宝到账
		try {
			boolean upSuccess = aliPaymentService.updateOUN(orderID, super.getAuthUser());
			if (upSuccess) {
				return "success";
			}
			return "fail";
		}catch(Exception e) {
			logger.error("更新订单用户名称异常,orderID:{}",orderID,e);
			return "fail";
		}
	}
	
	/**
	 * ajax 更新订单用户名字
	 */
	@RequestMapping(value = "/combineUOUN", method = RequestMethod.GET)
	public @ResponseBody String updateCombineOrderUserName(String ocid) {
		// FIXME:实现更新订单 影响支付宝到账
		try {
			OrderCredentials orderCredentials = credentialsService.get(ocid);
			List<String> orderNos = orderCredentials.getOrderNos();
			if (aliPaymentService.updateCombineOUN(orderNos, super.getAuthUser())) {
				return "success";
			}
		} catch (Exception e) {
			logger.error("更新订单用户名称异常,orderID:{}", ocid, e);
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
			@RequestParam(value = "flag", required = false) Boolean flag, HttpServletRequest request) throws IOException {
		try {
			double usefulBalance = getUseflBalance(request);
			if(orderId == null) {
				throw new ActionException();
			}
			//FIXME: 需要写在同一service中，组织事务！！
			orderService.userBalance(orderId, usefulBalance, flag);
			// 部分提交！！
			return orderDetail(mp, orderId, request);
		} catch (ActionException e) {
			logger.warn("orderId must be requered", e);
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
			// 退回活动限定套数
			List<OrderDetail> details = orderService.getOrderDetails(order.getOrderNo());
			activityLimitManager.releaseOrderToCart(order, details);
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
	 * 在头部位置放上 查看预订待发货的产品以及数量
	 * 
	 * @param usr
	 *            返回显示：仓库，产品名称，数量，所属订单，归属于哪个店铺
	 */
	@RequestMapping(value = "/preshow", method = RequestMethod.POST)
	public  @ResponseBody List<ProductBean> getUnSendProduct(//
			@RequestParam(value = "status", required = false) String status) throws IOException {
		List<ProductBean> productBeans = new ArrayList<ProductBean>();
		try {
			// 需要判断当前用户是不是老板
			if(!userCustomerManager.isBoss(super.getAuthUser().getLoginName())||StringUtil.isEmpty(status)){
				return productBeans;
			}
			
			UserCustomer userCustomer  = userCustomerManager.getByLoginName(super.getAuthUser().getLoginName()); 	
			// 搜索条件参数
			List<Integer> colletion = new ArrayList<>();
			
			colletion.add(Integer.parseInt(status));
			ProductBean productBean;
			Date now = new Date();
			List<ReserveRecord> reserveRecords = reserveRecordService.findByCustomerIdAndStatus(String.valueOf(userCustomer.getCustomerId()), colletion);
			for (ReserveRecord reserveRecord : reserveRecords) {
				// 所属活动
				String ActivityName = "";
				if (!Strings.isNullOrEmpty(reserveRecord.getActivityId())) {
					ActivityName = activityManager.get(reserveRecord.getActivityId()).getName();
				}
				// 从字典中获取仓库名称
				Dictionary wareHouse = dictManager.lookUpByCode(Warehouse.dict_type, String.valueOf(reserveRecord.getWarehouse()));
				// 封装前端BEAN
				productBean = new ProductBean(wareHouse.getName(), // 仓库
						productManager.get(reserveRecord.getProductId()).getName(), // 产品名称
						String.valueOf(reserveRecord.getQuantity()), // 设置数量
						reserveRecord.getOrderNo(), // 所属订单
						counterManager.getCounterByCode(reserveRecord.getCounterCode()).getCounterName(),// 柜台号
						reserveProductManager.get(reserveRecord.getProductId()) != null // 判空
						&& (now.getTime() - reserveProductManager.get(reserveRecord.getProductId()).getExcuteEnd().getTime()) //
						  < 0 ? false : true ,// 是否可以还欠
						reserveRecord.getOrderDetailId(), // id
						ActivityName // 所属活动名称
						);
				productBeans.add(productBean);
			}
			return productBeans;
		} catch (Exception e) {
			logger.error("显示未发货的产品",e);
			return productBeans;
		}
	}
	
	/**
	 * 显示所有的打欠预定的产品
	 *  以及活动
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/showallpre", method = RequestMethod.POST)
	public @ResponseBody List<ProductBean> showAllPrePro() throws IOException{
		List<ProductBean> productBeans = new ArrayList<ProductBean>();
		// 打欠产品 和 打欠活动
		List<ReserveProduct> correctReserveProducts = new ArrayList<>();
		List<ReservedActivity> reservedActs = new ArrayList<>();
		try {
			// All product
			List<ReserveProduct> reserveProducts = reserveProductManager.findAll();
			if(reserveProducts == null) {
				return productBeans;
			}
			
			// 过滤正确时间的活动产品
			for (ReserveProduct reserveProduct : reserveProducts) {
				if(reserveProductManager.isReserving(reserveProduct.getProductId(),super.getOrderCounter().getWarehouses())) {
					correctReserveProducts.add(reserveProduct);
				}
			}
			ProductBean productBean;
			for (ReserveProduct reserveProduct : correctReserveProducts) {
				// 封装前端BEAN 1打欠起始时间 2还欠其实时间 3产品实体 4活动
				productBean = new ProductBean(//
						DateTime.formatDate("yyyy-MM-dd", reserveProduct.getReserveStart()) + "-"//
							+ DateTime.formatDate("yyyy-MM-dd", reserveProduct.getReserveEnd()),//
						DateTime.formatDate("yyyy-MM-dd",reserveProduct.getExcuteStart()) + "-" //
							+ DateTime.formatDate("yyyy-MM-dd",reserveProduct.getExcuteEnd()),//
						productManager.get(reserveProduct.getProductId()),
						null);
				productBeans.add(productBean);
			}
			// All activity
			reservedActs = reservedActivityManager.getMyReservedActivity(super.getOrderCounter());
			if(reservedActs.size() > 0) {
				for (ReservedActivity reservedAct : reservedActs) {
					// 封装前端BEAN 1打欠起始时间 2还欠其实时间 3产品实体 4活动
					// 没有办法的循环嵌套循环
					for (String actId : reservedAct.getContext().getActivities()) {
					productBean = new ProductBean(//
							DateTime.formatDate("yyyy-MM-dd", reservedAct.getStartTime()) + "-"//
								+ DateTime.formatDate("yyyy-MM-dd", reservedAct.getEndTime()),//
							DateTime.formatDate("yyyy-MM-dd",reservedAct.getStartTime()) + "-" //
								+ DateTime.formatDate("yyyy-MM-dd",reservedAct.getEndTime()),//
							null,
							activityManager.get(actId));
					productBeans.add(productBean);
					}
				}
			}
			return productBeans;
		} catch (Exception e) {
			logger.error("显示所有预定产品异常");
			return productBeans;
		}
	}
	/**
	 * 取消预售订单
	 * 
	 * @param orderDetailId
	 * @throws IOException
	 */
	@RequestMapping(value = "/canclePreSale", method = RequestMethod.POST)
	public @ResponseBody DSResponse canclePreSaleGoods(@RequestParam(value = "orderDetailId", required = false)Long orderDetailIdVO) throws IOException {
		try {
			if (orderDetailIdVO == null) {
				return new DSResponse(-1,"orderDetailIdVO为空");
			}
			// step 1 : 
			OrderDetail orderDetail = orderDetailService.get(Integer.parseInt(orderDetailIdVO.toString()));
			if (orderDetail == null) {
				logger.info("没有此条订单明细 orderDetailID:{}",orderDetailIdVO);
				return new DSResponse(-1,"没有此条订单明细");
			}
			Long detailID = orderDetail.getId();
			String actvityId = orderDetail.getActivityId();
			if(!StringUtil.isEmpty(actvityId) // 判断当前明细id 
					&& !ActivityType.TYPE_BIGPACAKGE.equals(activityManager.get(actvityId).getType())) {// 04 大礼包不算
				// step 2.5 : 活动
				reserveRecordService.cancleReserveActivity(detailID);
			} else {
				// step 2 : 单品 和 大礼包(活动类型为04)
				reserveRecordService.cancleReserve(detailID);
			}
			return new DSResponse(DSResponse.SUCCESS);
		} catch (Exception e) {
			logger.error("取消订单异常,orderDetailID:{}",orderDetailIdVO);
			return new DSResponse(DSResponse.FAILURE);
		}
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
	private double saveBalanceRecord(Order order, int status, HttpServletRequest request) throws ActionException {
		double defaultBalance = 0.0;
		try {
			AuthUser authUser = this.getAuthUser();
			if(authUser == null) {
				throw new ActionException("登录超时");
			}
			
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
			defaultBalance = balanceService.getUsefulBalance(uc.getCustomerId());
			return defaultBalance;
		} catch (ActionException e) {
			logger.warn(e.getMessage());
		} catch (Exception e){
			logger.error("sava balace record error ,method{},orderNo{}","saveBalanceRecord",order.getOrderNo(),e);
		}
		return 0.0;
	}

	/**
	 * 支付前重新计算余额 并修改单头
	 * 
	 * @param orderId
	 * @throws Exception 
	 */
	private void recalculateBalance(Long orderId) throws Exception {
		if(orderId == null) {
			throw new Exception("OrderId must be not null!");
		}
		Order bdOrder = orderService.get(orderId);
		// 此订单余额
		double myBalance = bdOrder.getUseBalance();
		// 如果使用余额 确认支付时再计算一次可使用余额
		if (myBalance != 0) {
			// 可用余额中把本订单已经使用余额除去
			double usefulBalance = getUseflBalance(request);
			if (!Order.status_ForFinancialRefuse.equals(bdOrder.getOrderStatus())) {
				usefulBalance += myBalance;
			}
			// 重新使用余额
			orderService.userBalance(bdOrder.getId(), usefulBalance, true);
		}
	}

	/**
	 * 支付前重新计算余额 并修改单头
	 * 
	 * @param orderId
	 * @throws ActionException 
	 */
	private void recalculateBalance(List<Order> os) throws ActionException {
		try {
			List<Order> orders = orderService.getCombineOrders(os);
			//
			OrderCredentials ocb = credentialsService.getCombineOrderCredentials(orders.get(0).getOrderNo());
			Double myBalance = ocb.getUseBlance();
			// 如果使用余额 确认支付时再计算一次可使用余额
			if (myBalance != 0) {
				// 可用余额中把本订单已经使用余额除去
				double usefulBalance = getUseflBalance(request);
				usefulBalance += myBalance;
				// 重新使用余额
				orderService.userBalance(orders, usefulBalance, true);
			}
		} catch (Exception e){
			logger.error("combine orders is null");
			throw new ActionException(e);
		}
	}

	@Deprecated
	private boolean isExistMyActis(Counter counter) {
		List<Activity> myActis = activityManager.findMyActivities(counter);
		return StringUtil.isEmpty(myActis);
	}
	
	/**
	 * 校验库存是否足够所需数目
	 * 
	 * @param productRule
	 * @param suitNum
	 * @param order
	 */
	private int getSuitNum(ProductRule productRule,int suitNum,Order order){
		final StockService stockService = AppContext.getBean(StockService.class);
		// 有效校验
		Counter counter = counterManager.get(order.getCounterId());
		String wid = counter.getWarehouses();
		Assert.notNull(wid, "自动加入大礼包校验库存仓库为空");
		for (ProductItem item : productRule.getProductItem().values()) {
			Integer productId = item.getPid();
			Integer num = item.getQuantity();
			// 大礼包里产品打欠不校验库存
			if (reserveProductManager.isReserving(productId, wid)) {
				continue;
			}
			Integer qty = stockService.getStockProductQty(wid, productId.toString());
			int suit = qty / num;
			if (suit >= 0 && suit < suitNum) {
				suitNum = suit;
			}
		}
		return suitNum;
	}
	
	
	
}