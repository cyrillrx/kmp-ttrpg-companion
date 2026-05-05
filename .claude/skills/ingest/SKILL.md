---
name: ingest
description: Ingest new compendium entities from an external source file (JSON or YAML) into the normalized YAML format
argument-hint: <entity-type> <source-file>
allowed-tools:
  - Bash(python3 scripts/build_compendium.py*)
  - Bash(ls data/compendium/*)
  - Bash(gh repo view:*)
  - Read
  - Edit
  - Write
---

## Context

- Arguments: $ARGUMENTS
- Current branch: !`git branch --show-current`

## Your task

Follow these steps in order.

### Step 1 — Parse arguments

Extract from `$ARGUMENTS`:
- `<entity-type>`: one of `spell`, `monster`, `magical-item`
- `<source-file>`: path to the source file to ingest (JSON or YAML)

If either is missing, ask the user before proceeding.

### Step 2 — Read the ingestion guide

Read the relevant guide to understand the expected schema, field names, enum values, and validation rules:

| Entity type   | Guide |
|---------------|-------|
| `spell`       | `docs/data/srd-spell-ingestion.md` |
| `monster`     | `docs/data/srd-monster-ingestion.md` |
| `magical-item`| `docs/data/srd-magical-item-ingestion.md` |

Also read `docs/data/srd-fr-en.md` for enum mappings (schools, classes, types, rarities, etc.).

### Step 3 — Read and parse the source file

Read `<source-file>`. It may contain one entity or a list.

Detect the format:
- **JSON**: parse as object or array
- **YAML**: parse as mapping or sequence

### Step 4 — Normalize and triage

For each entity, map the source fields to the normalized schema defined in the ingestion guide.

Then present a triage table:

| # | id (proposed) | Name | Issues |
|---|---------------|------|--------|
| 1 | `slug` | Entity name | ⚠️ missing EN translation / ⚠️ unknown enum value / ✅ none |
| … | | | |

**Flag as ⚠️ when:**
- A required field is missing or null
- An enum value is unrecognized (school, type, rarity, class, etc.)
- The proposed `id` already exists in `data/compendium/<type>/`
- Only one locale is present (both `en` and `fr` are required)
- The `id` is not a valid slug (lowercase, hyphens only, English)

For each ⚠️, propose a resolution and ask the user to confirm before writing.

### Step 5 — Write YAML files

For each validated entity, write a YAML file to `data/compendium/<type>/<id>.yaml`.

Follow the style conventions:
- All keys in camelCase
- Multiline strings use block scalar style (`|`)
- Null values written explicitly as `null`
- No trailing whitespace

Do not overwrite an existing file without explicit user confirmation.

### Step 6 — Build and verify

Run the build script to regenerate the JSON and verify the round-trip:

```bash
python3 scripts/build_compendium.py
python3 scripts/build_compendium.py --check
```

If `--check` fails, show the error and stop. Do not proceed to git steps until the check passes.

### Step 7 — Ask for git permission

**Do NOT run any git commit, push, or rebase commands without explicit user approval.**

Summarise what was written (number of entities, any skipped), then ask:
> "May I commit and push these changes? Branch: `<branch>`, suggested commit message: `<conventional-commit-message>`"

Use commit type `feat(data)` for new entities, `fix(data)` for corrections.

Wait for approval before proceeding.

### Step 8 — Commit and push

Once approved, stage and commit the new YAML files and the regenerated JSON, then push to the current branch.
