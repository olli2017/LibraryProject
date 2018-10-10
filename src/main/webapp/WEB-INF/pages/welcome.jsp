<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.epam.model.Book" %>
<%@ page import="com.epam.model.User" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
<% User loggedUser = (User) request.getAttribute("loggedUser"); %>
<% List<Book> bookList = (List<Book>) request.getAttribute("bookList"); %>
<% List<Book> allBooks = (List<Book>) request.getAttribute("allBooks"); %>
<% List<Book> subList = (List<Book>) request.getAttribute("subList"); %>
<% Integer pageN = (Integer) request.getAttribute("page"); %>
<div align = "center">
<h3>Welcome, <% out.println(loggedUser.getName()); %>!</h3>
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
        <h3>All the books</h3>
    </th>
    <th>
        <form action="/YourBooks" method="get">
            <input type="submit" value="Go to Your Books"/>
        </form>
        <form action="/search" method="post">
            Search<input type="text" name="search"/><br/><br>
            <input type="submit" value="Search"/>
        </form>

    </th>
    <th>
        <h3>Your books</h3>
    </th>
</tr>
<tr>
<td valign = "top">
<div>
    <table border="1" cellpadding="2" width="500">
    <tr>
        <th>Author</th>
        <th>Title</th>
        <th>ISBN</th>
        <th>Rent</th>
        <th>Read</th>
    </tr>
            <% if(subList==null) { %>
                <c:forEach begin = "0" end = "9" var="book" items="${allBooks}">
                    <tr>
                        <td>${book.title}</td>
                        <td>${book.author}</td>
                        <td>${book.isbn}</td>
                        <td>
                            <form action="/rent" method="post">
                                <input type="hidden" name="rentId" value="${book.id}">
                                <input type="submit" value="Rent" <c:if test = "${book.getState().ordinal()!=0}"> disabled </c:if> />
                            </form>
                        </td>
                        <td>
                            <form action="/read" method="post">
                                <input type="hidden" name="readId" value="${book.id}">
                                <input type="submit" value="Read" <c:if test = "${book.getState().ordinal()!=0}"> disabled </c:if> />
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
                            <form action="/rent" method="post">
                                <input type="hidden" name="rentId" value="${book.id}">
                                <input type="submit" value="Rent" <c:if test = "${book.getState().ordinal()!=0}"> disabled </c:if> />
                            </form>
                        </td>
                        <td>
                            <form action="/read" method="post">
                                <input type="hidden" name="readId" value="${book.id}">
                                <input type="submit" value="Read" <c:if test = "${book.getState().ordinal()!=0}"> disabled </c:if> />
                            </form>
                        </td><% } else { %>
                        <td>
                            <form action="/rent/<%=pageN%>" method="post">
                                <input type="hidden" name="rentId" value="${book.id}">
                                <input type="submit" value="Rent" <c:if test = "${book.getState().ordinal()!=0}"> disabled </c:if> />
                            </form>
                        </td>
                        <td>
                            <form action="/read/<%=pageN%>" method="post">
                                <input type="hidden" name="readId" value="${book.id}">
                                <input type="submit" value="Read" <c:if test = "${book.getState().ordinal()!=0}"> disabled </c:if> />
                            </form>
                        </td> <% } %>
                    </tr>
                </c:forEach>
            <% } %>
    </table>
</div>
</td>
<td width = "200" align = "center" valign = "top">
<% for(Integer i=1; i <= (allBooks.size()/10)+1; i++){ %>
    <a href="/page/<%=i%>" <% if(pageN==i) { %> style="color:red" <%}%> > <% if(pageN==i) { %> <font size="5"> <%}%> <% out.println(i); %><% if(pageN==i) { %> </font> <%}%>
    &nbsp;
    <% if((i%5)==0) { %> <br> <%}%>
<% } %>
</td>
<td valign = "top">
<div>
    <table border="1" cellpadding="2" width="500">
    <tr>
        <th>Author</th>
        <th>Title</th>
        <th>ISBN</th>
        <th>Status</th>
    </tr>
    <% for(Book book:bookList){ %>
        <tr>
            <td><% out.println(book.getAuthor()); %></td>
            <td><% out.println(book.getTitle()); %></td>
            <td><% out.println(book.getIsbn()); %></td>
            <td><% switch(book.getState().ordinal()){
                            case 1: out.println("Reading");
                                    break;
                            case 2: out.println("Rented");
                                    break;
                            case 3: out.println("Pending");
                                    break;
                            case 4: out.println("Pending");
            }%></td>
        </tr>
    <% } %>
    </table>
</div>
</td>
</tr>
</table>
</div>
</body>
</html>