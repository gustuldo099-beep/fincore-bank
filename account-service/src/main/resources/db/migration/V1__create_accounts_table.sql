CREATE TABLE IF NOT EXISTS accounts (
    id              BIGSERIAL PRIMARY KEY,
    account_number  VARCHAR(20)    NOT NULL,
    agency          VARCHAR(10)    NOT NULL DEFAULT '0001',
    owner_email     VARCHAR(255)   NOT NULL,
    owner_name      VARCHAR(255)   NOT NULL,
    balance         NUMERIC(19,2)  NOT NULL DEFAULT 0.00,
    account_type    VARCHAR(20)    NOT NULL DEFAULT 'CHECKING',
    status          VARCHAR(20)    NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP      NOT NULL DEFAULT NOW(),

    CONSTRAINT accounts_number_unique UNIQUE (account_number),
    CONSTRAINT accounts_balance_check CHECK (balance >= 0),
    CONSTRAINT accounts_type_check    CHECK (account_type IN ('CHECKING', 'SAVINGS')),
    CONSTRAINT accounts_status_check  CHECK (status IN ('ACTIVE', 'INACTIVE', 'BLOCKED'))
);

CREATE INDEX idx_accounts_owner_email  ON accounts (owner_email);
CREATE INDEX idx_accounts_number       ON accounts (account_number);
CREATE INDEX idx_accounts_status       ON accounts (status);

COMMENT ON TABLE  accounts         IS 'Contas bancárias dos usuários';
COMMENT ON COLUMN accounts.balance IS 'Saldo em reais — nunca negativo';
