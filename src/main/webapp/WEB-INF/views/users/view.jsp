<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 600px;
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
        .user-details {
            margin: 20px 0;
        }
        .detail-row {
            margin: 10px 0;
            padding: 10px;
            background-color: #f9f9f9;
            border-radius: 4px;
        }
        .detail-label {
            font-weight: bold;
            color: #555;
        }
        .button {
            background-color: #4CAF50;
            border: none;
            color: white;
            padding: 10px 15px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 14px;
            margin: 4px 2px;
            cursor: pointer;
            border-radius: 4px;
        }
        .button.edit {
            background-color: #2196F3;
        }
        .button.back {
            background-color: #9e9e9e;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>User Details</h1>

    <div class="user-details">
        <div class="detail-row">
            <span class="detail-label">ID:</span> ${user.id}
        </div>
        <div class="detail-row">
            <span class="detail-label">Login:</span> ${user.login}
        </div>
        <div class="detail-row">
            <span class="detail-label">Name:</span> ${user.name}
        </div>
        <div class="detail-row">
            <span class="detail-label">Email:</span> ${user.email}
        </div>
    </div>

    <div>
        <a href="${pageContext.request.contextPath}/users/edit?id=${user.id}" class="button edit">Edit</a>
        <a href="${pageContext.request.contextPath}/users" class="button back">Back to List</a>
    </div>
</div>
</body>
</html>