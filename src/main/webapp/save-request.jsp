<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Форма заявки</title>
</head>
<body>
    <h1>Форма заявки</h1>

    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red"><%= request.getAttribute("error") %></p>
    <% } %>

    <form action="/save-request" method="post">
        <label>Имя: <input type="text" name="name" required></label><br><br>
        <label>Email: <input type="email" name="email" required></label><br><br>
        <label>Сообщение: <textarea name="message" required></textarea></label><br><br>
        <button type="submit">Отправить</button>
    </form>
</body>
</html>