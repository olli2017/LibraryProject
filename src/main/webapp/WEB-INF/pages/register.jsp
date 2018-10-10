<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
    <link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet">
</head>
<body>
<% Boolean tryAgain = (Boolean) request.getAttribute("tryAgain"); %>
<h3 align = "center"><font color = "red"><% if(tryAgain!=null) out.println("Password fields should match"); %></font></h3>
<div class="container">
    <section id="content">
        <form action="/users/register" method="post">
            Name:<input type="text" name="name"/><br/><br/>
            E-mail:<input type="text" name="email"/><br/><br/>
            Password:<input type="password" name="password"/><br/><br/>
            Confirm:<input type="password" name="confirmPassword"/><br/><br/>
            <input type="submit" value="Register"/>
        </form>
    </section>
</div>
</body>
</html>