#!/bin/bash

echo "🔧 Rebuilding All Microservices..."
echo "===================================="

# Stop services
echo "⏸️  Stopping services..."
docker-compose stop user-service account-service transaction-service

# Remove containers
echo "🗑️  Removing old containers..."
docker-compose rm -f user-service account-service transaction-service

# Rebuild and start
echo "🔨 Rebuilding and starting services..."
docker-compose up -d --build user-service account-service transaction-service

# Wait for services
sleep 15

# Check status
echo ""
echo "📊 Service Status:"
docker-compose ps

echo ""
echo "✅ Services have been rebuilt!"
echo ""
echo "💡 Test endpoints:"
echo "   curl http://localhost:8082/users"
echo "   curl http://localhost:8083/accounts"
echo "   curl http://localhost:8084/transactions"
echo ""
