-- ============================================================
-- Permissões no banco fincore_auth
-- ============================================================
\c fincore_auth

-- Revogar schema public do PUBLIC
REVOKE ALL ON SCHEMA public FROM PUBLIC;
GRANT USAGE ON SCHEMA public TO auth_user;

-- Permissões automáticas em tabelas futuras (criadas pelo Flyway)
ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO auth_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT USAGE, SELECT ON SEQUENCES TO auth_user;

-- Timeouts de segurança para evitar queries travadas
ALTER ROLE auth_user SET statement_timeout              = '30s';
ALTER ROLE auth_user SET lock_timeout                  = '10s';
ALTER ROLE auth_user SET idle_in_transaction_session_timeout = '60s';
