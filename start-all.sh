#!/bin/bash

echo "🚀 Starting Wallet Microservices Platform..."
echo "=============================================="

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Error: Docker is not running. Please start Docker Desktop first."
    exit 1
fi

# Check if docker-compose is available
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Error: docker-compose is not installed."
    exit 1
fi

# Stop any existing containers
echo "🧹 Cleaning up existing containers..."
docker-compose down

# Build and start all services
echo "📦 Building and starting all services..."
docker-compose up -d --build

# Wait for services to be healthy
echo "⏳ Waiting for services to be healthy..."
sleep 10

# Check service status
echo ""
echo "📊 Service Status:"
echo "=================="
docker-compose ps

echo ""
echo "✅ All services started successfully!"
echo ""
echo "🌐 Service URLs:"
echo "  - Eureka Discovery Server: http://localhost:8761"
echo "  - User Service:            http://localhost:8082"
echo "  - Account Service:         http://localhost:8083"
echo "  - Transaction Service:     http://localhost:8084"
echo "  - Notification Service:    http://localhost:8086"
echo ""
echo "📊 Database Ports:"
echo "  - User DB (PostgreSQL):        localhost:5432"
echo "  - Account DB (PostgreSQL):     localhost:5433"
echo "  - Transaction DB (PostgreSQL): localhost:5434"
echo ""
echo "📨 Kafka Broker: localhost:9092"
echo ""
echo "💡 To view logs: docker-compose logs -f [service-name]"
echo "💡 To stop all services: ./stop-all.sh or docker-compose down"
echo ""
