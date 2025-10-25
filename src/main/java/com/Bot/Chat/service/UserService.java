package com.Bot.Chat.service;

import com.Bot.Chat.model.Portfolio;
import com.Bot.Chat.model.User;
import com.Bot.Chat.repository.PortfolioRepository;
import com.Bot.Chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PortfolioRepository portfolioRepository;
    
    
    public User registerUser(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User(username, password, email);
        User savedUser = userRepository.save(user);
        
        // Create initial portfolio
        createInitialPortfolio(savedUser);
        
        return savedUser;
    }
    
    private void createInitialPortfolio(User user) {
        Portfolio portfolio = new Portfolio(user);
        portfolioRepository.save(portfolio);
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User updateLastLogin(User user) {
        user.setLastLogin(java.time.LocalDateTime.now());
        return userRepository.save(user);
    }
    
    public void deactivateUser(User user) {
        user.setActive(false);
        userRepository.save(user);
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = findByUsername(username);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}