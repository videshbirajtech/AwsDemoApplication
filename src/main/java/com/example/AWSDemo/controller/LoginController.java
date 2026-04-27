package com.example.AWSDemo.controller;

import com.example.AWSDemo.model.LoginRequest;
import com.example.AWSDemo.model.LoginResponse;
import com.example.AWSDemo.exception.BusinessException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        logger.info("Login request received for email: {}", email);
        
        // Example of business logic validation
        if (email != null && email.contains("blocked")) {
            throw new BusinessException("This email is blocked from login");
        }
        
        String message = "Hello " + email;
        LoginResponse response = new LoginResponse(message, email);
        
        logger.info("Login successful for email: {}", email);
        return ResponseEntity.ok(response);
    }
}