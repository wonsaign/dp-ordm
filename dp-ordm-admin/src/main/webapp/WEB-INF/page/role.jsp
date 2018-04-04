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
					<th>角色名称</th>
					<th>状态</th>
					<th>摘要</th>
					<th>操作时间</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${Rolelist}" var="r" varStatus="s">
				<tr>
					<td id="parameter"><c:out value="${r.rid}"></c:out></td>
					<td><c:out value="${r.commonName}"></c:out></td>
					<td><c:out value="${r.status}"></c:out></td>
					<td><c:out value="${r.summary}"></c:out></td>
					<td><c:out value="${r.lastUpdate}"></c:out></td>
				</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<th>ID</th>
					<th>角色名称</th>
					<th>状态</th>
					<th>摘要</th>
					<th>操作时间</th>
				</tr>
			</tfoot>
			</table>
	</body>
</html>