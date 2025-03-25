<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>C33onlHomeWorkMaven</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Welcome to C33onlHomeWorkMaven</h1>

        <div class="buttons">
            <a href="${pageContext.request.contextPath}/book" class="btn download-btn">Download Book</a>

            <form action="${pageContext.request.contextPath}/load-book" method="post" enctype="multipart/form-data">
                <input type="file" name="file" required>
                <button type="submit" class="btn upload-btn">Upload Book</button>
            </form>
        </div>
    </div>
</body>
</html>