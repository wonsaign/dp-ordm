<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>订货页</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"> 
	<link href="../css/index.css?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"css/index.css").lastModified()%>" rel="stylesheet" />
	<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
	<style>
		@media screen and (max-width:510px) and (min-width: 400px) {
			.search{
			width:90%;
			}
			.text_search{
			width:70%;
			}
			.button_search{
			width:20%;
			}
			.class_nav{
			width:100%;
			}
			.series_class,.series,.series_many{
			font-size:10px;
			}
			.series li{
			 width:25%；
			}
		}
		.t_pro,.t_pre,.t_mat,.sort_series,.sort_price{
			cursor: pointer;
		}
	</style>
</head>
<body>
    <%@ include file="../common/commonHT.jsp"%>
    <div class="search">
		<input type="hidden" class="searchid" name="searchid">
		<div class="text_search"><input name="searchname" type="text" maxlength="20" placeholder="商品名称/条码/价格" autocomplete="off"></div>
		<div class="bg-red button_search"><input class="" type="button"  value="搜 &nbsp;索" ></div>
		<div class="clear"></div>
		 <div class="operationpanel1">
			<input type="hidden" name="counterId" value="">
			<input type="hidden" name="cartId" value="">
	        <span name="materialname"></span>	<strong><span name="materialfee"></span></strong>&nbsp;
	        <span name="fee" style="color:green"></span>&nbsp;
	        <span name="feeadd" style="color:green"></span>	<strong style="color:red;"><span name="totalfee"></span></strong><span style="color:green" name="feename"></span>
		 </div>
	</div>
	<div class="class_nav">
	<input type="hidden" value="findbytype.do" id="11386" class="propertyOfPage">
		<div class="class_block">
			<div class="series_class">系列</div>
			<input type="hidden" class="TypeId" value="11386">
			<input type="hidden" class="ajaxurl" value="findbyseries.do">
			<ul class="series">
				<c:forEach items="${productClass}" var="proclass">
					<input type="hidden" value="${proclass.hardCode}" >
					<li>${proclass.name}</li>
				</c:forEach>
				<div class="clear"><div>
			</ul>
			<div class="series_many">更多</div>
			<div style="clear:both;"></div>
		</div>
		<div class="class_block">
			<div class="series_class">品类</div>
			<input type="hidden" class="TypeId" value="11386">
			<input type="hidden" class="ajaxurl" value="findbybodytype.do">
			<ul class="series">
				<c:forEach items="${bodyType}" var="bt">
					<input type="hidden" value="${bt.hardCode}" >
					<li>${bt.name}</li>
				</c:forEach>
				<div class="clear"><div>
			</ul>
			<div class="series_many">更多</div>
			<div style="clear:both;"></div>
		</div>
		<div class="class_block">
			<div class="series_class">推荐</div>
			<input type="hidden" class="ajaxurl" value="findbyptype.do">
			<ul class="series">
				<c:forEach items="${productType}" var="pt">
					<input type="hidden" value="${pt.hardCode}" >
					<li>${pt.name}</li>
				</c:forEach>
				<div class="clear"><div>
			</ul>
			<div class="series_many">更多</div>
			<div style="clear:both;"></div>
		</div>
		<div class="class_block">
			<div class="series_class">赠品</div>
			<input type="hidden" class="TypeId" value="11388">
			<input type="hidden" class="ajaxurl" value="findbyseries.do">
			<ul class="series">
				<c:forEach items="${presents}" var="present">
					<input type="hidden" value="${present.hardCode}" >
					<li>${present.name}</li>
				</c:forEach>
				<div class="clear"><div>
			</ul>
			<div class="series_many">更多</div>
			<div style="clear:both;"></div>
		</div>
		<div class="class_block">
			<div class="series_class">物料</div>
			<input type="hidden" class="TypeId" value="11389">
			<input type="hidden" class="ajaxurl" value="findbyseries.do">
			<ul class="series">
				<c:forEach items="${materiels}" var="materiel">
					<input type="hidden" value="${materiel.hardCode}" >
					<li>${materiel.name}</li>
				</c:forEach>
				<div class="clear"><div>
			</ul>
			<div class="series_many">更多</div>
			<div style="clear:both;"></div>
		</div>
	</div>
	<div class="mobile_hidden">
		<div class="mobile_nav">
			<div class="mobile_series">系列</div>
			<div class="mobile_series">品类</div>
			<div class="mobile_series">推荐</div>
			<div class="mobile_series">赠品</div>
			<div class="mobile_series">物料</div>
			<div class="clear"></div>
		</div>
		<div class="mobile_class class_block"></div>
	</div>
	<div class="center">
		<div class="sort">
		<%-- 
			<span  class="sort_series" id="global">全国销量</span>
			<span  class="sort_series" id="local" >本店销量</span>
			<input type="hidden" class="TypeId" value="">
			<input type="hidden" class="sorturl" value="findbyptype.do">
			<span class="sort_price" id="1" >价格↑</span>
			<input type="hidden" id="sort_hardCode">
		--%>
		</div>
		<div class="show_item">
		<c:if test="${lgroups!=null}">
			<c:forEach items="${lgroups}" var="act">
				<form class="addcart" action="xxx" method="post">
				<a href="../product/activity_${act.actId}.do">
					<div class="item">
						<div class="item_number">
						<c:if test="${act.imageURL==null}">
							<div class="mobile_img"><img class="item_img" src="../upload/default/zhengpin.png"></div>
						</c:if>
						<c:if test="${act.imageURL!=null}">
							<div class="mobile_img"><img class="item_img" src="${act.imageURL }"></div>
						</c:if>
							<div class="mobile_block">
								<span  class="goods_name">${act.actName}</span>
								<input type="hidden" name="productId" value="${act.actId }">
							</div>
						</div>										
					</div>
				</a>
				</form>
			</c:forEach>
		</c:if>
		<c:if test="${products!=null}">
			<c:forEach items="${products}" var="product">
				<c:if test="${product.avalible }">
					<form class="addcart" action="" method="post">
						<div class="item">
							<div class="item_number">
								<div class="mobile_img">
									<a target="_blank" href="../product/detail_${product.productId}.do">
										<img class="item_img" src="${product.imageURL}">
									</a>
									<c:if test="${product.prePro==0}">
											<c:if test="${product.inv[stockId]<=300 && product.inv[stockId]>0}">
												<div class="inventory_inv" title="${product.inv[stockId]}">库存:${product.inv[stockId]}</div>
											</c:if>
											<c:if test="${product.inv[stockId]<=0}">
												<div class="inventory" title="${product.inv[stockId]}">暂时无货</div>
											</c:if>
									</c:if>
									<c:if test="${product.prePro==3}">
											<div class="inventory">打欠结束</div>
									</c:if>
									<input class='actFlag' type='hidden' value="${ product.actFlag}">
									<input class='actItself' type='hidden' value="${ product.actItself}">
								</div>
								<div class="mobile_block">
									<span  class="goods_name">${product.name}</span>
									<div class="retail_price">零售价：￥${product.retailPrice}</div>
									<div class="member_price">会员价：￥${product.memberPrice}</div>
									<div class="member_price">本月销售：${product.qty}</div>
									<input type="hidden" name="productId" value="${product.productId }">
									<div class="suit_button">
										<div class="button_block">
											<input name="productId" value="${product.productId }" type="hidden" >
											<a class="min" type="button">-</a>
											<input name="num" type="number" class="text_box" style="font-size:14px" min="${product.minUnit}" step="${product.minUnit}" value="${product.minUnit}" />
											<a class="add" type="button">+</a>
											<div class="clear"></div>
										</div>
										<c:if test="${product.prePro==0}">
												<c:if test="${product.inv[stockId]<=0}">
													<input class="bg-red add_button" type="submit" value="购物车" disabled="disabled" style="background: #999;">
												</c:if>
												<c:if test="${product.inv[stockId]>0}">
													<input class="bg-red add_button" type="submit" value="购物车">
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
										<div class="clear"></div>
									</div>
								</div>
							</div>										
						</div>
					</form>
				</c:if>
			</c:forEach>
		</c:if>
		</div>
		<div class="clear"></div>	
	</div>
	<%@ include file="common/commonF.jsp"%>	
	<script src="../js/action.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/action.js").lastModified() %>"></script>
	<script src="../js/jquery.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/jquery.js").lastModified() %>"></script>
	<script src="../js/index.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/index.js").lastModified()%>"></script>		
	<script src="../js/product.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/product.js").lastModified()%>"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		initpage();
		initadddel();
		$(".item").each(function(){
/* 			if($(this).find(".inventory").attr("title")<=0||$(this).find(".inventory").attr("title")=="undefined"){
				$(this).find(".inventory").show();
			}; */
			if($(this).find(".actFlag").val()=="true"){
					$(this).find("input[name='num']").attr("readonly","readonly");
					$(this).find(".add_button").attr("disabled",true);
					$(this).find(".add_button").css("background","rgb(159, 159, 157)");
					$(this).find(".mobile_img").append("<img class='top_imgy' src='../img/top_imgy.png'>");
			}
		});
	 	$(".submit_order").click(function(){
			$(".message_block2").html($(".address_body").html()+"<p style='font-weight:bold;color:red'>请务必参照可配物料赠品金额，在此系统选择赠品及物料，未选择将视为放弃，后期不予补发！</p>");
			var counterId = $('input[name="counterId"]').val();
			var cartId= $('input[name="cartId"]').val();	 		
	 		$.post("../cart/getpresentprice.do",{cartId:cartId,counterId:counterId},function(data){
	 			for(var key in data){
	 				if(key=="materialfee"){
	 					var materialprice=Number(data[key]).toFixed(2);
	 					if(materialprice>=0){
	 						$('span[name="materialname"]').text("超出物料金额:");
	 						$('span[name="materialfee"]').text(materialprice);
	 						$('span[name="materialname"]').css("color","red");
	 						$('span[name="materialfee"]').css("color","red");
	 					}else{/* if(materialprice<0) */
	 						$('span[name="materialname"]').text("可配物料金额:");
	 						$('span[name="materialfee"]').text(-materialprice);
	 						$('span[name="materialname"]').css("color","green");
	 						$('span[name="materialfee"]').css("color","green");
	 					}/* else{
	 						$('span[name="materialname"]').text("可配物料金额:");
	 						$('span[name="materialfee"]').text(materialprice);
	 						$('span[name="materialname"]').css("color","red");
	 						$('span[name="materialfee"]').css("color","red");
	 					} */
	 				}
	 				if(key=="lqtys"){
	 					$('span[name="qty"]').text( data[key][0]);
	 					$('span[name="productqty"]').text(data[key][1]);
	 					$('span[name="materialqty"]').text(data[key][2]);
	 					$('span[name="totalfee"]').text(data[key][3]);
	 					//校验submit_order按钮是否可用，条件是物料数量是否为0
	 				 	if(data[key][2]==0){
	 				 		$(".message_body").show();
	 						$(".message_block2").html("<div>赠品&物料数量不能为<b style='color:red; font-size:16px;'>0</b>!</div><div>您还有可配物料<b style='color:red; font-size:16px;'>" + -materialprice +"</b>,请您选择赠品&物料数后再进行提交.</div>");
	 					}
	 				}
	 			}
	 		})
			$(".message_body").show();
			$(".address_body").hide();
			$('button[name="savecart"]').click(function(){
				var counterId = $('input[name="counterId"]').val();
				var cartId= $('input[name="cartId"]').val();
				if(counterId!=null && counterId!=0){
					$.ajax({
			 	 		type : "get",
			 	 		url : "../cart/cartcommit.do",
			 	 		async : false,
			 	 		timeout : 15000,
			 	 		dataType:"text",
			 	 		cache:false,
			 	 	    data:{counterId:counterId,cartId:cartId},
			 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
			 	 			$(".message_block2").html(errorThrown+"提交失败!");
			 				$(".message_body").show();
			 	 		},
			 	 		success : function(data) {
			 	 			window.location.href="../ordm/index.do";
						}
			 	 	});  
				}
			});
		});
	})
	
	</script>
</body>
</html>
