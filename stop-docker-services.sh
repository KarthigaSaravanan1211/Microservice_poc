#!/bin/bash

# Stop All Application Services for IntelliJ Mode
# This script stops all microservices in Docker while keeping infrastructure running
# Use this before running services from IntelliJ to avoid port conflicts

echo "🛑 Stopping Docker Application Services..."
echo "================================================"

# Stop all application services
docker-compose stop discovery-server user-service account-service transaction-service notification-service

echo ""
echo "✅ Application services stopped!"
echo ""
echo "📊 Infrastructure Status:"
echo "------------------------------------------------"

# Show running containers
docker-compose ps | grep -E 'user-db|account-db|transaction-db|kafka|zookeeper'

echo ""
echo "================================================"
echo "🚀 Ready for IntelliJ Development Mode!"
echo ""
echo "Infrastructure still running:"
echo "  ✓ user-db         (port 5434)"
echo "  ✓ account-db      (port 5433)"
echo "  ✓ transaction-db  (port 5435)"
echo "  ✓ kafka           (port 9092)"
echo "  ✓ zookeeper       (port 2181)"
echo ""
echo "Ports now free for IntelliJ:"
echo "  ✓ 8761 - Discovery Server"
echo "  ✓ 8082 - User Service"
echo "  ✓ 8083 - Account Service"
echo "  ✓ 8084 - Transaction Service"
echo "  ✓ 8086 - Notification Service"
echo ""
echo "Next: Run services from IntelliJ"
echo "================================================"
