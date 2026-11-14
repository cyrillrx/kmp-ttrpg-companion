# ADR-008: Replace the Go and Rust servers with a Ktor server

> **Status**: Accepted | **Date**: 2026-11-14 | **Context**: roadmap review ([PRD-000](../prd/prd-000-vision.md)): the compendium stays on the device and the backend is refocused on accounts, sync and sharing

## Decision

Server code runs on Ktor. The Go and Rust servers are replaced by a single Ktor server, in a `server/` Gradle module of the root build.

- The exchange format, today the `internal` `Api*` types of `app/shared/domain` (`ApiSpell`, `ApiMonster`, `ApiMagicalItem`), moves to a dedicated module outside `app/`, which both the app and `server/` depend on. Its folder (`core/` or `shared/`) and its name (`:model`, `:contract` or `:api`) are settled in the migration pull request.
- The Ktor server first takes over the compendium API (`/compendium/spells`, `/compendium/monsters`, `/compendium/magical-items`) in a format settled by the open questions below. The port adds format assertions to the Bruno collection, which today only checks the status and a non-empty array, and moves `ci-bruno.yml` from `server-rust` to the Ktor server.
- Once that API is ported, `server-go/` and `server-rust/` are deleted (see Consequences).
- How accounts, sync and sharing are backed is not decided here: a self-hosted Ktor server with its own database, or a managed backend such as Firebase. That choice gets its own ADR when the first online feature starts.
- The migration is not a prerequisite of the local roadmap.

## Context

The repository holds two backend servers, `server-go/` (chi) and `server-rust/` (Axum). They were written as an experiment, to compare the two stacks, and the app consumes neither. Both were deployed on Railway until the free trial expired; nothing runs today. A NAS is planned to host the author's apps but is not set up yet: the Go or Rust server may be redeployed there to check the API before the port.

The roadmap review keeps the whole compendium on the device and refocuses the backend on user accounts, sync and sharing. The compendium API is kept, unused by the app; the entry export format likely starts from the bundled `Api*` format instead (see the open questions below and [ADR-001](adr-001-data-model.md)).

## Rationale

### Why Ktor

The client is Kotlin Multiplatform. A Kotlin server reuses the app's exchange types instead of redeclaring them in Go or Rust, so a format change is made once and checked by the compiler on both sides. It also leaves a single language, build and toolchain to maintain.

### Why a dedicated module for the exchange format

The `Api*` types are `internal` to `app/shared/domain`, and depending on that module would pull SQLDelight, its drivers and the repositories into the server. A module holding only the exchange format gives the server what it needs and nothing else, and the entry export reuses the same types.

### Why port the compendium API first

It is the only API both servers expose and the only one with a format other documents rely on. Porting it proves the Ktor setup and the shared module on a known surface, before any online feature depends on them.

## Consequences

- A `server/` module and the shared exchange-format module join the Gradle build; CI builds and tests them with the rest.
- After the port, these are deleted or updated:
  - `server-go/` and `server-rust/`, including their `railway.toml`; the Ktor server keeps the `/health` route that `ci-bruno.yml` waits on and `bruno/Server/Health.bru` tests;
  - `bruno/Campaign/*.bru`, which targets a `/campaign` endpoint no server implements;
  - `Dockerfile.go` and `Dockerfile.rust`;
  - `.github/workflows/ci-server-go.yml` and `ci-server-rust.yml`;
  - `docs/conventions/go-conventions.md` and `rust-conventions.md`, and their entries in `AGENTS.md` and the README;
  - the `server-rust` and `server-go` commit scopes and CI jobs in `docs/conventions/git-and-collaboration.md`;
  - the `ci-kmp.yml` path filters, which must cover `server/**` and the shared module.
- ADR-001 and ADR-002 are not rewritten: their Rust and Go mentions stay as written, and ADR-001 points to this ADR.
- If the online backend choice goes to a managed backend, `server/` only serves the compendium API, which the app does not use, until it is removed.
- Reversibility: until the deletion, nothing is lost; after it, the Go and Rust servers remain in the git history.

## Alternatives considered

**Keep the Go server**. Rejected: its types are redeclared by hand and drift from the app's; it already drops the `savingThrows` the bundled data carries.

**Keep the Rust server**. Rejected: same drift, and a second toolchain to maintain.

**Keep both servers**. Rejected: the experiment is over; two backends double the cost of every server change.

**No server until the first online feature**. Rejected: porting the known compendium API early validates Ktor and the shared module before an online feature depends on them. The port is not scheduled, so this costs nothing until it happens.

## Open questions

- Which format does the Ktor compendium API serve? The Rust and Go servers already disagree (Go drops `savingThrows`, Rust folds them into the abilities), and the `Api*` types follow the bundled JSON. The bundled format changed most recently and is the likely starting point. To settle in the migration pull request.
- Can the exchange-format module be extracted before the port, for instance by the entry export, and does it also take `ApiCharacter` and `ApiCharacterExport`? To settle in whichever of the migration or the entry export pull request comes first.
