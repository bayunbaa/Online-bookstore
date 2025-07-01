<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%String path=request.getContextPath();
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";%>
<!DOCTYPE HTML PUBLIC"-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>用户登录</title>
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
    	margin: -175px 0 0 -150px;
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
    	height: 40px;
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
    <h1>登录</h1>
    <form method="post" action="UserServlet?action=login" method="post">
    	<input type="text" required="required" placeholder="用户名" name="uname"></input>
    	<input type="password" required="required" placeholder="密码" name="upassword"></input>
    	<button class="but" type="submit">登录</button>
		<button class="but" type="button" onclick="location.href='usercreate.jsp'">注册</button>
	</form>
</div>
</body>
</html>
