# AWS ECS Deployment Setup Checklist

## ✅ Pre-Deployment Checklist

### 1. AWS Account Setup
- [ ] AWS Account created and configured
- [ ] AWS CLI installed and configured
- [ ] Appropriate IAM permissions

### 2. Create AWS Resources

#### ECR Repository
```bash
aws ecr create-repository \
    --repository-name login-api \
    --region us-east-1
```
- [ ] ECR Repository created: `login-api`
- [ ] Note the repository URI: `YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/login-api`

#### ECS Cluster
```bash
aws ecs create-cluster \
    --cluster-name login-api-cluster \
    --capacity-providers FARGATE \
    --default-capacity-provider-strategy capacityProvider=FARGATE,weight=1
```
- [ ] ECS Cluster created: `login-api-cluster`

#### CloudWatch Log Group
```bash
aws logs create-log-group \
    --log-group-name /ecs/login-api \
    --region us-east-1
```
- [ ] CloudWatch Log Group created: `/ecs/login-api`

### 3. IAM Roles

#### ECS Task Execution Role
- [ ] Role created: `ecsTaskExecutionRole`
- [ ] Policy attached: `AmazonECSTaskExecutionRolePolicy`

#### ECS Task Role
- [ ] Role created: `ecsTaskRole`
- [ ] Custom policy for CloudWatch logs attached

#### GitHub Actions IAM User
- [ ] IAM User created: `github-actions-user`
- [ ] Access keys generated
- [ ] ECR and ECS permissions policy attached

### 4. Update Configuration Files

#### Task Definition (.aws/task-definition.json)
- [ ] Replace `YOUR_ACCOUNT_ID` with actual AWS account ID
- [ ] Update ECR image URI
- [ ] Update execution role ARN
- [ ] Update task role ARN
- [ ] Verify region settings

#### GitHub Secrets
Add these secrets to your GitHub repository:

- [ ] `AWS_ACCESS_KEY_ID`: GitHub Actions IAM user access key
- [ ] `AWS_SECRET_ACCESS_KEY`: GitHub Actions IAM user secret key
- [ ] `AWS_REGION`: `us-east-1` (or your preferred region)
- [ ] `ECR_REPOSITORY`: `login-api`
- [ ] `ECS_CLUSTER`: `login-api-cluster`
- [ ] `ECS_SERVICE`: `login-api-service`
- [ ] `ECS_TASK_DEFINITION`: `.aws/task-definition.json`
- [ ] `CONTAINER_NAME`: `login-api-container`

### 5. Network Configuration

#### VPC and Subnets
- [ ] Default VPC identified or custom VPC created
- [ ] Public subnets identified (at least 2 for high availability)
- [ ] Subnet IDs noted for service configuration

#### Security Group
- [ ] Security group created for ECS tasks
- [ ] Inbound rule: Port 8080 from 0.0.0.0/0 (or ALB security group)
- [ ] Outbound rule: All traffic to 0.0.0.0/0

### 6. ECS Service Creation

#### Register Task Definition
```bash
aws ecs register-task-definition \
    --cli-input-json file://.aws/task-definition.json
```
- [ ] Task definition registered

#### Create ECS Service
```bash
aws ecs create-service \
    --cluster login-api-cluster \
    --service-name login-api-service \
    --task-definition login-api-task \
    --desired-count 1 \
    --launch-type FARGATE \
    --network-configuration "awsvpcConfiguration={subnets=[subnet-xxx,subnet-yyy],securityGroups=[sg-xxx],assignPublicIp=ENABLED}"
```
- [ ] ECS Service created: `login-api-service`
- [ ] Service running with desired count

## 🚀 Deployment Process

### 1. Code Changes
- [ ] All code changes committed to repository
- [ ] GitHub Actions workflow file updated
- [ ] Task definition file updated with correct values

### 2. First Deployment
- [ ] Push code to `master` or `main` branch
- [ ] GitHub Actions workflow triggered
- [ ] Docker image built and pushed to ECR
- [ ] ECS service updated with new task definition

### 3. Verification
- [ ] GitHub Actions workflow completed successfully
- [ ] ECS service shows running tasks
- [ ] Application health check passes
- [ ] API endpoints accessible

## 🔍 Testing Commands

### Test ECR Access
```bash
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com
```

### Test ECS Service
```bash
aws ecs describe-services --cluster login-api-cluster --services login-api-service
```

### Test Application
```bash
# Get the public IP of the running task
aws ecs describe-tasks --cluster login-api-cluster --tasks TASK_ARN

# Test the API
curl -X POST http://TASK_PUBLIC_IP:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com"}'
```

## 🛠️ Troubleshooting

### Common Issues
- [ ] IAM permissions insufficient
- [ ] Security group blocking traffic
- [ ] Task definition errors
- [ ] ECR authentication issues
- [ ] GitHub secrets not configured

### Debug Commands
```bash
# Check ECS service events
aws ecs describe-services --cluster login-api-cluster --services login-api-service

# Check task logs
aws logs get-log-events --log-group-name /ecs/login-api --log-stream-name ecs/login-api-container/TASK_ID

# Check task definition
aws ecs describe-task-definition --task-definition login-api-task
```

## 📋 Post-Deployment

### Optional Enhancements
- [ ] Application Load Balancer setup
- [ ] Custom domain configuration
- [ ] SSL certificate setup
- [ ] Auto-scaling configuration
- [ ] CloudWatch alarms and monitoring
- [ ] CI/CD pipeline optimization

### Monitoring Setup
- [ ] CloudWatch dashboards created
- [ ] Log retention policies set
- [ ] Cost monitoring alerts configured
- [ ] Performance monitoring enabled

## 🎯 Success Criteria

- [ ] Application builds successfully in GitHub Actions
- [ ] Docker image pushes to ECR without errors
- [ ] ECS service deploys new tasks successfully
- [ ] Health checks pass consistently
- [ ] API endpoints return expected responses
- [ ] Logs are visible in CloudWatch
- [ ] No security vulnerabilities detected

## 📞 Support Resources

- [AWS ECS Documentation](https://docs.aws.amazon.com/ecs/)
- [AWS ECR Documentation](https://docs.aws.amazon.com/ecr/)
- [GitHub Actions AWS Documentation](https://github.com/aws-actions)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)

---

**Note**: Replace all placeholder values (YOUR_ACCOUNT_ID, subnet-xxx, sg-xxx) with your actual AWS resource identifiers before deployment.