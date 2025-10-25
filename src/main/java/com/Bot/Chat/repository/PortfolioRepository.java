package com.Bot.Chat.repository;

import com.Bot.Chat.model.Portfolio;
import com.Bot.Chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    
    Optional<Portfolio> findByUser(User user);
    
    @Query("SELECT p FROM Portfolio p WHERE p.user.id = :userId")
    Optional<Portfolio> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT p FROM Portfolio p WHERE p.user.username = :username")
    Optional<Portfolio> findByUsername(@Param("username") String username);
    
    @Query("SELECT p FROM Portfolio p WHERE p.totalValue > :minValue ORDER BY p.totalValue DESC")
    List<Portfolio> findTopPerformersByValue(@Param("minValue") java.math.BigDecimal minValue);
    
    @Query("SELECT p FROM Portfolio p WHERE p.totalReturnPercent > :minReturn ORDER BY p.totalReturnPercent DESC")
    List<Portfolio> findTopPerformersByReturn(@Param("minReturn") java.math.BigDecimal minReturn);
}