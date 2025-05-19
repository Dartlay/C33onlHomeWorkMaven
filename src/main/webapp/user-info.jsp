<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Information</title>
</head>
<body>
    <h1>User Details</h1>
    <p>ID: ${user.id}</p>
    <p>Username: ${user.username}</p>
    <p>Email: ${user.email}</p>
    <a href="${pageContext.request.contextPath}/index.jsp">Back to Home</a>
</body>
</html>