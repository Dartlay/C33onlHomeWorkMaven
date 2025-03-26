<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Загрузка книги</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Загрузить новую книгу</h1>
        <form action="${pageContext.request.contextPath}/load-book" method="post" enctype="multipart/form-data">
            <input type="file" name="book" accept=".pdf,.epub,.txt" required>
            <button type="submit" class="btn">Загрузить</button>
        </form>
        <a href="${pageContext.request.contextPath}/home" class="btn back-btn">На главную</a>
    </div>
</body>
</html>