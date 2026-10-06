<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Crystal Reports</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/app.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/reports">Crystal Reports</a>
        <div class="collapse navbar-collapse show">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/reports">Reports</a>
                </li>
                <c:if test="${showQuartz}">
                    <li class="nav-item">
                        <a class="nav-link disabled" href="#" tabindex="-1" aria-disabled="true">Quartz Jobs</a>
                    </li>
                </c:if>
            </ul>
            <span class="navbar-text">${currentUser.userId}</span>
        </div>
    </div>
</nav>

<div class="app-shell">
    <aside class="report-sidebar">
        <jsp:include page="reports/sidebar.jsp"/>
    </aside>
    <main id="report-body" class="report-body">
        <jsp:include page="${bodyPage}"/>
    </main>
</div>

<footer class="app-footer">
    <span>Crystal Reports Portal</span>
</footer>

<script src="${pageContext.request.contextPath}/static/js/reports.js"></script>
</body>
</html>
