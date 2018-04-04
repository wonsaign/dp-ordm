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
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
		<!-- DataTables -->
		<link rel="stylesheet" href="../css/dataTables.bootstrap.css">
	</head>
	<body>
		<div>
			<table id="table_position" class="table table-bordered table-striped">
				<thead>
					<tr>
						<th>编辑</th>
						<th>产品策略名称</th>
						<th>产品名称</th>
						<th>产品策略类型</th>
						<th>状态</th>
						<th>最小订货单位</th>
						<th>关联赠品物料</th>
						<th>操作时间</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${PRPBeanList}" var="prpb">
				<tr>
					<td>
						<a class="prp_amend" href="../productpolicy/updatebtn.do?policyId=${prpb.policyId}" target="_blank">修改</a>
				 		<c:if test="${prpb.productRelationPolicy.status==0}">
				 			<span class="prp_start" href="../productpolicy/enable">启用</span>
				 			<span class="prp_down" href="../productpolicy/disable" hidden="hidden">禁用</span>
				 		</c:if>
				 		<c:if test="${prpb.productRelationPolicy.status==1}">
				 			<span class="prp_start" href="../productpolicy/enable" hidden="hidden">启用</span>
				 			<span class="prp_down" href="../productpolicy/disable">禁用</span>
				 		</c:if>
					</td>
					<td>${prpb.name}<input type="hidden" value="${prpb.policyId}"></td>
					<td>${prpb.productName}</td>
					<td>${prpb.type}</td>
					<td class="status">${prpb.status}</td>
					<td>${prpb.minOrderUnit}</td>
					<td>${prpb.associatedProducts}</td>
					<td>${prpb.lastUpdate}</td>
				</tr>
				</c:forEach>
				</tbody>
				<tfoot>
	                <tr>
	                  <th>编辑</th>
					  <th>产品策略名称</th>
					  <th>产品名称</th>
					  <th>产品策略类型</th>
					  <th>状态</th>
					  <th>最小订货单位</th>
					  <th>关联赠品物料</th>
					  <th>操作时间</th>
	                </tr>
                </tfoot>
			</table>
		</div>
		<%-- <div class="pagination_div">
		<a hidden="hidden" name="paginationURL">../productpolicy/PRPpage</a>
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
		    $("#table_position_filter").append("<a href='../productpolicy/addbtn.do' target='_blank'>"
		    +"<input type='button' value='创建' class='ip btn-primary' /></a>");
		 });
		</script>
	</body>
</html>