package com.juggenova.doodleclone.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from React dev server
public class HelloController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello World from Spring Boot!";
    }
}
