# Spring Boot Server Conventions

This document outlines the architectural patterns, coding conventions, and best practices specifically for the Spring Boot server component of the project, located in the `spring-server` directory.

Refer to `../../AGENTS.md` for project-wide guidelines.

## 1. Tech Stack & Dependency Management

-   **Language**: Kotlin.
-   **Framework**: Spring Boot (latest stable version).
-   **Build Tool**: Gradle.
-   **Database**: PostgreSQL.
-   **ORM**: Spring Data JPA with Hibernate.
-   **Dependency Management**: Utilize Gradle for dependency management, ensuring versions are managed consistently (e.g., via Gradle Version Catalogs).

## 2. Architecture

The Spring Boot server will follow a layered architecture, typically including:

-   **Controller Layer**: Handles HTTP requests and responses, exposing RESTful APIs.
-   **Service Layer**: Contains business logic and orchestrates operations.
-   **Repository Layer**: Interacts with the database, abstracting data access.

## 3. Coding Conventions

-   Follow standard Kotlin/Java coding conventions (e.g., official style guides).
-   Use meaningful names for classes, methods, and variables.
-   Favor immutability where appropriate.

## 4. API Design

-   Design RESTful APIs following principles like statelessness, resource-based URLs, and standard HTTP methods.
-   Use clear and consistent naming conventions for API endpoints and request/response bodies.
-   Implement proper error handling with appropriate HTTP status codes and informative error messages.

## 5. Security

-   Implement authentication and authorization mechanisms (e.g., Spring Security with JWT).
-   Ensure sensitive data is handled securely (e.g., encryption, proper storage).
-   Validate all input to prevent common vulnerabilities like SQL injection and XSS.

## 6. Testing

-   **Unit Tests**: Write unit tests for business logic in the Service layer.
-   **Integration Tests**: Write integration tests for controllers and repository interactions using Spring Boot's testing utilities.
-   **End-to-End Tests**: Consider using tools like Bruno (see `BRUNO_CONVENTIONS.md`) for API end-to-end testing.

## 7. Configuration

-   Manage configuration using Spring Boot's externalized configuration features (e.g., `application.properties` or `application.yml`, environment variables).
-   Avoid hardcoding sensitive information; use environment variables or a secure vault solution.

## 8. Logging

-   Implement consistent logging practices using a logging framework (e.g., SLF4J with Logback).
-   Configure log levels appropriately for different environments.
