FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /app

# Copy Maven files for dependency resolution
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Make the Maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies (will be cached by Docker if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

# Create a smaller runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create a non-root user for running the application
RUN addgroup -S springgroup && adduser -S springuser -G springgroup
USER springuser

# Copy the built JAR file
COPY --from=build /app/target/*.jar app.jar

# Environment variables are set through docker-compose or docker run command
# These are just default values that will be overridden
ENV DB_HOST=postgres \
    DB_PORT=5432 \
    DB_NAME=circular_db \
    DB_USERNAME=admin \
    DB_PASSWORD=admin \
    SERVER_PORT=8080 \
    JWT_EXPIRATION=86400000 \
    JWT_SECRET=Your32CharacterLongBase64EncodedSecretKeydvererberbbe \
    ALLOWED_ORIGINS=http://localhost:4200,http://localhost:8080

# Expose port 8080
EXPOSE 8080

# Wait for PostgreSQL to be ready before starting the application
CMD ["java", "-jar", "app.jar"]