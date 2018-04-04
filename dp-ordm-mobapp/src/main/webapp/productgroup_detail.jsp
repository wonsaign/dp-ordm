<%@page import="com.zeusas.core.utils.AppConfig"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.zeusas.core.utils.*" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<link href="../css/index.css" rel="stylesheet">
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
		<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no;charset=utf-8" > 
		<title>活动详情</title>
		<style>
			.p_yx,.p_bx,.z_yx,.z_bx,.z_kx{
				color:#ff5050;
			}
			.product_add,.pay_cancel,.present_add,.free_cancel{
					width:80px;
			}
		</style>
	</head>
	<body>
		<%@ include file="common/commonHT.jsp"%>
		<div class="center">
			<form class="addcart" action="" method="post">
			<div class="suit_shopping"> 
				<div class="suit_goods">
					<img class="suit_img" src="<%=AppConfig.getVfsPrefix()%>${productgroup.image}">
					<img class='top_imgy' src='../img/active.png'>
					<div class="inventory1" title="${product.inv}">暂时无货</div>
				</div>
				<div class="product_detil">
					<div class="suit_details">
						活动名称：${productgroup.name }
					</div>
					<div class="suit_time">
						活动时间：${stTime } - ${enTime }
					</div>
					<div class="active_details">
						活动详情：${productgroup.description }
					</div>
					
					<input type="hidden" name="type" value="${productgroup.type}">
					<input type="hidden" name="actId" value="${productgroup.actId }">
				</div>
				<div class="clear"></div>
				<div class="suit_button">
						<input type="hidden" name="productId" value="${productgroup.actId }">
						<div class="button_block">
							<a class="min" type="button">-</a>
							<input name="num2" type="number" class="text_box" min="1" step="1" value="1" pattern="0"/>
							<a class="add" type="button">+</a>
							<div class="clear"></div>
						</div>
						<input class="bg-green add_button clean" type="button" value="清除">
						<input class="bg-green add_button addcat_bt" type="submit" value="加入购物车">						
						<div class="clear"></div>
					</div>
				<div class="clear"></div>		
			</div>
				<c:if test="${productgroup.type!=01}">
				<div class="tab_change">
						<div id="pay" class="tab_cy active">买<span class="p_bx" style="display:none;">${activityContext.actityGoods.quantity }</span>
								<span class="change_left">(<span class="p_yx">0</span>)</span>
						</div>
						<div id="free" class="tab_cy">赠
								<span class="z_bx" style="display:none;">${activityContext.actityExtra.quantity }</span>
								(
								<span class="change_left"><span class="z_yx">0</span></span>
								)			
					</div>
				</div>
				</c:if>
				<c:if test="${productgroup.type==01}">
				<div class="tab_change">
						<div id="pay" class="tab_cy active">买<span class="p_bx" style="display:none;">${activityContext.actityGoods.quantity }</span>
								<span class="change_left">(<span class="p_yx">0</span>)</span>
						</div>
						<div id="free" class="tab_cy">赠
								<span class="z_bx" style="display:none;">${activityContext.actityExtra.quantity }</span>
								(
								<span class="change_left"><span class="z_yx">0</span></span>/
								<span class="change_left"><span class="z_kx">0</span></span>
								)			
					</div>
				</div>
				</c:if>
			<div class="group_change">
				<div class="product_change">
					<%-- <div class="change">
						<span class="p_bx" style="display:none;">${activityContext.actityGoods.quantity }</span>
						<span class="change_left">已选<span class="p_yx">0</span>件</span>
						<span class="change_right change_more bg-red ">收起</span>
					</div> --%>
					<div class="pay pay_free">
					<c:forEach items="${activityContext.actityGoods.productItem}" var="product">
							<div class="item">
								<div class="item_number">
									<div class="mobile_img">
										<div class="Buy_gifts">买</div>
										<a href="../product/detail_${product.key}.do">
											<img class="item_img" src="${product.value.product.imageURL}">
										</a>
										<div class="inventory" title="${product.value.product.inv[stockId]}">暂时无货</div>
									</div>
									<div class="mobile_block">
										<span  class="goods_name">${product.value.product.name}</span>
										<div class="retail_price">零售价：￥${product.value.product.retailPrice}</div>
										<div class="member_price">会员价：￥${product.value.product.memberPrice}</div>
										<input type="hidden" name="productId" value="${product.key }">
										<div class="suit_button">
										<div class="button_block">
											<a class="min" type="button">-</a>
											<c:if test="${productgroup.type==01||productgroup.type==11}">
												<input name="num" type="number" class="text_box" min="${product.value.quantity}" step="${product.value.quantity}" value="${product.value.quantity}"/>
											</c:if>
											<c:if test="${productgroup.type==02}">
												<input name="num" type="number" class="text_box" min="${product.value.quantity}" step="${product.value.quantity}" value="${product.value.quantity}" />
											</c:if>
											<c:if test="${productgroup.type==08}">
												<input name="num" type="number" class="text_box" min="1" step="1" value="${product.value.quantity }" />
											</c:if>
											<a class="add" type="button">+</a>
											<div class="clear"></div>
										</div>
										<c:if test="${productgroup.type==01||productgroup.type==02||productgroup.type==11}">
											<input class="bg-red add_button product_add" type="button" value="选择" style="display:block;">
											<input class="bg-green add_button pay_cancel" type="button" value="已选中" style="display:none;">
										</c:if>
										<c:if test="${productgroup.type==08}">
											<input class="bg-green add_button pay_cancel" type="button" value="已选中" style="display:block;" disabled="disabled" >
										</c:if>
										<div class="clear"></div>
									</div>
									</div>
								</div>										
							</div>
					</c:forEach>
					<div class="clear"></div>
					</div>
					
					<c:if test="${productgroup.type!=04&&productgroup.type!=11}">
					<%-- <div class="change">
						<span class="z_bx" style="display:none;">${activityContext.actityExtra.quantity }</span>
						<span class="change_left">已选<span class="z_yx">0</span>件</span>
						<span class="change_left">还可以选<span class="z_kx">0</span>件</span>
						<span class="change_right change_more bg-red">收起</span>
					</div> --%>
					<div class="free pay_free" >
					<c:forEach items="${activityContext.actityExtra.productItem}" var="product">
							<div class="item">
								<div class="item_number">
									<div class="mobile_img">
										<div class="Buy_gifts1">赠</div>
										<a href="../product/detail_${product.key}.do">
											<img class="item_img" src="${product.value.product.imageURL}">
										</a>
										<div class="inventory" title="${product.value.product.inv[stockId]}">暂时无货</div>
									</div>
									<div class="mobile_block">
										<span  class="goods_name">${product.value.product.name}</span>
										<div class="retail_price">零售价：￥${product.value.product.retailPrice}</div>
										<div class="member_price">会员价：￥${product.value.product.memberPrice}</div>
										<input type="hidden" name="productId" value="${product.key }">
										<div class="suit_button">
										<div class="button_block">
											<a class="min" type="button">-</a>
											<c:if test="${productgroup.type==01}">
													<input name="num" type="number" class="text_box" min="${product.value.quantity }" step="${product.value.quantity }" value="${product.value.quantity }" />
											</c:if>
											<c:if test="${productgroup.type==02}">
												<input name="num" type="number" class="text_box" min="${product.value.quantity }" step="${product.value.quantity }" value="${product.value.quantity }" readonly="readonly" />
											</c:if>
											<a class="add" type="button">+</a>
											<div class="clear"></div>
										</div>
										<c:if test="${productgroup.type==01}">
										<input class="bg-red add_button present_add" type="button" value="选择" style="display:block;"> 
										<input class="bg-green add_button free_cancel" type="button" value="已选中" style="display:none;">
										</c:if>
										<c:if test="${productgroup.type==02}">
										<input class="bg-red add_button present_add" type="button" value="选择" style="display:block; background:rgb(159, 159, 157);" disabled="disabled"> 
										<input class="bg-green add_button free_cancel" type="button" value="已选中" style="display:none;" disabled="disabled">
										</c:if>
										<div class="clear"></div>
									</div>
									</div>
								</div>										
							</div>
						</c:forEach>
						<div class="clear"></div>
					</div> 
					</c:if>	
					<c:if test="${productgroup.type==04||productgroup.type==11}">
						<div class="free pay_free" >
							<ul id="myTab" class="nav nav-tabs">
							<c:forEach items="${activityContext.actityExtras}" varStatus="status" var="rule">
								<li>
									<a href="#bage${ status.index + 1}" data-toggle="tab"> <span class="lbyx">礼包${ status.index + 1}</span>(<span class="lbxz">0</span>/<span class="lbbm">0</span>)</a>
									<input type="hidden" value="${rule.quantity}" class="tabquantity">
								</li>
							</c:forEach>
								<div class="clear"></div>
							</ul>
							<div id="myTabContent" class="tab-content">
							<c:forEach items="${activityContext.actityExtras}" varStatus="status" var="rule">
								<div class="tab-pane fade" id="bage${ status.index + 1}">
									<div class="free">
									<c:forEach items="${rule.productItem}" var="product">
											<div class="item">
												<div class="item_number">
													<div class="mobile_img">
														<div class="Buy_gifts1">赠</div>
														<a href="../product/detail_${product.key}.do">
															<img class="item_img" src="${product.value.product.imageURL}">
														</a>
														<div class="inventory" title="${product.value.product.inv}">暂时无货</div>
													</div>
													<div class="mobile_block">
														<span  class="goods_name">${product.value.product.name}</span>
														<div class="retail_price">零售价：￥${product.value.product.retailPrice}</div>
														<div class="member_price">会员价：￥${product.value.product.memberPrice}</div>
														<input type="hidden" name="productId" value="${product.key}">
														<div class="suit_button">
														<div class="button_block">
															<a class="min" type="button">-</a>
																<input name="num" type="number" class="text_box" min="${product.value.quantity}" step="${product.value.quantity}" value="${product.value.quantity}"/>
															<a class="add" type="button">+</a>
															<div class="clear"></div>
														</div>
														<input type="hidden" value="${ status.index}" class="pindex">
														<c:if test="${rule.selected==false}">
															<input class="bg-red add_button present_add" type="button" value="选择" style="display:block;"> 
															<input class="bg-green add_button free_cancel" type="button" value="已选中" style="display:none;">
														</c:if>
														<c:if test="${rule.selected==true}">
														<input class="bg-green add_button free_cancel" type="button" value="已选中" style="display:block;" disabled="disabled">
														</c:if>
														<div class="clear"></div>
													</div>
													</div>
												</div>										
											</div>
									</c:forEach>
									</div>
								</div>
								</c:forEach>
							</div>
							</div>
					</c:if>				
				</div>
			</div>		
			<div style="clear: both;"></div>						
			</form>
		</div>
		<c:if test="${productgroup.type!=04&&productgroup.type!=08}">
		<div class="fix_mz operationpanel" style="background:#ff9400;">
			<input type="hidden" class="paq" value="${productgroup.context.actityGoods.quantity }">
			<div class="fix_mt">买<span class="k_mx">${productgroup.context.actityGoods.quantity }</span>赠<span class="k_zx">${productgroup.context.actityExtra.quantity }</span>&nbsp;/&nbsp;
			已买<span class="y_mx">0</span>赠<span class="y_zx">0</span></div>
		</div>
		</c:if>
	<%@ include file="common/commonF.jsp"%>
<script src="../js/jquery.js"></script>
<script src="../js/index.js"></script>
<script type="text/javascript">
	$(function(){
			function lcm(m,n){//辗转相除法 求最大公约数
				var u=0; 
				var v=0;
				var t;
				var u=+m,v=+n,t=v;
				while(v!=0){
					t=u%v;
					u=v;
					v=t;
				}
				return u
			};
			 if(type="11"){
					var kk=$(".k_mx").text();
					var mk=$(".pay").find(".item").eq(0).find("input[name='num']").val();
		      var mc=lcm(kk,mk);
					var fk;
					var k_zx=0;
					$("#myTab li").each(function(){
						t=$(this).find(".tabquantity").val();
						i=$(this).index();
						k_zx+=parseInt(t);
					});
					var y=parseInt(kk*mk)/parseInt(mc*kk);
					$(".k_mx").text(parseInt(kk*mk)/parseInt(mc));
					$(".k_zx").text(parseInt(k_zx)*parseInt(y));
				}; 
			$(".pay").show();			
			$(".tab_cy").click(function(){
				$(this).addClass("active");
				$(this).siblings(".tab_cy").removeClass("active");
				var tid= $(this).attr("id");
				$("."+tid).show();
				$("."+tid).siblings(".pay_free").hide();
			})
			$(".nav-tabs li").each(function(){
				var	nav=$(this).index();
				var tn=tab(nav);
				$(this).find(".lbxz").text(tn);
			});
			$(".lbyx").eq(0).text("礼包");
			function tab(a){
				var allnum=0;
				$(".tab-pane").eq(a).find(".item").each(function(){
					 	var num=$(this).find("input[name='num']").val();
				 		var pay=$(this).find(".free_cancel").css("display");
						if(pay=="block"){
							allnum+=parseInt(num);
						}
				})
				return allnum;
			};
		if($(".k_mx").text()==""){
			$(".k_mx").text(0);
		}
		if($(".k_zx").text()==""){
			$(".k_zx").text(0);
		}
		initadddel();	
		$("#myTab li").eq(0).addClass("active");
		$("#bage1").addClass("active in");
		$("#myTab li").click(function(){
			$(this).addClass("active");
			$(this).siblings().removeClass("active");
			var myid=$(this).find("a").attr("href");
			$(myid).addClass("active in");
			$(myid).siblings().removeClass("active in");
		});
		var position=$(this);
	 	$('.addcart').submit(function(event){
	 		var type=$("input[name='type']").val();
	 		var k_mx=$(".k_mx").text();
	 		var k_zx=$(".k_zx").text();
	 		var y_mx=$(".y_mx").text();
	 		var y_zx=$(".y_zx").text();
			event.preventDefault();
			//判断是否已选择购物车
 			if($('.select_text input').val()==0){
 				$(".message_body").css("display","block");
 				var shop=$(".shop_hover").html();
 				$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>"+"<div>"+shop+"</div>"+"</div>");
				//加入监听事件
				initcartwindowselect($('.message_block2'));
 				return false;
 			}
			if(type == 11 && parseInt(k_mx*y_zx) != parseInt(k_zx*y_mx)){
				$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>数量不符合配比，请选择符合配比的产品!</div>");
		 	 	$(".message_body").css("display","block");
				return false;
			}
 			var k=0;
			$(".pay_cancel").each(function(){
				if($(this).css("display")=="block"){
					var innum=$(this).parent().parent().prev().find(".inventory").attr("title");
					if(innum<=0){
						k++;
					}
				}
				
			});
			$(".free_cancel").each(function(){
				if($(this).css("display")=="block"){
					var innum=$(this).parent().parent().prev().find(".inventory").attr("title");
					if(innum<=0){
						k++;
					}
				}
			})
			if(k<1){
				var type=$("input[name='type']").val();
				var s=0;
				$(".nav-tabs li").each(function(){
					 if($(this).find(".lbxz").text()!=$(this).find(".lbbm").text()){
						  s++;
					 }
				})
				if(s!=0){
					$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>礼包数量不符合!</div>");
			 	 	$(".message_body").css("display","block");
	 	 			position.find(".add_button").attr("disabled",false);
				}else{
					var position =$(this);
					position.find(".add_button").attr("disabled",true);
					var totalnum=$(this).find('input[name="num2"]').val();
					var actId = $(this).find('input[name="actId"]').val();
					var $Items = new Array();
					position.find('.pay').children('.item').each(function (){
						var  msg = $(this).find(".pay_cancel");
						if(msg.css("display")=="block"){
							$Items.push({id: $(this).find('input[name="productId"]').val(),
				 				type:"PAY",
				 				num:$(this).find('input[name="num"]').val()});
						}
			 		});
					position.find('.free').children('.item').each(function (){
						var  msg = $(this).find(".free_cancel");
						if(msg.css("display")=="block"){
							$Items.push({id:$(this).find('input[name="productId"]').val(),
				 				type:"FREE",
				 				num:$(this).find('input[name="num"]').val()});
						}
			 		});
					var $info={actId:actId,num:totalnum,json:JSON.stringify($Items)};
			   	  	$.ajax({
			 	 		type : "post",
			 	 		url : "../cart/addactivity.do",
			 	 		async : false,
			 	 		timeout : 15000,
			 	 		dataType:"json",
			 	 		cache:false,
			 	 	    data:$info,
			 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
			 	 			$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>"+errorThrown+"加入购物车失败!</div>");
					 	 	$(".message_body").css("display","block");
			 	 			position.find(".add_button").attr("disabled",false);
			 	 		},
			 	 		success : function(data) {
			 	 			$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>加入购物车成功</div>");
					 	 	$(".message_body").css("display","block");
			 	 			position.find(".add_button").attr("disabled",false);
						}
			 	 	});
				}
			}else{
				$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>库存不足，不能加入购物车</div>");
		 	 	$(".message_body").css("display","block");
			}  
	 	});
	});
	$(document).ready(function(){
		function productgroup(){
			var p_num=0;
			var z_num=0;
			var i=0;
			var j=0;
			var pl=$(".pay .item").length;
			var fl=$(".free .item").length;	
			if(pl==0){
				$(".pay").hide();
			};
			if(fl==0){
				$(".free").hide();
			};
			
			$(".pay .item").each(function(){
				if($(this).find(".pay_cancel").css("display")=="block"){
					var num=$(this).find("input[name='num']").val();
					p_num = Number(p_num)+Number(num);
					$(this).find("input[name='num']").attr("readonly",true);
				}
				if($(this).find(".inventory").attr("title")<=0){
					$(this).find(".inventory").show();
					$(this).find("input[type='button']").attr({"disabled":"disabled"});
					$(this).find("input[type='button']").css({"background":"#9f9f9d"});
					$(this).find("input[name='num']").attr("readonly",true);
					i++;
				};
			});
			$(".p_yx").text(p_num);
			$(".free .item").each(function(){
				if($(this).find(".free_cancel").css("display")=="block"){
					var num=$(this).find("input[name='num']").val();
					z_num = Number(z_num)+Number(num);
					$(this).find("input[name='num']").attr("readonly",true);
				}
				if($(this).find(".inventory").attr("title")<=0){
					$(this).find(".inventory").show();
					$(this).find("input[type='button']").attr({"disabled":"disabled"});
					$(this).find("input[type='button']").css({"background":"#9f9f9d"});
					$(this).find("input[name='num']").attr("readonly",true);
					j++;
				};
				if($(this).find(".free_cancel").css("display")=="block"){
					 if($(this).find(".inventory").attr("title")<Number($(this).find("input[name='num']").val())+1){
						  $(".addcat_bt").attr("disabled","disabled");
						  $(".addcat_bt").css("background","rgb(159, 159, 157)");
						  $(".inventory1").show();
					 }
				};
			});
			if((pl!=0&&i==pl)||(fl!=0&&j==fl)){
				$("input[name='num2']").attr("readonly","readonly");
				$(".addcat_bt").attr("disabled","disabled");
				$(".addcat_bt").css("background","rgb(159, 159, 157)");
				$(".suit_goods").find(".inventory1").show();
			}else{
				$(".suit_goods").find(".inventory1").hide();
			};
			$(".z_yx").text(z_num);
			$(".y_mx").text(p_num);
			$(".y_zx").text(z_num);
		}
		productgroup();
		$(".product_add").click(function(){
			var p_yx= $(".p_yx");
			var p_bx= $(".p_bx").text();
			var z_bx= $(".z_bx").text();
			var z_yx= $(".z_yx").text();
			var type=$("input[name='type']").val();
			if(checknumber($(this).parent().find('input[name="num"]')) == false){
				return false;
			};
			var inventory=$(this).parent().parent().prev().find(".inventory").attr("title");
			var num = $(this).parent().find("input[name='num']").val();
			if(parseInt(num) > parseInt(inventory)){
				$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>该产品只有"+inventory+"件了</div>");
		 	 	$(".message_body").css("display","block");
		 	 	return false;
			};
			if(num<=0){
				$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>购买数量不能小于 1！</div>");
		 	 	$(".message_body").css("display","block");
				return;
			}
			result = Number(num)+Number(p_yx.text());
			$(".p_yx").text(result);
			$(this).css("display","none");
			$(this).next().css("display","block");
			$(this).prev().find("input[name='num']").attr("readonly",true);
			$(".y_mx").text(result);
			if(type == 02){
				 var payId=$(this).parent().parent().find("input[name='productId']").val();
				 var paynum=$(this).parent().parent().find("input[name='num']").val();
				 var paymin=$(this).parent().parent().find("input[name='num']").attr("min");
				 var allnum=parseInt($(".z_yx").text());
				 $(".free .item").each(function(){
					  var freeId = $(this).find("input[name='productId']").val();
					  var freemin=$(this).find("input[name='num']").attr("min");
					  if(payId == freeId){
						   $(this).find("input[name='num']").val(paynum/paymin*freemin);
						   $(this).find(".present_add").hide();
						   $(this).find(".free_cancel").show();
						   allnum += paynum/paymin*freemin;
					  }
				 });
				 $(".z_yx").text(allnum);
				 $(".z_kx").text(0);
				 $(".y_zx").text(allnum);
			}if(type == 11){
				$("#myTab li").each(function(){
					  var tabquantity = $(this).find(".tabquantity").val();
					  var y_mx = $(".y_mx").text();
					  var k_mx = $(".k_mx").text();
					  var y_k = parseInt(y_mx/k_mx);
					  var k_y = parseInt(y_mx%k_mx);
					  var paq = $(".paq").val();
					  if(k_y == 0){
						  $(this).find(".lbbm").text(parseInt(y_k*tabquantity*k_mx)/parseInt(paq));
					  }else{
						  $(this).find(".lbbm").text(0);
					  }
				 });
			}else{
				var max = Number(p_yx.text())/Number(p_bx)*Number(z_bx)-Number(z_yx);
				$(".z_kx").text(parseInt(max));
			}
		});
		$(".present_add").click(function(){
			if(checknumber($(this).parent().find('input[name="num"]')) == false){
				return false;
			}
			var inventory=$(this).parent().parent().prev().find(".inventory").attr("title");
			var num = $(this).parent().find("input[name='num']").val();
			if(parseInt(num) > parseInt(inventory)){
				$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>该产品只有"+inventory+"件了</div>");
		 	 	$(".message_body").css("display","block");
		 	 	return false;
			};
			var type=$("input[name='type']").val();
			if(type==04){
					var x= $(this).parent().parent().parent().parent().parent().parent().index();
					var lbxz=$(".lbxz").eq(x).text();
					var allnum=Number(num)+Number(lbxz);
				  if(allnum>$(".lbbm").eq(x).text()){
					  $(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>所选数量超过该礼包数量</div>");
					  $(".message_body").fadeIn();
			 	 		$(".message_body").fadeOut(3000);
				  }
					$(".lbxz").eq(x).text(allnum);
			}else if(type==11){
				var pindex = $(this).parent().find(".pindex").val();
				var lbbm= $("#myTab li").eq(pindex).find(".lbbm").text();
				var lbxz= $("#myTab li").eq(pindex).find(".lbxz").text();
				var y_zx = $(".y_zx").text();
				if(lbbm == 0){
					$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>你不能选赠品，请您确认产品数量配比</div>");
			 	 	$(".message_body").css("display","block");
					return;
				}
				lbxz = Number(lbxz)+Number(num);
			  if(Number(lbxz)>Number(lbbm)){
					$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>最多可以选【"+lbbm+"】</div>");
			 	 	$(".message_body").css("display","block");
					return;
				}
				if(num<=0){
					$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>购买数量不能小于 1！</div>");
			 	 	$(".message_body").css("display","block");
					return;
				}
				$("#myTab li").eq(pindex).find(".lbxz").text(lbxz);
				$(this).hide();
				$(this).next().show();
				y_zx = Number(y_zx)+Number(num);
				$(".y_zx").text(y_zx);
			}else{
					var kx = $(".z_kx");
					var yx= $(".z_yx");
					if(kx.text()==0){
						$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>你只能选择这些赠品，先选择【商品】呦！</div>");
				 	 	$(".message_body").css("display","block");
						return;
					}
					if(Number(num)>Number(kx.text())){
						$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>最多可以选【"+kx.text()+"】</div>");
				 	 	$(".message_body").css("display","block");
						return;
					}
					if(num<=0){
						$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>购买数量不能小于 1！</div>");
				 	 	$(".message_body").css("display","block");
						return;
					}
					result = Number(num)+Number(yx.text());
					$(".z_yx").text(result);
					kx.text(Number(kx.text())-Number(num));
					$(".y_zx").text(result);
				}
				$(this).css("display","none");
				$(this).next().css("display","block");
				$(this).parent().find("input[name='num']").attr("readonly",true);
				
		});
		
		$(".pay_cancel").click(function(){
			var p_yx = $(".p_yx").text();
			var p_bx = $(".p_bx").text();
			var z_bx= $(".z_bx").text();
			var z_yx= $(".z_yx").text();
			var type=$("input[name='type']").val();
			var def = $(this).prev().prev().find("input[name='num']").val();
			var newNum = parseInt(p_yx)-parseInt(def);
			$(".p_yx").text(newNum);
			$(this).css("display","none");
			$(this).prev().css("display","block"); 
			$(this).parent().find("input[name='num']").attr("readonly",false);
			if(zNewNum<0){
				$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>赠品多选了"+Math.abs(zNewNum)+"件 ！</div>" );
		 	 	$(".message_body").css("display","block");
			}
			$(".y_mx").text(newNum);
			if(type == 02){
				 var payId=$(this).parent().parent().find("input[name='productId']").val();
				 var paynum=$(this).parent().parent().find("input[name='num']").val();
				 var paymin=$(this).parent().parent().find("input[name='num']").attr("min");
				 var allnum=parseInt($(".z_yx").text());
				 $(".free .item").each(function(){
					  var freeId = $(this).find("input[name='productId']").val();
					  var freemin=$(this).find("input[name='num']").attr("min");
					  if(payId == freeId){
						  var onenum=$(this).find("input[name='num']").val();
						   $(this).find("input[name='num']").val(freemin);
						   $(this).find(".present_add").show();
						   $(this).find(".free_cancel").hide();
						   allnum -= onenum;
					  }
				 });
				 $(".z_yx").text(allnum);
				 $(".z_kx").text(0);
				 $(".y_zx").text(allnum);
			}else if(type == 11){
				$("#myTab li").each(function(){
					  var tabquantity = $(this).find(".tabquantity").val();
					  var y_mx = $(".y_mx").text();
					  var k_mx = $(".k_mx").text();
					  var y_k = parseInt(y_mx/k_mx);
					  var k_y = parseInt(y_mx%k_mx);
					  var paq = $(".paq").val();
					  if(k_y == 0){
						  $(this).find(".lbbm").text(parseInt(y_k*tabquantity*k_mx)/parseInt(paq));
					  }else{
						  $(this).find(".lbbm").text(0);
					  }
				 });
				}else{
				var zNewNum = newNum/Number(p_bx)*Number(z_bx)-Number(z_yx);
				$(".z_kx").text(zNewNum);
			};
		});
		
		$(".free_cancel").click(function(){
			var def = $(this).prev().prev().find("input[name='num']").val();
			var num = $(this).parent().find("input[name='num']").val();
			var type=$("input[name='type']").val();
			if(type==04){
					var x= $(this).parent().parent().parent().parent().parent().parent().index();
					var lbxz=$(".lbxz").eq(x).text();
				  $(".lbxz").eq(x).text(Number(lbxz)-Number(def));
		  }else if(type==11){
				var pindex = $(this).parent().find(".pindex").val();
				var lbbm= $("#myTab li").eq(pindex).find(".lbbm").text();
				var lbxz= $("#myTab li").eq(pindex).find(".lbxz").text();
				var y_zx = $(".y_zx").text();
				lbxz = Number(lbxz)-Number(num);
			  if(Number(lbxz)>Number(lbbm)){
					$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>最多可以选【"+lbbm+"】</div>");
			 	 	$(".message_body").css("display","block");
					return;
				}
				if(num<=0){
					$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>购买数量不能小于 1！</div>");
			 	 	$(".message_body").css("display","block");
					return;
				}
				$("#myTab li").eq(pindex).find(".lbxz").text(lbxz);
				$(this).hide();
				$(this).next().show();
				y_zx = Number(y_zx)-Number(num);
				$(".y_zx").text(y_zx);	
			}else{
			  var p_yx = $(".p_yx").text();
				var p_bx = $(".p_bx").text();
				var z_kx = $(".z_kx").text();
				var z_yx = $(".z_yx").text();
				var kxNum = parseInt(z_kx)+parseInt(def);
				var yxNum = parseInt(z_yx)-parseInt(def);
				$(".z_kx").text(kxNum);
				$(".z_yx").text(yxNum);
				$(".y_zx").text(yxNum);
		  }
			$(this).css("display","none");
			$(this).prev().css("display","block");
			$(this).parent().find("input[name='num']").attr("readonly",false);	
		});
		$(".addcat_bt").click(function(){
			var num = $(this).prev().find("input[name='num']").val();
			var p_yx = $(".p_yx").text();
			var p_bx = $(".p_bx").text();
			var z_kx = $(".z_kx").text();
			var z_yx = $(".z_yx").text();
			var z_bx = $(".z_bx").text();
			if(num<=0){
				$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>购买数量要大于0套呦！</div>");
		 	 	$(".message_body").css("display","block");
				return false;
			}
			if(parseInt(p_yx)<parseInt(p_bx)){
				$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>您选择的商品数量不足，请您继续选择</div>" );
		 	 	$(".message_body").css("display","block");
				return false;
			}
			if(z_kx > 0){
				$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>您还有"+Math.abs(z_kx)+"件赠品可以选，请选择赠品 ！</div>");
		 	 	$(".message_body").css("display","block");
				return false;
			}
			if(z_kx < 0){
				$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>您赠品多选了"+Math.abs(z_kx)+"件，请调整赠品数量</div>");
		 	 	$(".message_body").css("display","block");
				return false;
			}
			return true;
		});		
		$(".clean").click(function(){
			location.reload();
		});
		
		//修改购物车数量
		$('input[name="num"]').blur(function(){
			if(checknumber($(this)) == false){
				return false;
			}
			var detailId=$(this).parent().parent().find('input[name="detailId"]').val();
			var num=$(this).val();  
	 	});
	})
	
	 //校验input number 内容是否符合提交规范
 	function checknumber(position){
 		if(position.val()<=0){
 			$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>商品数量必须大于0!</div>");
	 	 	$(".message_body").css("display","block");
 			return false;
 		}else{
 			if(position.val()%position.attr('step')!=0){
 				$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>数量不符合规范：必须是"+position.attr('step')+"倍数!</div>");
		 	 	$(".message_body").css("display","block");
 				position.val(position.attr('step'));
 				return false;
 			}
 			return true;
 		}
 	};
</script>
	</body>

</html>
