<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible"content="IE=9; IE=8; IE=7; IE=EDGE">
		<title></title>
		<link rel="stylesheet" href="../css/bootstrap.min.css">
		<!-- Ionicons -->
		<link rel="stylesheet" href="../css/ionicons.min.css">
		<!-- DataTables -->
		<link rel="stylesheet" href="../css/dataTables.bootstrap.css">
	</head>
	<script type="text/javascript"></script>
	<body>
		<div class="row">
			<div class="col-sm-6"></div>
			<div class="col-sm-6">
					<!-- <input id="btn_seacher" type="button" value="搜索" class="search" href="../reserveproduct/search"/> -->				
					<!-- <input id="txt_seacher" type="text" value="" class="ip_text" name="productName" /> -->
			</div>
		</div>
		<div id="div1" class="tab">
			<table id="table_position" class="table table-bordered table-striped">
				<thead>
					<tr>
						<th>ID</th>
						<th>产品名称</th>
						<th>编辑</th>
						<th>打欠总量</th>
						<th>打欠起始时间</th>
						<th>打欠截止时间</th>
						<th>还欠起始时间</th>
						<th>还欠截止之间</th>
						<th>还欠完毕标记</th>
						<th>当前状态</th>
						<th>最后更新时间</th>
						<th>创建人</th>
						<th>创建日期</th>
					</tr>
					
				</thead>
				<tbody>
				<c:forEach items="${rpList}" var="rp" varStatus="s">
				<tr>
					<td id="parameter"><c:out value="${rp.productId}"></c:out></td>
					<td><c:out value="${rp.productName}"></c:out></td>
					<td>
						<a class="prp_amend" href="../reserveproduct/updateshow.do?productId=${rp.productId}" target="_blank">修改</a>
						<!-- <span class="set_del">删除</span> -->
					</td>
					<td><c:out value="${rp.totalReserve}"></c:out></td>
					<td><fmt:formatDate value='${rp.reserveStart}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
					<td><fmt:formatDate value='${rp.reserveEnd}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
					<td><fmt:formatDate value='${rp.excuteStart}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
					<td><fmt:formatDate value='${rp.excuteEnd}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
					<td>
					<c:if test="${rp.status==false}">
				 		已完成
				 	</c:if>
				 	<c:if test="${rp.status==true}">
				 		未完成
				 	</c:if>
					<td>
						<c:if test="${rp.avalible==false}">
				 			<span class="set_start" href="../reserveproduct/enable">禁用</span>
				 			<span class="set_down" href="../reserveproduct/disable" hidden="hidden">启用</span>
				 		</c:if>
				 		<c:if test="${rp.avalible==true}">
				 			<span class="set_start" href="../reserveproduct/enable" hidden="hidden">禁用</span>
				 			<span class="set_down" href="../reserveproduct/disable">启用</span>
				 		</c:if>
					</td>
					<td><fmt:formatDate value='${rp.lastUpdate}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
					<td><c:out value="${rp.creator}"></c:out></td> 
					<td>	<fmt:formatDate value='${rp.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/></td>
				</tr>
				</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<th>ID</th>
						<th>产品名称</th>
						<th>编辑</th>
						<th>打欠总量</th>
						<th>打欠起始时间</th>
						<th>打欠截止时间</th>
						<th>还欠起始时间</th>
						<th>还欠截止之间</th>
						<th>还欠完毕标记</th>
						<th>当前状态</th>
						<th>最后更新时间</th>
						<th>创建人</th>
						<th>创建日期</th>
					</tr>
				</tfoot>
			</table>			
		</div>
		<script src="../js/jquery-2.2.3.min.js"></script>
		<!-- Bootstrap 3.3.6 -->
		<script src="../js/bootstrap.min.js"></script>
		<!-- DataTables -->
		<script src="../js/jquery.dataTables.min.js"></script>
		<script src="../js/dataTables.bootstrap.min.js"></script>
		<script>
			$(document).ready(function(){
				
			/* 	$("#refresh").on("click",function(){
					window.location.reload();
				}) */
				
			 $("#table_position").DataTable();
			    $("#table_position_filter").append("<a href='../reserveproduct/add.do' target='_blank' >"
			    	+"<input type='button' value='创建' class='ip btn-primary' ></a>");
			   $("#btn_seacher").click(function() {
			    	var txt_seacher=$("#txt_seacher").val();
			    	var myDiv=$("#myDiv");
			    	myDiv.empty();
						$.ajax({
							type:"post",
							url:"../reserveproduct/search.do",
							async:true,
							data:{name:txt_seacher}, 
							success:function(data){
								myDiv.html(data);
							}
						})
					}); 
			/* 	$("#myDiv").empty();
				htmlobj=$.ajax({url:url,async:false});
			    $("#myDiv").html(htmlobj.responseText); */
			 });
		</script>
	</body>
</html>