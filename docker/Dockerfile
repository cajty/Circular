FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /app

# Copy Maven files for dependency resolution
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

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

# Environment variables for configuration
ENV DB_HOST=postgres
ENV DB_PORT=5432
ENV DB_NAME=circular
ENV DB_USERNAME=circular_user
ENV DB_PASSWORD=circular_pass
ENV SERVER_PORT=8080
ENV JWT_EXPIRATION=86400000
ENV JWT_SECRET=default_secret_to_be_replaced_in_production
ENV ALLOWED_ORIGINS=http://localhost:4200,http://localhost:8080
ENV AWS_ACCESS_KEY=${AWS_ACCESS_KEY}
ENV AWS_SECRET_KEY=${AWS_SECRET_KEY}
ENV AWS_REGION=${AWS_REGION}
ENV AWS_BUCKET_NAME=${AWS_BUCKET_NAME}


# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]