<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.zeusas.core.utils.*" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title></title>
		<link rel="stylesheet" href="http://www.jq22.com/jquery/font-awesome.4.6.0.css">
		<link href="../css/bootstrap.min.css" rel="stylesheet" type="text/css">
		<link href="../css/dataTables.bootstrap.css" rel="stylesheet" type="text/css">
		<link href="../css/foundation.min.css" rel="stylesheet" type="text/css">
		<link href="../css/foundation-datepicker.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" href="../css/index.css">
		<link   rel= "shortcut   icon "   href= "../img/favicon.ico ">
		<style>
	     	input[type="text"]{
				height:35px;
			}
			input[type="number"]{
				width:80px;
				height:35px;
				display:inline-block;
				margin:0px 10px;
				-moz-appearance:textfield;
			}
			input[type=number]::-webkit-inner-spin-button,  
			input[type=number]::-webkit-outer-spin-button {  
			    -webkit-appearance: none;  
			    margin: 0;  
			} 
			.example-modal .modal {
		      position: fixed;
		      width:100%;
		      height:100%;
		      top: 0px;
		      bottom: auto;
		      right: auto;
		      left: 0px;
		      display: block;
		      z-index: 1;
		      background: black;
		      display: none;
		      overflow: scroll;
		    }
		    .example-modal .modal {
		      background: rgba(0,0,0,0.5);
		    }
		    label{
		    	height:25px;
		    }
		    input[type="search"]{
		    	height:25px;
		    }
		    select[name="example_length"]{
		    	height:25px;
		    	line-height:20px;
		    	padding:0px 5px;
		    }
		    #example tr,#example1 tr,#example th,#example1 th,#example td,#example1 td{
		    	height:20px;
		    	font-size:12px;
		    	padding:0px;
		    	line-height:25px;
		    }
		    .dataTables_length{
		    	font-size:10px;
		    }
		    input[type="checkbox"]{
		    	width:15px;
		    	height:15px;
		    	cursor:pointer;
		    	margin:5px 15px;
		    	display:block;
		    }
		    .dataTables_info{
		    	font-size:12px;
		    } 
		    .productadd{
		    	position:relative;
		    	font-size:12px;
		    	line-height:29px;
		    	padding:2px 20px;
		    	display:inline-block;
		    	border:1px solid #ccc;
		    	background:#337ab7;
		    	cursor:pointer;
		    }
		    select{
		    	font-size:14px;
				font-weight:normal;
		    }
		    .producttable{
		        padding:0px;
		        margin:0px;
		    }
		    .producttable tr{
		    	padding:2px 5px;
		    	font-size:14px;
		    }
		    .producttable tr:hover{
		    	background:#CCC;
		    }
		    .producttable th{
		    	padding:2px 5px;
		    	font-size:14px;
				font-weight:bold;
		    }
		    .producttable td{
		    	padding:2px 5px;
		    	font-size:12px;
				font-weight:normal;
		    }
		    .delproduct{
		    	color:red;
		    	cursor:pointer;
		    }
		    #example tr:hover,#example1 tr:hover{
		    	background:#5bc0de;
		    	cursor:pointer;
		    }
		    .control-label{
		    	line-height:35px;
		    	font-weight: bold;
		    	padding-right: 0;
		    	text-align: right;
		    }
		    .control-label1{
		    	line-height:35px;
		    	font-weight: bold;
		    }
		    .num{
		    	color:blue;
		    	font-weight: bold;
		    }
		    #modal_pro,#modal_ser{
		    	display:none;
		    }
		    .alert{
		    	position:absolute;
		    	top:-50px;
		    	left:50px;
		    	background:rgba(51,122,183,0.75);
		    	color:white;
		    	font-size:12px;
		    	padding:2px;
		    	width:100px;
		    }
		</style>
	</head>
	<body>
		<%@ include file="common/common_window.jsp"%>
		<script src="../js/jquery.js"></script>
		<script src="../js/bootstrap.min.js"></script>
		<script src="../js/foundation-datepicker.js"></script>
		<script src="../js/foundation-datepicker.zh-CN.js"></script>
		<script src="../js/productpolicy.js"></script>
		<script src="../js/jquery.dataTables.min.js"></script>
		<script src="../js/dataTables.bootstrap.min.js"></script>
		<script src="../js/index.js"></script>
		<div class="show_body" value="${message}">
		<input type="hidden" value="${message}" id="hidden1">
			<c:if test="${message!=null&&''!=message}">${message}</c:if>
		</div>
		<sf:form action="../productgroup/saveactivity.do" method="post" enctype="multipart/form-data" >
		<div style="width:100%; background:#e6e6e6;" class="div_body">
			<div style="width:750px; color:#5bc0de; text-align:center; padding:10px 50px; font-weight:bold; margin:0px auto; background:white; border:2px solid white; border-radius:5px;">活动策略添加</div>
			<div style="width:750px; margin:10px  auto  0px auto; padding:20px 50px; background:white; border:2px solid white; border-radius:5px;">
				<div style="width:650px; margin:20px auto; min-height:350px; height: auto;">
				
				<div class="pro_gro">
					<div class="row">
	              		<div class="col-lg-8">
	              			<div class="form-group">							
				                <label class="summary col-sm-3 control-label">活动策略名称</label>
				                <div class="col-sm-7" >	
					                <input type="text" class="form-control" placeholder="输入 活动策略名称">
								</div>
							</div>
		              		<div class="form-group">
								<label class="summary col-sm-3 control-label">开始日期</label>
								<div class="col-sm-7">
									<input type="text" name="startTime" placeholder="输入 开始日期" class="form-control startTime" value="">
								</div>
							</div>
							<div class="form-group">
								<label class="summary col-sm-3 control-label">结束日期</label>
								<div class="col-sm-7">
								<input type="text" name="endTime" placeholder="输入 结束时间" class="form-control endTime" value="">
								</div>
							</div>
							<div class="form-group">
			                  <label class="summary col-sm-3 control-label">活动描述</label>
			                  <div class="col-sm-7">
			                  	<textarea class="form-control" rows="2" placeholder="Enter ..."></textarea>
			                  </div>
			                </div>
		              	</div>
		              	<div class="col-lg-4">
							<div style="width:170px;height:230px;">
								<img id ="preView" src="<%=AppConfig.getPutVfsPrefix()%>${pGroup.imageURL}" style="width: 170px; height: 170px; padding:5px;border: 1px dashed #003366;">
								<div><a href="#" class="file_a">上传图片<input type="file" value="选择图片" class="filepath" name="file" onchange="previewFile();"></a></div>
							</div>
		              	</div>
	              	</div>
					<div class="clear"></div>
					<div class="row">
						<div class="form-group activeType col-lg-6">
		                  <label class="summary col-sm-5 control-label1">活动策略类型</label>
		                  <select name ="activeType" class="dis_select col-sm-7">
							<c:if test="${pGroup==null}">
								<option value="0">请选择策略类型</option>
								<option value="1">买赠活动</option>
								<!-- <option value="2">集客商品</option> -->
								<!-- <option value="3">每月赠送</option> -->
								<option value="4">加价购活动</option>
								<option value="5">套装活动</option>
								<option value="6">满送活动</option>
							</c:if>
							<c:if test="${pGroup.actiType==2||pGroup.actiType==4}">
								<option value="1" selected="selected">活动套装</option>
							</c:if>
							<c:if test="${pGroup.actiType==1}">
								<option value="2" selected="selected">集客商品</option>
							</c:if>
						</select>
		                </div>
		                <div class="form-group col-lg-6">
		                    <label class="summary col-sm-5 control-label1">发货方式</label>
		                	<select class="col-sm-7">
		                		<option>货到</option>
		                	</select>
		                </div>
		            </div>
		            <div class="clear"></div>			
					<div class="dis_block col-lg-12">
					<%--显示的地方 --%>
					<c:if test="${pGroup.actiType==2||pGroup.actiType==4}">
						<div>
							买<input class="buyNum" name="buyNum" type="number" value="${pGroup.buyNum==null?0:pGroup.buyNum}">
							送<input class="giveNum" name="giveNum" type="number" value="${pGroup.giveNum==null?0:pGroup.giveNum}">
						</div>
						<div class="product">
							<span>购买条件</span>
							<span class="addition">添加</span>
						</div>										
						<div>
							<select name="actType" class="cha_select">
								<c:if test="${actType==null}">
									<option value="pro">单品</option>
									<option value="ser">系列</option>
								</c:if>
								<c:if test='${actType=="ser"}'>
									<option value="ser" selected="selected">系列</option>
								</c:if>
								<c:if test='${actType=="pro"}'>
									<option value="pro" selected="selected">单品</option>
								</c:if>
							</select>	
							<div class="gro_div">
								<input type="hidden" class="pid">
								<input type="text" class="gro_text" placeholder="输入 条形码 或 产品名称">
								<c:forEach items="${goods}" var="good">
								<div style="width: 600px;">
									<input type='checkbox' name='products' value='${good.key}' checked='checked' onclick='return false;'>
									<span>${good.value}</span>
									<span class='del'>删除</span>
								</div>
								</c:forEach>
								<ul class="gro_ul">
								</ul>
							</div>
							<div class="clear"></div>
						</div>
						<div class="giveproduct">
							<span>赠送物品</span>
							<span class="addgift">添加</span>
						</div>
						<div>
							<select class="cha_select">
								<option value="pro">单品</option>
							</select>	
							<div class="gro_div">
								<input type="hidden" class="pid">
								<input type="text" class="gro_text" placeholder="输入 条形码 或 产品名称">
								<c:forEach items="${presents}" var="present">
								<div style="width: 600px;">
									<input type='checkbox' name='gifts' value='${present.key}' checked='checked' onclick='return false;'>${present.value}
									<span class='del'>删除</span>
								</div>
								</c:forEach>
								<ul class="gro_ul">
								</ul>
							</div>
							<div class="clear"></div>
						</div>
					</c:if>
					<c:if test="${pGroup.actiType==1}">
						<div class="product">
							<span>购买条件</span>
							<span class="addition">添加</span>
						</div>										
						<div>
							<select name="actType" class="cha_select">
								<option value="pro">单品</option>
							</select>	
							<div class="gro_div">
								<input type="hidden" class="pid">
								<input type="text" class="gro_text" placeholder="输入 条形码 或 产品名称">
								<c:forEach items="${goods}" var="good">
									<div style="width: 600px;">
										<input type='checkbox' name='products' value='${good.key}' checked='checked' onclick='return false;'>
										<span>${good.value}</span>
										<span class='del'>删除</span>
									</div>
								</c:forEach>
								<ul class="gro_ul">
								</ul>
							</div>
							<div class="clear"></div>
						</div>
						<div>折扣
							<input type="number" step="0.01" min="0" max="1" name="discount" value="${pGroup.discount}">
						</div>
						<div>是否走客户策略
							<select name="pricePolicy" style="width:100px; margin-left:10px;">
								<c:if test="${pGroup==null}">
									<option value="true">是</option>
									<option value="false">否</option>
								</c:if>
								<c:if test="${pGroup.pricePolicy==true}">
									<option value="true">是</option>
								</c:if>
								<c:if test="${pGroup.pricePolicy==false}">
									<option value="false">否</option>
								</c:if>
							</select>
						</div>
					</c:if>
					</div>
					<div class="sure">
						<input type="submit" class="saveActivity btn btn-primary" value="保存" 
						<c:if test="${pGroup!=null}">
						disabled="disabled"
						</c:if>
						/>
					
					<input type="button" class="cancel btn btn-primary" value="返回"/>
				</div>	
				</div>
				
				</div>
			</div>
		</div>		
	</sf:form>
	<div class="clear"></div>
	<div class="dis_option">
		<div id="1">
			<div class="row col-lg-12">
				<div class="form-group">
                	买<input class="buyNum" name="buyNum" type="number" value="${pGroup.buyNum==null?0:pGroup.buyNum}">件&nbsp;
				            送<input class="giveNum" name="giveNum" type="number" value="${pGroup.giveNum==null?0:pGroup.giveNum}">件
				</div>
            </div>
            <div class="row">
				<div class="form-group col-lg-6">
                	<lable class="control-label1">购买物品</lable>
                	<div>
                		<select name="actType" class="cha_select">
							<c:if test="${actType==null}">
								<option value="pro">单品</option>
								<option value="ser">系列</option>
							</c:if>
							<c:if test='${actType=="ser"}'>
								<option value="ser" selected="selected">系列</option>
							</c:if>
							<c:if test='${actType=="pro"}'>
								<option value="pro" selected="selected">单品</option>
							</c:if>
						</select>
						<button type="button" class="btn btn-success productadd">添加</button>
						<div id="checkproduct">
						<div class="clear"></div>
						<table class="producttable">
							<thead>
								<tr>
									<th>序号</th>
									<th>产品</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						</div>
                	</div>
				</div>
				<div class="form-group col-lg-6">
                	<lable class="control-label1">赠送物品</lable>
                	<div>
                		<select name="actType" class="cha_select">
							<c:if test="${actType==null}">
								<option value="pro">单品</option>
								<option value="ser">系列</option>
							</c:if>
							<c:if test='${actType=="ser"}'>
								<option value="ser" selected="selected">系列</option>
							</c:if>
							<c:if test='${actType=="pro"}'>
								<option value="pro" selected="selected">单品</option>
							</c:if>
						</select>
						<button type="button" class="btn btn-success productadd">添加</button>
						<div id="checkgift">
							<div class="clear"></div>
							<table class="producttable">
								<thead>
									<tr>
										<th>序号</th>
										<th>产品</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
                	</div>
				</div>
            </div>
		</div>
		<div id="2">
		<div class="product">
			<span>购买条件</span>
			<span class="addition">添加</span>
		</div>										
		<div>
			<select name="actType" class="cha_select">
				<option value="pro">单品</option>
			</select>	
			<div class="gro_div">
				<input type="hidden" class="pid">
				<input type="text" class="gro_text" placeholder="输入 条形码 或 产品名称">
				<ul class="gro_ul">
				</ul>
			</div>
			<div class="clear"></div>
		</div>
		<div>折扣
			<input step="0.01" type="number" min="0" max="1" name="discount" value="${pGroup.discount}">
		</div>
		<div>是否走客户策略
			<select name="pricePolicy" style="width:100px; margin-left:10px;">
				<c:if test="${pGroup==null}">
					<option value="true">是</option>
					<option value="false">否</option>
				</c:if>
				<c:if test="${pGroup.pricePolicy==true}">
					<option value="true">是</option>
				</c:if>
				<c:if test="${pGroup.pricePolicy==false}">
					<option value="false">否</option>
				</c:if>
			</select>
		</div>
	</div>
	<div id="3">						
		<div class="pro_a">
			<span>赠送物品</span>
			<span class="addition">添加</span>
		</div>
	</div>
	<div id="4">
		<div class="form-group col-lg-12">
			买<input class="buyNum" name="buyNum" type="number" value="${pGroup.buyNum==null?0:pGroup.buyNum}">件&nbsp;
			加<input type="number" value="0">元&nbsp;
			送<input class="giveNum" name="giveNum" type="number" value="${pGroup.giveNum==null?0:pGroup.giveNum}">件&nbsp;
		</div>
		<div class="row">
			<div class="form-group col-lg-6">
               	<lable class="control-label1">购买物品</lable>
               	<div>
               		<select name="actType" class="cha_select">
						<c:if test="${actType==null}">
							<option value="pro">单品</option>
							<option value="ser">系列</option>
						</c:if>
						<c:if test='${actType=="ser"}'>
							<option value="ser" selected="selected">系列</option>
						</c:if>
						<c:if test='${actType=="pro"}'>
							<option value="pro" selected="selected">单品</option>
						</c:if>
					</select>
					<button type="button" class="btn btn-success productadd">添加</button>
					<div id="checkproduct">
					<div class="clear"></div>
					<table class="producttable">
						<thead>
							<tr>
								<th>序号</th>
								<th>产品</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					</div>
               	</div>
			</div>
			<div class="form-group col-lg-6">
               	<lable class="control-label1">赠送物品</lable>
               	<div>
               		<select name="actType" class="cha_select">
						<c:if test="${actType==null}">
							<option value="pro">单品</option>
							<option value="ser">系列</option>
						</c:if>
						<c:if test='${actType=="ser"}'>
							<option value="ser" selected="selected">系列</option>
						</c:if>
						<c:if test='${actType=="pro"}'>
							<option value="pro" selected="selected">单品</option>
						</c:if>
					</select>
					<button type="button" class="btn btn-success productadd">添加</button>
					<div id="checkgift">
						<div class="clear"></div>
						<table class="producttable">
							<thead>
								<tr>
									<th>序号</th>
									<th>产品</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
               	</div>
			</div>
           </div>
	</div>
	<div id="5">
		<div class="form-group col-lg-12">
             <lable class="control-label1">套装产品 </lable>
             <div>
             	<select name="actType" class="cha_select">
					<c:if test="${actType==null}">
						<option value="pro">单品</option>
						<option value="ser">系列</option>
					</c:if>
					<c:if test='${actType=="ser"}'>
						<option value="ser" selected="selected">系列</option>
					</c:if>
					<c:if test='${actType=="pro"}'>
						<option value="pro" selected="selected">单品</option>
					</c:if>
				</select>
				<button type="button" class="btn btn-success productadd">添加</button>
				<div id="productgroup">
					<div class="clear"></div>
					<table class="producttable">
						<thead>
							<tr>
								<th>序号</th>
								<th>产品</th>
								<th>单价</th>
								<th>数量</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
       	 	</div>
		</div>
	</div>
	<div id="6">
		<div class="form-group col-lg-12">
			买满<input class="buyNum" name="buyNum" type="number" value="">元&nbsp;
			赠<input class="buyNum" name="buyNum" type="number" value="1">件
		</div>
		<div class="row">
			<div class="form-group col-lg-12">
               	<lable class="control-label1">赠送产品</lable>
               	<div>
               		<select name="actType" class="cha_select">
						<c:if test="${actType==null}">
							<option value="pro">单品</option>
							<option value="ser">系列</option>
						</c:if>
						<c:if test='${actType=="ser"}'>
							<option value="ser" selected="selected">系列</option>
						</c:if>
						<c:if test='${actType=="pro"}'>
							<option value="pro" selected="selected">单品</option>
						</c:if>
					</select>
					<button type="button" class="btn btn-success productadd">添加</button>
					<div id="givegroup">
					<div class="clear"></div>
					<table class="producttable">
						<thead>
							<tr>
								<th>序号</th>
								<th>产品</th>
								<th>数量</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					</div>
               	</div>
			</div>
        </div>
	</div>
	</div>
	<div class="example-modal">
        <div class="modal">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">产品选择</h4>
              </div>
              <div class="modal-body" id="modal_pro">
                	<table id="example" class="table table-bordered table-striped example">
                		<thead>
                			<tr>
                				<th>选择</th>
                				<th>产品编码</th>
                				<th>产品名称</th>
                				<th>系列</th>
                			</tr>
                		</thead>
                		<tbody>
                			<tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">玫瑰乳液1</td>
                				<td>玫瑰</td>
                			</tr>
                			<tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">玫瑰乳液2</td>
                				<td>玫瑰</td>
                			</tr>
                			<tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">玫瑰乳液3</td>
                				<td>玫瑰</td>
                			</tr>
                			<tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">玫瑰乳液4</td>
                				<td>玫瑰</td>
                			</tr>
                			<tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">玫瑰乳液5</td>
                				<td>玫瑰</td>
                			</tr>
                			<tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">玫瑰乳液6</td>
                				<td>玫瑰</td>
                			</tr>
                			<tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">玫瑰乳液7</td>
                				<td>玫瑰</td>
                			</tr>
                			<tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">玫瑰乳液8</td>
                				<td>玫瑰</td>
                			</tr><tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">玫瑰乳液9</td>
                				<td>玫瑰</td>
                			</tr><tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">玫瑰乳液10</td>
                				<td>玫瑰</td>
                			</tr><tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">玫瑰乳液11</td>
                				<td>玫瑰</td>
                			</tr><tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">玫瑰乳液12</td>
                				<td>玫瑰</td>
                			</tr><tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">玫瑰乳液13</td>
                				<td>玫瑰</td>
                			</tr><tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">玫瑰乳液14</td>
                				<td>玫瑰</td>
                			</tr>
                		</tbody>
                		<tfoot>
                			<tr>
                				<th>选择</th>
                				<th>产品编码</th>
                				<th>产品名称</th>
                				<th>系列</th>
                			</tr>
                		</tfoot>	
                	</table>
                </div>
                <div class="modal-body" id="modal_ser">
                	<table id="example1" class="table table-bordered table-striped example">
                		<thead>
                			<tr>
                				<th>选择</th>
                				<th>产品编码</th>
                				<th>系列名称</th>
                			</tr>
                		</thead>
                		<tbody>
                			<tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">玫瑰系列1</td>
                			</tr>
                			<tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">青瓜系列2</td>
                			</tr>
                			<tr>
                				<td><input type="checkbox" class="onecheck"></td>
                				<td>1111</td>
                				<td class="productname">山茶花系列>
                			</tr>
                		</tbody>
                		<tfoot>
                			<tr>
                				<th>选择</th>
                				<th>产品编码</th>
                				<th>系列名称</th>
                			</tr>
                		</tfoot>	
                	</table>
              </div>
              <div class="modal-footer">
              	<button type="button" class="btn btn-primary">确认选中</button>
                <button type="button" class="btn btn-primary btn-primary-close" data-dismiss="modal">取消选中</button>
              </div>
            </div>
            <!-- /.modal-content -->
          </div>
          <!-- /.modal-dialog -->
        </div>
        <!-- /.modal -->
      </div>
    <div>
	</body>
	<script>
		$(document).ready(function(){
			$(".onecheck").parent().css('width','50px');
			$(".example").DataTable({
				"ordering": false,
			});
			$(".productadd").click(function(){
			  var  cha_val = $(this).prev().find("option:selected").val();
			  var productid = $(this).next().attr('id');
		  	  $(".example-modal .modal").show();
		  	  if(cha_val == "pro"){
		  		  $("#modal_pro").show();
		  	  }else{
		  		$("#modal_ser").show();
		  	  }
		  	  $(".onecheck").val(productid);
		  	  $(".onecheck").removeAttr("checked");
			  $(".btn-primary").click(function(){
					$(".onecheck").each(function(){	
					if($(this).is(':checked')){
							var product = $(this).parent().parent().find(".productname").text();
							if($(this).val() == "productgroup"){
								$("#"+$(this).val()).find("table tbody").append("<tr><td class='num'></td><td class='productleft'>"+product+"</td><td><input type='number' value='0'></td><td><input type='number' value='1'></td><td><span class='delproduct'>删除</span></td></tr>");
							}else if($(this).val() == "givegroup"){
								$("#"+$(this).val()).find("table tbody").append("<tr><td class='num'></td><td class='productleft'>"+product+"</td><td><input type='number' value='1'></td><td><span class='delproduct'>删除</span></td></tr>");
							}else{
								$("#"+$(this).val()).find("table tbody").append("<tr><td class='num'></td><td class='productleft'>"+product+"</td><td><span class='delproduct'>删除</span></td></tr>");
							}
						}
					});
					$(".onecheck").removeAttr("checked");
					$(".delproduct").on('click',function(){
						$(".onecheck").removeAttr("checked");
						$(this).parent().parent().remove();
					});
					$(".example-modal .modal").hide();
					$(".producttable .num").each(function(){
				  		$(this).text($($(this).parent().parent().find(".num")).index(this)+1);
					});
				});
		    });
		  	$(".example-modal .modal").on("click",".example tr",function(){
		  		if($(this).find(".onecheck").is(':checked')){
		  			$(this).find(".onecheck").removeAttr("checked");
		  		}else{
		  			$(this).find(".onecheck").attr("checked",true);
		  		}
		  	});
		    $(".close,.btn-primary-close").click(function(){
		      $(".onecheck").removeAttr("checked");
		  	  $(".example-modal .modal").hide();
		    });
			var wh= $(window).height()
			 $(".div_body").css("min-height",wh+"px");
			if($("#hidden1").val()!=null&&$("#hidden1").val()!=""){
				$(".hidden_body2").empty();
				$(".hidden_body").show();
				$(".hidden_body2").html($("#hidden1").val());
				$(".hidden_body").hide(5000); 
			}
			//输入框显示商品
			$(".gro_div").on("keyup paste change",".gro_text",function(){
				var value = $(this).parent().prev().find("option:selected").val();
				var ul = $(this).siblings(".gro_ul");
				var pname = $(this).val();
				ul.empty();
				if("pro"==value){
					$.ajax({
						type:"post",
						url:"../productgroup/getproducts.do",
						data:{pName:pname},
						async:true,
						success: function(data) {
							$.each(data,function(i,product){
								ul.append("<li value='"+product.productId+"'>"+product.name+"</li>");
							});
							ul.show();
						}
					});
				}
				if("ser"==value){
					$.ajax({
						type:"post",
						url:"../productgroup/getseriesbyname.do",
						data:{pName:pname},
						async:true,
						success: function(data) {
							$.each(data,function(i,series){
								ul.append("<li value='"+series.hardCode+"'>"+series.name+"</li>");
							});
							ul.show();
						}
					});
				}
			});
			
			//删除每行
			$(".gro_div").on("click",".del",function(){
				$(this).parent().remove();
			});
			//添加商品
			$(".product").on("click",".addition",function(){
				var pid= $(this).parent().next().find("input[class='pid']").val();
				var name= $(this).parent().next().find("input[class='gro_text']").val();
				var showP = $(this).parent().next().find(".gro_div");
				if(pid==""||name==""){
					return;
				}
				s = "<div style='width: 600px;'>"
					+"<input type='checkbox' name='products' value='"+pid
					+"' checked='checked' onclick='return false;'>"
					+"<span>"+name
					+"</span><span class='del'>删除</span></div>";
				showP.append(s);
			});
			//添加赠品
			$(".giveproduct").on("click",".addgift",function(){
				var pid= $(this).parent().next().find("input[class='pid']").val();
				var name= $(this).parent().next().find("input[class='gro_text']").val();
				var showP = $(this).parent().next().find(".gro_div");
				if(pid==""||name==""){
					return;
				}
				s = "<div style='width: 600px;'>"
					+"<input type='checkbox' name='gifts' value='"+pid
					+"' checked='checked' onclick='return false;'>"
					+"<span>"+name
					+"</span><span class='del'>删除</span></div>";
				showP.append(s);
			});
			
			//li点击事件
			$(".gro_ul").on("click","li",function(){
				var pid = $(this).val();
				var text = $(this).text();
				$(this).parent().parent().find("input[class='pid']").val(pid);
				$(this).parent().parent().find("input[class='gro_text']").val(text);
			});
			
			//获取焦点时候显示
			$(".gro_text").focus(function(){
				$(this).parent().find(".gro_ul").css("display","block");
			});
			
			//actType按钮发生改变时候事件
			$(".cha_select").mouseover(function(){
				$(this).parent().css("position","relative")
				$(this).parent().append("<div class='alert'>如果你改变所选，您之前的选择将被清空</div>");
			})
			$(".cha_select").mouseout(function(){
				$(this).parent().find(".alert").remove();
			})
			$(".cha_select").change(function(){
				/* var div = $(this).parent().find(".gro_div").find("div");
				div.remove(); */
				$("#modal_pro").hide();
		  		$("#modal_ser").hide();
		  		$(this).parent().find(".producttable tbody tr").remove();
			});
			//返回
			$(".cancel").click(function(){
				window.close();
			});
			
			//保存
		 	$(".saveActivity").click(function(){
		 		var activeType = $('select[name="activeType"]').find("option:selected").val();
		 		var parent = $(this).parent().parent();
		 		var actName = parent.find(".actName").val();
				var startTime = parent.find(".startTime").val();
				var endTime = parent.find(".endTime").val();
				var description = parent.find(".description").val();
				var imageURL = parent.find(".filepath").val()
				
				if(actName==""||actName==null){
					alert("活动策略名称不能为空!");
					return false;
				}
				if(startTime==""||startTime==null){
					alert("活动 开始时间 不能为空!");
					return false;
				}
				if(endTime==""||endTime==null){
					alert("活动 结束时间 不能为空!");
					return false;
				}
				if(description==""||description==null){
					alert("活动 描述 不能为空!");
					return false;
				}
				if(imageURL==""||imageURL==null){
					alert("活动 图片 不能为空!");
					return false;
				}
				if(0==activeType){
					alert("请选择活动策略类型！");
					return false;
				}
				if(1==activeType){
					var buyNum = $("#1").find(".buyNum").val();
					var giveNum = $("#1").find(".giveNum").val();
					var actType = $("#1").find(".product").next().find("option:selected").val();
					var products = $("#1").find(".product").next().find("input:checkbox[name='products']:checked");
					var gifts = $("#1").find(".giveproduct").next().find("input:checkbox[name='gifts']:checked");
					
					if(buyNum==""||buyNum==null||buyNum<=0){
						alert("亲,购买数量不正确哟!");
						return false;
					}
					if(giveNum==""||buyNum==null||giveNum<0){
						alert("亲,赠送不正确哟!");
						return false;
					}
					if(products==""||products==null||products.length<=0){
						alert("亲,请 选择需要购买的商品 哟!");
						return false;
					}
					if(gifts==""||gifts==null||gifts.length<=0){
						alert("亲,请 赠送商品 哟!");
						return false;
					}
				}
				if(2==activeType){
					var products = $("#2").find(".product").next().find("input:checkbox[name='products']:checked");
					var discount = $("#2").find('input[name="discount"]').val();
					var pricePolicy = $("#2").find('select[name="pricePolicy"]').find('option:selected').val();
					//alert("products:"+products.length+",discount:"+discount+",pricePolicy:"+pricePolicy);
					if(0==products.length){
						alert("请选择产品！");
						return false;
					}
					if(""==pricePolicy||null==pricePolicy||pricePolicy<0||pricePolicy>1){
						alert("折扣范围(0~1)");
						return false;
					}
				}
				if(3==activeType){
					return false;
				}
				return true;
			});
			
			$('.startTime').fdatepicker({
				format: 'yyyy-mm-dd hh:ii',
				pickTime: true
			});
			$('.endTime').fdatepicker({
				format: 'yyyy-mm-dd hh:ii',
				pickTime: true
			});
			$('.dis_select').change(function(){
				var disHTML = $('.dis_option').find("#"+$(this).val()).clone(true);
			 	$('.dis_block').html(disHTML);
				var select = $(this).val();
			});
		});
		
		//图片预览
		 function previewFile() {
			 var preview = $("#preView").attr('src');
			 var file  = document.querySelector('input[type=file]').files[0];
			 var reader = new FileReader();
			 reader.onloadend = function () {
				 $("#preView").attr("src",reader.result);
			 }
			 if (file) {
			  reader.readAsDataURL(file);
			 } else {
			  preview.src = "";
			 }
		}
	</script>
</html>