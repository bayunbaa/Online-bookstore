<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户注册</title>
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
    	width: 278px;
    	height: 18px;
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
    	width: 300px;
    	min-height: 20px;
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
    <h1>用户注册</h1>
    <form method="post" action="UserServlet?action=usercreate" method="post">
    	<input type="text" required="required" placeholder="用户名" name="uname"></input>
    	<input type="password" required="required" placeholder="密码" name="upassword"></input>
    	<input type="number" min="0" required="required" placeholder="年龄" name="uage"></input>
    	<input type="text" required="required" placeholder="电话" name="uphone"></input>
    	<button class="but" type="submit">注册</button>
		<button class="but" type="button" onclick="location.href='login.jsp'">返回登录</button>
	</form>
</div>
</body>
</html>
