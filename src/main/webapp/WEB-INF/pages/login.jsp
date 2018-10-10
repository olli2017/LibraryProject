<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Log In</title>
    <link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet">
</head>
<body>
<div class="container">
    <section id="content">
        <form action="/users/login" method="post">
            <h1>Login Form</h1>
            <div>
                Email:<input type="text" name="email" align="right"/><br/><br/>
            </div>
            <div>
                Password:<input type="password" name="password" align="right"/><br/><br/>
            </div>
            <div>
                <input type="submit" value="Log in"/>
                <a href="/register">Register</a>
            </div>
        </form>
    </section>
</div>

</body>
</html>