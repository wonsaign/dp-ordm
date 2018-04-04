<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible"content="IE=9; IE=8; IE=7; IE=EDGE">
		<title></title>
		<link rel="stylesheet" href="../css/bootstrap.min.css">
		<!-- Ionicons -->
		<link rel="stylesheet" href="../css/ionicons.min.css">
		<!-- DataTables -->
		<link rel="stylesheet" href="../css/dataTables.bootstrap.css">
	</head>
	<script type="text/javascript"></script>
	<body>
		<div class="row">
			<div class="col-sm-6"></div>
			<div class="col-sm-6">
					<input id="btn_seacher" type="button" value="搜索" class="search" href="../useradm/search"/>				
					<input id="txt_seacher" type="text" value="" class="ip_text" name="name" />
			</div>
		</div>
		<div id="div1" class="tab">
			<table id="table_position" class="table table-bordered table-striped">
				<thead>
					<tr>
						<th>ID</th>
						<shiro:hasAnyRoles name="root,adm">
						<th>编辑</th>
						</shiro:hasAnyRoles>
						<th>用户名</th>
						<th>组织ID</th>
						<th>姓名</th>
						<th>手机号</th>
						<th>角色</th>
						<th>状态</th>
	<!-- 					<th>操作人</th> -->
						<th>操作时间</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${UserBeanlist}" var="ub" varStatus="s">
				<tr>
					<td><c:out value="${ub.uid}"></c:out></td>
					<shiro:hasAnyRoles name="root,adm">
					<td>
						<a class="prp_amend" href="../useradm/updatebtn.do?loginName=${ub.loginName}" target="_blank">修改</a>
						<c:if test="${ub.authUser.status==0}">
				 			<span class="set_start" href="../useradm/enable">启用</span>
				 			<span class="set_down" href="../useradm/disable" hidden="hidden">禁用</span>
				 		</c:if>
				 		<c:if test="${ub.authUser.status==1}">
				 			<span class="set_start" href="../useradm/enable" hidden="hidden">启用</span>
				 			<span class="set_down" href="../useradm/disable">禁用</span>
				 		</c:if>
<!-- 				 		<span class="set_del">删除</span> -->
					</td>
					</shiro:hasAnyRoles>
					<td id="parameter"><c:out value="${ub.loginName}"></c:out></td>
					<td><c:out value="${ub.orgUnit}"></c:out></td>
					<td><c:out value="${ub.commonName}"></c:out></td>
					<td><c:out value="${ub.mobile}"></c:out></td>
					<td><c:out value="${ub.roles}"></c:out></td>
					<td class="status"><c:out value="${ub.status}"></c:out></td>
					<td><c:out value="${ub.lastUpdate}"></c:out></td>
				</tr>
				</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<th>ID</th>
						<shiro:hasAnyRoles name="root,adm">
						<th>编辑</th>
						</shiro:hasAnyRoles>
						<th>用户名</th>
						<th>组织ID</th>
						<th>姓名</th>
						<th>手机号</th>
						<th>角色</th>
						<th>状态</th>
	<!-- 					<th>操作人</th> -->
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
			$(document).ready(function(){
			 $("#table_position").DataTable();
			    $("#table_position_filter").append("<a href='../useradm/useradd.do' target='_blank' >"
			    	+"<input type='button' value='创建' class='ip btn-primary' ></a>");
			   $("#btn_seacher").click(function() {
			    	var txt_seacher=$("#txt_seacher").val();
			    	var myDiv=$("#myDiv");
			    	myDiv.empty();
						$.ajax({
							type:"post",
							url:"../useradm/search.do",
							async:true,
							data:{name:txt_seacher}, 
							success:function(data){
								myDiv.html(data);
							}
						})
					}); 
			/* 	$("#myDiv").empty();
				htmlobj=$.ajax({url:url,async:false});
			    $("#myDiv").html(htmlobj.responseText); */
			 });
		</script>
	</body>
</html>