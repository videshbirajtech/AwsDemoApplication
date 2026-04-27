package com.example.AWSDemo.controller;

import com.example.AWSDemo.model.LoginRequest;
import com.example.AWSDemo.model.LoginResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String email = loginRequest.getEmail();
            logger.info("Login request received for email: {}", email);
            
            if (email == null || email.trim().isEmpty()) {
                logger.warn("Login attempt with empty email");
                return ResponseEntity.badRequest()
                    .body(new LoginResponse("Email is required", null));
            }
            
            String message = "Hello " + email;
            LoginResponse response = new LoginResponse(message, email);
            
            logger.info("Login successful for email: {}", email);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing login request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new LoginResponse("Internal server error", null));
        }
    }
}