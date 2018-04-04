package com.zeusas.dp.ordm.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zeusas.core.http.ActionException;
import com.zeusas.core.utils.StringUtil;
import com.zeusas.dp.ordm.entity.Order;
import com.zeusas.dp.ordm.entity.OrderCredentials;
import com.zeusas.dp.ordm.service.OrderCredentialsService;
import com.zeusas.dp.ordm.service.OrderService;

@Controller
@RequestMapping("/alipay")
public class AlipayController {
	
	//设置单独的日志付款日志
	static Logger logger = LoggerFactory.getLogger(AlipayController.class);
	static final String COMBINE = "combine";// 合并订单前添加此字段用以区分合并订单

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderCredentialsService credentialsService;
	
	@RequestMapping("/index")
	public String index(
			@RequestParam(name="orderID",required=false)Long order_id,
			HttpServletRequest request) throws IOException{
			try{
				if(order_id == null) {
					throw new ActionException("OrderId must be not null");
				}
				Order order = orderService.get(order_id);
				if(order==null){
					return "redirect:/ordm/index.do";
				}
				String out_trade_no = order.getOrderNo();//订单号
				String subject = order.getId().toString();//订单名称->订单主键。
//				Double total_fee = order.getPaymentFee();;//订单金额
				//FIX ME:by shi
				Double total_fee = order.getPayable();//订单金额
				//下述参数任意为空，订单无效，返回到首页。
				if (StringUtil.isEmpty(out_trade_no) || StringUtil.isEmpty(subject)
						|| StringUtil.isEmpty(total_fee)) {
					return "redirect:/ordm/index.do";
				}
				request.setAttribute("out_trade_no", out_trade_no);
				request.setAttribute("subject", subject);
				request.setAttribute("total_fee", total_fee);
			}catch(ActionException e) {
				logger.error("订单号为null",e);
			}catch(Exception e){
				logger.error("支付错误，金额订单号不对",e);
			}
		return "alipay/alipay_index";
	}
	
	@RequestMapping("/combineindex")
	public String combineindex(
			@RequestParam(name="ocid",required=false)String ocid,Long ds,
			HttpServletRequest request) throws IOException{
			try{
				if(StringUtil.isEmpty(ocid)) {
					logger.warn("OrderId must be not null");
					return "redirect:/ordm/index.do";
				}
				OrderCredentials orderCredentials = credentialsService.get(ocid);
				if(orderCredentials==null){
					return "redirect:/ordm/index.do";
				}
				
				String out_trade_no = COMBINE + ocid;// 订单名称->订单主键。
				String subject = orderService.get(ds).getOrderNo();//订单号
				Double total_fee = orderCredentials.getTotalAmt();// 订单金额
				//下述参数任意为空，订单无效，返回到首页。
				if (StringUtil.isEmpty(out_trade_no) //
						|| StringUtil.isEmpty(subject) //
						|| StringUtil.isEmpty(total_fee)) {
					return "redirect:/ordm/index.do";
				}
				
				request.setAttribute("out_trade_no", out_trade_no);
				request.setAttribute("subject", subject);
				request.setAttribute("total_fee", total_fee);
				
				return "alipay/alipay_index";
			}catch(Exception e){
				logger.error("支付错误，金额订单号不对",e);
			}
			return "redirect:/ordm/index.do";
	}
	
	@RequestMapping("/pay")
	public String pay(HttpServletRequest request) throws IOException{
		return "pay";
	}
	
}
