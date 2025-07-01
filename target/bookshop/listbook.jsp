<%@page import="java.util.ArrayList"%>
<%@page import="com.entity.Book"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%ArrayList booklist = (ArrayList)request.getAttribute("books");%>
<%ArrayList typelist = (ArrayList)request.getAttribute("bookTypes");%>
<%
    // 检查用户是否已登录
    Integer uidObj = (Integer)session.getAttribute("uid");
    if (uidObj == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    int uid = uidObj.intValue();
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>书籍列表</title>
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
		.nav input[type="submit"] {
			background-color: #008CBA;
			color: white;
			padding: 8px 16px;
			border: none;
			cursor: pointer;
			margin-left: 5px;
		}
		.nav input[type="submit"]:hover {
			background-color: #007B9A;
		}
		.container {
			display: flex;
			gap: 20px;
		}
		.sidebar {
			width: 200px;
			background-color: white;
			padding: 15px;
			border: 1px solid #ddd;
		}
		.sidebar h3 {
			margin-top: 0;
			color: #333;
		}
		.sidebar form {
			margin-bottom: 5px;
		}
		.sidebar input[type="submit"] {
			background-color: #f1f1f1;
			border: 1px solid #ddd;
			padding: 8px 12px;
			width: 100%;
			cursor: pointer;
			text-align: left;
		}
		.sidebar input[type="submit"]:hover {
			background-color: #e1e1e1;
		}
		.main-content {
			flex: 1;
			background-color: white;
			padding: 20px;
			border: 1px solid #ddd;
		}
		.book-table {
			width: 100%;
			border-collapse: collapse;
		}
		.book-table th, .book-table td {
			border: 1px solid #ddd;
			padding: 8px;
			text-align: left;
		}
		.book-table th {
			background-color: #f2f2f2;
		}
		.book-table tr:nth-child(even) {
			background-color: #f9f9f9;
		}
		.buy-btn {
			background-color: #4CAF50;
			color: white;
			padding: 5px 10px;
			border: none;
			cursor: pointer;
		}
		.buy-btn:hover {
			background-color: #45a049;
		}
		.no-books {
			text-align: center;
			color: #666;
			padding: 40px;
		}
	</style>
</head>
<body>
	<div class="header">
		<h1>网上书店 - 书籍列表</h1>
	</div>
	
	<div class="nav">
		<form action="UserServlet?action=user" method="post" style="display: inline;">
			<input type="hidden" value="<%=uid%>" name="uid">
			<input type="submit" value="个人中心">
		</form>
		<input type="button" value="退出登录" onclick="location.href='login.jsp'">
	</div>

	<div class="container">
		<div class="sidebar">
			<h3>书籍分类</h3>
			<form action="BookServlet?action=list" method="post">
				<input type="submit" value="全部书籍">
			</form>
			<%if(typelist != null && typelist.size() != 0){
				for(int i = 0; i < typelist.size(); i++){
					String bookType = (String)typelist.get(i);%>
			<form action="BookServlet?action=type" method="post">
				<input type="hidden" value="<%=bookType%>" name="booktype">
				<input type="submit" value="<%=bookType%>">
			</form>
			<%}}%>
		</div>

		<div class="main-content">
			<h2>书籍列表</h2>
			<%if(booklist == null || booklist.size() == 0){%>
				<div class="no-books">
					<p>暂无书籍信息</p>
				</div>
			<%}else{%>
				<table class="book-table">
					<thead>
						<tr>
							<th>书名</th>
							<th>价格</th>
							<th>类型</th>
							<th>库存</th>
							<th>已售</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<%for(int i = 0; i < booklist.size(); i++){
							Book book = (Book)booklist.get(i);%>
						<tr>
							<td><%=book.getBname()%></td>
							<td>¥<%=book.getBprice()%></td>
							<td><%=book.getBtype()%></td>
							<td>
								<%if(book.getBstock() == 0){%>
									<span style="color: red;">已售罄</span>
								<%}else{%>
									<%=book.getBstock()%>本
								<%}%>
							</td>
							<td><%=book.getBsale()%>本</td>
							<td>
								<form id="buyForm_<%=book.getBid()%>" action="SaleServlet?action=booklist" method="post" style="display: inline;">
									<input type="hidden" value="<%=book.getBid()%>" name="bid">
									<input type="hidden" value="<%=uid%>" name="uid">
									<%if(book.getBstock() == 0){%>
										<input type="button" value="缺货" disabled style="background-color: #ccc;">
									<%}else{%>
										<input type="button" value="购买" class="buy-btn"
											onclick="confirmPurchase('<%=book.getBname()%>', <%=book.getBprice()%>, <%=book.getBid()%>)">
									<%}%>
								</form>
							</td>
						</tr>
						<%}%>
					</tbody>
				</table>
			<%}%>
		</div>
	</div>

	<script>
	// {{TITAN:
	// Action: Added
	// Reason: 添加购买确认弹窗功能，提升用户体验
	// Timestamp: 2025-01-27 14:55:00 +08:00
	// }}
	// {{START MODIFICATION}}
	function confirmPurchase(bookName, bookPrice, bookId) {
		var message = '确认购买《' + bookName + '》吗？\n' +
					  '价格：¥' + bookPrice.toFixed(2) + '\n' +
					  '数量：1本\n' +
					  '总价：¥' + bookPrice.toFixed(2);

		if (confirm(message)) {
			// 用户确认购买，提交表单
			document.getElementById('buyForm_' + bookId).submit();
		}
	}

	// 显示购买结果提示
	window.onload = function() {
		var urlParams = new URLSearchParams(window.location.search);
		if (urlParams.get('success') === 'purchase') {
			alert('购买成功！您可以在售卖记录中查看购买详情。');
		} else if (urlParams.get('error') === 'purchase') {
			alert('购买失败，请重试。');
		} else if (urlParams.get('error') === 'stock') {
			alert('库存不足，无法购买。');
		} else if (urlParams.get('error') === 'notfound') {
			alert('图书不存在。');
		}
	};
	// {{END MODIFICATION}}
	</script>
</body>
</html>
