# my-crud

A simple CRUD application built with Spring Boot.

## Tech Stack

- **Java** 21
- **Spring Boot** 4.0.5
- **Database** PostgreSQL

## Prerequisites

- Java 21
- Maven
- Docker & Docker Compose

## Getting Started

### 1. Run the compose

```bash
docker-compose up -d
```

### 2. Run the Application

```bash
./mvnw spring-boot:run
```

The application will be available at `http://localhost:8090`.

## API Documentation (Swagger)
After starting the app, you can access Swagger UI here: `http://localhost:8090/swagger-ui.html`

## Docker Compose

```yaml
services:
  postgres:
    image: postgres:latest
    container_name: postgres
    restart: unless-stopped
    environment:
      POSTGRES_USER: root
      POSTGRES_PASSWORD: 123qwe
      POSTGRES_DB: my_db
    ports:
      - "5432:5432"
    networks:
      - my-network

networks:
  my-network:
    driver: bridge
```