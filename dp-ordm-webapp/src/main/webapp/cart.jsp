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
	<style type="text/css">
	.address_button button{
		margin: 0px 10px;
	}
	</style>
</head>
<body>
	<%@ include file="common/commonHT.jsp"%>
	<div class="address_body">
		<div class="address_block">
			<div>您要提交${counter.counterName }店铺的购物车吗？</div>
			<div>地址：${counter.address }</div>
			<span>联系人：</span><span id="contact">${counter.contact }</span>
			<div class="address_button">
				<button type="button" name="savecart" class="bg-red">确定</button>
				<button type="button" class="bg-red cancel_cart">取消</button>
			</div>
		</div>
	</div>
	<div class="center">	
		<!-- 头部 -->
		<div class="cart_all">购&nbsp;&nbsp;物&nbsp;&nbsp;车</div>	
		<div class="address">
			<input type="hidden" name="counterId" value="${cart.counterId }">
			<input type="hidden" name="cartId" value="${cart.cartId }">
			<div>
				<span>门店名称：</span><span id="counterName">${counter.counterName }</span>
				<span>柜台号：</span><span id="counterCode">${counter.counterCode }</span>
				<span>联系人：</span><span id="contact">${counter.contact }</span>
			</div>
			<div>
				<span>收货地址：</span><span id="address"> ${counter.address }</span>
				<button type="button" class="cart_clear big bg-red">一键清除</button>
				<div class="clear"></div>
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
				 	<li class="operate">库存</li>
		 		</ul>					
			 </div>
			 <c:forEach items="${lcds }" var="cd">
			 <div class="operation_one">
			 	<div class="operation_top">
			 			<c:if test="${cd.revId != '' && cd.revId != null}">
			 	    <img class="shopping_nameReserve" src="../img/new1.png">
			 	    </c:if>
			 	    <span class="activityName">
			 	    ${cd.activityName }</span>
			 	    <div class="add_del">
			 	    	<input name="activityId" type="hidden" value="${cd.activityId }">
			 	    	<input name="activityName" type="hidden" value="${cd.activityName }">
			 	    	<input name="detailId" type="hidden" value="${cd.detailId }">
			 	    	<input class="suitPrice" type="hidden" value="${cd.price}">
			 	    	<a class="del">删  除</a>
			 	    	<div class="button_block" style="margin:2px 0; float:right;">
			 	    		<a class="min min_bt"/>-</a>
							<input name="num" type="number" class="text_box" required="required" class="text_box" value="${cd.quantity}" />
							<a class="add add_bt"/>+</a>
			 	    	</div>
						<span class="alltext"><i style="color:red;" class="allfee"></i></span>
						<div class="clear"></div>
			 	    </div>
			 	    <div class="clear"></div>
			 	</div>
			 	<!-- 明细条目 -->
			 	<c:forEach items="${lcdds }" var="cdd">
			 	<c:if test="${cdd.cartDetailDesc.cartDetailId==cd.detailId }">
			 	<div class="shopping_operate">
				 	<ul>
				 		<div class="clear"></div>
					 	<li class="information">
					 		<c:forEach items="${lps }" var="p">
					 			<c:if test="${p.productId==cdd.cartDetailDesc.productId }">
					 				<c:if test="${cd.activityId==''||cd.activityId==null||cd.activityId=='undefined'}">
							 			<c:if test="${p.prePro==2||p.prePro==3}">
							 				<div class="shopping_name"><img class="shopping_nameReserve" src="../img/owes1.png">${cdd.cartDetailDesc.productName }</div>
							 			</c:if>
							 			<c:if test="${p.prePro==0||p.prePro==1}">
							 				<div class="shopping_name">${cdd.cartDetailDesc.productName }</div>
							 			</c:if>
						 			</c:if>
						 			<c:if test="${cd.activityId!=''&&cd.activityId!=null&&cd.activityId!='undefined'}">
						 				<c:if test="${cdd.cartDetailDesc.reserve==true}">
						 					<c:if test="${cdd.status==2||cdd.status==3}">
								 				<div class="shopping_name"><img class="shopping_nameReserve" src="../img/owes1.png">${cdd.cartDetailDesc.productName }</div>
								 			</c:if>
								 			<c:if test="${cdd.status==0||cdd.status==1}">
								 				<div class="shopping_name">${cdd.cartDetailDesc.productName }</div>
								 			</c:if>
						 				</c:if>
						 				<c:if test="${cdd.cartDetailDesc.reserve!=true}">
						 					<div class="shopping_name">${cdd.cartDetailDesc.productName }</div>
						 				</c:if>
						 			</c:if>
						 		</c:if>
					 		</c:forEach>
					 		<input name="productId" type="hidden" value="${cdd.cartDetailDesc.productId }">
					 	</li>
					 	<li class="operate cdd_price">${cdd.cartDetailDesc.price }</li>
					 	<li class="operate product_num">${cdd.cartDetailDesc.quantity }</li>
					 	<li class="operate">
					 		<c:forEach items="${lps }" var="p">
					 			<c:if test="${p.productId==cdd.cartDetailDesc.productId }">
						 			<c:if test="${cd.activityId==''||cd.activityId==null||cd.activityId=='undefined'}">
						 			<input type="hidden" value="${p.prePro}" name="prePro">
						 			</c:if>
						 			<c:if test="${cd.activityId!=''&&cd.activityId!=null&&cd.activityId!='undefined'}">
						 			<input type="hidden" value="${cdd.status}" name="prePro"> 
						 			</c:if>
					 			${p.typeName }
					 			</c:if>
					 		</c:forEach>
					 	</li>
					 	<li class="operate"><span name="tips"></span></li>
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
		 		<span>总数量:</span> <strong> <span name="qty"></span></strong>&nbsp;
		 		<span>商品数量:</span> <strong> <span name="productqty"></span></strong>&nbsp;
		 		<span>赠品&物料数量:</span> <strong> <span name="materialqty"></strong></span>&nbsp;
		        <span name="materialname">物料价格：</span>	<strong> <span name="materialfee"></span></strong>&nbsp;
		        <span name="feeadd" style="color:green"></span>	<strong style="color:red;"><span name="reducefee"></span></strong><span style="color:green" name="feename"></span>&nbsp;
		        <span >总价格：</span>	<strong> <span name="totalfee"></span></strong>&nbsp;
		        <div style="text-align: right; margin: 0px 20px;"><button type="button" class="submit_order big bg-red">提    交</button></div>
		        <br/>
		        <hr/>
			 </div>
		</div>			 
	</div>
	<%@ include file="common/commonF.jsp"%>
<script src="../js/jquery.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/jquery.js").lastModified() %>"></script>
<script src="../js/index.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/index.js").lastModified()%>"></script>
<script type="text/javascript">
 	$(document).ready(function(){
 		var stockId = $('input[name="stockId"]').val();
 		//初始化购物车面板
 		initadddel();
 		checkproductcontent();
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
		$(".cart_clear").click(function(){
			$(".message_body").css("display","block");
	 	 		$(".message_block2").html("<div>您确认清除该购物车，清除后购物车后将无任何产品。<div class='address_button'>"
	 					+"<button type='button' name='savecart' class='bg-red sure_clear'>确定</button>"
	 					+"<button type='button' class='bg-red cancel_cart'>取消</button></div></div>");
			var cartid=$("input[name='cartId']").val();
				$(".sure_clear").click(function(){
					$.ajax({
			 	 		type : "post",                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
			 	 		url : "../cart/clear.do",
			 	 		async : false,
			 	 		timeout : 15000,
			 	 		dataType:ms_proto,
			 	 		data:{cartID:cartid},
			 	 		cache:false,
			 	 		success : function(data) {
			 	 			window.location.reload();
			 	 		},
			 	 		error:function(data) {}
				})
			});
			$(".cancel_cart").click(function(){
				$(".message_body").hide();
			})
		});
		$(".submit_order").click(function(){
			var stockstatus=true;
			var $lrs = new Array();
			var $pro = new Array();
			$('.shopping_operate').each(function (){
	 			$lrs.push({id:$(this).find('input[name="productId"]').val(),subId:stockId});
	 		});
			var a=0;
			//处理购物车商品的库存信息
			$.ajax({
	 	 		type : "get",                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
	 	 		url : ms_host+"/microservice/stockservice.do",
	 	 		async : false,
	 	 		timeout : 15000,
	 	 		dataType:ms_proto,
	 	 		data:{items:JSON.stringify($lrs)},
	 	 		cache:false,
	 	 		success : function(data) {
	 	 			$.each(data.data, function(id,item) {
	 	 				$('.shopping_operate').each( function() {
	 	 						if($(this).find('input[name="productId"]').val()==item.pid ){
	 	 						  if($(this).find("input[name='prePro']").val()==1){
		 	 							if(item.v-parseInt($(this).parent().find('input[name="num"]').val())*parseInt($(this).find('.product_num').text())<0){
		 	 								$pro.push({id:$(this).find('input[name="productId"]').val(),subId:stockId});  
		 	 								stockstatus=false;	
		 	 								  a++;
			 	 	 				 	 	$(this).find('span[name="tips"]').text("打欠"+parseInt($(this).parent().find('input[name="num"]').val())*parseInt($(this).find('.product_num').text()));
		 		 	 						$(this).css("background","#FFA4AF");
		 		 	 						$(this).find('span[name="tips"]').css({"color":"green","font-weight":"bold","font-size":"16px"});
			 	 	 						}else{
			 		 	 						$(this).find('span[name="tips"]').text("正常");
			 		 	 						$(this).find('span[name="tips"]').css("color","black");
			 	 	 						}
	 	 							}else if($(this).find("input[name='prePro']").val()==0){
	 	 								if(item.v-parseInt($(this).parent().find('input[name="num"]').val())*parseInt($(this).find('.product_num').text())<0){
	 	 	 	 							$(".message_body").css("display","block");
	 	 	 	 				 	 		$(".message_block2").html("<div>对不起，部分商品库存不足，该购物车不能审核通过。</div>");
	 	 	 	 							stockstatus=false;
	 	 		 	 						$(this).find('span[name="tips"]').text(item.v);
	 	 		 	 						$(this).css("background","#FFA4AF");
	 	 		 	 						$(this).find('span[name="tips"]').css({"color":"green","font-weight":"bold","font-size":"16px"});
	 	 	 	 						}else{
	 	 		 	 						$(this).find('span[name="tips"]').text("正常");
	 	 		 	 						$(this).find('span[name="tips"]').css("color","black");
	 	 	 	 						}
	 	 							}
	 	 	 						
	 	 	 					}
	 	 					
	 	 				})
	 	 			});
				}
				});
				if(a>0){
				 $(".message_body").css("display","block");
	 	 		 $(".message_block2").html("<div>您有打欠产品需要进行打欠处理，是否打欠。</div><div><button type='button' class='bid bg-red submit_order sure_owe'>确定</button>"
					 +"<button type='button' class='big bg-red submit_order cancel_owe'>取消</button></div>");
		 	 		 $(".sure_owe").click(function(){
		 	 	 	     stockstatus=true;
		  	 		   $(".message_body").hide();
				  	 		if(stockstatus){
										var counterId = $('input[name="counterId"]').val();
										var cartId= $('input[name="cartId"]').val();	 		
								 		$.post("../cart/getpresentprice.do",{cartId:cartId,counterId:counterId},function(data){
										 			for(var key in data){
										 				if(key=="materialfee"){
										 					var materialprice=Number(data[key]).toFixed(2);
										 					if(materialprice>=0){
										 						$('span[name="materialname"]').text("超出物料金额:");
										 						$('span[name="materialfee"]').text(materialprice);
										 						$('span[name="materialname"]').css("color","red");
										 						$('span[name="materialfee"]').css("color","red");
										 					}else{
										 						$('span[name="materialname"]').text("可配物料金额:");
										 						$('span[name="materialfee"]').text(-materialprice);
										 						$('span[name="materialname"]').css("color","green");
										 						$('span[name="materialfee"]').css("color","green");
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
										 						$(".message_block2").html("<div>赠品&物料数量不能为<b style='color:red; font-size:16px;'>0</b>!</div><div>您还有可配物料<b style='color:red; font-size:16px;'>" + -materialprice +"</b>,请您选择赠品&物料数后再进行提交.</div>");
										 					}else if(materialprice>0){
										 						$(".message_block2").html($(".address_body").html()+"<p style='font-weight:bold;color:red'>您有超出物料<b style='color:black; font-size:16px;'>￥"+ materialprice +"</b>元，参照可配金额，选择赠品及物料。<br/>未选择将视为放弃，后期不予补发!</p>");
										 						$(".message_body").show();
										 					}else{
										 						$(".message_block2").html($(".address_body").html()+"<p style='font-weight:bold;color:red'>您有可配物料<b style='color:black; font-size:16px;'>￥"+ -materialprice +"</b>元，参照可配金额，选择赠品及物料。<br/>未选择将视为放弃，后期不予补发!</p>");
										 						$(".message_body").show();
										 					};
										 					/* if(data["myAmount"]>0){
										 						$(".message_block2").append("<div>您已选石斛兰<b style='color:red'>"+data["myAmount"]+"</b>元(折扣前)<div>");
										 						if(data["doAct"] != undefined){
										 							$.each(data["doAct"],function(i,actIds){
											 							$(".message_block2").append("<div>您已参加活动"+i+"(<b>"+actIds+"</b>)</div>");
												 					})
										 						}
										 						 
										 					} */
										 				}
										 			}
												$(".address_body").hide();
												$(".cancel_cart").click(function(){
													$(".message_body").hide();
												});
												$('button[name="savecart"]').click(function(){
													var counterId = $('input[name="counterId"]').val();
													var cartId= $('input[name="cartId"]').val();
								 	 					$.ajax({
									 	 					url : "../microservice/checkflag.do",
												 	 		async : false,
												 	 		timeout : 15000,
												 	 		dataType:"json",
												 	 		cache:false,
												 	 	  data:{pids:JSON.stringify($pro)},
												 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
												 	 			$(".message_block2").html(errorThrown+"提交失败!");
												 	 			$(".message_body").fadeIn();
													 	 		$(".message_body").fadeOut(3000);
												 	 		},
												 	 		success:function(pid){
												 	 			if(counterId!=null && counterId!=0){
																	$.ajax({                                                                  
															 	 		url : "../cart/cartcommit.do",
															 	 		async : false,
															 	 		timeout : 15000,
															 	 		dataType:"json",
															 	 		cache:false,
															 	 	    data:{counterId:counterId,cartId:cartId},
															 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
															 	 			$(".message_block2").html(errorThrown+"提交失败!");
															 	 			$(".message_body").fadeIn();
																 	 		$(".message_body").fadeOut(3000);
															 	 		},
															 	 		success : function(data) {
															 	 			if(data.status==0){
															 	 				if(data.message!='' && data.message!=null && data.message!= 'undefined'){
															 	 					$(".message_block2").html(data.message);
																		 	 		setTimeout("window.location.href='../ordm/index.do'",5000);
															 	 				}else{
															 	 					window.location.href='../ordm/index.do';
															 	 				}
															 	 			}else{
															 	 				$(".message_block2").html(data.message);
																 	 			$(".message_body").fadeIn();
																	 	 		$(".message_body").fadeOut(5000);
															 	 			} 
																		}
															 	 	});  
																}
												 	 		
												 	 		}
									 	 				})
												});
								 	});
							}  
		  	 		});
		  	 		$(".cancel_owe").click(function(){
		  	 		   $(".message_body").hide();
		 		 	 		stockstatus=false;
		  	 		});
			}else{
				if(stockstatus){
					var counterId = $('input[name="counterId"]').val();
					var cartId= $('input[name="cartId"]').val();	 		
			 		$.post("../cart/getpresentprice.do",{cartId:cartId,counterId:counterId},function(data){
			 			for(var key in data){
			 				if(key=="materialfee"){
			 					var materialprice=Number(data[key]).toFixed(2);
			 					if(materialprice>=0){
			 						$('span[name="materialname"]').text("超出物料金额:");
			 						$('span[name="materialfee"]').text(materialprice);
			 						$('span[name="materialname"]').css("color","red");
			 						$('span[name="materialfee"]').css("color","red");
			 					}else{
			 						$('span[name="materialname"]').text("可配物料金额:");
			 						$('span[name="materialfee"]').text(-materialprice);
			 						$('span[name="materialname"]').css("color","green");
			 						$('span[name="materialfee"]').css("color","green");
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
			 						$(".message_block2").html("<div>赠品&物料数量不能为<b style='color:red; font-size:16px;'>0</b>!</div><div>您还有可配物料<b style='color:red; font-size:16px;'>" + -materialprice +"</b>,请您选择赠品&物料数后再进行提交.</div>");
			 					}else if(materialprice>0){
			 						$(".message_block2").html($(".address_body").html()+"<p style='font-weight:bold;color:red'>您有超出物料<b style='color:black; font-size:16px;'>￥"+ materialprice +"</b>元，参照可配金额，选择赠品及物料。<br/>未选择将视为放弃，后期不予补发!</p>");
			 						$(".message_body").show();
			 					}else{
			 						$(".message_block2").html($(".address_body").html()+"<p style='font-weight:bold;color:red'>您有可配物料<b style='color:black; font-size:16px;'>￥"+ -materialprice +"</b>元，参照可配金额，选择赠品及物料。<br/>未选择将视为放弃，后期不予补发!</p>");
			 						$(".message_body").show();
			 					};
			 					/* if(data["myAmount"]>0){
			 						$(".message_block2").append("<div>您已选石斛兰<b style='color:red'>"+data["myAmount"]+"</b>元(折扣前)<div>");
			 						if(data["doAct"] != undefined){
			 							$.each(data["doAct"],function(i,actIds){
				 							$(".message_block2").append("<div>您已参加活动"+i+"(<b>"+actIds+"</b>)</div>");
					 					})
			 						}
			 						 
			 					} */
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
					 	 			$(".message_block2").html(errorThrown+"提交失败!");
					 	 			$(".message_body").fadeIn();
						 	 		$(".message_body").fadeOut(5000);
					 	 		},
					 	 		success : function(data) {
					 	 			if(data.status==0){
					 	 				if(data.message!='' && data.message!=null && data.message!= 'undefined'){
					 	 					$(".message_block2").html(data.message);
							 	 			setTimeout("window.location.href='../ordm/index.do'",2000);
					 	 				}else{
					 	 					window.location.href='../ordm/index.do';
					 	 				}
					 	 				
					 	 			}else{
					 	 				$(".message_block2").html(data.message);
						 	 			$(".message_body").fadeIn();
							 	 		$(".message_body").fadeOut(5000);
					 	 			} 
								}
					 	 	});  
						}
					});
			 	});
					
				}
			}
			
		});
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
	 	});
		
		//删除商品
		$('.del').click(function(){
			var detailId=$(this).parent().parent().find('input[name="detailId"]').val();
			var position=$(this);
			var cartid=$("input[name='cartId']").val();
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
	 	 			if($(".operation_one").length==1){
		 	 			$.ajax({
					 	 		type : "post",                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
					 	 		url : "../cart/clear.do",
					 	 		async : false,
					 	 		timeout : 15000,
					 	 		dataType:ms_proto,
					 	 		data:{cartID:cartid},
					 	 		cache:false,
					 	 		success : function(data) {
					 	 			window.location.reload();
					 	 		},
					 	 		error:function(data) {}
						})
	 	 			}else{
	 	 				$(".message_block2").html("删除购物车成功"+data.message);
		 	 			$(".message_body").fadeIn();
			 	 		$(".message_body").fadeOut(3000);
			 	 		position.parent().parent().parent().remove();
		 	 			checkproductcontent();
	 	 			}
	 	 			
				}
	 	 	});  
	 	});
		
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
 						$('span[name="materialname"]').css("color","red");
 						$('span[name="materialfee"]').css("color","red");
 					}else{
 						$('span[name="materialname"]').text("可配物料金额:");
 						$('span[name="materialfee"]').text(-materialprice);
 						$('span[name="materialname"]').css("color","green");
 						$('span[name="materialfee"]').css("color","green");
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
