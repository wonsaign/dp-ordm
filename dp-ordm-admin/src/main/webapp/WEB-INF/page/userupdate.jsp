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
		<script src="../js/user.js"></script>
		<div class="show_body" value="${message}">
		<input type="hidden" value="${message}" id="hidden1">
			<c:if test="${message!=null&&''!=message}">${message}</c:if>
		</div>
		<sf:form action="../useradm/updateuser.do" method="post" enctype="multipart/form-data" >
		<div style="width:100%; background:#e6e6e6;" class="div_body">
			<div style="width:750px; color:#5bc0de; text-align:center; padding:10px 50px; font-weight:bold; margin:0px auto; height:30px; background:white; border:2px solid white; border-radius:5px;"> 用户修改</div>
			<div style="width:750px; margin:10px  auto  0px auto; padding:20px 50px; background:white; border:2px solid white; border-radius:10px;">
				<div style="width:650px; margin:20px auto; min-height:350px; height: auto;">
					
					<ul class="user_text">
	    				<li style="display: none;">id<input type="text" value=""></li>
		    			<li><span>用户名</span><input name="loginName" readonly="readonly" type="text"   value="${authUser.loginName}" readonly="true"/></li>
		    			<li><span>密码</span><input name="password" type="password" value=""  value=""/></li>
		    			<li><span>姓名</span><input name="commonName" type="text" value="${authUser.commonName}"/></li>
		    			<li><span>手机号</span><input name="mobile" type="text" value="${userDetail.mobile}"/></li>  				
	    			</ul>
	    			<div id="org_li">
	    				<span>组织机构</span>
	    				<input id="org_change" type="button" value="修改组织" href="../useradm/orgChange">
	    				<input type="hidden"  name='org_cascade_url' href="../org/Child">
	    				<input id="orgid" type="hidden" value="${ authUser.orgUnit==null?0:authUser.orgUnit}"  name='orgUnit'>
	    				<select class="user_select" name="orgUnit0" disabled="disabled">
		    				<c:forEach items="${Orglist}" var="ol" varStatus="s">
							 	<option value="${ol.orgId}">${ol.commonName}</option>
							</c:forEach>
						</select>
	    			</div>
				</div>
				<div class="user_role">
					<span>角色</span>
    				<input id="roleset" type="hidden" value=""  name='roles'>
    				<input  name="Role_cascade" type="button" value="更改角色" href="../useradm/cascade">
	    			<ul name="roles_ul">
						<c:forEach items="${allRoles}" var="allr" varStatus="vs">
		    				<input
		    					<c:forEach items="${myRoles}" var="myr">
		    						<c:if test="${myr.rid == allr.rid}">checked="checked"</c:if>
		    					</c:forEach>  
		    					type="checkbox" name="rid" id="${allr.rid }" value="${allr.rid}">
		    						<label for="${allr.rid }">${allr.commonName}</label>&nbsp;
		    			</c:forEach>
		    			<div class="clear"></div>
					</ul>
    			</div>
				<div class="sure">
					<input type="button" value="修改" onclick="this.form.submit()" />
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
				$(".hidden_body").hide(5000); 
			}
		});
	</script>
</html>
