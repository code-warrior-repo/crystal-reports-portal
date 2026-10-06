package com.company.reports.crystal;

import org.springframework.stereotype.Component;

@Component
public class CrystalConnectionConfigurer {

    public void apply(Object reportClientDocument) {
        // Prefer reports that use the Liberty-managed datasource or saved report connection.
        // If your Crystal runtime requires runtime DB logon, put that vendor-specific code here.
        // Keep it centralized so leaked connections can be fixed in one place.
    }
}
