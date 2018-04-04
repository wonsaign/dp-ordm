<%@page import="com.zeusas.core.utils.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
		<div class="back_block">
		</div>
		<div>
			<table id="table_position" class="table table-bordered table-striped">
				<thead>
					<tr name="table_head">
						<th>编号</th>
						<th>操作</th>
						<th>合并号</th>
						<th>客户</th>
						<th>可用余额</th>
						<th>订单的总价</th>
						<th>实际支付</th>
						<th>使用余额</th>
						<th>额外物料费用</th>
						<th>物流费用</th>
						<th>备注</th>
						<th>凭证</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${dsResponse.data}" var="oc" varStatus="s">
				<tr name="table_merge" title="双击显示明细">
					<td>${s.count}</td>
					<td style="min-width:70px">
						<input class="lihref" type="hidden" value="../orderadm/initmerge">
						<span href='../orderadm/passCombineOrder' class="order_pass">通过</span>
						<span href='../orderadm/refuseCombineOrder' class="order_refuse">退回</span>
					</td>
					<td class="mergeid">${oc.ocid}</td>
					<td>${oc.customerName}</td>
					<td <c:if test="${fn:contains(oc.usefulBalance,'-')}">style="color: red"</c:if>>${oc.usefulBalance}</td>
					<td <c:if test="${oc.totalAmt != (oc.actualPay+oc.useBlance)}">style="color: red"</c:if>>${oc.totalAmt}</td>
					<td <c:if test="${oc.totalAmt != (oc.actualPay+oc.useBlance)}">style="color: red"</c:if>>${oc.actualPay}</td>
					<td <c:if test="${oc.totalAmt != (oc.actualPay+oc.useBlance)}">style="color: red"</c:if>>${oc.useBlance}</td>
					<td>${oc.materialAmt}</td>
					<td>${oc.postage}</td>
					<td>${oc.customerDesc}</td>
					
					<td >
						<%-- <input class="img_prefix" type="hidden" value="<%=AppConfig.getVfsPrefix()%>"> --%>
						<div class="img_rul"  type="hidden">
						<c:forEach items="${oc.imgUrl}" var="url" varStatus="url_s">
							<input  type="hidden" value="<%=AppConfig.getVfsPrefix()%>${url}">
						</c:forEach>
						</div>
						<span <c:if test="${oc.haveImg}">style="color: blue"</c:if> class="order_img_show">
							<c:if test="${oc.haveImg}">有</c:if>
							<c:if test="${!oc.haveImg}">无</c:if>
						</span>
					</td>
				</tr>
				</c:forEach>
				</tbody>
				<tfoot>
					<tr name="table_head">
						<th>编号</th>
						<th>操作</th>
						<th>合并号</th>
						<th>客户</th>
						<th>可用余额</th>
						<th>订单的总价</th>
						<th>实际支付</th>
						<th>使用余额</th>
						<th>额外物料费用</th>
						<th>物流费用</th>
						<th>备注</th>
						<th>凭证</th>
					</tr>
				</tfoot>
			</table>
		</div>
		<%-- <div class="pagination_div row">
			<div class="col-sm-12">
			<a hidden="hidden" name="paginationURL">../orderadm/orderpage</a>
			<a hidden="hidden" name="VfsPrefix"><%=AppConfig.getVfsPrefix()%></a>
			第<a  name="page">${page}</a>页
			共<a  name="max">${max}</a>页
			<a hidden="hidden" name="key">${key}</a>
			跳到&nbsp;<input type="text" name="jumppage" value="${page}">
			<input class="page_btn" type="button" name="jump" value="跳转">
			<input class="page_btn" type="button" name="firstpage" value="首页">
			<input class="page_btn" type="button" name="prevpage" value="上一页">
			<input class="page_btn" type="button" name="nextpage" value="下一页">
			<input class="page_btn" type="button" name="lastpage" value="尾页">
			</div>
		</div> --%>
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
											<th>备注</th>
											<th>创建时间</th>
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
		<!-- Bootstrap 3.3.6 -->
		<script src="../js/bootstrap.min.js"></script>
		<!-- DataTables -->
		<script src="../js/jquery.dataTables.min.js"></script>
		<script src="../js/dataTables.bootstrap.min.js"></script>
	</body>
</html>