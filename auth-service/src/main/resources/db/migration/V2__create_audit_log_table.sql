CREATE TABLE IF NOT EXISTS auth_audit_log (
    id              BIGSERIAL PRIMARY KEY,
    user_email      VARCHAR(255),
    event_type      VARCHAR(50)  NOT NULL,
    ip_address      VARCHAR(45),
    user_agent      VARCHAR(500),
    success         BOOLEAN      NOT NULL,
    failure_reason  VARCHAR(255),
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT audit_event_type_check CHECK (
        event_type IN ('LOGIN', 'LOGOUT', 'REGISTER', 'REFRESH_TOKEN', 'LOGIN_FAILED')
    )
);

CREATE INDEX idx_audit_user_email ON auth_audit_log (user_email);
CREATE INDEX idx_audit_created_at ON auth_audit_log (created_at);
CREATE INDEX idx_audit_event_type ON auth_audit_log (event_type);
CREATE INDEX idx_audit_ip_address ON auth_audit_log (ip_address);

COMMENT ON TABLE auth_audit_log IS 'Log de auditoria de autenticação';
