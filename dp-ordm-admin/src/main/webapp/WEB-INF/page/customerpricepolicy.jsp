<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
				<table id="table_position" class="table table-bordered table-striped">
				<thead>
					<tr>
						<th>ID</th>
						<th>编辑</th>
						<th>客户类型</th>
						<th>折扣点数</th>
						<th>状态</th>
						<th>描述</th>
						<th>操作时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${PricePolicyBeanList}" var="ppb" varStatus="vs">
					<tr>
						<td>${ppb.id}</td>
						<td>
							<a class="prp_amend" href="../pricePolicy/updatebtn.do?typeId=${ppb.typeId}" target="_blank">修改</a>
	<!-- 				 		<span class="set_start">启用</span> -->
	<!-- 				 		<span class="set_down">禁用</span> -->
							</td>
						<td>${ppb.customerType}</td>
						<td>${ppb.discount}</td>
						<td>${ppb.status}</td>
						<td>${ppb.description}</td>
						<td>${ppb.lastUpdate}</td>
					</tr>
				</c:forEach>
				</tbody>
				 <tfoot>
				 	<tr>
						<th>ID</th>
						<th>编辑</th>
						<th>客户类型</th>
						<th>折扣点数</th>
						<th>状态</th>
						<th>描述</th>
						<th>操作时间</th>
					</tr>
				 </tfoot>
			</table>
			<script src="../js/jquery-2.2.3.min.js"></script>
			<!-- Bootstrap 3.3.6 -->
			<script src="../js/bootstrap.min.js"></script>
			<!-- DataTables -->
			<script src="../js/jquery.dataTables.min.js"></script>
			<script src="../js/dataTables.bootstrap.min.js"></script>
	</body>
</html>