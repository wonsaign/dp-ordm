$(document).ready(function(){
//	<%--产品关联策略 添加商品 ajax动态更新搜索下拉框--%>
	/*$('[name="productName"]').on('keyup paste change',function(){
		var ul = $(this).parent().find('.gro_ul');
		var pname = $(this).val();
		ul.empty();
		$.ajax({
			type:"post",
			url:"../productpolicy/getproductseries.do",
			data:{pName:pname},
			async:true,
			success: function(data) {
				$.each(data,function(i,product){
					ul.append("<li value='"+product.id+"'>"+product.name+"</li>");
				});
			}
		});
	});*/
	$('.gro_div').on('keyup','.productName',function(){
		$(this).parent().find(".gro_ul").show();
        var index = $.trim($(this).val().toString()); // 去掉两头空格
        if(index != ''){ // 如果搜索框输入为空
        	$(this).parent().find(".gro_ul li").addClass("on");
        	$(this).parent().find(".gro_ul li:contains('"+index+"')").removeClass("on");
        }
    });
	$('.gro_div').on('click','.gro_ul li',function(){
		$(this).parent().parent().find(".pid").val($(this).find("input").val());
		$(this).parent().parent().find(".productName").val($(this).text());
		$(this).parent().hide();
	});
//	<%--产品关联策略 添加商品下拉框 显示--%>
	/*$('[name="productName"]').on('focus',function(){
		$(this).parent().find('.gro_ul').show();
	});
//	<%--产品关联策略 添加商品 下拉框 li点击--%>
	$('.gro_ul').on('click','li',function(){
		var pid = $(this).val();
		var pName = $(this).text();
		$(this).parent().parent().find('[name="productName"]').val(pName);
		$(this).parent().parent().find('[name="pId"]').val(pid);
		$(this).parent().hide();
	});*/
	
//	<%--产品关联策略 添加物料 ajax动态更新搜索下拉框--%>
	/*$('.prp_m').on('keyup paste change',function(){
		var ul = $(this).parent().find(".gro_ul");
		var pname = $(this).val();
		ul.empty();
		$.ajax({
			type:"post",
			url:"../productpolicy/getmateriel.do",
			data:{pName:pname},
			async:true,
			success: function(data) {
				$.each(data,function(i,product){
					ul.append("<li value='"+product.productId+"'>"+product.name+"</li>");
				});
			}
		});
	});
//	<%--产品关联策略 添加物料下拉框 显示--%>
	$('.prp_m').on('focus',function(){
		$(this).parent().find(".gro_ul").show();
	});
//	<%--产品关联策略 添加物料 下拉框 li点击--%>
	$('.gro_ul').on('click','li',function(){
		var pid = $(this).val();
		var text = $(this).text();
		$(this).parent().parent().find("input[class='pid']").val(pid);
		$(this).parent().parent().find("input[class='prp_m mat']").val(text);
		$(this).parent().hide();
	});*/
//	<%--产品关联策略 添加 关联产品 物料--%>
	$('[name="addmateriel"]').on('click',function(){
		var position=$(this);
		var pid=$(this).prev().find(".pid").val();
		var pname=$(this).prev().find(".mat").val();
		var nextdiv=$(this).parent().next();
		if(pid!=""&&pname!=""){
			nextdiv.find('[name="materiel_ul"]').show();
			var htmlli="<div name='materiel' style='height:23px'><input class='pid' type='hidden' value="+pid+"><span>"
				+pname+":</span><input class='coeff' type='text'><span name='delmateriel' style='color:red;cursor:pointer;'>删除</span>"
				+"<a class='msg'></a></div>"
			nextdiv.find('[name="materiel_ul"]').append(htmlli);
		}
	});
//	<%--产品关联策略 删除 关联产品 物料--%>
	$('[name="materiel_ul"]').on("click","span[name='delmateriel']",function(){
		var position=$(this);
		position.parent().remove();	
	});
//	<%--产品关联策略 拼接Set<AssociatedProduct> 提交表单--%>
	$('[name="createPRP"]').on('click',function(){
		var position=$(this);
		var prpset="["
		$('[name="materiel_ul"]').children().each(function(){
		    var pid=$(this).children('.pid').val();
		    var coeff=$(this).children('.coeff').val();
		    prpset+="{\"pid\":"+pid+",\"coeff\":"+coeff+"},";
		  });;
		  if(prpset.charAt(prpset.length-1)==","){
			  prpset=prpset.substring(0, prpset.length-1);
		  }
		  prpset+="]";
		  $('[name="prpset"]').val(prpset);
		  
		  var flag=true;
		  //校验提交
		  var errormsg="";
		  position.parent().parent().find('.prp_t').each(function(i,product){
			 var val=$(this).val();
			 var msg=$(this).parent().find('.msg').text();
			 if(val==""){
				 errormsg+=$(this).parent().text().trim()+"不能为空  ";
				 flag=false;/[\r\n]/g
			 }else if(msg!=""){
				 errormsg+=$(this).parent().text().trim()+" ";
				 flag=false;
			 }
		  });
		  position.parent().parent().find('.coeff').each(function(i,product){
			 var val=$(this).val();
			 var msg=$(this).parent().find('.msg').text();
			 if(val==""){
				 errormsg+=$(this).prev().text().trim()+"不能为空  ";
				 flag=false;
			 }else if(msg!=""){
				 errormsg+=$(this).prev().text().trim()+" "+msg.trim()+" ";
				 flag=false;
			 }
		  });
		  
		  if(flag){
			 $('form').submit();
		  }else{
			  alert(errormsg.replace(/[\r\n]/g,""));
		  }
	});
//	<%--产品关联策略  启用--%>
	$('#myDiv').on('click','.prp_start',function(){
		var src=$(this).attr('href');
		var position=$(this);
		parameter=$(this).parent().siblings('[name="policyId"]').text();
		var status=$(this).parent().siblings(".status");
		$.ajax({
			type:"post",
			url:src+".do",
			data:{parameter:parameter},
			async:true,
			success: function(data) {
				position.hide();
				position.siblings('.prp_down').show();
				status.text("启用");
			}
		});
	});
//	<%--产品关联策略  禁用--%>
	
	$('#myDiv').on('click','.prp_down',function(){
		var src=$(this).attr('href');
		var position=$(this);
		parameter=$(this).parent().siblings('[name="policyId"]').text();
		var status=$(this).parent().siblings(".status");
		$.ajax({
			type:"post",
			url:src+".do",
			data:{parameter:parameter},
			async:true,
			success: function(data) {
				position.hide();
				position.siblings('.prp_start').show();
				status.text("禁用");
			}
		});
	});
//	//校验空
//	$('.prp_t').on('blur',function(){
//		if($(this).val()==""){
//			$(this).parent().find('.msg').text("输入不能为空");
//			$(this).parent().find('.msg').css("color","red");
//			}else{
//				$(this).next('.msg').text("");
//			}
//	});
//	$('.prp_m').on('blur',function(){
//		if($(this).val()==""){
//			$(this).parent().find('.msg').text("输入不能为空");
//			$(this).parent().find('.msg').css("color","red");
//			}else{
//				$(this).next('.msg').text("");
//			}
//		$(this).next().hide();
//	});
//	$('.prp_t').on('blur',function(){
//		if($(this).val()==""){
//			$(this).parent().find('.msg').text("输入不能为空");
//			$(this).parent().find('.msg').css("color","red");
//			}else{
//				$(this).next('.msg').text("");
//			}
//		$(this).next().hide();
//	});
	$('.gro_text').on('blur',function(){
		if($(this).val()==""){
			$(this).parent().find('.msg').text("输入不能为空");
			$(this).parent().find('.msg').css("color","red");
			}else{
				$(this).next('.msg').text("");
			}
		$(this).parent().find(".gro_ul").hide();
	});
	//校验数字
	$('[name="level"]').on('blur',function(){
		var reg = new RegExp("^[0-9]+.?[0-9]*$"); 
		if(!reg.test($(this).val())){
			$(this).parent().find('.msg').text("请输入数字");
			$(this).parent().find('.msg').css("color","red");
		}else{
			$(this).next('.msg').text("");
		}
	});
//	$('[name="minOrderUnit"]').on('blur',function(){
	$('div_body').on('blur','[name="minOrderUnit"]',function(){
		alert(111);
		var reg = new RegExp("^[0-9]+.?[0-9]*$"); 
		if(!reg.test($(this).val())){
			$(this).parent().find('.msg').text("请输入数字");
			$(this).parent().find('.msg').css("color","red");
		}else{
			$(this).next('.msg').text("");
		}
	});
	$('.coeff').on('blur',function(){
		var reg=new RegExp("^[0-9]+.?[0-9]*$");
		var val=$(this).val();
		if(val==""){
			$(this).parent().find('.msg').text("输入不能为空");
			$(this).parent().find('.msg').css("color","red");
			}else{
				if(!reg.test(val)){
					$(this).parent().find('.msg').text("请输入数字");
					$(this).parent().find('.msg').css("color","red");
				}else {
					$(this).parent().find('.msg').text("");
				}
			}
	});
	$('[name="name"]').on('blur',function(){
		var name = $(this).val();
		var position=$(this);
		$.ajax({
			type:"post",
			url:"../productpolicy/checkName.do",
			data:{name:name},
			async:true,
			success: function(data) {
				if(data){
					position.parent().find('.msg').text("策略名已存在");
					position.parent().find('.msg').css("color","red");
				}else{
					position.parent().find('.msg').text("");
				}
			}
		});
	});
});