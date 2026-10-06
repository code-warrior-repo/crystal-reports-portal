CREATE TABLE report_group (
    id BIGINT PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    sort_order INT NOT NULL,
    active CHAR(1) NOT NULL
);

CREATE TABLE report_definition (
    id BIGINT PRIMARY KEY,
    group_id BIGINT NOT NULL,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    rpt_file VARCHAR(500) NOT NULL,
    description VARCHAR(1000),
    active CHAR(1) NOT NULL,
    sort_order INT NOT NULL,
    FOREIGN KEY (group_id) REFERENCES report_group(id)
);

CREATE TABLE report_role (
    report_id BIGINT NOT NULL,
    role_code VARCHAR(100) NOT NULL,
    PRIMARY KEY (report_id, role_code),
    FOREIGN KEY (report_id) REFERENCES report_definition(id)
);

CREATE TABLE report_parameter (
    id BIGINT PRIMARY KEY,
    report_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    label VARCHAR(200) NOT NULL,
    type VARCHAR(50) NOT NULL,
    required CHAR(1) NOT NULL,
    default_value VARCHAR(500),
    sort_order INT NOT NULL,
    FOREIGN KEY (report_id) REFERENCES report_definition(id)
);

CREATE TABLE app_user_role (
    user_id VARCHAR(100) NOT NULL,
    role_code VARCHAR(100) NOT NULL,
    PRIMARY KEY (user_id, role_code)
);

CREATE TABLE used_launch_nonce (
    nonce VARCHAR(100) PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    used_at TIMESTAMP NOT NULL
);

CREATE TABLE report_audit (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    report_code VARCHAR(100) NOT NULL,
    action VARCHAR(50) NOT NULL,
    parameters_json CLOB,
    status VARCHAR(50) NOT NULL,
    message VARCHAR(1000),
    created_at TIMESTAMP NOT NULL
);
