# Kotlin Multiplatform & Compose Multiplatform Conventions

> [!IMPORTANT]
> **Canonical source of truth** (shared, project-agnostic): [`conventions/kmp-conventions.md`](https://github.com/cyrillrx/coding-conventions/blob/main/conventions/kmp-conventions.md) — do not duplicate here.

The full KMP / Compose Multiplatform conventions (MVVM + UDF, state & event modeling, navigation,
lifecycle-aware refresh, Compose rules, naming, formatting, testing — including in-memory fake naming
and the Maestro E2E approach) live in the canonical document and apply as-is to the
`cmp-ttrpg-companion/` client. Only the concrete bindings below are specific to this project.

## Project-specific additions

- **Concrete architecture** — module structure (`shared/core`, `composeApp`), package layout
  (`com.cyrillrx.rpg.*`), manual DI, the navigation/router pattern, and the design
  system are documented in [`AGENTS.md`](../../AGENTS.md) (sections 4–5).
- **Test location** — ViewModel tests live in `composeApp/src/commonTest/`.
- **E2E flows** — scenarios are documented in [`../testing/e2e-test-cases.md`](../testing/e2e-test-cases.md);
  Maestro flows live in `.maestro/flows/`. Run them with:

  ```bash
  cd cmp-ttrpg-companion
  ./gradlew installDebug
  maestro test .maestro/flows/
  ```
