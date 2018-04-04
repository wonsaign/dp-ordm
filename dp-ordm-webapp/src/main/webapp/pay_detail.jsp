<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.zeusas.core.utils.*"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<link href="../css/index.css?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"css/index.css").lastModified()%>" rel="stylesheet" />
<link href="../css/style.css?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"css/style.css").lastModified()%>" rel="stylesheet">
<link href="../css/ssi-uploader.css" rel="stylesheet">
<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
<meta name="viewport"
	content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<style>
	.pay_submit{
		display: none;
	}
</style>
</head>
<body>
	<%@ include file="../common/commonHT.jsp"%>
	<div class="position_body">
		<img class="bigimg" src="../img/240_R6EVBIG35920151010161300.jpg">
		<img class="bigimg_close" src="../img/close.png">
	</div>
	<div class="center">
	  <h3 style="color:#6dbb44; text-align: center;">合并支付</h3>
		<input type="button" class="bg-red submit_order all_check" value="全选">
		<input type="button" class="bg-red submit_order others_check" value="反选">
		<input type="button" class="bg-red combine" value="合并支付">
		<input type="button" class="bg-red split" value="拆除合并">
		<table class="pay_detail" cellpadding="0" cellspacing='0'>
			<thead>
				<tr>
					<td>订单号</td>
					<td>柜台号</td>
					<td>商品金额</td>
					<td>折后金额:</td>
					<td>物料费用:</td>
					<td>运费:</td>
					<td>应付金额:</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${orders}" var="ods">
				<tr class="paybox">
					<td><label><input type="checkbox" name="paybox" value="${ods.id}">${ods.orderNo}</label></td>
					<td>${ods.counterName}</td>
					<td>${ods.orderOriginalFee}</td>
					<td>${ods.productFee}</td>
					<td>${ods.materialFee}</td>
					<td>${ods.expressFee}</td>
					<td>${ods.paymentFee}</td>
					<td class="orderDetail"><a href="../order/detail.do?orderId=${ods.id}">查看明细</a></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class="settle">
			<hr />
			<div class="order">
			<label><strong>请选择额外礼包配送的订单：</strong></label>
			<tbody>
			<c:forEach items="${orders}" var="ods1">
				<tr>
					<div class="ordernos"> <input type="radio" name="order" class="orderno" value="${ods1.id}">${ods1.orderNo}</div>
				</tr>
			</c:forEach>
			
			</tbody>
			</div>
			<div class="other" style="color:red;"></div>
			<span class="information_settle">商品金额:￥<strong class="productAmt">${data[0]}</strong></span>
			<shiro:hasAnyRoles name="root,adm,14,12,11">
				<span class="information_settle">折后金额:￥<strong class="discountAmt">${data[1]}</strong></span>
				<br />
			</shiro:hasAnyRoles>
			<span class="information_settle red">额外物料费用:<strong class="extraMAmt">${data[1]}</strong></span><br />
			<span class="information_settle">运费:￥<strong class="extraFree"></strong>${data[3]}</span><br />
			<c:if test="${MyOrder.singleOrder.orderStatus==7 }">
				<span class="information_settle">扣款金额:</span>
			</c:if>
			<div class="clear"></div>
			<shiro:hasAnyRoles name="root,adm,14,12,11">
				<hr />
				<%-- <c:if test="${MyOrder.singleOrder.orderStatus!=2}">
					<span class="information_settle red">
						实付款:${MyOrder.singleOrder.paymentPrice} </span>
					<span class="information_settle red">使用余额:<span
						class="priceFormat">${MyOrder.singleOrder.useBalance}</span></span>
					<span class="information_settle red">应付金额:<strong
						id="paymentFee">${MyOrder.singleOrder.paymentFee}</strong></span>
				</c:if>--%>
				<span class="information_settle red">应付金额:<strong
					id="paymentFee">${data[1]}</strong></span>
				<span class="information_settle red">
					<input 
						<c:if test="${(empty data[4]||data[4]<=0)&&(empty data[5] ||data[5]==0)}">disabled</c:if>
						type="checkbox" id="balance"> <label for="balance" style="cursor: pointer";>使用余额:<span
						class="priceFor">0</span></label>
				</span>
				<span class="information_settle red balance_surplus"><label for="balance">剩余余额:</label><span
					class="priceFormat">${data[4]}</span></span>
			</shiro:hasAnyRoles>
			<div class="clear"></div>
			<hr />
		</div>
		<div class="accessory_body">
					<div class="accessory_block">
						<input name="id" type="hidden" value="" />
						<div>
							<span>实际支付金额:<b class="paymentPrice"></b></span>
						</div>
						<div>
							<span>备注:</span><b class="description"></b>
						</div>
						<input type="button" class="bg-red sure_all" value="确认提交">
						<input type="button" class="bg-red cancel_all" value="取消提交">
					</div>
			</div>
			<c:if test="${counter.type!='直营'}">
				<div class="pay_data">
					<sf:form action="../order/upload2.do" method="post"
						enctype="multipart/form-data">
						<input name="id" type="hidden" value="${order.id}" />
						<input name="orderNo" type="hidden" value="${order.orderNo}" />
						<div>
							<div style="margin: 10px 0;">
								<label for="paymentPrice"
									style="cursor: pointer; display: inline-block; width: 100px; float: left;">支付金额<b
									style="color: red">*</b></label> <input class="form-control"
									style="width: 100px; font-size: 18px; float: left;"
									id="paymentPrice" name="paymentPrice" min="0" step="0.01"
									type="number" required="required" value="${order.paymentPrice}">
								<div class="clear"></div>
							</div>
							<div style="margin: 10px 0;">
								<label for="description"
									style="width: 100px; display: inline-block; cursor: pointer; line-height: 75px; float: left; margin: 0px">备注</label>
								<textarea class="form-control"
									name="description" id="description"
									style="width: 80%; height: 75px; line-height: 25px; float: left;">无</textarea>
								<div class="clear"></div>
							</div>
						</div>
						<input id='ssi-uploadBtn' class='submit_order big bg-red'
							value='提交附件' type="submit">
					</sf:form>
					<div>
						<input type="file" multiple id="ssi-upload3" />
					</div>
				</div>
				<div class="clear"></div>
				<div class="pay_button">
					<c:if
						test="${message!='success' ||order.imageURL==null||''==order.imageURL }">
						<input type="button" style="font-size: 14px;"
							class="submit_button submit_order big bg-red" value="支付方式">
							
					</c:if>
				</div>
				<div class="pay_submit">
						<input type="button" class="submit_form submit_order big bg-red"
							value="提交">
						<div class="form_submit">
							<!-- <form action="../order/confirmCombineDirect.do" method="post"> -->
								<input type="hidden" name="">
								<div style="margin: 10px 0;">
									<input type="hidden" value="${order.id }" name="id"> 
									<input type="hidden" value="${order.orderNo }" name="orderNo">
									<label for="description"
										style="width: 40px; display: inline-block; cursor: pointer; line-height: 36px; position: relative; top: -15px;">备注:</label>
									<textarea class="form-control" value="" name="description"
										id="description">${order.description }</textarea>
								</div>
								<input type="submit" style="font-size: 14px;"
									class="submit_order big bg-red" value="提  交">
							<!-- </form> -->
						</div>
				</div>
			</c:if>
			<c:if
				test="${counter.type=='直营'}">
				<input type="button" class="submit_form submit_order big bg-red"
					value="提交">
				<div class="form_submit">
					<form action="../order/confirmdirect.do" method="post">
						<input type="hidden" name="">
						<div style="margin: 10px 0;">
							<input type="hidden" value="${order.id }" name="id"> <input
								type="hidden" value="${order.orderNo }" name="orderNo">
							<label for="description"
								style="width: 40px; display: inline-block; cursor: pointer; line-height: 36px; position: relative; top: -15px;">备注:</label>
							<textarea class="form-control" value="" name="description"
								id="description">${order.description }</textarea>
						</div>
						<input type="submit" style="font-size: 14px;"
							class="submit_order big bg-red" value="提  交">
					</form>
				</div>
			</c:if>
			<div class="clear"></div>
			<div class="pay_style">
				<div class="pay_style">
					<div class="pay_block">
						<div>
							<h3 style="margin: 0; padding: 10px 0; color: red;">
								<strong>支付方式</strong>
							</h3>
						</div>
						<div>
						 <h4 style="margin: 0;">线上支付</h4>
						</div>
						<div>
							<input style="padding: 0; margin: 0" id="onlinepay" type="radio"
								name="pay" value="aliPay"> <label for="onlinepay"
								style="font-size: 10px; cursor: pointer"> 支付宝 
						</div>
						<div>
							<h4 style="margin: 0;">线下支付</h4>
						</div> 
						<div>
							<input style="margin: 0" id="offlinepay" type="radio" name="pay"
								value="accessory" checked><label for="offlinepay"
								style="font-size: 10px; cursor: pointer">上传凭证</label>
						</div>
						<input type="button" value="支付" id="pay_sure"
							class="submit_order big bg-red"> <input type="button"
							value="取消支付" id="pay_close" class="submit_order big bg-red">
						<div class="pay_close">
							<img src="../img/close.png">
						</div>
					</div>
				</div>
			</div>
			<input type="hidden" name="ocid" value="${ocid}"> 
	</div>
	<%@ include file="common/commonF.jsp"%>
		<script src="../js/action.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/action.js").lastModified() %>"></script>
		<script src="../js/jquery.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/jquery.js").lastModified() %>"></script>
		<script src="../js/index.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/index.js").lastModified()%>"></script>
	<script src="../js/ssi-uploader.js"></script>
	<script>
		$(document).ready(function() {
			iToFixed();
			function iToFixed(){
				var num;
				var discountAmt = $(".discountAmt").text();
				var paymentFee = $("#paymentFee").text();
				var extraMAmt = $(".extraMAmt").text();
				if(discountAmt!=undefined||discountAmt!=null||discountAmt!=''){
					num = parseFloat(discountAmt).toFixed(2);
					$(".discountAmt").text(num);
				}
				if(extraMAmt!=undefined||extraMAmt!=null||extraMAmt!=''){
					num = parseFloat(extraMAmt).toFixed(2);
					$(".extraMAmt").text(num);
				}
				if(paymentFee!=undefined||paymentFee!=null||paymentFee!=''){
					num = parseFloat(paymentFee).toFixed(2);
					$("#paymentFee").text(num);
				}
			}
			$(".all_check").click(function(){
				$(".paybox").each(function(){
					$(this).find("input[name='paybox']").attr('checked',true);
				})
			})
			$(".others_check").click(function(){
				$(".paybox").each(function(){
					if(!$(this).find("input[name='paybox']").attr('checked')){
						$(this).find("input[name='paybox']").attr('checked',true);
					}else{
						$(this).find("input[name='paybox']").attr('checked',false);
					}
				})
			})
			$(".message_block").on("click",".submit_order ",function(){
				var os=new Array();
				$(".pay_detail tr").each(function(){
					if($(this).find("input[type='checkbox']").attr('checked')){
						os.push({id:$(this).find('input[name="paybox"]').val()});
					}
				});
				var orderId = $("input[name='order']:checked").val();
				$.ajax({
					type : "post",
		 	 		url : "../order/confirmCombineDirect.do",
		 	 		async : false,
		 	 		timeout : 15000,
		 	 		dataType:"json",
		 	 		cache:false,
		 	 	 	data:{
		 	 	 		os:JSON.stringify(os),
		 	 	 		description:$(".message_block textarea[name='description']").val(),
		 	 	 		ds:orderId
		 	 	 	},
		 	 		success : function(data) {
		 	 			location.href = "./index.do";
		 	 		},
		 	 		error:function(){
		 	 		}
				}); 
			});
			var href=location.href;
			if(href.indexOf("getCombineOrder")>0){
				$("input[name='paybox']").attr('checked',true);
				$("input[name='paybox']").attr('disabled',true);
				$(".combine").hide();
				$(".split").show();
				$(".settle").show();
				var $lrs = new Array();
				$(".pay_detail tr").each(function(){
					if($(this).find("input[type='checkbox']").attr('checked')){
						$lrs.push({id:$(this).find('input[name="paybox"]').val()});
					}
				});
				$("input[name='order']:first").attr('checked', 'checked');
				$.ajax({                                                                  
 	 				type : "post",
 	 				url : "../order/getOtherMessage.do",
		 	 		async : false,
		 	 		timeout : 15000,
		 	 		dataType:"json",
		 	 		cache:false,
		 	 	    data:{os:JSON.stringify($lrs)},
		 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
		 	 			$(".message_block2").html(errorThrown+"提交失败!");
		 	 			$(".message_body").fadeIn();
			 	 		$(".message_body").fadeOut(3000);
		 	 		},
		 	 		success : function(data) {
		 	 			if(data.status==0){
		 	 				if(data.message!='' && data.message!=null && data.message!= 'undefined'){
		 	 					$(".other").html(data.message);
		 	 					if(data.message == "额外礼包无库存"){$(".order").hide();}
		 	 				}else{
		 	 					$(".order").hide();
		 	 				}
		 	 			}else{
		 	 				$(".message_block2").html(data.message);
			 	 			$(".message_body").fadeIn();
				 	 		$(".message_body").fadeOut(5000);
		 	 			} 
					}
		 	 	});
			}else{
				$(".pay_button").hide();
				$(".settle").hide();
			}
			$(".combine").click(function(){
				var $lrs = new Array();
				var choseOrders = new Array();
				$(".pay_detail tr").each(function(){
					if($(this).find("input[type='checkbox']").attr('checked')){
						$lrs.push({id:$(this).find('input[name="paybox"]').val()});
						choseOrders.push($(this).find('input[name="paybox"]').val());
					}
				});
				
				$.ajax({
						type : "post",
			 	 		url : "../order/combineCount.do",
			 	 		async : false,
			 	 		timeout : 15000,
			 	 		dataType:"json",
			 	 		cache:false,
			 	 	  data:{os:JSON.stringify($lrs)},
			 	 		success : function(data) {
			 	 			$(".settle").show();
			 	 			// 禁用未合并的订单选项
			 	 			$(".orderno").each(function(){
			 	 				var temp = $(this).val();
			 	 				if ($.inArray(temp, choseOrders) == -1) {
			 	 					$(this).attr("disabled","disabled");
			 	 				}
							});
			 	 			$(".pay_button").show();
			 	 			$(".productAmt").text(data.data[0].toFixed(2));
			 	 			$(".discountAmt").text(data.data[1].toFixed(2));
			 	 			$(".extraMAmt").text(data.data[2].toFixed(2));
			 	 			$(".extraFree").text(data.data[3].toFixed(2));	 		
			 	 			$("#paymentFee").text(data.data[1].toFixed(2));
			 	 			$(".priceFormat").text(data.data[4].toFixed(2));
			 	 			$("input[name='ocid']").val(data.data[5]);
			 	 			$.ajax({                                                                  
			 	 				type : "post",
			 	 				url : "../order/getOtherMessage.do",
					 	 		async : false,
					 	 		timeout : 15000,
					 	 		dataType:"json",
					 	 		cache:false,
					 	 	    data:{os:JSON.stringify($lrs)},
					 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
					 	 			$(".message_block2").html(errorThrown+"提交失败!");
					 	 			$(".message_body").fadeIn();
						 	 		$(".message_body").fadeOut(3000);
					 	 		},
					 	 		success : function(data) {
					 	 			if(data.status==0){
					 	 				if(data.message!='' && data.message!=null && data.message!= 'undefined'){
					 	 					$(".other").html(data.message);
					 	 					$(".orderno").each(function() {
					 	 						if($(this).attr("disabled")=="disabled"){
					 	 							$(this).parent().remove();
					 	 						}
					 	 					})
					 	 					if(data.message == "额外礼包无库存"){$(".order").hide();}
					 	 					/* var checkbox = $('input[name="paybox"]:checked');
					 	 					for(ch in checkbox){
					 	 						var p = ch.value();
					 	 						
					 	 					}  */
					 	 					//$('input[name="paybox"]:unchecked').hide();
					 	 					/* $(".message_block2").html(data.message);
					 	 					$(".message_body").fadeIn();
								 	 		$(".message_body").fadeOut(5000); */
					 	 				}else{
					 	 					$(".order").hide();
					 	 				}
					 	 			}else{
					 	 				$(".message_block2").html(data.message);
						 	 			$(".message_body").fadeIn();
							 	 		$(".message_body").fadeOut(5000);
					 	 			} 
								}
					 	 	}); 
			 	 		},
			 	 		error:function(){
			 	 		}
					});
				    $("input[name='order']:first").attr('checked', 'checked');
					$("input[type='checkbox']").attr("disabled","disabled");
					$(".split").show();
					$(this).hide();
					$(".pay_button").show();
			});
			$(".split").click(function(){
				var $lrs = new Array();
				$(".pay_detail tr").each(function(){
					if($(this).find("input[type='checkbox']").attr('checked')){
						$lrs.push({id:$(this).find('input[name="paybox"]').val()});
					}
				});
				$.ajax({
					type : "post",
		 	 		url : "../order/dismantleCombine.do",
		 	 		async : false,
		 	 		timeout : 15000,
		 	 		dataType:"json",
		 	 		cache:false,
		 	 	  data:{os:JSON.stringify($lrs)},
		 	 		success : function() {
		 	 			location.href="../order/combinePage.do"
		 	 		},
		 	 		error:function(){
		 	 		}
				});
				/* $(".combine").show();
				$("input[type='checkbox']").attr("disabled",false);
				$("input[type='checkbox']").attr("checked",false);
				$(this).hide();
				$(".settle").hide();
				$(".pay_button").hide(); */
			})
			$(".submit_button").click(function() {
				$(".pay_style").css('display', 'block')
			})
			$("#pay_close").click(function() {
				$(".pay_style").hide();
			});
			$('#pay_sure').click(function() {
				var $lrs = new Array();
				$(".pay_detail tr").each(function(){
					if($(this).find("input[type='checkbox']").attr('checked')){
						$lrs.push({id:$(this).find('input[name="paybox"]').val()});
					}
				});
				if ($('#offlinepay').attr('checked')) {
							$(".pay_data").show();
							$(".pay_style").hide();
							$(".pay_button").hide();
						}
				if ($('#onlinepay').attr('checked')) {
					var ocid=$("input[name='ocid']").val();
					var orderId = $("input[name='order']:checked").val();
					$.ajax({
						type : "get",
						url : "../order/combineUOUN.do",
						async : true,
						timeout : 15000,
						dataType : "text",
						data : {
							ocid:ocid
						},
						cache : false,
						error : function(
								XMLHttpRequest,
								textStatus,
								errorThrown) {
						},
						success : function(
								data) {
							if (data == "success") {
								window.location.href = "../alipay/combineindex.do?ocid="
										+ ocid + "&ds=" + orderId;
								$(
										".pay_button")
										.hide();
							}
						}
					});
				}
						$('#ssi-upload3').ssi_uploader({
							url : '../order/uploadCombineImage.do',
							data : {os:JSON.stringify($lrs)},
							onUpload : function() {
							$.post("../order/uploadCombineOrders.do",
							{
								"os" : JSON.stringify($lrs),
								"actualPay" : $('input[name="paymentPrice"]').val(),
								"description" : $('textarea[name="description"]').val()
							},
							function() {
							});
							}, 
							dropZone : false,
							allowed : [ 'jpg', 'gif',
									'txt', 'png', 'pdf' ]
						});
			});
			$(".message_block").on("click",".sure_all",function(){
				var $lrs = new Array();
				$(".pay_detail tr").each(function(){
					if($(this).find("input[type='checkbox']").attr('checked')){
						$lrs.push({id:$(this).find('input[name="paybox"]').val()});
					}
				});
				
				var orderId = $("input[name='order']:checked").val();
				$.ajax({
					type : "post",
		 	 		url : "../order/confirmCombine.do", 
		 	 		async : false,
		 	 		timeout : 15000,
		 	 		dataType:"json",
		 	 		cache:false,
		 	 	  data:{os:JSON.stringify($lrs),ds:orderId},
		 	 		success : function(data) {
		 	 			location.href = "../order/index.do";
		 	 		},
		 	 		error:function(){
		 	 		}
				});
			});
			$(".submit_form").click(
			function() {
				$(".message_body").show();
				$(".message_block2").html(
						$(".form_submit").html());
			})
			$("#balance").click(function() {
				var ocid=$("input[name='ocid']").val();
				var flag;
				if ($("#balance").is(':checked')) {
					//使用余额
					flag=true;
				} else {
					//释放余额
					flag=false;
				}
				$.ajax({
					type:"post",
					url:"../order/useCombineBalance.do",
					async:false,
					timeout:15000,
					dataType:"json",
					cache:false,
					data:{ocid:ocid,flag:flag},
					success: function(data){
						$(".priceFormat").text(data.extra.usefulBalance);
						$(".priceFor").text(data.extra.useBalance);
						$("#paymentFee").text(data.extra.payable);
						if(data.extra.payable==0){
							$(".pay_submit").show();
							$(".pay_button").hide();
						}
					},
					error:function(){
						
					}
				})
			});
}) 
		</script>
	
</body>
</html>
