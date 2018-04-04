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
		<div class="upload">
				
		</div>
		<div>
			<table id="table_position" class="table table-bordered table-striped">
				<thead>
					<tr  name="table_head">
						<th>ID</th>
						<th>柜台编号</th>
						<th>柜台名称</th>
						<th>执行年月</th>
						<th>状态</th>
						<th>创建人</th>
						<th>创建时间</th>
						<th>最后更新时间</th>
					</tr>
				</thead>
				<tbody>
 				<c:forEach items="${presents}" var="pre">
					<tr  name="table_body">
						<td>${pre.id}</td>
						<td>${pre.counterCode}</td>
						<td>${pre.counterName}</td>
						<td>${pre.yearMonth}</td>
						<td>${pre.status}</td>
						<td>${pre.creator}</td>
						<td>${pre.createTime}</td>
						<td>${pre.lastUpdate}</td>
					</tr>
				</c:forEach>
				</tbody>
				<tfoot>
					<tr  name="table_head">
						<th>ID</th>
						<th>柜台编号</th>
						<th>柜台名称</th>
						<th>执行年月</th>
						<th>状态</th>
						<th>创建人</th>
						<th>创建时间</th>
						<th>最后更新时间</th>
					</tr>
				</tfoot>
			</table>
		</div>
		<%-- <div class="pagination_div">
			<a hidden="hidden" name="paginationURL">../monthpresent/presentpage</a>
			第<a  name="page">${page}</a>页
			共<a  name="max">${max}</a>页
			<a hidden="hidden" name="key">${key}</a>
			跳到<input type="text" name="jumppage" value="${page}">
			<input class="page_btn" type="button" name="jump" value="跳转">
			<input class="page_btn" type="button" name="firstpage" value="首页">
			<input class="page_btn" type="button" name="prevpage" value="上一页">
			<input class="page_btn" type="button" name="nextpage" value="下一页">
			<input class="page_btn" type="button" name="lastpage" value="尾页">
		</div> --%>
		<script src="../js/jquery-2.2.3.min.js"></script>
		<!-- Bootstrap 3.3.6 -->
		<script src="../js/bootstrap.min.js"></script>
		<!-- DataTables -->
		<script src="../js/jquery.dataTables.min.js"></script>
		<script src="../js/dataTables.bootstrap.min.js"></script>
		<script>
			$(document).ready(function(){
			 $("#table_position").DataTable();
			    $("#table_position_filter").append("<a href='../monthpresent/presentadd.do' target='_blank'>"
			     +"<input type='button' value='上传' class='ip btn-primary'/>");
			 });
		</script>
	</body>
</html>

    