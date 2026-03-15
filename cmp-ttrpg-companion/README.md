# TTRPG Companion — Client App

Cross-platform client application for the TTRPG Companion project, built with Kotlin Multiplatform and Compose Multiplatform. Targets Android, iOS, and Desktop (JVM).

For project-wide guidelines, see [`AGENTS.md`](../AGENTS.md).

## Features

- **Campaign Management**: Create and manage your TTRPG campaigns.
- **Character Sheets**: Create and manage character sheets for PCs and NPCs.
- **Spellbook**: Browse, search, and filter the D&D 5e spell catalog.
- **Bestiary**: Browse and manage a bestiary of creatures and NPCs.
- **Item List**: Access and manage magical items and equipment.
- **Virtual Dice**: Roll d4 to d100 with cumulative rolls and history.

## Tech Stack

| Layer                 | Technology                                            |
|-----------------------|-------------------------------------------------------|
| Language              | Kotlin (latest stable)                                |
| UI                    | Compose Multiplatform                                 |
| Architecture          | Clean Architecture · MVVM · Unidirectional Data Flow  |
| Local Database        | SQLDelight                                            |
| Async                 | Kotlin Coroutines · Flow                              |
| Dependency Injection  | Manual DI (no external DI library)                    |
| Dependency Management | Gradle Version Catalogs (`gradle/libs.versions.toml`) |
| Code Formatting       | ktlint                                                |

## Platforms

| Platform | Target              |
|----------|---------------------|
| Android  | `androidMain`       |
| iOS      | `iosMain`           |
| Desktop  | `desktopMain` (JVM) |

## Build & Run

```bash
# Build all targets
./gradlew build

# Run on Desktop
./gradlew desktopRun

# Run Android (requires emulator or device)
./gradlew installDebug

# Check formatting
./gradlew ktlintCheck

# Auto-fix formatting
./gradlew ktlintFormat
```

## Architecture

The app follows Clean Architecture split across two modules:

- **`shared/core`**: Domain layer (entities, use cases) and Data layer (SQLDelight repositories). Pure Kotlin, no framework dependencies.
- **`composeApp`**: Presentation layer — ViewModels and Compose UI screens.

See [`KMP_CONVENTIONS.md`](../docs/conventions/KMP_CONVENTIONS.md) for detailed architectural guidelines.
