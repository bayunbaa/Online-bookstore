<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>添加书籍</title>
</head>
<style>
	html{
    	width: 100%;
    	height: 100%;
    	overflow: hidden;
    	font-style: sans-serif;
	}
	body{
    	width: 100%;
    	height: 100%;
    	font-family: 'Open Sans',sans-serif;
    	margin: 0;
    	background-color: #566C73;
	}
	#login{
   		position: absolute;
    	top: 50%;
    	left:50%;
    	margin: -225px 0 0 -150px;
    	width: 300px;
    	height: 300px;
	}
	#login h1{
    	color: #fff;
    	font-size: 35px;
    	text-shadow:0 0 10px;
    	letter-spacing: 1px;
    	text-align: center;
	}
	h1{
    	font-size: 2em;
    	margin: 0.67em 0;
	}
	input{
    	width: 300px;
    	height: 25px;
    	margin-bottom: 10px;
    	outline: none;
    	padding: 10px;
    	font-size: 13px;
    	color: #fff;
    	border-top: 1px solid #312E3D;
    	border-left: 1px solid #312E3D;
    	border-right: 1px solid #312E3D;
    	border-bottom: 1px solid #56536A;
    	border-radius: 4px;
    	background-color: #2D2D3F;
	}
	.but{
    	width: 325px;
    	min-height: 25px;
    	display: block;
    	background-color: #4a77d4;
    	border: 1px solid #3762bc;
    	color: #fff;
    	padding: 9px 14px;
    	font-size: 15px;
    	line-height: normal;
    	border-radius: 5px;
    	margin: 0px 0px 10px 0px;
	}
</style>
<body>
<div id="login">
    <h1>添加书籍</h1>
    <form action="BookServlet?action=add" method="post">
    	<input type="text" required="required" placeholder="书籍名" name="bname"></input>
    	<input type="number" required="required" placeholder="价格" name="bprice" step = "0.01"></input>
		<input type="text" required="required" placeholder="种类" name="btype"></input>
		<input type="text" required="required" placeholder="库存" name="bstock"></input>
		<input type="text" required="required" placeholder="描述" name="bdes"></input>
    	<div><button class="but" type="submit">添加</button></div>
	</form>
</div>
</body>
</html>
