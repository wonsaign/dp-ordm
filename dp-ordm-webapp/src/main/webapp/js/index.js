//订货页面加载物料价格信息
function initmumber(){
    var counterId =$('input[name="counterId"][type="hidden"]');
	var cartId =$('input[name="cartId"]');
	var sval = $('select[name="shopselect"] option:selected').val();
	if(sval==-1){
		return ;
	}
	$.ajax({
 		type : "post",
 		url : "../cart/initcartnum.do",
 		async : false,
 		timeout : 10000,
 		dataType:"json",
 		cache:false,
 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 		},
 		success : function(carts) {
 			if(carts==null||carts==""){
 				return ;
 			}
 				var cid = carts[0].counterId;
 				var cd = carts[0].cartId;
 				counterId.val(cid);
 				cartId.val(cd);
 				getpresentprice();
 			}
		});
}
//获取购物车面板信息
function getpresentprice(){
	var counterId = $('input[name="counterId"]').val();
	var cartId= $('input[name="cartId"]').val();
	if(counterId != '' && counterId != undefined && counterId != null && cartId !='' && cartId != undefined && cartId != null ){
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
					if(data[key][3]>0){
						$('span[name="fee"]').html("已选<b style='color:red' class='changefee'>" +data[key][3].toFixed(2)+ "</b>元产品");
					}
					if(parseInt(data["freeemoney"]/data["discout"])-parseInt(data[key][3])>0){
						$('span[name="feeadd"]').text("再购");
						$('span[name="totalfee"]').text(parseInt(data["freeemoney"]/data["discout"])-parseInt(data[key][3]));
						$('span[name="feename"]').text("元免邮费");
					}else{
						$('span[name="feeadd"]').text("已免邮费");
						$('span[name="totalfee"]').text("");
						$('span[name="feename"]').text("");
					}
					//校验submit_order按钮是否可用，条件是物料数量是否为0
				}
			}
		})
	}
	
}

//初始化 加减购物车方法
	function initadddel(){
		$(".min").unbind("click");
		$(".add").unbind("click");
		$('.min').bind("click",function(){
			var t=$(this).parent().find('input[class*=text_box]'); 
			var step=t.attr("step");
			if(t.attr("readonly")=="readonly"){
				return;
			}
			t.val(parseInt(t.val())-parseInt(step));
			if(parseInt(t.val())<0){ 
			t.val(0); 
			} 
		});
		$(".add").bind("click",function(){ 
			var t=$(this).parent().find('input[class*=text_box]'); 
			var step=t.attr("step");
			if(t.attr("readonly")=="readonly"){
				return;
			}
			t.val(parseInt(t.val())+parseInt(step))
		});
	};
	function initpage(){
		$(".addcart").unbind("submit");
		$(".addcart").bind('submit',function(event){
			event.preventDefault();
			//判断是否已选择购物车
			if($('select[name="shopselect"]').val()==-1){
				$(".message_body").css("display","block");
				var shop=$(".shop_hover").html();
				$(".message_block2").html("<div style='text-align:center; color:red;'>不能打开购物车,请先选择门店!</div>"+"<div>"+shop+"</div>"+"<div><button class='sure_shopping bg-red'>确认店铺</button></div>");
				//加入监听事件
				initcartwindowselect($('.message_block2'));
				return false;
			}
			
			var position = $(this);
			var dis=position.find(".add_button").attr("disabled");
			position.find(".add_button").attr("disabled",true);
			var goodname =position.find(".goods_name").text();
			var num=position.find('input[name="num"]').val();
			var productId=position.find('input[name="productId"]').val();
			var stockId =$("input[name='stockId']").val();
			var $lrs = new Array();
			$lrs.push({id:productId,subID:stockId});
			//处理购物车商品的库存信息
			$.ajax({
	 	 		type : "get",
	 	 		url : ms_host+"/microservice/stockservice.do",
	 	 		timeout : 15000,
	 	 		dataType:ms_proto,
	 	 		data:{items:JSON.stringify($lrs)},
	 	 		async : false,
	 	 		cache:false,
	 	 		error:function(error){
		 	 		$(".message_block2").html("<div>对不起，该产品数据错误。</div>");
		 	 		$(".message_body").fadeIn();
		 	 		$(".message_body").fadeOut(2000);
		 	 		position.find(".add_button").attr("disabled",false);
		 	 		
		 		},
	 	 		success : function(data) {
	 	 			console.log(data);
	 	 			$.each(data.data, function(id,item) {
 						if(item.t==1||position.find(".add_button").css("background-color") == "rgb(109, 187, 68)"){
 							$.ajax({
 					 	 		type : "post",
 					 	 		url : "../cart/add.do",
 					 	 		async : false,
 					 	 		timeout : 15000,
 					 	 		dataType:"text",
 					 	 		cache:false,
 					 	 	    data:{productId:productId,num:num},
 					 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 					 	 			$(".message_block2").html(errorThrown+"加入购物车失败!");
 						 	 		$(".message_body").css("display","block");
 					 	 			position.find(".add_button").attr("disabled",false);
 					 	 		},
 					 	 		success : function(data) {
 					 	 			var counter=$(".shop_hover select").find("option:selected").text();
 					 	 			$(".message_block2").html("<div style='text-align:center; color:red;'>您已成功向<span style='color:#0e5993;font-weight:bold'>"+counter+"</span>购物车添加<span style='color:#0e5993;font-weight:bold'>"+goodname+"</span>商品</div>");
 					 	 			$(".message_body").fadeIn();
 					 	 			$(".message_body").fadeOut(1000);
 					 	 			position.find(".add_button").attr("disabled",false);
 						 	 		initmumber();
 								}
 					 	 	}); 
 						}else{
 							if(item.v <= 0){
 	 			 	 			$(".message_block2").html("<div>该产品现在没有库存,不能添加到购物车。</div>");
 	 			 	 			$(".message_body").fadeIn();
 					 	 		$(".message_body").fadeOut(3000);
 	 			 	 			position.find(".add_button").attr("disabled",false);
 	 						}else if(num-item.v>0){
 	 							$(".message_block2").html("<div>库存不足,您只能添加"+item.v+"件。</div>");
 	 			 	 			$(".message_body").fadeIn();
 					 	 		$(".message_body").fadeOut(3000);
 	 			 	 			position.find(".add_button").attr("disabled",false);
 	 						}else{
 	 							$.ajax({
 	 					 	 		type : "post",
 	 					 	 		url : "../cart/add.do",
 	 					 	 		async : false,
 	 					 	 		timeout : 15000,
 	 					 	 		dataType:"text",
 	 					 	 		cache:false,
 	 					 	 	    data:{productId:productId,num:num},
 	 					 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 	 					 	 			$(".message_block2").html(errorThrown+"加入购物车失败!");
 	 						 	 		$(".message_body").css("display","block");
 	 					 	 			position.find(".add_button").attr("disabled",false);
 	 					 	 		},
 	 					 	 		success : function(data) {
 	 					 	 			var counter=$(".shop_hover select").find("option:selected").text();
 	 					 	 			
 	 					 	 			$(".message_block2").html("<div style='text-align:center; color:red;'>您已成功向<span style='color:#0e5993;font-weight:bold'>"+counter+"</span>购物车添加<span style='color:#0e5993;font-weight:bold'>"+goodname+"</span>商品</div>");
 	 					 	 			$(".message_body").fadeIn();
 	 					 	 			$(".message_body").fadeOut(1000);
 	 					 	 			position.find(".add_button").attr("disabled",false);
 	 						 	 		initmumber();
 	 								}
 	 					 	 	});
 	 						}
 						}
	 	 			})
	 	 		}
			})
		});
		$('input[name="productId"]').each(function(){
			var position = $(this);
			if(position.parent().parent().prev().find(".inventory").attr("title")<=0||position.parent().parent().prev().find(".inventory").attr("title")=="undefined"){
				position.parent().parent().prev().find(".inventory").show();
			};
			if(position.parent().parent().prev().find(".actFlag").val()=="true"){
					position.parent().find("input[name='num']").attr("readonly","readonly");
					position.parent().find(".add_button").attr("disabled",true);
					position.parent().find(".add_button").css("background","rgb(159, 159, 157)");
					position.parent().parent().prev().append("<img class='top_imgy' src='../img/top_imgy.png'>");
			}
		});
	 	$(".text_search").change(function(){
	 		$(".text_search").prev().val("");
	 	});
}
	function topTab(type){
		var stock_tbody=$(".message_block2 .stock_tbody");
		stock_tbody.empty();
		var stockId =$("input[name='stockId']").val();
		$(".stock_table thead").html("<tr><th>产品名称</th><th>规格</th><th>库存量</th></tr>");
		$.ajax({
			type : "post",
	 		url : "../microservice/getall.do",
	 		async : false,
	 		timeout : 15000,
	 		dataType:"json",
	 		cache:false,
	 		data:{stockId:stockId,type:type},
	 	    success :function(data){
	 	    	$.each(data,function(i,product){
	 	    		$(".message_top").text("库存(仓库:"+i+")");
	 	    		$.each(data[i],function(j,pro){
	 	    			stock="<tr href="+pro.pid+">"
						+"<td class='grotd'>"+pro.name+"<img src='../img/top_imgy.png' class='groimg' title="+pro.joinAct+"></td>"
						+"<td>"+pro.spec+"</td>"
						+"<td>"+pro.qty+"</td></tr>";
 	    				stock_tbody.append(stock);
	 	    		})
				});
	 	    	$(".stock_tbody tr").each(function(){
	 	    		if($(this).find(".groimg").attr("title") == "true"){
	 	    			$(this).find(".groimg").show();
	 	    		}
	 	    	});
	 	    	$(".stock_tbody tr").hover(function(){
	 	    		$(this).css("background","rgb(195, 231, 177)");
	 	    		$(this).siblings().css("background","white");
	 	    	});
	 	    	$(".stock_tbody tr").click(function(){
	 	    		var trhref=$(this).attr("href");
	 	    		location.href="../product/detail_"+trhref+".do";
	 	    	});
	 	    },
	 	    error: function(){
	 	    	
	 	    }
	 	    
		});
	}
	function preTab(type){
		var stock_tbody=$(".message_block2 .stock_tbody");
		stock_tbody.empty();
		$(".message_block2 .stock_table thead").empty();
		if(type=='1'){
			$.ajax({
				type : "post",
		 		url : "../order/showallpre.do",
		 		async : false,
		 		timeout : 15000,
		 		dataType:"json",
		 		cache:false,
		 	    success :function(data){
		 	    	console.log(data);
		 	    	$(".stock_table thead").html("<tr><th>名称</th><th>打欠时间</th><th>还欠时间</th></tr>");
		 	    	$.each(data,function(i,prodata){
		 	    		if(prodata.product==null){
		 	    			stock="<tr href="+prodata.activity.actId+">"
							+"<td class='grotd'>"+prodata.activity.name+"</td>"
							+"<td class='grotd'>"+prodata.start+"</td>"
							+"<td>"+prodata.end+"</td></a><tr>"
		    				stock_tbody.append(stock);
		 	    		}else if(prodata.activity==null){
		 	    			stock="<tr href="+prodata.product.productId+">"
							+"<td class='grotd'>"+prodata.product.name+"</td>"
							+"<td class='grotd'>"+prodata.start+"</td>"
							+"<td>"+prodata.end+"</td></a><tr>"
		    				stock_tbody.append(stock);
		 	    		}
		 	    		
		 	    	});
		 	    	$(".stock_tbody tr").hover(function(){
		 	    		$(this).css("background","rgb(195, 231, 177)");
		 	    		$(this).siblings().css("background","white");
		 	    	});
		 	    	$(".stock_tbody tr").click(function(){
		 	    		var trhref=$(this).attr("href");
		 	    		location.href="../product/detail_"+trhref+".do";
		 	    	});
		 	    },
		 	    error: function(){
		 	    	
		 	    }
		 	    
			});
		}else if(type=='2'){
			$.ajax({
				type : "post",
		 		url : "../order/preshow.do",
		 		async : false,
		 		timeout : 15000,
		 		dataType:"json",
		 		cache:false,
		 		data:{status:type},
		 	    success :function(data){
		 	    	$(".stock_table thead").html("<tr><th>名称</th><th>数量</th><th>订单</th><th>仓库</th><th>店铺</th><th>所属活动</th><th>取消</th></tr>");
		 	    	var stock ='';
		 	    	$.each(data,function(i,product){
		 	    			stock+="<tr>"
							+"<td>"+product.pname+"</td>"
							+"<td>"+product.qty+"</td>"
							+"<td>"+product.belongOrder+"</td>"
							+"<td>"+product.warehouse+"</td>"
							+"<td>"+product.belongCounter+"</td>"
							+"<td>"+product.belongActivity+"</td>"
							if(product.canclePrePro){
								stock+="<td><input type='hidden' name='orderDetailId' value='"+product.orderDetailId+"'><input type='button' value='取消' class='bg-"+product.canclePrePro+" canclePrePro'></td></tr>";
							}else{
								stock+="<td><input type='hidden' name='orderDetailId' value='"+product.orderDetailId+"'><input type='button' value='取消' class='bg-"+product.canclePrePro+" canclePrePro' disabled='"+!product.canclePrePro+"'></td></tr>";
							}	
					});
		 	    	stock_tbody.append(stock);
		 	    },
		 	    error: function(){
		 	    	
		 	    }
		 	    
			});
		}else if(type=='3'){
			$.ajax({
				type : "post",
		 		url : "../order/preshow.do",
		 		async : false,
		 		timeout : 15000,
		 		dataType:"json",
		 		cache:false,
		 		data:{status:type},
		 	    success :function(data){
		 	    	$(".stock_table thead").html("<tr><th>名称</th><th>数量</th><th>订单</th><th>仓库</th><th>店铺</th><th>所属活动</th></tr>");
		 	    	$.each(data,function(i,product){
		 	    			stock="<tr>"
							+"<td>"+product.pname+"</td>"
							+"<td>"+product.qty+"</td>"
							+"<td>"+product.belongOrder+"</td>"
							+"<td>"+product.warehouse+"</td>"
							+"<td>"+product.belongCounter+"</td>"
		 	    			+"<td>"+product.belongActivity+"</td></tr>"
	 	    				stock_tbody.append(stock);
					});
		 	    },
		 	    error: function(){
		 	    	
		 	    }
		 	    
			});
		}else if(type=='4'){
			$.ajax({
				type : "post",
		 		url : "../order/preshow.do",
		 		async : false,
		 		timeout : 15000,
		 		dataType:"json",
		 		cache:false,
		 		data:{status:type},
		 	    success :function(data){
		 	    	$(".stock_table thead").html("<tr><th>名称</th><th>数量</th><th>订单</th><th>仓库</th><th>店铺</th><th>所属活动</th><th>备注</th></tr>");
		 	    	$.each(data,function(i,product){
		 	    			stock="<tr>"
							+"<td>"+product.pname+"</td>"
							+"<td>"+product.qty+"</td>"
							+"<td>"+product.belongOrder+"</td>"
							+"<td>"+product.warehouse+"</td>"
							+"<td>"+product.belongCounter+"</td>"
							+"<td>"+product.belongActivity+"</td>"
							+"<td>"+product.belongActivity+"</td></tr>"
	 	    				stock_tbody.append(stock);
					});
		 	    },
		 	    error: function(){
		 	    	
		 	    }
		 	    
			});
		}
		
	}
//开始
$(document).ready(function(){
	$(".stock_img").click(function(){
		$(".message_body").show();
		$(".message_block2").empty();
		$(".message_block2").html($(".stock_div").html());
		var type="11386";
		topTab(type);
		$("#topTab li").click(function(){
			$(this).addClass("active");
			$(this).siblings().removeClass("active");
			/*var myid=$(this).find("a").attr("href");
			$(myid).addClass("active in");
			$(myid).siblings().removeClass("active in");*/
			type=$(this).find("a").attr("id");
			topTab(type);
		});
	});
	$("#newpre").click(function(){
		$(".message_body").show();
		$(".message_block2").empty();
		$(".message_block2").html($(".newpre_div").html());
		var type='1';
		preTab(type);
		$(".message_block2").on('click','#new_Tab li',function(){
			$(this).addClass("active");
			$(this).siblings().removeClass("active");
			type=$(this).find("a").attr("id");
			preTab(type);
		});
		
	})
	$(".message_body").on('click','.canclePrePro',function(){
		$(".message_body1").show();
		$(".message_blocktwo2").html("<div style='text-algin:center;'>您确认取消该产品？</div><div style='text-algin:center;'><input type='button' value='确认' class='bg-red surePre'>&nbsp;<input type='button' value='取消' class='bg-red canclePre'></div>")
		var orderDetailId = $(this).parent().find("input[name='orderDetailId']").val();
		$(".message_blocktwo2").on('click','.surePre',function(){
			$.ajax({
				type : "post",
		 		url : "../order/canclePreSale.do",
		 		async : false,
		 		timeout : 15000,
		 		dataType:"json",
		 		cache:false,
		 		data:{orderDetailId:orderDetailId},
		 	    success :function(data){
		 	    	$(".message_body1").hide();
					$(".message_blocktwo2").empty();
					$(".message_block2").empty();
					$(".message_block2").html($(".newpre_div").html());
					$("#new_Tab li").removeClass("active");
					$("#new_Tab li").eq(1).addClass("active");
					preTab(1);
		 	    },
		 	    error: function(){
		 	    	
		 	    }
		 	    
			});
		})
		$(".message_blocktwo2").on('click','.canclePre',function(){
			$(".message_body1").hide();
			$(".message_blocktwo2").empty();
			
		})
		
		
	})
	var wh=$(window).height();
	$(".center").css("min-height",wh*0.6);
	initpage();
	initmumber();
	$(".order_detial").each(function(){
		$(this).children(".order_left").text($(this).index(".order_detial")+1);
	});	
	$(".order_type").each(function(){
		if($(this).text()=="正品"){
			$(this).parent().css("background","#fadadb");
		}
		if($(this).text()=="物料"){
			$(this).parent().css("background","#dffede");
		}
		if($(this).text()=="赠品"){
			$(this).parent().css("background","#ddddff");
		}
	});
	$(".series_many").click(function(){	
		var ss=$(this).prev().css("height");
		if(ss=="30px"){
			$(this).prev().css("height","auto");
			$(this).text("收起");
		}else{
			$(this).prev().css("height",30);
			$(this).text("更多");
		}
	});
	$(".class_block").each(function(){
		var sl=$(this).find(".series li").length;
		if(sl<6){
			$(this).find(".series_many").hide();
		}
		for(var i=0;i<5;i++){
			$(this).find(".series li").eq(i).css('font-weight','bold');
		}
	});
	$(".check_all").click(function(){
		if($(this).attr("checked")=="checked"){
			$(".check_all").attr("checked",true);
			$(".check_one").attr("checked",true);
		}else{
			$(".check_all").attr("checked",false);
			$(".check_one").attr("checked",false);
		}	
	})			
	$(".check_one").click(function(){
		var c=0;
		$(".check_one").each(function(){
			if($(this).attr("checked")=="checked"){
				c=c+1;
			}
		})
		if($(".check_one").length==c){
			$(".check_all").attr("checked",true);
			$(".check_one").attr("checked",true);
		}else{
			$(".check_all").attr("checked",false);
		}
	})
	var str=window.location.href;
	$(".nav th").each(function(){
		var sear=new RegExp($(this).find("a").attr("href"));
		var i=$(this).index();
		if(sear.test(str)){
			$(this).css("background","#c3e7b1");
		}
	});
	$(".big_class").click(function(){		
		var ss=$(".bigtype").css("display");
		if(ss=="block"){
			$(".bigtype").hide(500);
		}else{
			$(".bigtype").show(500);
		}
	});
	$(".type_class").click(function(){		
		var ss=$(".classtype").css("display");
		if(ss=="block"){
			$(".classtype").hide(500);
		}else{
			$(".classtype").show(500);
		}
	});
	$(".activity_class").click(function(){		
		var ss=$(".activity").css("display");
		if(ss=="block"){
			$(".activity").hide(500);
		}else{
			$(".activity").show(500);
		}
	});
	$(".sort span").click(function(){
		$(this).css("background","#f03e3e");
		$(this).siblings(".sort span").css("background","white");
	});
	if($(".item").length==0){
		$(".sort").hide();
	}else{
		$(".sort").show();
	};
	$(".top_back").click(function(){
		var sc=$(window).scrollTop();
		$('body,html').animate({scrollTop:0},50);
	});
	$(window).scroll(function(){
		var h=$(window).scrollTop();
		var ww=$(window).width();
		if(ww > 600){
			if(h>65){
				$('.top').css({'position':'fixed','top':'0px'});
				$('.operationpanel1').css({'position':'fixed','top':'53px','right':'40px'});
				$(".top_hide").show();
			}else{
				$('.top').css({'position':'relative'});
				$('.operationpanel1').css({'position':'static'});
				$(".top_hide").hide();
			}
		}else{
			if(h>60){
				$('.top').css({'position':'fixed','top':'0px'});
				$('.operationpanel1').css({'position':'fixed','top':'28px','right':'20px','font-size':'12px'});
				$(".top_hide").show();
			}else{
				$('.top').css({'position':'relative'});
				$('.operationpanel1').css({'position':'static'});
				$(".top_hide").hide();
			}
		}
	})
	$(".tactics_item").each(function(){
		if($(this).find(".item_title a").text()=="新品推出"){
			$(this).find(".item_number").append("<img class='top_imgx' src='../img/top_imgx.png'>");
		}else if($(this).find(".item_title").text()=="优惠活动"){
			$(this).find(".item_number").append("<img class='top_imgy' src='../img/top_imgy.png'>");
		}else if($(this).find(".item_title a").text()=="应季畅销"){
			$(this).find(".item_number").eq(0).append("<img class='top_img1' src='../img/top_img1.png'>");
			$(this).find(".item_number").eq(1).append("<img class='top_img2' src='../img/top_img2.png'>");
			$(this).find(".item_number").eq(2).append("<img class='top_img3' src='../img/top_img3.png'>");
		}else if($(this).find(".item_title").text()=="预定会"){
			$(this).find(".item_number").append("<img class='top_imgnew' src='../img/new.png'>");
		}else if($(this).find(".item_title a").text()=="打欠商品"){
			$(this).find(".item_number").append("<img class='top_imgowes' src='../img/owes.png'>");
		}else{
			$(this).find(".item_number").eq(0).append("<img class='top_img1' src='../img/top_img1.png'>");
			$(this).find(".item_number").eq(1).append("<img class='top_img2' src='../img/top_img2.png'>");
			$(this).find(".item_number").eq(2).append("<img class='top_img3' src='../img/top_img3.png'>");
		}
	});
	$("#message").click(function(){
		$(".message_body").show();
		$(".message_block2").html($(".message_hide").html())
	});
	$(".message_close").click(function(){
		$(".message_block2").empty();
		$(".message_body").hide();
	});
	//切换门店
	$('select[name="shopselect"]').click(function(){
		var position=$(this);
		$.ajax({
 	 		type : "post",
 	 		url : "../cart/getcartstatus.do",
 	 		async : false,
 	 		timeout : 15000,
 	 		dataType:"json",
 	 		cache:false,
 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 	 			alert(errorThrown+"失败!");
 	 		},
 	 		success : function(data) {
 	 			$.each(data,function(i,item){
 	 				position.find("option").each(function(){
 	 					if($(this).attr('data-id')==item.counterId && item.status!=666){
 	 						$(this).attr("disabled",true);
 	 						$(this).css("color","red");
 	 						$(this).attr("title",item.desc);
 	 					}
 	 					if($(this).attr('data-id')==item.counterId && item.status==666){
 	 						$(this).attr("disabled",false);
 	 						$(this).css("color","green");
 	 						$(this).attr("title",item.desc);
 	 					}
 	 					
 	 				});
 	 			});
			}
 	 	});
	});
	$('select[name="shopselect"]').change(function(){
		var imagepath = $('body').find('input[name="imagepath"]').val();
		var counterCode= $(this).val();
		$(this).find("option[value='-1']").remove();
		initcartbutton($('body'));
		$('.localtag').find(".item").remove();
		$.ajax({
 	 		type : "post",
 	 		url : "../series.do",
 	 		async : false,
 	 		timeout : 15000,
 	 		dataType:"json",
 	 		cache:false,
 	 	    data:{counterCode:counterCode},
 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 	 			alert(errorThrown+"获取系列失败!");
 	 		},
 	 		success : function(data) {
 	 			$.each(data,function(i,item){
 	 				if(i<3){
 	 					var itemhtml =	"<div class='item'><div class='item_number'>"
 	 						+"<img class='top_img"+parseInt(i+1)+"' src='../img/top_img"+parseInt(i+1)+".png'>"
 	 	 	 				+"<a href='../product/series_"+item.hardCode +".do'><img class='item_img' src='"+imagepath+item.url +"'></a>" 
 	 	 	 				+"<span class='goods_name goods_namecenter'>"+item.name +"</span>"
 	 	 	 				+"</div></div>";
 	 	 	 				
 	 				}else if(5>i>=3){
 	 					var itemhtml =	"<div class='item'><div class='item_number'>"
 	 	 	 				+"<a href='../product/series_"+item.hardCode +".do'><img class='item_img' src='"+imagepath+item.url +"'></a>" 
 	 	 	 				+"<span class='goods_name goods_namecenter'>"+item.name +"</span>"
 	 	 	 				+"</div></div>";
 	 				}
 	 				$('.localtag').append(itemhtml);
 	 			});
			}	
 	 	});
		$.ajax({
 	 		type : "post",
 	 		url : "../cart/getislockbyother.do",
 	 		async : false,
 	 		timeout : 15000,
 	 		dataType:"json",
 	 		cache:false,
 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 	 			alert(errorThrown+"失败!");
 	 		},
 	 		success : function(data) {
			}
 	 	});
		var href=window.location.href;
    	window.location.reload();
	});
	//初始化购物车按钮 未选择不允许点击购物车
	initcartbutton($(this));
	$('.shopping_block a').click(function(){
		var position = $(this);
		if($(this).attr("href")=="#"){
			$(".message_body").css("display","block");
				var shop=$(".shop_hover").html();
				$(".message_block2").html("<div style='text-align:center; color:red;'>不能打开购物车,请先选择门店!</div>"+"<div>"+shop+"</div>"+"<div><button class='sure_shopping bg-red'>确认店铺</button></div>");
				//加入监听事件
				initcartwindowselect($('.message_block2'));
		}else{
			$.ajax({
	 	 		type : "post",
	 	 		url : "../cart/getislockbyother.do",
	 	 		async : false,
	 	 		timeout : 15000,
	 	 		dataType:"json",
	 	 		cache:false,
	 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
	 	 		},
	 	 		success : function(data) {
	 	 			if(data.status==444){
	 	 				position.attr("href","#");
	 	 				$(".message_body").show();
	 					$(".message_block2").html(data.desc);
	 	 			}
	 	 			if(data.status==2){
 	 					position.attr("href","#");
 	 					$(".message_body").show();
	 					$(".message_block2").html(data.desc);
	 	 			}
				}
	 	 	});
		}
	});
	$(".message_body").scroll(function() {
		var wh=parseInt($(".message_block").css("margin-top"));
		var ww=parseInt($(".message_block").css("margin-right"));
		var mw=parseInt($(".message_block").width());
		var scr=$(".message_body").scrollTop();
		if(scr > wh){
			$(".message_top").width(mw);
			$(".message_top").css({"position":"fixed","top":"0px"});
			$(".message_close").css({"position": "fixed","top":"5px", "left": ww+mw-25});
			$(".tab-head").show();
			$("#topTab").css({"position":"fixed","top":"0px"});
		}else{
			$(".message_top").css({"position":"static"});
			$(".message_close").css({"position":"absolute","top":"5px", "left": mw-25});
			$(".tab-head").hide();
			$("#topTab").css({"position":"static"});
		}
	})
})

$(".img_block").hover(function(){
	$(this).parent().find(".left_right").animate({left:'-90px'});
},function(){
	$(this).parent().find(".left_right").animate({left:'0px'});
})

//初始化cart按钮函数
function initcartbutton(position){
	if( position.find('select[name="shopselect"] option:first').attr("data-id")== -1 ){
		position.find('.shopping_block a').attr("href","#");
	}else{
		position.find('.shopping_block a').attr("href","../cart/cart.do");
	}

}

//选择购物车弹窗处理函数
function initcartwindowselect(position){
	position.find(".sure_shopping").click(function(){
		$(".message_body").hide();
		window.location.reload();
	});
	position.find('select[name="shopselect"]').click(function(){
		var position=$(this);
		$.ajax({
 	 		type : "post",
 	 		url : "../cart/getcartstatus.do",
 	 		async : false,
 	 		timeout : 15000,
 	 		dataType:"json",
 	 		cache:false,
 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 	 			alert(errorThrown+"失败!");
 	 		},
 	 		success : function(data) {
 	 			$.each(data,function(i,item){
 	 				position.find("option").each(function(){
 	 					if($(this).attr('data-id')==item.counterId && item.status!=666){
 	 						$(this).attr("disabled",true);
 	 						$(this).css("color","red");
 	 						$(this).attr("title",item.desc);
 	 					}
 	 					if($(this).attr('data-id')==item.counterId && item.status==666){
 	 						$(this).attr("disabled",false);
 	 						$(this).css("color","green");
 	 						$(this).attr("title",item.desc);
 	 					}
 	 				});
 	 			});
			}
 	 	});
	});
	position.find('select[name="shopselect"]').change(function(){
		var counterCode= $(this).val();
		$(this).find("option[value='-1']").remove();
		$('.shop_hover').find("option[value='-1']").remove();
		$('.shop_hover').find('select[name="shopselect"]').val(counterCode);
		initcartbutton($('body'));
		$.ajax({
 	 		type : "post",
 	 		url : "../series.do",
 	 		async : false,
 	 		timeout : 15000,
 	 		dataType:"json",
 	 		cache:false,
 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 	 			alert(errorThrown+"失败!");
 	 		},
 	 	    data:{counterCode:counterCode},
 	 		success : function(data) {
			}
 	 	});
		$.ajax({
 	 		type : "post",
 	 		url : "../cart/getislockbyother.do",
 	 		async : false,
 	 		timeout : 15000,
 	 		dataType:"json",
 	 		cache:false,
 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 	 			alert(errorThrown+"失败!");
 	 		},
 	 		success : function(data) {
			}
 	 	});
	});
	
}

/*动态转换显示时间工具*/
function showTime(standarddate){ 
  var time=standarddate; 
  var year=time.getFullYear(); 
  var month=time.getMonth();
  var date=time.getDate();
  var hour=time.getHours();
  var minutes=time.getMinutes(); 
  var second=time.getSeconds();
  month=month+1; 
  month<10?month="0"+month:month;
  hour<10?hour='0'+hour:hour; 
  minutes<10?minutes='0'+minutes:minutes; 
  second<10?second='0'+second:second; 
  var now_time=year+'-'+month+'-'+date+' '+hour+':'+minutes+':'+second; 
  return now_time;
}
