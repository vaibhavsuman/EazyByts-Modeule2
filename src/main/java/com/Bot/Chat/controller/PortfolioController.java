package com.Bot.Chat.controller;

import com.Bot.Chat.model.Portfolio;
import com.Bot.Chat.model.PortfolioStock;
import com.Bot.Chat.model.Transaction;
import com.Bot.Chat.model.User;
import com.Bot.Chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<?> getPortfolio(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not authenticated"));
            }
            
            String username = authentication.getName();
            Optional<User> userOpt = userService.findByUsername(username);
            
            if (!userOpt.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }
            
            User user = userOpt.get();
            Portfolio portfolio = user.getPortfolio();
            
            // Update portfolio calculations with current data
            portfolio.updateCalculatedFields();
            
            Map<String, Object> response = new HashMap<>();
            response.put("portfolioId", portfolio.getId());
            response.put("totalValue", portfolio.getTotalValue());
            response.put("cashBalance", portfolio.getCashBalance());
            response.put("investedValue", portfolio.getInvestedValue());
            response.put("totalReturn", portfolio.getTotalReturn());
            response.put("totalReturnPercent", portfolio.getTotalReturnPercent());
            response.put("dayChange", portfolio.getDayChange());
            response.put("dayChangePercent", portfolio.getDayChangePercent());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/holdings")
    public ResponseEntity<?> getHoldings(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not authenticated"));
            }
            
            String username = authentication.getName();
            Optional<User> userOpt = userService.findByUsername(username);
            
            if (!userOpt.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }
            
            User user = userOpt.get();
            Portfolio portfolio = user.getPortfolio();
            List<PortfolioStock> holdings = portfolio.getPortfolioStocks();
            
            return ResponseEntity.ok(holdings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not authenticated"));
            }
            
            String username = authentication.getName();
            Optional<User> userOpt = userService.findByUsername(username);
            
            if (!userOpt.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }
            
            User user = userOpt.get();
            Portfolio portfolio = user.getPortfolio();
            List<Transaction> transactions = portfolio.getTransactions();
            
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}