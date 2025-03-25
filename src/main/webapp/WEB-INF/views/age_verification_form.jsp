<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Age Verification</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f5f5f5; margin: 0;
               display: flex; justify-content: center; align-items: center;
               min-height: 100vh; }
        .container { background: white; padding: 2rem; border-radius: 8px;
                   box-shadow: 0 2px 10px rgba(0,0,0,0.1); width: 100%;
                   max-width: 400px; text-align: center; }
        h1 { color: #2c3e50; margin-bottom: 1.5rem; }
        input { width: 100%; padding: 0.75rem; margin: 0.5rem 0;
               border: 1px solid #ddd; border-radius: 4px; font-size: 1rem; }
        button { background: #4CAF50; color: white; border: none;
                padding: 0.75rem; width: 100%; border-radius: 4px;
                font-size: 1rem; cursor: pointer; margin-top: 0.5rem; }
        .error { color: #e74c3c; margin-top: 1rem; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Age Verification</h1>
        <form method="POST">
            <input type="hidden" name="target" value="${requestURI}">
            <input type="number" name="age" min="1" max="120"
                   placeholder="Enter your age (1-120)" required>
            <button type="submit">Verify Age</button>
            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
        </form>
    </div>
</body>
</html>