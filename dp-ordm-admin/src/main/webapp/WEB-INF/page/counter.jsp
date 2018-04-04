<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
		<style>
			table{
				border: 1px solid black;
				width: 90%;
				margin: 20px auto;
			}
		</style>
	</head>
	<body>
<!-- 		<div class="button_div"> -->
<!-- 			<div class="button_block"> -->
<!-- 				<input type="submit" value="搜索" class="search" href="../base/searchcounter"/> -->
<!-- 				<input name="name" type="text" value="" class="ip_text"/>		 -->
<!-- 			</div> -->
<!-- 		</div> -->
		<div>
<!-- 			<table name="table_position" > -->
<!-- 				<tr name="table_head"> -->
			<table id="table_position" class="table table-bordered table-striped">
				<thead>
					<tr>
					<th>柜台号</th>
					<th>柜台名</th>
					<th>客户</th>
					<th>联系人</th>
					<th>手机</th>
					<th>门店地址</th>
					<th>门店类型</th>
					<th>状态</th>
					<th>修改时间</th>
					</tr>
				 </thead>
				 <tbody>
					<c:forEach items="${CounterBeanlist}" var="cbl">
					<tr>
						<td>${cbl.counterCode}</td>
						<td>${cbl.counterName}</td>
						<td>${cbl.customerName}</td>
						<td>${cbl.contact}</td>
						<td>${cbl.mobile}</td>
						<td>${cbl.address}</td>
						<td>${cbl.type}</td>
						<td>${cbl.status}</td>
						<td>${cbl.lastUpdate}</td>
					</tr>
					</c:forEach>
				</tbody>
				<tfoot>
					<tr>
					<th>柜台号</th>
					<th>柜台名</th>
					<th>客户</th>
					<th>联系人</th>
					<th>手机</th>
					<th>门店地址</th>
					<th>门店类型</th>
					<th>状态</th>
					<th>修改时间</th>
					</tr>
				</tfoot>
			</table>
		</div>
<!-- 		<div class="pagination_div"> -->
<!-- 			<a hidden="hidden" name="paginationURL">../base/counterpage</a> -->
<%-- 			第<a  name="page">${page}</a>页 --%>
<%-- 			共<a  name="max">${max}</a>页 --%>
<%-- 			<a hidden="hidden" name="key">${key}</a> --%>
<%-- 			跳到<input type="text" name="jumppage" value="${page}"> --%>
<!-- 			<input class="page_btn" type="button" name="jump" value="跳转"> -->
<!-- 			<input class="page_btn" type="button" name="firstpage" value="首页"> -->
<!-- 			<input class="page_btn" type="button" name="prevpage" value="上一页"> -->
<!-- 			<input class="page_btn" type="button" name="nextpage" value="下一页"> -->
<!-- 			<input class="page_btn" type="button" name="lastpage" value="尾页"> -->
<!-- 		</div> -->
	<script src="../js/jquery-2.2.3.min.js"></script>
		<!-- Bootstrap 3.3.6 -->
		<script src="../js/bootstrap.min.js"></script>
		<!-- DataTables -->
		<script src="../js/jquery.dataTables.min.js"></script>
		<script src="../js/dataTables.bootstrap.min.js"></script>
		<script>
		 $(document).ready(function(){
		    $("#table_position").DataTable();
		 })
		</script>
	</body>
</html>
