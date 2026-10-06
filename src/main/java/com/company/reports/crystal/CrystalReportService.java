package com.company.reports.crystal;

import com.company.reports.audit.ReportAuditService;
import com.company.reports.catalog.ReportCatalogService;
import com.company.reports.catalog.ReportDefinition;
import com.company.reports.config.ReportsProperties;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CrystalReportService {

    private final ReportsProperties properties;
    private final ServletContext servletContext;
    private final ReportCatalogService catalogService;
    private final CrystalConnectionConfigurer connectionConfigurer;
    private final CrystalParameterBinder parameterBinder;
    private final CrystalResourceCleaner resourceCleaner;
    private final ReportAuditService auditService;

    public CrystalReportService(ReportsProperties properties,
                                ServletContext servletContext,
                                ReportCatalogService catalogService,
                                CrystalConnectionConfigurer connectionConfigurer,
                                CrystalParameterBinder parameterBinder,
                                CrystalResourceCleaner resourceCleaner,
                                ReportAuditService auditService) {
        this.properties = properties;
        this.servletContext = servletContext;
        this.catalogService = catalogService;
        this.connectionConfigurer = connectionConfigurer;
        this.parameterBinder = parameterBinder;
        this.resourceCleaner = resourceCleaner;
        this.auditService = auditService;
    }

    public void renderReport(String reportCode,
                             Map<String, String[]> parameters,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        Object reportClientDocument = null;
        Object viewer = null;

        try {
            ReportDefinition definition = catalogService.requireAuthorizedDefinition(reportCode);
            Path rptPath = Path.of(properties.getRptRootPath(), definition.rptFile()).normalize();

            reportClientDocument = newReportClientDocument();
            invoke(reportClientDocument, "open", rptPath.toString(), Integer.valueOf(0));

            connectionConfigurer.apply(reportClientDocument);
            parameterBinder.apply(reportClientDocument, parameters);

            viewer = newCrystalReportViewer();
            Object reportSource = invoke(reportClientDocument, "getReportSource");
            invoke(viewer, "setReportSource", reportSource);
            invoke(viewer, "processHttpRequest", request, response, servletContext, null);

            auditService.success(reportCode, "VIEW", "{}");
        } catch (Exception exception) {
            auditService.failure(reportCode, "VIEW", "{}", exception);
            throw new ReportRenderException("Unable to render Crystal report " + reportCode, exception);
        } finally {
            resourceCleaner.disposeViewer(viewer);
            resourceCleaner.closeReport(reportClientDocument);
        }
    }

    private Object newReportClientDocument() throws ReflectiveOperationException {
        return Class.forName("com.crystaldecisions.reports.sdk.ReportClientDocument")
            .getConstructor()
            .newInstance();
    }

    private Object newCrystalReportViewer() throws ReflectiveOperationException {
        return Class.forName("com.crystaldecisions.report.web.viewer.CrystalReportViewer")
            .getConstructor()
            .newInstance();
    }

    private Object invoke(Object target, String methodName, Object... args) {
        try {
            Method method = findMethod(target.getClass(), methodName, args.length);
            return method.invoke(target, args);
        } catch (Exception exception) {
            throw new ReportRenderException("Crystal invocation failed: " + methodName, exception);
        }
    }

    private Method findMethod(Class<?> type, String methodName, int argCount) {
        for (Method method : type.getMethods()) {
            if (method.getName().equals(methodName) && method.getParameterCount() == argCount) {
                return method;
            }
        }
        throw new IllegalArgumentException("Method not found: " + methodName);
    }
}
