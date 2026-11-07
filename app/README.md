# TTRPG Companion — Client App

Cross-platform client application for the TTRPG Companion project, built with Kotlin Multiplatform and Compose Multiplatform. Targets Android, iOS, and Desktop (JVM).

For project-wide guidelines, see [`AGENTS.md`](../AGENTS.md).

## Features

- **Campaign Management**: Create and manage your TTRPG campaigns.
- **Character Sheets**: Create and manage character sheets for PCs and NPCs.
- **Spellbook**: Browse, search, and filter the D&D 5e spell catalog.
- **Bestiary**: Browse and manage a bestiary of monsters.
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

| Platform | Target        |
|----------|---------------|
| Android  | `androidMain` |
| iOS      | `iosMain`     |
| Desktop  | `jvmMain`     |

## Build & Run

Run from the repository root, where the Gradle build lives.

```bash
# Build all targets
./gradlew build

# Run on Desktop
./gradlew :app:desktopApp:run

# Run Android (requires emulator or device)
./gradlew :app:androidApp:installDebug

# Check formatting
./gradlew ktlintCheck

# Auto-fix formatting
./gradlew ktlintFormat
```

iOS builds from `iosApp/iosApp.xcodeproj` in Xcode.

## Modules

| Module            | Contains                                                                                       |
|-------------------|------------------------------------------------------------------------------------------------|
| `shared/domain`   | Domain layer (entities, use cases) and Data layer (SQLDelight repositories). No Compose.       |
| `shared/ui`       | Presentation layer: ViewModels and Compose UI screens. Ships to iOS as the `Shared` framework. |
| `androidApp`      | Android application wrapper.                                                                   |
| `desktopApp`      | JVM application wrapper.                                                                       |
| `baselineprofile` | Macrobenchmarks and Baseline Profile generation for `androidApp`.                              |
| `iosApp`          | Xcode project. Its build phase calls `:app:shared:ui:embedAndSignAppleFrameworkForXcode`.      |

See [`compose-conventions.md`](../docs/conventions/compose-conventions.md) for detailed architectural guidelines.
