<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>上传页面</title>
</head>
<body>
	<ul>
		<li>上传礼盒内容</li>
	</ul>
	<form method="post" action="../preorder/upload.do" enctype="multipart/form-data">
		<input type="file" name="uploadFile" id="target" /> <input
			type="submit" value="上传" />
	</form>
	<a href="../preorder/transtoOrder.do">转换订单</a>
</body>
</html>
