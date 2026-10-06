package com.company.reports.security;

import com.company.reports.catalog.ReportCatalogRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class ReportAuthorizationService {

    private final CurrentUserService currentUserService;
    private final ReportCatalogRepository reportCatalogRepository;

    public ReportAuthorizationService(CurrentUserService currentUserService,
                                      ReportCatalogRepository reportCatalogRepository) {
        this.currentUserService = currentUserService;
        this.reportCatalogRepository = reportCatalogRepository;
    }

    public void requireAccessToReport(String reportCode) {
        CurrentUser user = currentUserService.requiredUser();
        boolean allowed = reportCatalogRepository.userCanAccessReport(reportCode, user.roles());
        if (!allowed) {
            throw new AccessDeniedException("User is not allowed to access report " + reportCode);
        }
    }
}
