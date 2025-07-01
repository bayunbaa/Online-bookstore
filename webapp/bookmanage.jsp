<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.entity.Book"%>
<%ArrayList booklist=(ArrayList)session.getAttribute("booklist");%>
<!DOCTYPE html>
<html>
<head>
<title>书籍管理</title>
</head>
<style>
	#book{
		width: 900px;
		margin: auto;
    	font-size: 20px;
    	color: white;
    	font-weight: bold;
    	text-align: center;
   		background-color: #7db9de;
	}
	#book span{
		margin: 0;
		width: 150px;
		display:inline-block;
		display:-moz-inline-box;
		padding: 20px 0;
    	font-size: 20px;
    	color: white;
    	font-weight: bold;
    	text-align: center;
	}
	#book p{
		margin: 0;
		padding: 20px 0;
    	font-size: 20px;
    	color: white;
    	font-weight: bold;
    	text-align: center;
    	background-color: #577C8A;
	}
	#book input{
		width: 50px;
		padding: 10px 10px 6px 10px;
    	margin: 0 10px;
    	outline: none;
    	font-size: 15px;
    	color: #fff;
    	border-top: 1px solid #312E3D;
    	border-left: 1px solid #312E3D;
    	border-right: 1px solid #312E3D;
    	border-bottom: 1px solid #56536A;
    	border-radius: 4px;
    	background-color: #2D2D3F;
	}
	#book input[type="submit"]{
		width: 75px;
		height: 44px;
    	margin: 0 10px;
    	border: 0;
    	outline: none;
    	font-size: 20px;
    	font-weight: bold;
    	color: white;
    	border-radius: 0;
   		background-color: #7db9de;
	}
	#book input[type="submit"]:hover{
   		background-color: #2D2D3F;
	}
</style>
<body>
<div id="book">
	<div style="background-color: #577C8A;">
		<span style="width: 250px;">名称</span>
		<span>单价</span>
		<span>种类</span>
		<span>库存</span>
		<span>修改</span>
	</div>
<%if(booklist==null||booklist.size()==0){%>
	<p>没有书籍可显示！</p>
<%}else{
	for(int i=0;i<booklist.size();i++){
		Book book=(Book)booklist.get(i);%>
	<form action="BookServlet?action=update" method="post">
		<div>
			<input type="hidden" value="<%=book.getBid()%>" name="bid">
			<span style="width: 250px;"><%=book.getBname()%></span>
			<span><input type="number" step = "0.01" value="<%=book.getBprice()%>" name="bprice"></span>
			<span><%=book.getBtype()%></span>
			<span><input type="number" value="<%=book.getBstock()%>" name="bstock"></span>
			<span><input type="submit" value="修改"></span>
		</div>
	</form>
<%}}%>
</div>
</body>
</html>
