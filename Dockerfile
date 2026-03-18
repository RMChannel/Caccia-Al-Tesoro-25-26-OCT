# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21-jammy AS builder
WORKDIR /app
COPY pom.xml .
# Download dependencies for caching
RUN mvn dependency:go-offline -B
COPY src/ src/
RUN mvn clean package -DskipTests
# Rename the jar to a fixed name for the next stage
RUN find target -name "CacciaAlTesoro_V2-*.jar" ! -name "*.original" -exec mv {} target/app.jar \;

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# The application runs on port 80 as defined in application.properties
EXPOSE 80

COPY --from=builder /app/target/app.jar app.jar

# Default environment variables for database connection
# These should be overridden when running the container
ENV DB_URL=""
ENV DB_USER=""
ENV DB_PASS=""

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
