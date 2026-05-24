CREATE TABLE IF NOT EXISTS transactions (
    id               BIGSERIAL PRIMARY KEY,
    transaction_id   VARCHAR(36)    NOT NULL,
    account_id       BIGINT         NOT NULL REFERENCES accounts(id),
    type             VARCHAR(20)    NOT NULL,
    amount           NUMERIC(19,2)  NOT NULL,
    balance_before   NUMERIC(19,2)  NOT NULL,
    balance_after    NUMERIC(19,2)  NOT NULL,
    description      VARCHAR(255),
    counterpart_key  VARCHAR(255),
    status           VARCHAR(20)    NOT NULL DEFAULT 'COMPLETED',
    created_at       TIMESTAMP      NOT NULL DEFAULT NOW(),

    CONSTRAINT transactions_id_unique   UNIQUE (transaction_id),
    CONSTRAINT transactions_amount_check CHECK (amount > 0),
    CONSTRAINT transactions_type_check  CHECK (type IN ('PIX_IN','PIX_OUT','TED_IN','TED_OUT','DEPOSIT','WITHDRAWAL')),
    CONSTRAINT transactions_status_check CHECK (status IN ('PENDING','COMPLETED','FAILED','REVERSED'))
);

CREATE INDEX idx_transactions_account_id  ON transactions (account_id);
CREATE INDEX idx_transactions_created_at  ON transactions (created_at);
CREATE INDEX idx_transactions_type        ON transactions (type);
CREATE INDEX idx_transactions_status      ON transactions (status);

COMMENT ON TABLE transactions IS 'Histórico de todas as transações financeiras';
