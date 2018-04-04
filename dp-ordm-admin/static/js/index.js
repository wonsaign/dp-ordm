$(document).ready(function(){
	$("#myDiv").width($(window).width()-150)
	$(".drop-down").click(function(){
		var a=$(this).parent().find(".ul2").css("display");
		if(a=='none'){
			$(this).parent().find(".ul2").css('display','block');
		}else{
			$(this).parent().find(".ul2").css('display','none');
		}
	});		
	$('.page').click(function(){
		var a=$(this).attr('id');
		var i=$('.page').index(this);
		$('.page').css('font-weight','100');
		$('.page').eq(i).css('font-weight','bold');
		var xmlhttp;
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
		xmlhttp.open("GET",a+".html",true);
		xmlhttp.send();
	})
})