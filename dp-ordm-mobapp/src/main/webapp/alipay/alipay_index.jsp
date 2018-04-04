<%
/* *
 *功能：支付宝即时到账交易接口调试入口页面
 *版本：3.4
 *日期：2016-03-08
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 **********************************************
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>支付宝即时到账交易接口</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
</head>

<style>
    html,body {
        width:100%;
        height:auto;
        padding:0;
        margin:0;
        font-family:"微软雅黑";
        background-color:#242736
    }
    .header {
        width:100%;
        margin:0 auto;
        height:230px;
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
        width:100%;
        margin:0 auto;
        height:30px;
        background-color:#242736
    }
    .littlecode {
        width:16px;
        height:16px;
        margin-top:6px;
        cursor:pointer;
        float:right;
        margin-right: 5px;
    }
    .showqrs {
        top:30px;
        position:absolute;
        width:100px;
        margin-left:-85px;
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
        width:calc(100% - 20px);
        margin:0 auto;
        height:70px;
        padding:0px 10px;
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
        width:calc(100% - 20px);
        margin:0 auto;
        height:80px;
        line-height:80px;
        font-size:20px;
        color:#FFF;
        padding:0px 10px;
    }
    .content {
        width:100%;
        height:660px;
        background-color:#fff;      
    }
    .alipayform {
        width: calc(100% - 2px);
        margin:0 auto;
        height:600px;
        border:1px solid #0ae
    }
    .element {
        height:auto;
        font-size:16px;
        margin:15px 10px;
        text-align: center;
    }
    .etitle,.einput {
        float:left;
        height:26px
    }
    .etitle {
        padding-left:10px;
        line-height:26px;
        text-align:right
    }
    .einput {
        margin-left:20px;
        text-align: left;
        
    }
    .einput input {
        height:24px;
        border:1px solid #0ae;
        font-size:16px
    }
    .mark {
        margin: 10px 0px 0px 10px;
        width:100%;
        height:30px;
        line-height:30px;
        font-size:12px;
        color:#999;
        text-align: left;
    }
    .legend {
        font-size:18px
    }
    .alisubmit {
        width:calc(80% - 2px);
        height:40px;
        border:0;
        background-color:#0ae;
        font-size:16px;
        color:#FFF;
        cursor:pointer;
        margin:10px 10%;
    }
    .footer {
        width:100%;
        height:120px;
        background-color:#242735
    }
    .footer-sub a,span {
        color:#808080;
        font-size:12px;
        text-decoration:none
    }
    .footer-sub a:hover {
        color:#00aeee
    }
    .footer-sub span {
        margin:0 3px
    }
    .footer-sub {
        padding-top:40px;
        height:20px;
        margin:0 auto;
        text-align:center
    }
</style>
<body>
    <div class="header">
        <div class="container black">
            <div class="qrcode">
                <div class="littlecode">
                    <img width="16px" src="img/little_qrcode.jpg" id="licode">
                    <div class="showqrs" id="showqrs">
                        <div class="shtoparrow"></div>
                        <div class="guanzhuqr">
                            <img src="img/guanzhu_qrcode.png" width="80">
                            <div class="shmsg" style="margin-top:5px;">
                            请扫码关注
                            </div>
                            <div class="shmsg" style="margin-bottom:5px;">
                                接收重要信息
                            </div>
                        </div>
                    </div>
                </div>      
            </div>
        </div>
        <div class="container">
            <div class="nav">
                <a href="https://www.alipay.com/" class="logo"><img src="img/alipay_logo.png" height="30px"></a>
                <span class="divier"></span>
                <a href="http://open.alipay.com/platform/home.htm" class="open" target="_blank">开放平台</a>
                <ul class="navbar">
                    <li><a href="https://doc.open.alipay.com/doc2/detail?treeId=62&articleId=103566&docType=1" target="_blank">在线文档</a></li>
                    <li><a href="https://cschannel.alipay.com/portal.htm?sourceId=213" target="_blank">技术支持</a></li>
                </ul>
            </div>
        </div>
        <div class="container blue">
            <div class="title">支付宝即时到账(create_direct_pay_by_user)</div>
        </div>
    </div>
    <div class="content">
        <form action="alipayapi.jsp" class="alipayform" method="POST" target="_blank">
            <div class="element">
                <div class="legend">支付宝即时到账交易接口快速通道 </div>
            </div>
            <div class="element">
                <div class="etitle">商户订单号:</div>
                <div class="einput"><span style="font-size:16px;">${out_trade_no }</span>
                <input type="hidden" name="WIDout_trade_no" id="out_trade_no" value="${out_trade_no }" readonly></div>
                <br>
                <div class="mark">注意：商户订单号.</div>
            </div>
            
            <div class="element">
                <div class="etitle">商品名称:</div>
                <div class="einput"><span style="font-size:16px;">${subject }</span>
                <input type="hidden" name="WIDsubject" value="${subject }" readonly></div>
                <br>
                <div class="mark">注意：产品名称.</div>
            </div>
            <div class="element">
                <div class="etitle">付款金额:</div>
                <div class="einput"><span style="font-size:20px;color:red">${total_fee}</span>
                <input type="hidden" name="WIDtotal_fee" value="${total_fee}" readOnly></div>
                <br>
                <div class="mark">注意：付款金额(格式如：1.00,请精确到分).</div>
            </div>
			<div class="element">
                <div class="etitle">商品描述:</div>
                <div class="einput"><input type="text" name="WIDbody" ></div>
                <br>
                <div class="mark">注意：商品描述，选填.</div>
            </div>
            <div class="element">
                <input type="submit" class="alisubmit" value ="确认支付">
            </div>
        </form>
    </div>
    <div class="footer">
        <p class="footer-sub">
            <a href="http://ab.alipay.com/i/index.htm" target="_blank">关于支付宝</a><span>|</span>
            <a href="https://e.alipay.com/index.htm" target="_blank">商家中心</a><span>|</span>
            <a href="https://job.alibaba.com/zhaopin/index.htm" target="_blank">诚征英才</a><span>|</span>
            <a href="http://ab.alipay.com/i/lianxi.htm" target="_blank">联系我们</a><span>|</span>
            <a href="#" id="international" target="_blank">International&nbsp;Business</a><span>|</span>
            <a href="http://ab.alipay.com/i/jieshao.htm#en" target="_blank">About Alipay</a>
            <br>
             <span>支付宝版权所有</span>
            <span class="footer-date">2004-2016</span>
            <span><a href="http://fun.alipay.com/certificate/jyxkz.htm" target="_blank">ICP证：沪B2-20150087</a></span>
        </p>  
    </div>
</body>
<script>

        var even = document.getElementById("licode");   
        var showqrs = document.getElementById("showqrs");
         even.onmouseover = function(){
            showqrs.style.display = "block"; 
         }
         even.onmouseleave = function(){
            showqrs.style.display = "none";
         }
         
         var out_trade_no = document.getElementById("out_trade_no");

         
 
</script>

</html>