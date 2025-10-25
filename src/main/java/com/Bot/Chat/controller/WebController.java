package com.Bot.Chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "index";
    }
    
    @GetMapping("/trading")
    public String trading() {
        return "index";
    }
    
    @GetMapping("/portfolio")
    public String portfolio() {
        return "index";
    }
}