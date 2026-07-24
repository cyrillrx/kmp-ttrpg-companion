# TTRPG Companion

A digital toolkit for tabletop role-playing game sessions. Designed for Game Masters and players alike, it provides campaign management, character sheets, rules references, and in-session utilities — offline-first, cross-platform.

## Repository Structure

| Directory              | Description                                             |
|------------------------|---------------------------------------------------------|
| `cmp-ttrpg-companion/` | Kotlin Multiplatform client app (Android, iOS, Desktop) |
| `server-rust/`         | Rust backend server (Axum, REST API)                    |
| `server-go/`           | Go backend server (chi, REST API)                       |
| `data/`                | Shared compendium data (JSON files used by servers)     |
| `bruno/`               | API testing collections (Bruno)                         |
| `docs/`                | Project documentation (conventions, PRDs, specs)        |
| `Resources/`           | Shared static assets                                    |

## Tech Stack

| Component     | Stack                                                                  |
|---------------|------------------------------------------------------------------------|
| **Client**    | Kotlin Multiplatform · Compose Multiplatform · SQLDelight · Coroutines |
| **Server**    | Rust · Axum · Go · chi                                                 |
| **API Tests** | Bruno                                                                  |

## Documentation

| Document                                 | Description                                      |
|------------------------------------------|--------------------------------------------------|
| [`AGENTS.md`](AGENTS.md)                 | Central contributor guide — start here           |
| [`docs/ROADMAP.md`](docs/ROADMAP.md)     | Planned phases and feature progress              |
| [`docs/conventions/`](docs/conventions/) | Coding, Git, and technology-specific conventions |
| [`docs/prd/`](docs/prd/)                 | Product Requirement Documents                    |

### Conventions

The conventions are maintained, project-agnostic, in the shared
[`cyrillrx/coding-conventions`](https://github.com/cyrillrx/coding-conventions) repository (the single
source of truth). The files below are thin pointers to it, plus any project-specific bindings.

| File                                                                    | Description                                          |
|-------------------------------------------------------------------------|------------------------------------------------------|
| [`coding-conventions.md`](docs/conventions/coding-conventions.md)       | Clean Code principles (all technologies)             |
| [`git-and-collaboration.md`](docs/conventions/git-and-collaboration.md) | Commit format, branching, CI policies                |
| [`kmp-conventions.md`](docs/conventions/kmp-conventions.md)             | KMP/Compose Multiplatform architecture + E2E         |
| [`rust-conventions.md`](docs/conventions/rust-conventions.md)           | Rust conventions (`server-rust/`)                    |
| [`go-conventions.md`](docs/conventions/go-conventions.md)               | Go conventions (`server-go/`)                        |
| [`bruno-conventions.md`](docs/conventions/bruno-conventions.md)         | API testing with Bruno                               |
| [`docs-conventions.md`](docs/conventions/docs-conventions.md)           | Doc naming, Markdown tables, ruleset-scoped content  |

### Product Requirements

| PRD                                              | Feature                                                                                                                                            |
|--------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| [`PRD-000`](docs/prd/PRD-000-VISION.md)          | Vision, roles, connectivity phases                                                                                                                 |
| [`PRD-001`](docs/prd/PRD-001-REFERENCE-DATA.md)  | Reference data — [Spellbook](docs/prd/PRD-001a-SPELLBOOK.md) · [Items](docs/prd/PRD-001b-ITEM-LIST.md) · [Bestiary](docs/prd/PRD-001c-BESTIARY.md) |
| [`PRD-002`](docs/prd/PRD-002-CHARACTER-SHEET.md) | Character sheets                                                                                                                                   |
| [`PRD-003`](docs/prd/PRD-003-CAMPAIGNS.md)       | Campaigns                                                                                                                                          |
| [`PRD-004`](docs/prd/PRD-004-NOTES.md)           | Notes                                                                                                                                              |
| [`PRD-005`](docs/prd/PRD-005-GENERATORS.md)      | Generators                                                                                                                                         |
| [`PRD-006`](docs/prd/PRD-006-DICE.md)            | Virtual dice                                                                                                                                       |

## Getting Started

### Client (KMP)

```bash
cd cmp-ttrpg-companion
./gradlew build
```

### Server (Rust)

```bash
cd server-rust
cargo run
```

> Listens on port `3000` by default. Set `DATA_DIR` to override the path to `data/compendium/`.

### Server (Go)

```bash
cd server-go
go run cmd/server/main.go
```

> Listens on port `8080` by default. Set `DATA_DIR` to override the path to `data/compendium/`.

### API Tests (Bruno)

Open the `bruno/` directory in the [Bruno](https://www.usebruno.com/) app and select the appropriate environment (`local` or `production`).
