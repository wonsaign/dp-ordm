<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta charset="utf-8" />
<title>上传页面</title>
<link rel="stylesheet" href="../css/bootstrap.min.css">
<!-- Ionicons -->
<link rel="stylesheet" href="../css/ionicons.min.css">
<!-- DataTables -->
<link rel="stylesheet" href="../css/dataTables.bootstrap.css">
<style type="text/css">
.buttonAll{
		width: 100px;
    height: 30px;
    background-size: 100% 100%;
    border-radius: 4px;
    opacity: 0.7;
    color: white;
    font-size: 18px;
    margin-left: 20px;
    border: 1px solid #639fd9;
    float: right;
    font-weight: 100;
    text-align: center;
    line-height: 30px;
}
.button_div{
	height:auto;
}
.fileLeft{
  width:50px;
	float: right;
}
.fileImg{
	width: 50px;
}
.fileRight{
width:180px;
float: right;
}
</style>
</head>
<body>
			<div class="button_div">
				<div class="button_block">
				<form id="uploadForm" enctype="multipart/form-data">
					<input type="button" id="uploadFile" value="上传" class="btn-primary buttonAll"/>	
					<label id="target" class="btn-primary buttonAll">
						选择文件
						<input type="file" name="uploadFile" onchange="previewImage(this)" accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" style="display:none;"/>	
					</label>
					<div class='clear'></div>
				</form>
				<div id="file_block"></div>
				</div>
		</div>

	<table id="table_positionRedeem" class="table table-bordered table-striped">
				<thead>
						<tr>
							<th>序号</th>
							<th>柜台名</th>
							<th>产品名</th>
							<th>数量</th>
							<th>发货方式</th>
							<th>创建时间</th>
							<th>创建人</th>
							<th>执行单号</th>
							<th>备注</th>
							<th>操作</th>
						</tr>
				</thead>
				<tbody>
						<c:forEach items="${avalible}" var="ava" varStatus="s">
							<tr>
								<td class='avalibleId'>${ava.id}</td>
								<td>${ava.counterName}</td>
								<td>${ava.productName}</td>
								<td>${ava.num}</td>
								<td>${ava.deliveryWay}</td>
								<td>${ava.createTime}</td>
								<td>${ava.creator}</td>
								<td>${ava.excutorNo}</td> 
								<td>${ava.remark}</td> 
								<td><a class="cancel" href="#">取消</a></td> 
							</tr>
						</c:forEach>
				</tbody>
				<tfoot>
						<tr>
							<th>序号</th>
							<th>柜台名</th>
							<th>产品名</th>
							<th>数量</th>
							<th>发货方式</th>
							<th>创建时间</th>
							<th>创建人</th>
							<th>执行单号</th>
							<th>备注</th>
							<th>操作</th>
						</tr>
				</tfoot>
			</table>
			<script src="../js/jquery-2.2.3.min.js"></script>
			<script src="../js/bootstrap.min.js"></script>
			<!-- DataTables -->
			<script src="../js/jquery.dataTables.min.js"></script>
			<script src="../js/dataTables.bootstrap.min.js"></script>
			<script type="text/javascript">
			$("#table_positionRedeem").DataTable({
				"order" : [ 0, 'desc' ]
			});
			var src='';
			function previewImage(file){
				
	  	 if (file.files && file.files[0]) {
	  	    var reader = new FileReader();
	  	    reader.onload = function(evt) {
	  	    	$("#file_block").html("<div class='fileRight'><div>"+file.files[0].name+"</div><div>"+Number(file.files[0].size/1024).toFixed(2)+"KB</div></div><div class='fileLeft'><img class='fileImg' src='../img/file.png'></div><div class='clear'></div>");
	  	      src=evt.target.result;
	  	    }
	  	   reader.readAsDataURL(file.files[0]);
	  	  } else {//IE
	  		 src=evt.target.result; 
	  	  }  
	    } 
			$("#uploadFile").click(function(){
				var form = new FormData(document.getElementById("uploadForm"));
				$.ajax({
           url:"../redeempoint/upload.do",
           type:"post",
           data:form,
           processData:false,
           contentType:false,
           success:function(data){
               if(data.status==0){
            	   $(".fileImg").attr("src","../img/fileOK.png");
            	  
               }else{
            	   $(".fileImg").attr("src","../img/fileFAIL.png");
               }
               alert(data.message);
           },
           error:function(e){
               alert("错误！！");
               $(".fileImg").attr("src","../img/fileFAIL.png");
           }
       }); 
			})
			$(".cancel").click(function(){
				var that=this
				var avalibleId=$(that).parent().parent().find(".avalibleId").text();
				$.ajax({
           url:"../redeempoint/cancel.do",
           type:"post",
           data:{
        	   id:avalibleId
           },
           success:function(data){
               if(data.status==0){
            	  	$(that).parent().parent().remove();
               }
               alert(data.message);
           },
           error:function(e){
               alert("错误！！");
           }
       }); 
			})
			</script>
</body>
</html>
