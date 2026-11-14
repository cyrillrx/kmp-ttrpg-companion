# ADR-008: Freeze the Go and Rust servers; a self-hosted backend runs on Ktor

> **Status**: Accepted | **Date**: 2026-11-14 | **Context**: roadmap review ([PRD-000](../prd/prd-000-vision.md)): the compendium stays on the device and the backend is refocused on accounts, sync and sharing

## Decision

The Go and Rust servers are frozen: they get no new feature. Any self-hosted server is written in Ktor, in a `server/` Gradle module of the root build, and shares its exchange types with the app.

- How accounts, sync and sharing are backed is decided in its own ADR when the first online feature starts: a self-hosted Ktor server with its own database, or a managed backend such as Firebase. Nothing is ported before that choice.
- If that ADR picks self-hosting, the Ktor server first takes over the compendium API (`/compendium/spells`, `/compendium/monsters`, `/compendium/magical-items`) in a format settled by the open questions below. The port adds format assertions to the Bruno collection, which today only checks the status and a non-empty array, and moves `ci-bruno.yml` from `server-rust` to the Ktor server. `server-go/` and `server-rust/` are deleted once it is done.
- If it picks a managed backend, `server-go/` and `server-rust/` are deleted without a port.
- The exchange format, today the `internal` `Api*` types of `app/shared/domain` (`ApiSpell`, `ApiMonster`, `ApiMagicalItem`), moves to a dedicated module outside `app/`, which the app and any `server/` depend on. Its folder (`core/` or `shared/`) and its name (`:model`, `:contract` or `:api`) are settled in the pull request that extracts it.
- None of this is a prerequisite of the local roadmap.

## Context

The repository holds two backend servers, `server-go/` (chi) and `server-rust/` (Axum). They were written as an experiment, to compare the two stacks, and the app consumes neither. Both were deployed on Railway until the free trial expired; nothing runs today. A NAS is planned to host the author's apps but is not set up yet: the Go or Rust server may be redeployed there to check the API in the meantime.

The roadmap review keeps the whole compendium on the device and refocuses the backend on user accounts, sync and sharing. The compendium API is kept, unused by the app; the entry export format likely starts from the bundled `Api*` format instead (see the open questions below and [ADR-001](adr-001-data-model.md)).

## Rationale

### Why Ktor

The client is Kotlin Multiplatform. A Kotlin server reuses the app's exchange types instead of redeclaring them in Go or Rust, so a format change is made once and checked by the compiler on both sides. It also leaves a single language, build and toolchain to maintain.

### Why a dedicated module for the exchange format

The `Api*` types are `internal` to `app/shared/domain`, and depending on that module would pull SQLDelight, its drivers and the repositories into the server. A module holding only the exchange format gives the server what it needs and nothing else, and the entry export reuses the same types.

### Why the port waits for the backend choice

The app consumes no server today, so a port brings no user value on its own, and it would be lost if a managed backend wins. Keeping the frozen servers costs almost nothing: their CI only runs when their files change. The part with immediate value, the shared exchange-format module, does not depend on the server: the entry export justifies it.

### Why port the compendium API first, if self-hosted

It is the only API both servers expose and the only one with a format other documents rely on. Porting it proves the Ktor setup and the shared module on a known surface, before any online feature depends on them.

## Consequences

- The shared exchange-format module, and with self-hosting a `server/` module, join the Gradle build; CI builds and tests them with the rest.
- When the Go and Rust servers are deleted (after the port, or right after a managed-backend choice), these are deleted or updated:
  - `server-go/` and `server-rust/`, including their `railway.toml`; a Ktor server keeps the `/health` route that `ci-bruno.yml` waits on and `bruno/Server/Health.bru` tests;
  - `bruno/Campaign/*.bru`, which targets a `/campaign` endpoint no server implements;
  - `Dockerfile.go` and `Dockerfile.rust`;
  - `.github/workflows/ci-server-go.yml` and `ci-server-rust.yml`;
  - `docs/conventions/go-conventions.md` and `rust-conventions.md`, and their entries in `AGENTS.md` and the README;
  - the `server-rust` and `server-go` commit scopes and CI jobs in `docs/conventions/git-and-collaboration.md`;
  - the `ci-kmp.yml` path filters, which must cover `server/**` and the shared module.
- ADR-001 and ADR-002 are not rewritten: their Rust and Go mentions stay as written, and ADR-001 points to this ADR.
- Until the backend choice, the frozen servers stay in the repository with their CI and Dockerfiles.
- Reversibility: until the deletion, nothing is lost; after it, the Go and Rust servers remain in the git history.

## Alternatives considered

**Keep the Go server**. Rejected: its types are redeclared by hand and drift from the app's; it already drops the `savingThrows` the bundled data carries.

**Keep the Rust server**. Rejected: same drift, and a second toolchain to maintain.

**Keep both servers**. Rejected: the experiment is over; two backends double the cost of every server change.

**Port to Ktor now**. Rejected: no client consumes the API, and the port is lost if a managed backend wins.

## Open questions

- Which format does the Ktor compendium API serve? The Rust and Go servers already disagree (Go drops `savingThrows`, Rust folds them into the abilities), and the `Api*` types follow the bundled JSON. The bundled format changed most recently and is the likely starting point. To settle in the port pull request, if there is one.
- Can the exchange-format module be extracted before the port, for instance by the entry export, and does it also take `ApiCharacter` and `ApiCharacterExport`? To settle in the pull request that extracts the module.
