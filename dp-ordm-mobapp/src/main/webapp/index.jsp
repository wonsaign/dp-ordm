<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.zeusas.core.utils.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>

<html>
	<head>
		<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no;charset=utf-8" >
		<title>首页</title>
		<link href="../css/index.css" rel="stylesheet">
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
	</head>
	<body>
		<%@ include file="../common/commonHT.jsp"%>
		<div class="center">
		<div class="index_nav">
			<table cellspacing="10">
			<tr>
			<c:forEach items="${productBean}" var="pb">	
				<td id="${pb.productType.hardCode }">${pb.productType.name }</td>
			</c:forEach>
			</tr>
			</table>
		</div>
		<c:forEach items="${productBean}" var="pb">	
		<%-- 新品 --%>
			<c:if test="${pb.productType.hardCode==1}">
				<div class="tactics_item tactics${pb.productType.hardCode}">
					<div class="new_item">
						<c:forEach items="${pb.products}" var="product">
								<div class="index_item">
								<a href="${pageContext.request.contextPath}/product/detail_${product.productId}.do">
									<img class="item_index_img" src="${product.imageURL}">
								</a>
								<div class="index_div">
									<div  class="goods_name1">${product.name }</div>
									<div class="one_price">零售价：￥${product.retailPrice }</div>
									<div class="two_price">会员价：￥${product.memberPrice }</div>
								</div>
								</div>
						</c:forEach>
						<div class="clear"></div>
					</div>
				</div>
			</c:if>
			<%-- 集客 --%>	
			<%-- <c:if test="${pb.productType.hardCode==5}">
				<div class="tactics_item">
					<div class="new_item">
						<c:if test="${fn:length(pb.products)!=0 }">
						<div class="item_title"><a href="${pageContext.request.contextPath}/product/product_${pb.productType.hardCode }.do">${pb.productType.name }</a><a class="more" href="${pageContext.request.contextPath}/product/product_${pb.productType.hardCode}.do" style="float: right;"><img alt="" src="../img/more.jpg" width="20px" height="20px"></a></div>
						</c:if>
						<c:forEach items="${pb.products}" var="product" begin="0" end="4">
							<form class="addcart" action="" method="post">
								<div class="item">
						
									<div class="item_number">
									<a href="${pageContext.request.contextPath}/product/detail_${product.productId}.do">
										<img class="item_img" src="${product.imageURL}">
									</a>	
										<span  class="goods_name">${product.name }</span>
										<div class="retail_price">零售价：￥${product.retailPrice }</div>
										<div class="member_price">会员价：￥${product.memberPrice }</div>
									</div>
								</div>
							</form>
						</c:forEach>
						<div class="clear"></div>
					</div>
				</div>
			</c:if> --%>
			<%-- 优惠活动 --%>	
			<c:if test="${pb.productType.hardCode==4}">
				<div class="tactics_item tactics${pb.productType.hardCode}" id="act">
					<div class="new_item">
						<c:forEach items="${pb.activity}" var="act" >
								<div class="index_item">
									<a href="${pageContext.request.contextPath}/product/activity_${act.actId }.do">
										<c:if test="${act.image==null }">
											<img class="item_index_img" src="upload/default/xilie.jpg">
										</c:if>
										<img class="item_index_img" src="<%=AppConfig.getVfsPrefix()%>${act.image }">
									</a>
									<div class="index_div">  
										<span  class="goods_name1">${act.name }</span>
										<div  class="one_price"><span class="start">${act.start}</span>/<span class="end">${act.to}</span></div><br/>						
									</div>
								</div>
						</c:forEach>
						<div class="clear"></div>
					</div>
				</div>
			</c:if>
			<%-- 全国畅销 --%>	
			<c:if test="${pb.productType.hardCode==2}">
				<div class="tactics_item tactics${pb.productType.hardCode}">
					<div class="new_item">
						<c:forEach items="${pb.series}" var="series">
								<div class="index_item">
									<%-- <a href="${pageContext.request.contextPath}/product/${series.hardCode }.do"><img class="item_img" src="upload/productGroup/${series.imageURL }"></a> --%>
									<a href="${pageContext.request.contextPath}/product/series_${series.hardCode }.do">
										<img class="item_index_img" src="<%=AppConfig.getVfsPrefix() %>${series.url }">
									</a>
									<span  class="goods_namecenter">${series.name }</span>						
								</div>
						</c:forEach>
						<div class="clear"></div>
					</div>
				</div>
			</c:if>	
			<%-- 本地畅销 --%>	
			<c:if test="${pb.productType.hardCode==3}">
				<div class="tactics_item tactics${pb.productType.hardCode}">
					<div class="new_item">
						<div class="localtag">
						<c:forEach items="${pb.series}" var="series">
								<div class="index_item">
										<%-- <a href="${pageContext.request.contextPath}/product/${series.hardCode }.do"><img class="item_img" src="upload/productGroup/${series.imageURL }"></a> --%>
										<a href="${pageContext.request.contextPath}/product/series_${series.hardCode }.do">
											<img class="item_index_img" src="<%=AppConfig.getVfsPrefix() %>${series.url }">
										</a>  
										<span class="goods_namecenter">${series.name }</span>				
								</div>
						</c:forEach>
						</div>
						<div class="clear"></div>
					</div>
				</div>
			</c:if>	
		</c:forEach>
	</div>
	<%@ include file="common/commonF.jsp"%>
<script src="../js/jquery.js"></script>
<script src="../js/index.js"></script>
<script type="text/javascript">
 	$(function(){
		/* if($(window).width()<1000){
	 		var ith=$(".item").width()+10;
			var wm=Math.floor($(window).width()/ith);
		 	$(".center").width(ith*wm);
		}
		else{
			$(".center").width(1000);
		}; */
		$(window).resize(function(){
			if($(this).width()<1000){
				var ith=$(".item").width()+10;
				var ww=Math.floor($(this).width()/ith);
			 	$(".center").width(ith*ww);
			}else{
				$(".center").width(1000);
			}
		});
		$(".start").each(function(){
			var start=$(this).text();
			$(this).text(start.substr(0,10));
		});
		$(".end").each(function(){
			var end=$(this).text();
			$(this).text(end.substr(0,10));
		});
		$(".index_nav table td").eq(0).addClass("nav_in");
		$(".tactics3").show();
		$(".index_nav table td").click(function(){
			$(this).addClass("nav_in");
			$(this).siblings().removeClass("nav_in");
			var i=$(this).attr("id");
			$(".tactics"+i).show();
			$(".tactics_item").not(".tactics"+i).hide();
		})
		$(window).scroll(function(){
			var h=$(window).scrollTop();
			if(h>45){
				$('.index_nav').css({'position':'fixed','top':'0px'});
			}else{
				$('.index_nav').css({'position':'static'});
			}
		})
	});
</script>
</body>
<%-- <%if("1".equals(s.getAttribute("flogin"))){
	s.setAttribute("flogin", "2");
%>
<script type="text/javascript">
    $(".message_body").show();
    $(".message_block2").html($('.message_hide').html())
</script>
<%}%> --%>
</html>
