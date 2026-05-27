CREATE TABLE IF NOT EXISTS fraud_alerts (
    id              BIGSERIAL PRIMARY KEY,
    transaction_id  VARCHAR(36)    NOT NULL,
    account_id      BIGINT         NOT NULL,
    user_email      VARCHAR(255)   NOT NULL,
    amount          NUMERIC(19,2)  NOT NULL,
    rule_triggered  VARCHAR(100)   NOT NULL,
    severity        VARCHAR(20)    NOT NULL DEFAULT 'MEDIUM',
    status          VARCHAR(20)    NOT NULL DEFAULT 'OPEN',
    details         TEXT,
    resolved_at     TIMESTAMP,
    created_at      TIMESTAMP      NOT NULL DEFAULT NOW(),

    CONSTRAINT fraud_alerts_severity_check CHECK (severity IN ('LOW','MEDIUM','HIGH','CRITICAL')),
    CONSTRAINT fraud_alerts_status_check   CHECK (status   IN ('OPEN','INVESTIGATING','RESOLVED','FALSE_POSITIVE'))
);

CREATE INDEX idx_fraud_alerts_transaction_id ON fraud_alerts (transaction_id);
CREATE INDEX idx_fraud_alerts_account_id     ON fraud_alerts (account_id);
CREATE INDEX idx_fraud_alerts_user_email     ON fraud_alerts (user_email);
CREATE INDEX idx_fraud_alerts_status         ON fraud_alerts (status);
CREATE INDEX idx_fraud_alerts_severity       ON fraud_alerts (severity);
CREATE INDEX idx_fraud_alerts_created_at     ON fraud_alerts (created_at);

COMMENT ON TABLE fraud_alerts IS 'Alertas de fraude detectados pelo sistema';
