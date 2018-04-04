<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title></title>
	<link href="../css/index.css" rel="stylesheet">
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
		<div class="order_number">
		<div class="order_span">
				<span class="red order_span">待审核订单</span>
		</div>
		<input type="hidden" value="1705260011" id="orderid">
		<div class="information_settle">柜台名称:<span class="col-999" id="trade_no">${counter.counterName }</span></div>
		<div class="information_settle">柜台号:<span class="col-999">${counter.counterCode }</span></div>	
		<div class="clear"></div>
		</div>
		<div class="add_mar">
			<div class="address">
				<input type="hidden" name="counterId" value="${cart.counterId }">
				<input type="hidden" name="cartId" value="${cart.cartId }">
				<input type="hidden" id="NowCartId" name="NowCartId" value="${cart.cartId }">
				<img class="address_img" src="../img/address.png">
				<div class="address_div">
					<div>
						<span class="linkman">联系方式：</span><span id="contact">${counter.contact }	| ${counter.phone}</span>
					</div>
					<div>
						<span class="linkman">收货地址：</span><span id="address">${counter.address }</span>
					</div>
				</div>	
				<div class="clear"></div>
			</div>
		</div>
		 <!-- 明细清单-->
		  <div class="shopping_one">
		 	<%-- <div class="operation">
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
			 	    	<div class="button_block" style="margin:2px 0; float:right;">
			 	    		<a class="min min_bt"/>-</a>
							<input name="num" type="number" class="text_box" required="required" class="text_box" value="${cd.quantity }" />
							<a class="add add_bt"/>+</a>
			 	    	</div> 
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
					 		<div class="shopping_name">${cdd.productName }</div>
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
			 </c:forEach> --%>
			 <c:forEach items="${lcds }" var="cd">
					 <div class="operation_one">
						 	<div class="operation_top">
						 	    <span class="activityName">${cd.activityName }</span>
						 	    <button class="del bg-red">删  除</button>
						 	    <div class="clear"></div>
						 	    <span style="float:left; line-height:22px; padding-left:5px;">数量:</span>
						 	    <div class="add_del">
						 	    	<input name="activityId" type="hidden" value="${cd.activityId }">
						 	    	<input name="activityName" type="hidden" value="${cd.activityName }">
						 	    	<input name="detailId" type="hidden" value="${cd.detailId }">
						 	    	<input class="suitPrice" type="hidden" value="${cd.price}">
						 	    	<div class="button_block" style="height:22px;line-height:20px;margin-left:10px;">
						 	    		<%-- <a class="min min_bt" style="height:20px;line-height:20px;"/>-</a>--%>
											<input name="num" type="number" style="height:20px;line-height:20px;" class="text_box" required="required" class="text_box" value="${cd.quantity}" readonly="readonly" />
											<!-- <a class="add add_bt" style="height:20px;line-height:20px;"/>+</a>  -->
						 	    	</div>
										<div class="clear"></div>
						 	    </div>
						 	    <span class="alltext"><i style="color:red;" class="allfee"></i></span>
						 	    <div class="clear"></div>
						 	</div>
							 <c:forEach items="${lcdds }" var="cdd">
							 <c:if test="${cdd.cartDetailId==cd.detailId }">
								 	<div class="shopping_operate">
								 		<img class="img_new" src="../img/img_new.jpg">
								 		<div class="img_div">
								 			<div>
								 				<div class="shopping_name">${cdd.productName}</div>
								 				<input name="productId" type="hidden" value="${cdd.productId }">
								 			</div>
								 			<div class="user_left1 member_price">单价:<span class="cdd_price">${cdd.price }</span></div>
								 			<div class="user_left1"></div>
								 			<div class="user_left1 retail_price">数量:<span class="product_num">${cdd.quantity }</span></div>
								 			<div class="user_left1 retail_price tips">库存:<span name="tips"></span></div>
								 		</div>
								 		<div class="clear"></div>
								 		<c:forEach items="${lps }" var="p">
								 			<c:if test="${p.productId==cdd.productId }"><div class="product_type">${p.typeName }</div></c:if>
								 		</c:forEach>
								 	</div>
							 </c:if>
							 </c:forEach>
					 </div>
				 </c:forEach>
			 <!-- 底部 -->
			 <div class="operationpanel1">
			 	<div class="user_left1"><span>商品数量:</span> <span name="productqty"></span></div>
			 	<div class="user_left1"><span>赠品&物料数量:</span><span name="materialqty"></span></div>
		 		<div class="user_left1"><span>总数量:</span> <span name="qty"></span></div>
		 		<div class="user_left1"><span name="materialname">物料价格：</span><span name="materialfee"></span></div>
		 		<div class="user_left1">
		 			<span name="feeadd" style="color:green"></span>	
		 			<span name="reducefee"></span>
		 			<span name="feename"></span>
		 		</div>
		 		<div class="user_left1"><span >总价格：</span><span name="totalfee"></span></div>
		    <div>
        	<button type="button" class="cartcheckpass submit_order">审核通过</button>
        	<button type="button" class="cartcheckrefuse submit_order">退        回</button>
        </div>   
		    <div class="clear"></div>
			 </div>
			 <div class="cartfuse" style="float:right; margin:10px auto;">
       	<button type="button" class="cartcheckrefuse submit_order">退        回</button>
        </div>
	     <div class="clear"></div>
		</div>			 
	</div>
	<%@ include file="common/commonF.jsp"%>
<script src="../js/jquery.js"></script>
<script src="../js/index.js"></script>
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
				position.find('.activityName').text(position.find('.shopping_operate:eq(0) .shopping_name').text());
				$.post("../product/getminorderunit.do",{productId:position.find('.shopping_operate:eq(0) input[name="productId"]').val()}
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
	 	 			$(".message_block2").html("<div class='message_top'>提示</div><div class='message_center'>"+errorThrown+"删除购物车失败!</div>");
	 	 			$(".message_body").fadeIn();
		 	 		$(".message_body").fadeOut(3000);
	 	 		},
	 	 		success : function(data) {
	 	 			$(".message_block2").html("<div class='message_top'>提示</div><div class='message_center'>删除购物车成功</div>");
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
			//处理购物车商品的库存信息
				if(stockstatus){
 	 				//审核请求
					$(".message_block2").html("<div class='message_top'>提示</div><div class='message_center'>"
							+"<div>如果您审核通过该购物车后，将不能更改。</div><div><button class='bg-red cartcheckpass_sure'>确认审核</button>"
							+"<button class='bg-red cartcheckpass_cancel'>取消审核</button></div></div>");
			 	 	$(".message_body").show();
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
				 	 				$(".message_block2").html("<div class='message_top'>提示</div><div class='message_center'>订单出现异常，请联系信息部。</div>");
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
	 	 		
	 	 	});
		$('.cartcheckrefuse').click(function(){
			var counter=$("#counterName").text();
			$(".message_block2").html("<div class='message_top'>提示</div><div class='message_center'>"
					+"<div>如果您退回审核购物车后，该订单将退回到<i style='color:red;'>"+ counter + "</i>购物车<div>"
					+"<div><button class='bg-red cartcheckrefuse_sure'>确认退回</button>"
					+"<button class='bg-red cartcheckpass_cancel'>取消审核</button></div></div>");
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
 						$('span[name="materialname"]').css("color","#6dbb44");
 						$('span[name="materialfee"]').css("color","#6dbb44");
 					}else{
 						$('span[name="materialname"]').text("可配物料金额:");
 						$('span[name="materialfee"]').text(materialprice);
 						$('span[name="materialname"]').css("color","#ff5050");
 						$('span[name="materialfee"]').css("color","#ff5050");
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
 			$(".message_block2").html("<div class='message_top'>提示</div><div class='message_center'>商品数量必须大于0!</div>");
 			$(".message_body").fadeIn();
 	 		$(".message_body").fadeOut(3000);
 			return false;
 		}else{
 			if(position.val()%position.attr('step')!=0){
 				$(".message_block2").html("<div class='message_top'>提示</div><div class='message_center'>数量不符合规范：必须是"+position.attr('step')+"倍数!</div>");
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
