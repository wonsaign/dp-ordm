<%@page import="com.zeusas.core.utils.*"%>
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
			<div class="button_block">
				<input type="submit" value="搜索" class="search1" href="../product/searchseries">
				<input name="name" type="text" value="" class="ip_text"/>		
			</div>
			<table id="table_position" class="table table-bordered table-striped">
			<thead>
				<tr>
					<th>编辑</th>
					<th>系列代码</th>
					<th>系列名称</th>
					<th>状态</th>
					<th>图片</th>
					<th>操作时间</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${SeriesBeanList}" var="sb">
				<tr name="table_body">
					<%-- <td class="searchbyid" hidden="hidden">${sb.did}</td> --%>
					<td><a href="../product/getSeriesUrl.do?did=${sb.did}" target="_blank">修改</a></td>
					<td>${sb.hardCode}<input type="hidden" value="${sb.did}" class="searchbyid"></td>
					<td>${sb.name}</td>
					<td>${sb.active}</td>
					<td><input class="img_rul" hidden="hidden" value="<%=AppConfig.getVfsPrefix()%>${sb.url}"><span class="img_show">${sb.haveimg}</span></td>
					<td>${sb.lastUpdate}</td>
				</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<th>编辑</th>
					<th>系列代码</th>
					<th>系列名称</th>
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
		<script type="text/javascript">
		$('#myDiv').on('click','.search1',function(){
			var sr=$(this).attr('href');
			var ipv=$(this).next().val();
			var typeid=$(this).parent().find('select[name="typeid"]').val();
			if (window.XMLHttpRequest)
			  {// code for IE7+, Firefox, Chrome, Opera, Safari
			  xmlhttp=new XMLHttpRequest();
			  }
			else
			  {// code for IE6, IE5
			  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			  }
			xmlhttp.onreadystatechange=function()
			  {
			  if (xmlhttp.readyState==4 && xmlhttp.status==200)
			    {
			    document.getElementById("myDiv").innerHTML=xmlhttp.responseText;
			    }
			  }
			xmlhttp.open("GET",sr+".do?name="+ipv+"&typeid="+typeid,true);
			xmlhttp.send();	
		}); 
		</script>
	</body>
</html>
