# TTRPG Companion

A digital toolkit for tabletop role-playing game sessions. Designed for Game Masters and players alike, it provides campaign management, character sheets, rules references, and in-session utilities — offline-first, cross-platform.

## Repository Structure

| Directory              | Description                                             |
|------------------------|---------------------------------------------------------|
| `cmp-ttrpg-companion/` | Kotlin Multiplatform client app (Android, iOS, Desktop) |
| `spring-server/`       | Spring Boot backend server (REST API, PostgreSQL)       |
| `bruno/`               | API testing collections (Bruno)                         |
| `docs/`                | Project documentation (conventions, PRDs, specs)        |
| `Resources/`           | Shared static assets                                    |

## Tech Stack

| Component     | Stack                                                                  |
|---------------|------------------------------------------------------------------------|
| **Client**    | Kotlin Multiplatform · Compose Multiplatform · SQLDelight · Coroutines |
| **Server**    | Kotlin · Spring Boot · PostgreSQL · Spring Data JPA                    |
| **API Tests** | Bruno                                                                  |

## Documentation

| Document                                 | Description                                      |
|------------------------------------------|--------------------------------------------------|
| [`AGENTS.md`](AGENTS.md)                 | Central contributor guide — start here           |
| [`docs/conventions/`](docs/conventions/) | Coding, Git, and technology-specific conventions |
| [`docs/prd/`](docs/prd/)                 | Product Requirement Documents                    |

### Conventions

| File                                                                        | Description                                |
|-----------------------------------------------------------------------------|--------------------------------------------|
| [`CODING_CONVENTIONS.md`](docs/conventions/CODING_CONVENTIONS.md)           | Clean Code principles (all technologies)   |
| [`GIT_AND_COLLABORATION.md`](docs/conventions/GIT_AND_COLLABORATION.md)     | Commit format, branching, CI policies      |
| [`KMP_CONVENTIONS.md`](docs/conventions/KMP_CONVENTIONS.md)                 | KMP/Compose Multiplatform architecture     |
| [`SPRING_BOOT_CONVENTIONS.md`](docs/conventions/SPRING_BOOT_CONVENTIONS.md) | Spring Boot architecture and Kotlin idioms |
| [`RUST_CONVENTIONS.md`](docs/conventions/RUST_CONVENTIONS.md)               | Rust conventions (future)                  |
| [`BRUNO_CONVENTIONS.md`](docs/conventions/BRUNO_CONVENTIONS.md)             | API testing with Bruno                     |

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

### Server (Spring Boot)

```bash
cd spring-server
./gradlew bootRun
```

> Requires a running PostgreSQL instance. Configure connection in `application.yml`.

### API Tests (Bruno)

Open the `bruno/` directory in the [Bruno](https://www.usebruno.com/) app and select the appropriate environment (`local` or `production`).
