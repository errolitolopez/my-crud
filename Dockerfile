# Build
FROM maven:3.9.11-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# Run the application
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy jar
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# JVM optimizations
ENV JAVA_OPTS="-XX:MaxRAMPercentage=35.0 \
               -XX:InitialRAMPercentage=25.0 \
               -XX:MinRAMPercentage=25.0 \
               -XX:+UseSerialGC \
               -XX:+UseContainerSupport \
               -XshowSettings:vm"

# Run the app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]