package com.company.reports.catalog;

public record ReportMenuItem(String groupCode, String groupName, String reportCode, String reportName) {

    public String getGroupCode() {
        return groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getReportCode() {
        return reportCode;
    }

    public String getReportName() {
        return reportName;
    }
}
