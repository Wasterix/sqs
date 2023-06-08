package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    @RequestMapping("/beatles")
    public String getGreeting() {
        return "The beatles are great";
    }

    @RequestMapping("/metallica")
    public String getGreeting2() {
        return "Metallica is nice";
    }
}