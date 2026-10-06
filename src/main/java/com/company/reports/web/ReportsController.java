package com.company.reports.web;

import com.company.reports.catalog.ReportCatalogService;
import com.company.reports.config.ReportsProperties;
import com.company.reports.security.CurrentUser;
import com.company.reports.security.CurrentUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ReportsController {

    private final ReportCatalogService catalogService;
    private final CurrentUserService currentUserService;
    private final ReportsProperties reportsProperties;

    public ReportsController(ReportCatalogService catalogService,
                             CurrentUserService currentUserService,
                             ReportsProperties reportsProperties) {
        this.catalogService = catalogService;
        this.currentUserService = currentUserService;
        this.reportsProperties = reportsProperties;
    }

    @GetMapping("/reports")
    public String home(Model model) {
        addCommonModel(model);
        return "reports/home";
    }

    @GetMapping("/reports/{reportCode}/params")
    public String params(@PathVariable String reportCode, Model model) {
        addCommonModel(model);
        model.addAttribute("selectedReport", catalogService.requireAuthorizedDefinition(reportCode));
        model.addAttribute("parameters", catalogService.parametersForAuthorizedReport(reportCode));
        return "reports/params";
    }

    private void addCommonModel(Model model) {
        CurrentUser user = currentUserService.requiredUser();
        model.addAttribute("currentUser", user);
        model.addAttribute("reportGroups", catalogService.allowedMenu().values());
        model.addAttribute("showQuartz", user.hasAnyRole(reportsProperties.getSecurity().getDeveloperRoles()));
    }
}
