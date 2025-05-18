<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>School Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <header class="header">
        <div class="container">
            <h1><i class="fas fa-graduation-cap"></i> School Management</h1>
            <nav class="navbar">
                <a href="${pageContext.request.contextPath}/" class="nav-link"><i class="fas fa-home"></i> Home</a>
                <a href="${pageContext.request.contextPath}/students" class="nav-link active">
                        <i class="fas fa-users"></i> Students
                    </a>
                <a href="${pageContext.request.contextPath}/subjects" class="nav-link"><i class="fas fa-book"></i> Subjects</a>
                <a href="${pageContext.request.contextPath}/grades" class="nav-link"><i class="fas fa-star"></i> Grades</a>
            </nav>
        </div>
    </header>

    <main class="container">
        <c:if test="${not empty message}">
            <div class="alert alert-${messageType}">
                ${message}
            </div>
        </c:if>