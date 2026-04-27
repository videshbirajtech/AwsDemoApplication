package com.example.AWSDemo.controller;

import com.example.AWSDemo.exception.BusinessException;
import com.example.AWSDemo.model.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

/**
 * Test controller to demonstrate exception handling scenarios
 * Remove this controller in production
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @PostMapping("/validation-error")
    public ResponseEntity<String> testValidationError(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok("Validation passed for: " + request.getEmail());
    }

    @GetMapping("/business-error")
    public ResponseEntity<String> testBusinessError() {
        throw new BusinessException("This is a test business logic error");
    }

    @GetMapping("/custom-business-error")
    public ResponseEntity<String> testCustomBusinessError() {
        throw new BusinessException("Custom error message", "Custom Error Type", HttpStatus.CONFLICT);
    }

    @GetMapping("/runtime-error")
    public ResponseEntity<String> testRuntimeError() {
        throw new RuntimeException("This is a test runtime error");
    }

    @GetMapping("/null-pointer")
    public ResponseEntity<String> testNullPointerError() {
        String nullString = null;
        return ResponseEntity.ok(nullString.length() + ""); // This will throw NPE
    }

    @PostMapping("/json-error")
    public ResponseEntity<String> testJsonError(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok("JSON processed successfully");
    }

    @GetMapping("/type-mismatch/{id}")
    public ResponseEntity<String> testTypeMismatch(@PathVariable Long id) {
        return ResponseEntity.ok("ID received: " + id);
    }
}