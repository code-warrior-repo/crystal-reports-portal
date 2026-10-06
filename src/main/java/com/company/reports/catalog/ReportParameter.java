package com.company.reports.catalog;

public record ReportParameter(
    String name,
    String label,
    String type,
    boolean required,
    String defaultValue
) {

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
