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
		<style type="text/css">
			.custype li{
				float:left;
				padding:2px 10px;
				list-style: none;
			}
			.custype{
				width:100%;
				height: auto;
				margin:0px auto;
			}
		</style>
	</head>
	<body>
		<%@ include file="common/common_window.jsp"%>
		<script src="../js/jquery.js"></script>
 		<script src="../js/index.js"></script>
		<script src="../js/productpolicy.js"></script>
		<div class="show_body" value="${message}">
		<input type="hidden" value="${message}" id="hidden1">
			<c:if test="${message!=null&&''!=message}">${message}</c:if>
		</div>
		<sf:form action="../productpolicy/creatPRP.do" method="post" enctype="multipart/form-data" >
		<div style="width:100%; background:#e6e6e6;" class="div_body">
			<div style="width:750px; color:#5bc0de; text-align:center; padding:10px 50px; font-weight:bold; margin:0px auto; height:30px; background:white; border:2px solid white; border-radius:5px;"> 活动策略</div>
			<div style="width:750px; margin:10px  auto  0px auto; padding:20px 50px; background:white; border:2px solid white; border-radius:10px;">
			<div style="width:650px; margin:20px auto; min-height:350px; height: auto;">
			<div class="prp">
				<div>活动类型</div>
					<select>
						<option>买任意赠任意</option>
						<option>买自身赠自身</option>
						<option>满赠（大礼包）</option>
						<option>特价礼包</option>
						<option>特价礼包</option>
					</select>
					<div>
						<div>买任意赠任意</div>
						买<input type="number">赠<input type="number">
						<div>
							<span>买（产品）</span>
							<input type="text" >
							<select>
								<option>必选</option>
								<option>可选</option>
							</select>
							<input type="button" value="添加">
						</div>
						<div>
							<span>增（产品）</span>
							<input type="text" >
							<select>
								<option>必选</option>
								<option>可选</option>
							</select>
							<input type="button" value="添加">
						</div>
					</div>
					<div>
						<div>买自身赠自身</div>
						<div>
							<span>买（产品）</span>
							<input type="text" >
							<select>
								<option>必选</option>
								<option>可选</option>
							</select>
							<input type="button" value="添加">
						</div>
						<div>
							<span>增（产品）</span>
							<input type="text" >
							<select>
								<option>必选</option>
								<option>可选</option>
							</select>
							<input type="button" value="添加">
						</div>
					</div>
			</div> 	 
				</div>
				<div class="sure">
					<input type="button" <c:if test="${createflag}">disabled="disabled"</c:if> name="createPRP" value="保存" />
					<input type="button" value="返回" onclick="javascript:window.opener=null;window.open('','_self');window.close();"/>
				</div>				
			</div>
		</div>
	</sf:form>
	</body>
	<script>
		$(document).ready(function(){
			var wh= $(window).height()
			 $(".div_body").css("min-height",wh+"px");
			if($("#hidden1").val()!=null&&$("#hidden1").val()!=""){
				$(".hidden_body2").empty();
				$(".hidden_body").show();
				$(".hidden_body2").html($("#hidden1").val());
				$(".hidden_body").hide(5000); 
			}
			$(".custype label").click(function(){
				$(this).find("input[type='checkbox']").attr("disabled",false);
			})
			$(".status").change(function(){
				$(".status_value").val($(this).val());
			})
		});
	</script>
</html>