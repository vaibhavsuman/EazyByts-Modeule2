package com.Bot.Chat.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;

@Entity
@Table(name = "portfolio_stocks")
public class PortfolioStock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    @JsonIgnore
    private Portfolio portfolio;
    
    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;
    
    @Column(nullable = false)
    private Integer quantity = 0;
    
    @Column(name = "average_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal averagePrice = BigDecimal.ZERO;
    
    @Column(name = "current_price", precision = 10, scale = 2)
    private BigDecimal currentPrice = BigDecimal.ZERO;
    
    @Column(name = "total_cost", precision = 12, scale = 2)
    private BigDecimal totalCost = BigDecimal.ZERO;
    
    @Column(name = "current_value", precision = 12, scale = 2)
    private BigDecimal currentValue = BigDecimal.ZERO;
    
    @Column(name = "unrealized_gain_loss", precision = 12, scale = 2)
    private BigDecimal unrealizedGainLoss = BigDecimal.ZERO;
    
    @Column(name = "unrealized_gain_loss_percent", precision = 5, scale = 2)
    private BigDecimal unrealizedGainLossPercent = BigDecimal.ZERO;
    
    @Column(name = "day_change", precision = 12, scale = 2)
    private BigDecimal dayChange = BigDecimal.ZERO;
    
    @Column(name = "day_change_percent", precision = 5, scale = 2)
    private BigDecimal dayChangePercent = BigDecimal.ZERO;
    
    // Constructors
    public PortfolioStock() {}
    
    public PortfolioStock(Portfolio portfolio, Stock stock, Integer quantity, BigDecimal averagePrice) {
        this.portfolio = portfolio;
        this.stock = stock;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
        this.currentPrice = stock.getCurrentPrice();
        updateCalculatedFields();
    }
    
    public void updateCalculatedFields() {
        if (currentPrice != null && quantity != null && averagePrice != null) {
            totalCost = averagePrice.multiply(BigDecimal.valueOf(quantity));
            currentValue = currentPrice.multiply(BigDecimal.valueOf(quantity));
            unrealizedGainLoss = currentValue.subtract(totalCost);
            
            if (totalCost.compareTo(BigDecimal.ZERO) > 0) {
                unrealizedGainLossPercent = unrealizedGainLoss.divide(totalCost, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            }
            
            if (stock != null && stock.getPreviousClose() != null) {
                BigDecimal previousValue = stock.getPreviousClose().multiply(BigDecimal.valueOf(quantity));
                dayChange = currentValue.subtract(previousValue);
                
                if (previousValue.compareTo(BigDecimal.ZERO) > 0) {
                    dayChangePercent = dayChange.divide(previousValue, 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
                }
            }
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Portfolio getPortfolio() {
        return portfolio;
    }
    
    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
    
    public Stock getStock() {
        return stock;
    }
    
    public void setStock(Stock stock) {
        this.stock = stock;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getAveragePrice() {
        return averagePrice;
    }
    
    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }
    
    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }
    
    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }
    
    public BigDecimal getTotalCost() {
        return totalCost;
    }
    
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    
    public BigDecimal getCurrentValue() {
        return currentValue;
    }
    
    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }
    
    public BigDecimal getUnrealizedGainLoss() {
        return unrealizedGainLoss;
    }
    
    public void setUnrealizedGainLoss(BigDecimal unrealizedGainLoss) {
        this.unrealizedGainLoss = unrealizedGainLoss;
    }
    
    public BigDecimal getUnrealizedGainLossPercent() {
        return unrealizedGainLossPercent;
    }
    
    public void setUnrealizedGainLossPercent(BigDecimal unrealizedGainLossPercent) {
        this.unrealizedGainLossPercent = unrealizedGainLossPercent;
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
}