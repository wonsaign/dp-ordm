<%
/* *
 *功能：民生付
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 **********************************************
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "javax.servlet.http.HttpServlet" %>
<%@ page import = "javax.servlet.http.HttpServletRequest" %>
<%@ page import = "javax.servlet.http.HttpServletResponse" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@page import="java.io.File"%>
<% request.setCharacterEncoding("utf-8"); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>民生付即时到账交易接口</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"> 
</head>
<link href="../css/index.css?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"css/index.css").lastModified()%>" rel="stylesheet" />
<style>
    html {
        width:100%;
        height:100%;
        padding:0;
        margin:0;
        font-family:"微软雅黑";
        background-image: url("../img/cmbc.png");
		   	background-size: cover;
		   	background-repeat: no-repeat;
		   	vertical-align: middle;
    }
    body{
   	    width: 100%;
		    height: 100%;
		    position: absolute;
		    margin: 0px;
    }
    .header {
        width:100%;
        margin:0 auto;
        height:auto;
        background-color:#fff
    }
    .container {
        width:100%;
        min-width:100px;
        height:auto
    }
    .black {
        background-color:#242736
    }
    .blue {
        background-color:#0ae
    }
    .qrcode {
        margin:0 auto;
        height:30px;
        background-color:#242736
    }
    .littlecode {
        width:16px;
        height:16px;
        margin-top:6px;
        cursor:pointer;
        float:right
    }
    .showqrs {
        top:30px;
        position:absolute;
        width:100px;
        margin-left:-65px;
        height:160px;
        display:none
    }
    .shtoparrow {
        width:0;
        height:0;
        margin-left:65px;
        border-left:8px solid transparent;
        border-right:8px solid transparent;
        border-bottom:8px solid #e7e8eb;
        margin-bottom:0;
        font-size:0;
        line-height:0
    }
    .guanzhuqr {
        text-align:center;
        background-color:#e7e8eb;
        border:1px solid #e7e8eb
    }
    .guanzhuqr img {
        margin-top:10px;
        width:80px
    }
    .shmsg {
        margin-left:10px;
        width:80px;
        height:16px;
        line-height:16px;
        font-size:12px;
        color:#242323;
        text-align:center
    }
    .nav {
        margin:0 auto;
        height:70px;
    }
    .open,.logo {
        display:block;
        float:left;
        height:40px;
        width:85px;
        margin-top:20px
    }
    .divier {
        display:block;
        float:left;
        margin-left:20px;
        margin-right:20px;
        margin-top:23px;
        width:1px;
        height:24px;
        background-color:#d3d3d3
    }
    .open {
        line-height:30px;
        font-size:20px;
        text-decoration:none;
        color:#1a1a1a
    }
    .navbar {
        float:right;
        width:200px;
        height:40px;
        margin-top:15px;
        list-style:none
    }
    .navbar li {
        float:left;
        width:100px;
        height:40px
    }
    .navbar li a {
        display:inline-block;
        width:100px;
        height:40px;
        line-height:40px;
        font-size:16px;
        color:#1a1a1a;
        text-decoration:none;
        text-align:center
    }
    .navbar li a:hover {
        color:#00AAEE
    }
    .title {
        margin:0 auto;
        height:80px;
        line-height:80px;
        font-size:20px;
        color:#FFF;
        padding:0px 20%;
    }
    .content {
        width:100%;
        height:100%;  
        position: absolute; 
    }
    .alipayform {
        width:600px;
        margin:0px auto;
        height:400px;
        border-radius: 15px;
        background: white;
        box-shadow: 5px 5px 5px #ccc;
        position: absolute;
    		top: calc(50% - 200px);
    		left: calc(50% - 300px);
    		border:1px solid rgba(204, 204, 204, 0.65);
    }
    .element {
        width:calc(100% - 20px);
        line-height:50px;
        margin:10px auto;
        font-size:20px;
        text-align: center;
        font-size: 16px;
    }
    .etitle,.einput {
        float:left;
        line-height:50px
    }
    .etitle {
        width:100px;
        line-height:50px;
        text-align:left;
        padding-left:50px;
    }
    .einput {
        width:calc(100% - 170px);
      	text-align: left;
    }
    .einput input {
        width:80%;
        height:24px;
        border:1px solid #0ae;
        font-size:16px;
        max-width:400px;
    }
    .mark {
        margin-top: 10px;
        width:500px;
        height:30px;
        margin-left:80px;
        line-height:30px;
        font-size:12px;
        color:#999
    }
    .legend {
        font-size:24px;
        color: #6dbb44;
    }
    .alisubmit {
        width:60%;
        height:40px;
        border:0;
        background-color:#6dbb44;
        font-size:16px;
        color:#FFF;
        cursor:pointer;
        margin:30px auto;
        max-width:400px;
        border-radius: 5px;
    }
    .footer {
        width:100%;
        height:120px;
        background-color:#242735
    }
    .sureform{
    	width:80px;
    	height:30px;
    	margin-top:20px;
    }
    .etitle1{
       padding:10px 20px;
    }
    .cmbcdiv{
    	text-align: left;
    }
    @media screen and (max-width:800px) {
    	.alipayform{
    			width:calc(100% - 2px);
    			height:auto;
    			left: 0px;
    	}
    }
</style>
<body>
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
    <div class="content">
        <div class="alipayform">
        		<input type="hidden" value="" name="channel">
            <div class="element">
                <div class="legend">民生付即时到账交易接口快速通道 </div>
            </div>
            <div class="element">
                <div class="etitle">单号:</div>
                <div class="einput"><span style="font-size:16px;">${DSResponse.data.orderNo }</span>
                <input type="hidden" name="orderID" id="out_trade_no" value="${DSResponse.data.orderNo }" readonly></div>
                <br>
            </div>
            <div class="element">
                <div class="etitle">金额:</div>
                <div class="einput"><span style="font-size:20px;color:red">${DSResponse.data.payable}</span>
                <input type="hidden" name="txAmt" value="${DSResponse.data.payable}" readOnly></div>
                <br>
            </div>
						<div class="element">
                <div class="etitle">备注:</div>
                <div class="einput"><input type="text" name="remark" ></div>
                <br>
            </div>
            <div class="element">
                <input type="submit" class="alisubmit" value ="确认支付">
            </div>
            </div>
    </div>
</body>
<script src="../js/jquery.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/jquery.js").lastModified() %>"></script>
<script src="../js/index.js?ts=<%=new File(request.getSession().getServletContext().getRealPath("/")+"js/index.js").lastModified()%>"></script>
<script type="text/javascript">
$(document).ready(function(){
    var userAgentInfo = navigator.userAgent;
    var Agents = ["Android", "iPhone",
                "SymbianOS", "Windows Phone",
                "iPad", "iPod"];
    var flag = true;
    for (var v = 0; v < Agents.length; v++) {
        if (userAgentInfo.indexOf(Agents[v]) > 0) {
            flag = false;
            break;
        }
    }
    if(flag==true){
    	$("input[name='channel']").val(0);
    }else{
    	$("input[name='channel']").val(2);
    }
    $(".alisubmit").click(function(){
    	var orderNo=$("input[name='orderID']").val();
    	var txAmt=$("input[name='txAmt']").val();
    	var channel=$("input[name='channel']").val();
    	var remark=$("input[name='remark']").val();
    	$.ajax({
     		type : "post",
     		url : "../cmbc/confirm.do",
     		async : false,
     		timeout : 10000,
     		dataType:"json",
     		cache:false,
     		data:{orderNo:orderNo,txAmt:txAmt,channel:channel,remark:remark},
     		error : function(XMLHttpRequest, textStatus, errorThrown) {
     			alert('fail');
     		},
     		success : function(data) {
     			$(".message_body").css("display","block");
     			$(".message_top").text("确认订单支付");
     			$(".message_block2").append("<div class='cmbcdiv'><span class='etitle1'>订单号:</span><span>"+orderNo+"</span></div>"
     					+"<div class='cmbcdiv'><span class='etitle1'>金额:</span><span>"+txAmt+"</span></div>"
     					+"<div class='cmbcdiv'><span class='etitle1'>备注:</span><span>"+remark+"</span></div>"
     					+"<form action='https://pay.cmbc.com.cn/epay/cmbcpay.do' method='post'><input type='hidden' name='orderinfo' value='"+data.data+"'><input type='submit' class='bg-red sureform' value='确认'></form>");
     		}
    		});
    })
})
</script>

</html>