<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>订货</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"> 
	<link href="../css/index.css" rel="stylesheet" />
	<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
	<style>
		.center{
		    padding-bottom: 150px;
		}
	</style>
</head>
<body>
    <%@ include file="../common/commonHT.jsp"%>
    <div class="search">
			<input type="hidden" class="searchid" name="searchid"">
			<input class="text_search" name="searchname" type="text" maxlength="20" placeholder="商品名称/条码/价格" autocomplete="off">
			<input class=" bg-red button_search" type="button"  value="搜索" >
			<div class="operationpanel1">
				<input type="hidden" name="counterId" value="">
				<input type="hidden" name="cartId" value="">
        <div class="user_left1"><span name="materialname"></span>	<span name="materialfee"></span></div>
        <div class="user_left1"><span name="fee"></span></div>
        <div class="user_left1"><span name="feeadd"></span><span name="totalfee"></span><span name="feename"></span></div>
				<div class="clear"></div>
			</div>
		</div>
	<div class="class_nav">
	<input type="hidden" value="findbytype.do" id="11386" class="propertyOfPage">
		<div class="class_block">
			<input type="hidden" class="TypeId" value="11386">
			<input type="hidden" class="ajaxurl" value="findbyseries.do">
			<ul class="series">
				<c:forEach items="${productClass}" var="proclass">
					<input type="hidden" value="${proclass.hardCode}" >
					<li>${proclass.name}</li>
				</c:forEach>
				<div class="clear"><div>
			</ul>
			<div style="clear:both;"></div>
		</div>
		<div class="class_block">
			<input type="hidden" class="TypeId" value="11386">
			<input type="hidden" class="ajaxurl" value="findbybodytype.do">
			<ul class="series">
				<c:forEach items="${bodyType}" var="bt">
					<input type="hidden" value="${bt.hardCode}" >
					<li>${bt.name}</li>
				</c:forEach>
				<div class="clear"><div>
			</ul>
			<div style="clear:both;"></div>
		</div>
		<div class="class_block">
			<input type="hidden" class="ajaxurl" value="findbyptype.do">
			<ul class="series">
				<c:forEach items="${productType}" var="pt">
					<input type="hidden" value="${pt.hardCode}" >
					<li>${pt.name}</li>
				</c:forEach>
				<div class="clear"><div>
			</ul>
			<div style="clear:both;"></div>
		</div>
		<div class="class_block">
			<input type="hidden" class="TypeId" value="11388">
			<input type="hidden" class="ajaxurl" value="findbyseries.do">
			<ul class="series">
				<c:forEach items="${presents}" var="present">
					<input type="hidden" value="${present.hardCode}" >
					<li>${present.name}</li>
				</c:forEach>
				<div class="clear"><div>
			</ul>
			<div style="clear:both;"></div>
		</div>
		<div class="class_block">
			<input type="hidden" class="TypeId" value="11389">
			<input type="hidden" class="ajaxurl" value="findbyseries.do">
			<ul class="series">
				<c:forEach items="${materiels}" var="materiel">
					<input type="hidden" value="${materiel.hardCode}" >
					<li>${materiel.name}</li>
				</c:forEach>
				<div class="clear"><div>
			</ul>
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
	<div class="mask_div"></div>
	<div class="center">
		<%--<div class="sort">
		 
			<span  class="sort_series" id="global">全国销量</span>
			<span  class="sort_series" id="local" >本店销量</span>
			<input type="hidden" class="TypeId" value="">
			<input type="hidden" class="sorturl" value="findbyptype.do">
			<span class="sort_price" id="1" >价格↑</span>
			<input type="hidden" id="sort_hardCode">
		
		</div>--%>
		<%-- <c:if test="${lgroups!=null}">
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
		</c:if> --%>
		<div class="show_item">
		<c:if test="${products!=null}">
			<c:forEach items="${products}" var="product">
				<c:if test="${product.avalible }">
					<form class="addcart" action="" method="post">
						<div class="item">
							<div class="item_number">
								<div class="mobile_img">
									<a  href="../product/detail_${product.productId}.do">
										<img class="item_img" src="${product.imageURL}">
									</a>
									<div class="inventory" title="${product.inv['10615']}">暂时无货</div>
									<input class='actFlag' type='hidden' value="${ product.actFlag}">
									<input class='actItself' type='hidden' value="${ product.actItself}">
								</div>
								<div class="mobile_block">
									<div  class="goods_name">${product.name}</div>
									<div>
										<div class="retail_price">零售价：￥${product.retailPrice}</div>
										<div class="member_price">会员价：￥${product.memberPrice}</div>
										<div class="clear"></div>
									</div>
									<div class="member_qty">本月销售：${product.qty}</div>
									<input type="hidden" name="productId" value="${product.productId }">
									<div class="suit_button">
										<div class="button_block">
											<input name="productId" value="${product.productId }" type="hidden" >
											<a class="min" type="button">-</a>
											<input name="num" type="number" class="text_box" style="font-size:14px" min="${product.minUnit}" step="${product.minUnit}" value="${product.minUnit}" />
											<a class="add" type="button">+</a>
											<div class="clear"></div>
										</div>
										<input class="bg-green add_button" type="submit" value="加入购物车">
										<div class="clear"></div>
									</div>
								</div>
								<div class="clear"></div>
							</div>										
						</div>
					</form>
				</c:if>
			</c:forEach>
		</c:if>
		</div>
		<div class="clear"></div>	
	</div>
	<%-- <%@ include file="common/commonF.jsp"%>		 --%>
	<script src="../js/jquery.js"></script>
	<script src="../js/index.js"></script>
	<script src="../js/product.js"></script>
	<script src="../js/action.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		initpage();
		initadddel();
		$(".item").each(function(){
			if($(this).find(".inventory").attr("title")<=0){
				$(this).find(".inventory").show();
			};
			if($(this).find(".actFlag").val()=="true"){
					$(this).find("input[name='num']").attr("readonly","readonly");
					$(this).find(".add_button").attr("disabled",true);
					$(this).find(".add_button").css({"background":"rgb(159, 159, 157)","border-color":"rgb(159, 159, 157)"});
					$(this).find(".mobile_img").append("<img class='top_imgy' src='../img/active.png'>");
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
	 					}else{/* if(materialprice<0) */
	 						$('span[name="materialname"]').text("可配物料金额:");
	 						$('span[name="materialfee"]').text(-materialprice);
	 					}
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
