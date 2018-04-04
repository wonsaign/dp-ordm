$(document).ready(function(){
	/*点击审核通过*/ 
	$('#myDiv').on('click','.order_pass',function(){
		var lihref=$(this).parent().find(".lihref").val();
		var orderNo=$(this).parent().next().text();
		var ordhref=$(this).attr('href');
		$(".back_block").append("<div class='modal-header'><span style='font-weight:bold;'>订单审核通过</span></div>"
	      +"<input id='pass_orderNo' type='hidden' value='"+orderNo+"'>"
	      +"<input id='pass_lihref' type='hidden' value='"+lihref+"'>"
		  +"<div style='text-align:center; margin:10px auto;'>"	
		  +"<span href='"+ordhref+"' class='sure_pass btn-primary' style='padding:6px 12px;border-radius: 2px;text-align:center; margin-right:10px;'>确定</span>"		
		  +"<span class='sure_back btn-primary' style='padding:6px 12px;border-radius: 2px;text-align:center;'>取消</span></div>");		
		$(".back_block").show();
	});
	$('#myDiv').on('click','.sure_pass',function(){
		var table = $('[name="table_position"]');
		var orderNo = $("#pass_orderNo").val();
		var pass_lihref=$("#pass_lihref").val();
		var src=$(this).attr('href');
		$.ajax({
			type:"post",
			url:src+".do",
			data:{orderNo:orderNo},
			async:true,
			success: function(data) {
				$(".back_block").hide()
				$(".back_block").empty();
				jQuery("li[href='"+pass_lihref+"']").click();
			}
		});
	});
	//弹出退回窗
	$('#myDiv').on('click','.order_refuse',function(){
		var lihref=$(this).parent().find(".lihref").val();
		var orderNo=$(this).parent().next().text();
		var ordhref=$(this).attr('href');
		$(".back_block").append("<div class='modal-header'><span style='font-weight:bold;'>退回意见:</span></div>"
      +"<input id='refuse_orderNo' type='hidden' value='"+orderNo+"'>"
      +"<input id='refuse_lihref' type='hidden' value='"+lihref+"'>"
      +"<textarea name='description' class='description' rows='3' cols='40' maxlength='45' placeholder='最多45字' style='width:98%;height: 66px;resize:none; margin:10px 1%;'></textarea>"
	  +"<div class='col-sm-12' style='text-align:right;'>0/45</div><div style='text-align:center; margin:10px auto;'>"	
	  +"<span href='"+ordhref+"' class='sure_refuse btn-primary' style='padding:6px 12px;border-radius: 2px;text-align:center; margin-right:10px;'>确定</span>"		
	  +"<span class='sure_back btn-primary' style='padding:6px 12px;border-radius: 2px;text-align:center;'>取消</span></div>");
	  $(".back_block").show();
		
	});
	//退回窗口 确定
	$('#myDiv').on('click','.sure_refuse',function(){
		var src=$(this).attr('href');
		var position = $(this);
		var table = $('[name="table_position"]');
		var orderNo=$('#refuse_orderNo').val();
		var refuse_lihref=$("#refuse_lihref").val();
		var refuseCommon=$(this).parent().prev().prev().val();
		if(refuseCommon==""||refuseCommon==null){
			alert("意见不能为空哟！");
			return;
		}
		$.ajax({
			type:"post",
			//路径
			url:src+".do",
			data:{orderNo:orderNo,refuseCommon:refuseCommon},
			async:true,
			success: function(data) {
				$(".back_block").hide();
				$(".back_block").empty();
				jQuery("li[href='"+refuse_lihref+"']").click();
			}
	});
	});
	
	//确认无效订单
	$('#myDiv').on('click','.order_delete',function(){
		var msg = "您真的确定要删除吗？\n\n请确认！";
		var key = $("a[name='key']").text();
		var page = $("a[name='page']").text();
		var orderNo = $(this).parent().next().text();
		var num = 10;
		if (confirm(msg)==true){
		    var src=$(this).attr('href');
			htmlobj=$.ajax({
							  url:src+".do",
							  type:"POST",
							  data:{orderNo:orderNo,page:page,key:key},
							  async:false
							});
		    $("#myDiv").html(htmlobj.responseText);
		}
		
	});
	
	//确认作废
	$('#myDiv').on('click','.order_invalid',function(){
		var msg = "确定置为无效？\n\n请确认！";
		var page = $("a[name='page']").text();
		var key = $("a[name='key']").text();
		var orderNo = $(this).parent().next().text();
		var num = 10;
		if (confirm(msg)==true){
		    var src=$(this).attr('href');
			htmlobj=$.ajax({
							  url:src+".do",
							  type:"POST",
							  data:{orderNo:orderNo,page:page,key:key},
							  async:false
							});
		    $("#myDiv").html(htmlobj.responseText);
		}
		
	});
	
	//退回窗口 取消
	$('#myDiv').on('click','.sure_back',function(){
		var position = $(this);
		$(".back_block").hide();
		$(".back_block").empty();
		$(".back_block").children('textarea').val('');
		position.parent().prev().text("0/45");
	});
	
	$('#myDiv').on('click','.order_img_show',function(){
		var position=$(this);
		if($(this).text().trim()=="有"){
			$(".bigimg").attr('src','');
			$(".position_body").show();
			var firstImgUrl=$(this).parent().find(".img_rul").children().eq(0).val();
			$(".position_img").children().remove();
			$(this).parent().find(".img_rul").children().each(function(){
				$(".position_img").append("<img class='bigimg' src="+$(this).val()+">")
//				$(".position_body").append("<img src="+$(this).val()+">");
			});
				var img = new Image();
//				img.src =$(this).parent().find(".img_rul").val();
				img.src=firstImgUrl;
				img.onload = function(){
					var imgw=img.width;
					var imgh=img.height;
					var imgw_h=imgw/imgh;
					var winw=$(window).width();
					var winh=$(window).height();
					var winw_h=winw/winh;
					if(winw_h>1 & imgw_h > winw_h){
						$(".position_body").height(winh/imgw_h*0.8);
						$(".position_body").width(winw*0.8);
					}else if(imgw_h<1 && imgw_h>winw_h){
						$(".position_body").height(winh/imgw_h*0.8);
						$(".position_body").width(winw*0.8);
					}else{
						$(".position_body").height(winh*0.8);
						$(".position_body").width(winh*imgw_h*0.8);
					};
					var imgw1=$(".position_body").width();
					var imgh1=$(".position_body").height();
					$(".bigimg").width(imgw1);
					$(".bigimg").height(imgh1);
					var ww_iw = (winw-imgw1)/2;
					var wh_ih = (winh-imgh1)/2;
					$(".bigimg").each(function(){
						var index=$(this).index();
						$(this).attr('src',position.parent().find(".img_rul").children().eq(index).val());
					})
					$(".position_body").css({"position":"absolute","top":wh_ih,"left":ww_iw});
					$(".bigimg").eq(0).show();
					var imgl = $(".bigimg").length;
					var i=0;
						$(".go").click(function(){
							i++;
							$(".bigimg").eq(i).show();
							$(".bigimg").eq(i).siblings().hide();
							if(i == imgl-1){
								i=imgl-2;
							}
							
						})
						$(".back").click(function(){
							if(i == 0){
								i=1;
							}
							i--;
							$(".bigimg").eq(i).show();
							$(".bigimg").eq(i).siblings().hide();
							
						})
					
				}
			
			$(".bigimg_close").click(function(){
				$(".position_body").hide();
				$(".bigimg").attr('src','');
			})
			
		}
	});
});