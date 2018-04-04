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
<title>库存</title>
<link href="../css/index.css" rel="stylesheet">
<style type="text/css">
.grotd{
	text-align: right;
	padding-right:20px;
}
.th3{
	white-space:nowrap;
}
</style>
</head>
<body>
<%@ include file="../common/commonHT.jsp"%>
<input name="stockId" type="hidden" value="${stockId}">
<div class="stock_div">
		<div class="tab-content">
			<!-- <ul id="topTab" class="topnav">
				<li class="active">
					<a href="#bage1" data-toggle="tab" id="11386">正品</a>
				</li>
				<li>
					<a href="#bage2" data-toggle="tab" id="11388">赠品</a>
				</li>
				<li>
					<a href="#bage3" data-toggle="tab" id="11389">物料</a>
				</li>
				<div class="clear"></div>
			</ul> -->
			<table id="topTab" class="topnav" border="1"  cellspacing="0" cellpadding="0">
				<tr>
					<td class="active"><a href="#bage1" data-toggle="tab" id="11386">正品</a></td>
					<td><a href="#bage2" data-toggle="tab" id="11388">赠品</a></td>
					<td><a href="#bage3" data-toggle="tab" id="11389">物料</a></td>
				</tr>
			</table>
			<div class="tab-head"></div>
			<div class="tab-pane fade active in" id="bage">
					<table class="stock_table" border="0" cellspacing="0">
								<thead>
									<tr>
										<th class="grotd">产品名称</th>
										<th>规格</th>
										<th class='th3'>库存量</th>
									</tr>
								</thead>
								<tbody class="stock_tbody">
								</tbody>
					</table>
			</div>
		</div>	
	</div>
</body>
<script src="../js/jquery.js"></script>
<script src="../js/index.js"></script>
<script type="text/javascript">

function topTab(type){
	var stock_tbody=$(".stock_tbody");
	stock_tbody.empty();
	var stockId =10615;
	$.ajax({
		type : "post",
 		url : "../microservice/getall.do",
 		async : false,
 		timeout : 15000,
 		dataType:"json",
 		cache:false,
 		data:{stockId:stockId,type:type},
 	    success :function(data){
 	    	$.each(data,function(i,product){
				stock="<tr href="+product.pid+">"
						+"<td class='grotd'><img src='../img/active1.png' class='groimg' title="+product.joinAct+">"+product.name+"</td>"
						+"<td>"+product.spec+"</td>"
						+"<td class='th3'>"+product.qty+"</td></tr>";
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
});
</script>
</html>