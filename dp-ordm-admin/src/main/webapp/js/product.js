$(document).ready(function(){
	//产品系列上传图片
	$('#myDiv').on('click','.Series_amend',function(){
		var file=$(this).prev().find('input[name="file"]');
		var key=$('[name="key"]').text();
		var page=$('[name="page"]').text();
		var max=$('[name="max"]').text();
		var num=10;
		var did=$(this).parent().siblings(".searchbyid");
		if(file.val().length>0){
			htmlinput="<input hidden='hidden' name='key' value="+key+"></input>"
				+"<input hidden='hidden' name='page' value="+page+"></input>"
				+"<input hidden='hidden' name='num' value="+num+"></input>"
				+"<input hidden='hidden' name='max' value="+max+"></input>";
			$(this).prev().append(htmlinput);
			$(this).prev().submit();
			
//			alert("key:"+key+" page:"+page+" max:"+max+" mun:"+num);
//			var xmlhttp;
//			if (window.XMLHttpRequest)
//			  {// code for IE7+, Firefox, Chrome, Opera, Safari
//			  xmlhttp=new XMLHttpRequest();
//			  }
//			else
//			  {// code for IE6, IE5
//			  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
//			  }
//			xmlhttp.onreadystatechange=function()
//			  {
//			  if (xmlhttp.readyState==4 && xmlhttp.status==200)
//			    {
//			    document.getElementById("myDiv").innerHTML=xmlhttp.responseText;
//			    }
//			  }
//			xmlhttp.open("POST","../product/updateSeriesUrl.do",true);
//			xmlhttp.send("key="+key+"&num="+num+"&page="+page+"&max="+max+"&did="+did+"&file"+file);
		}else{
			alert("请选择上传图片");
		}
	});
	
	$('#myDiv').on('click','.pclose',function(){
		var pid = $(this).parent().prev().text();
		var parent= $(this).parent().parent();
		$.ajax({
			type:"post",
			url:"../product/changeavalibel.do",
			async:true,
			data:{pid:pid,avalible:false},
			success: function(data) {
				if("success"==data){
					parent.remove();
					alert("更改状态成功！");
				}
				if("falser"==data){
					alert("更改状态失败！");
				}
			},
			error:function(err){
				alert("err");
			}
		});
	});
	
	$('#myDiv').on('click','.popen',function(){
		var pid = $(this).parent().prev().text();
		var parent= $(this).parent().parent();
		$.ajax({
			type:"post",
			url:"../product/changeavalibel.do",
			async:true,
			data:{pid:pid,avalible:true},
			success: function(data) {
				if("success"==data){
					parent.remove();
					alert("更改状态成功");
				}
				if("falser"==data){
					alert("更改状态失败!");
				}
			},
			error:function(err){
				alert("err");
			}
		});
	});
});