#!/bin/bash

echo "🔧 Rebuilding Notification Service..."
echo "======================================"

# Stop the notification service
echo "⏸️  Stopping notification service..."
docker-compose stop notification-service

# Remove the container
echo "🗑️  Removing old container..."
docker-compose rm -f notification-service

# Rebuild and start
echo "🔨 Rebuilding and starting..."
docker-compose up -d --build notification-service

# Wait a moment
sleep 5

# Check status
echo ""
echo "📊 Service Status:"
docker-compose ps notification-service

echo ""
echo "📝 Recent Logs:"
docker-compose logs --tail=20 notification-service

echo ""
echo "✅ Notification service has been rebuilt!"
echo ""
echo "💡 To view live logs: docker-compose logs -f notification-service"
echo "💡 To check health: curl http://localhost:8086/actuator/health"
echo ""
