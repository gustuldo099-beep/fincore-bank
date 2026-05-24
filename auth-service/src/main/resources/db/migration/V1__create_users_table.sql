CREATE TABLE IF NOT EXISTS users (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    full_name       VARCHAR(255) NOT NULL,
    cpf             VARCHAR(14),
    role            VARCHAR(20)  NOT NULL DEFAULT 'USER',
    enabled         BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT users_email_unique UNIQUE (email),
    CONSTRAINT users_cpf_unique   UNIQUE (cpf),
    CONSTRAINT users_role_check   CHECK  (role IN ('USER', 'ADMIN'))
);

CREATE INDEX idx_users_email   ON users (email);
CREATE INDEX idx_users_cpf     ON users (cpf);
CREATE INDEX idx_users_enabled ON users (enabled);

COMMENT ON TABLE  users          IS 'Usuários do sistema FinCore';
COMMENT ON COLUMN users.password IS 'Hash BCrypt — nunca armazenar em texto puro';
COMMENT ON COLUMN users.cpf      IS 'CPF sem formatação';
