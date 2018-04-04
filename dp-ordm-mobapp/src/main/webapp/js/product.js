	var str_name = "<span  class='sort_series t_pro' id='p11386'>正品</span>"
		+"<span  class='sort_series t_pre' id='p11388'>赠品</span>" 
		+"<span  class='sort_series t_mat' id='m11389'>物料</span>";
	var str_body = "<input type='hidden' class='TypeId' value=''>"
			+"<input type='hidden' class='sorturl' value='findbyptype.do'>"
			+"<span class='sort_price' id='1' >价格↑</span>"
			+"<input type='hidden' id='sort_hardCode'>";
	var str_ser = "<span  class='sort_series global' id='global'>全国销售金额</span>"
			+"<span  class='sort_series local' id='local' >本店销售金额</span>"+str_body;
$(document).ready(function(){
	var stockid=$("input[name='stockId']").val();
	function search(){
		var item = $(".show_item");
		var pw=$(".text_search").val();
		var hardCode=$(".text_search").prev().val();
		var sort = $(".sort");
		if(pw==""||pw==null){
			alert("搜索内容不能为空！");
			return;
		}
		$(".propertyOfPage").val("");
		sort.empty();
		sort.append(str_name);
		item.empty();
		if(window.addEventListener){
			history.pushState({"hardCode":hardCode,"seachname":pw,"url":"product/getbyname"},'',"product.do");
		}
		$.ajax({
			type:"post",
			url:"getbyname.do",
			data:{seachname:pw,hardCode:hardCode},
			async:false,
			cache:false,
			success:function(data){
				if(data==null||data==""){
					item.append("暂时还没该商品呦！");
				}
				else{
					switch(data[0].typeId){
					case "11386":
						$(".t_pro").css("background","rgb(240, 62, 62)");
						$(".t_pre").css("background","white");
						$(".t_mat").css("background","white");
						break;
					case "11388":
						$(".t_pre").css("background","rgb(240, 62, 62)");
						$(".t_pro").css("background","white");
						$(".t_mat").css("background","white");
						break;
					case "11389":
						$(".t_mat").css("background","rgb(240, 62, 62)");
						$(".t_pre").css("background","white");
						$(".t_pro").css("background","white");
						break;
					}
					$.each(data,function(i,product){
						if(product.typeId=="11388"||product.typeId=="11389"){
							thead="<form class='addcart' action='' method='post'>"
								+"<div class='item'>"
								+"<div class='item_number'>"
								+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
								+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
								+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
								+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
								+"<div class='mobile_block'>"
								+"<div  class='goods_name'>"+product.name
								+"</div><div>"
								+"<div class='member_price'>物料价格：￥"+product.materialPrice
								+"</div><input type='hidden' name='productId' value='"+product.productId+"' />"
								+"<div class='suit_button'>"
								+"<div class='button_block'>"
								+"<a class='min' type='button'>-</a>"
								+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
								+"<a class='add' type='button'>+</a>"
								+"<div class='clear'></div></div>"
								+"<input class='bg-green add_button' type='submit' value='加入购物车'>"
								+"<div class='clear'></div></div>"
								+"</div></div></div>"
								+"</form>";
							item.append(thead);
						}
						else{
							thead="<form class='addcart' action='' method='post'>"
								+"<div class='item'>"
								+"<div class='item_number'>"
								+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
								+""+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
								+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
								+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
								+"<div class='mobile_block'>"
								+"<div  class='goods_name'>"+product.name
								+"</div><div><div class='retail_price'>零售价：￥"+product.retailPrice
								+"</div><div class='member_price'>会员价：￥"+product.memberPrice
								+"</div></div><input type='hidden' name='productId' value='"+product.productId+"' />"
								+"<div class='member_qty'>本月销售："+product.qty+"</div>"
								+"<div class='suit_button'>"
								+"<div class='button_block'>"
								+"<a class='min' type='button'>-</a>"
								+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
								+"<a class='add' type='button'>+</a>"
								+"<div class='clear'></div></div>"
								+"<input class='bg-green add_button' type='submit' value='加入购物车'>"
								+"<div class='clear'></div></div>"
								+"</div></div></div>"
								+"</form>";
							item.append(thead);
						}
					});
				}
			},
		   error:function(error){
			   item.append("暂时还没该商品呦！");
		   }
		});
		//加载初始化工具 及加减方法
		initpage();
		initadddel();
		if($(".item").length==0){
			$(".sort").hide();
		}else{
			$(".sort").show();
		};
	}
	$(".button_search").click(function(){
		search();
	});
	$(".text_search").keyup(function(){
        if(event.keyCode == 13){
        	jQuery(".button_search").click();
        }
    });
	$(window).scroll(function(){
		var h=$(window).scrollTop();
		var item = $(".show_item");
		var hardCode=$(".searchid").val();
		var tag = $(".show_item").children().length;
		var url = $(".propertyOfPage").val();
		var typeId = $(".propertyOfPage").attr("id");
		var stockid=$("input[name='stockId']").val();
		if(h>=$(document).height()-$(window).height()-5){
			if(url==null||url==""||typeId==null||typeId==""||tag==""||tag==null){
				return;
			};
			if("findbyseries.do"==url||"findbybodytype.do"==url){
				$.ajax({
					type:"post",
					url:url,
					data:{seachname:hardCode,typeId:typeId,tag:tag},
					async:false,
					cache:false,
					success:function(data){
						if(data==""||data==null){
							return;
						}
						$.each(data,function(i,product){
							if(product!=null){
									if(product.typeId=="11389"){
										thead="<form class='addcart' action='' method='post'>"
											+"<div class='item'>"
											+"<div class='item_number'>"
											+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
											+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
											+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
											+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
											+"<div class='mobile_block'>"
											+"<span  class='goods_name'>"+product.name
											+"</span><div>"
											+"<div class='member_price'>物料价格：￥"+product.materialPrice
											+"</div></div><div class='clear'></div><input type='hidden' name='productId' value='"+product.productId+"' />"
											+"<div class='suit_button'>"
											+"<div class='button_block'>"
											+"<a class='min' type='button'>-</a>"
											+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
											+"<a class='add' type='button'>+</a>"
											+"<div class='clear'></div></div>"
											+"<input class='bg-green add_button' type='submit' value='加入购物车'>"
											+"<div class='clear'></div></div>"
											+"</div></div></div>"
											+"</form>";
										item.append(thead);
										initpage();
										initadddel();
									}
									else if(product.typeId=="11388"){
										thead="<form class='addcart' action='' method='post'>"
											+"<div class='item'>"
											+"<div class='item_number'>"
											+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
											+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
											+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
											+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
											+"<div class='mobile_block'>"
											+"<span  class='goods_name'>"+product.name
											+"</span><div>"
											+"<div class='member_price'>赠品价格：￥"+product.materialPrice
											+"</div></div><input type='hidden' name='productId' value='"+product.productId+"' />"
											+"<div class='suit_button'>"
											+"<div class='button_block'>"
											+"<a class='min' type='button'>-</a>"
											+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
											+"<a class='add' type='button'>+</a>"
											+"<div class='clear'></div></div>"
											+"<input class='bg-green add_button' type='submit' value='加入购物车'>"
											+"<div class='clear'></div></div>"
											+"</div></div></div>"
											+"</form>";
										item.append(thead);
										initpage();
										initadddel();
									}
									else{
										thead="<form class='addcart' action='' method='post'>"
											+"<div class='item'>"
											+"<div class='item_number'>"
											+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
											+""+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
											+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
											+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
											+"<div class='mobile_block'>"
											+"<div  class='goods_name'>"+product.name
											+"</div><div><div class='retail_price'>零售价：￥"+product.retailPrice
											+"</div><div class='member_price'>会员价：￥"+product.memberPrice
											+"</div></div><input type='hidden' name='productId' value='"+product.productId+"' />"
											+"<div class='member_qty'>本月销售："+product.qty+"</div>"
											+"<div class='suit_button'>"
											+"<div class='button_block'>"
											+"<a class='min' type='button'>-</a>"
											+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
											+"<a class='add' type='button'>+</a>"
											+"<div class='clear'></div></div>"
											+"<input class='bg-green add_button' type='submit' value='加入购物车'>"
											+"<div class='clear'></div></div>"
											+"</div></div></div>"
											+"</form>";
										item.append(thead);
										initpage();
										initadddel();
									}
							}
						});
					},
				   error:function(error){
				   }
				});
				initpage();
				initadddel();
			}
			else{
				return;
			}
/*
			
			//加载初始化工具 及加减方法
			initpage();
			initadddel();*/
			if($(".item").length==0){
				$(".sort").hide();
			}else{
				$(".sort").show();
			};
		}
	});
	
	$(".sort").on("click",".sort_price",function(){
		var item = $(".show_item");
		var sortprice = $(".sort_price").attr("id");
		var hardCode = $("#sort_hardCode").val();
		var sorturl = $(".sorturl").val();
		var typeId = $(this).prev().prev().val();
		$(this).css({'background':'#d9534f','border-color':'#d43f3a'});
		$(this).siblings().css({'background':'white','border-color':'#cecccc'});
		if(sortprice==1){
			$(this).text("价格↓");
			$(this).attr("id",0);
		}
		if(sortprice==0){
			$(this).text("价格↑");
			$(this).attr("id",1);
		}
		item.empty();
		if(window.addEventListener){
			history.pushState({"sortPrice":sortprice,"seachname":hardCode,"url":"product/sorturl"},'',"product.do");
		}
		$.ajax({
			type:"post",
			url:sorturl,
			data:{seachname:hardCode,sortPrice:sortprice,typeId:typeId},
			async:false,
			cache:false,
			success:function(data){
				$.each(data,function(i,product){
					thead="<form class='addcart' action='' method='post'>"
						+"<div class='item'>"
						+"<div class='item_number'>"
						+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
						+""+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
						+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
						+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
						+"<div class='mobile_block'>"
						+"<div  class='goods_name'>"+product.name
						+"</div><div><div class='retail_price'>零售价：￥"+product.retailPrice
						+"</div><div class='member_price'>会员价：￥"+product.memberPrice
						+"</div></div><input type='hidden' name='productId' value='"+product.productId+"' />"
						+"<div class='member_qty'>本月销售："+product.qty+"</div>"
						+"<div class='suit_button'>"
						+"<div class='button_block'>"
						+"<a class='min' type='button'>-</a>"
						+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
						+"<a class='add' type='button'>+</a>"
						+"<div class='clear'></div></div>"
						+"<input class='bg-green add_button' type='submit' value='加入购物车'>"
						+"<div class='clear'></div></div>"
						+"</div></div></div>"
						+"</form>";
					item.append(thead);
				});
			},
		   error:function(error){
			   item.append("商品已下架！");
		   }
		});
		//加载初始化工具 及加减方法
		initpage();
		initadddel();
		if($(".item").length==0){
			$(".sort").hide();
		}else{
			$(".sort").show();
		};
	});
	
	$(".sort").on("click",".global,.local",function(){
		var item = $(".show_item");
		var sort = $(this).attr("id");
		$(this).css({'background':'#d9534f','border-color':'#d43f3a'});
		$(this).siblings().css({'background':'white','border-color':'#cecccc'});
		if(sort=="global"){
			var seriesGlobal = $("#sort_hardCode").val();			
		}
		if(sort=="local"){
			var serialLocal = $("#sort_hardCode").val();
		}
		item.empty();
		if(window.addEventListener){
			history.pushState({"serialGlobal":seriesGlobal,"serialLocal":serialLocal,"url":"product/sortProduct"},'',"product.do");
		}
		if(seriesGlobal!=null){
			$.ajax({
				type:"post",
				url:"sortProduct.do",
				data:{serialGlobal:seriesGlobal,serialLocal:serialLocal},
				async:false,
				cache:false,
				success:function(data){
					$.each(data,function(i,product){
						thead="<form class='addcart' action='' method='post'>"
							+"<div class='item'>"
							+"<div class='item_number'>"
							+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
							+""+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
							+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
							+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
							+"<div class='mobile_block'>"
							+"<div  class='goods_name'>"+product.name
							+"</div><div><div class='retail_price'>零售价：￥"+product.retailPrice
							+"</div><div class='member_price'>会员价：￥"+product.memberPrice
							+"</div></div><input type='hidden' name='productId' value='"+product.productId+"' />"
							+"<div class='suit_button'>"
							+"<div class='button_block'>"
							+"<a class='min' type='button'>-</a>"
							+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
							+"<a class='add' type='button'>+</a>"
							+"<div class='clear'></div></div>"
							+"<input class='bg-green add_button' type='submit' value='加入购物车'>"
							+"<div class='clear'></div></div>"
							+"</div></div></div>"
							+"</form>";
						item.append(thead);
					});
					
				},
			   error:function(error){
				   alert(error);
				   item.append("商品已下架！");
			   }
			});	
		}
		if(serialLocal!=null){
			$.ajax({
				type:"post",
				url:"sortProduct.do",
				data:{serialGlobal:seriesGlobal,serialLocal:serialLocal},
				async:false,
				cache:false,
				success:function(data){
					$.each(data,function(i,product){
						thead="<form class='addcart' action='' method='post'>"
							+"<div class='item'>"
							+"<div class='item_number'>"
							+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
							+""+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
							+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
							+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
							+"<div class='mobile_block'>"
							+"<div  class='goods_name'>"+product.name
							+"</div><div><div class='retail_price'>零售价：￥"+product.retailPrice
							+"</div><div class='member_price'>会员价：￥"+product.memberPrice
							+"</div></div><input type='hidden' name='productId' value='"+product.productId+"' />"
							+"<div class='member_qty'>本月销售："+product.qty+"</div>"
							+"<div class='suit_button'>"
							+"<div class='button_block'>"
							+"<a class='min' type='button'>-</a>"
							+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
							+"<a class='add' type='button'>+</a>"
							+"<div class='clear'></div></div>"
							+"<input class='bg-green add_button' type='submit' value='加入购物车'>"
							+"<div class='clear'></div></div>"
							+"</div></div></div>"
							+"</form>";
						item.append(thead);
					});
				},
			   error:function(error){
				   alert(error);
				   item.append("商品已下架！");
			   }
			});
		}
		
		//加载初始化工具 及加减方法
		initpage();
		initadddel();
		if($(".item").length==0){
			$(".sort").hide();
		}else{
			$(".sort").show();
		};
	});
	
	//初始化隐藏 sort按钮
	$("#global,#local,#1").hide();
	$(".mask_div").click(function(){
		$(".mobile_class").hide();
		$(this).hide();
		$(".center").show();
	});
		$(".mobile_series").click(function(){
		$(".mask_div").show();
		$(this).addClass("mobile_series1");
		$(this).siblings().removeClass("mobile_series1");
		var mi=$(this).index();
		$(".mobile_class").html($(".class_block").eq(mi).html());
		$(".mobile_class").show();
		$(".mobile_class").css("z-index","100");
		$(".center").hide();
		$(".series li").click(function(){
			$(".mobile_class").hide();
			$(".center").show();
			$(".mask_div").hide();
			$(this).css('color','red');
			$(this).siblings().css('color','#517c9d');
			$(this).parent().parent().siblings().find(".series li").css('color','#517c9d');
			var item = $(".show_item");
			var hardCode=$(this).prev().val();
			var url = $(this).parent().prev().val();
			var typeId = $(this).parent().prev().prev().val();
			var pw=$(this).text();
			var tex=$(this).parent().prev().prev().text();
			var sort = $(".sort");
			sort.empty();
			$(".propertyOfPage").val(url);
			$(".propertyOfPage").attr("id",typeId);
			$(".text_search").val(pw);
			$(".text_search").prev().val(hardCode);
			$.cookie('searchid', hardCode, { expires: 7, path: '/dp-ordm/product',raw: true});
	 		$.cookie('text_search', pw, { expires: 7, path: '/dp-ordm/product',raw: true});
	 		if(url=="findbyseries.do"&&typeId=="11386"){
				sort.append(str_ser);
				$(".sorturl").val(url);
			}
			if(url=="findbybodytype.do"&&typeId=="11386"){
				sort.append(str_body);
				$(".sorturl").val(url);
			}
			$("#sort_hardCode").val(hardCode);
			$("#sort_hardCode").parent().find(".TypeId").val(typeId);
			item.empty();
			if(hardCode==4){
				if(window.addEventListener){
					history.pushState({"seachname":hardCode,"url":"product/findgroup4"},'',"product.do");
				}
				$.ajax({
					type:"post",
					url:"findgroup.do",
					data:{seachname:hardCode},
					async:false,
					cache:false,
					success:function(pGroups){
						if(pGroups==""||pGroups==null){
							item.append("商品已下架！");
							return;
						}
						$.each(pGroups,function(i,pgroup){
							thead="<form class='addcart' action='' method='post'>"
								+"<div class='item'>"
								+"<div class='item_number'>"
								+"<div class='mobile_img'><a  href='../product/activity_"+pgroup.actId+".do' class='product_a'><img class='item_img' src='"
								+pgroup.image+"'></a><img class='top_imgy' src='../img/top_imgy.png'></div>"
								+"<div class='mobile_block'>"
								+"<span  class='goods_name'>"+pgroup.name
								+"</span></div></div>"
								+"</form>";
								item.append(thead);
						});
					},
				   error:function(error){
					   item.append("商品已下架！");
				   }
				});
				initpage();
			}
			else{
				if(window.addEventListener){
					history.pushState({"seachname":hardCode,"url":"product/findgroupO","realUrl":url},'',"product.do");
				}
				$.ajax({
					type:"post",
					url:url,
					data:{seachname:hardCode,typeId:typeId},
					async:false,
					cache:false,
					success:function(data){
						if(data==""||data==null){
							item.append("商品已下架！");
							return;
						}
						$.each(data,function(i,product){
							if(product!=null){
								if(product.typeId=="11388"||product.typeId=="11389"){
									if(product.typeId=="11389"){
										thead="<form class='addcart' action='' method='post'>"
											+"<div class='item'>"
											+"<div class='item_number'>"
											+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
											+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
											+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
											+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
											+"<div class='mobile_block'>"
											+"<div  class='goods_name'>"+product.name
											+"</div><div>"
											+"<div class='member_price'>物料价格：￥"+product.materialPrice
											+"</div><div class='clear'></div></div><input type='hidden' name='productId' value='"+product.productId+"' />"
											+"<div class='suit_button'>"
											+"<div class='button_block'>"
											+"<a class='min' type='button'>-</a>"
											+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
											+"<a class='add' type='button'>+</a>"
											+"<div class='clear'></div></div>"
											+"<input class='bg-green add_button' type='submit' value='加入购物车'>"
											+"<div class='clear'></div></div>"
											+"</div></div></div>"
											+"</form>";
										item.append(thead);
									}
									if(product.typeId=="11388"){
										thead="<form class='addcart' action='' method='post'>"
											+"<div class='item'>"
											+"<div class='item_number'>"
											+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
											+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
											+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
											+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
											+"<div class='mobile_block'>"
											+"<span  class='goods_name'>"+product.name
											+"</span><div>"
											+"<div class='member_price'>赠品价格：￥"+product.materialPrice
											+"</div><div class='clear'></div><input type='hidden' name='productId' value='"+product.productId+"' />"
											+"<div class='suit_button'>"
											+"<div class='button_block'>"
											+"<a class='min' type='button'>-</a>"
											+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
											+"<a class='add' type='button'>+</a>"
											+"<div class='clear'></div></div>"
											+"<input class='bg-green add_button' type='submit' value='加入购物车'>"
											+"<div class='clear'></div></div>"
											+"</div></div></div>"
											+"</form>";
										item.append(thead);
									}
								}
								else{
									thead="<form class='addcart' action='' method='post'>"
										+"<div class='item'>"
										+"<div class='item_number'>"
										+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
										+""+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
										+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
										+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
										+"<div class='mobile_block'>"
										+"<div  class='goods_name'>"+product.name
										+"</div><div><div class='retail_price'>零售价：￥"+product.retailPrice
										+"</div><div class='member_price'>会员价：￥"+product.memberPrice
										+"</div></div><input type='hidden' name='productId' value='"+product.productId+"' />"
										+"<div class='member_qty'>本月销售："+product.qty+"</div>"
										+"<div class='suit_button'>"
										+"<div class='button_block'>"
										+"<a class='min' type='button'>-</a>"
										+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
										+"<a class='add' type='button'>+</a>"
										+"<div class='clear'></div></div>"
										+"<input class='bg-green add_button' type='submit' value='加入购物车'>"
										+"<div class='clear'></div></div>"
										+"</div></div></div>"
										+"</form>";
									item.append(thead);
								}
							}
						});
					},
				   error:function(error){
					   item.append("商品已下架！");
				   }
				});
				initpage();
			}
			//加载初始化工具 及加减方法
			initpage();
			initadddel();
			if($(".item").length==0){
				$(".sort").hide();
			}else{
				$(".sort").show();
			};
			if($('body').height()<$(window).height()){
				var ch=$(window).height()-$(".icon").height()-$(".top").height()-$(".bottom").height();
				$(".center").height(ch);
			}
			$(this).parent().hide();
		});
		})
	$(".series li").click(function(){
		var stockid=$("input[name='stockId']").val();
		$(this).css('color','red');
		$(this).siblings().css('color','#517c9d');
		$(this).parent().parent().siblings().find(".series li").css('color','#517c9d');
		var item = $(".show_item");
		var hardCode=$(this).prev().val();
		var url = $(this).parent().prev().val();
		$(".propertyOfPage").val(url);
		var typeId = $(this).parent().prev().prev().val();
		var pw=$(this).text();
		var tex=$(this).parent().prev().prev().text();
		var sort = $(".sort");
		sort.empty();
		$(".propertyOfPage").val(url);
		$(".propertyOfPage").attr("id",typeId);
		$(".text_search").val(pw);
		$(".text_search").prev().val(hardCode);
		$.cookie('searchid', hardCode, { expires: 7, path: '/dp-ordm/product',raw: true});
 		$.cookie('text_search', pw, { expires: 7, path: '/dp-ordm/product',raw: true});
 		if(url=="findbyseries.do"&&typeId=="11386"){
			sort.append(str_ser);
		}
		if(url=="findbybodytype.do"&&typeId=="11386"){
			sort.append(str_body);
		}
		$("#sort_hardCode").val(hardCode);
		$("#sort_hardCode").parent().find(".TypeId").val(typeId);
		$(".sorturl").val(url);
		item.empty();
		if(hardCode==4){
			if(window.addEventListener){
				history.pushState({"seachname":hardCode,"url":"product/findgroupO","realUrl":url},'',"product.do");
			}
			$.ajax({
				type:"post",
				url:"findgroup.do",
				data:{seachname:hardCode},
				async:false,
				cache:false,
				success:function(pGroups){
					if(pGroups==""||pGroups==null){
						item.append("商品已下架！");
						return;
					}
					$.each(pGroups,function(i,act){
						thead="<form class='addcart' action='' method='post'>"
							+"<div class='item'>"
							+"<div class='item_number'>"
							+"<div class='mobile_img'><a  href='../product/activity_"+act.actId+".do' class='product_a'><img class='item_img' src='"
							+act.image+"'></a><img class='top_imgy' src='../img/top_imgy.png'></div>"
							+"<div class='mobile_block'>"
							+"<span  class='goods_name'>"+act.name
							+"</span></div></div>"
							+"</form>";
							item.append(thead);
					});
				},
			   error:function(error){
				   item.append("商品已下架！");
			   }
			});
			initpage();
		}
		else{
			if(window.addEventListener){
				history.pushState({"seachname":hardCode,"url":"product/findgroupO","realUrl":url},'',"product.do");
			}
			$.ajax({
				type:"post",
				url:url,
				data:{seachname:hardCode,typeId:typeId},
				async:false,
				cache:false,
				dataType:"json",
				success:function(data){
					if(data==""||data==null){
						item.append("商品已下架！");
						return;
					}
					$.each(data,function(i,product){
						if(product!=null){
							if(product.typeId=="11388"||product.typeId=="11389"){
								if(product.typeId=="11389"){
									thead="<form class='addcart' action='' method='post'>"
										+"<div class='item'>"
										+"<div class='item_number'>"
										+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
										+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
										+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
										+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
										+"<div class='mobile_block'>"
										+"<span  class='goods_name'>"+product.name
										+"</span><div class='retail_price'>"
										+"</div><div class='member_price'>物料价格：￥"+product.materialPrice
										+"<input type='hidden' name='productId' value='"+product.productId+"' />"
										+"<div class='suit_button'>"
										+"<div class='button_block'>"
										+"<a class='min' type='button'>-</a>"
										+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
										+"<a class='add' type='button'>+</a>"
										+"<div class='clear'></div></div>"
										+"<input class='bg-red add_button' type='submit' value='购物车'>"
										+"<div class='clear'></div></div>"
										+"</div></div></div>"
										+"</form>";
									item.append(thead);
								}
								if(product.typeId=="11388"){
									thead="<form class='addcart' action='' method='post'>"
										+"<div class='item'>"
										+"<div class='item_number'>"
										+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
										+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
										+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
										+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
										+"<div class='mobile_block'>"
										+"<span  class='goods_name'>"+product.name
										+"</span><div class='retail_price'>"
										+"</div><div class='member_price'>赠品价格：￥"+product.materialPrice
										+"<input type='hidden' name='productId' value='"+product.productId+"' />"
										+"<div class='suit_button'>"
										+"<div class='button_block'>"
										+"<a class='min' type='button'>-</a>"
										+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
										+"<a class='add' type='button'>+</a>"
										+"<div class='clear'></div></div>"
										+"<input class='bg-red add_button' type='submit' value='购物车'>"
										+"<div class='clear'></div></div>"
										+"</div></div></div>"
										+"</form>";
									item.append(thead);
								}
							}
							else{
								thead="<form class='addcart' action='' method='post'>"
									+"<div class='item'>"
									+"<div class='item_number'>"
									+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
									+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
									+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
									+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
									+"<div class='mobile_block'>"
									+"<span  class='goods_name'>"+product.name
									+"</span><div class='retail_price'>零售价：￥"+product.retailPrice
									+"</div><div class='member_price'>会员价：￥"+product.memberPrice
									+"<input type='hidden' name='productId' value='"+product.productId+"' />"
									+"<div>本月销售："+product.qty+"</div>"
									+"<div class='suit_button'>"
									+"<div class='button_block'>"
									+"<a class='min' type='button'>-</a>"
									+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
									+"<a class='add' type='button'>+</a>"
									+"<div class='clear'></div></div>"
									+"<input class='bg-red add_button' type='submit' value='购物车'>"
									+"<div class='clear'></div></div>"
									+"</div></div></div>"
									+"</form>";
								item.append(thead);
							}
							//alert(product.retailPrice);
							
						}
					});
				},
			   error:function(error){
				   item.append("商品已下架！");
			   }
			});
			initpage();
		}
		//加载初始化工具 及加减方法
		initadddel();
		if($(".item").length==0){
			$(".sort").hide();
		}else{
			$(".sort").show();
		};
		if($('body').height()<$(window).height()){
			var ch=$(window).height()-$(".icon").height()-$(".top").height()-$(".bottom").height();
			$(".center").height(ch);
		}
	});

});
$(document).ready(function(){
	var searchid = $.cookie('searchid');
	var text_search = $.cookie('text_search');
	if(searchid!=null&&searchid!="null"&&text_search!=null&&text_search!="null"){
		$(".searchid").val(searchid);
		$(".text_search").val(text_search);
	}
	
 	$(".search").on("change keyup",".text_search",function(){
 		var searchid = $(".searchid").val();
 		var text_search = $(".text_search").val();
 		var scid = $.cookie('searchid');
 		var tsh = $.cookie('text_search');
 		if(scid!=null&&tsh!=null){//如果存在内容，直接先清空
 			$.cookie('searchid', null);
 	 		$.cookie('text_search', null);
 		}
 		$.cookie('searchid', searchid, { expires: 7, path: '/dp-ordm/product',raw: true});
 		$.cookie('text_search', text_search, { expires: 7, path: '/dp-ordm/product',raw: true});
 		$(".text_search").prev().val("");
 	});
	$(".sort").on("click",".t_pro,.t_pre,.t_mat",function(){
 		var sid= $(this).attr('id');
 		var sname = $('input[name="searchname"]').val();
 		var hardCode=$(".text_search").prev().val();
 		var item = $(".show_item");
		var stockid=$("input[name='stockId']").val();
 		item.empty();
 		if(window.addEventListener){
 			history.pushState({"hardCode":hardCode,"sid":sid,"seachname":sname,"url":"product/getbyname"},'',"product.do");
 		};
 			switch(sid){
	 		case "p11386":
	 			//这里写的是加载 正品 的
	 			$(".t_pro").css("background","rgb(240, 62, 62)");
	 			$(".t_pre").css("background","white");
	 			$(".t_mat").css("background","white");
	 			$.ajax({
	 				type:"post",
	 				url:"../product/getbyname.do",
	 				data:{seachname:sname,sid:sid,hardCode:hardCode},
	 				async:false,
	 				cache:false,
	 				success:function(data){
	 					if(data==null||data==""){
 							item.append("暂时还没该商品呦！");
 						}else{
	 					$.each(data,function(i,product){
	 						if(product.typeId=="11386"){
 								thead="<form class='addcart' action='' method='post'>"
 									+"<div class='item'>"
 									+"<div class='item_number'>"
 									+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
 									+""+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
 									+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
 									+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
 									+"<div class='mobile_block'>"
 									+"<div  class='goods_name'>"+product.name
 									+"</div><div><div class='retail_price'>零售价：￥"+product.retailPrice
 									+"</div><div class='member_price'>会员价：￥"+product.memberPrice
 									+"</div></div><input type='hidden' name='productId' value='"+product.productId+"' />"
 									+"<div class='member_qty'>本月销售："+product.qty+"</div>"
 									+"<div class='suit_button'>"
 									+"<div class='button_block'>"
 									+"<a class='min' type='button'>-</a>"
 									+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
 									+"<a class='add' type='button'>+</a>"
 									+"<div class='clear'></div></div>"
 									+"<input class='bg-green add_button' type='submit' value='加入购物车'>"
 									+"<div class='clear'></div></div>"
 									+"</div></div></div>"
 									+"</form>";
 								item.append(thead);
	 						}
	 					});
 					  }
	 				},
	 			   error:function(error){
	 				   item.append("暂时还没该商品呦！");
	 			   }
	 			});
	 			initpage();
	 			initadddel();
	 			break;
	 		case "p11388":
	 			//这里写的是加载 赠品 的
	 			$(".t_pre").css("background","rgb(240, 62, 62)");
	 			$(".t_pro").css("background","white");
	 			$(".t_mat").css("background","white");
	 			$.ajax({
	 				type:"post",
	 				url:"../product/getbyname.do",
	 				data:{seachname:sname,sid:sid,hardCode:hardCode},
	 				async:false,
	 				cache:false,
	 				success:function(data){
	 					if(data==null||data==""){
 							item.append("暂时还没该赠品呦！");
 						}else{
	 					$.each(data,function(i,product){
 							if(product.typeId=="11388"){
 								thead="<form class='addcart' action='' method='post'>"
 									+"<div class='item'>"
 									+"<div class='item_number'>"
 									+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
 									+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
 									+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
 									+"<input class='actFlag' type='hidden' value="+product.actItself+"></div>"
 									+"<div class='mobile_block'>"
 									+"<span  class='goods_name'>"+product.name
 									+"</span><div>"
 									+"<div class='member_price'>赠品价格：￥"+product.materialPrice
 									+"</div><div class='clear'></div></div><input type='hidden' name='productId' value='"+product.productId+"' />"
 									+"<div class='suit_button'>"
 									+"<div class='button_block'>"
 									+"<a class='min' type='button'>-</a>"
 									+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
 									+"<a class='add' type='button'>+</a>"
 									+"<div class='clear'></div></div>"
 									+"<input class='bg-green add_button' type='submit' value='加入购物车'>"
 									+"<div class='clear'></div></div>"
 									+"</div></div></div>"
 									+"</form>";
 								item.append(thead);
 							}
	 					});
 						}
	 				},
	 			   error:function(error){
	 				  item.append("暂时还没该赠品呦！");
	 			   }
	 			});
	 			initpage();
	 			initadddel();
	 			break;
	 		case "m11389":
	 			//这里写的是加载 物料 的
	 			$(".t_mat").css("background","rgb(240, 62, 62)");
	 			$(".t_pro").css("background","white");
	 			$(".t_pre").css("background","white");
	 			$.ajax({
	 				type:"post",
	 				url:"../product/getbyname.do",
	 				data:{seachname:sname,sid:sid,hardCode:hardCode},
	 				async:false,
	 				cache:false,
	 				success:function(data){
	 					if(data==null||data==""){
 							item.append("暂时还没该物料呦！");
 						}else{
	 					$.each(data,function(i,product){
 							if(product.typeId=="11389"){
 								thead="<form class='addcart' action='' method='post'>"
 									+"<div class='item'>"
 									+"<div class='item_number'>"
 									+"<div class='mobile_img'><a  href='../product/detail_"+product.productId+".do' class='product_a'><img class='item_img' src='"
 									+product.imageURL+"'></a><div class='inventory' title='"+product.inv[stockid]+"'>暂时无货</div>"
 									+"<input class='actFlag' type='hidden' value="+product.actFlag+">"
 									+"<input class='actItself' type='hidden' value="+product.actItself+"></div>"
 									+"<div class='mobile_block'>"
 									+"<span  class='goods_name'>"+product.name
 									+"</span><div>"
 									+"<div class='member_price'>物料价格：￥"+product.materialPrice
 									+"</div><div class='clear'></div></div><input type='hidden' name='productId' value='"+product.productId+"' />"
 									+"<div class='suit_button'>"
 									+"<div class='button_block'>"
 									+"<a class='min' type='button'>-</a>"
 									+"<input name='num' type='number' class='text_box' min='"+product.minUnit+"'step='"+product.minUnit+"'value='"+product.minUnit+"' />"
 									+"<a class='add' type='button'>+</a>"
 									+"<div class='clear'></div></div>"
 									+"<input class='bg-green add_button' type='submit' value='加入购物车'>"
 									+"<div class='clear'></div></div>"
 									+"</div></div></div>"
 									+"</form>";
 								item.append(thead);
 							}
	 					});
 						}
	 				},
	 			   error:function(error){
	 				  item.append("暂时还没该物料呦！");
	 			   }
	 			});
	 			initpage();
	 			initadddel();
	 			break;
 		}

 	});
});


function initNumOfCat(CountId){
	$.ajax({
		type:"post",
		url:"",
		data:{CountId:CountId},
		async:false,
		cache:false,
		success:function(data){
			
		},
		error:function(err){
			
		}
	});
}
