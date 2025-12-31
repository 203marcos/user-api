# Base image with Java 21 (lightweight Alpine version)
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory inside the container
# All commands will be executed from this folder
WORKDIR /app

# Copy the compiled JAR file into the container
# This JAR is generated after running: mvn clean package
COPY target/demo2-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the Spring Boot application uses
EXPOSE 8080

# Command to start the application when the container runs
ENTRYPOINT ["java", "-jar", "app.jar"]
