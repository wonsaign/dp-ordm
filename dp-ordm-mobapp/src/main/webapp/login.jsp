<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no;charset=utf-8" > 
	<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
		<title>Login</title>
		<style>
		    body{
		    	padding: 0px;
		    	margin: 0px auto;
		    }
			.newname{
				width: 100%;
				height: 100px;
				text-align: center;
				font-size: 50px;
				line-height: 200px;
				color:#72b123;
			}
			.login_block{
				width: 300px; 
				height: 320px; 
				margin: 50px auto; 
				line-height: 60px;
				font-size: 26px;
			}
			.login_input{
				width: 293px;
				height: 40px;
				line-height:40px;
				border:1px solid #737571;
				font-size:20px;
			}
			.login_bottom{
				width: 100%;
				height: 50px;
				text-align: center;
				line-height: 25px;	
			}
			.login_sub{
				width: 295px;
				height: 50px;
				margin:20px auto;
				line-height:50px;
				border:1px solid #737571;
				background-image: url(img/submit.png);
				background-repeat-x:100%;
				color:white;
				font-size:28px;
				font-weight:bold;
				letter-spacing:20px;
				text-align: center;
			}
			@media screen and (max-width:600px) and (min-width: 400px){
			.newname{
				font-size: 30px;
				line-height: 100px;
				height:100px;
			}
			.login_block{
			    margin:0px auto; 
			    width:300px;
			    height:auto;
			    line-height: 40px;
			}
			}
			@media screen and (max-width:400px){
			.newname{
				height:80px;
				font-size: 28px;
				line-height: 80px;
			}
			.login_block{
			    margin:0px auto; 
			    width:70%;
			    line-height: 40px;
			}
			.login_input{
				width:99%;
			}
			.login_sub{
				width: 99%;
			}
			}
		</style>
	</head>
	<body>
		<%
		  request.getRemoteUser();
		if(request.getRemoteUser()!=null){
			response.setHeader("refresh","0;URL=ordm/index.do");
		}
		  %>
		<div style="width: 100%; height:100%; background:url(img/login.jpg); background-repeat: repeat-y; background-size: 100% 100%; position: fixed;z-index: -10;"></div>
		<div class="newname">
			植物医生直发订货系统
		</div>
		<div class="login_block">
			<c:if test="${message_login!=null && message_login!='用户名或密码不正确'}">
				<div class="login_block_msg">
					${message_login}
				</div>
			</c:if>
			<form method="post" action="login.do">
				<div>用户名：</div>
				<input class="login_input" type="text" value="" name="username"><br>
				<div>密码:</div>
				<input class="login_input" type="password" value="" name="password">
				<input class="login_button login_sub" type="submit" value="登录" />		 
			</form>
		</div>
		<div class="login_bottom">
			Copyright©2015-2016<br>
			北京明弘科贸有限责任公司版权所有
		</div>
	</body>
</html>