# my-crud

A simple CRUD application built with Spring Boot.

## Tech Stack

- **Java** 21
- **Spring Boot** 4
- **Database** PostgreSQL
- **CI/CD** Jenkins
- **Load Balancing** Nginx

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

## Jenkins

The application will be available at `http://localhost:8080`.

Create a pipeline setup config then execute build now (No GitHub hit)

For actual CI/CD test:
- **ngrok** to serve localhost on web
- create user token from Jenkins
- Github webhooks config

Sample webook:
`https://root:<token-from-jenkins>@<your-ngork-url>/github-webhook/`

## Verify Load Balancing

Access `http://localhost/actuator/health/instancePort` 

or

```bash
curl http://localhost/actuator/health/instancePort
```

```json
{
  "details": {
    "port": "8100"
  },
  "status": "UP"
}
```

The port should switch to:  `8100, 8101, 8102`


## Jaegar (UI)

Access `http://localhost:16686`


## Prometheus (UI)

Access `http://localhost:9090`