<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Management</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .container {
            background: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            text-align: center;
            max-width: 600px;
            width: 100%;
        }
        h1 {
            color: #333;
            margin-bottom: 30px;
            font-size: 2.2em;
        }
        .button {
            display: inline-block;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            padding: 12px 25px;
            margin: 10px;
            border-radius: 4px;
            font-size: 16px;
            transition: background-color 0.3s;
            border: none;
            cursor: pointer;
            min-width: 200px;
        }
        .button:hover {
            background-color: #45a049;
        }
        .button.users {
            background-color: #2196F3;
        }
        .button.users:hover {
            background-color: #0b7dda;
        }
        .button.create {
            background-color: #ff9800;
        }
        .button.create:hover {
            background-color: #e68a00;
        }
        .menu {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin-top: 30px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Welcome to User Management System</h1>

        <div class="menu">
            <a href="users" class="button users">View All Users</a>
            <a href="users/new" class="button create">Create New User</a>
        </div>
    </div>
</body>
</html>