package com.Bot.Chat.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "portfolios")
public class Portfolio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    
    @Column(name = "total_value", precision = 12, scale = 2)
    private BigDecimal totalValue = BigDecimal.ZERO;
    
    @Column(name = "cash_balance", precision = 12, scale = 2)
    private BigDecimal cashBalance = BigDecimal.ZERO;
    
    @Column(name = "invested_value", precision = 12, scale = 2)
    private BigDecimal investedValue = BigDecimal.ZERO;
    
    @Column(name = "total_return", precision = 12, scale = 2)
    private BigDecimal totalReturn = BigDecimal.ZERO;
    
    @Column(name = "total_return_percent", precision = 5, scale = 2)
    private BigDecimal totalReturnPercent = BigDecimal.ZERO;
    
    @Column(name = "day_change", precision = 12, scale = 2)
    private BigDecimal dayChange = BigDecimal.ZERO;
    
    @Column(name = "day_change_percent", precision = 5, scale = 2)
    private BigDecimal dayChangePercent = BigDecimal.ZERO;
    
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PortfolioStock> portfolioStocks;
    
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
    
    // Constructors
    public Portfolio() {}
    
    public Portfolio(User user) {
        this.user = user;
        this.cashBalance = user.getInitialBalance();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public BigDecimal getTotalValue() {
        return totalValue;
    }
    
    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
    
    public BigDecimal getCashBalance() {
        return cashBalance;
    }
    
    public void setCashBalance(BigDecimal cashBalance) {
        this.cashBalance = cashBalance;
    }
    
    public BigDecimal getInvestedValue() {
        return investedValue;
    }
    
    public void setInvestedValue(BigDecimal investedValue) {
        this.investedValue = investedValue;
    }
    
    public BigDecimal getTotalReturn() {
        return totalReturn;
    }
    
    public void setTotalReturn(BigDecimal totalReturn) {
        this.totalReturn = totalReturn;
    }
    
    public BigDecimal getTotalReturnPercent() {
        return totalReturnPercent;
    }
    
    public void setTotalReturnPercent(BigDecimal totalReturnPercent) {
        this.totalReturnPercent = totalReturnPercent;
    }
    
    public BigDecimal getDayChange() {
        return dayChange;
    }
    
    public void setDayChange(BigDecimal dayChange) {
        this.dayChange = dayChange;
    }
    
    public BigDecimal getDayChangePercent() {
        return dayChangePercent;
    }
    
    public void setDayChangePercent(BigDecimal dayChangePercent) {
        this.dayChangePercent = dayChangePercent;
    }
    
    public List<PortfolioStock> getPortfolioStocks() {
        return portfolioStocks;
    }
    
    public void setPortfolioStocks(List<PortfolioStock> portfolioStocks) {
        this.portfolioStocks = portfolioStocks;
    }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }
    
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    
    // Calculation methods
    public void updateCalculatedFields() {
        if (portfolioStocks != null) {
            // Calculate invested value (total cost of all holdings)
            investedValue = portfolioStocks.stream()
                .map(PortfolioStock::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // Calculate current value of all holdings
            BigDecimal currentValue = portfolioStocks.stream()
                .map(holding -> holding.getStock().getCurrentPrice().multiply(BigDecimal.valueOf(holding.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // Calculate total portfolio value (cash + current value of holdings)
            totalValue = cashBalance.add(currentValue);
            
            // Calculate total return (current value - invested value)
            totalReturn = currentValue.subtract(investedValue);
            
            // Calculate total return percentage
            if (investedValue.compareTo(BigDecimal.ZERO) > 0) {
                totalReturnPercent = totalReturn.divide(investedValue, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            } else {
                totalReturnPercent = BigDecimal.ZERO;
            }
        } else {
            investedValue = BigDecimal.ZERO;
            totalValue = cashBalance;
            totalReturn = BigDecimal.ZERO;
            totalReturnPercent = BigDecimal.ZERO;
        }
    }
}