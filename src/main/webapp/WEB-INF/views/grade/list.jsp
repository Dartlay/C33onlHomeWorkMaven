<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Grades Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .grade-badge {
            width: 40px;
            height: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            font-weight: bold;
        }

        .grade-A {
            background-color: #1cc88a;
            color: white;
        }

        .grade-B {
            background-color: #36b9cc;
            color: white;
        }

        .grade-C {
            background-color: #f6c23e;
            color: white;
        }

        .grade-D {
            background-color: #e74a3b;
            color: white;
        }

        .action-buttons .btn {
            width: 40px;
            padding: 0.25rem 0.5rem;
        }
    </style>
</head>
<body class="bg-light">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow mb-4">
        <div class="container">
            <a class="navbar-brand" href="${ctx}/">
                <i class="fas fa-graduation-cap me-2"></i>
                School Management
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${ctx}/">
                            <i class="fas fa-home me-1"></i> Home
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${ctx}/students">
                            <i class="fas fa-users me-1"></i> Students
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${ctx}/subjects">
                            <i class="fas fa-book me-1"></i> Subjects
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${ctx}/grades">
                            <i class="fas fa-star me-1"></i> Grades
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="container py-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1 class="h3 mb-0 text-gray-800">
                <i class="fas fa-star me-2"></i> Grades Management
            </h1>
            <div>
                <a href="${ctx}/grades?action=add" class="btn btn-primary me-2">
                    <i class="fas fa-plus me-1"></i> Add Grade
                </a>
                <a href="${ctx}/data?action=export&type=grades&format=txt" class="btn btn-outline-secondary">
                    <i class="fas fa-file-download me-1"></i> Export
                </a>
            </div>
        </div>
        <div class="card shadow mb-4">
            <div class="card-header py-3 d-flex justify-content-between align-items-center">
                <h6 class="m-0 font-weight-bold text-primary">Grades List</h6>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th>Student</th>
                                <th>Group</th>
                                <th>Subject</th>
                                <th>Grade</th>
                                <th>Date</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${grades}" var="grade">
                                <tr>
                                    <td>${grade.student.name}</td>
                                    <td>${grade.student.group}</td>
                                    <td>${grade.subject.name}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${grade.value >= 4}">
                                                <span class="grade-badge grade-A">${grade.value}</span>
                                            </c:when>
                                            <c:when test="${grade.value >= 3}">
                                                <span class="grade-badge grade-B">${grade.value}</span>
                                            </c:when>
                                            <c:when test="${grade.value >= 2}">
                                                <span class="grade-badge grade-C">${grade.value}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="grade-badge grade-D">${grade.value}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${grade.date}</td>
                                    <td class="action-buttons">
                                        <div class="d-flex gap-2">
                                            <a href="${ctx}/grades?action=edit&id=${grade.id}"
                                               class="btn btn-sm btn-warning">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <form action="${ctx}/grades" method="post"
                                                  onsubmit="return confirm('Delete this grade?')">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${grade.id}">
                                                <button type="submit" class="btn btn-sm btn-danger">
                                                    <i class="fas fa-trash-alt"></i>
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>