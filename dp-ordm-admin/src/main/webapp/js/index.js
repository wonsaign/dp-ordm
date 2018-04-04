$(document).ready(function(){
	/*$("#myDiv").width($(window).width()-150)*/
	$(".drop-down").click(function(){
		var a=$(this).parent().find(".ul2").css("display");
		if(a=='none'){
			$(this).parent().find(".ul2").show();
			$(this).find("span").addClass("span_top");
			$(this).parent().siblings().find(".ul2").hide();
			$(this).parent().siblings().find("span").removeClass("span_top");
		}else{
			$(this).parent().find(".ul2").hide();
			$(this).find("span").removeClass("span_top");
		}
	});	
	$(".close_img").click(function(){
		$(".hidden_body").hide();
	})
	$('.page').click(function(){
		event = $(this);
		init(event);
	});
	
	$('#loginOut').click(function(){
		var url=$(this).attr('href');
		window.location.href=url+".do";
	});
	//加载 配置
	$(".init").click(function(){
		var settings = $(this).next();//.css("display","block");
		if(settings.children().length>0){
			settings.show();//上下滑动
		}else{
			$.ajax({
				type:"post",
				url:"../settingadm/init.do",
				async:true,
				success: function(data) {
					$.each(data,function(index,value){
						str="<div href='../settingadm/setting_"+value.did
							+"' style='font-size: 5px;' class='setting'>"
							+value.summary+"</a></div>";
						settings.append(str);
					});
				} 
			});
		}

	});
	//点击
	$(".settings").on("click",".setting",function(){
		even = $(this);	
		even.addClass('selactive');
		var a=even.attr('href');
		var url = a+".do";
		htmlobj=$.ajax({url:url,async:false});
	    $("#myDiv").html(htmlobj.responseText);
	    $('.page').removeClass('selactive');
	});
	$("#myDiv").on("dblclick","tr[name='table_body']",function(){
		var orderid=$(this).find(".orderid").val();
		var tbody=$(".modal-body tbody");
		var title=$(".modal-title");
		var modalbody=$(".money");
		tbody.empty();
		title.empty();
		modalbody.empty();
		$.ajax({
			type:"post",
			url:"../orderadm/orderDetail.do",
			async:true,
			data:{id:orderid},
			success:function(data){
				order="<strong>订单明细</strong><span>(订单号:"+data.orderNo+"</span><span>店铺:"+data.counterName+"</span><span>客户:"+data.order.customerName+")</span>"
				title.append(order);
				$.each(data.orderDetails,function(i,product){
				tr="<tr><td>"+parseInt(i+1)+"</td><td>"+product.productName+"</td><td>"+product.memberPrice+"</td><td>"+product.quantity
				+"</td><td>"+parseInt(product.memberPrice*product.quantity).toFixed(2)+"</td><td>"+parseInt(product.quantity*product.unitPrice).toFixed(2)
				+"</td><td>"+product.realQty+"</td></tr>";
				tbody.append(tr);
				$(".modal").show();
				});
				money="<div class='settle'><hr>"
				+"<span class='information_settle'>商品总价:￥<strong>"+data.productPrice+"</strong></span><br>"
				+"<span class='information_settle'>额外物料费用:<strong>"+data.materialFee+"</strong></span><br>"
				+"<span class='information_settle'>运费:￥<strong>"+data.expressFee+"</strong></span><br>"
				+"<span class='information_settle'>订单的总价:￥<strong>"+data.paymentFee+"</strong></span>"
				+"<div class='clear'></div><hr>"
				+"<span class='information_settle red'>实付款:"+data.order.paymentPrice+" </span>"
				+"<span class='information_settle red'>使用余额:<span class='priceFormat'>"+data.order.useBalance+"</span></span>"
				+"<span class='information_settle red'>应付金额:<strong id='paymentFee'>"+data.paymentFee+"</strong></span><div class='clear'></div><hr></div>"
				modalbody.append(money);
			}					
		})
	});
	$("#myDiv").on("dblclick","tr[name='table_merge']",function(){
		var orderid=$(this).find(".mergeid").text();
		var tbody=$(".modal-body tbody");
		var title=$(".modal-title");
		var modalbody=$(".money");
		tbody.empty();
		title.empty();
		modalbody.empty();
		$.ajax({
			type:"post",
			url:"../orderadm/getMergeOrders.do",
			async:true,
			data:{id:orderid},
			success:function(data){
				order="<strong>订单明细</strong><span>(合并号:"+orderid+")</span>"
				title.append(order);
				$.each(data.data,function(i,order){
				tr="<tr><td>"+parseInt(i+1)+"</td><td>"+order.orderNo+"</td><td>"+order.counterName+"</td><td>"+order.customerName
				+"</td><td>"+order.paymentFee+"</td><td>"+order.paymentPrice
				+"</td><td>"+order.order.useBalance+"</td><td>"+order.productPrice+"</td>"
				+"<td>"+order.expressFee+"</td><td>"+order.materialFee+"</td>"
				+"<td>"+order.payType+"</td><td>"+order.orderStatus+"</td><td>"+order.description+"</td>"
				+"<td>"+order.orderCreatTime+"</td></tr>";
				tbody.append(tr);
				$(".modal").show();
				});
			}					
		})
	});
	$("#myDiv").on("click",".search_order",function(){
	  	  var start=$(".starttime").val();
	  	  var end=$(".endtime").val();
	  	  var tab=$(".tab");
	  	  var summary=$(".summary");
	  	  tab.empty();
	  	  summary.empty();
	  	  var tbody="";
	  	  var money="";
	  	  $.ajax({
	  		type:"post",
	  	    url:"../orderadm/getareaorder.do",
	  	  	async:true,
	  	  	data:{starttime:start,endtime:end},
	  	  	success:function(data){
	  	  		tbody+="<table id='table_position1' class='table table-bordered table-striped'>"
	  	  		+"<thead><tr name='table_head'><th>序号</th><th>客户类型</th><th>客户</th><th>单数</th><th>总金额</th></tr></thead><tbody>";
	  	  	    $.each(data,function(i,bean){
	  	  			if(i=="myItem"){
	  	  			tbody+="<tr name='tr_body' class="+i+" title='双击显示订单明细'><td class='td_one'></td><td class='cType'>"+bean.customerType+"<input type='hidden' class='cusid' value='"+bean.customerId+"'></td><td class='cName'>"+bean.customerName+"</td>"
	  	  				+"<td class='quantity'>"+bean.quantity+"</td><td class='realFee'>"+bean.realFeeFormat+"</td></tr>" 
	  	  			}else if(i=="summary"){
	  	  				money="<span class='information_settle red'>加盟(不包含直营)总订单:<strong>"+bean.quantity+"</strong></span>"
	  				    +"<span class='information_settle red'>加盟(不包含直营)总金额:￥<strong>"+bean.realFeeFormat+"</strong></span>"
	  	  			}else{
	  	  				$.each(bean,function(j,order){
		    	  				tbody+="<tr name='tr_body' class="+i+" title='双击显示订单明细'><td class='td_one'></td><td class='cType'>"+order.customerType+"<input type='hidden' class='cusid' value='"+order.customerId+"'></td><td class='cName'>"+order.customerName+"</td>"
		    	  				+"<td class='quantity'>"+order.quantity+"</td><td class='realFee'>"+order.realFeeFormat+"</td></tr>" 
		    	  			})
	  	  			}
	  	  		})
	  	  		tbody+="</tbody><tfoot><tr><th>序号</th><th>客户类型</th><th>客户</th><th>单数</th><th>总金额</th>"
	  	  		+"</tr></tfoot></table>";
	  	  	
	  	  		tab.append(tbody); 	
	  	  		$("tr[name='tr_body']").each(function(){
	  	  		  $(this).find(".td_one").text(Number($(this).index())+1);
	  	  	  })
	  	  	  $("#table_position1").DataTable({
	  	  		"order": [0, 'asc']  
	  	  	  }); 
	  	  	  summary.append(money); 
	  	  	},
		    error:function(){
	  	  		alert("erro");
	  	  	}
	  	  })
	  });
		$("#myDiv").on("dblclick","tr[name='tr_body']",function(){
			var orderid=$(this).find(".quantity").text();
			if(orderid!=0){
				var start=$(".starttime").val();
				var end=$(".endtime").val();
				var cusid=$(this).find(".cusid").val();
				var tbody=$(".modal-body tbody");
				var modalbody=$(".money");
				var cType=$(this).find(".cType").text();
				var cName=$(this).find(".cName").text();
				var realFee=$(this).find(".realFee").text();
				var title=$(".modal-title");
				var tr="";
				title.empty();
				tbody.empty();
				modalbody.empty();
				$.ajax({
					type:"post",
					url:"../orderadm/areaorderDetail.do",
					async:true,
					data:{starttime:start,endtime:end,customerId:cusid}, 
					success:function(data){
						order="<strong>订单明细</strong><span>(客户:"+cName+"</span><span>客户类型:"+cType+")</span>"
						 $.each(data,function(i,order){
								tr+="<tr><td>"+parseInt(i+1)+"<input type='hidden' name='orderId' value="+order.id+"></td><td>"+order.orderNo+"</td><td>"+order.counterName+"</td>"
								+"<td>"+order.paymentFee+"</td><td>"+order.realFee+"</td>"
								+"<td>"+order.orderCreatTime+"</td><td class='pro_order'>订单详情</td></tr>";
							})
							money="<div class='settle'><hr>"
							+"<span class='information_settle red'>总金额:￥<strong>"+realFee+"</strong></span><br><hr></div>"
							title.append(order);
							tbody.append(tr);
							modalbody.append(money);
							$("#amend").show(); 
						}				
				})
			}
		});
		$("#myDiv").on("click",".pro_order",function(){
			var tr_click=$(this).parent().next().attr('class');
			if(tr_click=="tr_click"){
				$(".tr_click").remove();
			}else{
				$(".tr_click").remove();
				var modalbody=$(this).parent();
				/*$(this).parent().after("<tr><td colspan='7'><table class='click_tab'><thead><tr><td>序号</td><td>商品名称</td><td>会员价</td><td>数量</td><td>金额</td><td>折扣价</td><td>实发</td></tr></thead></table></td></tr>");*/
				var orderid=$(this).parent().find("input[name='orderId']").val();
				var order;
				$.ajax({
					type:"post",
					url:"../orderadm/orderDetail.do",
					async:true,
					data:{id:orderid},
					success:function(data){
						order+="<tr class='tr_click'><td colspan='7'><strong>订单明细</strong><span>(订单号:"+data.orderNo+"</span><span>店铺:"+data.counterName+"</span><span>客户:"+data.order.customerName+")</span>"
						order+="<table class='click_tab'><thead><tr><td>序号</td><td>商品名称</td><td>会员价</td><td>数量</td><td>金额</td><td>折扣价</td><td>实发</td></tr></thead><tbody>"
						$.each(data.orderDetails,function(i,product){
						order+="<tr><td>"+parseInt(i+1)+"</td><td>"+product.productName+"</td><td>"+product.memberPrice+"</td><td>"+product.quantity
						+"</td><td>"+parseInt(product.memberPrice*product.quantity).toFixed(2)+"</td><td>"+parseInt(product.quantity*product.unitPrice).toFixed(2)
						+"</td><td>"+product.realQty+"</td></tr>";
						});
						order+="</tbody></table>"
						order+="<div class='settle'><hr>"
						+"<span class='information_settle'>商品总价:￥<strong>"+data.productPrice+"</strong></span><br>"
						+"<span class='information_settle'>额外物料费用:<strong>"+data.materialFee+"</strong></span><br>"
						+"<span class='information_settle'>运费:￥<strong>"+data.expressFee+"</strong></span><br>"
						+"<span class='information_settle'>订单的总价:￥<strong>"+data.paymentFee+"</strong></span>"
						+"<div class='clear'></div><hr>"
						+"<span class='information_settle red'>实付款:"+data.order.paymentPrice+" </span>"
						+"<span class='information_settle red'>使用余额:<span class='priceFormat'>"+data.order.useBalance+"</span></span>"
						+"<span class='information_settle red'>应付金额:<strong id='paymentFee'>"+data.paymentFee+"</strong></span><div class='clear'></div><hr></div></td></tr>"
						modalbody.after(order);
					}					
				})
			}
			
		})
	$("#amend").scroll(function() {
		var wh=parseInt($(".modal-dialog").css("margin-top"));
		var ww=parseInt($(".modal-dialog").css("margin-right"));
		var mw=parseInt($(".modal-dialog").width()-32);
		var scr=$("#amend").scrollTop();
		if(scr > wh){
			$(".modal-header").width(mw);
			$(".modal-header").css({"position":"fixed","top":"0px","left":"15%"});
		}else{
			$(".modal-header").css({"position":"static"});

		}
	})
	$("#myDiv").on("click",".close,.btn-primary-close",function(){
		$(".modal").hide();
		var tbody=$(".modal-body tbody");
		var title=$(".modal-title");
		var modalbody=$(".money");
		tbody.empty();
		title.empty();
		modalbody.empty();
	});
	$('#myDiv').on('click','.search_checked',function(){
		var sr=$(this).attr('href');
		var ipv=$(this).parent().find(".ip_text").val();
		var typeid=$(this).parent().find('select[name="typeid"]').val();
		if (window.XMLHttpRequest)
		  {// code for IE7+, Firefox, Chrome, Opera, Safari
		  xmlhttp=new XMLHttpRequest();
		  }
		else
		  {// code for IE6, IE5
		  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		  }
		xmlhttp.onreadystatechange=function()
		  {
		  if (xmlhttp.readyState==4 && xmlhttp.status==200)
		    {
		    document.getElementById("myDiv").innerHTML=xmlhttp.responseText;
		    }
		  }
		xmlhttp.open("get",sr+".do?name="+ipv+"&typeid="+typeid,true);
		xmlhttp.send();
	}); 
	$('#myDiv').on('click','.search',function(){
		var sr=$(this).attr('href');
		var ipv=$(this).parent().find(".ip_text").val();
		var typeid=$(this).parent().prev().find('select[name="typeid"]').val();
		var avalible=$(this).parent().prev().find('select[name="avalible"]').val();  
		var tbody="";
		var tab=$(".tab");
		tab.empty();
		$.ajax({
			type:"post",
			url: sr+".do",
			asyns:true,
			data:{name:ipv,typeid:typeid,avalible:avalible},
			success:function(data){
					tbody="<table id='table_position1' class='table table-bordered table-striped'>"
		  	  		+"<thead><tr name='table_head'><th>ID</th><th>编辑</th><th>商品代码</th><th>名称</th><th>分类</th><th>状态</th>"
		  	  		+"<th>图片</th><th>操作时间</th></tr></thead><tbody>";
		  	  	    $.each(data,function(i,product){
	  	  			tbody+="<tr><td>"+product.product.productId+"</td><td><a href='../product/productupdate.do?id=" +
	  	  			+product.product.productId+"' target='_blank'>"
					+"<span>修改</span></a>" 
					if(product.product.avalible==true){
						tbody+="<span class='pclose'>禁用</span>"
					}else{
						tbody+="<span class='popen'>启用</span>"
					}
	  	  			tbody+="</td><td>"+product.product.productCode+"</td><td>"+product.product.name+"</td><td>"+product.product.typeName+"</td>"
	  	  			+"<td class='ava'>"
		  	  		if(product.product.avalible==true){
						tbody+="禁用"
					}else{
						tbody+="启用"
					}
	  	  		    tbody+="</td><td><div class='img_rul'  type='hidden'><input  type='hidden' value='"+product.product.imageURL+"'></div>"
	  	  		    +"<span style='color: blue' class='order_img_show'>有</span></td><td>"+product.lastUpdate+"</td></tr>";		
		  	  		})
		  	  		tbody+="</tbody><tfoot><tr name='table_head'><th>ID</th><th>编辑</th><th>商品代码</th><th>名称</th><th>分类</th><th>状态</th>"
		  	  		+"<th>图片</th><th>操作时间</th></tr></tfoot></table>"
		  	  		tab.append(tbody); 		
		  	  	  $("#table_position1").DataTable(); 
			},
			error:function(){
				
			}
		})
	}); 
});


//init 加载右边页面
function init(even){
	var a=even.attr('href');	
	var url = a+".do";
	$("#myDiv").empty();
	htmlobj=$.ajax({url:url,async:false});
    $("#myDiv").html(htmlobj.responseText);
	var i=$('.page').index(even);
	$('.page').eq(i).addClass('selactive');
	$('.page:not(:eq('+i+'))').removeClass('selactive');
	$('.setting').removeClass('selactive');
	$("#table_position").DataTable();
}