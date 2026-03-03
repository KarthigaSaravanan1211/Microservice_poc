#!/bin/bash

echo "🛑 Stopping Wallet Microservices Platform..."
echo "=============================================="

# Stop all services
docker-compose down

# Optional: Remove volumes (uncomment if you want to clear all data)
# echo "🗑️  Removing volumes..."
# docker-compose down -v

echo ""
echo "✅ All services stopped successfully!"
echo ""
echo "💡 To restart services: ./start-all.sh"
echo "💡 To remove all data (volumes): docker-compose down -v"
echo ""
