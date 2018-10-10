<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.epam.model.Book" %>
<%@ page import="com.epam.model.User" %>
<%@ page import="com.epam.service.UserManagerImpl" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
<% List<Book> requestedBooks = (List<Book>) request.getAttribute("requestedBooks"); %>
<% UserManagerImpl userService = (UserManagerImpl) request.getAttribute("userService"); %>
<div align = "center">
<h3>Welcome, Librarian!</h3>
</div>
<div align = "right">
    <form action="/logout" method="get">
        <input type="submit" value="Log out"/>
    </form>
</div>
<div align = "center">
<table>
<tr>
    <th>
        <h3>Requested books</h3>
    </th>
</tr>
<tr>
<td valign = "top">
<div>
    <table border="1" cellpadding="2" width="1000">
    <tr>
        <th>Author</th>
        <th>Title</th>
        <th>ISBN</th>
        <th>User</th>
        <th>Where to</th>
        <th>Grant</th>
        <th>Refuse</th>
    </tr>
                <% for(Book book: requestedBooks){ %>
                    <tr>
                        <td><%out.println(book.getAuthor());%></td>
                        <td><%out.println(book.getTitle());%></td>
                        <td><%out.println(book.getIsbn());%></td>
                        <td><% out.println(userService.findById(book.getUserId()).getEmail()); %></td>
                        <td>
                            <%switch(book.getState().ordinal()){
                                case 3: out.println("Home");
                                        break;
                                case 4: out.println("Read room");
                            } %>
                        </td>
                        <td align = "center" valign = "center">
                            <form action="/grant" method="post">
                                <input type="hidden" name="bookId" value="<%=book.getId()%>">
                                <input type="submit" value="Grant" />
                            </form>
                        </td>
                        <td align = "center" valign = "center">
                            <form action="/refuse" method="post">
                                <input type="hidden" name="bookId" value="<%=book.getId()%>">
                                <input type="submit" value="Refuse" />
                            </form>
                        </td>
                    </tr>
                <%}%>
    </table>
</div>
</td>
</tr>
</table>
</div>
</body>
</html>