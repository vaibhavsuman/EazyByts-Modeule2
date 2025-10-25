package com.Bot.Chat.repository;

import com.Bot.Chat.model.Portfolio;
import com.Bot.Chat.model.PortfolioStock;
import com.Bot.Chat.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioStockRepository extends JpaRepository<PortfolioStock, Long> {
    
    Optional<PortfolioStock> findByPortfolioAndStock(Portfolio portfolio, Stock stock);
    
    List<PortfolioStock> findByPortfolio(Portfolio portfolio);
    
    @Query("SELECT ps FROM PortfolioStock ps WHERE ps.portfolio.user.id = :userId")
    List<PortfolioStock> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT ps FROM PortfolioStock ps WHERE ps.portfolio.user.username = :username")
    List<PortfolioStock> findByUsername(@Param("username") String username);
    
    @Query("SELECT ps FROM PortfolioStock ps WHERE ps.unrealizedGainLossPercent > :minGain ORDER BY ps.unrealizedGainLossPercent DESC")
    List<PortfolioStock> findTopPerformers(@Param("minGain") java.math.BigDecimal minGain);
    
    @Query("SELECT ps FROM PortfolioStock ps WHERE ps.unrealizedGainLoss < :maxLoss ORDER BY ps.unrealizedGainLoss ASC")
    List<PortfolioStock> findWorstPerformers(@Param("maxLoss") java.math.BigDecimal maxLoss);
}