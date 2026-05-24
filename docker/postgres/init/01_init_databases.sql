-- ============================================================
-- Inicialização segura do PostgreSQL - FinCore Banking
-- ============================================================

-- 1. Criar bancos separados por serviço (isolamento total)
CREATE DATABASE fincore_auth    ENCODING 'UTF8';
CREATE DATABASE fincore_account ENCODING 'UTF8';
CREATE DATABASE fincore_txn     ENCODING 'UTF8';

-- 2. Usuários com privilégios mínimos por serviço
CREATE USER auth_user    WITH PASSWORD 'Auth$3rv1c3#2024!'    NOSUPERUSER NOCREATEDB NOCREATEROLE CONNECTION LIMIT 20;
CREATE USER account_user WITH PASSWORD 'Acc0unt$3rv1c3#2024!' NOSUPERUSER NOCREATEDB NOCREATEROLE CONNECTION LIMIT 20;
CREATE USER txn_user     WITH PASSWORD 'Txn$3rv1c3#2024!'     NOSUPERUSER NOCREATEDB NOCREATEROLE CONNECTION LIMIT 20;

-- 3. Revogar privilégios padrão perigosos do PUBLIC
REVOKE ALL ON DATABASE fincore_auth    FROM PUBLIC;
REVOKE ALL ON DATABASE fincore_account FROM PUBLIC;
REVOKE ALL ON DATABASE fincore_txn     FROM PUBLIC;

-- 4. Conceder apenas CONNECT (princípio do menor privilégio)
GRANT CONNECT ON DATABASE fincore_auth    TO auth_user;
GRANT CONNECT ON DATABASE fincore_account TO account_user;
GRANT CONNECT ON DATABASE fincore_txn     TO txn_user;
