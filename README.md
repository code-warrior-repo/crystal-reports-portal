# Crystal Reports Portal

Spring Boot WAR starter for a JSP + Bootstrap Crystal Reports portal deployed on Liberty.

## Runtime

- JDK 21
- Spring Boot 4
- Gradle
- JSP
- JDBC metadata access
- Liberty datasource
- SAP Crystal Reports Java runtime jars in `lib/crystal`

## Launch Contract

The upstream portal should not pass a raw `userId` or `JSESSIONID`.
Use a signed, short-lived, one-time token:

```text
/launch?token=base64url(userId).base64url(epochSeconds).base64url(nonce).base64url(hmac)
```

The reports app validates the token, loads roles from `app_user_role`, creates its own session, then redirects to `/reports`.

The upstream portal can generate the token with the same algorithm as
`com.company.reports.launch.LaunchTokenSupport#createToken`.

## Crystal Cleanup Rule

Only `CrystalReportService` opens Crystal documents and viewers.
It always runs cleanup in `finally`:

```text
viewer.dispose()
reportClientDocument.close()
```

Controllers and JSPs must never open Crystal reports directly.

## Build

```bash
./gradlew clean war
```

Copy `build/libs/crystal-reports-portal.war` to Liberty.

## Required Local Configuration

- Put approved Crystal runtime jars in `lib/crystal`.
- Replace `reports.launch.shared-secret`.
- Configure Liberty `jdbc/ReportsDS`.
- Set `reports.rpt-root-path` to the folder containing `.rpt` files.
- Use `schema.sql` and optionally `sample-data.sql` to create the starter metadata tables.
