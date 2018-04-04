<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no;charset=utf-8"> 
		<title>权限设置</title>
		<link href="../css/index.css" rel="stylesheet">
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
		<style type="text/css">
			 body{
			 	background: #f3f3f3;
			 }   
		</style>
	</head>
	<body>
		<%@ include file="common/commonHT.jsp"%>
	    <div class="center">
	    	<form action="findusers.do" method="post">
	    	<div class="setting_edit">
		    	<div class="setting_text_search">
		    		<input value="" name="searchkey" type="text" class="search_text" maxlength="20" />
				 		<input value="查询" type="button" class="setting_button bg-green" onclick="this.form.submit()"/>
		    		<div class="clear"></div>
		    	</div>
				 <a href="../user/useradd.do" class="bg-green setting_right"/>创建</a> 
		    	<div class="clear"></div>
	    	</div>
	    	</form>
			 <div class="setting_edit">
			 <c:forEach items="${userBean}" var="user">
			 <div class="setting_one">
				 <c:if test="${user.authUser.status==1}">
				 		<div class="gborder"></div>
				 </c:if>
				 <c:if test="${user.authUser.status==0}">
				 		<div class="cborder"></div>
				 </c:if>
				 <div class="setting_name">
					 <div>
				 			<div class="setting_left">用户名:${user.authUser.loginName}</div>
				 			<div class="setting_left">姓名:${user.authUser.commonName }</div>
				 			<div class="clear"></div>
				 		</div>
				 		<div>角色:${user.roles}</div>
			 	 </div>
			 	 <a href="../user/userupdate.do?loginName=${user.authUser.loginName}&status=${user.authUser.status}">
				 		<img src="../img/edit.png">
			 	 </a>
			 	 <div class="clear"></div>
			 </div>
			 </c:forEach>
			 	<%-- <ul class="edit">	
				 	<li class="li_one" >编辑</li>
				 	<li class="li_two" >用户名</li>
				 	<li class="li_two" >姓名</li>
				 	<li class="li_two" >角色</li>
				 	<li class="li_two" >状态</li>
				 	<div class="clear"></div>
				</ul>
				<c:forEach items="${userBean}" var="user">
					<ul class="edit_row">
					 	<li class="li_one">
					 			<form method="post" action="../user/userupdate.do" class="set_amend">
							 		<input  name="loginName" type="hidden" value="${user.authUser.loginName}">
							 		<input name="status" type="hidden" value="1">
							 		<input class="set_amend btn-info" type="button" value="修改" onclick="this.form.submit()">
						 		</form>
					 		<c:if test="${user.authUser.status==0}">
							 	<form method="post" action="changestatus.do" class="set_amend">
							 		<input  name="uid" type="hidden" value="${user.authUser.uid}">
							 		<input name="status" type="hidden" value="1">
							 		<input class="set_start btn-success" type="button" value="启用" onclick="this.form.submit()">
						 		</form>
					 		</c:if>
					 		<c:if test="${user.authUser.status==1}">
						 		<form method="post" action="changestatus.do" class="down">
						 			<input  name="uid" type="hidden" value="${user.authUser.uid}">
						 			<input name="status" type="hidden" value="0">
							 		<input class="set_down bg-red" type="button" value="禁用" onclick="this.form.submit()">	
						 		</form>
					 		</c:if>
					 	</li>
					 	<li class="li_two" >${user.authUser.loginName}</li>
					 	<li class="li_two" >${user.authUser.commonName }</li>
					 	<li class="li_two" >${user.roles}</li>
					 	<c:if test="${user.authUser.status==1}">
					 		<li class="li_two">启用</li>
					 	</c:if>
					 	<c:if test="${user.authUser.status==0}">
					 		<li class="li_two">禁用</li>
					 	</c:if>
					 	<div class="clear"></div>
				 	</ul>
				 </c:forEach> --%>
			 </div>			 
		</div>
		<%@ include file="common/commonF.jsp"%>
		<script src="../js/jquery.js"></script>
		<script src="../js/index.js"></script>
		<script src="../js/setting.js"></script>
		<!-- baidu bridge  -->
		<script>
		var _hmt = _hmt || [];
		(function() {
		  var hm = document.createElement("script");
		  hm.src = "https://hm.baidu.com/hm.js?ffee89c382763b7bb90900c659ca5017";
		  var s = document.getElementsByTagName("script")[0]; 
		  s.parentNode.insertBefore(hm, s);
		})();
		</script>
	</body>
</html>
