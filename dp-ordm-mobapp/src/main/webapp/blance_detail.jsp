<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.zeusas.core.utils.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no;charset=utf-8">
<title>余额明细</title>
<link href="../css/index.css" rel="stylesheet">
<link href="../css/datepicker3.css" rel="stylesheet">
</head>
<body>
<%@ include file="../common/commonHT.jsp"%>
<div class="detail_body">
		<div class="begin_div">
			<div class="begin begin_width">
          <input type="text" class="form-control pull-right" id="qBeginTime">
			</div>
			<div class="begin" style="color:#cccccc">━━</div>
      <div class="begin begin_width">
           <input type="text" class="form-control pull-right" id="qEndTime">
			</div>
      <input class="bg-green begin_button" type="button" value="查询">
      <div class="clear"></div>
    </div>
		<div class="detail_table">
		</div>
		<div class='searchn'></div>
	</div>
</body>
<script src="../js/jquery.js"></script>
<script src="../js/bootstrap-datepicker.js"></script>
<script src="../js/bootstrap-datepicker.zh-CN.js"></script>
<script src="../js/index.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	 	$(".detail_table").empty();
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
	    language: "zh-CN",
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
	    language: "zh-CN",
	    todayBtn : "linked",  
	    autoclose : true,  
	    todayHighlight : true,  
	    endDate : new Date()  
}).on('changeDate',function(e){  
    var endTime = e.date;  
    $('#qBeginTime').datepicker('setEndDate',endTime);  
}); 	
	 	$(".begin_button").click(function(){
	 		$(".detail_table").empty();
	 		var qBeginTime=$("#qBeginTime").val();
	 		var qEndTime=$("#qEndTime").val();
	 		$.ajax({
			type:"POST",
			url:"../user/viewBlanceDetail.do",
			data:{startTime:qBeginTime,endTime:qEndTime},
			async:false,
			success:function(data){
 	 			$.each(data.data, function(i,detail){
 	 				$('.detail_table').append("<div class='blance_block'><div><div  class='orderNo blance_div '>单号:<span class='fNumber'>"+detail.fNumber+"</span></div>"
 	 						+"<div  class='col-red text-right blance_div amount1'>"+detail.amount+"</div></div><div>"
 	 					  +"<div  class='blance_div'><span class='col-333'>单据类型:</span><span class='col-666'>"+detail.fType+"</span></div>"
 	 					  +"<div  class='blance_div'><span class='col-333'>日期:</span><span class='col-666 time'>"+detail.fDate+"</span></div>"	
 	 					  +"</div><div class='clear'></div>"  	
 	 					  +"<div class='blance_text'><span class='col-333'>摘要:</span><span class='col-666 fExplanation'>"+detail.fExplanation
 	 					  +"</span></div></div>")
 				});
 	 			$(".blance_block").each(function(){
		 	 		var time=$(this).find(".time");
		 	 		time.text(time.text().substr(0,10));
		 	 		if($(this).find(".fNumber").text()=='null'){
		 	 			$(this).find(".fNumber").text('');
		 	 		};
		 	 		if($(this).find(".fExplanation").text()=='null'){
		 	 			$(this).find(".fExplanation").text('');
		 	 		};
		 	 		var amount1=Number($(this).find("amount1").text());
		 	 		$(this).find("amount1").text(amount1.toFixed(2));
		 	 	});
 	 			var serl=$(".blance_block").length;
 	 			$(".searchn").text("查询到"+serl+"条数据");
			},
			error:function(error){
				$(".message_block2 .searchn").text("查询数据出错");
		   }
		});
	 	});
	 	jQuery(".begin_button").click();
});
/* function topTab(type){
	var stock_tbody=$(".stock_tbody");
	stock_tbody.empty();
	var stockId =10615;
	alert(stockId+"+"+type);
	$.ajax({
		type : "post",
 		url : "../microservice/getall.do",
 		async : false,
 		timeout : 15000,
 		dataType:"json",
 		cache:false,
 		data:{stockId:stockId,type:type},
 	    success :function(data){
 	    	alert("111");
 	    	$.each(data,function(i,product){
				stock="<tr href="+product.pid+">"
						+"<td class='grotd'>"+product.name+"<img src='../img/top_imgy.png' class='groimg' title="+product.joinAct+"></td>"
						+"<td>"+product.spec+"</td>"
						+"<td>"+product.qty+"</td></tr>";
				stock_tbody.append(stock);
			});
 	    	$(".stock_tbody tr").each(function(){
 	    		if($(this).find(".groimg").attr("title") == "true"){
 	    			$(this).find(".groimg").show();
 	    		}
 	    	});
 	    	$(".stock_tbody tr").hover(function(){
 	    		$(this).css("background","rgb(195, 231, 177)");
 	    		$(this).siblings().css("background","white");
 	    	});
 	    	$(".stock_tbody tr").click(function(){
 	    		var trhref=$(this).attr("href");
 	    		location.href="../product/detail_"+trhref+".do";
 	    	});
 	    },
 	    error: function(){
 	    	
 	    }
 	    
	});
} 
$(document).ready(function(){
	var type="11386";
	$("#topTab td").eq(0).find("a").addClass("active in")
	 topTab(type);
	$("#topTab td").click(function(){
		var myid=$(this);
		myid.find("a").addClass("active in");
		myid.siblings().find("a").removeClass("active in");
		type=myid.find("a").attr("id");
		topTab(type);
	});
}); */
</script>
</html>