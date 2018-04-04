$(document).ready(function(){	
	//点击查询按钮执行查询
	$(".search_text").focus(function(){
		$(".counter_show").show();
	});
	//点击下拉框执行的获取用户的ajax
	$(".counter li").click(function(){
		var value = $(this).attr('id');
		$(".search_text").val(value);
		$(".counter_show").hide();
		});
	
})