$(document).ready(function(){
	$('textarea[name="description"]').on('blur keyup paste change',function(){
		var text=$(this).val();
		if(text.length>=45){
			$(this).val(text.slice(0, 45));
		}
		$(this).next().text($(this).val().length+"/45");
	});
	$('[name="discount"]').on('blur',function(){
		var reg=new RegExp("^0+.([0-9]{1,2})?$");
		var val=$(this).val();
		if(val==""){
			$(this).parent().find('.msg').text("输入不能为空");
			$(this).parent().find('.msg').css("color","red");
			}else{
				if(!reg.test(val)){
					$(this).parent().find('.msg').text("请输入小数(保留两位)");
					$(this).parent().find('.msg').css("color","red");
				}else {
					$(this).parent().find('.msg').text("");
				}
			}
	});
	
	$('#pricePolicyBtn').on('click',function(){
		var reg=new RegExp("^0+.([0-9]{1,2})?$");
		var val=$('[name="discount"]').val();
		var msg=$('.msg').text();
		if(val==""){
			alert("折扣点数: 输入不能为空");
		}else if(!reg.test(val)){
			alert("折扣点数: 请输入小数(保留两位)");
		}else if(msg!=""){
			alert("折扣点数: "+msg);
		}else{
			$('form').submit();
		}
	});
});