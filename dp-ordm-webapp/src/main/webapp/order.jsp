<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %> 
<!DOCTYPE html>
<%@ page import="com.zeusas.core.utils.*" %>
	<head>
		<meta charset="UTF-8">
		<title></title>
		<link href="../css/index.css?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"css/index.css").lastModified()%>" rel="stylesheet" />
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
		<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<script src="../js/action.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/action.js").lastModified() %>"></script>
		<script src="../js/jquery.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/jquery.js").lastModified() %>"></script>
		<script src="../js/index.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/index.js").lastModified()%>"></script>
		<style>
			@media screen and (max-width:1000px){
				.center{
				   width:100%;
				}
			}
			.intent_ol li{
				float:left;
			}
			.intent_ol li > a {
				height:34px;
				text-decoration: none;
			}
			
			.intent_ol li > a :hover{
				color:white;
				background:green;
				border:1px solid green;
				border-radius:4px;
			}
		</style>
	</head>
<body>
	<%@ include file="common/commonHT.jsp"%>
    <div class="center">
   		<table class="intent_select" name="typeId" >
   			<tr>
					<c:forEach items="${orders}" var="os" >
					<c:if test="${os.hardCode ==0||os.hardCode ==1||os.hardCode ==10}">
						<shiro:hasAnyRoles name="root,adm,14,12">
						<td>
						<c:forEach items="${all_order_size}" var="aos">
							<c:if test="${os.hardCode ==0&&aos.key=='invalid'}"><em>${aos.value}</em></c:if>
							<c:if test="${os.hardCode ==1&&aos.key=='owner'}"><em>${aos.value}</em></c:if>
							<c:if test="${os.hardCode ==10&&aos.key=='refuse'}"><em>${aos.value}</em></c:if>
						</c:forEach>
						<img src=""><div name="typeName">${os.name }</div> <input name="typeId" type="hidden" value="${os.hardCode }">
						</td>
						</shiro:hasAnyRoles>
					</c:if>
					<c:if test="${os.hardCode ==2||os.hardCode ==3}">
						<shiro:hasAnyRoles name="root,adm,14,11">
						<td>
						<c:forEach items="${all_order_size}" var="aos">
							<c:if test="${os.hardCode ==2&&aos.key=='unPay'}"><em>${aos.value}</em></c:if>
							<c:if test="${os.hardCode ==3&&aos.key=='doPay'}"><em>${aos.value}</em></c:if>
						</c:forEach>
						<img src=""><div name="typeName">${os.name }</div> <input name="typeId" type="hidden" value="${os.hardCode }">
						</td>
						</shiro:hasAnyRoles>
					</c:if>
					<c:if test="${os.hardCode ==6||os.hardCode ==7}">
						<shiro:hasAnyRoles name="root,adm,14,12,11,10">
						<td>
						<c:forEach items="${all_order_size}" var="aos">
							<c:if test="${os.hardCode ==6&&aos.key=='waitShip'}"><em>${aos.value}</em></c:if>
							<c:if test="${os.hardCode ==7&&aos.key=='shipping'}"><em>${aos.value}</em></c:if>
						</c:forEach>
						<img src=""><div name="typeName">${os.name }</div> <input name="typeId" type="hidden" value="${os.hardCode }">
						</td>
						</shiro:hasAnyRoles>
					</c:if>
					<c:if test="${os.hardCode ==9}">
						<shiro:hasAnyRoles name="root,adm,14,12">
							<td><c:forEach items="${all_order_size}" var="aos">
									<c:if test="${os.hardCode ==9&&aos.key=='completed'}">
										<em>${aos.value}</em>
									</c:if>
								</c:forEach> <img src="">
								<div name="typeName">${os.name }</div> <input name="typeId"
								type="hidden" value="${os.hardCode }"></td>
						</shiro:hasAnyRoles>
					</c:if>
				</c:forEach>
 			</tr>
 				<c:forEach items="${prelist}" var="prelist" >
 				<tr><td>${prelist.customName }</td>
 				<td>${prelist.counterName }</td>
 				<td>${prelist.counterCode }</td>
 				<td>${prelist.productName }</td>
 				<td>${prelist.qty }</td>
 				<td>${prelist.uploadDate }</td>
 				</tr>
 				</c:forEach>
		 </table>
		 <div class="intent_body">
		 	<div class="intent_div_top"></div>
		 	<div class="intent_div">
		 		 <img src="../img/order_img.png">
		 	</div>
		 </div>
	</div>
	<%@ include file="common/commonF.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	$(".intent_select td").each(function(){
		var typeId=$(this).find('input[name="typeId"]').val();
		$(this).find("img").attr("src","../img/order_"+typeId+".png");
		if($(this).find("em").text()==0||$(this).find("em").text()==''){
			$(this).find("em").hide();
		}
	});
	$(".intent_select td").click(function(){
		var typeId=$(this).find('input[name="typeId"]').val();
		$(".intent_div").empty();
		$(this).find('div[name="typeName"]').css("color","#e33bb2");
		$(this).siblings(".intent_select td").find('div[name="typeName"]').css("color","#737571");
		//设置时间typeId
		//history.pushState({"typeId":typeId,"url":"order/cart_manage"},'',"index.do");
		
		if($(this).find('input[name="typeId"]').val()==1 ){
			if(window.addEventListener){
				history.pushState({"typeId":typeId,"url":"order/cart_manage"},'',"index.do");
			}
			$.ajax({
	 	 		type : "post",
	 	 		url : "../cart/cart_manage.do",
	 	 		async : false,
	 	 		timeout : 15000,
	 	 		dataType:"json",
	 	 		cache:false,
	 	 	    data:{typeId:typeId},
	 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
	 	 			$(".message_block2").html(errorThrown+"查询失败");
			 	 	$(".message_body").css("display","block");
	 	 		},
	 	 		success : function(data) {
	 	 			$(".combine").remove();
	 	 			$(".intent_div").empty();
	 	 			$(".intent_div_top").empty();
	 	 			$(".intent_div_top").append("<ul class='intent_ul1'><li class='intent_li1'>序列</li><li class='intent_li4'>用户名</li><li class='intent_li4'>下单时间</li><li class='intent_li4'>门店</li><div class='clear'></div></ul>");
	 	 			$.each(data, function(i,item){
	 	 				$('.intent_div').append(
	 	 						"<a class='intent_a' href='../cart/cart_managedetail.do?cartId="+item.cartId+"'><ul class='intent_ol'><li class='intent_li1'>" +parseInt(i+1)+ "</li>"
	 	 								+"<li class='intent_li4'>" +item.userName+"</li><li class='intent_li4'>"
	 	 								+ showTime(new Date(item.lastUpdate))
	 	 								+"</li><li class='intent_li4'>"+item.counterName
	 	 								+"</li><div class='clear'></div></ul></a>");
	 				});
				}
	 	 	});
		}else if($(this).find('input[name="typeId"]').val()==2){
			if(window.addEventListener){
				history.pushState({"typeId":typeId,"url":"order/search"},'',"index.do"); 
			}
			$.ajax({
	 	 		type : "post",
	 	 		url : "search.do",
	 	 		async : false,
	 	 		timeout : 15000,
	 	 		dataType:"json",
	 	 		cache:false,
	 	 	    data:{typeId:typeId},
	 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
	 	 			$(".message_block2").html(errorThrown+"查询失败");
			 	 	$(".message_body").css("display","block");
	 	 		},
	 	 		success : function(data) {
	 	 			$(".intent_div_top").empty();
	 	 			$(".combine").remove();
	 	 			/* $(".intent_div_top").before("<div class='combine'><a href='../order/combinePage.do'><button class='bg-red payall'>批量支付</button></a></div>") */
	 	 			$(".intent_div_top").before("<div class='combine'><a href='../preorder/combinePage.do' style='width:100px; float:right'><button class='bg-red payall submit_order'>礼盒合并</button></a><a href='../order/combinePage.do' style='width:100px; float:right'><button class='bg-red payall submit_order'>批量支付</button></a><div class='clear'></div></div>")
	 	 			$(".intent_div_top").append("<ul class='intent_ul2'><li class='intent_li1'>序列</li><li class='intent_li2'>单号</li><li class='intent_li3'>柜台号</li><li class='intent_li3'>下单时间</li><li class='intent_li4'>收货地址</li><div class='clear'></div></ul>");
	 	 			$.each(data.data, function(i,item){
	 	 				if(item.issysin==2){
	 	 					$('.intent_div').append(
	 	 		 	 				"<a class='intent_a' href='../order/detail.do?orderId="+item.id+"'><ul class='intent_ol' style='background:rgba(231, 197, 177, 0.6);'><li class='intent_li1'>"+parseInt(i+1)
	 	 		 	 				+"(合并)<input name='isMerger' type='hidden' value="+item.merger+">"
	 	 		 	 				+"<input name='orderId' type='hidden' value="+item.id+"></li>"
	 	 							+"<li class='intent_li2'>" +item.orderNo+"</li><li class='address_font intent_li3'>"+item.counterName
	 	 							+"</li><li class='intent_li3'>"
	 	 							+ showTime(new Date(item.orderCreatTime))
	 	 							+"</li><li class='address_font intent_li4' >"+item.address
	 	 							+"</li><div class='clear'></div></ul></a>");
	 	 				}else if(item.issysin==1){
	 	 					$('.intent_div').append(
	 		 	 					"<a class='intent_a' href='../order/detail.do?orderId="+item.id+"'><ul class='intent_ol' style='background:#f3d899;'><li class='intent_li1'>"+parseInt(i+1)
	 		 		 	 				+"(礼盒)<input name='isMerger' type='hidden' value="+item.merger+">"
	 		 		 	 				+"<input name='orderId' type='hidden' value="+item.id+"></li>"
	 		 							+"<li class='intent_li2'>" +item.orderNo+"</li><li class='address_font intent_li3'>"+item.counterName
	 		 							+"</li><li class='intent_li3'>"
	 		 							+ showTime(new Date(item.orderCreatTime))
	 		 							+"</li><li class='address_font intent_li4' >"+item.address
	 		 							+"</li><div class='clear'></div></ul></a>");
	 	 				}else{
	 	 					$('.intent_div').append(
	 	 					"<a class='intent_a' href='../order/detail.do?orderId="+item.id+"'><ul class='intent_ol'><li class='intent_li1'>"+parseInt(i+1)
	 		 	 				+"<input name='isMerger' type='hidden' value="+item.merger+">"
	 		 	 				+"<input name='orderId' type='hidden' value="+item.id+"></li>"
	 							+"<li class='intent_li2'>" +item.orderNo+"</li><li class='address_font intent_li3'>"+item.counterName
	 							+"</li><li class='intent_li3'>"
	 							+ showTime(new Date(item.orderCreatTime))
	 							+"</li><li class='address_font intent_li4' >"+item.address
	 							+"</li><div class='clear'></div></ul></a>");
	 	 				}
	 	 				
	 				});
				}
	 	 	});
			$("input[name='isMerger']").each(function(){
				if($(this).val()=="true"){
					var orderId=$(this).next().val();
					$(this).parent().parent().css("background","rgba(158, 158, 158, 0.5)");
					$(this).parent().parent().parent().attr("href","../order/getCombineOrder.do?orderId="+orderId);
				}
			})
			
		}else if($(this).find('input[name="typeId"]').val()==10){
			if(window.addEventListener){
				history.pushState({"typeId":typeId,"url":"order/search"},'',"index.do"); 
			}
			$.ajax({
	 	 		type : "post",
	 	 		url : "search.do",
	 	 		async : false,
	 	 		timeout : 15000,
	 	 		dataType:"json",
	 	 		cache:false,
	 	 	    data:{typeId:typeId},
	 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
	 	 			$(".message_block2").html(errorThrown+"查询失败");
			 	 	$(".message_body").css("display","block");
	 	 		},
	 	 		success : function(data) {
	 	 			$(".intent_div_top").empty();
	 	 			$(".combine").remove();
	 	 			$(".intent_div_top").append("<ul class='intent_ul2'><li class='intent_li1'>序列</li><li class='intent_li2'>单号</li><li class='intent_li3'>柜台号</li><li class='intent_li3'>下单时间</li><li class='intent_li4'>收货地址</li><div class='clear'></div></ul>");
	 	 			$.each(data.data, function(i,item){
	 	 				if(item.issysin==2){
	 	 					$('.intent_div').append(
	 	 		 	 				"<a class='intent_a' href='../order/detail.do?orderId="+item.id+"'><ul class='intent_ol' style='background:rgba(231, 197, 177, 0.6);'><li class='intent_li1'>"+parseInt(i+1)
	 	 		 	 				+"(合并)<input name='isMerger' type='hidden' value="+item.merger+">"
	 	 		 	 				+"<input name='orderId' type='hidden' value="+item.id+"></li>"
	 	 							+"<li class='intent_li2'>" +item.orderNo+"</li><li class='address_font intent_li3'>"+item.counterName
	 	 							+"</li><li class='intent_li3'>"
	 	 							+ showTime(new Date(item.orderCreatTime))
	 	 							+"</li><li class='address_font intent_li4' >"+item.address
	 	 							+"</li><div class='clear'></div></ul></a>");
	 	 				}else if(item.issysin==1){
	 	 					$('.intent_div').append(
	 	 		 	 				"<a class='intent_a' href='../order/detail.do?orderId="+item.id+"'><ul class='intent_ol' style='background:#f3d899;'><li class='intent_li1'>"+parseInt(i+1)
	 	 		 	 				+"(礼盒)<input name='isMerger' type='hidden' value="+item.merger+">"
	 	 		 	 				+"<input name='orderId' type='hidden' value="+item.id+"></li>"
	 	 							+"<li class='intent_li2'>" +item.orderNo+"</li><li class='address_font intent_li3'>"+item.counterName
	 	 							+"</li><li class='intent_li3'>"
	 	 							+ showTime(new Date(item.orderCreatTime))
	 	 							+"</li><li class='address_font intent_li4' >"+item.address
	 	 							+"</li><div class='clear'></div></ul></a>");
	 	 				}else{
	 	 					$('.intent_div').append(
	 	 		 	 				"<a class='intent_a' href='../order/detail.do?orderId="+item.id+"'><ul class='intent_ol'><li class='intent_li1'>"+parseInt(i+1)
	 	 		 	 				+"<input name='isMerger' type='hidden' value="+item.merger+">"
	 	 		 	 				+"<input name='orderId' type='hidden' value="+item.id+"></li>"
	 	 							+"<li class='intent_li2'>" +item.orderNo+"</li><li class='address_font intent_li3'>"+item.counterName
	 	 							+"</li><li class='intent_li3'>"
	 	 							+ showTime(new Date(item.orderCreatTime))
	 	 							+"</li><li class='address_font intent_li4' >"+item.address
	 	 							+"</li><div class='clear'></div></ul></a>");
	 	 				}
	 	 				
	 				});
				}
	 	 	});
			$("input[name='isMerger']").each(function(){
				if($(this).val()=="true"){
					var orderId=$(this).next().val();
					$(this).parent().parent().css("background","rgba(158, 158, 158, 0.5)");
					$(this).parent().parent().parent().attr("href","../order/getCombineOrder.do?orderId="+orderId);
				}
			})
			
		}else{
			if(window.addEventListener){
				history.pushState({"typeId":typeId,"url":"order/search"},'',"index.do"); 
			}
			$.ajax({
	 	 		type : "post",
	 	 		url : "search.do",
	 	 		async : false,
	 	 		timeout : 15000,
	 	 		dataType:"json",
	 	 		cache:false,
	 	 	    data:{typeId:typeId},
	 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
	 	 			$(".message_block2").html(errorThrown+"查询失败");
			 	 	$(".message_body").css("display","block");
	 	 		},
	 	 		success : function(data) {
	 	 			$(".combine").remove();
	 	 			$(".intent_div_top").empty();
	 	 			$(".intent_div_top").append("<ul class='intent_ul2'><li class='intent_li1'>序列</li><li class='intent_li2'>单号</li><li class='intent_li3'>柜台号</li><li class='intent_li3'>下单时间</li><li class='intent_li4'>收货地址</li><div class='clear'></div></ul>");
	 	 			$.each(data.data, function(i,item){
	 	 				if(item.issysin==2){
	 	 					$('.intent_div').append(
	 	 		 	 				"<a class='intent_a' href='../order/detail.do?orderId="+item.id+"'><ul class='intent_ol' style='background:rgba(231, 197, 177, 0.6);'><li class='intent_li1'>"+parseInt(i+1)
	 	 		 	 				+"(合并)<input name='isMerger' type='hidden' value="+item.merger+">"
	 	 		 	 				+"<input name='orderId' type='hidden' value="+item.id+"></li>"
	 	 							+"<li class='intent_li2'>" +item.orderNo+"</li><li class='address_font intent_li3'>"+item.counterName
	 	 							+"</li><li class='intent_li3'>"
	 	 							+ showTime(new Date(item.orderCreatTime))
	 	 							+"</li><li class='address_font intent_li4' >"+item.address
	 	 							+"</li><div class='clear'></div></ul></a>");
	 	 				}else if(item.issysin==1){
	 	 					$('.intent_div').append(
	 	 		 	 				"<a class='intent_a' href='../order/detail.do?orderId="+item.id+"'><ul class='intent_ol' style='background:#f3d899;'><li class='intent_li1'>"+parseInt(i+1)
	 	 		 	 				+"(礼盒)<input name='isMerger' type='hidden' value="+item.merger+">"
	 	 		 	 				+"<input name='orderId' type='hidden' value="+item.id+"></li>"
	 	 							+"<li class='intent_li2'>" +item.orderNo+"</li><li class='address_font intent_li3'>"+item.counterName
	 	 							+"</li><li class='intent_li3'>"
	 	 							+ showTime(new Date(item.orderCreatTime))
	 	 							+"</li><li class='address_font intent_li4' >"+item.address
	 	 							+"</li><div class='clear'></div></ul></a>");
	 	 				}else{
	 	 					$('.intent_div').append(
	 	 		 	 				"<a class='intent_a' href='../order/detail.do?orderId="+item.id+"'><ul class='intent_ol'><li class='intent_li1'>"+parseInt(i+1)
	 	 		 	 				+"<input name='isMerger' type='hidden' value="+item.merger+">"
	 	 		 	 				+"<input name='orderId' type='hidden' value="+item.id+"></li>"
	 	 							+"<li class='intent_li2'>" +item.orderNo+"</li><li class='address_font intent_li3'>"+item.counterName
	 	 							+"</li><li class='intent_li3'>"
	 	 							+ showTime(new Date(item.orderCreatTime))
	 	 							+"</li><li class='address_font intent_li4' >"+item.address
	 	 							+"</li><div class='clear'></div></ul></a>");
	 	 				}
	 	 				
	 				});
				}
	 	 	});
		}
		if($('body').height()<$(window).height()){
			var ch=$(window).height()-$(".icon").height()-$(".top").height()-$(".bottom").height()+200;
			$(".center").height(ch);
		}
	});	
/* 
	$(".intent_ol").on("mouseover","li",function(){
		$(this).find("a").css("color","blue");
		$(this).find("a").css("background-color","#D1EEEE");
	});
	$(".intent_ol").on("mouseout","li",function(){
		$(this).find("a").css("color","black");
		$(this).find("a").css("background-color","white");
	}); 
	$(".intent_select").on("mouseover","td",function(){
		if($(window).width()>600){
			$(this).find("img").css({"width":"42px","height":"52px"});
		}else{
			$(this).find("img").css({"width":"35px","height":"45px"});
		}
	});
	$(".intent_select").on("mouseout","td",function(){
		if($(window).width()>600){
			$(this).find("img").css({"width":"40px","height":"50px"});
		}else{
			$(this).find("img").css({"width":"30px","height":"40px"});
		}
		
	});
	$(".intent_div").on("mouseover",".intent_ol",function(){
		$(this).css({"background":"#ffffca"});
	});
	$(".intent_div").on("mouseout",".intent_ol",function(){
		$(this).css({"background":"#ffffe3"});
	});
	*/
})
function conbineOrder(){
}
</script> 
</body>
</html>
