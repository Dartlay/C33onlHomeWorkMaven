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
        <h1>Доступные книги</h1>
        <c:choose>
            <c:when test="${not empty books}">
                <div class="book-list">
                    <table>
                        <thead>
                            <tr>
                                <th>Название книги</th>
                                <th>Действие</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${books}" var="book">
                                <tr>
                                    <td>${book}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/download-book?file=${book}"
                                           class="btn download-btn">Скачать</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <p class="error">Нет доступных книг для скачивания</p>
            </c:otherwise>
        </c:choose>
        <a href="${pageContext.request.contextPath}/home" class="btn back-btn">На главную</a>
    </div>
</body>
</html>