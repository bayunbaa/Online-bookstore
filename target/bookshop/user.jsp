<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.iflytek.bookshop.entity.User"%>
<%@page import="com.iflytek.bookshop.entity.Sale"%>
<%ArrayList userinfor = (ArrayList)session.getAttribute("userinfor");%>
<%ArrayList usersale = (ArrayList)session.getAttribute("usersale");%>
<%int uid = (int)session.getAttribute("uid");%>
<%User user = (User)userinfor.get(0);%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>个人中心</title>
	<style>
		body {
			font-family: Arial, sans-serif;
			margin: 0;
			padding: 20px;
			background-color: #f5f5f5;
		}
		.header {
			background-color: #4CAF50;
			color: white;
			padding: 15px;
			text-align: center;
			margin-bottom: 20px;
		}
		.nav {
			text-align: right;
			margin-bottom: 20px;
		}
		.nav input[type="button"] {
			background-color: #008CBA;
			color: white;
			padding: 8px 16px;
			border: none;
			cursor: pointer;
			margin-left: 5px;
		}
		.nav input[type="button"]:hover {
			background-color: #007B9A;
		}
		.user-info {
			background-color: white;
			padding: 20px;
			border: 1px solid #ddd;
			margin-bottom: 20px;
		}
		.user-info h2 {
			margin-top: 0;
			color: #333;
		}
		.info-table {
			width: 100%;
			border-collapse: collapse;
		}
		.info-table th, .info-table td {
			border: 1px solid #ddd;
			padding: 8px;
			text-align: left;
		}
		.info-table th {
			background-color: #f2f2f2;
			width: 120px;
		}
		.purchase-history {
			background-color: white;
			padding: 20px;
			border: 1px solid #ddd;
		}
		.purchase-history h2 {
			margin-top: 0;
			color: #333;
		}
		.purchase-table {
			width: 100%;
			border-collapse: collapse;
		}
		.purchase-table th, .purchase-table td {
			border: 1px solid #ddd;
			padding: 8px;
			text-align: left;
		}
		.purchase-table th {
			background-color: #f2f2f2;
		}
		.purchase-table tr:nth-child(even) {
			background-color: #f9f9f9;
		}
		.no-purchases {
			text-align: center;
			color: #666;
			padding: 40px;
		}
	</style>
</head>
<body>
	<div class="header">
		<h1>网上书店 - 个人中心</h1>
	</div>
	
	<div class="nav">
		<input type="button" value="返回书店" onclick="location.href='BookServlet?action=list'">
		<input type="button" value="退出登录" onclick="location.href='login.jsp'">
	</div>

	<div class="user-info">
		<h2>个人信息</h2>
		<table class="info-table">
			<tr>
				<th>用户ID</th>
				<td><%=user.getUid()%></td>
			</tr>
			<tr>
				<th>用户名</th>
				<td><%=user.getUname()%></td>
			</tr>
			<tr>
				<th>密码</th>
				<td><%=user.getUpwd()%></td>
			</tr>
			<tr>
				<th>邮箱</th>
				<td><%=user.getUemail()%></td>
			</tr>
		</table>
	</div>

	<div class="purchase-history">
		<h2>购买记录</h2>
		<%if(usersale == null || usersale.size() == 0){%>
			<div class="no-purchases">
				<p>暂无购买记录</p>
			</div>
		<%}else{%>
			<table class="purchase-table">
				<thead>
					<tr>
						<th>订单ID</th>
						<th>书籍名称</th>
						<th>购买数量</th>
						<th>单价</th>
						<th>总价</th>
						<th>购买时间</th>
					</tr>
				</thead>
				<tbody>
					<%for(int i = 0; i < usersale.size(); i++){
						Sale sale = (Sale)usersale.get(i);%>
					<tr>
						<td><%=sale.getSid()%></td>
						<td><%=sale.getBname()%></td>
						<td><%=sale.getScount()%></td>
						<td>¥<%=sale.getBprice()%></td>
						<td>¥<%=sale.getScount() * sale.getBprice()%></td>
						<td><%=sale.getStime()%></td>
					</tr>
					<%}%>
				</tbody>
			</table>
		<%}%>
	</div>
</body>
</html>
