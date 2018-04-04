<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.zeusas.core.utils.*" %>
<html>
	<head>
		<link href="../css/index.css?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"css/index.css").lastModified()%>" rel="stylesheet" />
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
		<script src="../js/action.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/action.js").lastModified() %>"></script>
		<script src="../js/jquery.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/jquery.js").lastModified() %>"></script>
		<script src="../js/index.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/index.js").lastModified()%>"></script>
		<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no;charset=utf-8" > 
		<title>产品详情页</title>
	</head>
	<body>
		<%@ include file="../common/commonHT.jsp"%>
		<div class="center">	
			<form class="addcart" action="" method="post">
			<div class="suit_shopping">
				<div class="suit_goods">
					<img class="suit_img" src="${product.imageURL }" class="file_img">
					<input class='actItself' type='hidden' value="${ product.actItself}">	
					<input type="hidden" value="${product.prePro}" class="prePro">
					<c:if test="${product.prePro==0}">
							<c:if test="${product.inv[stockId]<=300 && product.inv[stockId]>0}">
								<div class="inventory_inv1" title="${product.inv[stockId]}">库存:${product.inv[stockId]}</div>
							</c:if>
							<c:if test="${product.inv[stockId]<=0}">
								<div class="inventory1" title="${product.inv[stockId]}">暂时无货</div>
							</c:if>
					</c:if>
					<c:if test="${product.prePro==3}">
							<div class="inventory1">打欠结束</div>
					</c:if>
				</div>
				<div class="suit_change">
					<c:if test="${product.typeId==11386 }">
						<div class="suit_details">
							<b><span style="display:block;">${product.name }</span></b>
							<div class="price">
								<c:if test="${product.description !=null}">
							   	 	描述:${product.description }
							   	</c:if> 	
						    </div>
						    <div>
							    <span>零售价:<span class="red" style="font-size:20px;">￥<i>${product.retailPrice }</i></span></span>
								<span>会员价:<span class="red" style="font-size:20px;">￥<i>${product.memberPrice }</i></span></span>
								<span>规格:<span class="red" style="font-size:18px;"><i>${product.specifications }</i></span></span>
						    </div>
						</div>		
								 
						<div class="suit_button">
							<div class="button_block">
								<input name="productId" value="${product.productId }" type="hidden" >
								<a class="min" type="button">-</a>
								<input name="num" type="number" class="text_box" min="${product.minUnit}" step="${product.minUnit}" value="${product.minUnit}" />
								<a class="add" type="button">+</a>
								<div class="clear"></div>
							</div>
							<c:if test="${product.prePro==0}">
									<c:if test="${product.inv[stockId]<=0}">
										<input class="bg-red add_button" type="submit" value="购物车" disabled="disabled" style="background: #999;">
									</c:if>
									<c:if test="${product.inv[stockId]>0}">										
										<c:if test="${product.avalible==false}">
										<input class="bg-red add_button" type="submit" value="购物车" disabled="disabled" style="background: #999;">
										</c:if>
										<c:if test="${product.avalible==true && isAvailableProduct==true}">
											<input class="bg-red add_button" type="submit" value="购物车">
										</c:if>			
										<c:if test="${product.avalible==true && isAvailableProduct!=true}">
											<input class="bg-red add_button" type="submit" value="购物车" disabled="disabled" style="background: #999;">
										</c:if>									
									</c:if>
							</c:if>
							<c:if test="${product.prePro==1}">
									<c:if test="${product.avalible==false}">
									<input class="bg-red add_button" type="submit" value="购物车" disabled="disabled" style="background: #999;">
									</c:if>
									<c:if test="${product.avalible==true}">
									<input class="bg-red add_button" type="submit" value="购物车">
									</c:if>
							</c:if>
							<c:if test="${product.prePro==2}">
								<c:if test="${product.avalible==false}">
								<input class="bg-red add_button" type="submit" value="购物车" disabled="disabled" style="background: #999;">
								</c:if>
								<c:if test="${product.avalible==true}">
								<input class="bg-red add_button" type="submit" value="打欠中"  style="background: #6dbb44; border-color: #6dbb44;">
								</c:if>
							</c:if>
							<c:if test="${product.prePro==3}">
								<c:if test="${product.avalible==false}">
								<input class="bg-red add_button" type="submit" value="购物车" disabled="disabled" style="background: #999;">
								</c:if>
								<c:if test="${product.avalible==true}">
								<input class="bg-red add_button" type="submit" value="还欠中" disabled="disabled" style="background: #999;">
								</c:if>
							</c:if>
							<input class="bg-red back_button" type="button" value="返回">
							<div class="clear"></div>
						</div>
					</c:if>
					<c:if test="${product.typeId==11389||product.typeId==11388 }">
						<div class="suit_details">
							<span style="display:block;">${product.name }			</span>
						    <span class="price">描述:${product.description }</span>
						    <span>价格:￥${product.materialPrice }</span>
							<span>规格:${product.specifications }</span>
						</div>							 
						<div class="suit_button">
							<div class="button_block">
								<input name="productId" value="${product.productId }" type="hidden" >
								<a class="min" type="button">-</a>
								<input name="num" type="number" class="text_box"  min="${product.minUnit}" step="${product.minUnit}" value="${product.minUnit}" />
								<a class="add" type="button">+</a>
								<div class="clear"></div>
							</div>
							<c:if test="${product.prePro==0}">
									<c:if test="${product.inv[stockId]<=0}">
										<input class="bg-red add_button" type="submit" value="购物车" disabled="disabled" style="background: #999;">
									</c:if>
									<c:if test="${product.inv[stockId]>0}">
										<c:if test="${product.avalible==false}">
											<input class="bg-red add_button" type="submit" value="购物车" disabled="disabled" style="background: #999;">
										</c:if>
										<c:if test="${product.avalible==true}">
										<input class="bg-red add_button" type="submit" value="购物车">
										</c:if>
									</c:if>
							</c:if>
							<c:if test="${product.prePro==1}">
									<input class="bg-red add_button" type="submit" value="购物车">
							</c:if>
							<c:if test="${product.prePro==2}">
							<input class="bg-red add_button" type="submit" value="打欠中" style="background: #6dbb44; border-color: #6dbb44;">
							</c:if>
							<c:if test="${product.prePro==3}">
							<input class="bg-red add_button" type="submit" value="还欠中" disabled="disabled" style="background: #999;">
							</c:if>
							<input class="bg-red back_button" type="button" value="返回">
							<div class="clear"></div>
						</div>
					</c:if>
				</div>
				<div class="clear"></div>
				<div>
				<div>
					<div class="new_item">
						<c:if test="${activities!=null }">
						<div class="item_title"><a href="#">相关活动</a><a class="more" href="#" style="float: right;"><img alt="" src="../img/more.jpg" width="20px" height="20px"></a></div>
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
									<div class="suit_button">
									<div class="clear"></div>
								</div>
							</div>
						</div>										
					</div>
				</c:forEach>
				<div class="clear"></div>
			</div>
		</div>
		<%@ include file="common/commonF.jsp"%>
	<script type="text/javascript">
		$(document).ready(function(){
			initpage();
			initadddel();
			/* if($(".inventory1").attr("title")<=0||$(".inventory1").attr("title")=="undefined"){
				$(".inventory1").show();
			}; */
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

