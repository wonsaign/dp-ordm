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
<input type="hidden" name="imagepath" value="<%=AppConfig.getVfsPrefix() %>"> 
<div class="shop_div shop_hover">
	<div class="text-center counters">门店:<span class="counters_one"></span><input type="hidden" value="0"></div>
 <div>
		<%if(counters.size()<2){ %>
		 	<%for(int i = 0;i < counters.size();i++) {%>
			<% Counter counter = counters.get(i); %>
			<label class="shoplabel">
			<input type="radio" name="shopselect" value="<%=counter.getCounterCode() %>" 
				data-id="<%=counter.getCounterId() %>" selected >
				<span class="counterName"><%=counter.getCounterName() %></span>
			</label>
		<%		}
		 	}
		 %>
		<%if(counters.size()>1){%>
		<%if(cs==null){%> 
			<label class="shoplabel">
				<input type="radio" name="shopselect" value="0" 
				data-id="0" selected >
				<span class="counterName">请选择门店</span>
			</label>
		 <%} %>
			<% for(int i = 0;i < counters.size();i++) {%>
			<% Counter counter = counters.get(i); %>
			<label class="shoplabel">
				<input type="radio" name="shopselect" value="<%=counter.getCounterCode() %>" 
				data-id="<%=counter.getCounterId() %>" 
				<%=(cs!=null && cs.getCounterId()==counter.getCounterId() )?"checked":"" %> >
				<i></i>
				<span class="counterName"><%=counter.getCounterName() %></span>
			</label>
		<%
				}
			} 
		%>
	</div>
	<div class="text-center"><button class="bg-red sure_counters">确定</button></div>
</div>
<div class="icon1">
		<div class="select_text">
		<%if(cs==null){%> 
			<span class="span_text">请选择门店</span><input type="hidden" value="0" data-id="0">
		<%}else{ %>
			<span class="span_text"><%=cs.getCounterName() %></span><input type="hidden" value="<%=cs.getCounterId() %>" data-id="<%=cs.getCounterId() %>">
		<%} %>		
		</div>
		<div class="icon1_text"></div>
		<div class="message_img" id="message"><a href="../msg.do"><img src="../img/message.png"></a></div>
		<div class="stock_img"><a href="../microservice/mobilestock.do"><img src="../img/stock.png"></a></div>
</div>
<div class="icon1">
		<div class="icon1_back"><img src="../img/icon1_back.png"></div>
		<div class="icon1_text"></div>
</div>
	<%-- <div class="top">
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
	</div>--%>
	<input name="stockId" type="hidden" value="${stockId}">
	<div class="top">
		<table class="nav">
			<tr>
				<th class="header"><a href="${pageContext.request.contextPath}/ordm/index.do"><img src="../img/ico1.png"></a></th>
				<th class="header"><a href="${pageContext.request.contextPath}/product/product.do"><img src="../img/icoc2.png"></a></th>
				<th class="header shopping_block"><a href="${pageContext.request.contextPath}/cart/cart.do"><img src="../img/icoc4.png"></a></th>
				<th class="header"><a href="${pageContext.request.contextPath}/order/index.do"><img src="../img/icoc3.png"></a></th>		
				<th class="header"><a href="${pageContext.request.contextPath}/user/info.do"><img src="../img/icoc5.png"></a></th>
			</tr>
		</table>
	</div>
	<div class="top_hide"></div>
	<%-- <div class="right_position">
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
	</div> --%>
	<%-- <div class="message_hide">
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
	</div> --%>
	<div class="message_modal">
			<div class="message_body" id="message_modal">
					<div class="message_block2">			
					</div>
			</div>
	</div>