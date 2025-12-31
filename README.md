# User API

[![Build Status](https://github.com/203marcos/user-api/workflows/CI/badge.svg)](https://github.com/203marcos/user-api/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Production-ready REST API for managing users with Spring Boot 3.3, JPA, and PostgreSQL.

## 🚀 Quick Start

### Prerequisites
- JDK 21+
- Maven 3.8+
- Docker & Docker Compose (optional, for PostgreSQL)

### Run with Docker Compose (Recommended)
```bash
git clone https://github.com/203marcos/user-api.git
cd user-api
mvn clean package
docker-compose up
```
API runs at: `http://localhost:8080`

### Run Locally (PostgreSQL required)
```bash
# Make sure PostgreSQL is running on localhost:5432
mvn clean spring-boot:run
```

### Environment Variables

For production deployment, set these environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://db-host:5432/userdb
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=your-secure-password
export SPRING_PROFILES_ACTIVE=prod
export SPRING_JPA_HIBERNATE_DDL_AUTO=validate
```

Or in Docker:
```bash
docker-compose -f docker-compose.yml up
# Environment variables are already set in docker-compose.yml
```

## 📖 Using the API

### Swagger UI (Interactive Documentation)
```
http://localhost:8080/swagger-ui.html
```

### Example Requests

**Create User:**
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name": "João Silva", "email": "joao@email.com"}'
```

**List Users:**
```bash
curl http://localhost:8080/users
```

**Get User by ID:**
```bash
curl http://localhost:8080/users/1
```

**Update User:**
```bash
curl -X PUT http://localhost:8080/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "João Silva", "email": "joao.silva@email.com"}'
```

**Delete User:**
```bash
curl -X DELETE http://localhost:8080/users/1
```

### Response Examples

**Success - Create User (201 Created):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@email.com",
  "createdAt": "2025-12-31T10:30:00",
  "updatedAt": "2025-12-31T10:30:00"
}
```

**Success - List Users (200 OK):**
```json
[
  {
    "id": 1,
    "name": "João Silva",
    "email": "joao@email.com",
    "createdAt": "2025-12-31T10:30:00",
    "updatedAt": "2025-12-31T10:30:00"
  }
]
```

**Error - Validation Error (400 Bad Request):**
```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": [
    "Email must be valid",
    "Name must not be empty"
  ],
  "timestamp": "2025-12-31T10:30:00"
}
```

**Error - Not Found (404):**
```json
{
  "status": 404,
  "message": "User not found",
  "timestamp": "2025-12-31T10:30:00"
}
```

## 🧪 Testing

Run all tests:
```bash
mvn clean test
```

Test coverage includes:
- ✅ Repository (JPA) - `UserRepositoryTest`
- ✅ Service (Unit) - `UserServiceTest`
- ✅ Controller (Integration) - `UserControllerTest`
- ✅ Application Context - `Demo2ApplicationTests`

View coverage report:
```bash
mvn clean test jacoco:report
# Open: target/site/jacoco/index.html
```

## 🏗️ Architecture

```
src/
├── main/
│   ├── java/com/desafio2/demo2/
│   │   ├── Demo2Application.java          # Entry point
│   │   ├── controller/UserController.java # REST endpoints
│   │   ├── service/UserService.java       # Business logic
│   │   ├── repository/UserRepository.java # Data access (JPA)
│   │   ├── model/User.java                # Entity
│   │   ├── dto/                           # DTOs (Request/Response)
│   │   ├── exception/                     # Global error handling
│   │   └── config/                        # Configuration
│   └── resources/
│       ├── application.properties         # Main config
│       ├── application-test.properties    # Test config (H2)
│       └── db/migration/                  # Flyway migrations
└── test/
    └── java/com/desafio2/demo2/           # All tests
```

## 🛠️ Tech Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| **Runtime** | Spring Boot | 3.3.0 |
| **Web** | Spring Web | 3.3.0 |
| **Database** | Spring Data JPA | 3.3.0 |
| **ORM** | Hibernate | 6.x |
| **DB Driver** | PostgreSQL | 42.x |
| **Migrations** | Flyway | 9.x |
| **Validation** | Bean Validation (Jakarta) | 3.x |
| **Documentation** | Springdoc OpenAPI | 2.3.0 |
| **Logging** | SLF4J + Logback | 2.x |
| **Testing** | JUnit 5 + Mockito | 5.x |
| **Code Coverage** | JaCoCo | 0.8.10 |
| **Java** | Eclipse Temurin | 21 |
| **Container** | Docker | Alpine |

## ✅ Features Implemented

- ✅ **CRUD API** - Create, Read, Update, Delete users
- ✅ **Spring Boot 3.3** - Latest Spring Boot version
- ✅ **PostgreSQL** - Production database
- ✅ **Flyway** - Database migration versioning
- ✅ **JPA/Hibernate** - Object-relational mapping
- ✅ **Docker/Compose** - Containerization
- ✅ **Global Exception Handling** - Centralized error handling
- ✅ **Bean Validation** - Request validation
- ✅ **Logging** - SLF4J with console output
- ✅ **Swagger/OpenAPI** - Interactive API documentation
- ✅ **Unit Tests** - 100% coverage with JUnit 5
- ✅ **CI/CD** - GitHub Actions pipeline
- ✅ **Profiles** - dev, test, prod configurations

## 🚧 Roadmap (Coming Soon)

- [ ] **Auditing** - Track createdAt/updatedAt
- [ ] **Pagination** - GET /users?page=0&size=10&sort=name,asc
- [ ] **PATCH** - Partial updates
- [ ] **TestContainers** - Real PostgreSQL in tests
- [ ] **Security** - JWT authentication
- [ ] **Caching** - Redis integration
- [ ] **OpenAPI** - Better API documentation

## 📋 Database

### Flyway Migrations

Migrations are automatically applied on startup:

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

### H2 Console (Development Only)

Access in-memory H2 database console:
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
User: sa
```

## 📊 Profiles

- **default** - PostgreSQL local
- **test** - H2 in-memory (tests only)
- **prod** - Production configuration

## 🔍 Monitoring

Check application health:
```bash
# All endpoints
curl http://localhost:8080/swagger-ui.html

# Logs in Docker
docker logs user-api

# Database logs
docker logs user-api-postgres
```

## � Troubleshooting

### Port 8080 already in use
```bash
# Linux/Mac
lsof -i :8080
kill -9 <PID>

# Windows (PowerShell)
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### PostgreSQL connection refused
```bash
# Check if container is running
docker ps | grep postgres

# If not, start Docker Compose
docker-compose up -d

# Verify connection
docker exec user-api-postgres psql -U postgres -d userdb -c "SELECT 1"
```

### Tests failing with "password authentication failed"
```bash
# Make sure PostgreSQL container is running before tests
docker-compose up -d
sleep 5  # Wait for PostgreSQL to be ready
mvn clean test
```

### Cannot find the database migration files
```bash
# Make sure migration directory exists
ls src/main/resources/db/migration/

# Should contain V1__Create_users_table.sql
```

### H2 Console not working in production
H2 is only for tests. Use PostgreSQL in production:
```properties
# application.properties (dev)
spring.h2.console.enabled=true

# application-prod.properties (prod)
spring.h2.console.enabled=false
```

## �📝 Contributing

1. Create feature branch: `git checkout -b feature/xyz`
2. Make changes
3. Run tests: `mvn clean test`
4. Commit: `git commit -m "feat: add xyz"`
5. Push: `git push origin feature/xyz`
6. Create Pull Request

## 📄 License

MIT License - See LICENSE file for details

## 👨‍💻 Author

**Marcos** - [GitHub](https://github.com/203marcos)

---


