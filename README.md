# Stock Trading Simulation System

A comprehensive, full-stack stock trading simulation application built with Spring Boot, providing users with a realistic trading experience without financial risk. This system allows users to practice trading strategies, monitor portfolio performance, and enhance their trading skills.

## 🚀 Features

### Core Trading Features
- **Real-time Stock Trading**: Buy and sell stocks with simulated market prices
- **Portfolio Management**: Track holdings, performance, and portfolio allocation
- **Transaction History**: Complete audit trail of all trading activities
- **Real-time Updates**: WebSocket-based live price updates and portfolio changes

### Advanced Features
- **Algorithmic Trading**: Pre-built trading strategies with technical indicators
  - Moving Average Crossover
  - RSI Oversold/Overbought
  - Bollinger Bands
- **Risk Management**: Position sizing, stop-loss, and diversification analysis
- **Performance Analytics**: Comprehensive portfolio analytics and reporting

### User Experience
- **Responsive Design**: Modern, mobile-friendly interface
- **Real-time Dashboard**: Live portfolio updates and market data
- **Interactive Charts**: Portfolio performance and allocation visualization
- **User Authentication**: Secure login and registration system

## 🏗️ Architecture

### Backend Technologies
- **Spring Boot 3.5.6**: Main application framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database operations and ORM
- **Spring WebSocket**: Real-time communication
- **H2 Database**: In-memory database for development
- **MySQL**: Production database support
- **Maven**: Dependency management

### Frontend Technologies
- **HTML5**: Semantic markup
- **CSS3**: Modern styling with Bootstrap 5
- **JavaScript (ES6+)**: Interactive functionality
- **Chart.js**: Data visualization
- **WebSocket API**: Real-time updates

### Key Components

#### Models
- `User`: User accounts and authentication
- `Portfolio`: User portfolio management
- `Stock`: Stock information and pricing
- `Transaction`: Trading transaction records
- `PortfolioStock`: Portfolio holdings tracking
- `TradingStrategy`: Algorithmic trading strategies
- `StrategyExecution`: Strategy execution records

#### Services
- `TradingService`: Core trading operations
- `PortfolioService`: Portfolio management
- `StockService`: Stock data management
- `AnalyticsService`: Performance analytics
- `AlgorithmicTradingService`: Strategy execution
- `StockDataService`: Real-time data updates

#### Controllers
- `AuthController`: User authentication
- `TradingController`: Trading operations
- `PortfolioController`: Portfolio management
- `StockController`: Stock data access
- `AnalyticsController`: Analytics and reporting
- `AlgorithmicTradingController`: Strategy management

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd springStockTradingApp
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Web Interface: http://localhost:8080
   - H2 Database Console: http://localhost:8080/h2-console
   - API Documentation: Available through REST endpoints

### Demo Accounts
The system comes with pre-configured demo accounts:

- **Username**: `demo1` | **Password**: `password`
- **Username**: `demo2` | **Password**: `password`
- **Username**: `admin` | **Password**: `admin123`

## 📊 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `GET /api/auth/me` - Get current user

### Trading
- `POST /api/trading/buy` - Buy stocks
- `POST /api/trading/sell` - Sell stocks
- `GET /api/trading/transactions` - Get transaction history
- `POST /api/trading/transactions/{id}/cancel` - Cancel transaction

### Portfolio
- `GET /api/portfolio` - Get portfolio overview
- `GET /api/portfolio/stocks` - Get holdings
- `GET /api/portfolio/summary` - Get portfolio summary
- `POST /api/portfolio/update-values` - Update portfolio values

### Stocks
- `GET /api/stocks` - Get all stocks
- `GET /api/stocks/{symbol}` - Get stock by symbol
- `GET /api/stocks/search` - Search stocks
- `GET /api/stocks/top-gainers` - Get top gainers
- `GET /api/stocks/top-losers` - Get top losers

### Analytics
- `GET /api/analytics/portfolio` - Portfolio analytics
- `GET /api/analytics/performance` - Performance report
- `GET /api/analytics/diversification` - Diversification analysis
- `GET /api/analytics/risk` - Risk analysis

### Algorithmic Trading
- `GET /api/algorithmic/strategies` - Get trading strategies
- `POST /api/algorithmic/strategies` - Create strategy
- `GET /api/algorithmic/signals` - Get trading signals
- `POST /api/algorithmic/executions/{id}/execute` - Execute strategy

## 🎯 Key Features Explained

### Real-time Trading
- Simulated stock prices with realistic volatility
- Commission fees and transaction costs
- Order execution with status tracking
- Portfolio updates in real-time

### Portfolio Analytics
- **Performance Metrics**: Total return, percentage gain/loss
- **Risk Analysis**: Volatility, beta calculation
- **Diversification**: Sector allocation and concentration
- **Trading Metrics**: Activity frequency and patterns

### Algorithmic Trading
- **Technical Indicators**: SMA, EMA, RSI, Bollinger Bands
- **Strategy Execution**: Automated buy/sell signals
- **Backtesting**: Historical strategy performance
- **Risk Management**: Position sizing and stop-loss

## 🔧 Configuration

### Application Properties
Key configuration options in `application.properties`:

```properties
# Database
spring.datasource.url=jdbc:h2:mem:tradingdb
spring.h2.console.enabled=true

# Trading Configuration
trading.initial-balance=100000.00
trading.commission-rate=0.001
trading.min-order-amount=10.00

# Stock Data
stock.api.refresh-interval=30000
```

### Security Configuration
- Basic authentication enabled
- CORS configured for frontend access
- Password encoding with BCrypt
- Session-based authentication

## 📈 Sample Data

The application includes:
- **10 Sample Stocks**: Major companies (AAPL, MSFT, GOOGL, etc.)
- **Demo Users**: Pre-configured accounts with sample portfolios
- **Trading Strategies**: Pre-built algorithmic strategies

## 🚀 Deployment

### Development
```bash
mvn spring-boot:run
```

### Production
1. Configure MySQL database
2. Update `application.properties` for production settings
3. Build JAR file: `mvn clean package`
4. Run: `java -jar target/springStockTradingApp-0.0.1-SNAPSHOT.jar`

## 🧪 Testing

### Manual Testing
1. Register a new account or use demo credentials
2. Navigate through different sections (Dashboard, Portfolio, Trading)
3. Execute buy/sell orders
4. Monitor real-time updates

### API Testing
Use tools like Postman or curl to test REST endpoints:

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"demo1","password":"password"}'

# Get portfolio
curl -X GET http://localhost:8080/api/portfolio \
  -H "Authorization: Basic ZGVtbzE6cGFzc3dvcmQ="
```

## 🔮 Future Enhancements

### Planned Features
- **Real Market Data Integration**: Connect to live stock data APIs
- **Advanced Charting**: More sophisticated technical analysis tools
- **Social Trading**: Follow other traders and strategies
- **Mobile App**: Native iOS and Android applications
- **Advanced Analytics**: Machine learning-based insights
- **Paper Trading Competitions**: User competitions and leaderboards

### Technical Improvements
- **Microservices Architecture**: Split into smaller services
- **Event-Driven Architecture**: Implement event sourcing
- **Caching**: Redis for improved performance
- **Message Queues**: Asynchronous processing
- **API Gateway**: Centralized API management

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For questions or support:
- Create an issue in the repository
- Review the code comments

## 🙏 Acknowledgments

- Spring Boot community for excellent framework
- Bootstrap for responsive UI components
- Chart.js for data visualization
- All contributors and testers

---

**Note**: This is a simulation system for educational purposes. No real money is involved in any transactions.
