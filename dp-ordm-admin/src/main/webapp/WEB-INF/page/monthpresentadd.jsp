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
		<link rel="stylesheet" href="http://www.jq22.com/jquery/font-awesome.4.6.0.css">
		<link href="../css/foundation.min.css" rel="stylesheet" type="text/css">
		<link href="../css/foundation-datepicker.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" href="../css/index.css">
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
		<style>
	     	input[type="text"]{
				width:200px;
				height:35px;
			}
			.dis_select{
				width:200px;
				height:35px;
			}
			.pro_gro{
			    font-weight: bold;
    			line-height: 40px;
			}
			input[type="number"]{
				width:80px;
				height:35px;
				display:inline-block;
				margin:0px 10px;
			}
			.cha_select {
			    width: 80px;
			    height: 35px;
			    font-size: 18px;
			    float: left;
			}
			.gro_ul li{
				list-style:none;
				cursor: pointer;
			} 
			.gro_ul{
				position:absolute;
				padding:0px;
				line-height:18px;
				width:220px;
			    height:auto;
			    top:35px;
			    background:#FFC0CB;
			    left:-20px;
			    z-index: 100;
			    display: none;
			}
		</style>
	</head>
	<body>
		<%@ include file="common/common_window.jsp"%>
		<script src="../js/jquery.js"></script>
		<script src="../js/foundation-datepicker.js"></script>
		<script src="../js/foundation-datepicker.zh-CN.js"></script>
		<script src="../js/productpolicy.js"></script>
		<script src="../js/index.js"></script>
		<div class="show_body" value="${message}">
		<input type="hidden" value="${message}" id="hidden1">
			<c:if test="${message!=null&&''!=message}">${message}</c:if>
		</div>
		<sf:form action="../monthpresent/presentsave.do" method="post" enctype="multipart/form-data" >
		<div style="width:100%; background:#e6e6e6;" class="div_body">
			<div style="width:750px; color:#5bc0de; text-align:center; padding:10px 50px; font-weight:bold; margin:0px auto; background:white; border:2px solid white; border-radius:5px;">赠品Excel导入</div>
			<div style="width:750px; margin:10px  auto  0px auto; padding:20px 50px; background:white; border:2px solid white; border-radius:5px;">
				<span style="float:left;margin-right:20px;">类型:</span>
				<select name="type">
					<c:forEach items="${dsResponse.data}" var="presentType">
						<option value="${presentType.hardCode}" selected="selected">${presentType.name}</option>
					</c:forEach>
				</select>
				<div style="width:650px; margin:20px auto; min-height:350px; height: auto;">
					<div class="filename">xxx.xml</div>
					<a href="#" class="file_a">选择文件<input type="file" value="选择文件" name="file" class="filepath"></a>
					<a href="#" class="file_a">上传文件<input type="submit" value="上传" class="filepath"></a>
				</div>
			</div>
		</div>		
	</sf:form>
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
						
			//删除每行
			$(".gro_div").on("click",".del",function(){
				$(this).parent().remove();
			});

			
			//返回
			$(".cancel").click(function(){
				window.close();
			});

			$('.dis_select').change(function(){
				var disHTML = $('.dis_option').find("#"+$(this).val()).clone(true);
			 	$('.dis_block').html(disHTML);
				var select = $(this).val();
			});
			
			$('.filepath').change(function(){
				var value = $(this).val();
				var strFileName=value.replace(/^.+?\\([^\\]+?)(\.[^\.\\]*?)?$/gi,"$1");  //正则表达式获取文件名，不带后缀
				var FileExt=value.replace(/.+\./,"");   //正则表达式获取后缀
				if("xlsx"==FileExt||"xls"==FileExt){
					$('.filename').text(strFileName+"."+FileExt);
				}
				else{
					alert("文件类型不对！必须是.xlsx或者.xls");
				}
			});
		});
	</script>
	</body>
</html>