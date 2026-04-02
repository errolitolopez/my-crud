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

### 1. Build & run (Infrastructure)

```bash
docker compose -f docker-compose-infra.yml up -d
```

### 2. Build & run (Spring Boot Application)

```bash
docker compose up -d --build
```

The application will be available at `http://localhost:8100`.

## API Documentation (Swagger)

After starting the app, you can access Swagger UI here: `http://localhost:8100/swagger-ui.html`