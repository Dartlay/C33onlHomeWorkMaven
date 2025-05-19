<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Список книг</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Книги в библиотеке</h1>

        <table class="book-list">
            <thead>
                <tr>
                    <th>Название</th>
                    <th>Автор</th>
                    <th>Год</th>
                    <th>Действия</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${books}" var="book">
                    <tr>
                        <td>${book.title}</td>
                        <td>${book.author}</td>
                        <td>${book.publicationYear}</td>
                        <td>
                            <a href="/download-book?file=${book.filePath}"
                               class="btn download-btn">Скачать</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <a href="/home" class="btn back-btn">На главную</a>
    </div>
</body>
</html>