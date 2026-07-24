# Git & Collaboration Conventions

> **Single source of truth — do not duplicate here.**
> The Conventional Commits format, trunk-based branching, atomic-commit rules, PR etiquette,
> authorship rule, and ADR guidance are maintained, project-agnostic, in the shared
> [`cyrillrx/coding-conventions`](https://github.com/cyrillrx/coding-conventions) repository.
>
> 📖 **Read the canonical document:**
> <https://github.com/cyrillrx/coding-conventions/blob/main/collaboration/git-and-collaboration.md>
>
> Code review emoji legend:
> <https://github.com/cyrillrx/coding-conventions/blob/main/collaboration/code-review-emojis.md>

Only the project-specific bindings below differ.

## Project-specific additions

### Commit scopes

Use short, consistent scopes matching this repository's structure, e.g.
`project`, `compose-app`, `server-rust`, `server-go`, `bruno-api`, `agents`, `prd`, `spell`, `campaign`.

AI/agent configuration changes use the `docs(agents)` scope (see [`AGENTS.md`](../../AGENTS.md)).

### CI pipeline

CI is split per component under [`.github/workflows/`](../../.github/workflows/):
[`ci-kmp.yml`](../../.github/workflows/ci-kmp.yml) (KMP client — JVM tests via `./gradlew jvmTest`
in `cmp-ttrpg-companion/`), [`ci-server-rust.yml`](../../.github/workflows/ci-server-rust.yml),
[`ci-server-go.yml`](../../.github/workflows/ci-server-go.yml),
[`ci-bruno.yml`](../../.github/workflows/ci-bruno.yml), and
[`ci-compendium.yml`](../../.github/workflows/ci-compendium.yml). The relevant checks must pass
for a PR to be mergeable.

### ADRs

ADRs live in [`docs/adr/`](../adr/). Beyond the canonical triggers, this project requires an ADR for:

- Compendium source format or schema changes
- Data distribution format changes (JSON structure, API contracts)
- Database schema changes with migration implications
