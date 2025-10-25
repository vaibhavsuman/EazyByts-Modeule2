package com.Bot.Chat.service;

import com.Bot.Chat.model.*;
import com.Bot.Chat.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class TradingService {
    
    @Autowired
    private PortfolioRepository portfolioRepository;
    
    @Autowired
    private PortfolioStockRepository portfolioStockRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private StockRepository stockRepository;
    
    private final BigDecimal COMMISSION_RATE = new BigDecimal("0.001"); // 0.1%
    private final BigDecimal MIN_ORDER_AMOUNT = new BigDecimal("10.00");
    
    public Transaction buyStock(Long portfolioId, String stockSymbol, Integer quantity) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
            .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        
        Stock stock = stockRepository.findBySymbol(stockSymbol)
            .orElseThrow(() -> new RuntimeException("Stock not found"));
        
        BigDecimal totalCost = stock.getCurrentPrice().multiply(BigDecimal.valueOf(quantity));
        BigDecimal commission = totalCost.multiply(COMMISSION_RATE);
        BigDecimal netAmount = totalCost.add(commission);
        
        // Check if user has enough cash
        if (portfolio.getCashBalance().compareTo(netAmount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        
        // Check minimum order amount
        if (totalCost.compareTo(MIN_ORDER_AMOUNT) < 0) {
            throw new RuntimeException("Order amount below minimum");
        }
        
        // Create transaction
        Transaction transaction = new Transaction(portfolio, stock, Transaction.TransactionType.BUY, 
                                                quantity, stock.getCurrentPrice());
        transaction.setCommission(commission);
        transaction.setNetAmount(netAmount);
        transaction = transactionRepository.save(transaction);
        
        // Update portfolio
        portfolio.setCashBalance(portfolio.getCashBalance().subtract(netAmount));
        
        // Update or create portfolio stock
        Optional<PortfolioStock> existingHolding = portfolioStockRepository
            .findByPortfolioAndStock(portfolio, stock);
        
        if (existingHolding.isPresent()) {
            PortfolioStock holding = existingHolding.get();
            int newQuantity = holding.getQuantity() + quantity;
            BigDecimal newTotalCost = holding.getTotalCost().add(totalCost);
            BigDecimal newAveragePrice = newTotalCost.divide(BigDecimal.valueOf(newQuantity), 4, BigDecimal.ROUND_HALF_UP);
            
            holding.setQuantity(newQuantity);
            holding.setAveragePrice(newAveragePrice);
            holding.setTotalCost(newTotalCost);
            holding.updateCalculatedFields();
            
            portfolioStockRepository.save(holding);
        } else {
            PortfolioStock newHolding = new PortfolioStock(portfolio, stock, quantity, stock.getCurrentPrice());
            portfolioStockRepository.save(newHolding);
        }
        
        // Update portfolio calculations
        portfolio.updateCalculatedFields();
        portfolioRepository.save(portfolio);
        
        return transaction;
    }
    
    public Transaction sellStock(Long portfolioId, String stockSymbol, Integer quantity) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
            .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        
        Stock stock = stockRepository.findBySymbol(stockSymbol)
            .orElseThrow(() -> new RuntimeException("Stock not found"));
        
        PortfolioStock holding = portfolioStockRepository.findByPortfolioAndStock(portfolio, stock)
            .orElseThrow(() -> new RuntimeException("Stock not held in portfolio"));
        
        if (holding.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient shares to sell");
        }
        
        BigDecimal totalValue = stock.getCurrentPrice().multiply(BigDecimal.valueOf(quantity));
        BigDecimal commission = totalValue.multiply(COMMISSION_RATE);
        BigDecimal netAmount = totalValue.subtract(commission);
        
        // Create transaction
        Transaction transaction = new Transaction(portfolio, stock, Transaction.TransactionType.SELL, 
                                                quantity, stock.getCurrentPrice());
        transaction.setCommission(commission);
        transaction.setNetAmount(netAmount);
        transaction = transactionRepository.save(transaction);
        
        // Update portfolio
        portfolio.setCashBalance(portfolio.getCashBalance().add(netAmount));
        
        // Update portfolio stock
        int newQuantity = holding.getQuantity() - quantity;
        if (newQuantity == 0) {
            portfolioStockRepository.delete(holding);
        } else {
            BigDecimal newTotalCost = holding.getAveragePrice().multiply(BigDecimal.valueOf(newQuantity));
            holding.setQuantity(newQuantity);
            holding.setTotalCost(newTotalCost);
            holding.updateCalculatedFields();
            
            portfolioStockRepository.save(holding);
        }
        
        // Update portfolio calculations
        portfolio.updateCalculatedFields();
        portfolioRepository.save(portfolio);
        
        return transaction;
    }
}