# Kotlin Multiplatform & Compose Multiplatform Conventions

> **Single source of truth — do not duplicate here.**
> The KMP / Compose Multiplatform conventions (MVVM + UDF architecture, state & event modeling,
> navigation, lifecycle-aware refresh, Compose rules, naming, formatting, testing) are maintained,
> project-agnostic, in the shared
> [`cyrillrx/coding-conventions`](https://github.com/cyrillrx/coding-conventions) repository.
>
> 📖 **Read the canonical document:**
> <https://github.com/cyrillrx/coding-conventions/blob/main/conventions/kmp-conventions.md>

They apply as-is to the `cmp-ttrpg-companion/` client. The project-specific bindings below stay here.

## Project-specific additions

- **Concrete architecture** — module structure (`shared/core`, `composeApp`), package layout
  (`com.cyrillrx.rpg.*`), manual DI, the navigation/router pattern, state shape, and the design
  system are documented in [`AGENTS.md`](../../AGENTS.md) (sections 4–5).
- **Test location** — ViewModel tests live in `composeApp/src/commonTest/`; in-memory fakes follow
  the `RamXxxRepository` / `SampleXxxRepository` naming.
- **CI targets** — PRs must pass `ktlintCheck` and build for all targets (`android`, `ios`, `desktop`).

### E2E tests (Maestro)

Test scenarios per feature are documented in [`../testing/e2e-test-cases.md`](../testing/e2e-test-cases.md).
Automated flows live in `.maestro/flows/`.

Run all flows (requires a connected Android device or emulator):

```bash
cd cmp-ttrpg-companion
./gradlew installDebug
maestro test .maestro/flows/
```
