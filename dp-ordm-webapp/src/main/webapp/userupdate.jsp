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
	    	<div>
	    		<form method="post" action="updatesave.do">
	    		<div class="useradd">
	    			<div class='useradd_img'>
	    				<img src="../img/user.jpg">
	    			</div>
	    			<ul class="other_text">
	    				<li><input type="hidden"  name="oldLoginName" value="${aUser.loginName}"></li>
		    			<li>
		    				<span>用户名</span><input type="text" name="loginName" value="${aUser.loginName}" readonly="true"/>
		    				<c:if test="${msg!=null||msg!=''}">
		    					<div style="color: red">${msg}</div>
		    				</c:if>
		    			</li>
		    			<li><span>手机号</span><input type="text" name="mobile" value="${mobile}"/></li>
		    			<li><span>姓&nbsp;名</span><input type="text" name="commonName" value="${aUser.commonName}"/></li>
		    			<li><span>重置密码</span><input type="password" name="password" value=""/></li>
		    		</ul>
		    		<div class="clear"></div>
	    		</div>
	    		<div class="useradd">
	    			<div><span style="margin-left:20px;">柜台</span></div>
	    			<ul class="counter_text">
		    			<c:forEach items="${allCounters}" var="allc" varStatus="vs">
		    				<li>
		    				<c:if test="${!empty myCounters}">
		    					<input
		    						<c:forEach items="${myCounters}" var="myc">
		    							<c:if test="${myc.counterId == allc.counterId}">checked </c:if>
		    							</c:forEach>  
		    						type="checkbox" name="counterId" id="${allc.counterId }" value="${allc.counterId}"
		    						<c:if test="${idboss}">onclick="return false;" </c:if>>
			    				</c:if>
			    				<c:if test="${empty myCounters}">
				    				<input type="checkbox" name="counterId" id="${allc.counterId }" value="${allc.counterId}">
			    				</c:if>
		    				
	    						<label for="${allc.counterId }" <c:if test="${idboss}">onclick="return false;" </c:if>>${allc.counterName}</label>&nbsp;&nbsp;
		    				</li>
		    			</c:forEach>
		    			<div class="clear"></div>
	    			</ul>
	    		</div>
	    		<div class="useradd">
	    			<div><span style="margin-left:20px;">角色</span></div>
	    			<ul class="role_text">
	    				<c:forEach items="${allRoles}" var="allr" varStatus="vs">
		    				<li>
		    				<input
		    					<c:forEach items="${myRoles}" var="myr">
		    						<c:if test="${myr.rid == allr.rid}">checked </c:if>
		    					</c:forEach>  
		    					type="checkbox" name="rid" id="${allr.rid }" value="${allr.rid}" 
		    					<c:if test="${idboss}">onclick="return false;" </c:if>>
	    					<label for="${allr.rid }" <c:if test="${idboss}">onclick="return false;" </c:if>>${allr.commonName	}</label>&nbsp;&nbsp;
		    				</li>
		    			</c:forEach>
		    			<div class="clear"></div>
	    			</ul>
	    		</div>
	    		<div class="clear"></div>
	    		<div class="user_sure">
	    			<input type="submit" class="bg-red add_button" value="确定" />
	    			<input type="button" id="logout" class="bg-red" value="返回" />
	    		</div>
		    		
	    		</form>
	    	</div>
		</div>
		<%@ include file="common/commonF.jsp"%>	
	</body>
	<script src="../js/jquery.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/jquery.js").lastModified() %>"></script>
	<script src="../js/index.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/index.js").lastModified()%>"></script>
	<script>
		function message(){
			var msg = $(".massage").val();
			if(msg!=null||msg!=""){
				switch(msg){
				case "success":
			 		$(".message_body").show();
			 		$(".message_block2").html("<div style='color:green'>修改成功</div>");
			 		$(".message_body").fadeOut(3000);
					break;
				case "falser":
			 		$(".message_body").show();
			 		$(".message_block2").html("<div style='color:red'>修改失败</div>");
			 		$(".message_body").fadeOut(3000);
					break;
				}
			}
			return ;
	 	}
		
		$(document).ready(function(){
			message();
			$('.translate').click(function(){
			    var s=$(this).parent().find('input').attr("disabled",false);				
			});
			$('select[name="orgUnit"]').on('change', function() {
				var orgId = $(this).val();
				var src="../user/cascade"
				var li=$('input[name="rid"]').parent();
				if(orgId!=""&&orgId!=null){
					$.ajax({
						type:"post",
						url:src+".do",
						data:{orgId:orgId},
						dataType: "json",
						async:true,
						success: function(data) {
							li.empty();
							li.append("<p>角色</p>");
							$.each(data,function(index, value){
								if(index>0&&index%2==0){
									li.append("<br/>");
								}
								var htmlcheckbox="<input type='checkbox' name='rid' id='"+value.rid+"' value='"+value.rid+"'>"
											+"<label for='"+value.rid+"'>"+value.commonName+"</label>";
								li.append(htmlcheckbox);
							}); 
						}
					});
				}
			});
		});
		$('#create').click(function(){
			window.location.href='../user/create.do';
		});
		
	 	$('#logout').click(function(){
			window.location.href='../user/setting.do';
		});
	 	$(".add_button").click(function(){
	 		var loginName = $("input[name='loginName']").val();
	 		var pwd=$("input[name='password']").val();
	 		var mobile = $("input[name='mobile']").val();
	 		var commonName = $("input[name='commonName']").val();
	 		var counterId = $(".counter_text").find("input[type='checkbox']:checked");
	 		var rid = $(".role_text").find("input[type='checkbox']:checked");
	 		if(loginName==""||loginName==null){
	 			$("input[name='loginName']").focus();
	 			alert("姓名不能为空");
	 			return false;
	 		}
	 		if(pwd==""||pwd==null){
	 			$("input[name='password']").focus();
	 			alert("密码不能为空");
	 			return false;
	 		}else if(pwd.length<4){
	 			$("input[name='password']").focus();
	 			alert("密码长度不能小于4");
	 			return false;
	 		}
	 		if(mobile==""||mobile==null){
	 			$("input[name='mobile']").focus();
	 			alert("手机号码不能为空");
	 			return false;
	 		}
	 		if(commonName==""||commonName==null){
	 			$("input[name='commonName']").focus();
	 			alert("姓名不能为空");
	 			return false;
	 		}
	 		if(counterId.length==0){
	 			alert("请 选择【柜台】 ！");
	 			return false;
	 		}
	 		if(rid.length==0){
	 			alert("请 选择【权限 】！");
	 			return false;
	 		}
	 		return true;
	 	});
	</script>
</html>
