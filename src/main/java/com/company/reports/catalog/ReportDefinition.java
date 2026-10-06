package com.company.reports.catalog;

public record ReportDefinition(Long id, String code, String name, String rptFile, String description) {

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getRptFile() {
        return rptFile;
    }

    public String getDescription() {
        return description;
    }
}
