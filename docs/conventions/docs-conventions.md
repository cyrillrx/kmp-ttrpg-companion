# Documentation Conventions

> [!IMPORTANT]
> **Canonical source of truth** (shared, project-agnostic): [`conventions/docs-conventions.md`](https://github.com/cyrillrx/coding-conventions/blob/main/conventions/docs-conventions.md) — do not duplicate here.

Generic file-naming and Markdown-table rules live in the canonical document. The additions below are
specific to this project's game-ruleset content and stay here.

## Project-specific additions

### Ruleset-scoped docs

Files in `docs/data/` are scoped to a game ruleset using a filename prefix.

- Prefix filenames with the ruleset slug: `srd-`, `7th-sea-`, etc. (e.g. `srd-monster-ingestion.md`).
- `srd` covers the entire 5e-compatible family (D&D 2014, D&D 2024, Fateforge, Héros & Dragons, Role n Play, etc.).
- For a completely different system, use a distinct slug (e.g. `7th-sea-`, `pathfinder-`).
- When a ruleset accumulates 10 or more files, move them into a dedicated subdirectory (e.g. `docs/data/srd/`).

### `docs/rules/` naming

Files in `docs/rules/` name the **game system**, not the data source. Pattern: `{game-system}.md`.

Examples: `5e-srd.md` (5e-compatible core rules), `7th-sea.md`.

The ruleset prefix does NOT apply here — it is reserved for data content in `docs/data/`.
