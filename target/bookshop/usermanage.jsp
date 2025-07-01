<%@page import="java.util.ArrayList"%>
<%@page import="com.entity.User"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%ArrayList userlist = (ArrayList)request.getAttribute("userlist");%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>用户管理</title>
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
		.user-table {
			width: 100%;
			border-collapse: collapse;
		}
		.user-table th, .user-table td {
			border: 1px solid #ddd;
			padding: 8px;
			text-align: left;
		}
		.user-table th {
			background-color: #f2f2f2;
		}
		.user-table tr:nth-child(even) {
			background-color: #f9f9f9;
		}
		.delete-btn {
			background-color: #f44336;
			color: white;
			padding: 5px 10px;
			border: none;
			cursor: pointer;
		}
		.delete-btn:hover {
			background-color: #da190b;
		}
		.no-users {
			text-align: center;
			color: #666;
			padding: 40px;
		}
	</style>
</head>
<body>
	<div class="header">
		<h1>网上书店 - 用户管理</h1>
	</div>
	
	<div class="nav">
		<input type="button" value="返回管理" onclick="location.href='manage.jsp'">
		<input type="button" value="退出登录" onclick="location.href='login.jsp'">
	</div>

	<div class="main-content">
		<h2>用户管理</h2>
		<%if(userlist == null || userlist.size() == 0){%>
			<div class="no-users">
				<p>暂无用户信息</p>
			</div>
		<%}else{%>
			<table class="user-table">
				<thead>
					<tr>
						<th>用户ID</th>
						<th>用户名</th>
						<th>密码</th>
						<th>电话</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<%for(int i = 0; i < userlist.size(); i++){
						User user = (User)userlist.get(i);%>
					<tr>
						<td><%=user.getUid()%></td>
						<td><%=user.getUname()%></td>
						<td><%=user.getUpassword()%></td>
						<td><%=user.getUphone()%></td>
						<td>
							<form action="UserServlet?action=delete" method="post" style="display: inline;" 
								  onsubmit="return confirm('确定要删除用户 <%=user.getUname()%> 吗？')">
								<input type="hidden" value="<%=user.getUid()%>" name="uid">
								<input type="submit" value="删除" class="delete-btn">
							</form>
						</td>
					</tr>
					<%}%>
				</tbody>
			</table>
		<%}%>
	</div>
</body>
</html>
