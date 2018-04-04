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
	</head>
	<body>	
		<div>
			<table id="table_position" class="table table-bordered table-striped">
				<thead>
					<tr>
						<th>ID</th>
						<th>客户名称</th>
						<th>联系人</th>
						<th>手机</th>
						<th>客户类型</th>
						<th>邮编</th>
						<th>最后修改时间</th>
						<th>是否已创建用户</th>
						<th>状态</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${CustomerBeanList}" var="cbl">
					<tr>
						<td>${cbl.customerID}</td>
						<td>${cbl.customerName}</td>
						<td>${cbl.contact}</td>
						<td>${cbl.mobile}</td>
						<td>${cbl.customerType}</td>
						<td>${cbl.postCode}</td>
						<td>${cbl.lastUpdate}</td>
						<td>${cbl.createUser}</td>
						<td>${cbl.status}</td>
					</tr>
				</c:forEach>
				</tbody>
				<tfoot>
          <tr>
            <th>ID</th>
					  <th>客户名称</th>
					  <th>联系人</th>
					  <th>手机</th>
					  <th>客户类型</th>
					  <th>邮编</th>
					  <th>最后修改时间</th>
					  <th>是否已创建用户</th>
					  <th>状态</th>
           </tr>
          </tfoot>
			</table>
		</div>
		<%-- <div class="pagination_div">
			<a hidden="hidden" name="paginationURL">../base/customerpage</a>
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
	</body>
</html>
