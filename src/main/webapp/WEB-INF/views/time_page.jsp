<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Time in ${city}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 2rem;
            background-color: #f8f9fa;
        }
        h1 {
            color: #2c3e50;
            margin-bottom: 1.5rem;
        }
        .time {
            font-size: 1.5rem;
            margin: 1rem 0;
            padding: 1rem;
            background-color: #e9ecef;
            border-radius: 4px;
        }
        .age-info {
            color: #28a745;
            margin-top: 1rem;
            font-weight: bold;
        }
        .back-link {
            display: inline-block;
            margin-top: 2rem;
            color: #007bff;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <h1>Time in ${city}</h1>
    <div class="time">${time}</div>
    <div class="age-info">✓ Age verified: 18+</div>
    <a href="${pageContext.request.contextPath}/" class="back-link">Back to verification</a>
</body>
</html>