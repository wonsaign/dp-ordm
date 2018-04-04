<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.zeusas.core.utils.*"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>订单详情</title>
<link href="../css/index.css" rel="stylesheet">
<link href="../css/style.css" rel="stylesheet">
<link href="../css/ssi-uploader.css" rel="stylesheet">
<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
</head>
<body>
	<%@ include file="../common/commonHT.jsp"%>
	<div class="position_body">
		<img class="bigimg" src="../img/240_R6EVBIG35920151010161300.jpg">
		<img class="bigimg_close" src="../img/close.png">
	</div>
	<div class="center">
		<div class="order_number">
		<input type="hidden" value="${order.id }" id="orderid">
		<div class="order_span">
			<c:if test="${order.orderStatus==2}">
				<span class="red order_span">待付款订单</span>
				<input type="button" class="sure_cancel submit_order bg-green" value="作废订单">
				<div class="cancel_order">
					<sf:form method="post" action="cart.do">
						<input type="hidden" value="${order.id }" name="id">
						<input type="hidden" value="${order.orderNo }" name="orderNo">
						<div>如果作废该订单，此订单将为无效订单！</div>
						<input type="submit" value="确认作废" class="submit_left bg-red">
					</sf:form>
					<button class="cancel_invalid bg-red submit_left">取消作废</button>
					<div class="clear"></div>
				</div>
			</c:if>
			<c:if test="${order.orderStatus==10}">
				<span class="red order_span">财务退回订单</span>
			</c:if>
			<c:if test="${order.orderStatus==10||order.orderStatus==2}">
				<input type="button" class="return_cart submit_order submit_left bg-green" value="返回购物车">
				<div class="return_order">
					<sf:form method="post" action="rollBack.do">
						<input type="hidden" value="${order.id }" name="orderId">
						<div>如果作返回该订单，此订单车将重新提交审核！</div>
						<input type="submit" value="确认返回" class="submit_left bg-red">
					</sf:form>
					<button class="cancel_invalid bg-red submit_left">取消返回</button>
					<div class="clear"></div>
				</div>
			</c:if>
			<c:if test="${order.orderStatus==3}">
				<span class="red lin">已付款订单</span>
			</c:if>
			<c:if test="${order.orderStatus==5}">
				<span class="red lin">已付款订单</span>
			</c:if>
			<c:if test="${order.orderStatus==8}">
				<span class="red lin">待收货订单</span>
			</c:if>
			<c:if test="${order.orderStatus==9}">
				<span class="red lin">确认完成订单</span>
			</c:if>
			<c:if test="${order.orderStatus==0}">
				<span class="red lin">无效订单</span>
			</c:if>
		</div>
		<!-- 订单判断样式 -->
		<c:if test="${MyOrder.singleOrder.orderStatus==2||MyOrder.singleOrder.orderStatus==10}">
			<c:if test="${MyOrder.singleOrder.checkDesc!=null}">
				<div>
					<span class="information_settle">退回原因：<strong class="red"><i>${MyOrder.singleOrder.checkDesc}</i></strong></span>
				</div>
			</c:if>
			<div class="accessory_body">
				<sf:form action="../order/confirm.do" method="post"
					modelAttribute="order">
					<div class="accessory_block">
						<input name="id" type="hidden" value="${order.id}" />
						<div>
							<span>实际支付金额:<b class="paymentPrice"></b></span>
						</div>
						<div>
							<span>备注:</span><b class="description"></b>
						</div>
						<input type="submit" class="bg-red sure_all submit_left" value="确认提交">
						<input type="button" class="bg-red cancel_all submit_left" value="取消提交">
						<div class="clear"></div>
					</div>
				</sf:form>
			</div>
				<div class="clear"></div>
					<div class="pay_style">
							<div class="message_top">
									支付方式
							</div>
							<div class="message_center">
								<h4 style="padding-left:10px;margin:0px">线上支付</h4>	
								<div  class="online_pay">
									<label for="onlinepay"><input id="onlinepay" type="radio"name="pay" value="aliPay"><i></i>支付宝</label> 
								</div>
								<div  class="online_pay">
									<label for="onlinecmbc"><input id="onlinecmbc" type="radio"name="pay" value="aliPay"><i></i>民生付</label> 
								</div>
								<h4 style="padding-left:10px;margin:0px">线下支付</h4>
								<div class="online_pay">
									<label for="offlinepay"><input id="offlinepay" type="radio" name="pay" value="accessory"><i></i>上传凭证</label>
								</div>
								<input type="button" value="支付" id="pay_sure" class="submit_left bg-red"> 
								<input type="button" value="取消支付" id="pay_close" class="submit_left bg-red">
								<div class="clear"></div>
							</div>
					</div>
			</c:if>
			<!-- 订单判断结束 -->
		<div class="information_settle">订单号:<span class="col-999" id="trade_no">BXX${order.orderNo }</span></div>
		<div class="information_settle">下单时间:<span class="col-999 showtime">${(MyOrder.singleOrder.orderCreatTime)}</span></div>	
		<div class="clear"></div>
		</div>
		<div class="add_mar">
			<div class="address">
				<img class="address_img" src="../img/address.png">
				<div class="address_div">
					<div>
						<span class="linkman">联系方式：</span><span id="contact">${MyOrder.singleOrder.contact}	| ${MyOrder.singleOrder.phone}</span>
					</div>
					<div>
						<span class="linkman">收货地址：</span><span id="address">${MyOrder.singleOrder.address}</span>
					</div>
				</div>	
				<div class="clear"></div>
			</div>
		</div>
		<div class="settle">
			<span class="information_settle user_left1">柜台号:${MyOrder.singleOrder.counterCode}</span>
		  <span class="information_settle user_left1">柜台名称:${MyOrder.singleOrder.counterName}</span>
		  <span class="information_settle user_left1">商品数量:${MyOrder.singleOrder.totalNum}</span>
			<span class="information_settle user_left1">商品金额:￥${MyOrder.singleOrder.orderOriginalFee}</span>
			<shiro:hasAnyRoles name="root,adm,14,12,11">
				<span class="information_settle user_left1">折后金额:￥${MyOrder.singleOrder.productFee}</span>
			</shiro:hasAnyRoles>
			<span class="information_settle red user_left1">额外物料费用:${MyOrder.singleOrder.materialFee	}</span>
			<span class="information_settle user_left1">运费:￥${MyOrder.singleOrder.expressFee}</span>
			<c:if test="${MyOrder.singleOrder.orderStatus==7 }">
				<span class="information_settle user_left1">扣款金额:${MyOrder.singleOrder.realFee }</span>
			</c:if>
			<div class="clear"></div>
			<div class="address">
				<shiro:hasAnyRoles name="root,adm,14,12,11">
					<c:if test="${MyOrder.singleOrder.orderStatus!=2}">
						<span class="information_settle red user_left1">
							实付款:${MyOrder.singleOrder.paymentPrice} </span>
						<span class="information_settle red user_left1">使用余额:<span
							class="priceFormat">${MyOrder.singleOrder.useBalance}</span></span>
						<span class="information_settle red user_left1">应付金额:<strong
							id="paymentFee">${MyOrder.singleOrder.paymentFee}</strong></span>
					</c:if>
					<c:if test="${MyOrder.singleOrder.orderStatus==2}">
						<span class="information_settle red user_left1">应付金额:<strong
							id="paymentFee">${MyOrder.singleOrder.payable}</strong></span>
					</c:if>
					<c:if test="${MyOrder.singleOrder.orderStatus==2 }">
						<span class="information_settle red user_left1">
							<input type="checkbox"
							<c:if test="${(empty usefulBalance||usefulBalance<=0)&&(empty MyOrder.singleOrder.useBalance ||MyOrder.singleOrder.useBalance==0)}">disabled</c:if>
							<c:if test="${!empty MyOrder.singleOrder.useBalance&&MyOrder.singleOrder.useBalance!=0}">checked</c:if>
							id="balance"> <label for="balance" style="cursor: pointer";>使用余额:<span
								class="priceFormat">${MyOrder.singleOrder.useBalance}</span></label>
						</span>
						<span class="information_settle red balance_surplus user_left1"><label for="balance">剩余余额:</label><span
							class="priceFormat">${usefulBalance}</span></span>
					</c:if>
				</shiro:hasAnyRoles>
				<c:if test="${MyOrder.singleOrder.orderStatus==2||MyOrder.singleOrder.orderStatus==10}">
				<c:if test="${counter.type!='直营'&& MyOrder.singleOrder.useBalance!=MyOrder.singleOrder.paymentFee}">
					<div class="pay_button">
						<c:if test="${message!='success' ||order.imageURL==null||''==order.imageURL }">
							<input type="button" class="submit_button submit_order bg-green" value="支付方式">
						</c:if>
					</div>
				</c:if>
				<c:if
					test="${counter.type=='直营' ||(!empty MyOrder.singleOrder.useBalance&&MyOrder.singleOrder.useBalance!=0&&MyOrder.singleOrder.useBalance==MyOrder.singleOrder.paymentFee)}">
					<input type="button" class="submit_form submit_order bg-green"
						value="提交">
					<div class="form_submit">
						<form action="../order/confirmdirect.do" method="post">
							<input type="hidden" name="">
							<div style="margin: 10px 0;">
								<input type="hidden" value="${order.id }" name="id"> <input
									type="hidden" value="${order.orderNo }" name="orderNo">
								<label for="description"
									style="width: 40px; display: inline-block; cursor: pointer; line-height: 36px; position: relative; top: -15px;">备注:</label>
								<textarea rows="3" cols="20" class="form-control" value="" name="description"
									id="description">${order.description }</textarea>
							</div>
							<div class="text-center">
								<input type="submit" style="font-size: 14px;"	class="message_button bg-red" value="提  交">
							</div>	
						</form>
					</div>
				</c:if>
			</c:if>
				<div class="clear"></div>
				
			</div>
			<div class="clear"></div>
		</div>
		<div>
			<c:forEach items="${OrderDetails}" var="ODmap">
			<c:forEach items="${ODmap.value }" var="ods">
			<div class="order_list">
				<div>
				<span class="ordname">${ods.orderDetail.productName }</span>
				<shiro:hasAnyRoles name="root,adm,14,12,11">
					<span class="ordname1">￥${ods.orderDetail.quantity*ods.orderDetail.unitPrice}</span>
				</shiro:hasAnyRoles>
				<div class="clear"></div>
				</div>
				<div class="user_left1 col-666">会员价:${ods.orderDetail.memberPrice }</div>
				<div class="user_left1 col-666">
					<span>数量:${ods.orderDetail.quantity }</span>
					<c:if test="${MyOrder.singleOrder.orderStatus==7 }">
							<span
							<c:if test="${ods.orderDetail.realQty<ods.orderDetail.quantity}">style="background: #ff5050; padding-left:5px;"</c:if>
							<c:if test="${ods.orderDetail.realQty>=ods.orderDetail.quantity}">style="background: #6dbb44; padding-left:5px;"</c:if>>
							${ods.orderDetail.realQty }
							</span>
					</c:if>
				</div>
				<div class="user_left1 col-666">原价:${ods.orderDetail.quantity*ods.orderDetail.memberPrice}</div>
				<div class="user_left1 col-666">类型:${ods.product.typeName }</div>
				<div class="clear"></div>
			</div>
			</c:forEach>
			</c:forEach>
		</div>
		<c:if
			test="${MyOrder.singleOrder.orderStatus==7 && !empty diffDetails}">
			<hr />
			<div class="order_number">
				<span class="order_span"> 活动赠品</span>
			</div>
			<!-- 补发 或 订货政策 -->
			<table class="detial">
				<thead>
					<tr class="blue">
						<th>序号</th>
						<th>商品名称</th>
						<th>金额</th>
						<th>实发数量</th>
					</tr>
				</thead>
				<c:forEach items="${diffDetails}" var="diffDt">
					<tbody>
						<tr class="order_detial">
							<td class="order_left blue"></td>
							<td class="order_center">${diffDt.productName }</td>
							<td class="priceFormat order_center">${diffDt.realTotalPrice }</td>
							<td class="order_center">${diffDt.realQty }</td>
						</tr>
					</tbody>
				</c:forEach>
			</table>
		</c:if>

		
		<c:if test="${counter.type!='直营' }">
			<c:if test="${order.orderStatus >= 2}">
				<div style="margin: 0px 10px; color:#333333;">
					<span>备注：${order.description }</span>
				</div>
				<c:forEach items="${order.imageURL}" var="imgs">
					<div class="img_data">
						<img src="<%=AppConfig.getVfsPrefix() %>${imgs}" class="file_img">
					</div>
				</c:forEach>
				<div class="clear"></div>
			</c:if>
		</c:if>
		<c:if test="${MyOrder.singleOrder.orderStatus==8 }">
			<div class="sure_counter">
				<span>您确认收货吗？</span>
				<sf:form method="post" action="finishorder.do">
					<input type="hidden" value="${order.id }" name="id">
					<input type="hidden" value="${order.orderNo }" name="orderNo">
					<input type="submit" value="确认收货" class="submit_order big bg-red">
					<input type="button" value="取消收货"
						class="submit_order big bg-red close_goods">
				</sf:form>
			</div>
			<div style="width: 10%; margin: 5px auto;">
				<input type="button" value="确认收货"
					class="submit_order big bg-red sure_goods">
			</div>
		</c:if>
		<c:if test="${MyOrder.singleOrder.orderStatus==8 }">
			<div class="sure_counter">
				<span>您确认收货吗？</span>
				<sf:form method="post" action="finishorder.do">
					<input type="hidden" value="${order.id }" name="id">
					<input type="hidden" value="${order.orderNo }" name="orderNo">
					<input type="submit" value="确认收货" class="submit_order big bg-red">
					<input type="button" value="取消收货"
						class="submit_order big bg-red close_goods">
				</sf:form>
			</div>
			<div style="width: 10%; margin: 5px auto;">
				<input type="button" value="确认收货"
					class="submit_order big bg-red sure_goods">
			</div>
		</c:if>
		<form id="balanceForm" action="../order/useBalance.do" method="post">
			<input type="hidden" name="orderId" value="${order.id }"> 
			<input id="balanceFlag" type="hidden" name="flag">
		</form>
		<c:if test="${counter.type!='直营'&& MyOrder.singleOrder.useBalance!=MyOrder.singleOrder.paymentFee}">
			<div class="pay_data">
				<sf:form action="../order/upload2.do" method="post"
					enctype="multipart/form-data">
					<input name="id" type="hidden" value="${order.id}" />
					<input name="orderNo" type="hidden" value="${order.orderNo}" />
					<div>
						<div style="margin: 10px 0;">
							<label for="paymentPrice"
								style="cursor: pointer; display: inline-block; width: 100px; float: left;">支付金额<b
								style="color: red">*</b></label> <input class="form-control"
								style="width: 100px; font-size: 18px; float: left;"
								id="paymentPrice" name="paymentPrice" min="0" step="0.01"
								type="number" required="required" value="${order.paymentPrice}">
							<div class="clear"></div>
						</div>
						<div style="margin: 10px 0;">
							<label for="description"
								style="width: 100px; display: inline-block; cursor: pointer; line-height: 75px; float: left; margin: 0px">备注</label>
							<textarea class="form-control" value="${order.description }"
								name="description" id="description"
								style="width: 80%; height: 75px; line-height: 25px; float: left;">${order.description }</textarea>
							<div class="clear"></div>
						</div>
					</div>
					<input id='ssi-uploadBtn' class='submit_left bg-black'
						value='提交附件' type="submit" disabled>
				</sf:form>
				<div>
					<input type="file" multiple id="ssi-upload3" />
				</div>
			</div>
			<div class="clear"></div>
		</c:if>
	</div>
	<%@ include file="common/commonF.jsp"%>
	<script src="../js/jquery.js"></script>
	<script src="../js/action.js"></script>
	<script src="../js/index.js"></script>
	<script src="../js/ssi-uploader.js"></script>
	<script>
		$(document).ready(function() {
							var showtime =parseInt($(".showtime").text());
							$(".showtime").text(showTime(new Date(showtime)));
							$(".priceFormat").each(function() {
								var money = parseFloat($(this).text());
								var formatM = money.toFixed(2);
								$(this).text(formatM);
							})
							$(".submit_button").click(function() {
								$(".message_body").show();
								$(".message_block2").html($(".pay_style").html());
							})
							$(".message_body").on("click","#pay_close",function() {
								$(".message_body").hide();
							});
							function getObjectURL(file) {
								var url = null;
								if (window.createObjectURL != undefined) { // basic
									url = window.createObjectURL(file);
								} else if (window.URL != undefined) { // mozilla(firefox)
									url = window.URL.createObjectURL(file);
								} else if (window.webkitURL != undefined) { // webkit or chrome
									url = window.webkitURL
											.createObjectURL(file);
								}
								return url;
							}
							$(".message_body").on("click","#pay_sure",function() {
												if ($('#offlinepay').attr(
														'checked')) {
													$(".pay_data").show();
													$(".message_body").hide();
													$(".pay_button").hide();
												}
												if ($('#onlinepay').attr('checked')) {
													var order_id = $('#orderid').val();
													//var newTab=window.open('about:blank');
													$.ajax({
																type : "get",
																url : "../order/UOUN.do",
																async : true,
																timeout : 15000,
																dataType : "text",
																data : {
																	orderID : order_id
																},
																cache : false,
																error : function(
																		XMLHttpRequest,
																		textStatus,
																		errorThrown) {
																},
																success : function(
																		data) {
																	if (data == "success") {
																		var target_url = "../alipay/index.do?orderID="
																				+ order_id;
																		//newTab.location.href=target_url;
																		window.location.href = "../alipay/index.do?orderID="
																				+ order_id;
																		$(".pay_button").hide();
																	}
																}
															});
												}
												if ($('#onlinecmbc').attr('checked')) {
													
													var order_id = $('#orderid').val();
													$.ajax({
																type : "get",
																url : "../order/UOUN.do",
																async : true,
																timeout : 15000,
																dataType : "text",
																data : {
																	orderID : order_id
																},
																cache : false,
																error : function(
																		XMLHttpRequest,
																		textStatus,
																		errorThrown) {
																},
																success : function(
																		data) {
																	if (data == "success") {
																		window.location.href = "../cmbc/index.do?orderID="+order_id;
																		$(
																				".pay_button")
																				.hide();
																	}
																}
															});
												}
											});

							$(".file_button")
									.click(
											function() {
												$(this)
														.change(
																function() {
																	var srcs = getObjectURL(this.files[0]);
																	if (srcs) {
																		$(this)
																				.parents()
																				.find(
																						".file_img")
																				.attr(
																						"src",
																						srcs); //this指的是input
																	}
																})

											});
							$(".img_data").click(function() {
								$(".position_body").show();
								var img = new Image();
								img.src = $(".file_img").attr('src');
								$(".bigimg").attr("src", img.src);
								var imgw = img.width;
								var imgh = img.height;
								var imgw_h = imgw / imgh;
								var winw = $(window).width();
								var winh = $(window).height();
								var winw_h = winw / winh;
								if (winw_h > 1 & imgw_h > winw_h) {
									$(".bigimg").height(winh / imgw_h * 0.8);
									$(".bigimg").width(winw * 0.8);
								} else if (imgw_h<1 && imgw_h>winw_h) {
									$(".bigimg").height(winh / imgw_h * 0.8);
									$(".bigimg").width(winw * 0.8);
								} else {
									$(".bigimg").height(winh * 0.8);
									$(".bigimg").width(winh * imgw_h * 0.8);
								}
								;
								var imgw1 = $(".bigimg").width();
								var imgh1 = $(".bigimg").height();
								$(".bigimg").width(imgw1);
								$(".bigimg").height(imgh1);
								var ww_iw = (winw - imgw1) / 2;
								var wh_ih = (winh - imgh1) / 2;
								$(".bigimg").css({
									"position" : "absolute",
									"top" : wh_ih,
									"left" : ww_iw
								});
							})
							$(".bigimg_close").click(function() {
								$(".position_body").hide();
							});
							$(".sure_goods").click(
									function() {
										$(".message_body").show();
										$(".message_block2").html(
												$(".sure_counter").html());
										$(".close_goods").click(function() {
											$(".message_body").hide();
										})

									})
							$(".submit_form").click(
									function() {
										$(".message_body").show();
										var form_submit=$(".form_submit").html();
										$(".message_block2").html("<div class='message_top'>提交</div><div class='message_center'>"+form_submit+"</div>");
									})
							$(".sure_cancel").click(function() {
										$(".message_body").show();
										var cancel_order=$(".cancel_order").html();
										$(".message_block2").html("<div class='message_top'>作废订单</div><div class='message_center'>"+cancel_order+"</div>");
										$(".cancel_invalid").click(function() {
											$(".message_body").hide();
										});
									});
							$(".return_cart").click(
									function() {
										$(".message_body").show();
										var return_order=$(".return_order").html();
										$(".message_block2").html("<div class='message_top'>返回购物车</div><div class='message_center'>"+return_order+"</div>");
										$(".cancel_invalid").click(function() {
											$(".message_body").hide();
										});
									});
							var extrajson;
							$('#ssi-upload3')
									.ssi_uploader(
											{
												url : '../order/uploadimage.do',
												data : {
													"id" : $('input[name="id"]').val(),
													"orderNo" : $('input[name="orderNo"]').val()
												},
												onUpload : function() {
													$
															.post(
																	"../order/uploaddata.do",
																	{
																		"id" : $('input[name="id"]').val(),
																		"paymentPrice" : $('input[name="paymentPrice"]').val(),
																		"description" : $('textarea[name="description"]').val()
																	},
																	function() {
																	});
												},
												dropZone : false,
												allowed : [ 'jpg', 'gif',
														'txt', 'png', 'pdf' ]
											});
							$("#balance").click(function() {
								var from = $("#balanceForm");
								if ($("#balance").is(':checked')) {
									//使用余额
									$("#balanceFlag").val(true);
								} else {
									//释放余额
									$("#balanceFlag").val(false);
								}
								from.submit();
							});

						});
	</script>
</body>
</html>
