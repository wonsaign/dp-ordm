<%@page import="com.zeusas.core.utils.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title></title>
	</head>
	<body>
		<div class="row">
			<div class="col-sm-12 right">
				<label>开始时间:<input class='starttime input-sm' type='text'></label>
				<label>结束时间:<input class='endtime input-sm' type='text'></label>
				<input type='button' value='查询' class='btn-primary search_order'>	
			</div>
		</div>
		<div class="tab">
			<table id='table_position1' class='table table-bordered table-striped'>
				<thead>
   				<tr name='table_head'>
   					<th>序号</th>
   					<th>客户类型</th>
   					<th>客户</th>
   					<th>单数</th>
   					<th>总金额</th>
   				</tr>
   			</thead>
   			<tbody>
   				<tr name='tr_body' class="myItem">
   					<td>1</td>
   					<td>${areaOrderBean.myItem.customerType}</td>
   					<td>${areaOrderBean.myItem.customerName}</td>
   					<td>${areaOrderBean.myItem.quantity}</td>
   					<td>${areaOrderBean.myItem.realFeeFormat}</td>
   				</tr>
   				<c:forEach items="${areaOrderBean.agent}" var="item" varStatus="status">
	   				<tr name='tr_body' class="agent">
	   					<td>${ status.index + 2}</td>
	   					<td>${item.customerType}</td>
	   					<td>${item.customerName}</td>
	   					<td>${item.quantity}</td>
	   					<td>${item.realFeeFormat}</td>
	   				</tr>
   				</c:forEach>
   				<c:forEach items="${areaOrderBean.distributor}" var="item" varStatus="status">
	   				<tr name='tr_body' class="distributor">
	   					<td>${ status.index + 2}</td>
	   					<td>${item.customerType}</td>
	   					<td>${item.customerName}</td>
	   					<td>${item.quantity}</td>
	   					<td>${item.realFeeFormat}</td>
	   				</tr>
   				</c:forEach>
   				<c:forEach items="${areaOrderBean.frOfOperator}" var="item" varStatus="status" >
	   				<tr name='tr_body' class="frOfOperator">
	   					<td>${ status.index + 2}</td>
	   					<td>${item.customerType}</td>
	   					<td>${item.customerName}</td>
	   					<td>${item.quantity}</td>
	   					<td>${item.realFeeFormat}</td>
	   				</tr>
   				</c:forEach>
   				<c:forEach items="${areaOrderBean.frOfAgent}" var="item" varStatus="status">
	   				<tr name='tr_body' class="frOfAgent">
	   					<td>${ status.index + 2}</td>
	   					<td>${item.customerType}</td>
	   					<td>${item.customerName}</td>
	   					<td>${item.quantity}</td>
	   					<td>${item.realFeeFormat}</td>
	   				</tr>
   				</c:forEach>
   			</tbody>
   			<tfoot>
   				<tr name='table_head'>
   					<th>序号</th>
   					<th>客户类型</th>
   					<th>客户</th>
   					<th>单数</th>
   					<th>总金额</th>
   				</tr>
   			</tfoot>
			</table>
		</div>
		<div class="summary">
			<span class='information_settle red'>加盟(不包含直营)总订单:<strong>${areaOrderBean.summary.quantity}</strong></span>
	  	<span class='information_settle red'>加盟(不包含直营)总金额:￥<strong>${areaOrderBean.summary.realFeeFormat}</strong></span>
		</div>
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
	           				<tr>
	           					<td>序号</td>
	           					<td>订单号</td>
	           					<td>店铺</td>
	           					<td>订单金额</td>
	           					<td>实发金额</td>
	           					<td>创建时间</td>
	           					<td>订单详情</td>
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
	  <script src="../js/jquery-2.2.3.min.js"></script>
		<script src="../js/bootstrap.min.js"></script>
		<script src="../js/jquery.dataTables.min.js"></script>
		<script src="../js/dataTables.bootstrap.min.js"></script>
		<script src="../js/foundation-datepicker.js"></script>
		<script src="../js/foundation-datepicker.zh-CN.js"></script>
		<script>
			$('.starttime').fdatepicker({
        format: 'yyyy-mm-dd',
        initialDate: new Date(),
   		 });
			$('.endtime').fdatepicker({
        format: 'yyyy-mm-dd',
        initialDate: new Date(),
   		 });
			$("#table_position1").DataTable({
				"order": [0, 'asc']  
  	  }); 
		</script>
	</body>
</html>