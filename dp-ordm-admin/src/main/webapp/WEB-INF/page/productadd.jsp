<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.zeusas.core.utils.AppConfig" %>
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
		<div class="show_body" value="${message}">
		<input type="hidden" value="${message}" id="hidden1">
			<c:if test="${message!=null&&''!=message}">${message}</c:if>
		</div>
		<sf:form action="../product/productupdatesave2.do" method="post" enctype="multipart/form-data" 
modelAttribute="product">
		<div style="width:100%; background:#e6e6e6;" class="div_body">
			<div style="width:750px; color:#5bc0de; text-align:center; padding:10px 50px; font-weight:bold; margin:0px auto; height:30px; background:white; border:2px solid white; border-radius:5px; line-height:30px;"> 商品信息</div>
			<div style="width:750px; margin:10px auto; padding:20px 50px; background:white; border:2px solid white; border-radius:5px;">
				<div style="width:650px; margin:20px auto; min-height:350px; height: auto;">
					<div class="infor_block">
						<input name="productId" type="hidden" value="${product.productId }">
						<div class="infor_div">商品名称<span style="color:red;">（*必填）</span> <input type="text" class="add_product" value="${product.name }" readonly="readonly"></div>
						<div class="infor_div">商品类型<span style="color:red;">（*必填）</span><input type="text" value="${product.typeName }" readonly="readonly"></div>								
						<div class="infor_div">描述<span style="color:red;">（*必填）</span></div>
						<textarea name="description" rows="10" cols="31">${product.description }</textarea>
					</div>
					<div style="width: 300px;float: left; font-weight: bold;">
						<div class="infor_div">图片</div>
						<div style="border: 1px dashed #000000; width: 230px; height: 230px; border-radius:5px;">
							<img id="preView" src="${product.imageURL }"
							class="file_img" style="width: 220px; height: 220px; padding:5px;"/>
							<a href="#" class="file_a">上传图片<input type="file" onchange="previewFile()" name="file"  class="filepath"/></a>
						</div>
					</div>
				</div>
				<div class="sure">
					<input type="submit" value="保存" class="open_hidden"/>
					<input type="button" value="关闭" onclick="javascript:window.opener=null;window.open('','_self');window.close();"/>
				</div>				
			</div>
		</div>
				
	</sf:form>
	</body>
	<script src="../js/jquery.js"></script>
	<script src="../js/index.js"></script>
	<script>
		$(document).ready(function(){
			 $(".div_body").height($(window).height());
			if($("#hidden1").val()!=null&&$("#hidden1").val()!=""){
				$(".hidden_body2").empty();
				$(".hidden_body").show(); 
				$(".hidden_body2").html($("#hidden1").val());
				$(".hidden_body").hide(3000); 
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
    