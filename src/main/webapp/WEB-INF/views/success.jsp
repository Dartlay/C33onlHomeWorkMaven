<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Успешная загрузка</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Книга успешно загружена: ${fileName}</h1>
        <a href="${pageContext.request.contextPath}/home" class="btn">На главную</a>
    </div>
</body>
</html>