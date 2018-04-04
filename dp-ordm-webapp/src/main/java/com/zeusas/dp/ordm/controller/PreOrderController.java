package com.zeusas.dp.ordm.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.zeusas.common.utils.DSResponse;
import com.zeusas.common.utils.Status;
import com.zeusas.core.http.ActionException;
import com.zeusas.core.http.HttpUtil;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.service.CounterService;
import com.zeusas.dp.ordm.service.OrderService;
import com.zeusas.dp.ordm.service.PreOrderService;
import com.zeusas.dp.ordm.service.SystemOrderService;
import com.zeusas.security.auth.entity.AuthUser;

@RequestMapping("/preorder")
@Controller
/**
 * 导入预订单功能及预订单转换成为正常代付款订单+合并含礼盒订单
 * @author pengbo
 *
 */
public class PreOrderController extends OrdmBasicController {

	// 设置单独的日志付款日志
	static Logger logger = LoggerFactory.getLogger(PreOrderController.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	private PreOrderService preOrderService;

	@Autowired
	private SystemOrderService orderService;

	@Autowired
	private OrderService oService;

	@Autowired
	private CounterService counterService;

	@RequestMapping("/uploadpreorderpage")
	public String uploadpage() {
		return "preOrderPage";
	}

	@RequestMapping("/combinePage")
	public String combinePage(Model m) throws IOException {

		try {
			List<Integer> counterIds = super.getAllCounterIds();
			if (StringUtil.isEmpty(counterIds)) {
				throw new UnauthenticatedException("帐号异常 没有柜台，无权登录。");
			}
			List<Order> orders = oService.getOrders(counterIds, Order.Status_UnPay);
			List<Order> normalorders = new ArrayList<>();
			// 过滤合并订单
			List<Order> sysorder = new ArrayList<>();
			for (Order order : orders) {
				Integer issysin = order.getIssysin();
				if (issysin ==Order.PRESENT_TYPE_NORMAL) {
					normalorders.add(order);
				} else if (Order.PRESENT_TYPE_IMPORT == order.getIssysin()) {
					sysorder.add(order);
				} else {
					normalorders.add(order);
				}
			}
			m.addAttribute("counters", super.getAllCounters());
			m.addAttribute("orders", normalorders);
			m.addAttribute("sysorder", sysorder);
		} catch (Exception e) {
			logger.error("获取合并订单异常", e);
		}
		return "sysOrder_page";
	}

	@RequestMapping("/upload")
	@ResponseBody
	public DSResponse upload(@RequestParam("uploadFile") CommonsMultipartFile file, HttpServletResponse response, //
			HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		String realPath = this.getClass().getClassLoader().getResource("").getPath();
		String filename = file.getOriginalFilename();
		String path = realPath + "temp" + filename;
		DSResponse ds = DSResponse.OK;
		try {
			File upload = upload(path, file);
			if (preOrderService.insertExcel(upload)) {
				return ds;
			}
		} catch (Exception e) {
			logger.error("", e);
			return new DSResponse(Status.IO_ERROR, "文件解析错误。");
		}
		return ds;
	}

	@RequestMapping("/transtoOrder")
	@ResponseBody
	public DSResponse transtoOrder() {
		try {
			orderService.transPreOrderToOrder();
		} catch (Exception e) {
			logger.error("", e);
			return new DSResponse(Status.TRANSACTION_ERROR, "转换订单出错");
		}
		return DSResponse.OK;
	}

	public static File upload(String path, CommonsMultipartFile file) throws IOException {
		File newFile = new File(path);

		if (!newFile.exists()) {
			newFile.mkdirs();
		} else {
			newFile.delete();
			newFile.mkdirs();
		}
		newFile.createNewFile();
		file.transferTo(newFile);
		return newFile;
	}

	// 确认合并
	@RequestMapping(value = "/confirmCombine", method = RequestMethod.POST)
	public @ResponseBody DSResponse comfirmOrdersToCombine(@RequestParam(value = "orderNo", required = false) String orderNo,
			@RequestParam(value = "orderNos") List<String> orderNos,
			@RequestParam(value = "counterId") Integer counterId) throws IOException {
		logger.info("OrderNo:{},OrderNos:{},CounterId:{}",orderNo,JSON.toJSONString(orderNos),counterId);
		AuthUser authUser = getAuthUser();
		if (StringUtil.isEmpty(orderNos) //
				|| StringUtil.isEmpty(counterId)) {
			return new DSResponse(Status.FAILURE, "参数类型错误");
		}
		try {
			if(!HttpUtil.isEmpty(orderNo)) {
				Order getsingleOrder = oService.getsingleOrder(orderNo);
				if (getsingleOrder==null) {
					throw new ActionException("訂單不存在！");
				}
				counterId = getsingleOrder.getCounterId();
				orderNos.add(orderNo);
			}

			List<Integer> counterIds = super.getAllCounterIds();
			if (StringUtil.isEmpty(counterIds)) {
				throw new UnauthenticatedException("帐号异常 没有柜台，无权登录。");
			}
			Collection<Order> orders = oService.findByOrderNo(orderNos);
			if (orders.size() == 1) {
				throw new ActionException("合并至少2個訂單！");
			}
			for (Order order : orders) {
				// 1是未合并前系统导入订单，2是合并后
				if(order.getMerger()) {
					throw new ActionException("訂單處於批量支付狀態，ORERID="+order.getOrderNo());
				}
				if (!(counterIds.contains(order.getCounterId()) //
						&& Order.Status_UnPay.equals(order.getOrderStatus()))) {
					throw new ActionException("訂單狀態不正確，ORERNO="+order.getOrderNo());
				}
			}

			if (!counterIds.contains(counterId)) {
				logger.warn("参数类型错误,OrderNo:{},CounterId:{}",JSON.toJSONString(orderNos),counterId);
				throw new ActionException("櫃檯號不正確，counterId="+counterId);
			}

			String counterCode = counterService.get(counterId).getCounterCode();
			// 合并處理！
			preOrderService.combineOrder(orderNos, counterCode,authUser);
			return new DSResponse(Status.SUCCESS, "合并成功");
		} catch (ActionException e) {
			logger.error("",e.getMessage());
			return new DSResponse(Status.FAILURE, "合并失败");
		} catch (Exception e) {
			logger.error("合并失败", e);
		}
		return new DSResponse(Status.FAILURE, "合并失败");
	}

	/**
	 * 根据任意合并订单单号 获取 合并订单里所有的订单（仅计算未执行合并）
	 * 返回值为正品金额，物料金额，运费，折后，应付款
	 * @param OrderId
	 * @param mp
	 * @author wangs
	 * @return
	 */
	@RequestMapping(value = "/getCombineOrder", method = RequestMethod.GET)
	@ResponseBody
	public DSResponse combineOrderById(@RequestParam(value = "orderNo", required = false) String orderNo,
			@RequestParam(value = "orderNos") List<String> orderNos) {

		DSResponse dsResponse = new DSResponse();
		if (orderNo != null) {
			orderNos.add(orderNo);
		}

		Collection<Order> orders;
		// 商品金额
		double productAmt = 0.0;
		// 折后金额
		double discountAmt = 0.0;

		// 运费
		double freight = 0.0;
		// 应付金额
		double amount = 0.0;
		// 物料金额
		double materialAmt = 0.0;
		Double[] data = new Double[5];
		try {
			orders = oService.findByOrderNo(orderNos);
			for (Order o : orders) {
				productAmt += o.getOrderOriginalFee();
				discountAmt += o.getProductFee();
				materialAmt += o.getMaterialFee();
			}
			freight = discountAmt >= 5000d ? (discountAmt >= 8000d ? 0 : 150) : 100;
			amount = discountAmt + freight + materialAmt;
			data[0] = productAmt;
			data[1] = discountAmt;
			data[2] = freight;
			data[3] = amount;
			data[4] = materialAmt;
			dsResponse.setData(data);
			dsResponse.setMessage("合并成功");
		} catch (Exception e) {
			logger.error("获取合并订单异常", e);
			return new DSResponse(Status.FAILURE, "合并失败");
		}
		return dsResponse;
	}

}