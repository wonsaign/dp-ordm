$(document).ready(function(){
//	<%-- 用户创建修改  组织级联组织按钮--%>
	$('#org_li').on('change','.user_select', function() {
		var orgId = $(this).val();
		var position = $(this);
		var src = $('[name="org_cascade_url"]').attr('href');
		var num = $('.user_select').size();
		var selindex = $(this).index();
		position.next(".user_select").remove();
		if (orgId != 0) {
			$.ajax({
					type: "post",
					url: src + ".do",
					data: {orgId: orgId},
					dataType: "json",
					async: true,
					success: function(data) {
						if(data!=null){
							var htmlsel="<select class='user_select' name='orgUnit"+num+"'></select>"
							position.parent().append(htmlsel);
							position.parent().find('select[name="orgUnit' + num + '"]').append("<option value='0'>请选择</option>");
							$.each(data, function(index, value) {
								position.parent().find('select[name="orgUnit' + num + '"]').append("<option value='" + value.orgId + "'> " + value.commonName + "</option>");
							});
							position.prev(".user_select").attr("disabled","disabled");
						}else{	
							position.prev(".user_select").attr("disabled","disabled");
							position.prev().removeAttr("disabled");
						}
					}
			});
		}else {
			position.prev().removeAttr("disabled");
		}
	});

//	<%--用户创建修改  组织级联角色按钮--%>
	$('[name="Role_cascade"]').on('click',function(){
		var src=$(this).attr('href');
		var orgId=0;
		var lastsel = $("#org_li select").last();
//		<%--最后一个是否选择--%>
		if(lastsel.val()!=0){
			orgId=lastsel.val();
		}
//		<%--最后一级未选择 且最后一个不是第一个时 获取倒数第二个 --%>
		if(lastsel.val()==0&&$('.user_select').size()>1){
			orgId=lastsel.prev().val();
		}
		
//		<%--把orgId放到隐藏标签中--%>
//		alert(orgId);
		$("#orgid").val(orgId);	
//		<%--把之前选的角色放到input的值清空--%>
		$("#roleset").val("");
		if(orgId!=0){
			$.ajax({
				type:"post",
				url:src+".do",
				data:{orgId:orgId},
				dataType: "json",
				async:true,
				success: function(data) {
//				<%--先删除再加--%>
				$('[name="roles_ul"]').empty();
					$.each(data,function(index, value){
						var htmlsel="<input type='checkbox' name='rid' id='"+value.rid+"' value='"+value.rid+"'><label for='"+value.rid+"'>"+value.commonName+"</label>&nbsp;"
						$('[name="roles_ul"]').append(htmlsel);
					}); 
				}
			});
		}
	});
	
//	<%--用户  选角色--%>
	$('roles_ul').on('change','[type="checkbox"]',function(){
		var position=$(this);
		var roles =$("#roleset").val();
		var value=position.val();
		if(position.attr('checked')=="checked"){
			if(roles.length>0){
				if(roles.indexOf(value)<0){
					roles=roles+","+value;
				} 
			}else{
				roles=roles+value;
			}
		}else{
			value=","+value;
			if(roles.indexOf(value)>0){
				roles=roles.replace(value,"");
			}else{
				roles=value=value.substring(1);
				roles.replace(value,"");
			}
		}
		$("#roleset").val(roles);
	});
//	<%--用户  启用--%>
	$('#myDiv').on('click','.set_start',function(){
		var src=$(this).attr('href');
		parameter=$(this).parent().siblings("#parameter").text();
		var position=$(this);
		var status=$(this).parent().siblings(".status");
		$.ajax({
			type:"post",
			url:src+".do",
			data:{parameter:parameter},
			async:true,
			success: function(data) {
				position.hide();
				position.siblings('.set_down').show();
				status.text("启用");
			}
		});
		
	});
//	<%--用户  禁用--%>
	$('#myDiv').on('click','.set_down',function(){
		var src=$(this).attr('href');
		parameter=$(this).parent().siblings("#parameter").text();
		var position=$(this);
		var status=$(this).parent().siblings(".status");
		$.ajax({
			type:"post",
			url:src+".do",
			data:{parameter:parameter},
			async:true,
			success: function(data) {
				position.hide();
				position.siblings('.set_start').show();
				status.text("禁用");
			}
		});
	});
	
//	<%--用户修改 修改组织按钮--%>
	$('#org_change').on('click',function(){
		var position=$(this);
		var src=$(this).attr('href');
		$(".user_select").removeAttr("disabled");
		$(this).attr("disabled","disabled");
		$(this).parent().find('select [name="user_select"]').children().remove();
		var orgId=$('[name="orgUnit0"]').find("option:selected").val();
		$.ajax({
			type:"post",
			url:src+".do",
			data:{orgId:orgId},
			dataType: "json",
			async:true,
			success: function(data) {
				if(data!=null){
					position.parent().find('select[name="orgUnit0"]').remove();
//					<%--添加--%>
					var htmlsel="<select class='user_select' name='orgUnit0'></select>"
					position.parent().append(htmlsel);
					position.parent().find('select[name="orgUnit0"]').append("<option value='0'>请选择</option>");
					$.each(data,function(index, value){
						position.parent().find('select[name="orgUnit0"]').append("<option value='"+value.orgId+"'> "+value.commonName+"</option>");
					});
				}
			}
		});
	});
});