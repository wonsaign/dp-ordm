<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no;charset=utf-8">
		<title></title>
		<link href="../css/index.css" rel="stylesheet">	
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">	
	</head>
	<body>
		<%@ include file="../common/commonHT.jsp"%>
	    <div class="center">
	        <div class="pay_order_block">您的订单已经支付完成，请您注意收货</div>
	        <div class="pay_order_info">
				<div>订单号：</div>
				<div>柜台号：</div>
				<div>收货地址：</div>
			</div>
			<div class="pay_go">
				<div class="pay_go_order"><a href="${pageContext.request.contextPath}/ordm/index.do">查看订单</a></div>
				<div class="pay_go_index"><a href="${pageContext.request.contextPath}/order/index.do">继续购物</a></div>
				<div class="clear"></div>
			</div>
		</div>
		<%@ include file="common/commonF.jsp"%>
	 	<script src="../js/jquery.js"></script>
		<script src="../js/index.js"></script>
	</body>
</html>
