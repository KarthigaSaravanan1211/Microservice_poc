# Postman Testing Guide - Digital Wallet Microservices

## Setup

1. **Open Postman**
2. **Import this collection** or create requests manually
3. **Base URLs:**
   - User Service: `http://localhost:8082`
   - Account Service: `http://localhost:8083`
   - Transaction Service: `http://localhost:8084`

---

## 📊 GET Requests - View Existing Data

### 1. Get All Users ✅
```
Method: GET
URL: http://localhost:8082/users
Headers: (none required)
```

**Expected Response:**
```json
[
  {
    "userId": 1,
    "name": "Saravanan",
    "email": "saravanan@gmail.com"
  }
]
```

### 2. Get User by ID
```
Method: GET
URL: http://localhost:8082/users/1
Headers: (none required)
```

### 3. Get All Accounts ✅ NEW
```
Method: GET
URL: http://localhost:8083/accounts
Headers: (none required)
```

**Expected Response:**
```json
[
  {
    "accountId": 1,
    "userId": 1,
    "balance": 5000.00
  }
]
```

### 4. Get Account by User ID
```
Method: GET
URL: http://localhost:8083/accounts/1
Headers: (none required)
```

### 5. Get Account by Account ID ✅ NEW
```
Method: GET
URL: http://localhost:8083/accounts/id/1
Headers: (none required)
```

### 6. Get All Transactions ✅ NEW
```
Method: GET
URL: http://localhost:8084/transactions
Headers: (none required)
```

**Expected Response:**
```json
[
  {
    "transaction_id": 1,
    "from_user_id": 1,
    "to_user_id": 2,
    "amount": 100.00,
    "status": "SUCCESS",
    "created_at": "2026-03-02T10:30:00"
  }
]
```

### 7. Get User Transactions
```
Method: GET
URL: http://localhost:8084/transactions/user/1
Headers: (none required)
```

---

## ➕ POST Requests - Create Data

### 8. Create New User
```
Method: POST
URL: http://localhost:8082/users
Headers: 
  Content-Type: application/json
Body (raw JSON):
{
  "name": "John Doe",
  "email": "john.doe@example.com"
}
```

**Expected Response:**
```json
{
  "userId": 2,
  "name": "John Doe",
  "email": "john.doe@example.com"
}
```

### 9. Create Account for User
```
Method: POST
URL: http://localhost:8083/accounts/create/1
Headers: (none required)
```

**Expected Response:**
```json
{
  "message": "Account created",
  "userId": 1
}
```

### 10. Credit Money to Account
```
Method: POST
URL: http://localhost:8083/accounts/credit/1?amount=1000
Headers: (none required)
```

**Expected Response:**
```json
{
  "message": "Amount credited successfully",
  "userId": 1,
  "newBalance": 6000.00
}
```

### 11. Debit Money from Account
```
Method: POST
URL: http://localhost:8083/accounts/debit/1?amount=500
Headers: (none required)
```

**Expected Response:**
```json
{
  "message": "Amount debited successfully",
  "userId": 1,
  "newBalance": 5500.00
}
```

### 12. Transfer Money Between Users
```
Method: POST
URL: http://localhost:8084/transactions/transfer?fromUserId=1&toUserId=2&amount=250
Headers: (none required)
```

**Expected Response:**
```json
{
  "transaction_id": 2,
  "from_user_id": 1,
  "to_user_id": 2,
  "amount": 250.00,
  "status": "SUCCESS"
}
```

---

## 🔄 PUT Requests - Update Data

### 13. Update User
```
Method: PUT
URL: http://localhost:8082/users/1
Headers: 
  Content-Type: application/json
Body (raw JSON):
{
  "name": "Saravanan Updated",
  "email": "saravanan.updated@gmail.com"
}
```

---

## ❌ DELETE Requests

### 14. Delete User
```
Method: DELETE
URL: http://localhost:8082/users/1
Headers: (none required)
```

---

## 🧪 Complete Test Workflow

Follow this sequence to test all features:

### Step 1: View Existing Data
```
1. GET http://localhost:8082/users
2. GET http://localhost:8083/accounts
3. GET http://localhost:8084/transactions
```

### Step 2: Create New Users
```
4. POST http://localhost:8082/users
   Body: {"name": "Alice", "email": "alice@example.com"}

5. POST http://localhost:8082/users
   Body: {"name": "Bob", "email": "bob@example.com"}

6. GET http://localhost:8082/users  (verify)
```

### Step 3: Create Accounts
```
7. POST http://localhost:8083/accounts/create/2  (Alice)
8. POST http://localhost:8083/accounts/create/3  (Bob)
9. GET http://localhost:8083/accounts  (verify)
```

### Step 4: Add Money
```
10. POST http://localhost:8083/accounts/credit/2?amount=5000
11. POST http://localhost:8083/accounts/credit/3?amount=3000
12. GET http://localhost:8083/accounts  (verify balances)
```

### Step 5: Make Transaction
```
13. POST http://localhost:8084/transactions/transfer?fromUserId=2&toUserId=3&amount=500
14. GET http://localhost:8084/transactions  (verify)
15. GET http://localhost:8083/accounts  (verify balances updated)
```

### Step 6: View User-Specific Data
```
16. GET http://localhost:8084/transactions/user/2  (Alice's transactions)
17. GET http://localhost:8083/accounts/2  (Alice's account)
```

---

## 📋 Postman Collection JSON

You can import this JSON into Postman:

```json
{
  "info": {
    "name": "Digital Wallet Microservices",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "User Service",
      "item": [
        {
          "name": "Get All Users",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "http://localhost:8082/users",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8082",
              "path": ["users"]
            }
          }
        },
        {
          "name": "Get User by ID",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "http://localhost:8082/users/1",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8082",
              "path": ["users", "1"]
            }
          }
        },
        {
          "name": "Create User",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"name\": \"John Doe\",\n  \"email\": \"john@example.com\"\n}"
            },
            "url": {
              "raw": "http://localhost:8082/users",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8082",
              "path": ["users"]
            }
          }
        }
      ]
    },
    {
      "name": "Account Service",
      "item": [
        {
          "name": "Get All Accounts",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "http://localhost:8083/accounts",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8083",
              "path": ["accounts"]
            }
          }
        },
        {
          "name": "Get Account by User ID",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "http://localhost:8083/accounts/1",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8083",
              "path": ["accounts", "1"]
            }
          }
        },
        {
          "name": "Create Account",
          "request": {
            "method": "POST",
            "header": [],
            "url": {
              "raw": "http://localhost:8083/accounts/create/1",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8083",
              "path": ["accounts", "create", "1"]
            }
          }
        },
        {
          "name": "Credit Account",
          "request": {
            "method": "POST",
            "header": [],
            "url": {
              "raw": "http://localhost:8083/accounts/credit/1?amount=1000",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8083",
              "path": ["accounts", "credit", "1"],
              "query": [
                {
                  "key": "amount",
                  "value": "1000"
                }
              ]
            }
          }
        }
      ]
    },
    {
      "name": "Transaction Service",
      "item": [
        {
          "name": "Get All Transactions",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "http://localhost:8084/transactions",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8084",
              "path": ["transactions"]
            }
          }
        },
        {
          "name": "Get User Transactions",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "http://localhost:8084/transactions/user/1",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8084",
              "path": ["transactions", "user", "1"]
            }
          }
        },
        {
          "name": "Transfer Money",
          "request": {
            "method": "POST",
            "header": [],
            "url": {
              "raw": "http://localhost:8084/transactions/transfer?fromUserId=1&toUserId=2&amount=100",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8084",
              "path": ["transactions", "transfer"],
              "query": [
                {
                  "key": "fromUserId",
                  "value": "1"
                },
                {
                  "key": "toUserId",
                  "value": "2"
                },
                {
                  "key": "amount",
                  "value": "100"
                }
              ]
            }
          }
        }
      ]
    }
  ]
}
```

---

## Common Issues & Solutions

### Issue: "Connection refused" or "Cannot connect to localhost:8082"

**Solution:**
```bash
# Check if services are running
docker-compose ps

# If not running, start them
./start-all.sh
```

### Issue: Empty array returned `[]`

**Solution:**
```bash
# Check if data exists in database
docker exec -it user-db psql -U postgres -d user_db -c "SELECT * FROM users;"

# If no data, create some test data using POST requests
```

### Issue: 404 Not Found

**Solution:**
- Verify the endpoint URL is correct
- Check if services have been rebuilt after code changes
- Run: `./rebuild-services.sh`

### Issue: 500 Internal Server Error

**Solution:**
```bash
# Check service logs
docker-compose logs user-service
docker-compose logs account-service
docker-compose logs transaction-service

# Look for error messages
```

---

## Quick Reference Card

| Service | Port | Base URL |
|---------|------|----------|
| User Service | 8082 | http://localhost:8082 |
| Account Service | 8083 | http://localhost:8083 |
| Transaction Service | 8084 | http://localhost:8084 |
| Eureka Dashboard | 8761 | http://localhost:8761 |

| Database | Port | Connection |
|----------|------|------------|
| user_db | 5434 | localhost:5434 |
| account_db | 5433 | localhost:5433 |
| transaction_db | 5435 | localhost:5435 |

---

**Happy Testing! 🚀**
