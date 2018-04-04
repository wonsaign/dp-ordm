$(document).ready(function(){
	window.onpopstate = function(e){
		/*alert("data="+history.state);
		alert(history.state.url);*/
		if(history.state)
		{
			var state = history.state;
			var url = history.state.url;
			if(url.indexOf('select_div')!==-1){
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
				xmlhttp.open("GET",state.realUrl,true);
				xmlhttp.send();
			}else if(url.indexOf('search_product')!==-1){
				//var sr=$(this).attr('href');
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
				xmlhttp.open("get",state.realUrl,true);
				xmlhttp.send();
			}else if(url.indexOf('product/product_detail')!==-1){
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
				xmlhttp.open("POST",state.realUrl,true);
				xmlhttp.send();
			}else if(url.indexOf('pGroup/actiTactics')!==-1){
				alert('actiTactics in');
				$.ajax({
					type:"post",
					url:src+".do",
					data:{actID:state.actID,status:state.status},
					async:true,
					success:function(data){
						alert('success');
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
						xmlhttp.open("GET",state.realUrl,true);
						xmlhttp.send();
					}
				});
			}
		}
	}
});