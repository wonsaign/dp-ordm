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
		<link rel="stylesheet" href="http://www.jq22.com/jquery/font-awesome.4.6.0.css">
		<link href="../css/foundation.min.css" rel="stylesheet" type="text/css">
		<link href="../css/foundation-datepicker.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" href="../css/index.css">
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
		<style>
	     	input[type="text"]{
				width:200px;
				height:35px;
			}
			.dis_select{
				width:200px;
				height:35px;
			}
			.pro_gro{
			    font-weight: bold;
    			line-height: 40px;
			}
			input[type="number"]{
				width:80px;
				height:35px;
				display:inline-block;
				margin:0px 10px;
			}
			.cha_select {
			    width: 80px;
			    height: 35px;
			    font-size: 18px;
			    float: left;
			}
		</style>
	</head>
	<body style="height: 100%;">
		<%@ include file="common/common_window.jsp"%>
		<script src="../js/jquery.js"></script>
		<script src="../js/foundation-datepicker.js"></script>
		<script src="../js/foundation-datepicker.zh-CN.js"></script>
		<div class="show_body" value="${message}">
		<input type="hidden" value="${message}" id="hidden1">
			<c:if test="${message!=null&&''!=message}">${message}</c:if>
		</div>
		<sf:form action="../settingadm/updatesave.do" method="post" style="height:100%;">
		<div style="width:100%;height:100%; background:#e6e6e6;" class="div_body">
			<div style="width:750px; color:#5bc0de; text-align:center; padding:10px 50px; font-weight:bold; margin:0px auto; height:50px; background:white; border:2px solid white; border-radius:5px;">配置修改</div>
			<div style="width:750px; margin:10px  auto  0px auto; padding:20px 50px; background:white; border:2px solid white; border-radius:5px;">
				<div style="width:650px; margin:20px auto; min-height:350px; height: auto;">
					<input type="hidden" name="did" value="${dic.did}">
					<div style="padding-left: 25%;">
						<span>${dic.summary}：</span>
						<span>￥</span>
						<input type="number"  name="value" value="${dic.name}" style="width: 100px;height: 35px;">
					</div>
					<div class="sure">
					<input type="submit" value="保存" class="open_hidden"/>
					<input type="button" value="关闭" onclick="javascript:window.opener=null;window.open('','_self');window.close();"/>
				</div>	
				</div>
			</div>
		</div>
	</sf:form>
	<script>
		$(document).ready(function(){
			 $(".div_body").height($(window).height());
			if($("#hidden1").val()!=null&&$("#hidden1").val()!=""){
				$(".hidden_body2").empty();
				$(".hidden_body").show(); 
				$(".hidden_body2").html($("#hidden1").val());
				$(".hidden_body").hide(3000); 
			}
		});
	</script>
	</body>
</html>   