# ADR-002: Compendium Source Format — YAML per Entity with Markdown Descriptions

**Status**: Accepted  
**Date**: 2026-05-04  
**Context**: Improving contributor ergonomics before the compendium editing tool is built.

---

## 1. One YAML File per Entity

### Decision

The compendium source switches from three monolithic JSON files to **one YAML file per entity**, organized in typed subdirectories:

```
data/compendium/
  spells/
    fireball.yaml
    acid-splash.yaml
  monsters/
    goblin.yaml
    aarakocra-aeromancer.yaml
  magical-items/
    adamantine-armor.yaml
```

A **build script** (`scripts/build_compendium.py`) aggregates these files back into the three JSON files consumed by the KMP app and the Rust server. The JSON files remain in the repository as generated artifacts so that the app and server require no changes.

### Field naming

YAML keys use **camelCase**, identical to the JSON output, to keep a 1:1 mapping and avoid translation errors in the build script.

### Rationale

- **Git ergonomics**: adding or editing an entity produces a diff scoped to a single file. Merge conflicts on the monolithic JSON files were a persistent friction point.
- **Discoverability**: `find data/compendium/spells/ -name "*.yaml"` lists every spell. Searching within a 3 MB JSON file is not.
- **Tool-readiness**: one file per entity is the natural unit for a future CRUD editing tool.
- **YAML over JSON**: YAML supports multiline strings natively (via `|` block scalar), which is essential for readable Markdown descriptions. Comments are also supported for contributor notes.

---

## 2. Markdown Descriptions

### Decision

The `description` field (and any other prose field) in source YAML files uses **GitHub Flavored Markdown (GFM)**. Tables use the GFM pipe syntax.

The build script converts Markdown to HTML for the distributed JSON files (backward compatible with the existing `HtmlText` renderer). In Phase 2 (tracked separately), the Compose renderer will be updated to consume Markdown directly so that tables render natively.

### Rationale

- HTML tables are painful to write and read by hand. GFM tables are significantly easier to edit, especially before a dedicated editing tool exists.
- Markdown is renderable as-is in most editors (VS Code, Obsidian, GitHub) — contributors get a live preview for free.
- Converting Markdown → HTML at build time is trivial and reversible. Converting HTML → Markdown at migration time is a one-off cost.

---

## 3. Build Pipeline

### Decision

```
data/compendium/{type}/*.yaml
    ↓  scripts/build_compendium.py
data/compendium/{spells,monsters,magical-items}.json   ← generated, do not edit
    ↓  copied automatically
cmp-ttrpg-companion/composeApp/src/commonMain/composeResources/files/
```

A CI workflow (`.github/workflows/ci-compendium.yml`) runs the build script on every PR that touches `data/compendium/**/*.yaml` and verifies that the committed JSON files are up to date.

A migration script (`scripts/migrate_json_to_yaml.py`) handles the one-time conversion from the current JSON format to the new YAML source files (HTML → Markdown via `html2text`).

### Rationale

- Keeping the generated JSON in the repo means zero changes to consumers (KMP app, Rust server).
- CI enforcement prevents contributors from forgetting to run the build step.
- The migration script is retained in the repository for traceability, even after it has been run.

---

## 4. Consequences

### Positive

- Editing a single entity no longer requires locating it inside a large file.
- Tables in descriptions are human-readable and editable.
- The future editing tool can read/write individual YAML files without loading the entire compendium.

### Negative / Trade-offs

- A build step is now required before the app or server can pick up data changes. CI enforces this.
- The HTML → Markdown migration is automated but not perfect; manual review of a sample set is required after running the migration script.
- The Compose renderer does not yet render GFM tables. This is addressed in Phase 2 (separate feature branch `feat/compose-markdown-renderer`).

### Out of Scope

- Cross-entity linking — tracked in a future ADR.
- User-authored notes (lore, session logs, character annotations) — separate feature (PRD-004).
- The Compose Markdown renderer update — separate feature branch (`feat/compose-markdown-renderer`).
