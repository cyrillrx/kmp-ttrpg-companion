---
name: ingest
description: Ingest new compendium entities from an external source file (JSON, YAML, or HTML) into the normalized YAML format
argument-hint: <entity-type> <source-file>
allowed-tools:
  - Bash(python3 scripts/build_compendium.py*)
  - Bash(ls data/compendium/*)
  - Bash(git branch:*)
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

Detect the format by file extension first, then by content if the extension is absent or ambiguous:

| Extension | Format |
|-----------|--------|
| `.json` | JSON |
| `.yaml`, `.yml` | YAML |
| `.html`, `.htm` | HTML |
| other / none | inspect content: starts with `{` or `[` → JSON, starts with `<` → HTML, otherwise YAML or unknown |

- **JSON**: parse as object or array
- **YAML**: parse as mapping or sequence
- **HTML**: see extraction guidance below
- **Unknown**: if the format still cannot be determined, stop and ask the user before attempting any parsing.

#### HTML extraction guidance

HTML sources vary significantly by origin (D&D Beyond, aidedd.org, homebrew, etc.). For each entity block in the HTML:

1. Identify the repeating structure that delimits one entity (e.g. `<div class="monster">`, `<section>`, `<h2>` headings, etc.)
2. Extract fields by mapping HTML content to schema fields using the ingestion guide as reference:
   - Headings and labels → field names
   - Inline text, `<p>`, `<td>` → field values
   - `<table>` → structured data (speeds, abilities, damage affinities, etc.)
   - `<em>`, `<strong>` → formatting hints, not field boundaries
3. Preserve HTML markup in `description` fields — do not strip tags (descriptions are stored as HTML in Phase 1).
4. If the source locale cannot be determined from the HTML, ask the user.
5. If a field is ambiguous or cannot be reliably extracted, flag it as ⚠️ in the triage table rather than guessing.

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

**Empty description handling (before flagging):**
- If one locale has a description and the other does not — translate from the available locale. Do not flag as ⚠️; resolve it yourself.
- If both locales have an empty description — stop and ask the user for an alternative source (wikidot, D&D Beyond, official PDF, etc.) before writing any file.

For each remaining ⚠️, propose a resolution and ask the user to confirm before writing.

Once the full triage table is presented, **wait for explicit user confirmation** before proceeding to Step 5. Do not write any file until the user approves the triage as a whole.

### Step 5 — Write YAML files

For each validated entity, write a YAML file to `data/compendium/<type>/<id>.yaml`.

Follow the style conventions:
- All keys in camelCase
- Multiline strings use block scalar style with strip chomping (`|-`)
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
