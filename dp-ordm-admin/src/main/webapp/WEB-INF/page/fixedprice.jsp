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
						<th>产品名称</th>
						<th>固定价格</th>
						<th>产品价格</th>
						<th>固定折扣</th>
						<th>客户折扣</th>
						<th>是否费比</th>
						<th>状态</th>
						<th>最后更新</th>
					</tr>
				</thead>
				<tbody>
 				<c:forEach items="${DSResponse.data}" var="fixedPrices">
					<tr  name="table_body">
						<td>${fixedPrices.id}<input type="hidden" value="${fixedPrices.productId}" class="productId"></td>
						<td>
							<a href="../fixedPrice/updateBtn.do?productId=${fixedPrices.productId}" target="_blank">修改</a>
							<c:if test="${fixedPrices.status=='启用'}">
							<span style="color:#337ab7;" class="status" href="../fixedPrice/diable.do">禁用</span>
							</c:if>
							<c:if test="${fixedPrices.status=='禁用'}">
							<span style="color:#337ab7;" class="status" href="../fixedPrice/enable.do">启用</span>
							</c:if>
						</td>
						<td>${fixedPrices.productName}</td>
						<td>${fixedPrices.fix}</td>
						<td>${fixedPrices.price}</td>
						<td>${fixedPrices.discount}</td>
						<td>${fixedPrices.pricePolicy}</td>
						<td>${fixedPrices.costRatio}</td>
						<td>${fixedPrices.status}</td>
						<td>${fixedPrices.lastUpdate}</td>
					</tr>
				</c:forEach>
				</tbody>
				<tfoot>
					<tr  name="table_head">
						<th>ID</th>
						<th>操作</th>
						<th>产品名称</th>
						<th>固定价格</th>
						<th>产品价格</th>
						<th>固定折扣</th>
						<th>客户折扣</th>
						<th>是否费比</th>
						<th>状态</th>
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
			    $("#table_position_filter").append("<a href='../fixedPrice/createBtn.do' target='_blank'>"
			    +"<input type='button' value='创建' class='ip btn-primary' /></a>");
			    $("#table_position").on("click",".status",function(){
			    	var href=$(this).attr("href");
			    	var productId=$(this).parent().parent().find(".productId").val();
			    	$.ajax({
			    		type:"post",
							url:href,
							data:{productId:productId},
							async:true,
							success: function(data) {
								if(data.status==0){
									jQuery("li[href='../fixedPrice/init']").click();
								}
							}
			    	})
			    })
			 });
		</script>
	</body>
</html>

    