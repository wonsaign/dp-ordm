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
						<th>操作</th>
						<th>活动名称</th>
						<th>描述</th>
						<th>类型</th>
						<th>起始时间</th>
						<th>结束时间</th>
						<th>承担部门</th>
						<th>否是捆绑</th>
						<th>自动扫描</th>
						<th>创建人</th>
						<th>状态</th>
						<th>创建时间</th>
						<th>最后更新</th>
					</tr>
				</thead>
				<tbody>
 				<c:forEach items="${DSResponse.data}" var="activity">
					<tr  name="table_body">
						<td>${activity.actId}</td>
						<td>修改 禁用</td>
						<td>${activity.name}</td>
						<td>${activity.description}</td>
						<td>${activity.type}</td>
						<td>${activity.start}</td>
						<td>${activity.to}</td>
						<td>${activity.dutyCode}</td>
						<td>${activity.bunding}</td>
						<td>${activity.autoAlloca}</td>
						<td>${activity.ownerName}</td>
						<td>${activity.status}</td>
						<td>${activity.createDate}</td>
						<td>${activity.lastUpdate}</td>
					</tr>
				</c:forEach>
				</tbody>
				<tfoot>
					<tr  name="table_head">
						<th>ID</th>
						<th>操作</th>
						<th>活动名称</th>
						<th>描述</th>
						<th>类型</th>
						<th>起始时间</th>
						<th>结束时间</th>
						<th>承担部门</th>
						<th>否是捆绑</th>
						<th>自动扫描</th>
						<th>创建人</th>
						<th>状态</th>
						<th>创建时间</th>
						<th>最后更新</th>
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
			    $("#table_position_filter").append("<a href='../activity/createBtn.do' target='_blank'>"
			    +"<input type='button' value='创建' class='ip btn-primary' /></a>");
			 });
		</script>
	</body>
</html>

    