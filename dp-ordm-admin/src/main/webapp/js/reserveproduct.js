$(document).ready(function(){
	
	
	$('#sel_productId').on('change',function(){
		$('#productName').val($('#sel_productId').find("option:selected").text());
	});
	
	
	$('.gro_div').on('keyup','.productName',function(){
		$(this).parent().find(".gro_ul").show();
        var index = $.trim($(this).val().toString()); // 去掉两头空格
        if(index != ''){ // 如果搜索框输入为空
        	$(this).parent().find(".gro_ul li").addClass("on");
        	$(this).parent().find(".gro_ul li:contains('"+index+"')").removeClass("on");
        }
    });
	
	$('.gro_div').on('click','.gro_ul li',function(){
		$("#hid_id").val($(this).find("input").val());
		$(this).parent().parent().find(".productName").val($(this).text());
		$(this).parent().hide();
	});
	
	
//	<%--用户  启用--%>
	$('#myDiv').on('click','.set_start',function(){
		var src=$(this).attr('href');
		parameter=$(this).parent().siblings("#parameter").text();
		var position=$(this);
		var avalible=$(this).parent().siblings(".avalible");
		$.ajax({
			type:"post",
			url:src+".do",
			data:{parameter:parameter},
			async:true,
			success: function(data) {
				position.hide();
				position.siblings('.set_down').show();
				avalible.text("启用");
			}
		});
		
	});
//	<%--用户  禁用--%>
	$('#myDiv').on('click','.set_down',function(){
		var src=$(this).attr('href');
		parameter=$(this).parent().siblings("#parameter").text();
		var position=$(this);
		var avalible=$(this).parent().siblings(".avalible");
		$.ajax({
			type:"post",
			url:src+".do",
			data:{parameter:parameter},
			async:true,
			success: function(data) {
				position.hide();
				position.siblings('.set_start').show();
				avalible.text("禁用");
			}
		});
	});

});