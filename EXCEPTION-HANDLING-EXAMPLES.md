# Exception Handling Examples

This document demonstrates how the global exception handler works with various error scenarios.

## 🛡️ Global Exception Handler Features

- **Validation Errors**: Handles `@Valid` annotation failures
- **Constraint Violations**: Handles method parameter validation
- **Malformed JSON**: Handles invalid JSON requests
- **Type Mismatches**: Handles parameter type conversion errors
- **Business Logic Errors**: Handles custom business exceptions
- **Generic Errors**: Handles unexpected runtime exceptions

## 📋 Error Response Format

All errors return a consistent JSON structure:

```json
{
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input data",
  "path": "/api/login",
  "timestamp": "2024-04-27T11:30:45",
  "field_errors": {
    "email": "Please provide a valid email address"
  },
  "global_errors": ["Additional validation messages"]
}
```

## 🧪 Test Scenarios

### 1. Validation Error - Empty Email

**Request:**
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":""}'
```

**Response:**
```json
{
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input data",
  "path": "/api/login",
  "timestamp": "2024-04-27T11:30:45",
  "field_errors": {
    "email": "Email is required and cannot be empty"
  }
}
```

### 2. Validation Error - Invalid Email Format

**Request:**
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"invalid-email"}'
```

**Response:**
```json
{
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input data",
  "path": "/api/login",
  "timestamp": "2024-04-27T11:30:45",
  "field_errors": {
    "email": "Please provide a valid email address"
  }
}
```

### 3. Validation Error - Email Too Long

**Request:**
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"verylongemailaddressthatexceedsthemaximumlengthallowedfortheemailfield@example.com"}'
```

**Response:**
```json
{
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input data",
  "path": "/api/login",
  "timestamp": "2024-04-27T11:30:45",
  "field_errors": {
    "email": "Email must not exceed 100 characters"
  }
}
```

### 4. Malformed JSON

**Request:**
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com"'
```

**Response:**
```json
{
  "status": 400,
  "error": "Malformed JSON",
  "message": "Invalid JSON format in request body",
  "path": "/api/login",
  "timestamp": "2024-04-27T11:30:45"
}
```

### 5. Missing Request Body

**Request:**
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json"
```

**Response:**
```json
{
  "status": 400,
  "error": "Malformed JSON",
  "message": "Invalid JSON format in request body",
  "path": "/api/login",
  "timestamp": "2024-04-27T11:30:45"
}
```

### 6. Missing Email Field

**Request:**
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{}'
```

**Response:**
```json
{
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input data",
  "path": "/api/login",
  "timestamp": "2024-04-27T11:30:45",
  "field_errors": {
    "email": "Email is required and cannot be empty"
  }
}
```

### 7. Business Logic Error - Blocked Email

**Request:**
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"blocked@example.com"}'
```

**Response:**
```json
{
  "status": 400,
  "error": "Business Logic Error",
  "message": "This email is blocked from login",
  "path": "/api/login",
  "timestamp": "2024-04-27T11:30:45"
}
```

### 8. Multiple Validation Errors

**Request:**
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"verylonginvalidemail"}'
```

**Response:**
```json
{
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input data",
  "path": "/api/login",
  "timestamp": "2024-04-27T11:30:45",
  "field_errors": {
    "email": "Please provide a valid email address"
  }
}
```

### 9. Wrong Content Type

**Request:**
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: text/plain" \
  -d '{"email":"test@example.com"}'
```

**Response:**
```json
{
  "status": 400,
  "error": "Malformed JSON",
  "message": "Invalid JSON format in request body",
  "path": "/api/login",
  "timestamp": "2024-04-27T11:30:45"
}
```

### 10. Successful Request

**Request:**
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com"}'
```

**Response:**
```json
{
  "message": "Hello user@example.com",
  "email": "user@example.com",
  "timestamp": 1640995200000
}
```

## 🔧 Exception Handler Components

### GlobalExceptionHandler.java
- `@ControllerAdvice` annotation for global exception handling
- Specific handlers for different exception types
- Consistent error response format
- Proper logging for debugging

### ErrorResponse.java
- Standardized error response model
- Builder pattern for easy construction
- JSON serialization configuration
- Timestamp inclusion

### BusinessException.java
- Custom exception for business logic errors
- Configurable HTTP status codes
- Custom error messages and types

## 🎯 Benefits

✅ **Consistent Error Format**: All errors follow the same JSON structure  
✅ **Detailed Validation Messages**: Clear field-specific error messages  
✅ **Proper HTTP Status Codes**: Appropriate status codes for different error types  
✅ **Security**: No sensitive information leaked in error messages  
✅ **Logging**: All errors are properly logged for debugging  
✅ **Maintainability**: Centralized error handling logic  
✅ **Client-Friendly**: Easy to parse error responses for frontend applications  

## 🚀 Usage in Controllers

Controllers can now focus on business logic without manual error handling:

```java
@PostMapping("/login")
public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    // Validation errors are automatically handled by GlobalExceptionHandler
    
    // Business logic
    if (someBusinessCondition) {
        throw new BusinessException("Custom business error message");
    }
    
    // Success response
    return ResponseEntity.ok(new LoginResponse("Success", loginRequest.getEmail()));
}
```

## 🔍 Monitoring

All exceptions are logged with appropriate levels:
- **WARN**: Validation errors, business logic errors
- **ERROR**: Unexpected runtime errors, system errors

This helps with monitoring and debugging in production environments.