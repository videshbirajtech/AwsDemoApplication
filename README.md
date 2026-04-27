# Simple Login Backend API - AWS ECS Ready

A production-ready REST API backend for login functionality built with Spring Boot, optimized for AWS ECS deployment.

## 🚀 Quick Start

### Local Development
```bash
# Run with Maven
./mvnw spring-boot:run

# Run with Docker
docker-compose up --build -d

# Test the API
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com"}'
```

### AWS ECS Deployment
1. Follow the [AWS Setup Checklist](AWS-SETUP-CHECKLIST.md)
2. Configure GitHub Secrets
3. Push to `master` branch to trigger deployment

## 📋 Project Structure

```
├── .aws/
│   └── task-definition.json            # ECS task definition
├── .github/workflows/
│   └── aws-ecs-deploy.yml             # GitHub Actions deployment
├── src/main/java/com/example/AWSDemo/
│   ├── AwsDemoApplication.java        # Main Spring Boot application
│   ├── controller/
│   │   ├── LoginController.java       # Login API endpoint
│   │   └── HealthController.java      # Health check endpoint
│   ├── model/
│   │   ├── LoginRequest.java          # Request model with validation
│   │   └── LoginResponse.java         # Response model
│   └── config/
│       └── WebConfig.java             # CORS configuration
├── src/main/resources/
│   ├── application.properties         # Default configuration
│   └── application-production.properties # Production configuration
├── Dockerfile                         # Multi-stage production Docker build
└── docker-compose.yml                # Local development
```

## 🔌 API Endpoints

### POST /api/login
Login with email address

**Request:**
```json
{
  "email": "user@example.com"
}
```

**Response:**
```json
{
  "message": "Hello user@example.com",
  "email": "user@example.com",
  "timestamp": 1640995200000
}
```

### GET /api/status
Service status endpoint

**Response:**
```json
{
  "status": "UP",
  "service": "login-api",
  "version": "1.0.0",
  "timestamp": 1640995200000
}
```

### GET /actuator/health
Spring Boot health check

**Response:**
```json
{
  "status": "UP"
}
```

## 🏗️ AWS Architecture

```
GitHub → GitHub Actions → ECR → ECS Fargate
   ↓
[Build & Test] → [Docker Build] → [Push to ECR] → [Deploy to ECS]
```

### AWS Resources
- **ECR Repository**: `login-api`
- **ECS Cluster**: `login-api-cluster`
- **ECS Service**: `login-api-service`
- **Task Definition**: `login-api-task`
- **CloudWatch Logs**: `/ecs/login-api`

## 🔧 Configuration

### Environment Variables
- `SPRING_PROFILES_ACTIVE`: `production` (for ECS)
- `SERVER_PORT`: `8080`
- `AWS_REGION`: `us-east-1`

### GitHub Secrets Required
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_REGION`
- `ECR_REPOSITORY`
- `ECS_CLUSTER`
- `ECS_SERVICE`
- `ECS_TASK_DEFINITION`
- `CONTAINER_NAME`

## 🐳 Docker Features

### Multi-stage Build
- Build stage: Maven + OpenJDK 17
- Runtime stage: OpenJDK 17 JRE Alpine
- Non-root user for security
- Health checks included
- JVM optimizations for containers

### Container Optimizations
- Container-aware JVM settings
- G1 garbage collector
- Memory limit awareness
- Graceful shutdown support

## 📊 Monitoring & Logging

### Health Checks
- Application: `/actuator/health`
- Custom: `/api/status`
- Container: Built-in Docker health check
- ECS: Task health monitoring

### Logging
- Structured JSON logging
- CloudWatch integration
- Request/response logging
- Error tracking with stack traces

## 🔒 Security Features

- Input validation with Bean Validation
- CORS configuration
- Non-root container user
- Secure cookie settings
- Security headers
- Error handling without information leakage

## 🚀 Deployment Process

1. **Code Push** → Triggers GitHub Actions
2. **Test Phase** → Runs unit tests
3. **Build Phase** → Creates Docker image
4. **Push Phase** → Uploads to ECR
5. **Deploy Phase** → Updates ECS service
6. **Verify Phase** → Confirms deployment success

## 📈 Scaling & Performance

### Current Configuration
- **CPU**: 0.25 vCPU (256 CPU units)
- **Memory**: 512 MB
- **Cost**: ~$9/month for single instance

### Scaling Options
- Horizontal: Increase desired count
- Vertical: Increase CPU/memory
- Auto-scaling: Based on CPU/memory metrics
- Load balancer: For high availability

## 🔍 Troubleshooting

### Local Issues
```bash
# Check application logs
docker-compose logs backend

# Test health endpoint
curl http://localhost:8080/actuator/health
```

### AWS Issues
```bash
# Check ECS service
aws ecs describe-services --cluster login-api-cluster --services login-api-service

# View logs
aws logs tail /ecs/login-api --follow
```

## 📚 Documentation

- [AWS ECS Deployment Guide](AWS-ECS-DEPLOYMENT-GUIDE.md)
- [AWS Setup Checklist](AWS-SETUP-CHECKLIST.md)
- [API Testing Guide](API-TESTING.md)
- [GitHub Actions Troubleshooting](GITHUB-ACTIONS-TROUBLESHOOTING.md)

## 🛠️ Technology Stack

- **Framework**: Spring Boot 4.0.6
- **Java**: 17
- **Build**: Maven
- **Container**: Docker
- **Cloud**: AWS ECS Fargate
- **Registry**: Amazon ECR
- **CI/CD**: GitHub Actions
- **Monitoring**: CloudWatch

## 🎯 Production Ready Features

✅ **Container Optimized**  
✅ **Health Checks**  
✅ **Structured Logging**  
✅ **Input Validation**  
✅ **Error Handling**  
✅ **Security Headers**  
✅ **Graceful Shutdown**  
✅ **Auto Deployment**  
✅ **Monitoring Ready**  
✅ **Cost Optimized**  

## 📞 Support

For deployment issues:
1. Check the [AWS Setup Checklist](AWS-SETUP-CHECKLIST.md)
2. Review GitHub Actions logs
3. Check CloudWatch logs
4. Verify AWS resource configuration