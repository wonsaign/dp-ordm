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
<style>
.pay_submit {
	display: none;
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
		<h3 style="color: #6dbb44; text-align: center;">合并订单</h3>
		<input type="button" class="bg-red submit_order all_check" value="全选">
		<input type="button" class="bg-red submit_order others_check"
			value="反选"> <input type="button"
			class="bg-red submit_order combine" value="合并订单"> <input
			type="button" class="bg-red submit_order split" value="拆除合并">
		<form action="" id="orderNosForm">
			<div class="order_title">所有订单(最多选一个)</div>
			<table class="pay_detail" cellpadding="0" cellspacing='0'>
				<thead>
					<tr>
						<th>订单号</th>
						<th>柜台号</th>
						<th>商品金额</th>
						<th>折后金额:</th>
						<th>物料费用:</th>
						<th>运费:</th>
						<th>应付金额:</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${orders}" var="ods">
						<tr class="payOrderBox">
							<td><label title="${ods.counterId}"><input
									type="checkbox" name="orderNo" flag="payOrderBox"
									value="${ods.orderNo}">${ods.orderNo}</label></td>
							<td class="counterName">${ods.counterName}</td>
							<td>${ods.orderOriginalFee}</td>
							<td>${ods.productFee}</td>
							<td>${ods.materialFee}</td>
							<td>${ods.expressFee}</td>
							<td>${ods.paymentFee}</td>
							<td class="orderDetail"><a
								href="../order/detail.do?orderId=${ods.id}">查看明细</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="order_title">所有礼盒(最少选一个)</div>
			<table class="pay_detail" cellpadding="0" cellspacing='0'>
				<thead>
					<tr>
						<th>订单号</th>
						<th>柜台号</th>
						<th>商品金额</th>
						<th>折后金额:</th>
						<th>物料费用:</th>
						<th>运费:</th>
						<th>应付金额:</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${sysorder}" var="sysods">
						<tr class="paybox">
							<td><label><input type="checkbox" name="orderNos"
									flag="paybox" value="${sysods.orderNo}">${sysods.orderNo}</label></td>
							<td>${sysods.counterName}</td>
							<td>${sysods.orderOriginalFee}</td>
							<td>${sysods.productFee}</td>
							<td>${sysods.materialFee}</td>
							<td>${sysods.expressFee}</td>
							<td>${sysods.paymentFee}</td>
							<td class="orderDetail"><a
								href="../order/detail.do?orderId=${sysods.id}">查看明细</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</form>


		<div class="settle">
			<hr />
			<span class="information_settle">商品金额:￥<strong
				class="productAmt">${data[0]}</strong></span>
			<shiro:hasAnyRoles name="root,adm,14,12,11">
				<span class="information_settle">折后金额:￥<strong
					class="discountAmt">${data[1]}</strong></span>
				<br />
			</shiro:hasAnyRoles>
			<span class="information_settle red">额外物料费用:<strong
				class="extraMAmt">${data[4]}</strong></span><br /> <span
				class="information_settle">运费:￥<strong class="extraFree"></strong>${data[2]}</span><br />
			<c:if test="${MyOrder.singleOrder.orderStatus==7 }">
				<span class="information_settle">扣款金额:</span>
			</c:if>
			<div class="clear"></div>
			<shiro:hasAnyRoles name="root,adm,14,12,11">
				<hr />
				<span class="information_settle red">应付金额:<strong
					id="paymentFee">${data[3]}</strong></span>
			</shiro:hasAnyRoles>
			<div class="clear"></div>
			<div class='adressBox' style="text-align: right; margin: 10px auto">
				<span>收货门店:</span> <select id="counterId">
					<c:forEach items="${counters}" var="count">
						<option value="${count.counterId}">${count.counterName}</option>
					</c:forEach>
				</select>
			</div>
			<hr />
		</div>
		<div>
			<input type="button" value="确认合并" id="combine_sure"
				class="submit_order big bg-red" style="float: right; display: none;">
		</div>
		<div class="clear"></div>
	</div>
	<%@ include file="common/commonF.jsp"%>
	<script src="../js/action.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/action.js").lastModified() %>"></script>
	<script src="../js/jquery.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/jquery.js").lastModified() %>"></script>
	<script src="../js/index.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/index.js").lastModified()%>"></script>
	<script src="../js/ssi-uploader.js"></script>
	<script>
		$(document).ready(function() {
			$(".all_check").click(function() {
				$(".paybox").each(function() {
					$(this).find("input[flag='paybox']").attr('checked', true);
				})
			})
			$(".others_check").click(function() {
				$(".paybox").each(function() {
					if (!$(this).find("input[type='checkbox']").attr('checked')) {
						$(this).find("input[flag='paybox']").attr('checked', true);
					} else {
						$(this).find("input[flag='paybox']").attr('checked', false);
					}
				})
			})
			$(".payOrderBox label input[flag='payOrderBox']").click(function() {
				if ($(this).attr('checked')) {
					$("input[flag='payOrderBox']").attr('checked', false);
					$(this).attr('checked', true);
				}
			})
			var href = location.href;
			if (href.indexOf("getCombineOrder") > 0) {
				$("input[name='paybox']").attr('checked', true);
				$("input[name='paybox']").attr('disabled', true);
				$(".combine").hide();
				$(".split").show();
				$(".settle").show();
			} else {
				$(".pay_button").hide();
				$(".settle").hide();
			}
	
			$(".combine").click(function() {
				var $lrs = new Array();
				var i = 0;
				var j = 0;
				$(".pay_detail tr").each(function() {
					if ($(this).find("input[flag='paybox']").attr('checked')) {
						i++;
					}
					if ($(this).find("input[flag='payOrderBox']").attr('checked')) {
						j++;
					}
				});
				if (j == 1) {
					$(".adressBox").hide();
				}
				if (i + j > 1) {
					$.ajax({
						type : "get",
						url : "../preorder/getCombineOrder.do",
						async : false,
						timeout : 15000,
						dataType : "json",
						cache : false,
						data : $("#orderNosForm").serialize(),
						success : function(data) {
							$(".settle").show();
							$(".productAmt").text(data.data[0].toFixed(2));
							$(".discountAmt").text(data.data[1].toFixed(2));
							$(".extraFree").text(data.data[2].toFixed(2));
							$("#paymentFee").text(data.data[3].toFixed(2));
							$(".extraMAmt").text(data.data[4].toFixed(2));
							$("#combine_sure").show();
						},
						error : function() {}
					});
					$("input[type='checkbox']").attr("disabled", "disabled");
					$(".split").show();
					$(this).hide();
					$(".pay_button").show();
				} else {
					alert("合并订单数量不足,请最少选择两个订单");
				}
	
			});
			$('#combine_sure').click(function() {
				var counter = $('#counterId option:selected').text();
				var counterId = $("#counterId").val();;
				$(".pay_detail tr").each(function() {
					if ($(this).find("input[flag='payOrderBox']").attr('checked')) {
						counterId = $(this).find("label").attr("title");
						counter =$(this).find(".counterName").text();
					}
				});
				$(".message_block2").html("<div>您确认把订单都合并到<b style='color:red;'>" + counter + "</b>吗?。</div><div><button type='button' class='bid bg-red submit_order sure_com'>确定</button>"
					+ "<button type='button' class='big bg-red submit_order cancel_com'>取消</button></div>");
				$(".message_body").show();
				$(".sure_com").click(function() {
					$("input[type='checkbox']").attr("disabled", false);
					var orders = $("#orderNosForm").serialize();
					var params = orders + "&counterId=" + counterId;
					$("input[type='checkbox']").attr("disabled", true);
					$.ajax({
						type : "post",
						url : "../preorder/confirmCombine.do",
						async : false,
						timeout : 15000,
						dataType : "json",
						cache : false,
						data : params,
						success : function(res) {
							window.location.href = "../order/index.do"
						},
						error : function() {}
					});
				});
				$(".cancel_com").click(function() {
					$(".message_body").hide();
				})
			})
	
			$(".split").click(function() {
				window.location.reload();
			})
		})
	</script>

</body>
</html>
