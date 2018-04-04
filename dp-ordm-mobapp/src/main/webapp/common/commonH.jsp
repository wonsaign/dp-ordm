<%@ page import="java.util.List" %>
<%@ page import="com.zeusas.dp.ordm.controller.OrdmBasicController" %>
<%@ page import="com.zeusas.dp.ordm.entity.Counter" %> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
   
<%
HttpSession s = request.getSession();
List<Counter> counters = (List<Counter>) s.getAttribute(OrdmBasicController.ALL_COUNTERS); 
Counter cs=  (Counter)s.getAttribute(OrdmBasicController.ORDERCOUNTER);
%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
	<div class="icon1">
		<div class="icon_img">
			<img src="img/3.png">
			<span>植物医生直发系统</span>
		</div>
		<div class="right_div">
			<div class="user_div">
				<div class="shop_div">
					<span style="color:#0e5993"><a href="../user/info.do"><%=request.getRemoteUser()%></a></span>|
					<a href="logout.do" style="text-decoration: none;"><span style="color:red">退出</span></a>
				</div>
				<%-- <div class="shop_div shop_hover">
					<div class="shop_one">店铺</div>
					<ul class="shop">
						<c:forEach items="${shops }" var="shops">
							<li><a class="countername">${shops.counterName }</a><input name="counterCode" type="hidden" value="${shops.counterCode }"></li>
						</c:forEach>
						<div class="clear"></div>
					</ul>
				</div>	 --%>
				<div class="shop_div shop_hover">
					 <select name="shopselect">
						 <c:if test="${fn:length(shops)<2 }">
							<c:forEach items="${shops }" var="shops">
								<option data-code="${shops.counterCode }">${shops.counterName }</option>
							</c:forEach>
						 </c:if>
						 <c:if test="${fn:length(shops)>1 }">
								<option style="color:red;"><%=cs==null?"店铺":cs.getCounterName() %></option>
								<c:forEach items="${shops }" var="shops">
									<option class="countername" data-code="${shops.counterCode }">${shops.counterName }</option>
								</c:forEach>
						 </c:if>
					</select> 			
				</div>	
				<div class="shop_div">
					<img style="float:left" src="img/message.gif">
					<span class="mess cl-red" id="message">消息</span>
					<span class="mess cl-red">
					<a href="${pageContext.request.contextPath}/order/index.do">
						待处理订单
						</a>
					</span>
					<div class="clear"></div>
				</div>
				<div class="clear"></div>
			</div>
		</div>		
		<div class="clear"></div>
	</div>
	<div class="top">
		<ul class="nav">
			<li><a href="${pageContext.request.contextPath}/index.do">首页</a></li>
			<li><a href="${pageContext.request.contextPath}/product/product.do">订货</a></li>
			<li><a href="${pageContext.request.contextPath}/order/index.do">订单</a></li>
			<li><a href="${pageContext.request.contextPath}/user/info.do">用户</a></li>
			<li><a href="${pageContext.request.contextPath}/user/setting.do">设置</a></li>
		</ul>
	</div>
	<div class="shopping_block">
		<a href="cart/cart.do"><img src="img/cart.png"/></a>
	</div>
	<div class="top_back">
		<img src="img/top_back.png"/>
	</div>
	<div class="message_block">
		<div class="message_close"><img src="img/close.png"></div>
		<div class="message_top">消息</div>
		<div>购物消息</div>
		<div>
			<ol>
				<li>1111</li>
			</ol>
		</div>
		<div>系统消息</div>
		<div>
			<ol>
				<c:forEach items="${notify }" var="n">
				<li>${n.content }</li>
				</c:forEach>
			</ol>	
		</div>
	</div>