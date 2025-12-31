# User API

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A production-ready REST API for user management built with Spring Boot 3.3, PostgreSQL, and Docker.

## Getting Started

### Prerequisites

- **JDK 21** or higher
- **Maven 3.8** or higher
- **Docker & Docker Compose**

### Installation & Running

The recommended way is using Docker Compose, which sets up both the PostgreSQL database and the Spring Boot application:

```bash
git clone https://github.com/203marcos/user-api.git
cd user-api
mvn clean package
docker-compose up
```

The API will be available at: **http://localhost:8080**

#### What Happens with Docker Compose

1. **PostgreSQL 15** container starts on port 5432
2. **Spring Boot application** automatically waits for PostgreSQL to be ready
3. **Flyway migrations** run automatically to create the database schema
4. API is ready to accept requests

### Running Locally (Without Docker)

If you have PostgreSQL running locally on `localhost:5432`:

```bash
mvn clean spring-boot:run
```

Make sure to set the correct database credentials in `application.properties`.

## Database Configuration

### PostgreSQL Connection

By default, the application connects to PostgreSQL with these settings:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/userdb
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

When using Docker Compose, these are automatically configured.

### Flyway Migrations

Database schema is managed by Flyway. Migrations run automatically on startup:

```sql
-- V1__Create_users_table.sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Development with H2

For testing without PostgreSQL, use H2 in-memory database:

**Access H2 Console:** http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`

## API Documentation

### Swagger UI

Interactive API documentation is available at:

```
http://localhost:8080/swagger-ui.html
```

All endpoints (Create, Read, Update, Delete) can be tested directly from Swagger.

## Testing

### Run All Tests

```bash
mvn clean test
```

Tests use H2 in-memory database automatically.

### View Code Coverage

```bash
mvn clean test jacoco:report
open target/site/jacoco/index.html
```

## Technology Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| **Framework** | Spring Boot | 3.3.0 |
| **Database** | PostgreSQL | 15 |
| **ORM** | Hibernate | 6.x |
| **Migrations** | Flyway | 9.x |
| **Container** | Docker | Latest |
| **Java** | Eclipse Temurin | 21 |
| **Testing** | JUnit 5 + Mockito | 5.x |
| **API Docs** | Springdoc OpenAPI | 2.3.0 |

## Project Features

- **REST API** - Full CRUD operations for user management
- **PostgreSQL** - Production-grade relational database
- **Docker** - Containerized application and database
- **Flyway** - Automated database schema versioning
- **Validation** - Input validation with Bean Validation
- **Error Handling** - Global exception handler with meaningful error responses
- **Testing** - Unit and integration tests with high coverage
- **Documentation** - Swagger/OpenAPI for API exploration
- **Profiles** - Separate configurations for dev, test, and production

## Configuration

### Environment Variables (Production)

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://db-host:5432/userdb
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=your-secure-password
export SPRING_PROFILES_ACTIVE=prod
export SPRING_JPA_HIBERNATE_DDL_AUTO=validate
```

### Active Profiles

- **default** - PostgreSQL local connection
- **test** - H2 in-memory database (used for tests)
- **prod** - Production configuration with validation only

## Troubleshooting

### Docker Port 8080 Already in Use

```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### PostgreSQL Connection Refused

```bash
# Ensure Docker containers are running
docker-compose up -d

# Check if PostgreSQL is ready
docker logs user-api-postgres

# Should see: "database system is ready to accept connections"
```

### Tests Fail with Connection Error

PostgreSQL must be running before tests:

```bash
docker-compose up -d
sleep 5  # Wait for PostgreSQL to be ready
mvn clean test
```

### Database Schema Not Created

Verify Flyway migrations are in the correct directory:

```bash
ls src/main/resources/db/migration/
# Should contain: V1__Create_users_table.sql
```

## Future Enhancements

- [ ] **Auditing** - Automatic tracking of created/updated timestamps
- [ ] **Pagination** - Paginated results with sorting support
- [ ] **PATCH** - Support for partial updates
- [ ] **TestContainers** - Real PostgreSQL in integration tests
- [ ] **Authentication** - JWT token-based security
- [ ] **Caching** - Redis for improved performance

## License

MIT License - See [LICENSE](LICENSE) file for details


