<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no;charset=utf-8">
		<title></title>
		<link href="../css/index.css?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"css/index.css").lastModified()%>" rel="stylesheet" />
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
		<link href="../css/datepicker3.css" rel="stylesheet">
		<style type="text/css">
			@media screen and (max-width:1000px){
				.center{
				   width:100%;
				}
			}
		</style>
	</head>
	<body>
		<%@ include file="../common/commonHT.jsp"%>
	    <div class="center">
	    	<div>
	    		<form class="user_form" method="post" action="update.do">
	    		<div class="user_left">
	    			<div class="user_detail">
		    			<div class="user_img1">
		    				<img src="../img/user.jpg">
		    				<div class="user_detail1">
				    			<span class="user_span1">用户名：【<%=request.getRemoteUser() %>】</span>
			    				<shiro:hasAnyRoles name="14">
			    				<span class="user_span1">余&nbsp;额:￥<b class="user_balance">${usefulBalance}</b></span>
			    				<span class="money_detail">明细</span>
			    				</shiro:hasAnyRoles> 
				    		</div>
				    		<div class="clear"></div>
		    			</div>
		    			<div class="user_img">
		    				<div>
		    					<span class="user_span">姓　名</span>
		    					<input type="text" name="name"  value="${name}"/>
		    				</div>
		    				<div>
		    					<span class="user_span">手机号</span>
		    					<input type="text"  name="mobile" value="${mobile}" maxlength="11" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
		    				</div>
		    				<div>
		    					<span class="user_span">密　码</span>
			    				<span class="translate_password">修改</span></br>
		    				</div>
			    			<div class="old_password">
			    				<span class="user_span">原密码</span>
			    				<input name="oldPwd" class="form-control" type="password"  value=""/>
			    				<span class="password_sure">确认</span>
			    			</div>
			    			<div class="new_password">
			    				<span class="user_span">新密码</span>
			    				<input type="password"  value=""/>
			    			</div>
			    			<div class="new_password">
			    				<span class="user_span">确　认</span>
			    				<input type="password" name="newPwd" value=""/>
			    			</div>
		    			</div>
		    			<div class="clear"></div>
		    		</div>
		    		<div style="font-weight: 100; color: #666; padding-left:10px;">
		    		<span style="color:red">【注意】</span></br>
						1.看到余额为前一天的余额，可点击【明细】查询余额明细构成</br>
						2.余额负数表示有欠货款，正数表示有可使用的余额</br>
						3.由于线下转账需要财务确认收款后进行入账，余额更新会有1天延迟；如有疑问可联系总部财务核对</br>
						4.为了保证账户余额的准确性，转账时请务必注明付款客户信息，如【北京张三货款】</br>
		    		</div>
	    		</div>
	    		
	    		<div class="user_left1">
		    		<div>
		    			<span class="user_span">柜台</span>
	    				<span>${counters}</span>
		    		</div>
		    		<div>
		    			<span class="user_span">角色&nbsp;</span>
			    		<span>${roles}</span>
		    		</div>	
	    		</div>	
	    		<div class="clear"></div>
	    		<div class="detail_body">
	    			<div class="begin_div">
	    				<div class="begin">
    						<span class="begin"><b>起始时间:</b></span>
			          <div class="begin">
			             <input type="text" class="form-control pull-right" id="qBeginTime">
			          </div>
	    				</div>
		          <div class="begin">
    						<span class="begin"><b>结束时间:</b></span>
		            <div class="begin">
		               <input type="text" class="form-control pull-right" id="qEndTime">
		            </div>
	    				</div>
	            <div class="begin">
	               <input class="bg-red add_button begin_button" type="button" value="查询">
	            </div>
		          <div class="clear"></div>
		        </div>
	    			<table class="detail_table" border="1" cellspacing="0">
	    				<thead>
		    				<tr>
		    					<th>序列</th>
		    					<th>单号</th>
		    					<th>日期</th>
		    					<th>单据类型</th>
		    					<th>金额</th>
		    					<th>摘要</th>
		    				</tr>
	    				</thead>
	    				<tbody>
	    				</tbody>
	    			</table>
	    			<div class='searchn'></div>
	    		</div>
	    		<div class="user_sure">
   					<input class="bg-red add_button" style="margin:25px;text-align:center" type="button" value="确定" onclick="this.form.submit()"/>
    				<input class="bg-red add_button" style="margin:25px;text-align:center" id="logout"  type="button" value="退出"  />
   				</div>
		    	
	    		</form>
	    	</div>
		</div>
		<%@ include file="common/commonF.jsp"%>
		<script src="../js/jquery.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/jquery.js").lastModified() %>"></script>
	 	<script src="../js/bootstrap-datepicker.js"></script>
		<script src="../js/index.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/index.js").lastModified()%>"></script>
				<script>
		    $(document).ready(function(){
		    $(".user_balance").text(Number($(".user_balance").text()).toFixed(2));
		    $('.old_password').css('display','none');
				$('.new_password').css('display','none');
				$('.translate').click(function(){
				    var s=$(this).parent().find('input');	
				    s.attr("readonly",false);
				    s.css({'background':'white','border':'1px solid #828488'});
				});
		 		$('#logout').click(function(){
					window.location.href='../logout.do';
				}) ;
				$('.translate_password').click(function(){
					$('.old_password').css('display','block');
					$('.old_password input').css({'background':'white','border':'1px solid #828488'});
				})
				$('.password_sure').click(function(){
					var pwd = $(this).prev().val();
					if(pwd==""||pwd==null){
						$(".message_block2").html("密码不能为空！");
				 	 	$(".message_body").css("display","block");
						return;
					}
				 	$.ajax({
						type:"POST",
						url:"../user/checkpass.do",
						data:{oldpass:pwd},
						async:false,
						success:function(data){
							if(data=="success"){
								$('.new_password').css('display','block');
								$('.new_password input').css({'background':'white','border':'1px solid #828488'});
							}
							else{
								$(".message_block2").html("密码错误！");
						 	 	$(".message_body").css("display","block");
							}
						}
					}); 
				})
				$(".money_detail").click(function(){
					$(".message_block2").html($(".detail_body").html());
					$(".message_top").text("账单明细");
			 	 	$(".message_body").css("display","block");
			 	 	$(".detail_table tbody").empty();
			 	 	$(".searchn").empty();
			 	 	var mydate = new Date();
			 	 	var yy=mydate.getFullYear();
			 	 	var mm=mydate.getMonth()+1;
			 	 	if(mm<10){
			 	 		$("#qBeginTime,#qEndTime").val(yy+"-0"+mm);
			 	 	}else{
			 	 		$("#qBeginTime,#qEndTime").val(yy+"-"+mm);
			 	 	}
			 	 	$('#qBeginTime').datepicker({  
				    	format: 'yyyy-mm',
					    startView: 'months',
					    maxViewMode:'years',
					    minViewMode:'months',
			        todayBtn : "linked",  
			        autoclose : true,  
			        todayHighlight : true, 
			        startDate:new Date(2016,12),
			        endDate : new Date()  
			    }).on('changeDate',function(e){  
			        var startTime = e.date;  
			        $('#qEndTime').datepicker('setStartDate',startTime);  
			    });  
			    //结束时间：  
			    $('#qEndTime').datepicker({ 
				    	format: 'yyyy-mm',
					    startView: 'months',
					    maxViewMode:'years',
					    minViewMode:'months',
			        todayBtn : "linked",  
			        autoclose : true,  
			        todayHighlight : true,  
			        endDate : new Date()  
			    }).on('changeDate',function(e){  
			        var endTime = e.date;  
			        $('#qBeginTime').datepicker('setEndDate',endTime);  
			    }); 	
			 	 	$(".begin_button").click(function(){
			 	 		var qBeginTime=$("#qBeginTime").val();
			 	 		var qEndTime=$("#qEndTime").val();
			 	 		var tbody=$(".detail_table tbody");
			 	 		tbody.empty();
			 	 		$.ajax({
							type:"POST",
							url:"../user/viewBlanceDetail.do",
							data:{startTime:qBeginTime,endTime:qEndTime},
							async:false,
							success:function(data){
				 	 			$.each(data.data, function(i,detail){
				 	 				$('.message_block2 .detail_table tbody').append(
				 	 						"<tr><td class='serial'></td>"	
				    					+"<td class='fNumber'>"+detail.fNumber+"</td>"	
					    				+"<td class='time'>"+detail.fDate+"</td>"	
					    				+"<td>"+detail.fType+"</td>"	
					    				+"<td class='amount1'>"+detail.amount+"</td>"	
					    				+"<td>"+detail.fExplanation+"</td></tr>")
				 				});
				 	 			$(".serial").each(function(){
						 	 		$(this).text($(this).index(".serial")+1);
						 	 		var time=$(this).parent().find(".time");
						 	 		time.text(time.text().substr(0,10));
						 	 		if($(this).parent().find(".fNumber").text()=='null'){
						 	 			$(this).parent().find(".fNumber").text('');
						 	 		};
						 	 		if($(this).parent().find(".fExplanation").text()=='null'){
						 	 			$(this).parent().find(".fExplanation").text('');
						 	 		};
						 	 		var amount1=Number($(this).parent().find("amount1").text());
						 	 		$(this).parent().find("amount1").text(amount1.toFixed(2));
						 	 	});
				 	 			var serl=$(".serial").length;
				 	 			$(".searchn").text("查询到"+serl+"条数据");
							},
							error:function(error){
								$(".message_block2 .searchn").text("查询数据出错");
						   }
						});
			 	 	})
				})
		    })
	</script>
	<!-- baidu bridge  -->
	<script>
	var _hmt = _hmt || [];
	(function() {
	  var hm = document.createElement("script");
	  hm.src = "https://hm.baidu.com/hm.js?ffee89c382763b7bb90900c659ca5017";
	  var s = document.getElementsByTagName("script")[0]; 
	  s.parentNode.insertBefore(hm, s);
	})();
	</script>	
	</body>
</html>
