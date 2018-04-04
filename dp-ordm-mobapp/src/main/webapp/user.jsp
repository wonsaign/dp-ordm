<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no;charset=utf-8">
		<title>用户</title>
		<link href="../css/index.css" rel="stylesheet">
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
		<link href="../css/datepicker3.css" rel="stylesheet">
		<style type="text/css">
			body{
				background:#f3f3f3;
			}
		</style>
	</head>
	<body>
		<%@ include file="../common/commonHT.jsp"%>
	    <div class="center">
	    	<div  class="user_top">
    			<div class="user_detail">
	    			<div class="user_img1">
	    				<img src="../img/userhead.jpg">
	    				<div class="user_detail1">
			    			<span class="user_span1">用户名：<%=request.getRemoteUser() %></span>
			    		</div>
			    		<div class="clear"></div>
	    			</div>
	    			<div class="user_img">
	    				<div>
	    					<img src="../img/balance.png">
	    					<span class="user_span">${usefulBalance}</span>
	    					<div class='clear'></div>
	    				</div>
	    				<div>
	    					<img src="../img/name.png">
	    					<span class="user_span">${name}</span>
	    					<div class='clear'></div>
	    				</div>
	    				<div>
	    					<img src="../img/call.png">
	    					<span class="user_span">${mobile}</span>
	    					<div class='clear'></div>
	    				</div>
	    			</div>
	    			<div class="clear"></div>
	    		</div>
	    		<div class="user_bottom">
	    		<!-- <div class="user_left">在线咨询</div> -->
	    		<div style="text-align: center;"><a href="../user/balanceinit.do">余额明细</a></div>
	    		</div>
	    	</div>
	    	<div>
	    	<table class="intent_select"> 
	    		<shiro:hasAnyRoles name="root,adm,13,14"> 
	    	  <tr>
	    	  	<td class="select_td">
	    	  		<a href="../user/setting.do"><img src="../img/user_1.png"><div name="typeName">权限设置</div></a>
	    	  	</td>
	    	  </tr> 
	    	  </shiro:hasAnyRoles>
	    	  <tr>
	    	  	<td class="select_td">
	    	  	<a href="../user/userupdate.do?loginName=<%=request.getRemoteUser() %>&status=1">
	    	  		<img src="../img/user_2.png"><div name="typeName">修改信息</div>
	    	  	</a>
	    	  	</td>
	    	  </tr> 	
	    	  <tr>
	    	  	<td class="select_td">
	    	  		<a href="../msg.do"><img src="../img/user_3.png"><div name="typeName">消息中心</div></a>
	    	  	</td>
	    	  </tr>		
		 		</table>
		 		<table class="intent_select"> 
	    	  <tr>
	    	  	<td class="select_td" onclick="window.android.startGuideActivity()">
	    	  		<img src="../img/user_4.png"><div name="typeName">新手指南</div>
	    	  	</td>
	    	  </tr> 
	    	  <tr>
	    	  	<td class="select_td" onclick="window.android.startUpdateApp()">
	    	  		<img src="../img/user_5.png"><div name="typeName">版本更新</div>
	    	  	</td>
	    	  </tr> 	
	    	  <tr>
	    	  	<td class="select_td" onclick="window.android.startAboutActivity()">
	    	  		<img src="../img/user_6.png"><div name="typeName">关于我们</div>
	    	  	</td>
	    	  </tr>		
		 		</table>
		 		<table class="user_back"> 
			 		<td class="select_td" onclick="window.android.startLogOut()">
			 				<div class="user_backdiv"><img id="user_backimg" src="../img/user_back.png">退出账号</div>
			 		</td>
		 		</table>
	    	</div>
		</div>
		<%@ include file="common/commonF.jsp"%>
	 	<script src="../js/jquery.js"></script>
	 	<script src="../js/index.js"></script>
		<script>
				$(".icon1").css("background","transparent")
		    $(document).ready(function(){
		    $(".user_balance").text(parseInt($(".user_balance").text()).toFixed(2));
		    $('.old_password').css('display','none');
				$('.new_password').css('display','none');
				$('.translate').click(function(){
				    var s=$(this).parent().find('input');	
				    s.attr("readonly",false);
				    s.css({'background':'white','border':'1px solid #828488'});
				});
		 		$('#logout').click(function(){
					window.location.href='../logout.do';
				}) ;
				$('.translate_password').click(function(){
					$('.old_password').css('display','block');
					$('.old_password input').css({'background':'white','border':'1px solid #828488'});
				})
				$('.password_sure').click(function(){
					var pwd = $(this).prev().val();
					if(pwd==""||pwd==null){
						$(".message_block2").html("密码不能为空！");
				 	 	$(".message_body").css("display","block");
						return;
					}
				 	$.ajax({
						type:"POST",
						url:"../user/checkpass.do",
						data:{oldpass:pwd},
						async:false,
						success:function(data){
							if(data=="success"){
								$('.new_password').css('display','block');
								$('.new_password input').css({'background':'white','border':'1px solid #828488'});
							}
							else{
								$(".message_block2").html("密码错误！");
						 	 	$(".message_body").css("display","block");
							}
						}
					}); 
				})
				/* baidu bridge */ 
				var _hmt = _hmt || [];
				(function() {
				  var hm = document.createElement("script");
				  hm.src = "https://hm.baidu.com/hm.js?ffee89c382763b7bb90900c659ca5017";
				  var s = document.getElementsByTagName("script")[0]; 
				  s.parentNode.insertBefore(hm, s);
				})();
		    })
	</script>
	</body>
</html>
