<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %> 
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.zeusas.core.utils.*" %>
<%@ page import="com.zeusas.dp.ordm.controller.OrdmBasicController" %>
<%@ page import="com.zeusas.dp.ordm.entity.Counter" %> 
<%@ page import="com.zeusas.dp.ordm.entity.Notification" %> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%
HttpSession s = request.getSession();
List<Counter> counters = (List<Counter>) s.getAttribute(OrdmBasicController.ALL_COUNTERS); 
Counter cs=  (Counter)s.getAttribute(OrdmBasicController.ORDERCOUNTER);
List<Notification> notifications = (List<Notification>) s.getAttribute(OrdmBasicController.NOTIFICATION);
%>
<script type="text/javascript">
var ms_host="<%=AppConfig.getConfig("ms_host")%>";
var ms_proto="<%=AppConfig.getConfig("ms_proto")%>";
</script>
<div class="icon1">
		<input name="stockId" type="hidden" value="${stockId}">
		<div class="icon_img">
			<img src="../img/3.png">
			<span>植物医生直发系统</span>
		</div>
		<div class="right_div">
			<div class="user_div">
				<div class="shop_div">
					<span style="color:#0e5993"><%=request.getRemoteUser()%></span>|
					<a href="../logout.do" style="text-decoration: none;"><span style="color:red">退出</span></a>
				</div>
				<div class="shop_div shop_hover">
					<select name="shopselect">
						<%if(counters.size()<2){
						 	for(int i = 0;i < counters.size();i++) {%>
							<% Counter counter = counters.get(i); %>
							<option value="<%=counter.getCounterCode() %>" 
								data-id="<%=counter.getCounterId() %>" selected >
								<%=counter.getCounterName() %>
							</option>
						<%		}
						 	}
						 %>
						<%if(counters.size()>1){%>
							<%if(cs==null){%> <option value="-1" data-id="-1">请选择门店</option> <%} %>
							<% for(int i = 0;i < counters.size();i++) {%>
							<% Counter counter = counters.get(i); %>
							<option value="<%=counter.getCounterCode() %>" 
								data-id="<%=counter.getCounterId() %>" 
								<%=(cs!=null && cs.getCounterId()==counter.getCounterId() )?"selected":"" %> >
								<%=counter.getCounterName() %>
							</option>
						<%
								}
							} 
						%>
					</select>					
				</div> 
				<div class="shop_div">
					<span class="mess cl-red" id="newpre">预定</span>
					<div class="clear"></div>
				</div>
				<div class="shop_div">
					<img style="float:left" src="../img/message.gif">
					<span class="mess cl-red" id="message">消息</span>
					<div class="clear"></div>
				</div>
				<div class="clear"></div>
			</div>
		</div>
		
		<div class="clear"></div>
	</div>
	<div class="top">
		<table class="nav">
			<tr>
				<th class="header"><a href="${pageContext.request.contextPath}/ordm/index.do">首页</a></th>
				<th class="header"><a href="${pageContext.request.contextPath}/product/product.do">订货</a></th>
				<th class="header"><a href="${pageContext.request.contextPath}/order/index.do">订单</a></th>
				<th class="header"><a href="${pageContext.request.contextPath}/user/info.do">用户</a></th>
				<shiro:hasAnyRoles name="root,adm,13,14"> 
				<th class="header"><a href="${pageContext.request.contextPath}/user/setting.do">设置</a></th>
				</shiro:hasAnyRoles>
			</tr>
		</table>
	</div>
	<div class="top_hide"></div>
	<div class="right_position">
		<div href="#" class="body_back">
			<div class="img_block"><a href='javascript:history.go(-1)'><img src="../img/action_back.png"></a></div>
			<div class="left_right">后退</div>
		</div>
		<div class="shopping_block">
			 <div class="img_block">
			 	<a href="../cart/cart.do"><img src="../img/cart.png"/></a>
			 </div>
			 <div class="left_right">购物车</div>
		</div>
		<div class="shopping_block1">
			 <div class="img_block stock_img">
			 	<img src="../img/stock.png"/>
			 </div>
			 <div class="left_right">库存</div>
		</div>
		<div class="shopping_block1 shopact">
			 <div class="img_block">
			 	<a href="${pageContext.request.contextPath}/ordm/index.do#act"><img src="../img/top_imgy.png"/></a>
			 </div>
			 <div class="left_right">活动</div>
		</div> 
		<div class="top_back">
			<div class="img_block"><img src="../img/top_back.png"/></div>
			<div class="left_right">顶部</div>
		</div>
	</div>
	<div class="message_hide">
	<c:if test="${all_order_size!=null}">
		<div style="text-align: left;font-size: 14px; font-weight:bold;">订单消息</div>
		<div class="massage_show">
		<c:forEach items="${all_order_size}" var="m">
		<shiro:hasAnyRoles name="root,adm,14,12">
		<c:if test="${m.key=='owner'}">
			<a style="text-decoration: none;font-weight: bold;" href="../order/index.do">
				<span>待审核订单</span>
				(<span style="color:red;">${m.value}</span>)
			</a>
		</c:if>
		</shiro:hasAnyRoles>
		<shiro:hasAnyRoles name="root,adm,14,12,11">
		<c:if test="${m.key=='unPay'}">
			<a style="text-decoration: none;font-weight: bold;" href="../order/index.do">
				<span>待付款订单</span>
				(<span style="color:red;">${m.value}</span>)
			</a>
		</c:if>
		</shiro:hasAnyRoles>
		<shiro:hasAnyRoles name="root,adm,14,12,11,10">
		<c:if test="${m.key=='shipping'}">
			<a style="text-decoration: none;font-weight: bold;" href="../order/index.do">
				<span>待收货订单</span>
				(<span style="color:red;">${m.value}</span>)
			</a>
		</c:if>
		</shiro:hasAnyRoles>
		<shiro:hasAnyRoles name="root,adm,14,12">
		<c:if test="${m.key=='refuse'}">
			<a style="text-decoration: none;font-weight: bold;" href="../order/index.do">
				<span>财务退回单</span>
				(<span style="color:red;">${m.value}</span>)
			</a>
		</c:if>
		</shiro:hasAnyRoles>
		</c:forEach>
		</div>
	</c:if>	
		<div style="text-align: left; font-size: 14px; font-weight:bold;">系统消息</div>
		<div>
			<ol>
			<%if(notifications!=null) {%>
				<% for(int i = 0;i<notifications.size();i++){ %>
				<%Notification n = notifications.get(i); %>
					<li><%=n.getContent() %></li>
			<%
				}
			} 
			%>
			</ol>
		</div>
	</div>
	<div class="message_modal">
			<div class="message_body">
					<div class="message_block">
						<div class="message_top">消息</div>
						<div class="message_close"><img src="../img/close.png"></div>
						<div class="message_block2">			
						</div>
					</div>
			</div>
	</div>
	<div class="message_modal">
			<div class="message_body1">
					<div class="message_blocktwo">
						<div class="message_top">提示</div>
						<div class="message_blocktwo2">	
								
						</div>
					</div>
			</div>
	</div>
	<div class="stock_div">
		<div class="tab-content">
			<ul id="topTab" class="topnav">
				<li class="active">
					<a href="#bage1" data-toggle="tab" id="11386">正品</a>
				</li>
				<li>
					<a href="#bage2" data-toggle="tab" id="11388">赠品</a>
				</li>
				<li>
					<a href="#bage3" data-toggle="tab" id="11389">物料</a>
				</li>
				<div class="clear"></div>
			</ul>
			<div class="tab-head"></div>
			<div class="tab-pane fade active in" id="bage">
					<table class="stock_table" border="1" cellspacing="0">
								<thead>
									<tr>
										<th>产品名称</th>
										<th>规格</th>
										<th>库存量</th>
									</tr>
								</thead>
								<tbody class="stock_tbody">
								</tbody>
					</table>
			</div>
		</div>	
	</div>
	<div class="newpre_div">
		<div class="tab-content">
			<ul id="new_Tab" class="topnav">
				<li class="active">
					<a href="#bage1" data-toggle="tab" id="1">打欠</a>
				</li>
				<li>
					<a href="#bage2" data-toggle="tab" id="2"><span class="me">我的</span>欠货</a>
				</li>
				<li>
					<a href="#bage3" data-toggle="tab" id="3">待发货</a>
				</li>
				<li>
					<a href="#bage4" data-toggle="tab" id="4">已发货</a>
				</li>
				<div class="clear"></div>
			</ul>
			<div class="tab-head"></div>
			<div class="tab-pane fade active in" id="newpre_bage">
					<table class="stock_table" border="1" cellspacing="0">
								<thead>
								</thead>
								<tbody class="stock_tbody">
								</tbody>
					</table>
			</div>
		</div>	
	</div>