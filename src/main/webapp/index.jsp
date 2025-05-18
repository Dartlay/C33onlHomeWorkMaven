<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>School Dashboard</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Custom CSS -->
    <style>
        .card-icon {
            font-size: 2rem;
            opacity: 0.8;
        }
        .bg-primary { background-color: #4e73df !important; }
        .bg-success { background-color: #1cc88a !important; }
        .bg-info { background-color: #36b9cc !important; }
        .bg-warning { background-color: #f6c23e !important; }
        .stat-number {
            font-size: 1.75rem;
            font-weight: 700;
        }
        .sidebar {
            min-height: 100vh;
            background: linear-gradient(180deg, #4e73df 10%, #224abe 100%);
        }
        .sidebar .nav-link {
            color: rgba(255, 255, 255, 0.8);
            padding: 1rem;
            font-weight: 600;
        }
        .sidebar .nav-link:hover {
            color: #fff;
            background-color: rgba(255, 255, 255, 0.1);
        }
        .sidebar .nav-link.active {
            color: #fff;
        }
        .topbar {
            height: 4.375rem;
            box-shadow: 0 0.15rem 1.75rem 0 rgba(58, 59, 69, 0.15);
        }
    </style>
</head>
<body>
    <div class="d-flex">
        <!-- Sidebar -->
        <div class="sidebar d-none d-lg-block">
            <div class="px-3 py-4">
                <div class="sidebar-brand d-flex align-items-center justify-content-center">
                    <i class="fas fa-graduation-cap fa-2x text-white me-2"></i>
                    <div class="fs-4 fw-bold text-white">StudentLG</div>
                </div>
                <hr class="my-4 text-white">
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link active" href="${ctx}/">
                            <i class="fas fa-fw fa-home me-2"></i>Dashboard
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${ctx}/students">
                            <i class="fas fa-fw fa-users me-2"></i>Students
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${ctx}/subjects">
                            <i class="fas fa-fw fa-book me-2"></i>Subjects
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${ctx}/grades">
                            <i class="fas fa-fw fa-star me-2"></i>Grades
                        </a>
                    </li>
                </ul>
            </div>
        </div>
        <!-- Main Content -->
        <div class="flex-grow-1">
            <!-- Topbar -->
            <nav class="navbar topbar mb-4 static-top shadow">
                <div class="container-fluid justify-content-end">
                    <div class="text-dark fw-bold">
                        <i class="fas fa-calendar me-2"></i>
                        <span id="current-date"></span>
                    </div>
                </div>
            </nav>
            <!-- Content -->
            <div class="container-fluid px-4">
                <!-- Quick Actions -->
                <div class="row mb-4">
                    <div class="col-12">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Quick Actions</h6>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-4 mb-3">
                                        <a href="${ctx}/students?action=add" class="btn btn-primary w-100 py-3">
                                            <i class="fas fa-user-plus fa-2x mb-2"></i><br>
                                            Add New Student
                                        </a>
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <a href="${ctx}/subjects?action=add" class="btn btn-success w-100 py-3">
                                            <i class="fas fa-book-medical fa-2x mb-2"></i><br>
                                            Add New Subject
                                        </a>
                                    </div>
                                    <div class="col-md-4">
                                        <a href="${ctx}/grades?action=add" class="btn btn-info w-100 py-3 text-white">
                                            <i class="fas fa-star-half-alt fa-2x mb-2"></i><br>
                                            Add New Grade
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Page Heading -->
                                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                                    <h1 class="h3 mb-0 text-gray-800"></h1>
                                    <div>
                                        <a href="${ctx}/data?action=export&format=txt" class="btn btn-dark me-2">
                                            <i class="fas fa-download me-2"></i>Export TXT
                                        </a>
                                        <a href="${ctx}/data?action=export&format=json" class="btn btn-primary">
                                            <i class="fas fa-file-export me-2"></i>Export JSON
                                        </a>
                                    </div>
                                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Custom JS -->
    <script>
        const now = new Date();
        document.getElementById('current-date').textContent = now.toLocaleDateString('en-US', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    </script>
    </script>
        <script>
            setTimeout(function(){
                window.location.reload();
            }, 30000);
        </script>
</body>
</html>