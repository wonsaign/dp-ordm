<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>   
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title></title>
		<link href="../css/index.css?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"css/index.css").lastModified()%>" rel="stylesheet" />
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
		<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"> 
	</head>
	<body>
		<%@ include file="common/commonHT.jsp"%>
		<div class="center">
			<!-- <div class="search">
				<form>
					<input class="text_search" type="text" />
					<input class="button_search" type="button" value="搜索" style="background:#72b123; color: white;" />
				</form>
			</div>	 -->	
			 <!-- 头部 -->
			 <h3>待审核订单</h3>
			 <div class="cart_intent">
				 <ul class="cart_ul1">
				    <li>ID</li>
				    <li>用户名</li>
				    <li>柜台号</li>
				    <li>柜台名称</li>
				    <li>提交日期</li>
				    <li>编辑</li>
				 </ul>
				 <c:forEach items="${lcarts }" var="cart">
				 <ul class="cart_ul2">
				    <li>${cart.cartId }</li>
				    <li>${cart.userName }</li>
				    <c:forEach items="${lcounters }" var="counter">
				    <c:if test="${counter.counterId==cart.counterId }">
				    <li>${counter.counterCode }</li>
				    <li>${counter.counterName }</li>
				    </c:if>
				    </c:forEach>
				    <li>${cart.lastUpdate }</li>
				    <li><a href="../cart/cart_managedetail.do?cartId=${cart.cartId }"> <span class="audit">审核</span> </a></li>
				 </ul>
				 </c:forEach>
			 </div>
				 
			 <!-- 手机模块 -->
			 
			 <!-- 明细清单-->
			 			 
		</div>
		<%@ include file="common/commonF.jsp"%>
	</body>
<script src="../js/jquery.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/jquery.js").lastModified() %>"></script>
<script src="../js/index.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/index.js").lastModified()%>"></script>
</html>
