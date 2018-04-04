<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>购物车</title>
	<link href="../css/index.css" rel="stylesheet">
	<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"> 
</head>
<body>
	<%@ include file="common/commonHT.jsp"%>
	<div class="address_body">
		<div class="address_block">
			<div>您要提交${counter.counterName }店铺的购物车吗？</div>
			<div>地址：${counter.address }</div>
			<span>联系人：</span><span id="contact">${counter.contact }</span>
			<div class="address_button">
				<button type="button" name="savecart" class="bg-red submit_left">确定</button>
				<button type="button" class="bg-red cancel_cart submit_left">取消</button>
			</div>
		</div>
	</div>
	<div class="center">	
		<!-- 头部 -->
		<div class="address">
			<input type="hidden" name="counterId" value="${cart.counterId }">
			<input type="hidden" name="cartId" value="${cart.cartId }">
			<img class="address_img" src="../img/address.png">
			<div class="address_div">
				<div>
					<%-- <span>门店名称：</span><span id="counterName">${counter.counterName }</span>
					<span>柜台号：</span><span id="counterCode">${counter.counterCode }</span> --%>
					<span class="linkman">联系人：</span><span id="contact">${counter.contact }</span>
				</div>
				<div>
					<span class="linkman">收货地址：</span><span id="address"> ${counter.address }</span>
				</div>
			</div>	
			<div class="clear"></div>
		</div>
		<div class="cart_number">
			<div class="user_left1"><span>商品数量:</span><span name="productqty"></span></div>
			<div class="user_left1"><span>赠品&物料数量:</span><span name="materialqty"></span></div>
			<div class="user_left1"><span>总数量:</span><span name="qty"></span></div>
			<div class="clear"></div>
		</div>
		 <!-- 明细清单-->
		  <div class="shopping_one">
				 <c:forEach items="${lcds }" var="cd">
					 <div class="operation_one">
						 	<div class="operation_top">
						 	    <span class="activityName">${cd.activityName }</span>
						 	    <button class="del bg-red">删  除</button>
						 	    <div class="clear"></div>
						 	    <div class="add_del">
						 	    	<input name="activityId" type="hidden" value="${cd.activityId }">
						 	    	<input name="activityName" type="hidden" value="${cd.activityName }">
						 	    	<input name="detailId" type="hidden" value="${cd.detailId }">
						 	    	<input class="suitPrice" type="hidden" value="${cd.price}">
						 	    	<div class="button_block" style="height:22px;line-height:20px;margin-left:10px;">
						 	    		<a class="min min_bt" style="height:20px;line-height:20px;"/>-</a>
											<input name="num" type="number" style="height:20px;line-height:20px;" class="text_box" required="required" class="text_box" value="${cd.quantity}" />
											<a class="add add_bt" style="height:20px;line-height:20px;"/>+</a>
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
			 <div class="operationpanel">
		 		<div class='number_pay'>
		 			<span >总价格：</span><span name="totalfee"></span>&nbsp;&nbsp;
		 			<span name="feeadd"></span><span name="reducefee"></span><span name="feename"></span>
       		<br>
        	<span name="materialname">物料价格：</span>	<span name="materialfee"></span>&nbsp;
		 			
		 		</div>
        <button type="button" id="order_body" class="submit_order bg-green">提    交</button>
			 	<div class="clear"></div>
			 </div> 
		</div>			 
	</div>
	<%@ include file="common/commonF.jsp"%>
<script src="../js/jquery.js"></script>
<script src="../js/index.js"></script>
<script type="text/javascript">
 	$(document).ready(function(){
 		var stockId = $('input[name="stockId"]').val();
 		//初始化购物车面板
 		initadddel();
 		checkproductcontent();
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
		$(document).on("click",".submit_order",function(){
			var stockstatus=true;
			var $lrs = new Array();
			$('.shopping_operate').each(function (){
	 			$lrs.push({id:$(this).find('input[name="productId"]').val(),subId:stockId});
	 		});
			//处理购物车商品的库存信息
		$.ajax({
			type : "get",                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
 	 		url : "../microservice/stockservice.do",
 	 		async : false,
 	 		timeout : 15000,
 	 		dataType:"json",
 	 		data:{items:JSON.stringify($lrs)},
 	 		cache:false,
 	 		success : function(data) {
 	 			$.each(data.data, function(id,item) {
 	 			
 	 				 $('.shopping_operate').each( function() {		
 	 					if($(this).find('input[name="productId"]').val()==item.pid ){
 	 						if(item.v-parseInt($(this).parent().find('input[name="num"]').val())*parseInt($(this).find('.product_num').text())<0){
 	 							$(".message_body").css("display","block");
 	 				 	 		$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>对不起，部分商品库存不足，该购物车不能审核通过。</div>");
 	 							stockstatus=false;
 	 							$(".tips").show();
	 	 						$(this).find('span[name="tips"]').text(item.v);
	 	 						$(this).css("background","#FFA4AF");
	 	 						$(this).find('span[name="tips"]').css({"color":"#6dbb44","font-weight":"bold","font-size":"16px"});
 	 						}else{
 	 							$(".tips").show();
	 	 						$(this).find('span[name="tips"]').text("正常");
	 	 						$(this).find('span[name="tips"]').css("color","black");
 	 						}
 	 					}
 	 				})
 	 			}); 
			}
			});
			if(stockstatus){
				var counterId = $('input[name="counterId"]').val();
				var cartId= $('input[name="cartId"]').val();	 		
		 		$.post("../cart/getpresentprice.do",{cartId:cartId,counterId:counterId},function(data){
		 			for(var key in data){
		 				if(key=="materialfee"){
		 					// TODO: 增加新活动的金额提示
		 					var materialprice=Number(data[key]).toFixed(2);
		 					if(materialprice>=0){
		 						$('span[name="materialname"]').text("超出物料金额:");
		 						$('span[name="materialfee"]').text(materialprice);
		 						$('span[name="materialfee"]').css("color","#ff5050");
		 					}else{
		 						$('span[name="materialname"]').text("可配物料金额:");
		 						$('span[name="materialfee"]').text(-materialprice);
		 						$('span[name="materialfee"]').css("color","#6dbb44");
		 					}
		 				}
		 				if(key=="lqtys"){
		 					$('span[name="qty"]').text( data[key][0]);
		 					$('span[name="productqty"]').text(data[key][1]);
		 					$('span[name="materialqty"]').text(data[key][2]);
		 					$('span[name="totalfee"]').text(data[key][3].toFixed(2));
		 					//校验submit_order按钮是否可用，条件是物料数量是否为0
		 				 	if(parseInt(materialprice)<0&&data[key][2]==0){
		 				 		$(".message_body").show();
		 						$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'><div>赠品&物料数量不能为<b style='color:red; font-size:16px;'>0</b>!</div><div>您还有可配物料<b style='color:red; font-size:16px;'>" + -materialprice +"</b>,请您选择赠品&物料数后再进行提交.</div></div>");
		 					}else if(materialprice>0){
		 						var address_body=$(".address_body").html();
		 						$(".message_block2").html("<div class='message_top'>提交购物车</div><div class='message_center'>"+address_body+"<p style='font-weight:bold;color:red'>您有超出物料<b style='color:black; font-size:16px;'>￥"+ materialprice +"</b>元，参照可配金额，选择赠品及物料。<br/>未选择将视为放弃，后期不予补发!</p></div>");
		 						$(".message_body").show();
		 					}else{
		 						var address_body=$(".address_body").html();
		 						$(".message_block2").html("<div class='message_top'>提交购物车</div><div class='message_center'>"+address_body+"<p style='font-weight:bold;color:red'>您有可配物料<b style='color:black; font-size:16px;'>￥"+ -materialprice +"</b>元，参照可配金额，选择赠品及物料。<br/>未选择将视为放弃，后期不予补发!</p></div>");
		 						$(".message_body").show();
		 					};
		 					
		 				}
		 			}
				$(".address_body").hide();
				$(".cancel_cart").click(function(){
					$(".message_body").hide();
				});
				$('button[name="savecart"]').click(function(){
					var counterId = $('input[name="counterId"]').val();
					var cartId= $('input[name="cartId"]').val();
					if(counterId!=null && counterId!=0){
						$.ajax({                                                                  
				 	 		url : "../cart/cartcommit.do",
				 	 		async : false,
				 	 		timeout : 15000,
				 	 		dataType:"json",
				 	 		cache:false,
				 	 	    data:{counterId:counterId,cartId:cartId},
				 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
				 	 			$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>"+errorThrown+"提交失败!</div>");
				 	 			$(".message_body").fadeIn();
					 	 		$(".message_body").fadeOut(1000);
				 	 		},
				 	 		success : function(data) {
				 	 			if(data.status==0){
				 	 				window.location.href="../ordm/index.do"; 
				 	 			}else{
				 	 				$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>订单出现异常，请联系信息部。</div>");
					 	 			$(".message_body").fadeIn();
						 	 		$(".message_body").fadeOut(1000);
				 	 			} 
							}
				 	 	});  
					}
				});
		 	});
				
			}
		});
		//修改购物车数量
		$('input[name="num"]').focus(function(){
			var firstnum=$(this).val();
			$(this).blur(function(){
				if(checknumber($(this)) == false){
					$(this).val(firstnum);
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
		 	 			$(".message_block2").html(errorThrown+"<div class='message_top'>提示信息</div><div class='message_center'>修改购物车失败!</div>");
		 	 			$(".message_body").fadeIn();
			 	 		$(".message_body").fadeOut(1000);
		 	 		},
		 	 		success : function(data) {
		 	 			$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>修改购物车成功!</div>");
		 	 			$(".message_body").fadeIn();
			 	 		$(".message_body").fadeOut(1000);
		 	 			checkproductcontent();
					}
		 	 	});  
		 	})
		})
		//删除商品
		$('.del').click(function(){
			var detailId=$(this).parent().find('input[name="detailId"]').val();
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
	 	 			$(".message_block2").html(errorThrown+"<div class='message_top'>提示信息</div><div class='message_center'>删除购物车失败!</div>");
	 	 			$(".message_body").fadeIn();
		 	 		$(".message_body").fadeOut(1000);
	 	 		},
	 	 		success : function(data) {
	 	 			$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>删除购物车成功!"+data.message+"</div>");
	 	 			$(".message_body").fadeIn();
		 	 		$(".message_body").fadeOut(1000);
	 	 			position.parent().parent().remove();
	 	 			checkproductcontent();
				}
	 	 	});  
	 	});
		
		$(".min_bt").click(function(){
			if(checknumber($(this).parent().find('input[name="num"]')) == false){
				var step=$(this).parent().find('input[name="num"]').attr("step");
				$(this).parent().find('input[name="num"]').val(step);
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
	});
 	
 	//初始化购物车内容及按钮信息
 	function checkproductcontent(){
 		if($(".operation_one").length!=0){
			$(".operation").show();
			getpresentprice()
		}else{
			$(".operation").hide();
			$(".operationpanel").hide();
		}
 	}
 	
 	//获取购物车面板信息
 	function getpresentprice(){
 		var counterId = $('input[name="counterId"]').val();
		var cartId= $('input[name="cartId"]').val();
 		$.post("../cart/getpresentprice.do",{cartId:cartId,counterId:counterId},function(data){
 			for(var key in data){
 				if(key=="materialfee"){
 					var materialprice=Number(data[key]).toFixed(2);
 					if(materialprice>=0){
 						$('span[name="materialname"]').text("超出物料金额:");
 						$('span[name="materialfee"]').text(materialprice);
 						$('span[name="materialfee"]').css("color","#ff5050");
 					}else{
 						$('span[name="materialname"]').text("可配物料金额:");
 						$('span[name="materialfee"]').text(-materialprice);
 						$('span[name="materialfee"]').css("color","#6dbb44");
 					}
 				}
 				if(key=="lqtys"){
 					$('span[name="qty"]').text( data[key][0]);
 					$('span[name="productqty"]').text(data[key][1]);
 					$('span[name="materialqty"]').text(data[key][2]);
 					$('span[name="totalfee"]').text(data[key][3].toFixed(2));
 					$(".operation_one").each(function(){
 						var number = $(this).find('input[type="number"]').val();
 						var cdd_price= $(this).find('.suitPrice').val();
 						$(this).find(".allfee").text("￥"+Number(number*cdd_price).toFixed(2));
 					});
 					
 					//校验submit_order按钮是否可用，条件是物料数量是否为0
 					if(parseInt(data["freeemoney"]/data["discout"])-parseInt(data[key][3])>0){
 						$('span[name="feeadd"]').text("差");
 						$('span[name="reducefee"]').text(parseInt(data["freeemoney"]/data["discout"])-parseInt(data[key][3]));
 						$('span[name="feename"]').text("元免邮费");
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
 			$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>商品数量必须大于0!</div>");
 			$(".message_body").fadeIn();
 	 		$(".message_body").fadeOut(1000);
 			return false;
 		}else{
 			if(position.val()%position.attr('step')!=0){
 				$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>数量不符合规范：必须是"+position.attr('step')+"倍数!</div>");
 				$(".message_body").fadeIn();
	 	 		$(".message_body").fadeOut(1000);
 				return false;
 			}
 			return true;
 		}
 	}
 	
</script>
</body>
</html>
