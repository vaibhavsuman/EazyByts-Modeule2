package com.Bot.Chat.controller;

import com.Bot.Chat.model.Transaction;
import com.Bot.Chat.model.User;
import com.Bot.Chat.service.TradingService;
import com.Bot.Chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/trading")
@CrossOrigin(origins = "*")
public class TradingController {
    
    @Autowired
    private TradingService tradingService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/buy")
    public ResponseEntity<?> buyStock(Authentication authentication, @RequestBody Map<String, Object> request) {
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
            String stockSymbol = request.get("symbol").toString();
            Integer quantity = Integer.parseInt(request.get("quantity").toString());
            
            Transaction transaction = tradingService.buyStock(user.getPortfolio().getId(), stockSymbol, quantity);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Stock purchased successfully");
            response.put("transactionId", transaction.getId());
            response.put("symbol", stockSymbol);
            response.put("quantity", quantity);
            response.put("price", transaction.getPricePerShare());
            response.put("totalAmount", transaction.getTotalAmount());
            response.put("commission", transaction.getCommission());
            response.put("netAmount", transaction.getNetAmount());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/sell")
    public ResponseEntity<?> sellStock(Authentication authentication, @RequestBody Map<String, Object> request) {
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
            String stockSymbol = request.get("symbol").toString();
            Integer quantity = Integer.parseInt(request.get("quantity").toString());
            
            Transaction transaction = tradingService.sellStock(user.getPortfolio().getId(), stockSymbol, quantity);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Stock sold successfully");
            response.put("transactionId", transaction.getId());
            response.put("symbol", stockSymbol);
            response.put("quantity", quantity);
            response.put("price", transaction.getPricePerShare());
            response.put("totalAmount", transaction.getTotalAmount());
            response.put("commission", transaction.getCommission());
            response.put("netAmount", transaction.getNetAmount());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}