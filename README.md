# User API

Simple REST API for managing users with Spring Boot, JPA and H2.

## Getting Started

### Requirements
- JDK 21
- Maven 3.8

### Run
```bash
git clone https://github.com/seu-usuario/user-api.git
cd user-api
mvn spring-boot:run
```

App runs at `http://localhost:8080`

## Using the API

### Swagger
See all endpoints: `http://localhost:8080/swagger-ui.html`

### Database Console
Access H2: `http://localhost:8080/h2-console`
- URL: `jdbc:h2:mem:testdb`
- User: `sa`

## Running Tests

```bash
mvn clean test
```

Tests cover:
- Repository (JPA)
- Service (mocks)
- Controller (integration)
- Validation and errors

## What's Done

- ✅ Spring Boot CRUD API
- ✅ JPA with H2 database
- ✅ Global error handling
- ✅ Bean validation
- ✅ Unit and integration tests
- ✅ Logging with SLF4J
- ✅ GitHub Actions CI/CD

## Stack

- Spring Boot 3.3.0
- Spring Data JPA
- H2 Database
- JUnit 5 + Mockito
- SLF4J
- GitHub Actions

## Build Status

All tests passing ✅