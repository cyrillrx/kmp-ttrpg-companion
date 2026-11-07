# TTRPG Companion

A digital toolkit for tabletop role-playing game sessions. Designed for Game Masters and players alike, it provides campaign management, character sheets, rules references, and in-session utilities — offline-first, cross-platform.

## Repository Structure

| Directory              | Description                                             |
|------------------------|---------------------------------------------------------|
| `app/`                 | Kotlin Multiplatform client app (Android, iOS, Desktop) |
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
| [`docs/roadmap.md`](docs/roadmap.md)     | Planned phases and feature progress              |
| [`docs/conventions/`](docs/conventions/) | Coding, Git, and technology-specific conventions |
| [`docs/prd/`](docs/prd/)                 | Product Requirement Documents                    |

### Conventions

The conventions are maintained, project-agnostic, in the shared
[`cyrillrx/coding-conventions`](https://github.com/cyrillrx/coding-conventions) repository (the single
source of truth). The files below are thin pointers to it, plus any project-specific bindings.

`Type` shows whether a file is a pure pointer to the canonical doc or a pointer plus project-specific bindings.

| File                                                                    | Type                | Description                                  |
|-------------------------------------------------------------------------|---------------------|----------------------------------------------|
| [`coding-conventions.md`](docs/conventions/coding-conventions.md)       | → pointer           | Clean Code principles (all technologies)     |
| [`git-and-collaboration.md`](docs/conventions/git-and-collaboration.md) | → pointer + project | Commit format, branching, CI policies        |
| [`kotlin-conventions.md`](docs/conventions/kotlin-conventions.md)       | → pointer           | Kotlin style, idioms, testing, multiplatform |
| [`compose-conventions.md`](docs/conventions/compose-conventions.md)     | → pointer + project | Compose UI architecture + E2E                |
| [`rust-conventions.md`](docs/conventions/rust-conventions.md)           | → pointer           | Rust conventions (`server-rust/`)            |
| [`go-conventions.md`](docs/conventions/go-conventions.md)               | → pointer + project | Go conventions (`server-go/`)                |
| [`bruno-conventions.md`](docs/conventions/bruno-conventions.md)         | → pointer           | API testing with Bruno                       |
| [`docs-conventions.md`](docs/conventions/docs-conventions.md)           | → pointer + project | Doc naming, tables, ruleset-scoped content   |

### Product Requirements

| PRD                                              | Feature                                                                                                                                            |
|--------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| [`PRD-000`](docs/prd/prd-000-vision.md)          | Vision, roles, connectivity phases                                                                                                                 |
| [`PRD-001`](docs/prd/prd-001-reference-data.md)  | Reference data — [Spellbook](docs/prd/prd-001a-spellbook.md) · [Items](docs/prd/prd-001b-item-list.md) · [Bestiary](docs/prd/prd-001c-bestiary.md) |
| [`PRD-002`](docs/prd/prd-002-character-sheet.md) | Character sheets                                                                                                                                   |
| [`PRD-003`](docs/prd/prd-003-campaigns.md)       | Campaigns                                                                                                                                          |
| [`PRD-004`](docs/prd/prd-004-notes.md)           | Notes                                                                                                                                              |
| [`PRD-005`](docs/prd/prd-005-generators.md)      | Generators                                                                                                                                         |
| [`PRD-006`](docs/prd/prd-006-dice.md)            | Virtual dice                                                                                                                                       |

## Getting Started

### Client (KMP)

Run from the repository root, where the Gradle build lives:

```bash
./gradlew build
```

#### Android release

The release build is signed with the upload key when these Gradle properties are set:

```properties
ttrpgReleaseStoreFile=/absolute/path/to/upload.jks
ttrpgReleaseStorePassword=...
ttrpgReleaseKeyAlias=...
ttrpgReleaseKeyPassword=...
```

Gradle reads them from `~/.gradle/gradle.properties`, from `-P<name>=<value>`, or from `ORG_GRADLE_PROJECT_<name>` environment variables. Never put them in the repository's `gradle.properties`.

- **CI**: expose the secrets as `ORG_GRADLE_PROJECT_*` environment variables.
- **Local**: keep the store file and the alias in `~/.gradle/gradle.properties`, and the passwords out of any plain-text file, for example in the macOS Keychain:

```bash
security add-generic-password -s ttrpg-upload-store -a "$USER" -w
export ORG_GRADLE_PROJECT_ttrpgReleaseStorePassword="$(security find-generic-password -s ttrpg-upload-store -w)"
```

Each Play upload needs a higher version code, passed with `-PttrpgVersionCode=<n>`. Without these properties, `./gradlew :app:androidApp:bundleRelease` still builds, unsigned and with version code 1.

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
