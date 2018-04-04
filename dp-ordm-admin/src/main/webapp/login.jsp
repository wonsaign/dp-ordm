<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
				color:#2c82cf;
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
			}
			.login_button{
				margin-top: 20px;
				width: 297px; 
				height: 50px; 
				background-image: url(img/button.png);
				background-size: 100% 100%;
				color: white;
				font-size: 24px;
				font-weight: bold;
				letter-spacing:20px;
				opacity: 0.7;
			}
			.login_bottom{
				width: 100%;
				height: 50px;
				text-align: center;
				line-height: 25px;
				
			}
		</style>
	</head>
	<body>
	<script src="js/jquery.js"></script>
	<script>
		$(document).ready(function(){
			$('.login_input').keydown(function(e){
				var username= $('[name="username"]').val();
				var password= $('[name="password"]').val();
				if(e.keyCode == 13&&username!=""&&password!=""){
					$(this).parent().submit();
				}
			});
		});
	</script>
	<div
		style="width: 100%; height: 100%; background: url(img/4.png); background-repeat: no-repeat; background-size: 100% 100%; position: fixed; z-index: -10;"></div>
	<div class="newname">植物医生订货管理系统</div>
	<div class="login_block">
		<c:if test="${message_login!=null}">
			<div>${message_login}</div>
		</c:if>
		<form action="admin/login.do" method="post">
			用户名：<input class="login_input" type="text" name="username"><br>
			 密码：<input class="login_input"type="password" name="password" > 
			 <input class="login_button" type="button" value="登录" onclick="this.form.submit()" />
		</form>
	</div>
	<div class="login_bottom">
		Copyright©2015-2016.<br> 北京明弘科贸有限责任公司版权所有
	</div>
</body>
</html>
    