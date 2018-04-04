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
	</head>
	<body>
		<%@ include file="common/common_window.jsp"%>
		<script src="../js/jquery.js"></script>
		<script src="../js/productpolicy.js"></script>
		<div class="show_body" value="${message}">
		<input type="hidden" value="${message}" id="hidden1">
			<c:if test="${message!=null&&''!=message}">${message}</c:if>
		</div>
		<sf:form action="../product/updateSeriesUrl.do" method="post" enctype="multipart/form-data" >
		<div style="width:100%; background:#e6e6e6;" class="div_body">
			<div style="width:750px; color:#5bc0de; text-align:center; padding:10px 50px; font-weight:bold; margin:0px auto; height:30px; background:white; border:2px solid white; border-radius:5px;"> 系列产品</div>
			<div style="width:750px; margin:10px  auto  0px auto; padding:20px 50px; background:white; border:2px solid white; border-radius:5px;">
				<div style="width:650px; margin:20px auto; min-height:350px; height: auto;">
					<div class="infor_block">
						<input name="did" type="hidden" value="${dictionary.did}">
						<div class="infor_div">系列名称 <br><input type="text" name="name" class="add_product" value="${dictionary.name}" readonly="readonly"></div>
						<div class="infor_div">系列描述 <br></span><input  name ="summary" type="text" value="${dictionary.summary}"></div>								
					</div>
					<div style="width: 300px;float: left; font-weight: bold;">
						<div class="infor_div">图片</div>
						<div style="border: 1px dashed #000000; width: 240px; height: 240px; border-radius:5px;">
							<img id="preView" src='<%=AppConfig.getVfsPrefix() %>${dictionary.url}'
							class="file_img" style="width: 220px; height: 220px; padding:10px;"/>
						</div>
						<a href="#" class="file_a">
							上传图片<input type="file"  onchange="previewFile()" name="file" style="margin-top: 10px;" class="filepath"/>
						</a>
					</div>
				</div>
				<div class="sure">
					<input type="submit" value="保存" class="open_hidden"/>
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
		//图片预览
		 function previewFile() {
			 var preview = $("#preView").attr('src');
			 var file  = document.querySelector('input[type=file]').files[0];
			 var reader = new FileReader();
			 reader.onloadend = function () {
				 $("#preView").attr("src",reader.result);
			 }
			 if (file) {
			  reader.readAsDataURL(file);
			 } else {
			  preview.src = "";
			 }
		} 
	</script>
</html>