$(document).ready(function(){	
	window.onpopstate = function(e){
		//alert(history.state.url);
		if(history.state)
		{	
			var state = history.state;
			if(state.url.indexOf('order/cart_manage')!==-1){
				$(".intent_div").empty();
				$.ajax({
		 	 		type : "post",
		 	 		url : "../cart/cart_manage.do",
		 	 		async : true,
		 	 		timeout : 15000,
		 	 		dataType:"json",
		 	 		cache:false,
		 	 	    data:{typeId:state.typeId},
		 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
		 	 			$(".message_block2").html(errorThrown+"查询失败");
	 		 	 		$(".message_body").css("display","block");
		 	 		},
		 	 		success : function(data) {
		 	 			$(".intent_div").empty();
		 	 			$(".intent_div_top").empty();
		 	 			$(".intent_div_top").append("<ul class='intent_ul1'><li class='intent_li1'>序列</li><li class='intent_li2'>用户名</li><li class='intent_li3'>下单时间</li><li class='intent_li4'>门店</li><div class='clear'></div></ul>");
		 	 			$.each(data, function(i,item){
		 	 				$('.intent_div').append(
		 	 						"<a href='../cart/cart_managedetail.do?cartId="+item.cartId+"'><ul class='intent_ol'><li class='intent_li1'>1</li>"
		 	 								+"<li class='intent_li2'>" +item.userName+"</li><li class='intent_li3'>"
		 	 								+ showTime(new Date(item.lastUpdate))
		 	 								+"</li><li class='intent_li4'>"+item.counterName
		 	 								+"</li><div class='clear'></div></ul></a>");
		 				});
					}
		 	 	});
			}else if(state.url.indexOf('order/search')!==-1){
				$(".intent_div").empty();
				$.ajax({
		 	 		type : "post",
		 	 		url : "search.do",
		 	 		async : true,
		 	 		timeout : 15000,
		 	 		dataType:"json",
		 	 		cache:false,
		 	 	    data:{typeId:state.typeId},
		 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
		 	 			$(".message_block2").html(errorThrown+"查询失败");
	 		 	 		$(".message_body").css("display","block");
		 	 		},
		 	 		success : function(data) {
		 	 			$(".intent_div_top").empty();
		 	 			$(".intent_div_top").append("<ul class='intent_ul2'><li class='intent_li1'>序列</li><li class='intent_li2'>用户名</li><li class='intent_li3'>下单时间</li><li class='intent_li4'>收货地址</li><div class='clear'></div></ul>");
		 	 			$.each(data.data, function(i,item){
		 	 				$('.intent_div').append(
		 	 				"<a href='../order/detail.do?orderId="+item.id+"'><ul class='intent_ol'><li class='intent_li1'>1</li>"
							+"<li class='intent_li2'>" +item.orderNo+"</li><li class='intent_li3'>"
							+ showTime(new Date(item.lastUpdate))
							+"</li><li class='address_font intent_li4' >"+item.address
							+"</li><div class='clear'></div></ul></a>");
		 				});
					}
		 	 	});
			}else if(state.url.indexOf('product/getbyname')!==-1){
				var item = $(".show_item");
				item.empty();
				$.ajax({
					type:"post",
					url:"getbyname.do",
					data:{seachname:state.seachname,hardCode:state.hardCode},
					async:false,
					cache:false,
					success:function(data){
						$.each(data,function(i,product){
							thead="<form class='addcart' action='' method='post'>"
								+"<div class='item'>"
								+"<div class='item_number'>"
								+"<div class='mobile_img'><a href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
								+""+product.imageURL+"'></a></div>"
								+"<div class='mobile_block'>"
								+"<span  class='goods_name'>"+product.name
								+"</span><div class='retail_price'>零售价：￥"+product.retailPrice
								+"</div><div class='member_price'>会员价：￥"+product.memberPrice
								+"<input type='hidden' name='productId' value='"+product.productId+"' />"
								+"<div>本月销售：15</div>"
								+"<div class='suit_button'>"
								+"<div class='button_block'>"
								+"<a class='min' type='button'>-</a>"
								+"<input name='num' type='number' class='text_box' />"
								+"<a class='add' type='button'>+</a>"
								+"<div class='clear'></div></div>"
								+"<input class='add_button' type='submit' value='购物车'>"
								+"<div class='clear'></div></div>"
								+"</div></div></div><div class='clear'></div>"
								+"</form><div class='clear'></div>";
							item.append(thead);
						});
					},
				   error:function(error){
					   item.append("获取商品错误！");
				   }
				});
			}else if(state.url.indexOf('product/sorturl')!==-1){
				var item = $(".show_item");
				var sorturl = $(".sorturl").val();
				item.empty();
				$.ajax({
					type:"post",
					url:sorturl,
					data:{seachname:state.seachname,sortPrice:state.sortPrice},
					dataType:"json",
					timeout : 15000,
					async:true,
					cache:false,
					success:function(data){
						$.each(data,function(i,product){
							thead="<form class='addcart' action='' method='post'>"
								+"<div class='item'>"
								+"<div class='item_number'>"
								+"<div class='mobile_img'><a href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
								+""+product.imageURL+"'></a></div>"
								+"<div class='mobile_block'>"
								+"<span  class='goods_name'>"+product.name
								+"</span><div class='retail_price'>零售价：￥"+product.retailPrice
								+"</div><div class='member_price'>会员价：￥"+product.memberPrice
								+"<input type='hidden' name='productId' value='"+product.productId+"' />"
								+"<div>本月销售：15</div>"
								+"<div class='suit_button'>"
								+"<div class='button_block'>"
								+"<a class='min' type='button'>-</a>"
								+"<input name='num' type='number' class='text_box' />"
								+"<a class='add' type='button'>+</a>"
								+"<div class='clear'></div></div>"
								+"<input class='add_button' type='submit' value='购物车'>"
								+"<div class='clear'></div></div>"
								+"</div></div></div><div class='clear'></div>"
								+"</form><div class='clear'></div>";
							item.append(thead);
						});
						
					},
				   error:function(error){
					   item.append("获取商品错误！");
				   }
				});
			}else if(state.url.indexOf('product/sortProduct')!==-1){
				var item = $(".show_item");
				item.empty();
				$.ajax({
					type:"post",
					url:"sortProduct.do",
					data:{serialGlobal:state.serialGlobal,serialLocal:state.serialLocal},
					async:false,
					cache:false,
					success:function(data){
						$.each(data,function(i,product){
							thead="<form class='addcart' action='' method='post'>"
								+"<div class='item'>"
								+"<div class='item_number'>"
								+"<div class='mobile_img'><a href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
								+""+product.imageURL+"'></a></div>"
								+"<div class='mobile_block'>"
								+"<span  class='goods_name'>"+product.name
								+"</span><div class='retail_price'>零售价：￥"+product.retailPrice
								+"</div><div class='member_price'>会员价：￥"+product.memberPrice
								+"<input type='hidden' name='productId' value='"+product.productId+"' />"
								+"<div>本月销售：15</div>"
								+"<div class='suit_button'>"
								+"<div class='button_block'>"
								+"<a class='min' type='button'>-</a>"
								+"<input name='num' type='number' class='text_box' />"
								+"<a class='add' type='button'>+</a>"
								+"<div class='clear'></div></div>"
								+"<input class='add_button' type='submit' value='购物车'>"
								+"<div class='clear'></div></div>"
								+"</div></div></div><div class='clear'></div>"
								+"</form><div class='clear'></div>";
							item.append(thead);
						});
						
					},
				   error:function(error){
					   alert(error);
					   item.append("获取商品错误！");
				   }
				});
			}else if(state.url.indexOf('product/findgroup4')!==-1){
				$(".message_block2").html("action + findgroup4");
		 	 	$(".message_body").css("display","block");
				var item = $(".show_item");
				item.empty();
				$.ajax({
					type:"post",
					url:"findgroup.do",
					data:{seachname:state.seachname},
					async:false,
					cache:false,
					success:function(pGroups){
						//alert(pGroups);
						$.each(pGroups,function(i,pgroup){
							thead="<form class='addcart' action='' method='post'>"
								+"<div class='item'>"
								+"<div class='item_number'>"
								+"<div class='mobile_img'><a href='../product/activity_"+pgroup.actId+".do' class='product_a'><img class='item_img' src='"
								+"../"+pgroup.imageURL+"'></a></div>"
								+"<div class='mobile_block'>"
								+"<span  class='goods_name'>"+pgroup.actName
								+"</span></div></div><div class='clear'></div>"
								+"</form><div class='clear'></div>";
								item.append(thead);
						});
					},
				   error:function(error){
					   item.append("获取商品错误！");
				   }
				});
			}else if (state.url.indexOf('product/findgroupO')!==-1){
				var item = $(".show_item");
				item.empty();
				$.ajax({
					type:"post",
					url:state.realUrl,
					data:{seachname:state.seachname},
					async:false,
					cache:false,
					success:function(data){
						$.each(data,function(i,product){
							if(product!=null){
								if(product.typeId=="11388"||product.typeId=="11389"){
									thead="<form class='addcart' action='' method='post'>"
										+"<div class='item'>"
										+"<div class='item_number'>"
										+"<div class='mobile_img'><a target='_blank' href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
										+""+product.imageURL+"'></a></div>"
										+"<div class='mobile_block'>"
										+"<span  class='goods_name'>"+product.name
										+"</span><div class='retail_price'>"
										+"</div><div class='member_price'>物料价格：￥"+product.materialPrice
										+"<input type='hidden' name='productId' value='"+product.productId+"' />"
										+"<div class='suit_button'>"
										+"<div class='button_block'>"
										+"<a class='min' type='button'>-</a>"
										+"<input name='num' type='number' class='text_box' />"
										+"<a class='add' type='button'>+</a>"
										+"<div class='clear'></div></div>"
										+"<input class='add_button' type='submit' value='购物车'>"
										+"<div class='clear'></div></div>"
										+"</div></div></div><div class='clear'></div>"
										+"</form><div class='clear'></div>";
									item.append(thead);
								}
								else{
									thead="<form class='addcart' action='' method='post'>"
										+"<div class='item'>"
										+"<div class='item_number'>"
										+"<div class='mobile_img'><a target='_blank' href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
										+""+product.imageURL+"'></a></div>"
										+"<div class='mobile_block'>"
										+"<span  class='goods_name'>"+product.name
										+"</span><div class='retail_price'>零售价：￥"+product.retailPrice
										+"</div><div class='member_price'>会员价：￥"+product.memberPrice
										+"<input type='hidden' name='productId' value='"+product.productId+"' />"
										+"<div>本月销售：15</div>"
										+"<div class='suit_button'>"
										+"<div class='button_block'>"
										+"<a class='min' type='button'>-</a>"
										+"<input name='num' type='number' class='text_box' />"
										+"<a class='add' type='button'>+</a>"
										+"<div class='clear'></div></div>"
										+"<input class='add_button' type='submit' value='购物车'>"
										+"<div class='clear'></div></div>"
										+"</div></div></div><div class='clear'></div>"
										+"</form><div class='clear'></div>";
									item.append(thead);
								}
								//alert(product.retailPrice);
								
							}
						});
					},
				   error:function(error){
					   item.append("获取商品错误！");
				   }
				});
			}
		}
	}
	
})