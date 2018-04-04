<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title></title>
	<!-- 	<link rel="stylesheet" href="../css/bootstrap.min.css"> -->
		<!-- Ionicons -->
		<link rel="stylesheet" href="../css/ionicons.min.css">
		<!-- DataTables -->
		<!-- <link rel="stylesheet" href="../css/dataTables.bootstrap.css"> -->
		<!-- <link rel="stylesheet"  href="../css/jquery.dataTables.min.css"/> -->
		<link rel="stylesheet" href="../css/index.css">
		<link href="../css/foundation.min.css" rel="stylesheet">
		<link href="../css/foundation-datepicker.css" rel="stylesheet">
		
	</head>
	<body>
		<div>
		<div>
			<div class="col-sm-6"></div>
			<div class="col-sm-6">
				<input type="button" value="新增" class="btn-primary ip">
				<input type="submit" value="搜索" class="search btn-primary" href="">
				<input name="name" type="text" class="ip_text" value="" style="width: 150px; height: 30px; margin-left: 20px; border-radius: 4px 0px 0px 4px;">		
			</div>
		</div>
			<table class="table table-bordered table-striped">
				<thead>
					<tr  name="table_head">
						<th>产品名称</th>
						<th>总库存</th>
						<th>预留</th>
						<th>直发可用</th>
						<th>库存详情</th>
					</tr>
				</thead>
				<c:forEach items="${stock}" var="sto">
				<tbody>
				<tr <c:if test="${sto.availableAmt<100 }">style="background:#ff00009e"</c:if> >
					<td class="proName" style="cursor: pointer;"><input type="hidden" value="${sto.productId}">${sto.productName}</td>
					<td>${sto.totalAmt}</td>
					<td>${sto.reserveAmt}</td>
					<td>${sto.availableAmt}</td>
					<td class="stockDetail" style="cursor: pointer; color:rgb(57, 139, 218);">库存详情<i class="icon ion-chevron-down" style="margin: 0px 5px;"></i></td>
				</tr>
				</tbody>
				<tbody class="none">
				<c:forEach items="${sto.details}" var="stoDetail">
				<tr <c:if test="${stoDetail.availableAmt<100 }">style="background:#ff00009e"</c:if> >
					<td></td>
					<td>${stoDetail.totalAmt}</td>
					<td>${stoDetail.reserveAmt}</td>
					<td>${stoDetail.availableAmt}</td>
					<td>${stoDetail.stockName}</td>
				</tr>
				</c:forEach>
				</tbody>
				</c:forEach>
				<tfoot>
					<tr  name="table_head">
						<th>产品名称</th>
						<th>总库存</th>
						<th>预留</th>
						<th>直发可用</th>
						<th>库存详情</th>
					</tr>
				</tfoot>
			</table>
			<div class="example-modal">
	     <div class="modal" id="amend">
	       <div class="modal-dialog">
	         <div class="modal-content">
	           <div class="modal-header">
	             <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	               <span class="glyphicon glyphicon-remove"></span></button>
	             <h4 class="modal-title"></h4>
	           </div>
	           <div class="modal-body">
	           		<table>
	           			<thead>
	           				<tr name="table_head">
	           				  <th>记录号</th>
											<th>产品名称</th>
											<th>库存</th>
											<th>开始时间</th>
											<th>结束时间</th>
											<th>描述</th>
											<th>创建者</th>
											<th>创建时间</th>
											<th>操作/作废原因</th>
										</tr>
	           			</thead>
	           			<tbody>
	           			</tbody>
	           		</table>
	           		<div class="money"></div>
	           </div>
	           <div class="modal-footer">
	             <button type="button" class="btn btn-primary btn-primary-close" data-dismiss="modal">关闭</button>
	           </div>
	         </div>
	         <!-- /.modal-content -->
	       </div>
	       <!-- /.modal-dialog -->
	     </div>
	     <!-- /.modal -->
	  </div>
	  <div class="example-modal">
	     <div class="modal" id="amend1">
	       <div class="modal-dialog">
	         <div class="modal-content">
	           <div class="modal-header">
	             <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	               <span class="glyphicon glyphicon-remove"></span></button>
	             <h4 class="modal-title"></h4>
	           </div>
	           <div class="modal-body">
	           		<form class="save">
	           		<div>
		           		<span style="float:left;margin-right:20px;font-weight: bold;line-height: 40px;">产品名称:</span>
		           		<div class="gro_div" style="float:left; width:200px; position:relative;">
	         					<input class="productName prp_t" type="text" name="productName" placeholder="请输入产品名称" style="height:35px">
	          				<ul class="gro_ul" style="margin: 1px 17px; ">
	          				  <c:forEach items="${product}" var="pro">
	          				  	<li><input type="hidden" value="${pro.productId}" disabled="disabled">${pro.name}</li>
	          				  </c:forEach>
	          				</ul>
	         				</div>
         				</div>
	           		<table>
	           			<thead>
	           				<tr name="table_head">
											<th style='white-space:nowrap;'>产品名称</th>
											<th style='white-space:nowrap;'>仓库</th>
											<th style='white-space:nowrap;'>可用</th>
											<th>预留</th>
											<th>开始时间</th>
											<th>结束时间</th>
											<th>描述</th>
										</tr>
	           			</thead>
	           			<tbody>
	           			</tbody>
	           		</table>
	           		<div class="money"></div>
	           		</form>
	           </div>
	           <div class="modal-footer">
	             <button type="button" class="btn btn-primary btn-primary-save" data-dismiss="modal">保存</button>
	             <button type="button" class="btn btn-primary btn-primary-close" data-dismiss="modal">关闭</button>
	           </div>
	         </div>
	         <!-- /.modal-content -->
	       </div>
	       <!-- /.modal-dialog -->
	     </div>
	     <!-- /.modal -->
	  </div>
	  <div class="cancel_cause">
	  	<div class="cancel_cause_block">
	  		<h3>作废原因</h3>
	  		<textarea rows="5" cols="50"></textarea>
	  		<div>
	  			<button type="button" class="btn btn-primary cancel_cause_sure" data-dismiss="modal">确定</button>
  				<button type="button" class="btn btn-primary cancel_cause_cancel" data-dismiss="modal">取消</button>
	  		</div>
	  	</div>
	  </div>
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
		<script src="../js/foundation-datepicker.js"></script>
		<script src="../js/foundation-datepicker.zh-CN.js"></script>
		<script>
		 $(document).ready(function(){
			    $("#table_position").DataTable();
			    $("#table_position_filter").append("<a href='../activity/createBtn.do' target='_blank'>"
			    +"<input type='button' value='创建' class='ip btn-primary' /></a>");
			    $(".stockDetail").click(function(){
			    	$(this).parent().parent().next().toggle();
			    	if($(this).parent().parent().next().css("display")=="none"){
			    		$(this).find('i').removeClass("ion-chevron-up"); 
			    		$(this).find('i').addClass("ion-chevron-down");
			    	}else{
			    		$(this).find('i').removeClass("ion-chevron-down"); 
			    		$(this).find('i').addClass("ion-chevron-up");
			    	}
			    })
			   	var stockId;
			    $(".proName").click(function(){
			    	var productId=$(this).find("input[type='hidden']").val();
			    	$("#amend").show();
			    	var str='';
			    	var modalBody=$("#amend tbody");
			    	$.ajax({
							type:"get",
							url:"../stockReserve/record.do",
							async:true,
							data:{productId:productId},
							success: function(data) {
								console.log(data);
								$.each(data[0],function(i,product){
									str+="<tr><td class='stockId'>"+product.id+"</td><td>"+product.productName+"</td><td>";
									$.each(product.detail,function(j,proDetail){
										str+="<span class='stock'>"+proDetail+"</span>"
									}) 
									str+="</td><td>"+product.startTime+"</td><td>"+product.endTime+"</td><td>"+product.description+"</td>"
									+"<td>"+product.creator+"</td><td>"+product.createTime+"</td><td><span class='cancel_stock'>作废</span></td></tr>" 
								});  
								$.each(data[1],function(i,product){
									str+="<tr style='background:#ff78009e'><td class='stockId'>"+product.id+"</td><td>"+product.productName+"</td><td>";
									$.each(product.detail,function(j,proDetail){
										str+="<span class='stock'>"+proDetail+"</span>"
									}) 
									str+="</td><td>"+product.startTime+"</td><td>"+product.endTime+"</td><td>"+product.description+"</td>"
									+"<td>"+product.creator+"</td><td>"+product.createTime+"</td><td>"+product.cancellation+"</td></tr>" 
								}); 
								$.each(data[2],function(i,product){
									str+="<tr style='background:#4040449e'><td class='stockId'>"+product.id+"</td><td>"+product.productName+"</td><td>";
									$.each(product.detail,function(j,proDetail){
										str+="<span class='stock'>"+proDetail+"</span>"
									}) 
									str+="</td><td>"+product.startTime+"</td><td>"+product.endTime+"</td><td>"+product.description+"</td>"
									+"<td>"+product.creator+"</td><td>"+product.createTime+"</td><td>过期</td></tr>" 
								}); 
								modalBody.append(str);  
								$(".cancel_stock").click(function(){
									$(".cancel_cause").show();
									stockId=$(this).parent().parent().find(".stockId").text();
							})  
							}
						});  
			    })
			    $(".cancel_cause_sure").click(function(){
						if($(".cancel_cause").find("textarea").val()==''){
							alert("作废原因不能为空");
						}else{
							var cancel_cause=$(".cancel_cause").find("textarea").val();
							$.ajax({
								type:"get",
								url:"../stockReserve/cancel.do",
								async:true,
								data:{id:stockId,cancellation:cancel_cause},
								success: function(data) {
									if(data.status==0){
										$("#amend").hide();
										jQuery(".selactive").click();
									}else{
										alert(data.message);
									}
								}
							}) 
						}
						
					})
					$(".cancel_cause_cancel").click(function(){
						$(".cancel_cause").hide();
					})
			    $(".ip").click(function(){
			    	$("#amend1").show();
			    })
			   $('.gro_div').on('keyup focus','.productName',function(){
							$(this).parent().find(".gro_ul").show();
			        var index = $.trim($(this).val().toString()); // 去掉两头空格
			        if(index != ''){ // 如果搜索框输入为空
			        	$(this).parent().find(".gro_ul li").addClass("on");
			        	$(this).parent().find(".gro_ul li:contains('"+index+"')").removeClass("on");
			        } 
				   });
					$('.gro_div').on('click','.gro_ul li',function(){
						var modalBody=$("#amend1 tbody");
						$("#amend1 tbody").empty();
						$(this).parent().parent().find(".productName").val($(this).text());
						$(this).parent().hide(); 
						var productId=$(this).find("input").val();
						var productName=$(this).text();
						var str='';
						$.ajax({
							type:"get",
							url:"../stockReserve/productinstock.do",
							async:true,
							data:{productId:productId},
							success: function(data) {
								if(data.length==0){
									str+="<tr><td colspan='7' style='text-align:center'>"+productName+"没有可以预留库存</td></tr>"
								}else{
									$.each(data,function(i,product){
										if(i==0){
											str+="<tr><td rowspan="+data.length+" style='white-space:nowrap;'><input type='hidden' name='productId' value="+productId+">"+productName+"</td><td style='white-space:nowrap;'><input type='hidden' value="+product.stockId+" name='w'>"+product.stockName+"</td>"
											+"<td>"+product.availableAmt+"</td><td><input min='0' max="+product.availableAmt+" type='number' name='v'></td><td rowspan="+data.length+"><input type='text' id='qBeginTime' name='startTime'></td>"
											+"<td rowspan="+data.length+"><input type='text' id='qEndTime' name='endTime'></td><td rowspan="+data.length+"><textarea rows='5' cols='40' name='description'></textarea></td></tr>"
										}else{
											str+="<tr><td style='white-space:nowrap;'><input type='hidden' value="+product.stockId+" name='w'>"+product.stockName+"</td>"
											+"<td>"+product.availableAmt+"</td><td><input min='0' max="+product.availableAmt+" type='number' name='v'></td>"
											+"</tr>"
										}
									}); 
								}
								modalBody.append(str); 
								$('#qBeginTime').fdatepicker({
									format: 'yyyy-mm-dd hh:ii',
									pickTime: true,
									initialDate:new Date() 
								}).on('changeDate',function(e){  
								    var startTime = e.target.value;  
								    $("#qEndTime").fdatepicker('setStartDate',startTime);  
								});;
								$('#qEndTime').fdatepicker({
									format: 'yyyy-mm-dd hh:ii',
									pickTime: true,
									initialDate:new Date()
								}).on('changeDate',function(e){  
								    var endTime = e.date;  
								    $("#qBeginTime").fdatepicker('setEndDate',endTime);  
								});
							} 
						});
					}); 
					$(".btn-primary-save").click(function(){
						
					  var description=$("textarea[name='description']").val();
					  var startT=$("#qBeginTime").val();
						var endT=$("#qEndTime").val();
						var startDate = new Date(startT.replace(/-/g, '/'));
						var endDate= new Date(endT.replace(/-/g, '/'));
						var params = $(".save").serialize();
						var v=0;
						var t=0;
						var vt=0;
						if(startDate.getTime()>=endDate.getTime()){
							alert("结束时间必须大于开始时间！");
							return;
						}
						if(description==''){
							alert("描述不能为空");
							return;
						}
						$("input[name='v']").each(function(){
							if($(this).val()!=''&&$(this).val()!=0){
								v++;
							}
							if($(this).val()>Number($(this).parent().prev().text())){
								t++;
							}
						})
						if(v==0){
							alert("预留库存不能全部为空或零!");
							return;
						}
						if(t!=0){
							alert("预留库存不能大于可用库存!");
							return;
						}
						$.ajax({
							type : "post",
							url : "../stockReserve/save.do",
							async : false,
							timeout : 15000,
							cache : false,
							data : params,
							success : function(data) {
								if(status==0){
									$("#amend1 tbody").empty();
									$("#amend1").hide() ;
									jQuery(".selactive").click();
								}else{
									alert(data.message);
								}
							},
							error : function() {}
						});
					})
					$(".search").click(function(){
						var searchText = $.trim($(this).next().val().toString());
						if(searchText != ''){ // 如果搜索框输入为空
		        	$(".proName").parent().parent().addClass("on");
		        	$(".proName:contains('"+searchText+"')").parent().parent().removeClass("on")
		        }
					})
			 });
		</script>
	</body>
</html>

    