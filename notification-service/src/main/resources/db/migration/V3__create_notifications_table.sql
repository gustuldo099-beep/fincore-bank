-- Tabela de notificações (no banco fincore_account para simplicidade)
CREATE TABLE IF NOT EXISTS notifications (
    id              BIGSERIAL PRIMARY KEY,
    user_email      VARCHAR(255)  NOT NULL,
    type            VARCHAR(50)   NOT NULL,
    title           VARCHAR(255)  NOT NULL,
    message         TEXT          NOT NULL,
    reference_id    VARCHAR(36),
    read            BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP     NOT NULL DEFAULT NOW(),

    CONSTRAINT notifications_type_check CHECK (
        type IN ('PIX_COMPLETED', 'PIX_FAILED', 'DEPOSIT', 'LOGIN', 'SECURITY_ALERT')
    )
);

CREATE INDEX idx_notifications_user_email ON notifications (user_email);
CREATE INDEX idx_notifications_read       ON notifications (read);
CREATE INDEX idx_notifications_created_at ON notifications (created_at);

COMMENT ON TABLE notifications IS 'Notificações dos usuários geradas por eventos do sistema';
