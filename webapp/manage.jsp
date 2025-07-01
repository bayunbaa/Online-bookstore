<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>后台管理</title>
</head>
<style type="text/css">
	#container {
    	text-align: center;
    	width: 620px;
    	margin: auto;
    	padding: 10px 0 25px 0;
    	background-color: #fffffb;
    	font-weight: bold;
    	box-shadow: 10px 10px 15px gray;
	}
	body {
   		background-color: #dac9a6;
	}
	button {
    	width: 400px;
    	height: 80px;
  		margin: 10px 10px;
    	border: 0;
    	outline: none;
    	font-size: 25px;
    	font-weight: bold;
    	color: white;  
   		background-color: #7db9de;
	}
	button:hover {
    	background-color: #51a8dd;
	}
</style>
<body>
<div id="container">
	<h3 style="font-size: 30px;">后台管理</h3><hr>
	<button onclick="location.href='addbook.jsp'">添加书籍</button>
	<button onclick="location.href='BookServlet?action=manage'">书籍管理</button>
	<button onclick="location.href='UserServlet?action=manage'">用户管理</button>
	<button onclick="location.href='SaleServlet?action=list'">售卖记录</button>
	<button onclick="location.href='BookServlet?action=list'">书籍列表</button>
</div>
</body>
</html>