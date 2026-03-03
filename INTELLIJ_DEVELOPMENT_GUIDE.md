# Running Services from IntelliJ IDEA - Complete Guide

## Problem
When running services from IntelliJ, you get "Port already in use" errors because Docker containers are using the same ports.

## Solution: Choose Your Development Mode

---

## 🎯 Option 1: Run Everything from IntelliJ (Development/Debugging)

### Step 1: Stop All Docker Services
```bash
cd /Users/ksaravanan/Downloads/Microservice_poc
docker-compose stop discovery-server user-service account-service transaction-service
```

### Step 2: Keep Only Infrastructure Running
```bash
# Keep databases and Kafka running
docker-compose ps

# You should see these running:
# - user-db (port 5434)
# - account-db (port 5433)
# - transaction-db (port 5435)
# - zookeeper
# - kafka
# - notification-service
```

### Step 3: Update Application Configurations

#### Discovery Server - No changes needed
Already configured for localhost.

#### User Service
File: `wallet-user-service/src/main/resources/application.yml`

**Change from:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://user-db:5432/user_db
```

**To:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5434/user_db
```

#### Account Service
File: `wallet-account-service/src/main/resources/application.yml`

**Change from:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://account-db:5432/account_db
```

**To:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/account_db
```

#### Transaction Service
File: `wallet-transaction-service/src/main/resources/application.yml`

**Change from:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://transaction-db:5432/transaction_db
  kafka:
    bootstrap-servers: kafka:29092
```

**To:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5435/transaction_db
  kafka:
    bootstrap-servers: localhost:9092
```

### Step 4: Run Services in IntelliJ

Run in this order:
1. **Discovery Server** (Port 8761) - FIRST!
2. Wait 30 seconds for Eureka to start
3. **User Service** (Port 8082)
4. **Account Service** (Port 8083)
5. **Transaction Service** (Port 8084)

---

## 🐳 Option 2: Run Everything from Docker (Production-like)

### Step 1: Stop IntelliJ Services
Stop all running services in IntelliJ.

### Step 2: Start All Docker Services
```bash
docker-compose up -d
```

### Step 3: Test
```bash
curl http://localhost:8082/users
curl http://localhost:8083/accounts
curl http://localhost:8084/transactions
```

---

## 🔀 Option 3: Hybrid Mode (Recommended for Development)

Run infrastructure in Docker, services in IntelliJ:

### Step 1: Run Infrastructure Only in Docker
```bash
# Stop all application services
docker-compose stop discovery-server user-service account-service transaction-service

# Keep databases and Kafka running
docker-compose ps
```

### Step 2: Create IntelliJ Run Profiles

#### For User Service:
- Main class: `com.wallet.wallet_user_service.WalletUserServiceApplication`
- VM Options: `-Dspring.datasource.url=jdbc:postgresql://localhost:5434/user_db`
- Program arguments: (none)

#### For Account Service:
- Main class: `com.wallet.wallet_account_service.WalletAccountServiceApplication`
- VM Options: `-Dspring.datasource.url=jdbc:postgresql://localhost:5433/account_db`

#### For Transaction Service:
- Main class: `com.wallet.wallet_transaction_service.WalletTransactionServiceApplication`
- VM Options: `-Dspring.datasource.url=jdbc:postgresql://localhost:5435/transaction_db -Dspring.kafka.bootstrap-servers=localhost:9092`

---

## 🚨 Quick Fix for Current Error

You just need to stop the Docker Discovery Server:

```bash
docker-compose stop discovery-server
```

Then run it again from IntelliJ.

---

## Port Reference

| Service | Port | Docker Host | Local Host |
|---------|------|-------------|------------|
| Discovery Server | 8761 | discovery-server | localhost |
| User Service | 8082 | user-service | localhost |
| Account Service | 8083 | account-service | localhost |
| Transaction Service | 8084 | transaction-service | localhost |
| User DB | 5434 | user-db:5432 | localhost:5434 |
| Account DB | 5433 | account-db:5432 | localhost:5433 |
| Transaction DB | 5435 | transaction-db:5432 | localhost:5435 |
| Kafka | 9092 | kafka:29092 | localhost:9092 |

---

## Application Profiles (Alternative Solution)

Create separate profiles for Docker and local development:

### application-docker.yml
```yaml
spring:
  datasource:
    url: jdbc:postgresql://user-db:5432/user_db
eureka:
  client:
    service-url:
      defaultZone: http://discovery-server:8761/eureka
```

### application-local.yml
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5434/user_db
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

### Run with Profile
```bash
# In IntelliJ VM Options:
-Dspring.profiles.active=local

# Or in Docker:
-Dspring.profiles.active=docker
```

---

## Troubleshooting

### "Port 8761 already in use"
```bash
# Find what's using the port
lsof -i :8761

# Stop Docker discovery server
docker-compose stop discovery-server

# OR change the port in application.yml
server:
  port: 8762
```

### "Connection refused to localhost:8761"
Discovery Server isn't running. Start it first (either in Docker or IntelliJ).

### "Unable to connect to database"
```bash
# Check if databases are running
docker-compose ps

# Verify database ports
docker-compose ps | grep db

# Test connection
telnet localhost 5434  # user-db
telnet localhost 5433  # account-db
telnet localhost 5435  # transaction-db
```

### Services not registering with Eureka
1. Make sure Discovery Server is running
2. Wait 30 seconds after starting Discovery Server
3. Check Eureka dashboard: http://localhost:8761
4. Verify `eureka.client.service-url.defaultZone` is set correctly

---

## Recommended Workflow

### For Development (Debugging):
1. Run databases + Kafka in Docker
2. Run Spring Boot services in IntelliJ
3. Use `-Dspring.profiles.active=local` profile

### For Testing (End-to-end):
1. Run everything in Docker
2. Use docker-compose commands
3. Check logs with `docker-compose logs -f`

### For Production:
1. Use Docker images
2. Use `docker-compose.yml`
3. Set proper environment variables

---

## Summary

**Current Issue:** Port 8761 is already in use by Docker's discovery-server

**Quick Fix:**
```bash
docker-compose stop discovery-server
```

**Then run Discovery Server from IntelliJ.**

**Better Solution:** Use application profiles (local vs docker) to avoid manual configuration changes.
