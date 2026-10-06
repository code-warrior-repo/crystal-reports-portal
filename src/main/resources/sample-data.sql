INSERT INTO report_group (id, code, name, sort_order, active)
VALUES (1, 'FINANCE', 'Finance Reports', 10, 'Y');

INSERT INTO report_group (id, code, name, sort_order, active)
VALUES (2, 'OPERATIONS', 'Operations Reports', 20, 'Y');

INSERT INTO report_definition (id, group_id, code, name, rpt_file, description, active, sort_order)
VALUES (100, 1, 'REVENUE_SUMMARY', 'Revenue Summary', 'finance/revenue-summary.rpt',
        'Revenue summary by date range.', 'Y', 10);

INSERT INTO report_definition (id, group_id, code, name, rpt_file, description, active, sort_order)
VALUES (200, 2, 'DAILY_VOLUME', 'Daily Volume', 'operations/daily-volume.rpt',
        'Daily operational volume.', 'Y', 10);

INSERT INTO report_role (report_id, role_code)
VALUES (100, 'FINANCE_REPORTS');

INSERT INTO report_role (report_id, role_code)
VALUES (200, 'OPERATIONS_REPORTS');

INSERT INTO report_parameter (id, report_id, name, label, type, required, default_value, sort_order)
VALUES (1000, 100, 'startDate', 'Start Date', 'DATE', 'Y', '', 10);

INSERT INTO report_parameter (id, report_id, name, label, type, required, default_value, sort_order)
VALUES (1001, 100, 'endDate', 'End Date', 'DATE', 'Y', '', 20);

INSERT INTO app_user_role (user_id, role_code)
VALUES ('jsmith', 'FINANCE_REPORTS');

INSERT INTO app_user_role (user_id, role_code)
VALUES ('developer1', 'DEVELOPER');

INSERT INTO app_user_role (user_id, role_code)
VALUES ('developer1', 'FINANCE_REPORTS');
