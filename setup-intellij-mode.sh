#!/bin/bash

echo "🔧 Stopping Docker Services for IntelliJ Development..."
echo "======================================================="

# Stop application services but keep infrastructure
docker-compose stop discovery-server user-service account-service transaction-service

echo ""
echo "✅ Stopped Docker application services"
echo ""
echo "📊 Infrastructure Status:"
docker-compose ps

echo ""
echo "🎯 You can now run these from IntelliJ:"
echo "   1. Discovery Server (Port 8761)"
echo "   2. User Service (Port 8082)"
echo "   3. Account Service (Port 8083)"
echo "   4. Transaction Service (Port 8084)"
echo ""
echo "💡 Remember to use localhost URLs:"
echo "   - User DB: jdbc:postgresql://localhost:5434/user_db"
echo "   - Account DB: jdbc:postgresql://localhost:5433/account_db"
echo "   - Transaction DB: jdbc:postgresql://localhost:5435/transaction_db"
echo "   - Kafka: localhost:9092"
echo "   - Eureka: http://localhost:8761/eureka"
echo ""
echo "📝 See INTELLIJ_DEVELOPMENT_GUIDE.md for detailed instructions"
echo ""
