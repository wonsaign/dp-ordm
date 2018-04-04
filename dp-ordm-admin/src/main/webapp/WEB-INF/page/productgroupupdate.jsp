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
		<sf:form action="../productgroup/updatesave.do" method="post" enctype="multipart/form-data" >
		<div style="width:100%; background:#e6e6e6;" class="div_body">
			<div style="width:750px; color:#5bc0de; text-align:center; padding:10px 50px; font-weight:bold; margin:0px auto; height:50px; background:white; border:2px solid white; border-radius:5px;">活动策略修改</div>
			<div style="width:750px; margin:10px  auto  0px auto; padding:20px 50px; background:white; border:2px solid white; border-radius:5px;">
				<div style="width:650px; margin:20px auto; min-height:350px; height: auto;">
					<div class="pro_gro">
						<div>
						<div style="float: left;width: 50%;height: 100%">
							<div>
								<input type="hidden" name="actId" value="${pGroup.actId}">
								<span>活动策略名称</span>
								<input type="text" name="actName" placeholder="输入 活动策略名称" class="actName" value="${pGroup.actName}">
							</div>
							<div>
								<span>开始日期</span>
								<input type="text" name="startTime" placeholder="输入 开始日期" class="startTime" value="${startTime}">
							</div>
							<div>
								<span>结束日期</span>
								<input type="text" name="endTime" placeholder="输入 结束时间" class="endTime" value="${endTime}">
							</div>
							<div>
								<span>活动描述</span>
								<textarea name="description" class="description" rows="5" cols="50" maxlength="50" placeholder="最多50字" style="resize:none;width: 250px">${pGroup.description}</textarea>
							</div>
						</div>
						<div style="float: right;width: 50%;height: 100%;">
							<img id ="preView" src="<%=AppConfig.getPutVfsPrefix() %>${pGroup.imageURL}" style="width: 220px; height: 220px; padding:10px;border: 1px dashed #003366; border-radius:5px;">
							<a href="#" class="file_a">上传图片<input type="file" value="选择图片" class="filepath" name="file" onchange="previewFile();"></a>
						</div>
					</div>
					<div class="clear"></div>
						<div class="activeType">活动策略类型
							<select name ="activeType" class="dis_select">
								<c:if test="${pGroup.actiType==2||pGroup.actiType==4}">
									<option value="1" selected="selected">活动套装</option>
								</c:if>
								<c:if test="${pGroup.actiType==1}">
									<option value="2" selected="selected">集客商品</option>
								</c:if>
							</select>
						</div>
						
						<div>
							<c:if test="${pGroup.actiType==2||pGroup.actiType==4}">
								<div>
									买<input class="buyNum" name="buyNum" type="number" value="${pGroup.buyNum==null?0:pGroup.buyNum}">
									送<input class="giveNum" name="giveNum" type="number" value="${pGroup.giveNum==null?0:pGroup.giveNum}">
								</div>
								<div class="product">
									<span>购买条件</span>
									<span class="addition">添加</span>
								</div>
								<div>
									<select name="actType" class="cha_select">
									<c:if test="${pGroup.actiType==2}">
										<option value="pro" selected="selected">单品</option>
										<option value="ser">系列</option>
									</c:if>
									<c:if test="${pGroup.actiType==4}">
										<option value="pro">单品</option>
										<option value="ser" selected="selected">系列</option>
									</c:if>
									</select>	
									<div class="gro_div">
										<input type="hidden" class="pid">
										<input type="text" class="gro_text" placeholder="输入 条形码 或 产品名称">
										<c:forEach items="${goods}" var="good">
										<div style="width: 600px;">
											<input type='checkbox' name='products' value='${good.key}' checked='checked' onclick='return false;'>
											<span>${good.value}</span>
											<span class='del'>删除</span>
										</div>
										</c:forEach>
										<ul class="gro_ul">
										</ul>
									</div>
									<div class="clear"></div>
								</div>
								<div class="giveproduct">
									<span>赠送物品</span>
									<span class="addgift">添加</span>
								</div>
								<div>
									<select class="cha_select">
										<option value="pro">单品</option>
									</select>	
									<div class="gro_div">
										<input type="hidden" class="pid">
										<input type="text" class="gro_text" placeholder="输入 条形码 或 产品名称">
										<c:forEach items="${presents}" var="present">
										<div style="width: 600px;">
											<input type='checkbox' name='gifts' value='${present.key}' checked='checked' onclick='return false;'>${present.value}
											<span class='del'>删除</span>
										</div>
										</c:forEach>
										<ul class="gro_ul">
										</ul>
									</div>
									<div class="clear"></div>
								</div>
							</c:if>
							
							<c:if test="${pGroup.actiType==1}">
							<div id="2">
								<div class="product">
									<span>购买条件</span>
									<span class="addition">添加</span>
								</div>										
								<div>
									<select name="actType" class="cha_select">
										<option value="pro">单品</option>
									</select>	
									<div class="gro_div">
										<input type="text" class="pid">
										<input type="text" class="gro_text" placeholder="输入 条形码 或 产品名称">
										<c:forEach items="${goods}" var="good">
										<div style="width: 600px;">
											<input type='checkbox' name='products' value='${good.key}' checked='checked' onclick='return false;'>
											<span>${good.value}</span>
											<span class='del'>删除</span>
										</div>
										</c:forEach>
										<ul class="gro_ul">
										</ul>
									</div>
									<div class="clear"></div>
								</div>
								<div>折扣
									<input type="number" step="0.01" min="0" max="1" name="discount" value="${pGroup.discount}">
								</div>
								<div>是否走客户策略
									<select name="pricePolicy">
									<c:if test="${pGroup.pricePolicy==true}">
										<option value="true">是</option>
									</c:if>
									<c:if test="${pGroup.pricePolicy==false}">
										<option value="false">否</option>
									</c:if>
									</select>
								</div>
							</div>
							</c:if>
						</div>
						<div class="sure">
							<input type="submit" class="saveActivity" value="保存"/>
							<input type="button" class="cancel" value="返回"/>
						</div>				
					</div>
				</div>
			</div>
		</div>
				
	</sf:form>
	</body>
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
			//输入框显示商品
			$(".gro_div").on("keyup paste change",".gro_text",function(){
				var value = $(this).parent().prev().find("option:selected").val();
				var ul = $(this).siblings(".gro_ul");
				var pname = $(this).val();
				ul.empty();
				if("pro"==value){
					$.ajax({
						type:"post",
						url:"../productgroup/getproducts.do",
						data:{pName:pname},
						async:true,
						success: function(data) {
							$.each(data,function(i,product){
								ul.append("<li value='"+product.productId+"'>"+product.name+"</li>");
							});
							ul.show();
						}
					});
				}
				if("ser"==value){
					$.ajax({
						type:"post",
						url:"../productgroup/getseriesbyname.do",
						data:{pName:pname},
						async:true,
						success: function(data) {
							$.each(data,function(i,series){
								ul.append("<li value='"+series.hardCode+"'>"+series.name+"</li>");
							});
							ul.show();
						}
					});
				}
			});

			//删除每行
			$(".gro_div").on("click",".del",function(){
				$(this).parent().remove();
			});
			//添加商品
			$(".product").on("click",".addition",function(){
				var pid= $(this).parent().next().find("input[class='pid']").val();
				var name= $(this).parent().next().find("input[class='gro_text']").val();
				var showP = $(this).parent().next().find(".gro_div");
				if(pid==""||name==""){
					return;
				}
				s = "<div style='width: 600px;'>"
					+"<input type='checkbox' name='products' value='"+pid
					+"' checked='checked' onclick='return false;'>"
					+"<span>"+name
					+"</span><span class='del'>删除</span></div>";
				showP.append(s);
			});
			//添加赠品
			$(".giveproduct").on("click",".addgift",function(){
				var pid= $(this).parent().next().find("input[class='pid']").val();
				var name= $(this).parent().next().find("input[class='gro_text']").val();
				var showP = $(this).parent().next().find(".gro_div");
				if(pid==""||name==""){
					return;
				}
				s = "<div style='width: 600px;'>"
					+"<input type='checkbox' name='gifts' value='"+pid
					+"' checked='checked' onclick='return false;'>"
					+"<span>"+name
					+"</span><span class='del'>删除</span></div>";
				showP.append(s);
			});
			
			//li点击事件
			$(".gro_ul").on("click","li",function(){
				var pid = $(this).val();
				var text = $(this).text();
				$(this).parent().parent().find("input[class='pid']").val(pid);
				$(this).parent().parent().find("input[class='gro_text']").val(text);
			});
			
			//获取焦点时候显示
			$(".gro_text").focus(function(){
				$(this).parent().find(".gro_ul").css("display","block");
			});
			
			//actType按钮发生改变时候事件
			$(".cha_select").change(function(){
				var div = $(this).parent().find(".gro_div").find("div");
				div.remove();
			});
			
			//返回
			$(".cancel").click(function(){
				window.close();
			});
			
			//保存
/* 		 	$(".saveActivity").click(function(){
				this.submit();
			});  */
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
	
		$('.startTime').fdatepicker({
			format : 'yyyy-mm-dd hh:ii',
			pickTime : true
		});
		$('.endTime').fdatepicker({
			format : 'yyyy-mm-dd hh:ii',
			pickTime : true
		});
	</script>
</html>   