# Kotlin Multiplatform & Compose Multiplatform Conventions

This document details the specific architectural patterns, coding conventions, and testing guidelines for the Kotlin Multiplatform (KMP) and Compose Multiplatform (CMP) parts of the project, primarily located in the `cmp-ttrpg-companion` directory.

Refer to `../../AGENTS.md` for project-wide guidelines.

## 1. Tech Stack & Dependency Management

-   **Language**: Kotlin (latest stable version).
-   **Multiplatform**: Kotlin Multiplatform (KMP) targeting Android, iOS, and Desktop (JVM).
-   **UI Framework**: Compose Multiplatform.
-   **Local Database**: SQLDelight.
-   **Dependency Injection**: Manual DI only; no external DI libraries allowed.
-   **Dependency Management**:
    -   Gradle Version Catalogs (`gradle/libs.versions.toml`) + `buildSrc`.
    -   *Never hardcode dependency versions in build scripts.*

## 2. Architecture

The application follows a **Clean Architecture** and **Layered** approach adapted for Kotlin Multiplatform.

### Modules

-   **`shared/core`**: Contains pure Kotlin code (Domain layer, generic utilities) and cross-platform Data layer implementations (e.g., SQLDelight repositories).
-   **`composeApp`**: Contains the Presentation layer using Compose Multiplatform and ViewModels.

### Presentation Layer (MVVM + UDF)

-   Use **Model-View-ViewModel (MVVM)**.
-   Follow **Unidirectional Data Flow (UDF)**:
    -   State flows down: ViewModels expose a single `StateFlow<ViewState>`.
    -   Events flow up: Composables pass user actions to the ViewModel via specific functions (e.g., `viewModel::onActionClicked`).
-   Keep ViewModels platform-agnostic using `lifecycle-viewmodel-compose` from JetBrains.
-   Do not pass ViewModels down the Composable tree. Pass state and lambda callbacks instead.

### Domain & Data Layers

-   **Domain**: Pure Kotlin. Contains Entities and Use Cases/Interactors. Independent of any framework.
-   **Data**: Repositories abstract the data sources. Return Kotlin `Flow` for reactive data streams and `suspend` functions for one-shot operations.

## 3. Coding Conventions

The project enforces code formatting using **ktlint**.

### Kotlin Idioms

-   Favor immutability: use `val` over `var` and immutable collections (`List`, `Set`, `Map`) by default.
-   Use Kotlin Coroutines and Flows for asynchronous programming.
-   Use `require()`, `check()`, and `error()` for preconditions and state validation.
-   Avoid nullable types where possible. Use sealed classes/interfaces for representing exhaustive states (e.g., `Loading`, `Success`, `Error`).
-   **Comments**: Prioritize self-documenting code. Add comments only when the code's intent is not immediately obvious, or to explain *why* a particular approach was taken, rather than *what* the code does.

### Jetpack Compose Guidelines

-   **Naming**: Composable functions returning `Unit` must be `PascalCase` (e.g., `CreateCampaignScreen`). Composables returning a value should be `camelCase`.
-   **Modifiers**: Every Composable should accept a `modifier: Modifier = Modifier` as the first optional parameter.
-   **State Hoisting**: Hoist state to the lowest common parent. Composables should be as stateless as possible.
-   **Previews**: Write `@Preview` functions for all UI components. Use `androidx.compose.ui.tooling.preview.Preview` from `org.jetbrains.compose.ui:ui-tooling-preview` module for Multiplatform previews.
-   **Resources**: Use the generated KMP resources (`Res.string.xxx`, `Res.drawable.xxx`).

## 4. Testing

-   **Unit Tests**: Test pure business logic (Domain layer) and ViewModels in the `commonTest` source set.
-   **Coroutines Testing**: Use `runTest` and inject test dispatchers for predictable coroutine execution.
-   **UI Tests**: Test Compose UI components in isolation (if applicable via Android instrumented tests or Desktop UI tests).
-   Focus on behavior testing rather than implementation details.

## 5. CI & Policies (KMP Specific)

-   **Code Quality**: PRs must pass `ktlintCheck` and the project must build successfully for all targets (`android`, `ios`, `desktop`) on the CI pipeline.
-   **Warnings as Errors**: Treat compiler warnings seriously. Fix them proactively.
-   **Documentation**: Document complex business logic, public APIs, and architectural decisions. Prioritize self-documenting code. Use KDoc for public APIs in shared modules to clearly define their contracts, parameters, return values, and behavior, aiding consumers in understanding how to use the API without delving into implementation details.