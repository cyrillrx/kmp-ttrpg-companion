# TTRPG Companion - Project Guidelines for Contributors

This document serves as the central guide for all contributors (human or AI) to the project.
It outlines overarching principles, project structure, and general development practices. For technology-specific details, please refer to the dedicated specification files linked below.

## 1. Project Overview

The `ttrpg-companion` project aims to provide a comprehensive digital toolkit for tabletop role-playing games. It consists of multiple components:

- **Client Application**: A cross-platform application (Android, iOS, Desktop) for users to manage campaigns, characters, and game resources.
- **Backend Services**: One or more backend services to provide data storage, real-time functionalities, and potentially AI-driven features.
- **API Testing**: Collections for robust API testing using Bruno.

## 2. Project Structure

The project is organized into several key directories:

- `/cmp-ttrpg-companion`: Contains the Kotlin Multiplatform client application code.
- `/spring-server`: Contains the Spring Boot backend server code.
- `/bruno`: Contains API testing collections and environments for Bruno.
- `/rust-backend` (Future): Placeholder for potential Rust backend services.
- `/Resources`: Static assets, documentation, or other shared resources.

## 3. General Principles

### Collaboration & Communication

- **Respectful Interaction**: All interactions should be respectful and constructive.
- **Clear Communication**: Be clear and concise in your communications (code comments, commit messages, pull request descriptions).
- **Ask Questions**: Don't hesitate to ask questions if something is unclear.

### Code Quality & Maintainability

- **Readability**: Write code that is easy to understand and maintain.
- **Consistency**: Adhere to established coding conventions and architectural patterns.
- **Testing**: Write comprehensive tests to ensure correctness and prevent regressions.
- **Documentation**: Document complex logic, public APIs, and architectural decisions.

## 4. Project Guidelines & Conventions

For detailed guidelines on specific parts of the project, please refer to the following documents:

### General Conventions

- **General Coding Conventions**: [`CODING_CONVENTIONS.md`](docs/conventions/CODING_CONVENTIONS.md)
- **Git & CI Conventions**: [`GIT_CONVENTIONS.md`](docs/conventions/GIT_CONVENTIONS.md)

### Technology-Specific Guidelines

- **Client Application (KMP/Compose Multiplatform) Conventions**:
    - [`KMP_CONVENTIONS.md`](docs/conventions/KMP_CONVENTIONS.md)
- **Spring Boot Backend Server Conventions**:
    - [`SPRING_BOOT_CONVENTIONS.md`](docs/conventions/SPRING_BOOT_CONVENTIONS.md)
- **Rust Backend (Future) Conventions**:
    - [`RUST_CONVENTIONS.md`](docs/conventions/RUST_CONVENTIONS.md)
- **API Testing with Bruno Conventions**:
    - [`BRUNO_CONVENTIONS.md`](docs/conventions/BRUNO_CONVENTIONS.md)

## 5. Product & Features

- **Functional Specifications**: [`FUNCTIONAL_SPEC.md`](docs/specs/FUNCTIONAL_SPEC.md)
- **Ideas & Future Features**: [`IDEAS_BOX.md`](docs/specs/IDEAS_BOX.md)
