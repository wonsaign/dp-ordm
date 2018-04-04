<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title></title>
		<style>
			table{
				border: 1px solid black;
				width: 90%;
				margin: 20px auto;
			}
		</style>
	</head>
	<body>
		<div class="button_div">
			<div class="button_block">
			<a href="../productgroup/productgroupadd.do" target="_blank">
				<input type="button" value="创建" class="ip"/>
			</a>
		</div>
		</div>
		<div>
			<table id="pgroup">
				<tr>
					<th>ID</th>
					<th>活动策略名称</th>
					<th>活动时间</th>
					<th>状态</th>
					<th>操作人</th>
					<th>操作时间</th>
					<th>操作</th>
				</tr>
				<tbody>
				<c:forEach items="${lphBean}" var="pgroups">
				<tr>
					<td class="actID">${pgroups.actId}</td>
					<td>${pgroups.actName}</td>
					<td>${pgroups.startTime}~${pgroups.endTime}</td>
					<c:if test="${pgroups.status==true}">
						<td class="p_start">正常</td>
					</c:if>
					<c:if test="${pgroups.status==false}">
						<td class="p_down">失效</td>
					</c:if>
					<td>${pgroups.createUserName}</td>
					<td>${pgroups.lastUpdate}</td>
					<td>
						<a href="../productgroup/update.do?actId=${pgroups.actId}" target="_blank" style="text-decoration:none">
							<span class="p_amend">修改</span>
						</a>
						<c:if test="${pgroups.status==false}">
				 		<span class="p_start" href="../productgroup/changeStatus" id="true">启用</span>
				 		</c:if>
				 		<c:if test="${pgroups.status==true}">
				 		<span class="p_down" href="../productgroup/changeStatus" id="false">禁用</span>
				 		</c:if>
					</td>
				</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</body>
</html>

    