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
			<div class="row">
				<div class="col-sm-6">
					<select name="typeid" style="height:30px;">
						<c:forEach items="${ldicts }" var="dict">
						<option value="${dict.hardCode }" <c:if test="${dict.hardCode==typeid }">selected</c:if> >${ dict.name}</option>
						</c:forEach>
					</select>
					<select name="avalible" style="height:30px;">
						<c:if test="${avalible==1||avalible==null}">
							<option value="1" selected>已启用</option>
							<option value="0">已禁用</option>
						</c:if>
						<c:if test="${avalible==0}">
							<option value="1">已启用</option>
							<option value="0" selected>已禁用</option>
						</c:if>
					</select>
				</div>
				<div class="col-sm-6">
					<input type="submit" value="搜索" class="search btn-primary" href="../product/product"/>
					<input name="name" type="text" class="ip_text" value="${name }"/>		
				</div>
			</div>
			<div class="tab">
				<table id="table_position1" class="table table-bordered table-striped">
					<thead>
						<tr name='table_head'>
							<th>id</th>
							<th>编辑</th>
							<th>商品代价</th>
							<th>名称</th>
							<th>分类</th>
							<th>状态</th>
							<th>图片</th>
							<th>操作时间</th>
						</tr>
					</thead>
					<tbody>	
						<c:forEach items="${productBeans}" var="product">
							<tr>
								<td>${product.product.productId}</td>
								<td>
								<a href='../product/productupdate.do?id=${product.product.productId }' target='_blank'><span>修改</span></a>
								<c:if test="${product.product.avalible==true}">
									<span class='pclose'>禁用</span>
								</c:if>
								<c:if test="${product.product.avalible==false}">
									<span class='pclose'>禁用</span>
								</c:if>
								</td>
								<td>${product.product.productCode}</td>
								<td>${product.product.name}</td>
								<td>${product.product.typeName}</td>
								<td><c:if test="${product.product.avalible==true}">
									禁用
								</c:if>
								<c:if test="${product.product.avalible==false}">
									禁用
								</c:if></td>
								<td>${product.product.imageURL}</td>
								<td>${product.lastUpdate}</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr name='table_head'>
							<th>id</th>
							<th>编辑</th>
							<th>商品代价</th>
							<th>名称</th>
							<th>分类</th>
							<th>状态</th>
							<th>图片</th>
							<th>操作时间</th>
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
			$("#table_position1").DataTable({
	 		  "order": [0, 'desc']
	 	  }); 
		</script>
	</body>
</html>
