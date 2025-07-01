<%@page import="java.util.ArrayList"%>
<%@page import="com.entity.Sale"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%ArrayList sales = (ArrayList)request.getAttribute("sales");%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>销售管理</title>
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
		.main-content {
			background-color: white;
			padding: 20px;
			border: 1px solid #ddd;
		}
		.sale-table {
			width: 100%;
			border-collapse: collapse;
		}
		.sale-table th, .sale-table td {
			border: 1px solid #ddd;
			padding: 8px;
			text-align: left;
		}
		.sale-table th {
			background-color: #f2f2f2;
		}
		.sale-table tr:nth-child(even) {
			background-color: #f9f9f9;
		}
		.no-sales {
			text-align: center;
			color: #666;
			padding: 40px;
		}
		.stats {
			background-color: #e8f5e8;
			padding: 15px;
			margin-bottom: 20px;
			border: 1px solid #4CAF50;
		}
		.stats h3 {
			margin-top: 0;
			color: #2e7d32;
		}
	</style>
</head>
<body>
	<div class="header">
		<h1>网上书店 - 销售管理</h1>
	</div>
	
	<div class="nav">
		<input type="button" value="返回管理" onclick="location.href='manage.jsp'">
		<input type="button" value="退出登录" onclick="location.href='login.jsp'">
	</div>

	<div class="main-content">
		<h2>销售记录</h2>
		
		<%if(sales != null && sales.size() > 0){
			// 计算统计信息
			double totalRevenue = 0;
			int totalBooks = 0;
			for(int i = 0; i < sales.size(); i++){
				Sale sale = (Sale)sales.get(i);
				totalRevenue += sale.getBprice() * sale.getScount();
				totalBooks += sale.getScount();
			}
		%>
		<div class="stats">
			<h3>销售统计</h3>
			<p><strong>总订单数:</strong> <%=sales.size()%> 笔</p>
			<p><strong>总销售额:</strong> ¥<%=String.format("%.2f", totalRevenue)%></p>
			<p><strong>总售出书籍:</strong> <%=totalBooks%> 本</p>
			<p><strong>平均订单金额:</strong> ¥<%=String.format("%.2f", totalRevenue/sales.size())%></p>
		</div>
		<%}%>

		<%if(sales == null || sales.size() == 0){%>
			<div class="no-sales">
				<p>暂无销售记录</p>
			</div>
		<%}else{%>
			<table class="sale-table">
				<thead>
					<tr>
						<th>订单ID</th>
						<th>用户ID</th>
						<th>书籍ID</th>
						<th>书籍名称</th>
						<th>单价</th>
						<th>数量</th>
						<th>总价</th>
						<th>销售时间</th>
					</tr>
				</thead>
				<tbody>
					<%for(int i = 0; i < sales.size(); i++){
						Sale sale = (Sale)sales.get(i);%>
					<tr>
						<td><%=sale.getSid()%></td>
						<td><%=sale.getUid()%></td>
						<td><%=sale.getBid()%></td>
						<td><%=sale.getBname()%></td>
						<td>¥<%=sale.getBprice()%></td>
						<td><%=sale.getScount()%></td>
						<td>¥<%=String.format("%.2f", sale.getBprice() * sale.getScount())%></td>
						<td><%=sale.getStime()%></td>
					</tr>
					<%}%>
				</tbody>
			</table>
		<%}%>
	</div>
</body>
</html>
