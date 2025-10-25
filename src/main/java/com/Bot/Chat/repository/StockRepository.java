package com.Bot.Chat.repository;

import com.Bot.Chat.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    
    Optional<Stock> findBySymbol(String symbol);
    
    List<Stock> findByIsActiveTrue();
    
    List<Stock> findByNameContainingIgnoreCase(String name);
    
    boolean existsBySymbol(String symbol);
}