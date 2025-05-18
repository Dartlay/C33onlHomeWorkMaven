<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Edit Grade</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-light">
    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">
                            <i class="fas fa-edit me-2"></i> Edit Grade
                        </h4>
                    </div>
                    <div class="card-body">
                        <form action="${ctx}/grades" method="post">
                            <input type="hidden" name="action" value="edit">
                            <input type="hidden" name="id" value="${grade.id}">
                            <input type="hidden" name="date" value="${grade.date}">
                            <div class="mb-3">
                                <label for="studentId" class="form-label">Student:</label>
                                <select id="studentId" name="studentId" class="form-select" required>
                                    <c:forEach var="student" items="${students}">
                                        <option value="${student.id}" ${student.id == grade.studentId ? 'selected' : ''}>
                                            ${student.name} (${student.group})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="subjectId" class="form-label">Subject:</label>
                                <select id="subjectId" name="subjectId" class="form-select" required>
                                    <c:forEach var="subject" items="${subjects}">
                                        <option value="${subject.id}" ${subject.id == grade.subjectId ? 'selected' : ''}>
                                            ${subject.name}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="value" class="form-label">Grade (1-5):</label>
                                <input type="number" id="value" name="value" class="form-control"
                                       value="${grade.value}" min="1" max="5" required>
                            </div>
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="${ctx}/grades" class="btn btn-secondary me-md-2">
                                    <i class="fas fa-times me-1"></i> Cancel
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save me-1"></i> Save Changes
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>