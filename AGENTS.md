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

## 3. Project Guidelines & Conventions

For detailed guidelines on specific parts of the project, please refer to the following documents:

### Collaboration & Communication

- **Collaboration, Git & CI Conventions**: [`GIT_AND_COLLABORATION.md`](docs/conventions/GIT_AND_COLLABORATION.md)

### Code Quality & Maintainability

- **General Coding Conventions**: [`CODING_CONVENTIONS.md`](docs/conventions/CODING_CONVENTIONS.md)

### Technology-Specific Guidelines

- **Client Application (KMP/Compose Multiplatform) Conventions**:
    - [`KMP_CONVENTIONS.md`](docs/conventions/KMP_CONVENTIONS.md)
- **Spring Boot Backend Server Conventions**:
    - [`SPRING_BOOT_CONVENTIONS.md`](docs/conventions/SPRING_BOOT_CONVENTIONS.md)
- **Rust Backend (Future) Conventions**:
    - [`RUST_CONVENTIONS.md`](docs/conventions/RUST_CONVENTIONS.md)
- **API Testing with Bruno Conventions**:
    - [`BRUNO_CONVENTIONS.md`](docs/conventions/BRUNO_CONVENTIONS.md)

## 4. Product & Features

- **Functional Specifications**: [`FUNCTIONAL_SPEC.md`](docs/specs/FUNCTIONAL_SPEC.md)
- **Ideas & Future Features**: [`IDEAS_BOX.md`](docs/specs/IDEAS_BOX.md)
