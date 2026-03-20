# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository structure

This is a monorepo with two active components:

- `cmp-ttrpg-companion/` — Kotlin Multiplatform client (Android, iOS, Desktop)
- `bruno/` — Bruno API test collections

All conventions are in `docs/conventions/`. Product requirements are in `docs/prd/`. The project vision and role model start at `docs/prd/PRD-000-VISION.md`.

## KMP Client (`cmp-ttrpg-companion/`)

### Commands

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

### Architecture

Two Gradle modules:

**`shared/core`** — Domain + Data layers. Pure Kotlin, no UI framework.
- `domain/` — entities, repository interfaces, filters
- `data/` — repository implementations (JSON, RAM, SQLDelight)
- Platform-specific: `DatabaseDriverFactory` (Android/iOS/JVM SQLite drivers)

**`composeApp`** — Presentation layer only.
- `core/presentation/` — shared components, theme, navigation utilities
- `{feature}/presentation/` — ViewModels, screens, routers per feature

### Key patterns

**Manual DI** — No Hilt/Koin. Repositories and routers are instantiated inside `NavGraphBuilder` extension functions and injected via `ViewModelFactory`:

```kotlin
// In handleSpellRoutes(navController, fileReader):
composable<SpellRoute.List> {
    val router = SpellRouterImpl(navController)
    val factory = SpellBookViewModelFactory(router, JsonSpellRepository(fileReader))
    val viewModel = viewModel<SpellBookViewModel>(factory = factory)
    SpellListScreen(viewModel, router)
}
```

**Navigation** — `App.kt` is the `NavHost` root. Each feature registers its routes via a `NavGraphBuilder` extension function (`handleSpellRoutes`, `handleCampaignRoutes`, etc.). Routes are `@Serializable` objects/data classes. Complex objects are passed as serialized strings using `serialize()`/`deserialize()` helpers from `shared/core/src/commonMain/kotlin/com/cyrillrx/core/data/Serializer.kt`.

**Router pattern** — Each feature defines a `{Feature}Router` interface and a `{Feature}RouterImpl(navController)`. The interface is injected into ViewModels; the impl lives in the navigation layer.

**State** — `StateFlow` + sealed `Body` interface (`Loading`, `Empty`, `WithData`, `Error`) per screen.

**Stateless composables** — Each screen has two overloads: one taking `ViewModel + Router` (runtime), one taking `State + callbacks` (previews).

**Previews** — Always provide light and dark variants using `AppThemePreview(darkTheme = false/true)`.

### Package structure

```
com.cyrillrx.rpg.{feature}.domain     # entities, repository interfaces
com.cyrillrx.rpg.{feature}.data       # repository implementations
com.cyrillrx.rpg.{feature}.presentation.viewmodel
com.cyrillrx.rpg.{feature}.presentation.component
com.cyrillrx.rpg.{feature}.presentation.navigation  # {Feature}Route, {Feature}Router
```

Active features: `home`, `campaign`, `spell`, `creature`, `magicalitem`, `character`.

## AI Co-authorship

Do not add AI co-author tags (e.g. `Co-Authored-By: Claude`) to commits or pull requests.

## Git conventions

Conventional Commits format: `type(scope): description`

Common types: `feat`, `fix`, `refactor`, `style`, `docs`, `test`, `chore`
Common scopes: `home`, `campaign`, `spell`, `bestiary`, `item`, `character`, `core`, `agents`, `roadmap`, `prd`

Branch naming mirrors commit types: `feat/`, `fix/`, `docs/`, `chore/`, etc.

See `docs/conventions/GIT_AND_COLLABORATION.md` for the full branching strategy.
