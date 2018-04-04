<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no;charset=utf-8">
	<title>消息</title>
	<link href="css/index.css" rel="stylesheet">	
	<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">	
</head>
<body>
	<%@ include file="/common/commonHT.jsp"%>
	<div class="message_hide">
	<c:if test="${all_order_size!=null}">
		<div style="text-align: left;font-size: 14px; font-weight:bold;">订单消息</div>
		<div class="massage_show">
		<c:forEach items="${all_order_size}" var="m">
		<shiro:hasAnyRoles name="root,adm,14,12">
		<c:if test="${m.key=='owner'}">
			<a style="text-decoration: none;font-weight: bold;" href="../order/index.do">
				<span>待审核订单</span>
				(<span style="color:red;">${m.value}</span>)
			</a>
		</c:if>
		</shiro:hasAnyRoles>
		<shiro:hasAnyRoles name="root,adm,14,12,11">
		<c:if test="${m.key=='unPay'}">
			<a style="text-decoration: none;font-weight: bold;" href="../order/index.do">
				<span>待付款订单</span>
				(<span style="color:red;">${m.value}</span>)
			</a>
		</c:if>
		</shiro:hasAnyRoles>
		<shiro:hasAnyRoles name="root,adm,14,12,11,10">
		<c:if test="${m.key=='shipping'}">
			<a style="text-decoration: none;font-weight: bold;" href="../order/index.do">
				<span>待收货订单</span>
				(<span style="color:red;">${m.value}</span>)
			</a>
		</c:if>
		</shiro:hasAnyRoles>
		<shiro:hasAnyRoles name="root,adm,14,12">
		<c:if test="${m.key=='refuse'}">
			<a style="text-decoration: none;font-weight: bold;" href="../order/index.do">
				<span>财务退回单</span>
				(<span style="color:red;">${m.value}</span>)
			</a>
		</c:if>
		</shiro:hasAnyRoles>
		</c:forEach>
		</div>
	</c:if>	
		<div style="border-left-width: 3px; border-left-style: solid; border-left-color: #6dbb44; padding-left:10px; border-radius: 2px 0px 0px 2px;">系统消息</div>
		<div>
			<ol>
			<%if(notifications!=null) {%>
				<% for(int i = 0;i<notifications.size();i++){ %>
				<%Notification n = notifications.get(i); %>
					<li><%=n.getContent() %></li>
			<%
				}
			} 
			%>
			</ol>
		</div>
	</div>
	<script src="js/jquery.js"></script>
	<script src="js/index.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		$("img").each(function(){
			var src=$(this).attr("src");
			$(this).attr("src",src.substring(3))
		})
	})
	</script>
</body>
</html>