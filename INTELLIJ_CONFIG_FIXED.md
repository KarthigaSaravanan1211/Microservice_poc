# IntelliJ Configuration Fixed

## Problem
When running services from IntelliJ, they were trying to connect to databases using Docker container hostnames (`user-db`, `account-db`, etc.), which are not resolvable outside the Docker network.

**Error:** `java.net.UnknownHostException: user-db`

## Solution Applied
Updated all `application.yml` files to use `localhost` with the correct mapped ports for IntelliJ development mode.

---

## Changes Made

### 1. wallet-user-service/src/main/resources/application.yml
**Changed:**
```yaml
url: jdbc:postgresql://user-db:5432/user_db
```
**To:**
```yaml
url: jdbc:postgresql://localhost:5434/user_db
```

### 2. wallet-account-service/src/main/resources/application.yml
**Changed:**
```yaml
url: jdbc:postgresql://account-db:5432/account_db
kafka:
  bootstrap-servers: kafka:29092
eureka:
  client:
    service-url:
      defaultZone: http://discovery-server:8761/eureka
```
**To:**
```yaml
url: jdbc:postgresql://localhost:5433/account_db
kafka:
  bootstrap-servers: localhost:9092
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

### 3. wallet-transaction-service/src/main/resources/application.yml
**Changed:**
```yaml
url: jdbc:postgresql://localhost:5432/transaction_db
```
**To:**
```yaml
url: jdbc:postgresql://localhost:5435/transaction_db
```

---

## IntelliJ Development Mode - Configuration Reference

| Service | Database URL | Kafka | Eureka | Port |
|---------|-------------|-------|--------|------|
| User Service | `localhost:5434/user_db` | N/A | `localhost:8761` | 8082 |
| Account Service | `localhost:5433/account_db` | `localhost:9092` | `localhost:8761` | 8083 |
| Transaction Service | `localhost:5435/transaction_db` | `localhost:9092` | `localhost:8761` | 8084 |
| Discovery Server | N/A | N/A | N/A | 8761 |

---

## How to Run from IntelliJ

### Step 1: Ensure Infrastructure is Running in Docker
```bash
# Make sure databases and Kafka are running
docker-compose ps

# Should show:
# - user-db (port 5434)
# - account-db (port 5433)
# - transaction-db (port 5435)
# - zookeeper
# - kafka (port 9092)
```

### Step 2: Run Discovery Server
1. Open `wallet-discovery-server/src/main/java/.../WalletDiscoveryServerApplication.java`
2. Right-click → Run
3. Verify it starts on port 8761
4. Check: http://localhost:8761

### Step 3: Run Microservices in Order
1. **User Service** (port 8082)
2. **Account Service** (port 8083)
3. **Transaction Service** (port 8084)

Each should connect to:
- PostgreSQL on localhost (correct port)
- Eureka Discovery on localhost:8761
- Kafka on localhost:9092 (for Account & Transaction services)

### Step 4: Verify Services Registered
- Open: http://localhost:8761
- All services should appear in Eureka dashboard

---

## Testing APIs in Postman

### User Service (localhost:8082)
```http
GET http://localhost:8082/users
GET http://localhost:8082/users/1
POST http://localhost:8082/users
```

### Account Service (localhost:8083)
```http
GET http://localhost:8083/accounts
GET http://localhost:8083/accounts/id/1
GET http://localhost:8083/accounts/user/1
POST http://localhost:8083/accounts
```

### Transaction Service (localhost:8084)
```http
GET http://localhost:8084/transactions
GET http://localhost:8084/transactions/1
POST http://localhost:8084/transactions
```

---

## Switching Between Modes

### Switch to Docker Mode (All services in Docker)
If you want to run everything in Docker again, you need to:

1. **Revert application.yml files** to use Docker hostnames:
   - `user-db:5432` instead of `localhost:5434`
   - `account-db:5432` instead of `localhost:5433`
   - `transaction-db:5432` instead of `localhost:5435`
   - `kafka:29092` instead of `localhost:9092`
   - `discovery-server:8761` instead of `localhost:8761`

2. **Rebuild Docker images:**
   ```bash
   docker-compose down
   docker-compose up -d --build
   ```

### Switch to IntelliJ Mode (Current Configuration)
Already configured! Just ensure:
1. Infrastructure running: `docker-compose up -d user-db account-db transaction-db zookeeper kafka`
2. Run services from IntelliJ

---

## Troubleshooting

### Issue: "Port already in use"
**Solution:** Stop the service in Docker before running from IntelliJ
```bash
docker-compose stop user-service
docker-compose stop account-service
docker-compose stop transaction-service
docker-compose stop discovery-server
```

### Issue: "UnknownHostException: user-db"
**Solution:** This guide has already fixed this! The configuration now uses `localhost` instead of Docker hostnames.

### Issue: "Connection refused to localhost:5434"
**Solution:** Ensure the database container is running:
```bash
docker-compose ps user-db
# If not running:
docker-compose up -d user-db
```

### Issue: Flyway migration fails
**Solution:** Database might not be initialized. Check logs:
```bash
docker-compose logs user-db
```

---

## Important Notes

⚠️ **Current Configuration:** All services are now configured for **IntelliJ development mode** with `localhost` URLs.

⚠️ **Docker Mode:** If you want to run everything in Docker, you need to revert the database/kafka/eureka URLs to use container names.

✅ **Recommendation:** Keep this configuration for development/debugging. Use Docker mode only for production-like testing.

---

## Database Port Mappings (Docker to Localhost)

| Container | Internal Port | External Port (localhost) |
|-----------|--------------|---------------------------|
| user-db | 5432 | 5434 |
| account-db | 5432 | 5433 |
| transaction-db | 5432 | 5435 |
| kafka | 9092 (internal), 29092 (inter-container) | 9092 |
| zookeeper | 2181 | 2181 |
| discovery-server | 8761 | 8761 (when running from IntelliJ) |

---

## Summary

✅ Fixed `UnknownHostException: user-db` error  
✅ All services now use `localhost` for database connections  
✅ Kafka uses `localhost:9092` instead of `kafka:29092`  
✅ Eureka uses `localhost:8761` instead of `discovery-server:8761`  
✅ Correct port mappings (5434, 5433, 5435) for databases  
✅ Ready for IntelliJ development and debugging  

**Next Step:** Run the User Service again from IntelliJ - it should now connect successfully!
