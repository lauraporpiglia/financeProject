
# Finance Project

**Endpoints  **
TBD

### Technologies
---  
- Java 17
- Spring Boot 3.0
- Restful API
- Gradle
- Junit5
- Mockito
- Integration Tests
- H2 Database (In-Memory) - JPA Connection
- Spring Boot Actuator
- Docker
- Postman - API testing manual TODO: add json

next ? 
- Postman - API testing automate
- Open Api - Swagger setup
- API versioning
- CI/CD - ??? 
- Postman - Requestly / automate????
- Spring Cache
- Resilience4j (Rate Limiter)

###  Build/Run .....
TBD

### TEST .....

NewMan
Docker for Newman
    docker run -v $(pwd):/etc/newman -t postman/newman run testing/t_suite_1.json

brew install node
npm install -g newman
newman -v


suggestion:newman run /path/to/your_collection.json -e /path/to/your_environment.json

