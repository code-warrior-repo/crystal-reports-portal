package com.company.reports.catalog;

import java.util.List;

public record ReportGroup(String code, String name, List<ReportMenuItem> reports) {

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<ReportMenuItem> getReports() {
        return reports;
    }
}
