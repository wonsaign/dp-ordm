<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.zeusas.core.utils.*"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>
订单列表</title>
<link href="../css/index.css" rel="stylesheet">
<link href="../css/style.css" rel="stylesheet">
<link href="../css/ssi-uploader.css" rel="stylesheet">
<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
<meta name="viewport"
	content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<style>
	body{
	background:#f3f3f3;
	}
</style>
</head>
<body>
	<%@ include file="../common/commonHT.jsp"%>
	<div>
	<c:forEach items="${carts}" var="cart">
	<div class="cart_list">
			<div class="col-333">用户名:<span class="col-666">${cart.userName }</span></div>
			<div class="col-333">柜台号:<span class="col-666">${cart.counterName }</span></div>
			<div class="col-333">下单时间:<span class="col-666 creatTime">${cart.lastUpdate }</span></div>
			<a href="../cart/cart_managedetail.do?cartId=${ cart.cartId}">
				<div class="text_right">查看详情&nbsp;》</div>
			</a>
	</div>
	</c:forEach>
	<c:forEach items="${orders}" var="order">
	<div class="cart_list">
			<div class="col-333">订单号:<span class="col-666">${order.userName }</span></div>
			<div class="col-333">柜台号:<span class="col-666">${order.counterName }</span></div>
			<div class="col-333">下单时间:<span class="col-666 creatTime">${order.orderCreatTime }</span></div>
			<a href="../order/detail.do?orderId=${order.id }">
				<div class="text_right">查看详情&nbsp;》</div>
			</a>
	</div>
	</c:forEach>
	<!-- <a class='intent_a' href='../cart/cart_managedetail.do?cartId="+item.cartId+"'><ul class='intent_ol'><li class='intent_li1'></li>
	 	 								+"<li class='intent_li4'>" +item.userName+"</li><li class='intent_li4'>"
	 	 								+ showTime(new Date(item.lastUpdate))
	 	 								+"</li><li class='intent_li4'>"+item.counterName
	 	 								+"</li><div class='clear'></div></ul></a>
	</div> -->
	<%@ include file="common/commonF.jsp"%>
	<script src="../js/jquery.js"></script>
	<script src="../js/action.js"></script>
	<script src="../js/index.js"></script>
	<script src="../js/ssi-uploader.js"></script>
	<script type="text/javascript">
		var href=location.href;
		if(href.indexOf("typeId=1")>0){
			$(document).attr('title','待审核订单列表');
			$(".icon1_text").text("待审核订单列表");
		}
		if(href.indexOf("typeId=2")>0){
			$(document).attr('title','待付款订单列表');
			$(".icon1_text").text("待付款订单列表");
		}
		if(href.indexOf("typeId=3")>0){
			$(document).attr('title','已付款订单列表');
			$(".icon1_text").text("已付款订单列表");
		}
		if(href.indexOf("typeId=6")>0){
			$(document).attr('title','已执行推送订单列表');
			$(".icon1_text").text("已执行推送订单列表");
		}
		if(href.indexOf("typeId=7")>0){
			$(document).attr('title','物流已发货订单列表');
			$(".icon1_text").text("物流已发货订单列表");
		}
		if(href.indexOf("typeId=10")>0){
			$(document).attr('title','财务退回订单列表');
			$(".icon1_text").text("财务退回订单列表");
		}
		if(href.indexOf("typeId=0")>0){
			$(document).attr('title','无效订单列表');
			$(".icon1_text").text("无效订单列表");
		}
		$(".creatTime").each(function(){
			var creatTime=$(this).text();
			$(this).text(showTime(new Date(Number(creatTime))))
		})
	</script>
</body>
</html>
