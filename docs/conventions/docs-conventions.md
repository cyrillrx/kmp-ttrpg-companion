# Documentation Conventions

## File naming

- All documentation files use `lowercase-with-hyphens.md`.
- Exception: well-known tooling files (`README.md`, `AGENTS.md`, `CLAUDE.md`) stay uppercase — they are industry-standard names recognized by GitHub, Claude Code, or other tooling.
- Structured documents keep their lowercase prefix: `prd-001-vision.md`, `adr-001-data-model.md`.

## Ruleset-scoped docs

Files in `docs/data/` are scoped to a game ruleset using a filename prefix.

- Prefix filenames with the ruleset slug: `srd-`, `7th-sea-`, etc. (e.g. `srd-monster-ingestion.md`).
- `srd` covers the entire 5e-compatible family (D&D 2014, D&D 2024, Fateforge, Héros & Dragons, Role n Play, etc.).
- For a completely different system, use a distinct slug (e.g. `7th-sea-`, `pathfinder-`).
- When a ruleset accumulates 10 or more files, move them into a dedicated subdirectory (e.g. `docs/data/srd/`).

## `docs/rules/` naming

Files in `docs/rules/` name the **game system**, not the data source. Pattern: `{game-system}.md`.

Examples: `5e-srd.md` (5e-compatible core rules), `7th-sea.md`.

The ruleset prefix does NOT apply here — it is reserved for data content in `docs/data/`.

## Markdown tables

- Always align table columns with spaces so pipes are vertically aligned.
- Include a separator row (`| --- | --- |`) after the header row.
- Every table cell must have at least one space of padding on each side.

Example:

| Column A    | Column B         |
| ----------- | ---------------- |
| short value | a longer value   |
| another row | yet another cell |
