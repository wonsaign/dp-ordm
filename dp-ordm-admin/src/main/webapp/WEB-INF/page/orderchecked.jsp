<%@page import="com.zeusas.core.utils.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
		<!-- <select name="typeid">
				<option></option>
				<option></option>
		</select> -->
		<div class="button_div">
			<div class="button_block">
				<input type="button" value="搜索" class="search_checked" href="../orderadm/searchCheckedOrder"/>
				<input type="text" value="" class="ip_text"/>			
			</div>
		</div>
		<div>
			<table name="table_position" >
				<thead>
					<tr>
						<th>编号</th>
						<th>订单号</th>
						<th>店铺</th>
						<th>客户</th>
						<th>订单的总价</th>
						<th>实际支付</th>
						<th>使用余额</th>
						<th>商品总价</th>
						<th>物流费用</th>
						<th>额外物料费用</th>
						<th>支付类型</th>
						<th>状态</th>
						<th>订单备注</th>
						<th>退回备注</th>
						<th>凭证</th>
						<th>创建时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${OrderBeanList}" var="ob" varStatus="s">
					<tr name="table_body" title="双击显示明细">
						<td>${s.count}<input type="hidden" value="${ob.id}" class="orderid"></td>
						<td>${ob.orderNo}</td>
						<td>${ob.counterName}</td>
						<td>${ob.customerName}</td>
						<td <c:if test="${ob.paymentFee != (ob.paymentPrice+ob.order.useBalance)}">style="color: red"</c:if>>${ob.paymentFee}</td>
						<td <c:if test="${ob.paymentFee != (ob.paymentPrice+ob.order.useBalance)}">style="color: red"</c:if>>${ob.paymentPrice}</td>
						<td <c:if test="${ob.paymentFee != (ob.paymentPrice+ob.order.useBalance)}">style="color: red"</c:if>>${ob.order.useBalance}</td>
						<td>${ob.productPrice}</td>
						<td>${ob.expressFee}</td>
						<td>${ob.materialFee}</td>
						<td>${ob.payType}</td>
						<td>${ob.orderStatus}</td>
						<td>${ob.description}</td>
						<td>${ob.checkDesc}</td>
						<td >
							<input class="img_prefix" type="hidden" value="<%=AppConfig.getVfsPrefix()%>">
							<div class="img_rul"  type="hidden">
							<c:forEach items="${ob.imageURL}" var="url" varStatus="url_s">
								<input  type="hidden" value="<%=AppConfig.getVfsPrefix()%>${url}">
							</c:forEach>
							</div>
							<span <c:if test="${ob.img=='有'}">style="color: blue"</c:if> class="order_img_show" >${ob.img}</span>
						</td>
						<td>${ob.orderCreatTime}</td>
					</tr>
					</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<th>编号</th>
						<th>订单号</th>
						<th>店铺</th>
						<th>客户</th>
						<th>订单的总价</th>
						<th>实际支付</th>
						<th>使用余额</th>
						<th>商品总价</th>
						<th>物流费用</th>
						<th>额外物料费用</th>
						<th>支付类型</th>
						<th>状态</th>
						<th>订单备注</th>
						<th>退回备注</th>
						<th>凭证</th>
						<th>创建时间</th>
					</tr>
				</tfoot>
			</table>
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
	           					<td>商品名称</td>
	           					<td>会员价</td>
	           					<td>数量</td>
	           					<td>金额</td>
	           					<td>折扣价</td>
	           					<td>实发</td>
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
	<div class="pagination_div">
			<a hidden="hidden" name="paginationURL">../orderadm/checkedOrderPage</a>
			<a hidden="hidden" name="VfsPrefix"><%=AppConfig.getVfsPrefix()%></a>
			第<a  name="page">${page}</a>页
			共<a  name="max">${max}</a>页
			<a hidden="hidden" name="key">${key}</a>
			跳到<input type="text" name="jumppage" value="${page}">
			<input class="page_btn" type="button" name="jump" value="跳转">
			<input class="page_btn" type="button" name="firstpage" value="首页">
			<input class="page_btn" type="button" name="prevpage" value="上一页">
			<input class="page_btn" type="button" name="nextpage" value="下一页">
			<input class="page_btn" type="button" name="lastpage" value="尾页">
		</div> 
		<script src="../js/jquery-2.2.3.min.js"></script>
		<!-- Bootstrap 3.3.6 -->
		<script src="../js/bootstrap.min.js"></script>
		<!-- DataTables -->
		<script src="../js/jquery.dataTables.min.js"></script>
		<script src="../js/dataTables.bootstrap.min.js"></script>
	</body>
</html>