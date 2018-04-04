<%@page import="com.zeusas.core.http.HttpUtil"%>
<%@page import="com.zeusas.security.auth.entity.AuthUser"%>
<%@page import="com.zeusas.core.utils.AppContext"%>
<%--
/* *
 功能：支付宝页面跳转同步通知页面
 版本：3.2
 日期：2011-03-17
 说明：
 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 //***********页面功能说明***********
 该页面可在本机电脑测试
 可放入HTML等美化页面的代码、商户业务逻辑程序代码
 TRADE_FINISHED(表示交易已经成功结束，并不能再对该交易做后续操作);
 TRADE_SUCCESS(表示交易已经成功结束，可以对该交易做后续操作，如：分润、退款等);
 //********************************
 * */
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.alipay.util.*"%>
<%@ page import="com.alipay.config.*"%>
<%@ page import="com.zeusas.dp.ordm.entity.Order"%>
<%@ page import="com.zeusas.dp.ordm.service.OrderService"%>
<%@ page import="com.zeusas.dp.ordm.entity.AliPayment"%>
<%@ page import="com.zeusas.dp.ordm.service.AliPaymentService"%>


<html>
  <head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>支付宝页面跳转同步通知页面</title>
		<style type="text/css">
			a{
				text-decoration:none;
			}
			a:link{
				text-decoration:none;
			}
			a:visited{
				text-decoration:none;
			}
			a:hover{
				text-decoration:none;
			}
			a:active{
				text-decoration:none;
			}
		</style>
		<link href="../css/index.css" rel="stylesheet">		
  </head>
  <body>
<%
	//获取支付宝GET过来反馈信息
	Map<String,String> params = new HashMap<String,String>();
	Map<String,String[]> requestParams = request.getParameterMap();
	for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
		String name = (String) iter.next();
		String[] values = (String[]) requestParams.get(name);
		String valueStr = "";
		for (int i = 0; i < values.length; i++) {
	valueStr = (i == values.length - 1) ? valueStr + values[i]
	: valueStr + values[i] + ",";
		}
		//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
		//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
		params.put(name, valueStr);
	}
	
	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
	String total_fee = request.getParameter("total_fee")==null?request.getParameter("total_fee"):new String(request.getParameter("total_fee").getBytes("ISO-8859-1"),"UTF-8");
	String trade_no = request.getParameter("trade_no")==null?request.getParameter("trade_no"):new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
	String out_trade_no = request.getParameter("out_trade_no")==null?request.getParameter("out_trade_no"):new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
	
	//计算得出通知验证结果
	boolean verify_result = AlipayNotify.verify(params);
	if(verify_result){//验证成功
		try {
	%>
			<%@ include file="../common/commonHT.jsp"%>
		    <div class="center">
		        <div class="pay_order_block">您的订单已经支付完成，请您注意收货</div>
		        <div class="pay_order_info">
					<div>订单号：<%=out_trade_no %></div>
					<div>支付金额：<%=total_fee%></div>
				</div>
				<div class="pay_go">
					<div class="pay_go_order"><a href="/dp-ordm/product/product.do">继续购物</a></div>
					<div class="pay_go_index"><a href="/dp-ordm/order/index.do">查看订单</a></div>
					<div class="clear"></div>
				</div>
			</div>
			<%@ include file="../common/commonF.jsp"%>
			<script src="../js/jquery.js"></script>
			<script src="../js/index.js"></script>	
	<%

		} catch (Exception e) {
			log(e.getMessage());
		}
		//////////////////////////////////////////////////////////////////////////////////////////
	%>
	<%
	}else{
	%>		
		<%@ include file="../common/commonHT.jsp"%>
 		 <div class="center">
		        <div class="pay_order_block"><b style="color:red;"><i>金额</i></b>或<b style="color:red;"><i>单号</i></b>错误，请联系总部财务确认。</div>
		        <div class="pay_order_info">
					<div>订单号：BXX<%=out_trade_no %></div>
					<div>支付金额：<%=total_fee%></div>
				</div>
				<div class="pay_go">
					<div class="pay_go_index"><a href="/dp-ordm/order/index.do">查看订单</a></div>
					<div class="pay_go_order"><a href="/dp-ordm/login.do">返回首页</a></div>
					<div class="clear"></div>
				</div>
		</div>
		<%@ include file="../common/commonF.jsp"%>
		<script src="../js/jquery.js"></script>
		<script src="../js/index.js"></script>
<% 		
	}
%> 
  </body>
</html>