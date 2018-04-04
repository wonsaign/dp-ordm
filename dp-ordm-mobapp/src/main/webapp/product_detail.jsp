<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.zeusas.core.utils.*" %>
<html>
	<head>
		<link href="../css/index.css" rel="stylesheet">
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
		<script src="../js/jquery.js"></script>
		<script src="../js/action.js"></script>
		<script src="../js/index.js"></script>
		<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no;charset=utf-8" > 
		<title>产品详情</title>
	</head>
	<body>
		<%@ include file="../common/commonHT.jsp"%>
		<div class="center">	
			<form class="addcart" action="" method="post">
			<div class="suit_shopping">
				<div class="suit_goods">
					<img class="suit_img" src="${product.imageURL }" class="file_img">
					<input class='actItself' type='hidden' value="${ product.actItself}">	
					<div class="inventory1" title="${product.inv[stockId]}">暂时无货</div>
				</div>
				<div class="suit_change">
					<c:if test="${product.typeId==11386 }">
						<div class="suit_details">
								<div class="detail_name">${product.name }</div>
							  <div class="detail_number">零售价:<span class="detail_num">￥${product.retailPrice }</span></div>
								<div class="detail_number">会员价:<span class="detail_num">￥${product.memberPrice }</span></div>
								<div class="detail_number">规格:<span class="detail_num">${product.specifications }</span></div>
						    <div class="price">
									<c:if test="${product.description !=null}">
								   	 	描述:${product.description }
								 	</c:if> 	
						    </div>
						</div>							 
					</c:if>
					<c:if test="${product.typeId==11389||product.typeId==11388 }">
						<div class="suit_details">
							<div class="detail_name">${product.name }</div>
						  <div class="detail_number">价格:<span class="detail_num">￥${product.materialPrice }</span></div>
							<div class="detail_number">规格:<span class="detail_num">${product.specifications }</span></div>
					    <div class="price">描述:${product.description }	</div>
						</div>							 
					</c:if>
				</div>
				<div class="clear"></div>
				<div class="suit_button">
					<div class="button_block">
						<input name="productId" value="${product.productId }" type="hidden" >
						<a class="min" type="button">-</a>
						<input name="num" type="number" class="text_box" min="${product.minUnit}" step="${product.minUnit}" value="${product.minUnit}" />
						<a class="add" type="button">+</a>
						<div class="clear"></div>
					</div>
					<input class="bg-green add_button" type="submit" value="加入购物车">
					<div class="clear"></div>
				</div>
				<div class="new_item">
					<c:if test="${activities!=null }">
					<div class="item_title">相关活动</div>
						<c:forEach items="${ activities}" var="act">
							<div class="item">
								<div class="item_number">
									<a href="../product/activity_${act.actId }.do">
										<img class="item_img" src="<%=AppConfig.getVfsPrefix()%>${act.image }">
										<input class='actFlag' type='hidden' value="${ product.actFlag}">
									</a>  
									<span  class="goods_name">${act.name}</span>
									<div  class="retail_price"><span class="start">${act.start}</span>/<span class="end">${act.to}</span></div><br/>
								</div>							
							</div>
						</c:forEach>
					</c:if>	
					<div class="clear"></div>
				</div>
			</div>
			</form>
			<div>
				<c:forEach items="${additionalProduct }" var="aP">
					<div class="item">
						<div class="item_number">
								<c:if test="${aP.product.imageURL!=null}">
									<div class="mobile_img"><img width="186px" height="200px" src="${aP.product.imageURL}" ></div>
									<input class='actFlag' type='hidden' value="${ product.actFlag}">
								</c:if>
								<div class="mobile_block">
									<span  class="goods_name">${aP.product.name}</span>
									<div class="retail_price">${aP.product.typeName}</div>
									<div class="member_price">数量:${aP.count}</div>
									<input type="hidden" name="productId" value="${aP.product.productId }">
									<div class="suit_button"><div class="clear"></div></div>
								</div>
						</div>
					</div>										
				</c:forEach>
			</div>
		</div>
		<%@ include file="common/commonF.jsp"%>
	<script type="text/javascript">
		$(document).ready(function(){
			initpage();
			initadddel();
			if($(".inventory1").attr("title")<=0){
				$(".inventory1").show();
			};
			if($(".actItself").find(".actItself").val()=="true"){
				$(".suit_shopping").find("input[name='num']").attr("readonly","readonly");
				$(".suit_shopping").find(".add_button").attr("disabled",true);
				$(".suit_shopping").find(".add_button").css("background","rgb(159, 159, 157)");
			};
			$(".item").each(function(){
				if($(this).find(".actFlag").val()=="true"){
					$('.item_number').append("<img class='top_imgy' src='../img/top_imgy.png'>");
			}
			});
			$(".back_button").click(function(){
				window.opener=null;
				window.open('','_self');
				window.close();
			});
			$(".start").each(function(){
				var start=$(this).text();
				$(this).text(start.substr(0,10));
			});
			$(".end").each(function(){
				var end=$(this).text();
				$(this).text(end.substr(0,10));
			});
		});
 	
	</script>
	</body>
	
	
</html>
