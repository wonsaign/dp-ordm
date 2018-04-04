$(document).ready(function(){
	$('#myDiv').on('click','.p_down',function(){
		var src=$(this).attr('href');
		var actID=$(this).parent().siblings(".actID").text();
		var status=$(this).attr("id");
		//W:actiTactics
		var url = "../productgroup/productgroup.do";
		history.pushState({"url":"pGroup/actiTactics","realUrl":url,"actID":actID,"status":status},'',"index.do");
		$.ajax({
			type:"post",
			url:src+".do",
			data:{actID:actID,status:status},
			async:true,
			success:function(data){
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
				xmlhttp.open("GET","../productgroup/productgroup.do",true);
				xmlhttp.send();
			}
		});
	});
	$('#myDiv').on('click','.p_start',function(){
		var src=$(this).attr('href');
		var actID=$(this).parent().siblings(".actID").text();
		var status=$(this).attr("id");
		$.ajax({
			type:"post",
			url:src+".do",
			data:{actID:actID,status:status},
			async:true,
			success:function(data){
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
				xmlhttp.open("GET","../productgroup/productgroup.do",true);
				xmlhttp.send();
			}
		});
	});
	
	$('#myDiv').on('click','.p_amend',function(){
		var mod=$(this).attr('href');
		var actID=$(this).parent().siblings(".actID").text();
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
		xmlhttp.open("POST",mod+".do?actID="+actID,true);
		xmlhttp.send();
	});
	
	$('#myDiv').on('keyup paste change','.gro_text',function(){
		var value = $(this).parent().parent().find("option:selected").val();
		var ul = $(this).siblings(".gro_ul");
		var pname = $(this).val();
		ul.empty();
		if("pro"==value){
			$.ajax({
				type:"post",
				url:"../productgroup/getproducts.do",
				data:{pName:pname},
				async:true,
				success: function(data) {
					$.each(data,function(i,product){
						ul.append("<li value='"+product.productId+"'>"+product.name+"</li>");
					});
				}
			});
		}
		if("ser"==value){
			$.ajax({
				type:"post",
				url:"../productgroup/getseriesbyname.do",
				data:{pName:pname},
				async:true,
				success: function(data) {
					$.each(data,function(i,series){
						ul.append("<li value='"+series.hardCode+"'>"+series.name+"</li>");
					});
				}
			});
		}
	});


	$('#myDiv').on('focus','.gro_text',function(){
		$(this).siblings(".gro_ul").show();
	});

	/* $('#myDiv').on('blur','.gro_text',function(){
		$(this).siblings(".gro_ul").hide();
	}); */

	$('#myDiv').on('click','.gro_ul li',function(){
		var pid = $(this).val();
		var pName = $(this).text();
		$(this).parent().parent().find(".gro_text").val(pName);
		$(this).parent().parent().find(".pid").val(pid);
		$(this).parent().hide();
	});
	$('#myDiv').on('hover','.gro_ul li',function(){
		$(this).css("background","white");
		$(this).siblings(".gro_ul li").css("background","#FFC0CB");
	});

	
	$('#myDiv').on('click','.saveActivity',function(){
		var actName = $(this).parents().find(".actName").val();
		var startTime = $(this).parents().find(".startTime").val();
		var endTime = $(this).parents().find(".endTime").val();
		var description = $(this).parents().find(".description").val();
		var imageURL = $(this).parents().find(".file").val()
		var buyNum = $("#1").find(".buyNum").val();
		var giveNum = $("#1").find(".giveNum").val();
		var actType = $("#1").find(".product").next().find("option:selected").val();
		var products = $("#1").find(".product").next().find("input:checkbox[name='products']:checked");
		var gifts = $("#1").find(".giveproduct").next().find("input:checkbox[name='gifts']:checked");
		if(actName==""||actName==null){
			alert("活动策略名称不能为空!");
			return;
		}
		if(startTime==""||startTime==null){
			alert("活动 开始时间 不能为空!");
			return;
		}
		if(endTime==""||endTime==null){
			alert("活动 结束时间 不能为空!");
			return;
		}
		if(description==""||description==null){
			alert("活动 描述 不能为空!");
			return;
		}
		if(imageURL==""||imageURL==null){
			alert("活动 图片 不能为空!");
			return;
		}
/*		if(buyNum==""||buyNum==null||buyNum<=0){
			alert("亲,购买数量不正确哟!");
			return;
		}
		if(giveNum==""||buyNum==null||giveNum<0){
			alert("亲,赠送不正确哟!");
			return;
		}
		if(actType==""||actType==null){
			alert("亲,请选择 产品策略 哟!");
			return;
		}
		if(products==""||products==null||products.length<=0){
			alert("亲,请 选择需要购买的商品 哟!");
			return;
		}
		if(gifts==""||gifts==null||gifts.length<=0){
			alert("亲,请 赠送商品 哟!");
			return;
		}*/
		$("form").submit();
	});

	$('#myDiv').on('click','.cancel',function(){
		//alert("取消");
		$(".dis_option").empty();
		$(".dis_option").append("<div>hehe</div>");
	/* 	$(this).parents().find(".actName").val().empty();
		 $(this).parents().find(".startTime").val().empty();
		$(this).parents().find(".endTime").val().empty();
		$("#1").find(".buyNum").val().empty();
		$("#1").find(".giveNum").val().empty();
		$("#1").find(".product").next().find("option:selected").val().empty();
		$("#1").find(".product").next().find("input:checkbox[name='products']:checked").empty();
		$("#1").find(".giveproduct").next().find("input:checkbox[name='products']:checked").empty(); */
	});
	
	$('#myDiv').on('click','.p_amend',function(){
		var mod=$(this).attr('href');
		var actID=$(this).parent().siblings(".actID").text();
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
		xmlhttp.open("POST",mod+".do?actID="+actID,true);
		xmlhttp.send();
	});

	$('#myDiv').on('click','.page_bt',function(){
		var src=$('[name="paginationURL"]').text();
		alert(src);
		var nowpage=parseInt($('[name="page"]').text());
		var max=parseInt($('[name="max"]').text());
		var jumppage=$('[name="jumppage"]').val();
		var position=$(this);
		var page=1;
		var num=10;
		var flag=true;
		if(position.attr('name')=="firstpage"&&nowpage==1){
			flag=false;
		}else if(position.attr('name')=="prevpage"){
			if(nowpage>1){
				page=parseInt(nowpage)-1;
			}else{
				flag=false;
			}
		}else if(position.attr('name')=="nextpage"){
			if(nowpage<max){
				page=parseInt(nowpage)+1;
			}else{
				flag=false;
			}
		}else if(position.attr('name')=="lastpage"){
			if(page!=max){
				page=max;
			}else{
				flag=false;
			}
		}else if(position.attr('name')=="jump"){
			if(jumppage<=max&&jumppage>0){
				page=jumppage;
			}else{
				page=nowpage;
			}
		}
		if(flag){
			$.ajax({
				type:"post",
				url:src+".do",
				data:{page:page,num:num},
				async:true,
				success: function(data) {
					alert(data);
						var tbody=$("id='pgroup'").find("tbody");
						var tr;
						tbody.empty();
						$.each(data,function(index,pgroups){
							tr="<tr><td class='actID'>"+pgroups.actId+"</td><td>"
								+"<span class='p_amend' href='../productgroup/productgroupadd'>修改</span>";
							if(pgroups.status==false){
								tr+="<span class='p_start' href='../productgroup/changeStatus' id='true'>启用</span>";
							}
							if(pgroups.status==true){
								tr+="<span class='p_down' href='../productgroup/changeStatus' id='false'>禁用</span>";
							}
							tr+="</td><td>"+pgroups.actName+"</td><td>"+pgroups.startTime+"~"+pgroups.endTime
								+"</td><td class='p_start'>正常</td><td>"+pgroups.createUserName
								+"</td><td>"+pgroups.lastUpdate+"</td></tr>";
							tbody.append(tr);
						});
				},
				error:function(){
					alert(加载失败);
				}
			});
			$('[name="page"]').text(page);
			$('[name="jumppage"]').val(page);
		}
	});
	
//	$('#myDiv').on('click','.cancel',function(){
//		//alert($(this).next().find('ul').empty());
//		//$(this).next().find('ul').empty();
//		$('dis_option').empty();
//	});
	
	$('#myDiv').on('change','.dis_select',function(){
		var select = $('.dis_select').val();
		switch(select){
			case 1: $('dis_option').remove();
					break;
			case 2: $('dis_option').empty();
				break;
			case 3: $('dis_option').empty();
				break;
		}
	});
})
