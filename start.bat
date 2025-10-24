@echo off
REM Stock Trading Simulation System Startup Script for Windows

echo 🚀 Starting Stock Trading Simulation System...
echo ==============================================

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed. Please install Java 17 or higher.
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven is not installed. Please install Maven 3.6 or higher.
    pause
    exit /b 1
)

echo ✅ Java and Maven are available
echo.

REM Clean and build the project
echo 🔨 Building the project...
mvn clean install -DskipTests

if %errorlevel% neq 0 (
    echo ❌ Build failed. Please check the error messages above.
    pause
    exit /b 1
)

echo ✅ Build successful!
echo.

REM Start the application
echo 🎯 Starting the application...
echo 📍 Web Interface: http://localhost:8080
echo 📍 H2 Database Console: http://localhost:8080/h2-console
echo 📍 API Base URL: http://localhost:8080/api
echo.
echo 🔐 Demo Accounts:
echo    Username: demo1 ^| Password: password
echo    Username: demo2 ^| Password: password
echo    Username: admin ^| Password: admin123
echo.
echo ⏹️  Press Ctrl+C to stop the application
echo.

mvn spring-boot:run

pause
