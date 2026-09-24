# PRD-007 — Character Import & Export

> **Status**: Draft | **Version**: 0.1 | **Last updated**: 2026-09-19

## Overview

Import & Export lets a user take a character sheet out of the app as a single JSON file and bring one back in. It is the only form of sharing that works without a backend: campaign sharing, accounts and access control are specified in [PRD-003 — Campaigns](prd-003-campaigns.md) and depend on Phase 4.

The scope is character sheets. A sheet is self-contained — every value it displays travels with it — which is what makes a file exchange meaningful. Collections are not: a `UserCollection` holds `itemIds` pointing into the compendium, so exporting one raises the question of how a receiving device resolves references it does not have. That is a separate decision and a separate PRD.

## Goals

- Let a player hand a character sheet to someone else offline, through the platform share sheet.
- Let a user move a sheet between their own devices without an account.
- Produce a file that re-imports faithfully, and whose format can carry other entity types later.
- Hold a received file and a bundled preset to the same validation rules, through the same mapper.

## User Stories

| As a… | I want to…                                             | So that…                                                    |
|-------|--------------------------------------------------------|-------------------------------------------------------------|
| User  | Export a character sheet as a file                     | I can hand it to another player                              |
| User  | Share that file through the apps I already use         | I do not have to manage files by hand                        |
| User  | Import a character sheet file                          | I can play a character someone sent me                       |
| User  | Land on the imported sheet right after importing       | I can check at a glance that it is the right character       |
| User  | Keep my own sheets untouched by an import              | An import can never destroy work I did not mean to replace   |
| User  | Be told which field made a file unusable               | I can fix it, or ask for a correct file                      |

## File Format

### Envelope

```json
{
  "formatVersion": 1,
  "entityType": "character",
  "appVersion": "1.4.0",
  "exportedAt": "2026-09-19T10:24:00Z",
  "sourceId": "b2f1c8e4-7d3a-4f10-9c2b-5e81f0a6d934",
  "character": {}
}
```

| Field           | Required | Read on import | Description                                                          |
|-----------------|----------|----------------|----------------------------------------------------------------------|
| `formatVersion` | yes      | yes            | Integer, bumped on every breaking change to the format                |
| `entityType`    | yes      | yes            | `character` — the extension point for collections and full backups    |
| `appVersion`    | yes      | no             | Version of the app that produced the file, for support and debugging  |
| `exportedAt`    | yes      | no             | ISO-8601 timestamp of the export                                      |
| `sourceId`      | yes      | no             | Identifier the sheet had on the exporting device                      |
| `character`     | yes      | yes            | The character payload                                                 |

`sourceId` is written but deliberately ignored on import. Recording it now means a later version can detect that a sheet is already present, or offer to replace it, without invalidating files exported today.

### Payload

The payload is the `ApiCharacter` shape already used by the bundled presets (`pc-presets.json`, `npc-presets.json`), extended with the current game state:

- `currentHitPoints` and `temporaryHitPoints` are added as optional fields. Presets, which do not declare them, keep loading with the existing default of `currentHitPoints = maxHitPoints`.
- `translations` is exported in full, every locale included. A sheet written in English stays readable for someone running the app in French, and an export followed by an import loses nothing.

### Versioning

- A file whose `formatVersion` is higher than the highest version the app supports is refused, with a message inviting the user to update the app. The app never interprets a format it does not fully know.
- A file whose `entityType` the app does not handle is refused the same way.

### File name

`<character-name>.character.json`, for instance `Aldwin.character.json`, served as `application/json`.

The standard media type is what keeps the file usable across mail, messaging and cloud apps, which requalify unknown types as binary. The `.character` segment carries the identity that a custom extension would have provided, and remains compatible with a declared document type should the app later want to be opened from a file manager.

## Functional Requirements

### Export

- [ ] The character sheet overflow menu offers an **Export** action.
- [ ] Exporting produces one file containing exactly one character.
- [ ] The file carries the full sheet, including current and temporary hit points, and every translation.
- [ ] The export opens the native share sheet on Android and iOS, and a save dialog on Desktop.
- [ ] The file name derives from the character name, with a fallback when the name yields no usable file name.

### Import

- [ ] The character list overflow menu offers an **Import** action, opening the platform file picker.
- [ ] A valid file creates a new character sheet with a newly generated identifier.
- [ ] Existing sheets are never modified or replaced by an import.
- [ ] On success, the app opens the imported sheet.
- [ ] Importing the same file twice creates two distinct sheets.

### Error Handling

Import distinguishes what cannot be guessed from what has an obvious correction.

- [ ] A missing required field, or a value the app does not recognise (unknown class, race, size, alignment, language), **rejects the file** and names the offending field. The sheet is not created, even partially.
- [ ] A numeric value outside its valid range is **clamped and reported**, following the rules in `CreatureRulesExt.kt` (armor class, hit points, speeds) and `CharacterRulesExt.kt` (level, ability scores, walk speed). The sheet is created with the corrected value.
- [ ] A file that is not valid JSON, or whose envelope is malformed, is rejected with a distinct message.
- [ ] Rejection messages name the field, not the internal error type.

A received file and a bundled preset go through the same mapper and the same policy. Two divergent behaviours over one format would be a standing source of bugs.

## Non-Functional Requirements

- Android, iOS and Desktop are delivered together; the feature is not considered done on a subset of targets.
- The exported file is human-readable JSON: a user can open it to see what they are about to share.
- No credentials, device identifiers or telemetry are written to the file.
- The format is documented in this PRD, and any breaking change bumps `formatVersion`.

## Out of Scope

- Exporting collections — needs a decision on how a receiving device resolves compendium references it does not have; may be addressed in a future PRD.
- Full local backup and restore in a single file; may be addressed in a future PRD.
- A printable or presentable sheet (PDF, image); may be addressed in a future PRD.
- Online sharing, accounts and access control, covered by [PRD-003 — Campaigns](prd-003-campaigns.md).
- Reading third-party formats (aidedd, D&D Beyond, Roll20). The envelope declares its type so a mapper can be added later without breaking files exported today.

## Open Questions

- Should the app declare a document type so a file can be opened from a file manager or a mail attachment? Rejected for this version: it requires per-platform type declarations, and is not reachable on Desktop without native packaging.
- Should an imported file be able to join the preset gallery instead of becoming a sheet? Deferred until saving a sheet as a preset exists — the gallery is bundled resources today.
- How is a clamped value **reported**? A one-off message as the sheet opens says it once and loses it; a marker carried by the sheet says it forever, including long after the value has been edited by hand. The ground is ready either way: `Character` is `@Serializable` and persisted as a JSON blob, so a field with a default needs no migration, and `TintedTag` is already the design system's flag component. The channel itself is #234's subject — today an adjustment only reaches a `println`, and coercions are not part of any `errors` list.
- Should a later version use `sourceId` to offer replacing an existing sheet rather than duplicating it?
- What is the exact file-name normalization rule for accents, spaces and characters each platform forbids?
