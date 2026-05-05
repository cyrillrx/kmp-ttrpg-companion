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

## 2. Description Format — Phase 1: HTML, Phase 2: Markdown

### Phase 1 Decision (current)

Description fields are kept as **HTML** in the YAML source files, identical to the current JSON format. The build script aggregates them unchanged. No conversion is performed.

### Phase 2 Decision (future — tracked separately)

Description fields will be migrated to **GitHub Flavored Markdown (GFM)** when the Compose renderer is updated to consume Markdown directly. At that point, the build script will convert Markdown → HTML for backward compatibility until all clients are updated.

### Rationale

- Automated HTML → Markdown conversion produces artifacts on complex content (notably `<strong><em>` combinations in stat blocks). 507 of 513 monster descriptions are affected, making automated migration unsafe for Phase 1.
- Deferring to Phase 2 keeps the data migration risk-free: round-trip fidelity (YAML → JSON) is exact.
- Markdown descriptions remain a firm goal: GFM tables are significantly easier to edit than HTML tables, especially before a dedicated editing tool exists.

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

The JSON files in `cmp-ttrpg-companion/composeApp/src/commonMain/composeResources/files/` are **symlinks** to `data/compendium/*.json`. Writing to `data/compendium/spells.json` automatically updates the app resources — no copy step is needed. Do not replace these symlinks with regular files.

A migration script (`scripts/migrate_json_to_yaml.py`) handles the one-time conversion from the current JSON format to the new YAML source files. Descriptions are carried over as-is (HTML) — see §2.

### Rationale

- Keeping the generated JSON in the repo means zero changes to consumers (KMP app, Rust server).
- CI enforcement prevents contributors from forgetting to run the build step.
- The migration script is retained in the repository for traceability, even after it has been run.

---

## 4. Consequences

### Positive

- Editing a single entity no longer requires locating it inside a large file.
- The future editing tool can read/write individual YAML files without loading the entire compendium.
- Phase 2: descriptions in GFM Markdown will make tables human-readable and editable directly in source.

### Negative / Trade-offs

- A build step is now required before the app or server can pick up data changes. CI enforces this.
- The HTML → Markdown migration is automated but not perfect; manual review of a sample set is required after running the migration script.
- The Compose renderer does not yet render GFM tables. This is addressed in Phase 2 (separate feature branch `feat/compose-markdown-renderer`).

### Out of Scope

- Cross-entity linking — tracked in a future ADR.
- User-authored notes (lore, session logs, character annotations) — separate feature (PRD-004).
- The Compose Markdown renderer update — separate feature branch (`feat/compose-markdown-renderer`).
