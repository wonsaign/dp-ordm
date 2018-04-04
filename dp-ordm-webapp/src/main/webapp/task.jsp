<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>   
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport"
	content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no;charset=utf-8">
<title></title>
<link href="../css/index.css?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"css/index.css").lastModified()%>" rel="stylesheet" />
<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
</head>
<body>
<c:forEach items="${taskBeanforWeb }" var="tb">
 任务名: <a>${tb.taskBean.name}</a>&nbsp;
 可用: <a>${tb.taskBean.cronTask.valid}</a>&nbsp;
 暂停状态: <a>${tb.taskBean.cronTask.pause}</a>&nbsp;
 最后更新时间: <a>${tb.lastUpdate}</a> <br>
</c:forEach>
<h1>定时任务</h1>
<table  border="1" bordercolor="black">
	<tr>
		<th>&nbsp;</th>
		<th>取消订单</th>
		<th>门店</th>
		<th>客户</th>
		<th>订单</th>
		<th>组织机构</th>
		<th>产品</th>
		<th>销售数据</th>
		<th>库存</th>
		<th>订单差分</th>
		<th>月度物料</th>
		<th>打欠</th>
		<th>全部</th>
	</tr>
	<tr>
		<th>暂停</th>
		<td><a href="pause_cancelOrder.do">暂停取消订单</a></td>
		<td><a href="pause_counter.do">暂停门店</a></td>
		<td><a href="pause_customer.do">暂停客户</a></td>
		<td><a href="pause_order.do">暂停订单</a></td>
		<td><a href="pause_orgunit.do">暂停组织机构</a></td>
		<td><a href="pause_product.do">暂停产品</a></td>
		<td><a href="pause_sellerData.do">暂停销售数据</a></td>
		<td><a href="pause_storehouse.do">暂停库存</a></td>
		<td><a href="pause_difforder.do">暂停订单差分</a></td>
		<td><a href="pause_monthPresent.do">暂停月度物料</a></td>
		<td><a href="pause_reserveorder.do">打欠</a></td>
	    <td><a href="pause_all.do">暂停全部</a></td>
	</tr>
	
	<tr>
		<th>重启</th>
		<td><a href="restart_cancelOrder.do">重启取消订单</a></td>
		<td><a href="restart_counter.do">重启门店</a></td>
		<td><a href="restart_customer.do">重启客户</a></td>
		<td><a href="restart_order.do">重启订单</a></td>
		<td><a href="restart_orgunit.do">重启组织机构</a></td>
		<td><a href="restart_product.do">重启产品</a></td>
		<td><a href="restart_sellerData.do">重启销售数据</a></td>
		<td><a href="restart_storehouse.do">重启库存</a></td>
		<td><a href="restart_difforder.do">重启订单差分</a></td>
		<td><a href="restart_monthPresent.do">重启月度物料</a></td>
		<td><a href="restart_reserveorder.do">打欠</a></td>
	    <td><a href="restart_all.do">重启全部</a></td>
	</tr>
	<tr>
		<th>执行</th>
	    <td><a href="cancelOrder.do">取消订单</a></td>
		<td><a href="counter.do">门店</a></td>
		<td><a href="customer.do">客户</a></td>
		<td><a href="order.do">订单</a></td>
		<td><a href="orgunit.do">组织机构</a></td>
		<td><a href="product.do">产品</a></td>
		<td><a href="sellerData.do">销售数据</a></td>
		<td><a href="storehouse.do">库存</a></td>
		<td><a href="difforder.do">订单差分</a></td>
		<td><a href="monthPresent.do">月度物料</a></td>
		<td><a href="reserveOrder.do">打欠</a></td>
		<td><a href="shutdown.do">关闭管理器</a></td>
	</tr>
</table>

</body>
</html>
