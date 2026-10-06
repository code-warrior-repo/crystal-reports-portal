<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="sidebar-title">Report Groups</div>
<c:forEach var="group" items="${reportGroups}">
    <div class="report-group">
        <div class="report-group-name">${group.name}</div>
        <div class="list-group list-group-flush">
            <c:forEach var="report" items="${group.reports}">
                <a class="list-group-item list-group-item-action report-link"
                   href="${pageContext.request.contextPath}/reports/${report.reportCode}/params"
                   data-target="report-body">
                    ${report.reportName}
                </a>
            </c:forEach>
        </div>
    </div>
</c:forEach>
