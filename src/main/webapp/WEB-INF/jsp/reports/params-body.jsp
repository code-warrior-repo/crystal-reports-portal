<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<section class="report-panel">
    <div class="report-heading">
        <h1>${selectedReport.name}</h1>
        <p>${selectedReport.description}</p>
    </div>

    <form method="post"
          action="${pageContext.request.contextPath}/reports/${selectedReport.code}/viewer"
          target="report-frame"
          class="report-params">
        <c:forEach var="param" items="${parameters}">
            <div class="mb-3">
                <label class="form-label" for="${param.name}">${param.label}</label>
                <input class="form-control"
                       id="${param.name}"
                       name="${param.name}"
                       value="${param.defaultValue}"
                       <c:if test="${param.required}">required</c:if>>
            </div>
        </c:forEach>
        <button type="submit" class="btn btn-primary">Run Report</button>
    </form>

    <iframe id="report-frame" name="report-frame" class="report-frame" title="Crystal report viewer"></iframe>
</section>
