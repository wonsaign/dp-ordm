<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %> 
<!DOCTYPE html>
<%@ page import="com.zeusas.core.utils.*" %>
	<head>
		<meta charset="UTF-8">
		<title>订单</title>
		<link href="../css/index.css" rel="stylesheet">
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
		<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<script src="../js/jquery.js"></script>
		<script src="../js/action.js"></script>
		<script src="../js/index.js"></script>
		<style>
		body{
			background:#f3f3f3;
		}
		</style>
	</head>
<body>
	<%@ include file="common/commonHT.jsp"%>
    <div class="center">
   		<table class="intent_select" name="typeId" border="1" cellpadding="0" cellspacing="0">   			
					<c:forEach items="${orders}" var="os">
					<c:if test="${os.hardCode ==0||os.hardCode ==1||os.hardCode ==10}">
						<shiro:hasAnyRoles name="root,adm,14,12">
							<tr>
								<td class="select_td">
								<a href="../order/mobilesearch.do?typeId=${os.hardCode }">
								<c:forEach items="${all_order_size}" var="aos">
									<c:if test="${os.hardCode ==0&&aos.key=='invalid'}"><em>${aos.value}</em></c:if>
									<c:if test="${os.hardCode ==1&&aos.key=='owner'}"><em>${aos.value}</em></c:if>
									<c:if test="${os.hardCode ==10&&aos.key=='refuse'}"><em>${aos.value}</em></c:if>
								</c:forEach>
								<img src="../img/order_${os.hardCode }.png"><div name="typeName">${os.name }</div> <input name="typeId" type="hidden" value="${os.hardCode }">
								</a>
								</td>
							</tr>
						</shiro:hasAnyRoles>
					</c:if>
					<c:if test="${os.hardCode ==2||os.hardCode ==3}">
						<shiro:hasAnyRoles name="root,adm,14,12,11">
							<tr>
							<td class="select_td">
							<a href="../order/mobilesearch.do?typeId=${os.hardCode }">
							<c:forEach items="${all_order_size}" var="aos">
								<c:if test="${os.hardCode ==2&&aos.key=='unPay'}"><em>${aos.value}</em></c:if>
								<c:if test="${os.hardCode ==3&&aos.key=='doPay'}"><em>${aos.value}</em></c:if>
							</c:forEach>
							<img src="../img/order_${os.hardCode }.png"><div name="typeName">${os.name }</div> <input name="typeId" type="hidden" value="${os.hardCode }">
							</a>
							</td>
							</tr>
						</shiro:hasAnyRoles>
					</c:if>
				 <c:if test="${os.hardCode ==6||os.hardCode ==7}">
						<shiro:hasAnyRoles name="root,adm,14,12,11,10">
							<tr>
							<td class="select_td">
							<a href="../order/mobilesearch.do?typeId=${os.hardCode }">
							<c:forEach items="${all_order_size}" var="aos">
								<c:if test="${os.hardCode ==6&&aos.key=='waitShip'}"><em>${aos.value}</em></c:if>
								<c:if test="${os.hardCode ==7&&aos.key=='shipping'}"><em>${aos.value}</em></c:if>
							</c:forEach>
							<img src="../img/order_${os.hardCode }.png"><div name="typeName">${os.name }</div> <input name="typeId" type="hidden" value="${os.hardCode }">
							</a>
							</td>
							</tr>
						</shiro:hasAnyRoles>
					</c:if>
 			</c:forEach>
		 </table>
	</div>
	<%@ include file="common/commonF.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	$(".intent_select td").each(function(){
		if($(this).find("em").text()==0||$(this).find("em").text()==''){
			$(this).find("em").hide();
		}
		var typeId=$(this).find("input[name='typeId']").val();
		if(typeId==1){
			$(this).find("a").attr("href","../cart/mobilecart_manage.do?typeId="+typeId);
		}
	});
})
</script> 
</body>
</html>
