# 🚀 Quick Start Guide - IntelliJ Development Mode

## ✅ FIXED: Now connects to your REAL database with OLD data!

All services now use: **localhost:5432** (same as pgAdmin)

---

## ✅ Infrastructure Status
```
✓ PostgreSQL (localhost:5432) → YOUR OLD DATA
✓ kafka      (localhost:9092)  → Docker
✓ zookeeper  (localhost:2181)  → Docker
```

---

## 🎯 Run Services in This Order

### 1️⃣ Discovery Server (First!)
**File:** `wallet-discovery-server/.../WalletDiscoveryServerApplication.java`
**Port:** 8761
**URL:** http://localhost:8761

### 2️⃣ User Service
**File:** `wallet-user-service/.../WalletUserServiceApplication.java`
**Port:** 8082
**Test:** `curl http://localhost:8082/users`

### 3️⃣ Account Service
**File:** `wallet-account-service/.../WalletAccountServiceApplication.java`
**Port:** 8083
**Test:** `curl http://localhost:8083/accounts`

### 4️⃣ Transaction Service
**File:** `wallet-transaction-service/.../WalletTransactionServiceApplication.java`
**Port:** 8084
**Test:** `curl http://localhost:8084/transactions`

### 5️⃣ Notification Service (Optional)
**File:** `wallet-notification-service/.../WalletNotificationServerApplication.java`
**Port:** 8086
**Note:** Kafka consumer service - listens for transaction events

---

## 🔥 What Was Fixed

**First Error:**
```
java.net.UnknownHostException: user-db
```
Services were using Docker hostnames instead of `localhost`.

**Second Error:**
```
Port 8086 was already in use
```
Notification service Docker container was using port 8086.

**Fixed by:**
1. Updated all `application.yml` files to use `localhost`
2. Stopped notification-service Docker container
3. Services now use correct ports: 5434, 5433, 5435, 9092

---

## ✨ You're Ready!

Just run the **Notification Service** from IntelliJ again - it should start successfully now!

Port 8086 is now free (Docker container stopped).

**See READY_FOR_INTELLIJ.md for complete details.**
