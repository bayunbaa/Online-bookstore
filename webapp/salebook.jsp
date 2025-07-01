<%@page import="java.util.ArrayList"%>
<%@page import="com.entity.User"%>
<%@page import="com.entity.Book"%>
<%@page import="com.entity.Message"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%ArrayList booksale = (ArrayList)session.getAttribute("booksale");
ArrayList messagelist = (ArrayList)session.getAttribute("messagelist");
Book book = (Book)booksale.get(0);
int uid = (int)session.getAttribute("uid");%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>购买书籍 - <%=book.getBname()%></title>
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
		.container {
			display: flex;
			gap: 20px;
		}
		.book-info {
			flex: 1;
			background-color: white;
			padding: 20px;
			border: 1px solid #ddd;
		}
		.purchase-form {
			width: 300px;
			background-color: white;
			padding: 20px;
			border: 1px solid #ddd;
		}
		.book-info h2 {
			margin-top: 0;
			color: #333;
		}
		.info-table {
			width: 100%;
			border-collapse: collapse;
			margin-bottom: 20px;
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
		.purchase-form h3 {
			margin-top: 0;
			color: #333;
		}
		.form-group {
			margin-bottom: 15px;
		}
		.form-group label {
			display: block;
			margin-bottom: 5px;
			font-weight: bold;
		}
		.form-group input, .form-group textarea {
			width: 100%;
			padding: 8px;
			border: 1px solid #ddd;
			box-sizing: border-box;
		}
		.buy-btn {
			background-color: #4CAF50;
			color: white;
			padding: 10px 20px;
			border: none;
			cursor: pointer;
			width: 100%;
			font-size: 16px;
		}
		.buy-btn:hover {
			background-color: #45a049;
		}
		.buy-btn:disabled {
			background-color: #ccc;
			cursor: not-allowed;
		}
		.messages {
			margin-top: 20px;
		}
		.messages h3 {
			color: #333;
		}
		.message-item {
			background-color: #f9f9f9;
			padding: 10px;
			margin-bottom: 10px;
			border-left: 4px solid #4CAF50;
		}
		.no-messages {
			text-align: center;
			color: #666;
			padding: 20px;
		}
	</style>
</head>
<body>
	<div class="header">
		<h1>网上书店 - 购买书籍</h1>
	</div>
	
	<div class="nav">
		<input type="button" value="返回书店" onclick="location.href='BookServlet?action=list'">
		<input type="button" value="退出登录" onclick="location.href='login.jsp'">
	</div>

	<div class="container">
		<div class="book-info">
			<h2>书籍信息</h2>
			<table class="info-table">
				<tr>
					<th>书名</th>
					<td><%=book.getBname()%></td>
				</tr>
				<tr>
					<th>价格</th>
					<td>¥<%=book.getBprice()%></td>
				</tr>
				<tr>
					<th>类型</th>
					<td><%=book.getBtype()%></td>
				</tr>
				<tr>
					<th>库存</th>
					<td><%=book.getBstock()%>本</td>
				</tr>
				<tr>
					<th>已售</th>
					<td><%=book.getBsale()%>本</td>
				</tr>
			</table>

			<div class="messages">
				<h3>用户评价</h3>
				<%if(messagelist == null || messagelist.size() == 0){%>
					<div class="no-messages">
						<p>暂无评价信息</p>
					</div>
				<%}else{%>
					<%for(int i = 0; i < messagelist.size(); i++){
						Message message = (Message)messagelist.get(i);%>
					<div class="message-item">
						<strong><%=message.getUname()%>:</strong> <%=message.getMcontent()%>
						<br><small><%=message.getMtime()%></small>
					</div>
					<%}%>
				<%}%>
			</div>
		</div>

		<div class="purchase-form">
			<h3>购买信息</h3>
			<form action="SaleServlet?action=sale" method="post">
				<input type="hidden" value="<%=book.getBid()%>" name="bid">
				<input type="hidden" value="<%=uid%>" name="uid">
				
				<div class="form-group">
					<label>购买数量:</label>
					<input type="number" name="snum" value="1" min="1" max="<%=book.getBstock()%>" required>
				</div>
				
				<div class="form-group">
					<label>留言:</label>
					<textarea name="mcontent" rows="4" placeholder="请输入您的评价或留言..."></textarea>
				</div>
				
				<%if(book.getBstock() == 0){%>
					<input type="button" value="库存不足" class="buy-btn" disabled>
				<%}else{%>
					<input type="submit" value="立即购买" class="buy-btn">
				<%}%>
			</form>
		</div>
	</div>
</body>
</html>
