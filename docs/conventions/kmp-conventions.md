# Kotlin Multiplatform & Compose Multiplatform Conventions

This document details the specific architectural patterns, coding conventions, and testing guidelines for the Kotlin Multiplatform (KMP) and Compose Multiplatform (CMP) parts of the project, primarily located in the `cmp-ttrpg-companion` directory.

Refer to `../../AGENTS.md` for project-wide guidelines.

## 1. Tech Stack & Dependency Management

- **Language**: Kotlin (latest stable version).
- **Multiplatform**: Kotlin Multiplatform (KMP) targeting Android, iOS, and Desktop (JVM).
- **UI Framework**: Compose Multiplatform.
- **Local Database**: SQLDelight.
- **Dependency Injection**: Manual DI only; no external DI libraries allowed.
- **Dependency Management**:
    - Gradle Version Catalogs (`gradle/libs.versions.toml`) + `buildSrc`.
    - *Never hardcode dependency versions in build scripts.*

## 2. Architecture

The application follows a **Clean Architecture** and **Layered** approach adapted for Kotlin Multiplatform.

### Modules

- **`shared/core`**: Contains pure Kotlin code (Domain layer, generic utilities) and cross-platform Data layer implementations (e.g., SQLDelight repositories).
- **`composeApp`**: Contains the Presentation layer using Compose Multiplatform and ViewModels.

### Presentation Layer (MVVM + UDF)

- **Use Model-View-ViewModel (MVVM)**.
- **Follow Unidirectional Data Flow (UDF)**:
    - State flows down: ViewModels expose a single `StateFlow<ViewState>`.
    - Events flow up: Composables pass user actions to the ViewModel via lambda callbacks (e.g., `onCreateCampaignClicked = viewModel::openCreateCampaign`).
- **Keep ViewModels platform-agnostic** using `lifecycle-viewmodel-compose` from JetBrains.
- **Do not pass ViewModels down the Composable tree**. Pass state and lambda callbacks instead.

### ViewModel Method Naming

ViewModel public methods must reflect **behavior** (what the app does), not the UI event that triggered them. Composable callback parameters keep the UI-event convention because they are generic lambda slots.

| Context                       | Convention                     | Examples                                           |
|-------------------------------|--------------------------------|----------------------------------------------------|
| Composable callback parameter | `onXxxClicked`, `onXxxChanged` | `onCreateCampaignClicked`, `onSearchQueryChanged`  |
| ViewModel public method       | Functional verb phrase         | `openCreateCampaign`, `filterByQuery`, `silentRefresh` |

```kotlin
// ✅ Correct — callback param name ≠ ViewModel method name
fun CampaignListScreen(viewModel: CampaignListViewModel, router: CampaignRouter) {
    CampaignListScreen(
        onSearchQueryChanged = viewModel::filterByQuery,
        onCampaignClicked = viewModel::openCampaignDetail,
        onCreateCampaignClicked = viewModel::openCreateCampaign,
    )
}
```

### ViewModel State Shape

Every ViewModel exposes a single `StateFlow<XxxState>`. The shape of `XxxState` depends on the screen type.

**List / read-only screens** — use a data class with a sealed `Body` field:

```kotlin
data class SpellListState(val body: Body = Body.Loading, ...) {
    sealed interface Body {
        data object Loading : Body
        data object Empty : Body
        data class WithData(val spells: List<Spell>) : Body
        data class Error(val message: String) : Body
    }
}
```

**Edit / detail screens** — use a top-level sealed interface whose states model the data-fetch lifecycle:

```kotlin
sealed interface CharacterEditState {
    data object Loading : CharacterEditState
    data class NotFound(val id: String) : CharacterEditState
    data class Body(...) : CharacterEditState {
        sealed interface EditingField { ... }
        companion object { fun from(entity: Entity): Body = ... }
    }
}
```

- `Loading` — initial state set synchronously before the repository call.
- `NotFound` — set when the repository returns `null`; carries the ID for error display.
- `Body` — the active, mutable state of the screen; the only state that accepts user interactions.

The screen's ViewModel-taking overload uses an exhaustive `when` on the sealed state:

```kotlin
when (val s = viewModel.state.collectAsStateWithLifecycle().value) {
    CharacterEditState.Loading -> Loader(...)
    is CharacterEditState.NotFound -> ErrorLayout(stringResource(Res.string.character_not_found, s.id), ...)
    is CharacterEditState.Body -> CharacterDetailScreen(state = s, ...)
}
```

The screen's stateless overload takes `XxxState.Body` directly.

### Domain & Data Layers

- **Domain**: Pure Kotlin. Contains Entities and Use Cases/Interactors. Independent of any framework.
- **Data**: Repositories abstract the data sources. Return Kotlin `Flow` for reactive data streams and `suspend` functions for one-shot operations.

## 3. Coding Conventions

The project enforces code formatting using **ktlint**.

### Kotlin Idioms

- Favor immutability: use `val` over `var` and immutable collections (`List`, `Set`, `Map`) by default.
- Use Kotlin Coroutines and Flows for asynchronous programming.
- Use `require()`, `check()`, and `error()` for preconditions and state validation.
- Avoid nullable types where possible. Use sealed classes/interfaces for representing exhaustive states (e.g., `Loading`, `Success`, `Error`).

### Jetpack Compose Guidelines

- **Naming**: Composable functions returning `Unit` must be `PascalCase` (e.g., `CreateCampaignScreen`). Composables returning a value should be `camelCase`.
- **Modifiers**: Every Composable should accept a `modifier: Modifier = Modifier` as the first optional parameter.
- **State Hoisting**: Hoist state to the lowest common parent. Composables should be as stateless as possible.
- **Previews**: Write `@Preview` functions for all UI components. Use `androidx.compose.ui.tooling.preview.Preview` from `org.jetbrains.compose.ui:ui-tooling-preview` module for Multiplatform previews.
- **Resources**: Use the generated KMP resources (`Res.string.xxx`, `Res.drawable.xxx`).

### Lifecycle-aware Refresh

Screens that display mutable data (persisted in a repository) must refresh when the user returns to the screen via `LifecycleEventEffect(Lifecycle.Event.ON_RESUME)`. Use `silentRefresh()` — never the full load function — to avoid a flicker when data is already visible.

**ViewModel** — every ViewModel with `ON_RESUME` refresh must expose two distinct operations:

| Method                     | Sets `Loading` | Purpose                                                                                           |
|----------------------------|----------------|---------------------------------------------------------------------------------------------------|
| `loadXxx()` (private)      | ✅ Yes         | Initial load from `init`, or after an explicit user action (search query change, pull-to-refresh) |
| `silentRefresh()` (public) | ❌ No          | Called by the screen on `ON_RESUME`; no-ops when state is already `Loading`                       |

```kotlin
fun silentRefresh() {
    if (state.value.body is XxxState.Body.Loading) return
    viewModelScope.launch {
        try {
            fetchAndUpdate()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // Keep existing state on refresh failure
        }
    }
}
```

**Screen** — wire `silentRefresh()` to `ON_RESUME`:

```kotlin
LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
    viewModel.silentRefresh()
}
```

Never call a load function (e.g., `onSearchQueryChanged`) from `ON_RESUME`: the `init` block already handles the initial load, so doing so causes a redundant double fetch and an unnecessary `Loading` flash on every back-navigation.

## 4. Testing

### Rules

- Every ViewModel must have a test file in `composeApp/src/commonTest/`.
- Every new public method on an existing ViewModel must be covered by at least one test.
- Every bug fix must be accompanied by a regression test that fails before the fix and passes after.

### ViewModel tests — required cases

Use `StandardTestDispatcher` + `runTest`. Inject repositories via constructor; use in-memory fakes (`RamUserListRepository`, `SampleXxxRepository`).

| Case                                    | Description                                              |
|-----------------------------------------|----------------------------------------------------------|
| Initial `Loading` state                 | Before coroutines have run                               |
| `Error` state                           | When the repository throws                               |
| Happy path `WithData`                   | Data loaded and displayed correctly                      |
| `silentRefresh` reflects repo changes   | After mutating the repo, `silentRefresh()` updates state |
| `silentRefresh` does not show `Loading` | State does not regress to `Loading` during refresh       |
| `silentRefresh` no-op when `Loading`    | Early call has no effect                                 |

For ViewModels with mutations (delete, rename, add): test optimistic mutation, undo, commit, and repository persistence.

### Domain tests

- Every filter predicate → `{Feature}FilterTest`, `{Feature}MatchesFilterTest`
- Every bulk filter operation → `{Feature}ApplyFilterTest`
- Every pure extension function → dedicated unit test

### E2E tests (Maestro)

Test scenarios per feature are documented in [`docs/testing/e2e-test-cases.md`](../../docs/testing/e2e-test-cases.md).
Automated flows live in `.maestro/flows/`.

Run all flows (requires a connected Android device or emulator):

```bash
cd cmp-ttrpg-companion
./gradlew installDebug
maestro test .maestro/flows/
```

### What does NOT need tests

- Pure layout composables — covered by Compose previews
- `RouterImpl` — trivial delegation, covered indirectly by ViewModel tests

## 5. CI & Policies

Refer to [`git-and-collaboration.md`](git-and-collaboration.md) for general CI policies (warnings as errors, PR requirements, security scans).

KMP-specific CI requirements:
- PRs must pass `ktlintCheck` and the project must build successfully for all targets (`android`, `ios`, `desktop`).
- Use KDoc for public APIs in shared modules to clearly define their contracts.
