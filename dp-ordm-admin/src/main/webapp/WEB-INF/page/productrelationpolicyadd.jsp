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
 		<script src="../js/index.js"></script>
		<script src="../js/productpolicy.js"></script>
		<div class="show_body" value="${message}">
		<input type="hidden" value="${message}" id="hidden1">
			<c:if test="${message!=null&&''!=message}">${message}</c:if>
		</div>
		<sf:form action="../productpolicy/creatPRP.do" method="post" enctype="multipart/form-data" >
		<div style="width:100%; background:#e6e6e6;" class="div_body">
			<div style="width:750px; color:#5bc0de; text-align:center; padding:10px 50px; font-weight:bold; margin:0px auto; height:30px; background:white; border:2px solid white; border-radius:5px;"> 产品策略</div>
			<div style="width:750px; margin:10px  auto  0px auto; padding:20px 50px; background:white; border:2px solid white; border-radius:10px;">
				<div style="width:650px; margin:20px auto; min-height:350px; height: auto;">
					<input type="hidden" name="policyId" value="${policyBean.productRelationPolicy.policyId}">
		<div class="prp">
			<div>策略名称:
				<input name="name" type="text" class="prp_t" value="${policyBean.name}">
				<a class="msg"></a>
			</div>
			<div>
				<span style="float:left;margin-right:20px;">产品:</span>
				<div style="float:left; width:200px; position:relative;" class="gro_div">
					<input type="hidden" name="pId" class="pid">
					<input type="text" name="productName" class="prp_t mat productName">
					<ul class="gro_ul"  style="margin:1px 17px;">
						<c:forEach items="${DSResponse.extra['product']}" var="pro">
						   <li class="on"><input type="hidden" value="${pro.id}">${pro.name}</li>
						</c:forEach>
					</ul>
				</div>
				<div class="clear"></div>
				<a class="msg"></a>
				<ul name="product_ul"  hidden="hidden">
				</ul>
			</div>
			<div>策略等级:
				<input name="level" type="text" class="prp_t" value="${policyBean.level}">
				<a class="msg"></a>
				</div>
			<div>最小订货单位:
				<input name="minOrderUnit" type="text" class="prp_t" value="${policyBean.minOrderUnit}">
				<a class="msg"></a>
			</div>
			<div>策略类型:
				<select name="type">
					<c:if test="${not empty PRPtype}">
						<c:forEach items="${PRPtype}" var="type">
							<c:if test="${policyBean.productRelationPolicy.type == type.hardCode}">
								<option value="${type.hardCode}" selected="selected">${type.name}</option>
							</c:if>
							<c:if test="${policyBean.productRelationPolicy.type != type.hardCode}">
								<option value="${type.hardCode}" >${type.name}</option>
							</c:if>
						</c:forEach>
					</c:if>
					<c:if test="${empty PRPtype}">
						<c:forEach items="${PRPtype}" var="type">
							<option value="${type.hardCode }">${type.name }</option>
						</c:forEach>
					</c:if>
				</select>
			</div>
			<div><input name="prpset" hidden="hidden" type="text"></div>
			<%-- <div>
				<span style="float:left;margin-right:20px;">物料:</span>
				<div style="float:left; width:215px;  position:relative;">
					<input type="hidden" class="pid">
					<input type="text" class="prp_m mat" >
					<ul class="gro_ul" style="margin:1px 17px;">
					</ul>
				</div>
				<span name="addmateriel" style="float:left;color:blue;">添加</span>
				<div class="clear"></div>
				<a class="msg"></a>
				<ul name="product_ul"  hidden="hidden">
				</ul>
			</div>
			<div class="materiel_div">
				<span> 已选物料:</span>
				<ul name="materiel_ul">
					<c:forEach items="${policyBean.materielBean}" var="m" varStatus="s">
						<div name="materiel" style="height:23px">
							<input class="pid" type="hidden" value="${m.pid}"><span>${m.name}:</span>
							<input class="coeff" type="text" value="${m.coeff}"><span name="delmateriel" style="color:red;cursor:pointer;">删除</span>
							<a class="msg"></a>
						</div>
					</c:forEach>
				</ul>
			</div> --%>
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
		});
	</script>
</html>
    