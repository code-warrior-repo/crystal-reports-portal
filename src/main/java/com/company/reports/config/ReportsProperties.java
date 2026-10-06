package com.company.reports.config;

import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "reports")
public class ReportsProperties {

    private String rptRootPath;
    private Launch launch = new Launch();
    private Security security = new Security();

    public String getRptRootPath() {
        return rptRootPath;
    }

    public void setRptRootPath(String rptRootPath) {
        this.rptRootPath = rptRootPath;
    }

    public Launch getLaunch() {
        return launch;
    }

    public void setLaunch(Launch launch) {
        this.launch = launch;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public static class Launch {
        private String sharedSecret;
        private long maxTokenAgeSeconds = 300;

        public String getSharedSecret() {
            return sharedSecret;
        }

        public void setSharedSecret(String sharedSecret) {
            this.sharedSecret = sharedSecret;
        }

        public Duration getMaxTokenAge() {
            return Duration.ofSeconds(maxTokenAgeSeconds);
        }

        public long getMaxTokenAgeSeconds() {
            return maxTokenAgeSeconds;
        }

        public void setMaxTokenAgeSeconds(long seconds) {
            this.maxTokenAgeSeconds = seconds;
        }
    }

    public static class Security {
        private List<String> developerRoles = List.of("DEVELOPER", "REPORT_ADMIN");

        public List<String> getDeveloperRoles() {
            return developerRoles;
        }

        public void setDeveloperRoles(List<String> developerRoles) {
            this.developerRoles = developerRoles;
        }
    }
}
