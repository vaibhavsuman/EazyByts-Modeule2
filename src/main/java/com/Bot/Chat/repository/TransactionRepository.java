package com.Bot.Chat.repository;

import com.Bot.Chat.model.Portfolio;
import com.Bot.Chat.model.Stock;
import com.Bot.Chat.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByPortfolio(Portfolio portfolio);
    
    List<Transaction> findByStock(Stock stock);
    
    @Query("SELECT t FROM Transaction t WHERE t.portfolio.user.id = :userId")
    List<Transaction> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT t FROM Transaction t WHERE t.portfolio.user.username = :username")
    List<Transaction> findByUsername(@Param("username") String username);
    
    @Query("SELECT t FROM Transaction t WHERE t.type = :type AND t.portfolio.user.id = :userId")
    List<Transaction> findByTypeAndUserId(@Param("type") Transaction.TransactionType type, @Param("userId") Long userId);
    
    @Query("SELECT t FROM Transaction t WHERE t.createdAt BETWEEN :startDate AND :endDate")
    List<Transaction> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT t FROM Transaction t WHERE t.portfolio.user.id = :userId AND t.createdAt BETWEEN :startDate AND :endDate")
    List<Transaction> findByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}