<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Главная страница</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Добро пожаловать в менеджер книг</h1>
        <div class="button-group">
            <a href="${pageContext.request.contextPath}/book" class="btn">Скачать книгу</a>
            <a href="${pageContext.request.contextPath}/load-book-form" class="btn">Загрузить книгу</a>
        </div>
    </div>
</body>
</html>