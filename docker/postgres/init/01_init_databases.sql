-- Recria o superuser com senha simples
ALTER USER fincore_admin WITH PASSWORD 'fincore123';

-- Cria os bancos se não existirem
CREATE DATABASE fincore_auth OWNER fincore_admin;
CREATE DATABASE fincore_account OWNER fincore_admin;
CREATE DATABASE fincore_txn OWNER fincore_admin;

-- Cria usuários específicos
DO $$ BEGIN
  CREATE USER auth_user WITH PASSWORD 'fincore123' CONNECTION LIMIT 20;
EXCEPTION WHEN duplicate_object THEN NULL; END $$;

DO $$ BEGIN
  CREATE USER account_user WITH PASSWORD 'fincore123' CONNECTION LIMIT 20;
EXCEPTION WHEN duplicate_object THEN NULL; END $$;

DO $$ BEGIN
  CREATE USER txn_user WITH PASSWORD 'fincore123' CONNECTION LIMIT 20;
EXCEPTION WHEN duplicate_object THEN NULL; END $$;

-- Permissões
GRANT CONNECT ON DATABASE fincore_auth TO auth_user;
GRANT CONNECT ON DATABASE fincore_account TO account_user;
GRANT CONNECT ON DATABASE fincore_txn TO txn_user;
