
//选择购物车弹窗处理函数
function initcartwindowselect(position){
	$(".message_block2").on("click",'input[name="shopselect"]',function(){
		var counterval= $(this).val();
		var counterName=$(this).parent().find(".counterName").text();
		var counterdata=$(this).attr("data-id");
		$(".counters_one").text(counterName);
		$(".span_text").text(counterName);
		$(".counters input").val(counterval);
		$(".select_text input").val(counterval);
		$(".select_text input").attr("data-id",counterdata);
		$(".counters input").attr("data-id",counterdata);
	});
	$(".message_block2").on("click",'.sure_counters',function(){
		var imagepath = $('body').find('input[name="imagepath"]').val();
		var counterCode= $("input[name='shopselect']:checked").val();
		$('.localtag').find(".item").remove();
		initcartbutton($('body'));
		$(".message_body").hide();
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
 	 			var href=window.location.href;
 	 		    if(href.indexOf("index")>0){
 	 		    	$.each(data,function(i,item){
 	 					var itemhtml =	"<div class='index_item'>"+
 						"<a href='../product/series_"+item.hardCode+".do'>"+
 						"<img class='item_index_img' src='"+imagepath+item.url +"'></a>"+
 						"<span class='goods_namecenter'>"+item.name +"</span></div>"			
 						$('.localtag').append(itemhtml);
 	 	 			});
 	 		    	window.location.reload();
 	 		    }
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
 	 			alert("<div class='message_top'>提示信息</div><div class='message_center'>"+errorThrown+"失败!</div>");
 	 		},
 	 		success : function(data) {
 	 			
			}
 	 	});
		window.location.reload();
	});
	
}
function initmumber(){
	var counterId =$('input[name="counterId"]');
	var cartId =$('input[name="cartId"]');
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
	$.post("../cart/getpresentprice.do",{cartId:cartId,counterId:counterId},function(data){
		for(var key in data){
			if(key=="materialfee"){
				var materialprice=Number(data[key]).toFixed(2);
				if(materialprice>=0){
					$('span[name="materialname"]').text("超出物料金额:");
					$('span[name="materialfee"]').text(materialprice);
				}else{
					$('span[name="materialname"]').text("可配物料金额:");
					$('span[name="materialfee"]').text(-materialprice);
				}
			}
			if(key=="lqtys"){
				$('span[name="qty"]').text( data[key][0]);
				$('span[name="productqty"]').text(data[key][1]);
				$('span[name="materialqty"]').text(data[key][2]);
				if(data[key][3]>0){
					$('span[name="fee"]').html("已选<span class='changefee'>" +data[key][3].toFixed(2)+ "</span>元产品");
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
			if($(".select_text").find("input").val()==0){
				$(".message_body").show();
				var shop=$(".shop_hover").html();
				$(".message_block2").html(shop);
				var select_data=$(".select_text input").attr("data-id");
				$(".counters_one").text($(".select_text .span_text").text());
				$(".counters input").val($(".select_text input").val());
				$(".counters input").attr("data-id",select_data);
				//加入监听事件
				initcartwindowselect($('.message_block2'));
				return false;
			}
			var position =$(this);
			position.find(".add_button").attr("disabled",true);
			var goodname =position.find(".goods_name").text();
			var num=position.find('input[name="num"]').val();
			var productId=position.find('input[name="productId"]').val();
			var stockId =$("input[name='stockId']").val();
			//处理购物车商品的库存信息
			$.ajax({
	 	 		type : "post",
	 	 		url : "../cart/add.do",
	 	 		async : false,
	 	 		timeout : 15000,
	 	 		dataType:"text",
	 	 		cache:false,
	 	 	    data:{productId:productId,num:num},
	 	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
	 	 			$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>"+errorThrown+"加入购物车失败!</div>");
		 	 		$(".message_body").css("display","block");
	 	 			position.find(".add_button").attr("disabled",false);
	 	 		},
	 	 		success : function(data) {
	 	 			var counter=$(".span_text").text();
	 	 			$(".message_block2").html("<div class='message_top'>提示信息</div><div class='message_center'>您已成功向<span style='color:#0e5993;font-weight:bold'>"+counter+"</span>购物车添加<span style='color:#0e5993;font-weight:bold'>"+goodname+"</span>商品</div>");
	 	 			$(".message_body").fadeIn();
	 	 			$(".message_body").fadeOut(1000);
	 	 			position.find(".add_button").attr("disabled",false);
		 	 		initmumber();
				}
	 	 	}); 
		});
		$('input[name="productId"]').each(function(){
			var position = $(this);
			if(position.parent().prev().find(".inventory").attr("title")<=0){
				position.parent().prev().find(".inventory").show();
				position.parent().find(".add_button").attr("disabled",true);
				position.parent().find(".add_button").css({"background":"rgb(159, 159, 157)","border-color":"rgb(159, 159, 157)"});
			};
			if(position.parent().prev().find(".actFlag").val()=="true"){
					position.parent().find("input[name='num']").attr("readonly","readonly");
					position.parent().find(".add_button").attr("disabled",true);
					position.parent().find(".add_button").css({"background":"rgb(159, 159, 157)","border-color":"rgb(159, 159, 157)"});
					position.parent().prev().append("<img class='top_imgy' src='../img/active.png'>");
			}
		});
	 	$(".text_search").change(function(){
	 		$(".text_search").prev().val("");
	 	});
    }
	
//开始
$(document).ready(function(){
	$(".bottom").height(Number($(".operationpanel").height())+Number($(".operationpanel1").height())+Number($(".top").height())+10);
	$(".icon1_back").click(function(){
		window.history.back();  
	})
    var tit=$(document).attr('title');
    $(".icon1_text").text(tit);
	initcartbutton($('body'));
	$('.select_text').click(function(){
		var position=$(".message_block2");
		$(".message_body").show();
		$(".message_block2").html($(".shop_div").html());
		/*$("input[name='shopselect'][value='0']").remove();*/
		var span_text=$(".select_text .span_text").text();
		var counters=$(".select_text input").val();
		var select_data=$(".select_text input").attr("data-id");
		$(".counters_one").text(span_text);
		$(".counters input").val(counters);
		$(".counters input").attr("data-id",select_data);
		position.find("input[name='shopselect']").each(function(){
			if($(this).attr("data-id")==select_data){
				$(this).attr("checked",'checked');
			}
			if($(this).val() == 0){
				$(this).parent().remove();
			}
		});
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
 	 				position.find("input[name='shopselect']").each(function(){
 	 					if($(this).attr('data-id')==item.counterId && item.status!=666){
 	 						$(this).attr("disabled",true);
 	 						$(this).css("color","red");
 	 						$(this).attr("title",item.desc);
 	 					}
 	 					if($(this).attr('data-id')==item.counterId && item.status==666){
 	 						$(this).attr("disabled",false);
 	 						$(this).css("color","#6dbb44");
 	 						$(this).attr("title",item.desc);
 	 					}
 	 				});
 	 			});
			}
 	 	});
		initcartwindowselect($('.message_block2'));
	});
	var top=parseInt($(".top").height());
	var hh=$(window).height();
	$(".class_block").height(hh-132);
	$(".operationpanel1").css("bottom",top);
	$(".operationpanel").css("bottom",top);
	$(document).bind("click",function(e){ 
		var x=event.target; ;
		if(x.id=="message_modal"){
			$(".message_body").hide();
		}
	});
	var wh=$(window).height();
	$(".center").css("min-height",wh*0.6);
	initpage();
/*	initmumber();*/
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
	var j=0;
	$(".nav th").each(function(){
		var sear=new RegExp($(this).find("a").attr("href"));
		var i=$(this).index()+1;
		if(sear.test(str)){
			$(this).find("img").attr("src","../img/ico"+i+".png");
			j++;
		}else{
			$(this).find("img").attr("src","../img/icoc"+i+".png");
		}
	});
	if(j==0){
		$(".icon1").eq(1).show();
		$(".icon1").eq(0).hide();
	}else{
		$(".icon1").eq(0).show();
		$(".icon1").eq(1).hide();
	}
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
	$(".tactics4").find(".index_item a").each(function(){
		$(this).append("<img class='top_imgy' src='../img/active.png'>")
	})
	$(".message_close").click(function(){
		$(".message_block2").empty();
		$(".message_body").hide();
	});
	//切换门店
	//初始化购物车按钮 未选择不允许点击购物车
	$('.shopping_block a').click(function(){
		var position = $(this);
		if($(this).attr("href")=="#"){
				//加入监听事件
				$(".message_body").show();
				var shop=$(".shop_hover").html();
				$(".message_block2").html(shop);
				var select_data=$(".select_text input").attr("data-id");
				$(".counters_one").text($(".select_text .span_text").text());
				$(".counters input").val($(".select_text input").val());
				$(".counters input").attr("data-id",select_data);
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
	if( position.find('.select_text input').val() == 0){
		position.find('.shopping_block a').attr("href","#");
	}else{
		position.find('.shopping_block a').attr("href","../cart/cart.do");
	}
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
