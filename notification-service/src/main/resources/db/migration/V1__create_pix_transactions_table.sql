CREATE TABLE IF NOT EXISTS pix_transactions (
    id                  BIGSERIAL PRIMARY KEY,
    transaction_id      VARCHAR(36)    NOT NULL,
    sender_account_id   BIGINT         NOT NULL,
    sender_email        VARCHAR(255)   NOT NULL,
    receiver_key        VARCHAR(255)   NOT NULL,
    receiver_account_id BIGINT,
    amount              NUMERIC(19,2)  NOT NULL,
    description         VARCHAR(255),
    status              VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    failure_reason      VARCHAR(500),
    created_at          TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP      NOT NULL DEFAULT NOW(),

    CONSTRAINT pix_transaction_id_unique UNIQUE (transaction_id),
    CONSTRAINT pix_amount_check          CHECK  (amount > 0),
    CONSTRAINT pix_status_check          CHECK  (status IN ('PENDING','PROCESSING','COMPLETED','FAILED'))
);

CREATE INDEX idx_pix_transaction_id    ON pix_transactions (transaction_id);
CREATE INDEX idx_pix_sender_account_id ON pix_transactions (sender_account_id);
CREATE INDEX idx_pix_sender_email      ON pix_transactions (sender_email);
CREATE INDEX idx_pix_status            ON pix_transactions (status);
CREATE INDEX idx_pix_created_at        ON pix_transactions (created_at);

COMMENT ON TABLE pix_transactions IS 'Registro de todas as transações PIX';
