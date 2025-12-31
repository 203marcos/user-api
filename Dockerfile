# Stage 1: Build (compile the application)
FROM eclipse-temurin:21-jdk-alpine AS builde

WORKDIR /build

# Copy source code
COPY . .

# Build with Maven (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime (run the application)
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy only the compiled JAR from builder stage
COPY --from=builder /build/target/demo2-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the Spring Boot application uses
EXPOSE 8080

# Command to start the application when the container runs
ENTRYPOINT ["java", "-jar", "app.jar"]
