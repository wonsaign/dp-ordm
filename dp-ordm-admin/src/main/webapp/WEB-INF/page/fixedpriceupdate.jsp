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
			.price_type{
				display: none;
			}
		</style>
	</head>
	<body>
		<%@ include file="common/common_window.jsp"%>
		<script src="../js/jquery.js"></script>
 		<script src="../js/index.js"></script>
		<script src="../js/productpolicy.js"></script>
		<div class="show_body" value="${DSResponse.message}">
		<input type="hidden" value="${DSResponse.message}" id="hidden1">
			<c:if test="${ not empty DSResponse.message}">${DSResponse.message}</c:if>
		</div>
		<sf:form action="../fixedPrice/updateFixedPrice.do" method="post" enctype="multipart/form-data" >
		<div style="width:100%; background:#e6e6e6;" class="div_body">
			<div style="width:750px; color:#5bc0de; text-align:center; padding:10px 50px; font-weight:bold; margin:0px auto; height:30px; background:white; border:2px solid white; border-radius:5px;"> 价格策略</div>
			<div style="width:750px; margin:10px  auto  0px auto; padding:20px 50px; background:white; border:2px solid white; border-radius:10px;">
			<div style="width:650px; margin:20px auto; min-height:350px; height: auto;">
			<div class="prp">
			<div>
				<span style="float:left;margin-right:20px;">产品:</span>
				<div style="float:left; width:200px; position:relative;" class="gro_div">
					<input type="hidden" name="productId" class="pid" value="${DSResponse.data.productId}">
					<input type="text" name="productName" class="prp_t mat productName" value="${DSResponse.data.productName}" readonly="readonly">
					<%-- <ul class="gro_ul" style="margin:1px 17px;">
						<c:forEach items="${DSResponse.extra['product']}" var="pro">
						   <li class="on"><input type="hidden" value="${pro.id}">${pro.name}</li>
						</c:forEach>
					</ul> --%>
				</div>
				<div class="clear"></div>
			</div>
			<div>
			<span style="float:left;margin-right:20px;">一口价类型:</span>
				<select name="fix">
						<option value="1"<c:if test="${DSResponse.data.fix}">selected="selected" </c:if>>固定价格</option>
						<option value="0" <c:if test="${!DSResponse.data.fix}">selected="selected" </c:if>>固定折扣</option>
				</select>
			</div>
			
			<div class="price_type">
			<span style="float:left;margin-right:20px;">产品折扣:</span>
			<input type="number" class="prp_t mat" name="discount" placeholder="请输入产品折扣" value="${DSResponse.data.discount}">
			</div>
			<div class="price_type">
			<span style="float:left;margin-right:20px;">产品价格:</span>
			<input type="number" class="prp_t mat" name="price" placeholder="请输入产品价格" value="${DSResponse.data.price}">
			</div>
			<div>
			<span style="float:left;margin-right:20px;">客户折扣:</span>
				<select name="pricePolicy" class="status">
						<option value="1" <c:if test="${DSResponse.data.pricePolicy}">selected="selected" </c:if>>打折</option>
						<option value="0" <c:if test="${!DSResponse.data.pricePolicy}">selected="selected" </c:if>>不打折</option>
				</select>
			</div> 
		 <div>
			<span style="float:left;margin-right:20px;">是否费比:</span>
				<select name="costRatio">
						<option value="1" <c:if test="${DSResponse.data.costRatio}">selected="selected" </c:if>>走费比</option>
						<option value="0" <c:if test="${!DSResponse.data.costRatio}">selected="selected" </c:if>>不走费比</option>
				</select>
			</div>
			 <span style="float:left;margin-right:20px;">状态:</span>
				<select name="status" class="status">
						<option value="1" <c:if test="${DSResponse.data.status=='启用'}">selected="selected" </c:if>>启用</option>
						<option value="0" <c:if test="${DSResponse.data.status=='禁用'}">selected="selected" </c:if>>禁用</option>
				</select> 
			</div>
			<div class="clear"></div>
			<div>
				<div style="margin-right:20px;">客户类型:</div>
				<ul class="custype">
					<c:forEach items="${DSResponse.extra['customerType']}" var="allcus">
						<li><label>
							<input 
								<c:forEach items="${DSResponse.data.customerTypeId}" var="mycus">
		    						<c:if test="${mycus == allcus.hardCode}">checked="checked"</c:if>
		    				</c:forEach>
						 		type="checkbox" name="customerTypeId" value="${allcus.hardCode}" >
						 		${allcus.name}</label></li>
					</c:forEach>
					<div class="clear"></div>
				</ul>
				<div class="clear"></div>
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
			if($("#hidden1").val()!=null&&$("#hidden1").val()!=""&&$("#hidden1").val()!="[]"){
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
			var fix=$('select[name="fix"] option:selected').val();
			$(".price_type").eq(fix).show();
			$('select[name="fix"]').change(function(){
				var fix=$(this).val();
				$(".price_type").eq(fix).show();
				$(".price_type").eq(fix).siblings(".price_type").hide();
			})
		});
	</script>
</html>
    