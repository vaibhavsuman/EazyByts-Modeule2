#!/bin/bash

# Stock Trading Simulation System Startup Script

echo "🚀 Starting Stock Trading Simulation System..."
echo "=============================================="

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java version: $(java -version 2>&1 | head -n 1)"
echo "✅ Maven version: $(mvn -version | head -n 1)"
echo ""

# Clean and build the project
echo "🔨 Building the project..."
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed. Please check the error messages above."
    exit 1
fi

echo "✅ Build successful!"
echo ""

# Start the application
echo "🎯 Starting the application..."
echo "📍 Web Interface: http://localhost:8080"
echo "📍 H2 Database Console: http://localhost:8080/h2-console"
echo "📍 API Base URL: http://localhost:8080/api"
echo ""
echo "🔐 Demo Accounts:"
echo "   Username: demo1 | Password: password"
echo "   Username: demo2 | Password: password"
echo "   Username: admin | Password: admin123"
echo ""
echo "⏹️  Press Ctrl+C to stop the application"
echo ""

mvn spring-boot:run
