package com.company.reports.catalog;

import com.company.reports.security.CurrentUser;
import com.company.reports.security.CurrentUserService;
import com.company.reports.security.ReportAuthorizationService;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ReportCatalogService {

    private final CurrentUserService currentUserService;
    private final ReportAuthorizationService authorizationService;
    private final ReportCatalogRepository repository;

    public ReportCatalogService(CurrentUserService currentUserService,
                                ReportAuthorizationService authorizationService,
                                ReportCatalogRepository repository) {
        this.currentUserService = currentUserService;
        this.authorizationService = authorizationService;
        this.repository = repository;
    }

    public Map<String, ReportGroup> allowedMenu() {
        CurrentUser user = currentUserService.requiredUser();
        return repository.groupMenuItems(repository.findMenuItemsForRoles(user.roles()));
    }

    public ReportDefinition requireAuthorizedDefinition(String reportCode) {
        authorizationService.requireAccessToReport(reportCode);
        return repository.findDefinition(reportCode)
            .orElseThrow(() -> new IllegalArgumentException("Unknown report " + reportCode));
    }

    public List<ReportParameter> parametersForAuthorizedReport(String reportCode) {
        authorizationService.requireAccessToReport(reportCode);
        return repository.findParameters(reportCode);
    }
}
