# Spring Boot Server Conventions

This document outlines the architectural patterns, coding conventions, and best practices specifically for the Spring Boot server component of the project, located in the `spring-server` directory.

Refer to `../../AGENTS.md` for project-wide guidelines.

## 1. Tech Stack & Dependency Management

- **Language**: Kotlin.
- **Framework**: Spring Boot (latest stable version).
- **Build Tool**: Gradle.
- **Database**: PostgreSQL.
- **ORM**: Spring Data JPA with Hibernate.
- **Dependency Management**: Utilize Gradle for dependency management, ensuring versions are managed consistently (e.g., via Gradle Version Catalogs).

## 2. Architecture

The Spring Boot server follows **Clean Architecture** with a layered approach:

- **Controller Layer**: Handles HTTP requests/responses. Maps request bodies to domain models or DTOs and delegates to the Service layer. Never contains business logic.
- **Service Layer**: Contains all business logic and orchestrates operations between repositories and external services.
- **Repository Layer**: Abstracts data access. Returns domain models, not raw JPA entities.

### Package Structure

Organize by **feature/domain**, not by layer:

```
com.cyrillrx.rpg.server/
├── campaign/
│   ├── CampaignController.kt
│   ├── domain/
│   │   ├── CampaignService.kt
│   │   └── model/
│   └── data/
│       ├── CampaignEntity.kt
│       └── CampaignRepository.kt
├── security/
│   └── ...
└── shared/
    └── ...  # Cross-cutting utilities
```

### DTOs vs Domain Models

- Use dedicated **request/response DTOs** at the Controller boundary (e.g., `CreateCampaignRequest`, `CampaignResponse`).
- Keep **domain models** free of Spring/JPA annotations — they live in the `domain/model/` package.
- Use **JPA entities** only in the `data/` package; map to/from domain models before returning from repositories.

## 3. API Design

- Design RESTful APIs following principles like statelessness, resource-based URLs, and standard HTTP methods.
- Use clear and consistent naming conventions for API endpoints and request/response bodies.
- Implement proper error handling with appropriate HTTP status codes and informative error messages.

## 3.5. Kotlin Idioms for Spring Boot

- Use `data class` for request/response DTOs.
- Avoid `lateinit var` for injected dependencies; prefer constructor injection, which works naturally with Kotlin's `val`.
- Be explicit about nullability on JPA entity fields — Hibernate may set fields to `null` even for non-nullable Kotlin types. Use `var` with a sensible default or nullable type for JPA-managed fields.
- Prefer Kotlin's `require()` / `check()` for precondition validation in service methods.

## 4. Security

- Implement authentication and authorization mechanisms (e.g., Spring Security with JWT).
- Ensure sensitive data is handled securely (e.g., encryption, proper storage).
- Validate all input to prevent common vulnerabilities like SQL injection and XSS.

## 5. Testing

- **Unit Tests**: Write unit tests for business logic in the Service layer.
- **Integration Tests**: Write integration tests for controllers and repository interactions using Spring Boot's testing utilities.
- **End-to-End Tests**: Consider using tools like Bruno (see `BRUNO_CONVENTIONS.md`) for API end-to-end testing.

## 6. Configuration

- Manage configuration using Spring Boot's externalized configuration features (e.g., `application.properties` or `application.yml`, environment variables).
- Avoid hardcoding sensitive information; use environment variables or a secure vault solution.

## 7. Logging

- Implement consistent logging practices using a logging framework (e.g., SLF4J with Logback).
- Configure log levels appropriately for different environments.
