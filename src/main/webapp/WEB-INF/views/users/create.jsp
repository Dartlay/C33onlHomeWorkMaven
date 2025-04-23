<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create User</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 500px;
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
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #555;
        }
        input[type="text"], input[type="email"] {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
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
        .button.cancel {
            background-color: #9e9e9e;
        }
        .error-message {
            color: #f44336;
            padding: 10px;
            margin-bottom: 15px;
            background-color: #ffebee;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Create New User</h1>

    <c:if test="${not empty error}">
        <div class="error-message">${error}</div>
    </c:if>

   <form action="${pageContext.request.contextPath}/api/create" method="post">
        <div class="form-group">
            <label for="login">Login:</label>
            <input type="text" id="login" name="login" required>
        </div>
        <div class="form-group">
            <label for="name">Name:</label>
            <input type="text" id="name" name="name">
        </div>
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email">
        </div>

        <button type="submit" class="button">Create</button>
        <a href="${pageContext.request.contextPath}/users" class="button cancel">Cancel</a>
    </form>
</div>
</body>
</html>