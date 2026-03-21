# TTRPG Companion — Contributor Guide

Central reference for all contributors (human or AI).
For project overview, repository structure, and tech stack, see [`README.md`](README.md).

## 1. Product Requirements

For feature specifications and product decisions, refer to the [Product Requirement Documents (PRDs)](docs/prd/). The overall project vision and role model are defined in [`PRD-000`](docs/prd/PRD-000-VISION.md).

## 2. Project Guidelines and Conventions

### Collaboration and Communication

- **Collaboration, Git & CI Conventions**: [`GIT_AND_COLLABORATION.md`](docs/conventions/GIT_AND_COLLABORATION.md)
- **AI Co-authorship**: Do not add AI co-author tags (e.g. `Co-Authored-By: Claude`) to commits or pull requests.
- **AI/Agent rules**: All rules applying to AI agents must be written in this file (`AGENTS.md`). Agent-specific config files (e.g. `.claude/CLAUDE.md`) must only point to this file — never duplicate or extend rules there.
- **AI/Agent PR labeling**: PRs related to AI agent configuration or rules must use the `docs(agents)` type prefix (e.g. `docs(agents): ...`).

### Code Quality and Maintainability

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

## 3. KMP Client — Commands

All commands run from `cmp-ttrpg-companion/`:

```bash
./gradlew build               # Build all targets
./gradlew desktopRun          # Run on Desktop (JVM)
./gradlew installDebug        # Install on Android
./gradlew test                # Run all tests
./gradlew jvmTest             # Run JVM/Desktop tests only
./gradlew ktlintCheck         # Check formatting
./gradlew ktlintFormat        # Auto-fix formatting
```

ktlint is **strict** in `shared/core` (`ignoreFailures=false`) and permissive in `composeApp` (`ignoreFailures=true`).

## 4. KMP Client — Project-specific patterns

> For full architecture (MVVM/UDF, layer separation, Compose rules), see [`KMP_CONVENTIONS.md`](docs/conventions/KMP_CONVENTIONS.md).

### Module structure

Two Gradle modules:

**`shared/core`** — Domain + Data layers. Pure Kotlin, no UI framework.
- `domain/` — entities, repository interfaces, filters
- `data/` — repository implementations (JSON, RAM, SQLDelight)
- Platform-specific: `DatabaseDriverFactory` (Android/iOS/JVM SQLite drivers)

**`composeApp`** — Presentation layer only.
- `core/presentation/` — shared components, theme, navigation utilities
- `{feature}/presentation/` — ViewModels, screens, routers per feature

### Package structure

```
com.cyrillrx.rpg.{feature}.domain     # entities, repository interfaces
com.cyrillrx.rpg.{feature}.data       # repository implementations
com.cyrillrx.rpg.{feature}.presentation.viewmodel
com.cyrillrx.rpg.{feature}.presentation.component
com.cyrillrx.rpg.{feature}.presentation.navigation  # {Feature}Route, {Feature}Router
```

Active features: `home`, `campaign`, `spell`, `creature`, `magicalitem`, `character`.

### Manual DI

No Hilt/Koin. Repositories and routers are instantiated inside `NavGraphBuilder` extension functions and injected via `ViewModelFactory`:

```kotlin
// In handleSpellRoutes(navController, fileReader):
composable<SpellRoute.List> {
    val router = SpellRouterImpl(navController)
    val factory = SpellBookViewModelFactory(router, JsonSpellRepository(fileReader))
    val viewModel = viewModel<SpellBookViewModel>(factory = factory)
    SpellListScreen(viewModel, router)
}
```

### Navigation

`App.kt` is the `NavHost` root. Each feature registers its routes via a `NavGraphBuilder` extension function (`handleSpellRoutes`, `handleCampaignRoutes`, etc.). Routes are `@Serializable` objects/data classes. Complex objects are passed as serialized strings using `serialize()`/`deserialize()` helpers from the `Serializer.kt` utility in the `shared/core` module.

### Router pattern

Each feature defines a `{Feature}Router` interface and a `{Feature}RouterImpl(navController)`. The interface is injected into ViewModels; the impl lives in the navigation layer.

### State

`StateFlow` + sealed `Body` interface (`Loading`, `Empty`, `WithData`, `Error`) per screen.

### Stateless composables

Each screen has two overloads: one taking `ViewModel + Router` (runtime), one taking `State + callbacks` (previews). Always provide light and dark preview variants using `AppThemePreview(darkTheme = false/true)`.
