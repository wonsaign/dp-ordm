<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title></title>
<link rel="stylesheet" href="../css/bootstrap.min.css">
<!-- Ionicons -->
<link rel="stylesheet" href="../css/ionicons.min.css">
<!-- DataTables -->
<link rel="stylesheet" href="../css/dataTables.bootstrap.css">
<link href="../css/foundation-datepicker.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="../css/index.css">
<link rel="shortcut   icon " href="../img/favicon.ico ">
</head>
<body>
	<%@ include file="common/common_window.jsp"%>
	<div class="above">
		<div
			style="color: white; width: 152px; height: 50px; position: absolute; text-align: center; line-height: 50px; font-weight: bold; font-size: 22px; background: #367fa9;">订货管理系统</div>
		<div class="system_notice">系统通知</div>
		<div class="user_notice">
			<span style="float: left">用户:<%=request.getRemoteUser()%></span> <span
				style="clear: right; cursor: pointer;" id="loginOut"
				href="../admin/logout" title="点击退出系统">退出</span>
			<div style="clear: both"></div>
		</div>
		<div style="clear: both"></div>
	</div>
	<div id="myDiv"></div>
	<div class="sidebar">
		<ul class="ul1">
			<li class="li1"><a class="drop-down">基础数据<span>></span></a>
				<ul class="ul2">
					<shiro:hasAnyRoles name="root,adm,40">
						<li href="../product/init" class="page t"><a href="#">商品</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm,40">
						<li href="../product/productseries" class="page t"><a
							href="#">产品系列</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm,14">
						<li href="../customer/init" class="page t"><a href="#">客户</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm,14">
						<li href="../counter/init" class="page t"><a href="#">门店</a></li>
					</shiro:hasAnyRoles>
				</ul></li>
			<li class="li1"><a class="drop-down">订单管理<span>></span></a>
				<ul class="ul2">
					<shiro:hasAnyRoles name="root,adm,20,60">
						<li href="../orderadm/init" class="page t"><a href="#">订单审核</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm,20,60">
						<li href="../orderadm/initmerge" class="page t"><a href="#">合并订单</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm,20,14,60">
						<li href="../orderadm/getchecked" class="page t"><a href="#">订单明细</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="14">
						<li href="../orderadm/areaorder" class="page t"><a href="#">订单汇总</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm,20,60">
						<li href="../orderadm/getinvalid" class="page t"><a href="#">查看作废订单</a></li>
					</shiro:hasAnyRoles> 
					<shiro:hasAnyRoles name="root,adm,20,60">
						<li href="../orderadm/getreview" class="page t"><a href="#">作废订单</a></li>
					</shiro:hasAnyRoles>
				</ul></li>
			<li class="li1"><a class="drop-down">策略管理<span>></span></a>
				<ul class="ul2">
					<shiro:hasAnyRoles name="root,adm,50">
						<li href="../monthpresent/present" class="page t"><a href="#">每月赠送</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm,50">
						<li href="../materialtemplate/init" class="page t"><a
							href="#">新店物料</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm,50">
						<li href="../productpolicy/init" class="page t"><a href="#">产品策略</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm,50">
						<li href="../pricePolicy/init" class="page t"><a href="#">折扣策略</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm,40">
						<li href="../fixedPrice/init" class="page t"><a href="#">价格策略</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm,40">
						<li href="../activity/init" class="page t"><a href="#">活动配置</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm,40">
						<li href="../reserveproduct/init" class="page t"><a href="#">打欠设置</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm,40">
						<li href="../reserveproduct/reserverecord" class="page t"><a href="#">打欠记录报表</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm,40,50">
					<li href="../stockReserve/init" class="page t"><a href="#">库存预留</a></li>
					</shiro:hasAnyRoles>
 					<shiro:hasAnyRoles name="root,adm,40,50">
					<li href="../redeempoint/init" class="page t"><a href="#">积分兑换</a></li>
					</shiro:hasAnyRoles>
				</ul></li>
			<li class="li1"><a class="drop-down">系统设置<span>></span></a>
				<ul class="ul2">
					<shiro:hasAnyRoles name="root,adm,20,30,40,50,60,70">
						<li href="../useradm/user" class="page t"><a href="#">用户</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm">
						<li href="../roleadm/role" class="page t"><a href="#">角色</a></li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="root,adm">
						<li href="../settingadm/setting_204" class="page t">ordm配置</li>
						<!-- <div style="display: none;cursor: pointer;" class="settings init">
								<a href="#" style="font-size: 5px;text-decoration: none;">物流配置</a> 
							</div> -->
					</shiro:hasAnyRoles>
					<%-- 						<shiro:hasAnyRoles name="root,adm"> --%>
					<!-- 							<li href="../organizationadm/organization" class="page t">组织机构</li> -->
					<%-- 						</shiro:hasAnyRoles> --%>
					<%-- 						<shiro:hasAnyRoles name="root,adm"> --%>
					<!-- 							<li href="../system_informationadm/system_information" class="page t">系统信息</li> -->
					<%-- 						</shiro:hasAnyRoles> --%>
				</ul></li>
		</ul>
	</div>
</body>
<script src="../js/jquery-2.2.3.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/foundation-datepicker.js"></script>
<script src="../js/foundation-datepicker.zh-CN.js"></script>
<script src="../js/jquery.dataTables.min.js"></script>
<script src="../js/dataTables.bootstrap.min.js"></script>
<script src="../js/index.js"></script>
<script src="../js/product.js"></script>
<script src="../js/jquery.datetimepicker.full.js"></script>
<script src="../js/pagination.js"></script>
<script src="../js/user.js"></script>
<script src="../js/productpolicy.js"></script>
<script src="../js/productgroup.js"></script>
<script src="../js/pricepolicy.js"></script>
<script src="../js/order.js"></script>
<script src="../js/action.js"></script>
<script>
	$(document).ready(function() {
		$("#table_position1").DataTable({
			"order" : [ 0, 'asc' ]
		});
		$("#myDiv").on("click", ".add", function() {
			var sum = $(this).parent().find(".number").val();
			sum = Number(sum) + 1;
			$(this).parent().find(".number").val(sum);
		});
		$("#myDiv").on("click", ".sub", function() {
			var s = $(this).parent().find(".number").val();
			s = Number(s) - 1;
			$(this).parent().find(".number").val(s);
		});
		$(".li1").each(function() {
			var l = $(this).find("li").length;
			if (l == 0) {
				$(this).hide();
			}
		});
	})
</script>
</html>
