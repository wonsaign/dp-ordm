<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	
		
		<form method="post" action="<%=request.getContextPath()%>/product/fileUpLoad.do" enctype="multipart/form-data">  
    <input type="file" name="file"/>  
    <input type="submit" value="上传">  
</form>  
	</body>
</html>
