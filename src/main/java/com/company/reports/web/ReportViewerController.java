package com.company.reports.web;

import com.company.reports.crystal.CrystalReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReportViewerController {

    private final CrystalReportService crystalReportService;

    public ReportViewerController(CrystalReportService crystalReportService) {
        this.crystalReportService = crystalReportService;
    }

    @PostMapping("/reports/{reportCode}/viewer")
    public void viewer(@PathVariable String reportCode,
                       @RequestParam Map<String, String> ignored,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        crystalReportService.renderReport(reportCode, request.getParameterMap(), request, response);
    }
}
