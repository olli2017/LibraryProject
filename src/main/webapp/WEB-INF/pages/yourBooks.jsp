<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.epam.model.Book" %>
<%@ page import="com.epam.model.User" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Your books</title>
</head>
<body>
<% User loggedUser = (User) request.getAttribute("loggedUser"); %>
<% List<Book> bookList = (List<Book>) request.getAttribute("bookList"); %>
<% List<Book> subList = (List<Book>) request.getAttribute("subList"); %>
<% Integer pageN = (Integer) request.getAttribute("page"); %>
<div align = "center">
<h3>Welcome, <% out.println(loggedUser.getName()); %>!</h3>
</div>
<div align="center">
    <h4>Your books</h4>
<table>
<tr>
<td>
    <table border="1" cellpadding="2" width="500">
        <% if(subList==null) { %>
        <c:forEach begin = "0" end = "9" var="book" items="${bookList}">
            <tr>
                <td>${book.title}</td>
                <td>${book.author}</td>
                <td>${book.isbn}</td>
                <td align="center" valign = "center">
                    <form action="/return" method="post">
                        <input type="hidden" name="returnId" value="${book.id}">
                        <input type="submit" value="Return" />
                    </form>
                </td>
            </tr>
        </c:forEach>
        <% } else { %>
        <c:forEach var="book" items="${subList}">
            <tr>
                <td>${book.title}</td>
                <td>${book.author}</td>
                <td>${book.isbn}</td>
                <% if(pageN==null){ %><td>
                    <form action="/return" method="post">
                        <input type="hidden" name="rentId" value="${book.id}">
                        <input type="submit" value="Return" />
                    </form>
                </td>
                <% } else { %>
                <td>
                    <form action="/return/<%=pageN%>" method="post">
                        <input type="hidden" name="returnId" value="${book.id}">
                        <input type="submit" value="Return" />
                    </form>
                </td>
                <% } %>
            </tr>
        </c:forEach>
    <% } %>
</table>
</td>
<td align="center" valign="top">
    <% for(Integer i=1; i <= (bookList.size()/10)+1; i++){ %>
        <a href="/yourPage/<%=i%>" <% if(pageN==i) { %> style="color:red" <%}%> > <% if(pageN==i) { %> <font size="5"> <%}%> <% out.println(i); %><% if(pageN==i) { %> </font> <%}%>
        &nbsp;
        <% if((i%5)==0) { %> <br> <%}%>
    <% } %>
</td>
</tr>
</table>
</div>
<div align = "center">
    <form action="/welcome" method="get">
        <input type="submit" value="Browse All Books"/>
    </form>
</div>
</body>
</html>