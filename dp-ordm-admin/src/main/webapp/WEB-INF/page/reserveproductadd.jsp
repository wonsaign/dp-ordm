<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.zeusas.core.utils.*"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title></title>
<link rel="stylesheet" href="../css/index.css">
<link rel="stylesheet" href="../css/foundation-datepicker.css">
<link rel="shortcut   icon " href="../img/favicon.ico ">
<style type="text/css">
.user_select {
	width: 204px;
	height: 30px;
	font-size: 20px;
	display: block;
	margin: 0px;
}

.sure {
	width: 300px;
	margin: 80px auto;
	z-index: 80;
}
</style>
</head>
<body>
	<%@ include file="common/common_window.jsp"%>
	<script src="../js/jquery.js"></script>
	<script src="../js/reserveproduct.js"></script>
	<div class="show_body" value="${message}">
		<input type="hidden" value="${message}" id="hidden1">
		<c:if test="${message!=null&&''!=message}">${message}</c:if>
	</div>
	<sf:form action="../reserveproduct/create.do" method="post"
		enctype="multipart/form-data">
		<div style="width: 100%; background: #e6e6e6;" class="div_body">
			<div
				style="width: 750px; color: #5bc0de; text-align: center; padding: 10px 50px; font-weight: bold; margin: 0px auto; height: 30px; background: white; border: 2px solid white; border-radius: 5px;">创建打欠规则</div>
			<div
				style="width: 750px; margin: 10px auto 0px auto; padding: 20px 50px; background: white; border: 2px solid white; border-radius: 5px;">
				<div
					style="width: 650px; margin: 20px auto; min-height: 350px; height: auto;">

					<ul class="user_text">
						<input id="hid_id" name="productId" type="hidden"
							value="${reserveProduct.productId}" />
						<%-- <li><span>产品名称</span><select id="sel_productId" class="user_select" name="productId">
	    					<option class="option" value="0">请选择</option>
		    				<c:forEach items="${product}" var="p" varStatus="s">
							 	<option class="option" value="${p.productId}">${p.name}</option>
							</c:forEach>
						 </select></li> --%>
						<li><span>产品名称</span>
							<div style=" width: 200px;"
								class="gro_div">
								<input type="text" name="productName"
									class="prp_t mat productName"
									value="${reserveProduct.productName}">
								<ul class="gro_ul" style="margin: 1px 17px;">
									<c:forEach items="${product}" var="pro">
										<li class="on"><input type="hidden"
											value="${pro.productId}">${pro.name}</li>
									</c:forEach>
								</ul>
							</div>
						</li>

						<%-- <input id="productName" name="productName" type="hidden" value="${reserveProduct.productName}"/> --%>
						<li><span>打欠起始时间</span><input class='time'
							name="reserveStart" type="text" readonly="readonly"
							value="<fmt:formatDate value='${reserveProduct.reserveStart}' pattern='yyyy-MM-dd HH:mm:ss'/>" /></li>
						<li><span>打欠截止时间</span><input class='time' name="reserveEnd"
							type="text" readonly="readonly"
							value="<fmt:formatDate value='${reserveProduct.reserveEnd}' pattern='yyyy-MM-dd HH:mm:ss'/>" /></li>
						<li><span>还欠起始时间</span><input class='time' name="excuteStart"
							type="text" readonly="readonly"
							value="<fmt:formatDate value='${reserveProduct.excuteStart}' pattern='yyyy-MM-dd HH:mm:ss'/>" /></li>
					</ul>
					<ul class="user_text">
						<%-- <input id="hid_status"  type="hidden" value="${reserveProduct.status}"/>
						<li><span>还欠完毕标记</span><select name="status" id="status" class="user_select">
	    					<option value="0">未完成</option>
		    				<option value="1">已完成</option>
						 </select></li>
						 <input id="hid_avalible"  type="hidden" value="${reserveProduct.avalible}"/>
		    			<li><span>状态</span><select name="avalible" id="avalible" class="user_select">
		    				<option value="1">启用</option>
	    					<option value="0">禁用</option>
						 </select></li> --%>
						<li><span>还欠截止时间</span><input class='time' name="excuteEnd"
							type="text" readonly="readonly"
							value="<fmt:formatDate value='${reserveProduct.excuteEnd}' pattern='yyyy-MM-dd HH:mm:ss'/>" /></li>
						<li><span>最后更新时间</span><input class='time' name="lastUpdate"
							type="text" readonly="readonly"
							value="<fmt:formatDate value='${reserveProduct.lastUpdate}' pattern='yyyy-MM-dd HH:mm:ss'/>" /></li>
						<li><span>创建人</span><input name="creator" readonly="readonly"
							type="text" disabled="disabled"
							value="<%=request.getRemoteUser()%>" /></li>
						<li><span>创建日期</span><input class='time' name="createTime"
							type="text" readonly="readonly" disabled="disabled"
							value="<fmt:formatDate value='${reserveProduct.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" /></li>
					</ul>
				</div>

				<div class="sure">
					<input type="submit" value="创建"
						<c:if test="${createflag}">disabled="disabled"</c:if> /> <input
						type="button" value="返回"
						onclick="javascript:window.opener=null;window.open('','_self');window.close();" />
				</div>
			</div>
		</div>

	</sf:form>
</body>
<script src="../js/jquery.js"></script>
<script src="../js/index.js"></script>
<script src="../js/foundation-datepicker.js"></script>
<script src="../js/foundation-datepicker.zh-CN.js"></script>
<script>
	$(document).ready(
			function() {
				$(".time").fdatepicker({
					format : 'yyyy-mm-dd hh:ii:ss',
					initialDate : new Date(),
					pickTime : true,
				});

				var rpId = $('#hid_id').val();
				$("#sel_productId   option[value='" + rpId + "']").attr(
						"selected", true);

				/* var status = $('#hid_status').val();
				alert(status);
				var int_status = status ? 1:0;
				$("#status option[value='"+int_status+"']").attr("selected",true);
				
				var avalible = $('#hid_avalible').val();
				var int_avalible = avalible ? 1:0;
				$("#avalible option[value='"+int_avalible+"']").attr("selected",true); */

				var wh = $(window).height()
				$(".div_body").css("min-height", wh + "px");
				if ($("#hidden1").val() != null && $("#hidden1").val() != "") {
					$(".hidden_body2").empty();
					$(".hidden_body").show();
					$(".hidden_body2").html($("#hidden1").val());
					$(".hidden_body").hide(5000);
				}
			});
</script>
</html>
