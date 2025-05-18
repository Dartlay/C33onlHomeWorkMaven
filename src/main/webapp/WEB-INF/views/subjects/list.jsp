<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Subjects Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .action-buttons .btn {
            width: 40px;
            padding: 0.25rem 0.5rem;
        }

        .empty-state {
            text-align: center;
            padding: 2rem;
            color: #6c757d;
        }

        .empty-state i {
            margin-bottom: 1rem;
            color: #dee2e6;
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
                        <a class="nav-link active" href="${ctx}/subjects">
                            <i class="fas fa-book me-1"></i> Subjects
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${ctx}/grades">
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
                <i class="fas fa-book me-2"></i> Subjects Management
            </h1>
            <a href="${ctx}/subjects?action=add" class="btn btn-primary">
                <i class="fas fa-plus me-1"></i> Add Subject
            </a>
        </div>

        <div class="card shadow mb-4">
            <div class="card-header py-3 d-flex justify-content-between align-items-center">
                <h6 class="m-0 font-weight-bold text-primary">Subjects List</h6>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty subjects}">
                        <div class="empty-state">
                            <i class="fas fa-book-open fa-4x"></i>
                            <h4>No subjects added yet</h4>
                            <p class="mb-4">Start by adding your first subject</p>
                            <a href="${ctx}/subjects?action=add" class="btn btn-success">
                                <i class="fas fa-plus me-1"></i> Create First Subject
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="subject" items="${subjects}">
                                        <tr>
                                            <td>${subject.id}</td>
                                            <td>${subject.name}</td>
                                            <td class="action-buttons">
                                                <div class="d-flex gap-2">
                                                    <a href="${ctx}/subjects?action=edit&id=${subject.id}"
                                                       class="btn btn-sm btn-warning">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                    <form action="${ctx}/subjects" method="post"
                                                          onsubmit="return confirm('Delete this subject?')">
                                                        <input type="hidden" name="action" value="delete">
                                                        <input type="hidden" name="id" value="${subject.id}">
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
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>