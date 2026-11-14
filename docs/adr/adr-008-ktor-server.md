# ADR-008: Replace the Go and Rust servers with a Ktor server

> **Status**: Accepted | **Date**: 2026-11-14 | **Context**: roadmap review ([PRD-000](../prd/prd-000-vision.md)): the compendium stays on the device and the backend is refocused on accounts, sync and sharing

## Decision

The backend is a single Ktor server, in a new `server/` Gradle module of the root build. It depends on `app/shared/domain`, so the server and the app share the same Kotlin models.

- The Ktor server first takes over the compendium API (`/compendium/spells`, `/compendium/monsters`, `/compendium/magical-items`) with the same format, checked by the existing Bruno collection.
- Once that API is ported, `server-go/` and `server-rust/` are deleted, along with their Dockerfiles and their convention pointers.
- The migration is not a prerequisite of any roadmap item: it happens when it fits, and no online feature is built on the Go or Rust servers in the meantime.

## Context

The repository holds two backend servers, `server-go/` (chi) and `server-rust/` (Axum). They were written as an experiment, to compare the two stacks; neither runs in production and the app consumes neither.

The roadmap review keeps the whole compendium on the device and refocuses the backend on user accounts, sync and sharing. The compendium API they both serve is kept, unused by the app: its format is the starting point of the entry export format ([ADR-001](adr-001-data-model.md)).

## Rationale

### Why Ktor

The client is Kotlin Multiplatform. A Kotlin server reuses the domain models of `app/shared/domain` instead of redeclaring them in Go or Rust, so a model change is made once and checked by the compiler on both sides. It also leaves a single language, build and toolchain to maintain.

### Why port the compendium API first

It is the only API both servers expose and the only one with a format other documents rely on. Porting it with the Bruno collection as the acceptance check proves the Ktor setup and the model sharing on a known surface, before any online feature depends on it.

## Consequences

- A `server/` module joins the Gradle build; CI builds and tests it with the rest.
- `server-go/`, `server-rust/`, `Dockerfile.go`, `Dockerfile.rust`, `docs/conventions/go-conventions.md` and `docs/conventions/rust-conventions.md` are deleted after the port, and `AGENTS.md` and the README lose their sections.
- `app/shared/domain` becomes a dependency of the server: a change there can now break the server build.

## Alternatives considered

**Keep the Go server**. Rejected: its models are redeclared by hand and drift from the app's.

**Keep the Rust server**. Rejected: same drift, and a second toolchain to maintain.

**Keep both servers**. Rejected: the experiment is over; two backends double the cost of every online feature.
