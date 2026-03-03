# ✅ FIXED: Port 8761 Already in Use

## What Was the Problem?

You tried to run Discovery Server from IntelliJ while Docker container was also using port 8761.

**Error:**
```
Port 8761 was already in use.
```

## What I Did

✅ **Stopped Docker Discovery Server**
```bash
docker-compose stop discovery-server
```

Now port 8761 is free for IntelliJ!

---

## Current Status

✅ **Discovery Server running from IntelliJ** (PID 19037, Port 8761)  
✅ **Databases running in Docker** (ports 5434, 5433, 5435)  
✅ **Kafka running in Docker** (port 9092)

---

## Next Steps

### Option A: Run All Services from IntelliJ

1. **Keep infrastructure in Docker:**
   - Databases (user-db, account-db, transaction-db)
   - Kafka + Zookeeper
   - Notification Service

2. **Run from IntelliJ:**
   - Discovery Server ✅ (already running)
   - User Service
   - Account Service
   - Transaction Service

3. **Important:** Make sure service configurations use `localhost` instead of Docker hostnames:
   ```yaml
   # User Service
   spring.datasource.url: jdbc:postgresql://localhost:5434/user_db
   
   # Account Service
   spring.datasource.url: jdbc:postgresql://localhost:5433/account_db
   
   # Transaction Service
   spring.datasource.url: jdbc:postgresql://localhost:5435/transaction_db
   spring.kafka.bootstrap-servers: localhost:9092
   ```

### Option B: Run Everything in Docker

1. **Stop IntelliJ Services**
2. **Run the script:**
   ```bash
   ./setup-docker-mode.sh
   ```

---

## Quick Switch Scripts

### Switch to IntelliJ Development Mode
```bash
./setup-intellij-mode.sh
```
This stops Docker services and keeps only infrastructure running.

### Switch to Docker Mode
```bash
./setup-docker-mode.sh
```
This starts all services in Docker.

---

## Testing Your Setup

### If Running from IntelliJ:

```bash
# Check Eureka Dashboard
open http://localhost:8761

# Test User Service (run from IntelliJ first)
curl http://localhost:8082/users

# Test Account Service (run from IntelliJ first)
curl http://localhost:8083/accounts

# Test Transaction Service (run from IntelliJ first)
curl http://localhost:8084/transactions
```

### If Running from Docker:

```bash
# Start all services
docker-compose up -d

# Check status
docker-compose ps

# Test endpoints
curl http://localhost:8082/users
curl http://localhost:8083/accounts
curl http://localhost:8084/transactions
```

---

## Port Mapping Quick Reference

| Service | IntelliJ Port | Docker Port | Database Connection |
|---------|---------------|-------------|---------------------|
| Discovery Server | 8761 | 8761 | - |
| User Service | 8082 | 8082 | localhost:5434 |
| Account Service | 8083 | 8083 | localhost:5433 |
| Transaction Service | 8084 | 8084 | localhost:5435 |
| Kafka | localhost:9092 | kafka:29092 | - |

---

## Common Issues

### "Port already in use" error
**Fix:**
```bash
# Find what's using the port
lsof -i :8761

# If it's Docker:
docker-compose stop discovery-server

# If it's IntelliJ:
Stop the service in IntelliJ
```

### Services can't connect to database
**Check configuration:**
- IntelliJ mode: Use `localhost:5434`, `localhost:5433`, `localhost:5435`
- Docker mode: Use `user-db:5432`, `account-db:5432`, `transaction-db:5432`

### Services not appearing in Eureka
1. Wait 30 seconds after starting Discovery Server
2. Check Eureka URL is correct:
   - IntelliJ: `http://localhost:8761/eureka`
   - Docker: `http://discovery-server:8761/eureka`

---

## Documentation

- **INTELLIJ_DEVELOPMENT_GUIDE.md** - Complete IntelliJ setup guide
- **QUICK_START.md** - Quick reference for data retrieval
- **DATA_RETRIEVAL_FIX.md** - API endpoint details
- **POSTMAN_TESTING_GUIDE.md** - Postman collection and testing

---

## Summary

✅ **Problem Solved:** Docker Discovery Server stopped, IntelliJ can now run on port 8761  
✅ **Scripts Created:** Easy switching between IntelliJ and Docker modes  
✅ **Documentation:** Complete guides for both development approaches

**You're all set to develop! 🚀**
