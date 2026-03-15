# PRD-004 — Notes

> **Status**: Draft | **Version**: 0.1 | **Last updated**: 2026-03-15

## Overview

The Notes module allows users to create, browse, search, and manage free-form personal notes. It is **game-system-agnostic** and follows the same list/detail/CRUD pattern as the reference data modules ([PRD-001 — Reference Data](PRD-001-REFERENCE-DATA.md)).
Notes are standalone entities at this stage; attaching them to a campaign is a later-phase cross-entity relationship (see [PRD-003 — Campaigns](PRD-003-CAMPAIGNS.md)).

The Notes module also serves as the foundation for the Generators module ([PRD-005](PRD-005-GENERATORS.md)): generated content (NPCs, names, plants, etc.) is saved as notes and managed through the same pattern.

## Goals

- Provide a simple, fast note-taking tool usable during any session, regardless of game system.
- Keep notes fully accessible offline.
- Establish the base list/search/CRUD pattern reused by the Generators module.

## User Stories

| As a… | I want to…                          | So that…                                         |
|-------|-------------------------------------|--------------------------------------------------|
| User  | Create a note with a title and body | I can write down anything relevant to my session |
| User  | Browse and search my notes          | I can quickly find a note I wrote previously     |
| User  | Edit a note                         | I can update or correct its content              |
| User  | Delete a note                       | I can remove notes I no longer need              |
| User  | Organise my notes into named lists  | I can group related notes together               |

**Later phase — Campaign integration (see PRD-003)**

| As a… | I want to…                    | So that…                                                      |
|-------|-------------------------------|---------------------------------------------------------------|
| User  | Attach a note to a campaign   | I can organise session notes within their campaign context    |
| User  | Detach a note from a campaign | The note returns to my personal library without being deleted |

**Future — Cross-entity references**

| As a… | I want to…                                                                  | So that…                                               |
|-------|-----------------------------------------------------------------------------|--------------------------------------------------------|
| User  | Reference another note, creature, character, or resource from within a note | I can build a connected knowledge base for my campaign |

## Functional Requirements

### Phase 1 — MVP (Offline)

- Create a note with a title and a free-text body.
- List all personal notes, sorted by last modified date (most recent first).
- Full-text search on title and body.
- Edit and delete notes.
- Support linking to an external URL as an alternative to a written body (e.g. a Google Doc, Notion page).

### Phase 2 — Local Lists

- Create, edit, and delete personal named lists of notes stored locally.

### Phase 3 — Campaign integration

- Attach an existing note to a campaign (see PRD-003).
- Notes remain in the user's personal library even when detached from a campaign.

### Future — Cross-entity references

> The exact interaction model (simple navigable link vs. inline display) is to be decided during design. This feature requires all referenced entity types to be available and synced first.

- Within a note body, reference any other entity: another note, a monster, a character sheet, a spell, an item, etc.
- Referenced entities are navigable from within the note.
- **Visibility consistency**: a reference to a secret or restricted entity must respect that entity's access rules — users who cannot access the entity should not be able to navigate to it or infer its content from the reference.

## Non-Functional Requirements

- Notes must load and save instantly on-device (no perceptible delay).
- The module must work fully offline.

## Out of Scope

- Rich text formatting (bold, lists, headings) — may be considered in a future iteration.
- Sharing notes directly between users outside of a campaign context.

## Open Questions

- Should notes support tags or categories for personal organisation?
- Should there be a maximum note length?
- Cross-entity references: simple navigable link or inline display? (to be decided during design)
