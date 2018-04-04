<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.zeusas.core.utils.*"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<link href="../css/index.css?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"css/index.css").lastModified()%>" rel="stylesheet" />
<link href="../css/style.css?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"css/style.css").lastModified()%>" rel="stylesheet">
<link href="../css/ssi-uploader.css" rel="stylesheet">
<link rel="shortcut   icon " href="../img/favicon.ico ">
<meta name="viewport"
	content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<style type="text/css">
.address_button button {
	margin: 0px 10px;
}
</style>
</head>
<body>
	<%@ include file="../common/commonHT.jsp"%>
	<div class="position_body">
		<img class="bigimg" src="../img/240_R6EVBIG35920151010161300.jpg">
		<img class="bigimg_close" src="../img/close.png">
	</div>
	<div class="center">
		<div class="order_number">
			<span class="order_span"> <input type="hidden"
				value="${order.id }" id="orderid"> 订单号 <span id="trade_no">${order.orderNo }</span>
				订单状态:
				<c:if test="${order.orderStatus==2}">
					<span class="red order_span">待付款订单</span>
				</c:if>
				<c:if test="${order.orderStatus==10}">
					<span class="red order_span">财务退回订单</span>
					<%-- <sf:form method="post" action="cancelfull.do">
							<input type="hidden" value="${order.id }" name="id">
							<input type="hidden" value="${order.orderNo }" name="orderNo">
							<input type="submit" value="取消订单"  class="submit_order submit_left big bg-red">
						</sf:form> --%>
				</c:if>
				<c:if test="${order.orderStatus==6}">
					<span class="red order_span">已执行推送订单</span>
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
			</span>
			<div style="text-align: right;">
					<c:if test="${order.orderStatus==2}">
					<c:if test="${order.merger==false}">
					<c:if test="${order.issysin!=1 && order.issysin!=2}">
						<input type="button"
							class="sure_cancel submit_order submit_left bg-red" value="作废订单">
					</c:if>		
						<div class="cancel_order">
							<sf:form method="post" action="cancelfull.do">
								<input type="hidden" value="${order.id }" name="id">
								<input type="hidden" value="${order.orderNo }" name="orderNo">
								<div>如果作废该订单，此订单将为无效订单！</div>
								<input type="submit" value="确认作废"
									class="submit_order submit_left big bg-red">
							</sf:form>
							<button class="cancel_invalid big bg-red submit_order">取消作废</button>
						</div>
					</c:if>
				</c:if>
				
				
				<c:if test="${order.orderStatus==10||order.orderStatus==2}">
					<c:if test="${order.merger==false}">
					<c:if test="${order.issysin!=1 && order.issysin!=2}">
						<input type="button" class="return_cart submit_order submit_left bg-red" value="返回购物车">
					</c:if>
						<div class="return_order">
							<sf:form method="post" action="rollBack.do">
								<input type="hidden" value="${order.id }" name="orderId">
								<div>如果作返回该订单，此订单车将重新提交审核！</div>
								<input type="submit" value="确认返回"
									class="submit_order submit_left big bg-red">
							</sf:form>
							<button class="cancel_invalid big bg-red submit_order">取消返回</button>
						</div>
					</c:if>
				</c:if>
				<c:if test="${order.orderStatus==0}">
					<button type="button" class="bg-red ord_del">删除订单</button>
				</c:if>
				<div class="clear"></div>
			</div>
			
			
		</div>
		<table class="detial">
			<thead>
				<tr class="blue">
					<th>序号</th>
					<th>商品名称</th>
					<th>会员价</th>
					<th>数量</th>
					<th>原价</th>
					<shiro:hasAnyRoles name="root,adm,14,12,11">
						<th>折扣价</th>
					</shiro:hasAnyRoles>
					<th>类型</th>
					<c:if test="${MyOrder.singleOrder.orderStatus==7 }">
						<th>实发</th>
					</c:if>
				</tr>
			</thead>
			<c:forEach items="${OrderDetails}" var="ODmap">
				<tbody>
					<c:forEach items="${ODmap.value }" var="ods">
						<tr class="order_detial">
							<td class="order_left blue"></td>
							<td title="${ods.orderDetail.productName }">
							<c:if test="${ods.orderDetail.detailType == 1}">
							<img class="shopping_nameReserve1" src="../img/owes1.png">
							</c:if>
							${ods.orderDetail.productName }
							</td>
							<td>${ods.orderDetail.memberPrice }</td>
							<td>${ods.orderDetail.quantity }</td>
							<td class="priceFormat">${ods.orderDetail.quantity*ods.orderDetail.memberPrice}</td>
							<shiro:hasAnyRoles name="root,adm,14,12,11">
								<td class="priceFormat">${ods.orderDetail.quantity*ods.orderDetail.unitPrice}</td>
							</shiro:hasAnyRoles>
							<td class="order_type">${ods.product.typeName }</td>
							<c:if test="${MyOrder.singleOrder.orderStatus==7 }">
								<td class="order_center"
									<c:if test="${ods.orderDetail.realQty<ods.orderDetail.quantity}">style="background: #f06671"</c:if>
									<c:if test="${ods.orderDetail.realQty>ods.orderDetail.quantity}">style="background: #66c28f"</c:if>>${ods.orderDetail.realQty }</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</c:forEach>
		</table>

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

		<div class="settle">
			<hr />
			<span class="information_settle">地址:${MyOrder.singleOrder.address}</span>
			<span class="information_settle">联系方式:${MyOrder.singleOrder.contact}
				| ${MyOrder.singleOrder.phone}</span> <span class="information_settle">柜台名称:${MyOrder.singleOrder.counterName}</span>
			<span class="information_settle">柜台号:${MyOrder.singleOrder.counterCode}</span>
			<br /> <span class="information_settle">商品金额:￥<strong>${MyOrder.singleOrder.orderOriginalFee}</strong></span>
			<span class="information_settle">商品数量:<strong>${MyOrder.singleOrder.totalNum}</strong></span><br />
			<shiro:hasAnyRoles name="root,adm,14,12,11">
				<span class="information_settle">折后金额:￥<strong>${MyOrder.singleOrder.productFee}</strong></span>
				<br />
			</shiro:hasAnyRoles>
			<span class="information_settle red">额外物料费用:<strong>${MyOrder.singleOrder.materialFee	}</strong></span><br />
			<span class="information_settle">运费:￥<strong>${MyOrder.singleOrder.expressFee}</strong></span><br />
			<c:if test="${MyOrder.singleOrder.orderStatus==7 }">
				<span class="information_settle">扣款金额:${MyOrder.singleOrder.realFee }</span>
			</c:if>
			<div class="clear"></div>
			<shiro:hasAnyRoles name="root,adm,14,12,11">
				<hr />
				<c:if test="${MyOrder.singleOrder.orderStatus!=2}">
					<span class="information_settle red">
						实付款:${MyOrder.singleOrder.paymentPrice} </span>
					<span class="information_settle red">使用余额:<span
						class="priceFormat">${MyOrder.singleOrder.useBalance}</span></span>
					<span class="information_settle red">应付金额:<strong
						id="paymentFee">${MyOrder.singleOrder.paymentFee}</strong></span>
				</c:if>
				<c:if test="${MyOrder.singleOrder.orderStatus==2}">
					<span class="information_settle red">应付金额:<strong
						id="paymentFee">${MyOrder.singleOrder.payable}</strong></span>
				</c:if>
				<c:if test="${order.merger==false}">
					<c:if test="${MyOrder.singleOrder.orderStatus==2 }">
						<span class="information_settle red"> <input
							type="checkbox"
							<c:if test="${(empty usefulBalance||usefulBalance<=0)&&(empty MyOrder.singleOrder.useBalance ||MyOrder.singleOrder.useBalance==0)}">disabled</c:if>
							<c:if test="${!empty MyOrder.singleOrder.useBalance&&MyOrder.singleOrder.useBalance!=0}">checked</c:if>
							id="balance"> <label for="balance"
							style="cursor: pointer";>使用余额:<span class="priceFormat" >${MyOrder.singleOrder.useBalance}</span></label>
						</span>
						<span class="information_settle red balance_surplus"><label
							for="balance">剩余余额:</label><span class="priceFormat" id="useBalance">${usefulBalance}</span></span>
					</c:if>
				</c:if>
			</shiro:hasAnyRoles>


			<div class="clear"></div>
			<hr />
		</div>
		<c:if test="${counter.type!='直营' }">
			<c:if test="${order.orderStatus >= 2}">
				<div style="margin: 0px 20px;">
					<span style="margin: 10px 20px;">备注：${order.description }</span>
				</div>
				<c:forEach items="${order.imageURL}" var="imgs">
					<div class="img_data">
						<img src="<%=AppConfig.getVfsPrefix() %>${imgs}" class="file_img">
					</div>
				</c:forEach>
				<div class="clear"></div>
			</c:if>
		</c:if>
		<!-- 订单判断样式 -->
		<c:if
			test="${MyOrder.singleOrder.orderStatus==2||MyOrder.singleOrder.orderStatus==10}">
			<c:if test="${MyOrder.singleOrder.checkDesc!=null}">
				<div class="settle">
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
						<input type="submit" class="bg-red sure_all" value="确认提交">
						<input type="button" class="bg-red cancel_all" value="取消提交">
					</div>
				</sf:form>
			</div>
			<c:if
				test="${counter.type!='直营'&& MyOrder.singleOrder.useBalance!=MyOrder.singleOrder.paymentFee}">
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
								<textarea class="form-control" name="description"
									id="description"
									style="width: 80%; height: 75px; line-height: 25px; float: left;">无</textarea>
								<div class="clear"></div>
							</div>
						</div>
						<input id='ssi-uploadBtn' class='submit_order big bg-red'
							value='提交附件' type="submit">
					</sf:form>
					<div>
						<input type="file" multiple id="ssi-upload3" />
					</div>
				</div>
				<div class="clear"></div>
				<c:if test="${order.merger==false}">
					<div class="pay_button">
						<c:if
							test="${message!='success' ||order.imageURL==null||''==order.imageURL }">
							<input type="button" style="font-size: 14px;"
								class="submit_button submit_order big bg-red" value="支付方式">
						</c:if>
					</div>
				</c:if>
			</c:if>
			<c:if
				test="${counter.type=='直营' ||(!empty MyOrder.singleOrder.useBalance&&MyOrder.singleOrder.useBalance!=0&&MyOrder.singleOrder.useBalance==MyOrder.singleOrder.paymentFee)}">
				<c:if test="${order.merger==false}">
					<input type="button" class="submit_form submit_order big bg-red"
						value="提交">
				</c:if>
				<div class="form_submit">
					<form action="../order/confirmdirect.do" method="post">
						<input type="hidden" name="">
						<div style="margin: 10px 0;">
							<input type="hidden" value="${order.id }" name="id"> <input
								type="hidden" value="${order.orderNo }" name="orderNo">
							<label for="description"
								style="width: 40px; display: inline-block; cursor: pointer; line-height: 36px; position: relative; top: -15px;">备注:</label>
							<textarea class="form-control" value="" name="description"
								id="description">${order.description }</textarea>
						</div>
						<input type="submit" style="font-size: 14px;"
							class="submit_order big bg-red massage" value="提  交">
					</form>
				</div>
			</c:if>
			<div class="clear"></div>
			<div class="pay_style">
				<div class="pay_style">
					<div class="pay_block">
						<div>
							<h3 style="margin: 0; padding: 10px 0; color: red;">
								<strong>支付方式</strong>
							</h3>
						</div>
						<c:if test="${usefulBalance >= 0}">
							<div>
								<h4 style="margin: 0;">线上支付</h4>
							</div>
							<div>
								<label for="onlinepay" style="font-size: 10px; cursor: pointer">
									<input style="padding: 0; margin: 0" id="onlinepay" type="radio"
									name="pay" value="aliPay"> 支付宝
								</label>
							</div> 
 						<!--	
 							民生付暂停20171114 by: 彭红兵
 							<div>
									<label for="onlinecmbc" style="font-size: 10px; cursor: pointer">
										<input style="padding: 0; margin: 0" id="onlinecmbc"
										type="radio" name="pay" value="aliPay"> 民生付
									</label>
								</div> 
						-->
						</c:if>
						<!-- 管理员可以上传图片 -->
						<!--
						<shiro:hasAnyRoles name="root,adm,21">	
						</shiro:hasAnyRoles>
						-->
					   		<div>
								<h4 style="margin: 0;">线下支付</h4>
							</div>
							<div>
								<input style="margin: 0" id="offlinepay" type="radio" name="pay"
									value="accessory"><label for="offlinepay"
									style="font-size: 10px; cursor: pointer">上传凭证</label>
							</div>
	
						<input type="button" value="支付" id="pay_sure"
							class="submit_order big bg-red"> <input type="button"
							value="取消支付" id="pay_close" class="submit_order big bg-red">
						<div class="pay_close">
							<img src="../img/close.png">
						</div>
					</div>
				</div>
			</div>
		</c:if>
		<!-- 订单判断结束 -->
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
			<input type="hidden" name="orderId" value="${order.id }"> <input
				id="balanceFlag" type="hidden" name="flag">
		</form>
		<c:if test="${MyOrder.singleOrder.orderStatus==7 }">
			<div class="logistics">
				<div>
					<ol>
						<c:forEach items="${logistics}" var="logis">
							<li>${logis}</li>
						</c:forEach>
					</ol>
				</div>
				<c:if test="${logistics== '[]'}">
					<div class="logistics_no">
						<div>
							亲，请要不着急，<br />货物较多，请耐心等候，<br />我们会火速发往您的手中。
						</div>
					</div>
				</c:if>
			</div>
		</c:if>
	</div>
	<%@ include file="common/commonF.jsp"%>
	<script src="../js/action.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/action.js").lastModified() %>"></script>
	<script src="../js/jquery.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/jquery.js").lastModified() %>"></script>
	<script src="../js/index.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/index.js").lastModified()%>"></script>
	<script src="../js/ssi-uploader.js"></script>
	<script>
		$(document).ready(function() {
			$(".ord_del").click(function() {
				$(".message_body").show();
				$(".message_block2").html("<div>您确认删除该订单，删除之后将不能恢复。<div class='address_button'>"
											+ "<button type='button' class='bg-red sure_del'>确定</button>"
											+ "<button type='button' class='bg-red cancel_cart'>取消</button></div></div>");
				var order_id = $('#orderid').val();
				$(".sure_del").click(function() {
					$.ajax({
						type : "post",
						url : "../order/orderDelete.do",
						async : true,
						timeout : 15000,
						dataType : "json",
						data : {
							orderNo : order_id
						},
						cache : false,
						error : function() {},
						success : function(data) {
							location.href="index.do"
						}
					});
				})
				$(".cancel_cart").click(function() {
					$(".message_body").hide();
				})
			})
			$(".priceFormat").each(function() {
				var money = parseFloat($(this).text());
				var formatM = money.toFixed(2);
				$(this).text(formatM);
			})
			$(".submit_button").click(function() {
				if(!$('#balance').attr('checked') && Number($("#useBalance").text()) > 0){
					$(".message_body").show();
					$(".message_block2").html("<div>您确定不使用余额</div><div><input type='button' class='submit_order big bg-red sure_useBalance' value='确定'><input type='button' class='submit_order big bg-red cancel_useBalance' value='取消'></div>");
					$(".sure_useBalance").click(function(){
						$(".pay_style").show();
						$(".message_body").hide();
					})
					$(".cancel_useBalance").click(function(){
						$(".message_body").hide();
					})
				}else{
					$(".pay_style").show();
				}
			})
			$(".pay_close").click(function() {
				$(".pay_style").hide();
			});
			$("#pay_close").click(function() {
				$(".pay_style").hide();
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
			$('#pay_sure').click(function() {
				if ($('#offlinepay').attr('checked')) {
					$(".pay_data").show();
					$(".pay_style").hide();
					$(".pay_button").hide();
				} 
				if ($('#onlinepay').attr('checked')) {
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
								window.location.href = "../alipay/index.do?orderID="
										+ order_id;
								$(
										".pay_button")
										.hide();
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
								window.location.href = "../cmbc/index.do?orderID="
										+ order_id;
								$(".pay_button").hide(); 
							}
						}
					});
				}
			});

			$(".file_button").click(function() {
				$(this).change(function() {
					var srcs = getObjectURL(this.files[0]);
					if (srcs) {
						$(this).parents().find(".file_img").attr("src",srcs); //this指的是input
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
						$(".message_block2").html(
								$(".form_submit").html());
					})
			
			$(".sure_cancel").click(
					function() {
						$(".message_body").show();
						$(".message_block2").html(
								$(".cancel_order").html());
						$(".cancel_invalid").click(function() {
							$(".message_body").hide();
						});
					});
			$(".return_cart").click(function() {
				$(".message_body").show();
				$(".message_block2").html($(".return_order").html());
				$(".cancel_invalid").click(function() {
					$(".message_body").hide();
				});
			});
			var extrajson;
			$('#ssi-upload3').ssi_uploader({
				url : '../order/uploadimage.do',
				data : {
					"id" : $('input[name="id"]').val(),
					"orderNo" : $('input[name="orderNo"]').val()
				},
				onUpload : function() {
					$.post("../order/uploaddata.do",{
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
