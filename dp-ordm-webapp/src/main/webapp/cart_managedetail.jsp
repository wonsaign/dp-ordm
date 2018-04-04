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
	<div class="address_body">
		<div class="address_block">
			<div>您要提交xxx店铺的购物车吗？</div>
			<div>地址：${counter.address }</div>
			<span>联系人：</span><span id="contact">${counter.contact }</span>
			<div class="address_button">
				<button type="button" name="savecart">确定</button>
				<button type="button">取消</button>
			</div>
		</div>
	</div>
	<div class="center">		
		<!-- 头部 -->
		<div class="cart_all">审&nbsp;&nbsp;核&nbsp;&nbsp;购&nbsp;&nbsp;物&nbsp;&nbsp;车</div>
		<div class="address">
			<input type="hidden" name="counterId" value="${cart.counterId }">
			<input type="hidden" name="cartId" value="${cart.cartId }">
			<input type="hidden" id="NowCartId" name="NowCartId" value="${cart.cartId }">
			<div>
				<span>门店名称：</span><span id="counterName">${counter.counterName }</span>
				<span>柜台号：</span><span id="counterCode">${counter.counterCode }</span>
				<span>联系人：</span><span id="contact">${counter.contact }</span>
			</div>
			<div>
				<span>收货地址：</span><span id="address"> ${counter.address }</span>
			</div>
		</div>
		 <!-- 明细清单-->
		  <div class="shopping_one">
		 	<div class="operation">
		 		<ul>
				 	<li class="information">商品信息</li>
				 	<li class="operate">单价</li>
				 	<li class="operate">数量</li>
				 	<li class="operate">类型</li>
		 		</ul>					
			 </div>
			 <c:forEach items="${lcds }" var="cd">
			 <div class="operation_one">
			 	<div class="operation_top">
			 	    <span class="activityName">${cd.activityName }</span>
			 	    <div class="add_del">
			 	    	<input name="activityId" type="hidden" value="${cd.activityId }">
			 	    	<input name="activityName" type="hidden" value="${cd.activityName }">
			 	    	<input name="detailId" type="hidden" value="${cd.detailId }">
			 	    	<input class="suitPrice" type="hidden" value="${cd.price }">
			 	    	<a class="del">删除</a>
			 	    	<div class="button_block" style="margin:2px 0; float:right;">
			 	    		</span><input name="num" type="number" class="text_box" required="required" class="text_box" value="${cd.quantity }" readonly="readonly"/>
			 	    	</div>
			 	    	<div class="alltext">数量:</div>
			 	    	<%-- <div class="button_block" style="margin:2px 0; float:right;">
			 	    		<a class="min min_bt"/>-</a>
							<input name="num" type="number" class="text_box" required="required" class="text_box" value="${cd.quantity }" />
							<a class="add add_bt"/>+</a>
			 	    	</div>  --%>
						<span class="alltext"><i style="color:red;" class="allfee"></i></span>
						<div class="clear"></div>
			 	    </div>
			 	    <div class="clear"></div>
			 	</div>
			 	<!-- 明细条目 -->
			 	<c:forEach items="${lcdds }" var="cdd">
			 	<c:if test="${cdd.cartDetailId==cd.detailId }">
			 	<div class="shopping_operate">
				 	<ul>
				 		<div class="clear"></div>
					 	<li class="information">
					 		<c:forEach items="${lps }" var="p">
					 			<c:if test="${p.productId==cdd.productId }">
						 			<c:if test="${p.prePro==2||p.prePro==3}">
						 			<div class="shopping_name"><img class="shopping_nameReserve" src="../img/owes1.png">${cdd.productName }</div>
						 			</c:if>
						 			<c:if test="${p.prePro==0||p.prePro==1}">
						 				<div class="shopping_name">${cdd.productName }</div>
						 			</c:if>
						 		</c:if>
					 		</c:forEach>
					 		<input name="productId" type="hidden" value="${cdd.productId }">
					 	</li>
					 	<li class="operate cdd_price" >${cdd.price }</li>
					 	<li class="operate product_num">${cdd.quantity }</li>
					 	<li class="operate">
					 		<c:forEach items="${lps }" var="p">
					 			<c:if test="${p.productId==cdd.productId }">${p.typeName }</c:if>
					 		</c:forEach>
					 		<span name="tips" style="color:red"></span>
					 	</li>
					 	<div class="clear"></div>
				 	</ul>
			 	</div>
			 	</c:if>
			 	</c:forEach>
			 	
			 </div>
			 </c:forEach>
			 <!-- 底部 -->
			 <div class="operationpanel">
			 	<br/>
		        <hr/>
		 		<span>总数量:</span> <strong> <span name="qty"></strong></span>&nbsp;
		 		<span>商品数量:</span> <strong> <span name="productqty"></strong></span>&nbsp;
		 		<span>赠品&物料数量:</span> <strong> <span name="materialqty"></strong></span>&nbsp;
		        <span name="materialname">物料价格：</span>	<strong> <span name="materialfee"></strong></span>&nbsp;
		        <span name="feeadd" style="color:green"></span>	<strong style="color:red;"><span name="reducefee"></span></strong><span style="color:green" name="feename"></span>&nbsp;
		        <span >总价格：</span>	<strong> <span name="totalfee"></strong></span>&nbsp;<br/>
		        <br/>
		        <hr/>
		        <div style="float:right;">
		        	<button type="button" class="cartcheckpass submit_order big bg-red">审核通过</button>
		        	<button type="button" class="cartcheckrefuse submit_order big bg-red">退        回</button>
		        </div>
		        <div class="clear"></div>
			 </div>
			 <div class="cartfuse" style="float:right; margin:10px auto;">
	        	<button type="button" class="cartcheckrefuse submit_order big bg-red">退        回</button>
	         </div>
	         <div class="clear"></div>
		</div>			 
	</div>
	<%@ include file="common/commonF.jsp"%>
<script src="../js/jquery.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/jquery.js").lastModified() %>"></script>
<script src="../js/index.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/index.js").lastModified()%>"></script>
<script type="text/javascript">
 	$(document).ready(function(){
 		//初始化购物车面板
 		//初始化购物车面板
 		initadddel();
 		checkproductcontent();
 		if($(".operation_one").length!=0){
 			$(".operation").show();
 		};
		 // 删除功能：在审核中修正数量！
 		$('.operation_one').each(function(){
			var position = $(this);
			if(position.find('input[name="activityName"]').val()==0){
				position.find('.activityName').text(position.find('.shopping_operate ul li:eq(0) .shopping_name').text());
				$.post("../product/getminorderunit.do",{productId:position.find('.shopping_operate ul li:eq(0) input[name="productId"]').val()}
				,function (data) {
					position.find('input[name="num"]').attr("min",data.minOrderUnit);
					position.find('input[name="num"]').attr("step",data.minOrderUnit);
				});
			}else{
				position.find('.activityName').text("活动："+position.find('input[name="activityName"]').val() );
				position.find('input[name="num"]').attr("min",1);
				position.find('input[name="num"]').attr("step",1);
			}
		}); 
 		<%--
		//修改购物车数量
		 $('input[name="num"]').blur(function(){
			if(checknumber($(this)) == false){
				return false;
			}
			var detailId=$(this).parent().parent().find('input[name="detailId"]').val();
			var num=$(this).val();
	   	  	$.ajax({
	 	 		type : "post",
	 	 		url : "../cart/update.do",
	 	 		async : false,
	 	 		timeout : 15000,
	 	 		dataType:"text",
	 	 		cache:false,
	 	 	    data:{detailId:detailId,num:num},
	 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
	 	 			$(".message_block2").html(errorThrown+"修改购物车失败!");
	 	 			$(".message_body").fadeIn();
		 	 		$(".message_body").fadeOut(3000);
	 	 		},
	 	 		success : function(data) {
	 	 			$(".message_block2").html("修改购物车成功");
	 	 			$(".message_body").fadeIn();
		 	 		$(".message_body").fadeOut(3000);
	 	 			checkproductcontent();
				}
	 	 	});  
	 	}); --%>
		$(".min_bt").click(function(){
			if(checknumber($(this).parent().find('input[name="num"]')) == false){
				return false;
			}
			var detailId=$(this).parent().parent().find('input[name="detailId"]').val();
			var num=$(this).next().val();
	   	  	$.ajax({
	 	 		type : "post",
	 	 		url : "../cart/update.do",
	 	 		async : false,
	 	 		timeout : 15000,
	 	 		dataType:"text",
	 	 		cache:false,
	 	 	    data:{detailId:detailId,num:num},
	 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
	 	 		},
	 	 		success : function(data) {
	 	 			checkproductcontent();
				}
	 	 	});
		});
		$(".add_bt").click(function(){
			if(checknumber($(this).parent().find('input[name="num"]')) == false){
				return false;
			}
			var detailId=$(this).parent().parent().find('input[name="detailId"]').val();
			var num=$(this).prev().val();
	   	  	$.ajax({
	 	 		type : "post",
	 	 		url : "../cart/update.do",
	 	 		async : false,
	 	 		timeout : 15000,
	 	 		dataType:"text",
	 	 		cache:false,
	 	 	    data:{detailId:detailId,num:num},
	 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
	 	 		},
	 	 		success : function(data) {
	 	 			checkproductcontent();
				}
	 	 	});
		});
		//删除商品
		$('.del').click(function(){
			var detailId=$(this).parent().parent().find('input[name="detailId"]').val();
			var position=$(this);
	   	  	$.ajax({
	 	 		type : "post",
	 	 		url : "../cart/delete.do",
	 	 		async : false,
	 	 		timeout : 15000,
	 	 		dataType:"json",
	 	 		cache:false,
	 	 	    data:{detailId:detailId},
	 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
	 	 			$(".message_block2").html(errorThrown+"删除购物车失败!");
	 	 			$(".message_body").fadeIn();
		 	 		$(".message_body").fadeOut(3000);
	 	 		},
	 	 		success : function(data) {
	 	 			$(".message_block2").html("删除购物车成功");
	 	 			$(".message_body").fadeIn();
		 	 		$(".message_body").fadeOut(3000);
	 	 			position.parent().parent().parent().remove();
	 	 			checkproductcontent();
				}
	 	 	});  
	 	});
		
		$('.cartcheckpass').click(function(){
			var stockstatus=true;
			var $lrs = new Array();
			$('.shopping_operate').each(function (){
	 			$lrs.push({id:$(this).find('input[name="productId"]').val()});
	 		});
			var cartId=$('body').find('input[name="NowCartId"]').val();
			var totalfee= $("span[name='totalfee']").text();
			// TODO:添加了一个新的ajax，校验新型活动
			//处理购物车商品的库存信息
				$.ajax({
					type:"post",
					url:"../cart/checkcondition.do",
					async:false,
					data:{cartId:cartId},
					success : function(data) {
						$(".message_block2").html("<div><div>如果您审核通过该购物车后，将不能更改。</div><button class='bg-red cartcheckpass_sure submit_order'>确认审核</button>&nbsp;<button class='bg-red cartcheckpass_cancel submit_order'>取消审核</button></div>");
						$.each(data,function(i,act){
							/* if(i=='0'){
								$.each(act,function(j,pro){
									$(".message_block2").append("<div>您已参加活动"+j+"(<b>"+pro+"</b>)</div>");
								})
							} */
							$(".message_body").show();
							if(stockstatus){
				 	 				//审核请求
						 	 	$('.cartcheckpass_sure').click(function(){
						 	 		var cartId=$('body').find('input[name="NowCartId"]').val();
							   	  	$.ajax({
							 	 		type : "post",
							 	 		url : "../cart/cartcheckpass.do",
							 	 		async : false,
							 	 		timeout : 15000,
							 	 		dataType:"json",
							 	 		cache:false,
							 	 	    data:{cartId:cartId},
							 	 		success : function(data) {
							 	 			if(data.status==0){
							 	 				window.location.href="../ordm/index.do"; 
							 	 			}else{
							 	 				$(".message_block2").html(data.message);
								 	 			$(".message_body").fadeIn();
									 	 		$(".message_body").fadeOut(3000);
							 	 			} 
										}
							 	 	});
						 	 	})
						 	 	$(".cartcheckpass_cancel").click(function(){
						 	 		$(".message_body").hide();
						 	 	})
					 	 		}
							
						})
							
							
		 	 			
					}
				})
				
	 	 		
	 	 	});
		$('.cartcheckrefuse').click(function(){
			var counter=$("#counterName").text();
			$(".message_block2").html("<div>如果您退回审核购物车后，该订单将退回到<i style='color:red;'>"+ counter + "</i>购物车</div><button class='bg-red cartcheckrefuse_sure'>确认退回</button>&nbsp;<button class='bg-red cartcheckpass_cancel'>取消审核</button>");
	 	 	$(".message_body").css("display","block");
	 	 	$('.cartcheckrefuse_sure').click(function(){
	 	 		var cartId=$('body').find('input[name="NowCartId"]').val();
		   	  	$.ajax({
		 	 		type : "post",
		 	 		url : "../cart/cartcheckrefuse.do",
		 	 		async : false,
		 	 		timeout : 15000,
		 	 		dataType:"json",
		 	 		cache:false,
		 	 	    data:{cartId:cartId},
		 	 		success : function(data) {
		 	 			
		 	 			window.location.href="../order/index.do";
					}
		 	 	});  
	 	 	});
	 	 	$(".cartcheckpass_cancel").click(function(){
	 	 		$(".message_body").hide();
	 	 	})
		});
	});
 	//初始化购物车内容及按钮信息
 	function checkproductcontent(){
 		if($(".operation_one").length!=0){
			$(".operation").show();
			$(".operation").find('.cartcheckrefuse').attr("disabled",false);
			getpresentprice();
		}else{
			$(".operation").hide();
			$(".operationpanel").hide();
			$(".cartfuse").show();
			$(".operation").find('.cartcheckrefuse').attr("disabled",true);
		}
 	}
 	
 	//获取购物车面板信息
 	function getpresentprice(){
 		var counterId = $('input[name="counterId"]').val();
		var cartId= $('input[name="NowCartId"]').val();
		
 		$.post("../cart/getpresentprice.do",{cartId:cartId,counterId:counterId},function(data){
 			for(var key in data){
 				if(key=="materialfee"){
 					var materialprice=Number(data[key]).toFixed(2);
 					if(materialprice>=0){
 						$('span[name="materialname"]').text("超出物料金额:");
 						$('span[name="materialfee"]').text(materialprice);
 						$('span[name="materialname"]').css("color","red");
 						$('span[name="materialfee"]').css("color","red");
 					}else if(materialprice<0){
 						$('span[name="materialname"]').text("可配物料金额:");
 						$('span[name="materialfee"]').text(-materialprice);
 						$('span[name="materialname"]').css("color","green");
 						$('span[name="materialfee"]').css("color","green");
 					}else{
 						$('span[name="materialname"]').text("可配物料金额:");
 						$('span[name="materialfee"]').text(materialprice);
 						$('span[name="materialname"]').css("color","red");
 						$('span[name="materialfee"]').css("color","red");
 					}
 				}
 				if(key=="lqtys"){
 					$('span[name="qty"]').text(data[key][0]);
 					$('span[name="productqty"]').text(data[key][1]);
 					$('span[name="materialqty"]').text(data[key][2]);
 					$('span[name="totalfee"]').text(data[key][3].toFixed(2));
 					$(".operation_one").each(function(){
 						var number = $(this).find('input[type="number"]').val();
 						var cdd_price= $(this).find(".suitPrice").val();
 						$(this).find(".allfee").text("￥"+Number(number*cdd_price).toFixed(2));
 					});
 					if(parseInt(data["freeemoney"]/0.4)-parseInt(data[key][3])>0){
 						$('span[name="feeadd"]').text("添加");
 						$('span[name="reducefee"]').text(parseInt(data["freeemoney"]/0.4)-parseInt(data[key][3]));
 						$('span[name="feename"]').text("免邮费");
 					}else{
 						$('span[name="feeadd"]').text("已免邮费");
 						$('span[name="reducefee"]').text("");
 						$('span[name="feename"]').text("");
 					}
 				}
 			}
 		})
 	}
 	
 	//校验input number 内容是否符合提交规范
 	function checknumber(position){
 		if(position.val()<=0){
 			$(".message_block2").html("商品数量必须大于0!");
 			$(".message_body").fadeIn();
 	 		$(".message_body").fadeOut(3000);
 			return false;
 		}else{
 			if(position.val()%position.attr('step')!=0){
 				$(".message_block2").html("数量不符合规范：必须是"+position.attr('step')+"倍数!");
 				$(".message_body").fadeIn();
	 	 		$(".message_body").fadeOut(3000);
 				return false;
 			}
 			return true;
 		}
 	}
 	
</script>
</body>
</html>
