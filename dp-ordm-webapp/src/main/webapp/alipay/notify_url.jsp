<%@page import="com.zeusas.security.auth.service.AuthCenterManager"%>
<%@page import="com.zeusas.security.auth.service.AuthUserService"%>
<%
/* *
 功能：支付宝服务器异步通知页面
 版本：3.3
 日期：2012-08-17
 说明：
 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 //***********页面功能说明***********
 创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
 该页面调试工具请使用写文本函数logResult，该函数在com.alipay.util文件夹的AlipayCore.java类文件中
 如果没有收到该页面返回的 success 信息，支付宝会在24小时内按一定的时间策略重发通知
 //********************************
 * */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.zeusas.core.http.HttpUtil"%>
<%@ page import="com.zeusas.security.auth.entity.AuthUser"%>
<%@ page import="com.zeusas.core.utils.AppContext"%>
<%@ page import="com.zeusas.core.utils.StringUtil"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="java.time.LocalTime"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.alipay.util.*"%>
<%@ page import="com.alipay.config.*"%>
<%@ page import="com.zeusas.dp.ordm.entity.Order"%>
<%@ page import="com.zeusas.dp.ordm.service.OrderService"%>
<%@ page import="com.zeusas.dp.ordm.entity.AliPayment"%>
<%@ page import="com.zeusas.dp.ordm.service.AliPaymentService"%>
<%@ page import="com.zeusas.dp.ordm.service.MonthPresentManager"%>
<%@ page import="com.zeusas.dp.ordm.entity.MonthPresent"%>
<%@ page import="com.zeusas.dp.ordm.active.model.Activity"%>
<%@ page import="com.zeusas.dp.ordm.service.StockService"%>
<%@ page import="com.zeusas.dp.ordm.service.RedeemPointService"%>
<%@ page import="com.zeusas.dp.ordm.entity.OrderCredentials"%>
<%@ page import="com.zeusas.dp.ordm.service.OrderCredentialsService"%>

<%
	//获取支付宝POST过来反馈信息
	Map<String,String> params = new HashMap<String,String>();
	Map requestParams = request.getParameterMap();
	for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		String name = (String) iter.next();
		String[] values = (String[]) requestParams.get(name);
		String valueStr = "";
		for (int i = 0; i < values.length; i++) {
			valueStr = (i == values.length - 1) ? valueStr + values[i]
					: valueStr + values[i] + ",";
		}
		//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
		//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
		params.put(name, valueStr);
	}
	
	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
	
	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
	//1商户订单号
	String body = request.getParameter("body")==null?request.getParameter("body"):new String(request.getParameter("body").getBytes("ISO-8859-1"),"UTF-8");
	//5成功与否
	String is_success = request.getParameter("is_success")==null?request.getParameter("is_success"):new String(request.getParameter("is_success").getBytes("ISO-8859-1"),"UTF-8");
	//6异步id
	String notify_id = request.getParameter("notify_id")==null?request.getParameter("notify_id"):new String(request.getParameter("notify_id").getBytes("ISO-8859-1"),"UTF-8");
	//7异步时间
	String notify_time = request.getParameter("notify_time")==null?request.getParameter("notify_time"):new String(request.getParameter("notify_time").getBytes("ISO-8859-1"),"UTF-8");
	//8异步类型
	String notify_type = request.getParameter("notify_type")==null?request.getParameter("notify_type"):new String(request.getParameter("notify_type").getBytes("ISO-8859-1"),"UTF-8");
	//9商户订单号
	String out_trade_no = request.getParameter("out_trade_no")==null?request.getParameter("out_trade_no"):new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
	//10付款类型
	String payment_type = request.getParameter("payment_type")==null?request.getParameter("payment_type"):new String(request.getParameter("payment_type").getBytes("ISO-8859-1"),"UTF-8");
	//11收款方邮件
	String seller_email = request.getParameter("seller_email")==null?request.getParameter("seller_email"):new String(request.getParameter("seller_email").getBytes("ISO-8859-1"),"UTF-8");
	//12收款方id
	String seller_id = request.getParameter("seller_id")==null?request.getParameter("seller_id"):new String(request.getParameter("seller_id").getBytes("ISO-8859-1"),"UTF-8");
	//13订单名称
	String subject = request.getParameter("subject")==null?request.getParameter("subject"):new String(request.getParameter("subject").getBytes("ISO-8859-1"),"UTF-8");
	//14付款金额
	String total_fee = request.getParameter("total_fee")==null?request.getParameter("total_fee"):new String(request.getParameter("total_fee").getBytes("ISO-8859-1"),"UTF-8");
	//15支付宝交易号
	String trade_no = request.getParameter("trade_no")==null?request.getParameter("trade_no"):new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
	//16订单状态
	String trade_status = request.getParameter("trade_status")==null?request.getParameter("trade_status"):new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
	//17签名
	String sign = request.getParameter("sign")==null?request.getParameter("sign"):new String(request.getParameter("sign").getBytes("ISO-8859-1"),"UTF-8");
	//18签名类型
	String sign_type = request.getParameter("sign_type")==null?request.getParameter("sign_type"):new String(request.getParameter("sign_type").getBytes("ISO-8859-1"),"UTF-8");

	AliPayment ap=new AliPayment();
	ap.setPaymentId(trade_no);
	ap.setBody(body);
	ap.setIsSuccess(is_success);
	ap.setNotifyId(notify_id);
	ap.setNotifyTime(notify_time);
	ap.setNotifyType(notify_type);
	ap.setOutTradeNo(out_trade_no);
	ap.setPaymentType(payment_type);
	ap.setSellerEmail(seller_email);
	ap.setSellerId(seller_id);
	ap.setSign(sign);
	ap.setSignType(sign_type);
	ap.setSubject(subject);
	ap.setTotalFee(total_fee);
	ap.setTradeNo(trade_no);
	ap.setTradeStatus(trade_status);
	
	boolean isSuccess = false;
	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
	if(AlipayNotify.verify(params)){//验证成功
			//请在这里加上商户的业务逻辑程序代码
		try {
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		//数据库存储返回头 支付宝电子凭证保存数据库
			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			if(trade_status.equals("TRADE_FINISHED")){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
					//如果有做过处理，不执行商户的业务程序
				//注意：
				//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
				isSuccess = true;
			} else if (trade_status.equals("TRADE_SUCCESS")){
				AliPaymentService alipayService = AppContext.getBean(AliPaymentService.class);
				if (alipayService.get(trade_no)==null){
					// step1: 保存文档
					alipayService.save(ap);
					if(!out_trade_no.contains("combine")){
					// step2: 添加月度物料
					OrderService orderService = AppContext.getBean(OrderService.class);
					MonthPresentManager monthPresentManager = AppContext.getBean(MonthPresentManager.class);
					RedeemPointService redeemPointService  = AppContext.getBean(RedeemPointService.class);
					Order order = orderService.get(StringUtil.toLong(subject,-1));
					
					String counterCode = order.getCounterCode();
					int yearmonth=monthPresentManager.getyearmonth();
					MonthPresent monthPresent= monthPresentManager.findByMonthAndCode(yearmonth, counterCode);
					if(monthPresent!=null){
						monthPresentManager.excute(order.getOrderNo(), monthPresent);
					}
					
					// step3: 添加预售活动随单执行的产品
					order.setOrderPayTime(System.currentTimeMillis());
					orderService.saveReserveRecord(order.getOrderNo());
					
					// step4: 添加预售活动随单执行的产品
					if (!Order.TYPE_RESERVEACTIVITY.equals(order.getReserveFlag())) {
						orderService.addReserveRecord(order.getOrderNo());
					}
					
					//积分兑换
					redeemPointService.addToOrder(order);
					
					// step5:如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// final:生成包裹
					isSuccess = alipayService.checkSuccess(subject, total_fee, out_trade_no);
					//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
					//如果有做过处理，不执行商户的业务程序
				}else{
				  //处理合并支付
				    OrderService orderService = AppContext.getBean(OrderService.class);
					MonthPresentManager monthPresentManager = AppContext.getBean(MonthPresentManager.class);
					RedeemPointService redeemPointService  = AppContext.getBean(RedeemPointService.class);
					OrderCredentialsService credentialsService = AppContext.getBean(OrderCredentialsService.class);
					//去除combine字段
					out_trade_no = out_trade_no.substring(7);
					// 获取合并订单
					OrderCredentials orderCredentials = credentialsService.get(out_trade_no);
					List<String> orderNos = orderCredentials.getOrderNos();
					List<Order> orders = new ArrayList<Order>();
					for(String orderno:orderNos){
						Order order = orderService.getsingleOrder(orderno);
						orders.add(order);
					}
					// 根据订单号获取当前订单
					for(Order order:orders){
						String counterCode = order.getCounterCode();
						int yearmonth=monthPresentManager.getyearmonth();
						MonthPresent monthPresent= monthPresentManager.findByMonthAndCode(yearmonth, counterCode);
						if(monthPresent!=null){
							monthPresentManager.excute(order.getOrderNo(), monthPresent);
						}
						//step2.2.1 : 添加大礼包  2018/2/22王彬
						if(order.getOrderNo().equals(subject)){
							StockService stockService = AppContext.getBean(StockService.class);
							Map<Activity, Integer> act = orderService.getMergeOtherBigPackage(orders);
							for(Activity activity : act.keySet()){
								int suitNum = stockService.getSuitNum(activity.getContext().getActityExtra(),act.get(activity),order);
								if(suitNum>0){
								   orderService.AddBigPackageToOrder(order,activity.getActId(), suitNum);
								}
							}
						}
						
						// step3: 添加预售活动随单执行的产品
						order.setOrderPayTime(System.currentTimeMillis());
						orderService.saveReserveRecord(order.getOrderNo());
						
						// step4: 添加预售活动随单执行的产品
						if (!Order.TYPE_RESERVEACTIVITY.equals(order.getReserveFlag())) {
							orderService.addReserveRecord(order.getOrderNo());
						}
						
						//积分兑换
						redeemPointService.addToOrder(order);
					}
					// step5:如果没有做过处理，根据合并订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// final:生成包裹
					isSuccess = alipayService.combineCheckSuccess(total_fee, out_trade_no);
			  }
			  }
			} 
		  } catch (Exception e){
			  log(e.getMessage());
		  }
		
		 //——请根据您的业务逻辑来编写程序（以上代码仅作参考）
		 if(isSuccess){
			out.print("success");	//请不要修改或删除
		 } else {
			out.print("fail");	//请不要修改或删除
		 }
		 
	}else{//验证失败
		out.print("fail");
	}
%>