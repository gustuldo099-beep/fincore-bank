# 🏦 FinCore Banking System

Sistema bancário baseado em microsserviços desenvolvido com Java 21 e Spring Boot 3.3.6, implementando operações financeiras como autenticação JWT, gerenciamento de contas e transferências PIX com processamento assíncrono via Apache Kafka.


---

## 📐 Arquitetura

```
┌─────────────────────────────────────────────────────────────┐
│                        Cliente (curl / Postman)              │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP REST
          ┌────────────────┼────────────────────┐
          ▼                ▼                    ▼
   ┌─────────────┐  ┌─────────────┐   ┌─────────────────┐
   │auth-service │  │account-     │   │transaction-     │
   │  :8081      │  │service :8082│   │service :8083    │
   └─────────────┘  └─────────────┘   └────────┬────────┘
          │                │                    │
          └────────────────┘              Kafka │ pix-initiated
                    │                           │ pix-completed
               PostgreSQL                       │ pix-failed
                                    ┌───────────┼───────────┐
                                    ▼           ▼           ▼
                             ┌──────────┐ ┌──────────┐ (fraud)
                             │notif-    │ │fraud-    │
                             │service   │ │service   │
                             │:8084     │ │:8085     │
                             └──────────┘ └──────────┘
```

### Microsserviços

| Serviço | Porta | Responsabilidade |
|---|---|---|
| **auth-service** | 8081 | Registro, login e validação JWT |
| **account-service** | 8082 | Criação de contas, depósitos e extratos |
| **transaction-service** | 8083 | Iniciação e processamento de PIX |
| **notification-service** | 8084 | Notificações de eventos financeiros |
| **fraud-service** | 8085 | Análise de fraude em tempo real |

---

## 🛠️ Stack Tecnológica

- **Java 21** + **Spring Boot 3.3.6**
- **Spring Security** + **JWT** (jjwt 0.12.6)
- **Spring Data JPA** + **Hibernate**
- **PostgreSQL 16** (banco de dados)
- **Apache Kafka** (mensageria assíncrona)
- **Flyway** (migrations de banco)
- **Lombok** (redução de boilerplate)
- **Docker** + **Docker Compose**

---

## 🚀 Como Executar

### Pré-requisitos

- Java 21+
- Docker Desktop
- WSL2 (para Windows)

### 1. Clone o repositório

```bash
git clone https://github.com/gustulodo099-beep/fincore-bank.git
cd fincore-bank
```

### 2. Suba a infraestrutura (PostgreSQL + Kafka)

```bash
docker compose up -d postgres zookeeper kafka
```

### 3. Inicie os microsserviços (em terminais separados)

```bash
# auth-service
cd auth-service && ./mvnw spring-boot:run

# account-service
cd account-service && ./mvnw spring-boot:run

# transaction-service
cd transaction-service && ./mvnw spring-boot:run

# notification-service
cd notification-service && ./mvnw spring-boot:run

# fraud-service
cd fraud-service && ./mvnw spring-boot:run
```

> **Windows:** Execute os serviços via WSL2 para garantir conectividade com a rede Docker.

---

## 📡 Endpoints

### Auth Service (`:8081`)

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/auth/register` | Cadastro de usuário |
| `POST` | `/api/v1/auth/login` | Login e geração de JWT |
| `POST` | `/api/v1/auth/refresh` | Renovação do token |
| `GET` | `/api/v1/auth/me` | Dados do usuário autenticado |

### Account Service (`:8082`)

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/accounts` | Criar conta bancária |
| `GET` | `/api/v1/accounts/my` | Listar contas do usuário |
| `GET` | `/api/v1/accounts/{id}` | Buscar conta por ID |
| `POST` | `/api/v1/accounts/{id}/deposit` | Realizar depósito |
| `GET` | `/api/v1/accounts/{id}/statement` | Extrato da conta |

### Transaction Service (`:8083`)

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/pix` | Iniciar transferência PIX |
| `GET` | `/api/v1/pix/{transactionId}` | Consultar transação |
| `GET` | `/api/v1/pix/history` | Histórico de transações |

### Notification Service (`:8084`)

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/v1/notifications` | Listar notificações |
| `GET` | `/api/v1/notifications/unread-count` | Contador de não lidas |
| `PUT` | `/api/v1/notifications/{id}/read` | Marcar como lida |

---

## 🔄 Fluxo PIX

```
1. Cliente envia POST /api/v1/pix
2. transaction-service valida saldo e publica evento no Kafka (pix-initiated)
3. fraud-service consome o evento e analisa por fraude
4. transaction-service processa a transferência e publica (pix-completed / pix-failed)
5. account-service atualiza os saldos
6. notification-service envia notificação ao remetente
```

---

## 🧪 Exemplos de Uso

### Registro de usuário

```bash
curl -X POST http://localhost:8081/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "João Silva",
    "email": "joao@email.com",
    "password": "Senha@123",
    "cpf": "12345678901"
  }'
```

### Criar conta

```bash
curl -X POST http://localhost:8082/api/v1/accounts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "accountType": "CHECKING",
    "pixKey": "joao@email.com",
    "ownerName": "João Silva",
    "ownerEmail": "joao@email.com"
  }'
```

### Transferência PIX

```bash
curl -X POST http://localhost:8083/api/v1/pix \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "senderAccountId": 1,
    "receiverKey": "maria@email.com",
    "amount": 100.00,
    "description": "Pagamento"
  }'
```

---

## 🛡️ Regras de Fraude

O **fraud-service** analisa cada transação PIX com 4 regras:

| Regra | Condição | Severidade |
|---|---|---|
| `AMOUNT_EXCEEDED_LIMIT` | Valor > R$ 5.000 | ALTA |
| `SUSPICIOUS_AMOUNT` | Valor entre R$ 1.000 e R$ 5.000 | MÉDIA |
| `HIGH_FREQUENCY` | Mais de 10 transações no dia | ALTA |
| `SUSPICIOUS_RECEIVER_KEY` | Chave PIX suspeita | MÉDIA |

---

## 🗄️ Bancos de Dados

| Banco | Serviços |
|---|---|
| `fincore_auth` | auth-service |
| `fincore_account` | account-service, notification-service, fraud-service |
| `fincore_txn` | transaction-service |

---

## 📬 Tópicos Kafka

| Tópico | Produtor | Consumidores |
|---|---|---|
| `pix-initiated` | transaction-service | fraud-service |
| `pix-completed` | transaction-service | notification-service |
| `pix-failed` | transaction-service | notification-service |

---

## 👤 Autor

**Gustavo** — [GitHub](https://github.com/gustulodo099-beep)
