#!/bin/bash

echo "🔍 Verifying Wallet Microservices Setup..."
echo "=========================================="
echo ""

# Check Docker
echo "✓ Checking Docker..."
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker Desktop."
    exit 1
fi
echo "  Docker is running ✓"

# Check docker-compose
echo "✓ Checking docker-compose..."
if ! command -v docker-compose &> /dev/null; then
    echo "❌ docker-compose is not installed."
    exit 1
fi
echo "  docker-compose is installed ✓"

# Check docker-compose.yml
echo "✓ Checking docker-compose.yml..."
if [ ! -f "docker-compose.yml" ]; then
    echo "❌ docker-compose.yml not found."
    exit 1
fi
docker-compose config > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "  docker-compose.yml is valid ✓"
else
    echo "❌ docker-compose.yml has errors."
    exit 1
fi

# Check Dockerfiles
echo "✓ Checking Dockerfiles..."
services=("wallet-discovery-server" "wallet-user-service" "wallet-account-service" "wallet-transaction-service" "wallet-notification-service")
for service in "${services[@]}"; do
    if [ ! -f "$service/Dockerfile" ]; then
        echo "❌ Dockerfile not found in $service"
        exit 1
    fi
done
echo "  All Dockerfiles present ✓"

# Check pom.xml files
echo "✓ Checking pom.xml files..."
for service in "${services[@]}"; do
    if [ ! -f "$service/pom.xml" ]; then
        echo "❌ pom.xml not found in $service"
        exit 1
    fi
done
echo "  All pom.xml files present ✓"

echo ""
echo "✅ All checks passed! Your setup is ready."
echo ""
echo "🚀 To start the platform:"
echo "   ./start-all.sh"
echo ""
echo "🛑 To stop the platform:"
echo "   ./stop-all.sh"
echo ""
