<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
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
				<input type="button" value="创建" class="ip" href="../permissionadm/permissionadd" />			
				<input type="button" value="搜索" class="search"/>
				<input type="text" value="" class="ip_text" />
			</div>
		</div>
		<div>
			<table>
				<tr>
					<th>ID</th>
					<th>编辑</th>
					<th>权限名称</th>
					<th>按钮名称</th>
					<th>状态</th>
					<th>操作人</th>
					<th>操作时间</th>
				</tr>
				<tr>
					<td>1</td>
					<td>
						<span class="set_amend" href="../permissionadm/permissionadd">修改</span>
				 		<span class="set_start">启用</span>
				 		<span class="set_down">禁用</span>
				 		<span class="set_del">删除</span>
					</td>
					<td>gaoxing</td>
					<td>01245236</td>
					<td>高兴</td>
					<td>136954452</td>
					<td>系统管理员</td>
				</tr>
			</table>
		</div>
	</body>
</html>