# Wallet Microservices Platform

A comprehensive microservices-based digital wallet system built with Spring Boot, featuring service discovery, event-driven architecture with Kafka, and containerized deployment.

## 🏗️ Architecture

### Services

1. **Discovery Server** (Port: 8761)
   - Eureka Server for service discovery
   - Service registry and health monitoring

2. **User Service** (Port: 8082)
   - User management and authentication
   - PostgreSQL database: `user_db`

3. **Account Service** (Port: 8083)
   - Account management and balance tracking
   - PostgreSQL database: `account_db`

4. **Transaction Service** (Port: 8084)
   - Transaction processing and history
   - Kafka event producer
   - PostgreSQL database: `transaction_db`

5. **Notification Service** (Port: 8086)
   - Event-driven notification handler
   - Kafka event consumer

### Infrastructure

- **PostgreSQL**: Three separate databases for data isolation
- **Apache Kafka**: Event streaming platform
- **Zookeeper**: Kafka coordination service
- **Eureka**: Service discovery and registration

## 🚀 Quick Start

### Prerequisites

- Docker Desktop installed and running
- Docker Compose v2.0+
- Git

### Starting the Platform

1. **Clone and Navigate to Project**
   ```bash
   cd /Users/ksaravanan/Downloads/Microservice_poc
   ```

2. **Make Scripts Executable**
   ```bash
   chmod +x start-all.sh stop-all.sh
   ```

3. **Start All Services**
   ```bash
   ./start-all.sh
   ```

4. **Verify Services are Running**
   ```bash
   docker-compose ps
   ```

5. **Access Eureka Dashboard**
   - Open browser: http://localhost:8761
   - Check all services are registered

### Stopping the Platform

```bash
./stop-all.sh
```

### Removing All Data (Clean Start)

```bash
docker-compose down -v
```

## 📊 Service Endpoints

### Discovery Server
- Dashboard: http://localhost:8761

### User Service
- Base URL: http://localhost:8082
- Health: http://localhost:8082/actuator/health
- API: `/api/users`

### Account Service
- Base URL: http://localhost:8083
- Health: http://localhost:8083/actuator/health
- API: `/api/accounts`

### Transaction Service
- Base URL: http://localhost:8084
- Health: http://localhost:8084/actuator/health
- API: `/api/transactions`

### Notification Service
- Base URL: http://localhost:8086
- Health: http://localhost:8086/actuator/health

## 🗄️ Database Access

### User Database
```bash
docker exec -it user-db psql -U postgres -d user_db
```

### Account Database
```bash
docker exec -it account-db psql -U postgres -d account_db
```

### Transaction Database
```bash
docker exec -it transaction-db psql -U postgres -d transaction_db
```

## 📨 Kafka Management

### Access Kafka Container
```bash
docker exec -it kafka bash
```

### List Topics
```bash
kafka-topics --bootstrap-server localhost:9092 --list
```

### Monitor Messages
```bash
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic transaction-events --from-beginning
```

## 🔍 Monitoring & Logs

### View All Service Logs
```bash
docker-compose logs -f
```

### View Specific Service Logs
```bash
docker-compose logs -f user-service
docker-compose logs -f transaction-service
docker-compose logs -f notification-service
```

### Check Service Health
```bash
curl http://localhost:8082/actuator/health  # User Service
curl http://localhost:8083/actuator/health  # Account Service
curl http://localhost:8084/actuator/health  # Transaction Service
```

## 🧪 Testing the Platform

### 1. Create a User
```bash
curl -X POST http://localhost:8082/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### 2. Create an Account
```bash
curl -X POST http://localhost:8083/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "accountType": "SAVINGS",
    "balance": 1000.00
  }'
```

### 3. Create a Transaction
```bash
curl -X POST http://localhost:8084/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccountId": 1,
    "toAccountId": 2,
    "amount": 100.00,
    "type": "TRANSFER"
  }'
```

### 4. Verify Notification
```bash
docker-compose logs -f notification-service
```

## 🛠️ Development

### Building Individual Services

```bash
# Navigate to service directory
cd wallet-user-service

# Build with Maven
./mvnw clean package

# Run locally
java -jar target/*.jar
```

### Rebuild and Restart Single Service

```bash
docker-compose up -d --build user-service
```

## 📋 Project Structure

```
Microservice_poc/
├── docker-compose.yml          # Main orchestration file
├── start-all.sh                # Startup script
├── stop-all.sh                 # Shutdown script
├── KAFKA_INTEGRATION_GUIDE.md  # Kafka documentation
├── wallet-discovery-server/    # Eureka server
├── wallet-user-service/        # User management
├── wallet-account-service/     # Account management
├── wallet-transaction-service/ # Transaction processing
└── wallet-notification-service/# Event notifications
```

## 🔧 Configuration

### Database Credentials
- **Username**: postgres
- **Password**: root

### Service Ports
- Discovery Server: 8761
- User Service: 8082
- Account Service: 8083
- Transaction Service: 8084
- Notification Service: 8086
- PostgreSQL (User): 5432
- PostgreSQL (Account): 5433
- PostgreSQL (Transaction): 5434
- Kafka: 9092
- Zookeeper: 2181

## 🐛 Troubleshooting

### Services Not Starting

1. **Check Docker is Running**
   ```bash
   docker info
   ```

2. **Check Ports are Available**
   ```bash
   lsof -i :8761  # Check if port is already in use
   ```

3. **View Service Logs**
   ```bash
   docker-compose logs [service-name]
   ```

### Database Connection Issues

1. **Verify Database is Healthy**
   ```bash
   docker-compose ps
   ```

2. **Check Database Logs**
   ```bash
   docker-compose logs user-db
   ```

### Kafka Issues

1. **Ensure Zookeeper is Running**
   ```bash
   docker-compose ps zookeeper
   ```

2. **Check Kafka Broker Status**
   ```bash
   docker-compose logs kafka
   ```

### Clean Restart

```bash
# Stop all services
docker-compose down

# Remove all volumes (warning: deletes all data)
docker-compose down -v

# Remove all containers and images
docker-compose down --rmi all -v

# Start fresh
./start-all.sh
```

## 📚 Additional Documentation

- [Kafka Integration Guide](./KAFKA_INTEGRATION_GUIDE.md)
- [Fixes Applied](./wallet-account-service/FIXES_APPLIED.md)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📝 License

This project is for educational purposes.

## 👥 Support

For issues or questions:
- Check logs: `docker-compose logs -f`
- Review health endpoints
- Consult service-specific HELP.md files
