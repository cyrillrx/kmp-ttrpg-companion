# Git & Collaboration Conventions

> [!IMPORTANT]
> **Canonical source of truth** (shared, project-agnostic): [`collaboration/git-and-collaboration.md`](https://github.com/cyrillrx/coding-conventions/blob/main/collaboration/git-and-collaboration.md) — do not duplicate here.

Conventional Commits, trunk-based branching, atomic commits, PR etiquette, the authorship rule, ADR
guidance, and the [code review emoji legend](https://github.com/cyrillrx/coding-conventions/blob/main/collaboration/code-review-emojis.md)
all live in the canonical document. Only the project-specific bindings below differ.

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
