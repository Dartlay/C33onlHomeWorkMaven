<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1000px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            border-bottom: 2px solid #eee;
            padding-bottom: 10px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #4CAF50;
            color: white;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .button {
            background-color: #4CAF50;
            border: none;
            color: white;
            padding: 8px 12px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 14px;
            margin: 2px;
            cursor: pointer;
            border-radius: 4px;
        }
        .button.delete {
            background-color: #f44336;
        }
        .button.edit {
            background-color: #2196F3;
        }
        .button.view {
            background-color: #ff9800;
        }
        .button.new {
            background-color: #4CAF50;
            margin-bottom: 20px;
        }
        .nav-buttons {
            margin-top: 20px;
        }
        .success-message {
            color: #4CAF50;
            padding: 10px;
            margin-bottom: 15px;
            background-color: #e8f5e9;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>User Management</h1>

    <c:if test="${not empty param.deleted}">
        <div class="success-message">User deleted successfully!</div>
    </c:if>

    <a href="${pageContext.request.contextPath}/users/new" class="button new">Create New User</a>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Login</th>
                <th>Name</th>
                <th>Email</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${users}" var="user">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.login}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/users/view?id=${user.id}" class="button view">View</a>
                        <a href="${pageContext.request.contextPath}/users/edit?id=${user.id}" class="button edit">Edit</a>
                        <form action="${pageContext.request.contextPath}/api/delete" method="post" style="display:inline;">
                            <input type="hidden" name="id" value="${user.id}">
                            <button type="submit" class="button delete" onclick="return confirm('Are you sure?')">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>