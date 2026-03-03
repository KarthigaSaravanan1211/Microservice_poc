#!/bin/bash

echo "🐳 Starting All Services in Docker..."
echo "======================================"

# Stop any IntelliJ processes (manual)
echo "⚠️  Please stop all running services in IntelliJ first!"
echo ""
read -p "Press Enter when ready to start Docker services..."

# Start all services
docker-compose up -d

echo ""
echo "⏳ Waiting for services to start..."
sleep 15

echo ""
echo "📊 Service Status:"
docker-compose ps

echo ""
echo "✅ All services running in Docker"
echo ""
echo "🌐 Access URLs:"
echo "   - Eureka Dashboard: http://localhost:8761"
echo "   - User Service: http://localhost:8082"
echo "   - Account Service: http://localhost:8083"
echo "   - Transaction Service: http://localhost:8084"
echo ""
echo "🧪 Test Commands:"
echo "   curl http://localhost:8082/users"
echo "   curl http://localhost:8083/accounts"
echo "   curl http://localhost:8084/transactions"
echo ""
