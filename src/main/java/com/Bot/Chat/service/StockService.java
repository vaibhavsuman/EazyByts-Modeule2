package com.Bot.Chat.service;

import com.Bot.Chat.model.Stock;
import com.Bot.Chat.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {
    
    @Autowired
    private StockRepository stockRepository;
    
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
    
    public List<Stock> getActiveStocks() {
        return stockRepository.findByIsActiveTrue();
    }
    
    public Optional<Stock> findBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }
    
    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }
    
    public Stock updateStockPrice(String symbol, BigDecimal newPrice) {
        Optional<Stock> stockOpt = findBySymbol(symbol);
        if (stockOpt.isPresent()) {
            Stock stock = stockOpt.get();
            stock.setPreviousClose(stock.getCurrentPrice());
            stock.setCurrentPrice(newPrice);
            
            if (stock.getPreviousClose() != null) {
                BigDecimal change = newPrice.subtract(stock.getPreviousClose());
                stock.setDayChange(change);
                
                if (stock.getPreviousClose().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal changePercent = change.divide(stock.getPreviousClose(), 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
                    stock.setDayChangePercent(changePercent);
                }
            }
            
            stock.setLastUpdated(java.time.LocalDateTime.now());
            return stockRepository.save(stock);
        }
        return null;
    }
    
    @PostConstruct
    public void initializeSampleStocks() {
        if (stockRepository.count() == 0) {
            // Create some sample stocks
            Stock aapl = new Stock("AAPL", "Apple Inc.", new BigDecimal("150.00"));
            Stock googl = new Stock("GOOGL", "Alphabet Inc.", new BigDecimal("2800.00"));
            Stock msft = new Stock("MSFT", "Microsoft Corporation", new BigDecimal("300.00"));
            Stock tsla = new Stock("TSLA", "Tesla Inc.", new BigDecimal("800.00"));
            Stock amzn = new Stock("AMZN", "Amazon.com Inc.", new BigDecimal("3200.00"));
            
            stockRepository.saveAll(List.of(aapl, googl, msft, tsla, amzn));
        }
    }
    
    public List<Stock> findByNameContainingIgnoreCase(String name) {
        return stockRepository.findByNameContainingIgnoreCase(name);
    }
}