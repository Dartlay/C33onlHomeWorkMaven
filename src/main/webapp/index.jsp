<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home Page</title>
</head>
<body>
    <h1>${message}</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/fc/about">About Us</a>
    </nav>

    <h2>User Information</h2>
    <form action="${pageContext.request.contextPath}/fc/user" method="get">
        <button type="submit">Get User Info</button>
    </form>
</body>
</html>