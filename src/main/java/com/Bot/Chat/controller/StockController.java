package com.Bot.Chat.controller;

import com.Bot.Chat.model.Stock;
import com.Bot.Chat.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "*")
public class StockController {
    
    @Autowired
    private StockService stockService;
    
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = stockService.getActiveStocks();
        return ResponseEntity.ok(stocks);
    }
    
    @GetMapping("/{symbol}")
    public ResponseEntity<?> getStockBySymbol(@PathVariable String symbol) {
        Optional<Stock> stock = stockService.findBySymbol(symbol);
        if (stock.isPresent()) {
            return ResponseEntity.ok(stock.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<?> searchStocks(@RequestParam String query) {
        try {
            // Try to find by symbol first
            Optional<Stock> stockBySymbol = stockService.findBySymbol(query.toUpperCase());
            if (stockBySymbol.isPresent()) {
                return ResponseEntity.ok(List.of(stockBySymbol.get()));
            }
            
            // If not found by symbol, search by name
            List<Stock> stocksByName = stockService.findByNameContainingIgnoreCase(query);
            return ResponseEntity.ok(stocksByName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{symbol}/price")
    public ResponseEntity<?> updateStockPrice(@PathVariable String symbol, @RequestBody Map<String, Object> request) {
        try {
            BigDecimal newPrice = new BigDecimal(request.get("price").toString());
            Stock updatedStock = stockService.updateStockPrice(symbol, newPrice);
            
            if (updatedStock != null) {
                return ResponseEntity.ok(updatedStock);
            }
            
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/initialize")
    public ResponseEntity<?> initializeSampleStocks() {
        try {
            stockService.initializeSampleStocks();
            return ResponseEntity.ok(Map.of("message", "Sample stocks initialized successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}