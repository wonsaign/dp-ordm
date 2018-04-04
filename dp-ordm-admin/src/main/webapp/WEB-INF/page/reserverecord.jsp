<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title></title>
		<link rel="stylesheet" href="../css/bootstrap.min.css">
		<!-- Ionicons -->
		<link rel="stylesheet" href="../css/ionicons.min.css">
		<!-- DataTables -->
		<link rel="stylesheet" href="../css/dataTables.bootstrap.css">
	</head>
	<body>
		<div class="row">
			<div class="col-sm-12">
				<span style="float:left;margin-left:30px;font-weight:bold;">客户：</span>
				<input id="customerName" type="text" value="${customerName}" class="ip_text" style="float:left"/>
				<span style="float:left;margin-left:30px;font-weight:bold;">门店：</span>
				<input id="counterName" type="text" value="${counterName}" class="ip_text" style="float:left"/>
				<span style="float:left;margin-left:30px;font-weight:bold;">订单号：</span>
				<input id="orderNo" type="text" value="${orderNo}" class="ip_text" style="float:left"/>
				<input id="btn_seacher" type="button" value="查询" class="search" style="float:left"/>			
			</div>
		</div>
		<br>
		<div>
			<table id="table_position" class="table table-bordered table-striped">
				<thead>
					<tr name="table_head">
						<th>订单号</th>
						<th>客户</th>
						<th>柜台号</th>
						<th>门店名称</th>
						<th>产品编码</th>
						<th>产品名称 </th>
						<th>打欠数量</th>
						<th>状态</th>
						<th>还欠单号</th>
					</tr>
				</thead>
				<tbody>
 				<c:forEach items="${recordList}" var="rc" varStatus="s">
				<tr>
					<td id="parameter"><c:out value="${rc.orderNo}"></c:out></td>
					<td><c:out value="${rc.customerName}"></c:out></td>
					<td><c:out value="${rc.counterCode}"></c:out></td>
					<td><c:out value="${rc.counterName}"></c:out></td>
					<td><c:out value="${rc.productCode}"></c:out></td>
					<td><c:out value="${rc.productName}"></c:out></td>
					<td><c:out value="${rc.quantity}"></c:out></td>
					<td>
						<c:if test="${rc.status==2}">
					 		打欠
					 	</c:if>
					 	<c:if test="${rc.status==3}">
					 		还欠中
					 	</c:if>
					 	<c:if test="${rc.status==4}">
					 		已还欠
					 	</c:if>
					 	<c:if test="${rc.status==5}">
					 		取消还欠
					 	</c:if>
				 	</td>
					<td><c:out value="${rp.excuteOrderNo}"></c:out></td>
				</tr>
				</c:forEach>
				</tbody>
				<tfoot>
					<tr  name="table_head">
						<th>订单号</th>
						<th>客户</th>
						<th>柜台号</th>
						<th>门店名称</th>
						<th>产品编码</th>
						<th>产品名称 </th>
						<th>打欠数量</th>
						<th>状态</th>
						<th>还欠单号</th>
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
			$(document).ready(function() {
				$("#table_position").DataTable();
				$("#btn_seacher").click(function() {
					var customerName = $.trim($("#customerName").val());
					var counterName = $.trim($("#counterName").val());
					var orderNo = $.trim($("#orderNo").val());
					var myDiv = $("#myDiv");
					myDiv.empty();
					$.ajax({
						type : "post",
						url : "../reserveproduct/searchrecord.do",
						async : true,
						data : {
							customerName : customerName,
							counterName : counterName,
							orderNo : orderNo
						},
						success : function(data) {
							myDiv.html(data);
						}
					})
				});
			});
		</script>
	</body>
</html>

    