<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.epam.model.Book" %>
<%@ page import="com.epam.model.User" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Search Book</title>
</head>
<body>
<% User loggedUser = (User) request.getAttribute("loggedUser"); %>
<% List<Book> bookList = (List<Book>) request.getAttribute("searchedBooks"); %>
<td>
    <div align="justify">
        <table border="1" cellpadding="2" width="500">
            <tr>
                <th>Author</th>
                <th>Title</th>
                <th>ISBN</th>
                <th>Status</th>
                <th>Rent</th>
                <th>Read</th>
            </tr>
            <% for (Book book : bookList) { %>
            <tr>
                <td><% out.println(book.getAuthor()); %></td>
                <td><% out.println(book.getTitle()); %></td>
                <td><% out.println(book.getIsbn()); %></td>
                <td><% switch (book.getState().ordinal()) {
                    case 0:
                        out.println("Library");
                        break;
                    case 1:
                        out.println("Reading");
                        break;
                    case 2:
                        out.println("Rented");
                        break;
                    case 3:
                        out.println("Pending");
                        break;
                    case 4:
                        out.println("Pending");
                }%></td>
                <td align="center" valign="center">
                    <form action="/rent" method="post">
                        <input type="hidden" name="rentId" value="<%=book.getId()%>">
                        <input type="submit" value="Rent" <% if(book.getState().ordinal()!=0){ %> disabled <% } %>/>
                    </form>
                </td>
                <td align="center" valign="center">
                    <form action="/read" method="post">
                        <input type="hidden" name="readId" value="<%=book.getId()%>">
                        <input type="submit" value="Read" <% if(book.getState().ordinal()!=0){ %> disabled <% } %>/>
                    </form>
                </td>
            </tr>
            <% } %>
        </table>
    </div>
</td>

</body>
</html>