<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.zeusas.core.utils.*" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title></title>
		<link rel="stylesheet" href="../css/index.css">
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
	</head>
	<body>
		<%@ include file="common/common_window.jsp"%>
		<script src="../js/jquery.js"></script>
		<script src="../js/pricepolicy.js"></script>
		<div class="show_body" value="${message}">
		<input type="hidden" value="${message}" id="hidden1">
			<c:if test="${message!=null&&''!=message}">${message}</c:if>
		</div>
		<sf:form action="../pricePolicy/update.do" method="post" enctype="multipart/form-data" >
		<div style="width:100%; background:#e6e6e6;" class="div_body">
			<div style="width:750px; color:#5bc0de; text-align:center; padding:10px 50px; font-weight:bold; margin:0px auto; height:30px; background:white; border:2px solid white; border-radius:5px;">折扣策略</div>
			<div style="width:750px; margin:10px  auto  0px auto; padding:20px 50px; background:white; border:2px solid white; border-radius:5px;">
				<div style="width:650px; margin:20px auto; min-height:350px; height: auto; line-height:30px;">
			 		<div>客户类型 <br>
						<input type="text" readonly="readonly" value="${PricePolicyBean.customerType}">
						<input type="text" name="customerTypeId" value="${PricePolicyBean.typeId}" hidden="hidden">
					</div>
					<div>折扣点数<br>
						<input type="text" name="discount" value="${PricePolicyBean.discount}"><a class="msg"></a>
						</div>
					<div>描述<br>
						<textarea name="description" class="description" rows="3" cols="40" maxlength="50" placeholder="最多45字">${PricePolicyBean.description}</textarea>
						<a></a>					
					</div>
				</div>
				<div class="sure">
					<input type="button" id="pricePolicyBtn" value="保存" />
					<input type="button" value="返回" onclick="javascript:window.opener=null;window.open('','_self');window.close();"/>
				</div>				
			</div>
		</div>
				
	</sf:form>
	</body>
	<script src="../js/jquery.js"></script>
	<script src="../js/index.js"></script>
	<script>
		$(document).ready(function(){
			var wh= $(window).height()
			 $(".div_body").css("min-height",wh+"px");
			if($("#hidden1").val()!=null&&$("#hidden1").val()!=""){
				$(".hidden_body2").empty();
				$(".hidden_body").show();
				$(".hidden_body2").html($("#hidden1").val());
				$(".hidden_body").hide(3000); 
			}
		});
	</script>
</html>