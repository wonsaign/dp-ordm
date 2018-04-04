<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href="../css/bootstrap.min.css">
<!-- Ionicons -->
<link rel="stylesheet" href="../css/ionicons.min.css">
<!-- DataTables -->
<link rel="stylesheet" href="../css/dataTables.bootstrap.css">
</head>
<body>
	<body>
				<table id="table_position" class="table table-bordered table-striped">
					<thead>
						<tr>
							<th>DID</th>
							<th>编辑</th>
							<th>名称</th>
							<th>价格</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${dictionarys}" var="d">
					<tr>
						<td>${d.did}</td>
						<td>
							<span style="color:#9e6eec;font-weight: bold;cursor: pointer;">
								<a href="../settingadm/update.do?did=${d.did}" target="_blank" style="text-decoration: none;">修改</a>
							</span>
							<span style="color:red;font-weight: bold;cursor: pointer;">删除</span>
						</td>
						<td>${d.summary}</td>
						<td>￥${d.name}</td>
					</tr>
					</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<th>DID</th>
							<th>编辑</th>
							<th>名称</th>
							<th>价格</th>
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